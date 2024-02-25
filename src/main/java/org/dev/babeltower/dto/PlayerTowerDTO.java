package org.dev.babeltower.dto;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.dev.babeltower.service.LocationConvertor;
import org.dev.babeltower.views.ErrorViews;
import org.jetbrains.annotations.NotNull;

@Getter
@ToString
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
                this.latestFloor + 1,
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

    public Field getKey() {
        try {
            return this.getClass().getDeclaredField("nickname");
        } catch (NoSuchFieldException e) {
            ErrorViews.NO_SUCH_FIELD.printWith("PlayerTowerDTO");
            throw new RuntimeException(e);
        }
    }

    public void teleportToRoom(@NotNull TowerRoomDTO towerRoom) {
        Player player = Bukkit.getPlayer(this.nickname);
        System.out.println(towerRoom.getWorldName());
        System.out.println(player.getWorld().getName());
        Objects.requireNonNull(player)
            .teleport(LocationConvertor.listToLocation(towerRoom.getWorldName(),
                towerRoom.getTpCoordinate()));
    }
}
