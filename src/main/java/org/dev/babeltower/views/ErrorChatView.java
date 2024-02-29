package org.dev.babeltower.views;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@AllArgsConstructor
public enum ErrorChatView {
    DUPLICATED_ROOM_NUM("중복되는 방 번호입니다."),
    IS_NOT_OP_ERROR("OP 권한이 아닙니다."),
    IS_NOT_VALID_ROOM("""
        Is not valid room: \s 
        %s"""),
    IS_NOT_VALID_ROOM_NUM("%d room not exists"),
    IS_NULL_LOCATION("location is not valid"),
    FAIL_TO_CREATE_ROOM("바벨탑 방생성에 실패했습니다."),
    ALREADY_IN_RAID("이미 레이드 참여중입니다."),
    NOT_FOUND_TOWER_INFO("%d층은 존재하지 않습니다."),
    NO_PERMISSION("해당 명령어에 대한 권한이 없습니다."),
    NO_SUCH_PLAYER("%s 유저의 바벨탑 전적이 존재하지 않습니다."),
    RECENT_FAIL_IN_ONE_HOUR("%d분 후에 입장 가능합니다."),
    NO_RAID_IN_PROGRESS("진행 중인 레이드가 없습니다.");

    private final String messageFormat;

    public void sendTo(Player player, Object... args) {
        player.sendMessage(String.format(messageFormat, args));
    }
}
