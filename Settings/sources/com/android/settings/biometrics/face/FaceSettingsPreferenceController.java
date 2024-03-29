package com.android.settings.biometrics.face;

import android.content.Context;
import android.content.IntentFilter;
import com.android.settings.core.TogglePreferenceController;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;

public abstract class FaceSettingsPreferenceController extends TogglePreferenceController {
    private int mUserId;

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public int getSliceHighlightMenuRes() {
        return 0;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public final boolean isSliceable() {
        return false;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public FaceSettingsPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void setUserId(int i) {
        this.mUserId = i;
    }

    /* access modifiers changed from: protected */
    public int getUserId() {
        return this.mUserId;
    }

    /* access modifiers changed from: protected */
    public RestrictedLockUtils.EnforcedAdmin getRestrictingAdmin() {
        return RestrictedLockUtilsInternal.checkIfKeyguardFeaturesDisabled(this.mContext, 128, this.mUserId);
    }
}
