package me.drkapdor.funbazeapi.utility;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

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
}
