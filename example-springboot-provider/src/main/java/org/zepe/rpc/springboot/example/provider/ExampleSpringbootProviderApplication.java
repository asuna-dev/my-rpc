package org.zepe.rpc.springboot.example.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.zepe.rpc.springboot.starter.annotation.EnableRpc;

@SpringBootApplication
@EnableRpc(needServer = true)
public class ExampleSpringbootProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringbootProviderApplication.class, args);
    }

}
