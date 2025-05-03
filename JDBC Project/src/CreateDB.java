import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class CreateDB {
    public static void main(String[] args) throws Exception {
        try {
            String url = "jdbc:mysql://localhost:3306/";
            String databaseName = "My_DB";
            String userName = "root";
            String password = "HomeW098()@234";

            Connection connection = DriverManager.getConnection(url, userName, password);

            String sql = "CREATE DATABASE " + databaseName;

            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();

            JOptionPane.showMessageDialog(null, "Database created successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}
