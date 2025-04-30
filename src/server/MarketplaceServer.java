package server;
import java.net.*;
import java.io.*;
import java.util.*;
import database.*;

/**
 * MarketplaceServer class
 *
 * Implements the server-side logic for the marketplace system.
 * Listens for client connections, processes commands, and manages concurrent clients.
 * Handles user authentication, item management, messaging, transactions, and persistence.
 * 
 * @author L10-Team1
 * @version April 2024
 */

public class MarketplaceServer implements MarketplaceServerInterface {
    private ServerSocket serverSocket;
    private Database database;
    private boolean running;
    private Thread serverThread;

    /**
     * Constructor for MarketplaceServer
     */
    public MarketplaceServer() {
        this.database = new Database();
        this.database.loadFromDisk();
        this.running = false;
    }

    /**
     * Start the server on the specified port
     * @param port The port to start the server on
     */
    @Override
    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            serverThread = new Thread(this);
            serverThread.start();
            System.out.println("Server started on port " + port);
        } catch (IOException e) {
            System.err.println("Could not start server on port " + port);
            e.printStackTrace();
        }
    }

    /**
     * Stop the server
     */
    @Override
    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            System.out.println("Server stopped");
        } catch (IOException e) {
            System.err.println("Error stopping server");
            e.printStackTrace();
        }
    }

    /**
     * Check if the server is running
     * @return true if the server is running, false otherwise
     */
    @Override
    public boolean isRunning() {
        return running;
    }

    /**
     * Run method from Runnable interface
     * Continuously accepts client connections and handles them in new threads
     */
    public void run() {
        while(running) {
            try {
                Socket clientSocket = serverSocket.accept();
                // Handle client in a new thread
                new Thread(() -> handleClient(clientSocket)).start();
            } catch (IOException e) {
                if (running) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Handles a client connection
     * @param clientSocket The client socket connection
     */
    private void handleClient(Socket clientSocket) {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        String currentUser = null;
        boolean clientRunning = true;
        
        try {
            // Initialize streams
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ois = new ObjectInputStream(clientSocket.getInputStream());
            
            // Process client requests
            while (clientRunning) {
                String command = (String) ois.readObject();
                
                // Handle different commands
                switch (command) {
                    case "LOGIN":
                        currentUser = handleLogin(ois, oos);
                        break;
                    case "REGISTER":
                        handleRegister(ois, oos);
                        break;
                    case "LOGOUT":
                        currentUser = null;
                        oos.writeObject(new TransactionResult(true, "Logout successful"));
                        break;
                    case "ADD_ITEM":
                        handleAddItem(ois, oos, currentUser);
                        break;
                    case "SEARCH_ITEMS":
                        handleSearchItems(ois, oos);
                        break;
                    case "BUY_ITEM":
                        handleBuyItem(ois, oos, currentUser);
                        break;
                    case "GET_USER_ITEMS":
                        handleGetUserItems(oos, currentUser);
                        break;
                    case "SEND_MESSAGE":
                        handleSendMessage(ois, oos, currentUser);
                        break;
                    case "GET_MESSAGES":
                        handleGetMessages(oos, currentUser);
                        break;
                    case "GET_TRANSACTIONS":
                        handleGetTransactions(oos, currentUser);
                        break;
                    case "GET_BALANCE":
                        handleGetBalance(oos, currentUser);
                        break;
                    case "DELETE_ACCOUNT":
                        handleDeleteAccount(oos, currentUser);
                        if (currentUser != null) currentUser = null; // Reset user if account deleted
                        break;
                    case "DELETE_ITEM":
                        handleDeleteItem(ois, oos, currentUser);
                        break;
                    case "EXIT":
                        clientRunning = false;
                        break;
                    default:
                        oos.writeObject(new TransactionResult(false, "Unknown command"));
                }
                oos.flush();
            }
        } catch (EOFException e) {
            // Client disconnected
            System.out.println("Client disconnected");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (ois != null) ois.close();
                if (oos != null) oos.close();
                if (clientSocket != null) clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Handle login command
     * @return Username if login successful, null otherwise
     */
    private String handleLogin(ObjectInputStream ois, ObjectOutputStream oos) throws IOException, ClassNotFoundException {
        String username = (String) ois.readObject();
        String password = (String) ois.readObject();
        
        User user = database.getUserByUsername(username);
        
        if (user != null && user.checkPassword(password)) {
            oos.writeObject(new TransactionResult(true, "Login successful"));
            return username;
        } else {
            oos.writeObject(new TransactionResult(false, "Invalid username or password"));
            return null;
        }
    }
    
    /**
     * Handle register command
     */
    private void handleRegister(ObjectInputStream ois, ObjectOutputStream oos) throws IOException, ClassNotFoundException {
        String username = (String) ois.readObject();
        String password = (String) ois.readObject();
        
        User existingUser = database.getUserByUsername(username);
        
        if (existingUser != null) {
            oos.writeObject(new TransactionResult(false, "Username already exists"));
        } else {
            User newUser = new User(username, password);
            database.addUser(newUser);
            database.saveToDisk();
            oos.writeObject(new TransactionResult(true, "Registration successful"));
        }
    }
    
    /**
     * Handle add item command
     */
    private void handleAddItem(ObjectInputStream ois, ObjectOutputStream oos, String currentUser) 
            throws IOException, ClassNotFoundException {
        if (currentUser == null) {
            oos.writeObject(new TransactionResult(false, "Not logged in"));
            return;
        }
        
        String title = (String) ois.readObject();
        String description = (String) ois.readObject();
        double price = (Double) ois.readObject();
        
        Item item = new Item(title, description, price, currentUser);
        database.addItem(item);
        database.saveToDisk();
        
        oos.writeObject(new TransactionResult(true, "Item added successfully", item.getId()));
    }
    
    /**
     * Handle search items command
     */
    private void handleSearchItems(ObjectInputStream ois, ObjectOutputStream oos) throws IOException, ClassNotFoundException {
        String query = (String) ois.readObject();
        List<Item> items = database.searchItems(query);
        oos.writeObject(items);
    }
    
    /**
     * Handle buy item command
     */
    private void handleBuyItem(ObjectInputStream ois, ObjectOutputStream oos, String currentUser) 
            throws IOException, ClassNotFoundException {
        if (currentUser == null) {
            oos.writeObject(new TransactionResult(false, "Not logged in"));
            return;
        }
        
        int itemId = (Integer) ois.readObject();
        
        Item item = database.getItemById(itemId);
        if (item == null) {
            oos.writeObject(new TransactionResult(false, "Item not found"));
            return;
        }
        
        String seller = item.getSeller();
        if (seller.equals(currentUser)) {
            oos.writeObject(new TransactionResult(false, "Cannot buy your own item"));
            return;
        }
        
        PaymentProcessor processor = new PaymentProcessor(database);
        TransactionResult result = processor.processPayment(currentUser, seller, itemId);
        
        // The transaction has already been added to the database by the PaymentProcessor
        // and database.saveToDisk() was already called there, so we don't need to do it again here
        
        oos.writeObject(result);
    }
    
    /**
     * Handle get user items command
     */
    private void handleGetUserItems(ObjectOutputStream oos, String currentUser) throws IOException {
        if (currentUser == null) {
            oos.writeObject(new ArrayList<Item>());
            return;
        }
        
        List<Item> userItems = new ArrayList<>();
        
        for (int i = 1; ; i++) {
            Item item = database.getItemById(i);
            if (item == null) break;
            if (item.getSeller().equals(currentUser)) {
                userItems.add(item);
            }
        }
        
        oos.writeObject(userItems);
    }
    
    /**
     * Handle send message command
     */
    private void handleSendMessage(ObjectInputStream ois, ObjectOutputStream oos, String currentUser) 
            throws IOException, ClassNotFoundException {
        if (currentUser == null) {
            oos.writeObject(new TransactionResult(false, "Not logged in"));
            return;
        }
        
        String recipient = (String) ois.readObject();
        String content = (String) ois.readObject();
        int itemId = (Integer) ois.readObject();
        
        User recipientUser = database.getUserByUsername(recipient);
        
        if (recipientUser == null) {
            oos.writeObject(new TransactionResult(false, "Recipient not found"));
            return;
        }
        
        Message message = new Message(currentUser, recipient, content, itemId);
        database.addMessage(message);
        database.saveToDisk();
        
        oos.writeObject(new TransactionResult(true, "Message sent successfully"));
    }
    
    /**
     * Handle get messages command
     */
    private void handleGetMessages(ObjectOutputStream oos, String currentUser) throws IOException {
        if (currentUser == null) {
            oos.writeObject(new ArrayList<Message>());
            return;
        }
        
        List<Message> messages = database.getMessagesForUser(currentUser);
        oos.writeObject(messages);
    }
    
    /**
     * Handle get transactions command
     */
    private void handleGetTransactions(ObjectOutputStream oos, String currentUser) throws IOException {
        if (currentUser == null) {
            oos.writeObject(new ArrayList<Transaction>());
            return;
        }
        List<Transaction> transactions = database.getTransactionsForUser(currentUser);
        System.out.println("Server: Returning " + transactions.size() + " transactions for user " + currentUser);
        oos.writeObject(transactions);
    }
    
    /**
     * Handle get balance command
     */
    private void handleGetBalance(ObjectOutputStream oos, String currentUser) throws IOException {
        if (currentUser == null) {
            oos.writeObject(-1.0);
            return;
        }
        
        User user = database.getUserByUsername(currentUser);
        oos.writeObject(user.getBalance());
    }
    
    /**
     * Handle delete account command
     */
    private void handleDeleteAccount(ObjectOutputStream oos, String currentUser) throws IOException {
        if (currentUser == null) {
            oos.writeObject(new TransactionResult(false, "Not logged in"));
            return;
        }
        
        // Remove all user items from database
        List<Item> userItems = new ArrayList<>();
        for (int i = 1; ; i++) {
            Item item = database.getItemById(i);
            if (item == null) break;
            if (item.getSeller().equals(currentUser)) {
                userItems.add(item);
            }
        }
        
        // Remove all items owned by the user
        for (Item item : userItems) {
            database.removeItem(item.getId());
        }
        
        // Delete user account
        database.removeUser(currentUser);
        database.saveToDisk();
        
        oos.writeObject(new TransactionResult(true, "Account deleted successfully"));
    }
    
    /**
     * Handle delete item command
     */
    private void handleDeleteItem(ObjectInputStream ois, ObjectOutputStream oos, String currentUser) 
            throws IOException, ClassNotFoundException {
        if (currentUser == null) {
            oos.writeObject(new TransactionResult(false, "Not logged in"));
            return;
        }
        
        int itemId = (Integer) ois.readObject();
        
        Item item = database.getItemById(itemId);
        if (item == null) {
            oos.writeObject(new TransactionResult(false, "Item not found"));
            return;
        }
        
        // Check if user is the owner of the item
        if (!item.getSeller().equals(currentUser)) {
            oos.writeObject(new TransactionResult(false, "You can only delete your own items"));
            return;
        }
        
        // Delete the item
        database.removeItem(itemId);
        database.saveToDisk();
        
        oos.writeObject(new TransactionResult(true, "Item deleted successfully"));
    }
}
