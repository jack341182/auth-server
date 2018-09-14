package com.kybb.libra.service;

import com.kybb.columba.message.enums.MessageBusinessEnum;
import com.kybb.columba.message.request.MessageRequest;
import com.kybb.columba.message.vo.SmsCaptchaVO;
import com.kybb.common.cloud.integration.SmsCodeLogin;
import com.kybb.common.http.Body;
import com.kybb.libra.bean.SmsCodeStatus;
import com.kybb.libra.feign.MessageFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        // 检查缓存的验证码
        String cacheCode = getCode(smsCodeLogin);
        if (StringUtils.isEmpty(cacheCode)) {
            //TODO   发送短信
            ResponseEntity<Body<SmsCaptchaVO>> bodyResponseEntity = messageFeignClient.sendCaptcha(MessageRequest.builder()
                    .mobile(smsCodeLogin.getMobile())
                    .business(MessageBusinessEnum.LOGIN_CAPTCHA)
                    .build());
            int status = bodyResponseEntity.getStatusCode().value();
            if (status >= HttpStatus.OK.value() && status < 300 && status!= HttpStatus.NO_CONTENT.value()) {
                SmsCaptchaVO captchaVO = bodyResponseEntity.getBody().getData();
                String code = captchaVO.getValidateCode();
//                String code = "123456";
                String md5Hex = DigestUtils.md5Hex(code);
                String encode = passwordEncoder.encode(md5Hex);
                redisTemplate.opsForValue().set(SMS_CODE_PREFIX + smsCodeLogin.getMobile() + smsCodeLogin.getDeviceId(),
                        encode, 5, TimeUnit.MINUTES);
                if (log.isDebugEnabled()) {
                    log.debug(" mobile " + smsCodeLogin.getMobile() + " code " + code+",md5 " +md5Hex+", encode " +encode);
                }
                return SmsCodeStatus.builder()
                        .success(true)
                        .message("验证码发送成功")
                        .build();
            }
            return SmsCodeStatus.builder()
                    .success(false)
                    .message(bodyResponseEntity.getBody().getMessage())
                    .build();
        } else {
            return SmsCodeStatus.builder()
                    .success(false)
                    .message("验证码未失效，请检查手机短信")
                    .build();
        }
    }

    public String getCode(SmsCodeLogin smsCodeRequest) {
        return (String) redisTemplate.opsForValue().get(SMS_CODE_PREFIX + smsCodeRequest.getMobile() + smsCodeRequest.getDeviceId());
    }


}
