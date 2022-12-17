package com.android.settings.notification;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import com.android.settings.R$array;
import com.android.settings.core.BasePreferenceController;

public class SwipeDirectionPreferenceController extends BasePreferenceController implements Preference.OnPreferenceChangeListener {
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

    public SwipeDirectionPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void updateState(Preference preference) {
        ((ListPreference) preference).setValue(String.valueOf(Settings.Secure.getInt(this.mContext.getContentResolver(), "notification_dismiss_rtl", 1)));
        super.updateState(preference);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        Settings.Secure.putInt(this.mContext.getContentResolver(), "notification_dismiss_rtl", Integer.valueOf((String) obj).intValue());
        refreshSummary(preference);
        return true;
    }

    public CharSequence getSummary() {
        int i = Settings.Secure.getInt(this.mContext.getContentResolver(), "notification_dismiss_rtl", 1);
        String[] stringArray = this.mContext.getResources().getStringArray(R$array.swipe_direction_values);
        String[] stringArray2 = this.mContext.getResources().getStringArray(R$array.swipe_direction_titles);
        if (stringArray == null) {
            return null;
        }
        for (int i2 = 0; i2 < stringArray.length; i2++) {
            if (i == Integer.parseInt(stringArray[i2])) {
                return stringArray2[i2];
            }
        }
        return null;
    }
}
