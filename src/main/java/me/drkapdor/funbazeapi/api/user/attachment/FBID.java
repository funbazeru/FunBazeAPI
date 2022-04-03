package me.drkapdor.funbazeapi.api.user.attachment;

import me.drkapdor.funbazeapi.ApiPlugin;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Уникальный идентификатор пользователя <b>FunBaze</b><br>
 * Имеет конструкцию вида <b>ббб-ббб-ццц</b>, где <b>б</b> - случайная
 * буква русского алфавита, а <b>ц</b> - случайная цифра, причём <b>0 ≥ ц ≤ 9</b>
 * @author DrKapdor
 */

public class FBID {

    private final static List<String> chars = Arrays.asList("а", "б", "в", "г", "д", "е", "ж", "и", "к", "л", "м", "н", "п", "р", "с", "т", "у", "ф", "х", "ч", "ш");

    private final String id;

    private FBID(String id) {
        this.id = id;
    }

    /**
     * Конвертировать строку в экземпляр {@link FBID}
     *
     * @param id Уникальный идентификатор в формате строки
     * @return Уникальный идентификатор
     */

    public static FBID fromString(String id) {
        String input = id.toLowerCase();
        if (validate(input)) {
            return new FBID(input);
        } else return null;
    }

    /**
     * Создать экземпляр со случайно заданным параметром
     *
     * @return Уникальный идентификатор
     */

    public static FBID random() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 3; i++)
            builder.append(chars.get(random.nextInt(chars.size())));
        builder.append("-");
        for (int i = 0; i < 3; i++)
            builder.append(chars.get(random.nextInt(chars.size())));
        builder.append("-");
        for (int i = 0; i < 3; i++)
            builder.append(random.nextInt(10));
        return fromString(builder.toString());
    }

    /**
     * Проверить, является ли сгенерированный идентификатор уникальным,<br>
     * т.е. не записывался в базу данных ранее
     *
     * @return Является ли идентификатор уникальным
     */

    public boolean isUnique() {
        try {
            return !ApiPlugin.getDatabase().query("SELECT * FROM Players WHERE ID = '" + id + "'").next();
        } catch (SQLException exception) {
            return false;
        }
    }

    @Override
    public String toString() {
        return id;
    }

    private static boolean validate(String id) {
        String[] chunks = id.toLowerCase().split("-");
        return chunks[0].matches("[абвгдежиклмнпрстухфчш]+$") && chunks[1].matches("[абвгдежиклмнпрстухфчш]+$") && chunks[2].matches("[0-9]+$");
    }
}
