package gui;

import javax.swing.JPanel;

/**
 * DashboardPanelInterface
 *
 * This interface defines the required functionality for the dashboard panel,
 * which includes tabs for marketplace listings, user items, messages, and account.
 * 
 * @author L10-Team1 
 *
 * @version May 2024
 */
public interface DashboardPanelInterface {
    /**
     * Get the JPanel containing the dashboard UI
     * @return JPanel for dashboard
     */
    JPanel getPanel();
    
    /**
     * Refresh all dashboard data from server
     */
    void refreshData();
    
    /**
     * Navigate to a specific tab in the dashboard
     * @param tabIndex Index of tab to switch to
     */
    void switchToTab(int tabIndex);
    
    /**
     * Set the callback interface for dashboard events
     * @param callback Callback interface implementation
     */
    void setDashboardCallback(DashboardCallback callback);
    
    /**
     * Interface for dashboard event callbacks
     */
    interface DashboardCallback {
        /**
         * Called when user requests to logout
         */
        void onLogoutRequested();
        
        /**
         * Called when dashboard detects the session is invalid
         */
        void onSessionInvalid();
    }
}
