package org.dev.babeltower.managers;

import org.dev.babeltower.dto.PlayerTowerDTO;
import org.dev.babeltower.dto.Raid;
import org.dev.babeltower.dto.TowerRoomDTO;

public class RaidManager {

    private static RaidManager instance;

    private RaidManager() {
    }

    public synchronized static RaidManager getInstance() {
        if (instance == null) {
            instance = new RaidManager();
        }
        return instance;
    }

    public Raid createRaid(int floor, TowerRoomDTO towerRoom, PlayerTowerDTO playerTower) {
        return new Raid(floor, towerRoom, playerTower);
    }
}
