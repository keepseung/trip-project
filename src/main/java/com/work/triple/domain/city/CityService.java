package com.work.triple.domain.city;

import com.work.triple.domain.city.dto.CityDto;
import com.work.triple.domain.city.dto.CreateCityDto;

import java.util.List;

public interface CityService {
    // 도시 생성
    CityDto saveCity(CreateCityDto city);

    // 도시 단건 조회
    CityDto getCity(Long id);

    // 도시 리스트 조회
    List<CityDto> getCityList();

}
