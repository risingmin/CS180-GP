package client;
import database.*;

import java.util.*;

/**
 * MarketplaceClientInterface
 *
 * This interface defines the required functionality for marketplace clients,
 * including server connection, user authentication, and marketplace operations.
 * 
 * @author L10-Team1 
 *
 * @version April 2024
 *
 */
public interface MarketplaceClientInterface {
    /**
     * Connect to the marketplace server
     * @param host Server hostname or IP address
     * @param port Server port number
     * @return true if connection successful, false otherwise
     */
    boolean connect(String host, int port);
    
    /**
     * Disconnect from the marketplace server
     */
    void disconnect();
    
    /**
     * Login to the marketplace
     * @param username Username
     * @param password Password
     * @return TransactionResult indicating success/failure
     */
    TransactionResult login(String username, String password);
    
    /**
     * Register a new user
     * @param username Username
     * @param password Password
     * @return TransactionResult indicating success/failure
     */
    TransactionResult register(String username, String password);
    
    /**
     * Logout from the marketplace
     * @return TransactionResult indicating success/failure
     */
    TransactionResult logout();
    
    /**
     * Add an item to the marketplace
     * @param title Item title
     * @param description Item description
     * @param price Item price
     * @return TransactionResult indicating success/failure
     */
    TransactionResult addItem(String title, String description, double price);
    
    /**
     * Search for items in the marketplace
     * @param query Search query
     * @return List of matching items
     */
    List<Item> searchItems(String query);
    
    /**
     * Buy an item from the marketplace
     * @param itemId ID of the item to buy
     * @return TransactionResult indicating success/failure
     */
    TransactionResult buyItem(int itemId);
    
    /**
     * Get items listed by the logged-in user
     * @return List of user's items
     */
    List<Item> getUserItems();
    
    /**
     * Send a message to another user
     * @param recipient Recipient username
     * @param content Message content
     * @param itemId ID of item the message is about
     * @return TransactionResult indicating success/failure
     */
    TransactionResult sendMessage(String recipient, String content, int itemId);
    
    /**
     * Get messages for the logged-in user
     * @return List of messages
     */
    List<Message> getMessages();
    
    /**
     * Get transactions for the logged-in user
     * @return List of transactions
     */
    List<Transaction> getTransactions();
    
    /**
     * Get the balance of the logged-in user
     * @return User balance or -1 if not logged in or error
     */
    double getBalance();
    
    /**
     * Check if connected to server
     * @return true if connected, false otherwise
     */
    boolean isConnected();
    
    /**
     * Get the username of the logged-in user
     * @return Username or null if not logged in
     */
    String getLoggedInUser();
}
