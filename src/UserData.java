import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserData implements Serializable {
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

    public UserData() {}

    public UserData(String name, String email, String password, String accountNumber, double balance) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public UserData(String accountNumber, String transactionType, double amount, double balanceBeforeTransaction, double balanceAfterTransaction, String dateTime) {
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
        if (!file.exists() || file.length() == 0) {
            System.out.println("Customers.ser file does not exist or is empty. Returning an empty list.");
            return deserializedObjects;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            while (true) {
                try {
                    UserData obj = (UserData) ois.readObject();
                    deserializedObjects.add(obj);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return deserializedObjects;
    }

    public List<UserData> DeserializeTransactions() {
        deserializedObjects = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("TransactionHistory.ser"))) {
            while (true) {
                try {
                    UserData obj = (UserData) ois.readObject();
                    deserializedObjects.add(obj);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return deserializedObjects;
    }

    public UserData isUserRegistered(String email) {
        if (this.DeserializeCustomers().size() == 0) {
            return null;
        }
        for (UserData obj : deserializedObjects) {
            if (obj.getEmail().equals(email)) {
                return obj;
            }
        }
        return null;
    }

    public boolean isAccountExisting(String accountNumber) {
        if (this.DeserializeCustomers().size() == 0) {
            return false;
        }
        for (UserData obj : deserializedObjects) {
            if (obj.getAccountNumber().equals(accountNumber)) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
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

    public String getTransactionType() {
        return transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfterTransaction() {
        return balanceAfterTransaction;
    }

    public double getBalanceBeforeTransaction() {
        return balanceBeforeTransaction;
    }
}
