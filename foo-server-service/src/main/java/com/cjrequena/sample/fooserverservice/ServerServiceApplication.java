package com.cjrequena.sample.fooserverservice;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
@EnableAutoConfiguration
public class ServerServiceApplication implements CommandLineRunner {
  public static void main(String[] args) {
    SpringApplication.run(ServerServiceApplication.class, args);
  }

  @Override public void run(String... args) throws Exception {
    log.info("****************************");
    log.info("FOO SERVER SERVICE");
    log.info("****************************");

  }
}
