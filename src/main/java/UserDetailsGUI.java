import javax.swing.*;

import org.apache.http.auth.KerberosCredentials;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
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
        initialPanel.setBackground(Color.decode("#3559E0"));

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

        JLabel emailID=new JLabel("Enter Email ID:");
        emailID.setOpaque(true);
        emailID.setBackground(Color.decode("#64CCC5"));
        emailID.setForeground(Color.decode("#DAFFFB"));
        emailID.setFont(new Font("Arial", Font.BOLD, 14));
        fetchPanel.add(emailID);
        fetchPanel.add(emailField);
        fetchPanel.add(submitButton);
        submitButton.setOpaque(true);
        submitButton.setBorderPainted(false); // Needed for some look and feels like Nimbus
        submitButton.setBackground(Color.decode("#176B87")); // Choose the color you want
        submitButton.setForeground(Color.WHITE); 

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                sendEmailToServer(email);
                /*
                if (checkEmailExists(email)) {
                    // Fetch and display previous recommendation
                    // This requires implementation based on your database
                } else {
                    JOptionPane.showMessageDialog(UserDetailsGUI.this, "Email not found. Please enter details for a new recommendation.");
                    setupNewRecommendationUI();
                }
                */
            }
        });

        add(fetchPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    private boolean checkEmailExists(String email) {
        // Implement database check for email
        // Return true if exists, false otherwise
        return false; // Placeholder
    }
    
    private void setupNewRecommendationUI() {
        getContentPane().removeAll();
        setLayout(new GridLayout(0, 2));
        JSeparator separator = new JSeparator();
        separator.setVisible(false);
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

        //Name
        JLabel nameLabel =new JLabel("Name:");
        nameLabel.setOpaque(true);
        nameLabel.setForeground(Color.decode("#DAFFFB")); 
        nameLabel.setBackground(Color.decode("#04364A"));
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setHorizontalAlignment(JLabel.CENTER);
        add(nameLabel);
        nameField = new JTextField();
        add(nameField);
        
        //Email
        JLabel emailLabel= new JLabel("Email ID:");
        emailLabel.setOpaque(true);
        emailLabel.setForeground(Color.decode("#D04364A")); 
        emailLabel.setBackground(Color.decode("#DAFFFB"));
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setHorizontalAlignment(JLabel.CENTER);
        add(emailLabel);
        emailField = new JTextField();
        add(emailField);

        //City
        JLabel cityLabel=new JLabel("City:");
        cityLabel.setOpaque(true);
        cityLabel.setForeground(Color.decode("#DAFFFB")); 
        cityLabel.setBackground(Color.decode("#04364A"));
        cityLabel.setFont(new Font("Arial", Font.BOLD, 14));
        cityLabel.setHorizontalAlignment(JLabel.CENTER);
        add(cityLabel);
        cityField = new JTextField();
        add(cityField);

        //Gender
        JLabel genderLabel=new JLabel("Gender:");
        genderLabel.setOpaque(true);
        genderLabel.setForeground(Color.decode("#04364A"));
        genderLabel.setBackground(Color.decode("#DAFFFB"));
        genderLabel.setFont(new Font("Arial", Font.BOLD, 14));
        genderLabel.setHorizontalAlignment(JLabel.CENTER);
        add(genderLabel);
        String[] genders = {"Male", "Female", "Transgender", "Non-Binary", "Prefer not to respond"};
        genderComboBox = new JComboBox<>(genders);
        add(genderComboBox);
        add(new JSeparator());
        add(new JSeparator());

        //Ethnicity
        JLabel ethnic=new JLabel("Ethnicity:");
        ethnic.setOpaque(true);
        ethnic.setForeground(Color.decode("#DAFFFB"));
        ethnic.setBackground(Color.decode("#04364A"));
        ethnic.setFont(new Font("Arial", Font.BOLD, 14));
        ethnic.setHorizontalAlignment(JLabel.CENTER);
        add(ethnic);
        String[] ethnicities = {"African American", "Asian", "Caucasian", "Hispanic", "Other"};
        ethnicityComboBox = new JComboBox<>(ethnicities);
        add(ethnicityComboBox);


        //Age
        JLabel age=new JLabel("Age:");
        age.setOpaque(true);
        age.setForeground(Color.decode("#04364A"));
        age.setBackground(Color.decode("#DAFFFB"));
        age.setFont(new Font("Arial", Font.BOLD, 14));
        age.setHorizontalAlignment(JLabel.CENTER);
        add(age);
        ageField = new JTextField();
        add(ageField);

        //Height
        JLabel height=new JLabel("Height (in cm):");
        height.setOpaque(true);
        height.setForeground(Color.decode("#DAFFFB"));
        height.setBackground(Color.decode("#04364A"));
        height.setFont(new Font("Arial", Font.BOLD, 14));
        height.setHorizontalAlignment(JLabel.CENTER);
        add(height);
        heightField = new JTextField();
        add(heightField);

        //Weight
        JLabel weight=new JLabel("Current Weight (in lb):");
        weight.setOpaque(true);
        weight.setForeground(Color.decode("#04364A"));
        weight.setBackground(Color.decode("#DAFFFB"));
        weight.setFont(new Font("Arial", Font.BOLD, 14));
        weight.setHorizontalAlignment(JLabel.CENTER);
        add(weight);
        weightField = new JTextField();
        add(weightField);

        //Any conditions
        JLabel condition=new JLabel("Any existing medical conditions or physical limitations:");
        condition.setOpaque(true);
        condition.setForeground(Color.decode("#DAFFFB"));
        condition.setBackground(Color.decode("#04364A"));
        condition.setFont(new Font("Arial", Font.BOLD, 14));
        condition.setHorizontalAlignment(JLabel.CENTER);
        add(condition);

        String[] medicalConditions = {"Cardiovascular Conditions", "Respiratory Conditions", "Joint or Muscle Issues", "None"};
        medicalConditionsComboBox = new JComboBox<>(medicalConditions);
        add(medicalConditionsComboBox);
        add(new JSeparator());
        add(new JSeparator());


        //Diet Type
        JLabel diet=new JLabel("Select Diet type:");
        diet.setOpaque(true);
        diet.setForeground(Color.decode("#04364A"));
        diet.setBackground(Color.decode("#DAFFFB"));
        diet.setFont(new Font("Arial", Font.BOLD, 14));
        diet.setHorizontalAlignment(JLabel.CENTER);
        add(diet);
        String[] dietTypes = {"Vegan", "Omnivore", "Pescatarian", "Vegetarian"};
        dietTypeComboBox = new JComboBox<>(dietTypes);
        add(dietTypeComboBox);
        // Add here
        add(new JSeparator());
        add(new JSeparator());

        //Vegetables
        JLabel veggies=new JLabel("Select Vegetables:");
        veggies.setOpaque(true);
        veggies.setForeground(Color.decode("#DAFFFB"));
        veggies.setBackground(Color.decode("#04364A"));
        veggies.setFont(new Font("Arial", Font.BOLD, 14));
        add(veggies);
        add(new JLabel());
        String[] vegetables = {"Carrot", "Cauliflower", "Beans", "Potato", "Cabbage", "Spinach", "Tomato", "Onion", "Eggplant", "Bell Pepper"};
        vegetableCheckBoxes = createCheckBoxes(vegetables, "None");
        addCheckBoxes(vegetableCheckBoxes);
        for (JCheckBox checkBox : vegetableCheckBoxes) {
            checkBox.setOpaque(true);
            checkBox.setBackground(Color.decode("#DAFFFB")); // Set to your desired color
        }

        //Dummy
        JLabel f=new JLabel();
        f.setOpaque(true);
        f.setBackground(Color.decode("#DAFFFB"));
        add(f);
        add(new JSeparator());
        add(new JSeparator());


        //Fruits
        JLabel fru=new JLabel("Select Fruits:");
        fru.setOpaque(true);
        fru.setForeground(Color.decode("#DAFFFB"));
        fru.setBackground(Color.decode("#04364A"));
        fru.setFont(new Font("Arial", Font.BOLD, 14));
        add(fru);
        add(new JLabel());
        String[] fruits = {"Orange", "Apple", "Banana", "Grape", "Mango", "Pineapple", "Papaya"};
        fruitCheckBoxes = createCheckBoxes(fruits, "None");
        addCheckBoxes(fruitCheckBoxes);
        for (JCheckBox checkBox : fruitCheckBoxes) {
            checkBox.setOpaque(true);
            checkBox.setBackground(Color.decode("#DAFFFB")); // Set to your desired color
        }

        add(new JSeparator());
        add(new JSeparator());



        //Dairy
        JLabel dairyLabel=new JLabel("Select Dairy:");
        dairyLabel.setOpaque(true);
        dairyLabel.setForeground(Color.decode("#DAFFFB"));
        dairyLabel.setBackground(Color.decode("#04364A"));
        dairyLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(dairyLabel);
        add(new JLabel());
        String[] dairy = {"Cheddar Cheese", "Cottage Cheese", "Almond Milk", "Milk", "Coconut Milk", "Yogurt"};
        dairyCheckBoxes = createCheckBoxes(dairy, "None");
        addCheckBoxes(dairyCheckBoxes);
        for (JCheckBox checkBox : dairyCheckBoxes) {
            checkBox.setOpaque(true);
            checkBox.setBackground(Color.decode("#DAFFFB")); // Set to your desired color
        }
        add(new JLabel());
        add(new JSeparator());
        add(new JSeparator());


        //Meat
        JLabel meatLabel=new JLabel("Select Meat & Eggs:");
        meatLabel.setOpaque(true);
        meatLabel.setForeground(Color.decode("#DAFFFB"));
        meatLabel.setBackground(Color.decode("#04364A"));
        meatLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(meatLabel);
        add(new JLabel());
        String[] meatAndEggs = {"Chicken", "Mutton", "Seafood", "Turkey", "Beef", "Eggs"};
        meatAndEggsCheckBoxes = createCheckBoxes(meatAndEggs, "None");
        addCheckBoxes(meatAndEggsCheckBoxes);
        for (JCheckBox checkBox : meatAndEggsCheckBoxes) {
            checkBox.setOpaque(true);
            checkBox.setBackground(Color.decode("#DAFFFB")); // Set to your desired color
        }
        add(new JLabel());
        add(new JSeparator());
        add(new JSeparator());


        //Grains
        JLabel grainLabel=new JLabel("Select Whole Grains:");
        grainLabel.setOpaque(true);
        grainLabel.setForeground(Color.decode("#DAFFFB"));
        grainLabel.setBackground(Color.decode("#04364A"));
        grainLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(grainLabel);
        add(new JLabel());
        String[] wholeGrains = {"Millet", "Oats", "Rice", "Quinoa"};
        wholeGrainsCheckBoxes = createCheckBoxes(wholeGrains, "None");
        addCheckBoxes(wholeGrainsCheckBoxes);
        for (JCheckBox checkBox : wholeGrainsCheckBoxes) {
            checkBox.setOpaque(true);
            checkBox.setBackground(Color.decode("#DAFFFB")); // Set to your desired color
        }
        add(new JLabel());
        add(new JSeparator());
        add(new JSeparator());

        //Allergies
        JLabel allergyLabel=new JLabel("Any Food Allergies:");
        allergyLabel.setOpaque(true);
        allergyLabel.setForeground(Color.decode("#DAFFFB"));
        allergyLabel.setBackground(Color.decode("#04364A"));
        allergyLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(allergyLabel);
        String[] allergies = {"Seafood", "Gluten", "Dairy", "Nuts", "None"};
        allergiesComboBox = new JComboBox<>(allergies);
        add(allergiesComboBox);
        add(new JSeparator(SwingConstants.HORIZONTAL));
        add(new JSeparator());

        //Goal
        JLabel goalLabel=new JLabel("Enter Goal:");
        goalLabel.setOpaque(true);
        goalLabel.setForeground(Color.decode("#04364A"));
        goalLabel.setBackground(Color.decode("#DAFFFB"));
        goalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(goalLabel);
        String[] goals = {"Lose Weight", "Gain Muscle"};
        goalsComboBox = new JComboBox<>(goals);
        add(goalsComboBox);
        add(new JSeparator(SwingConstants.HORIZONTAL));
        add(new JSeparator());


        //Target Weight

        JLabel targetWeight=new JLabel("Enter Target Weight (in lb):");
        targetWeight.setOpaque(true);
       targetWeight.setForeground(Color.decode("#DAFFFB"));
        targetWeight.setBackground(Color.decode("#04364A"));
        targetWeight.setFont(new Font("Arial", Font.BOLD, 14));
        add(targetWeight);

        
        targetWeightField = new JTextField();
        add(targetWeightField);

        // Initialize the JTextArea and JScrollPane
        responseArea = new JTextArea();
        responseArea.setEditable(false); // Make it read-only
        scrollPane = new JScrollPane(responseArea);
        scrollPane.setPreferredSize(new Dimension(800, 600));

        add(new JSeparator());
        add(new JSeparator(SwingConstants.HORIZONTAL));
        add(new JLabel(""));
        JButton submitButton = new JButton("Submit");
        submitButton.setOpaque(true);
        submitButton.setBorderPainted(false); // Needed for some look and feels like Nimbus
        submitButton.setBackground(Color.decode("#176B87")); // Choose the color you want
        submitButton.setForeground(Color.WHITE); 


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
                        System.out.println(response);
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
        getContentPane().setBackground(Color.YELLOW); // Choose your desired color

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

                // Read lines until the end of the stream
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line).append("\n");
                }

                // Get the final multiline response as a String
                String gptResponse = responseBuilder.toString();

                System.out.println("Received GPT response from server: " + gptResponse);
                
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
                
                // Optionally, you can notify the user that the data has been sent successfully
                JOptionPane.showMessageDialog(UserDetailsGUI.this, "User data sent successfully!");
            } else {
                // Handle the case where PrintWriter is not initialized
                JOptionPane.showMessageDialog(UserDetailsGUI.this, "Error: PrintWriter not initialized.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
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
