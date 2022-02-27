package me.drkapdor.funbazeapi.api.event.roleplay;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Событие, вызываемое в момент окончательной потери игроком сознания
 */

public class PlayerSalaryReceiveEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private int salary;
    private int bonus;

    public PlayerSalaryReceiveEvent(Player player, int salary, int bonus) {
        super(player);
        this.salary = salary;
        this.bonus = bonus;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public String getEventName() {
        return "PlayerSalaryReceiveEvent";
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    /**
     * Получить зарплату, выданную игроку
     * @return Зарплата
     */

    public int getSalary() {
        return salary;
    }

    /**
     * Изменить зарплату, получаемую игроком
     * @param salary Новая зарплата
     */

    public void setSalary(int salary) {
        this.salary = salary;
    }

    /**
     * Изменить бонус к зарплате, получаемой игроком
     * @param bonus Новый бонус к зарплате
     */

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    /**
     * Получить бонус к зарплате, выданной игроку
     * @return Бонус к зарплате
     */

    public int getBonus() {
        return bonus;
    }

    /**
     * Получить суммарно зарплату и бонус
     * @return Сумма зарплаты и бонуса к ней
     */

    public int getSalaryTotal() {
        return salary + bonus;
    }
}
