package com.kybb.libra.entrypoint;

import com.kybb.common.cloud.integration.SmsCodeRequest;
import com.kybb.common.http.ResponseUtil;
import com.kybb.libra.service.SmsCodeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: vicykie
 * @Date: 2018/8/14 19:16
 * @Description:
 */
@RestController
@RequestMapping("/sms")
public class SmsController {
    @Autowired
    private SmsCodeService smsCodeService;

    /**
     * 发送短信验证码
     *
     * @param smsCodeRequest
     * @return
     */
    @PostMapping("/code")
    public ResponseEntity addCode(@RequestBody SmsCodeRequest smsCodeRequest) {
        if (StringUtils.isEmpty(smsCodeRequest.getMobile())) {
            return ResponseUtil.badRequest("参数错误。mobile为空");
        }
        if (StringUtils.isEmpty(smsCodeRequest.getDeviceId())) {
            return ResponseUtil.badRequest("参数错误。deviceId为空");
        }
        return ResponseUtil.ok(smsCodeService.saveAndSendCode(smsCodeRequest));
    }

}
