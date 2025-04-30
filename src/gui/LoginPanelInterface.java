package gui;

import javax.swing.JPanel;

/**
 * LoginPanelInterface
 *
 * This interface defines the required functionality for the login panel,
 * including login, registration, and navigation to other screens.
 * 
 * @author L10-Team1 
 *
 * @version May 2024
 */
public interface LoginPanelInterface {
    /**
     * Get the JPanel containing the login UI
     * @return JPanel for login
     */
    JPanel getPanel();
    
    /**
     * Process login attempt with credentials
     * @param username Username to login with
     * @param password Password for login
     * @return true if login successful, false otherwise
     */
    boolean processLogin(String username, String password);
    
    /**
     * Process registration attempt with new credentials
     * @param username Username to register
     * @param password Password to set
     * @return true if registration successful, false otherwise
     */
    boolean processRegistration(String username, String password);
    
    /**
     * Set the callback interface to notify about login events
     * @param callback Callback interface implementation
     */
    void setLoginCallback(LoginCallback callback);
    
    /**
     * Interface for login event callbacks
     */
    interface LoginCallback {
        /**
         * Called when login is successful
         * @param username Username that logged in
         */
        void onLoginSuccess(String username);
        
        /**
         * Called when login fails
         * @param errorMessage Error message explaining failure
         */
        void onLoginFailure(String errorMessage);
    }
}
