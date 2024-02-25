package org.dev.babeltower.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.dev.babeltower.managers.CoordinateManager;
import org.dev.babeltower.service.UserIdentificationService;
import org.jetbrains.annotations.NotNull;

public class CoordinateCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command,
        @NotNull String s, @NotNull String[] strings) {
        if (UserIdentificationService.validateNotPlayer(commandSender)) {
            return false;
        }
        if (!commandSender.isOp()) {
            return false;
        }
        Player player = (Player) commandSender;
        if (strings.length < 1) {
            player.sendMessage(CoordinateManager.getInstance().findBy(player).toString());
            return true;
        }
        switch (strings[0]) {
            case "설정": {
                CoordinateManager.getInstance().registerPlayer((Player) commandSender);
                player.sendMessage("좌표 설정해주세요");
                return true;
            }
            case "취소": {
                CoordinateManager.getInstance().unregister((Player) commandSender);
                player.sendMessage("좌표 취소");
                return true;
            }
            default: {
                player.sendMessage("좆망 커맨드");
            }
        }
        return false;
    }

}
