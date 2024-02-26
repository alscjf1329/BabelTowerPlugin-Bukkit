package org.dev.babeltower.event.handler;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
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
        Map<String, Entity> mobs = raid.getMobs();
        mobs.remove(mythicMobDeathEvent.getMobType().getInternalName());
        if (mobs.isEmpty()) {
            long currentTimeMillis = System.currentTimeMillis();
            RaidResultDTO successedRaidResultDTO = RaidResultDTO.createSuccessedRaidResultDTO(
                raid, currentTimeMillis);

            Bukkit.getServer().getPluginManager()
                .callEvent(new RaidIsOverEvent(successedRaidResultDTO));
            RaidManager.getInstance().clearRaidBy(killer);
        }
    }
}
