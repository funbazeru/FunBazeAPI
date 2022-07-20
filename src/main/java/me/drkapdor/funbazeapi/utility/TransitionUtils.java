package me.drkapdor.funbazeapi.utility;

import me.drkapdor.funbazeapi.FunBazeApiPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TransitionUtils {
    public static void teleport(Player player, Location location) {
        if (!player.getPassengers().isEmpty()) {
            List<Entity> entities = new ArrayList<>(player.getPassengers());
            for (Entity entity : player.getPassengers())
                player.removePassenger(entity);
            player.teleport(location);
            Bukkit.getScheduler().runTaskLater(FunBazeApiPlugin.getInstance(), () -> entities.forEach(player::addPassenger), 5);
        } else player.teleport(location);
    }
}
