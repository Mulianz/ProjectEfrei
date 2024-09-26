package apigamefinal2;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Comparator;

public class GameScoreManager {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> playerScoresCollection;

    public GameScoreManager() {
    mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("Projet_java");
        playerScoresCollection = database.getCollection("player_scores");
    }
    public boolean playerExists(String player) {
        Document query = new Document("player", player);
        Document playerDoc = playerScoresCollection.find(query).first();
        return playerDoc != null;
    }

    public void recordWin(String player) {
        Document query = new Document("player", player);
        Document playerDoc = playerScoresCollection.find(query).first();
        
        if (playerDoc == null) {
            playerScoresCollection.insertOne(new Document("player", player).append("score", 1));
        } else {
            int currentScore = playerDoc.getInteger("score");
            playerScoresCollection.updateOne(query, new Document("$set", new Document("score", currentScore + 1)));
        }
    }

    public int getPlayerScore(String player) {
        Document query = new Document("player", player);
        Document playerDoc = playerScoresCollection.find(query).first();
        
        if (playerDoc != null) {
            return playerDoc.getInteger("score");
        } else {
            return 0;
        }
    }

    public List<Map.Entry<String, Integer>> getTopPlayers() {
        return playerScoresCollection.find()
            .sort(new Document("score", -1))
            .limit(3)
            .map(doc -> Map.entry(doc.getString("player"), doc.getInteger("score")))
            .into(new java.util.ArrayList<>());
    }

    public void close() {
        mongoClient.close();
    }
}