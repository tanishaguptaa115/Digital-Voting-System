import java.awt.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;

public class v2 {

    private Connection connection;
    private JFrame frame;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new v2().createAndShowGUI());
    }

    private void createAndShowGUI() {
        connectToDatabase();
        frame = new JFrame("Voting System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 750);
        showMainMenu();
        frame.setVisible(true);
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/votingsystem";

            String user = "root";
            String password = "HomeW098()@234";
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database connection error: " + e.getMessage());
            System.exit(1);
        }
    }
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(Color.WHITE); // Changed to white
        button.setForeground(Color.BLACK); // Text color for contrast
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // Optional border
        return button;
    }
    

    private void showMainMenu() {
        frame.getContentPane().removeAll();

        JPanel panel = new JPanel() {
            Image background = new ImageIcon("C:/Users/gupta/OneDrive/Desktop/My Programs/voting_management_system/bg.jpg").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Dimension size = getSize(); // get the current size of the panel/frame
                g.drawImage(background, 0, 0, size.width, size.height, this); // scale image to fit
            }
        };
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 350, 30, 350));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.gridy = 0;

        JLabel title = new JLabel("Welcome User!");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(title, gbc);

        gbc.gridy = 1;
        JButton voteButton = createStyledButton("Vote");
        gbc.gridy++;
        panel.add(voteButton, gbc);

        gbc.gridy = 2;
        JButton adminButton = createStyledButton("Admin Login");
        gbc.gridy++;
        panel.add(adminButton, gbc);

        gbc.gridy = 3;
        JButton registerButton = createStyledButton("Register");
        gbc.gridy++;
        panel.add(registerButton, gbc);

        gbc.gridy = 4;
        JButton exitButton = createStyledButton("Exit");
        gbc.gridy++;
        panel.add(exitButton, gbc);

        voteButton.addActionListener(e -> showVoterLogin());
        adminButton.addActionListener(e -> showAdminLogin());
        registerButton.addActionListener(e -> showRegistrationPanel());
        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to exit?",
                    "Exit Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        frame.setContentPane(panel);
        frame.revalidate();
        frame.repaint();
    }

    private void showVoterLogin() {
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField idField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        ImageIcon originalIcon = new ImageIcon("C:/Users/gupta/OneDrive/Documents/voter.jpg");
        Image scaledImage = originalIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        ImageIcon customIcon = new ImageIcon(scaledImage); 

    
        Object[] inputs = {
            "Name:", nameField,
            "Age:", ageField,
            "Voter ID:", idField,
            "Password:", passwordField
        };
    
        int result = JOptionPane.showConfirmDialog(
                frame, inputs, "Voter Verification", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE, customIcon);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String voterID = idField.getText().trim().toUpperCase();
            String password = new String(passwordField.getPassword());
            int age;
    
            try {
                age = Integer.parseInt(ageField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid age entered.");
                return;
            }
    
            try {
                PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM voters WHERE id = ? AND name = ? AND password = ?");
                stmt.setString(1, voterID);
                stmt.setString(2, name);
                stmt.setString(3, password);
                ResultSet rs = stmt.executeQuery();
    
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(frame, "Voter not found or credentials incorrect.");
                    return;
                }
    
                if (age < 18) {
                    JOptionPane.showMessageDialog(frame, "You must be 18 or older to vote.");
                    return;
                }
    
                boolean hasVoted = rs.getBoolean("has_voted");
                if (hasVoted) {
                    JOptionPane.showMessageDialog(frame, "You have already voted.");
                    return;
                }
    
                showVotingScreen(voterID);
    
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "DB Error: " + e.getMessage());
            }
        }
    }
    

    private void showVotingScreen(String voterID) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM candidates");
    
            JPanel panel = new JPanel(new GridLayout(0, 1));
            ButtonGroup group = new ButtonGroup();
            Map<AbstractButton, String> voteMap = new HashMap<>();
    
            // Fetch candidates and populate radio buttons
            while (rs.next()) {
                String name = rs.getString("name");
                String symbol = rs.getString("symbol");
                JRadioButton button = new JRadioButton(symbol + " - " + name);
                voteMap.put(button, symbol); // Use symbol instead of id
                group.add(button);
                panel.add(button);
            }
    
            // Show confirmation dialog
            int result = JOptionPane.showConfirmDialog(frame, panel, "Cast Your Vote", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                for (AbstractButton button : voteMap.keySet()) {
                    if (button.isSelected()) {
                        String selectedSymbol = voteMap.get(button);
    
                        // Increment vote count for selected candidate
                        PreparedStatement voteStmt = connection.prepareStatement(
                                "UPDATE candidates SET vote_count = vote_count + 1 WHERE symbol = ?");
                        voteStmt.setString(1, selectedSymbol);
                        voteStmt.executeUpdate();
    
                        // Mark voter as having voted
                        PreparedStatement updateVoter = connection.prepareStatement(
                                "UPDATE voters SET has_voted = 1 WHERE id = ?");
                        updateVoter.setString(1, voterID);
                        updateVoter.executeUpdate();
    
                        JOptionPane.showMessageDialog(frame, "Thank you for voting!");
                        showMainMenu(); // Return to main menu
                        return;
                    }
                }
                JOptionPane.showMessageDialog(frame, "Please select a candidate.");
            }
    
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }
    
    private void showAdminLogin() {
        JTextField emailField = new JTextField();
        JPasswordField passField = new JPasswordField();
        ImageIcon originalIcon = new ImageIcon("admin.jpg");
        Image scaledImage = originalIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        ImageIcon adminIcon = new ImageIcon(scaledImage);

        Object[] inputs = {
            "Email:", emailField,
            "Password:", passField
        };
    
        int result = JOptionPane.showConfirmDialog(frame, inputs, "Admin Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,adminIcon);

        if (result == JOptionPane.OK_OPTION) {
            String email = emailField.getText().trim();
            String password = new String(passField.getPassword());
    
            try {
                PreparedStatement stmt = connection.prepareStatement(
                        "SELECT * FROM admins WHERE email = ? AND password = ?");
                stmt.setString(1, email);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();
    
                if (rs.next()) {
                    JOptionPane.showMessageDialog(frame, "Login successful!");
                    showAdminMenu();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid credentials.");
                }
    
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
            }
        }
    }
    

    private void showAdminMenu() {
        String[] options = {"View Voters", "View Results", "Reset Votes", "Back"};
        int choice = JOptionPane.showOptionDialog(frame, "Choose an admin option", "Admin Menu",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                

        switch (choice) {
            case 0 -> showVoterList();
            case 1 -> showResults();
            case 2 -> resetVotes();
            case 3 -> showMainMenu();
        }
    }

    private void showVoterList() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM voters");

            StringBuilder sb = new StringBuilder("Voter List:\n");
            while (rs.next()) {
                sb.append(rs.getString("name"))
                  .append(" (")
                  .append(rs.getString("id"))
                  .append(") - ")
                  .append(rs.getBoolean("has_voted") ? "Voted" : "Not Voted")
                  .append("\n");
            }

            JOptionPane.showMessageDialog(frame, sb.toString());

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }

    private void showResults() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM candidates");
    
            StringBuilder sb = new StringBuilder("Results:\n");
            while (rs.next()) {
                sb.append(rs.getString("name"))
                  .append(" (")
                  .append(rs.getString("symbol"))
                  .append("): ")
                  .append(rs.getInt("vote_count"))
                  .append(" votes\n");
            }
    
            JOptionPane.showMessageDialog(frame, sb.toString());
    
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }
    

    private void resetVotes() {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("UPDATE candidates SET vote_count = 0");
            stmt.executeUpdate("UPDATE voters SET has_voted = 0");
            JOptionPane.showMessageDialog(frame, "All votes have been reset successfully.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }
    
    private void showRegistrationPanel() {
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        ImageIcon originalIcon = new ImageIcon("reg.jpg");
        Image scaledImage = originalIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        ImageIcon regIcon = new ImageIcon(scaledImage);

        Object[] inputs = {
            "Name:", nameField,
            "Age:", ageField,
            "Set Password:", passwordField
        };
    
        int result = JOptionPane.showConfirmDialog(frame, inputs, "Register", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, regIcon);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String password = new String(passwordField.getPassword());
            int age;
    
            try {
                age = Integer.parseInt(ageField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid age entered.");
                return;
            }
    
            if (age < 18) {
                JOptionPane.showMessageDialog(frame, "You must be at least 18 years old to register.");
                return;
            }
    
            try {
                // Generate a unique voter ID
                String voterID = "VOT" + System.currentTimeMillis();
    
                PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO voters (id, name, age, password, has_voted) VALUES (?, ?, ?, ?, 0)");
                stmt.setString(1, voterID);
                stmt.setString(2, name);
                stmt.setInt(3, age);
                stmt.setString(4, password);
                stmt.executeUpdate();
    
                JOptionPane.showMessageDialog(frame, "Registration successful!\nYour Voter ID is: " + voterID);
    
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Database Error: " + e.getMessage());
            }
        }
    }
    
}
