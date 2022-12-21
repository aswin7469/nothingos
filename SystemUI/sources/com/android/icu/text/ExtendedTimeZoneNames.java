package com.android.icu.text;

import android.icu.text.TimeZoneNames;
import android.icu.util.ULocale;

public class ExtendedTimeZoneNames {
    private ExtendedTimeZoneNames() {
        throw new RuntimeException("Stub!");
    }

    public static ExtendedTimeZoneNames getInstance(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public TimeZoneNames getTimeZoneNames() {
        throw new RuntimeException("Stub!");
    }

    public Match matchName(CharSequence charSequence, int i, String str) {
        throw new RuntimeException("Stub!");
    }

    public static final class Match {
        private Match() {
            throw new RuntimeException("Stub!");
        }

        public int getMatchLength() {
            throw new RuntimeException("Stub!");
        }

        public String getTzId() {
            throw new RuntimeException("Stub!");
        }

        public boolean isDst() {
            throw new RuntimeException("Stub!");
        }
    }
}
