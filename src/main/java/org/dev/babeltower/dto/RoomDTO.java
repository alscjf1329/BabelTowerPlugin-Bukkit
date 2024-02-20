package org.dev.babeltower.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomDTO {
    private int num;                    // room 번호
    private List<long[]> coordinate;    // room 소환 위치
    private List<long[]> tpCoordinate;  // user 소환 위치
    private List<long[]> mobCoordinate; // mob 소환 위치
}
