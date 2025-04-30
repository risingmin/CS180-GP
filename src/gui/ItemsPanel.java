package gui;

import client.MarketplaceClient;
import database.Item;
import database.TransactionResult;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ItemsPanel class
 *
 * This class implements the items panel for the Marketplace client.
 * It handles displaying, searching, adding, and managing marketplace items.
 * 
 * @author L10-Team1 
 *
 * @version May 2024
 *
 */
public class ItemsPanel implements ItemsPanelInterface {
    
    private JPanel mainPanel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton addItemButton;
    private JButton refreshButton;
    private JTable itemsTable;
    private DefaultTableModel tableModel;
    private JButton buyButton;
    private JButton deleteButton;
    
    private MarketplaceClient client;
    private boolean myItemsMode;
    
    /**
     * Constructor for ItemsPanel
     * @param client The marketplace client to connect to backend
     * @param myItemsMode If true, shows only user's items with delete option, otherwise shows all items with buy option
     */
    public ItemsPanel(MarketplaceClient client, boolean myItemsMode) {
        this.client = client;
        this.myItemsMode = myItemsMode;
        initializeUI();
    }
    
    /**
     * Initialize the UI components
     */
    private void initializeUI() {
        mainPanel = new JPanel(new BorderLayout());
        
        // Create search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchItems(searchField.getText()));
        
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshItems());
        
        if (myItemsMode) {
            addItemButton = new JButton("Add Item");
            addItemButton.addActionListener(e -> showAddItemDialog());
            
            deleteButton = new JButton("Delete Item");
            deleteButton.addActionListener(e -> {
                int selectedRow = itemsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int itemId = (int) tableModel.getValueAt(selectedRow, 0);
                    deleteItem(itemId);
                } else {
                    JOptionPane.showMessageDialog(mainPanel,
                        "Please select an item to delete",
                        "Selection Required", JOptionPane.WARNING_MESSAGE);
                }
            });
            
