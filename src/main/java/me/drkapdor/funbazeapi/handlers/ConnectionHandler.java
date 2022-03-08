package me.drkapdor.funbazeapi.handlers;

import me.drkapdor.funbazeapi.ApiPlugin;
import me.drkapdor.funbazeapi.api.FunBazeApi;
import me.drkapdor.funbazeapi.api.event.user.UserLoadEvent;
import me.drkapdor.funbazeapi.api.user.FBUser;
import me.drkapdor.funbazeapi.api.user.manager.CacheMethod;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;

public class ConnectionHandler implements Listener {

    @EventHandler (priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(ApiPlugin.getInstance(), () -> {
            FBUser user = ApiPlugin.getApi().getUserManager().load(event.getPlayer().getName(), CacheMethod.GAME_SESSION);
            if (user == null)
                ApiPlugin.getApi().getUserManager().createUser(event.getPlayer());
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLaterAsynchronously(ApiPlugin.getInstance(), () -> {
            if (Bukkit.getPlayerExact(player.getName()) == null)
                ApiPlugin.getApi().getUserManager().unCache(player.getName());
        }, 400);
    }

    @EventHandler
    public void onLoad(UserLoadEvent event) {
        FBUser user = event.getUser();
        File userFolder = new File(ApiPlugin.dataFolder + File.separator + user.getNickname());
        Bukkit.getScheduler().runTaskAsynchronously(ApiPlugin.getInstance(), () -> {
            File skinsFolder;
            if (!userFolder.exists()) {
                userFolder.mkdir();
                skinsFolder = new File(userFolder + File.separator + "skins");
                skinsFolder.mkdir();
            }
            else {
                skinsFolder = new File(userFolder + File.separator + "skins");
                if (!skinsFolder.exists())
                    skinsFolder.mkdir();
            }
        });
    }
}
