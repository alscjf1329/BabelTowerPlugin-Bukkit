package org.dev.babeltower.dto;

import io.lumine.mythic.api.exceptions.InvalidMobTypeException;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.dev.babeltower.BabelTower;
import org.dev.babeltower.config.ConfigOptions;
import org.dev.babeltower.event.RaidIsOverEvent;
import org.dev.babeltower.managers.TowerManager;
import org.dev.babeltower.service.LocationConvertor;
import org.dev.babeltower.service.TikTimeUnit;
import org.dev.babeltower.views.ErrorViews;

public class Raid implements Listener {

    public static int WAITING_TIME = 5;

    private final Raid instance;
    @Getter
    private final TowerDTO tower;
    private final TowerRoomDTO towerRoom;
    @Getter
    private final PlayerTowerDTO playerTower;
    @Getter
    private final Map<String, Entity> mobs;
    @Getter
    private long startTime;
    private BossBar timerBar;
    private BukkitTask raidTimerTask;

    private final long timeLimit;
    @Getter
    private long remainedSeconds;

    public Raid(int floor, TowerRoomDTO towerRoom, PlayerTowerDTO playerTower) {
        this.instance = this;
        this.tower = TowerManager.getInstance().findTowerInfo(floor);
        this.towerRoom = towerRoom;
        this.playerTower = playerTower;
        this.timeLimit = tower.getTimeLimit(); //초
        this.remainedSeconds = timeLimit;
        this.mobs = new HashMap<>();
    }

    public void start() {
        startTimerBar();
        spawnMobs(this.towerRoom, this.tower.getMobs());
    }

    private void startTimerBar() {
        //todo timerBarManager 만들어서 따로 관리해보기
        timerBar = Bukkit.createBossBar("Raid Timer", BarColor.YELLOW, BarStyle.SEGMENTED_10);

        if (this.raidTimerTask == null) {
            this.raidTimerTask = new BukkitRunnable() {
                @Override
                public void run() {
                    startTime = System.currentTimeMillis();
                    if ((remainedSeconds -= 1) == 0) {
                        stopTimerBar();
                        long currentTimeMillis = System.currentTimeMillis();
                        RaidResultDTO failedRaidResult = RaidResultDTO.createFailedRaidResultDTO(
                            instance, currentTimeMillis);
                        Bukkit.getServer().getPluginManager()
                            .callEvent(new RaidIsOverEvent(failedRaidResult));
                    } else {
                        timerBar.setTitle("남은 시간: " + remainedSeconds + "초");
                        timerBar.setProgress((double) remainedSeconds / timeLimit);
                    }
                }
            }.runTaskTimer(BabelTower.getInstance(),
                TikTimeUnit.SECONDS.toTik(WAITING_TIME),
                TikTimeUnit.SECONDS.getTikCount());
        }
        timerBar.setVisible(true);
        timerBar.addPlayer(Objects.requireNonNull(Bukkit.getPlayer(playerTower.getNickname())));
    }

    public void stopTimerBar() {
        raidTimerTask.cancel();
        timerBar.removeAll();
    }

    public void clear() {
        // 타이머 끄기
        stopTimerBar();
        // 해당 레이드 몹 삭제
        for (Entity mob : mobs.values()) {
            mob.remove();
        }
        // 마을로 복귀
        Player player = Bukkit.getPlayer(playerTower.getNickname());
        String returnWorld = BabelTower.getInstance().getConfig()
            .getString(ConfigOptions.RETURN_WORLD.getName());
        List<Double> returnLocation = BabelTower.getInstance().getConfig()
            .getDoubleList(ConfigOptions.RETURN_COORDINATE.getName());
        Objects.requireNonNull(player)
            .teleport(LocationConvertor.listToLocation(returnWorld, returnLocation));
    }

    private void spawnMobs(TowerRoomDTO towerRoom, List<String> mobNames) {
        List<List<Double>> mobCoordinates = towerRoom.getMobCoordinate();
        Random random = new Random();

        for (String mobName : mobNames) {
            int randomNum = random.nextInt(mobCoordinates.size());
            List<Double> mobCoordinate = mobCoordinates.get(randomNum);
            try {
                Entity entity = BabelTower.getBukkitAPIHelper()
                    .spawnMythicMob(mobName, LocationConvertor.listToLocation(
                        towerRoom.getWorldName(), mobCoordinate));
                this.mobs.put(mobName, entity);
            } catch (InvalidMobTypeException e) {
                ErrorViews.NO_SUCH_MOB_TYPE.printWith(mobName);
                throw new RuntimeException(e);
            }
        }
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Raid Information:\n");
        sb.append(" - Nickname: ").append(playerTower.getNickname()).append("\n");
        sb.append(" - Remaining Time: ").append(remainedSeconds).append(" seconds\n");
        sb.append(" - Remaining Mobs:\n");

        for (Map.Entry<String, Entity> entry : mobs.entrySet()) {
            sb.append("    - Mob Name: ").append(entry.getKey()).append("\n");
        }

        return sb.toString();
    }
}
