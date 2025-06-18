package org.fergoeqs.blps1;

import org.camunda.bpm.spring.boot.starter.CamundaBpmAutoConfiguration;
import org.fergoeqs.blps1.services.VacancyService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Blps1Application {

    public static void main(String[] args) {
        SpringApplication.run(Blps1Application.class, args);
    }

}
