public interface TransactionResultInterface {
    boolean isSuccess();
    String getMessage();
    int getItemId();
}