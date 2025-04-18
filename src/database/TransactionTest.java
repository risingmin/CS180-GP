package database;
import static org.junit.Assert.*;
import org.junit.*;
import java.util.Date;

public class TransactionTest {
    private Transaction transaction;
    private Date beforeCreation;
    
    @Before
    public void setUp() {
        beforeCreation = new Date();
        transaction = new Transaction("buyerUser", "sellerUser", 123, 99.99);
    }
    
    @Test
    public void testGetBuyer() {
        assertEquals("Buyer should match", "buyerUser", transaction.getBuyer());
    }
    
    @Test
    public void testGetSeller() {
        assertEquals("Seller should match", "sellerUser", transaction.getSeller());
    }
    
    @Test
    public void testGetItemId() {
        assertEquals("Item ID should match", 123, transaction.getItemId());
    }
    
    @Test
    public void testGetAmount() {
        assertEquals("Amount should match", 99.99, transaction.getAmount(), 0.001);
    }
    
    @Test
    public void testGetTimestamp() {
        Date timestamp = transaction.getTimestamp();
        assertNotNull("Timestamp should not be null", timestamp);
        
        // Timestamp should be between before creation and now
        assertTrue("Timestamp should be after or equal to beforeCreation", 
            timestamp.compareTo(beforeCreation) >= 0);
        assertTrue("Timestamp should be before or equal to now", 
            timestamp.compareTo(new Date()) <= 0);
    }
}
