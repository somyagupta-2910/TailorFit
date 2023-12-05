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

// Now you have the User object on the server side
			// Create data input and output streams
            // Scanner in = new Scanner(clientSocket.getInputStream());
            // PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			
            // Add the client's PrintWriter to the list of clients
            // clients.add(out);

            /* 
            while (true) {
                
                try {
                    // Read messages from the client
                    // String encryptedMessage = in.nextLine();
                    // String decryptedMessage = Encryption.decrypt(clientAesKey, encryptedMessage);
                    String message = in.nextLine();
                    // Broadcast the message to all clients with the thread ID
                    // broadcast(threadId + ": " + decryptedMessage);
                    // broadcast(threadId + ": " + message);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            */
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

        /*
        // Create multiple tasks for submitting forms
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                // Simulate form submission
                User user = getUserFromForm(); // Get user data from your GUI form

                // Process the form data
                processFormData(user);
            });
        }

        // Shutdown the executorService when the application is done
        executorService.shutdown();
         */
    }

    /* 
    private static void processFormData(User user) {
        // Use a connection pool to get a connection
        try (Connection connection = getConnectionFromPool()) {
            // Perform database operations with the user data
            saveUserToDatabase(connection, user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Simulate getting user data from a GUI form
    private static User getUserFromForm() {
        // Replace this with your actual logic to get user data from the GUI form
        return new User(User data);
    }

    */

    // Simulate saving user data to the database
    private static void saveUserToDatabase(Connection connection, User user) throws SQLException {
        
            String sql = "INSERT INTO Users (FirstName, Email) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Set parameters and execute the statement
                preparedStatement.setString(1, user.getFirstName());
                //preparedStatement.setString(2, user.getLastName());
                preparedStatement.setString(2, user.getEmail());
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
