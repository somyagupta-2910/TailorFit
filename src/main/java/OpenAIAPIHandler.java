import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

public class OpenAIAPIHandler {

    private final String apiKey;
    private final String apiEndpoint = "https://api.openai.com/v1/chat/completions";

    public OpenAIAPIHandler(String apiKey) {
        this.apiKey = apiKey;
    }
    
    public static String joinArrayToString(String[] arr) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }

    public String sendPromptToGPT(User user) {
        String prompt = buildPrompt(user);
        System.out.println("Prompt that is sent: ");
        System.out.println(prompt);
        return sendPostRequest(prompt);
    }

    private String buildPrompt(User user) {
        return "Task and Persona: I want you to act as a personal trainer. I will provide you with all the information needed about a user looking to become fitter, stronger and healthier through physical training, and your role is to devise the best plan for that person depending on their current fitness level, goals and lifestyle habits. You should use your knowledge of exercise science, nutrition advice, and other relevant factors in order to create a personalized fitness plan suitable for them. \n"
                + "\n"
                + "User Info:\n"
                + "Name: " + user.getFirstName() + "\n"
                + "City: " + user.getCity() + "\n"
                + "Gender: " + user.getGender() + "\n"
                + "Country: " + user.getEthnicity() + "\n"
                + "Age: " + user.getAge() + "\n"
                + "Height: " + user.getHeight() + "\n"
                + "Current Weight: " + user.getCurrentWeight() + "\n"
                + "Existing medical conditions or physical limitations: " + user.getMedicalConditions() + "\n"
                + "Diet type: " + user.getDietType() + "\n"
                + "Food allergies: " + user.getFoodAllergies() + "\n"
                + "Diet Elements to be included:"
                + "\n"
                + "Vegetables: " + joinArrayToString(user.getVegetables()) + "\n"
                + "Meat: " + joinArrayToString(user.getMeatAndEggs())+ "\n"
                + "Fruits: " + joinArrayToString(user.getFruits()) + "\n"
                + "Whole Grains: " + joinArrayToString(user.getWholeGrains()) + "\n"
                + "Dairy: " + joinArrayToString(user.getDairy()) + "\n"
                + "Goal: " + user.getGoal() + "\n"
                + "Target Weight: " + user.getTargetWeight() + "\n"
                + "\n"
                + "Output format:\n"
                + "Current BMI Analysis\n"
                + "\n"
                + "Ideal BMI and Weight Target\n"
                + "\n"
                + "Number of Calories needed to burn to achieve the target weight\n"
                + "\n"
                + "Number of Calories to consume everyday to achieve the target weight\n"
                + "\n"
                + "7-day Extensive Workout plan \n"
                + "- Give a detailed workout plan based on the given goal and individual's characteristics. Be mindful of the person's health conditions.\n"
                + "\n"
                + "7-day 3 meals plan \n"
                + "- Give a detailed and a healthy meal plan based on the individual's goal and characteristics\n"
                + "- give a macros breakdown (Protein, Carbs, and Fat) and calories count of each meal.\n"
                + "- The diet plan should be based on the person's country of birth and where the person currently lives.\n"
                + "- Also, make sure to check on the person's existing medical conditions and food allergies and give meal plan recommendations accordingly.\n"
                + "\n"
                + "NOTE: Do not provide any other message than the ones asked in the Output format";
    }

    private String sendPostRequest(String prompt) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost(apiEndpoint);

        try {
            postRequest.setHeader("Authorization", "Bearer " + apiKey);
            postRequest.setHeader("Content-Type", "application/json");

            JSONObject message = new JSONObject();
            message.put("role", "user");
            message.put("content", prompt);

            JSONArray messagesArray = new JSONArray();
            messagesArray.put(message);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("model", "gpt-3.5-turbo");
            jsonBody.put("messages", messagesArray);
            jsonBody.put("temperature", 1);
            jsonBody.put("max_tokens", 3500);
            jsonBody.put("top_p", 1);
            jsonBody.put("frequency_penalty", 0);
            jsonBody.put("presence_penalty", 0);

            StringEntity stringEntity = new StringEntity(jsonBody.toString());
            postRequest.setEntity(stringEntity);

            HttpResponse response = httpClient.execute(postRequest);
            String responseString = EntityUtils.toString(response.getEntity());

            JSONObject jsonResponse = new JSONObject(responseString);

            if (jsonResponse.has("choices")) {
                JSONArray choices = jsonResponse.getJSONArray("choices");
                if (choices.length() > 0) {
                    JSONObject firstChoice = choices.getJSONObject(0);
                    JSONObject messageObject = firstChoice.getJSONObject("message");
                    return messageObject.getString("content");
                } else {
                    return "Error: No choices in response";
                }
            } else {
                System.out.println("No 'choices' found in response. Response: " + responseString);
                return "Error: No 'choices' key found in API response";
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "Error: Unable to connect to OpenAI API";
        } catch (JSONException e) {
            e.printStackTrace();
            return "Error: Issue in parsing the API response";
        }
    }
}


