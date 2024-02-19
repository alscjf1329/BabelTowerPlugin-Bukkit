package org.dev.babeltower.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.dev.babeltower.BabelTower;
import org.dev.babeltower.config.ConfigOptions;

public class MongoDBManager {

    private static MongoDBManager instance;
    private static final String MONGODB_URI_FORMAT = "mongodb://%s:%d";
    private MongoClient client;
    @Getter
    private MongoDatabase RPGSharpDB;

    public static synchronized MongoDBManager getInstance() {
        if (instance == null) {
            instance = new MongoDBManager();
        }
        return instance;
    }

    private MongoDBManager() {
    }

    public void connect() {
        FileConfiguration config = BabelTower.getInstance().getConfig();
        String host = config.getString(ConfigOptions.HOST_OPTION.getName());
        int port = config.getInt(ConfigOptions.PORT_OPTION.getName());
        String MongoDBUri = convertToMongoDBUri(host, port);

        client = MongoClients.create(MongoDBUri);
        String dbName = config.getString(ConfigOptions.DB_OPTION.getName(),
            ConfigOptions.DB_OPTION.getDefaultVal());
        RPGSharpDB = client.getDatabase(dbName);
    }

    public void disconnect() {
        if (client != null) {
            client.close();
        }
    }

    private String convertToMongoDBUri(String host, int port) {
        return String.format(MONGODB_URI_FORMAT, host, port);
    }
}
