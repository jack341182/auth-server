package com.kybb.libra.auth;

import com.kybb.common.cloud.integration.IntegrationUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * jwt增强器
 *
 * @Description: 往jwt的 token增加自己的信息
 * spring默认生成token的方法在DefaultTokenService里，是private，生成的是uuid，没办法重写，只能是增强器把uuid转换成jwt，添加一些信息
 */
@Component
public class JwtTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        //往jwt添加的自定义信息
        IntegrationUser principal = (IntegrationUser) authentication.getUserAuthentication().getPrincipal();// 与登录时候放进去的UserDetail实现类一直查看link{SecurityConfiguration}

        Map<String, Object> info = new HashMap<>();
        info.put("userId", principal.getId());

//        info.put("wxOpenId", principal.getWxOpenId());
//        info.put("telephone", principal.getTelephone());
        info.put("userType", principal.getUserType());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        return accessToken;
    }

}
