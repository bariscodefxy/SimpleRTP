package dev.bariscodefx.simplertp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Reload implements CommandExecutor {

    private final SimpleRTP plugin;

    public Reload(SimpleRTP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("SimpleRtp.reload")) {
                plugin.saveConfig();
                plugin.reloadConfig();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&a" + "config.yml has reloaded!"));
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&cError: You have no permission to perform this command!"));
            }

        } else {
            plugin.saveConfig();
            plugin.reloadConfig();
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&a" + "config.yml has reloaded!"));
        }
        return true;
    }
}
