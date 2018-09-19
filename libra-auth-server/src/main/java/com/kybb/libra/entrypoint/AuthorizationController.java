package com.kybb.libra.entrypoint;

import com.kybb.common.http.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
public class AuthorizationController {
    @Autowired
    @Qualifier("consumerTokenServices")
    private ConsumerTokenServices consumerTokenServices;

    @PutMapping("/token/evict")
    public ResponseEntity evictToken(@RequestParam("token") String token) {
        consumerTokenServices.revokeToken(token);
        log.info("=====>清除 token  " + token);
        return ResponseUtil.ok(null, "用户已退出");
    }


}
