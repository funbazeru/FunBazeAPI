package me.drkapdor.funbazeapi.api.user.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.drkapdor.funbazeapi.FunBazeApiPlugin;
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
        //Таск-таймер (T = 25 / 20 секунд), проверяющий наличие учётных записей в очереди и сохраняющий их в базу данных
        Bukkit.getScheduler().runTaskTimer(FunBazeApiPlugin.getInstance(), () -> queue.forEach(nickname -> Bukkit.getScheduler().runTaskAsynchronously(FunBazeApiPlugin.getInstance(), () -> {
            try {
                if (queue.contains(nickname.toLowerCase())) {
                    FBUser user = FunBazeApiPlugin.getApi().getUserManager().getUser(nickname);
                    if (user != null) {
                        String name = gson.toJson(user.getUserName());
                        String data = gson.toJson(user.getData());
                        String access = gson.toJson(user.getAccess());
                        String sql = "UPDATE Players SET ID = '" + user.getId() + "', Name = '" + name + "', IP = '" + user.getIp() + "', DiscordID = '" + user.getDiscordId() + "', Access = '" + access + "', Data = '" + data + "' WHERE Nickname = '" + user.getNickname() + "'";
                        FunBazeApiPlugin.getDatabase().update(sql);
                        Bukkit.getPluginManager().callEvent(new UserSavedEvent(user));
                    }
                    queue.remove(nickname.toLowerCase());
                    FunBazeApiPlugin.getApi().getUserManager().unCache(nickname);
                }
            } catch(UserNotLoadedException ignored){
            }
        })), 0, 25);
    }

    /**
     * Проверяет, отправлена ли учётная запись в очередь на сохранение в базу данных
     *
     * @param user Имя учётной записи (никнейм)
     * @return Поставлена ли учётная запись в очередь
     */

    public static boolean isSent(String user) {
        return queue.contains(user.toLowerCase());
    }

    /**
     * Отправляет учётную запись в очередь на сохранение в базу данных
     * @param user Имя учётной записи (никнейм)
     */

    public static void send(String user) {
        queue.add(user.toLowerCase());
    }
}
