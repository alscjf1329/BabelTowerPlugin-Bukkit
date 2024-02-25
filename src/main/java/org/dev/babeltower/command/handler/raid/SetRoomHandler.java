package org.dev.babeltower.command.handler.raid;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.dev.babeltower.command.handler.CommandHandler;
import org.dev.babeltower.command.usage.RaidCommandUsage;
import org.dev.babeltower.dto.TowerRoomDTO;
import org.dev.babeltower.managers.CoordinateManager;
import org.dev.babeltower.managers.TowerRoomManager;
import org.dev.babeltower.service.LocationConvertor;
import org.dev.babeltower.views.ErrorChatView;
import org.jetbrains.annotations.NotNull;

public class SetRoomHandler implements CommandHandler {

    @Override
    public boolean handle(@NotNull CommandSender commandSender, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        if (!commandSender.isOp()) {
            ErrorChatView.IS_NOT_OP_ERROR.sendTo(player);
            return false;
        }
        if (strings.length < 3) {
            RaidCommandUsage.SET_ROOM.sendTo(player);
            return false;
        }
        int roomNum = Integer.parseInt(strings[1]);
        TowerRoomDTO towerRoom = TowerRoomManager.getInstance().findBy(roomNum);
        if (towerRoom == null) {
            ErrorChatView.IS_NOT_VALID_ROOM_NUM.sendTo(player, roomNum);
            return false;
        }
        Location location = CoordinateManager.getInstance().findBy(player).getPos1();
        if (location == null) {
            ErrorChatView.IS_NULL_LOCATION.sendTo(player);
            return false;
        }
        switch (strings[2]) {
            case "유저스폰": {
                towerRoom.setTpCoordinate(LocationConvertor.locationToList(location));
                TowerRoomManager.getInstance().updateTowerRoom(player, towerRoom);
                return true;
            }
            case "몹스폰": {
                if (strings.length < 4) {
                    RaidCommandUsage.SET_ROOM.sendTo(player);
                    return false;
                }
                switch (strings[3]) {
                    case "추가": {
                        List<Double> xyz = LocationConvertor.locationToList(location);
                        towerRoom.addMobCoordinate(xyz);
                        break;
                    }
                    case "초기화": {
                        towerRoom.setMobCoordinate(new ArrayList<>());
                        break;
                    }
                }
                TowerRoomManager.getInstance().updateTowerRoom(player, towerRoom);
                return true;
            }
        }
        return true;
    }
}
