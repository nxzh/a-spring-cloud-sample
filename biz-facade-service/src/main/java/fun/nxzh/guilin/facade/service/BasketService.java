package fun.nxzh.guilin.facade.service;

import fun.nxzh.guilin.facade.client.BasketClient;
import fun.nxzh.guilin.facade.model.BasketCheckout;
import org.springframework.stereotype.Service;

@Service
public class BasketService {
  private BasketClient basketClient;

  public BasketService(BasketClient basketClient) {
    this.basketClient = basketClient;
  }

  public String checkout(BasketCheckout basketCheckout) {
    return basketClient.checkout(basketCheckout);
  }
}
