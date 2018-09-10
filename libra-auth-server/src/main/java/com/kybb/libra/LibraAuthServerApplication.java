package com.kybb.libra;

import feign.Request;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@SpringBootApplication(scanBasePackages = {"com.kybb"})
@EnableFeignClients(basePackages = {"com.kybb.**"})
@ComponentScan(basePackages = {"com.kybb"})
@EnableCircuitBreaker
public class LibraAuthServerApplication {

    @Bean
    Request.Options feignOptions() {
        return new Request.Options(10 * 1000, 10 * 1000);
    }

    @Bean
    public MessageSource securityMessageSource(){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.addBasenames("classpath:messages");
        return messageSource;
    }

    public static void main(String[] args) {
        SpringApplication.run(LibraAuthServerApplication.class, args);
    }
}
