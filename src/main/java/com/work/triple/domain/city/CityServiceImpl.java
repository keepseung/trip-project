package com.work.triple.domain.city;

import com.work.triple.common.exception.BadRequestException;
import com.work.triple.common.exception.NotFoundException;
import com.work.triple.domain.city.dto.CityDto;
import com.work.triple.domain.city.dto.CreateCityDto;
import com.work.triple.domain.trip.Trip;
import com.work.triple.domain.trip.TripRepository;
import com.work.triple.domain.trip.dto.TripDto;
import com.work.triple.domain.user.User;
import com.work.triple.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CityServiceImpl implements CityService{
    private final CityRepository cityRepository;
    private final CitySearchHistoryRepository citySearchHistoryRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final TripRepository tripRepository;

    public static final int CITY_LIMIT_SIZE = 10;
    @Override
    public CityDto saveCity(CreateCityDto city) {
        Optional<City> optionalCity = cityRepository.findByName(city.getName());
        if (optionalCity.isPresent()){
            throw new BadRequestException("duplicateCity");
        }
        City savedCity = cityRepository.save(City.builder()
                .name(city.getName())
                .build());
        return modelMapper.map(savedCity, CityDto.class);
    }

    @Override
    public CityDto getCity(Long id) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUser(username);
        City city = getCityElseThrow(id);

        // 사용자가 도시 검색하는 기록을 저장한다.
        // 나중에 도시 리스트 조회할 때 우선순위 데이터로 사용하기 위함
        citySearchHistoryRepository.save(CitySearchHistory.builder()
                        .city(city)
                        .user(user).build());
        return modelMapper.map(city, CityDto.class);
    }

    private City getCityElseThrow(Long id) {
        return cityRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("cityNotFound");
        });
    }

    @Transactional(readOnly = true)
    @Override
    public List<CityDto> getCityList() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUser(username);
        PageRequest pageRequest = PageRequest.of(0, CITY_LIMIT_SIZE);
        LocalDate now = LocalDate.now();
        List<Trip> validTrips = tripRepository.findValidTrip(user, now, pageRequest);

        // 1. 여행중인 도시가 10개 이상인 경우 여행중인 도시를 여행 시작일이 빠른 순서대로
        if (validTrips.size() >= CITY_LIMIT_SIZE){
            return mapTripToCityDto(validTrips, CITY_LIMIT_SIZE).subList(0,10);
        }

        // 10개 이하인 여행중인 도시 결과 응답값에 넣어두기
        List<CityDto> cityDtos = new ArrayList<>(); // 도시 리스트 응답값
        int validCitySize = validTrips.size();
        cityDtos.addAll(mapTripToCityDto(validTrips, validCitySize));

        // 2. 아직 시작하지 않은 여행을 여행 시작일에 가까운 순서로 응답값에 넣어줌
        List<Trip> notStartTrips = tripRepository.findNotStartTrip(user, now, pageRequest);
        cityDtos.addAll(mapTripToCityDto(notStartTrips, CITY_LIMIT_SIZE));

        if (cityDtos.size()>=CITY_LIMIT_SIZE){
            return cityDtos.subList(0,10);
        }

        // 3. 최근 1일 이내 등록된 도시들을 등록된 순서로 넣기
        PageRequest pageRequest2 = PageRequest.of(0, 2*CITY_LIMIT_SIZE);
        List<City> innerOneDayCities = cityRepository.findCity(LocalDateTime.now().minusDays(1), pageRequest2);
        List<CityDto> innerOneDayCityDtos = getCityDtos(innerOneDayCities);
        // 이미 선택된 도시는 제거
        innerOneDayCityDtos.removeAll(cityDtos);
        cityDtos.addAll(innerOneDayCityDtos);
        if (cityDtos.size()>=CITY_LIMIT_SIZE){
            return cityDtos.subList(0,10);
        }

        // 4. 최근 1주일 이내에 단건조회 된 도시들을 최근에 조회한 순서대로
        List<CitySearchHistory> searchHistory = citySearchHistoryRepository.findHistory(LocalDateTime.now().minusDays(7), pageRequest2);
        List<City> searchCities = searchHistory.stream().map(CitySearchHistory::getCity).collect(Collectors.toList());
        List<CityDto> searchCitiyDtos = getCityDtos(searchCities);
        searchCitiyDtos.removeAll(cityDtos);
        cityDtos.addAll(searchCitiyDtos);
        if (cityDtos.size()>=CITY_LIMIT_SIZE){
            return cityDtos.subList(0,10);
        }

        // 5. 위 조건에 해당되지 않는 도시들은 랜덤하게 정렬
        long count = cityRepository.count();
        double random = Math.random();
        List<Long> randomList= new ArrayList<>();
        for (int i =0; i <2*CITY_LIMIT_SIZE; i++){
            long randomValue = (long)(random * count-1) + 1;
            randomList.add(randomValue);
        }
        List<City> cityRandom = cityRepository.findCityRandom(randomList);
        List<CityDto> cityRandomDtos = getCityDtos(cityRandom);
        cityRandomDtos.removeAll(cityDtos);
        cityDtos.addAll(cityRandomDtos);

        if (cityDtos.size()>=CITY_LIMIT_SIZE){
            return cityDtos.subList(0,10);
        }
        return cityDtos;
    }

    private List<CityDto> getCityDtos(List<City> innerOneDayCities) {
        return innerOneDayCities.stream().map(it -> CityDto.builder()
                        .id(it.getId())
                        .name(it.getName())
                        .build())
                .collect(Collectors.toList());
    }

    private List<CityDto> mapTripToCityDto(List<Trip> trips, int limit) {
        return trips.stream().map(it->CityDto.builder()
                .id(it.getCity().getId())
                .name(it.getCity().getName())
                .trip(TripDto.builder().
                        id(it.getId())
                        .id(it.getId())
                        .startDate(it.getStartDate())
                        .endDate(it.getEndDate())
                        .build())
                .build())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
