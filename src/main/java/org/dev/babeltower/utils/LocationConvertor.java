package org.dev.babeltower.utils;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationConvertor {

    public static Location listToLocation(String worldName,List<Double> list) {
        Double x = list.get(0);
        Double y = list.get(1);
        Double z = list.get(2);
        World world = Bukkit.getWorld(worldName);
        return new Location(world, x, y, z);
    }

    public static List<Double> locationToList(Location location) {
        return Arrays.asList(location.getX(), location.getY(), location.getZ());
    }
}
