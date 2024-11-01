import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class CustomerService implements Serializable {
    private static final long serialVersionUID = 1L;

    public CustomerService() {
        ensureFileExists("Customers.ser");
        ensureFileExists("TransactionHistory.ser");
    }

    private void ensureFileExists(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                System.out.println(fileName + " has been created successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File already exists.");
        }
    }

    public UserData login(String email, String password) {
        List<UserData> customers = deserializeCustomers();
        for (UserData customer : customers) {
            if (customer.getEmail().equals(email) && customer.getPassword().equals(password)) {
                System.out.println("Login successful. Welcome, " + customer.getName() + "!");
                return customer;
            }
        }
        System.out.println("Invalid credentials.");
        return null;
    }

    public UserData signUp(String name, String email, String password) {
        List<UserData> customers = deserializeCustomers();
        for (UserData customer : customers) {
            if (customer.getEmail().equals(email)) {
                System.out.println("User already exists,");
                return null;
            }
        }
        String accountNumber = generateAccountNumber();
        UserData newCustomer = new UserData(name, email, password, accountNumber, 0.0);
        customers.add(newCustomer);
        saveCustomers(customers);
        System.out.println("Sign-up successful. Your account number is " + accountNumber);
        return newCustomer;
    }

    private String generateAccountNumber() {
        UserData checker = new UserData();
        String accountNumber;
        do {
            accountNumber = "ACCT" + (int) (Math.random() * 1000000);
        } while (checker.isAccountExisting(accountNumber));
        return accountNumber;
    }

    public List<UserData> deserializeCustomers() {
        List<UserData> customers = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Customers.ser"))) {
            while (true) {
                try {
                    UserData customer = (UserData) ois.readObject();
                    customers.add(customer);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
        }
        return customers;
    }

    public void saveCustomers(List<UserData> customers) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Customers.ser"))) {
            for (UserData customer : customers) {
                oos.writeObject(customer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
