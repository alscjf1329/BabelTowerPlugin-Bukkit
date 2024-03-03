package org.dev.babeltower.command;

import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.dev.babeltower.command.handler.CommandHandler;
import org.dev.babeltower.command.handler.raid.EnterRoomHandler;
import org.dev.babeltower.command.handler.raid.SearchPlayerHandler;
import org.dev.babeltower.command.handler.raid.ShowBabelRankingHandler;
import org.dev.babeltower.command.handler.raid.ShowRaidStatusHandler;
import org.dev.babeltower.command.handler.raid.ShowRoomsHandler;
import org.dev.babeltower.utils.UserIdentificationService;
import org.dev.babeltower.views.UserCommandUsage;
import org.jetbrains.annotations.NotNull;

public class UserCommand implements CommandExecutor {

    public static final String COMMAND = "바벨탑";
    private static final Map<String, CommandHandler> COMMAND_HANDLER_MAP = new LinkedHashMap<>();

    static {
        COMMAND_HANDLER_MAP.put("진행상태", new ShowRaidStatusHandler());
        COMMAND_HANDLER_MAP.put("방목록", new ShowRoomsHandler());
        COMMAND_HANDLER_MAP.put("입장", new EnterRoomHandler());
        COMMAND_HANDLER_MAP.put("랭킹", new ShowBabelRankingHandler());
        COMMAND_HANDLER_MAP.put("정보", new SearchPlayerHandler());
        // "보상받기" 추가 예정
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command,
        @NotNull String s, @NotNull String[] strings) {
        if (!UserIdentificationService.validatePlayer(commandSender)) {
            return false;
        }
        Player player = (Player) commandSender;
        if (strings.length == 0) {
            player.sendMessage(UserCommandUsage.getAllUsage());
            return true;
        }
        if (!COMMAND_HANDLER_MAP.containsKey(strings[0])) {
            return false;
        }
        return COMMAND_HANDLER_MAP.get(strings[0]).handle(commandSender, strings);
    }
}
