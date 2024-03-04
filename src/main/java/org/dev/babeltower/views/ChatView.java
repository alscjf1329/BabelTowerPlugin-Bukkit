package org.dev.babeltower.views;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@AllArgsConstructor
public enum ChatView {
    ROOM_MATCHING_LOADING("&a바벨탑에 입장 중 입니다..."),

    FULL_ROOM("%d번방 - %s님의 레이드 진행 중 (남은 시간: %d)"),
    EMPTY_ROOM("%d번방 - 사용 가능"),
    IS_NOT_VALID("%d번방 - 수정 중"),
    SUCCESS_RAID("""
        &a%d층 도전에 성공했습니다.\s
        &f클리어 타임: %d분 %d초"""),
    FAIL_RAID("&c%d층 도전에 실패했습니다."),
    ENTER_RAID("&a%d층에 입장했습니다."),
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
    BABEL_RANK_FORMAT("§7§o%d §f%s §e§o[%d층/%d분 %d초]"),
    BABEL_PLAYER_FORMAT("""
        ◇ %s\s
        &f\s
        ◇ 최고 층: %d층\s
        ◇ 클리어 타임: %d초\s
        """);
    private final String messageFormat;

    public String getMessageFormat() {
        return messageFormat.replace('&', '§');
    }

    public void sendTo(Player player, Object... args) {
        String replacedColorCodeMessageFormat = messageFormat.replace('&', '§');
        player.sendMessage(String.format(replacedColorCodeMessageFormat, args));
    }
}
