package com.work.triple.trip;

import com.work.triple.common.BaseControllerTest;
import com.work.triple.domain.city.City;
import com.work.triple.domain.city.CityRepository;
import com.work.triple.domain.city.CitySearchHistoryRepository;
import com.work.triple.domain.trip.Trip;
import com.work.triple.domain.trip.TripRepository;
import com.work.triple.domain.trip.dto.CreateTripDto;
import com.work.triple.domain.user.RoleRepository;
import com.work.triple.domain.user.User;
import com.work.triple.domain.user.UserRepository;
import com.work.triple.domain.user.UserService;
import com.work.triple.domain.user.dto.CreateUserDto;
import com.work.triple.domain.user.dto.LoginDto;
import com.work.triple.domain.user.dto.UserDto;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TripControllerTest extends BaseControllerTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    TripRepository tripRepository;
    @Autowired
    CityRepository cityRepository;
    @Autowired
    CitySearchHistoryRepository citySearchHistoryRepository;

    @Autowired
    UserService userService;
    @BeforeEach
    public void setUp(){
        this.citySearchHistoryRepository.deleteAll();;
        this.tripRepository.deleteAll();
        this.cityRepository.deleteAll();
        this.userRepository.deleteAll();
        this.roleRepository.deleteAll();
    }

    // 여행 등록
    @Test
    @DisplayName("여행 등록 201")
    public void create_trip_return_201() throws Exception {
        // 도시 생성
        City city = cityRepository.save(City.builder()
                .name("서울")
                .build());

        // 여행 생성
        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalDate startDate = LocalDate.now().minusDays(1);
        CreateTripDto createTripDto = CreateTripDto.builder()
                .cityId(city.getId())
                .endDate(endDate)
                .startDate(startDate)
                .build();

        int cityId = Integer.valueOf(String.valueOf(city.getId()));
        mockMvc.perform(post("/api/trip")
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken(true))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTripDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)) // 헤더 검증
                .andExpect(jsonPath("$.success", equalTo(true))) // 성공 검증
                .andExpect(jsonPath("$.data.id", equalTo(cityId))) // 도시 데이터 검증
        .andExpect(jsonPath("$.data.trip.startDate", equalTo(startDate.toString()))) // 여행 데이터 검증
        .andExpect(jsonPath("$.data.trip.endDate", equalTo(endDate.toString())));// 여행 데이터 검증
    }

    // 여행 등록 400
    @Test
    @DisplayName("여행 등록 400")
    public void create_trip_return_400() throws Exception {
        // 도시 생성
        City city = cityRepository.save(City.builder()
                .name("서울")
                .build());

        // 여행 생성
        CreateTripDto createTripDto = CreateTripDto.builder()
                .cityId(0L)
                .build();

        mockMvc.perform(post("/api/trip")
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken(true))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTripDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)) // 헤더 검증
                .andExpect(jsonPath("$.success", equalTo(false))); // 실패 검증
    }

    @Test
    @DisplayName("여행 단건 조회 200")
    public void get_trip_return_200() throws Exception {
        //Given
        String username = "test@example.com";
        String password = "1234";
        createUser(username, password);

        City city = cityRepository.save(City.builder()
                .name("서울")
                .build());
        Trip trip = generateTrip(city);

        mockMvc.perform(get("/api/trip/"+trip.getId())
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken(false)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)) // 헤더 검증
                .andExpect(jsonPath("$.success", equalTo(true))) // 성공 검증
                .andExpect(jsonPath("$.data.id").isNotEmpty()) // 도시 데이터 검증
                .andExpect(jsonPath("$.data.trip.startDate").isNotEmpty()) // 여행 데이터 검증
                .andExpect(jsonPath("$.data.trip.endDate").isNotEmpty());// 여행 데이터 검증
    }

    @Test
    @DisplayName("여행 조회 404")
    public void get_trip_return_404() throws Exception {
        //Given
        String username = "test@example.com";
        String password = "1234";
        createUser(username, password);
        IntStream.range(0, 30).forEach(this::generateTrip);

        mockMvc.perform(get("/api/trip/35")
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken(false)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)) // 헤더 검증
                .andExpect(jsonPath("$.success", equalTo(false))); // 실패 검증
    }


    private Trip generateTrip(int index) {
        Trip trip = buildTrip(index);
        return this.tripRepository.save(trip);
    }

    private Trip generateTrip(City city) {
        Trip trip = buildTrip(city);
        return this.tripRepository.save(trip);
    }

    private Trip buildTrip(int index) {
        City city = saveCity(index);
        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalDate startDate = LocalDate.now().minusDays(1);

        String username = "test@example.com";
        User user = userRepository.findByUsername(username).get();

        return tripRepository.save(Trip.builder()
                .startDate(startDate)
                .endDate(endDate)
                .user(user)
                .city(city)
                .build());
    }

    private Trip buildTrip(City city) {
        LocalDate endDate = LocalDate.now().plusDays(10);
        LocalDate startDate = LocalDate.now().minusDays(1);

        String username = "test@example.com";
        User user = userRepository.findByUsername(username).get();

        return tripRepository.save(Trip.builder()
                .startDate(startDate)
                .endDate(endDate)
                .user(user)
                .city(city)
                .build());
    }

    private City saveCity(int index) {
        return cityRepository.save(City.builder()
                .name("서울" + index)
                .build());
    }

    @NotNull
    private String getBearerToken(boolean needToCreate) throws Exception {
        return "Bearer "+getAccessToken(needToCreate);
    }

    private String getAccessToken(boolean needToCreateAccount) throws Exception {
        String username = "test@example.com";
        String password = "1234";
        if (needToCreateAccount){
            createUser(username, password);
        }

        LoginDto loginDto = LoginDto.builder()
                .username(username)
                .password(password)
                .build();
        ResultActions perform = this.mockMvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)));
        String responseBody = perform.andReturn().getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject data = jsonObject.getJSONObject("data");

        System.out.println("responseBody "+responseBody);
        return data.getString("accessToken");
    }

    private UserDto createUser(String username, String password) {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .username(username)
                .password(password)
                .build();
        return this.userService.saveUser(createUserDto);
    }
    

}

