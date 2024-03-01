package org.dev.babeltower.dto;

import java.sql.Timestamp;
import java.util.Calendar;
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
    private Timestamp recentFail;

    public PlayerTowerDTO applyRaid(RaidResultDTO raidResult) {
        if (raidResult.isSucceeded()) {
            return new PlayerTowerDTO(
                this.uuid, this.nickname,
                raidResult.getRaid().getTower().getFloor(),
                raidResult.getClearTime(), null
            );
        }
        return new PlayerTowerDTO(
            this.uuid, this.nickname,
            this.latestFloor,
            this.clearTime,
            new Timestamp(System.currentTimeMillis())
        );
    }

    public static PlayerTowerDTO createDefault(Player player) {
        return new PlayerTowerDTO(
            player.getUniqueId().toString(), player.getName(), 0, null, null
        );
    }

    public String getKeyFieldName() {
        return "nickname";
    }

    public void teleportToRoom(@NotNull TowerRoomDTO towerRoom) {
        Player player = Bukkit.getPlayer(this.nickname);
        System.out.println(towerRoom.getWorldName());
        System.out.println(player.getWorld().getName());
        Objects.requireNonNull(player)
            .teleport(LocationConvertor.listToLocation(towerRoom.getWorldName(),
                towerRoom.getTpCoordinate()));
    }

    public long isWithinOneHourAfterRecentFail() {
        if (recentFail == null) {
            return -1;
        }
        long currentTimeMillis = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(recentFail.getTime());

        // recentTime에 1시간을 더한 시간
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        long oneHourAfterRecentTime = calendar.getTimeInMillis();

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
