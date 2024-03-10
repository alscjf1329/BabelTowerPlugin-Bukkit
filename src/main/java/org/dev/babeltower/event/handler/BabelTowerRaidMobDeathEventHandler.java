package org.dev.babeltower.event.handler;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.dev.babeltower.dto.BabelTowerRaid;
import org.dev.babeltower.dto.BabelTowerRaidResultDTO;
import org.dev.babeltower.event.BabelTowerRaidIsOverEvent;
import org.dev.babeltower.managers.BabelTowerRaidManager;

public class BabelTowerRaidMobDeathEventHandler implements Listener {

    @EventHandler
    public void onMobDeathEvent(MythicMobDeathEvent mythicMobDeathEvent) {
        Player killer = (Player) mythicMobDeathEvent.getKiller();
        BabelTowerRaid raid = BabelTowerRaidManager.getInstance().findRaidBy(killer);
        if (raid == null) {
            return;
        }
        raid.removeMob(mythicMobDeathEvent.getEntity());
        if (raid.isAllMobDead()) {
            BabelTowerRaidResultDTO successedRaidResultDTO = BabelTowerRaidResultDTO.createSuccessedRaidResultDTO(raid);

            Bukkit.getServer().getPluginManager()
                .callEvent(new BabelTowerRaidIsOverEvent(successedRaidResultDTO));
        }
    }
}
