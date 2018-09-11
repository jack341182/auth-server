package com.kybb.libra.auth.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * @Auther: vicykie
 * @Date: 2018/8/22 23:11
 * @Description:
 */
@Configuration
@EnableResourceServer
public class AuthResourceConfig extends ResourceServerConfigurerAdapter {
    @Autowired
    IntegrationAuthenticationSecurityConfig integrationAuthenticationSecurityConfig;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().antMatchers("/token/evict", "/sms/code",
                "/actuator/**", "/encryption/code").permitAll()
                .and().authorizeRequests().anyRequest().authenticated()
                .and().csrf().disable()
                .apply(integrationAuthenticationSecurityConfig);
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("auth-server");
    }
}
