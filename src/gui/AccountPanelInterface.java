package gui;

import javax.swing.JPanel;

/**
 * AccountPanelInterface
 *
 * This interface defines the required functionality for the account panel,
 * which includes viewing and managing user account details and transactions.
 * 
 * @author L10-Team1 
 *
 * @version May 2024
 */
public interface AccountPanelInterface {
    /**
     * Get the JPanel containing the account UI
     * @return JPanel for account
     */
    JPanel getPanel();
    
    /**
     * Refresh account data from server
     */
    void refreshAccountData();
    
    /**
     * Get the current user's balance
     * @return User balance
     */
    double getBalance();
    
    /**
     * Delete the user's account after confirmation
     * @return true if account deleted successfully, false otherwise
     */
    boolean deleteAccount();
    
    /**
     * Set the callback interface for account events
     * @param callback Callback interface implementation
     */
    void setAccountCallback(AccountCallback callback);
    
    /**
     * Interface for account event callbacks
     */
    interface AccountCallback {
        /**
         * Called when account has been deleted
         */
        void onAccountDeleted();
        
        /**
         * Called when balance changes
         * @param newBalance Updated balance
         */
        void onBalanceChanged(double newBalance);
    }
}
