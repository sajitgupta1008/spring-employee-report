package com.adpushup.task.springemployeereport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class SpringEmployeeReportApplication {
    
    @Autowired
    ObjectMapper objectMapper;
    
    public static void main(String[] args) {
        SpringApplication.run(SpringEmployeeReportApplication.class, args);
    }
    
    @PostConstruct
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }
    
}
