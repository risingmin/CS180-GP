package gui;

import javax.swing.JPanel;

/**
 * MessagesPanelInterface
 *
 * This interface defines the required functionality for the messages panel,
 * which includes viewing, sending, and managing user messages.
 * 
 * @author L10-Team1 
 *
 * @version May 2024
 */
public interface MessagesPanelInterface {
    /**
     * Get the JPanel containing the messages UI
     * @return JPanel for messages
     */
    JPanel getPanel();
    
    /**
     * Refresh messages from server
     */
    void refreshMessages();
    
    /**
     * Send a new message
     * @param recipient Username of message recipient
     * @param content Message content
     * @param itemId ID of related item (0 for general messages)
     * @return true if message sent successfully, false otherwise
     */
    boolean sendMessage(String recipient, String content, int itemId);
    
    /**
     * Show message composition dialog
     * @param recipient Pre-filled recipient (can be null)
     * @param itemId Pre-filled item ID (0 for none)
     */
    void showComposeDialog(String recipient, int itemId);
}
