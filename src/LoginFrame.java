import javax.swing.*;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton, signupButton, resetPasswordButton;

    public LoginFrame() {
        setTitle("Airline Reservation - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 270);  // Increased height for new button
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(30, 30, 80, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(120, 30, 180, 25);
        add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 70, 80, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(120, 70, 180, 25);
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(30, 120, 90, 25);
        add(loginButton);

        signupButton = new JButton("Signup");
        signupButton.setBounds(130, 120, 90, 25);
        add(signupButton);

        resetPasswordButton = new JButton("Reset Password");
        resetPasswordButton.setBounds(80, 160, 150, 25);
        add(resetPasswordButton);

        loginButton.addActionListener(e -> handleLogin());
        signupButton.addActionListener(e -> {
            new SignupFrame(); // Open signup form
            dispose();
        });

        resetPasswordButton.addActionListener(e -> {
            new ResetPasswordFrame();  // Open Reset Password form
            dispose();
        });

        setVisible(true);
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        String role = UserDAO.validateLogin(email, password);
        if (role != null) {
            JOptionPane.showMessageDialog(this, "Login successful as " + role);
            if (role.equals("admin")) {
                new AdminDashboard();  // Open Admin Dashboard
            } else {
                new UserDashboard();   // Open User Dashboard
            }
            dispose();  // Close login form after successful login
        } else {
            JOptionPane.showMessageDialog(this, "Invalid email or password.");
        }
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
