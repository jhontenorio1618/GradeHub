package com.github.gradehub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class GradeHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(GradeHubApplication.class, args);

    }

    @GetMapping("/secured")
    public String secured(){
        return "This is secured";

    }

    @GetMapping("/public")
    public String publc(){
        return "This is secured";

    }

}
