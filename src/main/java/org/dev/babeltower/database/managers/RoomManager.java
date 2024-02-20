package org.dev.babeltower.database.managers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.dev.babeltower.database.MongoDBCollections;
import org.dev.babeltower.database.MongoDBManager;
import org.dev.babeltower.dto.TowerDTO;
import org.dev.babeltower.dto.RoomDTO;
import org.dev.babeltower.service.ClassExtractionService;
import org.dev.babeltower.service.DocumentConvertor;

@Getter
public class RoomManager {

    private static final List<String> sortStandard = List.of("num");
    private static RoomManager instance;
    private final List<RoomDTO> roomInfos;

    private RoomManager() {
        roomInfos = readRoomInfos();
    }

    public static synchronized RoomManager getInstance() {
        if (instance == null) {
            instance = new RoomManager();
        }
        return instance;
    }

    public static List<RoomDTO> readRoomInfos() {
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
                    // 예외 처리 로직 추가 (예: 로깅 등)
                    System.err.println("Error converting document to TowerDTO: " + e.getMessage());
                }
            }
        }
        return roomInfos;
    }
}
