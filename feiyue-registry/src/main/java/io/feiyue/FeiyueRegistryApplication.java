package io.feiyue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class FeiyueRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeiyueRegistryApplication.class, args);
    }
}
