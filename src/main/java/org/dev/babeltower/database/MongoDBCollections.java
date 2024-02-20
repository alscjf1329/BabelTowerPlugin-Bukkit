package org.dev.babeltower.database;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongoDBCollections {
    TOWER("Tower"),
    ROOM("TowerRoom"),
    PLAYER("PlayerTower");
    private final String name;
}
