package gui;

import client.MarketplaceClient;

import javax.swing.*;
import java.awt.*;

/**
 * DashboardPanel class
 *
 * This class implements the main dashboard panel for the Marketplace client.
 * It contains tabs for different functionality areas such as marketplace,
 * user items, messages, and account management.
 * 
 * @author L10-Team1 
 *
 * @version May 2024
 *
 */
public class DashboardPanel implements DashboardPanelInterface {
    
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JButton logoutButton;
    
    private ItemsPanel marketplacePanel;
    private ItemsPanel myItemsPanel;
    private MessagesPanel messagesPanel;
    private AccountPanel accountPanel;
    
    private MarketplaceClient client;
    private DashboardCallback callback;
    
    // Tab indices
    public static final int MARKETPLACE_TAB = 0;
    public static final int MY_ITEMS_TAB = 1;
    public static final int MESSAGES_TAB = 2;
    public static final int ACCOUNT_TAB = 3;
    
    /**
     * Constructor for DashboardPanel
     * @param client The marketplace client to connect to backend
     */
    public DashboardPanel(MarketplaceClient client) {
        this.client = client;
        initializeUI();
    }
    
    /**
     * Initialize the UI components
     */
    private void initializeUI() {
        mainPanel = new JPanel(new BorderLayout());
        
        // Create header with logout button
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Marketplace Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));
        
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            if (callback != null) {
                callback.onLogoutRequested();
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(logoutButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Create tabbed pane for different sections
        tabbedPane = new JTabbedPane();
        
        // Initialize panels
        marketplacePanel = new ItemsPanel(client, false);
        myItemsPanel = new ItemsPanel(client, true);
        messagesPanel = new MessagesPanel(client);
        accountPanel = new AccountPanel(client);
        
        // Set up account callback
        accountPanel.setAccountCallback(new AccountPanel.AccountCallback() {
            @Override
            public void onAccountDeleted() {
                if (callback != null) {
                    callback.onSessionInvalid(); // Treat account deletion as session ending
                }
            }
            
            @Override
            public void onBalanceChanged(double newBalance) {
                // Nothing to do here
            }
        });
        
        // Add tabs
        tabbedPane.addTab("Marketplace", marketplacePanel.getPanel());
        tabbedPane.addTab("My Items", myItemsPanel.getPanel());
        tabbedPane.addTab("Messages", messagesPanel.getPanel());
        tabbedPane.addTab("Account", accountPanel.getPanel());
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Add listener for tab changes to refresh data
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            switch (selectedIndex) {
                case MARKETPLACE_TAB:
                    marketplacePanel.refreshItems();
                    break;
                case MY_ITEMS_TAB:
                    myItemsPanel.refreshItems();
                    break;
                case MESSAGES_TAB:
                    messagesPanel.refreshMessages();
                    break;
                case ACCOUNT_TAB:
                    accountPanel.refreshAccountData();
                    break;
            }
        });
    }
    
    /**
     * Get the JPanel containing the dashboard UI
     * @return JPanel for dashboard
     */
    @Override
    public JPanel getPanel() {
        return mainPanel;
    }
    
    /**
     * Refresh all dashboard data from server
     */
    @Override
    public void refreshData() {
        // Check if user is still logged in
        if (client.getLoggedInUser() == null) {
            if (callback != null) {
                callback.onSessionInvalid();
            }
            return;
        }
        
        // Refresh current tab
        int currentTab = tabbedPane.getSelectedIndex();
        switch (currentTab) {
            case MARKETPLACE_TAB:
                marketplacePanel.refreshItems();
                break;
            case MY_ITEMS_TAB:
                myItemsPanel.refreshItems();
                break;
            case MESSAGES_TAB:
                messagesPanel.refreshMessages();
                break;
            case ACCOUNT_TAB:
                accountPanel.refreshAccountData();
                break;
        }
    }
    
    /**
     * Navigate to a specific tab in the dashboard
     * @param tabIndex Index of tab to switch to
     */
    @Override
    public void switchToTab(int tabIndex) {
        if (tabIndex >= 0 && tabIndex < tabbedPane.getTabCount()) {
            tabbedPane.setSelectedIndex(tabIndex);
        }
    }
    
    /**
     * Set the callback interface for dashboard events
     * @param callback Callback interface implementation
     */
    @Override
    public void setDashboardCallback(DashboardCallback callback) {
        this.callback = callback;
    }
}
