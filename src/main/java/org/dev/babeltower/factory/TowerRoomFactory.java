package org.dev.babeltower.factory;

import java.util.ArrayList;
import java.util.List;
import org.dev.babeltower.dto.CoordinateDTO;
import org.dev.babeltower.dto.TowerRoomDTO;
import org.dev.babeltower.service.LocationConvertor;
import org.dev.babeltower.views.ErrorViews;

public class TowerRoomFactory {

    private static boolean isNotValidRoomCoordinate(CoordinateDTO coordinateDTO) {
        if (!coordinateDTO.isFull()) {
            return true;
        }
        if (coordinateDTO.getPos1().getWorld() != coordinateDTO.getPos2().getWorld()) {
            return true;
        }
        return false;
    }

    public static TowerRoomDTO create(int roomNum, CoordinateDTO coordinate,
        List<Double> tpCoordinate, List<List<Double>> mobCoordinates) {
        if (isNotValidRoomCoordinate(coordinate)) {
            ErrorViews.NOT_VALID_ROOM_COORDINATE.printWith(coordinate.getPos1(),
                coordinate.getPos2());
            throw new IllegalArgumentException();
        }
        String worldName = coordinate.getPos1().getWorld().getName();
        List<Double> pos1 = LocationConvertor.locationToList(coordinate.getPos1());
        List<Double> pos2 = LocationConvertor.locationToList(coordinate.getPos2());
        return new TowerRoomDTO(roomNum, worldName, pos1, pos2, tpCoordinate, mobCoordinates);
    }

    public static TowerRoomDTO create(int roomNum, CoordinateDTO coordinateDTO) {
        return create(roomNum, coordinateDTO, null, new ArrayList<>());
    }
}
