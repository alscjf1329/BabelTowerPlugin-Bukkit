package org.dev.babeltower.managers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.dev.babeltower.database.MongoDBCollections;
import org.dev.babeltower.database.MongoDBManager;
import org.dev.babeltower.dto.TowerDTO;
import org.dev.babeltower.service.ClassExtractionService;
import org.dev.babeltower.service.DocumentConvertor;
import org.dev.babeltower.views.ErrorViews;

@Getter
public class TowerManager {

    private static final List<String> sortStandard = List.of("floor");
    private static TowerManager instance;
    private final Map<Integer, TowerDTO> towerInfos;

    private TowerManager() {
        towerInfos = readTowerInfos();
    }

    public static synchronized TowerManager getInstance() {
        if (instance == null) {
            instance = new TowerManager();
        }
        return instance;
    }

    private static Map<Integer, TowerDTO> readTowerInfos() {
        MongoCollection<Document> towerCollection = MongoDBManager.getInstance().getRPGSharpDB()
            .getCollection(MongoDBCollections.TOWER.getName());

        Bson projectionFields = Projections.fields(
            Projections.include(ClassExtractionService.extractFieldNames(TowerDTO.class)),
            Projections.excludeId());

        Map<Integer, TowerDTO> towerInfos = new HashMap<>();
        try (MongoCursor<Document> cursor = towerCollection.find()
            .projection(projectionFields)
            .sort(Sorts.descending(sortStandard)).iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                try {
                    TowerDTO towerInfo = DocumentConvertor.convertTo(document, TowerDTO.class);
                    towerInfos.put(towerInfo.getFloor(), towerInfo);
                } catch (ReflectiveOperationException e) {
                    ErrorViews.CASTING_FAIL.printWith(TowerDTO.class.getName());
                }
            }
        }
        return towerInfos;
    }

    public TowerDTO findTowerInfo(int floor) {
        return towerInfos.get(floor);
    }

    public int findMaxFloor() {
        return towerInfos.size();
    }
}
