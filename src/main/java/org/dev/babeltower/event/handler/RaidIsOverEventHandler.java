package org.dev.babeltower.event.handler;

import com.hj.rpgsharp.rpg.apis.rpgsharp.utils.Serializer;
import com.hj.rpgsharp.rpg.plugins.mailbox.objects.Mail;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.dev.babeltower.BabelTower;
import org.dev.babeltower.dto.RaidResultDTO;
import org.dev.babeltower.event.RaidIsOverEvent;
import org.dev.babeltower.managers.RaidManager;
import org.dev.babeltower.managers.TowerRoomManager;
import org.dev.babeltower.views.ChatView;

public class RaidIsOverEventHandler implements Listener {

    @EventHandler
    public RaidResultDTO onRaidIsOver(RaidIsOverEvent raidIsOverEvent) {
        RaidResultDTO raidResult = raidIsOverEvent.getRaidResult();
        String nickname = raidResult.getRaid().getPlayerTower().getNickname();
        Player player = Objects.requireNonNull(Bukkit.getPlayer(nickname));

        int floor = raidResult.getRaid().getTower().getFloor();
        if (raidResult.isSucceeded()) {
            String serializedReward = raidResult.getRaid().getTower().getSerializedReward();
            Inventory inventory = Serializer.deserializeInventory(serializedReward);
            long clearTime = raidResult.getClearTime();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(clearTime);

            String message = String.format(ChatView.SUCCESS_RAID.getMessageFormat(),
                player.getName(), floor, seconds);
            Mail mail = Mail.createMail(BabelTower.NAME, message, 0.0, inventory.getContents());
            mail.receive(player);
        } else {
            ChatView.FAIL_RAID.sendTo(player, player.getName(), floor);
        }
        RaidManager.getInstance().clearRaidBy(player);
        TowerRoomManager.getInstance().unregister(player);
        return raidResult;
    }
}
