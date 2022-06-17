package com.work.triple.domain.trip;

import com.work.triple.common.response.SingleResponse;
import com.work.triple.domain.city.dto.CityDto;
import com.work.triple.domain.trip.dto.CreateTripDto;
import com.work.triple.domain.trip.dto.TripDto;
import com.work.triple.domain.user.dto.CreateUserDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.service.MediaTypes;

import javax.validation.Valid;

import static com.work.triple.common.response.SingleResponse.getSingleResponse;

@RequiredArgsConstructor
@RequestMapping(value = "/api/trip",
        produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class TripController {

    private final TripService tripService;

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "여행 등록")
    @PostMapping
    public SingleResponse<CityDto> saveTripForCity(@Valid @RequestBody CreateTripDto createTripDto) {
        return getSingleResponse(tripService.saveTrip(createTripDto));
    }

    @Operation(summary = "여행 조회")
    @GetMapping("/{id}")
    public SingleResponse<CityDto> saveTripForCity(@PathVariable("id") Long id) {
        return getSingleResponse(tripService.getTrip(id));
    }
}
