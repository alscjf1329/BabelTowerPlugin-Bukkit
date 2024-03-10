package org.dev.babeltower.command.handler.raid;

import java.util.Map;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.dev.babeltower.command.handler.CommandHandler;
import org.dev.babeltower.dto.BabelTowerRaid;
import org.dev.babeltower.dto.TowerRoomDTO;
import org.dev.babeltower.managers.BabelTowerRaidManager;
import org.dev.babeltower.managers.TowerRoomManager;
import org.dev.babeltower.views.ChatView;
import org.dev.babeltower.views.ErrorChatView;
import org.jetbrains.annotations.NotNull;

public class ShowRoomsHandler implements CommandHandler {

    @Override
    public boolean handle(@NotNull CommandSender commandSender, @NotNull String[] strings) {
        Player sender = (Player) commandSender;
        Map<TowerRoomDTO, Player> roomInfos = TowerRoomManager.getInstance().findAll();
        for (TowerRoomDTO room : roomInfos.keySet()) {
            if (!room.isValid()) {
                ErrorChatView.IS_NOT_VALID_ROOM_FORMAT.sendTo(sender, room.getNum());
                continue;
            }
            Player roomUser = roomInfos.get(room);
            if (roomUser == null) {
                ChatView.EMPTY_ROOM.sendTo(sender, room.getNum());
                continue;
            }
            BabelTowerRaid raid = BabelTowerRaidManager.getInstance().findRaidBy(roomUser);
            ChatView.FULL_ROOM.sendTo(sender, room.getNum(), roomUser.getName(),
                raid.getRemainedSeconds());
        }
        return true;
    }
}
