package org.dev.babeltower.command.handler;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public interface CommandHandler {
    public boolean handle(@NotNull CommandSender commandSender, @NotNull String[] strings);
}
