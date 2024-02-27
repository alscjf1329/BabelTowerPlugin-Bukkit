package org.dev.babeltower.command.handler.raid;

import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.dev.babeltower.command.handler.CommandHandler;
import org.dev.babeltower.dto.PlayerTowerDTO;
import org.dev.babeltower.dto.Raid;
import org.dev.babeltower.dto.TowerRoomDTO;
import org.dev.babeltower.managers.PlayerTowerManager;
import org.dev.babeltower.managers.RaidManager;
import org.dev.babeltower.managers.TowerManager;
import org.dev.babeltower.managers.TowerRoomManager;
import org.dev.babeltower.views.ChatView;
import org.dev.babeltower.views.ErrorChatView;
import org.jetbrains.annotations.NotNull;

public class EnterRoomHandler implements CommandHandler {

    @Override
    public boolean handle(@NotNull CommandSender commandSender, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        if (RaidManager.getInstance().validatePlayerInRaid(player)) {
            ErrorChatView.ALREADY_IN_RAID.sendTo(player);
            return false;
        }
        PlayerTowerDTO playerTower;
        try {
            playerTower = PlayerTowerManager.savePlayerInfo(player);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
        // /바벨탑 방입장
        if (strings.length == 1) {
            enterNextFloor(playerTower);
            return true;
        }
        // /바벨탑 방입장 <방번호>
        enterFloor(playerTower, Integer.parseInt(strings[1]));
        return false;
    }

    private void enterNextFloor(PlayerTowerDTO playerTower) {
        enterFloor(playerTower, playerTower.getLatestFloor() + 1);
    }

    private void enterFloor(PlayerTowerDTO playerTower, int floor) {
        Player player = Objects.requireNonNull(Bukkit.getPlayer(playerTower.getNickname()));
        ChatView.ROOM_MATCHING_LOADING.sendTo(player);
        TowerRoomDTO towerRoom = TowerRoomManager.getInstance().matchPlayer(player);
        if (towerRoom == null) {
            ChatView.ROOM_MATCHING_FAIL.sendTo(player);
            return;
        }
        playerTower.teleportToRoom(towerRoom);

        int maxFloor = TowerManager.getInstance().findMaxFloor();
        if (floor > maxFloor) {
            ChatView.BIGGER_THAN_MAX_FLOOR.sendTo(player, player.getName());
            return;
        }
        Raid raid = RaidManager.getInstance().createRaid(floor, towerRoom, playerTower);
        raid.start();
        ChatView.ENTER_RAID.sendTo(player, player.getName(), floor);
    }
}
