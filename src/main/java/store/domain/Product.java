package store.domain;

import java.util.Objects;

public class Product {
  private final String name;
  private final int price;
  private int quantity;
  private final Promotion promotion;

  public Product(String name, int price, int quantity, Promotion promotion) {
    this.name = name;
    this.price = price;
    this.quantity = quantity;
    this.promotion = promotion;
  }

  public String getName() {
    return name;
  }

  public int getPrice() {
    return price;
  }

  public int getQuantity() {
    return quantity;
  }

  public Promotion getPromotion(){
    return promotion;
  }

  public boolean isPromotion() {
    return !Objects.equals(promotion.getName(), "null");
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public void sellOne() {
    this.quantity -= 1;
  }
}
