package com.liu.authserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Administrator on 2018/8/9.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
//@ComponentScan("com.liu.authserver")
@MapperScan("com.liu.authserver.mapper")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }

    @Bean
    @LoadBalanced
    public RestOperations restTemplate() {
        return new RestTemplate();
    }
}