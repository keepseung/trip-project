package com.work.triple.common.exception;

import lombok.Getter;

public class UnAuthorizedException extends RuntimeException {

    @Getter
    private String errKey;

    public UnAuthorizedException(String errKey) {
        super(errKey);
        this.errKey = errKey;
    }
}
