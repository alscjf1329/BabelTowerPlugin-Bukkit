package org.dev.babeltower.command;

import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.dev.babeltower.command.handler.CommandHandler;
import org.dev.babeltower.command.handler.raid.CreateRoomHandler;
import org.dev.babeltower.command.handler.raid.RemoveRoomHandler;
import org.dev.babeltower.command.handler.raid.SetRoomHandler;
import org.dev.babeltower.command.handler.raid.SetTowerRewardHandler;
import org.dev.babeltower.utils.UserIdentificationService;
import org.dev.babeltower.views.AdminCommandUsage;
import org.dev.babeltower.views.ErrorViews;
import org.jetbrains.annotations.NotNull;

public class AdminCommand implements CommandExecutor {

    public static final String COMMAND = "바벨탑관리";

    private static final Map<String, CommandHandler> COMMAND_HANDLER_MAP = new LinkedHashMap<>();

    static {
        COMMAND_HANDLER_MAP.put("보상설정", new SetTowerRewardHandler());
        COMMAND_HANDLER_MAP.put("방생성", new CreateRoomHandler());
        COMMAND_HANDLER_MAP.put("방설정", new SetRoomHandler());
        COMMAND_HANDLER_MAP.put("방삭제", new RemoveRoomHandler());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command,
        @NotNull String s, @NotNull String[] strings) {
        if (!UserIdentificationService.validateOPPlayer(commandSender)) {
            ErrorViews.NOT_OP_PERMISSION.printWith();
            return false;
        }
        Player player = (Player) commandSender;
        if (strings.length == 0) {
            player.sendMessage(AdminCommandUsage.getAllUsage());
            return true;
        }
        if (!COMMAND_HANDLER_MAP.containsKey(strings[0])) {
            return false;
        }
        return COMMAND_HANDLER_MAP.get(strings[0]).handle(commandSender, strings);
    }
}
