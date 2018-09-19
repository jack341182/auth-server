package com.kybb.libra.auth.config;

import com.kybb.libra.auth.filter.SmsCodeAuthenticationFilter;
import com.kybb.libra.auth.filter.WechatAuthenticationFilter;
import com.kybb.libra.auth.provider.SmsCodeLoginAuthenticationProvider;
import com.kybb.libra.auth.provider.WechatLoginAuthenticationProvider;
import com.kybb.libra.properties.WechatProperties;
import com.kybb.libra.service.IntegrationUserDetailService;
import com.kybb.libra.service.SmsCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * 短信验证码配置
 * ClassName: SmsCodeAuthenticationSecurityConfig
 */
@Component
@EnableConfigurationProperties(value = {WechatProperties.class})
public class IntegrationAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private IntegrationUserDetailService userDetailsService;

    @Autowired
    private WechatProperties wechatProperties;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //1，配置短信验证码过滤器
        SmsCodeAuthenticationFilter smsCodeAuthenticationFilter = new SmsCodeAuthenticationFilter();
        smsCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        //设置认证失败成功处理器
        smsCodeAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        smsCodeAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);


        http.authenticationProvider(smsCodeAuthenticationProvider())
                .addFilterAfter(smsCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        //微信
        WechatAuthenticationFilter wechatAuthenticationFilter = new WechatAuthenticationFilter(wechatProperties);
        wechatAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        //设置认证失败成功处理器
        wechatAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        wechatAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);

        http.authenticationProvider(wechatAuthenticationProvider())
                .addFilterAfter(wechatAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Autowired
    SmsCodeService smsCodeService;

    @Bean
    public AuthenticationProvider smsCodeAuthenticationProvider() {
        //配置provider
        SmsCodeLoginAuthenticationProvider smsCodeAuthenticationProvider = new SmsCodeLoginAuthenticationProvider();
        smsCodeAuthenticationProvider.setUserDetailsService(userDetailsService);
        smsCodeAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        smsCodeAuthenticationProvider.setSmsCodeService(smsCodeService);
        return smsCodeAuthenticationProvider;
    }

    @Bean
    public AuthenticationProvider wechatAuthenticationProvider() {
        //配置provider
        WechatLoginAuthenticationProvider wechatAuthenticationProvider = new WechatLoginAuthenticationProvider();
        wechatAuthenticationProvider.setUserDetailsService(userDetailsService);
        wechatAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        return wechatAuthenticationProvider;
    }


}
