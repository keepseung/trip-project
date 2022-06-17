package com.work.triple.domain.user;

import com.work.triple.domain.user.dto.*;

public interface UserService {
    // 유저 저장
    UserDto saveUser(CreateUserDto user);
    // 유저 조회
    User getUser(String username);
    // 역할 저장
    RoleDto saveRole(String roleName);
}
