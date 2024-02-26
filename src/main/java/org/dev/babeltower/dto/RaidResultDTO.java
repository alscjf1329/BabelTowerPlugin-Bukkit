package org.dev.babeltower.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RaidResultDTO {

    private Raid raid;
    private boolean isSucceeded;
    private Long clearTime;

    public static RaidResultDTO createSuccessedRaidResultDTO(Raid raid, long finishMills) {
        return new RaidResultDTO(raid, true, finishMills - raid.getStartTime());
    }

    public static RaidResultDTO createFailedRaidResultDTO(Raid raid, long finishMills) {
        return new RaidResultDTO(raid, false, null);
    }
}
