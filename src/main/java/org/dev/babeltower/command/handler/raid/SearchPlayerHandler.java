package org.dev.babeltower.command.handler.raid;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.dev.babeltower.command.handler.CommandHandler;
import org.dev.babeltower.command.usage.RaidCommandUsage;
import org.dev.babeltower.dto.PlayerTowerDTO;
import org.dev.babeltower.managers.PlayerTowerManager;
import org.dev.babeltower.views.ErrorChatView;
import org.jetbrains.annotations.NotNull;

public class SearchPlayerHandler implements CommandHandler {


    @Override
    public boolean handle(@NotNull CommandSender commandSender, @NotNull String[] strings) {
        if (!RaidCommandUsage.SEARCH_PLAYER.validatePermission(commandSender)) {
            return false;
        }
        Player player = (Player) commandSender;
        String nickname;
        if (strings.length < 2) {
            nickname = player.getName();
        } else {
            nickname = strings[1];
        }
        try {
            PlayerTowerDTO playerInfo = PlayerTowerManager.findPlayerInfo(nickname);
            if (playerInfo == null) {
                ErrorChatView.NO_SUCH_PLAYER.sendTo(player, nickname);
                return false;
            }
            player.sendMessage(playerInfo.toString());
        } catch (ReflectiveOperationException e) {
            return false;
        }
        return true;
    }
}
