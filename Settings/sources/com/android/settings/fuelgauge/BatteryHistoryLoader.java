package com.android.settings.fuelgauge;

import android.content.Context;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.utils.AsyncLoaderCompat;
import java.util.Map;
/* loaded from: classes.dex */
public class BatteryHistoryLoader extends AsyncLoaderCompat<Map<Long, Map<String, BatteryHistEntry>>> {
    private final Context mContext;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settingslib.utils.AsyncLoaderCompat
    public void onDiscardResult(Map<Long, Map<String, BatteryHistEntry>> map) {
    }

    public BatteryHistoryLoader(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override // androidx.loader.content.AsyncTaskLoader
    /* renamed from: loadInBackground  reason: collision with other method in class */
    public Map<Long, Map<String, BatteryHistEntry>> mo611loadInBackground() {
        return FeatureFactory.getFactory(this.mContext).getPowerUsageFeatureProvider(this.mContext).getBatteryHistory(this.mContext);
    }
}
