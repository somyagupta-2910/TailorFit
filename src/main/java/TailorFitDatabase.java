import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class TailorFitDatabase {
    
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";
    private static final String DATABASE_NAME = "tailorfit"; // Change this to your desired database name

    public static void main(String[] args) {
        // Step 1: Register JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        // Step 2: Create the database (if it doesn't exist)
        createDatabase();

        // Step 3: Establish connection to the created database
        try (Connection connection = DriverManager.getConnection(JDBC_URL + DATABASE_NAME, USERNAME, PASSWORD)) {
            System.out.println("Connected to the database!");

            // Step 4: Create tables
            createTables(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createDatabase() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            // SQL statement to create the database
            String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(createDatabaseSQL);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTables(Connection connection) throws SQLException {
        // SQL statement to create the Users table
        String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS Users ("
                + "UserID INT PRIMARY KEY AUTO_INCREMENT,"
                + "FirstName VARCHAR(50) NOT NULL,"
                + "LastName VARCHAR(50) NOT NULL,"
                + "Email VARCHAR(100) UNIQUE NOT NULL,"
                + "City VARCHAR(50),"
                + "Gender VARCHAR(10),"
                + "Ethnicity VARCHAR(50),"
                + "Age INT,"
                + "Height DOUBLE,"
                + "CurrentWeight DOUBLE,"
                + "MedicalConditions VARCHAR(255),"
                + "FoodOptions VARCHAR(255),"
                + "DietType VARCHAR(20),"
                + "FoodAllergies VARCHAR(255),"
                + "Goal VARCHAR(20),"
                + "TargetWeight DOUBLE"
                + ")";

                /*
                 * CREATE TABLE Users (
                    UserID INT PRIMARY KEY AUTO_INCREMENT,
                    FirstName VARCHAR(50) NOT NULL,
                    LastName VARCHAR(50) NOT NULL,
                    Email VARCHAR(100) UNIQUE NOT NULL,
                    City VARCHAR(50),
                    Gender VARCHAR(10),
                    Ethnicity VARCHAR(50),
                    Age INT,
                    Height DOUBLE,
                    CurrentWeight DOUBLE,
                    MedicalConditions VARCHAR(255),
                    FoodOptions VARCHAR(255),
                    DietType VARCHAR(20),
                    FoodAllergies VARCHAR(255),
                    Goal VARCHAR(20),
                    TargetWeight DOUBLE
                );

                -- Sample data for MedicalConditions
                CREATE TABLE MedicalConditions (
                    ConditionID INT PRIMARY KEY AUTO_INCREMENT,
                    ConditionName VARCHAR(50) UNIQUE NOT NULL
                );

                -- Sample data for FoodOptions
                CREATE TABLE FoodOptions (
                    FoodOptionID INT PRIMARY KEY AUTO_INCREMENT,
                    OptionName VARCHAR(50) UNIQUE NOT NULL
                );

                -- Sample data for DietTypes
                CREATE TABLE DietTypes (
                    DietTypeID INT PRIMARY KEY AUTO_INCREMENT,
                    DietTypeName VARCHAR(20) UNIQUE NOT NULL
                );

                -- Sample data for Goals
                CREATE TABLE Goals (
                    GoalID INT PRIMARY KEY AUTO_INCREMENT,
                    GoalName VARCHAR(20) UNIQUE NOT NULL
                );
                 */

        String createGoalsTableSQL = "CREATE TABLE IF NOT EXISTS Goals ("
                + "GoalID INT PRIMARY KEY AUTO_INCREMENT,"
                + "GoalName VARCHAR(20) UNIQUE NOT NULL"
                + ")";
        
        String createDietTableSQL = "CREATE TABLE IF NOT EXISTS DietTypes ("
                + "DietTypeID INT PRIMARY KEY AUTO_INCREMENT,"
                + "DietTypeName VARCHAR(20) UNIQUE NOT NULL"
                + ")"; 
        
        String createFoodOptionsTableSQL = "CREATE TABLE IF NOT EXISTS FoodOptions ("
                + "FoodOptionID INT PRIMARY KEY AUTO_INCREMENT,"
                + "OptionName VARCHAR(50) UNIQUE NOT NULL"
                + ")";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createUsersTableSQL);
            statement.executeUpdate(createGoalsTableSQL);
            statement.executeUpdate(createDietTableSQL);
            statement.executeUpdate(createFoodOptionsTableSQL);
        }
    }

    // Function to create a new user
    public static void createUser(String[] userData) {
        // Ensure the userData array has the required length
        if (userData.length != 15) { // Adjust the length based on the number of fields
            System.out.println("Invalid number of elements in the userData array.");
            return;
        }

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            // SQL statement to insert a new user into the Users table
            String insertUserSQL = "INSERT INTO Users (FirstName, LastName, Email, City, Gender, " +
                    "Ethnicity, Age, Height, CurrentWeight, MedicalConditions, FoodOptions, " +
                    "DietType, FoodAllergies, Goal, TargetWeight) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertUserSQL)) {
                // Set parameters from the array
                for (int i = 0; i < userData.length; i++) {
                    preparedStatement.setString(i + 1, userData[i]);
                }

                // Execute the update
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("User created successfully!");
                } else {
                    System.out.println("Failed to create user.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
