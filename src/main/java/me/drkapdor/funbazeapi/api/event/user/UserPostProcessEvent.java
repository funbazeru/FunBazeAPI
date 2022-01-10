package me.drkapdor.funbazeapi.api.event.user;

import me.drkapdor.funbazeapi.api.user.FBUser;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Событие, вызываемое в момент окончательного формирования учётной записи игрока
 */

public class UserPostProcessEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final FBUser user;

    public UserPostProcessEvent(FBUser user) {
        this.user = user;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public String getEventName() {
        return "UserPostProcessEvent";
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public FBUser getUser() {
        return user;
    }
}
