package database;
public interface UserInterface {
    String getUsername();
    boolean checkPassword(String password);
    double getBalance();
    void setBalance(double balance);
}
