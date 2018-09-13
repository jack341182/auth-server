package com.kybb.libra.entrypoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Auther: vicykie
 * @Date: 2018/8/16 09:44
 * @Description:
 */
@RestController
@RequestMapping("/encryption")
@Slf4j
public class EncryptController {

    @Resource
    @Qualifier("bCryptPasswordEncoder")
    private PasswordEncoder passwordEncoder;

    @PostMapping("/code")
    public String encrypt(@RequestParam("input") String input) {
        String encode = passwordEncoder.encode(input);
        if (log.isDebugEnabled()) {
            log.debug("===  input str is " + input);
            log.debug("===  encode  str is " + encode);
        }
        System.out.println(passwordEncoder.matches("e10adc3949ba59abbe56e057f20f883e","$2a$04$pNAHSX5vYuzG4JHvMjL98ePGrFXrl9aT8KNwnlEyNSu2wy5bG25bi"));
        return encode;
    }


}
