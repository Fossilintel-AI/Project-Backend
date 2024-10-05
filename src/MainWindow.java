import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.*;


public class MainWindow {
    CustomerService customerService = new CustomerService();
    UserData customer = null;
    BankAccount bankAccount = new BankAccount(customer);


    private final JFrame frame;
    private final JPanel mainPanel;
    private final JPanel loginPanel;
    private final JPanel registerPanel;
    private final JPanel transactionPanel; // New transaction panel
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private final JComboBox<String> comboBox;
    JLabel imageLabel;

    // Constructor to create the JFrame
    public MainWindow() {
        // Create a new JFrame with a title
        frame = new JFrame("Fossilintel Banking System");
        // Set the size of the window (width x height)
        frame.setSize(600, 400);
        // Specify what happens when the close button is clicked
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Center the window on the screen
        frame.setLocationRelativeTo(null);

        // Load the image
        ImageIcon logoImage = new ImageIcon("wallpaper.png");
        // Create a JLabel to hold the image
        imageLabel = new JLabel(logoImage);



        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();
        // Create the "Account" menu
        JMenu UserMenu = new JMenu("Home");
        JMenuItem HomePage = new JMenuItem("Homepage");
        JMenuItem signIn = new JMenuItem("Sign-in");
        UserMenu.add(HomePage);
        UserMenu.add(signIn);

        // Create the "Bank Statement" menu
        JMenu bankInfoMenu = new JMenu("Bank");
        JMenuItem aboutItem = new JMenuItem("About");
        JMenuItem contactItem = new JMenuItem("Contacts");
        bankInfoMenu.add(aboutItem);
        bankInfoMenu.add(contactItem);
        // create the More menu
        JMenu moreMenu = new JMenu("More");
        JMenuItem exit = new JMenuItem("Exit");
        moreMenu.add(exit);
        // Add menus to the menu bar
        menuBar.add(UserMenu);
        menuBar.add(bankInfoMenu);
        menuBar.add(moreMenu);
        // Add the menu bar to the frame
        frame.setJMenuBar(menuBar);

        HomePage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchPanels("Homepage");
            }
        });

        signIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchPanels("Login");
            }
        });





        // Action listener for "Exit" menu item
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the program
                readFiles reader = new readFiles();
                String data = reader.getData("src/AboutUs.txt");

                JOptionPane.showMessageDialog(frame, data, "About Us", JOptionPane.INFORMATION_MESSAGE);

            }
        });

        contactItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the program
                readFiles reader = new readFiles();
                String data = reader.getData("src/contactUs.txt");

                JOptionPane.showMessageDialog(frame, data, "Contacts", JOptionPane.INFORMATION_MESSAGE);

            }
        });



        // Action listener for "About" menu item
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the program
                System.exit(0);
            }
        });



        // Create the main input panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());

        // Create the JComboBox for switching between login and registration
        comboBox = new JComboBox<>(new String[]{"Login", "Register"});
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchPanels(comboBox.getSelectedItem().toString());
            }
        });
        mainPanel.add(comboBox, BorderLayout.NORTH);

        // Create the login and registration panels
        loginPanel = createLoginPanel();
        registerPanel = createRegisterPanel();
        transactionPanel = createTransactionPanel(); // Initialize transaction panel

        // Initially display the login panel
        mainPanel.add(loginPanel, BorderLayout.CENTER);

        // Add the image label as the main Jlabel/panel at first
        frame.add(imageLabel);

        // Make the window visible
        frame.setVisible(true);
    }

    // Method to switch between panels
    private void switchPanels(String selected) {
        mainPanel.remove(loginPanel);
        mainPanel.remove(registerPanel);
        mainPanel.remove(transactionPanel);
        if ("Login".equals(selected)) {
            mainPanel.add(comboBox, BorderLayout.NORTH);
            frame.remove(imageLabel);
            frame.setVisible(false);

            mainPanel.add(loginPanel, BorderLayout.CENTER);
            frame.add(mainPanel);

            // Make the window visible
            frame.setVisible(true);
        } else if("Register".equals(selected)) {
            mainPanel.add(comboBox, BorderLayout.NORTH);
            frame.remove(imageLabel);
            frame.setVisible(false);

            mainPanel.add(registerPanel, BorderLayout.CENTER);
            frame.add(mainPanel);
            frame.setVisible(true);
        }
        else {
            frame.remove(mainPanel);
            frame.setVisible(false);
            // Center the image
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            imageLabel.setVerticalAlignment(JLabel.CENTER);

            // Add the image to the frame
            frame.add(imageLabel);

            // Make the window visible
            frame.setVisible(true);
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Method to create the login panel
    private JPanel createLoginPanel() {


        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        // Create input fields for login
        JTextField emailField = new JTextField();
        JTextField passwordField = new JPasswordField();

        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        JButton loginButton = new JButton("Login");
        panel.add(loginButton);

        //Event Listener for the registration button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve user input

                String email = emailField.getText();
                String password = new String(((JPasswordField) passwordField).getPassword());
                System.out.println("we here");
                System.out.println(email+ " "+ password);

// Simple validation
                if ( email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!email.contains("@")) {  // Check if the email contains "@"
                    JOptionPane.showMessageDialog(frame, "Please enter a valid email address!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    customer = customerService.login(email, password);
                    if (customer == null) {
                        JOptionPane.showMessageDialog(frame,  "Customer not found or invalid credentials. Please try again", "Failed Login", JOptionPane.INFORMATION_MESSAGE);

                    }
                    else {
                        bankAccount= new BankAccount(null);
                        bankAccount = new BankAccount(customer);
                        // Display success message (replace this with your registration logic)
                        JOptionPane.showMessageDialog(frame, "Login successful. Welcome, " + customer.getName() + "!", "Success", JOptionPane.INFORMATION_MESSAGE);

                        // Clear fields after registration
                        nameField.setText("");
                        emailField.setText("");
                        passwordField.setText("");


                        // Remove the JComboBox and switch to the transaction panel
                        mainPanel.remove(comboBox);
                        mainPanel.remove(imageLabel);
                        switchToTransactionPanel();
                    }
                }
            }
        });
        return panel;
    }

    // Method to create the registration panel
    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        // Create input fields for registration
        nameField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        JButton registerButton = new JButton("Register");
        panel.add(registerButton);

        // Event listener for the registration button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve user input
                String name = nameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

