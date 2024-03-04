package org.dev.babeltower.event.handler;

import com.hj.rpgsharp.rpg.apis.rpgsharp.RPGSharpAPI;
import com.hj.rpgsharp.rpg.apis.rpgsharp.utils.Serializer;
import com.hj.rpgsharp.rpg.objects.RPGPlayer;
import com.hj.rpgsharp.rpg.plugins.mailbox.objects.Mail;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.dev.babeltower.BabelTower;
import org.dev.babeltower.dto.PlayerTowerDTO;
import org.dev.babeltower.dto.RaidResultDTO;
import org.dev.babeltower.event.RaidIsOverEvent;
import org.dev.babeltower.managers.PlayerTowerManager;
import org.dev.babeltower.managers.RaidManager;
import org.dev.babeltower.managers.TowerRoomManager;
import org.dev.babeltower.views.ChatView;

public class RaidIsOverEventHandler implements Listener {

    @EventHandler
    public RaidResultDTO onRaidIsOver(RaidIsOverEvent raidIsOverEvent) {
        RaidResultDTO raidResult = raidIsOverEvent.getRaidResult();
        raidResult.getRaid().stopTimerBar();
        String nickname = raidResult.getRaid().getPlayerTower().getNickname();
        Player player = Objects.requireNonNull(Bukkit.getPlayer(nickname));

        int floor = raidResult.getRaid().getTower().getFloor();
        if (raidResult.isSucceeded()) {
            long clearTime = raidResult.getClearTime();
            long minutes = TimeUnit.SECONDS.toMinutes(clearTime);
            clearTime -= TimeUnit.MINUTES.toSeconds(minutes);
            long seconds = clearTime;

            String message = String.format(ChatView.SUCCESS_RAID.getMessageFormat(),
                floor, minutes, seconds);

            ChatView.SUCCESS_RAID.sendTo(player, floor, minutes, seconds);
            int latestFloor = raidResult.getRaid().getPlayerTower().getLatestFloor();
            int raidFloor = raidResult.getRaid().getTower().getFloor();
            if (raidFloor > latestFloor) {
                // 보상 메일 보내기
                player.sendMessage("메일함에서 보상 확인하세요.");
                String serializedReward = raidResult.getRaid().getTower().getSerializedReward();
                Inventory inventory = Serializer.deserializeInventory(serializedReward);
                Mail mail = Mail.createMail(BabelTower.NAME, message, 0.0, inventory.getContents());
                RPGPlayer rpgPlayer = RPGSharpAPI.getRPGPlayerAPI().getRPGPlayer(player);
                rpgPlayer.getRPGMail().addMail(mail);
                rpgPlayer.write();
            }
        } else {
            ChatView.FAIL_RAID.sendTo(player, floor);
        }

        PlayerTowerDTO playerTower = raidIsOverEvent.getRaidResult().getRaid().getPlayerTower();
        PlayerTowerManager.updateRaidResult(playerTower, raidResult);
        RaidManager.getInstance().clearRaidBy(player);
        TowerRoomManager.getInstance().unregister(player);
        return raidResult;
    }
}
