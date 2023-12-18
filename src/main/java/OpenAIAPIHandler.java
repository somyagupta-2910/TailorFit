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
    
    // Helper method to join an array of strings into a comma-separated string.
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

    // Method to send a prompt to the OpenAI GPT-3 API and retrieve a response.
    public String sendPromptToGPT(User user) {
        String prompt = buildPrompt(user);
        System.out.println("Prompt that is sent: ");
        System.out.println(prompt);
        return sendPostRequest(prompt);
    }

    // Build a prompt string using user information and specific instructions.
    private String buildPrompt(User user) {
        return "Task and Persona: I want you to act as a personal trainer. I will provide you with all the information needed about a user looking to become fitter, stronger and healthier through physical training, and your task is to devise a comprehensive fitness plan that includes a 7-day extensive workout plan and a 7-day meal plan, tailored to the User's goals, characteristics, and dietary restrictions. You should use your knowledge of exercise science, nutrition advice, and other relevant factors in order to create a personalized fitness plan suitable for them.\n"
                + "\n"
                + "User Info:\n"
                + "Name: " + user.getFirstName() + "\n"
                + "City: " + user.getCity() + "\n"
                + "Gender: " + user.getGender() + "\n"
                + "Country: " + user.getEthnicity() + "\n"
                + "Age: " + user.getAge() + "\n"
                + "Height: " + user.getHeight() + " cm" + "\n"
                + "Current Weight: " + user.getCurrentWeight() + " lb" + "\n"
                + "Existing medical conditions or physical limitations: " + user.getMedicalConditions() + "\n"
                + "Diet type: " + user.getDietType() + "\n"
                + "Food allergies: " + user.getFoodAllergies() + "\n"
                + "Diet Elements to be included:\n"
                + "Vegetables: " + joinArrayToString(user.getVegetables()) + "\n"
                + "Meat: " + joinArrayToString(user.getMeatAndEggs())+ "\n"
                + "Fruits: " + joinArrayToString(user.getFruits()) + "\n"
                + "Whole Grains: " + joinArrayToString(user.getWholeGrains()) + "\n"
                + "Dairy: " + joinArrayToString(user.getDairy()) + "\n"
                + "Goal: " + user.getGoal() + "\n"
                + "Target Weight: " + user.getTargetWeight() + " lb" + "\n"
                + "\n"
                + "Output format:\n"
                + "Current BMI: [Provide the current BMI without showing any calculations]\n"
                + "Ideal BMI based on Target Weight: [Provide the ideal BMI based on the target weight without showing any calculations]\n"
                + "Number of Calories to eat everyday to achieve the target weight: [Show the number of calories]\n"
                + "\n"
                + "7-day Extensive Workout plan:\n"
                + "[Provide a detailed workout plan for each day, considering the User's goal, current fitness level, and any health conditions]\n"
                + "\n"
                + "7-day Meal plan: \n"
                + "[Provide a detailed and healthy meal plan for each day, considering the User's goal, characteristics, dietary preferences, and restrictions]\n"
                + "- Include a macro breakdown (Protein, Carbs, and Fat) and calorie count for each meal.\n"
                + "- Tailor the meal plan based on the User's country of birth and current residence.\n"
                + "- Take into account the User's existing medical conditions and food allergies when recommending meal plan options.\n"
                + "NOTE: Do not provide any other message than the ones asked in the Output format. Do not send this prompt itself as a response.";
    }

    // Send a POST request to the OpenAI API and retrieve the response.
    private String sendPostRequest(String prompt) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost(apiEndpoint);

        try {
            postRequest.setHeader("Authorization", "Bearer " + apiKey);
            postRequest.setHeader("Content-Type", "application/json");

            // Create a JSON object to hold the message content.
            JSONObject message = new JSONObject();
            message.put("role", "user");
            message.put("content", prompt);

            // Create a JSON array to hold the message object.
            JSONArray messagesArray = new JSONArray();
            messagesArray.put(message);

            // Create the main JSON body with necessary parameters.
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("model", "gpt-3.5-turbo");
            jsonBody.put("messages", messagesArray);
            jsonBody.put("temperature", 1);
            jsonBody.put("max_tokens", 3500);
            jsonBody.put("top_p", 1);
            jsonBody.put("frequency_penalty", 0);
            jsonBody.put("presence_penalty", 0);

            // Create a string entity for the JSON body.
            StringEntity stringEntity = new StringEntity(jsonBody.toString());
            postRequest.setEntity(stringEntity);

            // Execute the HTTP POST request.
            HttpResponse response = httpClient.execute(postRequest);
            String responseString = EntityUtils.toString(response.getEntity());

            // Parse the JSON response to extract the GPT-3 generated content.
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
