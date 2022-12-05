package com.android.settings.applications;

import android.app.ActivityManager;
import android.app.ActivityThread;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.content.pm.UserInfo;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.format.Formatter;
import android.util.Log;
import android.util.SparseArray;
import com.android.settings.R;
import com.android.settingslib.Utils;
import com.android.settingslib.applications.InterestingConfigChanges;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class RunningState {
    static Object sGlobalLock = new Object();
    static RunningState sInstance;
    final ActivityManager mAm;
    final Context mApplicationContext;
    final BackgroundHandler mBackgroundHandler;
    long mBackgroundProcessMemory;
    final HandlerThread mBackgroundThread;
    long mForegroundProcessMemory;
    boolean mHaveData;
    final boolean mHideManagedProfiles;
    final int mMyUserId;
    int mNumBackgroundProcesses;
    int mNumForegroundProcesses;
    int mNumServiceProcesses;
    final PackageManager mPm;
    OnRefreshUiListener mRefreshUiListener;
    boolean mResumed;
    long mServiceProcessMemory;
    final UserManager mUm;
    private final UserManagerBroadcastReceiver mUmBroadcastReceiver;
    boolean mWatchingBackgroundItems;
    final InterestingConfigChanges mInterestingConfigChanges = new InterestingConfigChanges();
    final SparseArray<HashMap<String, ProcessItem>> mServiceProcessesByName = new SparseArray<>();
    final SparseArray<ProcessItem> mServiceProcessesByPid = new SparseArray<>();
    final ServiceProcessComparator mServiceProcessComparator = new ServiceProcessComparator();
    final ArrayList<ProcessItem> mInterestingProcesses = new ArrayList<>();
    final SparseArray<ProcessItem> mRunningProcesses = new SparseArray<>();
    final ArrayList<ProcessItem> mProcessItems = new ArrayList<>();
    final ArrayList<ProcessItem> mAllProcessItems = new ArrayList<>();
    final SparseArray<MergedItem> mOtherUserMergedItems = new SparseArray<>();
    final SparseArray<MergedItem> mOtherUserBackgroundItems = new SparseArray<>();
    final SparseArray<AppProcessInfo> mTmpAppProcesses = new SparseArray<>();
    int mSequence = 0;
    final Comparator<MergedItem> mBackgroundComparator = new Comparator<MergedItem>() { // from class: com.android.settings.applications.RunningState.1
        @Override // java.util.Comparator
        public int compare(MergedItem mergedItem, MergedItem mergedItem2) {
            int i = mergedItem.mUserId;
            int i2 = mergedItem2.mUserId;
            if (i != i2) {
                int i3 = RunningState.this.mMyUserId;
                if (i == i3) {
                    return -1;
                }
                return (i2 != i3 && i < i2) ? -1 : 1;
            }
            ProcessItem processItem = mergedItem.mProcess;
            ProcessItem processItem2 = mergedItem2.mProcess;
            if (processItem == processItem2) {
                String str = mergedItem.mLabel;
                String str2 = mergedItem2.mLabel;
                if (str == str2) {
                    return 0;
                }
                if (str == null) {
                    return -1;
                }
                return str.compareTo(str2);
            } else if (processItem == null) {
                return -1;
            } else {
                if (processItem2 == null) {
                    return 1;
                }
                ActivityManager.RunningAppProcessInfo runningAppProcessInfo = processItem.mRunningProcessInfo;
                ActivityManager.RunningAppProcessInfo runningAppProcessInfo2 = processItem2.mRunningProcessInfo;
                boolean z = runningAppProcessInfo.importance >= 400;
                if (z != (runningAppProcessInfo2.importance >= 400)) {
                    return z ? 1 : -1;
                }
                boolean z2 = (runningAppProcessInfo.flags & 4) != 0;
                if (z2 != ((runningAppProcessInfo2.flags & 4) != 0)) {
                    return z2 ? -1 : 1;
                }
                int i4 = runningAppProcessInfo.lru;
                int i5 = runningAppProcessInfo2.lru;
                if (i4 != i5) {
                    return i4 < i5 ? -1 : 1;
                }
                String str3 = processItem.mLabel;
                String str4 = processItem2.mLabel;
                if (str3 == str4) {
                    return 0;
                }
                if (str3 == null) {
                    return 1;
                }
                if (str4 != null) {
                    return str3.compareTo(str4);
                }
                return -1;
            }
        }
    };
    final Object mLock = new Object();
    ArrayList<BaseItem> mItems = new ArrayList<>();
    ArrayList<MergedItem> mMergedItems = new ArrayList<>();
    ArrayList<MergedItem> mBackgroundItems = new ArrayList<>();
    ArrayList<MergedItem> mUserBackgroundItems = new ArrayList<>();
    final Handler mHandler = new Handler() { // from class: com.android.settings.applications.RunningState.2
        int mNextUpdate = 0;

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 3) {
                this.mNextUpdate = message.arg1 != 0 ? 2 : 1;
            } else if (i != 4) {
            } else {
                synchronized (RunningState.this.mLock) {
                    if (!RunningState.this.mResumed) {
                        return;
                    }
                    removeMessages(4);
                    sendMessageDelayed(obtainMessage(4), 1000L);
                    OnRefreshUiListener onRefreshUiListener = RunningState.this.mRefreshUiListener;
                    if (onRefreshUiListener == null) {
                        return;
                    }
                    onRefreshUiListener.onRefreshUi(this.mNextUpdate);
                    this.mNextUpdate = 0;
                }
            }
        }
    };

    /* loaded from: classes.dex */
    interface OnRefreshUiListener {
        void onRefreshUi(int i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class AppProcessInfo {
        boolean hasForegroundServices;
        boolean hasServices;
        final ActivityManager.RunningAppProcessInfo info;

        AppProcessInfo(ActivityManager.RunningAppProcessInfo runningAppProcessInfo) {
            this.info = runningAppProcessInfo;
        }
    }

    /* loaded from: classes.dex */
    final class BackgroundHandler extends Handler {
        public BackgroundHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                RunningState.this.reset();
            } else if (i != 2) {
            } else {
                synchronized (RunningState.this.mLock) {
                    RunningState runningState = RunningState.this;
                    if (!runningState.mResumed) {
                        return;
                    }
                    Message obtainMessage = runningState.mHandler.obtainMessage(3);
                    RunningState runningState2 = RunningState.this;
                    obtainMessage.arg1 = runningState2.update(runningState2.mApplicationContext, runningState2.mAm) ? 1 : 0;
                    RunningState.this.mHandler.sendMessage(obtainMessage);
                    removeMessages(2);
                    sendMessageDelayed(obtainMessage(2), 2000L);
                }
            }
        }
    }

    /* loaded from: classes.dex */
    private final class UserManagerBroadcastReceiver extends BroadcastReceiver {
        private volatile boolean usersChanged;

        private UserManagerBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            synchronized (RunningState.this.mLock) {
                RunningState runningState = RunningState.this;
                if (runningState.mResumed) {
                    runningState.mHaveData = false;
                    runningState.mBackgroundHandler.removeMessages(1);
                    RunningState.this.mBackgroundHandler.sendEmptyMessage(1);
                    RunningState.this.mBackgroundHandler.removeMessages(2);
                    RunningState.this.mBackgroundHandler.sendEmptyMessage(2);
                } else {
                    this.usersChanged = true;
                }
            }
        }

        public boolean checkUsersChangedLocked() {
            boolean z = this.usersChanged;
            this.usersChanged = false;
            return z;
        }

        void register(Context context) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.USER_STOPPED");
            intentFilter.addAction("android.intent.action.USER_STARTED");
            intentFilter.addAction("android.intent.action.USER_INFO_CHANGED");
            context.registerReceiverAsUser(this, UserHandle.ALL, intentFilter, null, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class UserState {
        Drawable mIcon;
        UserInfo mInfo;
        String mLabel;

        UserState() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class BaseItem {
        long mActiveSince;
        boolean mBackground;
        int mCurSeq;
        String mCurSizeStr;
        String mDescription;
        CharSequence mDisplayLabel;
        final boolean mIsProcess;
        String mLabel;
        boolean mNeedDivider;
        PackageItemInfo mPackageInfo;
        long mSize;
        String mSizeStr;
        final int mUserId;

        public BaseItem(boolean z, int i) {
            this.mIsProcess = z;
            this.mUserId = i;
        }

        public Drawable loadIcon(Context context, RunningState runningState) {
            PackageItemInfo packageItemInfo = this.mPackageInfo;
            if (packageItemInfo != null) {
                return runningState.mPm.getUserBadgedIcon(packageItemInfo.loadUnbadgedIcon(runningState.mPm), new UserHandle(this.mUserId));
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class ServiceItem extends BaseItem {
        MergedItem mMergedItem;
        ActivityManager.RunningServiceInfo mRunningService;
        ServiceInfo mServiceInfo;
        boolean mShownAsStarted;

        public ServiceItem(int i) {
            super(false, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class ProcessItem extends BaseItem {
        long mActiveSince;
        ProcessItem mClient;
        boolean mInteresting;
        boolean mIsStarted;
        boolean mIsSystem;
        int mLastNumDependentProcesses;
        MergedItem mMergedItem;
        int mPid;
        final String mProcessName;
        ActivityManager.RunningAppProcessInfo mRunningProcessInfo;
        int mRunningSeq;
        final int mUid;
        final HashMap<ComponentName, ServiceItem> mServices = new HashMap<>();
        final SparseArray<ProcessItem> mDependentProcesses = new SparseArray<>();

        public ProcessItem(Context context, int i, String str) {
            super(true, UserHandle.getUserId(i));
            this.mDescription = context.getResources().getString(R.string.service_process_name, str);
            this.mUid = i;
            this.mProcessName = str;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void ensureLabel(PackageManager packageManager) {
            CharSequence text;
            if (this.mLabel != null) {
                return;
            }
            try {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(this.mProcessName, 4194304);
                if (applicationInfo.uid == this.mUid) {
                    CharSequence loadLabel = applicationInfo.loadLabel(packageManager);
                    this.mDisplayLabel = loadLabel;
                    this.mLabel = loadLabel.toString();
                    this.mPackageInfo = applicationInfo;
                    return;
                }
            } catch (PackageManager.NameNotFoundException unused) {
            }
            String[] packagesForUid = packageManager.getPackagesForUid(this.mUid);
            if (packagesForUid.length == 1) {
                try {
                    ApplicationInfo applicationInfo2 = packageManager.getApplicationInfo(packagesForUid[0], 4194304);
                    CharSequence loadLabel2 = applicationInfo2.loadLabel(packageManager);
                    this.mDisplayLabel = loadLabel2;
                    this.mLabel = loadLabel2.toString();
                    this.mPackageInfo = applicationInfo2;
                    return;
                } catch (PackageManager.NameNotFoundException unused2) {
                }
            }
            for (String str : packagesForUid) {
                try {
                    PackageInfo packageInfo = packageManager.getPackageInfo(str, 0);
                    int i = packageInfo.sharedUserLabel;
                    if (i != 0 && (text = packageManager.getText(str, i, packageInfo.applicationInfo)) != null) {
                        this.mDisplayLabel = text;
                        this.mLabel = text.toString();
                        this.mPackageInfo = packageInfo.applicationInfo;
                        return;
                    }
                } catch (PackageManager.NameNotFoundException unused3) {
                }
            }
            if (this.mServices.size() > 0) {
                ApplicationInfo applicationInfo3 = this.mServices.values().iterator().next().mServiceInfo.applicationInfo;
                this.mPackageInfo = applicationInfo3;
                CharSequence loadLabel3 = applicationInfo3.loadLabel(packageManager);
                this.mDisplayLabel = loadLabel3;
                this.mLabel = loadLabel3.toString();
                return;
            }
            try {
                ApplicationInfo applicationInfo4 = packageManager.getApplicationInfo(packagesForUid[0], 4194304);
                CharSequence loadLabel4 = applicationInfo4.loadLabel(packageManager);
                this.mDisplayLabel = loadLabel4;
                this.mLabel = loadLabel4.toString();
                this.mPackageInfo = applicationInfo4;
            } catch (PackageManager.NameNotFoundException unused4) {
            }
        }

        boolean updateService(Context context, ActivityManager.RunningServiceInfo runningServiceInfo) {
            boolean z;
            PackageManager packageManager = context.getPackageManager();
            ServiceItem serviceItem = this.mServices.get(runningServiceInfo.service);
            boolean z2 = true;
            if (serviceItem == null) {
                serviceItem = new ServiceItem(this.mUserId);
                serviceItem.mRunningService = runningServiceInfo;
                try {
                    ServiceInfo serviceInfo = ActivityThread.getPackageManager().getServiceInfo(runningServiceInfo.service, 4194304, UserHandle.getUserId(runningServiceInfo.uid));
                    serviceItem.mServiceInfo = serviceInfo;
                    if (serviceInfo == null) {
                        Log.d("RunningService", "getServiceInfo returned null for: " + runningServiceInfo.service);
                        return false;
                    }
                } catch (RemoteException unused) {
                }
                serviceItem.mDisplayLabel = RunningState.makeLabel(packageManager, serviceItem.mRunningService.service.getClassName(), serviceItem.mServiceInfo);
                CharSequence charSequence = this.mDisplayLabel;
                this.mLabel = charSequence != null ? charSequence.toString() : null;
                serviceItem.mPackageInfo = serviceItem.mServiceInfo.applicationInfo;
                this.mServices.put(runningServiceInfo.service, serviceItem);
                z = true;
            } else {
                z = false;
            }
            serviceItem.mCurSeq = this.mCurSeq;
            serviceItem.mRunningService = runningServiceInfo;
            long j = runningServiceInfo.restarting == 0 ? runningServiceInfo.activeSince : -1L;
            if (serviceItem.mActiveSince != j) {
                serviceItem.mActiveSince = j;
                z = true;
            }
            String str = runningServiceInfo.clientPackage;
            if (str != null && runningServiceInfo.clientLabel != 0) {
                if (serviceItem.mShownAsStarted) {
                    serviceItem.mShownAsStarted = false;
                    z = true;
                }
                try {
                    serviceItem.mDescription = context.getResources().getString(R.string.service_client_name, packageManager.getResourcesForApplication(str).getString(runningServiceInfo.clientLabel));
                    return z;
                } catch (PackageManager.NameNotFoundException unused2) {
                    serviceItem.mDescription = null;
                    return z;
                }
            }
            if (!serviceItem.mShownAsStarted) {
                serviceItem.mShownAsStarted = true;
            } else {
                z2 = z;
            }
            serviceItem.mDescription = context.getResources().getString(R.string.service_started_by_app);
            return z2;
        }

        boolean updateSize(Context context, long j, int i) {
            long j2 = j * 1024;
            this.mSize = j2;
            if (this.mCurSeq == i) {
                String formatShortFileSize = Formatter.formatShortFileSize(context, j2);
                if (!formatShortFileSize.equals(this.mSizeStr)) {
                    this.mSizeStr = formatShortFileSize;
                }
            }
            return false;
        }

        boolean buildDependencyChain(Context context, PackageManager packageManager, int i) {
            int size = this.mDependentProcesses.size();
            boolean z = false;
            for (int i2 = 0; i2 < size; i2++) {
                ProcessItem valueAt = this.mDependentProcesses.valueAt(i2);
                if (valueAt.mClient != this) {
                    valueAt.mClient = this;
                    z = true;
                }
                valueAt.mCurSeq = i;
                valueAt.ensureLabel(packageManager);
                z |= valueAt.buildDependencyChain(context, packageManager, i);
            }
            if (this.mLastNumDependentProcesses != this.mDependentProcesses.size()) {
                this.mLastNumDependentProcesses = this.mDependentProcesses.size();
                return true;
            }
            return z;
        }

        void addDependentProcesses(ArrayList<BaseItem> arrayList, ArrayList<ProcessItem> arrayList2) {
            int size = this.mDependentProcesses.size();
            for (int i = 0; i < size; i++) {
                ProcessItem valueAt = this.mDependentProcesses.valueAt(i);
                valueAt.addDependentProcesses(arrayList, arrayList2);
                arrayList.add(valueAt);
                if (valueAt.mPid > 0) {
                    arrayList2.add(valueAt);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class MergedItem extends BaseItem {
        ProcessItem mProcess;
        UserState mUser;
        final ArrayList<ProcessItem> mOtherProcesses = new ArrayList<>();
        final ArrayList<ServiceItem> mServices = new ArrayList<>();
        final ArrayList<MergedItem> mChildren = new ArrayList<>();
        private int mLastNumProcesses = -1;
        private int mLastNumServices = -1;

        MergedItem(int i) {
            super(false, i);
        }

        private void setDescription(Context context, int i, int i2) {
            if (this.mLastNumProcesses == i && this.mLastNumServices == i2) {
                return;
            }
            this.mLastNumProcesses = i;
            this.mLastNumServices = i2;
            int i3 = R.string.running_processes_item_description_s_s;
            if (i != 1) {
                if (i2 != 1) {
                    i3 = R.string.running_processes_item_description_p_p;
                } else {
                    i3 = R.string.running_processes_item_description_p_s;
                }
            } else if (i2 != 1) {
                i3 = R.string.running_processes_item_description_s_p;
            }
            this.mDescription = context.getResources().getString(i3, Integer.valueOf(i), Integer.valueOf(i2));
        }

        boolean update(Context context, boolean z) {
            this.mBackground = z;
            if (this.mUser != null) {
                this.mPackageInfo = this.mChildren.get(0).mProcess.mPackageInfo;
                UserState userState = this.mUser;
                String str = userState != null ? userState.mLabel : null;
                this.mLabel = str;
                this.mDisplayLabel = str;
                this.mActiveSince = -1L;
                int i = 0;
                int i2 = 0;
                for (int i3 = 0; i3 < this.mChildren.size(); i3++) {
                    MergedItem mergedItem = this.mChildren.get(i3);
                    i += mergedItem.mLastNumProcesses;
                    i2 += mergedItem.mLastNumServices;
                    long j = mergedItem.mActiveSince;
                    if (j >= 0 && this.mActiveSince < j) {
                        this.mActiveSince = j;
                    }
                }
                if (!this.mBackground) {
                    setDescription(context, i, i2);
                }
            } else {
                ProcessItem processItem = this.mProcess;
                this.mPackageInfo = processItem.mPackageInfo;
                this.mDisplayLabel = processItem.mDisplayLabel;
                this.mLabel = processItem.mLabel;
                if (!z) {
                    setDescription(context, (processItem.mPid > 0 ? 1 : 0) + this.mOtherProcesses.size(), this.mServices.size());
                }
                this.mActiveSince = -1L;
                for (int i4 = 0; i4 < this.mServices.size(); i4++) {
                    long j2 = this.mServices.get(i4).mActiveSince;
                    if (j2 >= 0 && this.mActiveSince < j2) {
                        this.mActiveSince = j2;
                    }
                }
            }
            return false;
        }

        boolean updateSize(Context context) {
            if (this.mUser != null) {
                this.mSize = 0L;
                for (int i = 0; i < this.mChildren.size(); i++) {
                    MergedItem mergedItem = this.mChildren.get(i);
                    mergedItem.updateSize(context);
                    this.mSize += mergedItem.mSize;
                }
            } else {
                this.mSize = this.mProcess.mSize;
                for (int i2 = 0; i2 < this.mOtherProcesses.size(); i2++) {
                    this.mSize += this.mOtherProcesses.get(i2).mSize;
                }
            }
            String formatShortFileSize = Formatter.formatShortFileSize(context, this.mSize);
            if (!formatShortFileSize.equals(this.mSizeStr)) {
                this.mSizeStr = formatShortFileSize;
            }
            return false;
        }

        @Override // com.android.settings.applications.RunningState.BaseItem
        public Drawable loadIcon(Context context, RunningState runningState) {
            UserState userState = this.mUser;
            if (userState == null) {
                return super.loadIcon(context, runningState);
            }
            Drawable drawable = userState.mIcon;
            if (drawable != null) {
                Drawable.ConstantState constantState = drawable.getConstantState();
                if (constantState == null) {
                    return this.mUser.mIcon;
                }
                return constantState.newDrawable();
            }
            return context.getDrawable(17302705);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class ServiceProcessComparator implements Comparator<ProcessItem> {
        ServiceProcessComparator() {
        }

        @Override // java.util.Comparator
        public int compare(ProcessItem processItem, ProcessItem processItem2) {
            int i = processItem.mUserId;
            int i2 = processItem2.mUserId;
            if (i != i2) {
                int i3 = RunningState.this.mMyUserId;
                if (i == i3) {
                    return -1;
                }
                return (i2 != i3 && i < i2) ? -1 : 1;
            }
            boolean z = processItem.mIsStarted;
            if (z != processItem2.mIsStarted) {
                return z ? -1 : 1;
            }
            boolean z2 = processItem.mIsSystem;
            if (z2 != processItem2.mIsSystem) {
                return z2 ? 1 : -1;
            }
            long j = processItem.mActiveSince;
            long j2 = processItem2.mActiveSince;
            if (j == j2) {
                return 0;
            }
            return j > j2 ? -1 : 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CharSequence makeLabel(PackageManager packageManager, String str, PackageItemInfo packageItemInfo) {
        CharSequence loadLabel;
        if (packageItemInfo == null || ((packageItemInfo.labelRes == 0 && packageItemInfo.nonLocalizedLabel == null) || (loadLabel = packageItemInfo.loadLabel(packageManager)) == null)) {
            int lastIndexOf = str.lastIndexOf(46);
            return lastIndexOf >= 0 ? str.substring(lastIndexOf + 1, str.length()) : str;
        }
        return loadLabel;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static RunningState getInstance(Context context) {
        RunningState runningState;
        synchronized (sGlobalLock) {
            if (sInstance == null) {
                sInstance = new RunningState(context);
            }
            runningState = sInstance;
        }
        return runningState;
    }

    private RunningState(Context context) {
        UserManagerBroadcastReceiver userManagerBroadcastReceiver = new UserManagerBroadcastReceiver();
        this.mUmBroadcastReceiver = userManagerBroadcastReceiver;
        Context applicationContext = context.getApplicationContext();
        this.mApplicationContext = applicationContext;
        this.mAm = (ActivityManager) applicationContext.getSystemService(ActivityManager.class);
        this.mPm = applicationContext.getPackageManager();
        UserManager userManager = (UserManager) applicationContext.getSystemService(UserManager.class);
        this.mUm = userManager;
        int myUserId = UserHandle.myUserId();
        this.mMyUserId = myUserId;
        UserInfo userInfo = userManager.getUserInfo(myUserId);
        this.mHideManagedProfiles = userInfo == null || !userInfo.canHaveProfile();
        this.mResumed = false;
        HandlerThread handlerThread = new HandlerThread("RunningState:Background");
        this.mBackgroundThread = handlerThread;
        handlerThread.start();
        this.mBackgroundHandler = new BackgroundHandler(handlerThread.getLooper());
        userManagerBroadcastReceiver.register(applicationContext);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resume(OnRefreshUiListener onRefreshUiListener) {
        synchronized (this.mLock) {
            this.mResumed = true;
            this.mRefreshUiListener = onRefreshUiListener;
            boolean checkUsersChangedLocked = this.mUmBroadcastReceiver.checkUsersChangedLocked();
            boolean applyNewConfig = this.mInterestingConfigChanges.applyNewConfig(this.mApplicationContext.getResources());
            if (checkUsersChangedLocked || applyNewConfig) {
                this.mHaveData = false;
                this.mBackgroundHandler.removeMessages(1);
                this.mBackgroundHandler.removeMessages(2);
                this.mBackgroundHandler.sendEmptyMessage(1);
            }
            if (!this.mBackgroundHandler.hasMessages(2)) {
                this.mBackgroundHandler.sendEmptyMessage(2);
            }
            this.mHandler.sendEmptyMessage(4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateNow() {
        synchronized (this.mLock) {
            this.mBackgroundHandler.removeMessages(2);
            this.mBackgroundHandler.sendEmptyMessage(2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasData() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mHaveData;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void waitForData() {
        synchronized (this.mLock) {
            while (!this.mHaveData) {
                try {
                    this.mLock.wait(0L);
                } catch (InterruptedException unused) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void pause() {
        synchronized (this.mLock) {
            this.mResumed = false;
            this.mRefreshUiListener = null;
            this.mHandler.removeMessages(4);
        }
    }

    private boolean isInterestingProcess(ActivityManager.RunningAppProcessInfo runningAppProcessInfo) {
        int i;
        int i2 = runningAppProcessInfo.flags;
        if ((i2 & 1) != 0) {
            return true;
        }
        return (i2 & 2) == 0 && (i = runningAppProcessInfo.importance) >= 100 && i < 350 && runningAppProcessInfo.importanceReasonCode == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reset() {
        this.mServiceProcessesByName.clear();
        this.mServiceProcessesByPid.clear();
        this.mInterestingProcesses.clear();
        this.mRunningProcesses.clear();
        this.mProcessItems.clear();
        this.mAllProcessItems.clear();
    }

    private void addOtherUserItem(Context context, ArrayList<MergedItem> arrayList, SparseArray<MergedItem> sparseArray, MergedItem mergedItem) {
        MergedItem mergedItem2 = sparseArray.get(mergedItem.mUserId);
        if (mergedItem2 == null || mergedItem2.mCurSeq != this.mSequence) {
            UserInfo userInfo = this.mUm.getUserInfo(mergedItem.mUserId);
            if (userInfo == null) {
                return;
            }
            if (this.mHideManagedProfiles && userInfo.isManagedProfile()) {
                return;
            }
            if (mergedItem2 == null) {
                mergedItem2 = new MergedItem(mergedItem.mUserId);
                sparseArray.put(mergedItem.mUserId, mergedItem2);
            } else {
                mergedItem2.mChildren.clear();
            }
            mergedItem2.mCurSeq = this.mSequence;
            UserState userState = new UserState();
            mergedItem2.mUser = userState;
            userState.mInfo = userInfo;
            userState.mIcon = Utils.getUserIcon(context, this.mUm, userInfo);
            mergedItem2.mUser.mLabel = Utils.getUserLabel(context, userInfo);
            arrayList.add(mergedItem2);
        }
        mergedItem2.mChildren.add(mergedItem);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:333:0x066f  */
    /* JADX WARN: Removed duplicated region for block: B:347:0x06a4  */
    /* JADX WARN: Removed duplicated region for block: B:352:0x06f8 A[LOOP:25: B:350:0x06f0->B:352:0x06f8, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:356:0x0709 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:391:0x06ee  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean update(Context context, ActivityManager activityManager) {
        long j;
        int i;
        long j2;
        long j3;
        ArrayList<MergedItem> arrayList;
        int i2;
        boolean z;
        long j4;
        int i3;
        long j5;
        long j6;
        long j7;
        ArrayList<MergedItem> arrayList2;
        int i4;
        long j8;
        int i5;
        long[] jArr;
        MergedItem mergedItem;
        int i6;
        ArrayList<MergedItem> arrayList3;
        boolean z2;
        int i7;
        List<ActivityManager.RunningAppProcessInfo> list;
        int i8;
        AppProcessInfo appProcessInfo;
        boolean z3;
        int i9;
        AppProcessInfo appProcessInfo2;
        PackageManager packageManager = context.getPackageManager();
        boolean z4 = true;
        this.mSequence++;
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(100);
        int size = runningServices != null ? runningServices.size() : 0;
        int i10 = 0;
        while (i10 < size) {
            ActivityManager.RunningServiceInfo runningServiceInfo = runningServices.get(i10);
            if (!runningServiceInfo.started && runningServiceInfo.clientLabel == 0) {
                runningServices.remove(i10);
            } else if ((runningServiceInfo.flags & 8) != 0) {
                runningServices.remove(i10);
            } else {
                i10++;
            }
            i10--;
            size--;
            i10++;
        }
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        int size2 = runningAppProcesses != null ? runningAppProcesses.size() : 0;
        this.mTmpAppProcesses.clear();
        for (int i11 = 0; i11 < size2; i11++) {
            ActivityManager.RunningAppProcessInfo runningAppProcessInfo = runningAppProcesses.get(i11);
            this.mTmpAppProcesses.put(runningAppProcessInfo.pid, new AppProcessInfo(runningAppProcessInfo));
        }
        int i12 = 0;
        while (true) {
            j = 0;
            if (i12 >= size) {
                break;
            }
            ActivityManager.RunningServiceInfo runningServiceInfo2 = runningServices.get(i12);
            if (runningServiceInfo2.restarting == 0 && (i9 = runningServiceInfo2.pid) > 0 && (appProcessInfo2 = this.mTmpAppProcesses.get(i9)) != null) {
                appProcessInfo2.hasServices = true;
                if (runningServiceInfo2.foreground) {
                    appProcessInfo2.hasForegroundServices = true;
                }
            }
            i12++;
        }
        int i13 = 0;
        boolean z5 = false;
        while (i13 < size) {
            ActivityManager.RunningServiceInfo runningServiceInfo3 = runningServices.get(i13);
            if (runningServiceInfo3.restarting == 0 && (i8 = runningServiceInfo3.pid) > 0 && (appProcessInfo = this.mTmpAppProcesses.get(i8)) != null && !appProcessInfo.hasForegroundServices) {
                ActivityManager.RunningAppProcessInfo runningAppProcessInfo2 = appProcessInfo.info;
                if (runningAppProcessInfo2.importance < 300) {
                    AppProcessInfo appProcessInfo3 = this.mTmpAppProcesses.get(runningAppProcessInfo2.importanceReasonPid);
                    while (appProcessInfo3 != null) {
                        if (appProcessInfo3.hasServices || isInterestingProcess(appProcessInfo3.info)) {
                            z3 = z4;
                            break;
                        }
                        appProcessInfo3 = this.mTmpAppProcesses.get(appProcessInfo3.info.importanceReasonPid);
                    }
                    z3 = false;
                    if (z3) {
                        list = runningAppProcesses;
                        i13++;
                        runningAppProcesses = list;
                        z4 = true;
                    }
                }
            }
            HashMap<String, ProcessItem> hashMap = this.mServiceProcessesByName.get(runningServiceInfo3.uid);
            if (hashMap == null) {
                hashMap = new HashMap<>();
                this.mServiceProcessesByName.put(runningServiceInfo3.uid, hashMap);
            }
            ProcessItem processItem = hashMap.get(runningServiceInfo3.process);
            if (processItem == null) {
                processItem = new ProcessItem(context, runningServiceInfo3.uid, runningServiceInfo3.process);
                hashMap.put(runningServiceInfo3.process, processItem);
                z5 = z4;
            }
            list = runningAppProcesses;
            if (processItem.mCurSeq != this.mSequence) {
                int i14 = runningServiceInfo3.restarting == 0 ? runningServiceInfo3.pid : 0;
                int i15 = processItem.mPid;
                if (i14 != i15) {
                    if (i15 != i14) {
                        if (i15 != 0) {
                            this.mServiceProcessesByPid.remove(i15);
                        }
                        if (i14 != 0) {
                            this.mServiceProcessesByPid.put(i14, processItem);
                        }
                        processItem.mPid = i14;
                    }
                    z5 = true;
                }
                processItem.mDependentProcesses.clear();
                processItem.mCurSeq = this.mSequence;
            }
            z5 |= processItem.updateService(context, runningServiceInfo3);
            i13++;
            runningAppProcesses = list;
            z4 = true;
        }
        List<ActivityManager.RunningAppProcessInfo> list2 = runningAppProcesses;
        int i16 = 0;
        while (i16 < size2) {
            List<ActivityManager.RunningAppProcessInfo> list3 = list2;
            ActivityManager.RunningAppProcessInfo runningAppProcessInfo3 = list3.get(i16);
            ProcessItem processItem2 = this.mServiceProcessesByPid.get(runningAppProcessInfo3.pid);
            if (processItem2 == null) {
                processItem2 = this.mRunningProcesses.get(runningAppProcessInfo3.pid);
                if (processItem2 == null) {
                    processItem2 = new ProcessItem(context, runningAppProcessInfo3.uid, runningAppProcessInfo3.processName);
                    int i17 = runningAppProcessInfo3.pid;
                    processItem2.mPid = i17;
                    this.mRunningProcesses.put(i17, processItem2);
                    z5 = true;
                }
                processItem2.mDependentProcesses.clear();
            }
            if (isInterestingProcess(runningAppProcessInfo3)) {
                if (!this.mInterestingProcesses.contains(processItem2)) {
                    this.mInterestingProcesses.add(processItem2);
                    z5 = true;
                }
                processItem2.mCurSeq = this.mSequence;
                processItem2.mInteresting = true;
                processItem2.ensureLabel(packageManager);
            } else {
                processItem2.mInteresting = false;
            }
            processItem2.mRunningSeq = this.mSequence;
            processItem2.mRunningProcessInfo = runningAppProcessInfo3;
            i16++;
            list2 = list3;
        }
        int size3 = this.mRunningProcesses.size();
        int i18 = 0;
        while (i18 < size3) {
            ProcessItem valueAt = this.mRunningProcesses.valueAt(i18);
            if (valueAt.mRunningSeq == this.mSequence) {
                int i19 = valueAt.mRunningProcessInfo.importanceReasonPid;
                if (i19 != 0) {
                    ProcessItem processItem3 = this.mServiceProcessesByPid.get(i19);
                    if (processItem3 == null) {
                        processItem3 = this.mRunningProcesses.get(i19);
                    }
                    if (processItem3 != null) {
                        processItem3.mDependentProcesses.put(valueAt.mPid, valueAt);
                    }
                } else {
                    valueAt.mClient = null;
                }
                i18++;
            } else {
                SparseArray<ProcessItem> sparseArray = this.mRunningProcesses;
                sparseArray.remove(sparseArray.keyAt(i18));
                size3--;
                z5 = true;
            }
        }
        int size4 = this.mInterestingProcesses.size();
        int i20 = 0;
        while (i20 < size4) {
            ProcessItem processItem4 = this.mInterestingProcesses.get(i20);
            if (!processItem4.mInteresting || this.mRunningProcesses.get(processItem4.mPid) == null) {
                this.mInterestingProcesses.remove(i20);
                i20--;
                size4--;
                i7 = 1;
                z5 = true;
            } else {
                i7 = 1;
            }
            i20 += i7;
        }
        int size5 = this.mServiceProcessesByPid.size();
        for (int i21 = 0; i21 < size5; i21++) {
            ProcessItem valueAt2 = this.mServiceProcessesByPid.valueAt(i21);
            int i22 = valueAt2.mCurSeq;
            int i23 = this.mSequence;
            if (i22 == i23) {
                z5 = valueAt2.buildDependencyChain(context, packageManager, i23) | z5;
            }
        }
        ArrayList arrayList4 = null;
        for (int i24 = 0; i24 < this.mServiceProcessesByName.size(); i24++) {
            HashMap<String, ProcessItem> valueAt3 = this.mServiceProcessesByName.valueAt(i24);
            Iterator<ProcessItem> it = valueAt3.values().iterator();
            while (it.hasNext()) {
                ProcessItem next = it.next();
                if (next.mCurSeq == this.mSequence) {
                    next.ensureLabel(packageManager);
                    if (next.mPid == 0) {
                        next.mDependentProcesses.clear();
                    }
                    Iterator<ServiceItem> it2 = next.mServices.values().iterator();
                    while (it2.hasNext()) {
                        if (it2.next().mCurSeq != this.mSequence) {
                            it2.remove();
                            z5 = true;
                        }
                    }
                } else {
                    it.remove();
                    if (valueAt3.size() == 0) {
                        if (arrayList4 == null) {
                            arrayList4 = new ArrayList();
                        }
                        arrayList4.add(Integer.valueOf(this.mServiceProcessesByName.keyAt(i24)));
                    }
                    int i25 = next.mPid;
                    if (i25 != 0) {
                        this.mServiceProcessesByPid.remove(i25);
                    }
                    z5 = true;
                }
            }
        }
        if (arrayList4 != null) {
            for (int i26 = 0; i26 < arrayList4.size(); i26++) {
                this.mServiceProcessesByName.remove(((Integer) arrayList4.get(i26)).intValue());
            }
        }
        if (z5) {
            ArrayList arrayList5 = new ArrayList();
            for (int i27 = 0; i27 < this.mServiceProcessesByName.size(); i27++) {
                for (ProcessItem processItem5 : this.mServiceProcessesByName.valueAt(i27).values()) {
                    processItem5.mIsSystem = false;
                    processItem5.mIsStarted = true;
                    processItem5.mActiveSince = Long.MAX_VALUE;
                    for (ServiceItem serviceItem : processItem5.mServices.values()) {
                        ServiceInfo serviceInfo = serviceItem.mServiceInfo;
                        if (serviceInfo != null && (serviceInfo.applicationInfo.flags & 1) != 0) {
                            processItem5.mIsSystem = true;
                        }
                        ActivityManager.RunningServiceInfo runningServiceInfo4 = serviceItem.mRunningService;
                        if (runningServiceInfo4 != null && runningServiceInfo4.clientLabel != 0) {
                            processItem5.mIsStarted = false;
                            long j9 = processItem5.mActiveSince;
                            long j10 = runningServiceInfo4.activeSince;
                            if (j9 > j10) {
                                processItem5.mActiveSince = j10;
                            }
                        }
                    }
                    arrayList5.add(processItem5);
                }
            }
            Collections.sort(arrayList5, this.mServiceProcessComparator);
            ArrayList<BaseItem> arrayList6 = new ArrayList<>();
            ArrayList<MergedItem> arrayList7 = new ArrayList<>();
            this.mProcessItems.clear();
            for (int i28 = 0; i28 < arrayList5.size(); i28++) {
                ProcessItem processItem6 = (ProcessItem) arrayList5.get(i28);
                processItem6.mNeedDivider = false;
                processItem6.addDependentProcesses(arrayList6, this.mProcessItems);
                arrayList6.add(processItem6);
                if (processItem6.mPid > 0) {
                    this.mProcessItems.add(processItem6);
                }
                boolean z6 = false;
                for (ServiceItem serviceItem2 : processItem6.mServices.values()) {
                    serviceItem2.mNeedDivider = z6;
                    arrayList6.add(serviceItem2);
                    if (serviceItem2.mMergedItem != null) {
                    }
                    z6 = true;
                }
                MergedItem mergedItem2 = new MergedItem(processItem6.mUserId);
                for (ServiceItem serviceItem3 : processItem6.mServices.values()) {
                    mergedItem2.mServices.add(serviceItem3);
                    serviceItem3.mMergedItem = mergedItem2;
                }
                mergedItem2.mProcess = processItem6;
                mergedItem2.mOtherProcesses.clear();
                for (int size6 = this.mProcessItems.size(); size6 < this.mProcessItems.size() - 1; size6++) {
                    mergedItem2.mOtherProcesses.add(this.mProcessItems.get(size6));
                }
                mergedItem2.update(context, false);
                if (mergedItem2.mUserId != this.mMyUserId) {
                    addOtherUserItem(context, arrayList7, this.mOtherUserMergedItems, mergedItem2);
                } else {
                    arrayList7.add(mergedItem2);
                }
            }
            int size7 = this.mInterestingProcesses.size();
            for (int i29 = 0; i29 < size7; i29++) {
                ProcessItem processItem7 = this.mInterestingProcesses.get(i29);
                if (processItem7.mClient == null && processItem7.mServices.size() <= 0) {
                    if (processItem7.mMergedItem == null) {
                        MergedItem mergedItem3 = new MergedItem(processItem7.mUserId);
                        processItem7.mMergedItem = mergedItem3;
                        mergedItem3.mProcess = processItem7;
                    }
                    processItem7.mMergedItem.update(context, false);
                    MergedItem mergedItem4 = processItem7.mMergedItem;
                    if (mergedItem4.mUserId != this.mMyUserId) {
                        addOtherUserItem(context, arrayList7, this.mOtherUserMergedItems, mergedItem4);
                    } else {
                        arrayList7.add(0, mergedItem4);
                    }
                    this.mProcessItems.add(processItem7);
                }
            }
            int size8 = this.mOtherUserMergedItems.size();
            for (int i30 = 0; i30 < size8; i30++) {
                MergedItem valueAt4 = this.mOtherUserMergedItems.valueAt(i30);
                if (valueAt4.mCurSeq == this.mSequence) {
                    valueAt4.update(context, false);
                }
            }
            i = 0;
            synchronized (this.mLock) {
                this.mItems = arrayList6;
                this.mMergedItems = arrayList7;
            }
        } else {
            i = 0;
        }
        this.mAllProcessItems.clear();
        this.mAllProcessItems.addAll(this.mProcessItems);
        int size9 = this.mRunningProcesses.size();
        int i31 = i;
        int i32 = i31;
        int i33 = i32;
        for (int i34 = i33; i34 < size9; i34++) {
            ProcessItem valueAt5 = this.mRunningProcesses.valueAt(i34);
            if (valueAt5.mCurSeq != this.mSequence) {
                int i35 = valueAt5.mRunningProcessInfo.importance;
                if (i35 >= 400) {
                    i32++;
                    this.mAllProcessItems.add(valueAt5);
                } else if (i35 <= 200) {
                    i33++;
                    this.mAllProcessItems.add(valueAt5);
                } else {
                    Log.i("RunningState", "Unknown non-service process: " + valueAt5.mProcessName + " #" + valueAt5.mPid);
                }
            } else {
                i31++;
            }
        }
        try {
            int size10 = this.mAllProcessItems.size();
            int[] iArr = new int[size10];
            for (int i36 = i; i36 < size10; i36++) {
                try {
                    iArr[i36] = this.mAllProcessItems.get(i36).mPid;
                } catch (RemoteException unused) {
                    i2 = i;
                    j2 = 0;
                    j3 = 0;
                    arrayList = null;
                    j4 = j;
                    z = z5;
                    j5 = j2;
                    i3 = i2;
                    j6 = j3;
                    if (arrayList == null) {
                    }
                    j7 = j6;
                    if (arrayList == null) {
                    }
                    while (i4 < this.mMergedItems.size()) {
                    }
                    synchronized (this.mLock) {
                    }
                }
            }
            long[] processPss = ActivityManager.getService().getProcessPss(iArr);
            j4 = 0;
            j2 = 0;
            j3 = 0;
            z = z5;
            ArrayList<MergedItem> arrayList8 = null;
            int i37 = i;
            i3 = i37;
            int i38 = i3;
            while (i37 < size10) {
                try {
                    ProcessItem processItem8 = this.mAllProcessItems.get(i37);
                    j8 = j4;
                    try {
                        z |= processItem8.updateSize(context, processPss[i37], this.mSequence);
                        if (processItem8.mCurSeq == this.mSequence) {
                            j3 += processItem8.mSize;
                            i5 = size10;
                            arrayList = arrayList8;
                            jArr = processPss;
                        } else {
                            int i39 = processItem8.mRunningProcessInfo.importance;
                            if (i39 >= 400) {
                                long j11 = j8 + processItem8.mSize;
                                if (arrayList8 != null) {
                                    try {
                                        mergedItem = new MergedItem(processItem8.mUserId);
                                        processItem8.mMergedItem = mergedItem;
                                        mergedItem.mProcess = processItem8;
                                        i6 = (mergedItem.mUserId != this.mMyUserId ? 1 : 0) | i3;
                                    } catch (RemoteException unused2) {
                                        arrayList = arrayList8;
                                        i2 = i3;
                                        j = j11;
                                        z5 = z;
                                        j4 = j;
                                        z = z5;
                                        j5 = j2;
                                        i3 = i2;
                                        j6 = j3;
                                        if (arrayList == null) {
                                        }
                                        j7 = j6;
                                        if (arrayList == null) {
                                        }
                                        while (i4 < this.mMergedItems.size()) {
                                        }
                                        synchronized (this.mLock) {
                                        }
                                    }
                                    try {
                                        arrayList8.add(mergedItem);
                                        i5 = size10;
                                        arrayList3 = arrayList8;
                                        jArr = processPss;
                                        z2 = true;
                                        i2 = i6;
                                    } catch (RemoteException unused3) {
                                        arrayList = arrayList8;
                                        i2 = i6;
                                        j = j11;
                                        z5 = z;
                                        j4 = j;
                                        z = z5;
                                        j5 = j2;
                                        i3 = i2;
                                        j6 = j3;
                                        if (arrayList == null) {
                                        }
                                        j7 = j6;
                                        if (arrayList == null) {
                                        }
                                        while (i4 < this.mMergedItems.size()) {
                                        }
                                        synchronized (this.mLock) {
                                        }
                                    }
                                } else {
                                    if (i38 < this.mBackgroundItems.size() && this.mBackgroundItems.get(i38).mProcess == processItem8) {
                                        mergedItem = this.mBackgroundItems.get(i38);
                                        i5 = size10;
                                        arrayList3 = arrayList8;
                                        jArr = processPss;
                                        i2 = i3;
                                        z2 = true;
                                    }
                                    arrayList3 = new ArrayList<>(i32);
                                    int i40 = 0;
                                    while (i40 < i38) {
                                        try {
                                            MergedItem mergedItem5 = this.mBackgroundItems.get(i40);
                                            int i41 = size10;
                                            long[] jArr2 = processPss;
                                            i3 |= mergedItem5.mUserId != this.mMyUserId ? 1 : 0;
                                            arrayList3.add(mergedItem5);
                                            i40++;
                                            size10 = i41;
                                            processPss = jArr2;
                                        } catch (RemoteException unused4) {
                                            arrayList = arrayList3;
                                            i2 = i3;
                                            j = j11;
                                            z5 = z;
                                            j4 = j;
                                            z = z5;
                                            j5 = j2;
                                            i3 = i2;
                                            j6 = j3;
                                            if (arrayList == null) {
                                            }
                                            j7 = j6;
                                            if (arrayList == null) {
                                            }
                                            while (i4 < this.mMergedItems.size()) {
                                            }
                                            synchronized (this.mLock) {
                                            }
                                        }
                                    }
                                    i5 = size10;
                                    jArr = processPss;
                                    mergedItem = new MergedItem(processItem8.mUserId);
                                    processItem8.mMergedItem = mergedItem;
                                    mergedItem.mProcess = processItem8;
                                    i2 = i3 | (mergedItem.mUserId != this.mMyUserId ? 1 : 0);
                                    arrayList3.add(mergedItem);
                                    z2 = true;
                                }
                                try {
                                    mergedItem.update(context, z2);
                                    mergedItem.updateSize(context);
                                    i38++;
                                    arrayList = arrayList3;
                                    i3 = i2;
                                    j4 = j11;
                                    i37++;
                                    arrayList8 = arrayList;
                                    size10 = i5;
                                    processPss = jArr;
                                } catch (RemoteException unused5) {
                                    arrayList = arrayList3;
                                    j = j11;
                                    z5 = z;
                                    j4 = j;
                                    z = z5;
                                    j5 = j2;
                                    i3 = i2;
                                    j6 = j3;
                                    if (arrayList == null) {
                                    }
                                    j7 = j6;
                                    if (arrayList == null) {
                                    }
                                    while (i4 < this.mMergedItems.size()) {
                                    }
                                    synchronized (this.mLock) {
                                    }
                                }
                            } else {
                                i5 = size10;
                                arrayList = arrayList8;
                                jArr = processPss;
                                if (i39 <= 200) {
                                    try {
                                        j2 += processItem8.mSize;
                                    } catch (RemoteException unused6) {
                                        i2 = i3;
                                        z5 = z;
                                        j = j8;
                                        j4 = j;
                                        z = z5;
                                        j5 = j2;
                                        i3 = i2;
                                        j6 = j3;
                                        if (arrayList == null) {
                                        }
                                        j7 = j6;
                                        if (arrayList == null) {
                                        }
                                        while (i4 < this.mMergedItems.size()) {
                                        }
                                        synchronized (this.mLock) {
                                        }
                                    }
                                }
                            }
                        }
                        j4 = j8;
                        i37++;
                        arrayList8 = arrayList;
                        size10 = i5;
                        processPss = jArr;
                    } catch (RemoteException unused7) {
                        arrayList = arrayList8;
                    }
                } catch (RemoteException unused8) {
                    arrayList = arrayList8;
                    j8 = j4;
                }
            }
            arrayList = arrayList8;
            j5 = j2;
        } catch (RemoteException unused9) {
            j2 = 0;
            j3 = 0;
            arrayList = null;
            i2 = 0;
        }
        j6 = j3;
        if (arrayList == null || this.mBackgroundItems.size() <= i32) {
            j7 = j6;
        } else {
            arrayList = new ArrayList<>(i32);
            int i42 = i3;
            int i43 = 0;
            while (i43 < i32) {
                MergedItem mergedItem6 = this.mBackgroundItems.get(i43);
                long j12 = j6;
                i42 |= mergedItem6.mUserId != this.mMyUserId ? 1 : 0;
                arrayList.add(mergedItem6);
                i43++;
                j6 = j12;
            }
            j7 = j6;
            i3 = i42;
        }
        if (arrayList == null) {
            arrayList2 = null;
        } else if (i3 == 0) {
            arrayList2 = arrayList;
        } else {
            arrayList2 = new ArrayList<>();
            int size11 = arrayList.size();
            for (int i44 = 0; i44 < size11; i44++) {
                MergedItem mergedItem7 = arrayList.get(i44);
                if (mergedItem7.mUserId != this.mMyUserId) {
                    addOtherUserItem(context, arrayList2, this.mOtherUserBackgroundItems, mergedItem7);
                } else {
                    arrayList2.add(mergedItem7);
                }
            }
            int size12 = this.mOtherUserBackgroundItems.size();
            for (int i45 = 0; i45 < size12; i45++) {
                MergedItem valueAt6 = this.mOtherUserBackgroundItems.valueAt(i45);
                if (valueAt6.mCurSeq == this.mSequence) {
                    valueAt6.update(context, true);
                    valueAt6.updateSize(context);
                }
            }
        }
        for (i4 = 0; i4 < this.mMergedItems.size(); i4++) {
            this.mMergedItems.get(i4).updateSize(context);
        }
        synchronized (this.mLock) {
            this.mNumBackgroundProcesses = i32;
            this.mNumForegroundProcesses = i33;
            this.mNumServiceProcesses = i31;
            this.mBackgroundProcessMemory = j4;
            this.mForegroundProcessMemory = j5;
            this.mServiceProcessMemory = j7;
            if (arrayList != null) {
                this.mBackgroundItems = arrayList;
                this.mUserBackgroundItems = arrayList2;
                if (this.mWatchingBackgroundItems) {
                    z = true;
                }
            }
            if (!this.mHaveData) {
                this.mHaveData = true;
                this.mLock.notifyAll();
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setWatchingBackgroundItems(boolean z) {
        synchronized (this.mLock) {
            this.mWatchingBackgroundItems = z;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList<MergedItem> getCurrentMergedItems() {
        ArrayList<MergedItem> arrayList;
        synchronized (this.mLock) {
            arrayList = this.mMergedItems;
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList<MergedItem> getCurrentBackgroundItems() {
        ArrayList<MergedItem> arrayList;
        synchronized (this.mLock) {
            arrayList = this.mUserBackgroundItems;
        }
        return arrayList;
    }
}
