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
        return encode;
    }

    @GetMapping("/matches")
    public boolean matches(@RequestParam("raw") String raw, @RequestParam("encode") String encode) {
        System.out.println(raw);
        System.out.println(encode);
//        if (log.isDebugEnabled()) {
            log.debug("===  raw password is " + raw);
            log.debug("===  encode  password is " + encode);
//        }
        System.out.println(passwordEncoder.encode(raw));
        return passwordEncoder.matches(raw, encode);
    }


}
