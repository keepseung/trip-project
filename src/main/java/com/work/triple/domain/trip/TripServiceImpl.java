package com.work.triple.domain.trip;

import com.work.triple.common.exception.BadRequestException;
import com.work.triple.common.exception.NotFoundException;
import com.work.triple.domain.city.City;
import com.work.triple.domain.city.CityRepository;
import com.work.triple.domain.city.dto.CityDto;
import com.work.triple.domain.trip.dto.CreateTripDto;
import com.work.triple.domain.trip.dto.TripDto;
import com.work.triple.domain.user.User;
import com.work.triple.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class TripServiceImpl implements TripService{
    private final TripRepository tripRepository;
    private final UserService userService;
    private final CityRepository cityRepository;
    private final ModelMapper modelMapper;

    @Override
    public CityDto saveTrip(CreateTripDto createTripDto) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(username);
        City city = getCityElseThrow(createTripDto.getCityId());

        // 이미 같은 사용자가 등록한 도시 여행이 있는 경우
        Optional<Trip> optionalTrip = tripRepository.findByUserAndCity(user, city);
        if (optionalTrip.isPresent()){
            throw new BadRequestException("alreadyCityTrip");
        }

        // 시작 날짜가 종료 날짜보다 큰 경우 예외 발생
        if (createTripDto.getStartDate().isAfter(createTripDto.getEndDate())){
            throw new BadRequestException("tripStartOrEndDateWrong");
        }
//        if ((createTripDto.getStartDate().isBefore(now) || createTripDto.getStartDate().isEqual(now))
//         && (createTripDto.getEndDate().isAfter(now) || createTripDto.getEndDate().isEqual(now))){

            Trip trip = tripRepository.save(Trip.builder()
                    .startDate(createTripDto.getStartDate())
                    .endDate(createTripDto.getEndDate())
                    .user(user)
                    .city(city)
                    .build());
            TripDto tripDto = modelMapper.map(trip, TripDto.class);
            CityDto cityDto = CityDto.builder()
                    .id(trip.getCity().getId())
                    .name(trip.getCity().getName())
                    .trip(tripDto)
                    .build();

            return cityDto;
//        }else{
//            throw new IllegalArgumentException("tripStartOrEndDateWrong");
//        }
    }

    @Transactional(readOnly = true)
    @Override
    public CityDto getTrip(Long id) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUser(username);

        Trip trip = tripRepository.findByUserAndId(user, id).orElseThrow(() -> {
            throw new NotFoundException("tripNotFound");
        });

        TripDto tripDto = modelMapper.map(trip, TripDto.class);
        CityDto cityDto = CityDto.builder()
                .id(trip.getCity().getId())
                .name(trip.getCity().getName())
                .trip(tripDto)
                .build();
        return cityDto;
    }

    private City getCityElseThrow(Long id) {
        return cityRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("cityNotFound");
        });
    }
}
