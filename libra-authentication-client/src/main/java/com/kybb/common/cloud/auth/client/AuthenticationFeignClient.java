package com.kybb.common.cloud.auth.client;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: vicykie
 * @Date: 2018/8/16 09:48
 * @Description:
 */
@FeignClient(value = "auth-server", fallbackFactory = AuthenticationFallBackFactory.class)
public interface AuthenticationFeignClient {
    /**
     * 密码加密
     *
     * @param input
     * @return
     */
    @PostMapping("/encryption/code")
    String encrypt(@RequestParam("input") String input);

    /**
     * 清除token，
     * 清除后需要重新登录
     *
     * @param token
     * @return
     */
    @PutMapping("/token/evict")
    void evictToken(@RequestParam("token") String token);

    /**
     * @param raw    原始密码（md5）
     * @param encode 加密后
     * @return
     */
    @GetMapping("/encryption/matches")
    boolean matches(@RequestParam("raw") String raw, @RequestParam("encode") String encode);
}

@Component
@Slf4j
class AuthenticationFallBackFactory implements FallbackFactory<AuthenticationFeignClient> {
    @Override
    public AuthenticationFeignClient create(Throwable throwable) {
        return new AuthenticationFeignClient() {
            @Override
            public String encrypt(String input) {
                log.error("[exception] " + throwable.getClass().getName() + " [error message] " + throwable.getMessage() + "  [args] " + input);
                return "default_password";
            }

            @Override
            public void evictToken(String token) {
                log.error("[exception] " + throwable.getClass().getName() + " [error message] " + throwable.getMessage() + "  [args] " + token);
            }

            @Override
            public boolean matches(String raw, String encode) {
                log.error("[exception] " + throwable.getClass().getName() + " [error message] " + throwable.getMessage() + "  [args] " + raw + " , " + encode);
                return false;
            }
        };
    }
}