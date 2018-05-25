package com.bright.apollo.springcloudali2;

import com.bright.apollo.listener.ContextListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
@ComponentScan(basePackages = {"com.bright.apollo.*"})
@ServletComponentScan
public class SpringcloudAli2Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringcloudAli2Application.class);
        app.addListeners(new ContextListener());
        app.run(args);
//        SpringApplication.run(SpringcloudAli2Application.class, args);
    }
}
