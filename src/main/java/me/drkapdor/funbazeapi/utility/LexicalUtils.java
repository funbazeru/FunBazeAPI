package me.drkapdor.funbazeapi.utility;

import java.util.Arrays;
import java.util.List;

/**
 * Утилита для проверки этичности содержимого строк
 * @author DrKapdor
 */

public class LexicalUtils {

    /**
     * Список слов-триггеров (ругательства)
     */
    public static final List<String> SWEAR_TRIGGERS = Arrays.asList(
            "чмо", "хуй", "хуе", "хуё", "хуи", "хуя", "говн", "ебан", "пизда", "пиздо", "бляд", "блят",
            "ебл", "пизд", "пезд", "елда", "елду", "елдой", "манда", "мандавошка", "мондовошка",
            "мудак", "мудень", "мудозвон", "мудазвон", "ебать", "педик", "гомосек", "целка",
            "выеб", "выиб", "дроч", "пидор", "пидр", "пидар", "пидер", "шалава", "мандовошка",
            "дрист", "дресн", "дрисн", "пизд", "высер", "выср", "высир", "сука", "мраз", "шлюх"
    );

    /**
     * Список слов-триггеров (запрещённые профессии)
     */
    public static final List<String> BLOCKED_JOBS = Arrays.asList(
            "админ", "оп", "ор", "читер", "администратор", "модератор", "модер", "хелпер", "президент", "призеден",
            "чиновник", "милиция", "омон", "вип", "донат", "бебра", "снюс", "морген", "хакер", "петух", "капдор", "гнег",
            "гнегк", "фбр", "мвд", "донатер"
    );

    /**
     * Проверить, содержит ли строка ругательство
     *
     * @param word Проверяемая строка
     * @return Содержит ли строка ругательство
     */

    public static boolean isWordSwearing(String word) {
        for (String swearing : SWEAR_TRIGGERS) {
            if (word.toLowerCase().contains(swearing))
                return true;
        }
        return false;
    }

    /**
     * Проверить, является ли профессия запрещённой
     *
     * @param job Название профессии
     * @return Является ли профессия запрещённой
     */

    public static boolean isJobBlocked(String job) {
        for (String swearing : BLOCKED_JOBS) {
            if (job.toLowerCase().equalsIgnoreCase(swearing))
                return true;
        }
        return false;
    }

    /**
     * Вернуть строку с заглавной буквы вне зависимости от регистра
     *
     * @param str Строка на вход
     * @return Строка на выход
     */

    public static String capitalize(String str) {
        return str.toUpperCase().charAt(0) + str.toLowerCase().substring(1);
    }
}
