package com.android.systemui.util.settings;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.Settings;
import javax.inject.Inject;

class SystemSettingsImpl implements SystemSettings {
    private final ContentResolver mContentResolver;

    @Inject
    SystemSettingsImpl(ContentResolver contentResolver) {
        this.mContentResolver = contentResolver;
    }

    public ContentResolver getContentResolver() {
        return this.mContentResolver;
    }

    public Uri getUriFor(String str) {
        return Settings.System.getUriFor(str);
    }

    public String getStringForUser(String str, int i) {
        return Settings.System.getStringForUser(this.mContentResolver, str, i);
    }

    public boolean putString(String str, String str2, boolean z) {
        return Settings.System.putString(this.mContentResolver, str, str2, z);
    }

    public boolean putStringForUser(String str, String str2, int i) {
        return Settings.System.putStringForUser(this.mContentResolver, str, str2, i);
    }

    public boolean putStringForUser(String str, String str2, String str3, boolean z, int i, boolean z2) {
        throw new UnsupportedOperationException("This method only exists publicly for Settings.Secure and Settings.Global");
    }

    public boolean putString(String str, String str2, String str3, boolean z) {
        throw new UnsupportedOperationException("This method only exists publicly for Settings.Secure and Settings.Global");
    }
}
