package com.nothing.settings.deviceinfo.aboutphone;

import android.content.Context;
import android.content.IntentFilter;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.Utils;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.deviceinfo.imei.ImeiInfoDialogFragment;
import com.nothing.settings.deviceinfo.aboutphone.DeviceImeiPreference;
import java.util.Objects;

public class ImeiInfoPreferenceController extends BasePreferenceController implements DeviceImeiPreference.OnClickListener {
    public static final String KEY = "nt_device_imei_info";
    public static final int SLOT_0 = 0;
    public static final int SLOT_0_CLICK_INDEX = 21;
    public static final int SLOT_1 = 1;
    public static final int SLOT_1_CLICK_INDEX = 22;
    public static final String TAG = "ImeiInfoPreferenceController";
    private final Context mContext;
    private final Fragment mFragment;

    public int getAvailabilityStatus() {
        return 2;
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

    public ImeiInfoPreferenceController(Context context, Fragment fragment) {
        super(context, KEY);
        this.mContext = context;
        this.mFragment = fragment;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        DeviceImeiPreference deviceImeiPreference = (DeviceImeiPreference) preferenceScreen.findPreference(KEY);
        Objects.requireNonNull(deviceImeiPreference);
        DeviceImeiPreference deviceImeiPreference2 = deviceImeiPreference;
        deviceImeiPreference.setOnClickListener(this);
    }

    private String getImeiTitle(int i, int i2) {
        return this.mContext.getString(i, new Object[]{Integer.valueOf(i2)});
    }

    public void onClick(int i) {
        if (!Utils.isSupportCTPA(this.mContext)) {
            if (i == 21) {
                ImeiInfoDialogFragment.show(this.mFragment, 0, getImeiTitle(R$string.nt_imei_multi_sim, 1));
            } else if (i == 22) {
                ImeiInfoDialogFragment.show(this.mFragment, 1, getImeiTitle(R$string.nt_imei_multi_sim, 2));
            }
        }
    }
}
