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
    public String encrypt(@RequestBody String input) {

        System.out.println(passwordEncoder.matches(input,
                "$2a$10$hKAeCsPP6K.2OCd7ijvoT.CVKpYWroColGLYacEmCbf63BwhMflZ2"));
        String encode = passwordEncoder.encode(input);
        if (log.isDebugEnabled()) {
            log.debug("===  input str is " + input);
            log.debug("===  encode  str is " + encode);

//            e10adc3949ba59abbe56e057f20f883e
//            $2a$10$AmyDiCqFkRY4wsUOuNutx.lWZCSYgpWwM/7taJAXc9MTAAEOfrFP6
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode(input));
        System.out.println(encoder.matches(input,
                "$2a$10$2noOSC9xPmbwkGMMmy1J/uhmXhTPXuySRRexJVTrrYOIJaEDWXcBm"));
        return encode;
    }


}
