package store.domain;

public enum Promotion {
  ONE_PLUS_ONE_FOR_YEAR("MD추천상품",1),
  ONE_PLUS_ONE_FOR_MONTH("반짝할인",2),
  TWO_PLUS_ONE("탄산2+1",3),
  NO_PROMOTION("null",0);

  private final String title;
  private final int code;

  Promotion(String title, int code) {
    this.title = title;
    this.code = code;
  }

   public static Promotion getPromo(String promoTitle) {
     if (promoTitle.equals("MD추천상품")) return ONE_PLUS_ONE_FOR_YEAR;
     if (promoTitle.equals("반짝할인")) return ONE_PLUS_ONE_FOR_MONTH;
     if (promoTitle.equals("탄산2+1")) return TWO_PLUS_ONE;
     if (promoTitle.equals("null")) return NO_PROMOTION;
     throw new IllegalArgumentException("해당 프로모션은 존재하지 않습니다.");
   }
}
