package fun.nxzh.guilin.facade.client;

import fun.nxzh.guilin.facade.model.Basket;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "basket-service", path = "/api/v1", decode404 = true)
public interface BasketClient {

  @PostMapping(value = "/checkout", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  String checkout(Basket basket);
}
