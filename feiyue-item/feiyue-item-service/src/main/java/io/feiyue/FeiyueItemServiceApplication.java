package io.feiyue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("io.feiyue.item.mapper")
public class FeiyueItemServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FeiyueItemServiceApplication.class, args);
    }
}
