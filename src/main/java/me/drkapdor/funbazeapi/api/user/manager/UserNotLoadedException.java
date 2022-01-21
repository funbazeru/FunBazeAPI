package me.drkapdor.funbazeapi.api.user.manager;

import me.drkapdor.funbazeapi.ApiPlugin;
import me.drkapdor.funbazeapi.api.FunBazeApi;

public class UserNotLoadedException extends Exception {

    public UserNotLoadedException(String nickname) {
        super("Информация о пользователе " + nickname + " ещё не загружена из хранилища данных!");
        ApiPlugin.getApi().getUserManager().load(nickname, CacheMethod.OFFLINE_REQUEST);
    }
}
