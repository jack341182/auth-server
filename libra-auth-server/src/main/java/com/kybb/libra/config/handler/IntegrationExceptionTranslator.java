package com.kybb.libra.config.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class IntegrationExceptionTranslator<E extends OAuth2Exception> extends DefaultWebResponseExceptionTranslator {
    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        log.info("====》  处理异常信息 [exception ]" + e.getClass().getName());
        if (e instanceof InvalidGrantException) {
            return new ResponseEntity<>(new OAuth2Exception("用户名/密码错误"), HttpStatus.UNAUTHORIZED);
        }
        return super.translate(e);
    }
}
