package org.dev.babeltower.command.handler.raid;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.dev.babeltower.command.handler.CommandHandler;
import org.dev.babeltower.dto.Raid;
import org.dev.babeltower.managers.RaidManager;
import org.dev.babeltower.views.ErrorChatView;
import org.jetbrains.annotations.NotNull;

public class ShowRaidStatusHandler implements CommandHandler {

    @Override
    public boolean handle(@NotNull CommandSender commandSender, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        Raid raid = RaidManager.getInstance().findRaidBy(player);
        if (raid == null) {
            ErrorChatView.NO_RAID_IN_PROGRESS.sendTo(player);
            return false;
        }
        player.sendMessage(raid.toString());
        return true;
    }
}
