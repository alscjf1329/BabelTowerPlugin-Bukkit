package org.dev.babeltower;

import io.lumine.mythic.bukkit.BukkitAPIHelper;
import java.util.Objects;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.dev.babeltower.command.CoordinateCommand;
import org.dev.babeltower.command.RaidCommand;
import org.dev.babeltower.database.MongoDBManager;
import org.dev.babeltower.event.handler.RaidIsOverEventHandler;
import org.dev.babeltower.event.handler.SetPositionEventHandler;


public final class BabelTower extends JavaPlugin {

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
        registerEvents();
    }

    @Override
    public void onDisable() {
        MongoDBManager.getInstance().disconnect();
    }


    private void registerCommand() {
        Objects.requireNonNull(this.getCommand("바벨탑")).setExecutor(new RaidCommand());
        Objects.requireNonNull(this.getCommand("좌표")).setExecutor(new CoordinateCommand());
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new RaidIsOverEventHandler(), this);
        Bukkit.getPluginManager().registerEvents(new SetPositionEventHandler(), this);
    }
}
