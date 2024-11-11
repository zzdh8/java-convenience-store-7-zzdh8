package store.domain;

public enum Answer {

  YES("Y", true),
  NO("N", false);

  private final String message;
  private final boolean value;

  Answer(String message, boolean value) {
    this.message = message;
    this.value = value;
  }

  public static void check(String answer) {
    if (answer.equals(YES.message) || answer.equals(NO.message)) {
      return;
    }
    throw new IllegalArgumentException("잘못된 입력입니다.");
  }

  public static boolean provideValue(String answer) {
    if (answer.equals("Y")) {
      return Answer.YES.value;
    }
    return Answer.NO.value;
  }
}
