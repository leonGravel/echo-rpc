package com.gravel.echo.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.gravel.echo")
public class ServerExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerExampleApplication.class, args);
    }

}
