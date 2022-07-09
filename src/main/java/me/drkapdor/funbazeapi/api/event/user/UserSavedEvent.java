package me.drkapdor.funbazeapi.api.event.user;

import me.drkapdor.funbazeapi.api.user.FBUser;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Событие, вызываемое в момент учётная запись сохраняется в базу данных
 * @author DrKapdor
 */

public class UserSavedEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final FBUser user;

    /**
     * Конструктор события
     * @param user Учётная запись игрока
     */

    public UserSavedEvent(FBUser user) {
        this.user = user;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public String getEventName() {
        return "UserSavedEvent";
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

}
