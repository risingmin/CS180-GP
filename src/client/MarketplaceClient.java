package client;
import database.*;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * MarketplaceClient class for connecting to the marketplace server
 * All data is stored on the server and accessed via network I/O
 */
public class MarketplaceClient {
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
     */
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
     * Login to the marketplace
     * @param username Username
     * @param password Password
     * @return TransactionResult indicating success/failure
     */
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
     * Register a new user
     * @param username Username
     * @param password Password
     * @return TransactionResult indicating success/failure
     */
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
     * Logout from the marketplace
     * @return TransactionResult indicating success/failure
     */
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
     * Add an item to the marketplace
     * @param title Item title
     * @param description Item description
     * @param price Item price
     * @return TransactionResult indicating success/failure
     */
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
     * Search for items in the marketplace
     * @param query Search query
     * @return List of matching items
     */
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
     * Buy an item from the marketplace
     * @param itemId ID of the item to buy
     * @return TransactionResult indicating success/failure
     */
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
     * Get items listed by the logged-in user
     * @return List of user's items
     */
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
     * Send a message to another user
     * @param recipient Recipient username
     * @param content Message content
     * @param itemId ID of item the message is about
     * @return TransactionResult indicating success/failure
     */
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
     * Get messages for the logged-in user
     * @return List of messages
     */
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
     * Get transactions for the logged-in user
     * @return List of transactions
     */
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
     * Get the balance of the logged-in user
     * @return User balance or -1 if not logged in or error
     */
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
     * Check if connected to server
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        return connected;
    }
    
    /**
     * Get the username of the logged-in user
     * @return Username or null if not logged in
     */
    public String getLoggedInUser() {
        return loggedInUser;
    }
}
