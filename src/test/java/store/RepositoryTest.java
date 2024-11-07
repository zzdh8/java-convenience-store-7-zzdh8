package store;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.Product;
import store.domain.Promotion;
import store.repository.DataLoader;
import store.repository.ProductsRepository;
import store.repository.PromotionRepository;

public class RepositoryTest {

  private ProductsRepository productsRepository;
  private PromotionRepository promotionRepository;

  @BeforeEach
  void setUp() {
    productsRepository = new ProductsRepository();
    promotionRepository = new PromotionRepository();
    DataLoader dataLoader = new DataLoader(productsRepository, promotionRepository);
  }
  @Test
  void 파일에서_데이터를_추출해서_저장됐는지_확인한다() {
    // given
    Product cola;
    Promotion md_recommend;

    // when
    cola = productsRepository.findByName("콜라");
    md_recommend = promotionRepository.findByName("MD추천상품");

    // then
    Assertions.assertEquals(10, cola.getQuantity());
    Assertions.assertEquals(1, md_recommend.getBuy());
  }
}
