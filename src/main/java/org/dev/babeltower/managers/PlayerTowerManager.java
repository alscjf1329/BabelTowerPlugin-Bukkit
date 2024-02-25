package org.dev.babeltower.managers;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.entity.Player;
import org.dev.babeltower.database.MongoDBCollections;
import org.dev.babeltower.database.MongoDBManager;
import org.dev.babeltower.dto.PlayerTowerDTO;
import org.dev.babeltower.dto.RaidResultDTO;
import org.dev.babeltower.service.ClassExtractionService;
import org.dev.babeltower.service.DocumentConvertor;
import org.dev.babeltower.views.ErrorViews;
import org.jetbrains.annotations.NotNull;

public class PlayerTowerManager {

    private static final List<String> sortStandard = List.of("latestFloor", "clearTime");

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


    public static PlayerTowerDTO findPlayerInfo(Player player)
        throws ReflectiveOperationException {

        Bson projectionFields = Projections.fields(
            Projections.include(ClassExtractionService.extractFieldNames(PlayerTowerDTO.class)),
            Projections.excludeId());
        Bson nicknameFilter = Filters.eq("nickname", player.getName());

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


    /**
     * player 정보 존재한다면 읽어오고
     * 존재하지 않는다면 기본 player를 생성후 player 정보를 반환
     */

    public static @NotNull PlayerTowerDTO savePlayerInfo(Player player)
        throws ReflectiveOperationException {
        PlayerTowerDTO playerTower = findPlayerInfo(player);

        if (playerTower == null) {
            PlayerTowerDTO defaultPlayerTower = PlayerTowerDTO.createDefault(player);
            Gson gson = new Gson();
            getCollection().insertOne(Document.parse(gson.toJson(defaultPlayerTower)));
            return defaultPlayerTower;
        }
        return playerTower;
    }

//    public static PlayerTowerDTO updateRaidResultToPlayer(PlayerTowerDTO playerTowerDTO,
//        RaidResultDTO raidResult) {
//        PlayerTowerDTO appliedPlayerInfo = playerTowerDTO.applyRaid(raidResult);
//        updatePlayerTower(appliedPlayerInfo);
//        return appliedPlayerInfo;
//    }

//    private static void updatePlayerTower(PlayerTowerDTO playerTowerDTO) {
//        Document filterQuery = new Document().append(playerTowerDTO.getKey().getName(),
//            playerTowerDTO.getNickname());
//        Gson gson = new Gson();
//        Bson updates = Updates.combine();
//        List<Bson> updateOptions = new ArrayList<>();
//
//        for (Field field : playerTowerDTO.getClass().getDeclaredFields()) {
//            field.setAccessible(true);
//            Object value;
//            try {
//                value = field.get(playerTowerDTO);
//            } catch (IllegalAccessException e) {
//                ErrorViews.NO_SUCH_FIELD.printWith("updatePlayerTower");
//                throw new RuntimeException(e);
//            }
//            updateOptions.add(Updates.set(field.getName(), value));
//        }
//    }

}
