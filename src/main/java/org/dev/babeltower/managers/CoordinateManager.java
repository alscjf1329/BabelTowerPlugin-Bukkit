package org.dev.babeltower.managers;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.dev.babeltower.dto.CoordinateDTO;

public class CoordinateManager {

    private static CoordinateManager instance;
    private final Map<Player, CoordinateDTO> createdCoordinateInfos;

    private CoordinateManager() {
        instance = this;
        createdCoordinateInfos = new HashMap<>();
    }

    public static synchronized CoordinateManager getInstance() {
        if (instance == null) {
            instance = new CoordinateManager();
        }
        return instance;
    }

    public CoordinateDTO registerPlayer(Player player) {
        CoordinateDTO coordinate = new CoordinateDTO();
        createdCoordinateInfos.put(player, coordinate);
        return coordinate;
    }

    public boolean validateRegisteredPlayer(Player player) {
        return createdCoordinateInfos.containsKey(player);
    }

    public CoordinateDTO findBy(Player player) {
        return createdCoordinateInfos.get(player);
    }

    public void put(Player player, CoordinateDTO coordinate) {
        createdCoordinateInfos.put(player, coordinate);
    }

    public void unregister(Player player) {
        createdCoordinateInfos.remove(player);
    }
}
