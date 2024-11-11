package store.domain.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import store.domain.Product;
import store.domain.Promotion;

public class DataLoader {

  private final ProductsRepository productsRepository;
  private final PromotionRepository promotionsRepository;
  private final SimpleDateFormat format;

  private static final int NAME_INDEX = 0;
  private static final int PRICE_INDEX = 1;
  private static final int QUANTITY_INDEX = 2;
  private static final int PROMOTION_INDEX = 3;
  private static final String PRODUCT_FILE_PATH = "src\\main\\resources\\products.md";
  private static final String PROMOTION_FILE_PATH = "src\\main\\resources\\promotions.md";
  private static final String DATE_FORMAT = "yyyy-MM-dd";
  private static final String DIVIDER = ",";

  public DataLoader(ProductsRepository productsRepository, PromotionRepository promotionsRepository) {
    format = new SimpleDateFormat(DATE_FORMAT);
    this.productsRepository = productsRepository;
    this.promotionsRepository = promotionsRepository;
    loadPromotionsFromFile();
    loadProductsFromFile();
  }

  private void loadPromotionsFromFile() {
    try {
      List<String> lines = readLinesFromFile(PROMOTION_FILE_PATH);
      List<Promotion> promotions = parsePromotions(lines);
      for (Promotion promotion : promotions) {
        promotionsRepository.add(promotion);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void loadProductsFromFile() {
    try {
      List<String> lines = readLinesFromFile(PRODUCT_FILE_PATH);
      List<Product> products = parseProducts(lines);
      for (Product product : products) {
        productsRepository.add(product);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private List<String> readLinesFromFile(String filePath) throws IOException {
    Path path = Paths.get(filePath);
    return Files.readAllLines(path);
  }

  private List<Promotion> parsePromotions(List<String> lines) {
    List<Promotion> promotions = new ArrayList<>();
    extractPromotions(lines, promotions);
    return promotions;
  }

  private void extractPromotions(List<String> lines, List<Promotion> promotions) {
    for (String line : lines.subList(1, lines.size())) {
      String[] parts = line.split(DIVIDER);
      String name = parts[0];
      int buy = Integer.parseInt(parts[1]);
      int get = Integer.parseInt(parts[2]);
      Date startDate = parseDate(parts[3]);
      Date endDate = parseDate(parts[4]);
      promotions.add(new Promotion(name, buy, get, startDate, endDate));
    }
  }

  private List<Product> parseProducts(List<String> lines) {
    List<Product> products = new ArrayList<>();
    extractProduct(lines, products);
    return products;
  }

  private void extractProduct(List<String> lines, List<Product> products) {
    for (String line : lines.subList(1, lines.size())) {
      String[] parts = line.split(DIVIDER);
      String name = parts[NAME_INDEX];
      int price = Integer.parseInt(parts[PRICE_INDEX]);
      int quantity = Integer.parseInt(parts[QUANTITY_INDEX]);
      Promotion promotion = promotionsRepository.findByName(parts[PROMOTION_INDEX]);
      products.add(new Product(name, price, quantity, promotion));
    }
  }

  private Date parseDate(String date) {
    try {
      return format.parse(date);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    throw new IllegalArgumentException("유효하지 않은 날짜 형식입니다.");
  }
}