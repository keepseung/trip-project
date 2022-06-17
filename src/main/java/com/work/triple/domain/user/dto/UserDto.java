package com.work.triple.domain.user.dto;

import com.work.triple.domain.user.Role;
import com.work.triple.domain.user.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
//    private List<Trip> trips = new ArrayList<>();
    private List<String> roles = new ArrayList<>();

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
    }
}
