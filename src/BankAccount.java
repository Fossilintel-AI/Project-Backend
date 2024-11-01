import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BankAccount implements Serializable {
    private UserData customer;
    private List<UserData> transactions;
    private static final long serialVersionUID = 1L;

    public BankAccount(UserData customer) {
        this.customer = customer;
        this.transactions = new ArrayList<>();
    }

    public void deposit(double amount) {
        if (amount > 0) {
            double newBalance = customer.getBalance() + amount;
            customer.setBalance(newBalance);
            UserData transaction = new UserData(customer.getAccountNumber(), "Deposit", amount, newBalance - amount, newBalance, getCurrentDateTime());
            transactions.add(transaction);
            appendTransaction(transaction);
            updateCustomerBalanceInFile(newBalance, customer);
            System.out.println("Deposit successful. New balance: " + newBalance);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && customer.getBalance() >= amount) {
            double newBalance = customer.getBalance() - amount;
            customer.setBalance(newBalance);
            UserData transaction = new UserData(customer.getAccountNumber(), "Withdrawal", amount, newBalance + amount, newBalance, getCurrentDateTime());
            transactions.add(transaction);
            appendTransaction(transaction);
            updateCustomerBalanceInFile(newBalance, customer);
            System.out.println("Withdrawal successful. New balance: " + newBalance);
        } else {
            System.out.println("Insufficient funds or invalid amount.");
        }
    }

    public void Transfer(String email, double amount) {
        UserData traverser = new UserData();
        UserData recipient = traverser.isUserRegistered(email);
        double senderNewBalance = customer.getBalance() - amount;
        customer.setBalance(senderNewBalance);
        UserData senderTransaction = new UserData(customer.getAccountNumber(), "Transfer--", amount, senderNewBalance + amount, senderNewBalance, getCurrentDateTime());
        transactions.add(senderTransaction);
        appendTransaction(senderTransaction);
        updateCustomerBalanceInFile(senderNewBalance, customer);
        System.out.println("Transfer successful. New balance: R" + senderNewBalance);

        if (recipient != null) {
            double recipientNewBalance = recipient.getBalance() + amount;
            recipient.setBalance(recipientNewBalance);
            UserData recipientTransaction = new UserData(recipient.getAccountNumber(), "Transfer++", amount, recipientNewBalance - amount, recipientNewBalance, getCurrentDateTime());
            transactions.add(recipientTransaction);
            appendTransaction(recipientTransaction);
            updateCustomerBalanceInFile(senderNewBalance, recipient);
            System.out.println("Recipient's account credited with R" + amount + ". New balance: R" + recipientNewBalance);
        }
    }

    private void appendTransaction(UserData transaction) {
        List<UserData> transactionHistory = customer.DeserializeTransactions();
        transactionHistory.add(transaction);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("TransactionHistory.ser"))) {
            for (UserData trans : transactionHistory) {
                oos.writeObject(trans);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateCustomerBalanceInFile(double newBalance, UserData customer) {
        CustomerService customerService = new CustomerService();
        List<UserData> customers = customerService.deserializeCustomers();
        for (UserData cust : customers) {
            if (cust.getEmail().equals(customer.getEmail())) {
                cust.setBalance(newBalance);
            }
        }
        customerService.saveCustomers(customers);
    }

    private String getCurrentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(new Date());
    }

    public String printStatement() {
        List<UserData> history;
        if (this.transactions.isEmpty()) {
            history = customer.DeserializeTransactions();
            if (history.isEmpty()) {
                System.out.println("No history available.");
                return "No history available.";
            }
        } else {
            history = customer.DeserializeTransactions();
        }

        StringBuilder statement = new StringBuilder();
        statement.append("Bank Statement for Account: ").append(customer.getAccountNumber()).append("\n");
        statement.append(String.format("%-15s %-15s %-10s %-25s %-25s%n", "Account Number", "Type", "Amount", "Balance Before", "Balance After"));
        statement.append("---------------------------------------------------------------------------\n");
        for (UserData transaction : history) {
            if (transaction.getAccountNumber().equals(customer.getAccountNumber())) {
                statement.append(String.format("%-15s %-15s %-10s %-25s %-25s%n",
                        transaction.getAccountNumber(),
                        transaction.getTransactionType(),
                        String.format("R%.2f", transaction.getAmount()),
                        String.format("R%.2f", transaction.getBalanceBeforeTransaction()),
                        String.format("R%.2f", transaction.getBalanceAfterTransaction())));
            }
        }
        statement.append("---------------------------------------------------------------------------\n");
        statement.append(String.format("%-45s R%.2f%n", "Available balance", customer.getBalance()));
        return statement.toString();
    }

    public void saveBankStatement() {
        String filePath = "BST_" + customer.getAccountNumber() + ".txt";
        List<UserData> history = customer.DeserializeTransactions();
        if (history.isEmpty()) {
            System.out.println("No transactions available to export.");
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Bank Statement for Account: " + customer.getAccountNumber() + "\n\n");
            writer.write(String.format("%-15s %-15s %-10s %-25s %-25s%n",
                    "Account Number", "Type", "Amount", "Balance Before", "Balance After"));
            writer.write("---------------------------------------------------------------------------\n");
            for (UserData transaction : history) {
                if (transaction.getAccountNumber().equals(customer.getAccountNumber())) {
                    writer.write(String.format("%-15s %-15s %-10s %-25s %-25s%n",
                            transaction.getAccountNumber(),
                            transaction.getTransactionType(),
                            String.format("R%.2f", transaction.getAmount()),
                            String.format("R%.2f", transaction.getBalanceBeforeTransaction()),
                            String.format("R%.2f", transaction.getBalanceAfterTransaction())));
                }
            }
            writer.write("---------------------------------------------------------------------------\n");
            writer.write(String.format("%-45s R%.2f%n", "Available balance", customer.getBalance()));
            System.out.println("Bank statement exported to " + filePath);
        } catch (IOException e) {
            System.out.println("Error exporting bank statement: " + e.getMessage());
        }
    }

    public double checkBalance() {
        return customer.getBalance();
    }
}
