package store.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Receipt {

  private final List<Product> purchasedProducts;
  private final Map<String, Integer> productInfo;
  private final List<Product> freeProducts;

  private int totalAmount;

  private int totalPurchasePrice;

  private int promotionDiscount;
  private int membershipDiscount;
  private int finalPaymentPrice;

  public Receipt() {
    productInfo = new LinkedHashMap<>();
    purchasedProducts = new ArrayList<>();
    freeProducts = new ArrayList<>();
  }

  public List<Product> getPurchasedProducts() {
    return purchasedProducts;
  }

  private int getTotalQuantity() {
    return productInfo.values().stream()
            .mapToInt(Integer::intValue)
            .sum();
  }

  public int getTotalAmount() {
    return totalAmount;
  }

  public int getPromotionDiscount() {
    return promotionDiscount;
  }

  public void addPurchasedProducts(Product product) {
    purchasedProducts.add(product);
  }

  public void addFreeProducts(Product product) {
    freeProducts.add(product);
  }

  public void addTotalAmount(int totalAmount) {
    this.totalAmount += totalAmount;
  }

  public void setFinalPaymentPrice() {
    this.finalPaymentPrice = totalPurchasePrice - promotionDiscount - membershipDiscount;
  }

  public void addPromotionDiscount(int promotionDiscount) {
    this.promotionDiscount += promotionDiscount;
  }

  public void setMembershipDiscount(int membershipDiscount) {
    this.membershipDiscount = membershipDiscount;
  }

  public void putProductInfo(String productName, int quantity) {
    productInfo.put(productName, quantity);
  }

  private String formatPrice(int price) {
    return String.format("%,d", price);
  }

  public void addTotalPurchasePrice(int price) {
    this.totalPurchasePrice += price;
  }

  public void printReceipt() {
    System.out.println("==============W 편의점================");
    System.out.println("상품명\t\t수량\t\t금액");

    for (Product product : purchasedProducts) {
      System.out.printf("%s\t\t\t%s\t\t%s\n", product.getName(), productInfo.get(product.getName()), formatPrice(productInfo.get(product.getName()) * product.getPrice()));
    }

    System.out.println("===============증\t정=================");
    for (Product product : freeProducts) {
      System.out.printf("%s\t\t\t%d\n", product.getName(), product.getQuantity());
    }

    System.out.println("=====================================");
    System.out.printf("총구매액\t\t%d\t\t%s\n", getTotalQuantity(), formatPrice(totalPurchasePrice));
    System.out.printf("행사할인\t\t\t\t-%s\n", formatPrice(promotionDiscount));
    System.out.printf("멤버십할인\t\t\t-%s\n", formatPrice(membershipDiscount));
    System.out.printf("내실돈\t\t\t\t%s\n", formatPrice(finalPaymentPrice));
  }
}
