package me.drkapdor.funbazeapi.api.promocode;

import me.drkapdor.funbazeapi.api.user.FBUser;

/**
 * Абстрактный промокод, предусматривающий дальнейшую
 * реализаци метода выдачи награды за его активацию.
 *
 * @see CustomPromoCode#grant(FBUser)
 * @author DrKapdor
 */

public abstract class CustomPromoCode {

    private final String id;

    /**
     * Конструктор промокода
     * @param id Идентификатор
     */

    public CustomPromoCode(String id) {
        this.id = id;
    }

    /**
     * Возвращает идентификатор промокода
     * @return Идентификатор промокода
     */

    public String getId() {
        return id;
    }

    /**
     * Вызывается в случае активации пользователем промокода
     * @param user Пользователь, от имени которого активируется промокод
     */

    public abstract void grant(FBUser user);
}
