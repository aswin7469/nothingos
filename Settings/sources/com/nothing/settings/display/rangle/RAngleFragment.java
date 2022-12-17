package com.nothing.settings.display.rangle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R$string;
import com.android.settings.SettingsPreferenceFragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RAngleFragment extends SettingsPreferenceFragment implements LoaderManager.LoaderCallbacks<List<AppEntry>> {
    protected RAngleAppEntryAdapter mAdapter;
    protected AppIconMemoryOptimizeHelper mAppIconMemoryOptimizeHelper;
    public List<AppEntry> mAppList = new ArrayList();
    protected Context mContext;
    private RAngleController mController;
    private RecyclerView mListView;
    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            super.onScrollStateChanged(recyclerView, i);
        }

        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            super.onScrolled(recyclerView, i, i2);
        }
    };

    public int getMetricsCategory() {
        return 0;
    }

    public void onLoaderReset(Loader<List<AppEntry>> loader) {
        Log.d("RAngle", "RAngleFragment::onLoaderReset");
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FragmentActivity activity = getActivity();
        this.mContext = activity;
        AppIconMemoryOptimizeHelper instance = AppIconMemoryOptimizeHelper.getInstance(activity);
        this.mAppIconMemoryOptimizeHelper = instance;
        instance.startLoadIcon();
    }

    public void onDestroy() {
        super.onDestroy();
        this.mAppIconMemoryOptimizeHelper.stopLoadIcon();
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mController = RAngleController.getInstance(getContext());
        getActivity().setTitle(R$string.r_angle_adjust);
        initListView();
        startAppLoader();
    }

    private void initListView() {
        RecyclerView listView = getListView();
        this.mListView = listView;
        listView.clearOnScrollListeners();
        this.mListView.addOnScrollListener(this.mOnScrollListener);
    }

    /* access modifiers changed from: protected */
    public Loader<List<AppEntry>> createAppLoader(int i, Bundle bundle) {
        return new RAngleAppLoader(getActivity(), new HashMap());
    }

    /* access modifiers changed from: protected */
    public void startAppLoader() {
        Log.d("RAngle", "RAngleFragment::startAppLoader");
        getLoaderManager().initLoader(0, (Bundle) null, this);
    }

    public Loader<List<AppEntry>> onCreateLoader(int i, Bundle bundle) {
        Log.d("RAngle", "AppListBaseSettings::onCreateLoader");
        return createAppLoader(i, bundle);
    }

    public void onLoadFinished(Loader<List<AppEntry>> loader, List<AppEntry> list) {
        synchronized (this.mAppList) {
            Log.d("RAngle", "AppListBaseSettings::onLoadFinished");
            this.mAppList.clear();
            this.mAppList.addAll(list);
            RAngleAppEntryAdapter createAppEntryAdapter = createAppEntryAdapter(this.mAppList);
            this.mAdapter = createAppEntryAdapter;
            this.mListView.setAdapter(createAppEntryAdapter);
            this.mAdapter.notifyDataSetChanged();
        }
    }

    /* access modifiers changed from: protected */
    public RAngleAppEntryAdapter createAppEntryAdapter(List<AppEntry> list) {
        return new RAngleAppEntryAdapter(getActivity(), this.mListView, list, this.mAppIconMemoryOptimizeHelper);
    }

    public static class InterestingConfigChanges {
        final Configuration mLastConfiguration = new Configuration();
        int mLastDensity;

        public boolean applyNewConfig(Resources resources) {
            int updateFrom = this.mLastConfiguration.updateFrom(resources.getConfiguration());
            if (!(this.mLastDensity != resources.getDisplayMetrics().densityDpi) && (updateFrom & 772) == 0) {
                return false;
            }
            this.mLastDensity = resources.getDisplayMetrics().densityDpi;
            return true;
        }
    }

    public static class PackageIntentReceiver extends BroadcastReceiver {
        final AsyncTaskLoader<List<AppEntry>> mLoader;

        public PackageIntentReceiver(AsyncTaskLoader<List<AppEntry>> asyncTaskLoader) {
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

        public void onReceive(Context context, Intent intent) {
            this.mLoader.onContentChanged();
        }
    }
}
