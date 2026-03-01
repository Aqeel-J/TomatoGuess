package com.aqeel.tomatoguess.database;


import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

/**
 * Handles the interaction with the database for managing player preferences.
 * This class provides methods to add, retrieve and update player preferences in
 * the database.
 *
 * *
 * <p>
 * Preferences include information such as IP address, email, music preference,
 * and sound preference.
 * </p>
 *
 * @author Aqeel Jabir
 */
public class PreferencesHandler {
    /* The database connection instance */
    private static final DBConnection db = DBConnection.getInstance();

    /* MongoDB collection for storing player preferences */
    private static final MongoCollection<Document> preferencesCollection = db
            .getDBCollection(DBConnection.PREFERENCE_COLLECTION);

    /**
     * Adds initial player preferences to the database.
     *
     * @param email of the player to whom the preferences will be added.
     * @param ip    address of the player.
     */
    public static void addPreferences(String email, String ip) {

        // Create a document representing player preferences.
        Document prefeDocument = new Document("ipAddress", ip)
                .append("email", email)
                .append("music", true)
                .append("sound", true);

        // Insert the document into preferences collection.
        preferencesCollection.insertOne(prefeDocument);
    }

    /**
     * Retrieves player preferences from the database based on player email and IP
     * address.
     *
     * @param email of the player.
     * @param ip    address of the player.
     * @return document containing player preferences.
     */
    public static Document getPreferences(String email, String ip) {
        try {
            // Create a query document based on player email and IP address.
            Document query = new Document("ipAddress", ip).append("email", email);

            // Find preferences based on the query.
            FindIterable<Document> findIterable = preferencesCollection.find(query);

            // Returns the first document in the result.
            return findIterable.first();

        } catch (Exception e) {

            // Print exception stack trace in case of an error.
            e.printStackTrace();

            // Return null if an exception occurs.
            return null;
        }
    }

    /**
     * Updates player preferences in the database based on player email and IP
     * address
     *
     * @param email  of the player.
     * @param ip     address of the player.
     * @param update document containing the updated player preferences.
     */
    public static void updatePreferences(String email, String ip, Document update) {
        preferencesCollection
                .updateOne(new Document("email", email)
                        .append("ipAddress", ip), update);
    }
}
