package me.drkapdor.funbazeapi.api.user.manager;

/**
 * Метод кэширования учётной записи пользователя
 * @author DrKapdor
 */

public enum CacheMethod {
    /**
     * Метод, вызываемый для кратковременного хранения информации об
     * учётной записи пользователя в кэше (Данные хранятся 1.5 минуты)
     */
    OFFLINE_REQUEST,

    /**
     * Метод, вызываемый для долговременного хранения информации об
     * учётной записи пользователя в кэше (Информация удаляется из кэша
     * только после того, как игрок покинул сервер)
     */
    GAME_SESSION,

    /**
     * Метод, вызываемый для кэширования по умолчанию (Данные хранятся 30 минут)
     */
    DEFAULT
}