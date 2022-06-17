package com.work.triple.domain.user.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter @NoArgsConstructor
@Builder @AllArgsConstructor
public class RoleDto {
    private Long id;
    private String name;
}
