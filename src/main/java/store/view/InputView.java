package store.view;

import static camp.nextstep.edu.missionutils.Console.readLine;

import store.domain.Answer;
import store.domain.Product;

public class InputView {

  private static final String INPUT_PATTERN = "\\[(.+?)-(.+?)]";

  public String readItem() {
    System.out.println("\n구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
    String input = readLine();
    if (!input.matches(INPUT_PATTERN)) {
      throw new IllegalArgumentException("잘못된 입력입니다. 다시 입력해 주세요.");
    }
    return input;
  }

  public String readMemberShipDiscount() {
    System.out.println("\n멤버십 할인을 받으시겠습니까? (Y/N)");
    String input = readLine();
    Answer.check(input);
    return input;
  }

  public boolean readPurchaseAgain() {
    System.out.println("\n감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
    String input = readLine();
    Answer.check(input);
    return Answer.provideValue(input);
  }

  public String readNotPromoDiscount(String productName, int quantityForRegularPrice) {
    System.out.printf("\n현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)", productName, quantityForRegularPrice);
    String input = readLine();
    Answer.check(input);
    if (input.equals(Answer.NO.name())) {
      throw new IllegalArgumentException("다시 입력해 주세요.");
    }
    return input;
  }

  public String readPromoDiscount(Product product) {
    System.out.printf("\n현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)", product.getName(), 1);
    String input = readLine();
    Answer.check(input);
    return input;
  }
}
