package com.kybb.libra.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConfigurationProperties(prefix = "spring.auth.config")
@Getter
@Setter
@Configuration
public class AuthorizationProperties {
    private List<String> phoneForTest;
    private String defaultSmsCode;

}
