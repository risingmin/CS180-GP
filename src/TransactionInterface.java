import java.util.Date;

public interface TransactionInterface {
    String getBuyer();
    String getSeller();
    int getItemId();
    double getAmount();
    Date getTimestamp();
}
