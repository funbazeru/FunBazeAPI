package me.drkapdor.funbazeapi.api.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.drkapdor.funbazeapi.FunBazeApiPlugin;
import me.drkapdor.funbazeapi.api.user.attachment.FBID;
import me.drkapdor.funbazeapi.api.user.attachment.UserData;
import me.drkapdor.funbazeapi.api.user.attachment.UserAccess;
import me.drkapdor.funbazeapi.api.user.attachment.roleplay.UserName;
import me.drkapdor.funbazeapi.api.user.storage.SavingQueue;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Информация об учётной записи пользователя
 * @author DrKapdor
 */

public class FBUser {

    //6280788311630442126, local class serialVersionUID = 2847867950364127031
    //2847867950364127031, local class serialVersionUID = 6280788311630442126

    //private static final long serialVersionUID = 6280788311630442126L;

    private final FBID id;
    private final String nickname;
    private final String ip;
    private final long discordId;
    private UserName userName;
    private UserData userData;
    private UserAccess userAccess;

    /**
     * Конструктор учётной записи
     * @param id Уникальный идентификатор формата {@link FBID}
     * @param nickname Игровой никнейм
     * @param ip IP адрес пользователя
     */

    public FBUser(FBID id, String nickname, String ip) {
        this.id = id;
        this.nickname = nickname;
        this.ip = ip;
        userName = new UserName();
        userData = new UserData();
        discordId = 0;
        userAccess = new UserAccess();
    }

    /**
     * Возвращает уникальный идентификатор формата {@link FBID}
     * @return Уникальный идентификатор формата {@link FBID}
     */

    public FBID getId() {
        return id;
    }

    /**
     * Возвращает никнейм пользователя
     * @return Никнейм пользователя
     */

    public String getNickname() {
        return nickname;
    }

    /**
     * Возвращает IP адрес пользователя
     * @return IP адрес пользователя
     */

    public String getIp() {
        return ip;
    }

    /**
     * Возвращает DiscordID пользователя
     * @return DiscordID адрес пользователя
     */

    public long getDiscordId() {
        return discordId;
    }

    /**
     * Проверяет на привязку учётной записи к Discord
     * @return Учётная запись привязана в Discord
     */

    public boolean hasDiscord() {
        return discordId != 0;
    }

    /**
     * Возвращает ролевое имя пользователя
     * @return Ролевое имя
     */

    public UserName getUserName() {
        return userName;
    }

    /**
     * Устанавливает ролевое имя пользователя
     * @param userName Ролевое имя
     */

    public void setUserName(UserName userName) {
        this.userName = userName;
    }

    /**
     * Устанавливает набор атрибутов пользователя
     * @param userData Набор атрибутов
     */

    public void setData(UserData userData) {
        this.userData = userData;
    }

    /**
     * Возвращает набор атрибутов пользователя
     * @return userData Набор атрибутов
     */

    public UserData getData() {
        return userData;
    }

    /**
     * Возвращает настройки доступа к учётной записи
     * @return Настройки доступа
     */

    public UserAccess getAccess() {
        return userAccess;
    }

    /**
     * Устанавливает настройки доступа к учётной записи
     * @param userAccess Настройки доступа
     */


    public void setAccess(UserAccess userAccess) {
        this.userAccess = userAccess;
    }

    /**
     * Возвращает пользователя в виде игрока {@link Player}
     * @return Игрок
     */

    public Player asBukkit() {
        return Bukkit.getPlayerExact(nickname);
    }

    /**
     * Возвращает список список сгенерированных скинов
     * @return Список скинов
     */

    @Deprecated
    public Map<String, BufferedImage> getCustomSkins() {
        Map<String, BufferedImage> skins = new HashMap<>();
        File userFolder = new File(FunBazeApiPlugin.dataFolder + File.separator + nickname);
        if (userFolder.isDirectory()) {
            File skinsFolder = new File(userFolder + File.separator + "skins");
            if (skinsFolder.isDirectory()) {
                for (File file : Objects.requireNonNull(skinsFolder.listFiles())) {
                    try {
                        skins.put(file.getName().split("\\.")[0], ImageIO.read(file));
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }
        return skins;
    }

    /**
     * Возвращает сгенерированный скин
     * @param skinId Идентификатор скина
     * @return Развертка скина в формате png (64x64 или 64x32)
     */

    @Deprecated
    public BufferedImage getCustomSkin(String skinId) {
        File userFolder = new File(FunBazeApiPlugin.dataFolder + File.separator + nickname);
        if (userFolder.isDirectory()) {
            File skin = new File(userFolder + File.separator + "skins" + File.separator + skinId + ".png");
            if (skin.exists()) {
                try {
                    return ImageIO.read(skin);
                } catch (IOException exception) {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * Возвращает сгенерированный скин
     * @param skinId Идентификатор скина
     * @param byDefault Файл скина, устанавиваемый по умолчанию
     * @return Развертка скина в формате png (64x64 или 64x32)
     */

    @Deprecated
    public BufferedImage getCustomSkin(String skinId, File byDefault) {
        File userFolder = new File(FunBazeApiPlugin.dataFolder + File.separator + nickname);
        if (userFolder.isDirectory()) {
            File skin = new File(userFolder + File.separator + "skins" + File.separator + skinId + ".png");
            try {
                if (skin.exists())
                    return ImageIO.read(skin);
                else return ImageIO.read(byDefault);
            } catch (IOException exception) {
                return null;
            }
        }
        return null;
    }

    /**
     * Сохраняет информацию о пользователе в БД
     * @param create Требуется ли создать новую запись в таблице
     */

    public void save(boolean create) {
        Gson gson = new GsonBuilder().create();
        String name = gson.toJson(userName);
        String data = gson.toJson(userData);
        String access = gson.toJson(userAccess);
        if (create) {
            String sql = "INSERT INTO Players (`ID`, `Nickname`, `Name`, `IP`, `DiscordID`, `Access`, `Data`) VALUES ('" + id + "', '" + nickname + "', '" + name + "', '" +  ip + "', '" +  discordId + "', '" + access + "', '" + data + "')";
            FunBazeApiPlugin.getDatabase().update(sql);
        } else {
            SavingQueue.send(nickname);
        }
    }

    /**
     * Сохранить информацию о пользователе в БД (create = true)
     */

    public void save() {
        save(false);
    }
}
