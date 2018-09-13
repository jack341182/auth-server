package com.kybb.libra.entrypoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/consul")
public class URLController {
    @Autowired
    ConsulDiscoveryClient discoveryClient;
    @GetMapping("/services")
    public List<String> getUrlList(){
        return discoveryClient.getServices();
    }
    @GetMapping("/instances")
    public List<ServiceInstance> getServiceInstances(){
        return discoveryClient.getAllInstances();
    }
}
