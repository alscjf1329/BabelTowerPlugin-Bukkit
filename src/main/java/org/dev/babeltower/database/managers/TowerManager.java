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
import org.dev.babeltower.service.ClassExtractionService;
import org.dev.babeltower.service.DocumentConvertor;

@Getter
public class TowerManager {

    private static final List<String> sortStandard = List.of("floor");
    private static TowerManager instance;
    private final List<TowerDTO> towerInfos;

    private TowerManager() {
        towerInfos = readTowerInfos();
    }

    public static synchronized TowerManager getInstance() {
        if (instance == null) {
            instance = new TowerManager();
        }
        return instance;
    }

    public static List<TowerDTO> readTowerInfos() {
        MongoCollection<Document> towerCollection = MongoDBManager.getInstance().getRPGSharpDB()
            .getCollection(MongoDBCollections.TOWER.getName());

        Bson projectionFields = Projections.fields(
            Projections.include(ClassExtractionService.extractFieldNames(TowerDTO.class)),
            Projections.excludeId());

        List<TowerDTO> towerInfos = new ArrayList<>();
        try (MongoCursor<Document> cursor = towerCollection.find()
            .projection(projectionFields)
            .sort(Sorts.descending(sortStandard)).iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                try {
                    TowerDTO towerInfo = DocumentConvertor.convert(document, TowerDTO.class);
                    towerInfos.add(towerInfo);
                } catch (ReflectiveOperationException e) {
                    // 예외 처리 로직 추가 (예: 로깅 등)
                    System.err.println("Error converting document to TowerDTO: " + e.getMessage());
                }
            }
        }
        return towerInfos;
    }
}
