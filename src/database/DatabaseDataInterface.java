package database;
import java.util.List;
import java.util.Map;

public interface DatabaseDataInterface {
    Map<String, User> getUsers();
    Map<Integer, Item> getItems();
    List<Message> getMessages();
    List<Transaction> getTransactions();
    int getNextItemId();
}