import static org.junit.Assert.*;
import org.junit.*;

public class ItemTest {
    private Item item;
    
    @Before
    public void setUp() {
        item = new Item("Test Item", "This is a test item", 50.0, "testUser");
        item.setId(123);
    }
    
    @Test
    public void testGetId() {
        assertEquals("Item ID should match", 123, item.getId());
    }
    
    @Test
    public void testSetId() {
        item.setId(456);
        assertEquals("Item ID should be updated", 456, item.getId());
    }
    
    @Test
    public void testGetTitle() {
        assertEquals("Item title should match", "Test Item", item.getTitle());
    }
    
    @Test
    public void testGetDescription() {
        assertEquals("Item description should match", "This is a test item", item.getDescription());
    }
    
    @Test
    public void testGetPrice() {
        assertEquals("Item price should match", 50.0, item.getPrice(), 0.001);
    }
    
    @Test
    public void testGetSeller() {
        assertEquals("Item seller should match", "testUser", item.getSeller());
    }
    
    @Test
    public void testIsSoldInitialValue() {
        assertFalse("Item should not be sold initially", item.isSold());
    }
    
    @Test
    public void testSetSold() {
        item.setSold(true);
        assertTrue("Item should be marked as sold", item.isSold());
        
        item.setSold(false);
        assertFalse("Item should be marked as not sold", item.isSold());
    }
}
