package store.repository;

import java.util.ArrayList;
import java.util.List;
import store.domain.Promotion;

public class PromotionRepository {
  private final List<Promotion> promotions;

  public PromotionRepository() {
    this.promotions = new ArrayList<>();
  }

  public void add(Promotion promotion) {
    promotions.add(promotion);
  }

  public Promotion findByName(String name) {
    for (Promotion promotion : promotions) {
      if (promotion.getName().equals(name)) {
        return promotion;
      }
    }
    return null;
  }

  public List<Promotion> getAllPromotions() {
    return new ArrayList<>(promotions);
  }
}