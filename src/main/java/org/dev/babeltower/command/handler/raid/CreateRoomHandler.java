package org.dev.babeltower.command.handler.raid;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.dev.babeltower.command.handler.CommandHandler;
import org.dev.babeltower.command.usage.RaidCommandUsage;
import org.dev.babeltower.dto.TowerRoomDTO;
import org.dev.babeltower.factory.TowerRoomFactory;
import org.dev.babeltower.managers.TowerRoomManager;
import org.dev.babeltower.service.LocationConvertor;
import org.dev.babeltower.views.ErrorChatView;
import org.dev.coordinateplugin.dto.CoordinateDTO;
import org.dev.coordinateplugin.holders.CoordinateHolder;
import org.jetbrains.annotations.NotNull;

public class CreateRoomHandler implements CommandHandler {

    @Override
    public boolean handle(@NotNull CommandSender commandSender, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        if (!commandSender.isOp()) {
            ErrorChatView.IS_NOT_OP_ERROR.sendTo(player);
            return false;
        }
        if (strings.length < 2) {
            RaidCommandUsage.CREATE_ROOM.sendTo(player);
            return false;
        }
        int roomNum = Integer.parseInt(strings[1]);
        // 방생성
        CoordinateDTO coordinate = CoordinateHolder.getInstance().findBy(player);
        TowerRoomDTO towerRoom = TowerRoomFactory.create(roomNum, coordinate);
        Location location = player.getLocation();
        towerRoom.setTpCoordinate(LocationConvertor.locationToList(location));

        boolean successFlag = TowerRoomManager.getInstance().registerTowerRoom(towerRoom);
        if (!successFlag) {
            ErrorChatView.IS_NOT_VALID_ROOM.sendTo(player, towerRoom);
            return false;
        }
        CoordinateHolder.getInstance().unregister(player);
        player.sendMessage(towerRoom.toString());
        return true;
    }
}
