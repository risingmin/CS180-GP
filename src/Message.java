import java.io.Serializable;
import java.util.Date;

/**
 * Message class
 *
 * Represents a message sent between users in the system.
 * Fields are not meant to be modified after creation.
 * 
 * @author L10-Team1 
 *
 * @version April 2024
 *
 */
public class Message implements MessageInterface, Serializable {
    
    private String sender; //ID of the sender
    private String recipient; //ID of the recipient
    private String content; //Content of the message
    private Date timestamp; //Time of message creation
    private int itemId; //ID of the related item
    

    public Message(String sender, String recipient, String content, int itemId) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.timestamp = new Date();
        this.itemId = itemId;
    }
    
    /**
    * Returns the ID of the sender.
    *
    * @return sender
    */
    public String getSender() {
        return sender;
    }

    /**
    * Returns the ID of the recipient.
    *
    * @return recipient
    */
    public String getRecipient() {
        return recipient;
    }

    /**
    * Returns the content of the message.
    *
    * @return content
    */
    public String getContent() {
        return content;
    }

    /**
    * Returns the time of the message creation.
    *
    * @return timestamp
    */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
    * Returns the ID of the related item.
    *
    * @return itemId
    */
    public int getItemId() {
        return itemId;
    }
}