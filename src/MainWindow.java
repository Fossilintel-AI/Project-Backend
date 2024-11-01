import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainWindow {

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

        //JFrame setup
        frame = new JFrame("Fossilintel Banking System");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        //Image setup
        ImageIcon logoImage = new ImageIcon("wallpaper.png");
        imageLabel = new JLabel(logoImage);



        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();
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

        //Homepage action listener
        HomePage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchPanels("Homepage");
            }
        });

        //signIn action listner
        signIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchPanels("Login");
            }
        });


        //aboutItem Action listener
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readFiles reader = new readFiles();
                String data = reader.getData("src/AboutUs.txt");
                JOptionPane.showMessageDialog(frame, data, "About Us", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        //contact action listener
        contactItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readFiles reader = new readFiles();
                String data = reader.getData("src/contactUs.txt");
                JOptionPane.showMessageDialog(frame, data, "Contacts", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        //Exit action listener
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

        //adding ComboBox in Frame
        mainPanel.add(comboBox, BorderLayout.NORTH);

        // Create the login and registration panels
        loginPanel = createLoginPanel();
        registerPanel = createRegisterPanel();
        transactionPanel = createTransactionPanel();

        // Initially display the login panel
        mainPanel.add(loginPanel, BorderLayout.CENTER);

        // Add the image label
        frame.add(imageLabel);
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
            //Image is put if Non is selected
            frame.remove(mainPanel);
            frame.setVisible(false);
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            imageLabel.setVerticalAlignment(JLabel.CENTER);
            frame.add(imageLabel);
            frame.setVisible(true);
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Method to create the login panel
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));


        JTextField emailField = new JTextField();
        JTextField passwordField = new JPasswordField();

        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        JButton loginButton = new JButton("Login");
        panel.add(loginButton);
        CustomerService customerService = new CustomerService();

        //Event Listener for the Login button
        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(((JPasswordField) passwordField).getPassword());


            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!email.contains("@")) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid email address!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                customer = customerService.login(email, password);

                if (customer == null) {
                    JOptionPane.showMessageDialog(frame, "Customer not found or invalid credentials. Please try again", "Failed Login", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    bankAccount = new BankAccount(customer);
                    JOptionPane.showMessageDialog(frame, "Login successful. Welcome, " + customer.getName() + "!", "Success", JOptionPane.INFORMATION_MESSAGE);

                    // Clear fields after login
                    nameField.setText("");
                    emailField.setText("");
                    passwordField.setText("");

                    // Remove combo box and image, switch to transaction panel
                    mainPanel.remove(comboBox);
                    mainPanel.remove(imageLabel);
                    switchToTransactionPanel();
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
        CustomerService customerService = new CustomerService();

        // Event listener for the registration button
        registerButton.addActionListener(e -> {

            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            // Simple validation
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!email.contains("@")) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid email address!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                customer = customerService.signUp(name, email, password);

                if (customer == null) {
                    JOptionPane.showMessageDialog(frame, "User already exists. Please log in.", "User Exists", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    bankAccount = new BankAccount(customer);
                    JOptionPane.showMessageDialog(frame, "Registration successful for " + name, "Success", JOptionPane.INFORMATION_MESSAGE);

                    // Clear fields after registration
                    nameField.setText("");
                    emailField.setText("");
                    passwordField.setText("");

                    // Remove JComboBox and switch to transaction panel
                    mainPanel.remove(comboBox);
                    mainPanel.remove(imageLabel);
                    switchToTransactionPanel();
                }
            }
        });
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

        // Event listener for all the functionalities button
        this.DepositEvent(depositButton);
        this.WithdrawalEvent(withdrawButton);
        this.TransferEvent(transferButton);
        this.ShowBalance(showBalanceButton);
        this.PrintStatement(printBalanceButton);
        this.SaveStatement(saveBalanceButton);
        return panel;
    }

    private void DepositEvent(JButton depositButton) {
        depositButton.addActionListener(e -> {
            // Open a dialog to enter the deposit amount
            JTextField amountField = new JTextField();
            Object[] message = { "Enter amount to deposit:", amountField };
            int option = JOptionPane.showConfirmDialog(frame, message, "Deposit Amount", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                try {
                    double amount = Double.parseDouble(amountField.getText());
                    bankAccount.deposit(amount);  // Perform deposit
                    JOptionPane.showMessageDialog(frame, "Successfully deposited: R" + amount, "Deposit", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid amount. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void WithdrawalEvent(JButton withdrawalButton) {
        withdrawalButton.addActionListener(e -> {
            // Open a dialog to enter the withdrawal amount
            JTextField amountField = new JTextField();
            Object[] message = { "Enter amount to withdraw:", amountField };
            int option = JOptionPane.showConfirmDialog(frame, message, "Withdraw Amount", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                try {
                    double amount = Double.parseDouble(amountField.getText());

                    // Check for sufficient balance before withdrawing
                    if (bankAccount.checkBalance() >= amount) {
                        bankAccount.withdraw(amount);
                        JOptionPane.showMessageDialog(frame, "Successfully withdrawn: R" + amount, "Withdrawal", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Insufficient funds.", "Withdrawal Failed", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid amount. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void TransferEvent(JButton TransferButton) {
        TransferButton.addActionListener(e -> {
            // Create input fields for the transfer amount and recipient's email
            JTextField amountField = new JTextField();
            JTextField emailField = new JTextField();

            // First dialog for the amount to transfer
            Object[] amountMessage = { "Enter amount to transfer:", amountField };
            int option = JOptionPane.showConfirmDialog(frame, amountMessage, "Credit Transfer", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                // Second dialog for the recipient's email
                Object[] emailMessage = { "Enter recipient's email address:", emailField };
                int option2 = JOptionPane.showConfirmDialog(frame, emailMessage, "Credit Transfer", JOptionPane.OK_CANCEL_OPTION);

                if (option2 == JOptionPane.OK_OPTION) {
                    try {
                        String email = emailField.getText();
                        String amountText = amountField.getText();

                        // Validate fields
                        if (email.isEmpty() || amountText.isEmpty()) {
                            JOptionPane.showMessageDialog(frame, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (!email.contains("@")) {
                            JOptionPane.showMessageDialog(frame, "Invalid email address.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Validate and parse amount
                        double amount = Double.parseDouble(amountText);
                        if (amount <= 0) {
                            JOptionPane.showMessageDialog(frame, "Invalid transfer amount.", "Error", JOptionPane.ERROR_MESSAGE);
                        } else if (bankAccount.checkBalance() < amount) {
                            JOptionPane.showMessageDialog(frame, "Insufficient balance for the transfer.", "Transfer Error", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            bankAccount.Transfer(email, amount); // Call the transfer method
                            JOptionPane.showMessageDialog(frame, "Successfully transferred: R" + amount, "Transfer", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid amount. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

    }

    private void ShowBalance(JButton showBalanceButton) {
        showBalanceButton.addActionListener(e -> {
            double balance = bankAccount.checkBalance();
            JOptionPane.showMessageDialog(frame, "Available balance: R" + balance, "Balance", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void PrintStatement(JButton printStatementButton) {
        printStatementButton.addActionListener(e -> {
            String data = bankAccount.printStatement();
            JOptionPane.showMessageDialog(frame, data, "Bank Statement", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void SaveStatement(JButton saveStatementButton) {
        saveStatementButton.addActionListener(e -> {
            bankAccount.saveBankStatement();
            JOptionPane.showMessageDialog(frame, "Bank statement saved", "Bank Statement", JOptionPane.INFORMATION_MESSAGE);
        });
    }
}