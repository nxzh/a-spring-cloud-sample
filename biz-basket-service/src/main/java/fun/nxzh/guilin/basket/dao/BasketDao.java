package fun.nxzh.guilin.basket.dao;

import fun.nxzh.guilin.basket.model.Basket;

public interface BasketDao {
  Basket findBasketByUserId(String userId);
}
