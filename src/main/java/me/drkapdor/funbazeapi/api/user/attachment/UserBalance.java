package me.drkapdor.funbazeapi.api.user.attachment;

/**
 * Баланс пользователя
 * @author DrKapdor
 */

public class UserBalance {

    private static final int LIMIT = 1000000000;

    private int money; //Игровая валюта (рубли)
    private int vouchers; //Донат-валюта (ваучеры)
    private int rating; //Социальный рейтинг

    public UserBalance() {
    }

    /**
     * Конструктор баланса пользователя
     * @param money Игровая валюта
     * @param vouchers Донат-валюта
     * @param rating Социальный рейтинг
     */

    public UserBalance(int money, int vouchers, int rating) {
        this.money = money;
        this.vouchers = vouchers;
        this.rating = rating;
    }

    /**
     * Изъять с баланса пользователя некоторое кол-во игровой валюты
     * @param money Кол-во игровой валюты
     * @return Успешен ли результат
     */

    public boolean takeMoney(int money) {
        if (this.money < money) return false;
        this.money -= money;
        return true;
    }

    /**
     * Начислить на баланс пользователя некоторое кол-во игровой валюты
     * @param money Кол-во игровой валюты
     * @return Успешен ли результат
     */

    public boolean addMoney(int money) {
        if (this.money + money > LIMIT) return false;
        this.money += money;
        return true;
    }

    /**
     * Проверить наличие некоторой суммы игровой валюты
     * @param money Некоторая сумма в игровой валюте
     * @return Имеет ли пользовать некоторое кол-во игровой валюты
     */

    public boolean hasMoney(int money) {
        return this.money >= money;
    }

    /**
     * Получить кол-во игровой валюты пользователя
     * @return Кол-во игровой валюты
     */

    public int getMoney() {
        return money;
    }

    /**
     * Изъять с баланса пользователя некоторое кол-во донат-валюты
     * @param vouchers Кол-во донат-валюты
     * @return Успешен ли результат
     */

    public boolean takeVouchers(int vouchers) {
        if (this.vouchers < vouchers) return false;
        this.vouchers -= vouchers;
        return true;
    }

    /**
     * Начислить на баланс пользователя некоторое кол-во донат-валюты
     * @param vouchers Кол-во донат-валюты
     * @return Успешен ли результат
     */

    public boolean addVouchers(int vouchers) {
        if (this.vouchers + vouchers > LIMIT) return false;
        this.vouchers += vouchers;
        return true;
    }

    /**
     * Проверить наличие некоторой суммы в донат-валюты
     * @param vouchers Некоторая сумма в донат-валюте
     * @return Имеет ли пользовать некоторое кол-во донат-валюты
     */

    public boolean hasVouchers(int vouchers) {
        return this.vouchers >= vouchers;
    }

    /**
     * Получить количество донат-валюты
     * @return Кол-во донат валюты
     */

    public int getVouchers() {
        return vouchers;
    }

    /**
     * Добавить пользователю некоторое кол-во социального рейтинга
     * @param rating Кол-во социального рейтинга
     */

    public void addRating(int rating) {
        this.rating += rating;
    }

    /**
     * Отчислить некоторое кол-во социального рейтина пользователя
     * @param rating Кол-во социального рейтинга
     */

    public void takeRating(int rating) {
        this.rating -= rating;
    }

    /**
     * Получить значение социального рейтинга пользователя
     * @return Значение социального рейтинга
     */

    public int getRating() {
        return rating;
    }

}
