package me.drkapdor.funbazeapi.api.event.auth;

/**
 * Типы авторизации на сервере
 * @author DrKapdor
 */
public enum AuthenticationType {

    /**
     * Авторизация при помощи использования пароля
     */
    DEFAULT,

    /**
     * Авторизация при помощи сервиса ВКонтакте
     */
    VKONTAKTE

}
