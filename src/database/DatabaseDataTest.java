package database;
import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class DatabaseDataTest {
    private Map<String, User> users;
    private Map<Integer, Item> items;
    private List<Message> messages;
    private List<Transaction> transactions;
    private DatabaseData databaseData;
    
    @Before
    public void setUp() {
        users = new HashMap<>();
        users.put("user1", new User("user1", "password1"));
        
        items = new HashMap<>();
        Item item = new Item("Item1", "Description1", 10.0, "user1");
        item.setId(1);
        items.put(1, item);
        
        messages = new ArrayList<>();
        messages.add(new Message("user1", "user2", "Hello", 1));
        
        transactions = new ArrayList<>();
        transactions.add(new Transaction("user2", "user1", 1, 10.0));
        
        databaseData = new DatabaseData(users, items, messages, transactions, 2);
    }
    
    @Test
    public void testGetUsers() {
        Map<String, User> result = databaseData.getUsers();
        assertNotNull("Users map should not be null", result);
        assertEquals("Users map should contain 1 user", 1, result.size());
        assertTrue("Users map should contain user1", result.containsKey("user1"));
    }
    
    @Test
    public void testGetItems() {
        Map<Integer, Item> result = databaseData.getItems();
        assertNotNull("Items map should not be null", result);
        assertEquals("Items map should contain 1 item", 1, result.size());
        assertTrue("Items map should contain item with ID 1", result.containsKey(1));
        
        Item item = result.get(1);
        assertEquals("Item title should match", "Item1", item.getTitle());
    }
    
    @Test
    public void testGetMessages() {
        List<Message> result = databaseData.getMessages();
        assertNotNull("Messages list should not be null", result);
        assertEquals("Messages list should contain 1 message", 1, result.size());
        assertEquals("Message content should match", "Hello", result.get(0).getContent());
    }
    
    @Test
    public void testGetTransactions() {
        List<Transaction> result = databaseData.getTransactions();
        assertNotNull("Transactions list should not be null", result);
        assertEquals("Transactions list should contain 1 transaction", 1, result.size());
        assertEquals("Transaction buyer should match", "user2", result.get(0).getBuyer());
    }
    
    @Test
    public void testGetNextItemId() {
        int nextItemId = databaseData.getNextItemId();
        assertEquals("Next item ID should be 2", 2, nextItemId);
    }
}
