package com.tujia.os.lilyth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class LilythApplication {

  public static void main(String[] args) {
    SpringApplication.run(LilythApplication.class, args);
  }

}
