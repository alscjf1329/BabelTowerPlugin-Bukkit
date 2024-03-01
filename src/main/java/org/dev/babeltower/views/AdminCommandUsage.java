package org.dev.babeltower.views;

import java.util.Arrays;
import java.util.Comparator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public enum AdminCommandUsage {
    CREATE_ROOM(1, "방생성 방번호"),
    SET_ROOM(2, "방설정 방번호 유저스폰 | 몹스폰 <추가|초기화>"),
    REMOVE_ROOM(3, "방삭제 방번호"),
    SET_REWARD(4, "보상설정 층");
    public static final String COMMAND = "바벨탑관리";
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
        AdminCommandUsage[] values = AdminCommandUsage.values();
        Arrays.sort(values, Comparator.comparingInt(AdminCommandUsage::getOrder));
        for (AdminCommandUsage usage : AdminCommandUsage.values()) {
            sb.append(usage.getUsage()).append("\n");
        }
        return sb.toString();
    }
}
