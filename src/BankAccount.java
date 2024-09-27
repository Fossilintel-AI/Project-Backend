import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BankAccount {
    // Attributes
    private UserData customer;  // Associated customer
    private List<UserData> transactions;  // Transaction history
    private String dateTime;


    // Constructor
    public BankAccount(UserData customer) {
        this.customer = customer;
        this.transactions = new LinkedList<>();  // Initialize an empty transaction history
    }

    // Deposit method
    public void deposit(double amount) {
        if (amount > 0) {

            double newBalance = customer.getBalance() + amount;
            customer.setBalance(newBalance);
            UserData userTransaction = new UserData(customer.getAccountNumber(),"Deposit", amount,newBalance-amount, newBalance,getCurrentDateTime());
            transactions.add(userTransaction);
            this.updateCustomerBalanceInDatabase(newBalance);


//            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("TransactionHistory.ser"))) {
//
//                oos.writeObject(userTransaction);  // Writing first object
//
//
//                // Writing second object
//            } catch (IOException e) {
//
//                System.out.println("Error during writting to the file streem: " + e.getMessage());
//            }

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
            UserData userTransaction = new UserData(customer.getAccountNumber(),"Withdrawal", amount,newBalance+amount, newBalance,getCurrentDateTime());
            transactions.add(userTransaction);
            this.updateCustomerBalanceInDatabase(newBalance);
//            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("TransactionHistory.ser"))) {
//
//                oos.writeObject(userTransaction);  // Writing first object
//
//                // Writing second object
//            } catch (IOException e) {
//
//                System.out.println("Error during writing to the file streem: " + e.getMessage());
//            }

            System.out.println("Withdrawal successful. New balance: " + newBalance);
        } else {
            System.out.println("Insufficient balance or invalid amount.");
        }
    }


    // Method to update customer balance in the database
    private void updateCustomerBalanceInDatabase(Double newBalance) {
     List<UserData> history = customer.DeserializeCustomers();
        for(UserData client : history )
        {
            if(client.getEmail().equals(customer.getEmail()))
            {
                //System.out.println(client);
                client.setBalance(newBalance);
            }
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Customers.ser"))) {
            for (UserData obj : history) {
                oos.writeObject(obj);  // Write each object back to the file
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }


    // Check balance
    public double checkBalance() {
        return customer.getBalance();
    }

    // Get the current date and time in a readable format
    public String getCurrentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(new Date());
    }

    public void printStatement() {
        List<UserData> History = new ArrayList<>();
        if(this.transactions.isEmpty())
        {
            History = customer.DeserializeTransactions();
        }
        else {
            customer.saveTransactions(transactions);
            History = customer.DeserializeTransactions();
        }

        System.out.println("Bank Statement for Account: " + customer.getAccountNumber());
        for (UserData transaction : History) {
            System.out.println("[Account number: "+ transaction.getAccountNumber() + "| Type: "+ transaction.getTransactionType()+ "| Amount: R"+ transaction.getAmount() +"| BalanceBeforeTransaction: R"+transaction.getBalanceBeforeTransaction()+ "| BalanceAfterTransaction: R"+ transaction.getBalanceAfterTransaction() +"]");
           System.out.println();
        }
    }

    public void saveBankStatement(){
        {
            String filePath = "BST_" + customer.getAccountNumber() + ".txt";
            List<UserData> History = customer.DeserializeTransactions();
            if (History.isEmpty()) {
                System.out.println("No transactions available to export.");
                return;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write("Bank Statement for Account: " + customer.getAccountNumber() + "\n\n");
                for (UserData transaction : History) {
                    writer.write("[Account number: "+ transaction.getAccountNumber() + "| Type: "+ transaction.getTransactionType()+ "| Amount: R"+ transaction.getAmount() +"| BalanceBeforeTransaction: R"+transaction.getBalanceBeforeTransaction()+ "| BalanceAfterTransaction: R"+ transaction.getBalanceAfterTransaction() +"] \n");

                }
                System.out.println("Bank statement exported to " + filePath);
            } catch (IOException e) {
                System.out.println("Error exporting bank statement: " + e.getMessage());
            }
        }

    }



}
