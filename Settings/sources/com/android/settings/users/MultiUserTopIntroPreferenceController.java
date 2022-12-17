package com.android.settings.users;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.Preference;
import com.android.settings.core.BasePreferenceController;

public class MultiUserTopIntroPreferenceController extends BasePreferenceController {
    final UserCapabilities mUserCaps;

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

    public MultiUserTopIntroPreferenceController(Context context, String str) {
        super(context, str);
        this.mUserCaps = UserCapabilities.create(context);
    }

    public int getAvailabilityStatus() {
        UserCapabilities userCapabilities = this.mUserCaps;
        return (!userCapabilities.mEnabled || userCapabilities.mUserSwitcherEnabled) ? 4 : 1;
    }

    public void updateState(Preference preference) {
        this.mUserCaps.updateAddUserCapabilities(this.mContext);
        preference.setVisible(isAvailable());
    }
}
