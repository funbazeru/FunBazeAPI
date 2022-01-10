package me.drkapdor.funbazeapi.api;


import me.drkapdor.funbazeapi.api.role.RolesManager;
import me.drkapdor.funbazeapi.api.skin.SkinsManager;
import me.drkapdor.funbazeapi.api.user.manager.UserManager;

/**
 * Узел взаимодействия со всей инфраструктурой <b>FunBaze</b>
 * @author DrKapdor
 */

public class FunBazeApi {

    private static final UserManager userManager = new UserManager();
    private static final RolesManager rolesManager = new RolesManager();
    private static final SkinsManager skinsManager = new SkinsManager();

    /**
     * Получить менеджер учётных записей пользователей
     * @return Менеджер учётных записей
     */

    public static UserManager getUserManager() {
        return userManager;
    }

    /**
     * Получить менеджер ролевых профессий
     * @return Менеджер ролевых профессий
     */

    public static RolesManager getRolesManager() {
        return rolesManager;
    }

    /**
     * Получить менеджер скинов
     * @return Менеджер скинов
     */

    public static SkinsManager getSkinsManager() {
        return skinsManager;
    }
}
