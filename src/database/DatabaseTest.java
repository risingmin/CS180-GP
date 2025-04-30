package database;
import static org.junit.Assert.*;
import org.junit.*;

import java.io.File;
import java.util.List;

public class DatabaseTest {
    private Database database;
    private User testUser;
    private Item testItem;
    private Message testMessage;
    private Transaction testTransaction;
    
    @Before
    public void setUp() {
        database = new Database();
        testUser = new User("testUser", "password123");
        testItem = new Item("Test Item", "This is a test item", 50.0, "testUser");
        
        // Add user and item to database
        database.addUser(testUser);
        database.addItem(testItem);
        
        // Create test message
        testMessage = new Message("testUser", "anotherUser", "Test message", testItem.getId());
        
        // Create test transaction
        testTransaction = new Transaction("anotherUser", "testUser", testItem.getId(), testItem.getPrice());
    }
    
    @After
    public void tearDown() {
        // Delete the serialized file after tests
        File file = new File("marketplace_data.ser");
        if (file.exists()) {
            file.delete();
        }
    }
    
    @Test
    public void testAddAndGetUserByUsername() {
        User user = database.getUserByUsername("testUser");
        assertNotNull("User should not be null", user);
        assertEquals("Username should match", "testUser", user.getUsername());
    }
    
    @Test
    public void testRemoveUser() {
        database.removeUser("testUser");
        User user = database.getUserByUsername("testUser");
        assertNull("User should be null after removal", user);
    }
    
    @Test
    public void testAddAndGetItemById() {
        // ID should be set by the database
        assertTrue("Item ID should be greater than 0", testItem.getId() > 0);
        
        Item item = database.getItemById(testItem.getId());
        assertNotNull("Item should not be null", item);
        assertEquals("Item title should match", "Test Item", item.getTitle());
    }
    
    @Test
    public void testRemoveItem() {
        int itemId = testItem.getId();
        database.removeItem(itemId);
        Item item = database.getItemById(itemId);
        assertNull("Item should be null after removal", item);
    }
    
    @Test
    public void testSearchItems() {
        List<Item> items = database.searchItems("test");
        assertNotNull("Items list should not be null", items);
        assertTrue("Items list should not be empty", !items.isEmpty());
        assertEquals("Should find 1 item", 1, items.size());
        
        // Mark item as sold
        testItem.setSold(true);
        items = database.searchItems("test");
        assertTrue("Sold items should not be returned", items.isEmpty());
    }
    
    @Test
    public void testAddAndGetMessagesForUser() {
        database.addMessage(testMessage);
        
        List<Message> messages = database.getMessagesForUser("testUser");
        assertNotNull("Messages list should not be null", messages);
        assertTrue("Messages list should not be empty", !messages.isEmpty());
        assertEquals("Should find 1 message", 1, messages.size());
        assertEquals("Message content should match", "Test message", messages.get(0).getContent());
    }
    
    @Test
    public void testAddAndGetTransactionsForUser() {
        database.addTransaction(testTransaction);
        
        List<Transaction> transactions = database.getTransactionsForUser("testUser");
        assertNotNull("Transactions list should not be null", transactions);
        assertTrue("Transactions list should not be empty", !transactions.isEmpty());
        assertEquals("Should find 1 transaction", 1, transactions.size());
        assertEquals("Transaction seller should match", "testUser", transactions.get(0).getSeller());
    }
    
    @Test
    public void testSaveToDisk() {
        // Add data
        User user = new User("saveTest", "password");
        database.addUser(user);
        
        // Save to disk
        database.saveToDisk();
        
        // Check file exists
        File file = new File("marketplace_data.ser");
        assertTrue("File should exist after saving", file.exists());
    }
    
    @Test
    public void testLoadFromDisk() {
        // Add data and save
        User user = new User("loadTest", "password");
        database.addUser(user);
        database.saveToDisk();
        
        // Create new database instance and load
        Database newDb = new Database();
        newDb.loadFromDisk();
        
        // Verify data was loaded
        User loadedUser = newDb.getUserByUsername("loadTest");
        assertNotNull("User should be loaded from disk", loadedUser);
        assertEquals("Username should match", "loadTest", loadedUser.getUsername());
    }
    
    @Test
    public void testTransactionPersistence() {
        database.addTransaction(testTransaction);
        database.saveToDisk();

        Database db2 = new Database();
        db2.loadFromDisk();
        List<Transaction> txs = db2.getTransactionsForUser("anotherUser");
        assertNotNull("Transactions should not be null after reload", txs);
        assertTrue("Should find at least one transaction after reload", !txs.isEmpty());
        assertEquals("Transaction buyer should match after reload", "anotherUser", txs.get(0).getBuyer());
    }
}
