import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class CustomerService implements Serializable {
    private static final long serialVersionUID = 1L;

    public CustomerService() {
        String fileName = "Customers.ser";  // Name of the .ser file

        // Check if the file exists, if not create it
        File file = new File(fileName);
        if (!file.exists()) {
            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                System.out.println(fileName + " has been created successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Login method to validate customer credentials
    public UserData login(String email, String password) {
        List<UserData> customers = DeserializeCustomers();
        for (UserData customer : customers) {
            if (customer.getEmail().equals(email) && customer.getPassword().equals(password)) {
                System.out.println("Login successful. Welcome, " + customer.getName() + "!");
                return customer;
            }
        }
        System.out.println("Invalid credentials.");
        return null;
    }

    // Sign-up method to create a new customer
    public UserData signUp(String name, String email, String password) {
        List<UserData> customers = DeserializeCustomers();

        for (UserData customer : customers) {
            if (customer.getEmail().equals(email)) {
                System.out.println("User already exists,");
                return null;
            }
        }

        String accountNumber = generateAccountNumber();
        UserData newCustomer = new UserData(name, email, password, accountNumber, 0.0);  // Start with 0 balance
        customers.add(newCustomer);  // Add the new customer to the list

        saveCustomers(customers);  // Save the updated list to the file
        System.out.println("Sign-up successful. Your account number is " + accountNumber);
        return newCustomer;
    }

    // Helper method to generate a random account number
    private String generateAccountNumber() {
        //Creating an instant of Userdata just to do a test
        UserData checker = new UserData();
        String accountNumber;
        do {
            accountNumber = "ACCT" + (int) (Math.random() * 1000000);
        } while (checker.isAccountExisting(accountNumber));

        return accountNumber;
    }

    // Deserialize customers from Customers.ser
    public List<UserData> DeserializeCustomers() {
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
            // Ignore error if the file is empty or doesn't exist yet
        }
        return customers;
    }

    // Save customers to Customers.ser
    public void saveCustomers(List<UserData> customers) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Customers.ser"))) {
            for (UserData customer : customers) {
                oos.writeObject(customer);  // Write each customer object
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
