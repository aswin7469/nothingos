package com.android.settings.deviceinfo;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$bool;
import com.android.settings.Utils;
import com.android.settings.core.BasePreferenceController;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class HardwareInfoPreferenceController extends BasePreferenceController {
    private static final String TAG = "DeviceModelPrefCtrl";

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

    public HardwareInfoPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
    }

    public int getAvailabilityStatus() {
        return this.mContext.getResources().getBoolean(R$bool.config_show_device_model) ? 0 : 3;
    }

    public CharSequence getSummary() {
        if (!Utils.isSupportCTPA(this.mContext)) {
            return getDeviceModel();
        }
        String string = Utils.getString(this.mContext, "ext_model_name_from_meta");
        return (string == null || string.isEmpty()) ? getDeviceModel() : string;
    }

    public static String getDeviceModel() {
        FutureTask futureTask = new FutureTask(new HardwareInfoPreferenceController$$ExternalSyntheticLambda0());
        futureTask.run();
        try {
            return Build.MODEL + ((String) futureTask.get());
        } catch (ExecutionException unused) {
            Log.e(TAG, "Execution error, so we only show model name");
            return Build.MODEL;
        } catch (InterruptedException unused2) {
            Log.e(TAG, "Interruption error, so we only show model name");
            return Build.MODEL;
        }
    }
}
