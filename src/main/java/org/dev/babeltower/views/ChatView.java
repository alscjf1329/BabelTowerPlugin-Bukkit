package org.dev.babeltower.views;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ChatView {
    ROOM_METCHING_FAIL("남아 있는 룸이 존재하지 않습니다.");
    private String messageFormat;

    public String getMessage(Object... args) {
        return String.format(messageFormat, args);
    }
}
