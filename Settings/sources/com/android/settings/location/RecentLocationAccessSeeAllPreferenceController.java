package com.android.settings.location;

import android.content.Context;
import android.content.IntentFilter;
import android.os.UserManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.location.RecentLocationAccesses;
import com.android.settingslib.widget.AppPreference;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class RecentLocationAccessSeeAllPreferenceController extends LocationBasePreferenceController {
    private PreferenceScreen mCategoryAllRecentLocationAccess;
    private Preference mPreference;
    private final RecentLocationAccesses mRecentLocationAccesses;
    private boolean mShowSystem = false;
    private int mType = 3;

    @Override // com.android.settings.location.LocationBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.location.LocationBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.location.LocationBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.location.LocationBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.location.LocationBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.location.LocationBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    @Override // com.android.settings.location.LocationBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    @Override // com.android.settings.location.LocationBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public RecentLocationAccessSeeAllPreferenceController(Context context, String str) {
        super(context, str);
        this.mRecentLocationAccesses = new RecentLocationAccesses(context);
    }

    @Override // com.android.settings.location.LocationBasePreferenceController, com.android.settings.location.LocationEnabler.LocationModeChangeListener
    public void onLocationModeChanged(int i, boolean z) {
        this.mCategoryAllRecentLocationAccess.setEnabled(this.mLocationEnabler.isEnabled(i));
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mCategoryAllRecentLocationAccess = (PreferenceScreen) preferenceScreen.findPreference(getPreferenceKey());
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        this.mCategoryAllRecentLocationAccess.removeAll();
        this.mPreference = preference;
        UserManager userManager = UserManager.get(this.mContext);
        ArrayList<RecentLocationAccesses.Access> arrayList = new ArrayList();
        for (RecentLocationAccesses.Access access : this.mRecentLocationAccesses.getAppListSorted(this.mShowSystem)) {
            if (RecentLocationAccessPreferenceController.isRequestMatchesProfileType(userManager, access, this.mType)) {
                arrayList.add(access);
            }
        }
        if (arrayList.isEmpty()) {
            AppPreference appPreference = new AppPreference(this.mContext);
            appPreference.setTitle(R.string.location_no_recent_apps);
            appPreference.setSelectable(false);
            this.mCategoryAllRecentLocationAccess.addPreference(appPreference);
            return;
        }
        for (RecentLocationAccesses.Access access2 : arrayList) {
            this.mCategoryAllRecentLocationAccess.addPreference(RecentLocationAccessPreferenceController.createAppPreference(preference.getContext(), access2, this.mFragment));
        }
    }

    public void setProfileType(int i) {
        this.mType = i;
    }

    public void setShowSystem(boolean z) {
        this.mShowSystem = z;
        Preference preference = this.mPreference;
        if (preference != null) {
            updateState(preference);
        }
    }
}
