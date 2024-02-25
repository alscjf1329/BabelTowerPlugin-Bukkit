package org.dev.babeltower.service;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UserIdentificationService {

    public static boolean validateNotPlayer(CommandSender commandSender) {
        return !(commandSender instanceof Player);
    }

}
