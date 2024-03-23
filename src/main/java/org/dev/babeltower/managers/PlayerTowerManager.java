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
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.entity.Player;
import org.dev.babeltower.database.MongoDBCollections;
import org.dev.babeltower.database.MongoDBManager;
import org.dev.babeltower.dto.BabelTowerRaidResultDTO;
import org.dev.babeltower.dto.PlayerTowerDTO;
import org.dev.babeltower.utils.ClassExtractionService;
import org.dev.babeltower.utils.DocumentConvertor;
import org.dev.babeltower.views.ErrorViews;
import org.jetbrains.annotations.NotNull;

public class PlayerTowerManager {

    private static final Bson sortStandard = Sorts.orderBy(Sorts.descending("latestFloor"),
        Sorts.ascending("clearTime"));

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


    public static PlayerTowerDTO findPlayerInfoByNickname(String nickname)
        throws ReflectiveOperationException {

        Bson projectionFields = Projections.fields(
            Projections.include(ClassExtractionService.extractFieldNames(PlayerTowerDTO.class)),
            Projections.excludeId());
        Bson nicknameFilter = Filters.eq("nickname", nickname);

        Document document = getCollection().find()
            .projection(projectionFields)
            .filter(nicknameFilter)
            .sort(sortStandard)
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

    public static PlayerTowerDTO findPlayerInfoByUuid(String uuid)
        throws ReflectiveOperationException {

        Bson projectionFields = Projections.fields(
            Projections.include(ClassExtractionService.extractFieldNames(PlayerTowerDTO.class)),
            Projections.excludeId());
        Bson nicknameFilter = Filters.eq("uuid", uuid);

        Document document = getCollection().find()
            .projection(projectionFields)
            .filter(nicknameFilter)
            .sort(sortStandard)
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
            .sort(sortStandard)
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
        PlayerTowerDTO playerTower = findPlayerInfoByUuid(player.getUniqueId().toString());
        if (playerTower == null) {
            PlayerTowerDTO defaultPlayerTower = PlayerTowerDTO.createDefault(player);
            Gson gson = new Gson();
            getCollection().insertOne(Document.parse(gson.toJson(defaultPlayerTower)));
            return defaultPlayerTower;
        }

        if (!player.getName().equals(playerTower.getNickname())) {
            updateNickname(player);
        }
        return playerTower;
    }

    public static PlayerTowerDTO updateRaidResult(PlayerTowerDTO playerTowerDTO,
        BabelTowerRaidResultDTO raidResult) {
        if (raidResult.getRaid().getTower().getFloor() < playerTowerDTO.getLatestFloor()) {
            return null;
        }
        PlayerTowerDTO appliedPlayerInfo = playerTowerDTO.applyRaidResult(raidResult);

        Document filterQuery = new Document().append(playerTowerDTO.getKeyFieldName(),
            playerTowerDTO.getUuid().toString());
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

    public static void updateNickname(Player player) {
        PlayerTowerDTO playerInfo;
        try {
            playerInfo = findPlayerInfoByUuid(player.getUniqueId().toString());
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
        if (playerInfo == null) {
            return;
        }
        Document filterQuery = new Document().append(playerInfo.getKeyFieldName(),
            player.getUniqueId().toString());

        Bson updates = Updates.combine(
            Updates.set("nickname", player.getName())
        );
        try {
            getCollection().updateOne(filterQuery, updates);
        } catch (MongoException me) {
            ErrorViews.UPDATE_ROOM_ERROR.printWith();
            throw me;
        }
    }
}
