package com.arthur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @authur arthur
 * @desc
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ServicePersonApplication {
    public static void main(String[] args){
        SpringApplication.run(ServicePersonApplication.class,args);
    }
}
