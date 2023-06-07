package com.bagrov.springpmhw;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SpringPMHWApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringPMHWApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Videorent store path: http://localhost:8081");
        System.out.println("SWAGGER path: http://localhost:8081/swagger-ui/index.html");
    }
}
