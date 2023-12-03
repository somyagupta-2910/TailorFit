public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String city;
    private String gender;
    private String ethnicity;
    private int age;
    private double height;
    private double currentWeight;
    private String medicalConditions;
    private String foodOptions;
    private String dietType;
    private String foodAllergies;
    private String goal;
    private double targetWeight;

    // Constructors, getters, setters, and other methods can be added as needed

    // Constructor
    public User(String firstName, String lastName, String email, String city, String gender,
                String ethnicity, int age, double height, double currentWeight,
                String medicalConditions, String foodOptions, String dietType,
                String foodAllergies, String goal, double targetWeight) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.city = city;
        this.gender = gender;
        this.ethnicity = ethnicity;
        this.age = age;
        this.height = height;
        this.currentWeight = currentWeight;
        this.medicalConditions = medicalConditions;
        this.foodOptions = foodOptions;
        this.dietType = dietType;
        this.foodAllergies = foodAllergies;
        this.goal = goal;
        this.targetWeight = targetWeight;
    }

    // Getters and setters for each field

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Repeat the pattern for other fields...

    // toString method for easy debugging and logging
    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                ", gender='" + gender + '\'' +
                ", ethnicity='" + ethnicity + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", currentWeight=" + currentWeight +
                ", medicalConditions='" + medicalConditions + '\'' +
                ", foodOptions='" + foodOptions + '\'' +
                ", dietType='" + dietType + '\'' +
                ", foodAllergies='" + foodAllergies + '\'' +
                ", goal='" + goal + '\'' +
                ", targetWeight=" + targetWeight +
                '}';
    }
}
