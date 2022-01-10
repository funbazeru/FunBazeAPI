package me.drkapdor.funbazeapi.api.role;

/**
 * Категория роли
 */

public enum RoleCategory {

    /**
     * Государственные роли
     */
    GOVERNMENT("§aГосударство"),

    /**
     * Криминальные роли
     */
    CRIMINAL("§cКриминал"),

    /**
     * Нейтральные роли
     */
    NEUTRAL("§eНейтральные"),

    /**
     * Не относящиеся к РП роли
     */
    NON_RP("§dNONRP");

    private final String displayName;

    /**
     * Конструктор категории
     * @param displayName Отображаемое название
     */
    RoleCategory(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

}
