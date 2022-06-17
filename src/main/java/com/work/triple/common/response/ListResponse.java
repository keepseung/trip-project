package com.work.triple.common.response;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @description 클라이언트에게 반환되는 리스트 데이터 클래스
 */
@Getter
@Setter @NoArgsConstructor
public class ListResponse<T> extends CommonResponse{

    @Schema(name = "리스트 데이터")
    private List<T> data; // 결과 값

    @Builder
    public ListResponse(List<T> data) {
        setSuccess(true);
        this.data = data;
    }

    public static <T> ListResponse<T> getListResponse(List<T> list) {
        return ListResponse.<T>builder()
                .data(list).build();
    }

}