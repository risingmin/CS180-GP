package database;
import java.io.Serializable;
import java.util.Date;

/**
 * Transaction class
 *
 * This class represents a transaction between a buyer and a seller.
 * 
 * @author L10-Team1 
 *
 * @version April 2024
 *
 */
public class Transaction implements TransactionInterface, Serializable {
    
    private String buyer; //ID of the buyer
    private String seller; //ID of the seller
    private int itemId; //ID of the item
    private double amount; //Transaction amount
    private Date timestamp; //Time of transaction
    

    public Transaction(String buyer, String seller, int itemId, double amount) {
        this.buyer = buyer;
        this.seller = seller;
        this.itemId = itemId;
        this.amount = amount;
        this.timestamp = new Date();
    }
    
    /**
    * Returns the buyer of the transaction.
    *
    * @return buyer
    */
    public String getBuyer() {
        return buyer;
    }
    
    /**
    * Returns the seller of the transaction.
    *
    * @return seller
    */
    public String getSeller() {
        return seller;
    }
    
    /**
    * Returns item ID of the transaction.
    *
    * @return itemId
    */
    public int getItemId() {
        return itemId;
    }
    
    /**
    * Returns amount of the transaction.
    *
    * @return amount
    */
    public double getAmount() {
        return amount;
    }
    
    /**
    * Returns time of the transaction.
    *
    * @return timestamp
    */
    public Date getTimestamp() {
        return timestamp;
    }
}
