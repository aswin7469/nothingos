package com.android.settings.connecteddevice;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.nfc.NfcPreferenceController;

public class AdvancedConnectedDeviceController extends BasePreferenceController {
    private static final String DRIVING_MODE_SETTINGS_ENABLED = "gearhead:driving_mode_settings_enabled";
    private static final String GEARHEAD_PACKAGE = "com.google.android.projection.gearhead";

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

    public AdvancedConnectedDeviceController(Context context, String str) {
        super(context, str);
    }

    public CharSequence getSummary() {
        Context context = this.mContext;
        return context.getText(getConnectedDevicesSummaryResourceId(context));
    }

    public static int getConnectedDevicesSummaryResourceId(Context context) {
        return getConnectedDevicesSummaryResourceId(new NfcPreferenceController(context, NfcPreferenceController.KEY_TOGGLE_NFC), isDrivingModeAvailable(context), isAndroidAutoSettingAvailable(context));
    }

    static boolean isDrivingModeAvailable(Context context) {
        return Settings.System.getInt(context.getContentResolver(), DRIVING_MODE_SETTINGS_ENABLED, 0) == 1;
    }

    static boolean isAndroidAutoSettingAvailable(Context context) {
        Intent intent = new Intent("com.android.settings.action.IA_SETTINGS");
        intent.setPackage(GEARHEAD_PACKAGE);
        return intent.resolveActivity(context.getPackageManager()) != null;
    }

    static int getConnectedDevicesSummaryResourceId(NfcPreferenceController nfcPreferenceController, boolean z, boolean z2) {
        if (z2) {
            if (nfcPreferenceController.isAvailable()) {
                if (z) {
                    return R$string.connected_devices_dashboard_android_auto_summary;
                }
                return R$string.connected_devices_dashboard_android_auto_no_driving_mode_summary;
            } else if (z) {
                return R$string.connected_devices_dashboard_android_auto_no_nfc_summary;
            } else {
                return R$string.connected_devices_dashboard_android_auto_no_nfc_no_driving_mode;
            }
        } else if (nfcPreferenceController.isAvailable()) {
            if (z) {
                return R$string.connected_devices_dashboard_summary;
            }
            return R$string.connected_devices_dashboard_no_driving_mode_summary;
        } else if (z) {
            return R$string.connected_devices_dashboard_no_nfc_summary;
        } else {
            return R$string.connected_devices_dashboard_no_driving_mode_no_nfc_summary;
        }
    }
}
