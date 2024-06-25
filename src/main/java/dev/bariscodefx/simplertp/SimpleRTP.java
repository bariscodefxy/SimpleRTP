package dev.bariscodefx.simplertp;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class SimpleRTP extends JavaPlugin {
    public MovementDisabler movementDisabler;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getConsoleSender().sendMessage("Starting up SimpleRTP!");
        this.movementDisabler = new MovementDisabler();
        getServer().getPluginManager().registerEvents(movementDisabler, this);

        File pluginFolder = getDataFolder();

        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs();
        }

        saveDefaultConfig();

        getCommand("rtp").setExecutor(new RTPCommand(this));
        getCommand("rtpReload").setExecutor(new Reload(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public String getPrefix() {
        return this.getConfig().getString("Chat Prefix");
    }
}
