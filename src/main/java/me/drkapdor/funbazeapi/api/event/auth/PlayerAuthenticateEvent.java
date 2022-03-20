package me.drkapdor.funbazeapi.api.event.auth;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerAuthenticateEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    private final int vkId;
    private final String accessToken;
    private final boolean success;
    private final boolean bySession;
    private String email;

    public PlayerAuthenticateEvent(Player player, int vkId, String accessToken, boolean success, boolean bySession) {
        super(player);
        this.vkId = vkId;
        this.accessToken = accessToken;
        this.success = success;
        this.bySession = bySession;
    }

    @Override
    public String getEventName() {
        return "PlayerAuthenticateEvent";
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

    public boolean isSuccess() {
        return success;
    }

    public boolean isBySession() {
        return bySession;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
