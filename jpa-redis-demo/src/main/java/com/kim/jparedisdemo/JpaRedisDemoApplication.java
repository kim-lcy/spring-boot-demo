package com.kim.jparedisdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class JpaRedisDemoApplication {

    public static void main(String[] args) { SpringApplication.run(JpaRedisDemoApplication.class, args);}

}
