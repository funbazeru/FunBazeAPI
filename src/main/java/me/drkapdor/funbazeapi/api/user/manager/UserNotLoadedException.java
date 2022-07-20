package me.drkapdor.funbazeapi.api.user.manager;

import me.drkapdor.funbazeapi.FunBazeApiPlugin;

public class UserNotLoadedException extends Exception {

    public UserNotLoadedException(String nickname) {
        super("Информация о пользователе " + nickname + " ещё не загружена из хранилища данных!");
        FunBazeApiPlugin.getApi().getUserManager().load(nickname, CacheMethod.OFFLINE_REQUEST);
    }
}
