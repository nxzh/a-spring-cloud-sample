package fun.nxzh.guilin.facade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class FacadeApplication {
  public static void main(String[] args) {
    SpringApplication.run(FacadeApplication.class, args);
  }

  @GetMapping(value = "/api/v1/test")
  public String test() {
    return "test";
  }
}
