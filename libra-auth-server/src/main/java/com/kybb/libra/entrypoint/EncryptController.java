package com.kybb.libra.entrypoint;

import com.kybb.common.http.Body;
import com.kybb.common.http.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
public class EncryptController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/code")
    public String encrypt(@RequestParam("input") String input) {
        return passwordEncoder.encode(input);
    }


//    public static void main(String[] args) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String raw = "b8a5d79fdffc7766c64735288beb064b";
//        String raw1 = "e10adc3949ba59abbe56e057f20f883e";
//        System.out.println(encoder.encode(raw));
//        System.out.println(encoder.matches(raw, "$2a$10$By1N1IAuZB4nVm52svL3UuGC7n20I7knwSjNBjyRkXdg0T9EfF.fC"));
//        System.out.println(encoder.matches(raw1, "$2a$10$JE4GSz2cvk0sbew8rqC3ue08owNWumRzEBokvIHC8PZyXf8ujt5Ja"));
//    }
}
