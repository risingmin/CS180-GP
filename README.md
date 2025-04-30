# CS180 Group Project - Marketplace
## L10-Team1 Phase 2
### Sungmin Lee, Justin Zheng

## Compilation and Running Instructions

### Compilation
To compile the project, open a terminal in the project root directory and run:

```bash
javac -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar -d bin src/*/*.java
```

### Running the Server
To run the server:

```bash
java -cp bin server.MarketplaceServerMain
```

### Running the Client
To run the command-line client:

```bash
java -cp bin client.MarketplaceClientMain
```

To run the GUI client:

```bash
java -cp bin gui.MarketplaceClientGUI
```


### Running Tests
To run all JUnit tests:

```bash
java -cp bin:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore database.DatabaseTest database.DatabaseDataTest database.ItemTest database.MessageTest database.PaymentProcessorTest database.TransactionTest database.TransactionResultTest database.UserTest server.MarketplaceServerTest client.MarketplaceClientTest
```

To run a specific test class (ex. UserTest):

```bash
java -cp bin:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore database.UserTest
```

## I/O Testing Information

1. **Starting the Server**
   - Run the server using the command provided above
   - The server will start and display "Server started on port 8080"

2. **Connecting to the Server**
   - Run the client using the command provided above
   - The client will attempt to connect to the server
   - If successful, it will display "Connected to server"

3. **User Registration**
   - Select option 1 to register a new user
   - Enter a username and password
   - The client will display "Registration successful" or an error message

4. **User Login**
   - Select option 2 to login
   - Enter your username and password
   - The client will display "Login successful" or "Invalid username or password"

5. **Adding Items**
   - After logging in, select option 3 to add an item
   - Enter the title, description, and price
   - The client will display "Item added successfully" with the item ID

6. **Searching Items**
   - Select option 4 to search for items
   - Enter a search query
   - The client will display a list of matching items

7. **Buying Items**
   - Select option 5 to buy an item
   - Enter the item ID
   - The client will display "Payment processed successfully" or an error message

8. **Viewing User Items**
   - Select option 6 to view your listed items
   - The client will display a list of your items

9. **Sending Messages**
   - Select option 7 to send a message
   - Enter the recipient username, message content, and related item ID
   - The client will display "Message sent successfully" or an error message

10. **Viewing Messages**
    - Select option 8 to view your messages
    - The client will display a list of messages sent to or from you

11. **Viewing Transactions**
    - Select option 9 to view your transactions
    - The client will display a list of your buying and selling transactions

12. **Checking Balance**
    - Select option 10 to check your balance
    - The client will display your current balance

13. **Deleting Items**
    - Select option 11 to delete an item
    - Enter the ID of the item to delete
    - The client will display a confirmation or error message

14. **Deleting Account**
    - Select option 12 to delete your account
    - Confirm the deletion
    - The client will display a success message or error message

15. **Logging Out**
    - Select option 13 to logout
    - The client will display "Logout successful"

16. **Exiting the Client**
    - Select option 14 to exit
    - The client will disconnect from the server and exit

17. **GUI Client Testing**
    - Run the GUI client using the command provided above
    - Enter server host and port when prompted
    - Use the login screen to register or login
    - Navigate between marketplace items, user items, messages, and account tabs
    - Test all functions including buying, selling, sending messages, and account management

18. **Multiple Clients**
    - Run multiple client instances simultaneously
    - Each client should be able to interact with the server independently
    - Test concurrent actions like multiple users trying to buy the same item

19. **Server Persistence**
    - After shutting down the server and restarting it, all data should persist
    - Users should be able to login with existing credentials
    - Previously listed items should still be available

## Submission Information

| Team Member     | Submission Responsibility                   |
|-----------------|---------------------------------------------|
| Justin Zheng    | Submitted Vocareum workspace                |

## Class Descriptions

### User
**Functionality:**
- User account with username, password, and balance

**Testing:**
- Username, password, and balance logic tested

**Relationships:**
- Managed by Database, used by PaymentProcessor

### Item
**Functionality:**
- Marketplace item with id, title, description, price, seller, and sold status

