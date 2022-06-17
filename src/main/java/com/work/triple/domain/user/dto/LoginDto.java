package com.work.triple.domain.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Builder
@NoArgsConstructor @AllArgsConstructor
public class LoginDto {
    @ApiModelProperty(value = "유저 아이디", dataType = "string", required = true, example = "user1@triple.com")
    @NotBlank
    private String username;
    @ApiModelProperty(value = "패스워드", dataType = "string", required = true, example = "1234")
    @NotNull
    private String password;
}
