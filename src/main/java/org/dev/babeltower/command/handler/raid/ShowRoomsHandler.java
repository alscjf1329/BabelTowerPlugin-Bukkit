package org.dev.babeltower.command.handler.raid;

import java.util.Map;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.dev.babeltower.command.handler.CommandHandler;
import org.dev.babeltower.dto.Raid;
import org.dev.babeltower.dto.TowerRoomDTO;
import org.dev.babeltower.managers.RaidManager;
import org.dev.babeltower.managers.TowerRoomManager;
import org.dev.babeltower.views.ChatView;
import org.jetbrains.annotations.NotNull;

public class ShowRoomsHandler implements CommandHandler {

    @Override
    public boolean handle(@NotNull CommandSender commandSender, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        Map<TowerRoomDTO, Player> roomInfos = TowerRoomManager.getInstance().findAll();
        for (TowerRoomDTO room : roomInfos.keySet()) {
            if (!room.isValid()) {
                ChatView.IS_NOT_VALID.sendTo(player, room.getNum());
                continue;
            }
            if (roomInfos.get(room) == null) {
                ChatView.EMPTY_ROOM.sendTo(player, room.getNum(), player.getName());
                continue;
            }
            Raid raid = RaidManager.getInstance().findRaidBy(player);
            ChatView.FULL_ROOM.sendTo(player, room.getNum(), player.getName(),
                raid.getRemainedSeconds());
        }
        return true;
    }
}
