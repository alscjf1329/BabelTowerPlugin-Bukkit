package org.dev.babeltower.command.usage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Turtle;
import org.dev.babeltower.command.handler.CommandHandler;
import org.dev.babeltower.command.handler.raid.CreateRoomHandler;
import org.dev.babeltower.command.handler.raid.EnterRoomHandler;
import org.dev.babeltower.command.handler.raid.RemoveRoomHandler;
import org.dev.babeltower.command.handler.raid.SearchPlayerHandler;
import org.dev.babeltower.command.handler.raid.SetRoomHandler;
import org.dev.babeltower.command.handler.raid.SetTowerRewardHandler;
import org.dev.babeltower.command.handler.raid.ShowRaidStatusHandler;
import org.dev.babeltower.command.handler.raid.ShowRoomsHandler;
import org.dev.babeltower.views.ErrorChatView;
import org.dev.babeltower.views.ErrorViews;

@Getter
@AllArgsConstructor
public enum RaidCommandUsage {
    CREATE_ROOM("방생성 roomNum", new CreateRoomHandler(), true),
    ENTER_ROOM("입장 <floor>", new EnterRoomHandler(), false),
    SET_ROOM("""
        \s
        방설정 roomNum 유저스폰 \s 
        방설정 roomNum 몹스폰 <추가|초기화> \s
        """, new SetRoomHandler(), true),
    SET_REWARD("보상설정 floor", new SetTowerRewardHandler(), true),
    SEARCH_PLAYER("정보 nickname", new SearchPlayerHandler(), false),
    SHOW_ROOMS("방목록",new ShowRoomsHandler(), false),
    SHOW_RAID_STATUS("진행상태",new ShowRaidStatusHandler(), false),
    REMOVE_ROOM("방삭제",new RemoveRoomHandler(), true);
    public static final String COMMAND = "바벨탑";
    private final String message;
    private final CommandHandler commandHandler;
    private final boolean isOPCommand;

    public void sendTo(Player player) {
        player.sendMessage(String.format("/%s %s", COMMAND, message));
    }
    public boolean validatePermission(CommandSender commandSender){
        if(!(commandSender instanceof Player)){
            ErrorViews.NO_PERMISSION.printWith();
            return false;
        }
        if(this.isOPCommand && (!commandSender.isOp())){
            ErrorChatView.NO_PERMISSION.sendTo((Player) commandSender);
            return false;
        }
        return true;
    }
}
