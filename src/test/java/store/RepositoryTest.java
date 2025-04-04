package store;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.repository.DataLoader;
import store.domain.repository.ProductsRepository;
import store.domain.repository.PromotionRepository;

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
  void 파일에서_데이터를_추출해서_저장됐는지_출력_및_객체로_확인한다() {
    // given
    Product cola;
    Promotion md_recommend;

    // when
    cola = productsRepository.findGeneralByName("콜라");
    md_recommend = promotionRepository.findByName("MD추천상품");
    promotionRepository.getAllPromotions().forEach(s -> System.out.println(s.getName() + " : " + s.getBuy()));
    productsRepository.getProducts().forEach(s -> System.out.println(s.getName() + " : " + s.getQuantity()+ " : " + s.getPromotion().getName()));

    // then
    Assertions.assertEquals(10, cola.getQuantity());
    Assertions.assertEquals(1, md_recommend.getBuy());
  }
}
