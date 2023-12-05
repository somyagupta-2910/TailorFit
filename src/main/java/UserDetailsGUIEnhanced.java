import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class UserDetailsGUIEnhanced extends JFrame {
    private JTextField nameField, emailField, cityField, ageField, heightField, weightField, targetWeightField;
    private JComboBox<String> genderComboBox, ethnicityComboBox, medicalConditionsComboBox, dietTypeComboBox, allergiesComboBox, goalsComboBox;
    private JCheckBox[] vegetableCheckBoxes, fruitCheckBoxes, dairyCheckBoxes, meatAndEggsCheckBoxes, wholeGrainsCheckBoxes;
    private JPanel inputPanel;
    private JTextArea responseArea;
    private JScrollPane scrollPane;

    public UserDetailsGUIEnhanced() {
        setTitle("TailorFit");
        setLayout(new GridLayout(0, 2));

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
        String[] ethnicities = {"Caucasian", "African American", "Asian", "Hispanic", "Other"};
        ethnicityComboBox = new JComboBox<>(ethnicities);
        add(ethnicityComboBox);

        add(new JLabel("Age:"));
        ageField = new JTextField();
        add(ageField);

        add(new JLabel("Height:"));
        heightField = new JTextField();
        add(heightField);

        add(new JLabel("Current Weight:"));
        weightField = new JTextField();
        add(weightField);

        add(new JLabel("Any existing medical conditions or physical limitations:"));
        String[] medicalConditions = {"Cardiovascular Conditions", "Respiratory Conditions", "Joint or Muscle Issues", "Other (Please Specify)", "None"};
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
        add(new JLabel("Any Food Allergies:"));
        String[] allergies = {"Seafood", "Gluten", "Dairy", "Nuts", "Other (Please Specify)", "None"};
        allergiesComboBox = new JComboBox<>(allergies);
        add(allergiesComboBox);

        add(new JLabel("Enter Goal:"));
        String[] goals = {"Lose Weight", "Gain Muscle"};
        goalsComboBox = new JComboBox<>(goals);
        add(goalsComboBox);

        add(new JLabel("Enter Target Weight:"));
        targetWeightField = new JTextField();
        add(targetWeightField);

        // Initialize the JTextArea and JScrollPane
        responseArea = new JTextArea();
        responseArea.setEditable(false); // Make it read-only
        scrollPane = new JScrollPane(responseArea);
        scrollPane.setPreferredSize(new Dimension(800, 600));

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (!validateInputs()) {
                    JOptionPane.showMessageDialog(UserDetailsGUIEnhanced.this, "Please fill in all required fields with valid information.", "Input Error", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(UserDetailsGUIEnhanced.this, "Please enter a valid name using only alphabets.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate the email field with basic regex pattern
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
        if (emailField.getText().isEmpty() || !emailPattern.matcher(emailField.getText()).matches()) {
            JOptionPane.showMessageDialog(UserDetailsGUIEnhanced.this, "Please enter a valid email address.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate the city field
        if (cityField.getText().isEmpty() || !cityField.getText().matches("[A-Za-z ]+")) {
            JOptionPane.showMessageDialog(UserDetailsGUIEnhanced.this, "Please enter a valid city using only alphabets.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate the age field for positive numbers
        try {
            int age = Integer.parseInt(ageField.getText());
            if (age <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(UserDetailsGUIEnhanced.this, "Please enter a valid age as a positive number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate the weight field for positive numbers
        try {
            double weight = Double.parseDouble(weightField.getText());
            if (weight <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(UserDetailsGUIEnhanced.this, "Please enter a valid weight as a positive number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate the target weight field for positive numbers
        try {
            double targetWeight = Double.parseDouble(targetWeightField.getText());
            if (targetWeight <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(UserDetailsGUIEnhanced.this, "Please enter a valid target weight as a positive number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (!validateCheckboxSelection(vegetableCheckBoxes)
                || !validateCheckboxSelection(fruitCheckBoxes)
                || !validateCheckboxSelection(dairyCheckBoxes)
                || !validateCheckboxSelection(meatAndEggsCheckBoxes)
                || !validateCheckboxSelection(wholeGrainsCheckBoxes)) {
                JOptionPane.showMessageDialog(UserDetailsGUIEnhanced.this, "Please select at least one option or 'None' for each category.", "Input Error", JOptionPane.ERROR_MESSAGE);
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
                selectedWholeGrains
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





    private void addCheckBoxes(JCheckBox[] checkBoxes) {
        for (JCheckBox checkBox : checkBoxes) {
            add(checkBox);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UserDetailsGUIEnhanced();
            }
        });
    }

}

