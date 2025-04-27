package org.zepe.rpc.springboot.example.comsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.zepe.rpc.springboot.starter.annotation.EnableRpc;

@SpringBootApplication
@EnableRpc(needServer = false)
public class ExampleSpringbootComsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringbootComsumerApplication.class, args);
    }

}
