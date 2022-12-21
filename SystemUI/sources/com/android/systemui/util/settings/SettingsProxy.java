package com.android.systemui.util.settings;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.provider.Settings;

public interface SettingsProxy {
    ContentResolver getContentResolver();

    String getStringForUser(String str, int i);

    Uri getUriFor(String str);

    boolean putString(String str, String str2, String str3, boolean z);

    boolean putString(String str, String str2, boolean z);

    boolean putStringForUser(String str, String str2, int i);

    boolean putStringForUser(String str, String str2, String str3, boolean z, int i, boolean z2);

    int getUserId() {
        return getContentResolver().getUserId();
    }

    void registerContentObserver(String str, ContentObserver contentObserver) {
        registerContentObserver(getUriFor(str), contentObserver);
    }

    void registerContentObserver(Uri uri, ContentObserver contentObserver) {
        registerContentObserverForUser(uri, contentObserver, getUserId());
    }

    void registerContentObserver(String str, boolean z, ContentObserver contentObserver) {
        registerContentObserver(getUriFor(str), z, contentObserver);
    }

    void registerContentObserver(Uri uri, boolean z, ContentObserver contentObserver) {
        registerContentObserverForUser(uri, z, contentObserver, getUserId());
    }

    void registerContentObserverForUser(String str, ContentObserver contentObserver, int i) {
        registerContentObserverForUser(getUriFor(str), contentObserver, i);
    }

    void registerContentObserverForUser(Uri uri, ContentObserver contentObserver, int i) {
        registerContentObserverForUser(uri, false, contentObserver, i);
    }

    void registerContentObserverForUser(String str, boolean z, ContentObserver contentObserver, int i) {
        registerContentObserverForUser(getUriFor(str), z, contentObserver, i);
    }

    void registerContentObserverForUser(Uri uri, boolean z, ContentObserver contentObserver, int i) {
        getContentResolver().registerContentObserver(uri, z, contentObserver, i);
    }

    void unregisterContentObserver(ContentObserver contentObserver) {
        getContentResolver().unregisterContentObserver(contentObserver);
    }

    String getString(String str) {
        return getStringForUser(str, getUserId());
    }

    boolean putString(String str, String str2) {
        return putStringForUser(str, str2, getUserId());
    }

    int getInt(String str, int i) {
        return getIntForUser(str, i, getUserId());
    }

    int getIntForUser(String str, int i, int i2) {
        String stringForUser = getStringForUser(str, i2);
        if (stringForUser == null) {
            return i;
        }
        try {
            return Integer.parseInt(stringForUser);
        } catch (NumberFormatException unused) {
            return i;
        }
    }

    int getInt(String str) throws Settings.SettingNotFoundException {
        return getIntForUser(str, getUserId());
    }

    int getIntForUser(String str, int i) throws Settings.SettingNotFoundException {
        try {
            return Integer.parseInt(getStringForUser(str, i));
        } catch (NumberFormatException unused) {
            throw new Settings.SettingNotFoundException(str);
        }
    }

    boolean putInt(String str, int i) {
        return putIntForUser(str, i, getUserId());
    }

    boolean putIntForUser(String str, int i, int i2) {
        return putStringForUser(str, Integer.toString(i), i2);
    }

    boolean getBool(String str, boolean z) {
        return getBoolForUser(str, z, getUserId());
    }

    boolean getBoolForUser(String str, boolean z, int i) {
        return getIntForUser(str, z ? 1 : 0, i) != 0;
    }

    boolean getBool(String str) throws Settings.SettingNotFoundException {
        return getBoolForUser(str, getUserId());
    }

    boolean getBoolForUser(String str, int i) throws Settings.SettingNotFoundException {
        return getIntForUser(str, i) != 0;
    }

    boolean putBool(String str, boolean z) {
        return putBoolForUser(str, z, getUserId());
    }

    boolean putBoolForUser(String str, boolean z, int i) {
        return putIntForUser(str, z ? 1 : 0, i);
    }

    long getLong(String str, long j) {
        return getLongForUser(str, j, getUserId());
    }

    long getLongForUser(String str, long j, int i) {
        String stringForUser = getStringForUser(str, i);
        if (stringForUser == null) {
            return j;
        }
        try {
            return Long.parseLong(stringForUser);
        } catch (NumberFormatException unused) {
            return j;
        }
    }

    long getLong(String str) throws Settings.SettingNotFoundException {
        return getLongForUser(str, getUserId());
    }

    long getLongForUser(String str, int i) throws Settings.SettingNotFoundException {
        try {
            return Long.parseLong(getStringForUser(str, i));
        } catch (NumberFormatException unused) {
            throw new Settings.SettingNotFoundException(str);
        }
    }

    boolean putLong(String str, long j) {
        return putLongForUser(str, j, getUserId());
    }

    boolean putLongForUser(String str, long j, int i) {
        return putStringForUser(str, Long.toString(j), i);
    }

    float getFloat(String str, float f) {
        return getFloatForUser(str, f, getUserId());
    }

    float getFloatForUser(String str, float f, int i) {
        String stringForUser = getStringForUser(str, i);
        if (stringForUser == null) {
            return f;
        }
        try {
            return Float.parseFloat(stringForUser);
        } catch (NumberFormatException unused) {
            return f;
        }
    }

    float getFloat(String str) throws Settings.SettingNotFoundException {
        return getFloatForUser(str, getUserId());
    }

    float getFloatForUser(String str, int i) throws Settings.SettingNotFoundException {
        String stringForUser = getStringForUser(str, i);
        if (stringForUser != null) {
            try {
                return Float.parseFloat(stringForUser);
            } catch (NumberFormatException unused) {
                throw new Settings.SettingNotFoundException(str);
            }
        } else {
            throw new Settings.SettingNotFoundException(str);
        }
    }

    boolean putFloat(String str, float f) {
        return putFloatForUser(str, f, getUserId());
    }

    boolean putFloatForUser(String str, float f, int i) {
        return putStringForUser(str, Float.toString(f), i);
    }
}
