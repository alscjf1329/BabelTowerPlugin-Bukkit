package org.dev.babeltower.views;

import java.util.Arrays;
import java.util.Comparator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public enum UserCommandUsage {
    SHOW_ROOMS(1, "방목록"),
    ENTER_ROOM(2, "입장 <층>"),
    SHOW_RAID_STATUS(3, "진행상태"),
    SEARCH_PLAYER(4, "정보 <닉네임>"),
    SHOW_BABEL_RANKING(5, "랭킹");
    public static final String COMMAND = "바벨탑";
    private final int order;
    private final String message;

    public String getUsage() {
        return String.format("/%s %s", COMMAND, message);
    }

    public void sendTo(Player player) {
        player.sendMessage(getUsage());
    }

    public static String getAllUsage() {
        StringBuilder sb = new StringBuilder();
        UserCommandUsage[] values = UserCommandUsage.values();
        Arrays.sort(values, Comparator.comparingInt(UserCommandUsage::getOrder));
        for (UserCommandUsage usage : values) {
            sb.append(usage.getUsage()).append("\n");
        }
        return sb.toString();
    }
}
