package com.work.triple.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @description 클라이언트에게 반환되는 실패 에러 코드, 메세지 클래스
 */
@Getter @Setter
@NoArgsConstructor
public class ErrorResponse extends CommonResponse {
    @Schema(name = "에러 코드")
    private String errorCode;
    @Schema(name = "에러 메세지")
    private String message;

    @Builder
    public ErrorResponse(String errorCode, String message) {
        setSuccess(false);
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorResponse(String errorCode) {
        this.errorCode = errorCode;
    }
}