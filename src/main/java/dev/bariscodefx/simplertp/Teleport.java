package dev.bariscodefx.simplertp;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Teleport {
    private final SimpleRTP plugin;
    private int i = 0; //A fail-safe just incase there are no safe locations after a set number of tries
    private final Player player;
    private Location newLocation;
    private final Random random = new Random();
    private final Material[] list = {Material.LAVA, Material.WATER, Material.FIRE, Material.MAGMA_BLOCK, Material.AIR, Material.VOID_AIR};
    private final ArrayList<Material> unsafeMaterials = new ArrayList<>(List.of(list));

    public Teleport(Player player, SimpleRTP plugin) {
        this.player = player;
        this.plugin = plugin;
        getNewLocation();
    }

    private void getNewLocation() {
        if (!player.getWorld().getEnvironment().equals(World.Environment.NETHER) || player.getWorld().getEnvironment().equals(World.Environment.NORMAL) && !plugin.getConfig().getBoolean("Overworld") || player.getWorld().getEnvironment().equals(World.Environment.THE_END) && !plugin.getConfig().getBoolean("End") || !player.getWorld().getEnvironment().equals(World.Environment.CUSTOM)) {
            if (plugin.getConfig().getInt("RTP Range") > 0 && plugin.getConfig().getInt("RTP Range") < this.player.getWorld().getWorldBorder().getSize() / 2 - 1) {
                while (true) {
                    int x = random.nextInt(-plugin.getConfig().getInt("RTP Range"), plugin.getConfig().getInt("RTP Range"));
                    int z = random.nextInt(-plugin.getConfig().getInt("RTP Range"), plugin.getConfig().getInt("RTP Range"));
                    Block block = player.getWorld().getHighestBlockAt(x, z);
                    this.newLocation = new Location(this.player.getWorld(), x, this.player.getWorld().getHighestBlockYAt(x, z) + 1, z);
                    if (!unsafeMaterials.contains(block.getBlockData().getMaterial())) {
                        teleportPlayer();
                        break;
                    } else {
                        this.i += 1;
                        if (this.i == 25) {
                            this.i = 0;
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&cError: Unable to find a safe spot to teleport you!"));
                            break;
                        }
                    }
                }
            } else {
                while (true) {
                    int x = random.nextInt(-(int) this.player.getWorld().getWorldBorder().getSize() / 2 - 1, (int) this.player.getWorld().getWorldBorder().getSize() / 2 - 1);
                    int z = random.nextInt(-(int) this.player.getWorld().getWorldBorder().getSize() / 2 - 1, (int) this.player.getWorld().getWorldBorder().getSize() / 2 - 1);
                    Block block = player.getWorld().getHighestBlockAt(x, z);
                    this.newLocation = new Location(this.player.getWorld(), x, this.player.getWorld().getHighestBlockYAt(x, z) + 1, z);
                    if (!unsafeMaterials.contains(block.getBlockData().getMaterial())) {
                        teleportPlayer();
                        break;
                    } else {
                        this.i += 1;
                        if (this.i == 25) {
                            this.i = 0;
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&cError: Unable to find a safe spot to teleport you!"));
                            break;
                        }
                    }
                }
            }
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&cError: /rtp isn't usable in this dimension!"));
        }


    }

    private void teleportPlayer() {
        plugin.movementDisabler.disabledUsers.put(player.getUniqueId(), true);
        sendTitleWithDelay(player, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Teleporting Counter Color") + "3"), () -> {
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            sendTitleWithDelay(player, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Teleporting Counter Color") + "2"), () -> {
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                sendTitleWithDelay(player, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Teleporting Counter Color") + "1"), () -> {
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    sendTitleWithDelay(player, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Teleport Successful Subtitle")), () -> {
                        String locStr = newLocation.getX() + ", " + newLocation.getY() + ", " + newLocation.getZ();
                        player.teleport(newLocation);
                        player.playSound(newLocation, Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(plugin.getConfig().getString("Teleport Successful Message"), locStr)));
                        player.sendTitle("", locStr, 10, 40, 10);
                        plugin.movementDisabler.disabledUsers.put(player.getUniqueId(), false);
                    });
                });
            });
        });
    }

    private void sendTitleWithDelay(Player player, String subtitle, Runnable nextTask) {
        new BukkitRunnable() {
            @Override
            public void run() {
                nextTask.run();
                player.sendTitle(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Teleporting Title")), subtitle, 0, 20, 0);
                player.getWorld().spawnParticle(Particle.DRAGON_BREATH, player.getLocation().add(0, 1, 0), 100, 0.5, 0.5, 0.5, 0);
            }
        }.runTaskLater(plugin, 20);
    }
}