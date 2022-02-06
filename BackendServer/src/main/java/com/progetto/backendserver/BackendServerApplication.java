package com.progetto.backendserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendServerApplication {
    private static final Logger log = LoggerFactory.getLogger(BackendServerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(BackendServerApplication.class, args);
    }

}
