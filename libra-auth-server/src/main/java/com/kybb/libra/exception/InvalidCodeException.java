package com.kybb.libra.exception;

import org.springframework.security.authentication.AccountStatusException;

public class InvalidCodeException extends AccountStatusException {
    public InvalidCodeException(String msg) {
        super(msg);
    }

    public InvalidCodeException(String msg, Throwable t) {
        super(msg, t);
    }
}
