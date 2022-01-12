package me.drkapdor.funbazeapi.api.skin;

import com.mojang.authlib.properties.Property;
import me.drkapdor.funbazeapi.ApiPlugin;
import me.drkapdor.funbazeapi.api.user.stored.UserSkin;
import me.drkapdor.pmapi.bukkit.MessagingAPI;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Менеджер системы скинов
 * @author DrKapdor
 */

public class SkinsManager {

    private final Map<String, UserSkin> cache = new HashMap<>();

    /**
     * Установить игроку скин
     * @param player Игрок
     * @param skin Скин
     * @param updateSelf Отправлять ли игроку пакеты обновления скина
     */

    public void setSkin(Player player, UserSkin skin, boolean updateSelf) {
        if (skin != null && !skin.getValue().isEmpty() && !skin.getSignature().isEmpty()) {
            MessagingAPI.getInstance().sendCustomData("skins", player.getName() + ":::" + skin.getValue() + ":::" + skin.getSignature());
            Bukkit.getScheduler().runTaskLater(ApiPlugin.getInstance(), () -> {
                ((CraftPlayer)player).getHandle().getProfile().getProperties().put("textures", new Property("textures", skin.getValue(), skin.getSignature()));
                updateSkin(player, updateSelf);
            }, 5);
        }
    }

    /**
     * Обновить скин игрока
     * @param player Игрок
     * @param updateSelf Отправлять ли игроку пакеты обновления скина
     */

    public void updateSkin(Player player, boolean updateSelf) {
        Location location = player.getLocation();
        CraftPlayer craftPlayer = (CraftPlayer)player;
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        World world = entityPlayer.getWorld();
        PlayerConnection playerConnection = entityPlayer.playerConnection;
        PacketPlayOutPlayerInfo removePlayerPacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer);
        PacketPlayOutPlayerInfo addPlayerPacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer);
        PacketPlayOutEntityDestroy destroyEntityPacket = new PacketPlayOutEntityDestroy(player.getEntityId());
        PacketPlayOutNamedEntitySpawn entitySpawnPacket = new PacketPlayOutNamedEntitySpawn(entityPlayer);
        PacketPlayOutRespawn respawnEntityPacket = new PacketPlayOutRespawn(craftPlayer.getWorld().getEnvironment().getId(), world.getDifficulty(), world.getWorldData().getType(), entityPlayer.playerInteractManager.getGameMode());
        PacketPlayOutPosition positionPacket = new PacketPlayOutPosition(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), new HashSet<>(), 0);
        PacketPlayOutHeldItemSlot heldItemPacket = new PacketPlayOutHeldItemSlot(player.getInventory().getHeldItemSlot());
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.getUniqueId().equals(player.getUniqueId())) {
                PlayerConnection connection = ((CraftPlayer)onlinePlayer).getHandle().playerConnection;
                connection.sendPacket(removePlayerPacket);
                connection.sendPacket(addPlayerPacket);
                connection.sendPacket(destroyEntityPacket);
                connection.sendPacket(entitySpawnPacket);
            }
        }
        if (updateSelf) {
            playerConnection.sendPacket(removePlayerPacket);
            playerConnection.sendPacket(addPlayerPacket);
            playerConnection.sendPacket(respawnEntityPacket);
            playerConnection.sendPacket(positionPacket);
            playerConnection.sendPacket(heldItemPacket);
            player.updateInventory();
            craftPlayer.updateScaledHealth();
            entityPlayer.triggerHealthUpdate();
            player.openInventory(Bukkit.createInventory(null, 0, ""));
            player.closeInventory();
        }
    }

    /**
     * Получить список кэшированных скинов и их владельцев
     * @return Список скинов
     */

    public Map<String, UserSkin> getCache() {
        return cache;
    }
}
