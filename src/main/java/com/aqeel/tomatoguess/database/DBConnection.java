package com.aqeel.tomatoguess.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Manages the database connection to the MongoDB database for the Application.
 * This class provides methods to connect to the MongoDB server, access the
 * "tomatoguessdb" database and retrieve collections from the database.
 *
 * <p>
 * The class establishes a connection to the MongoDB server and the
 * "tomatoguessdb" database,
 * and it contains methods for retrieving collections such as player
 * information, bonus information,
 * player preferences, and player scores.
 * </p>
 *
 * @author Aqeel Jabir
 *
 */
public class DBConnection {
    /* Name of the MongoDB database */
    private static final String DB_NAME = "tomatoGuessDB";

    /* The connection string for MongoDB */
    private static final String DB_CONNECTION = "mongodb://localhost:27017";

    /* The collection name for player information */
    public static final String PLAYER_COLLECTION = "players";

    /* The collection name for bonus information */
    public static final String BONUS_COLLECTION = "bonuses";

    /* The collection name for player preferences */
    public static final String PREFERENCE_COLLECTION = "preferences";

    /* The collection name for player scores */
    public static final String SCOREBOARD_COLLECTION = "scoreboard";

    /* Singleton instance to ensure a single shared database connection */
    private static DBConnection instance = null;

    /* MongoDB0 database instance */
    private static MongoDatabase db;

    /**
     * Establishing a connection to the MongoDB server and the "tomatoGuessDB"
     * database. Prints a success message if the connection is successful. Prints an
     * error message and stack trace if there is an issue connecting to the
     * database.
     */
    private DBConnection() {
        try {
            // Building MongoDB client settings using the connection string.
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(DB_CONNECTION))
                    .build();

            // Create a MongoDB client and retrieve the "tomatoGuessDB" database.
            MongoClient mongoClient = MongoClients.create(settings);
            db = mongoClient.getDatabase(DB_NAME);

            // Verify the connection by listing database names.
            mongoClient.listDatabaseNames().first();

            System.out.println("Connected to the database successfully.");

        } catch (MongoException e) {

            // Print and error message and stack trace if there is an issue connecting to the database.
            System.err.println("Error connecting to the database: " + e.getMessage());
            e.fillInStackTrace();
        }
    }

    /**
     * Returns the singleton instance of the {@link DBConnection} class.
     * <p>
     * This ensures that only one connection to the MongoDB database is created and
     * shared throughout the application. If the instance does not exist yet,
     * it will initialize a new {@code DBConnection}.
     * </p>
     *
     * @return the singleton instance of {@code DBConnection}
     */
    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    /**
     * Returns the specified collection from the database.
     *
     * @param collection Name of the collection to be retrieved.
     * @return the specified collection
     */
    public MongoCollection<Document> getDBCollection(String collection) {
        return db.getCollection(collection);
    }
}
