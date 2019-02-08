package fun.nxzh.guilin.facade.controller;

import fun.nxzh.guilin.facade.model.BasketCheckout;
import fun.nxzh.guilin.facade.service.BasketService;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/basket")
public class BasketController {
  private BasketService basketService;

  public BasketController(BasketService basketService) {
    this.basketService = basketService;
  }

  @PostMapping(value = "/checkout", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @PreAuthorize("hasAuthority('SCOPE_all') and hasAuthority('ROLE_USER')")
  public String checkout(@RequestBody BasketCheckout basketCheckout) {
    // TODO: Add gateway filter to mark the requestId
    JwtAuthenticationToken token = (JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();;
    String userId = (String)token.getTokenAttributes().get("user_id");
    basketCheckout.setBuyer(userId);
    basketCheckout.setRequestId(UUID.randomUUID().toString());
    return basketService.checkout(basketCheckout);
  }
}
