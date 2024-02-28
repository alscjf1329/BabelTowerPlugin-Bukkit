package org.dev.babeltower.dto;

import io.lumine.mythic.bukkit.utils.lib.jooq.impl.QOM.Floor;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TowerDTO {

    private Integer floor;          // 층
    private List<String> mobs;       // mobs
    private Integer timeLimit;        // 제한 시간 (분)
    @Setter
    private String serializedReward;  // 직렬화된 보상 (인벤토리)

    public static String getKeyFieldName() {
        return "floor";
    }

    public int getKeyFieldValue() {
        return floor;
    }
}

