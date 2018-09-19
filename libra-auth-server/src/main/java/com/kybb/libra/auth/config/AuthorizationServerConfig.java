package com.kybb.libra.auth.config;


import com.kybb.libra.auth.IntegrationTokenEnhancer;
import com.kybb.libra.auth.IntegrationTokenService;
import com.kybb.libra.auth.handler.IntegrationExceptionTranslator;
import com.kybb.libra.service.IntegrationUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: vicykie
 * @Date: 2018/8/22 21:18
 * @Description:
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Bean
    @Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("bCryptPasswordEncoder")
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(4);
    }


    @Autowired
    private IntegrationUserDetailService userDetailsService;

    @Autowired
    private IntegrationExceptionTranslator integrationExceptionTranslator;

    @Bean
    @Primary
    public AuthorizationServerTokenServices tokenServices(){
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> enhancers = new ArrayList<TokenEnhancer>();
//        enhancers.add(jwtAccessTokenConverter());
        enhancers.add(enhancer);
        enhancerChain.setTokenEnhancers(enhancers);
        IntegrationTokenService integrationTokenService = new IntegrationTokenService();
        integrationTokenService.setTokenStore(tokenStore());
        integrationTokenService.setAuthenticationManager(authenticationManager);
        integrationTokenService.setTokenEnhancer(enhancerChain);
        integrationTokenService.setAlwaysNewAccessToken(true);
        integrationTokenService.setSupportRefreshToken(true);
        return integrationTokenService;
    }
    @Autowired
    IntegrationTokenEnhancer enhancer;
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //拿到增强器链
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> enhancers = new ArrayList<TokenEnhancer>();
//        enhancers.add(jwtAccessTokenConverter());
        enhancers.add(enhancer);
        enhancerChain.setTokenEnhancers(enhancers);

        endpoints.tokenStore(tokenStore()).tokenServices(tokenServices())
                .userDetailsService(userDetailsService).tokenEnhancer(enhancerChain)
                .exceptionTranslator(integrationExceptionTranslator).authenticationManager(authenticationManager);

//        endpoints.userDetailsService(userDetailsService);
//        endpoints.tokenStore(tokenStore())
//                .authenticationManager(authenticationManager)
//                .userDetailsService(userDetailsService);
//        endpoints.tokenEnhancer(enhancerChain);
//        endpoints.exceptionTranslator(integrationExceptionTranslator);
    }


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()"); //检查token需要鉴权
    }
    @Bean
    public ClientDetailsService clientDetailsService(){
        return new JdbcClientDetailsService(dataSource);
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService());
//        clients.jdbc(dataSource);
    }
}
