package me.drkapdor.funbazeapi.api.skin;

/**
 * Вариация скина
 * @author DrKapdor
 */

public enum SkinVariant {

    /**
     * Классический (Стив)
     */
    CLASSIC("classic"),

    /**
     * Стройный (Алекс)
     */
    SLIM("slim");

    private final String type;

    SkinVariant(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

}
