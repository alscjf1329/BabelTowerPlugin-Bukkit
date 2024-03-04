package org.dev.babeltower.event.handler;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.dev.babeltower.dto.Raid;
import org.dev.babeltower.dto.RaidResultDTO;
import org.dev.babeltower.event.RaidIsOverEvent;
import org.dev.babeltower.managers.RaidManager;

public class RaidMobDeathEventHandler implements Listener {

    @EventHandler
    public void onMobDeathEvent(MythicMobDeathEvent mythicMobDeathEvent) {
        Player killer = (Player) mythicMobDeathEvent.getKiller();
        Raid raid = RaidManager.getInstance().findRaidBy(killer);
        if (raid == null) {
            return;
        }
        raid.removeMob(mythicMobDeathEvent.getEntity());
        if (raid.isAllMobDead()) {
            RaidResultDTO successedRaidResultDTO = RaidResultDTO.createSuccessedRaidResultDTO(raid);

            Bukkit.getServer().getPluginManager()
                .callEvent(new RaidIsOverEvent(successedRaidResultDTO));
        }
    }
}
