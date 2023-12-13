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
                + "Height: " + user.getHeight() + "cm" + "\n"
                + "Current Weight: " + user.getCurrentWeight() + "lb" + "\n"
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
                + "Target Weight: " + user.getTargetWeight() + "cm" + "\n"
                + "\n"
                + "Output format:\n"
                + "Current BMI\n"
                + "Ideal BMI based on Target Weight\n"
                + "Number of Calories needed to be burnt to achieve the target weight\n"
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