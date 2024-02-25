package org.dev.babeltower.config;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ConfigOptions {
    HOST_OPTION("MongoDB.host", "localhost"),
    PORT_OPTION("MongoDB.port", "27017"),
    DB_OPTION("MongoDB.db", "RPGSharp"),
    PLAYER_COLLECTION_OPTION("MongoDB.collections.player", "PlayerTower"),
    TOWER_COLLECTION_OPTION("MongoDB.collections.tower", "Tower"),
    WORLD_NAME_OPTION("world", "babel"),
    RETURN_COORDINATE("returnLocation.coordinate", List.of(0, 0, 0)),
    RETURN_WORLD("returnLocation.coordinate", "world");

    private final String name;
    private final Object defaultVal;
}
