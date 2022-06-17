package com.work.triple.domain.user;

import com.work.triple.common.response.SingleResponse;
import com.work.triple.domain.user.dto.CreateUserDto;
import com.work.triple.domain.user.dto.LoginDto;
import com.work.triple.domain.user.dto.TokenDto;
import com.work.triple.domain.user.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.work.triple.common.response.SingleResponse.getSingleResponse;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@RequestMapping(value = "/api/user",
        produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    // 회원가입
    @Operation(summary = "사용자 회원가입")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/join")
    public SingleResponse<UserDto> join(@Valid @RequestBody CreateUserDto createUserDto) {
        return getSingleResponse(userService.saveUser(createUserDto));
    }

    @Operation(summary = "사용자 로그인")
    @PostMapping("/login")
    public ResponseEntity<SingleResponse<TokenDto>> login(@Valid @RequestBody LoginDto loginDto) {
        TokenDto tokenDto = authService.login(loginDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, "Bearer " + tokenDto.getAccessToken());
        return new ResponseEntity<>(getSingleResponse(tokenDto), httpHeaders, HttpStatus.OK);
    }
}
