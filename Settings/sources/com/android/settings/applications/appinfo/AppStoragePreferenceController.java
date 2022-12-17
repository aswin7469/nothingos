package com.android.settings.applications.appinfo;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.format.Formatter;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.applications.AppStorageSettings;
import com.android.settings.applications.FetchPackageStorageAsyncLoader;
import com.android.settingslib.applications.AppUtils;
import com.android.settingslib.applications.StorageStatsSource;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;

public class AppStoragePreferenceController extends AppInfoPreferenceControllerBase implements LoaderManager.LoaderCallbacks<StorageStatsSource.AppStorageStats>, LifecycleObserver, OnResume, OnPause {
    private StorageStatsSource.AppStorageStats mLastResult;

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

    public void onLoaderReset(Loader<StorageStatsSource.AppStorageStats> loader) {
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AppStoragePreferenceController(Context context, String str) {
        super(context, str);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference.setEnabled(AppUtils.isAppInstalled(this.mAppEntry));
    }

    public void updateState(Preference preference) {
        if (!AppUtils.isAppInstalled(this.mAppEntry)) {
            preference.setSummary((CharSequence) "");
        } else {
            preference.setSummary(getStorageSummary(this.mLastResult, (this.mAppEntry.info.flags & 262144) != 0));
        }
    }

    public void onResume() {
        this.mParent.getLoaderManager().restartLoader(3, Bundle.EMPTY, this);
    }

    public void onPause() {
        this.mParent.getLoaderManager().destroyLoader(3);
    }

    /* access modifiers changed from: protected */
    public Class<? extends SettingsPreferenceFragment> getDetailFragmentClass() {
        return AppStorageSettings.class;
    }

    /* access modifiers changed from: package-private */
    public CharSequence getStorageSummary(StorageStatsSource.AppStorageStats appStorageStats, boolean z) {
        int i;
        if (appStorageStats == null) {
            return this.mContext.getText(R$string.computing_size);
        }
        Context context = this.mContext;
        if (z) {
            i = R$string.storage_type_external;
        } else {
            i = R$string.storage_type_internal;
        }
        String string = context.getString(i);
        Context context2 = this.mContext;
        return context2.getString(R$string.storage_summary_format, new Object[]{Formatter.formatFileSize(context2, appStorageStats.getTotalBytes()), string.toString()});
    }

    public Loader<StorageStatsSource.AppStorageStats> onCreateLoader(int i, Bundle bundle) {
        Context context = this.mContext;
        return new FetchPackageStorageAsyncLoader(context, new StorageStatsSource(context), this.mParent.getAppEntry().info, UserHandle.of(UserHandle.myUserId()));
    }

    public void onLoadFinished(Loader<StorageStatsSource.AppStorageStats> loader, StorageStatsSource.AppStorageStats appStorageStats) {
        this.mLastResult = appStorageStats;
        updateState(this.mPreference);
    }
}
