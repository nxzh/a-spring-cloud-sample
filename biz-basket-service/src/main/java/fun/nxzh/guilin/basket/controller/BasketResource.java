package fun.nxzh.guilin.basket.controller;

import fun.nxzh.guilin.basket.config.event.IntegrationEventBus;
import fun.nxzh.guilin.basket.event.UserCheckoutAccepted;
import fun.nxzh.guilin.basket.model.Basket;
import fun.nxzh.guilin.basket.model.BasketCheckout;
import fun.nxzh.guilin.basket.service.BasketService;
import fun.nxzh.guilin.basket.util.IntegrationEventHelper;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class BasketResource {

  private BasketService basketService;
  private IntegrationEventBus eventBus;

  public BasketResource(BasketService basketService, IntegrationEventBus eventBus) {
    this.basketService = basketService;
    this.eventBus = eventBus;
  }

  @PostMapping(value = "/checkout", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @Transactional
  public ResponseEntity<?> checkout(@RequestBody BasketCheckout basketCheckout) {
    // validate basket.buyerId and requestId
    Basket basket = basketService.findBasketByUser(basketCheckout.getBuyer());
    if (basket == null) {
      throw new IllegalStateException(
          String.format("Can't find basket for user-%s!", basket.getBuyerId()));
    }
    UserCheckoutAccepted checkoutAccepted =
        new UserCheckoutAccepted(
            basket.getBuyerId(), UUID.randomUUID().toString(), basketCheckout, basket);
    eventBus.publish(IntegrationEventHelper.createEvent(checkoutAccepted, "UserCheckoutAccepted"));
    return ResponseEntity.ok(checkoutAccepted.getOrderId());
  }
}
