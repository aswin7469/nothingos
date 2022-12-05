package com.android.settings.emergency;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
/* loaded from: classes.dex */
public class EmergencyGestureEntrypointPreferenceController extends BasePreferenceController {
    static final String ACTION_EMERGENCY_GESTURE_SETTINGS = "com.android.settings.action.emergency_gesture_settings";
    private static final String TAG = "EmergencyGestureEntry";
    Intent mIntent;
    private boolean mUseCustomIntent;

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public EmergencyGestureEntrypointPreferenceController(Context context, String str) {
        super(context, str);
        String string = context.getResources().getString(R.string.emergency_gesture_settings_package);
        if (!TextUtils.isEmpty(string)) {
            Intent intent = new Intent(ACTION_EMERGENCY_GESTURE_SETTINGS).setPackage(string);
            if (!canResolveIntent(intent)) {
                return;
            }
            this.mUseCustomIntent = true;
            this.mIntent = intent;
        }
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        super.updateState(preference);
        boolean canHandleClicks = canHandleClicks();
        if (preference != null) {
            preference.setEnabled(canHandleClicks);
        }
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public boolean handlePreferenceTreeClick(Preference preference) {
        Intent intent;
        if (TextUtils.equals(getPreferenceKey(), preference.getKey()) && (intent = this.mIntent) != null) {
            intent.setFlags(67108864);
            this.mContext.startActivity(this.mIntent);
            return true;
        }
        return super.handlePreferenceTreeClick(preference);
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return (this.mContext.getResources().getBoolean(R.bool.config_show_emergency_gesture_settings) && canHandleClicks()) ? 0 : 3;
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    /* renamed from: getSummary */
    public CharSequence mo485getSummary() {
        if (this.mUseCustomIntent) {
            String string = this.mContext.getResources().getString(R.string.emergency_gesture_settings_package);
            try {
                PackageManager packageManager = this.mContext.getPackageManager();
                return this.mContext.getString(R.string.emergency_gesture_entrypoint_summary, packageManager.getApplicationInfo(string, 33280).loadLabel(packageManager));
            } catch (Exception unused) {
                Log.d(TAG, "Failed to get custom summary, falling back.");
                return super.mo485getSummary();
            }
        }
        return super.mo485getSummary();
    }

    public boolean shouldSuppressFromSearch() {
        return this.mUseCustomIntent;
    }

    private boolean canHandleClicks() {
        return !this.mUseCustomIntent || this.mIntent != null;
    }

    private boolean canResolveIntent(Intent intent) {
        return this.mContext.getPackageManager().resolveActivity(intent, 0) != null;
    }
}
