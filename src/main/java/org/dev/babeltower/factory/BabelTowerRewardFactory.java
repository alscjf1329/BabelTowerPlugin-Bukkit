package org.dev.babeltower.factory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class BabelTowerRewardFactory {

    public static final String REWARD_TITLE_FORMAT = "바벨탑 보상 - %d";


    private static BabelTowerRewardFactory instance;

    private BabelTowerRewardFactory() {
        instance = this;
    }

    public static synchronized BabelTowerRewardFactory getInstance() {
        if (instance == null) {
            instance = new BabelTowerRewardFactory();
        }
        return instance;
    }

    public Inventory createInventory(int floor) {
        return Bukkit.createInventory(null, 54, createTitle(floor));
    }

    public String createTitle(int floor) {
        return String.format(REWARD_TITLE_FORMAT, floor);
    }

    public int extractFloor(String title) {
        if (!isCreated(title)) {
            throw new IllegalArgumentException();
        }
        String[] split = title.split("-");
        return Integer.parseInt(split[1].trim());
    }

    public boolean isCreated(String title) {
        String pattern = "바벨탑 보상 - \\d+";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(title);

        return matcher.matches();
    }
}
