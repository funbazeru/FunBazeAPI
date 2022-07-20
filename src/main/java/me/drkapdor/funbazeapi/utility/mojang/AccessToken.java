package me.drkapdor.funbazeapi.utility.mojang;

import me.drkapdor.funbazeapi.FunBazeApiPlugin;

import java.util.Base64;

/**
 * Генерируемый токен для доступа к аккаунту Mojang
 * @author DrKapdor
 */

public class AccessToken {

    private final String uuid;
    private final String token;

    protected AccessToken(String uuid, String token) {
        this.uuid = uuid;
        this.token = token;
    }

    /**
     * Получить уникальный идентификатор токена
     * @return Уникальный идентификатор
     */

    public String getUUID() {
        return uuid;
    }

    /**
     * Проверить, является ли токен валидным
     * @return Является ли токен валидным
     */

    public boolean isValid() {
        return  FunBazeApiPlugin.getJsonParser().parse(new String(Base64.getDecoder().decode(token.split("\\.")[1]))).getAsJsonObject().get("exp").getAsLong() > System.currentTimeMillis();
    }

    public void refresh() {

    }

    @Override
    public String toString() {
        return token;
    }
}
