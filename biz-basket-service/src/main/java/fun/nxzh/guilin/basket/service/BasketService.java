package fun.nxzh.guilin.basket.service;

import fun.nxzh.guilin.basket.dao.BasketDao;
import fun.nxzh.guilin.basket.model.Basket;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BasketService {
  private BasketDao basketDao;

  public BasketService(BasketDao basketDao) {
    this.basketDao = basketDao;
  }

  @Transactional
  public Basket findBasketByUser(String userId) {
    return basketDao.findBasketByUserId(userId);
  }
}
