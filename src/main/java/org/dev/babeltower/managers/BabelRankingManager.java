package org.dev.babeltower.managers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.bukkit.scheduler.BukkitRunnable;
import org.dev.babeltower.BabelTower;
import org.dev.babeltower.dto.PlayerTowerDTO;
import org.dev.babeltower.utils.TikTimeUnit;
import org.dev.babeltower.views.ChatView;
import org.dev.babeltower.views.ErrorViews;

public class BabelRankingManager {

    public static final String TITLE = "BABEL RANK";

    public static final int RANKING_LIMIT = 10;
    private static BabelRankingManager instance;
    private LocalDateTime lastModifiedAt;
    private List<PlayerTowerDTO> ranking;
    private final BukkitRunnable updateTask;

    private BabelRankingManager() {
        update();
        updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                update();
            }
        };
    }

    public static synchronized BabelRankingManager getInstance() {
        if (instance == null) {
            instance = new BabelRankingManager();
        }
        return instance;
    }

    public void update() {
        lastModifiedAt = LocalDateTime.now();
        try {
            ranking = PlayerTowerManager.readTopPlayers(RANKING_LIMIT);
        } catch (ReflectiveOperationException e) {
            ErrorViews.FAIL_TO_READ_PLAYER.printWith();
            throw new RuntimeException(e);
        }
    }

    public void start() {
        updateTask.runTaskTimer(BabelTower.getInstance(),
            0, TikTimeUnit.MINUTES.toTik(5));
    }

    public void cancel() {
        updateTask.cancel();
    }

    public String readRanking() {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        sb.append(String.format(ChatView.BABEL_RANK_HEADER_FORMAT.getMessageFormat(), TITLE,
            lastModifiedAt.format(DateTimeFormatter.ofPattern("HH:mm")))).append("\n");

        for (PlayerTowerDTO player : ranking) {
            if(player.getLatestFloor() ==0 || player.getClearTime() == null){
                continue;
            }
            long clearTime = player.getClearTime();
            long minutes = TimeUnit.SECONDS.toMinutes(clearTime);
            clearTime -= TimeUnit.MINUTES.toSeconds(minutes);
            long seconds = clearTime;

            sb.append(String.format(ChatView.BABEL_RANK_FORMAT.getMessageFormat()
                    , i++, player.getNickname(), player.getLatestFloor(), minutes, seconds))
                .append("\n");
        }
        return sb.toString();
    }
}
