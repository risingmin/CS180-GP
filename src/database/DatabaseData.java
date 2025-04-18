package database;
import java.io.Serializable;
import java.util.*;

/**
 * DatabaseData class
 *
 * This class represents the data structure for the database.
 * Contains all the necessary data for the application.
 * 
 * @author L10-Team1 
 *
 * @version April 2024
 *
 */
public class DatabaseData implements DatabaseDataInterface, Serializable {
    
    private Map<String, User> users; //Map of users, (username, User object) opairs
    private Map<Integer, Item> items; //Map of items, (itemID, Item object) pairs
    private List<Message> messages; //List of messages sent
    private List<Transaction> transactions; //List of transactions made
    private int nextItemId; //Next item ID to be assigned
    
    public DatabaseData(Map<String, User> users, Map<Integer, Item> items, List<Message> messages, 
                        List<Transaction> transactions, int nextItemId) {
        this.users = users;
        this.items = items;
        this.messages = messages;
        this.transactions = transactions;
        this.nextItemId = nextItemId;
    }
    
    /**
    * Returns the map of users.
    *
    * @return users
    */
    public Map<String, User> getUsers() {
        return users;
    }
    
    /**
    * Returns the map of items.
    *
    * @return items
    */
    public Map<Integer, Item> getItems() {
        return items;
    }
    
    /**
    * Returns the list of messages.
    *
    * @return messages
    */
    public List<Message> getMessages() {
        return messages;
    }
    
    /**
    * Returns the list of transactions.
    *
    * @return transactions
    */
    public List<Transaction> getTransactions() {
        return transactions;
    }
    
    /**
    * Returns the next item ID.
    *
    * @return nextItemId
    */
    public int getNextItemId() {
        return nextItemId;
    }
}