// Simple validation
                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!email.contains("@")) {  // Check if the email contains "@"
                    JOptionPane.showMessageDialog(frame, "Please enter a valid email address!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    customer = null;
                    bankAccount= new BankAccount(null);
                    customer = customerService.signUp(name, email, password);
                    if (customer == null)
                    {
                        JOptionPane.showMessageDialog(frame, "User Already exist, Login " , "User Exist", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {

                        bankAccount = new BankAccount(customer);
                        // Display success message (replace this with your registration logic)
                        JOptionPane.showMessageDialog(frame, "Registration successful for " + name, "Success", JOptionPane.INFORMATION_MESSAGE);

                        // Clear fields after registration
                        nameField.setText("");
                        emailField.setText("");
                        passwordField.setText("");


                        // Remove the JComboBox and switch to the transaction panel
                        mainPanel.remove(comboBox);
                        mainPanel.remove(imageLabel);
                        switchToTransactionPanel();
                    }
                }
            }
        });

        //Event Listener for the registration button


        return panel;
    }

    // Method to switch to the transaction panel
    private void switchToTransactionPanel() {
        mainPanel.remove(loginPanel);
        mainPanel.remove(registerPanel);
        mainPanel.add(transactionPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Method to create the transaction panel
    private JPanel createTransactionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        //JLabel label = new JLabel("Transaction Options");
        //panel.add(label);

        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton transferButton = new JButton("Transfer");
        JButton showBalanceButton = new JButton("Show Balance");
        JButton printBalanceButton = new JButton("Print Bank statement");
        JButton saveBalanceButton = new JButton("Save Bank statement");

        panel.add(depositButton);
        panel.add(withdrawButton);
        panel.add(transferButton);
        panel.add(showBalanceButton);
        panel.add(printBalanceButton);
        panel.add(saveBalanceButton);

        // Event listener for the deposit button
        this.DepositEvent(depositButton);
        this.WithdrawalEvent(withdrawButton);
        this.TransferEvent(transferButton);
        this.ShowBalance(showBalanceButton);
        this.PrintStatement(printBalanceButton);
        this.SaveStatement(saveBalanceButton);


        return panel;
    }

    private void DepositEvent(JButton depositButton)
    {
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a dialog to enter the deposit amount
                JTextField amountField = new JTextField();
                Object[] message = {
                        "Enter amount to deposit:", amountField
                };

                int option = JOptionPane.showConfirmDialog(frame, message, "Deposit Amount", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    try {
                        double amount = Double.parseDouble(amountField.getText());
                        // Logic for deposit can be implemented here
                        bankAccount.deposit(amount);
                        JOptionPane.showMessageDialog(frame, "Successfully deposited: R" + amount, "Deposit", JOptionPane.INFORMATION_MESSAGE);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid amount. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    private void WithdrawalEvent(JButton withdrawalButton)
    {
        withdrawalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a dialog to enter the deposit amount
                JTextField amountField = new JTextField();
                Object[] message = {
                        "Enter amount to Withdraw:", amountField
                };

                int option = JOptionPane.showConfirmDialog(frame, message, "Withdraw Amount", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    try {
                        double amount = Double.parseDouble(amountField.getText());
                        // Logic for deposit can be implemented here
                        if (bankAccount.checkBalance() >= amount) {
                            bankAccount.withdraw(amount);
                            JOptionPane.showMessageDialog(frame, "Successfully Withdraw: R" + amount, "Withdraw", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Insufficient fund", "Withdrawal Failed", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid amount. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    private void TransferEvent(JButton TransferButton) {
        TransferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create input fields for the transfer amount and recipient's email
                JTextField amountField = new JTextField();
                JTextField emailField = new JTextField();

                // First dialog for the amount to transfer
                Object[] message = {
                        "Enter amount to transfer:", amountField
                };

                int option = JOptionPane.showConfirmDialog(frame, message, "Credit Transfer", JOptionPane.OK_CANCEL_OPTION);

                // Second dialog for the recipient's email
                if (option == JOptionPane.OK_OPTION) {
                    Object[] messages = {
                            "Enter recipient's email address:", emailField
                    };

                    int option2 = JOptionPane.showConfirmDialog(frame, messages, "Credit Transfer", JOptionPane.OK_CANCEL_OPTION);

                    if (option2 == JOptionPane.OK_OPTION) {
                        try {
                            String email = emailField.getText();
                            String amountText = amountField.getText();

                            // Validate if both fields are filled
                            if (email.isEmpty() || amountText.isEmpty()) {
                                JOptionPane.showMessageDialog(frame, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            // Validate email (must contain "@")
                            if (!email.contains("@")) {
                                JOptionPane.showMessageDialog(frame, "Invalid email address.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            // Validate amount
                            double amount = Double.parseDouble(amountText);
                            if (amount <= 0) {
                                JOptionPane.showMessageDialog(frame, "Invalid transfer amount.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            else if (bankAccount.checkBalance() < amount) {
                                JOptionPane.showMessageDialog(frame, "Insufficient balance for the transfer." , "Transfer error", JOptionPane.INFORMATION_MESSAGE);

                            } else {
                                // If all validations pass, process the transfer logic
                                JOptionPane.showMessageDialog(frame, "Successfully transferred: R" + amount, "Transfer", JOptionPane.INFORMATION_MESSAGE);

                                bankAccount.Transfer(email, amount); // Call the Transfer method
                            }



                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, "Invalid amount. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
    }


    private void ShowBalance(JButton showBalanceButton)
    {
        showBalanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a dialog to enter the deposit amount
                //JTextField amountField = new JTextField();
                double balance = bankAccount.checkBalance();

                JOptionPane.showMessageDialog(frame,  "Available balance: R"+ balance, "Balance", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void PrintStatement(JButton printStatementButton)
    {
        printStatementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String data = bankAccount.printStatement();
                // Open a dialog to enter the deposit amount
                JOptionPane.showMessageDialog(frame, data, "Bank Statement", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void SaveStatement(JButton saveStatementButton)
    {
        saveStatementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a dialog to enter the deposit amount
                bankAccount.saveBankStatement();
                JOptionPane.showMessageDialog(frame, "Bank statement saved", "Bank Statement", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }


}