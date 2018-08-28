package com.kybb.libra.config;

import com.kybb.libra.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
public class IntegrationAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;

	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;

	@Autowired
	private CustomUserDetailService userDetailsService;
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		//1，配置短信验证码过滤器
		SmsCodeAuthenticationFilter smsCodeAuthenticationFilter = new SmsCodeAuthenticationFilter();
		smsCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
		//设置认证失败成功处理器
		smsCodeAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
		smsCodeAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);

		//配置provider
		IntegrationAuthenticationProvider smsCodeAuthenticationProvider = new IntegrationAuthenticationProvider();
		smsCodeAuthenticationProvider.setUserDetailsService(userDetailsService);

		http.authenticationProvider(smsCodeAuthenticationProvider)
			.addFilterAfter(smsCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


		//微信
		WechatAuthenticationFilter wechatAuthenticationFilter = new WechatAuthenticationFilter();
		wechatAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
		//设置认证失败成功处理器
		wechatAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
		wechatAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);


		//配置provider
		WechatAuthenticationProvider wechatAuthenticationProvider = new WechatAuthenticationProvider();
		wechatAuthenticationProvider.setUserDetailsService(userDetailsService);
		http.authenticationProvider(wechatAuthenticationProvider)
				.addFilterAfter(wechatAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

	}

	
}
