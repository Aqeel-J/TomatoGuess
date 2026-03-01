package com.aqeel.tomatoguess.database;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

/**
 * Handles the interaction with the database for managing player information.
 * This class provides methods to add, retrieve and update player information in
 * the database.
 *
 * @author Aqeel Jabir
 */
public class PlayerHandler {
    /* The database connection instance */
    private static final DBConnection db = DBConnection.getInstance();

    /* MongoDB collection for storing player information */
    private static final MongoCollection<Document> playerCollection = db.getDBCollection(DBConnection.PLAYER_COLLECTION);

    /**
     * Adds a new player to the database.
     *
     * @param playerDocument Passing player information as a MongoDB document.
     */
    public static void addPlayer(Document playerDocument) {
        playerCollection.insertOne(playerDocument);
    }

    /**
     * Retrieves player information from the database based on player email.
     *
     * @param email of the player to be retrieved.
     * @return document containing the player information.
     */
    public static Document getPlayer(String email) {
        return playerCollection.find(new Document("email", email)).first();

    }

    /**
     * Updates player information in the database based on player email.
     *
     * @param email  of the player to be updated.
     * @param update document contaning the updated player information.
     */
    public static void updatePlayer(String email, Document update) {
        playerCollection.updateOne(new Document("email", email), update);
    }
}
