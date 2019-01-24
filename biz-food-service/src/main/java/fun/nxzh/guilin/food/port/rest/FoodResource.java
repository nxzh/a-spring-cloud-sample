package fun.nxzh.guilin.food.port.rest;

import fun.nxzh.guilin.food.infra.config.ApplicationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class FoodResource {
  private ApplicationProperties applicationProperties;

  public FoodResource(ApplicationProperties applicationProperties) {
    this.applicationProperties = applicationProperties;
  }
}
