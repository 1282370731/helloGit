package com.hnj;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@ServletComponentScan
@SpringBootApplication
@EnableTransactionManagement
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Test
    public void test(){
        String s = "123";
        int a= Integer.parseInt(s);
        System.out.println(Integer.parseInt(s));
        System.out.println(Integer.valueOf(s).getClass());
    }
}

