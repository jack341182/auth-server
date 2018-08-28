package com.kybb.libra.feign;

import com.kybb.common.http.Body;
import com.kybb.common.http.ResponseUtil;
import com.kybb.solar.user.request.AccountRequest;
import com.kybb.solar.user.vo.AccountVO;
import feign.hystrix.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

@FeignClient(
        value = "user-center-api",
        fallbackFactory = UserHystrix1.class
)
public interface UserFeignClient1 {
    @PostMapping({"/users/unique"})
    ResponseEntity<Body<AccountVO>> accounts(@RequestBody AccountRequest var1);
}

@Component
class UserHystrix1 implements FallbackFactory<UserFeignClient1> {


    @Override
    public UserFeignClient1 create(Throwable throwable) {
        return new UserFeignClient1() {
            @Override
            public ResponseEntity<Body<AccountVO>> accounts(AccountRequest var1) {
                try {
                    return ResponseUtil.formThrowError(throwable, "user-center-api");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return ResponseUtil.serviceUnavailable("user-center-api");
            }
        };
    }
}