//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//import org.json.JSONObject;
//import org.json.JSONArray;
//import org.json.JSONException;
//
//import java.io.IOException;
//
//public class OpenAIAPIHandler {
//
//	private final String apiKey;
//    private final String apiEndpoint = "https://api.openai.com/v1/chat/completions";
//
//    public OpenAIAPIHandler(String apiKey) {
//        this.apiKey = apiKey;
//    }
//
//    public String sendPromptToGPT(String name, String email, String city, String gender, String country, int age, 
//                                  int height, int currentWeight, String medicalConditions, String dietType, 
//                                  String foodAllergies, String vegetables, String ingredients, String meat, 
//                                  String fruits, String goal, int targetWeight) {
//    		// The prompt to get the Workout and Meal Plan
//        String prompt = buildPrompt(name, city, gender, country, age, height, currentWeight, medicalConditions, 
//                                    dietType, foodAllergies, vegetables, ingredients, meat, fruits, goal, targetWeight);
//        
//        // Receive prompt from GPT
//        String response = sendPostRequest(prompt);
//
//        return response;
//    }
//
//    private String buildPrompt(String name, String city, String gender, String country, int age, 
//                               int height, int currentWeight, String medicalConditions, String dietType, 
//                               String foodAllergies, String vegetables, String ingredients, String meat, 
//                               String fruits, String goal, int targetWeight) {
//        // Build the complex prompt as per the provided structure.
//        return "Task and Persona: I want you to act as a personal trainer. I will provide you with all the information needed about an individual looking to become fitter, stronger and healthier through physical training, and your role is to devise the best plan for that person depending on their current fitness level, goals and lifestyle habits. You should use your knowledge of exercise science, nutrition advice, and other relevant factors in order to create a personalized fitness plan suitable for them. \n"
//        		+ "\n"
//        		+ "Information of the Individual:\n"
//        		+ "Name: " + name + "\n"
//        		+ "City: " + city + "\n"
//        		+ "Gender: " + gender + "\n"
//        		+ "Country: " + country + "\n"
//        		+ "Age: " + age + "\n"
//        		+ "Height: " + height + "\n"
//        		+ "Current Weight: " + currentWeight + "\n"
//        		+ "Existing medical conditions or physical limitations: " + medicalConditions + "\n"
//        		+ "Diet type: " + dietType + "\n"
//        		+ "Food allergies: " + foodAllergies + "\n"
//        		+ "Diet Elements to be included:\n"
//        		+ "Vegetables: " + vegetables + "n"
//        		+ "Ingredients: " + ingredients + "\n"
//        		+ "Meat: " + meat + "\n"
//        		+ "Fruits: " + fruits + "\n"
//        		+ "\n"
//        		+ "Goal: " + goal + "\n"
//        		+ "Target Weight: " + targetWeight + "\n"
//        		+ "\n"
//        		+ "Output format:\n"
//        		+ "Current BMI Analysis\n"
//        		+ "\n"
//        		+ "Ideal BMI and Weight Target\n"
//        		+ "\n"
//        		+ "Number of Calories needed to burn to achieve the target weight\n"
//        		+ "\n"
//        		+ "Number of Calories to consume everyday to achieve the target weight\n"
//        		+ "\n"
//        		+ "7-day Extensive Workout plan \n"
//        		+ "- Give a detailed workout plan based on the given goal and individual's characteristics. Be mindful of the person's health conditions (if any).\n"
//        		+ "\n"
//        		+ "7-day 3 meals plan \n"
//        		+ "- Give a detailed and a healthy meal plan based on the individual's goal and characteristics\n"
//        		+ "- give a macros breakdown (Protein, Carbs, and Fat) and calories count of each meal.\n"
//        		+ "- The diet plan should be based on the person's country of birth and where the person currently lives.\n"
//        		+ "- Also, make sure to check on the person's existing medical conditions and food allergies and give meal plan recommendations accordingly.\n"
//        		+ "- Give a meal plan for 7 days. DO NOT STOP at DAY 1\n"
//        		+ "\n"
//        		+ "Additional Tips:\n"
//        		+ "Hydration:\n"
//        		+ "Sleep: \n"
//        		+ "Stress Management:\n"
//        		+ "Regular Check-ins:\n"
//        		+ "\n"
//        		+ "NOTE: Do not provide any other message than the ones asked in the Output format";
//        // Make sure to replace the placeholders in the prompt with the variables.
//    }
//
//    private String sendPostRequest(String prompt) {
//        HttpClient httpClient = HttpClients.createDefault();
//        HttpPost postRequest = new HttpPost(apiEndpoint);
//
//        try {
//            postRequest.setHeader("Authorization", "Bearer " + apiKey);
//            postRequest.setHeader("Content-Type", "application/json");
//
//            JSONObject message = new JSONObject();
//            message.put("role", "user");
//            message.put("content", prompt);
//
//            JSONArray messagesArray = new JSONArray();
//            messagesArray.put(message);
//
//            JSONObject jsonBody = new JSONObject();
//            jsonBody.put("model", "gpt-3.5-turbo-1106"); //gpt-4-1106-preview gpt-3.5-turbo-1106
//            jsonBody.put("messages", messagesArray);
//            jsonBody.put("temperature", 1);
//            jsonBody.put("max_tokens", 4096);
//            jsonBody.put("top_p", 1);
//            jsonBody.put("frequency_penalty", 0);
//            jsonBody.put("presence_penalty", 0);
//
//            StringEntity stringEntity = new StringEntity(jsonBody.toString());
//            postRequest.setEntity(stringEntity);
//
//            HttpResponse response = httpClient.execute(postRequest);
//            String responseString = EntityUtils.toString(response.getEntity());
//
//            JSONObject jsonResponse = new JSONObject(responseString);
//
//            // Extract the 'content' from the 'message' object within the first element of the 'choices' array.
//            if (jsonResponse.has("choices")) {
//                JSONArray choices = jsonResponse.getJSONArray("choices");
//                if (choices.length() > 0) {
//                    JSONObject firstChoice = choices.getJSONObject(0);
//                    JSONObject messageObject = firstChoice.getJSONObject("message");
//                    return messageObject.getString("content");
//                } else {
//                    return "Error: No choices in response";
//                }
//            } else {
//                System.out.println("No 'choices' found in response. Response: " + responseString);
//                return "Error: No 'choices' key found in API response";
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "Error: Unable to connect to OpenAI API";
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return "Error: Issue in parsing the API response";
//        }
//    }
//
//    public static void main(String[] args) {
//        OpenAIAPIHandler apiHandler = new OpenAIAPIHandler("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"); // Replace with your actual API key
//
//        String response = apiHandler.sendPromptToGPT("Somya Gupta", "somyag00@gmail.com", "New York", "Male", "India", 24, 170, 100, "None", "Omnivore", "Dairy", "Potato, Onion, Tomato, Carrot", "Lentils", "Chicken", "Apple, Banana", "Lose Weight", 70);
//        System.out.println(response);
//    }
//}
