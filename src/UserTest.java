import static org.junit.Assert.*;
import org.junit.*;

public class UserTest {
    private User user;
    
    @Before
    public void setUp() {
        user = new User("testUser", "password123");
    }
    
    @Test
    public void testGetUsername() {
        assertEquals("Username should match", "testUser", user.getUsername());
    }
    
    @Test
    public void testCheckPassword_Correct() {
        assertTrue("Correct password should return true", user.checkPassword("password123"));
    }
    
    @Test
    public void testCheckPassword_Incorrect() {
        assertFalse("Incorrect password should return false", user.checkPassword("wrongPassword"));
    }
    
    @Test
    public void testGetBalance_InitialValue() {
        assertEquals("Initial balance should be 100.0", 100.0, user.getBalance(), 0.001);
    }
    
    @Test
    public void testSetBalance() {
        user.setBalance(250.0);
        assertEquals("Balance should be updated", 250.0, user.getBalance(), 0.001);
        
        user.setBalance(0.0);
        assertEquals("Balance should be updated to zero", 0.0, user.getBalance(), 0.001);
        
        user.setBalance(-50.0);
        assertEquals("Balance should be updated to negative", -50.0, user.getBalance(), 0.001);
    }
}
