package me.drkapdor.funbazeapi.api.role;

import me.drkapdor.funbazeapi.ApiPlugin;
import me.drkapdor.funbazeapi.api.FunBazeApi;
import me.drkapdor.funbazeapi.api.user.attachment.UserStatus;
import me.drkapdor.funbazeapi.api.user.attachment.roleplay.UserGender;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Роль, отыгрываемая в режиме RolePlay
 * @author DrKapdor
 */

public abstract class Role {

    private final String name;
    private final String prefix;
    private final String skin;
    private final String description;
    private final RoleCategory category;
    private GunPermission gunPermission;
    private final int tabPriority;
    private int salary;
    private RoleTag tag;
    private UserStatus statusRestriction;
    private boolean needVote;
    private int limit = 999;
    private String[] defaultSkinData;
    private final HashMap<String, Integer> playtimeLimit = new HashMap<>();

    /**
     * Стандартная роль
     */
    public static final String DEFAULT = "горожанин";

    /**
     * Роль главы города
     */
    public static final String MAYOR = "мэр";

    /**
     * Роль заключённого
     */
    public static final String PRISONER = "заключённый";

    /**
     * Конструктор роли
     *
     * @param name Название
     * @param prefix Префикс
     * @param skin Идентификатор скина
     * @param description Описание
     * @param category Категория
     * @param tabPriority Приоритет в табе
     */

    public Role(String name, String prefix, String skin, String description, RoleCategory category, int tabPriority) {
        this.name = name;
        this.prefix = prefix;
        this.skin = skin;
        this.description = description;
        this.category = category;
        this.tabPriority = tabPriority;
        defaultSkinData = new String[] {
                "ewogICJ0aW1lc3RhbXAiIDogMTYzMzAyMDEzMjQ3NywKICAicHJvZmlsZUlkIiA6ICI3ZTYxZGZhMzMyMDc0ODMwYTNmNDg3NzI0NzRjMzI3YyIsCiAgInByb2ZpbGVOYW1lIiA6ICJGdW5CYXplXzAxIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzc0OTYxYjFlYTA4MjZkNzFjYjcwOTBmZGMxMDVjODE1ZTRlYzUxZjU3YjA1NmFhNWE2NDE2MjFlYmY4MGU4MDQiCiAgICB9CiAgfQp9",
                "Fwv1HWfE/Fb3MLdB5+99gAkyNGIZapMWhibJ/RXpAbFE8ylPQwXn96QG32M+42ZWmsC3b5wqefy0QimEg6LEDirvK2OZPfA0kr4+GpAjOD8gPY2XNuJVF1QX2r815QdoJR++CYXKX5sqbJOmgAgIWCbspeyLSN20zLbck5KW1E6r1+7ukakQEy/Wpfx3WoAhNjFo/un6NdcyzLLWfKAVTD0fsc05OSYrh5K3TXwrgVCMP2lU7WsQUaAvMMoYS8yoAew2ilD8wyWh9vd8Zloo720KOJf3VEA7to8UZtDqhfzb10p0929AFHe50YYXPFMqj3eFhxO31k/un8nw796uN8lg07ccurISvxOn+PXcsjGR+I1G0WN2TObB4A74fkujsP2IP3C9OP7Gmh5eZpoUCi2SAXgUqUpRnMTZLhiOfiUVKNzm9Jy3XOYk09ryEXDg4A+yyyHL3LB4GMDwFs+tJs7MKgIptgwdhH4o29FeE7scTBtattQPzGSJs+MnZ9dzMBlAd8df3Hr8HoVhw79HRcmH3mKTs5M24nnSARR/ovvYlsf3I+mExzFsASZVVBZLbZvmf+Poo8dZBiZajmvhoI+pJlnL7v6qviTvuuG7dkqOueY0zVMrI+e2mii2hRwjex7Ywu5wDMjIKJo7YUAKMeZDrBKrGgGHo4qA62sU8wY="
        };
        statusRestriction = UserStatus.DEFAULT;
    }

    /**
     * Получить название профессии
     *
     * @return Название професии
     */

    public String getName() {
        return name;
    }

    /**
     * Получить префикс профессии
     *
     * @return Префикс професии
     */

    public String getPrefix() {
        return prefix;
    }

    /**
     * Получить цвет префикс профессии
     *
     * @return Цвет префикса професии
     */

    public ChatColor getPrefixColor() {
        return ChatColor.getByChar(prefix.charAt(1));
    }

    /**
     * Получить соответствующий идентификатор скина для роли
     *
     * @return Идентификатор скина
     */

    public String getSkinName() {
        return getSkinName(UserGender.MALE);
    }

    /**
     * Получить соответствующий идентификатор скина для роли
     *
     * @param gender Версия скина относительно биологического пола
     * @return Идентификатор скина
     */

    public String getSkinName(UserGender gender) {
        return skin  + (gender == UserGender.MALE ? "_male" : "_female");
    }

    /**
     * Получить описание роли
     *
     * @return Описание роли
     */

    public String getDescription() {
        return description;
    }

