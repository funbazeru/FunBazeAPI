package me.drkapdor.funbazeapi.handlers;

import de.tr7zw.nbtapi.NBTItem;
import me.drkapdor.funbazeapi.ApiPlugin;
import me.drkapdor.funbazeapi.api.FunBazeApi;
import me.drkapdor.funbazeapi.api.event.roleplay.PlayerRoleChangeEvent;
import me.drkapdor.funbazeapi.api.role.Role;
import me.drkapdor.funbazeapi.api.skin.SkinsManager;
import me.drkapdor.funbazeapi.api.user.FBUser;
import me.drkapdor.funbazeapi.api.user.attachment.roleplay.UserGender;
import me.drkapdor.funbazeapi.api.user.attachment.roleplay.UserMeta;
import me.drkapdor.funbazeapi.api.user.manager.CacheMethod;
import me.drkapdor.funbazeapi.api.user.manager.UserNotLoadedException;
import me.drkapdor.funbazeapi.api.user.stored.UserSkin;
import me.drkapdor.funbazeapi.utility.SkinUtils;
import me.drkapdor.funbazeapi.utility.mojang.AccessToken;
import me.drkapdor.funbazeapi.utility.mojang.MojangUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.awt.image.BufferedImage;

public class RolesHandler implements Listener {

    @EventHandler
    public void onRoleChange(PlayerRoleChangeEvent event) {
        Player player = event.getPlayer();
        //Очищаем игрока от эффектов и удаляем ролевые предметы
        clearPlayer(player);

        FBUser user;
        try {
            user = ApiPlugin.getApi().getUserManager().getUser(player);
        } catch (UserNotLoadedException exception) {
            user = ApiPlugin.getApi().getUserManager().load(player.getName(), CacheMethod.GAME_SESSION);
        }

        if (user != null) {
            //Сохраняем отыгранное на роли время
            UserMeta meta = user.getData().getMeta();
            Role previous = event.getPrevious();
            if (!event.isStart()) {
                if (meta.getJoinDate(previous.getName()) > 0) {
                    meta.addPlayedTime(previous.getName(), (System.currentTimeMillis() - meta.getJoinDate(previous.getName())));
                    meta.setJoinDate(previous.getName(), 0);
                }
                meta.setJoinDate(event.getRole().getName(), System.currentTimeMillis());
                user.getData().setMeta(meta);
                user.save();
            }

            if (event.isChangeSkinNeeded()) {
                //Обновляем скин в соответствии с ролью
                Role current = event.getRole();
                BufferedImage customSkin = user.getCustomSkin(current.getSkinName(user.getData().getMeta().getGender()));
                SkinsManager skinsManager = ApiPlugin.getApi().getSkinsManager();
                if (customSkin != null) {
                    Bukkit.getScheduler().runTaskAsynchronously(ApiPlugin.getInstance(), () -> {
                        AccessToken token = MojangUtils.nextToken();
                        MojangUtils.uploadSkin(token, meta.getGender() == UserGender.MALE ? "classic" : "slim", customSkin);
                        UserSkin skin = SkinUtils.getCustomSkin();
                        skinsManager.setSkin(player, skin, true);
                        if (event.enableParticles())
                            player.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, player.getEyeLocation().add(0, -0.2, 0), 2);
                        skinsManager.getCache().put(player.getName(), skin);
                    });
                } else {
                    String[] textures = current.getDefaultSkinData();
                    UserSkin skin = new UserSkin(textures[0], textures[1]);
                    skinsManager.setSkin(player, skin, true);
                    if (event.enableParticles())
                        player.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, player.getEyeLocation().add(0, -0.2, 0), 2);
                }
            }
        }
    }

    private static void clearPlayer(Player player) {
        ItemStack offHand = player.getInventory().getItemInOffHand();
        if (offHand != null && offHand.getType() != Material.AIR) {
            NBTItem nbtItem = new NBTItem(offHand);
            if (!nbtItem.hasKey("rpitem") && !nbtItem.hasKey("rplicense")) {
                player.getInventory().addItem(offHand.clone());
            }
            player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
        }
        ItemStack cursor = player.getItemOnCursor();
        if (cursor != null && cursor.getType() != Material.AIR) {
            NBTItem nbtItem = new NBTItem(cursor);
            if (nbtItem.hasKey("rpitem") || nbtItem.hasKey("rplicense")) {
                player.setItemOnCursor(new ItemStack(Material.AIR));
            }
        }
        for (ItemStack itemStack : player.getInventory().getContents())
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                NBTItem nbtItem = new NBTItem(itemStack);
                if (nbtItem.hasKey("rpitem") || nbtItem.hasKey("rplicense")) {
                    player.getInventory().remove(itemStack);
                }
            }
        for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());
    }
}
