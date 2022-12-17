package com.android.settings.applications;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.util.Log;
import com.android.settings.applications.AppStateBaseBridge;
import com.android.settingslib.applications.ApplicationsState;
import java.util.ArrayList;
import java.util.List;

public class AppStateLocaleBridge extends AppStateBaseBridge {
    public static final ApplicationsState.AppFilter FILTER_APPS_LOCALE = new ApplicationsState.AppFilter() {
        public void init() {
        }

        public boolean filterApp(ApplicationsState.AppEntry appEntry) {
            Object obj = appEntry.extraInfo;
            if (obj != null) {
                return ((Boolean) obj).booleanValue();
            }
            String r2 = AppStateLocaleBridge.TAG;
            Log.d(r2, "[" + appEntry.info.packageName + "] has No extra info.");
            return false;
        }
    };
    /* access modifiers changed from: private */
    public static final String TAG = "AppStateLocaleBridge";
    private final Context mContext;
    private final List<ResolveInfo> mListInfos;

    public AppStateLocaleBridge(Context context, ApplicationsState applicationsState, AppStateBaseBridge.Callback callback) {
        super(applicationsState, callback);
        this.mContext = context;
        this.mListInfos = context.getPackageManager().queryIntentActivities(AppLocaleUtil.LAUNCHER_ENTRY_INTENT, 128);
    }

    /* access modifiers changed from: protected */
    public void updateExtraInfo(ApplicationsState.AppEntry appEntry, String str, int i) {
        appEntry.extraInfo = AppLocaleUtil.canDisplayLocaleUi(this.mContext, appEntry.info.packageName, this.mListInfos) ? Boolean.TRUE : Boolean.FALSE;
    }

    /* access modifiers changed from: protected */
    public void loadAllExtraInfo() {
        ArrayList<ApplicationsState.AppEntry> allApps = this.mAppSession.getAllApps();
        for (int i = 0; i < allApps.size(); i++) {
            ApplicationsState.AppEntry appEntry = allApps.get(i);
            appEntry.extraInfo = AppLocaleUtil.canDisplayLocaleUi(this.mContext, appEntry.info.packageName, this.mListInfos) ? Boolean.TRUE : Boolean.FALSE;
        }
    }
}
