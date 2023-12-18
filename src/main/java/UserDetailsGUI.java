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
	// Declaration of UI components and variables
    private JTextField nameField, emailField, cityField, ageField, heightField, weightField, targetWeightField;
    private JComboBox<String> genderComboBox, ethnicityComboBox, medicalConditionsComboBox, dietTypeComboBox, allergiesComboBox, goalsComboBox;
    private JCheckBox[] vegetableCheckBoxes, fruitCheckBoxes, dairyCheckBoxes, meatAndEggsCheckBoxes, wholeGrainsCheckBoxes;
    private JPanel inputPanel;
    private JTextArea responseArea;
    private JScrollPane scrollPane;
    
    private JPanel initialPanel;
    private JButton fetchButton, generateButton;
    
    // Socket variables for network communication
 	Socket clientSocket;
 	PrintWriter out;

    public UserDetailsGUI() {
    	setTitle("TailorFit: Personalized Fitness Recommendations");
        createInitialScreen();
    }
    

    // Method to create inital screen layout
    private void createInitialScreen() {
    	getContentPane().removeAll();
        getContentPane().setBackground(Color.decode("#F8F8FF")); // Light background color for the content pane

        // Set a larger default size for the JFrame
        setSize(800, 600); // Example size, you can adjust this

        initialPanel = new JPanel();
        initialPanel.setLayout(new GridBagLayout());
        initialPanel.setBackground(Color.decode("#F8F8FF"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);

        Color buttonBackgroundColor = Color.decode("#176B87"); // A dark teal color

        fetchButton = new RoundedButton("Fetch Previous Recommendation");
        generateButton = new RoundedButton("Generate New Recommendation");

        // Style the buttons
        styleButton(fetchButton, buttonBackgroundColor);
        styleButton(generateButton, buttonBackgroundColor);

        // Add buttons to the panel
        initialPanel.add(fetchButton, gbc);
        initialPanel.add(generateButton, gbc);

        // Set up action listeners for buttons
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

        // Set up the menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem connectMenuItem = new JMenuItem("Connect");
        connectMenuItem.addActionListener(new ConnectActionListener());
        fileMenu.add(connectMenuItem);

        JMenuItem closeMenuItem = new JMenuItem("Close");
        closeMenuItem.addActionListener(new CloseActionListener());
        fileMenu.add(closeMenuItem);

        JMenuItem goBackMenuItem = new JMenuItem("Go back to initial screen");
        goBackMenuItem.addActionListener(new GoBackActionListener());
        fileMenu.add(goBackMenuItem);

        // Add the panel to the frame
        getContentPane().add(initialPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        revalidate();
        repaint();
    }
    
    private void styleButton(JButton button, Color backgroundColor) {
        button.setFont(new Font("Calibri", Font.PLAIN, 14));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
    }

    // Custom JButton class with rounded corners
    class RoundedButton extends JButton {
        private static final int ARC_WIDTH = 20;
        private static final int ARC_HEIGHT = 20;

        public RoundedButton(String label) {
            super(label);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(getBackground());
            g.fillRoundRect(0, 0, getWidth(), getHeight(), ARC_WIDTH, ARC_HEIGHT);
            super.paintComponent(g);
        }
    }
    
    // Method to create the screen for fetching recommendations
    private void createFetchRecommendationScreen() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        // Set a background color for the fetchPanel
        JPanel fetchPanel = new JPanel(new GridLayout(0, 2));
        fetchPanel.setBackground(Color.decode("#F8F8FF")); // Light background color for the panel

        // Create the email input field and label
        JLabel emailLabel = new JLabel("Enter Email ID:");
        final JTextField emailField = new JTextField();
        emailLabel.setLabelFor(emailField); // Associate the label with the input field

        // Style the submit button
        JButton submitButton = new JButton("Submit");
        submitButton.setOpaque(true);
        submitButton.setBorderPainted(false);
        submitButton.setBackground(Color.decode("#176B87")); // Dark teal background color
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Calibri", Font.PLAIN, 14)); // Calibri font

        // Add components to the fetchPanel
        fetchPanel.add(emailLabel);
        fetchPanel.add(emailField);

        // Create a panel to hold the submit button and center it
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(fetchPanel.getBackground()); // Use the same background color
        buttonPanel.add(submitButton);

        // Add action listener to the submit button
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                sendEmailToServer(email);
            }
        });

        // Add the fetchPanel and buttonPanel to the frame
        add(fetchPanel, BorderLayout.NORTH); // The form at the top
        add(buttonPanel, BorderLayout.CENTER); // The submit button centered

        revalidate();
        repaint();
    }
    
    // Method to set up the UI for new recommendation
    private void setupNewRecommendationUI() {
    	// Define the colors
    	Color primaryColor = Color.decode("#003366"); // Deep Blue
        Color secondaryColor = Color.decode("#FFFDD0"); // Electric Lime
        Color tertiaryColor = Color.decode("#D3D3D3"); // Light Grey
        Color highlightColor = Color.decode("#FF6EC7"); // Neon Pink
        Color backgroundColor = Color.decode("#F8F8FF"); // Off-White
        Color fontColorPrimary = Color.decode("#505050"); // Dark Grey
        Color fontColorSecondary = Color.WHITE; // White
        
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

        // Adding "Go back to initial screen" menu item
        JMenuItem goBackMenuItem = new JMenuItem("Go back to initial screen");
        goBackMenuItem.addActionListener(new GoBackActionListener());
        fileMenu.add(goBackMenuItem);
        

        
        // Setting up input panel for user data entry
        inputPanel = new JPanel(new GridLayout(0, 2));
        
        // Adding user input fields to the panel
        // Name, email, city, gender, ethnicity, age, height, weight, medical conditions, diet type, allergies, goals, target weight
        // Each label and input field is added to the panel
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setOpaque(true);
        nameLabel.setForeground(fontColorSecondary);
        nameLabel.setBackground(primaryColor);
        nameLabel.setFont(new Font("Montserrat", Font.BOLD, 14));
        nameLabel.setHorizontalAlignment(JLabel.CENTER);
        add(nameLabel);
        nameField = new JTextField();
        add(nameField);

        // Email
        JLabel emailLabel = new JLabel("Email ID:");
        emailLabel.setOpaque(true);
        emailLabel.setForeground(fontColorPrimary);
        emailLabel.setBackground(secondaryColor);
        emailLabel.setFont(new Font("Montserrat", Font.BOLD, 14));
        emailLabel.setHorizontalAlignment(JLabel.CENTER);
        add(emailLabel);
        emailField = new JTextField();
        add(emailField);

        // City
        JLabel cityLabel = new JLabel("City:");
        cityLabel.setOpaque(true);
        cityLabel.setForeground(fontColorSecondary); 
        cityLabel.setBackground(primaryColor);
        cityLabel.setFont(new Font("Montserrat", Font.BOLD, 14));
        cityLabel.setHorizontalAlignment(JLabel.CENTER);
        add(cityLabel);
        cityField = new JTextField();
        add(cityField);

        // Gender
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setOpaque(true);
        genderLabel.setForeground(fontColorPrimary);
        genderLabel.setBackground(secondaryColor);
        genderLabel.setFont(new Font("Montserrat", Font.BOLD, 14));
        genderLabel.setHorizontalAlignment(JLabel.CENTER);
        add(genderLabel);
        String[] genders = {"Male", "Female", "Transgender", "Non-Binary", "Prefer not to respond"};
        genderComboBox = new JComboBox<>(genders);
        genderComboBox.setBackground(backgroundColor);
        genderComboBox.setForeground(fontColorPrimary);
        add(genderComboBox);

        // Ethnicity
        JLabel ethnicLabel = new JLabel("Ethnicity:");
        ethnicLabel.setOpaque(true);
        ethnicLabel.setForeground(fontColorSecondary);
        ethnicLabel.setBackground(primaryColor);
        ethnicLabel.setFont(new Font("Montserrat", Font.BOLD, 14));
        ethnicLabel.setHorizontalAlignment(JLabel.CENTER);
        add(ethnicLabel);
        String[] ethnicities = {"African American", "Asian", "Caucasian", "Hispanic", "Other"};
        ethnicityComboBox = new JComboBox<>(ethnicities);
        ethnicityComboBox.setBackground(backgroundColor);
        ethnicityComboBox.setForeground(fontColorPrimary);
        add(ethnicityComboBox);

        // Age
        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setOpaque(true);
        ageLabel.setForeground(fontColorPrimary);
        ageLabel.setBackground(secondaryColor);
        ageLabel.setFont(new Font("Montserrat", Font.BOLD, 14));
        ageLabel.setHorizontalAlignment(JLabel.CENTER);
        add(ageLabel);
        ageField = new JTextField();
        add(ageField);

        // Height
        JLabel heightLabel = new JLabel("Height (in cm):");
        heightLabel.setOpaque(true);
        heightLabel.setForeground(fontColorSecondary);
        heightLabel.setBackground(primaryColor);
        heightLabel.setFont(new Font("Montserrat", Font.BOLD, 14));
        heightLabel.setHorizontalAlignment(JLabel.CENTER);
        add(heightLabel);
        heightField = new JTextField();
        add(heightField);

        // Weight
        JLabel weightLabel = new JLabel("Current Weight (in lb):");
        weightLabel.setOpaque(true);
        weightLabel.setForeground(fontColorPrimary);
        weightLabel.setBackground(secondaryColor);
        weightLabel.setFont(new Font("Montserrat", Font.BOLD, 14));
        weightLabel.setHorizontalAlignment(JLabel.CENTER);
        add(weightLabel);
        weightField = new JTextField();
        add(weightField);

        //Any conditions
        JLabel condition=new JLabel("Any existing medical conditions or physical limitations:");
        condition.setOpaque(true);
        condition.setForeground(fontColorSecondary);
        condition.setBackground(primaryColor);
        condition.setFont(new Font("Montserrat", Font.BOLD, 14));
        condition.setHorizontalAlignment(JLabel.CENTER);
        add(condition);

        String[] medicalConditions = {"None", "Cardiovascular Conditions", "Diabetes", "Joint or Muscle Issues", "Respiratory Conditions"};
        medicalConditionsComboBox = new JComboBox<>(medicalConditions);
        add(medicalConditionsComboBox);

        //Diet Type
        JLabel diet=new JLabel("Select Diet type:");
        diet.setOpaque(true);
        diet.setForeground(fontColorPrimary);
        diet.setBackground(secondaryColor);
        diet.setFont(new Font("Montserrat", Font.BOLD, 14));
        diet.setHorizontalAlignment(JLabel.CENTER);
        add(diet);
        String[] dietTypes = {"Vegetarian",  "Omnivore", "Vegan", "Pescatarian"};
        dietTypeComboBox = new JComboBox<>(dietTypes);
        add(dietTypeComboBox);

      //Vegetables
        JLabel veggies=new JLabel("Select Vegetables:");
        veggies.setOpaque(true);
        veggies.setForeground(fontColorSecondary);
        veggies.setBackground(primaryColor);
        veggies.setFont(new Font("Montserrat", Font.BOLD, 14));
        add(veggies);
        add(new JLabel());
        String[] vegetables = {"Avacado", "Carrot", "Cauliflower", "Beans", "Potato", "Cabbage", "Spinach", "Tomato", "Onion", "Eggplant", "Bell Pepper"};
        vegetableCheckBoxes = createCheckBoxes(vegetables, "None");
        addCheckBoxes(vegetableCheckBoxes);
        for (JCheckBox checkBox : vegetableCheckBoxes) {
            checkBox.setOpaque(true);
            checkBox.setBackground(secondaryColor); 
            checkBox.setForeground(fontColorPrimary);
        }


        //Fruits
        JLabel fru=new JLabel("Select Fruits:");
        fru.setOpaque(true);
        fru.setForeground(fontColorSecondary);
        fru.setBackground(primaryColor);
        fru.setFont(new Font("Montserrat", Font.BOLD, 14));
        add(fru);
        add(new JLabel());
        String[] fruits = {"Orange", "Apple", "Banana", "Grape", "Mango", "Pineapple", "Papaya"};
        fruitCheckBoxes = createCheckBoxes(fruits, "None");
        addCheckBoxes(fruitCheckBoxes);
        for (JCheckBox checkBox : fruitCheckBoxes) {
        	checkBox.setOpaque(true);
            checkBox.setBackground(secondaryColor); 
            checkBox.setForeground(fontColorPrimary);
        }



        //Dairy
        JLabel dairyLabel=new JLabel("Select Dairy:");
        dairyLabel.setOpaque(true);
        dairyLabel.setForeground(fontColorSecondary);
        dairyLabel.setBackground(primaryColor);
        dairyLabel.setFont(new Font("Montserrat", Font.BOLD, 14));
        add(dairyLabel);
        add(new JLabel());
        String[] dairy = {"Cheddar Cheese", "Cottage Cheese", "Almond Milk", "Milk", "Coconut Milk", "Yogurt", "Whey Protein"};
        dairyCheckBoxes = createCheckBoxes(dairy, "None");
        addCheckBoxes(dairyCheckBoxes);
        for (JCheckBox checkBox : dairyCheckBoxes) {
        	checkBox.setOpaque(true);
            checkBox.setBackground(secondaryColor); 
            checkBox.setForeground(fontColorPrimary);
        }


        //Meat
        JLabel meatLabel=new JLabel("Select Meat & Eggs:");
        meatLabel.setOpaque(true);
        meatLabel.setForeground(fontColorSecondary);
        meatLabel.setBackground(primaryColor);
        meatLabel.setFont(new Font("Montserrat", Font.BOLD, 14));
        add(meatLabel);
        add(new JLabel());
        String[] meatAndEggs = {"Chicken", "Mutton", "Seafood", "Turkey", "Beef", "Eggs", "Pork"};
        meatAndEggsCheckBoxes = createCheckBoxes(meatAndEggs, "None");
        addCheckBoxes(meatAndEggsCheckBoxes);
        for (JCheckBox checkBox : meatAndEggsCheckBoxes) {
        	checkBox.setOpaque(true);
            checkBox.setBackground(secondaryColor); 
            checkBox.setForeground(fontColorPrimary);
        }


        //Grains
        JLabel grainLabel=new JLabel("Select Whole Grains:");
        grainLabel.setOpaque(true);
        grainLabel.setForeground(fontColorSecondary);
        grainLabel.setBackground(primaryColor);
        grainLabel.setFont(new Font("Calibri", Font.BOLD, 14));
        add(grainLabel);
        add(new JLabel());
        String[] wholeGrains = {"Millet", "Oats", "Rice", "Quinoa", "Wheat"};
        wholeGrainsCheckBoxes = createCheckBoxes(wholeGrains, "None");
        addCheckBoxes(wholeGrainsCheckBoxes);
        for (JCheckBox checkBox : wholeGrainsCheckBoxes) {
        	checkBox.setOpaque(true);
            checkBox.setBackground(secondaryColor); 
            checkBox.setForeground(fontColorPrimary);
        }

        //Allergies
        JLabel allergyLabel=new JLabel("Any Food Allergies:");
        allergyLabel.setOpaque(true);
        allergyLabel.setForeground(fontColorSecondary);
        allergyLabel.setBackground(primaryColor);
        allergyLabel.setFont(new Font("Montserrat", Font.BOLD, 14));
        add(allergyLabel);
        String[] allergies = {"None", "Dairy", "Nuts", "Seafood", "Gluten"};
        allergiesComboBox = new JComboBox<>(allergies);
        add(allergiesComboBox);

        //Goal
        JLabel goalLabel=new JLabel("Enter Goal:");
        goalLabel.setOpaque(true);
        goalLabel.setForeground(fontColorPrimary);
        goalLabel.setBackground(secondaryColor);
        goalLabel.setFont(new Font("Montserrat", Font.BOLD, 14));
        add(goalLabel);
        String[] goals = {"Lose Weight", "Gain Muscle"};
        goalsComboBox = new JComboBox<>(goals);
        add(goalsComboBox);



        //Target Weight

        JLabel targetWeight=new JLabel("Enter Target Weight (in lb):");
        targetWeight.setOpaque(true);
        targetWeight.setForeground(fontColorSecondary);
        targetWeight.setBackground(primaryColor);
        targetWeight.setFont(new Font("Montserrat", Font.BOLD, 14));
        add(targetWeight);

        
        targetWeightField = new JTextField();
        add(targetWeightField);

        // Initialize the JTextArea and JScrollPane for displaying responses
        responseArea = new JTextArea();
        responseArea.setEditable(false); // Make it read-only
        scrollPane = new JScrollPane(responseArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(primaryColor));
        scrollPane.setPreferredSize(new Dimension(800, 1200));

        
        // Adding submit button with action listener
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
            	// Validate user inputs before proceeding
            	if (!validateInputs()) {
                    JOptionPane.showMessageDialog(UserDetailsGUI.this, "Please fill in all required fields with valid information.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                getContentPane().removeAll();
                responseArea.setText("Generating Fitness Plan...\nWait for approximately two minutes...\nTill then go get some push ups done!!!");
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
    
    // Method to validate checkbox selections in the UI
    private boolean validateCheckboxSelection(JCheckBox[] checkBoxes) {
    	// Check if 'None' is selected or any other checkbox is selected
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
    
    // Method to validate all user inputs
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

    // Method to create a User object from input fields
    private User createUser() {
    	// Extract data from input fields and create a User object
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
    
    // Method to get selected items from checkboxes
    private String[] getSelectedItems(JCheckBox[] checkBoxes) {
    	// Get the selected items from the checkbox array
        ArrayList<String> selectedItems = new ArrayList<>();
        for (JCheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) {
                selectedItems.add(checkBox.getText());
            }
        }
        return selectedItems.toArray(new String[0]);
    }
    
    // Method to create checkboxes for options
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
    
    // Method to display the GPT response
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
    
    // Method to send email to the server
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

    // ActionListener for "Connect" menu item
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
    
    // ActionListener for "Close" menu item
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
    
    // ActionListener for "Go back to initial screen" menu item
    private class GoBackActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Call createInitialScreen to go back to the initial screen
            createInitialScreen();
        }
    }
    
    // Method to send user data to the server
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

    // Method to add checkboxes to the UI
    private void addCheckBoxes(JCheckBox[] checkBoxes) {
        for (JCheckBox checkBox : checkBoxes) {
            add(checkBox);
        }
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UserDetailsGUI();
            }
        });
    }

}