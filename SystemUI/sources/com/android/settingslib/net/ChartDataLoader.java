package com.android.settingslib.net;

import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.NetworkTemplate;
import android.os.Bundle;
import android.os.RemoteException;
import com.android.settingslib.AppItem;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class ChartDataLoader extends AsyncTaskLoader<ChartData> {
    private static final String KEY_APP = "app";
    private static final String KEY_TEMPLATE = "template";
    private final Bundle mArgs;
    private final NetworkStatsManager mNetworkStatsManager;

    public static Bundle buildArgs(NetworkTemplate networkTemplate, AppItem appItem) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_TEMPLATE, networkTemplate);
        bundle.putParcelable(KEY_APP, appItem);
        return bundle;
    }

    public ChartDataLoader(Context context, NetworkStatsManager networkStatsManager, Bundle bundle) {
        super(context);
        this.mNetworkStatsManager = networkStatsManager;
        this.mArgs = bundle;
    }

    /* access modifiers changed from: protected */
    public void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    public ChartData loadInBackground() {
        try {
            return loadInBackground((NetworkTemplate) this.mArgs.getParcelable(KEY_TEMPLATE), (AppItem) this.mArgs.getParcelable(KEY_APP));
        } catch (RemoteException e) {
            throw new RuntimeException("problem reading network stats", e);
        }
    }

    private List<NetworkStats.Bucket> convertToBuckets(NetworkStats networkStats) {
        ArrayList arrayList = new ArrayList();
        while (networkStats.hasNextBucket()) {
            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
            networkStats.getNextBucket(bucket);
            arrayList.add(bucket);
        }
        return arrayList;
    }

    private ChartData loadInBackground(NetworkTemplate networkTemplate, AppItem appItem) throws RemoteException {
        ChartData chartData = new ChartData();
        chartData.network = convertToBuckets(this.mNetworkStatsManager.queryDetailsForDevice(networkTemplate, Long.MIN_VALUE, Long.MAX_VALUE));
        if (appItem != null) {
            int size = appItem.uids.size();
            for (int i = 0; i < size; i++) {
                int keyAt = appItem.uids.keyAt(i);
                chartData.detailDefault = collectHistoryForUid(networkTemplate, keyAt, 0, chartData.detailDefault);
                chartData.detailForeground = collectHistoryForUid(networkTemplate, keyAt, 1, chartData.detailForeground);
            }
            if (size > 0) {
                chartData.detail = new ArrayList();
                chartData.detail.addAll(chartData.detailDefault);
                chartData.detail.addAll(chartData.detailForeground);
            } else {
                chartData.detailDefault = new ArrayList();
                chartData.detailForeground = new ArrayList();
                chartData.detail = new ArrayList();
            }
        }
        return chartData;
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

    private List<NetworkStats.Bucket> collectHistoryForUid(NetworkTemplate networkTemplate, int i, int i2, List<NetworkStats.Bucket> list) {
        List<NetworkStats.Bucket> convertToBuckets = convertToBuckets(this.mNetworkStatsManager.queryDetailsForUidTagState(networkTemplate, Long.MIN_VALUE, Long.MAX_VALUE, i, 0, i2));
        if (list == null) {
            return convertToBuckets;
        }
        list.addAll(convertToBuckets);
        return list;
    }
}
