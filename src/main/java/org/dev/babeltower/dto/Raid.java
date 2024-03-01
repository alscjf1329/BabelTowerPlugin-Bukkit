package org.dev.babeltower.dto;

import io.lumine.mythic.api.exceptions.InvalidMobTypeException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.dev.babeltower.BabelTower;
import org.dev.babeltower.config.ConfigOptions;
import org.dev.babeltower.event.RaidIsOverEvent;
import org.dev.babeltower.managers.TowerManager;
import org.dev.babeltower.utils.LocationConvertor;
import org.dev.babeltower.utils.TikTimeUnit;
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
    private final List<Entity> mobs;
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
        this.mobs = new ArrayList<>();
        this.remainedSeconds = timeLimit;
    }

    public void start() {
        startRaidTimerBar();
        spawnMobs(this.towerRoom, this.tower.getMobs());
    }

    public void removeMob(Entity entity) {
        mobs.remove(entity);
    }

    public boolean isAllMobDead() {
        return mobs.isEmpty();
    }

    private void startRaidTimerBar() {
        timerBar = Bukkit.createBossBar("Raid Timer", BarColor.YELLOW, BarStyle.SEGMENTED_10);
        if (this.raidTimerTask == null) {
            this.startTime = System.currentTimeMillis();

            this.raidTimerTask = new BukkitRunnable() {
                int waitingSeconds = WAITING_TIME;

                @Override
                public void run() {
                    if ((waitingSeconds -= 1) != 0) {
                        timerBar.setTitle("남은 대기시간: " + waitingSeconds + "초");
                        timerBar.setProgress((double) waitingSeconds / WAITING_TIME);
                    } else if ((remainedSeconds -= 1) != 0) {
                        timerBar.setTitle("남은 시간: " + remainedSeconds + "초");
                        timerBar.setProgress((double) remainedSeconds / timeLimit);
                    } else {
                        stopTimerBar();
                        long currentTimeMillis = System.currentTimeMillis();
                        RaidResultDTO failedRaidResult = RaidResultDTO.createFailedRaidResultDTO(
                            instance, currentTimeMillis);
                        Bukkit.getServer().getPluginManager()
                            .callEvent(new RaidIsOverEvent(failedRaidResult));
                    }
                }
            }.runTaskTimer(BabelTower.getInstance(),
                0, TikTimeUnit.SECONDS.getTikCount());
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
        towerRoom.clearEntitiesExcludePlayer();
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
                this.mobs.add(entity);
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

        for (Entity entity : mobs) {
            sb.append("    - Mob Name: ").append(entity.getName()).append("\n");
        }

        return sb.toString();
    }
}
