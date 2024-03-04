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
    private static final String MONGODB_URI_FORMAT = "mongodb://%s:%s@%s:%d";
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
        String user = config.getString(ConfigOptions.DB_USERNAME.getName(),
            (String) ConfigOptions.DB_USERNAME.getDefaultVal());
        String pw = config.getString(ConfigOptions.DB_PW.getName(),
            (String) ConfigOptions.DB_PW.getDefaultVal());
        String MongoDBUri = convertToMongoDBUri(user, pw, host, port);
        client = MongoClients.create(MongoDBUri);

        String dbName = config.getString(ConfigOptions.DB_OPTION.getName(),
            (String) ConfigOptions.DB_OPTION.getDefaultVal());
        RPGSharpDB = client.getDatabase(dbName);
    }

    public void disconnect() {
        if (client != null) {
            client.close();
        }
    }

    private String convertToMongoDBUri(String user, String pw, String host, int port) {
        return String.format(MONGODB_URI_FORMAT, user, pw, host, port);
    }
}
