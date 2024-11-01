import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

public class BankingApplicationTest {
    private CustomerService customerService;
    private UserData user;

    @BeforeEach
    public void setUp() {
        // Initialize CustomerService and create a test user
        customerService = new CustomerService();
        user = customerService.signUp("Test User", "test@example.com", "password123");
    }

    @Test
    public void testUserRegistration() {
        assertNotNull(user, "User should be successfully registered.");
        assertEquals("Test User", user.getName(), "User name should match.");
        assertEquals("test@example.com", user.getEmail(), "User email should match.");
        assertTrue(user.getAccountNumber().startsWith("ACCT"), "Account number should start with 'ACCT'.");
    }

    @Test
    public void testLoginSuccess() {
        UserData loggedInUser = customerService.login("test@example.com", "password123");
        assertNotNull(loggedInUser, "Login should succeed for valid credentials.");
        assertEquals(user.getEmail(), loggedInUser.getEmail(), "Logged in user's email should match.");
    }

    @Test
    public void testLoginFailure() {
        UserData loggedInUser = customerService.login("test@example.com", "wrongpassword");
        assertNull(loggedInUser, "Login should fail for invalid credentials.");
    }

    @Test
    public void testDeposit() {
        BankAccount bankAccount = new BankAccount(user);
        double initialBalance = bankAccount.checkBalance();
        double depositAmount = 100.0;

        bankAccount.deposit(depositAmount);

        assertEquals(initialBalance + depositAmount, bankAccount.checkBalance(), "Balance should be updated after deposit.");
    }

    @Test
    public void testWithdraw() {
        BankAccount bankAccount = new BankAccount(user);
        bankAccount.deposit(200.0); // Deposit to ensure there are funds
        double initialBalance = bankAccount.checkBalance();
        double withdrawAmount = 100.0;

        bankAccount.withdraw(withdrawAmount);

        assertEquals(initialBalance - withdrawAmount, bankAccount.checkBalance(), "Balance should be updated after withdrawal.");
    }

    @Test
    public void testWithdrawInsufficientFunds() {
        BankAccount bankAccount = new BankAccount(user);
        double initialBalance = bankAccount.checkBalance();
        bankAccount.withdraw(initialBalance + 100.0); // Attempt to withdraw more than balance

        assertEquals(initialBalance, bankAccount.checkBalance(), "Balance should not change after failed withdrawal.");
    }

    @Test
    public void testTransfer() {
        // Create another user for the transfer
        UserData recipient = customerService.signUp("Recipient User", "recipient@example.com", "password456");
        BankAccount senderAccount = new BankAccount(user);
        BankAccount recipientAccount = new BankAccount(recipient);

        senderAccount.deposit(200.0); // Deposit to sender's account

        double initialSenderBalance = senderAccount.checkBalance();
        double initialRecipientBalance = recipientAccount.checkBalance();
        double transferAmount = 50.0;

        senderAccount.Transfer(recipient.getEmail(), transferAmount);

        assertEquals(initialSenderBalance - transferAmount, senderAccount.checkBalance(), "Sender's balance should be reduced after transfer.");
       
    }

    @Test
    public void testCheckBalance() {
        BankAccount bankAccount = new BankAccount(user);
        bankAccount.deposit(150.0);
        assertEquals(150.0, bankAccount.checkBalance(), "Check balance should return the correct amount.");
    }

    @Test
    public void testFileCreation() {
        // Check if required files exist
        File customersFile = new File("Customers.ser");
        File transactionsFile = new File("TransactionHistory.ser");
        assertTrue(customersFile.exists(), "Customers.ser file should exist.");
        assertTrue(transactionsFile.exists(), "TransactionHistory.ser file should exist.");
    }

    @AfterEach
    public void tearDown() {
        // Optionally clean up test data
        List<UserData> customers = customerService.deserializeCustomers();
        customers.removeIf(c -> c.getEmail().equals("test@example.com") || c.getEmail().equals("recipient@example.com"));
        customerService.saveCustomers(customers);
    }
}
