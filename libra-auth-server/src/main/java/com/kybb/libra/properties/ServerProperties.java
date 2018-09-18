package com.kybb.libra.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

@ConfigurationProperties(prefix = "spring.cloud.network")
@Getter
@Setter
public class ServerProperties {
    private String localIp;

    public static void main(String[] args) throws UnknownHostException {
        InetAddress localHost = Inet4Address.getLocalHost();
        System.out.println(localHost.toString());
    }
}
