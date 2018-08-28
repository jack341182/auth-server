package com.kybb.common.cloud.integration;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: vicykie
 * @Date: 2018/8/14 19:23
 * @Description:
 */
@Data
public class SmsCodeRequest implements Serializable {
    private String mobile;
    private String deviceId;
    private String code;
}
