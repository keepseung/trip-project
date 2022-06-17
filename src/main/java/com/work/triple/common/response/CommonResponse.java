package com.work.triple.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResponse {

    @Schema(name = "응답 성공여부 : true/false")
    private boolean success;
}

