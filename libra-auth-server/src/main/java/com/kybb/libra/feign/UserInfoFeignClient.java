package com.kybb.libra.feign;

import com.kybb.common.http.Body;
import com.kybb.common.http.ResponseUtil;
import com.kybb.solar.user.request.AccountRequest;
import com.kybb.solar.user.vo.AccountVO;
import com.kybb.solar.user.vo.UserInfoVO;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@FeignClient(
        value = "user-center-api",
        fallbackFactory = UserInfoFeignClientFallbackFactory.class
)
public interface UserInfoFeignClient {

    @PostMapping({"/users/unique"})
    ResponseEntity<Body<AccountVO>> accounts(@RequestBody AccountRequest accountRequest);

    @GetMapping({"/users/user-info"})
    ResponseEntity<Body<List<UserInfoVO>>> getUserInfo(@RequestParam("ids") List<Long> var1);

    @PutMapping("/users/{id}/token")
    ResponseEntity<Body> updateTokenByUserId(@PathVariable("id") long id, @RequestParam("token") String token);

}

@Component
@Slf4j
class UserInfoFeignClientFallbackFactory implements FallbackFactory<UserInfoFeignClient> {
    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public UserInfoFeignClient create(Throwable throwable) {
        return new UserInfoFeignClient() {
            @Override
            public ResponseEntity<Body<AccountVO>> accounts(AccountRequest accountRequest) {
                log.error("[exception] " + throwable.getClass().getName() + " [error message] " + throwable.getMessage() + "  [args] " + accountRequest);
                return ResponseUtil.ok(null);
            }

            @Override
            public ResponseEntity<Body<List<UserInfoVO>>> getUserInfo(List<Long> var1) {
                log.error("[exception] " + throwable.getClass().getName() + " [error message] " + throwable.getMessage() + "  [args] " + var1);
                return ResponseUtil.ok(null);
            }

            @Override
            public ResponseEntity updateTokenByUserId(long id, String token) {
                log.error("[exception] " + throwable.getClass().getName() + " [error message] " + throwable.getMessage() + "  [args] " + token + "," + id);
                return ResponseUtil.ok(null);

            }
        };
    }
}
