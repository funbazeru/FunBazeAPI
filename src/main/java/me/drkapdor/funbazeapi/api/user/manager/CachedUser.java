package me.drkapdor.funbazeapi.api.user.manager;

import me.drkapdor.funbazeapi.api.user.FBUser;

/**
 * Оболочка для кэширования учётная запись пользователя
 * @author DrKapdor
 */

public class CachedUser {

    private final FBUser user;
    private final CacheMethod cacheMethod;
    private final long creationDate;

    /**
     * Конструктор оболочки для кеширования учётной записи пользователя
     * @param user Учётная запись
     * @param cacheMethod Метод кэширования
     */

    public CachedUser(FBUser user, CacheMethod cacheMethod) {
        this.user = user;
        this.cacheMethod = cacheMethod;
        creationDate = System.currentTimeMillis();
    }

    /**
     * Получить учётную запись пользователя
     * @return Учётная запись пользователя
     */

    public FBUser getContent() {
        return user;
    }

    /**
     * Получить метод кэширования оболочки
     * @return Метод кэширования
     */

    public CacheMethod getCacheMethod() {
        return cacheMethod;
    }

    /**
     * Проверить, является ли оболочка перманентно кэшуремой
     * @return Является ли оболочка перманентно кэшуремой
     */

    public boolean isPermanent() {
        return cacheMethod == CacheMethod.GAME_SESSION;
    }

    /**
     * Проверить, истёк ли срок действия оболочки
     * @return Истёк ли срок действия оболочки
     */

    public boolean isExpired() {
        if (isPermanent()) return false;
        return System.currentTimeMillis() - creationDate >= getDuration(cacheMethod);
    }

    /**
     * Получить срок действия метода
     * @param cacheMethod Метод кэширования
     * @return Срок действия
     */

    private static long getDuration(CacheMethod cacheMethod) {
        switch (cacheMethod) {
            case DEFAULT: {
                return 1800000;
            }
            case OFFLINE_REQUEST: {
                return 900000;
            }
            default: {
                return 0;
            }
        }
    }
}
