package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONObject;
import io.qameta.allure.Step;
import io.qameta.allure.Allure;

/**
 * Helper class to interact with the OrangeHRM API
 */
public class ApiHelper {
    private static final String API_URL = "https://opensource-demo.orangehrmlive.com/web/index.php/api/v2/pim/employees?nameOrId=";
    private static final Random random = new Random();
    
    // Fallback list of known employees in case API fails
    private static final List<String> FALLBACK_EMPLOYEES = List.of(
        "Charlie Carter",
        "Odis Adalwin",
        "Peter Mac Anderson",
        "Linda Jane Anderson",
        "Cassidy Hope",
        "Kevin Mathews",
        "Anthony Nolan",
        "Fiona Grace",
        "Dominic Chase",
        "Paul Collings",
        "Lisa Andrews",
        "Cecil Bonaparte",
        "Aaliyah Haq",
        "Rebecca Harmony",
        "Joe Root"
    );
    
    /**
     * Fetches employee names from the OrangeHRM API
     * @param searchTerm The search term to use (can be a single letter like 'a', 'b', etc.)
     * @return A list of employee names
     */
    @Step("Fetching employee names from API with search term: {0}")
    public static List<String> fetchEmployeeNames(String searchTerm) {
        List<String> employeeNames = new ArrayList<>();
        
        try {
            URL url = new URL(API_URL + searchTerm);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            // Set request properties
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
            connection.setRequestProperty("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("DNT", "1");
            connection.setRequestProperty("Referer", "https://opensource-demo.orangehrmlive.com/web/index.php/admin/saveSystemUser");
            connection.setRequestProperty("Sec-Fetch-Dest", "empty");
            connection.setRequestProperty("Sec-Fetch-Mode", "cors");
            connection.setRequestProperty("Sec-Fetch-Site", "same-origin");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Safari/537.36");
            connection.setRequestProperty("sec-ch-ua", "\"Chromium\";v=\"133\", \"Not(A:Brand\";v=\"99\"");
            connection.setRequestProperty("sec-ch-ua-mobile", "?0");
            connection.setRequestProperty("sec-ch-ua-platform", "\"macOS\"");
            
            // Add cookie for authentication (this is a demo site, so using a fixed cookie)
            connection.setRequestProperty("Cookie", "orangehrm=k11d8qi45baciiok2ngh9slvsk");
            
            // Get response
            int responseCode = connection.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                // Log the response for debugging
                String responseStr = response.toString();
                Allure.addAttachment("API Response for " + searchTerm, responseStr);
                
                // Parse JSON response
                JSONObject jsonResponse = new JSONObject(responseStr);
                JSONArray data = jsonResponse.getJSONArray("data");
                
                for (int i = 0; i < data.length(); i++) {
                    JSONObject employee = data.getJSONObject(i);
                    
                    // Handle different response formats - check if firstName is a string or object
                    String firstName = "";
                    String lastName = "";
                    String middleName = "";
                    
                    try {
                        // Try to get firstName as a string (new format)
                        if (employee.has("firstName") && !employee.isNull("firstName")) {
                            if (employee.get("firstName") instanceof String) {
                                firstName = employee.getString("firstName");
                            } else if (employee.get("firstName") instanceof JSONObject) {
                                firstName = employee.getJSONObject("firstName").getString("name");
                            }
                        }
                        
                        // Try to get lastName as a string (new format)
                        if (employee.has("lastName") && !employee.isNull("lastName")) {
                            if (employee.get("lastName") instanceof String) {
                                lastName = employee.getString("lastName");
                            } else if (employee.get("lastName") instanceof JSONObject) {
                                lastName = employee.getJSONObject("lastName").getString("name");
                            }
                        }
                        
                        // Try to get middleName as a string (new format)
                        if (employee.has("middleName") && !employee.isNull("middleName")) {
                            if (employee.get("middleName") instanceof String) {
                                middleName = employee.getString("middleName");
                            } else if (employee.get("middleName") instanceof JSONObject) {
                                middleName = employee.getJSONObject("middleName").getString("name");
                            }
                        }
                        
                        String fullName = middleName.isEmpty() ? 
                            firstName + " " + lastName : 
                            firstName + " " + middleName + " " + lastName;
                        
                        employeeNames.add(fullName);
                    } catch (Exception e) {
                        Allure.addAttachment("Employee Parsing Error", 
                            "Error parsing employee data: " + e.getMessage() + "\n" +
                            "Employee JSON: " + employee.toString());
                    }
                }
            } else {
                Allure.addAttachment("API Error", "Response code: " + responseCode);
            }
        } catch (Exception e) {
            Allure.addAttachment("API Exception", e.getMessage());
            System.err.println("Error fetching employee names: " + e.getMessage());
        }
        
        // If API call failed, return fallback list
        if (employeeNames.isEmpty()) {
            Allure.addAttachment("API Fallback", "Using fallback employee list due to API failure");
            return new ArrayList<>(FALLBACK_EMPLOYEES);
        }
        
        return employeeNames;
    }
    
    /**
     * Gets a random employee name from the API
     * @return A random employee name
     */
    @Step("Getting random employee name from API")
    public static String getRandomEmployeeName() {
        // Use different search letters to get a variety of employees
        String[] searchLetters = {"a", "b", "c", "d", "e", "j", "k", "m", "n", "o", "p", "r", "s", "t", "y"};
        String searchTerm = searchLetters[random.nextInt(searchLetters.length)];
        
        List<String> employees = fetchEmployeeNames(searchTerm);
        
        if (employees.isEmpty()) {
            // This shouldn't happen now with the fallback list, but just in case
            return "Charlie Carter";
        }
        
        return employees.get(random.nextInt(employees.size()));
    }
} 