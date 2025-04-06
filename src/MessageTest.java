import static org.junit.Assert.*;
import org.junit.*;
import java.util.Date;

public class MessageTest {
    private Message message;
    private Date beforeCreation;
    
    @Before
    public void setUp() {
        beforeCreation = new Date();
        message = new Message("sender123", "recipient456", "Hello, this is a test message", 789);
    }
    
    @Test
    public void testGetSender() {
        assertEquals("Sender should match", "sender123", message.getSender());
    }
    
    @Test
    public void testGetRecipient() {
        assertEquals("Recipient should match", "recipient456", message.getRecipient());
    }
    
    @Test
    public void testGetContent() {
        assertEquals("Content should match", "Hello, this is a test message", message.getContent());
    }
    
    @Test
    public void testGetTimestamp() {
        Date timestamp = message.getTimestamp();
        assertNotNull("Timestamp should not be null", timestamp);
        
        // Timestamp should be between before creation and now
        assertTrue("Timestamp should be after or equal to beforeCreation", 
            timestamp.compareTo(beforeCreation) >= 0);
        assertTrue("Timestamp should be before or equal to now", 
            timestamp.compareTo(new Date()) <= 0);
    }
    
    @Test
    public void testGetItemId() {
        assertEquals("Item ID should match", 789, message.getItemId());
    }
}
