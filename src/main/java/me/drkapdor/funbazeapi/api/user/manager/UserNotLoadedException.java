package me.drkapdor.funbazeapi.api.user.manager;

import me.drkapdor.funbazeapi.api.FunBazeApi;

public class UserNotLoadedException extends Exception {

    public UserNotLoadedException(String nickname) {
        super("Информация о пользователе " + nickname + " ещё не загружена из хранилища данных!");
        FunBazeApi.getUserManager().load(nickname, CacheMethod.OFFLINE_REQUEST);
    }
}
