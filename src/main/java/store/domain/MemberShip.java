package store.domain;

public class MemberShip {

  private static final int MAX_DISCOUNT = 8000;
  private static final double DISCOUNT_RATE = 0.3;

  public static int calculateDiscount(int price) {
    int discount = (int) (price * DISCOUNT_RATE);
    return Math.min(discount, MAX_DISCOUNT);
  }

}