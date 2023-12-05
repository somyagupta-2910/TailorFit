import java.util.Arrays;
import java.io.Serializable;

public class User implements Serializable{
    private String firstName;
    private String email;
    private String city;
    private String gender;
    private String ethnicity;
    private int age;
    private double height;
    private double currentWeight;
    private String medicalConditions;
    private String dietType;
    private String foodAllergies;
    private String goal;
    private double targetWeight;
    private String[] vegetables;
    private String[] fruits;
    private String[] dairy;
    private String[] meatAndEggs;
    private String[] wholeGrains;

    // Constructor
    public User(String firstName, String email, String city, String gender,
            String ethnicity, int age, double height, double currentWeight,
            String medicalConditions, String dietType,
            String foodAllergies, String goal, double targetWeight,
            String[] vegetables, String[] fruits, String[] dairy,
            String[] meatAndEggs, String[] wholeGrains) {
        this.firstName = firstName;
        this.email = email;
        this.city = city;
        this.gender = gender;
        this.ethnicity = ethnicity;
        this.age = age;
        this.height = height;
        this.currentWeight = currentWeight;
        this.medicalConditions = medicalConditions;
        this.dietType = dietType;
        this.foodAllergies = foodAllergies;
        this.goal = goal;
        this.targetWeight = targetWeight;
        this.vegetables = vegetables;
        this.fruits = fruits;
        this.dairy = dairy;
        this.meatAndEggs = meatAndEggs;
        this.wholeGrains = wholeGrains;
    }

    // Getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }

    public String getMedicalConditions() {
        return medicalConditions;
    }

    public void setMedicalConditions(String medicalConditions) {
        this.medicalConditions = medicalConditions;
    }

    public String getDietType() {
        return dietType;
    }

    public void setDietType(String dietType) {
        this.dietType = dietType;
    }

    public String getFoodAllergies() {
        return foodAllergies;
    }

    public void setFoodAllergies(String foodAllergies) {
        this.foodAllergies = foodAllergies;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public double getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(double targetWeight) {
        this.targetWeight = targetWeight;
    }
    
    public String[] getVegetables() {
        return vegetables;
    }

    public void setVegetables(String[] vegetables) {
        this.vegetables = vegetables;
    }

    public String[] getFruits() {
        return fruits;
    }

    public void setFruits(String[] fruits) {
        this.fruits = fruits;
    }

    public String[] getDairy() {
        return dairy;
    }

    public void setDairy(String[] dairy) {
        this.dairy = dairy;
    }

    public String[] getMeatAndEggs() {
        return meatAndEggs;
    }

    public void setMeatAndEggs(String[] meatAndEggs) {
        this.meatAndEggs = meatAndEggs;
    }

    public String[] getWholeGrains() {
        return wholeGrains;
    }

    public void setWholeGrains(String[] wholeGrains) {
        this.wholeGrains = wholeGrains;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                ", gender='" + gender + '\'' +
                ", ethnicity='" + ethnicity + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", currentWeight=" + currentWeight +
                ", medicalConditions='" + medicalConditions + '\'' +
                ", dietType='" + dietType + '\'' +
                ", foodAllergies='" + foodAllergies + '\'' +
                ", goal='" + goal + '\'' +
                ", targetWeight=" + targetWeight +
                ", vegetables=" + Arrays.toString(vegetables) +
                ", fruits=" + Arrays.toString(fruits) +
                ", dairy=" + Arrays.toString(dairy) +
                ", meatAndEggs=" + Arrays.toString(meatAndEggs) +
                ", wholeGrains=" + Arrays.toString(wholeGrains) +
                '}';
    }

}
