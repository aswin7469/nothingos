package androidx.core.p004os;

import android.content.res.Configuration;
import android.os.LocaleList;

/* renamed from: androidx.core.os.ConfigurationCompat */
public final class ConfigurationCompat {
    private ConfigurationCompat() {
    }

    public static LocaleListCompat getLocales(Configuration configuration) {
        return LocaleListCompat.wrap(Api24Impl.getLocales(configuration));
    }

    /* renamed from: androidx.core.os.ConfigurationCompat$Api24Impl */
    static class Api24Impl {
        private Api24Impl() {
        }

        static LocaleList getLocales(Configuration configuration) {
            return configuration.getLocales();
        }
    }
}
