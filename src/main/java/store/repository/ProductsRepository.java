package store.repository;

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

  public Product findByName(String name) {
    for (Product product : products) {
      if (product.getName().equals(name)) {
        return product;
      }
    }
    return null;
  }

  public List<Product> getProducts() {
    return products;
  }
}