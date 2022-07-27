package com.symphony.devsol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class WorkflowerExtApp {
    public static void main(String[] args) {
        SpringApplication.run(WorkflowerExtApp.class, args);
    }
}
