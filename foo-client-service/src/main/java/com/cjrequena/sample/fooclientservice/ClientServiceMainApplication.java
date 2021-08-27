package com.cjrequena.sample.fooclientservice;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@Log4j2
@SpringBootApplication
@EnableDiscoveryClient
@EnableAutoConfiguration
public class ClientServiceMainApplication {
  public static void main(String[] args) {
    SpringApplication.run(ClientServiceMainApplication.class, args);
  }
}
