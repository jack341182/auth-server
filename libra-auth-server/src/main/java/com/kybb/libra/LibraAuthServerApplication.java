package com.kybb.libra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
//@ComponentScan(basePackages = {"com.kybb"})
public class LibraAuthServerApplication {

//    @Bean
//    Request.Options feignOptions() {
//        return new Request.Options(10 * 1000, 10 * 1000);
//    }
//


    public static void main(String[] args) {
//        SpringApplication springApplication = new SpringApplication();
//
//        ConfigurableEnvironment environment = new StandardEnvironment();
////        environment.getPropertySources().addFirst(new ServerProperties());
//        springApplication.setEnvironment(environment);
        SpringApplication.run(LibraAuthServerApplication.class, args);
    }


}
