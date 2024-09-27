import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {


        Scanner scanner = new Scanner(System.in);
        CustomerService customerService = new CustomerService();
        UserData customer = null;

        System.out.println("Welcome to the Banking System!");

        // Login or sign-up flow
        while (customer == null) {
            System.out.println("Please choose an option: ");
            System.out.println("1. Login");
            System.out.println("2. Sign up");
            System.out.print("Your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Login flow
                    System.out.print("Enter your email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter your password: ");
                    String password = scanner.nextLine();
                    customer = customerService.login(email, password);
                    if (customer == null) {
                        System.out.println("Customer not found or invalid credentials. Please try again.");
                    }
                    break;
                case 2:
                    // Sign-up flow
                    System.out.print("Enter your name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter your email: ");
                    email = scanner.nextLine();
                    System.out.print("Enter a password: ");
                    password = scanner.nextLine();
                    customer = customerService.signUp(name, email, password);
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }

//        // At this point, the user is logged in or signed up
//        BankAccount bankAccount = new BankAccount(customer, db);
//        BankStatementManager statementManager = new BankStatementManager(bankAccount, db);
//
//        boolean running = true;
//        while (running) {
//            System.out.println("\nPlease choose a transaction: ");
//            System.out.println("1. Deposit");
//            System.out.println("2. Withdraw");
//            System.out.println("3. Check Balance");
//            System.out.println("4. Print Bank Statement");
//            System.out.println("5. Export Bank Statement to File");
//            System.out.println("6. Exit");
//            System.out.print("Your choice: ");
//            int choice = scanner.nextInt();
//
//            switch (choice) {
//                case 1:
//                    System.out.print("Enter deposit amount: ");
//                    double depositAmount = scanner.nextDouble();
//                    bankAccount.deposit(depositAmount);
//                    break;
//                case 2:
//                    System.out.print("Enter withdrawal amount: ");
//                    double withdrawAmount = scanner.nextDouble();
//                    if (bankAccount.checkBalance() >= withdrawAmount) {
//                        bankAccount.withdraw(withdrawAmount);
//                    } else {
//                        System.out.println("Insufficient funds.");
//                    }
//                    break;
//                case 3:
//                    System.out.println("Current Balance: " + bankAccount.checkBalance());
//                    break;
//                case 4:
//                    // Print bank statement to console
//                    bankAccount.printStatement();
//                    break;
//                case 5:
//                    // Generate and export bank statement to a file
//                    statementManager.generateStatement(); // Fetch transactions
//                    statementManager.exportStatementToFile(); // Save to file
//                    break;
//                case 6:
//                    running = false;
//                    System.out.println("Exiting... Thank you for using the banking system!");
//                    break;
//                default:
//                    System.out.println("Invalid choice, please try again.");
//            }
//        }
//
//        try {
//            db.close();  // Close the database connection
//        } catch (SQLException e) {
//            System.out.println("Error closing the database: " + e.getMessage());
//        }

        scanner.close();  // Close the scanner
    }
}
