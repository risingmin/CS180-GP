import java.io.Serializable;

/**
 * TransactionResult class
 *
 * This class represents the result of a transaction, including its success status, message, and item ID.
 * 
 * @author L10-Team1
 *
 * @version April 2024
 *
 */
public class TransactionResult implements TransactionResultInterface, Serializable {
    
    private boolean success; // Indicates if the transaction was successful
    private String message; // Message related to the transaction result
    private int itemId; // ID of the item involved in the transaction
    
    public TransactionResult(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.itemId = -1; // Default value for itemId when not provided
    }
    
    public TransactionResult(boolean success, String message, int itemId) {
        this.success = success;
        this.message = message;
        this.itemId = itemId;
    }
    
    /**
    * Returns the success status of the transaction.
    *
    * @return true if the transaction was successful, false otherwise
    */
    public boolean isSuccess() {
        return success;
    }
    
    /**
    * Returns the message related to the transaction result.
    *
    * @return message
    */
    public String getMessage() {
        return message;
    }
    
    /**
    * Returns the Item ID related to the transaction.
    *
    * @return itemId
    */
    public int getItemId() {
        return itemId;
    }
}