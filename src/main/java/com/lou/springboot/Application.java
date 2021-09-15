package com.lou.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 声明：联通性测试程序，无可靠性、性能、扩展性、安全性保证
 *
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        System.out.println("启动 Spring Boot...");
        SpringApplication.run(Application.class, args);
        System.out.println("ヾ(◍°∇°◍)ﾉﾞ  Application启动成功   ヾ(◍°∇°◍)ﾉﾞ");
    }
}