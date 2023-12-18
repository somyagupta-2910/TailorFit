import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class TailorFitSQL_Lite {
    public static void main(String[] args) {
        // JDBC URL for SQLite
        String jdbcUrl = "jdbc:sqlite:database.db";

        try {
            Class.forName("org.sqlite.JDBC");

            // Create a connection to the database
            try (Connection connection = DriverManager.getConnection(jdbcUrl)) {
                System.out.println("Connected to the database.");
    
                // Sets up table schema
                String createTableSQL = "CREATE TABLE IF NOT EXISTS Users (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "Name TEXT," +
                        "Email TEXT UNIQUE," +
                        "City TEXT," +
                        "Gender TEXT," +
                        "Ethnicity TEXT," +
                        "Age INTEGER," +
                        "Height REAL," +
                        "CurrentWeight REAL," +
                        "MedicalConditions TEXT," +
                        "FoodOptions TEXT," +
                        "DietType TEXT," +
                        "FoodAllergies TEXT," +
                        "Goal TEXT," +
                        "TargetWeight REAL," +
                        "GPTResponse TEXT" +
                        ")";
    
                // Create a PreparedStatement object
                try (PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL)) {
                    // Execute the SQL statement to create the table if it doesn't exist
                    preparedStatement.executeUpdate();
                    System.out.println("Table created (if not exists).");
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
