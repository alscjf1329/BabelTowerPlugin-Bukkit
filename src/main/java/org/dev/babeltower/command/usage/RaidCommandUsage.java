package org.dev.babeltower.command.usage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.dev.babeltower.command.handler.CommandHandler;
import org.dev.babeltower.command.handler.raid.CreateRoomHandler;
import org.dev.babeltower.command.handler.raid.EnterRoomHandler;
import org.dev.babeltower.command.handler.raid.SetRoomHandler;
import org.dev.babeltower.command.handler.raid.SetTowerRewardHandler;

@Getter
@AllArgsConstructor
public enum RaidCommandUsage {
    CREATE_ROOM("방생성 roomNum", new CreateRoomHandler()),
    ENTER_ROOM("입장 <floor>", new EnterRoomHandler()),
    SET_ROOM("""
        \s
        방설정 roomNum 유저스폰 \s 
        방설정 roomNum 몹스폰 <추가|초기화> \s
        """, new SetRoomHandler()),
    SET_REWARD("보상설정 floor", new SetTowerRewardHandler());
    public static final String COMMAND = "바벨탑";
    private final String message;
    private final CommandHandler commandHandler;

    public void sendTo(Player player) {
        player.sendMessage(String.format("/%s %s", COMMAND, message));
    }
}
