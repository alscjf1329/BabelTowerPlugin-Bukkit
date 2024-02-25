package org.dev.babeltower.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TowerDTO {

    private Integer floor;          // 층
    private List<String> mobs;       // mobs
    private Integer timeLimit;        // 제한 시간 (분)
    private String reward;           // 직렬화된 보상 (인벤토리)
}

