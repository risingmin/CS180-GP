package gui;

/**
 * MarketplaceClientGUIInterface
 *
 * This interface defines the required functionality for the marketplace client GUI,
 * including initializing the GUI, handling navigation, and managing the user session.
 * 
 * @author L10-Team1 
 *
 * @version May 2024
 */
public interface MarketplaceClientGUIInterface {
    /**
     * Initialize and display the main application window
     */
    void initialize();
    
    /**
     * Show the login screen
     */
    void showLoginScreen();
    
    /**
     * Show the dashboard screen after successful login
     */
    void showDashboard();
    
    /**
     * Handle user logout
     */
    void handleLogout();
    
    /**
     * Shutdown the application
     */
    void shutdown();
}
