package me.drkapdor.funbazeapi.api.event.roleplay;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Событие, вызываемое в момент окончательной потери игроком сознания
 * @author DrKapdor
 */

public class PlayerFinalDeathEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player killer;

    /**
     * Конструктор события
     *
     * @param player Игрок (Жертва)
     * @param killer Убийца
     */

    public PlayerFinalDeathEvent(Player player, Player killer) {
        super(player);
        this.killer = killer;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public String getEventName() {
        return "PlayerFinalDeathEvent";
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    /**
     * Возвращает убийцу игрока
     * @return Убийца игрока
     */

    public Player getKiller() {
        return killer;
    }
}
