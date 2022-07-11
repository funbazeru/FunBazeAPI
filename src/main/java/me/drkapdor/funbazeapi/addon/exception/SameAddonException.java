package me.drkapdor.funbazeapi.addon.exception;

/**
 * Исключение, вызываемое в момент подгрузки двух дополнений с одинаковыми именами
 * @author DrKapdor
 */

public class SameAddonException extends Exception {

    public SameAddonException(String message) {
        super(message);
    }

}
