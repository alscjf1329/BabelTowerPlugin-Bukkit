package org.dev.babeltower.command.handler.raid;

import org.bukkit.command.CommandSender;
import org.dev.babeltower.command.handler.CommandHandler;
import org.dev.babeltower.managers.BabelRankingManager;
import org.jetbrains.annotations.NotNull;

public class ShowBabelRankingHandler implements CommandHandler {

    @Override
    public boolean handle(@NotNull CommandSender commandSender, @NotNull String[] strings) {
        commandSender.sendMessage(BabelRankingManager.getInstance().readRanking());
        return true;
    }
}
