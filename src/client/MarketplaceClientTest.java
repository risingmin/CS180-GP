package client;

import static org.junit.Assert.*;
import org.junit.*;
import database.*;

public class MarketplaceClientTest {
    private MarketplaceClient client;
    
    @Before
    public void setUp() {
        client = new MarketplaceClient();
    }
    
    @Test
    public void testInitialState() {
        assertFalse("Client should not be connected initially", client.isConnected());
        assertNull("Logged in user should be null initially", client.getLoggedInUser());
    }
    
    @Test
    public void testConnect() {
        // Note: This test assumes there's no server running on this port
        // It's just testing the client behavior when connection fails
        boolean result = client.connect("localhost", 9999);
        assertFalse("Connection should fail if no server is running", result);
        assertFalse("Client should not be connected after failed attempt", client.isConnected());
    }
    
    @Test
    public void testDisconnect() {
        // Even if not connected, disconnect should not throw exceptions
        client.disconnect();
        assertFalse("Client should not be connected after disconnect", client.isConnected());
        assertNull("Logged in user should be null after disconnect", client.getLoggedInUser());
    }
}
