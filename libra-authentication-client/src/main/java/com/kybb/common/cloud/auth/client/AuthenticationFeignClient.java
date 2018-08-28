package com.kybb.common.cloud.auth.client;

import com.kybb.common.http.Body;
import com.kybb.common.http.ResponseUtil;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Auther: vicykie
 * @Date: 2018/8/16 09:48
 * @Description:
 */
@FeignClient(value = "auth-server")
public interface AuthenticationFeignClient {
    @PostMapping("/encryption/code")
    ResponseEntity<Body<String>> encrypt(@RequestParam("input") String input);

    @PostMapping("/token/evict")
    ResponseEntity evictToken(@RequestParam("token") String token);


}
