package client;
import database.*;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * MarketplaceClient class
 *
 * This class implements the MarketplaceClientInterface and provides functionality for connecting
 * to the marketplace server, handling user authentication, and performing marketplace operations.
 * All data is stored on the server and accessed via network I/O.
 * 
 * @author L10-Team1 
 *
 * @version April 2024
 *
 */
public class MarketplaceClient implements MarketplaceClientInterface {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private String loggedInUser;
    private boolean connected;

    /**
     * Connect to the marketplace server
     * @param host Server hostname or IP address
     * @param port Server port number
     * @return true if connection successful, false otherwise
     */
    @Override
    public boolean connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            connected = true;
            return true;
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
            connected = false;
            return false;
        }
    }
    
    /**
     * Disconnect from the marketplace server
     * Sends exit command to server and closes all resources
     */
    @Override
    public void disconnect() {
        if (!connected) return;
        
        try {
            oos.writeObject("EXIT");
            oos.flush();
            
            socket.close();
            oos.close();
            ois.close();
            connected = false;
            loggedInUser = null;
        } catch (IOException e) {
            System.err.println("Error disconnecting: " + e.getMessage());
        }
    }
    
    /**
     * Login to the marketplace with credentials
     * @param username Username to login with
     * @param password Password for the user
     * @return TransactionResult indicating success/failure of login attempt
     */
    @Override
    public TransactionResult login(String username, String password) {
        try {
            oos.writeObject("LOGIN");
            oos.writeObject(username);
            oos.writeObject(password);
            oos.flush();
            
            TransactionResult result = (TransactionResult) ois.readObject();
            if (result.isSuccess()) {
                loggedInUser = username;
            }
            return result;
        } catch (IOException | ClassNotFoundException e) {
            return new TransactionResult(false, "Error: " + e.getMessage());
        }
    }
    
    /**
     * Register a new user in the marketplace
     * @param username Username for new account
     * @param password Password for new account
     * @return TransactionResult indicating success/failure of registration
     */
    @Override
    public TransactionResult register(String username, String password) {
        try {
            oos.writeObject("REGISTER");
            oos.writeObject(username);
            oos.writeObject(password);
            oos.flush();
            
            return (TransactionResult) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new TransactionResult(false, "Error: " + e.getMessage());
        }
    }
    
    /**
     * Logout current user from the marketplace
     * @return TransactionResult indicating success/failure of logout
     */
    @Override
    public TransactionResult logout() {
        try {
            oos.writeObject("LOGOUT");
            oos.flush();
            
            TransactionResult result = (TransactionResult) ois.readObject();
            if (result.isSuccess()) {
                loggedInUser = null;
            }
            return result;
        } catch (IOException | ClassNotFoundException e) {
            return new TransactionResult(false, "Error: " + e.getMessage());
        }
    }
    
    /**
     * Add an item to the marketplace for sale
     * @param title Item title
     * @param description Item description
     * @param price Item price
     * @return TransactionResult indicating success/failure and the new item ID
     */
    @Override
    public TransactionResult addItem(String title, String description, double price) {
        try {
            oos.writeObject("ADD_ITEM");
            oos.writeObject(title);
            oos.writeObject(description);
            oos.writeObject(price);
            oos.flush();
            
            return (TransactionResult) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new TransactionResult(false, "Error: " + e.getMessage());
        }
    }
    
    /**
     * Search for items in the marketplace by keyword
     * @param query Search keyword
     * @return List of matching items, empty list if none found or error
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Item> searchItems(String query) {
        try {
            oos.writeObject("SEARCH_ITEMS");
            oos.writeObject(query);
            oos.flush();
            
            return (List<Item>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error searching items: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Purchase an item from the marketplace
     * @param itemId ID of the item to buy
     * @return TransactionResult indicating success/failure of purchase
     */
    @Override
    public TransactionResult buyItem(int itemId) {
        try {
            oos.writeObject("BUY_ITEM");
            oos.writeObject(itemId);
            oos.flush();
            
            return (TransactionResult) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new TransactionResult(false, "Error: " + e.getMessage());
        }
    }
    
    /**
     * Retrieve items listed by the current logged-in user
     * @return List of user's items, empty list if none or error
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Item> getUserItems() {
        try {
            oos.writeObject("GET_USER_ITEMS");
            oos.flush();
            
            return (List<Item>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error getting user items: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Send a message to another user regarding an item
     * @param recipient Username of message recipient
     * @param content Message text content
     * @param itemId ID of item the message is about (0 for general messages)
     * @return TransactionResult indicating success/failure of message sending
     */
    @Override
    public TransactionResult sendMessage(String recipient, String content, int itemId) {
        try {
            oos.writeObject("SEND_MESSAGE");
            oos.writeObject(recipient);
            oos.writeObject(content);
            oos.writeObject(itemId);
            oos.flush();
            
            return (TransactionResult) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new TransactionResult(false, "Error: " + e.getMessage());
        }
    }
    
    /**
     * Get all messages for the current logged-in user
     * @return List of messages, empty list if none or error
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Message> getMessages() {
        try {
            oos.writeObject("GET_MESSAGES");
            oos.flush();
            
            return (List<Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error getting messages: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Get transaction history for the current logged-in user
     * @return List of transactions, empty list if none or error
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Transaction> getTransactions() {
        try {
            oos.writeObject("GET_TRANSACTIONS");
            oos.flush();
            
            return (List<Transaction>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error getting transactions: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Get current balance of logged-in user
     * @return User balance or -1 if not logged in or error
     */
    @Override
    public double getBalance() {
        try {
            oos.writeObject("GET_BALANCE");
            oos.flush();
            
            return (Double) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error getting balance: " + e.getMessage());
            return -1;
        }
    }
    
    /**
     * Check if client is connected to server
     * @return true if connected, false otherwise
     */
    @Override
    public boolean isConnected() {
        return connected;
    }
    
    /**
     * Get username of currently logged-in user
     * @return Username or null if not logged in
     */
    @Override
    public String getLoggedInUser() {
        return loggedInUser;
    }
}
