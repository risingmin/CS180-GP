package database;
import java.util.ArrayList;
import java.util.List;

/**
 * PaymentProcessor class that implements PaymentProcessorInterface
 *
 * This class handles the payment processing between buyers and sellers,
 * including checking user balances, processing transactions, and saving data.
 * It interacts with the Database class to manage user and item data.
 * 
 * @author L10-Team1 
 *
 * @version April 2024
 *
 */
public class PaymentProcessor implements PaymentProcessorInterface {
    private Database database; // Database object to interact with user and item data
    
    public PaymentProcessor(Database database) {
        this.database = database;
    }
    
    /**
     * Process a payment from a buyer to a seller for an item
     *
     * @param buyerUsername The username of the buyer
     * @param sellerUsername The username of the seller
     * @param itemId The ID of the item being purchased
     * @return TransactionResult containing success status and message
     */
    @Override
    public TransactionResult processPayment(String buyerUsername, String sellerUsername, int itemId) {
        // Get the user objects
        User buyer = database.getUserByUsername(buyerUsername);
        User seller = database.getUserByUsername(sellerUsername);
        
        // Check if users exist
        if (buyer == null) {
            return new TransactionResult(false, "Buyer not found");
        }
        if (seller == null) {
            return new TransactionResult(false, "Seller not found");
        }
        
        // Get the item
        Item item = database.getItemById(itemId);
        if (item == null) {
            return new TransactionResult(false, "Item not found");
        }
        
        // Check if the item has already been sold
        if (item.isSold()) {
            return new TransactionResult(false, "Item is already sold");
        }
        
        // Verify that the seller owns the item
        if (!item.getSeller().equals(sellerUsername)) {
            return new TransactionResult(false, "Seller doesn't own this item");
        }
        
        double price = item.getPrice();
        
        // Check if buyer has sufficient funds
        if (!hasSufficientFunds(buyerUsername, price)) {
            return new TransactionResult(false, "Insufficient funds");
        }
        
        // Process payment - deduct from buyer
        buyer.setBalance(buyer.getBalance() - price);
        
        // Add to seller
        seller.setBalance(seller.getBalance() + price);
        
        // Mark item as sold
        item.setSold(true);
        
        // Create transaction record
        Transaction transaction = new Transaction(buyerUsername, sellerUsername, itemId, price);
        
        // Add to the database (removed local transactions list since it's redundant)
        database.addTransaction(transaction);
        
        // Save changes to disk
        database.saveToDisk();
        
        System.out.println("Transaction created: ID=" + transaction.getId() + 
                           ", Buyer=" + transaction.getBuyer() + 
                           ", Seller=" + transaction.getSeller() + 
                           ", ItemId=" + transaction.getItemId());
        
        return new TransactionResult(true, "Payment processed successfully", itemId);
    }
    
    /**
     * Check if a user has sufficient funds for a purchase
     * 
     * @param username username of the user
     * @param amount amount to compare
     * @return true if sufficient funds, false otherwise
     */
    @Override
    public boolean hasSufficientFunds(String username, double amount) {
        User user = database.getUserByUsername(username);
        if (user == null) {
            return false;
        }
        
        return user.getBalance() >= amount;
    }
    
    /**
     * Return all transactions specific to a user
     * 
     * @param username username of the user
     * @return userTransactions list of transactions for the user (buyer or seller)
     */
    public List<Transaction> getTransactionsByUser(String username) {
        // Delegate to the database rather than maintaining a separate list
        return database.getTransactionsForUser(username);
    }
}
