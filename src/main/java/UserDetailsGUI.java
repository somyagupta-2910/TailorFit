import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserDetailsGUI extends JFrame {
    public UserDetailsGUI() {
        Color color = UIManager.getColor("Panel. background");
        JPanel separatorPanel = new JPanel();
        separatorPanel.setPreferredSize(new Dimension(10, 1)); // Adjust the height as needed
        separatorPanel.setBackground(color); // Set the background color to simulate a line
        // Set the title of the window
        setTitle("Fitness App");

        // Set the layout manager
        setLayout(new GridLayout(0, 2));

        // Create labels and text fields for user details
        add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        add(nameField);

        add(new JLabel("Email ID:"));
        JTextField emailField = new JTextField();
        add(emailField);

        add(new JLabel("City:"));
        JTextField cityField = new JTextField();
        add(cityField);

        add(new JLabel("Gender:"));
        String[] genders = {"Male", "Female", "Transgender","Non-Binary","Prefer not to respond"};
        JComboBox<String> genderComboBox = new JComboBox<>(genders);
        add(genderComboBox);

        // add(new JLabel("Ethnicity:"));
        // JTextField ethnicityField = new JTextField();
        // add(ethnicityField);

        add(new JLabel("Ethnicity:"));
        String[] ethnicities = {"Caucasian", "African American", "Asian", "Hispanic", "Other"};
        JComboBox<String> ethnicityComboBox = new JComboBox<>(ethnicities);
        add(ethnicityComboBox);

        add(new JLabel("Age:"));
        JTextField ageField = new JTextField();
        add(ageField);

        add(new JLabel("Height:"));
        JTextField heightField = new JTextField();
        add(heightField);

        add(new JLabel("Current Weight:"));
        JTextField weightField = new JTextField();
        add(weightField);

        // Add a line
        add(new JSeparator());
        add(new JSeparator());

        // Create labels and dropdowns for medical conditions
        add(new JLabel("Any existing medical conditions or physical limitations:"));
        String[] medicalConditions = {"Cardiovascular Conditions", "Respiratory Conditions", "Joint or Muscle Issues", "Other (Please Specify)", "None"};
        JComboBox<String> medicalConditionsComboBox = new JComboBox<>(medicalConditions);
        add(medicalConditionsComboBox);

        // Text box for specifying other medical conditions
        // JTextField otherMedicalConditionField = new JTextField();
        // add(otherMedicalConditionField);

        // Add a line
        add(new JSeparator());
        add(new JSeparator());
        // Create labels and dropdowns for diet type
        add(new JLabel("Select Diet type:"));
        String[] dietTypes = {"Vegan", "Omnivore", "Pescatarian", "Vegetarian"};
        JComboBox<String> dietTypeComboBox = new JComboBox<>(dietTypes);
        add(dietTypeComboBox);

        // Add a line
        add(new JSeparator());
        add(new JSeparator());
        // Create checkboxes for selecting vegetables, fruits, dairy, meat & eggs, whole grains
        add(new JLabel("Select Vegetables:"));
        // add(separatorPanel);
        // add(separatorPanel);
        // add(separatorPanel);
        //add(new JSeparator());
        String[] vegetables = {"Carrot", "Cauliflower", "Beans", "Potato", "Cabbage", "Spinach", "Tomato", "Onion", "Eggplant", "Bell Pepper"};
        JCheckBox[] vegetableCheckBoxes = createCheckBoxes(vegetables);
        addCheckBoxes(vegetableCheckBoxes);

         // Add a line
        // add(new JSeparator());
        // add(new JSeparator());
        // add(new JSeparator());
        // Create an empty panel to act as a separator

        add(separatorPanel);
        //add(new JSeparator());
        add(new JSeparator());
        add(new JLabel("Select Fruits:"));
        String[] fruits = {"Orange", "Apple", "Banana", "Grape", "Mango", "Pineapple", "Papaya"};
        JCheckBox[] fruitCheckBoxes = createCheckBoxes(fruits);
        addCheckBoxes(fruitCheckBoxes);

        // Add a line
        //add(new JSeparator());
        add(separatorPanel);
        add(new JLabel("Select Dairy:"));
        String[] dairy = {"Cheddar Cheese", "Cottage Cheese", "Almond Milk", "Milk", "Coconut Milk", "Yogurt"};
        JCheckBox[] dairyCheckBoxes = createCheckBoxes(dairy);
        addCheckBoxes(dairyCheckBoxes);

        // Add a line
        add(new JSeparator());
        add(new JSeparator());
        add(new JLabel("Select Meat & Eggs:"));
        String[] meatAndEggs = {"Chicken", "Mutton", "Seafood", "Turkey", "Beef", "Eggs"};
        JCheckBox[] meatAndEggsCheckBoxes = createCheckBoxes(meatAndEggs);
        addCheckBoxes(meatAndEggsCheckBoxes);

        // Add a line
        add(new JSeparator());
        add(new JSeparator());
        add(new JLabel("Select Whole Grains:"));
        String[] wholeGrains = {"Millet", "Oats", "Rice", "Quinoa"};
        JCheckBox[] wholeGrainsCheckBoxes = createCheckBoxes(wholeGrains);
        addCheckBoxes(wholeGrainsCheckBoxes);

        // Add a line
        add(new JSeparator());
        add(new JSeparator());
        // Create labels and dropdowns for food allergies
        add(separatorPanel);
        add(new JLabel("Any Food Allergies:"));
        String[] allergies = {"Seafood", "Gluten", "Dairy", "Nuts", "Other (Please Specify)", "None"};
        JComboBox<String> allergiesComboBox = new JComboBox<>(allergies);
        add(allergiesComboBox);
        add(separatorPanel);
        // Add a line
        add(new JSeparator());

        // // Text box for specifying other food allergies
        // JTextField otherAllergiesField = new JTextField();
        // add(otherAllergiesField);

        // Add a line
        add(new JSeparator());
        add(separatorPanel);
        // Create label and dropdown for user's goal
        add(new JLabel("Enter Goal:"));
        String[] goals = {"Lose Weight", "Gain Muscle"};
        JComboBox<String> goalsComboBox = new JComboBox<>(goals);
        add(goalsComboBox);

        // Add a line
        add(new JSeparator());
        add(new JSeparator());
        add(separatorPanel);
        // Create text box for entering target weight
        add(new JLabel("Enter Target Weight:"));
        JTextField targetWeightField = new JTextField();
        add(targetWeightField);

        add(new JSeparator());
        add(new JSeparator());
        add(separatorPanel);
        // add(separatorPanel);
        // Create submit button
        JButton submitButton = new JButton("Submit");
        // Create a panel for the submit button and set its layout manager to FlowLayout with center alignment
        JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        submitPanel.add(submitButton);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle the submit button action here
                // You can retrieve the values entered by the user using the fields and dropdowns
                // For example:
                String name = nameField.getText();
                String email = emailField.getText();
                // Retrieve other values similarly

                // Print or use the values as needed
                System.out.println("Name: " + name);
                System.out.println("Email: " + email);
                // Print or use other values similarly
            }
        });
        add(submitButton);

        // Set frame properties
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }

    private JCheckBox[] createCheckBoxes(String[] options) {
        JCheckBox[] checkBoxes = new JCheckBox[options.length];
        for (int i = 0; i < options.length; i++) {
            checkBoxes[i] = new JCheckBox(options[i]);
        }
        return checkBoxes;
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

//________________
// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;

// public class UserDetailsGUI extends JFrame {
//     public UserDetailsGUI() {
//         // Set the title of the window
//         setTitle("Fitness App");
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//         // Create a main panel with a border layout
//         JPanel mainPanel = new JPanel(new BorderLayout());
//         mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

//         // Create a grid panel for labels and input fields
//         JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 5));
//         inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

//         // Create labels and text fields for user details
//         inputPanel.add(new JLabel("Name:"));
//         JTextField nameField = new JTextField();
//         inputPanel.add(nameField);

//         inputPanel.add(new JLabel("Email ID:"));
//         JTextField emailField = new JTextField();
//         inputPanel.add(emailField);

//         inputPanel.add(new JLabel("City:"));
//         JTextField cityField = new JTextField();
//         inputPanel.add(cityField);

//         inputPanel.add(new JLabel("Gender:"));
//         String[] genders = {"Male", "Female", "Transgender", "Non-Binary", "Prefer not to respond"};
//         JComboBox<String> genderComboBox = new JComboBox<>(genders);
//         inputPanel.add(genderComboBox);

//         inputPanel.add(new JLabel("Ethnicity:"));
//         String[] ethnicities = {"Caucasian", "African American", "Asian", "Hispanic", "Other"};
//         JComboBox<String> ethnicityComboBox = new JComboBox<>(ethnicities);
//         inputPanel.add(ethnicityComboBox);

//         inputPanel.add(new JLabel("Age:"));
//         JTextField ageField = new JTextField();
//         inputPanel.add(ageField);

//         inputPanel.add(new JLabel("Height:"));
//         JTextField heightField = new JTextField();
//         inputPanel.add(heightField);

//         inputPanel.add(new JLabel("Current Weight:"));
//         JTextField weightField = new JTextField();
//         inputPanel.add(weightField);

//         // Create labels and dropdowns for medical conditions
//         inputPanel.add(new JLabel("Any existing medical conditions or physical limitations:"));
//         String[] medicalConditions = {"Cardiovascular Conditions", "Respiratory Conditions", "Joint or Muscle Issues", "Other (Please Specify)", "None"};
//         JComboBox<String> medicalConditionsComboBox = new JComboBox<>(medicalConditions);
//         inputPanel.add(medicalConditionsComboBox);

//         // Create labels and dropdowns for diet type
//         inputPanel.add(new JLabel("Select Diet type:"));
//         String[] dietTypes = {"Vegan", "Omnivore", "Pescatarian", "Vegetarian"};
//         JComboBox<String> dietTypeComboBox = new JComboBox<>(dietTypes);
//         inputPanel.add(dietTypeComboBox);

//         // Create checkboxes for selecting vegetables, fruits, dairy, meat & eggs, whole grains
//         JPanel checkBoxPanel = new JPanel(new GridLayout(0, 5, 5, 5));
//         checkBoxPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

//         addCheckBoxes(checkBoxPanel, "Select Vegetables:", 
//                 "Carrot", "Cauliflower", "Beans", "Potato", "Cabbage", "Spinach", "Tomato", "Onion", "Eggplant", "Bell Pepper");

//         addCheckBoxes(checkBoxPanel, "Select Fruits:", 
//                 "Orange", "Apple", "Banana", "Grape", "Mango", "Pineapple", "Papaya");

//         addCheckBoxes(checkBoxPanel, "Select Dairy:", 
//                 "Cheddar Cheese", "Cottage Cheese", "Almond Milk", "Milk", "Coconut Milk", "Yogurt");

//         addCheckBoxes(checkBoxPanel, "Select Meat & Eggs:", 
//                 "Chicken", "Mutton", "Seafood", "Turkey", "Beef", "Eggs");

//         addCheckBoxes(checkBoxPanel, "Select Whole Grains:", "Millet", "Oats", "Rice", "Quinoa");

//         // Create labels and dropdowns for food allergies
//         inputPanel.add(new JLabel("Any Food Allergies:"));
//         String[] allergies = {"Seafood", "Gluten", "Dairy", "Nuts", "Other (Please Specify)", "None"};
//         JComboBox<String> allergiesComboBox = new JComboBox<>(allergies);
//         inputPanel.add(allergiesComboBox);

//         // Create label and dropdown for user's goal
//         inputPanel.add(new JLabel("Enter Goal:"));
//         String[] goals = {"Lose Weight", "Gain Muscle"};
//         JComboBox<String> goalsComboBox = new JComboBox<>(goals);
//         inputPanel.add(goalsComboBox);

//         // Create text box for entering target weight
//         inputPanel.add(new JLabel("Enter Target Weight:"));
//         JTextField targetWeightField = new JTextField();
//         inputPanel.add(targetWeightField);

//         // Create submit button
//         JButton submitButton = new JButton("Submit");
//         submitButton.addActionListener(new ActionListener() {
//             @Override
//             public void actionPerformed(ActionEvent e) {
//                 // Handle the submit button action here
//                 // You can retrieve the values entered by the user using the fields and dropdowns
//                 // For example:
//                 String name = nameField.getText();
//                 String email = emailField.getText();
//                 // Retrieve other values similarly

//                 // Print or use the values as needed
//                 System.out.println("Name: " + name);
//                 System.out.println("Email: " + email);
//                 // Print or use other values similarly
//             }
//         });

//         // Add components to the main panel
//         mainPanel.add(inputPanel, BorderLayout.CENTER);
//         mainPanel.add(checkBoxPanel, BorderLayout.SOUTH);
//         mainPanel.add(submitButton, BorderLayout.SOUTH);

//         // Set frame properties
//         add(mainPanel);
//         pack();
//         setLocationRelativeTo(null); // Center the frame on the screen
//         setVisible(true);
//     }

//     private void addCheckBoxes(JPanel panel, String label, String... options) {
//         JPanel checkBoxPanel = new JPanel();
//         checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.X_AXIS));
//         checkBoxPanel.add(new JLabel(label));

//         for (String option : options) {
//             JCheckBox checkBox = new JCheckBox(option);
//             checkBoxPanel.add(checkBox);
//         }

//         panel.add(checkBoxPanel);
//     }
    
    

//     public static void main(String[] args) {
//         SwingUtilities.invokeLater(UserDetailsGUI::new);
//     }
// }
//__________________________________________
// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;

// public class UserDetailsGUI extends JFrame {
//     public UserDetailsGUI() {
//         setTitle("Fitness App");
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//         JPanel mainPanel = new JPanel(new BorderLayout());
//         mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

//         JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 5));
//         inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

//         inputPanel.add(new JLabel("Name:"));
//         JTextField nameField = new JTextField();
//         inputPanel.add(nameField);

//         inputPanel.add(new JLabel("Email ID:"));
//         JTextField emailField = new JTextField();
//         inputPanel.add(emailField);

//         inputPanel.add(new JLabel("City:"));
//         JTextField cityField = new JTextField();
//         inputPanel.add(cityField);

//         inputPanel.add(new JLabel("Gender:"));
//         String[] genders = {"Male", "Female", "Transgender", "Non-Binary", "Prefer not to respond"};
//         JComboBox<String> genderComboBox = new JComboBox<>(genders);
//         inputPanel.add(genderComboBox);

//         inputPanel.add(new JLabel("Ethnicity:"));
//         String[] ethnicities = {"Caucasian", "African American", "Asian", "Hispanic", "Other"};
//         JComboBox<String> ethnicityComboBox = new JComboBox<>(ethnicities);
//         inputPanel.add(ethnicityComboBox);

//         inputPanel.add(new JLabel("Age:"));
//         JTextField ageField = new JTextField();
//         inputPanel.add(ageField);

//         inputPanel.add(new JLabel("Height:"));
//         JTextField heightField = new JTextField();
//         inputPanel.add(heightField);

//         inputPanel.add(new JLabel("Current Weight:"));
//         JTextField weightField = new JTextField();
//         inputPanel.add(weightField);

//         JPanel checkBoxPanel = new JPanel(new GridLayout(0, 1, 0, 10));
//         checkBoxPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

//         addCheckBoxes(checkBoxPanel, "Select Vegetables:", 
//                 "Carrot", "Cauliflower", "Beans", "Potato", "Cabbage", "Spinach", "Tomato", "Onion", "Eggplant", "Bell Pepper");

//         addCheckBoxes(checkBoxPanel, "Select Fruits:", 
//                 "Orange", "Apple", "Banana", "Grape", "Mango", "Pineapple", "Papaya");

//         addCheckBoxes(checkBoxPanel, "Select Dairy:", 
//                 "Cheddar Cheese", "Cottage Cheese", "Almond Milk", "Milk", "Coconut Milk", "Yogurt");

//         addCheckBoxes(checkBoxPanel, "Select Meat & Eggs:", 
//                 "Chicken", "Mutton", "Seafood", "Turkey", "Beef", "Eggs");

//         addCheckBoxes(checkBoxPanel, "Select Whole Grains:", "Millet", "Oats", "Rice", "Quinoa");

//         mainPanel.add(inputPanel, BorderLayout.CENTER);
//         mainPanel.add(checkBoxPanel, BorderLayout.SOUTH);

//         JButton submitButton = new JButton("Submit");
//         submitButton.addActionListener(new ActionListener() {
//             @Override
//             public void actionPerformed(ActionEvent e) {
//                 String name = nameField.getText();
//                 String email = emailField.getText();
//                 // Retrieve other values similarly

//                 System.out.println("Name: " + name);
//                 System.out.println("Email: " + email);
//                 // Print or use other values similarly
//             }
//         });

//         mainPanel.add(submitButton, BorderLayout.SOUTH);

//         add(mainPanel);
//         pack();
//         setLocationRelativeTo(null);
//         setVisible(true);
//     }

//     private void addCheckBoxes(JPanel panel, String label, String... options) {
//         JPanel checkBoxPanel = new JPanel();
//         checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.X_AXIS));
//         checkBoxPanel.add(new JLabel(label));

//         JPanel optionsPanel = new JPanel(new GridLayout(0, 3, 10, 5));

//         for (String option : options) {
//             JCheckBox checkBox = new JCheckBox(option);
//             optionsPanel.add(checkBox);
//         }

//         checkBoxPanel.add(optionsPanel);
//         panel.add(checkBoxPanel);
//     }

//     public static void main(String[] args) {
//         SwingUtilities.invokeLater(UserDetailsGUI::new);
//     }
// }
