package com.arthur.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @authur arthur
 * @desc
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ServerGatewayMain {
    public static void main(String[] args){
        SpringApplication.run(ServerGatewayMain.class,args);
    }
}
