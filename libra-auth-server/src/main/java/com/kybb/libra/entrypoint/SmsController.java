package com.kybb.libra.entrypoint;

import com.kybb.common.cloud.integration.SmsCodeLogin;
import com.kybb.common.cloud.integration.SmsCodeRequest;
import com.kybb.common.http.Body;
import com.kybb.common.http.ResponseUtil;

import com.kybb.libra.bean.SmsCodeStatus;
import com.kybb.libra.feign.UserInfoFeignClient;
import com.kybb.libra.service.SmsCodeService;
import com.kybb.solar.user.vo.UserInfoVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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


    @Autowired
    private UserInfoFeignClient userFeignClient;


    /**
     * 发送短信验证码
     *
     * @param smsCodeRequest
     * @return
     */
    @PostMapping("/code")
    public ResponseEntity addCode(@RequestBody SmsCodeLogin smsCodeRequest) {
        if (StringUtils.isEmpty(smsCodeRequest.getMobile())) {
            return ResponseUtil.badRequest("参数错误。mobile为空");
        }
        if (StringUtils.isEmpty(smsCodeRequest.getDeviceId())) {
            return ResponseUtil.badRequest("参数错误。deviceId为空");
        }
        SmsCodeStatus smsCodeStatus = smsCodeService.saveAndSendCode(smsCodeRequest);
        return smsCodeStatus.isSuccess() ? ResponseUtil.ok(null, smsCodeStatus.getMessage()) : ResponseUtil.notAcceptable(smsCodeStatus.getMessage());
    }

    @GetMapping("/test")
    public ResponseEntity test() {
        List<Long> ids = new ArrayList<>();
        ids.add(4L);
//        ResponseEntity<Body<List<UserInfoVO>>> userInfo = userFeignClient.getUserInfo(ids);

        return ResponseEntity.ok("");
    }

}
