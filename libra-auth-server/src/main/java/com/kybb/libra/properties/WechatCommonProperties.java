package com.kybb.libra.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
public class WechatCommonProperties {
    private String appId;
    private String appSecret;
}
