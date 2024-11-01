import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserData  implements Serializable {
    // Attributes
    private String name;
    private String email;
    private String password;
    private double balance;
    private String accountNumber;
    private String transactionType;
    private double amount;
    private double balanceAfterTransaction;
    private String dateTime;
    private double balanceBeforeTransaction;

    public List<UserData> deserializedObjects;
    private static final long serialVersionUID = 1L;

    public UserData()
    {

    }

    // Constructor for User information
    public UserData(String name, String email, String password, String accountNumber, double balance) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    // Constructor for Transaction information
    public UserData(String accountNumber, String transactionType, double amount,double balanceBeforeTransaction, double balanceAfterTransaction, String dateTime) {
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceAfterTransaction = balanceAfterTransaction;
        this.dateTime = dateTime;
        this.balanceBeforeTransaction = balanceBeforeTransaction;
    }

    public List<UserData> DeserializeCustomers() {
        deserializedObjects = new ArrayList<>();

        File file = new File("Customers.ser");

        // Check if file exists and is not empty
        if (!file.exists() || file.length() == 0) {
            System.out.println("Customers.ser file does not exist or is empty. Returning an empty list.");
            return deserializedObjects;  // Return an empty list if file is missing or empty
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            while (true) {
                try {
                    UserData obj = (UserData) ois.readObject();  // Read each object
                    deserializedObjects.add(obj);  // Add to the list
                } catch (EOFException e) {
                    break;  // End of file reached, stop reading
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();  // Log the exception
        }

        return deserializedObjects;  // Return the deserialized list
    }


    public List<UserData> DeserializeTransactions()
    {
        deserializedObjects = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("TransactionHistory.ser"))) {
            while (true) {
                try {
                    UserData obj = (UserData) ois.readObject();  // Read each object
                    deserializedObjects.add(obj);  // Add to the list
                } catch (EOFException e) {
                    break;  // End of file reached
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return deserializedObjects;
    }

    public UserData isUserRegistered(String email)
    {
        if (this.DeserializeCustomers().size() == 0)
        {
            return null;
        }
        for (UserData obj : deserializedObjects) {
            if (obj.getEmail().equals(email)) {  // Modify the object with id 2
               return obj;
            }
        }
        return null;
    }

    public boolean isAccountExisting(String accountNumber)
    {
        if (this.DeserializeCustomers().size() == 0)
        {
            return false;
        }
        for (UserData obj : deserializedObjects) {
            if (obj.getAccountNumber().equals(accountNumber)) {  // Modify the object with id 2
                return true;
            }
        }
        return false;
    }

    public void saveTransactions(List<UserData> data)
    {
        List<UserData> temp = this.DeserializeTransactions();
        data.addAll(temp);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("TransactionHistory.ser"))) {
            for (UserData obj : data) {
                oos.writeObject(obj);  // Write each object back to the file
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }










    // Getters and Setters (if needed)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBalanceAfterTransaction() {
        return balanceAfterTransaction;
    }

    public void setBalanceAfterTransaction(double balanceAfterTransaction) {
        this.balanceAfterTransaction = balanceAfterTransaction;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getBalanceBeforeTransaction() {
        return balanceBeforeTransaction;
    }

    public void setBalanceBeforeTransaction(double balanceBeforeTransaction) {
        this.balanceBeforeTransaction = balanceBeforeTransaction;
    }
}
