package me.drkapdor.funbazeapi.api.user.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.drkapdor.funbazeapi.ApiPlugin;
import me.drkapdor.funbazeapi.api.FunBazeApi;
import me.drkapdor.funbazeapi.api.event.user.UserSavedEvent;
import me.drkapdor.funbazeapi.api.user.FBUser;
import me.drkapdor.funbazeapi.api.user.manager.UserNotLoadedException;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

/**
 * Очередь сохранения учётных записей в базу данных
 * @author DrKapdor
 */

public class SavingQueue {

    private static final List<String> queue = new ArrayList<>();
    private static final Gson gson = new GsonBuilder().create();

    static {
        Bukkit.getScheduler().runTaskTimer(ApiPlugin.getInstance(), () -> queue.forEach(nickname -> Bukkit.getScheduler().runTaskAsynchronously(ApiPlugin.getInstance(), () -> {
            try {
                if (queue.contains(nickname.toLowerCase())) {
                    FBUser user = ApiPlugin.getApi().getUserManager().getUser(nickname);
                    if (user != null) {
                        String name = gson.toJson(user.getUserName());
                        String data = gson.toJson(user.getData());
                        String access = gson.toJson(user.getAccess());
                        String sql = "UPDATE Players SET ID = '" + user.getId() + "', Name = '" + name + "', IP = '" + user.getIp() + "', DiscordID = '" + user.getDiscordId() + "', Access = '" + access + "', Data = '" + data + "' WHERE Nickname = '" + user.getNickname() + "'";
                        ApiPlugin.getMySQLDatabase().update(sql);
                        Bukkit.getPluginManager().callEvent(new UserSavedEvent(user));
                    }
                    queue.remove(nickname.toLowerCase());
                    ApiPlugin.getApi().getUserManager().unCache(nickname);
                }
            } catch(UserNotLoadedException ignored){
            }
        })), 0, 25);
    }

    /**
     * Проверить, отправлена ли учётная запись в очередь на сохранение в базу данных
     * @param user Имя учётной записи (никнейм)
     * @return Поставлена ли учётная запись в очередь
     */

    public static boolean isSent(String user) {
        return queue.contains(user.toLowerCase());
    }

    /**
     * Отправить учётную запись в очередь на сохранение в базу данных
     * @param user Имя учётной записи (никнейм)
     */

    public static void send(String user) {
        queue.add(user.toLowerCase());
    }
}
