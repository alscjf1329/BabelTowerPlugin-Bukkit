package org.dev.babeltower.command.handler.raid;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.dev.babeltower.command.handler.CommandHandler;
import org.dev.babeltower.command.usage.RaidCommandUsage;
import org.dev.babeltower.dto.CoordinateDTO;
import org.dev.babeltower.dto.TowerRoomDTO;
import org.dev.babeltower.factory.TowerRoomFactory;
import org.dev.babeltower.managers.CoordinateManager;
import org.dev.babeltower.managers.TowerRoomManager;
import org.dev.babeltower.service.LocationConvertor;
import org.dev.babeltower.views.ErrorChatView;
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
        CoordinateDTO coordinate = CoordinateManager.getInstance().findBy(player);
        TowerRoomDTO towerRoom = TowerRoomFactory.create(roomNum, coordinate);

        boolean successFlag = TowerRoomManager.getInstance().registerTowerRoom(towerRoom);
        if (!successFlag) {
            ErrorChatView.IS_NOT_VALID_ROOM.sendTo(player, towerRoom);
        }
        // 유저스폰 설정
        Location location = player.getLocation();
        towerRoom.setTpCoordinate(LocationConvertor.locationToList(location));
        TowerRoomDTO updatedResult = TowerRoomManager.getInstance()
            .updateTowerRoom(player, towerRoom);
        if (updatedResult == null) {
            ErrorChatView.FAIL_TO_CREATE_ROOM.sendTo(player);
            return false;
        }
        player.sendMessage(updatedResult.toString());
        return true;
    }
}
