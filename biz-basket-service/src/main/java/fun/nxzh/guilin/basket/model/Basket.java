package fun.nxzh.guilin.basket.model;

import java.util.List;

public class Basket {
  private String buyerId;
  private List<BasketItem> items;

  public String getBuyerId() {
    return buyerId;
  }

  public void setBuyerId(String buyerId) {
    this.buyerId = buyerId;
  }

  public List<BasketItem> getItems() {
    return items;
  }

  public void setItems(List<BasketItem> items) {
    this.items = items;
  }
}
