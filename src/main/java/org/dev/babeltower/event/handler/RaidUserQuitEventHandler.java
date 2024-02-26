package org.dev.babeltower.event.handler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.dev.babeltower.managers.RaidManager;
import org.dev.babeltower.managers.TowerRoomManager;

public class RaidUserQuitEventHandler implements Listener {

    @EventHandler
    public void onRaidUserQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        RaidManager.getInstance().clearRaidBy(player);
        TowerRoomManager.getInstance().unregister(player);
    }
}
