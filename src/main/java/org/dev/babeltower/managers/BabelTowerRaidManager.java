package org.dev.babeltower.managers;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.dev.babeltower.dto.PlayerTowerDTO;
import org.dev.babeltower.dto.BabelTowerRaid;
import org.dev.babeltower.dto.TowerRoomDTO;

public class BabelTowerRaidManager {

    private static BabelTowerRaidManager instance;
    private final Map<Player, BabelTowerRaid> raids;

    private BabelTowerRaidManager() {
        raids = new HashMap<>();
    }

    public BabelTowerRaid findRaidBy(Player player) {
        return raids.get(player);
    }
    public boolean validatePlayerInRaid(Player player){
        return raids.containsKey(player);
    }

    public synchronized static BabelTowerRaidManager getInstance() {
        if (instance == null) {
            instance = new BabelTowerRaidManager();
        }
        return instance;
    }

    public void clearAll() {
        for (BabelTowerRaid raid : raids.values()) {
            raid.clear();
        }
    }

    public BabelTowerRaid clearRaidBy(Player player) {
        BabelTowerRaid removedRaid = raids.remove(player);
        if(removedRaid == null){
            return null;
        }
        removedRaid.clear();
        return removedRaid;
    }

    public BabelTowerRaid createRaid(int floor, TowerRoomDTO towerRoom, PlayerTowerDTO playerTower) {
        BabelTowerRaid raid = new BabelTowerRaid(floor, towerRoom, playerTower);
        raids.put(Bukkit.getPlayer(playerTower.getNickname()), raid);
        return raid;
    }
}
