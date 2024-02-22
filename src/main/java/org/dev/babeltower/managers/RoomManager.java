package org.dev.babeltower.managers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.entity.Player;
import org.dev.babeltower.database.MongoDBCollections;
import org.dev.babeltower.database.MongoDBManager;
import org.dev.babeltower.dto.RoomDTO;
import org.dev.babeltower.service.ClassExtractionService;
import org.dev.babeltower.service.DocumentConvertor;
import org.dev.babeltower.views.ChatView;
import org.dev.babeltower.views.ErrorViews;

public class RoomManager {

    private static final List<String> sortStandard = List.of("num");
    private static RoomManager instance;
    private final Map<RoomDTO, Player> roomInfos;

    private RoomManager() {
        List<RoomDTO> rooms = readRoomInfos();
        roomInfos = rooms.stream().collect(
            Collectors.toMap(Function.identity(), null)
        );
    }

    public static synchronized RoomManager getInstance() {
        if (instance == null) {
            instance = new RoomManager();
        }
        return instance;
    }

    public RoomDTO findAvailableRoom() {
        for (Map.Entry<RoomDTO, Player> entry : roomInfos.entrySet()) {
            RoomDTO room = entry.getKey();
            Player player = entry.getValue();
            if (player == null) {
                return room;
            }
        }
        return null;
    }

    public RoomDTO matchPlayer(Player player) {
        RoomDTO room = findAvailableRoom();
        if (room == null) {
            player.sendMessage(ChatView.ROOM_METCHING_FAIL.getMessage());
            return null;
        }
        roomInfos.put(room, player);
        return room;
    }

    private static List<RoomDTO> readRoomInfos() {
        MongoCollection<Document> roomCollection = MongoDBManager.getInstance().getRPGSharpDB()
            .getCollection(MongoDBCollections.ROOM.getName());

        Bson projectionFields = Projections.fields(
            Projections.include(ClassExtractionService.extractFieldNames(RoomDTO.class)),
            Projections.excludeId());

        List<RoomDTO> roomInfos = new ArrayList<>();
        try (MongoCursor<Document> cursor = roomCollection.find()
            .projection(projectionFields)
            .sort(Sorts.descending(sortStandard)).iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                try {
                    RoomDTO roomInfo = DocumentConvertor.convert(document, RoomDTO.class);
                    roomInfos.add(roomInfo);
                } catch (ReflectiveOperationException e) {
                    ErrorViews.CASTING_FAIL.printWith(RoomDTO.class.getName());
                }
            }
        }
        return roomInfos;
    }
}
