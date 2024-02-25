package org.dev.babeltower.views;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@AllArgsConstructor
public enum ChatView {
    ROOM_MATCHING_LOADING("방에 입장 중 입니다..."),

    ROOM_MATCHING_FAIL("남아 있는 방이 존재하지 않습니다."),
    BIGGER_THAN_MAX_FLOOR("%s님은 이미 최고층에 도달했습니다."),
    FULL_ROOM("%d번방 - %s님 사용 중"),
    EMPTY_ROOM("%d번방 - 사용 가능"),
    IS_NOT_VALID("%d번방 - 수정 중"),
    SUCCESS_RAID("""
        %s님이 %d층 도전에 성공했습니다.\s
        clear time: %f"""),
    FAIL_RAID("%s님이 %d층 도전에 실패했습니다.");

    private final String messageFormat;

    public void sendTo(Player player, Object... args) {
        player.sendMessage(String.format(messageFormat, args));
    }
}
