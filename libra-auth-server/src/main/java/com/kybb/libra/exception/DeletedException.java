package com.kybb.libra.exception;

import org.springframework.security.authentication.AccountStatusException;

public class DeletedException extends AccountStatusException {
    public DeletedException(String msg) {
        super(msg);
    }

    public DeletedException(String msg, Throwable t) {
        super(msg, t);
    }
}
