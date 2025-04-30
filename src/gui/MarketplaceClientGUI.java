package gui;

import client.MarketplaceClient;
import database.TransactionResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * MarketplaceClientGUI class
 *
 * This class implements the main GUI window for the Marketplace client.
 * It manages navigation between different panels and handles the client connection.
 * 
 * @author L10-Team1 
 *
 * @version May 2024
 *
 */
public class MarketplaceClientGUI implements MarketplaceClientGUIInterface, 
        LoginPanelInterface.LoginCallback, 
        DashboardPanelInterface.DashboardCallback,
        AccountPanelInterface.AccountCallback {

    private JFrame mainFrame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    private LoginPanel loginPanel;
    private DashboardPanel dashboardPanel;
    
    private MarketplaceClient client;
    private String loggedInUser;
    
    // Panel identifiers for CardLayout
    private static final String LOGIN_PANEL = "LOGIN_PANEL";
    private static final String DASHBOARD_PANEL = "DASHBOARD_PANEL";
    
    /**
     * Constructor for MarketplaceClientGUI
     * @param client The marketplace client to connect to backend
     */
    public MarketplaceClientGUI(MarketplaceClient client) {
        this.client = client;
    }
    
    /**
     * Initialize and display the main application window
     */
    @Override
    public void initialize() {
        // Set a larger default font for all Swing components
        setGlobalUIFont(new Font("SansSerif", Font.PLAIN, 18));

        // Create the main frame
        mainFrame = new JFrame("Marketplace Client");
        mainFrame.setSize(1000, 700);
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // Add window close listener to handle clean shutdown
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                shutdown();
            }
        });
        
        // Create the main panel with card layout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Initialize panels
        loginPanel = new LoginPanel(client);
        loginPanel.setLoginCallback(this);
        mainPanel.add(loginPanel.getPanel(), LOGIN_PANEL);
        
        dashboardPanel = new DashboardPanel(client);
        dashboardPanel.setDashboardCallback(this);
        mainPanel.add(dashboardPanel.getPanel(), DASHBOARD_PANEL);
        
        // Set initial view to login
        cardLayout.show(mainPanel, LOGIN_PANEL);
        
        // Add the main panel to the frame
        mainFrame.add(mainPanel);
        
        // Center the frame on screen
        mainFrame.setLocationRelativeTo(null);
        
        // Make the frame visible
        mainFrame.setVisible(true);
    }
    
    /**
     * Utility method to set a global font for all Swing components
     */
    private static void setGlobalUIFont(Font font) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }
    
    /**
     * Show the login screen
     */
    @Override
    public void showLoginScreen() {
        cardLayout.show(mainPanel, LOGIN_PANEL);
        mainFrame.setTitle("Marketplace Client - Login");
    }
    
    /**
     * Show the dashboard screen after successful login
     */
    @Override
    public void showDashboard() {
        cardLayout.show(mainPanel, DASHBOARD_PANEL);
        mainFrame.setTitle("Marketplace Client - " + loggedInUser);
        dashboardPanel.refreshData();
    }
    
    /**
     * Handle user logout
     */
    @Override
    public void handleLogout() {
        // Attempt logout
        TransactionResult result = client.logout();
        if (result.isSuccess()) {
            loggedInUser = null;
            showLoginScreen();
        } else {
            JOptionPane.showMessageDialog(mainFrame, 
                "Error logging out: " + result.getMessage(),
                "Logout Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Shutdown the application
     */
    @Override
    public void shutdown() {
        // If logged in, attempt logout
        if (client.getLoggedInUser() != null) {
            client.logout();
        }
        
        // Disconnect from server
        client.disconnect();
        
        // Close the window and exit
        mainFrame.dispose();
        System.exit(0);
    }
    
    /**
     * Called when login is successful
     * @param username Username that logged in
     */
    @Override
    public void onLoginSuccess(String username) {
        loggedInUser = username;
        showDashboard();
    }
    
    /**
     * Called when login fails
     * @param errorMessage Error message explaining failure
     */
    @Override
    public void onLoginFailure(String errorMessage) {
        // Already handled by login panel
    }
    
    /**
     * Called when user requests to logout
     */
    @Override
    public void onLogoutRequested() {
        handleLogout();
    }
    
    /**
     * Called when dashboard detects the session is invalid
     */
    @Override
    public void onSessionInvalid() {
        JOptionPane.showMessageDialog(mainFrame, 
            "Your session has expired. Please login again.",
            "Session Expired", JOptionPane.WARNING_MESSAGE);
        showLoginScreen();
    }
    
    /**
     * Called when account has been deleted
     */
    @Override
    public void onAccountDeleted() {
        JOptionPane.showMessageDialog(mainFrame, 
            "Your account has been successfully deleted.",
            "Account Deleted", JOptionPane.INFORMATION_MESSAGE);
        loggedInUser = null;
        showLoginScreen();
    }
    
    /**
     * Called when balance changes
     * @param newBalance Updated balance
     */
    @Override
    public void onBalanceChanged(double newBalance) {
        // Can be used to update UI elements that show balance
    }
    
    /**
     * Main method to start the GUI application
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Use Swing's event dispatch thread for thread safety
        SwingUtilities.invokeLater(() -> {
            // Create the client
            final MarketplaceClient client = new MarketplaceClient();
            
            // Show connection dialog
            final String host = JOptionPane.showInputDialog("Enter server host:", "localhost");
            final String hostToUse = (host == null || host.trim().isEmpty()) ? "localhost" : host;
            
            // Handle port input
            String portStr = JOptionPane.showInputDialog("Enter server port:", "8080");
            int portValue = 8080; // Default port
            try {
                portValue = Integer.parseInt(portStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, 
                    "Invalid port. Using default: 8080", 
                    "Warning", JOptionPane.WARNING_MESSAGE);
            }
            final int port = portValue;
            
            // Connect to server
            boolean connected = client.connect(hostToUse, port);
            if (!connected) {
                JOptionPane.showMessageDialog(null, 
                    "Failed to connect to server at " + hostToUse + ":" + port, 
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            
            // Initialize and show GUI
            MarketplaceClientGUI gui = new MarketplaceClientGUI(client);
            gui.initialize();
        });
    }
}