            buttonPanel.add(addItemButton);
            buttonPanel.add(deleteButton);
        } else {
            buyButton = new JButton("Buy Item");
            buyButton.addActionListener(e -> {
                int selectedRow = itemsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int itemId = (int) tableModel.getValueAt(selectedRow, 0);
                    buyItem(itemId);
                } else {
                    JOptionPane.showMessageDialog(mainPanel,
                        "Please select an item to buy",
                        "Selection Required", JOptionPane.WARNING_MESSAGE);
                }
            });
            
            buttonPanel.add(buyButton);
        }
        
        buttonPanel.add(refreshButton);
        
        // Combine search and button panels
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Create table for items
        String[] columnNames;
        if (myItemsMode) {
            columnNames = new String[]{"ID", "Title", "Price", "Sold"};
        } else {
            columnNames = new String[]{"ID", "Title", "Price", "Seller"};
        }
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Integer.class;
                if (columnIndex == 2) return Double.class;
                if (columnIndex == 3 && myItemsMode) return Boolean.class;
                return String.class;
            }
        };
        
        itemsTable = new JTable(tableModel);
        itemsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemsTable.setAutoCreateRowSorter(true);
        
        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(itemsTable);
        
        // Add components to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Initially load items
        refreshItems();
    }
    
    /**
     * Get the JPanel containing the items UI
     * @return JPanel for items
     */
    @Override
    public JPanel getPanel() {
        return mainPanel;
    }
    
    /**
     * Refresh the items list from server
     */
    @Override
    public void refreshItems() {
        // Clear table
        tableModel.setRowCount(0);
        
        // Load items
        if (myItemsMode) {
            // Get user's items
            List<Item> items = client.getUserItems();
            for (Item item : items) {
                tableModel.addRow(new Object[]{
                    item.getId(),
                    item.getTitle(),
                    item.getPrice(),
                    item.isSold()
                });
            }
        } else {
            // Get all items (using empty search query)
            List<Item> items = client.searchItems("");
            for (Item item : items) {
                tableModel.addRow(new Object[]{
                    item.getId(),
                    item.getTitle(),
                    item.getPrice(),
                    item.getSeller()
                });
            }
        }
    }
    
    /**
     * Search for items based on a query
     * @param query The search query
     */
    @Override
    public void searchItems(String query) {
        // Clear table
        tableModel.setRowCount(0);
        
        if (myItemsMode) {
            // Get user's items and filter locally
            List<Item> items = client.getUserItems();
            String lowerQuery = query.toLowerCase();
            
            for (Item item : items) {
                if (item.getTitle().toLowerCase().contains(lowerQuery) || 
                    item.getDescription().toLowerCase().contains(lowerQuery)) {
                    
                    tableModel.addRow(new Object[]{
                        item.getId(),
                        item.getTitle(),
                        item.getPrice(),
                        item.isSold()
                    });
                }
            }
        } else {
            // Search server-side
            List<Item> items = client.searchItems(query);
            
            for (Item item : items) {
                tableModel.addRow(new Object[]{
                    item.getId(),
                    item.getTitle(),
                    item.getPrice(),
                    item.getSeller()
                });
            }
        }
    }
    
    /**
     * Show dialog to add a new item
     */
    private void showAddItemDialog() {
        // Create dialog components
        JTextField titleField = new JTextField(20);
        JTextField descriptionField = new JTextField(20);
        JTextField priceField = new JTextField(10);
        
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        
        // Show dialog
        int result = JOptionPane.showConfirmDialog(mainPanel, panel, 
            "Add Item", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String description = descriptionField.getText();
            double price;
            
            try {
                price = Double.parseDouble(priceField.getText());
                if (price <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(mainPanel,
                    "Please enter a valid price (greater than 0)",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            addItem(title, description, price);
        }
    }
    
    /**
     * Add a new item to the marketplace
     * @param title Item title
     * @param description Item description
     * @param price Item price
     * @return true if item added successfully, false otherwise
     */
    @Override
    public boolean addItem(String title, String description, double price) {
        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel,
                "Title and description cannot be empty",
                "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        TransactionResult result = client.addItem(title, description, price);
        
        if (result.isSuccess()) {
            JOptionPane.showMessageDialog(mainPanel,
                "Item added successfully! Item ID: " + result.getItemId(),
                "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshItems();
            return true;
        } else {
            JOptionPane.showMessageDialog(mainPanel,
                "Failed to add item: " + result.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Delete an item from the marketplace
     * @param itemId ID of item to delete
     * @return true if item deleted successfully, false otherwise
     */
    @Override
    public boolean deleteItem(int itemId) {
        int confirm = JOptionPane.showConfirmDialog(mainPanel,
            "Are you sure you want to delete this item?",
            "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return false;
        }
        
        TransactionResult result = client.deleteItem(itemId);
        
        if (result.isSuccess()) {
            JOptionPane.showMessageDialog(mainPanel,
                "Item deleted successfully",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshItems();
            return true;
        } else {
            JOptionPane.showMessageDialog(mainPanel,
                "Failed to delete item: " + result.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Buy an item from the marketplace
     * @param itemId ID of item to buy
     * @return true if purchase successful, false otherwise
     */
    @Override
    public boolean buyItem(int itemId) {
        int confirm = JOptionPane.showConfirmDialog(mainPanel,
            "Are you sure you want to buy this item?",
            "Confirm Purchase", JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return false;
        }
        
        TransactionResult result = client.buyItem(itemId);
        
        if (result.isSuccess()) {
            JOptionPane.showMessageDialog(mainPanel,
                "Purchase successful!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshItems();
            return true;
        } else {
            JOptionPane.showMessageDialog(mainPanel,
                "Failed to purchase item: " + result.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Show detailed view of an item
     * @param item Item to display details for
     */
    @Override
    public void showItemDetails(Item item) {
        if (item == null) return;
        
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("ID: " + item.getId()));
        panel.add(new JLabel("Title: " + item.getTitle()));
        panel.add(new JLabel("Description: " + item.getDescription()));
        panel.add(new JLabel("Price: $" + item.getPrice()));
        panel.add(new JLabel("Seller: " + item.getSeller()));
        panel.add(new JLabel("Status: " + (item.isSold() ? "Sold" : "Available")));
        
        JOptionPane.showMessageDialog(mainPanel, panel, 
            "Item Details", JOptionPane.INFORMATION_MESSAGE);
    }
}
