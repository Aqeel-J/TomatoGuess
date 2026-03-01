package com.aqeel.tomatoguess.database;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

/**
 * Handles the interaction with the database for managing player bonuses.
 * This class provides methods to add, retrieve and update bonus information in
 * the database for a given player.
 *
 * @author Aqeel Jabir
 */
public class BonusesHandler {
    /* The database connection instance */
    private static final DBConnection db = DBConnection.getInstance();

    /* MongoDB collection for storing bonus information */
    private static final MongoCollection<Document> bonusCollection = db.getDBCollection(DBConnection.BONUS_COLLECTION);

    /**
     * Adds initial bonus information for a player to the database.
     *
     * @param email of the player to whom the bonus will be added.
     */
    public static void addBonuses(String email) {
        // Create a document representing initial bonus information.
        Document bonusDocument = new Document("email", email)
                .append("life", 0)
                .append("skips", 0)
                .append("hints", 0)
                .append("time", 0);

        // Insert the document into bonuses collection.
        bonusCollection.insertOne(bonusDocument);
    }

    /**
     * Retrieve bonus information for a player from the database.
     *
     * @param email of the player for whom bonuses will be retrieved.
     * @return document containing the bonus information of the player.
     */
    public static Document getBonuses(String email) {
        return bonusCollection.find(new Document("email", email)).first();
    }

    /**
     * Updates bonus information for a player in the database.
     *
     * @param email  of the player to whom the bonus will be updated.
     * @param update document containing the updated bonus information.
     */
    public static void updateBonuses(String email, Document update) {
        bonusCollection.updateOne(new Document("email", email), update);
    }
}
