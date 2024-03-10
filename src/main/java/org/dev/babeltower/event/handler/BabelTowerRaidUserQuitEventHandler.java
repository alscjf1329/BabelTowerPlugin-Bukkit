package org.dev.babeltower.event.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.dev.babeltower.dto.BabelTowerRaid;
import org.dev.babeltower.dto.BabelTowerRaidResultDTO;
import org.dev.babeltower.event.BabelTowerRaidIsOverEvent;
import org.dev.babeltower.managers.BabelTowerRaidManager;

public class BabelTowerRaidUserQuitEventHandler implements Listener {

    @EventHandler
    public void onRaidUserQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        BabelTowerRaid raid = BabelTowerRaidManager.getInstance().findRaidBy(player);
        if (raid == null) {
            return;
        }
        BabelTowerRaidResultDTO failedRaidResult = BabelTowerRaidResultDTO.createFailedRaidResultDTO(
            raid);
        Bukkit.getServer().getPluginManager()
            .callEvent(new BabelTowerRaidIsOverEvent(failedRaidResult));
    }
}
