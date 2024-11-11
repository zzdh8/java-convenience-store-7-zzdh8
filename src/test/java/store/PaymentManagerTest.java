package store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.service.PaymentManager;

public class PaymentManagerTest {

  private PaymentManager paymentManager;

  @BeforeEach
  void setUp() {
    paymentManager = new PaymentManager();
  }
  @Test
  void 구매상품과_수량을_입력을_제대로_받았는지_확인한다() {
  }
}
