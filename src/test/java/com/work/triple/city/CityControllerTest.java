package com.work.triple.city;

import com.work.triple.common.BaseControllerTest;
import com.work.triple.domain.city.City;
import com.work.triple.domain.city.CityRepository;
import com.work.triple.domain.city.CitySearchHistoryRepository;
import com.work.triple.domain.city.dto.CreateCityDto;
import com.work.triple.domain.trip.TripRepository;
import com.work.triple.domain.user.RoleRepository;
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CityControllerTest extends BaseControllerTest {
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
    public void setUp() {
        this.citySearchHistoryRepository.deleteAll();
        this.tripRepository.deleteAll();
        this.cityRepository.deleteAll();
        this.userRepository.deleteAll();
        this.roleRepository.deleteAll();
    }

    @Test
    @DisplayName("?????? ?????? 201")
    public void save_city_return_201() throws Exception {
        String name = "??????";
        CreateCityDto createCityDto = CreateCityDto.builder()
                .name(name)
                .build();
        mockMvc.perform(post("/api/city")
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken(true))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCityDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)) // ?????? ??????
                .andExpect(jsonPath("$.success", equalTo(true))) // ?????? ??????
                .andExpect(jsonPath("$.data.name", equalTo(name))); // ?????? ????????? ??????
    }

    @Test
    @DisplayName("?????? ?????? 400")
    public void save_city_return_400() throws Exception {
        CreateCityDto createCityDto = CreateCityDto.builder()
                .name(null)
                .build();
        mockMvc.perform(post("/api/city")
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken(true))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCityDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)) // ?????? ??????
                .andExpect(jsonPath("$.success", equalTo(false))); // ?????? ??????
    }

    @Test
    @DisplayName("?????? ?????? ?????? 200")
    public void get_city_return_200() throws Exception {
        String name = "??????";
        City city = cityRepository.save(City.builder()
                .name(name)
                .build());
        mockMvc.perform(get("/api/city/" + city.getId())
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken(true)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)) // ?????? ??????
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.data.name", equalTo(name))); // ?????? ????????? ??????
    }

    @Test
    @DisplayName("?????? ?????? ?????? 404")
    public void get_city_return_404() throws Exception {
        mockMvc.perform(get("/api/city/159")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken(true)))
            .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)) // ?????? ??????
                .andExpect(jsonPath("$.success", equalTo(false))); // ?????? ??????
    }

    @Test
    @DisplayName("?????? ????????? ??????")
    public void get_city_return_list() throws Exception {
        mockMvc.perform(get("/api/city")
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken(true)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)) // ?????? ??????
                .andExpect(jsonPath("$.success", equalTo(true))); // ?????? ??????
    }

    private City saveCity(int index) {
        return cityRepository.save(City.builder()
                .name("??????" + index)
                .build());
    }

    @NotNull
    private String getBearerToken(boolean needToCreate) throws Exception {
        return "Bearer " + getAccessToken(needToCreate);
    }

    private String getAccessToken(boolean needToCreateAccount) throws Exception {
        String username = "test@example.com";
        String password = "1234";
        if (needToCreateAccount) {
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

        System.out.println("responseBody " + responseBody);
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
