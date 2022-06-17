package com.work.triple.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;

@Component
@Getter
@Setter
public class JwtProperties {
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.token-validation-in-seconds}")
    private long tokenValidationInSeconds;

    @PostConstruct
    public void init(){
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }
}
