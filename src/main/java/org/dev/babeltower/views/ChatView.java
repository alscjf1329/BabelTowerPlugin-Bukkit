package org.dev.babeltower.views;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public enum ChatView {
    ROOM_MATCHING_LOADING("방에 입장 중 입니다..."),

    ROOM_MATCHING_FAIL("남아 있는 방이 존재하지 않습니다."),
    BIGGER_THAN_MAX_FLOOR("%s님은 이미 최고층에 도달했습니다."),
    FULL_ROOM("%d번방 - %s님의 레이드 진행 중 (남은 시간: %d)"),
    EMPTY_ROOM("%d번방 - 사용 가능"),
    IS_NOT_VALID("%d번방 - 수정 중"),
    SUCCESS_RAID("""
        %s님이 %d층 도전에 성공했습니다.\s
        clear time: %d"""),
    FAIL_RAID("%s님이 %d층 도전에 실패했습니다."),
    ENTER_RAID("%s님이 %d층에 입장했습니다."),
    SUCCESS_TO_ADD_TP_COORDINATE("""
        %d번 방 유저스폰 좌표 추가 성공:\s
        %s"""),
    SUCCESS_TO_ADD_MOB_COORDINATE("""
        %d번 방 몹스폰 좌표 추가 성공:\s
        %s"""),
    SUCCESS_TO_RESET_MOB_COORDINATES("%d번 방 몹스폰 초기화 성공"),
    SUCCESS_TO_UPSERT_REWARD("%d층 보상 설정 성공"),
    ITEM_DESCRIPTION("%s(%d개)"),
    BABEL_RANK_HEADER_FORMAT(
        "§a%s §a(%s)§a§m                              §m"),
    BABEL_RANK_FORMAT("§7§o%d §f%s §e§o[%d층/%d분 %d초]");
    private final String messageFormat;

    public void sendTo(Player player, Object... args) {
        player.sendMessage(String.format(messageFormat, args));
    }
}
