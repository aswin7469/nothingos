package com.android.settings.bluetooth;

import androidx.preference.Preference;
/* loaded from: classes.dex */
public interface BleBroadcastSourceInfoPreferenceCallback {
    void onBroadcastSourceInfoAdded(Preference preference);

    void onBroadcastSourceInfoRemoved(Preference preference);
}
