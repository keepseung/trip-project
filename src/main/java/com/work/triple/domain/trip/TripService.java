package com.work.triple.domain.trip;

import com.work.triple.domain.city.dto.CityDto;
import com.work.triple.domain.trip.dto.CreateTripDto;
import com.work.triple.domain.trip.dto.TripDto;

public interface TripService {
    // 여행 등록
    CityDto saveTrip(CreateTripDto createTripDto);
    // 여행 조회
    CityDto getTrip(Long id);
}
