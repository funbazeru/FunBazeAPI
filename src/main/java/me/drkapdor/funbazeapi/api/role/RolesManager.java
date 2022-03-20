package me.drkapdor.funbazeapi.api.role;

import me.drkapdor.funbazeapi.ApiPlugin;
import me.drkapdor.funbazeapi.api.FunBazeApi;
import me.drkapdor.funbazeapi.api.event.roleplay.PlayerRoleChangeEvent;
import me.drkapdor.funbazeapi.api.user.FBUser;
import me.drkapdor.funbazeapi.api.user.attachment.roleplay.UserMeta;
import me.drkapdor.funbazeapi.api.user.manager.CacheMethod;
import me.drkapdor.funbazeapi.api.user.manager.UserNotLoadedException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Менеджер ролей
 * @author DrKapdor
 */

public class RolesManager {

    private final Scoreboard scoreboard;
    public final Team hiddenTeam;
    public final Team pendingTeam;

    private final Map<Player, Role> playersRoles = new HashMap<>();
    private final LinkedHashMap<String, Role> roles = new LinkedHashMap<>();
    private final Map<String, HashMap<String, Long>> rolesCoolDowns = new HashMap<>();

    public RolesManager() {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        hiddenTeam = scoreboard.registerNewTeam("hidden");
        hiddenTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OTHER_TEAMS);
        pendingTeam = scoreboard.registerNewTeam("9999pending");
        pendingTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        pendingTeam.setPrefix("§7");
        pendingTeam.setColor(ChatColor.DARK_GRAY);
    }

    /**
     * Регистрация роли в менеджере
     * @param role Регистрируемая роль
     */

    public void registerRole(Role role) {
        roles.put(role.getName().toLowerCase(), role);
        Team team = scoreboard.registerNewTeam((99 - role.getTabPriority()) + role.getName());
        team.setPrefix(role.getPrefix());
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
    }

    /**
     * Добавление игрока в скорборд роли
     * @param player Игрок
     * @param role Роль
     */

    public void setTeam(Player player, Role role) {
        Role currentRole = getPlayerRole(player);
        if (currentRole == null)
            currentRole = role;
        scoreboard.getTeam((99 - currentRole.getTabPriority()) + currentRole.getName()).removeEntry(player.getName());
        scoreboard.getTeam((99 - role.getTabPriority()) + role.getName()).addEntry(player.getName());
    }

    /**
     * Установить определённую роль игроку
     * @param player Игрок, которому устанавливается роль
     * @param role Устанавливаемая игроку роль
     * @param particles Включить частицы взрыва при выборе роли
     * @param start Обозначить то, что роль назначается игроку при входе
     * @param skinChangeNeeded Нужно ли изменять скин при выборе роли
     * @return Результат выполнения
     */

    public boolean setRole(Player player, String role, boolean particles, boolean start, boolean skinChangeNeeded) {
        if (roles.get(role.toLowerCase()) == null)
            return false;
        Team team = scoreboard.getTeam((99 - roles.get(role).getTabPriority()) + roles.get(role).getName());
        if (team == null)
            return false;
        Bukkit.getPluginManager().callEvent(new PlayerRoleChangeEvent(player, roles.get(role), getPlayerRole(player), start, particles, skinChangeNeeded));
        player.closeInventory();
        clearEmptyTeams(player);
        team.addEntry(player.getName());
        playersRoles.put(player, roles.get(role));
        Bukkit.getScheduler().runTask(ApiPlugin.getInstance(), () -> roles.get(role).applyJobEffects(player));
        roles.get(role).giveJobItems(player);
        return true;
    }

    /**
     * Установить определённую роль игроку (partisles = false)
     * @param player Игрок, которому устанавливается роль
     * @param role Устанавливаемая игроку роль
     * @param start Обозначить то, что роль назначается игроку при входе
     * @return Результат выполнения
     */

    public boolean setRole(Player player, String role, boolean start) {
        return setRole(player, role, start, false, true);
    }

    /**
     * Уточнить роль игрока
     * @param player Игрок
     * @param job Уточнение для роли
     */

    public void setJob(Player player, String job) {
        clearEmptyTeams(player);
        Role role = playersRoles.get(player);
        Team team = scoreboard.getTeam((99 - role.getTabPriority()) + job);
        if (team == null) {
            team = scoreboard.registerNewTeam((99 - role.getTabPriority()) + job);
            team.setPrefix(role.getPrefixColor() + job + " §7");
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }
        team.addEntry(player.getName());
    }

    /**
     * Сбросить роль игроку
     * @param player Игрок
     */

    public void dropRole(Player player) {
        FBUser user;
        try {
            user = ApiPlugin.getApi().getUserManager().getUser(player);
        } catch (UserNotLoadedException ex) {
            user = ApiPlugin.getApi().getUserManager().load(player.getName(), CacheMethod.OFFLINE_REQUEST);
        }
        Role role = getPlayerRole(player);
        if (role != null) {
            String currentRole = role.getName();
            UserMeta meta = user.getData().getMeta();
            if (meta.getJoinDate(currentRole) > 0) {
                meta.addPlayedTime(currentRole, System.currentTimeMillis() - meta.getJoinDate(currentRole));
                meta.setJoinDate(currentRole, 0);
                user.getData().setMeta(meta);
                user.save();
            }
            clearEmptyTeams(player);
        }
        playersRoles.remove(player);
    }

    /**
     * Получить роль
     *
     * @param name Название роли
     * @return Искомая роль
     */

    public Role getRole(String name) {
        return roles.get(name);
    }

    /**
     * Получить хешмапу игроков и занимаемых ими ролей
     *
     * @return Список игроков и их ролей
     */

    public Map<Player, Role> getPlayersRoles() {
        return playersRoles;
    }

    /**
     * Получить роль игрока
     *
     * @param player Игрок
     * @return Роль игрока
     */

    public Role getPlayerRole(Player player) {
        return playersRoles.get(player);
    }

    /**
     * Получить список игроков, занимающих опеределённую роль
     *
     * @param role Роль
     * @return Список игроков
     */

    public Collection<Player> getPlayersByRole(String role) {
        List<Player> players = new ArrayList<>();
        for (Map.Entry<Player, Role> entry : playersRoles.entrySet()) {
            if (entry.getValue().getName().equalsIgnoreCase(role))
                players.add(entry.getKey());
        }
        return players;
    }

    /**
     * Получить все доступные роли
     *
     * @return Список ролей
     */

    public Collection<Role> getRoles() {
        return roles.values();
    }

    /**
     * Установить игроку задержку на роль
     *
     * @param player Игрок
     * @param role Роль
     * @param seconds Время задержки в секундах
     */

    public void setRoleCoolDown(Player player, String role, int seconds) {
        HashMap<String, Long> cooldowns = rolesCoolDowns.containsKey(player.getName()) ? rolesCoolDowns.get(player.getName()) : new HashMap<>();
        cooldowns.put(role, System.currentTimeMillis() + seconds * 1000L);
        rolesCoolDowns.put(player.getName(), cooldowns);
    }

    /**
     * Получить дату установки задержки на роль в long
     *
     * @param player Игрок
     * @param role Роль
     * @return Дата установки задержки
     */

    public long getRoleCoolDown(Player player, String role) {
        if (!rolesCoolDowns.containsKey(player.getName())) return 0;
        else if (!rolesCoolDowns.get(player.getName()).containsKey(role)) return 0;
        else return rolesCoolDowns.get(player.getName()).get(role);
    }

    /**
     * Проверить, есть ли у игрока задержка на роль
     *
     * @param player Игрок
     * @param role Роль
     * @return Установлена/не установлена задержка
     */

    public boolean isRoleCoolDowned(Player player, String role) {
        if (rolesCoolDowns.containsKey(player.getName())) {
            if (rolesCoolDowns.get(player.getName()).containsKey(role)) {
                if (rolesCoolDowns.get(player.getName()).get(role) < System.currentTimeMillis()) {
                    rolesCoolDowns.get(player.getName()).remove(role);
                    if (rolesCoolDowns.get(player.getName()).isEmpty()) rolesCoolDowns.remove(player.getName());
                    return false;
                } else return true;
            } else return false;
        } else return false;
    }

    /**
     * Получить скорборд с командами ролей
     * @return Скорборд
     */

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public void clearEmptyTeams(Player player) {
        for (Team otherTeam : scoreboard.getTeams()) {
            if (otherTeam.getEntries().contains(player.getName())) {
                otherTeam.removeEntry(player.getName());
                if (!otherTeam.getName().equals(pendingTeam.getName()) && otherTeam.getEntries().isEmpty() &&
                        !roles.containsKey(otherTeam.getName().toLowerCase().substring(2)))
                    otherTeam.unregister();
            }
        }
    }
}
