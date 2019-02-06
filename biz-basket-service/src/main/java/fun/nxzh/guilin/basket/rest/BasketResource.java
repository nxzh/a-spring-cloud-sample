package fun.nxzh.guilin.basket.rest;

import fun.nxzh.guilin.basket.model.Basket;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class BasketResource {

  @PostMapping(value = "/checkout", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<?> checkout(Basket basket) {
    String info = basket.getUserId() + " : " + basket.getBasketId();
    return ResponseEntity.ok(info);
  }
}
