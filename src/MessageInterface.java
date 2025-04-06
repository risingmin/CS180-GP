import java.util.Date;

public interface MessageInterface {
    String getSender();
    String getRecipient();
    String getContent();
    Date getTimestamp();
    int getItemId();
}
