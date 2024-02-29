package org.dev.babeltower.views;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ErrorViews {

    CASTING_FAIL("Error converting document to %s: "),
    NO_SUCH_FIELD("No such field ERROR in %s: "),
    NO_SUCH_MOB_TYPE("No such mob type %s: "),
    DUPLICATED_ROOM("""
        Duplicated room:\s
        your room:\s
         %s\s
        duplicated target:\s
         %s"""),
    NOT_VALID_ROOM_COORDINATE("""
        Room coordinate is not valid: \s
        coordinate1: \s
        %s \s
        coordinate2: \s
        %s"""),
    UPDATE_ROOM_ERROR("Fail to update Room in mongodb"),
    UPDATE_RAID_RESULT("Fail to update Raid Result in mongodb"),
    NOT_VALID_TOWER_FLOOR("Is not valid floor: %d");


    private String messageFormat;

    public void printWith(Object... args) {
        System.err.printf(messageFormat, args);
    }
}