    /**
     * Получить категорию роли
     *
     * @return Категория роли
     */

    public RoleCategory getCategory() {
        return category;
    }

    /**
     * Получить уровень доступа к оружию
     *
     * @return Уровень доступа к оружию
     */

    public GunPermission getGunPermission() {
        return gunPermission;
    }

    /**
     * Назначить уровень доступа к оружию
     *
     * @param gunPermission Уровень доступа к оружию
     */

    public void setGunPermission(GunPermission gunPermission) {
        this.gunPermission = gunPermission;
    }

    /**
     * Получить уровень приоритета в табе
     *
     * @return Уровень приоритета
     */

    public int getTabPriority() {
        return tabPriority;
    }

    /**
     * Получить размер зарплаты
     *
     * @return Размер зарплаты
     */

    public int getSalary() {
        return salary;
    }

    /**
     * Установить размер зарплаты
     *
     * @param salary Размер зарплаты
     */

    public void setSalary(int salary) {
        this.salary = salary;
    }

    /**
     * Получить список ролей и требуемого времени отыгровки на них
     * для того, чтобы пользователь имел возможность выбрать данную роль
     *
     * @return Список ролей и требуемого времени отыгровки
     */

    public Set<Map.Entry<String, Integer>> getPlaytimeLimits() {
        return playtimeLimit.entrySet();
    }

    /**
     * Получить время (в секундах), которое нужно отыграть на роли
     * чтобы пользователь имел возможность выбрать данную роль
     *
     * @param role Роль
     * @return Требуемое время отыгровки
     */

    public int getPlaytimeLimit(String role) {
        return playtimeLimit.get(role.toLowerCase());
    }

    /**
     * Установить время (в секундах), которое нужно отыграть на роли
     * чтобы пользователь имел возможность выбрать данную роль
     * @param role Название роли
     * @param limit Требуемое время отыгровки
     */

    public void setPlaytimeLimit(String role, int limit) {
        playtimeLimit.put(role, limit);
    }

    /**
     * Установить стандартный скин роли
     * @param value Значение скина, закодированное в Base64
     * @param signature Сигнатура скина, закодированная в Base64
     */

    public void setDefaultSkinData(String value, String signature) {
        this.defaultSkinData = new String[] { value, signature };
    }

    /**
     * Получить стандартный скин роли
     * @return Массив, содержащий строки:<br>
     * [0] - Значение скина, закодированный в Base64<br>
     * [1] - Сигнатура скина, закодированная в Base64
     */

    public String[] getDefaultSkinData() {
        return defaultSkinData;
    }

    /**
     * Установить особую метку роли
     * @param tag Метка роли
     */

    public void setTag(RoleTag tag) {
        this.tag = tag;
    }

    /**
     * Получить особую метку роли
     * @return Метка роли
     */

    public RoleTag getTag() {
        return tag;
    }

    /**
     * Установить ограничение по статусу для выбора роли
     * @param statusRestriction Требуемый статус
     */

    public void setRestriction(UserStatus statusRestriction) {
        this.statusRestriction = statusRestriction;
    }

    /**
     * Получить ограничение по статусу для выбора роли
     * @return Требуемый статус
     */

    public UserStatus getRestriction() {
        return statusRestriction;
    }

    /**
     * Проверить, является ли роль доступна для статуса
     * @param status Игровой статус
     * @return Является ли роль доступной
     */

    public boolean isAllowed(UserStatus status) {
        if (statusRestriction == UserStatus.DEFAULT) return true;
        else if (status.isStaff() && statusRestriction != UserStatus.ADMIN) return true;
        else return status == statusRestriction;
    }

    /**
     * Проверить, требуется ли голосование для выбора этой роли
     * @return Требуется ли голосование
     */

    public boolean isNeedVote() {
        return needVote;
    }

    /**
     * Установить требование голосования для выбора роли
     * @param needVote Требовать ли голосование для выбора роли
     */

    public void setNeedVote(boolean needVote) {
        this.needVote = needVote;
    }

    /**
     * Получить максимальное число игроков, занимающих роль
     * @return Максимальное число игроков
     */

    public int getLimit() {
        return limit;
    }

    /**
     * Установить максимальное число игроков, занимающих роль
     * @param limit Максимальное число игроков
     */

    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Получить количество игроков, занимающих роль
     * @return Количество игроков
     */

    public int getClaimed() {
        return ApiPlugin.getApi().getRolesManager().getPlayersByRole(name).size();
    }

    /**
     * Проверить, занимает ли игрок данную роль
     * @param player Игрок
     * @return Занимает ли игрок роль
     */

    public boolean isClaimed(Player player) {
        return ApiPlugin.getApi().getRolesManager().getPlayerRole(player).getName().equals(name);
    }

    /**
     * Выдать ролевые предметы игроку
     * @param player Игрок
     */

    public abstract void giveJobItems(Player player);

    /**
     * Применить ролевые эффекты игроку
     * @param player Игрок
     */

    public abstract void applyJobEffects(Player player);

}
