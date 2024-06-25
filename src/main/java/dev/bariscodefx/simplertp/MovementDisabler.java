package dev.bariscodefx.simplertp;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.UUID;

public class MovementDisabler implements Listener {
    public HashMap<UUID, Boolean> disabledUsers;

    MovementDisabler() {
        disabledUsers = new HashMap<>();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (disabledUsers.containsKey(event.getPlayer().getUniqueId()) && disabledUsers.get(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
