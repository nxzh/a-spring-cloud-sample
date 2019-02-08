package fun.nxzh.guilin.basket.event;

import fun.nxzh.guilin.basket.model.Basket;
import fun.nxzh.guilin.basket.model.BasketCheckout;

public class UserCheckoutAccepted {
  private String userId;
  private String orderId;
  private BasketCheckout checkout;
  private Basket basket;

  public UserCheckoutAccepted(
      String userId, String orderId, BasketCheckout checkout, Basket basket) {
    this.userId = userId;
    this.orderId = orderId;
    this.checkout = checkout;
    this.basket = basket;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public BasketCheckout getCheckout() {
    return checkout;
  }

  public void setCheckout(BasketCheckout checkout) {
    this.checkout = checkout;
  }

  public Basket getBasket() {
    return basket;
  }

  public void setBasket(Basket basket) {
    this.basket = basket;
  }
}
