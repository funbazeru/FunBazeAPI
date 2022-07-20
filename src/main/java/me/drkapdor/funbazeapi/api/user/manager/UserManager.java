package me.drkapdor.funbazeapi.api.user.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.drkapdor.funbazeapi.FunBazeApiPlugin;
import me.drkapdor.funbazeapi.api.event.user.UserLoadEvent;
import me.drkapdor.funbazeapi.api.event.user.UserPostProcessEvent;
import me.drkapdor.funbazeapi.api.user.FBUser;
import me.drkapdor.funbazeapi.api.user.attachment.FBID;
import me.drkapdor.funbazeapi.api.user.attachment.UserData;
import me.drkapdor.funbazeapi.api.user.attachment.roleplay.UserAccess;
import me.drkapdor.funbazeapi.api.user.attachment.roleplay.UserName;
import me.drkapdor.funbazeapi.api.user.storage.SavingQueue;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Менеджер учётных записей
 * @author DrKapdor
 */

public class UserManager {

    private final Map<String, CachedUser> cacheMap;

    public UserManager() {
        cacheMap = new HashMap<>();
        Bukkit.getScheduler().runTaskTimer(FunBazeApiPlugin.getInstance(), () -> {
            for (Map.Entry<String, CachedUser> entry : new HashSet<>(cacheMap.entrySet())) {
                if (entry.getValue().isExpired())
                    cacheMap.remove(entry.getKey().toLowerCase());
            }
        }, 0, 20 * 60 * 5);
    }

    /**
     * Создать учётную запись {@link FBUser} и сохранить её в базу данных
     * @param player Игрок
     * @return Учётная запись
     */

    public FBUser createUser(Player player) {
        //Генерация случайного FBID
        FBID id = FBID.random();
        while (!id.isUnique())
            id = FBID.random();
        FBUser user = new FBUser(id, player.getName(), player.getAddress().getHostString());
        user.save(true);
        //Отправляем учётную запись в кэш
        cacheMap.put(player.getName().toLowerCase(), new CachedUser(user, CacheMethod.DEFAULT));
        FunBazeApiPlugin.getInstance().getServer().getPluginManager().callEvent(new UserLoadEvent(user, player));
        //Создаём папку для будущих скинов
        Bukkit.getScheduler().runTaskLater(FunBazeApiPlugin.getInstance(), () -> {
            File userFolder = new File(FunBazeApiPlugin.dataFolder + File.separator + player.getName());
            if (!userFolder.isDirectory())
                if (userFolder.mkdir()) new File(userFolder + File.separator + "skins").mkdir();
            FunBazeApiPlugin.getInstance().getServer().getPluginManager().callEvent(new UserPostProcessEvent(cacheMap.get(user.getNickname().toLowerCase()).getContent()));
        }, 20L);
        return user;
    }

    /**
     * Загрузить учётную запись {@link FBUser} из базы данных в кеш
     * @param name Никнейм игрока
     * @param cacheMethod Метод кэширования
     * @return Учётная запись
     */

    public FBUser load(String name, CacheMethod cacheMethod) {
        //Если учётная запись имеется в кеше, вычленяем её оттуда
        if (cacheMap.containsKey(name.toLowerCase())) {
            FBUser user = cacheMap.get(name.toLowerCase()).getContent();
            Bukkit.getPluginManager().callEvent(new UserLoadEvent(user, Bukkit.getPlayerExact(name)));
            return user;
        } else {
            if (!cacheMap.containsKey(name.toLowerCase())) {
                ResultSet resultSet = FunBazeApiPlugin.getDatabase().query("SELECT * FROM Players WHERE Nickname = '" + name + "'");
                try {
                    if (resultSet.next()) {
                        FBUser user = new FBUser(FBID.fromString(resultSet.getString("ID")), resultSet.getString("Nickname"), resultSet.getString("IP"));
                        Gson gson = new GsonBuilder().create();
                        user.setUserName(gson.fromJson(FunBazeApiPlugin.getJsonParser().parse(resultSet.getString("Name")), UserName.class));
                        user.setAccess(gson.fromJson(FunBazeApiPlugin.getJsonParser().parse(resultSet.getString("Access")), UserAccess.class));
                        user.setData(gson.fromJson(FunBazeApiPlugin.getJsonParser().parse(resultSet.getString("Data")), UserData.class));
                        cacheMap.put(name.toLowerCase(), new CachedUser(user, cacheMethod));
                        Bukkit.getPluginManager().callEvent(new UserLoadEvent(user, Bukkit.getPlayerExact(name)));
                        return user;
                    }
                    return null;
                } catch (SQLException exception) {
                    exception.printStackTrace();
                    return null;
                }
            } else return cacheMap.get(name.toLowerCase()).getContent();
        }
    }

