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
To run the client:

```bash
java -cp bin client.MarketplaceClientMain
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

13. **Logging Out**
    - Select option 11 to logout
    - The client will display "Logout successful"

14. **Exiting the Client**
    - Select option 12 to exit
    - The client will disconnect from the server and exit

15. **Multiple Clients**
    - Run multiple client instances simultaneously
    - Each client should be able to interact with the server independently
    - Test concurrent actions like multiple users trying to buy the same item

16. **Server Persistence**
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