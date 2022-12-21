package com.android.settingslib.net;

import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.NetworkTemplate;
import android.os.Bundle;

@Deprecated
public class SummaryForAllUidLoader extends AsyncTaskLoader<NetworkStats> {
    private static final String KEY_END = "end";
    private static final String KEY_START = "start";
    private static final String KEY_TEMPLATE = "template";
    private final Bundle mArgs;
    private final NetworkStatsManager mNetworkStatsManager;

    public static Bundle buildArgs(NetworkTemplate networkTemplate, long j, long j2) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_TEMPLATE, networkTemplate);
        bundle.putLong(KEY_START, j);
        bundle.putLong(KEY_END, j2);
        return bundle;
    }

    public SummaryForAllUidLoader(Context context, Bundle bundle) {
        super(context);
        this.mNetworkStatsManager = (NetworkStatsManager) context.getSystemService(NetworkStatsManager.class);
        this.mArgs = bundle;
    }

    /* access modifiers changed from: protected */
    public void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    public NetworkStats loadInBackground() {
        long j = this.mArgs.getLong(KEY_START);
        long j2 = this.mArgs.getLong(KEY_END);
        return this.mNetworkStatsManager.querySummary((NetworkTemplate) this.mArgs.getParcelable(KEY_TEMPLATE), j, j2);
    }

    /* access modifiers changed from: protected */
    public void onStopLoading() {
        super.onStopLoading();
        cancelLoad();
    }

    /* access modifiers changed from: protected */
    public void onReset() {
        super.onReset();
        cancelLoad();
    }
}
