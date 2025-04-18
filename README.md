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

The client-server communication functionality can be tested manually as follows:

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
| Sungmin Lee     | Submitted Vocareum workspace                |

## Class Descriptions

### User
**Functionality:**
- Represents a user in the system with username, password, and balance
- Provides methods to get username, check password, get/set balance
- All users start with an initial, arbitrary balance of 100.0

**Testing:**
- Verified username retrieval functionality
- Confirmed that password validation works correctly for both correct and incorrect passwords
- Validated that initial balance is set to 100.0
- Tested balance modification including setting to positive, zero, and negative values

**Relationships:**
- Referenced by Database for user management
- Used by PaymentProcessor to handle transactions

### Item
**Functionality:**
- Represents an item in the marketplace with id, title, description, price, seller, and sold status
- Items are created with unsold status by default
- Provides methods to access all properties

**Testing:**
- Verified all getters and setters
- Confirmed default sold status is false
- Tested sold status modification
- Validated all item properties are stored and retrieved correctly

**Relationships:**
- Stored and managed by Database
- Used in Transaction to represent purchased items
- Referenced in Message for item-related messaging

### Message
**Functionality:**
- Represents communication between users about specific items
- Contains sender, recipient, content, timestamp, and associated item ID
- Timestamps are automatically generated upon message creation

**Testing:**
- Verified all message properties are stored and retrieved correctly
- Confirmed timestamp is generated appropriately
- Tested retrieval of sender, recipient, content, and associated item ID

**Relationships:**
- Stored and managed by Database
- Links users to items through communications

### Transaction
**Functionality:**
- Represents a financial transaction between a buyer and seller for a specific item
- Contains buyer, seller, item ID, amount, and timestamp
- Timestamps are automatically generated upon transaction creation

**Testing:**
- Verified all transaction properties are stored correctly
- Confirmed timestamp generation is appropriate
- Validated buyer, seller, item ID, and amount information

**Relationships:**
- Generated by PaymentProcessor
- Stored by Database
- Connects buyers and sellers to items

### TransactionResult
**Functionality:**
- Represents the outcome of a payment processing attempt
- Contains success status, message, and optionally an item ID
- Provides feedback on why a transaction succeeded or failed

**Testing:**
- Tested success and failure states
- Confirmed appropriate messages are stored
- Verified item ID handling for both provided and default cases

**Relationships:**
- Returned by PaymentProcessor to indicate transaction results

### PaymentProcessor
**Functionality:**
- Manages payment transactions between users
- Verifies sufficient funds before processing
- Updates user balances and marks items as sold
- Records transaction history

**Testing:**
- Tested sufficient funds verification for various scenarios
- Confirmed successful payment processing with balance updates
- Verified error handling for all potential failure cases
- Tested transaction history recording and retrieval

**Relationships:**
- Uses Database to access and modify user and item data
- Creates and manages Transaction objects
- Returns TransactionResult objects

### Database
**Functionality:**
- Central data store for the entire marketplace system
- Manages users, items, messages, and transactions
- Provides methods to search, add, update, and remove data

**Testing:**
- Verified user management (add, retrieve, remove)
- Confirmed item management (add, retrieve, remove, search)
- Tested message storage and retrieval
- Validated transaction recording and retrieval
- Confirmed data persistence through save/load operations

**Relationships:**
- Core component that interfaces with all other classes
- Used by PaymentProcessor for data access and modification

### DatabaseData
**Functionality:**
- Container class for serializable data
- Holds collections of users, items, messages, and transactions
- Maintains next item ID for sequential assignment

**Testing:**
- Verified all data collections are properly stored and retrieved
- Confirmed next item ID tracking
- Validated integrity of stored collections

**Relationships:**
- Used by Database for data serialization and storage

## Interface Descriptions

- **UserInterface**: Defines user functionality including username, password checking, and balance management
- **ItemInterface**: Defines item properties and behavior
- **MessageInterface**: Specifies message attributes and access methods
- **TransactionInterface**: Outlines transaction data structure
- **TransactionResultInterface**: Defines transaction outcome representation
- **PaymentProcessorInterface**: Specifies payment processing functionality
- **DatabaseInterface**: Defines database operations
- **DatabaseDataInterface**: Specifies data container requirements
