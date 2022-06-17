package com.work.triple.domain.city.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.work.triple.domain.trip.dto.TripDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityDto {
    private Long id;
    private String name;
    private TripDto trip;
}
