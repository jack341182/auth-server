package com.kybb.libra.bean;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class SmsCodeStatus implements Serializable {

    private boolean success;

    private String message;
}
