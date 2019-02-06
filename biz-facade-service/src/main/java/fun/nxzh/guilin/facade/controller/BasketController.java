package fun.nxzh.guilin.facade.controller;

import fun.nxzh.guilin.facade.model.Basket;
import fun.nxzh.guilin.facade.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/basket")
public class BasketController {
  private BasketService basketService;
  @Autowired private OAuth2AuthorizedClientService clientService;

  public BasketController(BasketService basketService) {
    this.basketService = basketService;
  }

  @PostMapping(value = "/checkout", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @PreAuthorize("hasAuthority('SCOPE_all') and hasAuthority('ROLE_USER')")
  public String checkout(Basket basket) {
    return basketService.checkout(basket);
  }
}
