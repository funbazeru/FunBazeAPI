package me.drkapdor.funbazeapi.api.event.roleplay;

import me.drkapdor.funbazeapi.api.role.Role;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Событие, вызываемое в момент изменения роли игрока
 */

public class PlayerRoleChangeEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Role role;
    private final Role previous;
    private final boolean start;
    private final boolean particles;

    public PlayerRoleChangeEvent(Player player, Role role, Role previous, boolean start, boolean particles) {
        super(player);
        this.role = role;
        this.previous = previous;
        this.start = start;
        this.particles = particles;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public String getEventName() {
        return "PlayerRoleChangeEvent";
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    /**
     * Получить роль, на которую игрок будет назначен
     *
     * @return Роль, на которую игрок будет назначен
     */

    public Role getRole() {
        return role;
    }

    /**
     * Получить роль, на которую игрок уже был назначен
     *
     * @return Роль, на которую игрок уже был назначен
     */

    public Role getPrevious() {
        return previous;
    }

    /**
     * Проверить, выдаётся ли роль автоматически в момент подключения игрока к серверу
     *
     * @return Выдаётся ли роль автоматически в момент подключения игрока к серверу
     */

    public boolean isStart() {
        return start;
    }

    /**
     * Проверить, будут ли отображаться частицы взрыва при выборе роли
     *
     * @return Будут ли отображаться частицы взрыва при выборе роли
     */

    public boolean enableParticles() {
        return particles;
    }
}
