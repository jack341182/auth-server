package com.kybb.libra.config.auth;

import com.kybb.libra.config.IntegrationAuthenticationSecurityConfig;
import com.kybb.libra.config.SmsCodeFilter;
import com.kybb.libra.config.WechatLoginFilter;
import com.kybb.libra.service.SmsCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Auther: vicykie
 * @Date: 2018/8/22 23:11
 * @Description:
 */
@Configuration
@EnableResourceServer
public class AuthResourceConfig extends ResourceServerConfigurerAdapter {
    @Autowired
    IntegrationAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    //自定义的登录成功后的处理器
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    //自定义的认证失败后的处理器
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private SmsCodeService smsCodeService;
    @Override
    public void configure(HttpSecurity http) throws Exception {
        SmsCodeFilter filter = new SmsCodeFilter(smsCodeService, authenticationFailureHandler);
        WechatLoginFilter wechatLoginFilter = new WechatLoginFilter(authenticationFailureHandler);
        http
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(wechatLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .successHandler(authenticationSuccessHandler)//自定义的认证后处理器
                .failureHandler(authenticationFailureHandler) //登录失败后的处理
                .and()
                .authorizeRequests()
                .antMatchers("/token/evict","/sms/code","/sms/test","/oauth/check_token","/actuator/**","/encryption/code").permitAll()
                .and().csrf().disable()
                .apply(smsCodeAuthenticationSecurityConfig);
    }



}
