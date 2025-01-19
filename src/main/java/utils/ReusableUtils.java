package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.spi.ObjectThreadContextMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class ReusableUtils {
    public static Map<String, Object> readData(String jsonFilename, String fieldName, String fieldValue) throws ParseException, IOException {

        String dataFilePath = "C:\\Users\\karan\\IdeaProjects\\TestAssignment\\dataFiles\\";
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = (JSONArray)jsonParser.parse(new FileReader(dataFilePath+ jsonFilename+ ".json"));
        for(int index = 0; index<jsonArray.size();index++){
            JSONObject jsonObject = (JSONObject)jsonArray.get(index);
            if(jsonObject.get(fieldName).equals(fieldValue)){
                JSONObject obj = (JSONObject)jsonArray.get(index);
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(obj.toString(), Map.class);
            }
        }return null;
    }

    /**
     * Method to take screenshot
     * @param fileName
     */
    public static void takeScreenshot(String fileName, WebDriver driver) {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileHandler.copy(screenshot, new File("C:\\Users\\karan\\IdeaProjects\\TestAssignment\\test-output\\" + fileName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
