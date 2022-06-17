package com.work.triple.domain.city;

import com.work.triple.common.response.ListResponse;
import com.work.triple.common.response.SingleResponse;
import com.work.triple.domain.city.dto.CityDto;
import com.work.triple.domain.city.dto.CreateCityDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.work.triple.common.response.SingleResponse.*;

@RequiredArgsConstructor
@RequestMapping(value = "/api/city", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class CityController {

    private final CityService cityService;

    @Operation(summary = "도시 생성")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public SingleResponse<CityDto> saveCity(@Valid @RequestBody CreateCityDto createCityDto){
        return getSingleResponse(cityService.saveCity(createCityDto));
    }

    @Operation(summary = "도시 단건 조회")
    @GetMapping(value = "/{id}")
    public SingleResponse<CityDto> getCity(@PathVariable(name = "id") Long id){
        return getSingleResponse(cityService.getCity(id));
    }

    @Operation(summary = "도시 리스트 조회")
    @GetMapping
    public ListResponse<CityDto> getCityList(){
        return ListResponse.getListResponse(cityService.getCityList());
    }



}
