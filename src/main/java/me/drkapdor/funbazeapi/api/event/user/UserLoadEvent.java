package me.drkapdor.funbazeapi.api.event.user;

import me.drkapdor.funbazeapi.api.user.FBUser;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Событие, вызываемое в момент подгрузки учётной записи из базы данных в кэш
 * @author DrKapdor
 */

public class UserLoadEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final FBUser user;
    private final Player player;

    /**
     * Конструктор события
     *
     * @param user Учётная запись игрок
     * @param player Игрок
     */

    public UserLoadEvent(FBUser user, Player player) {
        this.user = user;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public String getEventName() {
        return "UserLoadEvent";
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    /**
     * Возвращает учётную запись игрока
     * @return Учётная запись
     */

    public FBUser getUser() {
        return user;
    }

    /**
     * Возвращает игрока
     * @return Игрок
     */

    public Player getPlayer() {
        return player;
    }
}
