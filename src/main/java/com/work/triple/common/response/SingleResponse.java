package com.work.triple.common.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @description 클라이언트에게 반환되는 단일 데이터 클래스
 */
@Getter
@Setter
@Builder @NoArgsConstructor
public class SingleResponse<T> extends CommonResponse{

    private T data; // 결과 값

    @Builder
    public SingleResponse(T data) {
        setSuccess(true);
        this.data = data;
    }

    public static <T> SingleResponse<T> getSingleResponse(T data) {
        return SingleResponse.<T>builder()
                .data(data).build();
    }
}