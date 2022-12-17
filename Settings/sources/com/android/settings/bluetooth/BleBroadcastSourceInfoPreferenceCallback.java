package com.android.settings.bluetooth;

import androidx.preference.Preference;

public interface BleBroadcastSourceInfoPreferenceCallback {
    void onBroadcastSourceInfoAdded(Preference preference);

    void onBroadcastSourceInfoRemoved(Preference preference);
}
