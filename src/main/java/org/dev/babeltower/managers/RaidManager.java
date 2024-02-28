package org.dev.babeltower.managers;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.dev.babeltower.dto.PlayerTowerDTO;
import org.dev.babeltower.dto.Raid;
import org.dev.babeltower.dto.TowerRoomDTO;

public class RaidManager {

    private static RaidManager instance;
    private final Map<Player, Raid> raids;

    private RaidManager() {
        raids = new HashMap<>();
    }

    public Raid findRaidBy(Player player) {
        return raids.get(player);
    }
    public boolean validatePlayerInRaid(Player player){
        return raids.containsKey(player);
    }

    public synchronized static RaidManager getInstance() {
        if (instance == null) {
            instance = new RaidManager();
        }
        return instance;
    }

    public void clearAll() {
        for (Raid raid : raids.values()) {
            raid.clear();
        }
    }

    public void clearRaidBy(Player player) {
        Raid removedRaid = raids.remove(player);
        if(removedRaid == null){
            return;
        }
        removedRaid.clear();
    }

    public Raid createRaid(int floor, TowerRoomDTO towerRoom, PlayerTowerDTO playerTower) {
        Raid raid = new Raid(floor, towerRoom, playerTower);
        raids.put(Bukkit.getPlayer(playerTower.getNickname()), raid);
        return raid;
    }
}
