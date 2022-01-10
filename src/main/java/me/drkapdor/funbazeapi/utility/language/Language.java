package me.drkapdor.funbazeapi.utility.language;

import me.drkapdor.funbazeapi.utility.language.protocol.LanguageProtocol;
import me.drkapdor.funbazeapi.utility.language.protocol.NamesProtocol;
import me.drkapdor.funbazeapi.utility.language.protocol.TimesProtocol;

public class Language {

    public static LanguageProtocol names = new NamesProtocol();
    public static LanguageProtocol times = new TimesProtocol();


}
