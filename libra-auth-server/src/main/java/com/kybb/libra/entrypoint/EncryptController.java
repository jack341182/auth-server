package com.kybb.libra.entrypoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: vicykie
 * @Date: 2018/8/16 09:44
 * @Description:
 */
@RestController
@RequestMapping("/encryption")
@Slf4j
public class EncryptController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/code")
    public String encrypt(@RequestParam("input") String input) {
        String encode = passwordEncoder.encode(input);
        if (log.isDebugEnabled()) {
            log.debug("===  input str is " + input);
            log.debug("===  encode  str is " + encode);
        }
        return encode;
    }


}
