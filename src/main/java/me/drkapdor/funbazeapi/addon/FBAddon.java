package me.drkapdor.funbazeapi.addon;

/**
 * Интерфейс дополнения к инфраструктуре FunBaze
 */

public interface FBAddon {

    /**
     * Возвращает автора дополнения.
     * @return Автор дополнения
     */

    String getAuthor();

    /**
     * Возвращает название дополнения.
     * @return Название дополнения
     */

    String getName();

    /**
     * Возвращает версию дополнения.
     * @return Версия дополнения
     */

    String getVersion();

    /**
     * Инициализирует дополнение.
     */

    void init();

}
