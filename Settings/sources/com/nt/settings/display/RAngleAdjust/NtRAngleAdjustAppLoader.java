package com.nt.settings.display.RAngleAdjust;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import com.android.settings.applications.InstalledAppCounter;
import com.nt.settings.AppListBaseSettings;
import com.nt.settings.widget.NtAppEntry;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
/* loaded from: classes2.dex */
public class NtRAngleAdjustAppLoader extends AsyncTaskLoader<List<NtAppEntry>> {
    public static final Comparator<NtAppEntry> ALPHA_COMPARATOR = new Comparator<NtAppEntry>() { // from class: com.nt.settings.display.RAngleAdjust.NtRAngleAdjustAppLoader.1
        private final Collator sCollator = Collator.getInstance();

        @Override // java.util.Comparator
        public int compare(NtAppEntry ntAppEntry, NtAppEntry ntAppEntry2) {
            ApplicationInfo applicationInfo;
            int compare;
            int compare2 = this.sCollator.compare(ntAppEntry.getLabel(), ntAppEntry2.getLabel());
            if (compare2 != 0) {
                return compare2;
            }
            ApplicationInfo applicationInfo2 = ntAppEntry.mInfo;
            return (applicationInfo2 == null || (applicationInfo = ntAppEntry2.mInfo) == null || (compare = this.sCollator.compare(applicationInfo2.packageName, applicationInfo.packageName)) == 0) ? ntAppEntry.mInfo.uid - ntAppEntry2.mInfo.uid : compare;
        }
    };
    private HashMap<String, NtAppEntry> mAppMap;
    private List<NtAppEntry> mApps;
    private Context mContext;
    private int[] mGroupCounts;
    private HashMap<String, String> mLauncherNames;
    private NtRAngleAdjustController mNtRAngleAdjustController;
    private AppListBaseSettings.PackageIntentReceiver mPackageObserver;
    final AppListBaseSettings.InterestingConfigChanges mLastConfig = new AppListBaseSettings.InterestingConfigChanges();
    final PackageManager mPm = getContext().getPackageManager();

    protected void onReleaseResources(List<NtAppEntry> list) {
    }

    public NtRAngleAdjustAppLoader(Context context, HashMap<String, NtAppEntry> hashMap, HashMap<String, String> hashMap2, int[] iArr) {
        super(context, Executors.newSingleThreadExecutor());
        this.mAppMap = new HashMap<>();
        this.mGroupCounts = new int[]{0, 0};
        this.mLauncherNames = new HashMap<>();
        this.mLauncherNames = hashMap2;
        this.mGroupCounts = iArr;
        this.mAppMap = hashMap;
        this.mContext = context;
        this.mNtRAngleAdjustController = NtRAngleAdjustController.getInstance(context);
    }

    @Override // android.content.AsyncTaskLoader
    public List<NtAppEntry> loadInBackground() {
        Log.d("NtRAngleAdjustAppLoader", "loader...loadInBackground");
        this.mNtRAngleAdjustController.initLocalRAngleAppList();
        this.mAppMap.clear();
        this.mLauncherNames.clear();
        int[] iArr = this.mGroupCounts;
        iArr[0] = 0;
        iArr[1] = 0;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (UserInfo userInfo : ((UserManager) this.mContext.getSystemService("user")).getProfiles(UserHandle.myUserId())) {
            for (ApplicationInfo applicationInfo : this.mPm.getInstalledApplicationsAsUser(33280 | (userInfo.isAdmin() ? 4194304 : 0), userInfo.id)) {
                if (InstalledAppCounter.includeInCount(-1, this.mPm, applicationInfo)) {
                    String str = applicationInfo.packageName;
                    if ("com.android.settings".equals(str)) {
                        Log.i("NtRAngleAdjustAppLoader", "@_@ ------- exclude settings app itself");
                    } else {
                        if (!this.mLauncherNames.containsKey(str)) {
                            this.mLauncherNames.put(str, applicationInfo.loadLabel(this.mPm).toString());
                        }
                        if (!this.mAppMap.containsKey(str)) {
                            boolean isRAngleUnadjustOpened = this.mNtRAngleAdjustController.isRAngleUnadjustOpened(str);
                            NtAppEntry ntAppEntry = new NtAppEntry(this.mContext, this, applicationInfo, null);
                            String charSequence = applicationInfo.loadLabel(this.mPm).toString();
                            if (charSequence == null || charSequence.length() <= 0) {
                                charSequence = str;
                            }
                            ntAppEntry.setLabel(charSequence);
                            this.mAppMap.put(str, ntAppEntry);
                            if (isRAngleUnadjustOpened) {
                                ntAppEntry.mIsRAngleAdjust = true;
                                arrayList.add(ntAppEntry);
                                int[] iArr2 = this.mGroupCounts;
                                iArr2[0] = iArr2[0] + 1;
                            } else {
                                ntAppEntry.mIsRAngleAdjust = false;
                                arrayList2.add(ntAppEntry);
                                int[] iArr3 = this.mGroupCounts;
                                iArr3[1] = iArr3[1] + 1;
                            }
                        }
                    }
                }
            }
        }
        this.mApps = new ArrayList();
        if (arrayList.size() != 0) {
            Collections.sort(arrayList, ALPHA_COMPARATOR);
            this.mApps.addAll(arrayList);
        }
        if (arrayList2.size() != 0) {
            Collections.sort(arrayList2, ALPHA_COMPARATOR);
            this.mApps.addAll(arrayList2);
        }
        Log.d("NtRAngleAdjustAppLoader", "@_@ ------- " + this.mApps.size() + ", " + arrayList.size() + ", " + arrayList2.size());
        return this.mApps;
    }

    @Override // android.content.Loader
    public void deliverResult(List<NtAppEntry> list) {
        if (isReset() && list != null) {
            onReleaseResources(list);
        }
        if (isStarted()) {
            super.deliverResult((NtRAngleAdjustAppLoader) list);
        }
    }

    @Override // android.content.AsyncTaskLoader
    public void onCanceled(List<NtAppEntry> list) {
        super.onCanceled((NtRAngleAdjustAppLoader) list);
        onReleaseResources(list);
    }

    @Override // android.content.Loader
    protected void onStartLoading() {
        List<NtAppEntry> list = this.mApps;
        if (list != null) {
            deliverResult(list);
        }
        if (this.mPackageObserver == null) {
            this.mPackageObserver = new AppListBaseSettings.PackageIntentReceiver(this);
        }
        boolean applyNewConfig = this.mLastConfig.applyNewConfig(getContext().getResources());
        if (takeContentChanged() || this.mApps == null || applyNewConfig) {
            forceLoad();
        }
    }

    @Override // android.content.Loader
    protected void onReset() {
        super.onReset();
        onStopLoading();
        List<NtAppEntry> list = this.mApps;
        if (list != null) {
            onReleaseResources(list);
            this.mApps = null;
        }
        if (this.mPackageObserver != null) {
            getContext().unregisterReceiver(this.mPackageObserver);
            this.mPackageObserver = null;
        }
    }

    @Override // android.content.Loader
    protected void onStopLoading() {
        cancelLoad();
    }
}
