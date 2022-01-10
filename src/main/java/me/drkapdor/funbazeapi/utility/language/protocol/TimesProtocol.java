package me.drkapdor.funbazeapi.utility.language.protocol;

import java.util.concurrent.TimeUnit;

public class TimesProtocol implements LanguageProtocol {

    @Override
    public String convert(Object... args) {
        try {
            long time = (long) args[0];
            long days = TimeUnit.MILLISECONDS.toDays(time);
            time -= TimeUnit.DAYS.toMillis(days);
            long hours = TimeUnit.MILLISECONDS.toHours(time);
            time -= TimeUnit.HOURS.toMillis(hours);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
            time -= TimeUnit.MINUTES.toMillis(minutes);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(time);
            String response = "";
            if (days > 0)
                response += " " + getForm(days, TimeUnit.DAYS);
            if (hours > 0)
                response += " " + getForm(hours, TimeUnit.HOURS);
            if (minutes > 0)
                response += " " + getForm(minutes, TimeUnit.MINUTES);
            if (seconds > 0)
                response += " " + getForm(seconds, TimeUnit.SECONDS);
            return response.substring(1);
        } catch (Exception exception) {
            return "0 секунд";
        }
    }

    private String getForm(long value, TimeUnit unit) {
        String stringed = String.valueOf(value);
        int ending;
        if (value > 9) ending = Integer.parseInt(stringed.substring(stringed.length() - 2));
        else ending = Integer.parseInt(stringed);
        switch (unit) {
            case DAYS: {
                if (ending >= 10 && ending <= 20)
                    return value + " дней";
                if (ending % 10 >= 5)
                    return value + " дней";
                if (ending % 10 >= 2 || ending >= 2 && ending < 4)
                    return value + " дня";
                if (ending % 10 == 1 || ending == 1)
                    return value + " день";
                if (ending % 10 == 0)
                    return value + " дней";
            }
            case HOURS: {
                if (ending >= 10 && ending <= 20)
                    return value + " часов";
                if (ending % 10 >= 5)
                    return value + " часов";
                if (ending % 10 >= 2 || ending >= 2 && ending < 4)
                    return value + " часа";
                if (ending % 10 == 1 || ending == 1)
                    return value + " час";
                if (ending % 10 == 0)
                    return value + " часов";
            }
            case MINUTES: {
                if (ending >= 10 && ending <= 20)
                    return value + " минут";
                if (ending % 10 >= 5)
                    return value + " минут";
                if (ending % 10 >= 2 || ending >= 2 && ending < 4)
                    return value + " минуты";
                if (ending % 10 == 1 || ending == 1)
                    return value + " минута";
                if (ending % 10 == 0)
                    return value + " минту";
            }
            case SECONDS: {
                if (ending >= 10 && ending <= 20)
                    return value + " секунд";
                if (ending % 10 >= 5)
                    return value + " секунд";
                if (ending % 10 >= 2 || ending >= 2 && ending < 4)
                    return value + " секунды";
                if (ending % 10 == 1 || ending == 1)
                    return value + " секунда";
                if (ending % 10 == 0)
                    return value + " секунд";
            }
        }
        return "0 секунд";
    }
}
