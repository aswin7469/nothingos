package com.nothing.settings.display.rangle;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.UserInfo;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import androidx.loader.content.AsyncTaskLoader;
import com.android.settings.applications.InstalledAppCounter;
import com.nothing.settings.display.rangle.RAngleFragment;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class RAngleAppLoader extends AsyncTaskLoader<List<AppEntry>> {
    public static final Comparator<AppEntry> ALPHA_COMPARATOR = new Comparator<AppEntry>() {
        private final Collator sCollator = Collator.getInstance();

        /* JADX WARNING: Code restructure failed: missing block: B:6:0x001d, code lost:
            r3 = r3.sCollator.compare(r0.packageName, r1.packageName);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int compare(com.nothing.settings.display.rangle.AppEntry r4, com.nothing.settings.display.rangle.AppEntry r5) {
            /*
                r3 = this;
                java.text.Collator r0 = r3.sCollator
                java.lang.String r1 = r4.getLabel()
                java.lang.String r2 = r5.getLabel()
                int r0 = r0.compare(r1, r2)
                if (r0 == 0) goto L_0x0011
                return r0
            L_0x0011:
                android.content.pm.ApplicationInfo r0 = r4.getInfo()
                android.content.pm.ApplicationInfo r1 = r5.getInfo()
                if (r0 == 0) goto L_0x0029
                if (r1 == 0) goto L_0x0029
                java.text.Collator r3 = r3.sCollator
                java.lang.String r0 = r0.packageName
                java.lang.String r1 = r1.packageName
                int r3 = r3.compare(r0, r1)
                if (r3 != 0) goto L_0x0036
            L_0x0029:
                android.content.pm.ApplicationInfo r3 = r4.getInfo()
                int r3 = r3.uid
                android.content.pm.ApplicationInfo r4 = r5.getInfo()
                int r4 = r4.uid
                int r3 = r3 - r4
            L_0x0036:
                return r3
            */
            throw new UnsupportedOperationException("Method not decompiled: com.nothing.settings.display.rangle.RAngleAppLoader.C19511.compare(com.nothing.settings.display.rangle.AppEntry, com.nothing.settings.display.rangle.AppEntry):int");
        }
    };
    private HashMap<String, AppEntry> mAppMap = new HashMap<>();
    private List<AppEntry> mApps;
    private Context mContext;
    private RAngleController mController;
    private int[] mGroupCounts = {0, 0};
    final RAngleFragment.InterestingConfigChanges mLastConfig = new RAngleFragment.InterestingConfigChanges();
    private RAngleFragment.PackageIntentReceiver mPackageObserver;
    final PackageManager mPm = getContext().getPackageManager();

    /* access modifiers changed from: protected */
    public void onReleaseResources(List<AppEntry> list) {
    }

    public RAngleAppLoader(Context context, HashMap<String, AppEntry> hashMap) {
        super(context);
        this.mAppMap = hashMap;
        this.mContext = context;
        this.mController = RAngleController.getInstance(context);
    }

    public List<AppEntry> loadInBackground() {
        Log.d("RAngle", "loader...loadInBackground");
        this.mController.initLocalRAngleAppList();
        this.mAppMap.clear();
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
                        Log.i("RAngle", "exclude settings app itself");
                    } else if (!this.mAppMap.containsKey(str)) {
                        AppEntry appEntry = new AppEntry(this.mContext, this, applicationInfo, (ResolveInfo) null);
                        String charSequence = applicationInfo.loadLabel(this.mPm).toString();
                        if (charSequence == null || charSequence.isEmpty()) {
                            charSequence = str;
                        }
                        appEntry.setLabel(charSequence);
                        this.mAppMap.put(str, appEntry);
                        if (this.mController.isRAngleNotAdjustOpened(str)) {
                            appEntry.setIsRAngleAdjust(true);
                            arrayList.add(appEntry);
                            int[] iArr2 = this.mGroupCounts;
                            iArr2[0] = iArr2[0] + 1;
                        } else {
                            appEntry.setIsRAngleAdjust(false);
                            arrayList2.add(appEntry);
                            int[] iArr3 = this.mGroupCounts;
                            iArr3[1] = iArr3[1] + 1;
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
        Log.d("RAngle", "RAngleAppLoader::loadInBackground app size:" + this.mApps.size() + ", first array size: " + arrayList.size() + ", second array size: " + arrayList2.size());
        return this.mApps;
    }

    public void deliverResult(List<AppEntry> list) {
        if (isReset() && list != null) {
            onReleaseResources(list);
        }
        if (isStarted()) {
            super.deliverResult(list);
        }
    }

    public void onCanceled(List<AppEntry> list) {
        super.onCanceled(list);
        onReleaseResources(list);
    }

    /* access modifiers changed from: protected */
    public void onStartLoading() {
        List<AppEntry> list = this.mApps;
        if (list != null) {
            deliverResult(list);
        }
        if (this.mPackageObserver == null) {
            this.mPackageObserver = new RAngleFragment.PackageIntentReceiver(this);
        }
        boolean applyNewConfig = this.mLastConfig.applyNewConfig(getContext().getResources());
        if (takeContentChanged() || this.mApps == null || applyNewConfig) {
            forceLoad();
        }
    }

    /* access modifiers changed from: protected */
    public void onReset() {
        super.onReset();
        onStopLoading();
        List<AppEntry> list = this.mApps;
        if (list != null) {
            onReleaseResources(list);
            this.mApps = null;
        }
        if (this.mPackageObserver != null) {
            getContext().unregisterReceiver(this.mPackageObserver);
            this.mPackageObserver = null;
        }
    }

    /* access modifiers changed from: protected */
    public void onStopLoading() {
        cancelLoad();
    }
}
