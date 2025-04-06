import static org.junit.Assert.*;
import org.junit.*;

public class TransactionResultTest {
    private TransactionResult successResult;
    private TransactionResult failureResult;
    private TransactionResult resultWithItemId;
    
    @Before
    public void setUp() {
        successResult = new TransactionResult(true, "Success message");
        failureResult = new TransactionResult(false, "Error message");
        resultWithItemId = new TransactionResult(true, "Success with item", 123);
    }
    
    @Test
    public void testIsSuccess() {
        assertTrue("Success result should return true", successResult.isSuccess());
        assertFalse("Failure result should return false", failureResult.isSuccess());
        assertTrue("Result with item ID should return true", resultWithItemId.isSuccess());
    }
    
    @Test
    public void testGetMessage() {
        assertEquals("Success message should match", "Success message", successResult.getMessage());
        assertEquals("Error message should match", "Error message", failureResult.getMessage());
        assertEquals("Success with item message should match", "Success with item", resultWithItemId.getMessage());
    }
    
    @Test
    public void testGetItemId() {
        assertEquals("Default item ID should be -1", -1, successResult.getItemId());
        assertEquals("Default item ID should be -1", -1, failureResult.getItemId());
        assertEquals("Item ID should match", 123, resultWithItemId.getItemId());
    }
}
