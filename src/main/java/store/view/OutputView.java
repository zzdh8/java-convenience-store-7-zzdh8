package store.view;

import store.domain.Product;

public class OutputView {

  private static final String OUT_OF_STOCK = "재고 없음";
  private static final String WELOCOME_MESSAGE = "안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n";
  private static final String STR_NULL = "null";

  public void printWelcomeMessage() {
    System.out.println(WELOCOME_MESSAGE);
  }

  private String formatPrice(int price) {
    return String.format("%,d", price);
  }

  public void printProductList(Product product) {
    if (product.getQuantity() != 0 && !product.getPromotion().getName().equals(STR_NULL)) {
      System.out.printf("- %s %s원 %d개 %s\n", product.getName(), formatPrice(product.getPrice()), product.getQuantity(),
              product.getPromotion().getName());
    }
    if (product.getQuantity() == 0 && !product.getPromotion().getName().equals(STR_NULL)) {
      System.out.printf("- %s %s원 %s %s\n", product.getName(), formatPrice(product.getPrice()), OUT_OF_STOCK,
              product.getPromotion().getName());
    }
    if (product.getQuantity() != 0 && product.getPromotion().getName().equals(STR_NULL)) {
      System.out.printf("- %s %s원 %d개\n", product.getName(), formatPrice(product.getPrice()), product.getQuantity());
    }
    if (product.getQuantity() == 0 && product.getPromotion().getName().equals(STR_NULL)) {
      System.out.printf("- %s %s원 %s\n", product.getName(), formatPrice(product.getPrice()), OUT_OF_STOCK);
    }
  }
}
