package me.drkapdor.funbazeapi.api;


import me.drkapdor.funbazeapi.ApiPlugin;
import me.drkapdor.funbazeapi.addon.AddonsManager;
import me.drkapdor.funbazeapi.api.npc.NPCManager;
import me.drkapdor.funbazeapi.api.promocode.PromoCodeManager;
import me.drkapdor.funbazeapi.api.role.RolesManager;
import me.drkapdor.funbazeapi.api.skin.SkinsManager;
import me.drkapdor.funbazeapi.api.user.manager.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

/**
 * Интерфейс взаимодействия со всей инфраструктурой <b>FunBaze</b>
 * @author DrKapdor
 */

public class FunBazeApi {

    private final UserManager userManager;
    private final RolesManager rolesManager;
    private final SkinsManager skinsManager;
    private final NPCManager npcManager;
    private final PromoCodeManager promoCodeManager;
    private final AddonsManager addonsManager;

    public FunBazeApi(){
        userManager = new UserManager();
        rolesManager = new RolesManager();
        skinsManager = new SkinsManager();
        npcManager = new NPCManager();
        promoCodeManager = new PromoCodeManager();
        addonsManager = new AddonsManager();
        Bukkit.getServer().getServicesManager().register(FunBazeApi.class, this, ApiPlugin.getInstance(), ServicePriority.Lowest);
    }

    /**
     * Возвращает менеджер учётных записей пользователей
     * @return Менеджер учётных записей
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * Возвращает менеджер ролевых профессий
     * @return Менеджер ролевых профессий
     */
    public RolesManager getRolesManager() {
        return rolesManager;
    }

    /**
     * Возвращает менеджер скинов
     * @return Менеджер скинов
     */
    public SkinsManager getSkinsManager() {
        return skinsManager;
    }

    /**
     * Возвращает менеджер NPC
     * @return Менеджер NPC
     */
    public NPCManager getNpcManager() {
        return npcManager;
    }

    /**
     * Возвращает менеджер промокодов
     * @return Менеджер промокодов
     */
    public PromoCodeManager getPromoCodeManager() {
        return promoCodeManager;
    }

    /**
     * Возвращает менеджер дополнений
     * @return Менеджер дополнений
     */

    public AddonsManager getAddonsManager() {
        return addonsManager;
    }
}
