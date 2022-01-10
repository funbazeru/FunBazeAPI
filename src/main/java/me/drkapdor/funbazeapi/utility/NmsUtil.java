package me.drkapdor.funbazeapi.utility;

import com.mojang.authlib.GameProfile;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Утилита для упрощённого взаимодействия с элементами NMS
 * @author DrKapdor
 */

public class NmsUtil {

    /**
     * Получить {@link GameProfile} игрока
     *
     * @param player Игрок
     * @return Псевдопрофиль Mojang
     */

    public static GameProfile getGameProfile(Player player) {
        return ((CraftPlayer)player).getHandle().getProfile();
    }

}
