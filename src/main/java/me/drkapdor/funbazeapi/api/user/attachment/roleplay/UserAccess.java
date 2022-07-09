package me.drkapdor.funbazeapi.api.user.attachment.roleplay;

import java.util.ArrayList;
import java.util.List;

/**
 * Настройки доступа к учётной записи
 * @author DrKapdor
 */

public class UserAccess {

    private final List<String> allowedIp;

    public UserAccess() {
        allowedIp = new ArrayList<>();
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
}
