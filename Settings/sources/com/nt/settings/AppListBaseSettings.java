package com.nt.settings;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.nt.common.widget.SingleArrayPartitionAdapter;
import com.nt.settings.applications.AppIconMemoryOptimizeHelper;
import com.nt.settings.utils.NtUtils;
import com.nt.settings.widget.NtAppEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/* loaded from: classes2.dex */
public abstract class AppListBaseSettings extends SettingsPreferenceFragment implements LoaderManager.LoaderCallbacks<List<NtAppEntry>> {
    protected AppIconMemoryOptimizeHelper mAppIconMemoryOptimizeHelper;
    protected Context mContext;
    protected SingleArrayPartitionAdapter<NtAppEntry> mGroupAdapter;
    public ListView mListView;
    public HashMap<String, String> mLaunchNames = new HashMap<>();
    public List<NtAppEntry> mAppList = new ArrayList();
    public final int[] mSwitchOffOnSizeArray = {0, 0};
    public int mSwitchOffSize = 0;
    public int mSwitchOnSize = 0;

    public abstract SingleArrayPartitionAdapter<NtAppEntry> createNtSingleArrayGroupAdapter(List<NtAppEntry> list);

    protected abstract AdapterView.OnItemClickListener createOnItemClickListener();

    public abstract Loader<List<NtAppEntry>> onCreateAppLoader(int i, Bundle bundle);

    @Override // android.app.LoaderManager.LoaderCallbacks
    public void onLoaderReset(Loader<List<NtAppEntry>> loader) {
    }

    @Override // com.nt.settings.SettingsPreferenceFragment, com.nt.settings.core.lifecycle.ObservablePreferenceFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Activity activity = getActivity();
        this.mContext = activity;
        AppIconMemoryOptimizeHelper appIconMemoryOptimizeHelper = AppIconMemoryOptimizeHelper.getInstance(activity);
        this.mAppIconMemoryOptimizeHelper = appIconMemoryOptimizeHelper;
        appIconMemoryOptimizeHelper.startLoadIcon();
    }

    @Override // com.nt.settings.SettingsPreferenceFragment, com.nt.settings.core.lifecycle.ObservablePreferenceFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.mAppIconMemoryOptimizeHelper.stopLoadIcon();
    }

    @Override // com.nt.settings.SettingsPreferenceFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        initListView();
        startAppLoader();
    }

    private void initListView() {
        ListView listView = getListView();
        this.mListView = listView;
        listView.setOnItemClickListener(createOnItemClickListener());
        this.mListView.setDivider(null);
        NtUtils.setListViewHoldPress(this.mListView);
    }

    protected void startAppLoader() {
        getLoaderManager().initLoader(0, null, this);
    }

    @Override // android.app.LoaderManager.LoaderCallbacks
    public Loader<List<NtAppEntry>> onCreateLoader(int i, Bundle bundle) {
        return onCreateAppLoader(i, bundle);
    }

    @Override // android.app.LoaderManager.LoaderCallbacks
    public void onLoadFinished(Loader<List<NtAppEntry>> loader, List<NtAppEntry> list) {
        synchronized (this.mAppList) {
            this.mAppList.clear();
            this.mAppList.addAll(list);
            this.mSwitchOnSize = 0;
            this.mSwitchOffSize = 0;
            this.mGroupAdapter = createNtSingleArrayGroupAdapter(this.mAppList);
            refreshView();
        }
    }

    private void refreshView() {
        this.mListView.setVisibility(0);
        this.mListView.setAdapter((ListAdapter) this.mGroupAdapter);
    }

    /* loaded from: classes2.dex */
    public static class InterestingConfigChanges {
        final Configuration mLastConfiguration = new Configuration();
        int mLastDensity;

        public boolean applyNewConfig(Resources resources) {
            int updateFrom = this.mLastConfiguration.updateFrom(resources.getConfiguration());
            if ((this.mLastDensity != resources.getDisplayMetrics().densityDpi) || (updateFrom & 772) != 0) {
                this.mLastDensity = resources.getDisplayMetrics().densityDpi;
                return true;
            }
            return false;
        }
    }

    /* loaded from: classes2.dex */
    public static class PackageIntentReceiver extends BroadcastReceiver {
        final AsyncTaskLoader<List<NtAppEntry>> mLoader;

        public PackageIntentReceiver(AsyncTaskLoader<List<NtAppEntry>> asyncTaskLoader) {
            this.mLoader = asyncTaskLoader;
            registerReceiver();
        }

        private void registerReceiver() {
            IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
            intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
            intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
            intentFilter.addDataScheme("package");
            this.mLoader.getContext().registerReceiver(this, intentFilter);
            IntentFilter intentFilter2 = new IntentFilter();
            intentFilter2.addAction("android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE");
            intentFilter2.addAction("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE");
            this.mLoader.getContext().registerReceiver(this, intentFilter2);
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            this.mLoader.onContentChanged();
        }
    }
}
