package server;

/**
 * MarketplaceServerMain class
 *
 * This class provides the entry point for running the marketplace server.
 * It handles command-line arguments, server initialization, and graceful shutdown.
 * 
 * @author L10-Team1 
 *
 * @version April 2024
 *
 */
public class MarketplaceServerMain {
    private static final int DEFAULT_PORT = 8080;

    /**
     * Main method to run the Marketplace server
     * @param args Command-line arguments, first argument can be port number
     */
    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        
        // Allow port to be specified as command line argument
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number. Using default port " + DEFAULT_PORT);
            }
        }
        
        System.out.println("Starting Marketplace Server on port " + port);
        
        // Create and start the server
        MarketplaceServer server = new MarketplaceServer();
        server.start(port);
        
        // Add shutdown hook to stop server gracefully
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down server...");
            server.stop();
        }));
        
        System.out.println("Server is running. Press Ctrl+C to stop.");
    }
}
