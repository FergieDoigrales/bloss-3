package org.fergoeqs.blps1;

import org.camunda.bpm.engine.test.Deployment;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@Deployment(resources = {"classpath:jjj.bpmn"})
//@EnableProcessApplication("jeb")
public class Blps1Application {

    public static void main(String[] args) {
        SpringApplication.run(Blps1Application.class, args);
    }

}
