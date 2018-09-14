package com.kybb.libra.feign;

import com.kybb.columba.message.request.MessageRequest;
import com.kybb.columba.message.vo.SmsCaptchaVO;
import com.kybb.common.http.Body;
import com.kybb.common.http.ResponseUtil;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "columba-message", fallbackFactory = MessageServiceFallbackFactory.class)
public interface MessageFeignClient {
    @PostMapping({"/mobiles/inner/captcha"})
    ResponseEntity<Body<SmsCaptchaVO>> sendCaptcha(@RequestBody MessageRequest messageRequest);
}

@Component
@Slf4j
class MessageServiceFallbackFactory implements FallbackFactory<MessageFeignClient> {

    @Override
    public MessageFeignClient create(Throwable throwable) {
        return new MessageFeignClient() {
            @Override
            public ResponseEntity<Body<SmsCaptchaVO>> sendCaptcha(MessageRequest messageRequest) {
                log.error("[exception] " + throwable.getClass().getName() + " [error message] " + throwable.getMessage() + "  [args] " + messageRequest);
                return ResponseUtil.ok();
            }
        };
    }
}
