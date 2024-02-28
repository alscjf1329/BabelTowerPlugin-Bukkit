package org.dev.babeltower.command;

import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.dev.babeltower.command.usage.RaidCommandUsage;
import org.dev.babeltower.dto.Raid;
import org.dev.babeltower.dto.TowerRoomDTO;
import org.dev.babeltower.managers.RaidManager;
import org.dev.babeltower.managers.TowerRoomManager;
import org.dev.babeltower.service.UserIdentificationService;
import org.dev.babeltower.views.ChatView;
import org.jetbrains.annotations.NotNull;

public class RaidCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command,
        @NotNull String s, @NotNull String[] strings) {
        if (UserIdentificationService.validateNotPlayer(commandSender)) {
            return false;
        }
        Player player = (Player) commandSender;
        if (strings.length < 1) {
            for (RaidCommandUsage usage : RaidCommandUsage.values()) {
                usage.sendTo(player);
            }
            return false;
        }
        switch (strings[0]) {
            case "보상설정": {
                return RaidCommandUsage.SET_REWARD.getCommandHandler()
                    .handle(commandSender, strings);
            }
            case "진행상태": {
                Raid raid = RaidManager.getInstance().findRaidBy(player);
                if (raid == null) {
                    player.sendMessage("진행 중인 레이드가 없습니다.");
                    return true;
                }
                player.sendMessage(raid.toString());
                return true;
            }
            case "방목록": {
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
            case "입장": {
                return RaidCommandUsage.ENTER_ROOM.getCommandHandler()
                    .handle(commandSender, strings);
            }
            case "랭킹": {
                return true;
            }
            case "정보": {
                return true;
            }
            case "보상받기": {
                return true;
            }
            case "방생성": {
                return RaidCommandUsage.CREATE_ROOM.getCommandHandler()
                    .handle(commandSender, strings);
            }
            case "방설정": {
                return RaidCommandUsage.SET_ROOM.getCommandHandler()
                    .handle(commandSender, strings);
            }
            case "방삭제": {
                if (strings.length < 2) {
                    return false;
                }
                int roomNum = Integer.parseInt(strings[1]);
                TowerRoomDTO towerRoom = TowerRoomManager.getInstance().findBy(roomNum);
                TowerRoomManager.getInstance().unregisterTowerRoom(towerRoom);
                return true;
            }
        }
        return false;
    }
}
