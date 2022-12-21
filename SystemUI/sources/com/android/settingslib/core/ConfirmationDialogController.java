package com.android.settingslib.core;

import androidx.preference.Preference;

public interface ConfirmationDialogController {
    void dismissConfirmationDialog();

    String getPreferenceKey();

    boolean isConfirmationDialogShowing();

    void showConfirmationDialog(Preference preference);
}
