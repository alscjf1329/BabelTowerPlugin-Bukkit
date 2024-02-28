package org.dev.babeltower.managers;

import com.hj.rpgsharp.rpg.apis.rpgsharp.utils.Serializer;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import java.util.List;
import java.util.TreeMap;
import lombok.Getter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.inventory.Inventory;
import org.dev.babeltower.database.MongoDBCollections;
import org.dev.babeltower.database.MongoDBManager;
import org.dev.babeltower.dto.TowerDTO;
import org.dev.babeltower.factory.BabelTowerRewardFactory;
import org.dev.babeltower.service.ClassExtractionService;
import org.dev.babeltower.service.DocumentConvertor;
import org.dev.babeltower.views.ErrorViews;

@Getter
public class TowerManager {

    private static final List<String> sortStandard = List.of("floor");
    private static TowerManager instance;
    private final TreeMap<Integer, TowerDTO> towerInfos;

    private TowerManager() {
        towerInfos = readTowerInfos();
    }

    public static synchronized TowerManager getInstance() {
        if (instance == null) {
            instance = new TowerManager();
        }
        return instance;
    }

    private MongoCollection<Document> getCollection() {
        return MongoDBManager.getInstance().getRPGSharpDB()
            .getCollection(MongoDBCollections.TOWER.getName());
    }

    private TreeMap<Integer, TowerDTO> readTowerInfos() {
        Bson projectionFields = Projections.fields(
            Projections.include(ClassExtractionService.extractFieldNames(TowerDTO.class)),
            Projections.excludeId());

        TreeMap<Integer, TowerDTO> towerInfos = new TreeMap<>();
        try (MongoCursor<Document> cursor = getCollection().find()
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
        return towerInfos.lastKey();
    }

    public synchronized TowerDTO updateReward(int floor, Inventory inventory) {
        TowerDTO towerInfo = findTowerInfo(floor);
        if (towerInfo == null) {
            ErrorViews.NOT_VALID_TOWER_FLOOR.printWith(floor);
            return null;
        }
        String title = BabelTowerRewardFactory.getInstance().createTitle(floor);
        String serializedInventory = Serializer.serializeInventory(inventory, title);
        towerInfo.setSerializedReward(serializedInventory);

        Document fillerQuery = new Document().append(TowerDTO.getKeyFieldName(),
            towerInfo.getKeyFieldValue());
        Bson updates = Updates.combine(
            Updates.set("serializedReward", serializedInventory));
        try {
            getCollection().updateOne(fillerQuery, updates);
            towerInfos.put(floor, towerInfo);
        } catch (MongoException me) {
            ErrorViews.UPDATE_ROOM_ERROR.printWith();
            throw me;
        }
        return towerInfo;
    }
}
