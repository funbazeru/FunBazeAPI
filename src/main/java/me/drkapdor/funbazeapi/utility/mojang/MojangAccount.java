package me.drkapdor.funbazeapi.utility.mojang;

public class MojangAccount {

    private final String userName;
    private final String password;
    private final String uuid;

    public MojangAccount(String login, String password, String uuid) {
        this.userName = login;
        this.password = password;
        this.uuid = uuid;
    }

    protected String getUserName() {
        return userName;
    }

    protected String getPassword() {
        return password;
    }

    public String getUUID() {
        return uuid;
    }
}
