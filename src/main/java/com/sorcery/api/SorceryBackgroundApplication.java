package com.sorcery.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author jinglv
 * @date 2021/01/18
 */
@SpringBootApplication
@MapperScan(basePackages = "com.sorcery.api.dao")
public class SorceryBackgroundApplication {
    public static void main(String[] args) {
        SpringApplication.run(SorceryBackgroundApplication.class, args);
    }

}
