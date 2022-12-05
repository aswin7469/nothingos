package com.nt.settings.about;

import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.deviceinfo.simstatus.SimStatusDialogFragment;
import com.android.settings.slices.SliceBackgroundWorker;
import com.nt.settings.about.NtAboutPhoneSimPreference;
/* loaded from: classes2.dex */
public class NtSimsPreferenceController extends BasePreferenceController implements NtAboutPhoneSimPreference.OnClickListener {
    public static final String KEY = "nt_device_sims";
    public static final String TAG = "NtSimsPreferenceCtrl";
    private Context mContext;
    private final Fragment mFragment;
    private NtAboutPhoneSimPreference mPreference;

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return 1;
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

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public NtSimsPreferenceController(Context context, Fragment fragment) {
        super(context, KEY);
        this.mContext = context;
        this.mFragment = fragment;
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        NtAboutPhoneSimPreference ntAboutPhoneSimPreference = (NtAboutPhoneSimPreference) preferenceScreen.findPreference(KEY);
        this.mPreference = ntAboutPhoneSimPreference;
        ntAboutPhoneSimPreference.setOnClickListener(this);
    }

    private String getSimTitle(int i, int i2) {
        return this.mContext.getString(i, Integer.valueOf(i2));
    }

    @Override // com.nt.settings.about.NtAboutPhoneSimPreference.OnClickListener
    public void onClick(int i) {
        Log.d(TAG, "@_@ ------ onClick item_type = " + i);
        if (i == 21) {
            SimStatusDialogFragment.show(this.mFragment, 0, getSimTitle(R.string.sim_card_number_title, 1));
        } else if (i != 22) {
        } else {
            SimStatusDialogFragment.show(this.mFragment, 1, getSimTitle(R.string.sim_card_number_title, 2));
        }
    }
}
