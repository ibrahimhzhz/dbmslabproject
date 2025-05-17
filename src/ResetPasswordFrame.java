import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class ResetPasswordFrame extends JFrame {
    private JTextField identifierField; // Email or CNIC
    private JPasswordField oldPasswordField, newPasswordField, confirmNewPasswordField;
    private JButton resetButton, backButton;

    public ResetPasswordFrame() {
        setTitle("Reset Password");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel identifierLabel = new JLabel("Email or CNIC:");
        identifierLabel.setBounds(30, 30, 120, 25);
        add(identifierLabel);

        identifierField = new JTextField();
        identifierField.setBounds(160, 30, 200, 25);
        add(identifierField);

        JLabel oldPasswordLabel = new JLabel("Old Password:");
        oldPasswordLabel.setBounds(30, 70, 120, 25);
        add(oldPasswordLabel);

        oldPasswordField = new JPasswordField();
        oldPasswordField.setBounds(160, 70, 200, 25);
        add(oldPasswordField);

        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordLabel.setBounds(30, 110, 120, 25);
        add(newPasswordLabel);

        newPasswordField = new JPasswordField();
        newPasswordField.setBounds(160, 110, 200, 25);
        add(newPasswordField);

        JLabel confirmNewPasswordLabel = new JLabel("Confirm New Password:");
        confirmNewPasswordLabel.setBounds(30, 150, 140, 25);
        add(confirmNewPasswordLabel);

        confirmNewPasswordField = new JPasswordField();
        confirmNewPasswordField.setBounds(160, 150, 200, 25);
        add(confirmNewPasswordField);

        resetButton = new JButton("Reset Password");
        resetButton.setBounds(80, 200, 130, 30);
        add(resetButton);

        backButton = new JButton("Back to Login");
        backButton.setBounds(230, 200, 130, 30);
        add(backButton);

        resetButton.addActionListener(e -> resetPassword());
        backButton.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        setVisible(true);
    }

    private void resetPassword() {
        String identifier = identifierField.getText().trim();
        String oldPass = new String(oldPasswordField.getPassword());
        String newPass = new String(newPasswordField.getPassword());
        String confirmNewPass = new String(confirmNewPasswordField.getPassword());

        if (identifier.isEmpty() || oldPass.isEmpty() || newPass.isEmpty() || confirmNewPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        if (!newPass.equals(confirmNewPass)) {
            JOptionPane.showMessageDialog(this, "New password and confirm password do not match.");
            return;
        }

        // Call stored procedure ResetUserPassword
        try (Connection conn = DBConnection.getConnection()) {  // Your DB connection util
            CallableStatement stmt = conn.prepareCall("{call ResetUserPassword(?, ?, ?, ?, ?)}");
            stmt.setString(1, identifier);
            stmt.setString(2, oldPass);
            stmt.setString(3, newPass);
            stmt.registerOutParameter(4, Types.INTEGER);
            stmt.registerOutParameter(5, Types.VARCHAR);

            stmt.execute();

            int statusCode = stmt.getInt(4);
            String message = stmt.getString(5);

            JOptionPane.showMessageDialog(this, message);

            if (statusCode == 1) {
                // Password changed successfully, go back to login
                new LoginFrame();
                dispose();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }
}
