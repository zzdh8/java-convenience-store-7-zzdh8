package store.repository;

import java.util.LinkedHashMap;
import java.util.Map;
import store.domain.Product;

public class ProductsRepository {
  private final Map<String, Product> products;

  public ProductsRepository() {
    this.products = new LinkedHashMap<>();
  }

  public void add(Product product) {
    products.put(product.getName(), product);
  }

  public Product findByName(String name) {
    return products.get(name);
  }
}
