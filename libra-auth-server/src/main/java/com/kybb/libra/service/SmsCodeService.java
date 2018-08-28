package com.kybb.libra.service;

import com.kybb.columba.message.feign.MobileMessageFeignClient;
import com.kybb.common.cloud.integration.SmsCodeLogin;
import com.kybb.common.cloud.integration.SmsCodeRequest;
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
    private MobileMessageFeignClient mobileMessageFeignClient;

    /**
     * 校验是否存在，存在则不返回
     *
     * @param smsCodeRequest
     * @return
     */
    public String saveAndSendCode(SmsCodeRequest smsCodeRequest) {
        // TODO: 2018/8/22  发送短信
//        Map<String, Object> map = new HashMap<>();
//        map.put("product", "just login in");
//        ResponseData<SmsCaptchaVO> responseData = mobileMessageFeignClient.sendCaptcha(MessageRequest.builder()
//                .async(false)
//                .mobile(smsCodeRequest.getMobile())
//                .template("SMS_78750039")
//                .templateParams(map)
//                .build());
//        if (responseData.isSuccess()) {
//            String code = responseData.getData().getValidateCode();
            String code = "123456";
            String md5Hex = DigestUtils.md5Hex(code);
            String encode = passwordEncoder.encode(md5Hex);
            log.info("模拟发送短信  " + smsCodeRequest.getMobile() + " /code  " + code + "  md5 " + md5Hex);
            log.info("模拟发送短信  " + smsCodeRequest.getMobile() + " /code  " + code + "  hex " + encode);
            redisTemplate.opsForValue().set(SMS_CODE_PREFIX + smsCodeRequest.getMobile() + smsCodeRequest.getDeviceId(),
                    encode, 50, TimeUnit.MINUTES);
            return code;
//        }
//        throw new ServiceException("短信发送失败,请稍后重试");
    }
    public String encode(String raw){
        return passwordEncoder.encode(raw);
    }

    public boolean match(String raw,String encode){
        return passwordEncoder.matches(raw,encode);
    }

    public String getCode(SmsCodeLogin smsCodeRequest) {
        return (String) redisTemplate.opsForValue().get(SMS_CODE_PREFIX + smsCodeRequest.getMobile() + smsCodeRequest.getDeviceId());
    }
}
