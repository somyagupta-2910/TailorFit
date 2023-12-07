import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class Server extends JFrame implements Runnable{
    // Defining ChatServer class
	private static final AtomicLong clientThreadId = new AtomicLong(0);
	// Text area for displaying contents
	JTextArea ta;
	
    // Database credentials
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";
    private static final String DATABASE_NAME = "tailorfit"; // Change this to your desired database name


    public Server() {
		// Constructor
		super("Server");
		ta = new JTextArea();
		this.add(ta);

		// Set the size of the JFrame
		setSize(400, 200);
		
		// Start the server and set initialization text area
        ta.setText("Chat server started at " + new Date() + "\n");
		
		// create a new thread for the server and start it
		Thread t = new Thread(this);
		t.start();
		
	}

    public void run() {
		try {
			// Create a server socket
			ServerSocket serverSocket = new ServerSocket(9898);

			while (true) {
				
				// Listen for a connection request
				Socket clientSocket = serverSocket.accept();

				// Display client connection on server text area
				ta.append("Starting thread for client " + clientThreadId.incrementAndGet() + " on " + new Date() + "\n");
				// Create a new thread for each client
                
                Thread clientThread = new Thread(() -> {
                    try {
                        handleClient(clientSocket, new PrintWriter(clientSocket.getOutputStream(), true));
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                
                });    
				
				// start server thread to handle client
                clientThread.start();
                Thread.sleep(1);
				
			}
	  	}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

    // Method to handle client for each server thread
	private static void handleClient(Socket clientSocket, PrintWriter clientOut) {
		try {
            // Read the User object from the client
            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

            // Read the received object
            Object receivedObject = objectInputStream.readObject();

            if(receivedObject instanceof User){
                // Received object is a User object
                User receivedUser = (User) receivedObject;
                System.out.println("Received user: " + receivedUser.toString());
                try (Connection connection = DriverManager.getConnection(JDBC_URL + DATABASE_NAME, USERNAME, PASSWORD)) {
                    System.out.println("Connected to the database!");
                    saveUserToDatabase(connection, receivedUser);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else if(receivedObject instanceof String){
                // Received object is a String
                String receivedEmail = (String) receivedObject;
                System.out.println("Received string: " + receivedEmail);
                try (Connection connection = DriverManager.getConnection(JDBC_URL + DATABASE_NAME, USERNAME, PASSWORD)) {
                    System.out.println("Connected to the database!");
                    String gptResponse = getGPTResponseByEmail(connection, receivedEmail);

                    // Send the GPT response back to the client
                    clientOut.println(gptResponse);
                    // Close the client socket's input stream
                    clientSocket.shutdownInput();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("Unsupported object type received: " + receivedObject.getClass().getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {

        // Instantiate chat server
		Server appServer = new Server();
		// Set the close operation of the JFrame
		appServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Set the visibility of the JFrame
		appServer.setVisible(true);

    }

    // Save user data to the database
    private static void saveUserToDatabase(Connection connection, User user) throws SQLException {
            // Revise this to update if this user already exists
            String sql = "INSERT INTO Users (FirstName, LastName, Email, City, Gender, Ethnicity, Age, Height, CurrentWeight, MedicalConditions, FoodOptions, DietType, FoodAllergies, Goal, TargetWeight, GPTResponse) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Set parameters and execute the statement
                preparedStatement.setString(1, user.getFirstName());
                preparedStatement.setString(2, "Doe");
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setString(4, user.getCity());
                preparedStatement.setString(5, user.getGender());
                preparedStatement.setString(6, user.getEthnicity());
                preparedStatement.setInt(7, user.getAge());
                preparedStatement.setDouble(8, user.getHeight());
                preparedStatement.setDouble(9, user.getCurrentWeight());
                preparedStatement.setString(10, user.getMedicalConditions());
                preparedStatement.setString(11, Arrays.toString(user.getVegetables()));
                preparedStatement.setString(12, user.getDietType());
                preparedStatement.setString(13, user.getFoodAllergies());
                preparedStatement.setString(14, user.getGoal());
                preparedStatement.setDouble(15, user.getTargetWeight());
                preparedStatement.setString(16, user.getGPTResponse());

                preparedStatement.executeUpdate();
            }catch(Exception e){
                e.printStackTrace();
            }
    }

    // Method to fetch GPTResponse for a specific user based on email
    public static String getGPTResponseByEmail(Connection connection, String email) {
        String gptResponse = null;

        // SQL statement to select GPTResponse for a specific user
        String selectGPTResponseSQL = "SELECT GPTResponse FROM Users WHERE Email = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(selectGPTResponseSQL)){

            // Set the email parameter in the prepared statement
            preparedStatement.setString(1, email);

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Check if a result was found
                if (resultSet.next()) {
                    // Retrieve the GPTResponse from the result set
                    gptResponse = resultSet.getString("GPTResponse");
                    System.out.println("GPTResponse for user with email " + email + ": " + gptResponse);
                } else {
                    System.out.println("No user found with email: " + email);
                    gptResponse = "No user found with email: " + email;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 

        return gptResponse;
    }


    /* 
    // Simulate getting a connection from a connection pool
    private static Connection getConnectionFromPool() throws SQLException {
        // Replace this with your actual logic to get a connection from a connection pool
        return YourConnectionPool.getConnection();
    }
    */
}
