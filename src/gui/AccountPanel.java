package gui;

import client.MarketplaceClient;
import database.Transaction;
import database.TransactionResult;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * AccountPanel class
 *
 * This class implements the account panel for the Marketplace client.
 * It handles displaying user account information, transaction history,
 * and account management functionality like balance updates and deletion.
 * 
 * @author L10-Team1 
 *
 * @version May 2024
 *
 */
public class AccountPanel implements AccountPanelInterface {
    
    private JPanel mainPanel;
    private JLabel balanceLabel;
    private JButton refreshButton;
    private JButton deleteAccountButton;
    private JTable transactionsTable;
    private DefaultTableModel tableModel;
    
    private MarketplaceClient client;
    private AccountCallback callback;
    private SimpleDateFormat dateFormat;
    private double currentBalance;
    
    /**
     * Constructor for AccountPanel
     * @param client The marketplace client to connect to backend
     */
    public AccountPanel(MarketplaceClient client) {
        this.client = client;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        initializeUI();
    }
    
    /**
     * Initialize the UI components
     */
    private void initializeUI() {
        mainPanel = new JPanel(new BorderLayout());
        
        // Create account info panel
        JPanel accountInfoPanel = new JPanel(new BorderLayout());
        
        // Balance display
        JPanel balancePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        balanceLabel = new JLabel("Current Balance: $0.00");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        balancePanel.add(balanceLabel);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        refreshButton = new JButton("Refresh Transactions");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 12));
        refreshButton.addActionListener(e -> refreshAccountData());
        
        deleteAccountButton = new JButton("Delete Account");
        deleteAccountButton.addActionListener(e -> deleteAccount());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteAccountButton);
        
        accountInfoPanel.add(balancePanel, BorderLayout.WEST);
        accountInfoPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Create transactions label
        JLabel transactionsLabel = new JLabel("Transaction History");
        transactionsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        transactionsLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 0));
        
        // Create transactions table
        String[] columnNames = {"Transaction ID", "Item ID", "Type", "Amount", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 1) return Integer.class;
                if (columnIndex == 3) return Double.class;
                return String.class;
            }
        };
        
        transactionsTable = new JTable(tableModel);
        transactionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        transactionsTable.setAutoCreateRowSorter(true);
        
        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(transactionsTable);
        
        // Add components to main panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(accountInfoPanel, BorderLayout.NORTH);
        topPanel.add(transactionsLabel, BorderLayout.SOUTH);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Initially load data
        refreshAccountData();
    }
    
    /**
     * Get the JPanel containing the account UI
     * @return JPanel for account
     */
    @Override
    public JPanel getPanel() {
        return mainPanel;
    }
    
    /**
     * Refresh account data from server
     */
    @Override
    public void refreshAccountData() {
        // Update balance
        currentBalance = client.getBalance();
        balanceLabel.setText(String.format("Current Balance: $%.2f", currentBalance));
        
        // Update transactions table
        tableModel.setRowCount(0);
        
        List<Transaction> transactions = client.getTransactions();() + " transactions");
        int rowNum = 1;  // Counter for transaction numbers
        for (Transaction transaction : transactions) { // Counter for transaction numbers
            String type;
            if (transaction.getBuyer().equals(client.getLoggedInUser())) {
                type = "Purchase from " + transaction.getSeller();saction.getBuyer().equals(client.getLoggedInUser())) {
            } else {ller();
                type = "Sale to " + transaction.getBuyer();   System.out.println("Found purchase: Item ID " + transaction.getItemId() + 
            }                     " from " + transaction.getSeller() + 
             transaction.getAmount());
            tableModel.addRow(new Object[]{
                rowNum++,  // Use row number instead of transaction.getId()saction.getBuyer();
                transaction.getItemId(),m.out.println("Found sale: Item ID " + transaction.getItemId() + 
                type,+ transaction.getBuyer() + 
                transaction.getAmount(),ount());
                dateFormat.format(transaction.getTimestamp())
            });   
        }    tableModel.addRow(new Object[]{
        
        // Notify callback if balance has changedItemId(),
        if (callback != null) {
            callback.onBalanceChanged(currentBalance);       transaction.getAmount(),
        }           dateFormat.format(transaction.getTimestamp())
    }        });
     }
    /**
     * Get the current user's balanceEmpty()) {
     * @return User balance     System.out.println("No transactions found for user: " + client.getLoggedInUser());
     */
    @Override
    public double getBalance() {balance has changed
        return currentBalance;   if (callback != null) {
    }        callback.onBalanceChanged(currentBalance);
     }
    /**
     * Delete the user's account after confirmation
     * @return true if account deleted successfully, false otherwise
     */e current user's balance
    @Override
    public boolean deleteAccount() {
        int confirm = JOptionPane.showConfirmDialog(mainPanel,
            "Are you sure you want to delete your account? This cannot be undone.",
            "Confirm Account Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);return currentBalance;
        
        if (confirm != JOptionPane.YES_OPTION) {
            return false;
        }elete the user's account after confirmation
        cessfully, false otherwise
        // Ask for additional confirmation
        int secondConfirm = JOptionPane.showConfirmDialog(mainPanel,
            "This will permanently delete all your data including items and transaction history.\n" +
            "Please confirm once more to proceed.",
            "Final Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);    "Are you sure you want to delete your account? This cannot be undone.",
        S_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (secondConfirm != JOptionPane.YES_OPTION) {
            return false;f (confirm != JOptionPane.YES_OPTION) {
        }    return false;
        
        TransactionResult result = client.deleteAccount();
        firmation
        if (result.isSuccess()) {Pane.showConfirmDialog(mainPanel,
            if (callback != null) {l your data including items and transaction history.\n" +
                callback.onAccountDeleted();Please confirm once more to proceed.",
            }rmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            return true;
        } else { {
            JOptionPane.showMessageDialog(mainPanel,
                "Failed to delete account: " + result.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;ransactionResult result = client.deleteAccount();
        }   
    }    if (result.isSuccess()) {
         if (callback != null) {
    /**
     * Set the callback interface for account events
     * @param callback Callback interface implementation     return true;
     */e {
    @Override
    public void setAccountCallback(AccountCallback callback) { account: " + result.getMessage(),
        this.callback = callback;           "Error", JOptionPane.ERROR_MESSAGE);
    }           return false;
}        }

    }
    
    /**
     * Set the callback interface for account events
     * @param callback Callback interface implementation
     */
    @Override
    public void setAccountCallback(AccountCallback callback) {
        this.callback = callback;
    }
}
