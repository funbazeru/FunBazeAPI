package me.drkapdor.funbazeapi.api.user.attachment;

import me.drkapdor.funbazeapi.api.user.attachment.roleplay.PromotionData;
import me.drkapdor.funbazeapi.api.user.attachment.roleplay.UserGender;
import me.drkapdor.funbazeapi.api.user.attachment.roleplay.UserMeta;
import me.drkapdor.funbazeapi.api.user.stored.PendingPayment;

/**
 * Атрибуты учётной записи пользователя
 * @author DrKapdor
 */

public class UserData {

    private long registerDate;
    private long vipExpirationDate;
    private boolean hasDiscordLinkedBefore;
    private long skinChangeDate;
    private UserStatus status;
    private UserMeta meta;
    private UserBalance balance;
    private PendingPayment pendingPayment;
    private PromotionData promotionData;

    public UserData() {
        registerDate = System.currentTimeMillis();
        status = UserStatus.DEFAULT;
        meta = new UserMeta(UserGender.MALE, 18);
        balance = new UserBalance();
        promotionData = new PromotionData();
    }

    /**
     * Получить дату регистрации пользователя
     * @return Дата регистрации пользователя
     */

    public long getRegisterDate() {
        return registerDate;
    }

    /**
     * Установить дату регистрации пользователя
     * @param registerDate Дата регистрации пользователя
     */

    public void setRegisterDate(long registerDate) {
        this.registerDate = registerDate;
    }

    /**
     * Получить дату истечения срока действия статуса VIP
     * @return Дата истечения срока
     */

    public long getVipExpirationDate() {
        return vipExpirationDate;
    }

    /**
     * Установить дату истечения срока действия статуса VIP
     * @param vipExpirationDate Дата истечения срока
     */

    public void setVipExpirationDate(long vipExpirationDate) {
        this.vipExpirationDate = vipExpirationDate;
    }

    /**
     * Пометить то, что игрок игрок привязывал учётную запись к аккаунту Discord ранее
     */

    public void setDiscordLinkedBefore() {
        this.hasDiscordLinkedBefore = true;
    }

    /**
     * Проверить, привязывал ли игрок учётную запись к аккаунту Discord ранее
     * @return Привязывал ли игрок учётную запись
     */

    public boolean hasDiscordLinkedBefore() {
        return hasDiscordLinkedBefore;
    }

    /**
     * Установить дату крайнего изменения скина
     * @param skinChangeDate Дата крайнего изменения скина
     */

    public void setSkinChangeDate(long skinChangeDate) {
        this.skinChangeDate = skinChangeDate;
    }

    /**
     * Получить дату крайней смены скина
     * @return Дата крайней смены скина
     */

    public long getSkinChangeDate() {
        return skinChangeDate;
    }

    /**
     * Получить пользовательский статус учётной записи
     * @return Пользовательский статус
     */

    public UserStatus getStatus() {
        return status;
    }

    /**
     * Установить пользовательский статус учётной записи
     * @param status Пользовательский статус
     */

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    /**
     * Получить метаданные ролевого персонажа
     * @return Метаданные
     */

    public UserMeta getMeta() {
        return meta;
    }

    /**
     * Установить метаданные ролевого персонажа
     * @param meta Метаданные
     */

    public void setMeta(UserMeta meta) {
        this.meta = meta;
    }

    /**
     * Получить баланс пользователя
     * @return Баланс
     */

    public UserBalance getBalance() {
        return balance;
    }

    /**
     * Установить баланс пользователя
     * @param balance Баланс
     */

    public void setBalance(UserBalance balance) {
        this.balance = balance;
    }

    /**
     * Проверить наличие ожидающего оплаты счёта
     * @return Имеется ли ожидающий оплаты счёт
     */

    public boolean hasPendingPayment() {
        return pendingPayment != null;
    }

    /**
     * Получить ожидающий оплаты счёт
     * @return Счёт, подлежащий оплате
     */

    public PendingPayment getPendingPayment() {
        return pendingPayment;
    }

    /**
     * Установить ожидающий оплаты счёт
     * @param pendingPayment Счёт, подлежащий оплате
     */

    public void setPendingPayment(PendingPayment pendingPayment) {
        this.pendingPayment = pendingPayment;
    }

    /**
     * Получить информацию об участии пользователя в различных акциях
     * @return Информация об участии в различных акциях
     */

    public PromotionData getPromotionData() {
        return promotionData;
    }

    /**
     * Установить информацию об участии пользователя в различных акциях
     * @param promotionData Информация об участии в различных акциях
     */

    public void setPromotionData(PromotionData promotionData) {
        this.promotionData = promotionData;
    }
}
