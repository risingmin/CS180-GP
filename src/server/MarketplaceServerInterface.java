package server;

/**
 * Interface for MarketplaceServer
 * Defines the required functionality for the server
 */
public interface MarketplaceServerInterface extends Runnable {
    void start(int port);
    void stop();
    boolean isRunning();
}