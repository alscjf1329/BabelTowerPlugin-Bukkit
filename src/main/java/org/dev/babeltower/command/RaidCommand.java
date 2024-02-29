package org.dev.babeltower.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.dev.babeltower.command.usage.RaidCommandUsage;
import org.dev.babeltower.service.UserIdentificationService;
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
                if (usage.isOPCommand() && !player.isOp()) {
                    continue;
                }
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
                return RaidCommandUsage.SHOW_RAID_STATUS.getCommandHandler()
                    .handle(commandSender, strings);
            }
            case "방목록": {
                return RaidCommandUsage.SHOW_ROOMS.getCommandHandler()
                    .handle(commandSender, strings);
            }
            case "입장": {
                return RaidCommandUsage.ENTER_ROOM.getCommandHandler()
                    .handle(commandSender, strings);
            }
            case "랭킹": {
                return true;
            }
            case "정보": {
                return RaidCommandUsage.SEARCH_PLAYER.getCommandHandler()
                    .handle(commandSender, strings);
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
                return RaidCommandUsage.REMOVE_ROOM.getCommandHandler()
                    .handle(commandSender, strings);
            }
        }
        return false;
    }
}
