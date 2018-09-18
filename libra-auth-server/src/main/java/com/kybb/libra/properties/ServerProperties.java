package com.kybb.libra.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.PropertySource;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.springframework.cloud.commons.util.InetUtilsProperties.PREFIX;

//@ConfigurationProperties(prefix = "spring.cloud.network")
@Getter
@Setter
@Slf4j
public class ServerProperties extends PropertySource<Object> {
    public static final String SERVER_IP_PREFIX = "application.server";


    public ServerProperties(String name, Object source) {
        super(name, source);
    }

    public ServerProperties(String name) {
        super(name);
    }

    @Override
    public Object getProperty(String name) {
        if (!name.startsWith(SERVER_IP_PREFIX)) {
            return null;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Generating server ip property for '" + name + "'");
        }
        return getServerIp(name.substring(PREFIX.length()));
    }

    private Object getServerIp(String substring) {
        try {
            if (substring.equalsIgnoreCase("ip-address")) {
                InetAddress localHost = Inet4Address.getLocalHost();
                return localHost.getHostAddress()+"~~~~";
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }


}
