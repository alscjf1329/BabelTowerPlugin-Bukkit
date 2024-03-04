package org.dev.babeltower.event.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.dev.babeltower.dto.Raid;
import org.dev.babeltower.dto.RaidResultDTO;
import org.dev.babeltower.event.RaidIsOverEvent;
import org.dev.babeltower.managers.RaidManager;

public class RaidUserQuitEventHandler implements Listener {

    @EventHandler
    public void onRaidUserQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        Raid raid = RaidManager.getInstance().findRaidBy(player);
        if (raid == null) {
            return;
        }
        RaidResultDTO failedRaidResult = RaidResultDTO.createFailedRaidResultDTO(
            raid);
        Bukkit.getServer().getPluginManager()
            .callEvent(new RaidIsOverEvent(failedRaidResult));
    }
}
