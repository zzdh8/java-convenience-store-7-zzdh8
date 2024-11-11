package store;

import store.controller.PaymentController;

public class Application {
    public static void main(String[] args) {
        PaymentController paymentController = new PaymentController();
        paymentController.run();
    }
}
