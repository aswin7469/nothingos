package com.nothing.settings.apps;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;
import androidx.preference.Preference;
import com.android.settings.core.BasePreferenceController;

public class AssistantConfigsPreferenceController extends BasePreferenceController {
    private static final String KEY = "nt_assistant_all_settings";

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AssistantConfigsPreferenceController(Context context, String str) {
        super(context, str);
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!KEY.equals(preference.getKey())) {
            return super.handlePreferenceTreeClick(preference);
        }
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.google.android.googlequicksearchbox", "com.google.android.apps.gsa.assistant.settings.AssistantSettingsActivity"));
            preference.getContext().startActivityForResult("AssistantConfigsPreferenceController", intent, 0, (Bundle) null);
            return true;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(preference.getContext(), "Google assistant not found!", 0).show();
            return true;
        }
    }
}
