package libcore.internal;

import android.net.wifi.WifiEnterpriseConfig;
import com.android.settingslib.datetime.ZoneGetter;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class Java11LanguageFeatures {
    public static String collectUpperCaseStrings(List<String> list) {
        return (String) list.stream().map(new Java11LanguageFeatures$$ExternalSyntheticLambda0()).collect(Collectors.joining(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER));
    }

    public static String guessTheString(String str) {
        if (!("The answer to the universe, life and everything" instanceof String)) {
            return null;
        }
        if (!str.equals("The answer to the universe, life and everything")) {
            return "";
        }
        return "The answer to the universe, life and everything";
    }

    public static class Person {
        private final String name;
        private final Vocabulary vocabulary = new Vocabulary();

        public Person(String str) {
            this.name = str;
        }

        public String greet() throws NoSuchMethodException, InvocationTargetException, NoSuchFieldException, IllegalAccessException {
            return (String) Vocabulary.class.getDeclaredMethod("greet", new Class[0]).invoke(this.vocabulary, new Object[0]);
        }

        private class Vocabulary {
            private Vocabulary() {
            }

            private String greet() throws NoSuchFieldException, IllegalAccessException {
                return "Hello " + ((String) Person.class.getDeclaredField(ZoneGetter.KEY_DISPLAYNAME).get(Person.this));
            }
        }
    }
}
