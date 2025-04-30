package database;

import java.io.Serializable;
import java.util.Date;

/**
 * Transaction class
 *
 * Represents a transaction between a buyer and seller for a specific item.
 * Stores transaction details including ID, item ID, amount, timestamp, and involved parties.
 * 
 * @author L10-Team1
 * @version April 2024
 */
public class Transaction implements TransactionInterface, Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private int itemId;
    private double amount;
    private Date timestamp;
    private String buyer;
    private String seller;
    
    /**
     * Constructor for Transaction with auto-generated ID
     * 
     * @param buyer Username of buyer
     * @param seller Username of seller
     * @param itemId ID of the item involved
     * @param amount Transaction amount/price
     */
    public Transaction(String buyer, String seller, int itemId, double amount) {
        this.id = (int)(System.currentTimeMillis() % Integer.MAX_VALUE);
        this.buyer = buyer;
        this.seller = seller;
        this.itemId = itemId;
        this.amount = amount;
        this.timestamp = new Date(); // Current time
    }
    
    /**
     * Constructor for Transaction with specific ID
     * 
     * @param id Transaction ID
     * @param itemId ID of the item involved
     * @param amount Transaction amount/price
     * @param buyer Username of buyer
     * @param seller Username of seller
     */
    public Transaction(int id, int itemId, double amount, String buyer, String seller) {
        this.id = id;
        this.itemId = itemId;
        this.amount = amount;
        this.timestamp = new Date(); // Current time
        this.buyer = buyer;
        this.seller = seller;
    }
    
    /**
     * Get the transaction ID
     * @return Transaction ID
     */
    public int getId() {
        return id;
    }
    
    /**
     * Get the item ID
     * @return Item ID
     */
    @Override
    public int getItemId() {
        return itemId;
    }
    
    /**
     * Get the transaction amount
     * @return Amount
     */
    @Override
    public double getAmount() {
        return amount;
    }
    
    /**
     * Get the timestamp when the transaction occurred
     * @return Timestamp
     */
    @Override
    public Date getTimestamp() {
        return timestamp;
    }
    
    /**
     * Get the buyer's username
     * @return Buyer username
     */
    @Override
    public String getBuyer() {
        return buyer;
    }
    
    /**
     * Get the seller's username
     * @return Seller username
     */
    @Override
    public String getSeller() {
        return seller;
    }
}
