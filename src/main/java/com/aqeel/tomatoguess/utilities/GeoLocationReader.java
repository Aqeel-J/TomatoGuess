package com.aqeel.tomatoguess.utilities;


import com.aqeel.tomatoguess.resources.AppStrings;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

/**
 * Utility class for fetching geolocation information based on player's IP
 * address.
 * The class utilizes the IPify API to get the country details.
 * Additionally, it provides methods to extract the country code and full
 * country name.
 *
 * @author Aqeel Jabir
 * @see <a href="https://www.ipify.org/">IPify API</a>
 *
 */
public class GeoLocationReader {
    private static GeoLocationReader instance = null;
    private final String locationDetails;

    /**
     * Singleton pattern to ensure a single instance of GeolocationReader is
     * created.
     *
     * @return singleton instance of GeolocationReader.
     */
    public static GeoLocationReader getInstance() {
        if (instance == null) {
            instance = new GeoLocationReader();
        }
        return instance;
    }

    private final String API_KEY = "at_UTfmne6GEePKTIiQ6VhRFIAyz3jHy";
    private final String API_URL = "https://geo.ipify.org/api/v2/country?";

    /**
     * Private constructor to initialize GeolocationReader.
     * This constructor is made private to enforce the Singleton pattern.
     * It fetches the geolocation details using the IPify API during the instance
     * creation.
     */
    private GeoLocationReader() {
        this.locationDetails = fetchLocationDetails();
    }

    /**
     * Fetches the geolocation details using the Ipify API.
     * Constructs the URL with the API key and the public IP address of the
     * user. Then, it reads the JSON response from the API and returns it as a
     * String.
     *
     * @return a string containing the JSON response from the API.
     */
//    private String fetchLocationDetails() {
//        // Construct the URL for the IPify API using the API key and public IP address.
//        String url = API_URL + "apiKey=" + API_KEY + "&ipAddress=" + IPAddressReader.getPublicIpAddress();
//        try (java.util.Scanner s = new java.util.Scanner(new java.net.URI(url).toURL().openStream())) {
//            // Use Scanner to read the stream and return geoloation details as a string.
//            return s.useDelimiter("\\A").next();
//        } catch (Exception ex) {
//            ex.fillInStackTrace();
//            return null;
//        }
//    }

    private String fetchLocationDetails() {
        String ip = IPAddressReader.getPublicIpAddress(); // your method to get IP
        String urlStr = "http://ip-api.com/json/" + ip;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            try (InputStreamReader reader = new InputStreamReader(conn.getInputStream())) {
                StringBuilder sb = new StringBuilder();
                int c;
                while ((c = reader.read()) != -1) sb.append((char) c);
                return sb.toString();
            }
        } catch (Exception e) {
            e.fillInStackTrace();
            return null; // fallback gracefully
        }
    }

    /**
     * Gets the country code of the player based on geolocation details.
     *
     * @return The country code of the player within 2 characters (e.g. "UK").
     */
//    public String getCountryCode() {
//        try {
//            // Extract the country code from the geolocation details obtained from Ipify
//            // API.
//            JSONObject jsonObject = new JSONObject(locationDetails);
//            JSONObject location = jsonObject.getJSONObject("location");
//            return location.getString("country");
//        } catch (JSONException e) {
//            e.fillInStackTrace();
//            return null;
//        }
//    }


    public String getCountryCode() {
        if (locationDetails == null) return null;
        try {
            JSONObject json = new JSONObject(locationDetails);
            return json.optString("countryCode", null); // ip-api returns countryCode
        } catch (Exception e) {
            e.fillInStackTrace();
            return null;
        }
    }


    /**
     * Gets the country of the player using the country code.
     * Returns the full name of the country by matching the country code with the
     * data from {@link countries.json} file.
     *
     * @return The full name of the player's country (e.g. "United Kingdom").
     */
//    public String getCountry() {
//        try {
//            // Read the JSON file containing the list of countries and their codes.
//            JSONArray countriesArray = new JSONArray(Objects.requireNonNull(JsonFileReader.getJsonFile(AppStrings.COUNTRIES_FILE_PATH)));
//
//            // Iterate through the array to find a match for the player's country code.
//            for (int i = 0; i < countriesArray.length(); i++) {
//                JSONObject countryObject = countriesArray.getJSONObject(i);
//
//                // Check if the country code from the JSON file matches the player's country
//                // code.
//                if (countryObject.getString("code").equalsIgnoreCase(getCountryCode())) {
//                    return countryObject.getString("name");
//                }
//            }
//        } catch (JSONException e) {
//            e.fillInStackTrace();
//        }
//        return null;
//    }

    public String getCountry() {
        // fallback to local JSON if exists
        try {
            JSONArray countriesArray = new JSONArray(
                    Objects.requireNonNull(JsonFileReader.getJsonFile(AppStrings.COUNTRIES_FILE_PATH))
            );
            String code = getCountryCode();
            if (code == null) return null;

            for (int i = 0; i < countriesArray.length(); i++) {
                JSONObject obj = countriesArray.getJSONObject(i);
                if (code.equalsIgnoreCase(obj.getString("code"))) {
                    return obj.getString("name");
                }
            }
        } catch (Exception e) {
            // optional: fallback to country name from API directly
            try {
                JSONObject json = new JSONObject(locationDetails);
                return json.optString("country", null);
            } catch (Exception ignored) {}
        }
        return null;
    }
}
