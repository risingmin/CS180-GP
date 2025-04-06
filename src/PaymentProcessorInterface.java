public interface PaymentProcessorInterface {
    TransactionResult processPayment(String buyerUsername, String sellerUsername, int itemId);
    boolean hasSufficientFunds(String username, double amount);
}