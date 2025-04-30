package gui;

import client.MarketplaceClient;
import database.Message;
import database.TransactionResult;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * MessagesPanel class
 *
 * This class implements the messages panel for the Marketplace client.
 * It handles viewing and sending messages between users.
 * 
 * @author L10-Team1 
 *
 * @version May 2024
 *
 */
public class MessagesPanel implements MessagesPanelInterface {
    
    private JPanel mainPanel;
    private JTable messagesTable;
    private DefaultTableModel tableModel;
    private JButton composeButton;
    private JButton refreshButton;
    private JButton viewButton;
    
    private MarketplaceClient client;
    private SimpleDateFormat dateFormat;
    
    /**
     * Constructor for MessagesPanel
     * @param client The marketplace client to connect to backend
     */
    public MessagesPanel(MarketplaceClient client) {
        this.client = client;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        initializeUI();
    }
    
    /**
     * Initialize the UI components
     */
    private void initializeUI() {
        mainPanel = new JPanel(new BorderLayout());
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        composeButton = new JButton("Compose Message");
        composeButton.addActionListener(e -> showComposeDialog(null, 0));
        
        viewButton = new JButton("View Message");
        viewButton.addActionListener(e -> {
            int selectedRow = messagesTable.getSelectedRow();
            if (selectedRow >= 0) {
                showMessageDetails(selectedRow);
            } else {
                JOptionPane.showMessageDialog(mainPanel,
                    "Please select a message to view",
                    "Selection Required", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshMessages());
        
        buttonPanel.add(composeButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(refreshButton);
        
        // Create table for messages
        String[] columnNames = {"From", "To", "Preview", "Date", "Item ID"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) return Integer.class;
                return String.class;
            }
        };
        
        messagesTable = new JTable(tableModel);
        messagesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(messagesTable);
        
        // Add components to main panel
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Initially load messages
        refreshMessages();
    }
    
    /**
     * Get the JPanel containing the messages UI
     * @return JPanel for messages
     */
    @Override
    public JPanel getPanel() {
        return mainPanel;
    }
    
    /**
     * Refresh messages from server
     */
    @Override
    public void refreshMessages() {
        // Clear table
        tableModel.setRowCount(0);
        
        // Load messages
        List<Message> messages = client.getMessages();
        for (Message message : messages) {
            String preview = message.getContent();
            if (preview.length() > 30) {
                preview = preview.substring(0, 27) + "...";
            }
            
            tableModel.addRow(new Object[]{
                message.getSender(),
                message.getRecipient(),
                preview,
                dateFormat.format(message.getTimestamp()),
                message.getItemId()
            });
        }
    }
    
    /**
     * Show details of a selected message
     * @param rowIndex Row index in table
     */
    private void showMessageDetails(int rowIndex) {
        List<Message> messages = client.getMessages();
        if (rowIndex < 0 || rowIndex >= messages.size()) return;
        
        Message message = messages.get(rowIndex);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(400, 300));
        
        JPanel headerPanel = new JPanel(new GridLayout(0, 2));
        headerPanel.add(new JLabel("From:"));
        headerPanel.add(new JLabel(message.getSender()));
        headerPanel.add(new JLabel("To:"));
        headerPanel.add(new JLabel(message.getRecipient()));
        headerPanel.add(new JLabel("Date:"));
        headerPanel.add(new JLabel(dateFormat.format(message.getTimestamp())));
        headerPanel.add(new JLabel("Item ID:"));
        headerPanel.add(new JLabel(String.valueOf(message.getItemId())));
        
        JTextArea contentArea = new JTextArea(message.getContent());
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane contentScroll = new JScrollPane(contentArea);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentScroll, BorderLayout.CENTER);
        
        // Add reply button if the message is from someone else
        if (!message.getSender().equals(client.getLoggedInUser())) {
            JButton replyButton = new JButton("Reply");
            replyButton.addActionListener(e -> {
                JDialog dialog = (JDialog) SwingUtilities.getWindowAncestor(replyButton);
                if (dialog != null) {
                    dialog.dispose();
                }
                showComposeDialog(message.getSender(), message.getItemId());
            });
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(replyButton);
            panel.add(buttonPanel, BorderLayout.SOUTH);
        }
        
        JOptionPane.showOptionDialog(mainPanel, panel, 
            "Message Details", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, 
            null, new Object[]{}, null);
    }
    
    /**
     * Show message composition dialog
     * @param recipient Pre-filled recipient (can be null)
     * @param itemId Pre-filled item ID (0 for none)
     */
    @Override
    public void showComposeDialog(String recipient, int itemId) {
        JTextField toField = new JTextField(20);
        if (recipient != null) {
            toField.setText(recipient);
        }
        
        JTextField itemIdField = new JTextField(10);
        if (itemId > 0) {
            itemIdField.setText(String.valueOf(itemId));
        } else {
            itemIdField.setText("0");
        }
        
        JTextArea contentArea = new JTextArea(10, 30);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane contentScroll = new JScrollPane(contentArea);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JPanel toPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toPanel.add(new JLabel("To:"));
        toPanel.add(toField);
        
        JPanel itemIdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        itemIdPanel.add(new JLabel("Item ID (0 for general):"));
        itemIdPanel.add(itemIdField);
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(new JLabel("Message:"), BorderLayout.NORTH);
        contentPanel.add(contentScroll, BorderLayout.CENTER);
        
        panel.add(toPanel);
        panel.add(itemIdPanel);
        panel.add(contentPanel);
        
        int result = JOptionPane.showConfirmDialog(mainPanel, panel,
            "Compose Message", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            String to = toField.getText();
            String content = contentArea.getText();
            int itemIdValue;
            
            try {
                itemIdValue = Integer.parseInt(itemIdField.getText());
                if (itemIdValue < 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(mainPanel,
                    "Please enter a valid Item ID (0 or positive number)",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            sendMessage(to, content, itemIdValue);
        }
    }
    
    /**
     * Send a new message
     * @param recipient Username of message recipient
     * @param content Message content
     * @param itemId ID of related item (0 for general messages)
     * @return true if message sent successfully, false otherwise
     */
    @Override
    public boolean sendMessage(String recipient, String content, int itemId) {
        if (recipient.trim().isEmpty() || content.trim().isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel,
                "Recipient and message content cannot be empty",
                "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        TransactionResult result = client.sendMessage(recipient, content, itemId);
        
        if (result.isSuccess()) {
            JOptionPane.showMessageDialog(mainPanel,
                "Message sent successfully",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshMessages();
            return true;
        } else {
            JOptionPane.showMessageDialog(mainPanel,
                "Failed to send message: " + result.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
