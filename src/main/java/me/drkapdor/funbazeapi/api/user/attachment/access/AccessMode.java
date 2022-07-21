package me.drkapdor.funbazeapi.api.user.attachment.access;

/**
 * Режимы авторизации на сервере
 * @author DrKapdor
 */

public enum AccessMode {

    /**
     * Классическая система авторизации (/register && /login)
     */
    CLASSIC,

    /**
     * Система авторизации через сервисы ВКонтакте
     */
    VKONTAKTE

}
