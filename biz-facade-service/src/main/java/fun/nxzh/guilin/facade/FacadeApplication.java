package fun.nxzh.guilin.facade;

import fun.nxzh.guilin.facade.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
@EnableConfigurationProperties(ApplicationProperties.class)
public class FacadeApplication {

  public static void main(String[] args) {
    SpringApplication.run(FacadeApplication.class, args);
  }
}
