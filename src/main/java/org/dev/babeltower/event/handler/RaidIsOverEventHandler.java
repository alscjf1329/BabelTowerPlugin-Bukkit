package org.dev.babeltower.event.handler;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.dev.babeltower.dto.RaidResultDTO;
import org.dev.babeltower.event.RaidIsOverEvent;
import org.dev.babeltower.views.ChatView;

public class RaidIsOverEventHandler implements Listener {

    @EventHandler
    public RaidResultDTO onRaidIsOver(RaidIsOverEvent raidIsOverEvent) {
        RaidResultDTO raidResult = raidIsOverEvent.getRaidResult();
        String nickname = raidResult.getRaid().getPlayerTower().getNickname();
        Player player = Objects.requireNonNull(Bukkit.getPlayer(nickname));

        long clearTime = raidResult.getClearTime();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(clearTime);
        int floor = raidResult.getRaid().getTower().getFloor();
        if (raidResult.isSucceeded()) {
            ChatView.SUCCESS_RAID.sendTo(player, floor, seconds);
        } else {
            ChatView.FAIL_RAID.sendTo(player, floor);
        }

        raidResult.getRaid().clear();
        return raidResult;
    }
}
