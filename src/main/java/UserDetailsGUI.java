import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class UserDetailsGUI extends JFrame {
    private JTextField nameField, emailField, cityField, ageField, heightField, weightField, targetWeightField;
    private JComboBox<String> genderComboBox, ethnicityComboBox, medicalConditionsComboBox, dietTypeComboBox, allergiesComboBox, goalsComboBox;
    private JCheckBox[] vegetableCheckBoxes, fruitCheckBoxes, dairyCheckBoxes, meatAndEggsCheckBoxes, wholeGrainsCheckBoxes;
    private JPanel inputPanel;
    private JTextArea responseArea;
    private JScrollPane scrollPane;
    
    private JPanel initialPanel;
    private JButton fetchButton, generateButton;
    // private JMenu fileMenu;
    
 // Socket variables
 	Socket clientSocket;
 	PrintWriter out;

    public UserDetailsGUI() {
    	setTitle("TailorFit");
        createInitialScreen();
    }
    
    private void createInitialScreen() {
        getContentPane().removeAll();
        initialPanel = new JPanel(new GridLayout(0, 1));
        fetchButton = new JButton("Fetch Previous Recommendation");
        generateButton = new JButton("Generate New Recommendation");

        fetchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createFetchRecommendationScreen();
            }
        });

        generateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setupNewRecommendationUI();
            }
        });

        initialPanel.add(fetchButton);
        initialPanel.add(generateButton);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);


        // Add "Go back to initial screen" option to the file menu
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        // Create "Connect" menu item
        JMenuItem connectMenuItem = new JMenuItem("Connect");
        connectMenuItem.addActionListener(new ConnectActionListener());
        fileMenu.add(connectMenuItem);

        // Create "Close" menu item
        JMenuItem closeMenuItem = new JMenuItem("Close");
        closeMenuItem.addActionListener(new CloseActionListener());
        fileMenu.add(closeMenuItem);
        //

        // Create "Close" menu item
        JMenuItem goBackMenuItem = new JMenuItem("Go back to initial screen");
        goBackMenuItem.addActionListener(new GoBackActionListener());
        fileMenu.add(goBackMenuItem);

        add(initialPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        revalidate();
        repaint();
    }
    
    private void createFetchRecommendationScreen() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        JPanel fetchPanel = new JPanel(new GridLayout(0, 2));
        final JTextField emailField = new JTextField();
        JButton submitButton = new JButton("Submit");

        fetchPanel.add(new JLabel("Enter Email ID:"));
        fetchPanel.add(emailField);
        fetchPanel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                sendEmailToServer(email);
            }
        });

        add(fetchPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    private void setupNewRecommendationUI() {
        getContentPane().removeAll();
        setLayout(new GridLayout(0, 2));
        
        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Create "File" menu
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        // Create "Connect" menu item
        JMenuItem connectMenuItem = new JMenuItem("Connect");
        connectMenuItem.addActionListener(new ConnectActionListener());
        fileMenu.add(connectMenuItem);

        // Create "Close" menu item
        JMenuItem closeMenuItem = new JMenuItem("Close");
        closeMenuItem.addActionListener(new CloseActionListener());
        fileMenu.add(closeMenuItem);
        //

        // Create "Close" menu item
        JMenuItem goBackMenuItem = new JMenuItem("Go back to initial screen");
        goBackMenuItem.addActionListener(new GoBackActionListener());
        fileMenu.add(goBackMenuItem);

        inputPanel = new JPanel(new GridLayout(0, 2));

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Email ID:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("City:"));
        cityField = new JTextField();
        add(cityField);

        add(new JLabel("Gender:"));
        String[] genders = {"Male", "Female", "Transgender", "Non-Binary", "Prefer not to respond"};
        genderComboBox = new JComboBox<>(genders);
        add(genderComboBox);

        add(new JLabel("Ethnicity:"));
        String[] ethnicities = {"African American", "Asian", "Caucasian", "Hispanic", "Other"};
        ethnicityComboBox = new JComboBox<>(ethnicities);
        add(ethnicityComboBox);

        add(new JLabel("Age:"));
        ageField = new JTextField();
        add(ageField);

        add(new JLabel("Height (in cm):"));
        heightField = new JTextField();
        add(heightField);

        add(new JLabel("Current Weight (in lb):"));
        weightField = new JTextField();
        add(weightField);

        add(new JLabel("Any existing medical conditions or physical limitations:"));
        String[] medicalConditions = {"Cardiovascular Conditions", "Respiratory Conditions", "Joint or Muscle Issues", "None"};
        medicalConditionsComboBox = new JComboBox<>(medicalConditions);
        add(medicalConditionsComboBox);

        add(new JLabel("Select Diet type:"));
        String[] dietTypes = {"Vegan", "Omnivore", "Pescatarian", "Vegetarian"};
        dietTypeComboBox = new JComboBox<>(dietTypes);
        add(dietTypeComboBox);

        // Add here
        add(new JSeparator());
        add(new JSeparator());
        add(new JLabel("Select Vegetables:"));
        String[] vegetables = {"Carrot", "Cauliflower", "Beans", "Potato", "Cabbage", "Spinach", "Tomato", "Onion", "Eggplant", "Bell Pepper"};
        vegetableCheckBoxes = createCheckBoxes(vegetables, "None");
        addCheckBoxes(vegetableCheckBoxes);

        add(new JSeparator());
        add(new JSeparator());
        add(new JLabel("Select Fruits:"));
        String[] fruits = {"Orange", "Apple", "Banana", "Grape", "Mango", "Pineapple", "Papaya"};
        fruitCheckBoxes = createCheckBoxes(fruits, "None");
        addCheckBoxes(fruitCheckBoxes);

        add(new JSeparator());
        add(new JSeparator());
        add(new JLabel("Select Dairy:"));
        String[] dairy = {"Cheddar Cheese", "Cottage Cheese", "Almond Milk", "Milk", "Coconut Milk", "Yogurt"};
        dairyCheckBoxes = createCheckBoxes(dairy, "None");
        addCheckBoxes(dairyCheckBoxes);

        add(new JSeparator());
        add(new JSeparator());
        add(new JLabel("Select Meat & Eggs:"));
        String[] meatAndEggs = {"Chicken", "Mutton", "Seafood", "Turkey", "Beef", "Eggs"};
        meatAndEggsCheckBoxes = createCheckBoxes(meatAndEggs, "None");
        addCheckBoxes(meatAndEggsCheckBoxes);

        add(new JSeparator());
        add(new JSeparator());
        add(new JLabel("Select Whole Grains:"));
        String[] wholeGrains = {"Millet", "Oats", "Rice", "Quinoa"};
        wholeGrainsCheckBoxes = createCheckBoxes(wholeGrains, "None");
        addCheckBoxes(wholeGrainsCheckBoxes);

        add(new JSeparator());
        add(new JSeparator());
        add(new JLabel("Any Food Allergies:"));
        String[] allergies = {"Seafood", "Gluten", "Dairy", "Nuts", "None"};
        allergiesComboBox = new JComboBox<>(allergies);
        add(allergiesComboBox);
        
        add(new JSeparator());
        add(new JLabel("Enter Goal:"));
        String[] goals = {"Lose Weight", "Gain Muscle"};
        goalsComboBox = new JComboBox<>(goals);
        add(goalsComboBox);
        
        add(new JSeparator());
        add(new JLabel("Enter Target Weight (in lb):"));
        targetWeightField = new JTextField();
        add(targetWeightField);

        // Initialize the JTextArea and JScrollPane
        responseArea = new JTextArea();
        responseArea.setEditable(false); // Make it read-only
        scrollPane = new JScrollPane(responseArea);
        scrollPane.setPreferredSize(new Dimension(800, 600));

        add(new JSeparator());
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (!validateInputs()) {
                    JOptionPane.showMessageDialog(UserDetailsGUI.this, "Please fill in all required fields with valid information.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                getContentPane().removeAll();
                responseArea.setText("Generating Fitness Plan...");
                add(scrollPane, BorderLayout.CENTER);
                revalidate();
                repaint();

                // Perform the API call in a new Thread to avoid freezing the GUI
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        User user = createUser();
                        OpenAIAPIHandler apiHandler = new OpenAIAPIHandler("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");
                        String response = apiHandler.sendPromptToGPT(user);
                        // System.out.println(response);
                        responseArea.setText(response); // Update with the actual response
                        user.setGPTResponse(response);
                        sendUserToServer(user);
                    }
                }).start();

            }
        });
        inputPanel.add(submitButton);

        add(inputPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        revalidate();
        repaint();
    }
    
    private boolean validateCheckboxSelection(JCheckBox[] checkBoxes) {
        boolean noneSelected = checkBoxes[0].isSelected();
        boolean otherSelected = false;
        for (int i = 1; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isSelected()) {
                otherSelected = true;
                break;
            }
        }
        return noneSelected || otherSelected;
    }
    
    private boolean validateInputs() {
        // Validate the name field
        if (nameField.getText().isEmpty() || !nameField.getText().matches("[A-Za-z ]+")) {
            JOptionPane.showMessageDialog(UserDetailsGUI.this, "Please enter a valid name using only alphabets.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate the email field with basic regex pattern
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
        if (emailField.getText().isEmpty() || !emailPattern.matcher(emailField.getText()).matches()) {
            JOptionPane.showMessageDialog(UserDetailsGUI.this, "Please enter a valid email address.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate the city field
        if (cityField.getText().isEmpty() || !cityField.getText().matches("[A-Za-z ]+")) {
            JOptionPane.showMessageDialog(UserDetailsGUI.this, "Please enter a valid city using only alphabets.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate the age field for positive numbers
        try {
            int age = Integer.parseInt(ageField.getText());
            if (age <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(UserDetailsGUI.this, "Please enter a valid age as a positive number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate the weight field for positive numbers
        try {
            double weight = Double.parseDouble(weightField.getText());
            if (weight <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(UserDetailsGUI.this, "Please enter a valid weight as a positive number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate the target weight field for positive numbers
        try {
            double targetWeight = Double.parseDouble(targetWeightField.getText());
            if (targetWeight <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(UserDetailsGUI.this, "Please enter a valid target weight as a positive number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (!validateCheckboxSelection(vegetableCheckBoxes)
                || !validateCheckboxSelection(fruitCheckBoxes)
                || !validateCheckboxSelection(dairyCheckBoxes)
                || !validateCheckboxSelection(meatAndEggsCheckBoxes)
                || !validateCheckboxSelection(wholeGrainsCheckBoxes)) {
                JOptionPane.showMessageDialog(UserDetailsGUI.this, "Please select at least one option or 'None' for each category.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }


        return true;
    }

    private User createUser() {
        String[] selectedVegetables = getSelectedItems(vegetableCheckBoxes);
        String[] selectedFruits = getSelectedItems(fruitCheckBoxes);
        String[] selectedDairy = getSelectedItems(dairyCheckBoxes);
        String[] selectedMeatAndEggs = getSelectedItems(meatAndEggsCheckBoxes);
        String[] selectedWholeGrains = getSelectedItems(wholeGrainsCheckBoxes);

        return new User(
                nameField.getText(),
                emailField.getText(),
                cityField.getText(),
                (String) genderComboBox.getSelectedItem(),
                (String) ethnicityComboBox.getSelectedItem(),
                Integer.parseInt(ageField.getText()),
                Double.parseDouble(heightField.getText()),
                Double.parseDouble(weightField.getText()),
                (String) medicalConditionsComboBox.getSelectedItem(),
                (String) dietTypeComboBox.getSelectedItem(),
                (String) allergiesComboBox.getSelectedItem(),
                (String) goalsComboBox.getSelectedItem(),
                Double.parseDouble(targetWeightField.getText()),
                selectedVegetables,
                selectedFruits,
                selectedDairy,
                selectedMeatAndEggs,
                selectedWholeGrains,
                null
        );
    }

    private String[] getSelectedItems(JCheckBox[] checkBoxes) {
        ArrayList<String> selectedItems = new ArrayList<>();
        for (JCheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) {
                selectedItems.add(checkBox.getText());
            }
        }
        return selectedItems.toArray(new String[0]);
    }

    private JCheckBox[] createCheckBoxes(String[] options, String noneOption) {
        JCheckBox[] checkBoxes = new JCheckBox[options.length + 1];

        // Declare noneCheckBox as final
        final JCheckBox noneCheckBox = new JCheckBox(noneOption, true);
        checkBoxes[0] = noneCheckBox;

        final JCheckBox[] finalCheckBoxes = checkBoxes; // Declare final array

        for (int i = 0; i < options.length; i++) {
            final int index = i; // Create a final variable for the index
            final JCheckBox checkBox = new JCheckBox(options[i]);

            // Add an ActionListener to each checkBox
            checkBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (checkBox.isSelected()) {
                        noneCheckBox.setSelected(false);
                    }
                }
            });

            finalCheckBoxes[i + 1] = checkBox; // Use the final array

            checkBoxes = finalCheckBoxes; // Assign the final array back to checkBoxes
        }

        // Add an ActionListener to noneCheckBox
        noneCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (noneCheckBox.isSelected()) {
                    for (int i = 1; i < finalCheckBoxes.length; i++) {
                        finalCheckBoxes[i].setSelected(false);
                    }
                }
            }
        });

        return finalCheckBoxes; // Return the final array
    }
    
    
    private void displayGPTResponse(String gptResponse) {
        // Clear current content
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        // Create a JTextArea for the response
        JTextArea responseTextArea = new JTextArea(20, 50); // Set preferred size
        responseTextArea.setText(gptResponse);
        responseTextArea.setWrapStyleWord(true);
        responseTextArea.setLineWrap(true);
        responseTextArea.setEditable(false);

        // Add JTextArea to JScrollPane for scrollability
        JScrollPane scrollPane = new JScrollPane(responseTextArea);
        add(scrollPane, BorderLayout.CENTER);

        // Refresh the frame to display the new components
        revalidate();
        repaint();
    }

    private void sendEmailToServer(String email) {
        try {
            // Ensure the PrintWriter is initialized
            if (out != null) {
                
                // Use ObjectOutputStream to send the User object over the network
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                objectOutputStream.writeObject(email);
                objectOutputStream.flush();
                
                // Optionally, you can notify the user that the data has been sent successfully
                JOptionPane.showMessageDialog(UserDetailsGUI.this, "User email sent successfully!");

                // Use BufferedReader to receive the String response from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // Create a StringBuilder to store the multiline response
                StringBuilder responseBuilder = new StringBuilder();
                String line;

                // Read lines until the end of the stream or until the delimiter is encountered
                while ((line = reader.readLine()) != null) {
                    // System.out.println("Received line from server: " + line);
                    if ("END_OF_RESPONSE".equals(line)) {
                        break; // Exit the loop when the delimiter is encountered
                    }
                    responseBuilder.append(line).append("\n");
                }

                // Get the final multiline response as a String
                String gptResponse = responseBuilder.toString();

                // System.out.println("Received GPT response from server: " + gptResponse);
                
                // Display the GPT response in the GUI
                displayGPTResponse(gptResponse);
                
                
            } else {
                // Handle the case where PrintWriter is not initialized
                JOptionPane.showMessageDialog(UserDetailsGUI.this, "Error: PrintWriter not initialized.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(UserDetailsGUI.this, "Error sending user email: " + ex.getMessage());
        }
    }


    private class ConnectActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
			// Set up client connection
			try {
				clientSocket = new Socket("localhost", 9898);
				// Scanner serverInput = new Scanner(clientSocket.getInputStream());
				out = new PrintWriter(clientSocket.getOutputStream(), true);

				
			} catch (Exception e1) {
				throw new RuntimeException("Error connecting to the server.", e1);
			}
		}
    }
    
    private class CloseActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
			try {
				if (clientSocket != null && !clientSocket.isClosed()) {
					clientSocket.close();
					// textArea.append("Connection closed\n");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
				// textArea.append("Error closing connection: " + ex.getMessage() + "\n");
			}finally {
				// Dispose of the JFrame
				dispose();
			}
        }
    }

    private class GoBackActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Call createInitialScreen to go back to the initial screen
            createInitialScreen();
        }
    }
    
    private void sendUserToServer(User user) {
        try {
            // Ensure the PrintWriter is initialized
            if (out != null) {
                // Use ObjectOutputStream to send the User object over the network
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                objectOutputStream.writeObject(user);
                objectOutputStream.flush();
                
                // Notify that User Data has been sent successfully
                JOptionPane.showMessageDialog(UserDetailsGUI.this, "User data sent successfully!");
            } else {
                // Handle the case where PrintWriter is not initialized
                JOptionPane.showMessageDialog(UserDetailsGUI.this, "Error: PrintWriter not initialized.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            // Notify that User Data has not been sent successfully
            JOptionPane.showMessageDialog(UserDetailsGUI.this, "Error sending user data: " + ex.getMessage());
        }
    }


    private void addCheckBoxes(JCheckBox[] checkBoxes) {
        for (JCheckBox checkBox : checkBoxes) {
            add(checkBox);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UserDetailsGUI();
            }
        });
    }

}