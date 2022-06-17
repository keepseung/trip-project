package com.work.triple.common.exception;

import lombok.Getter;

public class BadRequestException extends RuntimeException {

    @Getter
    private String errKey;

    public BadRequestException(String errKey) {
        super(errKey);
        this.errKey = errKey;
    }
}
