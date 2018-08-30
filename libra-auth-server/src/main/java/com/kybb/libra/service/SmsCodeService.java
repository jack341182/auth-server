package com.kybb.libra.service;

import com.kybb.common.cloud.integration.SmsCodeLogin;
import com.kybb.common.cloud.integration.SmsCodeRequest;
import com.kybb.libra.feign.MessageFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author vicykie
 * @Date: 2018/8/14 19:25
 * @Description:
 */
@Service
@Slf4j
public class SmsCodeService {

    private static final String SMS_CODE_PREFIX = "sms_code_cache_";
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageFeignClient messageFeignClient;

    /**
     * 校验是否存在，存在则不返回
     *
     * @param smsCodeRequest
     * @return
     */
    public String saveAndSendCode(SmsCodeRequest smsCodeRequest) {
        // TODO: 2018/8/22  发送短信
//        ResponseEntity<Body<SmsCaptchaVO>> bodyResponseEntity = messageFeignClient.sendCaptcha(MessageRequest.builder()
//                .mobile(smsCodeRequest.getMobile())
//                .business(MessageBusinessEnum.LOGIN_CAPTCHA)
//                .build());
//        if (bodyResponseEntity.getStatusCode().value() >= HttpStatus.OK.value() && bodyResponseEntity.getStatusCode().value() < 300) {
//
//            SmsCaptchaVO code = bodyResponseEntity.getBody().getData();
//
//        }
        String code = "123456";
        String md5Hex = DigestUtils.md5Hex(code);
        String encode = passwordEncoder.encode(md5Hex);
        redisTemplate.opsForValue().set(SMS_CODE_PREFIX + smsCodeRequest.getMobile() + smsCodeRequest.getDeviceId(),
                encode, 10, TimeUnit.MINUTES);
        return code;
//        if (log.isDebugEnabled()) {
//            log.debug("message service status code" + bodyResponseEntity.getStatusCode() + " , message: " + bodyResponseEntity.getBody().getMessage());
//        }
//        throw new ServiceException("短信发送失败,请稍后重试");
    }

    public String encode(String raw) {
        return passwordEncoder.encode(raw);
    }

    public boolean match(String raw, String encode) {
        return passwordEncoder.matches(raw, encode);
    }

    public String getCode(SmsCodeLogin smsCodeRequest) {
        return (String) redisTemplate.opsForValue().get(SMS_CODE_PREFIX + smsCodeRequest.getMobile() + smsCodeRequest.getDeviceId());
    }
}
