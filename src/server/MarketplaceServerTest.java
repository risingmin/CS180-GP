package server;

import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MarketplaceServerTest {
    private MarketplaceServer server;
    
    @Before
    public void setUp() {
        server = new MarketplaceServer();
    }
    
    @After
    public void tearDown() {
        if (server.isRunning()) {
            server.stop();
        }
    }
    
    @Test
    public void testInitialState() {
        assertFalse("Server should not be running initially", server.isRunning());
    }
    
    @Test
    public void testStartStop() {
        // Use a port unlikely to be in use
        int testPort = 12345;
        
        // Start the server
        server.start(testPort);
        assertTrue("Server should be running after start", server.isRunning());
        
        // Try to connect to verify the server is actually running
        Socket testSocket = null;
        try {
            testSocket = new Socket("localhost", testPort);
            assertTrue("Should be able to connect to the server", testSocket.isConnected());
        } catch (IOException e) {
            fail("Should be able to connect to the server: " + e.getMessage());
        } finally {
            if (testSocket != null) {
                try {
                    testSocket.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
        
        // Stop the server
        server.stop();
        assertFalse("Server should not be running after stop", server.isRunning());
        
        // Verify server is not accepting connections
        try {
            new Socket("localhost", testPort);
            fail("Should not be able to connect after server is stopped");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test
    public void testServerHandlesMultipleClients() throws InterruptedException {
        int testPort = 23456;
        
        server.start(testPort);
        assertTrue("Server should be running", server.isRunning());

        AtomicBoolean client1Connected = new AtomicBoolean(false);
        AtomicBoolean client2Connected = new AtomicBoolean(false);

        // First client thread
        Thread client1 = new Thread(() -> {
            try (Socket socket = new Socket("localhost", testPort)) {
                client1Connected.set(true);
                // Keep connection open
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Second client thread
        Thread client2 = new Thread(() -> {
            try (Socket socket = new Socket("localhost", testPort)) {
                client2Connected.set(true);
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        client1.start();
        client2.start();

        // Wait for clients to connect
        Thread.sleep(1000);

        assertTrue("First client should have connected", client1Connected.get());
        assertTrue("Second client should have connected", client2Connected.get());

        client1.join();
        client2.join();
    }
}
