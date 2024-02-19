package org.dev.babeltower;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.plugin.java.JavaPlugin;
import org.dev.babeltower.config.ConfigOptions;
import org.dev.babeltower.database.MongoDBManager;

public final class BabelTower extends JavaPlugin {

    @Getter
    private static JavaPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        // resources/config.yml 형식으로 config file 만들어지게 설정
        saveResource("config.yml", false);
        saveConfig();
        MongoDBManager.getInstance().connect();

    }

    @Override
    public void onDisable() {
        MongoDBManager.getInstance().disconnect();
    }
}
