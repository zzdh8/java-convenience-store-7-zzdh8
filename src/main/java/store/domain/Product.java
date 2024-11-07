package store.domain;

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

  public String getNameWithoutPromo() {
    return name.substring(0,name.length()-2);
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

  public void sell(int quantity) {
    this.quantity -= quantity;
  }
}
