package me.drkapdor.funbazeapi.api;


import me.drkapdor.funbazeapi.ApiPlugin;
import me.drkapdor.funbazeapi.api.npc.NPCManager;
import me.drkapdor.funbazeapi.api.promocode.PromoCodeManager;
import me.drkapdor.funbazeapi.api.role.RolesManager;
import me.drkapdor.funbazeapi.api.skin.SkinsManager;
import me.drkapdor.funbazeapi.api.user.manager.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

/**
 * Узел взаимодействия со всей инфраструктурой <b>FunBaze</b>
 * @author DrKapdor
 */

public class FunBazeApi {

    private final UserManager userManager = new UserManager();
    private final RolesManager rolesManager = new RolesManager();
    private final SkinsManager skinsManager = new SkinsManager();
    private final NPCManager npcManager = new NPCManager();
    private final PromoCodeManager promoCodeManager = new PromoCodeManager();

    public FunBazeApi(){
        Bukkit.getServer().getServicesManager().register(FunBazeApi.class, this, ApiPlugin.getInstance(), ServicePriority.Highest);
    }

    /**
     * Получить менеджер учётных записей пользователей
     * @return Менеджер учётных записей
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * Получить менеджер ролевых профессий
     * @return Менеджер ролевых профессий
     */
    public RolesManager getRolesManager() {
        return rolesManager;
    }

    /**
     * Получить менеджер скинов
     * @return Менеджер скинов
     */
    public SkinsManager getSkinsManager() {
        return skinsManager;
    }

    /**
     * Получить менеджер NPC
     * @return Менеджер NPC
     */
    public NPCManager getNpcManager() {
        return npcManager;
    }

    /**
     * Получить менеджер промокодов
     * @return Менеджер промокодов
     */
    public PromoCodeManager getPromoCodeManager() {
        return promoCodeManager;
    }
}
