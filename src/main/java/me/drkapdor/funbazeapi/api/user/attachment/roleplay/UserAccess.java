package me.drkapdor.funbazeapi.api.user.attachment.roleplay;

import java.util.ArrayList;
import java.util.List;

/**
 * Настройки доступа к учётной записи
 * @author DrKapdor
 */

public class UserAccess {

    private String password;
    private long lastPasswordChangeDate = -1;
    private String recoveryEmail;

    private final List<String> usedPasswords;
    private final List<String> allowedIp;

    public UserAccess() {
        usedPasswords = new ArrayList<>();
        allowedIp = new ArrayList<>();
    }

    /**
     * Возвращает пароль пользователя в зашифрованном виде
     * @return Пароль пользователя
     */

    public String getPassword() {
        return password;
    }

    /**
     * Устанавливает пароль пользователя
     * @param password Пароль
     */

    public void setPassword(String password) {
        if (usedPasswords.size() >= 3)
            usedPasswords.clear();
        usedPasswords.add(password);
        this.password = password;
        lastPasswordChangeDate = System.currentTimeMillis();
    }

    /**
     * Возвращает адрес почтового ящика для восстановления пароля
     * @return Адрес почтового ящика
     */

    public String getRecoveryEmail() {
        return recoveryEmail;
    }

    /**
     * Устанавливает адрес почтового ящика для восстановления пароля
     * @param email Адрес почтового ящика
     */

    public void setRecoveryEmail(String email) {
        recoveryEmail = email;
    }

    /**
     * Проверяет наличие почтового ящика для восстановления пароля
     * @return Есть почтовый ящик для восстановления пароля
     */

    public boolean hasRecoveryEmail() {
        return recoveryEmail != null && !recoveryEmail.isEmpty();
    }

    /**
     * Даёт доступ к учётной записи с некоторого IP адреса
     * @param ip IP адрес
     */

    public void allowIp(String ip) {
        allowedIp.add(ip);
    }

    /**
     * Блокирует доступ к учётной записи с некоторого IP адреса
     * @param ip IP адрес
     */

    public void disallowIp(String ip) {
        allowedIp.remove(ip);
    }

    /**
     * Проверяет наличие доступа к учётной записи с некоторого IP адреса
     * @param ip IP адрес
     * @return Возможно ли получить доступ
     */

    public boolean isIpAllowed(String ip) {
        return allowedIp.contains(ip);
    }

    /**
     * Возвращает дату последней смены пароля
     * @return Дата последней смены пароля
     */

    public long getLastPasswordChangeDate() {
        return lastPasswordChangeDate;
    }
}
