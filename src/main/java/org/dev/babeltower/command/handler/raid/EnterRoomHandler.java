package org.dev.babeltower.command.handler.raid;

import java.util.Objects;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.dev.babeltower.command.handler.CommandHandler;
import org.dev.babeltower.dto.BabelTowerRaid;
import org.dev.babeltower.dto.PlayerTowerDTO;
import org.dev.babeltower.dto.TowerRoomDTO;
import org.dev.babeltower.managers.BabelTowerRaidManager;
import org.dev.babeltower.managers.PlayerTowerManager;
import org.dev.babeltower.managers.TowerManager;
import org.dev.babeltower.managers.TowerRoomManager;
import org.dev.babeltower.views.ChatView;
import org.dev.babeltower.views.ErrorChatView;
import org.jetbrains.annotations.NotNull;

public class EnterRoomHandler implements CommandHandler {

    @Override
    public boolean handle(@NotNull CommandSender commandSender, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        if (BabelTowerRaidManager.getInstance().validatePlayerInRaid(player)) {
            ErrorChatView.ALREADY_IN_RAID.sendTo(player);
            return false;
        }
        PlayerTowerDTO playerTower;
        try {
            playerTower = PlayerTowerManager.savePlayerInfo(player);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
        if (playerTower.getRecentFail() != null) {
            long remainingSeconds = 0;
//            long remainingSeconds = playerTower.isWithinOneHourAfterRecentFail();
            if (remainingSeconds > 0) {
                ErrorChatView.RECENT_FAIL_IN_ONE_HOUR.sendTo(player, remainingSeconds);
                return false;
            }
        }
        // /바벨탑 입장
        if (strings.length == 1) {
            enterNextFloor(playerTower);
            return true;
        }
        // /바벨탑 입장 <방번호>
        enterFloor(playerTower, Integer.parseInt(strings[1]));
        return false;
    }

    private void enterNextFloor(PlayerTowerDTO playerTower) {
        enterFloor(playerTower, playerTower.getLatestFloor() + 1);
    }

    private void enterFloor(PlayerTowerDTO playerTower, int floor) {
        UUID uuid = UUID.fromString(playerTower.getUuid());
        Player player = Objects.requireNonNull(Bukkit.getPlayer(uuid));
        ChatView.ROOM_MATCHING_LOADING.sendTo(player);
        TowerRoomDTO availableRoom = TowerRoomManager.getInstance().findAvailableRoom();
        // 비어 있는 방이 있는지 확인
        if (availableRoom == null) {
            ErrorChatView.ROOM_MATCHING_FAIL.sendTo(player);
            return;
        }

        // 해당 플레이어의 (최고층 기록 + 1)보다 높은 층에 입장하려는지 확인
        if (floor > (playerTower.getLatestFloor() + 1)) {
            ErrorChatView.BIGGER_THAN_ACCESSIBLE_FLOOR.sendTo(player,
                playerTower.getLatestFloor() + 1);
            return;
        }

        // 존재하지 않는 층에 입장하는지 확인
        if (TowerManager.getInstance().findTowerInfo(floor) == null) {
            ErrorChatView.NO_SUCH_FLOOR.sendTo(player, floor);
            return;
        }

        TowerRoomDTO towerRoom = TowerRoomManager.getInstance().matchPlayer(player);
        playerTower.teleportToRoom(towerRoom);
        BabelTowerRaid raid = BabelTowerRaidManager.getInstance()
            .createRaid(floor, towerRoom, playerTower);
        raid.start();
        ChatView.ENTER_RAID.sendTo(player, floor);
    }
}
