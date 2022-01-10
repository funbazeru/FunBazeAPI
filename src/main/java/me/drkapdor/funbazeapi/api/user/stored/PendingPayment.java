package me.drkapdor.funbazeapi.api.user.stored;

import java.util.UUID;

/**
 * Запись выставленного на оплату счёта за статус VIP
 * @author DrKapdor
 */

public class PendingPayment {

    private final UUID uuid;
    private final PaymentPeriod paymentPeriod;
    private final long creationDate;

    /**
     * Конструктор заиси
     * @param uuid Уникальный идентификатор
     * @param paymentPeriod Период оплаты статуса VIP
     */

    public PendingPayment(UUID uuid, PaymentPeriod paymentPeriod) {
        this.uuid = uuid;
        this.paymentPeriod = paymentPeriod;
        creationDate = System.currentTimeMillis();
    }

    /**
     * Получить уникальный идентификатор счёта
     * @return Уникальный идентификатор
     */

    public UUID getUUID() {
        return uuid;
    }

    /**
     * Получить период оплаты счёта
     * @return Период оплаты
     */

    public PaymentPeriod getPeriod() {
        return paymentPeriod;
    }

    /**
     * Получить дату выставления счёта
     * @return Дата выставления счёта
     */

    public long getCreationDate() {
        return creationDate;
    }
}
