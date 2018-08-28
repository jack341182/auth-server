package com.kybb.common.cloud.exception;

/**
 * @Auther: vicykie
 * @Date: 2018/8/23 00:15
 * @Description:
 */
public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException() {
        super();
    }
}
