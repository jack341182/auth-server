package com.kybb.libra.service;

import com.kybb.common.cloud.integration.SmsCodeLogin;
import com.kybb.common.cloud.integration.SmsCodeRequest;
import com.kybb.libra.bean.SmsCodeStatus;
import com.kybb.libra.feign.MessageFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
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
     * @param smsCodeLogin
     * @return
     */
    public SmsCodeStatus saveAndSendCode(SmsCodeLogin smsCodeLogin) {
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
        // TODO: 2018/8/31 检查缓存的验证码
        String cacheCode = getCode(smsCodeLogin);
        if(StringUtils.isEmpty(cacheCode)){
            String code = "123456";
            String md5Hex = DigestUtils.md5Hex(code);
            String encode = passwordEncoder.encode(md5Hex);
            redisTemplate.opsForValue().set(SMS_CODE_PREFIX + smsCodeLogin.getMobile() + smsCodeLogin.getDeviceId(),
                    encode, 10, TimeUnit.MINUTES);
            return SmsCodeStatus.builder()
                    .success(true)
                    .message("验证码发送成功")
                    .build();
        }else {
            return SmsCodeStatus.builder()
                    .success(false)
                    .message("验证码未失效，请检查手机短信")
                    .build();
        }
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
