package database;
import java.io.Serializable;

/**
 * User class
 *
 * This class represents a user with a username, password, and balance.
 * 
 * @author L10-Team1 
 *
 * @version April 2024
 *
 */
public class User implements UserInterface, Serializable {
    
    private String username; //username of the user
    private String password; //password of the user
    private double balance; //balance(money) of the user
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.balance = 100.0; // Initial balance after account creation is 100
    }
    
    /**
    * Returns the username of the user.
    *
    * @return username
    */
    public String getUsername() {
        return username;
    }
    
    /**
    * Check password of the user.
    *
    * @param password the password to check
    * @return true if the password matches, false otherwise
    */
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
    
    /**
    * Returns the balance of the user.
    *
    * @return balance
    */
    public double getBalance() {
        return balance;
    }
    
    /**
    * Sets the username of the user.
    *
    * @param balance the balance to set
    */
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
}
