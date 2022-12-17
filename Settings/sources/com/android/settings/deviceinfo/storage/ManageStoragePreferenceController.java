package com.android.settings.deviceinfo.storage;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Pair;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.overlay.FeatureFactory;

public class ManageStoragePreferenceController extends BasePreferenceController {
    private Drawable mManageStorageDrawable;
    private int mUserId;

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

    public ManageStoragePreferenceController(Context context, String str) {
        super(context, str);
    }

    public void setUserId(int i) {
        this.mUserId = i;
        this.mManageStorageDrawable = StorageUtils.getManageStorageIcon(this.mContext, i);
    }

    public int getAvailabilityStatus() {
        return this.mManageStorageDrawable == null ? 2 : 0;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        preferenceScreen.findPreference(getPreferenceKey()).setIcon(this.mManageStorageDrawable);
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals(getPreferenceKey(), preference.getKey())) {
            return super.handlePreferenceTreeClick(preference);
        }
        FeatureFactory.getFactory(this.mContext).getMetricsFeatureProvider().action(this.mContext, 840, (Pair<Integer, Object>[]) new Pair[0]);
        Intent intent = new Intent("android.os.storage.action.MANAGE_STORAGE");
        intent.addFlags(268435456);
        this.mContext.startActivityAsUser(intent, new UserHandle(this.mUserId));
        return true;
    }
}
