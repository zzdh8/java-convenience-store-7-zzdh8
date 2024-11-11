package store.controller;

import java.util.function.Supplier;
import store.domain.Answer;
import store.domain.Product;
import store.domain.Receipt;
import store.domain.repository.DataLoader;
import store.domain.repository.ProductsRepository;
import store.domain.repository.PromotionRepository;
import store.service.PaymentManager;
import store.view.InputView;
import store.view.OutputView;

public class PaymentController {

  private static ProductsRepository productsRepository;
  private static PromotionRepository promotionRepository;
  private final InputView inputView;
  private final OutputView outputView;
  private final PaymentManager paymentManager;
  private Receipt receipt;

  public PaymentController() {
    productsRepository = new ProductsRepository();
    promotionRepository = new PromotionRepository();
    new DataLoader(productsRepository, promotionRepository);
    this.inputView = new InputView();
    this.outputView = new OutputView();
    this.paymentManager = new PaymentManager(productsRepository, promotionRepository);
  }

  private <T> T doLoop(Supplier<T> inputFunction) {
    while (true) {
      try {
        return inputFunction.get();
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  public void run() {
    boolean isContinue = true;
    outputView.printWelcomeMessage();
    do {
      receipt = new Receipt();
      productsRepository.getProducts().forEach(outputView::printProductList);
      paymentManager.getPurchasingProducts(doLoop(inputView::readItem));
      processPromotionDiscount();
      paymentManager.refreshProducts();
      isContinue = doLoop(inputView::readPurchaseAgain);
    } while (isContinue);
  }

  private void processPromotionDiscount() {
    for (String productName : paymentManager.getProducts().keySet()) {
      if (paymentManager.isPromotionAvailable(productName) && paymentManager.isPromotionQuantityAvailable(productName)) {
        Product productForSearch = productsRepository.findPromotionByName(productName);
        if (productForSearch != null && !paymentManager.isNotEnoughPromotionQuantity(productForSearch, paymentManager.getProducts().get(productForSearch.getName()))) {
          String answer = doLoop(() -> inputView.readPromoDiscount(productForSearch));
          if (Answer.provideValue(answer)) {
            paymentManager.modifyProducts(productForSearch.getName());
          }
        }
        int freeQuantity = paymentManager.calculateFreeQuantity(productForSearch, paymentManager.getProducts().get(productForSearch.getName()));
        writeFreeProductsToReceipt(productForSearch, freeQuantity);
      }
      if (paymentManager.isPromotionAvailable(productName) && paymentManager.checkExcessPromotionQuantity(productName)) {
        Product productForSearch = productsRepository.findPromotionByName(productName);
        if (productForSearch != null) {
          int quantityForRegularPrice = paymentManager.calculateExcessPromotionQuantity(productName);
          String answer = doLoop(() -> inputView.readNotPromoDiscount(productName, quantityForRegularPrice));
          if (Answer.provideValue(answer)) {
            int freeQuantity = paymentManager.calculateFreeQuantity(productForSearch, quantityForRegularPrice);
            writeFreeProductsToReceipt(productForSearch, freeQuantity);
          }
        }
      }
    }
    writePurchaseProductsToReceipt();
    boolean isMembershipDiscount = Answer.provideValue(doLoop(inputView::readMemberShipDiscount));
    receipt = paymentManager.applyMembershipDiscount(receipt, isMembershipDiscount);
    writeFinalPriceToReceipt();
    receipt.printReceipt();
    paymentManager.updateStock();
  }

  private void writePurchaseProductsToReceipt() {
    for (String productName : paymentManager.getProducts().keySet()) {
      Product product = productsRepository.findByNameWhereQuantityIsNotZero(productName);
      receipt.addPurchasedProducts(product);
      receipt.putProductInfo(productName, paymentManager.getProducts().get(productName));
      receipt.addTotalPurchasePrice(product.getPrice() * paymentManager.getProducts().get(productName));
      receipt.addTotalAmount(paymentManager.getProducts().get(productName));
    }
  }

  private void writeFreeProductsToReceipt(Product product, int freeQuantity) {
    receipt.addFreeProducts(new Product(product.getName(), product.getPrice(), freeQuantity, product.getPromotion()));
    receipt.addPromotionDiscount(freeQuantity * product.getPrice());
  }

  private void writeFinalPriceToReceipt() {
    receipt.setFinalPaymentPrice();
  }
}