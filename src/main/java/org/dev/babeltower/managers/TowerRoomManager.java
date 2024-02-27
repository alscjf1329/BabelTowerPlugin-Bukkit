package org.dev.babeltower.managers;

import com.google.gson.Gson;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import javax.annotation.Nullable;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.entity.Player;
import org.dev.babeltower.database.MongoDBCollections;
import org.dev.babeltower.database.MongoDBManager;
import org.dev.babeltower.dto.TowerRoomDTO;
import org.dev.babeltower.service.ClassExtractionService;
import org.dev.babeltower.service.DocumentConvertor;
import org.dev.babeltower.views.ErrorViews;

public class TowerRoomManager {

    private static final List<String> sortStandard = List.of("num");
    private static TowerRoomManager instance;
    private final TreeMap<TowerRoomDTO, Player> roomInfos;

    private TowerRoomManager() {
        List<TowerRoomDTO> rooms = readRoomInfos();
        roomInfos = new TreeMap<>(Comparator.comparingInt(TowerRoomDTO::getNum));
        for (TowerRoomDTO room : rooms) {
            roomInfos.put(room, null);
        }
    }

    public static synchronized TowerRoomManager getInstance() {
        if (instance == null) {
            instance = new TowerRoomManager();
        }
        return instance;
    }


    public Map<TowerRoomDTO, Player> findAll() {
        return new TreeMap<>(roomInfos);
    }

    public TowerRoomDTO findAvailableRoom() {
        for (Map.Entry<TowerRoomDTO, Player> entry : roomInfos.entrySet()) {
            TowerRoomDTO room = entry.getKey();
            Player player = entry.getValue();
            if (player == null && room.isValid()) {
                return room;
            }
        }
        return null;
    }

    public @Nullable TowerRoomDTO matchPlayer(Player player) {
        TowerRoomDTO room = findAvailableRoom();
        if (room == null) {
            return null;
        }
        roomInfos.put(room, player);
        return room;
    }

    private List<TowerRoomDTO> readRoomInfos() {
        Bson projectionFields = Projections.fields(
            Projections.include(ClassExtractionService.extractFieldNames(TowerRoomDTO.class)),
            Projections.excludeId());

        List<TowerRoomDTO> roomInfos = new ArrayList<>();
        try (MongoCursor<Document> cursor = getCollection().find()
            .projection(projectionFields)
            .sort(Sorts.descending(sortStandard)).iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                try {
                    TowerRoomDTO roomInfo = DocumentConvertor.convertTo(document,
                        TowerRoomDTO.class);
                    roomInfos.add(roomInfo);
                } catch (ReflectiveOperationException e) {
                    ErrorViews.CASTING_FAIL.printWith(TowerRoomDTO.class.getName());
                }
            }
        }
        return roomInfos;
    }

    public TowerRoomDTO findBy(int roomNum) {
        for (TowerRoomDTO towerRoomDTO : roomInfos.keySet()) {
            if (roomNum == towerRoomDTO.getNum()) {
                return towerRoomDTO;
            }
        }
        return null;
    }

    public boolean validateDuplicatedRoom(TowerRoomDTO towerRoomDTO) {
        TowerRoomDTO duplicatedTarget = findBy(towerRoomDTO.getNum());
        if (duplicatedTarget != null) {
            ErrorViews.DUPLICATED_ROOM.printWith(towerRoomDTO, duplicatedTarget);
            return true;
        }
        return false;
    }

    public MongoCollection<Document> getCollection() {
        return MongoDBManager.getInstance().getRPGSharpDB()
            .getCollection(MongoDBCollections.ROOM.getName());
    }

    public synchronized boolean registerTowerRoom(TowerRoomDTO towerRoom) {
        if (validateDuplicatedRoom(towerRoom)) {
            return false;
        }
        Gson gson = new Gson();
        getCollection().insertOne(Document.parse(gson.toJson(towerRoom)));
        roomInfos.put(towerRoom, null);
        return true;
    }

    public synchronized boolean unregisterTowerRoom(TowerRoomDTO towerRoom) {
        Bson filterQuery = Filters.eq(TowerRoomDTO.getKeyFieldName(), towerRoom.getKeyFieldValue());
        getCollection().deleteOne(filterQuery);
        roomInfos.remove(towerRoom);
        return true;
    }

    public synchronized void unregister(Player player) {
        for (Entry<TowerRoomDTO, Player> entry : roomInfos.entrySet()) {
            if (entry.getValue() == player) {
                roomInfos.put(entry.getKey(), null);
                return;
            }
        }
    }

    public boolean validateEmptyIn(int roomNum) {
        TowerRoomDTO towerRoom = findBy(roomNum);
        Player player = roomInfos.get(towerRoom);
        return player == null;
    }

    public synchronized TowerRoomDTO updateTowerRoom(Player player, TowerRoomDTO towerRoom) {
        if (!validateEmptyIn(towerRoom.getNum())) {
            return null;
        }
        roomInfos.put(towerRoom, player);
        Document fillerQuery = new Document().append(TowerRoomDTO.getKeyFieldName(),
            towerRoom.getKeyFieldValue());

        Bson updates = Updates.combine(
            Updates.set("tpCoordinate", towerRoom.getTpCoordinate()),
            Updates.set("mobCoordinate", towerRoom.getMobCoordinate()));

        try {
            getCollection().updateOne(fillerQuery, updates);
            roomInfos.put(towerRoom, null);
        } catch (MongoException me) {
            ErrorViews.UPDATE_ROOM_ERROR.printWith();
            throw me;
        }
        return towerRoom;
    }
}
