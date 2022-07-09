package me.drkapdor.funbazeapi.api.user.records;

/**
 * Запись скина пользователя
 * @author DrKapdor
 */

public class UserSkin {

    private final String value;
    private final String signature;

    /**
     * Конструктор записи скина пользователя
     * @param value Закодированное в Base64 значение скина
     * @param signature Закодированная в Base64 сигнатура скина
     */

    public UserSkin(String value, String signature) {
        this.value = value;
        this.signature = signature;
    }

    /**
     * Получить закодированное в Base64 значение скина
     * @return Значение скина
     */

    public String getValue() {
        return value;
    }

    /**
     * Получить закодированную в Base64 сигнатуру скина
     * @return Сигнатура скина
     */

    public String getSignature() {
        return signature;
    }
}
