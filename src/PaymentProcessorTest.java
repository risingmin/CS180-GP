import static org.junit.Assert.*;
import org.junit.*;
import java.util.List;

public class PaymentProcessorTest {
    private Database database;
    private PaymentProcessor paymentProcessor;
    private User buyer;
    private User seller;
    private Item item;
    
    @Before
    public void setUp() {
        database = new Database();
        paymentProcessor = new PaymentProcessor(database);
        
        // Create test users
        buyer = new User("buyer", "password");
        buyer.setBalance(200.0);
        seller = new User("seller", "password");
        seller.setBalance(100.0);
        
        // Create test item
        item = new Item("Test Item", "This is a test item", 50.0, "seller");
        
        // Add to database
        database.addUser(buyer);
        database.addUser(seller);
        database.addItem(item);
    }
    
    @Test
    public void testHasSufficientFunds_Sufficient() {
        assertTrue("Buyer should have sufficient funds", 
            paymentProcessor.hasSufficientFunds("buyer", 50.0));
    }
    
    @Test
    public void testHasSufficientFunds_Insufficient() {
        assertFalse("Buyer should not have sufficient funds", 
            paymentProcessor.hasSufficientFunds("buyer", 300.0));
    }
    
    @Test
    public void testHasSufficientFunds_UserNotFound() {
        assertFalse("Non-existent user should not have sufficient funds", 
            paymentProcessor.hasSufficientFunds("nonexistent", 50.0));
    }
    
    @Test
    public void testProcessPayment_Success() {
        double buyerInitialBalance = buyer.getBalance();
        double sellerInitialBalance = seller.getBalance();
        
        TransactionResult result = paymentProcessor.processPayment("buyer", "seller", item.getId());
        
        assertTrue("Payment should succeed", result.isSuccess());
        assertEquals("Item ID should match", item.getId(), result.getItemId());
        assertTrue("Item should be marked as sold", item.isSold());
        
        // Check balances
        assertEquals("Buyer's balance should decrease", 
            buyerInitialBalance - item.getPrice(), buyer.getBalance(), 0.001);
        assertEquals("Seller's balance should increase", 
            sellerInitialBalance + item.getPrice(), seller.getBalance(), 0.001);
    }
    
    @Test
    public void testProcessPayment_BuyerNotFound() {
        TransactionResult result = paymentProcessor.processPayment("nonexistent", "seller", item.getId());
        
        assertFalse("Payment should fail", result.isSuccess());
        assertEquals("Error message should match", "Buyer not found", result.getMessage());
    }
    
    @Test
    public void testProcessPayment_SellerNotFound() {
        TransactionResult result = paymentProcessor.processPayment("buyer", "nonexistent", item.getId());
        
        assertFalse("Payment should fail", result.isSuccess());
        assertEquals("Error message should match", "Seller not found", result.getMessage());
    }
    
    @Test
    public void testProcessPayment_ItemNotFound() {
        TransactionResult result = paymentProcessor.processPayment("buyer", "seller", 9999);
        
        assertFalse("Payment should fail", result.isSuccess());
        assertEquals("Error message should match", "Item not found", result.getMessage());
    }
    
    @Test
    public void testProcessPayment_ItemAlreadySold() {
        // Mark item as sold
        item.setSold(true);
        
        TransactionResult result = paymentProcessor.processPayment("buyer", "seller", item.getId());
        
        assertFalse("Payment should fail", result.isSuccess());
        assertEquals("Error message should match", "Item is already sold", result.getMessage());
    }
    
    @Test
    public void testProcessPayment_WrongSeller() {
        // Change item seller
        Item newItem = new Item("New Item", "This is a new item", 50.0, "otherSeller");
        database.addItem(newItem);
        
        TransactionResult result = paymentProcessor.processPayment("buyer", "seller", newItem.getId());
        
        assertFalse("Payment should fail", result.isSuccess());
        assertEquals("Error message should match", "Seller doesn't own this item", result.getMessage());
    }
    
    @Test
    public void testProcessPayment_InsufficientFunds() {
        // Set buyer balance to less than item price
        buyer.setBalance(10.0);
        
        TransactionResult result = paymentProcessor.processPayment("buyer", "seller", item.getId());
        
        assertFalse("Payment should fail", result.isSuccess());
        assertEquals("Error message should match", "Insufficient funds", result.getMessage());
    }
    
    @Test
    public void testGetTransactionsByUser() {
        // Process a payment to create a transaction
        paymentProcessor.processPayment("buyer", "seller", item.getId());
        
        // Test getting buyer transactions
        List<Transaction> buyerTransactions = paymentProcessor.getTransactionsByUser("buyer");
        assertNotNull("Buyer transactions should not be null", buyerTransactions);
        assertEquals("Buyer should have 1 transaction", 1, buyerTransactions.size());
        assertEquals("Transaction buyer should be buyer", "buyer", buyerTransactions.get(0).getBuyer());
        
        // Test getting seller transactions
        List<Transaction> sellerTransactions = paymentProcessor.getTransactionsByUser("seller");
        assertNotNull("Seller transactions should not be null", sellerTransactions);
        assertEquals("Seller should have 1 transaction", 1, sellerTransactions.size());
        assertEquals("Transaction seller should be seller", "seller", sellerTransactions.get(0).getSeller());
    }
}
