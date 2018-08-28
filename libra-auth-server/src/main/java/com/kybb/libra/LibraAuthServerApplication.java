package com.kybb.libra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages={"com.kybb"})
@EnableFeignClients(basePackages={"com.kybb.**"})
@ComponentScan(basePackages = {"com.kybb"})
@EnableHystrix
public class LibraAuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraAuthServerApplication.class, args);
    }
}
