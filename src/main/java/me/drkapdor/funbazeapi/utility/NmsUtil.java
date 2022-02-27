package me.drkapdor.funbazeapi.utility;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_12_R1.WorldBorder;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Утилита для упрощённого взаимодействия с элементами NMS
 * @author DrKapdor
 */

public class NmsUtil {

    /**
     * Получить {@link GameProfile} игрока
     * @param player Игрок
     * @return Псевдопрофиль Mojang
     */

    public static GameProfile getGameProfile(Player player) {
        return ((CraftPlayer)player).getHandle().getProfile();
    }

    /**
     * Проиграть анимацию удара/взаимодействия игрока
     * @param player Игрок
     */

    public static void playHandAnimation(Player player) {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        PacketContainer container = manager.createPacket(PacketType.Play.Server.ANIMATION);
        container.getModifier().writeDefaults();
        container.getIntegers().write(0, player.getEntityId()).write(1, 0);
        Bukkit.getOnlinePlayers().forEach(recipient -> {
            try {
                manager.sendServerPacket(recipient, container);
            } catch (InvocationTargetException ignored) {
            }
        });
    }

    /**
     * Вызвать эффект покраснения экрана, возникающий при
     * приближении к границе мира
     * @param player Игрок
     * @param warningBlocks Расстояние игрока до виртуальной границы (чем ближе, тем сильнее эффект)
     */

    public static void sendWorldBorderGlowing(Player player, int warningBlocks) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        WorldBorder playerWorldBorder = entityPlayer.world.getWorldBorder();
        PacketPlayOutWorldBorder worldBorder = new PacketPlayOutWorldBorder(playerWorldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_WARNING_BLOCKS);
        try {
            Field field = worldBorder.getClass().getDeclaredField("i");
            field.setAccessible(true);
            field.setInt(worldBorder, warningBlocks);
            field.setAccessible(!field.isAccessible());
        } catch (Exception e) {
            e.printStackTrace();
        }
        entityPlayer.playerConnection.sendPacket(worldBorder);
    }
}
