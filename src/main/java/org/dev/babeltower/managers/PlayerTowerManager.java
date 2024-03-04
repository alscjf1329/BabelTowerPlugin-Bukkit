package org.dev.babeltower.managers;

import com.google.gson.Gson;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.entity.Player;
import org.dev.babeltower.database.MongoDBCollections;
import org.dev.babeltower.database.MongoDBManager;
import org.dev.babeltower.dto.PlayerTowerDTO;
import org.dev.babeltower.dto.RaidResultDTO;
import org.dev.babeltower.utils.ClassExtractionService;
import org.dev.babeltower.utils.DocumentConvertor;
import org.dev.babeltower.views.ErrorViews;
import org.jetbrains.annotations.NotNull;

public class PlayerTowerManager {
    private static final Map<String, List<String>> sortStandard =
        Map.of("ascending", List.of("clearTime"),
            "descending", List.of("latestFloor"));

    public static MongoCollection<Document> getCollection() {
        return MongoDBManager.getInstance().getRPGSharpDB()
            .getCollection(MongoDBCollections.PLAYER.getName());
    }

    public static int readLatestFloor(Player player) {
        try {
            PlayerTowerDTO playerInfo = savePlayerInfo(player);
            return playerInfo.getLatestFloor();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }


    public static PlayerTowerDTO findPlayerInfo(String nickname)
        throws ReflectiveOperationException {

        Bson projectionFields = Projections.fields(
            Projections.include(ClassExtractionService.extractFieldNames(PlayerTowerDTO.class)),
            Projections.excludeId());
        Bson nicknameFilter = Filters.eq("nickname", nickname);

        Document document = getCollection().find()
            .projection(projectionFields)
            .filter(nicknameFilter)
            .first();

        if (document == null) {
            return null;
        }

        try {
            return DocumentConvertor.convertTo(document, PlayerTowerDTO.class);
        } catch (ReflectiveOperationException e) {
            ErrorViews.CASTING_FAIL.printWith(PlayerTowerDTO.class.getName());
            throw e;
        }
    }

    public static List<PlayerTowerDTO> readTopPlayers(int limit)
        throws ReflectiveOperationException {

        Bson projectionFields = Projections.fields(
            Projections.include(ClassExtractionService.extractFieldNames(PlayerTowerDTO.class)),
            Projections.excludeId());

        FindIterable<Document> documents = getCollection().find()
            .projection(projectionFields)
            .sort(Sorts.descending(sortStandard.get("descending")))
            .sort(Sorts.ascending(sortStandard.get("ascending")))
            .limit(limit);

        List<PlayerTowerDTO> results = new ArrayList<>();
        for (Document document : documents) {
            try {
                results.add(DocumentConvertor.convertTo(document, PlayerTowerDTO.class));
            } catch (ReflectiveOperationException e) {
                ErrorViews.CASTING_FAIL.printWith(PlayerTowerDTO.class.getName());
                throw e;
            }
        }
        return results;
    }


    /**
     * player 정보 존재한다면 읽어오고 존재하지 않는다면 기본 player를 생성후 player 정보를 반환
     */

    public static @NotNull PlayerTowerDTO savePlayerInfo(Player player)
        throws ReflectiveOperationException {
        PlayerTowerDTO playerTower = findPlayerInfo(player.getName());

        if (playerTower == null) {
            PlayerTowerDTO defaultPlayerTower = PlayerTowerDTO.createDefault(player);
            Gson gson = new Gson();
            getCollection().insertOne(Document.parse(gson.toJson(defaultPlayerTower)));
            return defaultPlayerTower;
        }
        return playerTower;
    }

    public static PlayerTowerDTO updateRaidResult(PlayerTowerDTO playerTowerDTO,
        RaidResultDTO raidResult) {
        if(raidResult.getRaid().getTower().getFloor() < playerTowerDTO.getLatestFloor()){
            return null;
        }
        PlayerTowerDTO appliedPlayerInfo = playerTowerDTO.applyRaidResult(raidResult);

        Document filterQuery = new Document().append(playerTowerDTO.getKeyFieldName(),
            playerTowerDTO.getNickname());
        Bson updates = Updates.combine(
            Updates.set("latestFloor", appliedPlayerInfo.getLatestFloor()),
            Updates.set("clearTime", appliedPlayerInfo.getClearTime()),
            Updates.set("recentFail", appliedPlayerInfo.getRecentFail())
        );
        try {
            getCollection().updateOne(filterQuery, updates);
        } catch (MongoException me) {
            ErrorViews.UPDATE_ROOM_ERROR.printWith();
            throw me;
        }
        return appliedPlayerInfo;
    }
}
