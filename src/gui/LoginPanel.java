package gui;

import client.MarketplaceClient;
import database.TransactionResult;

import javax.swing.*;
import java.awt.*;

/**
 * LoginPanel class
 *
 * This class implements the login and registration panel for the Marketplace client.
 * It handles user authentication and registration.
 * 
 * @author L10-Team1 
 *
 * @version May 2024
 *
 */
public class LoginPanel implements LoginPanelInterface {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JPanel loginTab;
    private JPanel registerTab;
    
    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JButton loginButton;
    
    private JTextField registerUsernameField;
    private JPasswordField registerPasswordField;
    private JPasswordField registerConfirmField;
    private JButton registerButton;
    
    private MarketplaceClient client;
    private LoginCallback callback;
    
    /**
     * Constructor for LoginPanel
     * @param client The marketplace client to connect to backend
     */
    public LoginPanel(MarketplaceClient client) {
        this.client = client;
        initializeUI();
    }
    
    /**
     * Initialize the UI components
     */
    private void initializeUI() {
        mainPanel = new JPanel(new BorderLayout());
        
        // Create header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel("Marketplace Client");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Login or Register to continue");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(Box.createVerticalStrut(20));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(subtitleLabel);
        headerPanel.add(Box.createVerticalStrut(20));
        
        // Create tabbed pane for login and register
        tabbedPane = new JTabbedPane();
        
        // Build login panel
        loginTab = new JPanel();
        loginTab.setLayout(new BoxLayout(loginTab, BoxLayout.Y_AXIS));
        
        JPanel loginUsernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel loginUsernameLabel = new JLabel("Username: ");
        loginUsernameField = new JTextField(20);
        loginUsernamePanel.add(loginUsernameLabel);
        loginUsernamePanel.add(loginUsernameField);
        
        JPanel loginPasswordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel loginPasswordLabel = new JLabel("Password: ");
        loginPasswordField = new JPasswordField(20);
        loginPasswordPanel.add(loginPasswordLabel);
        loginPasswordPanel.add(loginPasswordField);
        
        JPanel loginButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String username = loginUsernameField.getText();
            String password = new String(loginPasswordField.getPassword());
            processLogin(username, password);
        });
        loginButtonPanel.add(loginButton);
        
        loginTab.add(Box.createVerticalStrut(30));
        loginTab.add(loginUsernamePanel);
        loginTab.add(loginPasswordPanel);
        loginTab.add(Box.createVerticalStrut(20));
        loginTab.add(loginButtonPanel);
        loginTab.add(Box.createVerticalGlue());
        
        // Build register panel
        registerTab = new JPanel();
        registerTab.setLayout(new BoxLayout(registerTab, BoxLayout.Y_AXIS));
        
        JPanel registerUsernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel registerUsernameLabel = new JLabel("Username: ");
        registerUsernameField = new JTextField(20);
        registerUsernamePanel.add(registerUsernameLabel);
        registerUsernamePanel.add(registerUsernameField);
        
        JPanel registerPasswordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel registerPasswordLabel = new JLabel("Password: ");
        registerPasswordField = new JPasswordField(20);
        registerPasswordPanel.add(registerPasswordLabel);
        registerPasswordPanel.add(registerPasswordField);
        
        JPanel registerConfirmPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel registerConfirmLabel = new JLabel("Confirm: ");
        registerConfirmField = new JPasswordField(20);
        registerConfirmPanel.add(registerConfirmLabel);
        registerConfirmPanel.add(registerConfirmField);
        
        JPanel registerButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            String username = registerUsernameField.getText();
            String password = new String(registerPasswordField.getPassword());
            String confirm = new String(registerConfirmField.getPassword());
            
            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(mainPanel, 
                    "Passwords do not match", 
                    "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            processRegistration(username, password);
        });
        registerButtonPanel.add(registerButton);
        
        registerTab.add(Box.createVerticalStrut(30));
        registerTab.add(registerUsernamePanel);
        registerTab.add(registerPasswordPanel);
        registerTab.add(registerConfirmPanel);
        registerTab.add(Box.createVerticalStrut(20));
        registerTab.add(registerButtonPanel);
        registerTab.add(Box.createVerticalGlue());
        
        // Add tabs to tabbed pane
        tabbedPane.addTab("Login", loginTab);
        tabbedPane.addTab("Register", registerTab);
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Add bottom padding
        mainPanel.add(Box.createVerticalStrut(30), BorderLayout.SOUTH);
    }
    
    /**
     * Get the JPanel containing the login UI
     * @return JPanel for login
     */
    @Override
    public JPanel getPanel() {
        return mainPanel;
    }
    
    /**
     * Process login attempt with credentials
     * @param username Username to login with
     * @param password Password for login
     * @return true if login successful, false otherwise
     */
    @Override
    public boolean processLogin(String username, String password) {
        if (username.trim().isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, 
                "Username and password cannot be empty", 
                "Login Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        TransactionResult result = client.login(username, password);
        
        if (result.isSuccess()) {
            if (callback != null) {
                callback.onLoginSuccess(username);
            }
            return true;
        } else {
            JOptionPane.showMessageDialog(mainPanel, 
                result.getMessage(), 
                "Login Error", JOptionPane.ERROR_MESSAGE);
            if (callback != null) {
                callback.onLoginFailure(result.getMessage());
            }
            return false;
        }
    }
    
    /**
     * Process registration attempt with new credentials
     * @param username Username to register
     * @param password Password to set
     * @return true if registration successful, false otherwise
     */
    @Override
    public boolean processRegistration(String username, String password) {
        if (username.trim().isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, 
                "Username and password cannot be empty", 
                "Registration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        TransactionResult result = client.register(username, password);
        
        if (result.isSuccess()) {
            JOptionPane.showMessageDialog(mainPanel, 
                "Registration successful! You can now log in.", 
                "Registration Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Switch to login tab and prefill username
            tabbedPane.setSelectedIndex(0);
            loginUsernameField.setText(username);
            loginPasswordField.setText("");
            
            return true;
        } else {
            JOptionPane.showMessageDialog(mainPanel, 
                result.getMessage(), 
                "Registration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Set the callback interface to notify about login events
     * @param callback Callback interface implementation
     */
    @Override
    public void setLoginCallback(LoginCallback callback) {
        this.callback = callback;
    }
}
