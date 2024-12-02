package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class T1OpenAcademyApplication {

    public static void main(String[] args) {
        SpringApplication.run(T1OpenAcademyApplication.class, args);
    }

}
