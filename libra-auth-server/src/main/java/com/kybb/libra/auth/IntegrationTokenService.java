package com.kybb.libra.auth;

import com.kybb.libra.auth.config.AuthorizationServerConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.*;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

//@Component
@Slf4j
@Getter
@Setter
public class IntegrationTokenService extends DefaultTokenServices {
    private TokenStore tokenStore;

    private TokenEnhancer accessTokenEnhancer;

    private AuthenticationManager authenticationManager;

    private boolean alwaysNewAccessToken;

    @Override
    public void setTokenEnhancer(TokenEnhancer accessTokenEnhancer) {
        this.accessTokenEnhancer = accessTokenEnhancer;
        super.setTokenEnhancer(accessTokenEnhancer);
    }

    @Override
    public void setTokenStore(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
        super.setTokenStore(tokenStore);
    }

    @Override
    public void setSupportRefreshToken(boolean supportRefreshToken) {
        super.setSupportRefreshToken(true);
    }

    @Override
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {

        OAuth2AccessToken existingAccessToken = tokenStore.getAccessToken(authentication);
        OAuth2RefreshToken refreshToken = null;
        //判断是否产生新的token
        if (alwaysNewAccessToken) {
            if (existingAccessToken != null) {
//                if (existingAccessToken.isExpired()) {
                if (existingAccessToken.getRefreshToken() != null) {
                    refreshToken = existingAccessToken.getRefreshToken();
                    // The token store could remove the refresh token when the
                    // access token is removed, but we want to
                    // be sure...
                    tokenStore.removeRefreshToken(refreshToken);
                }
                tokenStore.removeAccessToken(existingAccessToken);
            }
        }
        return super.createAccessToken(authentication);
    }

}