package com.aqeel.tomatoguess.database;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Handles the interaction with the database for managing scoreboard
 * information.
 * This class provides methods to add, retrieve, and filter scoreboard entries
 * in the database.
 *
 * <p>
 * The scoreboard entries include player scores, total stars, and playtime.
 * </p>
 *
 * @author Aqeel Jabir
 */

public class ScoreboardHandler {

    /* The database connection instance */
    private static final DBConnection db = DBConnection.getInstance();

    /* MongoDB collection for storing scoreboard information */
    private static final  MongoCollection<Document> scoreboardCollection = db
            .getDBCollection(DBConnection.SCOREBOARD_COLLECTION);

    /**
     * Adds a new scoreboard entry to the database.
     *
     * @param document containing player score.
     */
    public static void addScore(Document document) {
        scoreboardCollection.insertOne(document);
    }

    /**
     * Retrieves the global top score from the database.
     *
     * @return lift of Documents containing the global top scores.
     */
    public static List<Document> getGlobalScores() {

        // Initialize the result list to store top-scoring documents.
        List<Document> resultList = new ArrayList<>();
        try {
            // Perform Aggregation to group entries by email.
            AggregateIterable<Document> result = scoreboardCollection.aggregate(List.of(
                    new Document("$group", new Document("_id", "$email"))));

            // Iterate through the group entries.
            for (Document document : result) {
                // Find top scoring entry for each email.
                FindIterable<Document> res = scoreboardCollection
                        .find(new Document("email", document.getString("_id")))
                        .sort(Sorts.orderBy(
                                Sorts.descending("score", "totalStars"),
                                Sorts.ascending("totalPlayTime")))
                        .limit(1);

                // Get the iterator for the results.
                MongoCursor<Document> cursor = res.iterator();
                // Check if there is a top scoring entry.
                if (cursor.hasNext()) {
                    // Add the top scoring entry to the result list.
                    Document doc = cursor.next();
                    resultList.add(doc);
                }
            }
            // Return the list of top scoring documents.
            return resultList;

        } catch (Exception e) {

            // Print the exception stack trace in case of an error.
            e.printStackTrace();

            // Return null if an exception occurs.
            return null;
        }
    }

    /**
     * Retrieves the local top scores filtered by country from the database.
     *
     * @param country for which the local scores are retrieved.
     * @return list of documents containing the local top scores.
     */
    public static List<Document> getLocalScores(String country) {

        // Initialize the result list to store top scoring documents.
        List<Document> resultList = new ArrayList<>();
        try {
            // Perform aggregation to match by player country and group entries by email.
            AggregateIterable<Document> result = scoreboardCollection.aggregate(Arrays.asList(
                    new Document("$match", new Document("country", country)),
                    new Document("$group", new Document("_id", "$email"))));

            // Iterate through group entries.
            for (Document document : result) {

                // Find top scoring entry for each email.
                FindIterable<Document> res = scoreboardCollection
                        .find(new Document("email", document.getString("_id")))
                        .sort(Sorts.orderBy(
                                Sorts.descending("score", "totalStars"),
                                Sorts.ascending("totalPlayTime")))
                        .limit(1);

                // Get the iterator for the results.
                MongoCursor<Document> cursor = res.iterator();

                // Check if there is top scoring entry.
                if (cursor.hasNext()) {

                    // Add top scoring entry to the result list.
                    Document doc = cursor.next();
                    resultList.add(doc);
                }
            }

            // Return the list of top scoring documents.
            return resultList;

        } catch (Exception e) {

            // Print the exception stack trace in case of an error.
            e.printStackTrace();

            // Return null if an exception occurs.
            return null;
        }
    }

    /**
     * Retrieves the personal scores for a player from the database.
     *
     * @param playerEmail The email of the player.
     * @return MongoCursor document containing personal scores.
     */
    public static MongoCursor<Document> getPersonalScores(String playerEmail) {

        // Create filter based on player's email.
        Document filter = new Document("email", playerEmail);

        // Find and sort results by score and total play time.
        FindIterable<Document> result = scoreboardCollection.find(filter)
                .sort(Sorts.orderBy(
                        Sorts.descending("score", "totalStars"),
                        Sorts.ascending("totalPlayTime")));

        // Return the iterator for the results.
        return result.iterator();
    }
}
