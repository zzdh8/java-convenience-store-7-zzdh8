package store.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import store.domain.Product;
import store.domain.Promotion;

public class ProductLoader {
  private final ProductsRepository productsRepository;

  private static final int NAME_INDEX = 0;
  private static final int PRICE_INDEX = 1;
  private static final int QUANTITY_INDEX = 2;
  private static final int PROMOTION_INDEX = 3;
  private static final String FILE_PATH = "src\\main\\resources\\products.md";

  public ProductLoader(ProductsRepository productsRepository) {
    this.productsRepository = productsRepository;
    loadProductsFromFile();
  }

  private void loadProductsFromFile() {
    try {
      List<String> lines = readLinesFromFile();
      List<Product> products = parseProducts(lines);
      for (Product product : products) {
        productsRepository.add(product);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private List<String> readLinesFromFile() throws IOException {
    Path path = Paths.get(ProductLoader.FILE_PATH);
    return Files.readAllLines(path);
  }

  private List<Product> parseProducts(List<String> lines) {
    List<Product> products = new ArrayList<>();
    extractProductsInfo(lines.subList(1, lines.size()), products);
    return products;
  }

  private void extractProductsInfo(List<String> lines, List<Product> products) {
    for (String line : lines) {
      String[] parts = line.split(",");
      String name = isPromo(parts[PROMOTION_INDEX], parts[NAME_INDEX]);
      int price = Integer.parseInt(parts[PRICE_INDEX]);
      int quantity = Integer.parseInt(parts[QUANTITY_INDEX]);
      Promotion promotion = Promotion.getPromo(parts[PROMOTION_INDEX]);
      products.add(new Product(name, price, quantity, promotion));
    }
  }

  private String isPromo(String promo, String name) {
    if (!promo.equals("null")) {
      return name += "_P";
    }
    return name;
  }

}