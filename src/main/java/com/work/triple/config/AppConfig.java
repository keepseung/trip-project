package com.work.triple.config;

import com.work.triple.domain.city.*;
import com.work.triple.domain.trip.Trip;
import com.work.triple.domain.trip.TripRepository;
import com.work.triple.domain.user.User;
import com.work.triple.domain.user.UserRepository;
import com.work.triple.domain.user.UserService;
import com.work.triple.domain.user.dto.CreateUserDto;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Profile("!test")
    @Bean
    CommandLineRunner run(UserService userService, UserRepository userRepository, CityService cityService,
                          CityRepository cityRepository, TripRepository tripRepository, CitySearchHistoryRepository citySearchHistoryRepository){
        return args -> {
            // 유저 역할 저장
            userService.saveRole("ROLE_USER");
            userService.saveRole("ROLE_ADMIN");

            // 유저 저장
            userService.saveUser(getUser("user1@triple.com", "1234"));
            userService.saveUser(getUser("user2@triple.com", "12345"));
            userService.saveUser(getUser("user3@triple.com", "12346"));
            userService.saveUser(getUser("user4@triple.com", "12347"));

            // 도시 저장
            List<City> cityList = new ArrayList<>();
            for (int i =0; i < 100; i++){
                cityList.add(getCity("서울"+i));
            }
            cityRepository.saveAll(cityList);

            // 사용자의 여행 저장
            User user = userRepository.findByUsername("user1@triple.com").get();

            LocalDate now = LocalDate.now();

            // 도시 리스트 조회를 위한 테스트 데이터 등록
            // 1. 5개의 여행을 오늘이 여행 시작, 종료일 사이에 들어가게 저장한다.
            List<Trip> tripList = new ArrayList<>();
            for (int i =1; i <=3; i++){
                City city = cityRepository.findById(Long.valueOf(i)).get();
                tripList.add(getTrip(city, user, now.minusDays(i), now.plusDays(i)));
            }
            tripRepository.saveAll(tripList);

            // 2. 아직 시작하지 않은 여행 추가
            List<Trip> notStartTripList = new ArrayList<>();
            for (int i =4; i <=5; i++){
                City city = cityRepository.findById(Long.valueOf(i)).get();
                notStartTripList.add(getTrip(city, user, now.plusDays(i), now.plusDays(i+2)));
            }
            tripRepository.saveAll(notStartTripList);

            // 4. 도시 2개 단건 조회하기
            for (int i =6; i <=7; i++){
                citySearchHistoryRepository.save(CitySearchHistory.builder()
                        .city(cityRepository.findById(Long.valueOf(i)).get())
                        .user(user).build());
            }
        };
    }

    private Trip getTrip(City city, User user, LocalDate startDate, LocalDate endDate) {
        return Trip.builder()
                .city(city)
                .user(user)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    private CreateUserDto getUser(String username, String password) {
        return CreateUserDto.builder()
                .username(username)
                .password(password)
                .build();
    }

    private City getCity(String name){
        return City.builder()
                .name(name)
                .build();
    }
}
