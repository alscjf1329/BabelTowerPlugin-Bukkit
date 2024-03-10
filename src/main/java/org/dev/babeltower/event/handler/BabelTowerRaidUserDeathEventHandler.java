package org.dev.babeltower.event.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.dev.babeltower.dto.BabelTowerRaid;
import org.dev.babeltower.dto.BabelTowerRaidResultDTO;
import org.dev.babeltower.event.BabelTowerRaidIsOverEvent;
import org.dev.babeltower.managers.BabelTowerRaidManager;

public class BabelTowerRaidUserDeathEventHandler implements Listener {

    @EventHandler
    public void onRaidUserDeathEvent(PlayerDeathEvent playerDeathEvent) {
        Player player = playerDeathEvent.getPlayer();

        BabelTowerRaid raid = BabelTowerRaidManager.getInstance().findRaidBy(player);
        BabelTowerRaidResultDTO failedRaidResult = BabelTowerRaidResultDTO.createFailedRaidResultDTO(
            raid);
        Bukkit.getServer().getPluginManager()
            .callEvent(new BabelTowerRaidIsOverEvent(failedRaidResult));
    }
}
