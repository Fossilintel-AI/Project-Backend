import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BankAccount implements Serializable {
    private UserData customer;  // Associated customer
    private List<UserData> transactions;  // Store transactions temporarily
    private static final long serialVersionUID = 1L;

    // Constructor
    public BankAccount(UserData customer) {
        this.customer = customer;
        this.transactions = new ArrayList<>();  // Initialize transaction list
    }

    // Deposit method
    public void deposit(double amount) {
        if (amount > 0) {
            double newBalance = customer.getBalance() + amount;
            customer.setBalance(newBalance);

            UserData transaction = new UserData(customer.getAccountNumber(), "Deposit", amount, newBalance - amount, newBalance, getCurrentDateTime());
            transactions.add(transaction);  // Temporarily store transaction
            appendTransaction(transaction);  // Append transaction to file
            updateCustomerBalanceInFile(newBalance,customer);  // Update customer balance in file

            System.out.println("Deposit successful. New balance: " + newBalance);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    // Withdraw method
    public void withdraw(double amount) {
        if (amount > 0 && customer.getBalance() >= amount) {
            double newBalance = customer.getBalance() - amount;
            customer.setBalance(newBalance);

            UserData transaction = new UserData(customer.getAccountNumber(), "Withdrawal", amount, newBalance + amount, newBalance, getCurrentDateTime());
            transactions.add(transaction);  // Temporarily store transaction
            appendTransaction(transaction);  // Append transaction to file
            updateCustomerBalanceInFile(newBalance,customer);  // Update customer balance in file

            System.out.println("Withdrawal successful. New balance: " + newBalance);
        } else {
            System.out.println("Insufficient funds or invalid amount.");
        }
    }

    public void Transfer(String email, double amount)
    {
        UserData traverser = new UserData();
        UserData recipient = traverser.isUserRegistered(email);

        double senderNewBalance = customer.getBalance() - amount;
        customer.setBalance(senderNewBalance);

        UserData senderTransaction = new UserData(
                customer.getAccountNumber(),
                "Transfer",
                amount,
                senderNewBalance + amount,
                senderNewBalance,
                getCurrentDateTime()
        );

        transactions.add(senderTransaction);  // Add to temporary list
        appendTransaction(senderTransaction);  // Append to TransactionHistory.ser
        updateCustomerBalanceInFile(senderNewBalance,customer);  // Update the sender's balance in Customers.ser

        // Print confirmation for sender
        System.out.println("Transfer successful. New balance: R" + senderNewBalance);


        if (recipient != null) {
            // Step 5a: Increment recipient's balance
            double recipientNewBalance = recipient.getBalance() + amount;
            recipient.setBalance(recipientNewBalance);

            // Step 5b: Create a "Credit Transfer" transaction for the recipient
            UserData recipientTransaction = new UserData(
                    recipient.getAccountNumber(),
                    "Transfer",
                    amount,
                    recipientNewBalance - amount,
                    recipientNewBalance,
                    getCurrentDateTime()
            );

            transactions.add(recipientTransaction);  // Add to temporary list
            appendTransaction(recipientTransaction);  // Append to TransactionHistory.ser
            updateCustomerBalanceInFile(senderNewBalance,recipient);

            System.out.println("Recipient's account credited with R" + amount + ". New balance: R" + recipientNewBalance);
        }

    }



    // Append a transaction to the TransactionHistory.ser file
    private void appendTransaction(UserData transaction) {
        List<UserData> transactionHistory = customer.DeserializeTransactions();
        transactionHistory.add(transaction);  // Add the new transaction

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("TransactionHistory.ser"))) {
            for (UserData trans : transactionHistory) {
                oos.writeObject(trans);  // Write all transactions back to the file
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Update customer balance in the Customers.ser file
    private void updateCustomerBalanceInFile(double newBalance,UserData customer) {
        CustomerService customerService = new CustomerService();
        List<UserData> customers = customerService.DeserializeCustomers();
        for (UserData cust : customers) {
            if (cust.getEmail().equals(customer.getEmail())) {
                cust.setBalance(newBalance);
            }
        }
        customerService.saveCustomers(customers);  // Save updated customer list
    }

    // Get the current date and time
    private String getCurrentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(new Date());
    }

    // Method to print the bank statement
    public void printStatement() {
        List<UserData> history;

        // Check if there are new transactions or load from file if none exist
        if (this.transactions.isEmpty()) {
            history = customer.DeserializeTransactions();  // Load from file if no new transactions
            if (history.isEmpty()) {
                System.out.println("No history available.");
                return;
            }
        } else {
            // Save new transactions and then reload full history
            customer.saveTransactions(transactions);
            history = customer.DeserializeTransactions();
        }

        // Print bank statement header
        System.out.println("Bank Statement for Account: " + customer.getAccountNumber());
        System.out.printf("%-15s %-15s %-10s %-25s %-25s%n", "Account Number", "Type", "Amount", "Balance Before", "Balance After");
        System.out.println("---------------------------------------------------------------------------");

        // Iterate through transaction history and print aligned output
        for (UserData transaction : history) {
            if (transaction.getAccountNumber().equals(customer.getAccountNumber())) {
                System.out.printf("%-15s %-15s %-10s %-25s %-25s%n",
                        transaction.getAccountNumber(),
                        transaction.getTransactionType(),
                        String.format("R%.2f", transaction.getAmount()),  // Format amount to 2 decimal places
                        String.format("R%.2f", transaction.getBalanceBeforeTransaction()),  // Balance before
                        String.format("R%.2f", transaction.getBalanceAfterTransaction()));  // Balance after
            }
        }

        // Print the available balance at the end
        System.out.println("---------------------------------------------------------------------------");
        System.out.printf("%-45s R%.2f%n", "Available balance", customer.getBalance());  // Format available balance
    }


    // Method to save the bank statement to a file
    public void saveBankStatement() {
        String filePath = "BST_" + customer.getAccountNumber() + ".txt";
        List<UserData> history = customer.DeserializeTransactions();

        if (history.isEmpty()) {
            System.out.println("No transactions available to export.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write the header for the bank statement
            writer.write("Bank Statement for Account: " + customer.getAccountNumber() + "\n\n");

            // Write the column headers
            writer.write(String.format("%-15s %-15s %-10s %-25s %-25s%n",
                    "Account Number", "Type", "Amount", "Balance Before", "Balance After"));
            writer.write("---------------------------------------------------------------------------\n");

            // Write the transaction history
            for (UserData transaction : history) {
                if (transaction.getAccountNumber().equals(customer.getAccountNumber())) {
                    writer.write(String.format("%-15s %-15s %-10s %-25s %-25s%n",
                            transaction.getAccountNumber(),
                            transaction.getTransactionType(),
                            String.format("R%.2f", transaction.getAmount()),   // Format amount to 2 decimal places
                            String.format("R%.2f", transaction.getBalanceBeforeTransaction()),  // Balance before
                            String.format("R%.2f", transaction.getBalanceAfterTransaction())));  // Balance after
                }
            }

            // Write the available balance
            writer.write("---------------------------------------------------------------------------\n");
            writer.write(String.format("%-45s R%.2f%n", "Available balance", customer.getBalance()));  // Available balance

            System.out.println("Bank statement exported to " + filePath);
        } catch (IOException e) {
            System.out.println("Error exporting bank statement: " + e.getMessage());
        }
    }

    // Check balance
    public double checkBalance() {
        return customer.getBalance();
    }

}
