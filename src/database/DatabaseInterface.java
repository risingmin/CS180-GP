package database;
import java.util.List;

public interface DatabaseInterface {
    void loadFromDisk();
    void saveToDisk();
    User getUserByUsername(String username);
    void addUser(User user);
    void removeUser(String username);
    void addItem(Item item);
    Item getItemById(int id);
    void removeItem(int id);
    List<Item> searchItems(String query);
    void addMessage(Message message);
    List<Message> getMessagesForUser(String username);
    void addTransaction(Transaction transaction);
    List<Transaction> getTransactionsForUser(String username);
}
