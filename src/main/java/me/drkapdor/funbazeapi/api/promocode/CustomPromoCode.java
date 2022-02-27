package me.drkapdor.funbazeapi.api.promocode;

import me.drkapdor.funbazeapi.api.user.FBUser;

public abstract class CustomPromoCode {

    private final String id;

    public CustomPromoCode(String id) {
        this.id = id;
    }

    /**
     * Получить идентификатор промокода
     * @return Идентификатор промокода
     */

    public String getId() {
        return id;
    }

    /**
     * Метод, вызываемый при активации промокода
     * @param user Пользователь, от имени которого активируется промокод
     */

    public abstract void grant(FBUser user);
}
