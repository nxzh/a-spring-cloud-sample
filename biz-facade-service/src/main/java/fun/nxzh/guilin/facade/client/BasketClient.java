package fun.nxzh.guilin.facade.client;

import feign.hystrix.FallbackFactory;
import fun.nxzh.guilin.facade.model.BasketCheckout;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "basket-service", path = "/api/v1", decode404 = true, fallbackFactory = HystrixClientFallbackFactory.class)
public interface BasketClient {

  @PostMapping(value = "/checkout", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  String checkout(BasketCheckout basket);
}

@Component
class HystrixClientFallbackFactory implements FallbackFactory<BasketClient> {

  @Override
  public BasketClient create(Throwable cause) {
    return basket -> "return by hystrix --------";
  }
}

