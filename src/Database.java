import java.io.*;
import java.util.*;

/**
 * Database class
 *
 * This class implements the DatabaseInterface and provides methods to manage users, items, messages, and transactions.
 * Also handles loading and saving data to disk.
 * All methods are synchronized to ensure thread safety.
 * 
 * @author L10-Team1 
 *
 * @version April 2024
 *
 */
public class Database implements DatabaseInterface {
    private Map<String, User> users; // Map of users (username, User object) pairs
    private Map<Integer, Item> items; // Map of items (itemID, Item object) pairs
    private List<Message> messages; // List of messages sent
    private List<Transaction> transactions; // List of transactions made
    private int nextItemId; // Next item ID to be assigned
    
    private final String DATA_FILE = "marketplace_data.ser"; // File name for serialized data file
    
    public Database() {
        this.users = new HashMap<>();
        this.items = new HashMap<>();
        this.messages = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.nextItemId = 1;
    }
    
    /**
    * Loads the database from disk.
    * If the file does not exist and Exception is thrown, it will be caught and handled.
    */
    public void loadFromDisk() {
        synchronized(this) {
            File file = new File(DATA_FILE);
            if (!file.exists()) {
                return;
            }
            
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                DatabaseData data = (DatabaseData) ois.readObject();
                this.users = data.getUsers();
                this.items = data.getItems();
                this.messages = data.getMessages();
                this.nextItemId = data.getNextItemId();
                this.transactions = data.getTransactions();
                System.out.println("Database loaded from disk");
            } catch (Exception e) {
                System.err.println("Error loading database from disk: " + e.getMessage());
            }
        }
    }
    
    /**
    * Saves the database to disk.
    * If an Exception is thrown, it will be caught and handled. 
    */
    public void saveToDisk() {
        synchronized(this) {
            DatabaseData data = new DatabaseData(users, items, messages, transactions, nextItemId);
            
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
                oos.writeObject(data);
                System.out.println("Database saved to disk");
            } catch (IOException e) {
                System.err.println("Error saving database to disk: " + e.getMessage());
            }
        }
    }
    
    /**
    * Returns the user object for the given username.
    * @param username
    * @return User object
    */
    public User getUserByUsername(String username) {
        synchronized(this) {
            return users.get(username);
        }
    }

    /**
    * Adds a user to the database.
    * @param user
    */
    public void addUser(User user) {
        synchronized(this) {
            users.put(user.getUsername(), user);
        }
    }
    
    /**
    * Removes a user from the database. 
    * @param username
    */
    public void removeUser(String username) {
        synchronized(this) {
            users.remove(username);
        }
    }
    
    /**
    * Adds an item to the database.
    * @return 
    */
    public void addItem(Item item) {
        synchronized(this) {
            item.setId(nextItemId++);
            items.put(item.getId(), item);
        }
    }
    /**
    * Returns the item object for the given item ID.
    * @param id
    * @return Item object
    */
    public Item getItemById(int id) {
        synchronized(this) {
            return items.get(id);
        }
    }
    
    /**
    * Removes an item from the database.
    * @param id
    */
    public void removeItem(int id) {
        synchronized(this) {
            items.remove(id);
        }
    }
    
    /**
    * Searches for items based on the given query.
    * @param query
    * @return List of items matching the query
    */
    public List<Item> searchItems(String itemToSearch) {
        synchronized(this) {
            List<Item> results = new ArrayList<>();
            String lower = itemToSearch.toLowerCase();
            
            for (Item item : items.values()) {
                if (!item.isSold() && 
                    (item.getTitle().toLowerCase().contains(lower) || 
                     item.getDescription().toLowerCase().contains(lower))) {
                    results.add(item);
                }
            }
            
            return results;
        }
    }
    
    /**
    * Adds a message to the database.
    * @param message
    */
    public void addMessage(Message message) {
        synchronized(this) {
            messages.add(message);
        }
    }
    
    /**
    * Returns the list of messages for the given user.
    * @param username
    * @return List of messages for the user
    */
    public List<Message> getMessagesForUser(String username) {
        synchronized(this) {
            List<Message> userMessages = new ArrayList<>();
            
            for (Message message : messages) {
                if (message.getSender().equals(username) || message.getRecipient().equals(username)) {
                    userMessages.add(message);
                }
            }
            
            return userMessages;
        }
    }
    
    /**
    * Adds a transaction to the database. 
    * @param transaction
    */
    public void addTransaction(Transaction transaction) {
        synchronized(this) {
            transactions.add(transaction);
        }
    }
    
    /**
    * getTransactionsForUser
    * @param username
    * @return List of transactions for the user
    */
    public List<Transaction> getTransactionsForUser(String username) {
        synchronized(this) {
            List<Transaction> userTransactions = new ArrayList<>();
            
            for (Transaction transaction : transactions) {
                if (transaction.getBuyer().equals(username) || transaction.getSeller().equals(username)) {
                    userTransactions.add(transaction);
                }
            }
            
            return userTransactions;
        }
    }
}
