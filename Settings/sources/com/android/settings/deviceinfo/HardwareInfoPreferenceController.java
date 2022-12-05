package com.android.settings.deviceinfo;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
/* loaded from: classes.dex */
public class HardwareInfoPreferenceController extends BasePreferenceController {
    private static final String TAG = "DeviceModelPrefCtrl";

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

    public HardwareInfoPreferenceController(Context context, String str) {
        super(context, str);
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return this.mContext.getResources().getBoolean(R.bool.config_show_device_model) ? 0 : 3;
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    /* renamed from: getSummary */
    public CharSequence mo485getSummary() {
        if (Utils.isSupportCTPA(this.mContext)) {
            String string = Utils.getString(this.mContext, "ext_model_name_from_meta");
            return (string == null || string.isEmpty()) ? getDeviceModel() : string;
        }
        return getDeviceModel();
    }

    public static String getDeviceModel() {
        FutureTask futureTask = new FutureTask(HardwareInfoPreferenceController$$ExternalSyntheticLambda0.INSTANCE);
        futureTask.run();
        try {
            return Build.MODEL + ((String) futureTask.get());
        } catch (InterruptedException unused) {
            Log.e(TAG, "Interruption error, so we only show model name");
            return Build.MODEL;
        } catch (ExecutionException unused2) {
            Log.e(TAG, "Execution error, so we only show model name");
            return Build.MODEL;
        }
    }
}