**Testing:**
- All fields and sold status tested

**Relationships:**
- Managed by Database, referenced in Transaction and Message

### Message
**Functionality:**
- Message between users about an item

**Testing:**
- All fields and timestamp tested

**Relationships:**
- Managed by Database

### Transaction
**Functionality:**
- Buyer/seller transaction for an item

**Testing:**
- All fields and timestamp tested

**Relationships:**
- Created by PaymentProcessor, stored in Database

### TransactionResult
**Functionality:**
- Result of a payment attempt

**Testing:**
- Success/failure and item ID tested

**Relationships:**
- Returned by PaymentProcessor

### PaymentProcessor
**Functionality:**
- Handles payments and updates balances

**Testing:**
- Payment logic and error cases tested

**Relationships:**
- Uses Database, creates Transactions

### Database
**Functionality:**
- Stores users, items, messages, and transactions

**Testing:**
- CRUD and persistence tested

**Relationships:**
- Used by all server logic

### DatabaseData
**Functionality:**
- Serializable container for all marketplace data

**Testing:**
- Data integrity tested

**Relationships:**
- Used for persistence

### MarketplaceServer
**Functionality:**
- Server for client connections and marketplace operations

**Testing:**
- Startup, shutdown, and command handling tested

**Relationships:**
- Uses Database, implements MarketplaceServerInterface

### MarketplaceClient
**Functionality:**
- Client for connecting to the server and performing actions

**Testing:**
- Connection, command, and error handling tested

**Relationships:**
- Implements MarketplaceClientInterface

### MarketplaceClientMain
**Functionality:**
- Command-line interface for the client

**Testing:**
- Manual testing of user commands

**Relationships:**
- Uses MarketplaceClient

### MarketplaceServerMain
**Functionality:**
- Main class to initialize and run the server

**Testing:**
- Manual testing of server startup and shutdown

**Relationships:**
- Uses MarketplaceServer

### MarketplaceClientGUI
**Functionality:**
- GUI interface for the client with navigation between panels

**Testing:**
- Manual testing of GUI components and interactions

**Relationships:**
- Uses MarketplaceClient, manages GUI panels

### LoginPanel
**Functionality:**
- Login and registration UI panel

**Testing:**
- Manual testing of authentication flow

**Relationships:**
- Used by MarketplaceClientGUI, implements LoginPanelInterface

### DashboardPanel
**Functionality:**
- Main dashboard panel with tabs for different functionality areas

**Testing:**
- Manual testing of navigation and data display

**Relationships:**
- Used by MarketplaceClientGUI, implements DashboardPanelInterface

### ItemsPanel
**Functionality:**
- Panel for viewing, searching, adding, buying and managing items

**Testing:**
- Manual testing of item management operations

**Relationships:**
- Used by DashboardPanel, implements ItemsPanelInterface

### AccountPanel
**Functionality:**
- Panel for viewing balance, transaction history, and account management

**Testing:**
- Manual testing of account operations and balance display

**Relationships:**
- Used by DashboardPanel, implements AccountPanelInterface

### MessagesPanel
**Functionality:**
- Panel for viewing and sending messages

**Testing:**
- Manual testing of messaging functionality

**Relationships:**
- Used by DashboardPanel, implements MessagesPanelInterface

## Interface Descriptions

- **UserInterface**: User account methods
- **ItemInterface**: Item properties
- **MessageInterface**: Message properties
- **TransactionInterface**: Transaction properties
- **TransactionResultInterface**: Transaction result
- **PaymentProcessorInterface**: Payment processing
- **DatabaseInterface**: Database operations
- **DatabaseDataInterface**: Data container
- **MarketplaceServerInterface**: Server operations
- **MarketplaceClientInterface**: Client operations
- **MarketplaceClientGUIInterface**: GUI client operations
- **LoginPanelInterface**: Login UI operations
- **DashboardPanelInterface**: Dashboard UI operations
- **ItemsPanelInterface**: Item management UI operations
- **AccountPanelInterface**: Account management UI operations
- **MessagesPanelInterface**: Messaging UI operations