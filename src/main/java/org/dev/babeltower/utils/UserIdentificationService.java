package org.dev.babeltower.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UserIdentificationService {

    public static boolean validatePlayer(CommandSender commandSender) {
        return commandSender instanceof Player;
    }

    public static boolean validateOPPlayer(CommandSender commandSender) {
        return (commandSender instanceof Player) && commandSender.isOp();
    }

}
