package com.ekoskladvalidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.ekoskladvalidator"})
public class EkoskladvalidatorApplication {

    private static final Logger log = LoggerFactory.getLogger(EkoskladvalidatorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(EkoskladvalidatorApplication.class, args);
    }

}

