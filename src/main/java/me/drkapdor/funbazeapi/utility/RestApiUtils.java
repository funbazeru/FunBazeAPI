package me.drkapdor.funbazeapi.utility;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_16;
import static java.util.stream.Collectors.*;

public class RestApiUtils {

    public static Map<String, List<String>> splitQuery(String query) {
        if (query == null || "".equals(query)) {
            return Collections.emptyMap();
        }
        return Pattern.compile("&").splitAsStream(query)
                .map(s -> Arrays.copyOf(s.split("="), 2))
                .collect(groupingBy(s -> decode(s[0]), mapping(s -> decode(s[1]), toList())));
    }

    private static String decode(final String encoded) {
        try {
            return encoded == null ? null : URLDecoder.decode(encoded, UTF_16.toString());
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is a required encoding", e);
        }
    }

    public static String cyrilize(String input) {
        return input
                .replace("à", "а")
                .replace("á", "б")
                .replace("â", "в")
                .replace("ã", "г")
                .replace("ä", "д")
                .replace("å", "е")
                .replace("¸", "ё")
                .replace("æ", "ж")
                .replace("ç", "з")
                .replace("è", "и")
                .replace("é", "й")
                .replace("ê", "к")
                .replace("ë", "л")
                .replace("ì", "м")
                .replace("í", "н")
                .replace("î", "о")
                .replace("ï", "п")
                .replace("ð", "р")
                .replace("ñ", "с")
                .replace("ò", "т")
                .replace("ó", "у")
                .replace("ô", "ф")
                .replace("õ", "х")
                .replace("ö", "ц")
                .replace("÷", "ч")
                .replace("ø", "ш")
                .replace("ù", "щ")
                .replace("ú", "ъ")
                .replace("û", "ы")
                .replace("ü", "ь")
                .replace("ý", "э")
                .replace("þ", "ю")
                .replace("ÿ", "я");
    }

}
