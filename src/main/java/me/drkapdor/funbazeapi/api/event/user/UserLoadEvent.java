package me.drkapdor.funbazeapi.api.event.user;

import me.drkapdor.funbazeapi.api.user.FBUser;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Событие, вызываемое в момент подгрузки игрока из базы данных в кэш
 */

public class UserLoadEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final FBUser user;
    private final Player player;

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

    public FBUser getUser() {
        return user;
    }

    public Player getPlayer() {
        return player;
    }
}
