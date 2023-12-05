import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
	// List of clients
	// private static List<PrintWriter> clients = new ArrayList<>();
    // private static final ExecutorService executorService = Executors.newFixedThreadPool(10);
    
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
				Thread clientThread = new Thread(() -> handleClient(clientSocket));
				
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
	private static void handleClient(Socket clientSocket) {
		try {
			// long threadId = clientThreadId.get();

            // Read the User object from the client
            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            User receivedUser = (User) objectInputStream.readObject();

            System.out.println("Received user: " + receivedUser.toString());
            try (Connection connection = DriverManager.getConnection(JDBC_URL + DATABASE_NAME, USERNAME, PASSWORD)) {
                System.out.println("Connected to the database!");
                saveUserToDatabase(connection, receivedUser);
            } catch (SQLException e) {
                e.printStackTrace();
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

    // Simulate saving user data to the database
    private static void saveUserToDatabase(Connection connection, User user) throws SQLException {
        
            String sql = "INSERT INTO Users (FirstName, LastName, Email, City, Gender, Ethnicity, Age, Height, CurrentWeight, MedicalConditions, FoodOptions, DietType, FoodAllergies, Goal, TargetWeight) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                
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

                // ... Set other parameters

                preparedStatement.executeUpdate();
            }catch(Exception e){
                e.printStackTrace();
            }
    }

    /* 
    // Simulate getting a connection from a connection pool
    private static Connection getConnectionFromPool() throws SQLException {
        // Replace this with your actual logic to get a connection from a connection pool
        return YourConnectionPool.getConnection();
    }
    */
}
