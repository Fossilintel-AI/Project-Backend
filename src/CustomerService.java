import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Scanner;


public class CustomerService implements Serializable {
    private static final long serialVersionUID = 1L;


    // Login method to validate customer credentials
    public UserData login(String email, String password) {
        UserData userCheck = new UserData();
        UserData customer =  userCheck.isUserRegistered(email);
        if (customer != null && customer.getPassword().equals(password)) {
            System.out.println("Login successful. Welcome, " + customer.getName() + "!");
            return customer;
        } else {
            System.out.println("Invalid credentials.");
            return null;
        }
    }

    // Sign-up method to create a new customer
    public UserData signUp(String name, String email, String password) {


        UserData userCheck = new UserData();
        UserData userExist =  userCheck.isUserRegistered(email);
        if (userExist != null)
        {
            System.out.println("User Already exist");
            return null;
        }
        String accountNumber = generateAccountNumber();

        UserData customer = new UserData(name, email, password, accountNumber, 0.0);  // Start with 0 balance


        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("objects.ser"))) {

            oos.writeObject(customer);  // Writing first object
            System.out.println("Sign-up successful. Your account number is " + accountNumber);

           // Writing second object
        } catch (IOException e) {

            System.out.println("Error during sign-up: " + e.getMessage());
        }
        return customer;
    }


    // Helper method to generate a random account number
    private String generateAccountNumber() {
        return "ACCT" + (int) (Math.random() * 1000000);
    }
}
