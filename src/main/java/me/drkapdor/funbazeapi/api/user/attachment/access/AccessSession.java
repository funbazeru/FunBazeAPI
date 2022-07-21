package me.drkapdor.funbazeapi.api.user.attachment.access;

import com.google.gson.GsonBuilder;

/**
 * Сессия автоматической авторизации
 * @author DrKapdor
 */

public class AccessSession {

    private final AccessMode mode;
    private final String address;
    private final long creationDate;
    private final long expiresAt;

    /**
     * Конструктор класса
     * @param mode Режим авторизации
     * @param address IP адрес, с которого осуществлён вход
     * @param expirationTime Время, которое сессия будет действительна
     */

    public AccessSession(AccessMode mode, String address, long expirationTime) {
        this.mode = mode;
        this.address = address;
        creationDate = System.currentTimeMillis();
        expiresAt = creationDate + expirationTime;
    }

    /**
     * Возвращает способ авторизации
     * @return Способ авторизации
     */

    public AccessMode getMode() {
        return mode;
    }

    /**
     * Возвращает IP адрес, с которого был совершён вход
     * @return IP адрес
     */

    public String getAddress() {
        return address;
    }

    /**
     * Возвращает дату генерации класса
     * @return Дата генерации класса
     */

    public long getCreationDate() {
        return creationDate;
    }

    /**
     * Проверяет, истекла ли сессия
     * @return Сессия истекла
     */

    public boolean isExpired() {
        return System.currentTimeMillis() > expiresAt;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }
}
