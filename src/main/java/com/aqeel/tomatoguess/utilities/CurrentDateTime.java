package com.aqeel.tomatoguess.utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class provides a method to retrieve the current date and time formatted
 * according to the specified pattern.
 *
 * @author Aqeel Jabir
 */

public class CurrentDateTime {

    /**
     * Gets the current date and time as a formatted string.
     *
     * @param pattern to format the date and time.
     * @return a string representing the current date and time.
     */
    public static String getDateTime(String pattern) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return currentDateTime.format(formatter);
    }
}
