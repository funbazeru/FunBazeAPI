package me.drkapdor.funbazeapi.handlers;

import me.drkapdor.funbazeapi.FunBazeApiPlugin;
import me.drkapdor.funbazeapi.api.event.auth.PlayerAuthenticateEvent;
import me.drkapdor.funbazeapi.api.event.auth.PlayerRegisterEvent;
import me.drkapdor.funbazeapi.api.event.user.UserLoadEvent;
import me.drkapdor.funbazeapi.api.user.FBUser;
import me.drkapdor.funbazeapi.api.user.attachment.vk.VkData;
import me.drkapdor.funbazeapi.api.user.manager.CacheMethod;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;

public class ConnectionHandler implements Listener {

    @EventHandler (priority = EventPriority.NORMAL)
    public void onJoin(PlayerLoginEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(FunBazeApiPlugin.getInstance(), () ->
                FunBazeApiPlugin.getApi().getUserManager().load(event.getPlayer().getName(), CacheMethod.GAME_SESSION));
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onRegister(PlayerRegisterEvent event) {
        FBUser user = FunBazeApiPlugin.getApi().getUserManager().load(event.getPlayer().getName(), CacheMethod.GAME_SESSION);
        if (user == null)
            user = FunBazeApiPlugin.getApi().getUserManager().createUser(event.getPlayer());
        VkData vkData = new VkData();
        vkData.setUserId(event.getVkId());
        vkData.setAccessToken(event.getAccessToken());
        vkData.setEmail(event.getEmail());
        user.getData().setVk(vkData);
        user.save();
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onAuthenticate(PlayerAuthenticateEvent event) {
        if (event.isSuccess() && !event.isBySession()) {
            FBUser user = FunBazeApiPlugin.getApi().getUserManager().load(event.getPlayer().getName(), CacheMethod.GAME_SESSION);
            VkData vk = user.getData().getVk();
            boolean needUpdate = false;
            if (vk.getEmail() != null && !vk.getEmail().equals(event.getEmail())) {
                vk.setEmail(event.getEmail());
                needUpdate = true;
            }
            if (!vk.getAccessToken().equals(event.getAccessToken())) {
                vk.setAccessToken(event.getAccessToken());
                if (!needUpdate)
                    needUpdate = true;
            }
            if (needUpdate) {
                user.getData().setVk(vk);
                user.save();
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLaterAsynchronously(FunBazeApiPlugin.getInstance(), () -> {
            if (Bukkit.getPlayerExact(player.getName()) == null)
                FunBazeApiPlugin.getApi().getUserManager().unCache(player.getName());
        }, 400);
    }

    @EventHandler
    public void onLoad(UserLoadEvent event) {
        FBUser user = event.getUser();
        File userFolder = new File(FunBazeApiPlugin.dataFolder + File.separator + user.getNickname());
        Bukkit.getScheduler().runTaskAsynchronously(FunBazeApiPlugin.getInstance(), () -> {
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
