package store.domain.repository;

import java.util.ArrayList;
import java.util.List;
import store.domain.Product;

public class ProductsRepository {
  private final List<Product> products;

  public ProductsRepository() {
    this.products = new ArrayList<>();
  }

  public void add(Product product) {
    products.add(product);
  }

  public Product findGeneralByName(String name) {
    for (Product product : products) {
      if (product.getName().equals(name) && product.getPromotion().getName().equals("null")) {
        return product;
      }
    }
    return null;
  }

  public Product findPromotionByName(String name) {
    for (Product product : products) {
      if (product.getName().equals(name) && product.isPromotion()) {
        return product;
      }
    }
    return null;
  }

  public Product findByNameWhereQuantityIsNotZero(String name) {
    for (Product product : products) {
      if (product.getName().equals(name) && product.getQuantity() > 0) {
        return product;
      }
    }
    return null;
  }

  public List<Product> getProducts() {
    return products;
  }

  public boolean existPromotion(String productName) {
    return products.stream()
            .anyMatch(product -> product.getName().equals(productName) && product.isPromotion());
  }

  public void updateProductQuantity(Product product, int quantity) {
    products.stream()
            .filter(p -> p.getName().equals(product.getName()) && p.getPromotion().getName().equals(product.getPromotion().getName()))
            .findFirst()
            .ifPresent(p -> p.setQuantity(quantity));
  }

  public void deleteProduct(Product product) {
    products.remove(product);
  }
}