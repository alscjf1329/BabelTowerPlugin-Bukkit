package org.dev.babeltower.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class TowerDTO {

    private Integer floor;          // 층
    private List<String> mobs;       // mobs
    private String timeLimit;        // 제한 시간
    private String reward;           // 직렬화된 보상 (인벤토리)

    @Override
    public String toString() {
        return "TowerDTO{" +
            "floor=" + floor +
            ", mobs=" + mobs +
            ", timeLimit='" + timeLimit + '\'' +
            ", reward='" + reward + '\'' +
            '}';
    }
}

