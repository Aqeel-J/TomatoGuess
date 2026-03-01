package com.aqeel.tomatoguess.utilities;

/**
 * Utility class for converting time in milliseconds to a formatted string
 * (HH : mm : ss).
 */
public class TimeConvertor {
    private static long hours;
    private static long minutes;
    private static long seconds;

    /**
     * Converts time in milliseconds to a formatted string.
     *
     * @param milliSeconds to be converted.
     * @return the formatted time string.
     */
    public static String getTime(long milliSeconds) {
        hours = milliSeconds / (1000 * 60 * 60);
        minutes = (milliSeconds / (1000 * 60)) % 60;
        seconds = (milliSeconds / 1000) % 60;
        return String.format("%02d", hours) + " : "
                + String.format("%02d", minutes) + " : "
                + String.format("%02d", seconds);
    }
}
