package fun.nxzh.guilin.facade.model;

import java.time.LocalDateTime;

public class BasketCheckout {

  private String city;
  private String street;
  private String state;
  private String country;
  private String zipCode;
  private String cardNumber;
  private String cardHolderName;
  private LocalDateTime cardExpiration;
  private String cardSecurityNumber;
  private int cardTypeId;
  private String buyer;
  private String requestId;

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public String getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
  }

  public String getCardHolderName() {
    return cardHolderName;
  }

  public void setCardHolderName(String cardHolderName) {
    this.cardHolderName = cardHolderName;
  }

  public LocalDateTime getCardExpiration() {
    return cardExpiration;
  }

  public void setCardExpiration(LocalDateTime cardExpiration) {
    this.cardExpiration = cardExpiration;
  }

  public String getCardSecurityNumber() {
    return cardSecurityNumber;
  }

  public void setCardSecurityNumber(String cardSecurityNumber) {
    this.cardSecurityNumber = cardSecurityNumber;
  }

  public int getCardTypeId() {
    return cardTypeId;
  }

  public void setCardTypeId(int cardTypeId) {
    this.cardTypeId = cardTypeId;
  }

  public String getBuyer() {
    return buyer;
  }

  public void setBuyer(String buyer) {
    this.buyer = buyer;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }
}
