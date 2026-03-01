package com.aqeel.tomatoguess.utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Utility class for reading JSON files.
 *
 * @author Aqeel Jabir
 */
public class JsonFileReader {

    /**
     * Reads the content of a JSON file and returns it as a String.
     *
     * @param filePath to the JSON file.
     * @return the content of the JSON file as a String.
     */
    public static String getJsonFile(String filePath) {
        StringBuilder content;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
