package me.drkapdor.funbazeapi.api.event.roleplay;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Событие, вызываемое в момент возвращения игрока в чувства
 * @author DrKapdor
 */

public class PlayerReanimatedEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player reAnimator;

    /**
     * Конструктор события
     *
     * @param player Игрок
     * @param reAnimator Оказавший помощь
     */

    public PlayerReanimatedEvent(Player player, Player reAnimator) {
        super(player);
        this.reAnimator = reAnimator;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public String getEventName() {
        return "PlayerReanimatedEvent";
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    /**
     * Получить игрока, оказавшего помощь
     * @return Игрок, оказавший помощь
     */

    public Player getReAnimator() {
        return reAnimator;
    }
}
