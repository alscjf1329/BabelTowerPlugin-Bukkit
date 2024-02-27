package org.dev.babeltower.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.dev.babeltower.service.LocationConvertor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TowerRoomDTO {

    private int num;                    // room 번호
    private String worldName;           // world 이름
    private List<Double> pos1;          // room 위치
    private List<Double> pos2;
    private List<Double> tpCoordinate;  // user 소환 위치
    private List<List<Double>> mobCoordinate; // mob 소환 위치

    public static String getKeyFieldName() {
        return "num";
    }

    public int getKeyFieldValue() {
        return this.num;
    }

    public void addMobCoordinate(List<Double> mobCoordinate) {
        if (this.mobCoordinate == null) {
            this.mobCoordinate = new ArrayList<>();
        }
        this.mobCoordinate.add(mobCoordinate);
    }

    public boolean isValid() {
        List<Object> fields = Arrays.asList(num, worldName, pos1, pos2, tpCoordinate,
            mobCoordinate);
        for (Object field : fields) {
            if (field == null) {
                return false;
            }
        }
        if (mobCoordinate.isEmpty()) {
            return false;
        }
        return true;
    }

    public List<Entity> getEntities() {
        List<Entity> entities = new ArrayList<>();

        World world = Bukkit.getWorld(this.worldName);
        if (world == null) {
            return null;
        }
        Location pos1 = LocationConvertor.listToLocation(this.worldName, this.pos1);
        Location pos2 = LocationConvertor.listToLocation(this.worldName, this.pos2);
        Chunk pos1Chunk = pos1.getChunk();
        Chunk pos2Chunk = pos2.getChunk();

        int minPosChunkX = Math.min(pos1Chunk.getX(), pos2Chunk.getX());
        int maxPosChunkX = Math.max(pos1Chunk.getX(), pos2Chunk.getX());
        int minPosChunkZ = Math.min(pos1Chunk.getZ(), pos2Chunk.getZ());
        int maxPosChunkZ = Math.max(pos1Chunk.getZ(), pos2Chunk.getZ());
        for (int i = minPosChunkX; i <= maxPosChunkX; ++i) {
            for (int j = minPosChunkZ; j <= maxPosChunkZ; ++j) {
                entities.addAll(Arrays.asList(world.getChunkAt(i, j).getEntities()));
            }
        }
        return entities;
    }

    public void clearEntitiesExcludePlayer() {
        List<Entity> excludedEntities = getEntities().stream().filter(e -> e instanceof Player)
            .toList();
        getEntities().removeAll(excludedEntities);
    }

    public String toString() {
        return "TowerRoomDTO{" +
            "num=" + num +
            ", worldName='" + worldName + '\'' +
            ", pos1=" + pos1 +
            ", pos2=" + pos2 +
            ", tpCoordinate=" + tpCoordinate +
            ", mobCoordinate=" + mobCoordinate +
            '}';
    }
}

//todo TowerRoomFactory에서만 만들어지도록 수정
