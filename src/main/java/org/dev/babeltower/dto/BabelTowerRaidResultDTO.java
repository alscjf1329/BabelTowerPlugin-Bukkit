package org.dev.babeltower.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BabelTowerRaidResultDTO {

    private BabelTowerRaid raid;
    private boolean isSucceeded;
    private Long clearTime;

    public static BabelTowerRaidResultDTO createSuccessedRaidResultDTO(BabelTowerRaid raid) {
        return new BabelTowerRaidResultDTO(raid, true, raid.getTimeLimit() - raid.getRemainedSeconds());
    }

    public static BabelTowerRaidResultDTO createFailedRaidResultDTO(BabelTowerRaid raid) {
        return new BabelTowerRaidResultDTO(raid, false, null);
    }
}
