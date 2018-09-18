package com.kybb.libra;

import com.kybb.libra.properties.ServerProperties;
import feign.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

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
