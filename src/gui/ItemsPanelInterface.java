package gui;

import javax.swing.JPanel;
import database.Item;

/**
 * ItemsPanelInterface
 *
 * This interface defines the required functionality for the items panel,
 * which includes searching, displaying, and managing marketplace items.
 * 
 * @author L10-Team1 
 *
 * @version May 2024
 */
public interface ItemsPanelInterface {
    /**
     * Get the JPanel containing the items UI
     * @return JPanel for items
     */
    JPanel getPanel();
    
    /**
     * Refresh the items list from server
     */
    void refreshItems();
    
    /**
     * Search for items based on a query
     * @param query The search query
     */
    void searchItems(String query);
    
    /**
     * Add a new item to the marketplace
     * @param title Item title
     * @param description Item description
     * @param price Item price
     * @return true if item added successfully, false otherwise
     */
    boolean addItem(String title, String description, double price);
    
    /**
     * Delete an item from the marketplace
     * @param itemId ID of item to delete
     * @return true if item deleted successfully, false otherwise
     */
    boolean deleteItem(int itemId);
    
    /**
     * Buy an item from the marketplace
     * @param itemId ID of item to buy
     * @return true if purchase successful, false otherwise
     */
    boolean buyItem(int itemId);
    
    /**
     * Show detailed view of an item
     * @param item Item to display details for
     */
    void showItemDetails(Item item);
}
