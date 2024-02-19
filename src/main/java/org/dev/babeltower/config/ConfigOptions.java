package org.dev.babeltower.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ConfigOptions {
    HOST_OPTION("MongoDB.host", "localhost"),
    PORT_OPTION("MongoDB.port", "27017"),
    DB_OPTION("MongoDB.db","RPGSharp"),
    PLAYER_COLLECTION_OPTION("MongoDB.collections.player", "PlayerTower"),
    TOWER_COLLECTION_OPTION("MongoDB.collections.tower", "Tower");
    private final String name;
    private final String defaultVal;
}
