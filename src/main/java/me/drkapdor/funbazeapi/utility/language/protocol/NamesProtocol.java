package me.drkapdor.funbazeapi.utility.language.protocol;

import me.drkapdor.funbazeapi.api.user.attachment.roleplay.UserGender;
import me.drkapdor.funbazeapi.utility.language.protocol.name.Case;

public class NamesProtocol implements LanguageProtocol {

    public static void main(String[] args) {
        System.out.println(convertName("Полковник", UserGender.MALE, Case.GENITIVE));
    }

    @Override
    public String convert(Object... args) {
        if (args.length == 3) {
            String name = (String) args[0];
            UserGender gender = (UserGender) args[1];
            Case nameCase = (Case) args[2];
            return convertName(name, gender, nameCase);
        } else if (args.length == 4) {
            String name = (String) args[0];
            String surname = (String) args[1];
            UserGender gender = (UserGender) args[2];
            Case nameCase = (Case) args[3];
            return convertName(name, gender, nameCase) + " " + convertName(surname, gender, nameCase);
        }
        return null;
    }

    private static String convertName(String name, UserGender gender, Case type) {
        String substring1 = name.substring(0, name.length() - 2);
        String s = name.substring(0, name.length() - 1) + "ой";
        final boolean b1 = name.endsWith("рий") ||
                name.endsWith("сий") ||
                name.endsWith("лий") ||
                name.endsWith("вий") ||
                name.endsWith("мий") ||
                name.endsWith("фий") ||
                name.endsWith("хий") ||
                name.endsWith("ций") ||
                name.endsWith("пий");
        String s6 = name.substring(0, name.length() - 1) + "ю";
        String s4 = name.substring(0, name.length() - 1) + "е";
        if (gender == UserGender.MALE) {
            switch (type) {
                case NOMINATIVE: {
                    return name;
                }
                case ACCUSATIVE: {
                    if (name.endsWith("а"))
                        return name.substring(0, name.length() - 1) + "у";
                    else if (name.endsWith("б"))
                        return name + "а";
                    else if (name.endsWith("в"))
                        return name + "а";
                    else if (name.endsWith("г"))
                        return name + "а";
                    else if (name.endsWith("д"))
                        return name + "а";
                    else if (name.endsWith("е"))
                        return name;
                    else if (name.endsWith("ж"))
                        return name + "у";
                    else if (name.endsWith("и"))
                        return name;
                    else if (b1)
                        return name.substring(0, name.length() - 1) + "я";
                    else if (name.endsWith("кий"))
                        return substring1 + "ого";
                    else if (name.endsWith("чий") ||
                            name.endsWith("oий"))
                        return substring1 + "ьего";
                    else if (name.endsWith("ший"))
                        return name.substring(0, name.length() - 2) + "его";
                    else if (name.endsWith("ий"))
                        return substring1 + "ия";
                    else if (name.endsWith("ый"))
                        return substring1 + "ого";
                    else if (name.endsWith("ой"))
                        return substring1 + "ого";
                    else if (name.endsWith("й"))
                        return name.substring(0, name.length() - 1) + "я";
                    else if (name.endsWith("к"))
                        return name + "а";
                    else if (name.endsWith("ел"))
                        return substring1 + "ла";
                    else if (name.endsWith("л"))
                        return name + "а";
                    else if (name.endsWith("м"))
                        return name + "а";
                    else if (name.endsWith("н"))
                        return name + "а";
                    else if (name.endsWith("о"))
                        return name;
                    else if (name.endsWith("п"))
                        return name + "а";
                    else if (name.endsWith("рь"))
                        return name.substring(0, name.length() - 1) + "я";
                    else if (name.endsWith("р"))
                        return name + "а";
                    else if (name.endsWith("с"))
                        return name + "а";
                    else if (name.endsWith("т"))
                        return name + "а";
                    else if (name.endsWith("у"))
                        return name;
                    else if (name.endsWith("ф"))
                        return name + "а";
                    else if (name.endsWith("х"))
                        return name + "а";
                    else if (name.endsWith("ч"))
                        return name + "а";
                    else if (name.endsWith("ц"))
                        return name + "а";
                    else if (name.endsWith("з"))
                        return name + "а";
                    else if (name.endsWith("ш"))
                        return name + "а";
                    else if (name.endsWith("ъ"))
                        return name;
                    else if (name.endsWith("щ"))
                        return name + "а";
                    else if (name.endsWith("ь"))
                        return name.substring(0, name.length() - 1) + "я";
                    else if (name.endsWith("э"))
                        return name;
                    else if (name.endsWith("ю"))
                        return name;
                    else if (name.endsWith("я"))
                        return name;
                }
                case GENITIVE: {
                    if (name.endsWith("ша") || name.endsWith("ка"))
                        return name.substring(0, name.length() - 1) + "и";
                    else if (name.endsWith("ха"))
                        return name.substring(0, name.length() - 1) + "и";
                    else if (name.endsWith("а"))
                        return name.substring(0, name.length() - 1) + "ы";
                    else if (name.endsWith("б"))
                        return name + "а";
                    else if (name.endsWith("в"))
                        return name + "а";
                    else if (name.endsWith("г"))
                        return name + "а";
                    else if (name.endsWith("д"))
                        return name + "а";
                    else if (name.endsWith("ел"))
                        return substring1 + "ла";
                    else if (name.endsWith("е"))
                        return name;
                    else if (name.endsWith("ж"))
                        return name + "а";
                    else if (name.endsWith("и"))
                        return name;
                    else if (b1)
                        return name.substring(0, name.length() - 1) + "я";
                    else if (name.endsWith("кий"))
                        return substring1 + "ого";
                    else if (name.endsWith("чий") ||
                            name.endsWith("oий"))
                        return substring1 + "ьего";
                    else if (name.endsWith("ший"))
                        return name.substring(0, name.length() - 2) + "его";
                    else if (name.endsWith("ий"))
                        return substring1 + "ия";
                    else if (name.endsWith("ый") ||
                            name.endsWith("ой"))
                        return substring1 + "ого";
                    else if (name.endsWith("й"))
                        return name.substring(0, name.length() - 1) + "я";
                    else if (name.endsWith("к"))
                        return name + "а";
                    else if (name.endsWith("л"))
                        return name + "а";
                    else if (name.endsWith("м"))
                        return name + "а";
                    else if (name.endsWith("н"))
                        return name + "а";
                    else if (name.endsWith("о"))
                        return name;
                    else if (name.endsWith("п"))
                        return name + "а";
                    else if (name.endsWith("рь"))
                        return name.substring(0, name.length() - 1) + "я";
                    else if (name.endsWith("р"))
                        return name + "а";
                    else if (name.endsWith("с"))
                        return name + "а";
                    else if (name.endsWith("т"))
                        return name + "а";
                    else if (name.endsWith("у"))
                        return name;
                    else if (name.endsWith("ф"))
                        return name + "а";
                    else if (name.endsWith("х"))
                        return name + "а";
                    else if (name.endsWith("ч"))
                        return name + "а";
                    else if (name.endsWith("ц"))
                        return name + "а";
                    else if (name.endsWith("з"))
                        return name + "а";
                    else if (name.endsWith("ш"))
                        return name + "а";
                    else if (name.endsWith("ъ"))
                        return name;
                    else if (name.endsWith("щ"))
                        return name + "а";
                    else if (name.endsWith("ь"))
                        return name.substring(0, name.length() - 1) + "я";
                    else if (name.endsWith("э"))
                        return name;
                    else if (name.endsWith("ю"))
                        return name;
                    else if (name.endsWith("я"))
                        return name;
                }
                case DATIVE: {
                    if (name.endsWith("а"))
                        return s4;
                    else if (name.endsWith("б"))
                        return name + "у";
                    else if (name.endsWith("в"))
                        return name + "у";
                    else if (name.endsWith("г"))
                        return name + "у";
                    else if (name.endsWith("д"))
                        return name + "у";
                    else if (name.endsWith("ел"))
                        return substring1 + "лу";
                    else if (name.endsWith("е"))
                        return name;
                    else if (name.endsWith("ж"))
                        return name + "у";
                    else if (name.endsWith("и"))
                        return name;
                    else if (b1)
                        return s6;
                    else if (name.endsWith("кий"))
                        return substring1 + "ому";
                    else if (name.endsWith("чий") ||
                            name.endsWith("oий"))
                        return substring1 + "ьиму";
                    else if (name.endsWith("ший"))
                        return name.substring(0, name.length() - 2) + "ему";
                    else if (name.endsWith("ий"))
                        return substring1 + "ию";
                    else if (name.endsWith("ый") ||
                            name.endsWith("ой"))
                        return substring1 + "ому";
                    else if (name.endsWith("й"))
                        return s6;
                    else if (name.endsWith("к"))
                        return name + "у";
                    else if (name.endsWith("л"))
                        return name + "у";
                    else if (name.endsWith("м"))
                        return name + "у";
                    else if (name.endsWith("н"))
                        return name + "у";
                    else if (name.endsWith("о"))
                        return name;
                    else if (name.endsWith("п"))
                        return name + "у";
                    else if (name.endsWith("рь"))
                        return s6;
                    else if (name.endsWith("р"))
                        return name + "у";
                    else if (name.endsWith("с"))
                        return name + "у";
                    else if (name.endsWith("т"))
                        return name + "у";
                    else if (name.endsWith("у"))
                        return name;
                    else if (name.endsWith("ф"))
                        return name + "у";
                    else if (name.endsWith("х"))
                        return name + "у";
                    else if (name.endsWith("ц"))
                        return name + "у";
                    else if (name.endsWith("з"))
                        return name + "у";
                    else if (name.endsWith("ч"))
                        return name + "у";
                    else if (name.endsWith("ш"))
                        return name + "у";
                    else if (name.endsWith("ъ"))
                        return name;
                    else if (name.endsWith("щ"))
                        return name + "у";
                    else if (name.endsWith("ь"))
                        return s6;
                    else if (name.endsWith("э"))
                        return name;
                    else if (name.endsWith("ю"))
                        return name;
                    else if (name.endsWith("я"))
                        return name;
                }
                case INSTRUMENTAL: {
                    if (name.endsWith("а"))
                        return s;
                    else if (name.endsWith("б"))
                        return name + "ом";
                    else if (name.endsWith("ев"))
                        return name + "ым";
                    else if (name.endsWith("в"))
                        return name + "ым";
                    else if (name.endsWith("г"))
                        return name + "ом";
                    else if (name.endsWith("д"))
                        return name + "ом";
                    else if (name.endsWith("ел"))
                        return substring1 + "ом";
                    else if (name.endsWith("е"))
                        return name;
                    else if (name.endsWith("ж"))
                        return name + "ом";
                    else if (name.endsWith("и"))
                        return name;
                    else {
                        String s1 = name.substring(0, name.length() - 1) + "ем";
                        if (b1)
                            return s1;
                        else if (name.endsWith("кий"))
                            return substring1 + "им";
                        else if (name.endsWith("чий") ||
                                name.endsWith("щий"))
                            return substring1 + "ьим";
                        else if (name.endsWith("ший"))
                            return name.substring(0, name.length() - 2) + "им";
                        else if (name.endsWith("ий"))
                            return substring1 + "ием";
                        else if (name.endsWith("ый") ||
                                name.endsWith("ой"))
                            return substring1 + "ым";
                        else if (name.endsWith("й"))
                            return s1;
                        else if (name.endsWith("к"))
                            return name + "ом";
                        else if (name.endsWith("л"))
                            return name + "ом";
                        else if (name.endsWith("м"))
                            return name + "ом";
                        else if (name.endsWith("тин"))
                            return name + "ом";
                        else if (name.endsWith("н"))
                            return name + "ым";
                        else if (name.endsWith("о"))
                            return name;
                        else if (name.endsWith("п"))
                            return name + "ом";
                        else if (name.endsWith("рь"))
                            return s1;
                        else if (name.endsWith("р"))
                            return name + "ом";
                        else if (name.endsWith("с"))
                            return name + "ом";
                        else if (name.endsWith("т"))
                            return name + "ом";
                        else if (name.endsWith("у"))
                            return name;
                        else if (name.endsWith("ф"))
                            return name + "ом";
                        else if (name.endsWith("х"))
                            return name + "ом";
                        else if (name.endsWith("ч"))
                            return name + "ем";
                        else if (name.endsWith("ц"))
                            return name + "ем";
                        else if (name.endsWith("з"))
                            return name + "ом";
                        else if (name.endsWith("ш"))
                            return name + "ом";
                        else if (name.endsWith("ъ"))
                            return name;
                        else if (name.endsWith("щ"))
                            return name + "у";
                        else if (name.endsWith("ь"))
                            return s1;
                        else if (name.endsWith("э"))
                            return name;
                        else if (name.endsWith("ю"))
                            return name;
                        else if (name.endsWith("я"))
                            return name;
                    }
                }
                case PREPOSITIONS: {
                    if (name.endsWith("а"))
                        return s4;
                    else if (name.endsWith("б"))
                        return name + "е";
                    else if (name.endsWith("ев"))
                        return name + "е";
                    else if (name.endsWith("в"))
                        return name + "е";
                    else if (name.endsWith("г"))
                        return name + "е";
                    else if (name.endsWith("д"))
                        return name + "е";
                    else if (name.endsWith("ел"))
                        return substring1 + "ле";
                    else if (name.endsWith("е"))
                        return name;
                    else if (name.endsWith("ж"))
                        return name + "е";
                    else if (name.endsWith("и"))
                        return name;
                    else if (b1)
                        return s4;
                    else if (name.endsWith("кий"))
                        return substring1 + "ом";
                    else if (name.endsWith("чий") ||
                            name.endsWith("щий"))
                        return substring1 + "ьием";
                    else if (name.endsWith("ший"))
                        return name.substring(0, name.length() - 2) + "ем";
                    else if (name.endsWith("ий"))
                        return substring1 + "ие";
                    else if (name.endsWith("ый") ||
                            name.endsWith("ой"))
                        return substring1 + "ом";
                    else if (name.endsWith("й"))
                        return name.substring(0, name.length() - 1) + "е";
                    else if (name.endsWith("к"))
                        return name + "е";
                    else if (name.endsWith("л"))
                        return name + "е";
                    else if (name.endsWith("м"))
                        return name + "е";
                    else if (name.endsWith("н"))
                        return name + "е";
                    else if (name.endsWith("о"))
                        return name;
                    else if (name.endsWith("п"))
                        return name + "е";
                    else if (name.endsWith("рь"))
                        return s4;
                    else if (name.endsWith("р"))
                        return name + "е";
                    else if (name.endsWith("с"))
                        return name + "е";
                    else if (name.endsWith("т"))
                        return name + "е";
                    else if (name.endsWith("у"))
                        return name;
                    else if (name.endsWith("ф"))
                        return name + "е";
                    else if (name.endsWith("х"))
                        return name + "е";
                    else if (name.endsWith("ц"))
                        return name + "е";
                    else if (name.endsWith("з"))
                        return name + "е";
                    else if (name.endsWith("ч"))
                        return name + "е";
                    else if (name.endsWith("ш"))
                        return name + "е";
                    else if (name.endsWith("ъ"))
                        return name;
                    else if (name.endsWith("щ"))
                        return name + "е";
                    else if (name.endsWith("ь"))
                        return s4;
                    else if (name.endsWith("э"))
                        return name;
                    else if (name.endsWith("ю"))
                        return name;
                    else if (name.endsWith("я"))
                        return name;
                }
            }
        } else {
            switch (type) {
                case NOMINATIVE: {
                    return name;
                }
                case ACCUSATIVE: {
                    if (name.endsWith("а"))
                        return name.substring(0, name.length() - 1) + "у";
                    else if (name.endsWith("б"))
                        return name + "у";
                    else if (name.endsWith("в"))
                        return name + "у";
                    else if (name.endsWith("г"))
                        return name;
                    else if (name.endsWith("д"))
                        return name;
                    else if (name.endsWith("е"))
                        return name;
                    else if (name.endsWith("ж"))
                        return name;
                    else if (name.endsWith("з"))
                        return name;
                    else if (name.endsWith("и"))
                        return name;
                    else if (name.endsWith("й"))
                        return name;
                    else if (name.endsWith("к"))
                        return name;
                    else if (name.endsWith("л"))
                        return name;
                    else if (name.endsWith("м"))
                        return name;
                    else if (name.endsWith("н"))
                        return name;
                    else if (name.endsWith("о"))
                        return name;
                    else if (name.endsWith("п"))
                        return name;
                    else if (name.endsWith("р"))
                        return name;
                    else if (name.endsWith("с"))
                        return name;
                    else if (name.endsWith("т"))
                        return name + "а";
                    else if (name.endsWith("у"))
                        return name;
                    else if (name.endsWith("ф"))
                        return name;
                    else if (name.endsWith("х"))
                        return name;
                    else if (name.endsWith("ц"))
                        return name;
                    else if (name.endsWith("ч"))
                        return name;
                    else if (name.endsWith("ш"))
                        return name + "а";
                    else if (name.endsWith("ъ"))
                        return name;
                    else if (name.endsWith("щ"))
                        return name;
                    else if (name.endsWith("ь"))
                        return name;
                    else if (name.endsWith("э"))
                        return name;
                    else if (name.endsWith("ю"))
                        return name;
                    else if (name.endsWith("я"))
                        return s6;
                }
                case GENITIVE: {
                    if (b1)
                        return name;
                    else if (name.endsWith("ья"))
                        return substring1 + "ьи";
                    else if (name.endsWith("ий"))
                        return substring1;
                    else if (name.endsWith("ия"))
                        return substring1 + "ии";
                    else if (name.endsWith("ая"))
                        return substring1;
                    else if (name.endsWith("ва"))
                        return name.substring(0, name.length() - 1) + "ой";
                    else if (name.endsWith("а"))
                        return name.substring(0, name.length() - 1) + "и";
                    else if (name.endsWith("б"))
                        return name + "ы";
                    else if (name.endsWith("в"))
                        return name + "ы";
                    else if (name.endsWith("г"))
                        return name + "и";
                    else if (name.endsWith("д"))
                        return name + "ы";
                    else if (name.endsWith("е"))
                        return name;
                    else if (name.endsWith("ж"))
                        return name + "а";
                    else if (name.endsWith("и"))
                        return name;
                    else if (name.endsWith("й"))
                        return name;
                    else if (name.endsWith("к"))
                        return name;
                    else if (name.endsWith("л"))
                        return name;
                    else if (name.endsWith("м"))
                        return name;
                    else if (name.endsWith("н"))
                        return name;
                    else if (name.endsWith("о"))
                        return name;
                    else if (name.endsWith("п"))
                        return name;
                    else if (name.endsWith("р"))
                        return name;
                    else if (name.endsWith("с"))
                        return name;
                    else if (name.endsWith("т"))
                        return name;
                    else if (name.endsWith("у"))
                        return name;
                    else if (name.endsWith("ф"))
                        return name;
                    else if (name.endsWith("х"))
                        return name;
                    else if (name.endsWith("ч"))
                        return name;
                    else if (name.endsWith("ш"))
                        return name;
                    else if (name.endsWith("ъ"))
                        return name;
                    else if (name.endsWith("щ"))
                        return name;
                    else if (name.endsWith("ь"))
                        return name;
                    else if (name.endsWith("э"))
                        return name;
                    else if (name.endsWith("ю"))
                        return name;
                }
                case DATIVE: {
                    if (b1)
                        return name;
                    else if (name.endsWith("ий"))
                        return substring1 + "ей";
                    else if (name.endsWith("ая"))
                        return substring1 + "и";
                    else if (name.endsWith("ия"))
                        return substring1 + "ии";
                    else if (name.endsWith("й"))
                        return name;
                    else if (name.endsWith("ра") ||
                            name.endsWith("да") ||
                            name.endsWith("на") ||
                            name.endsWith("фа") ||
                            name.endsWith("па") ||
                            name.endsWith("ла"))
                        return name.substring(0, name.length() - 1) + "е";
                    else if (name.endsWith("ва"))
                        return name.substring(0, name.length() - 1) + "ой";
                    else if (name.endsWith("а"))
                        return s4;
                    else if (name.endsWith("б"))
                        return name;
                    else if (name.endsWith("в"))
                        return name;
                    else if (name.endsWith("г"))
                        return name;
                    else if (name.endsWith("д"))
                        return name;
                    else if (name.endsWith("е"))
                        return name;
                    else if (name.endsWith("ж"))
                        return name;
                    else if (name.endsWith("и"))
                        return name;
                    else if (name.endsWith("к"))
                        return name;
                    else if (name.endsWith("л"))
                        return name;
                    else if (name.endsWith("м"))
                        return name;
                    else if (name.endsWith("н"))
                        return name;
                    else if (name.endsWith("о"))
                        return name;
                    else if (name.endsWith("п"))
                        return name;
                    else if (name.endsWith("р"))
                        return name;
                    else if (name.endsWith("с"))
                        return name;
                    else if (name.endsWith("т"))
                        return name;
                    else if (name.endsWith("у"))
                        return name;
                    else if (name.endsWith("ф"))
                        return name;
                    else if (name.endsWith("х"))
                        return name;
                    else if (name.endsWith("ч"))
                        return name;
                    else if (name.endsWith("ш"))
                        return name;
                    else if (name.endsWith("ъ"))
                        return name;
                    else if (name.endsWith("щ"))
                        return name;
                    else if (name.endsWith("ь"))
                        return name;
                    else if (name.endsWith("э"))
                        return name;
                    else if (name.endsWith("ю"))
                        return name;
                    else if (name.endsWith("ья"))
                        return substring1 + "ье";
                }
                case INSTRUMENTAL: {
                    if (b1)
                        return name;
                    else if (name.endsWith("ья"))
                        return name.substring(0, name.length() - 1) + "ей";
                    else if (name.endsWith("ий"))
                        return substring1 + "ей";
                    else if (name.endsWith("ая"))
                        return substring1 + "ой";
                    else if (name.endsWith("ва"))
                        return name.substring(0, name.length() - 1) + "ой";
                    else if (name.endsWith("а"))
                        return s;
                    else if (name.endsWith("б"))
                        return name;
                    else if (name.endsWith("ев"))
                        return name;
                    else if (name.endsWith("в"))
                        return name;
                    else if (name.endsWith("г"))
                        return name;
                    else if (name.endsWith("д"))
                        return name;
                    else if (name.endsWith("е"))
                        return name;
                    else if (name.endsWith("ж"))
                        return name;
                    else if (name.endsWith("и"))
                        return name;
                    else if (name.endsWith("й"))
                        return name;
                    else if (name.endsWith("к"))
                        return name;
                    else if (name.endsWith("л"))
                        return name;
                    else if (name.endsWith("м"))
                        return name;
                    else if (name.endsWith("н"))
                        return name;
                    else if (name.endsWith("о"))
                        return name;
                    else if (name.endsWith("п"))
                        return name;
                    else if (name.endsWith("р"))
                        return name;
                    else if (name.endsWith("с"))
                        return name;
                    else if (name.endsWith("т"))
                        return name;
                    else if (name.endsWith("у"))
                        return name;
                    else if (name.endsWith("ф"))
                        return name;
                    else if (name.endsWith("х"))
                        return name + "ом";
                    else if (name.endsWith("ч"))
                        return name + "ем";
                    else if (name.endsWith("ш"))
                        return name + "ом";
                    else if (name.endsWith("ъ"))
                        return name;
                    else if (name.endsWith("щ"))
                        return name + "у";
                    else if (name.endsWith("ь"))
                        return name;
                    else if (name.endsWith("э"))
                        return name;
                    else if (name.endsWith("ю"))
                        return name;
                    else if (name.endsWith("я"))
                        return name.substring(0, name.length() - 1) + "и";
                }
                case PREPOSITIONS: {
                    if (name.endsWith("ва"))
                        return name.substring(0, name.length() - 1) + "ой";
                    else if (name.endsWith("а"))
                        return s4;
                    else if (name.endsWith("б"))
                        return name;
                    else if (name.endsWith("в"))
                        return name;
                    else if (name.endsWith("г"))
                        return name;
                    else if (name.endsWith("д"))
                        return name;
                    else if (name.endsWith("е"))
                        return name;
                    else if (name.endsWith("ж"))
                        return name;
                    else if (b1)
                        return name;
                    else if (name.endsWith("ий"))
                        return substring1 + "ей";
                    else if (name.endsWith("ая"))
                        return substring1 + "ой";
                    else if (name.endsWith("й"))
                        return name;
                    else if (name.endsWith("к"))
                        return name;
                    else if (name.endsWith("л"))
                        return name;
                    else if (name.endsWith("м"))
                        return name;
                    else if (name.endsWith("н"))
                        return name;
                    else if (name.endsWith("о"))
                        return name;
                    else if (name.endsWith("п"))
                        return name;
                    else if (name.endsWith("р"))
                        return name;
                    else if (name.endsWith("с"))
                        return name;
                    else if (name.endsWith("т"))
                        return name;
                    else if (name.endsWith("у"))
                        return name;
                    else if (name.endsWith("ф"))
                        return name;
                    else if (name.endsWith("х"))
                        return name;
                    else if (name.endsWith("ч"))
                        return name;
                    else if (name.endsWith("ш"))
                        return name;
                    else if (name.endsWith("ъ"))
                        return name;
                    else if (name.endsWith("щ"))
                        return name;
                    else if (name.endsWith("ь"))
                        return name;
                    else if (name.endsWith("э"))
                        return name;
                    else if (name.endsWith("ю"))
                        return name;
                    else if (name.endsWith("ья"))
                        return s4;
                    else if (name.endsWith("я"))
                        return name.substring(0, name.length() - 1) + "и";
                }
            }
        }
        return name;
    }
}
