package me.drkapdor.funbazeapi.api.event.auth;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerRegisterEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    private final int vkId;
    private final String accessToken;
    private String email;

    public PlayerRegisterEvent(Player player, int vkId, String accessToken) {
        super(player);
        this.vkId = vkId;
        this.accessToken = accessToken;
    }

    @Override
    public String getEventName() {
        return "PlayerRegisterEvent";
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public int getVkId() {
        return vkId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
