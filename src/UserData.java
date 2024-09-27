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
    private LocalDateTime dateTime;

    public List<UserData> deserializedObjects;

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
    public UserData(String accountNumber, String transactionType, double amount, double balanceAfterTransaction, LocalDateTime dateTime) {
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceAfterTransaction = balanceAfterTransaction;
        this.dateTime = dateTime;
    }

    public List<UserData> Deserialize()
    {
        deserializedObjects = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("objects.ser"))) {
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
        if (this.Deserialize().size() == 0)
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
