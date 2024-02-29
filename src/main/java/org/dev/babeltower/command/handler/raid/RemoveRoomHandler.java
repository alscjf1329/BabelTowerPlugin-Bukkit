package org.dev.babeltower.command.handler.raid;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.dev.babeltower.command.handler.CommandHandler;
import org.dev.babeltower.command.usage.RaidCommandUsage;
import org.dev.babeltower.dto.TowerRoomDTO;
import org.dev.babeltower.managers.TowerRoomManager;
import org.jetbrains.annotations.NotNull;

public class RemoveRoomHandler implements CommandHandler {

    @Override
    public boolean handle(@NotNull CommandSender commandSender, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        if (strings.length < 2) {
            RaidCommandUsage.REMOVE_ROOM.sendTo(player);
            return false;
        }
        int roomNum = Integer.parseInt(strings[1]);
        TowerRoomDTO towerRoom = TowerRoomManager.getInstance().findBy(roomNum);
        TowerRoomManager.getInstance().unregisterTowerRoom(towerRoom);
        return true;
    }
}
