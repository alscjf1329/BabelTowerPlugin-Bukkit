package org.dev.babeltower.event.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.dev.babeltower.dto.Raid;
import org.dev.babeltower.dto.RaidResultDTO;
import org.dev.babeltower.event.RaidIsOverEvent;
import org.dev.babeltower.managers.RaidManager;

public class RaidUserDeathEventHandler implements Listener {

    @EventHandler
    public void onRaidUserDeathEvent(PlayerDeathEvent playerDeathEvent) {
        Player player = playerDeathEvent.getPlayer();

        Raid raid = RaidManager.getInstance().findRaidBy(player);
        RaidResultDTO failedRaidResult = RaidResultDTO.createFailedRaidResultDTO(
            raid);
        Bukkit.getServer().getPluginManager()
            .callEvent(new RaidIsOverEvent(failedRaidResult));
    }
}
