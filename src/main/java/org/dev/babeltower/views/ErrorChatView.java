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
    IS_NULL_LOCATION("location is not valid");

    private final String messageFormat;

    public void sendTo(Player player, Object... args) {
        player.sendMessage(String.format(messageFormat, args));
    }
}
