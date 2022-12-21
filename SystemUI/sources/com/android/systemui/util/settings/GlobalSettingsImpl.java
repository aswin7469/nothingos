package com.android.systemui.util.settings;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.Settings;
import javax.inject.Inject;

class GlobalSettingsImpl implements GlobalSettings {
    private final ContentResolver mContentResolver;

    @Inject
    GlobalSettingsImpl(ContentResolver contentResolver) {
        this.mContentResolver = contentResolver;
    }

    public ContentResolver getContentResolver() {
        return this.mContentResolver;
    }

    public Uri getUriFor(String str) {
        return Settings.Global.getUriFor(str);
    }

    public String getStringForUser(String str, int i) {
        return Settings.Global.getStringForUser(this.mContentResolver, str, i);
    }

    public boolean putString(String str, String str2, boolean z) {
        throw new UnsupportedOperationException("This method only exists publicly for Settings.System and Settings.Secure");
    }

    public boolean putStringForUser(String str, String str2, int i) {
        return Settings.Global.putStringForUser(this.mContentResolver, str, str2, i);
    }

    public boolean putStringForUser(String str, String str2, String str3, boolean z, int i, boolean z2) {
        return Settings.Global.putStringForUser(this.mContentResolver, str, str2, str3, z, i, z2);
    }

    public boolean putString(String str, String str2, String str3, boolean z) {
        return Settings.Global.putString(this.mContentResolver, str, str2, str3, z);
    }
}
