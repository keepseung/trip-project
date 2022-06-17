package com.work.triple.domain.user;

import com.work.triple.common.exception.UnAuthorizedException;
import com.work.triple.config.jwt.JwtTokenProvider;
import com.work.triple.domain.user.dto.LoginDto;
import com.work.triple.domain.user.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(readOnly = true)
    public TokenDto login(LoginDto loginDto) {

        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtTokenProvider.createAccessToken(authentication);
            return TokenDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(jwtTokenProvider.createRefreshToken(authentication))
                    .build();
        }catch (AuthenticationException ex){
            throw new UnAuthorizedException("loginFail");
        }
    }
}
