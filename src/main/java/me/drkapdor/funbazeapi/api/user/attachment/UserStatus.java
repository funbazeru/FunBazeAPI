package me.drkapdor.funbazeapi.api.user.attachment;

/**
 * Пользовательский статус
 * @author DrKapdor
 */

public enum UserStatus {

    DEFAULT("§7Игрок", ""),
    VIP("§6VIP", "§6ᵛᴵᴾ"),
    MODER("§9Модератор", "§9ᴹᴰᴿ"),
    MANAGER("§5Менеджер", "§5ᴹᴳᴿ"),
    ADMIN("§4Администратор", "§4ᴬᴰᴹ");

    private final transient String display;
    private final transient String signature;

    UserStatus(String display, String signature) {
        this.display = display;
        this.signature = signature;
    }

    public String getDisplay() {
        return display;
    }

    public String getSignature() {
        return signature;
    }

    public boolean isStaff() {
        return this != DEFAULT && this != VIP;
    }
}
