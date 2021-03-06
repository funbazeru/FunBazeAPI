package me.drkapdor.funbazeapi.api.user.attachment.roleplay;

import java.util.HashMap;
import java.util.Map;

/**
 * Метаданные ролевого персонажа пользователя
 * @author DrKapdor
 */

public class UserMeta {

    private UserGender gender;
    private int age;
    private final HashMap<String, Long> joinTime = new HashMap<>();
    private final HashMap<String, Long> playedTime = new HashMap<>();

    /**
     * Конструктор блока метаданных
     *
     * @param gender Биологический пол персонажа
     * @param age Возраст персонажа
     */

    public UserMeta(UserGender gender, int age) {
        this.gender = gender;
        this.age = age;
    }

    /**
     * Возвращает биологический пол персонажа
     * @return Биологический пол
     */

    public UserGender getGender() {
        return gender;
    }

    /**
     * Устанавливает биологический пол персонажа
     * @param gender Биологический пол
     */

    public void setGender(UserGender gender) {
        this.gender = gender;
    }

    /**
     * Возвращает возраст персонажа
     * @return Возраст
     */

    public int getAge() {
        return age;
    }

    /**
     * Устанавливает возраст персонажа
     * @param age Возраст
     */

    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Возвращает крайнюю дату выбора роли
     *
     * @param job Роль
     * @return Дата выбора
     */

    public long getJoinDate(String job) {
        return joinTime.containsKey(job) ? joinTime.get(job) : 0;
    }

    /**
     * Устанавливает крайнюю дату выбора роли
     *
     * @param job Роль
     * @param joinTime Дата выбора
     */

    public void setJoinDate(String job, long joinTime) {
        this.joinTime.put(job, joinTime);
    }

    /**
     * Возвращает отыгранное на роли время
     *
     * @param job Роль
     * @return Время отыгровки
     */

    public long getPlayedTime(String job) {
        return playedTime.containsKey(job) ?  playedTime.get(job) : 0;
    }

    /**
     * Возвращает общее время, отыгранное на ролях
     * @return Время отыгровки на всех ролях
     */

    public long getPlayerTime() {
        long totally = 0;
        for (long played : playedTime.values())
            totally += played;
        return totally;
    }

    /**
     * Добавляет отыгранное на роли время
     *
     * @param job Роль
     * @param playedTime Время отыгровки
     */

    public void addPlayedTime(String job, long playedTime) {
        this.playedTime.put(job, (this.playedTime.containsKey(job) ? this.playedTime.get(job) : 0) + playedTime);
    }

    /**
     * Возвращает любимую роль пользователя, основываясь на статистике времени отыгровки
     * @return Любимя роль пользователя (по статистике)
     */

    public String getFavoriteRole() {
        String favorite = "";
        long max = 0;
        for (Map.Entry<String, Long> entry : playedTime.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                favorite = entry.getKey();
            }
        }
        return favorite;
    }

}
