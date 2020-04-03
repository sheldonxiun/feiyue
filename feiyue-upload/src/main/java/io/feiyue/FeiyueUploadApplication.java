package io.feiyue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FeiyueUploadApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeiyueUploadApplication.class, args);
    }
}
