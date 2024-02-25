package org.dev.babeltower.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
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
}

//todo TowerRoomFactory에서만 만들어지도록 수정
