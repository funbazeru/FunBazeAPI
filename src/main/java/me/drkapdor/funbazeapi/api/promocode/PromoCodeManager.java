package me.drkapdor.funbazeapi.api.promocode;

import me.drkapdor.funbazeapi.api.user.FBUser;

import java.util.HashMap;
import java.util.Map;

public class PromoCodeManager {

    private final Map<String, CustomPromoCode> map = new HashMap<>();

    /**
     * Активировать промокод от имени паользователя
     * @param id Идентификатор промокода
     * @param user Пользователь
     * @return Успешен ли результат
     */

    public boolean activate(String id, FBUser user) {
        CustomPromoCode promoCode = map.get(id);
        if (promoCode != null) {
            promoCode.grant(user);
            return true;
        }
        return false;
    }

    /**
     * Зарегистрировать новый промокод
     * @param promoCode Промокод
     */

    public void register(CustomPromoCode promoCode) {
        map.put(promoCode.getId(), promoCode);
    }
}
