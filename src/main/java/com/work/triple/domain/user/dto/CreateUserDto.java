package com.work.triple.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor @AllArgsConstructor @Builder
public class CreateUserDto {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
