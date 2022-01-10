package me.drkapdor.funbazeapi.api.role;

/**
 * Уровень доступа к оружию роли
 * @author DrKapdor
 */

public enum GunPermission {

    /**
     * Гражданское оружие
     */
    CIVILIAN,

    /**
     * Служебное оружие (гос.)
     */
    SERVICE,

    /**
     * Любое оружие
     */
    ALL;

    /**
     * Получить тип разрешения по идентификатору оружия
     * @param id Идентификатор оружия
     * @return Тип разрешения
     */

    public static GunPermission getType(String id) {
        switch (id.toLowerCase()) {
            case "pm":
            case "pb":
            case "sw686":
            case "toz34": {
                return CIVILIAN;
            }
            case "grom":
            case "aks74":
            case "aks74u": {
                return SERVICE;
            }
            default: {
                return ALL;
            }
        }
    }
}
