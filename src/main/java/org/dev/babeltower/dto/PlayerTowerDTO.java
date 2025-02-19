package org.dev.babeltower.dto;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.dev.babeltower.utils.LocationConvertor;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerTowerDTO {

    private String uuid;
    private String nickname;
    private Integer latestFloor;
    private Long clearTime;
    private Long recentFail;

    public PlayerTowerDTO applyRaidResult(BabelTowerRaidResultDTO raidResult) {
        if (raidResult.isSucceeded()) {
            int resultFloor = raidResult.getRaid().getTower().getFloor();
            long resultClearTime = raidResult.getClearTime();

            if (this.clearTime == null) {
                this.clearTime = resultClearTime;
            }
            if (this.latestFloor < resultFloor) {
                resultClearTime = this.clearTime;
            } else if (this.latestFloor == resultFloor) {
                resultClearTime = Math.min(this.clearTime, resultClearTime);
            }
            return new PlayerTowerDTO(
                this.uuid, this.nickname,
                resultFloor, resultClearTime, null
            );
        }
        return new PlayerTowerDTO(
            this.uuid, this.nickname,
            this.latestFloor,
            this.clearTime,
            System.currentTimeMillis()
        );
    }

    public static PlayerTowerDTO createDefault(Player player) {
        return new PlayerTowerDTO(
            player.getUniqueId().toString(), player.getName(), 0, null, null
        );
    }

    public String getKeyFieldName() {
        return "uuid";
    }

    public void teleportToRoom(@NotNull TowerRoomDTO towerRoom) {
        Player player = Bukkit.getPlayer(this.nickname);
        Objects.requireNonNull(player)
            .teleport(LocationConvertor.listToLocation(towerRoom.getWorldName(),
                towerRoom.getTpCoordinate()));
    }

    public long isWithinOneHourAfterRecentFail() {
        if (recentFail == null) {
            return -1;
        }
        long currentTimeMillis = System.currentTimeMillis();

        // recentTime에 1시간을 더한 시간
        long oneHourAfterRecentTime = recentFail + TimeUnit.HOURS.toMillis(1);

        long remainingTimeMillis = oneHourAfterRecentTime - currentTimeMillis;
        return TimeUnit.MILLISECONDS.toSeconds(remainingTimeMillis);
    }

    @Override
    public String toString() {
        return String.format(
            "PlayerTowerDTO{\n" +
                "  uuid='%s',\n" +
                "  nickname='%s',\n" +
                "  latestFloor=%d,\n" +
                "  clearTime=%d,\n" +
                "  recentFail=%s\n" +
                "}",
            uuid, nickname, latestFloor, clearTime, recentFail
        );
    }
}