    /**
     * Подргурзить учётную запись {@link FBUser} из кэша
     * @param name Никнейм игрока
     * @return Учётная запись
     * @throws UserNotLoadedException Учётная запись отсутствует в кеше
     */

    public FBUser getUser(String name) throws UserNotLoadedException {
        if (cacheMap.containsKey(name.toLowerCase()))
            return cacheMap.get(name.toLowerCase()).getContent();
        throw new UserNotLoadedException(name);
    }

    /**
     * Подргурзить учётную запись {@link FBUser} из кэша
     * @param player Игрок
     * @return Учётная запись
     * @throws UserNotLoadedException Учётная запись отсутствует в кеше
     */

    public FBUser getUser(Player player) throws UserNotLoadedException {
        return getUser(player.getName());
    }

    /**
     * Загрузить учётную запись {@link FBUser} из базы данных
     * @param id Уникальный идентификатор формата {@link FBID}
     * @param cacheMethod Метод кеширования
     * @return Учётная запись
     */

    public FBUser getUser(FBID id, CacheMethod cacheMethod) {
        ResultSet resultSet = FunBazeApiPlugin.getDatabase().query("SELECT Nickname FROM Players ID = '" + id + "'");
        try {
            if (resultSet.next())
                return load(resultSet.getString("Nickname"), cacheMethod);
            return null;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Загрузить учётную запись {@link FBUser} из базы данных (OFFLINE_REQUEST)
     * @param id Уникальный идентификатор формата {@link FBID}
     * @return Учётная запись
     */


    public FBUser getUser(FBID id) {
        return getUser(id, CacheMethod.OFFLINE_REQUEST);
    }

    /**
     * Загрузить учётную запись {@link FBUser} из базы данных (OFFLINE_REQUEST)
     * @param discordId Идентификатор Discord
     * @return Учётная запись
     */

    public FBUser getUser(long discordId) {
        ResultSet resultSet = FunBazeApiPlugin.getDatabase().query("SELECT Nickname FROM Players WHERE DiscordID = '" + discordId + "'");
        try {
            if (resultSet.next())
                return load(resultSet.getString("Nickname"), CacheMethod.OFFLINE_REQUEST);
            return null;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Получить список игроков по ролевому имени
     * @param userName Ролево имя
     * @return Список игроков
     */

    public Collection<FBUser> getUsersByName(UserName userName) {
        List<FBUser> users = new ArrayList<>();
        ResultSet resultSet = FunBazeApiPlugin.getDatabase().query("SELECT Nickname FROM Players WHERE Name = '" + new Gson().toJson(userName) + "'");
        try {
            while (resultSet.next())
                users.add(load(resultSet.getString("Nickname"), CacheMethod.OFFLINE_REQUEST));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return users;
    }

    /**
     * Получить список всех игроков сервер
     * @return Список игроков
     */

    public Collection<FBUser> getAllUsers() {
        LinkedList<FBUser> users = new LinkedList<>();
        ResultSet resultSet = FunBazeApiPlugin.getDatabase().query("SELECT * FROM Players");
        try {
            while (resultSet.next()) {
                FBUser user = new FBUser(FBID.fromString(resultSet.getString("ID")), resultSet.getString("Nickname"), resultSet.getString("IP"));
                Gson gson = new GsonBuilder().create();
                user.setUserName(gson.fromJson(FunBazeApiPlugin.getJsonParser().parse(resultSet.getString("Name")), UserName.class));
                user.setAccess(gson.fromJson(FunBazeApiPlugin.getJsonParser().parse(resultSet.getString("Access")), UserAccess.class));
                user.setData(gson.fromJson(FunBazeApiPlugin.getJsonParser().parse(resultSet.getString("Data")), UserData.class));
                if (cacheMap.containsKey(user.getNickname().toLowerCase()))
                    cacheMap.put(user.getNickname().toLowerCase(), new CachedUser(user, cacheMap.get(user.getNickname().toLowerCase()).getCacheMethod()));
                users.add(user);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return users;
        }
        return users;
    }

    /**
     * Сохранить учётную запись в кэш
     * @param user Учётная запись
     */

    public void cache(FBUser user) {

    }

    /**
     * Удалить учётную запись из кэша
     * @param name Имя учётной записи
     */

    public void unCache(String name) {
        if (cacheMap.containsKey(name.toLowerCase())) {
            if (!SavingQueue.isSent(name)) {
                Player player = Bukkit.getPlayerExact(name);
                if (player == null || !player.isOnline()) {
                    cacheMap.remove(name.toLowerCase());
                }
            }
        }
    }
}
