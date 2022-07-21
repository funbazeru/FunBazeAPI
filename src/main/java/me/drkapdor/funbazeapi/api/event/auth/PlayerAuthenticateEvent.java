package me.drkapdor.funbazeapi.api.event.auth;

import me.drkapdor.funbazeapi.api.user.attachment.access.AccessMode;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Событие, вызываемое в момент авторизации игрока
 * @author DrKapdor
 */

public class PlayerAuthenticateEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    private final AccessMode mode;
    private final boolean success;
    private final boolean bySession;

    // Переменные принимают значения в случае, если type = AuthenticationType.VKONTAKTE
    private String email;
    private int vkId;
    private String accessToken;

    /**
     * Конструктор события
     *
     * @param player Игрок
     * @param mode Режим авторизации
     * @param success Успешна ли авторизация
     * @param bySession Совершена ли авторизация автоматически
     */

    public PlayerAuthenticateEvent(Player player, AccessMode mode, boolean success, boolean bySession) {
        super(player);
        this.mode = mode;
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

    /**
     * Возвращает режим авторизации
     *
     * VKONTAKTE - Вход через сервис ВКонтакте
     * CLASSIC - Вход с использованием пароля
     *
     * @return Тип авторизации
     */

    public AccessMode getMode() {
        return mode;
    }

    /**
     * Проверяет, успешна ли авторизация
     * @return Авторизация успешна
     */

    public boolean isSuccess() {
        return success;
    }

    /**
     * Проверяет, авторизовался ли игрок благодаря активной сессии
     * @return Игрок авторизовался благодаря активной сессии
     */

    public boolean isBySession() {
        return bySession;
    }

    /**
     * Устанавливает идентификатор пользователя ВКонтакте (его страницы).
     *
     * Используется в случае, если авторизация осуществляется
     * через сервисы выше упомянутой социальной сети.
     *
     * @param vkId Идентификатор пользователя ВКонтакте
     */

    public void setVkId(int vkId) {
        this.vkId = vkId;
    }

    /**
     * Возвращает идентификатор пользователя ВКонтакте (его страницы).
     *
     * Возвращает отличное от -1 значение в случае авторизации через
     * сервисы выше упомянутой социальной сети.
     *
     * @return Идентификатор пользователя ВКонтакте
     */

    public int getVkId() {
        return vkId;
    }

    /**
     * Устанавливает access-token пользователя ВКонтакте.
     *
     * Используется в случае, если авторизация осуществляется
     * через сервисы выше упомянутой социальной сети.
     *
     * @param accessToken Access-token пользователя
     */

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Возвращает access-token к странице ВКонтакте.
     *
     * Возвращает отличное от null значение в случае,
     * если авторизация осуществляется через сервисы
     * выше упомянутой социальной сети.
     *
     * @return Access-token пользователя
     */

    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Возвращает e-mail, привязанный к странице пользователя во ВКонтакте.
     *
     * Возвращает отличное от null значение в случае,
     * если авторизация осуществляется через сервисы
     * выше упомянутой социальной сети.
     *
     * @return E-mail пользователя, привязанный к его странице
     */

    public String getEmail() {
        return email;
    }

    /**
     * Устанавливает e-mail, привязанный к странице пользователя во ВКонтакте.
     *
     * Используется в случае, если авторизация осуществляется
     * через сервисы выше упомянутой социальной сети.
     *
     * @param email E-mail пользователя, привязанный к его странице
     */

    public void setEmail(String email) {
        this.email = email;
    }

}
