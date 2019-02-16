package fun.nxzh.guilin.turbine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.turbine.stream.EnableTurbineStream;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableDiscoveryClient
@EnableTurbineStream
@EnableBinding
public class TurbineService {

  public static void main(String[] args) {
    SpringApplication.run(TurbineService.class, args);
  }
}
