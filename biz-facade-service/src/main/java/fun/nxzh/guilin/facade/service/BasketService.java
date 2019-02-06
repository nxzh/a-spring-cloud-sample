package fun.nxzh.guilin.facade.service;

import fun.nxzh.guilin.facade.client.BasketClient;
import fun.nxzh.guilin.facade.model.Basket;
import org.springframework.stereotype.Service;

@Service
public class BasketService {
  private BasketClient basketClient;

  public BasketService(BasketClient basketClient) {
    this.basketClient = basketClient;
  }

  public String checkout(Basket basket) {
    return basketClient.checkout(basket);
  }
}
