package org.dev.babeltower;

import io.lumine.mythic.bukkit.BukkitAPIHelper;
import java.util.Objects;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.dev.babeltower.command.AdminCommand;
import org.dev.babeltower.command.UserCommand;
import org.dev.babeltower.database.MongoDBManager;
import org.dev.babeltower.event.handler.InventoryCloseEventHandler;
import org.dev.babeltower.event.handler.RaidIsOverEventHandler;
import org.dev.babeltower.event.handler.RaidMobDeathEventHandler;
import org.dev.babeltower.event.handler.RaidUserDeathEventHandler;
import org.dev.babeltower.event.handler.RaidUserQuitEventHandler;
import org.dev.babeltower.event.handler.SetPositionEventHandler;
import org.dev.babeltower.managers.BabelRankingManager;


public final class BabelTower extends JavaPlugin {

    public static final String NAME = "BabelTower";

    @Getter
    private static JavaPlugin instance;
    @Getter
    private static BukkitAPIHelper bukkitAPIHelper;

    @Override
    public void onEnable() {
        bukkitAPIHelper = new BukkitAPIHelper();
        instance = this;
        // resources/config.yml 형식으로 config file 만들어지게 설정
        saveResource("config.yml", false);
        saveConfig();
        MongoDBManager.getInstance().connect();
        registerCommand();
        registerEventHandler();
        BabelRankingManager.getInstance().start();
    }

    @Override
    public void onDisable() {
        MongoDBManager.getInstance().disconnect();
        BabelRankingManager.getInstance().cancel();
    }


    private void registerCommand() {
        Objects.requireNonNull(this.getCommand(AdminCommand.COMMAND))
            .setExecutor(new AdminCommand());
        Objects.requireNonNull(this.getCommand(UserCommand.COMMAND)).setExecutor(new UserCommand());
    }

    private void registerEventHandler() {
        Bukkit.getPluginManager().registerEvents(new RaidIsOverEventHandler(), this);
        Bukkit.getPluginManager().registerEvents(new SetPositionEventHandler(), this);
        Bukkit.getPluginManager().registerEvents(new RaidMobDeathEventHandler(), this);
        Bukkit.getPluginManager().registerEvents(new RaidUserQuitEventHandler(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryCloseEventHandler(), this);
        Bukkit.getPluginManager().registerEvents(new RaidUserDeathEventHandler(), this);
    }
}
