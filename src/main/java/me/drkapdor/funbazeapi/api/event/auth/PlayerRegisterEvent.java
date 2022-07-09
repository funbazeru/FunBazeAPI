package me.drkapdor.funbazeapi.api.event.auth;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Событие, вызываемое в момент регистрации нового пользователя
 * @author DrKapdor
 */

public class PlayerRegisterEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /*
    Переменная принимает значение в случае
    авторизации с использованием пароля
    */
    private String password;

    /*
    Переменные принимают значение в случае
    авторизации с использованием ВКонтакте
     */
    private int vkId;
    private String accessToken;
    private String email;

    /**
     * Конструктор события
     * @param player Пользователь
     */

    public PlayerRegisterEvent(Player player) {
        super(player);
    }

    @Override
    public String getEventName() {
        return "PlayerRegisterEvent";
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    /**
     * Возвращает пароль от аккаунта пользователя
     *
     * Используется в случае, если авторизация осуществляется
     * при помощи классической системы логин-пароля.
     *
     * @return Пароль (в зашифрованном виде)
     */

    public String getPassword() {
        return password;
    }

    /**
     * Устанавливает пароль от аккаунта пользователя
     *
     * Возвращает отличное от null значение в случае,
     * если авторизация осуществляется при помощи
     * классическойсистемы логин-пароля.
     *
     * @param password Пароль (в зашифрованном виде)
     */

    public void setPassword(String password) {
        this.password = password;
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
