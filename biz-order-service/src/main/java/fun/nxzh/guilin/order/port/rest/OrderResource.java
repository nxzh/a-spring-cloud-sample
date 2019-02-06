package fun.nxzh.guilin.order.port.rest;

import fun.nxzh.guilin.order.infra.config.ApplicationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class OrderResource {

  private ApplicationProperties applicationProperties;

  public OrderResource(ApplicationProperties applicationProperties) {
    this.applicationProperties = applicationProperties;
  }


}
