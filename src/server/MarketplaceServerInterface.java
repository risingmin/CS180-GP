package server;

/**
 * MarketplaceServerInterface
 *
 * This interface defines the required functionality for the marketplace server,
 * including starting, stopping, and checking the server status.
 * 
 * @author L10-Team1 
 *
 * @version April 2024
 *
 */
public interface MarketplaceServerInterface extends Runnable {
    /**
     * Start the server on the specified port
     * @param port The port number to listen on
     */
    void start(int port);
    
    /**
     * Stop the server and close all connections
     */
    void stop();
    
    /**
     * Check if the server is currently running
     * @return true if the server is running, false otherwise
     */
    boolean isRunning();
}