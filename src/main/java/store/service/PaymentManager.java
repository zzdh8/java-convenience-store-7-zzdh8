package store.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.domain.MemberShip;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.Receipt;
import store.domain.repository.ProductsRepository;
import store.domain.repository.PromotionRepository;

public class PaymentManager {

  private final Map<String, Integer> products;

  private final ProductsRepository productsRepository;
  private final PromotionRepository promotionRepository;
  private static final String REGEX = "\\[(.+?)-(.+?)]";

  public PaymentManager(ProductsRepository productsRepository,
                        PromotionRepository promotionRepository) {
    products = new LinkedHashMap<>();
    this.productsRepository = productsRepository;
    this.promotionRepository = promotionRepository;
  }

  public void getPurchasingProducts(String input) {
    Pattern pattern = Pattern.compile(REGEX);
    Matcher matcher = pattern.matcher(input);
    putProductMap(matcher);
  }

  public void modifyProducts(String productName) {
    products.put(productName, products.get(productName) + 1);
  }

  public Map<String, Integer> getProducts() {
    return products;
  }

  public void refreshProducts() {
    products.clear();
  }

  private void putProductMap(Matcher matcher) {
    while (matcher.find()) {
      String name = matcher.group(1);
      int quantity = Integer.parseInt(matcher.group(2));
      checkStock(name);
      checkExcessStock(name, quantity);
      products.put(name, quantity);
    }
  }

  private void checkStock(String name) {
    Product product = productsRepository.findPromotionByName(name);
    if (product == null || product.getQuantity() == 0) {
      product = productsRepository.findGeneralByName(name);
    }
    if (product.getQuantity() == 0) {
      throw new IllegalArgumentException("[ERROR] 구매한 상품 중 하나의 재고가 0개입니다. 다른 상품을 입력해주세요.");
    }
  }

  private void checkExcessStock(String name, int quantity) {
    Product product = productsRepository.findPromotionByName(name);
    if (product == null || product.getQuantity() == 0) {
      product = productsRepository.findGeneralByName(name);
    }
    if (product.getQuantity() < quantity) {
      throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
    }
  }

  
  public boolean isPromotionAvailable(String name) {
    Product product = productsRepository.findPromotionByName(name);
    return product != null && product.isPromotion();
  }

  public boolean isPromotionQuantityAvailable(String name) {
    Product product = productsRepository.findPromotionByName(name);
    return product.getQuantity() >= products.get(name);
  }

  public boolean isNotEnoughPromotionQuantity(Product product, int quantity) {
    Promotion promotion = product.getPromotion();
    int buy = promotion.getBuy();
    int get = promotion.getGet();
    while (quantity > 0) {
      quantity -= buy;
      if (quantity <= 0) return true;
      quantity -= get;
    }
    return false;
  }

  public int calculateFreeQuantity(Product product, int quantity) {
    Promotion promotion = product.getPromotion();
    int freequantity = (quantity / promotion.getBuy()) * promotion.getGet();
    if (freequantity == quantity) {
      return 0;
    }
    return freequantity;
  }

  public boolean checkExcessPromotionQuantity(String productName) {
    Product product = productsRepository.findPromotionByName(productName);
    int quantityForPurchase = products.get(productName);
    return product.getQuantity() < quantityForPurchase;
  }

  public int calculateExcessPromotionQuantity(String productName) {
    Product product = productsRepository.findPromotionByName(productName);
    int buy = product.getPromotion().getBuy();
    int get = product.getPromotion().getGet();
    int quantityForPurchase = products.get(productName);
    int quantityStock = product.getQuantity();

    while (quantityForPurchase >= buy && quantityStock >= buy) {
      quantityForPurchase -= buy;
      quantityStock -= buy;
      if (quantityStock < get) {
        break;
      }
      quantityStock -= get;
    }
    return quantityForPurchase;
  }

  public Receipt applyMembershipDiscount(Receipt receiptInWrite, boolean applyMembershipDiscount) {
    boolean isGeneralProduct = receiptInWrite.getPurchasedProducts().stream()
        .anyMatch(product -> !product.isPromotion());
    if (applyMembershipDiscount && receiptInWrite.getPromotionDiscount() == 0 && !isGeneralProduct) {
      int membershipDiscount = MemberShip.calculateDiscount(receiptInWrite.getTotalAmount());
      receiptInWrite.setMembershipDiscount(membershipDiscount);
    }
    return receiptInWrite;
  }


  public void updateStock() {
    for (String productName : products.keySet()) {
      int purchaseQuantity = products.get(productName);

      Product promotionProduct = productsRepository.findPromotionByName(productName);
      if (productsRepository.existPromotion(productName)) {
        while (purchaseQuantity > 0 && promotionProduct.getQuantity() > 0) {
          promotionProduct.sellOne();
          purchaseQuantity -= 1;
        }
        productsRepository.updateProductQuantity(promotionProduct, promotionProduct.getQuantity());
      }
      Product generalProduct = productsRepository.findGeneralByName(productName);
      if (purchaseQuantity > 0) {
        while (purchaseQuantity > 0 && generalProduct.getQuantity() > 0) {
          generalProduct.sellOne();
          purchaseQuantity -= 1;
        }
        productsRepository.deleteProduct(generalProduct);
      }
    }
  }

}
