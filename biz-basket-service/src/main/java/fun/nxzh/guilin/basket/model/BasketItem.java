package fun.nxzh.guilin.basket.model;

import java.math.BigDecimal;

public class BasketItem {
  private String id;
  private String productId;
  private String productName;
  private BigDecimal UnitPrice;
  private BigDecimal oldUnitPrice;
  private Integer Quantity;

  public BasketItem() {}

  public BasketItem(
      String id,
      String productId,
      String productName,
      BigDecimal unitPrice,
      BigDecimal oldUnitPrice,
      Integer quantity) {
    this.id = id;
    this.productId = productId;
    this.productName = productName;
    UnitPrice = unitPrice;
    this.oldUnitPrice = oldUnitPrice;
    Quantity = quantity;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public BigDecimal getUnitPrice() {
    return UnitPrice;
  }

  public void setUnitPrice(BigDecimal unitPrice) {
    UnitPrice = unitPrice;
  }

  public BigDecimal getOldUnitPrice() {
    return oldUnitPrice;
  }

  public void setOldUnitPrice(BigDecimal oldUnitPrice) {
    this.oldUnitPrice = oldUnitPrice;
  }

  public Integer getQuantity() {
    return Quantity;
  }

  public void setQuantity(Integer quantity) {
    Quantity = quantity;
  }
}
