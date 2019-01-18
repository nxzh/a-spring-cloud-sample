package fun.nxzh.guilin.food;

import fun.nxzh.guilin.food.infra.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties(ApplicationProperties.class)
public class FoodApplication {

  public static void main(String[] args) {
    SpringApplication.run(FoodApplication.class, args);
  }
}
