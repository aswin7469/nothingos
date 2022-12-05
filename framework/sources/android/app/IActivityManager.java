package android.app;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.ApplicationErrorReport;
import android.app.IActivityController;
import android.app.IApplicationThread;
import android.app.IInstrumentationWatcher;
import android.app.IProcessObserver;
import android.app.IServiceConnection;
import android.app.IStopUserCallback;
import android.app.ITaskStackListener;
import android.app.IUiAutomationConnection;
import android.app.IUidObserver;
import android.app.IUserSwitchObserver;
import android.content.ComponentName;
import android.content.IIntentReceiver;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.LocusId;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.ParceledListSlice;
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IProgressListener;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.StrictMode;
import android.os.WorkSource;
import android.text.TextUtils;
import com.android.internal.os.IResultReceiver;
import java.util.List;
/* loaded from: classes.dex */
public interface IActivityManager extends IInterface {
    void addInstrumentationResults(IApplicationThread iApplicationThread, Bundle bundle) throws RemoteException;

    void addPackageDependency(String str) throws RemoteException;

    void appNotResponding(String str) throws RemoteException;

    void appNotRespondingViaProvider(IBinder iBinder) throws RemoteException;

    void attachApplication(IApplicationThread iApplicationThread, long j) throws RemoteException;

    void backgroundAllowlistUid(int i) throws RemoteException;

    void backupAgentCreated(String str, IBinder iBinder, int i) throws RemoteException;

    boolean bindBackupAgent(String str, int i, int i2, int i3) throws RemoteException;

    int bindIsolatedService(IApplicationThread iApplicationThread, IBinder iBinder, Intent intent, String str, IServiceConnection iServiceConnection, int i, String str2, String str3, int i2) throws RemoteException;

    int bindService(IApplicationThread iApplicationThread, IBinder iBinder, Intent intent, String str, IServiceConnection iServiceConnection, int i, String str2, int i2) throws RemoteException;

    void bootAnimationComplete() throws RemoteException;

    @Deprecated
    int broadcastIntent(IApplicationThread iApplicationThread, Intent intent, String str, IIntentReceiver iIntentReceiver, int i, String str2, Bundle bundle, String[] strArr, int i2, Bundle bundle2, boolean z, boolean z2, int i3) throws RemoteException;

    int broadcastIntentWithFeature(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IIntentReceiver iIntentReceiver, int i, String str3, Bundle bundle, String[] strArr, String[] strArr2, String[] strArr3, int i2, Bundle bundle2, boolean z, boolean z2, int i3) throws RemoteException;

    void cancelIntentSender(IIntentSender iIntentSender) throws RemoteException;

    void cancelTaskWindowTransition(int i) throws RemoteException;

    int checkPermission(String str, int i, int i2) throws RemoteException;

    int checkUriPermission(Uri uri, int i, int i2, int i3, int i4, IBinder iBinder) throws RemoteException;

    int[] checkUriPermissions(List<Uri> list, int i, int i2, int i3, IBinder iBinder) throws RemoteException;

    boolean clearApplicationUserData(String str, boolean z, IPackageDataObserver iPackageDataObserver, int i) throws RemoteException;

    void closeSystemDialogs(String str) throws RemoteException;

    void crashApplicationWithType(int i, int i2, String str, int i3, String str2, boolean z, int i4) throws RemoteException;

    void crashApplicationWithTypeWithExtras(int i, int i2, String str, int i3, String str2, boolean z, int i4, Bundle bundle) throws RemoteException;

    boolean dumpHeap(String str, int i, boolean z, boolean z2, boolean z3, String str2, ParcelFileDescriptor parcelFileDescriptor, RemoteCallback remoteCallback) throws RemoteException;

    void dumpHeapFinished(String str) throws RemoteException;

    boolean enableAppFreezer(boolean z) throws RemoteException;

    boolean enableFgsNotificationRateLimit(boolean z) throws RemoteException;

    void enterSafeMode() throws RemoteException;

    boolean finishActivity(IBinder iBinder, int i, Intent intent, int i2) throws RemoteException;

    void finishHeavyWeightApp() throws RemoteException;

    void finishInstrumentation(IApplicationThread iApplicationThread, int i, Bundle bundle) throws RemoteException;

    void finishReceiver(IBinder iBinder, int i, String str, Bundle bundle, boolean z, int i2) throws RemoteException;

    void forceStopPackage(String str, int i) throws RemoteException;

    List<ActivityTaskManager.RootTaskInfo> getAllRootTaskInfos() throws RemoteException;

    List<String> getBugreportWhitelistedPackages() throws RemoteException;

    Configuration getConfiguration() throws RemoteException;

    ContentProviderHolder getContentProvider(IApplicationThread iApplicationThread, String str, String str2, int i, boolean z) throws RemoteException;

    ContentProviderHolder getContentProviderExternal(String str, int i, IBinder iBinder, String str2) throws RemoteException;

    UserInfo getCurrentUser() throws RemoteException;

    int getCurrentUserId() throws RemoteException;

    List<String> getDelegatedShellPermissions() throws RemoteException;

    ActivityTaskManager.RootTaskInfo getFocusedRootTaskInfo() throws RemoteException;

    int getForegroundServiceType(ComponentName componentName, IBinder iBinder) throws RemoteException;

    ParceledListSlice<ApplicationExitInfo> getHistoricalProcessExitReasons(String str, int i, int i2, int i3) throws RemoteException;

    ActivityManager.PendingIntentInfo getInfoForIntentSender(IIntentSender iIntentSender) throws RemoteException;

    Intent getIntentForIntentSender(IIntentSender iIntentSender) throws RemoteException;

    @Deprecated
    IIntentSender getIntentSender(int i, String str, IBinder iBinder, String str2, int i2, Intent[] intentArr, String[] strArr, int i3, Bundle bundle, int i4) throws RemoteException;

    IIntentSender getIntentSenderWithFeature(int i, String str, String str2, IBinder iBinder, String str3, int i2, Intent[] intentArr, String[] strArr, int i3, Bundle bundle, int i4) throws RemoteException;

    String getLaunchedFromPackage(IBinder iBinder) throws RemoteException;

    int getLaunchedFromUid(IBinder iBinder) throws RemoteException;

    ParcelFileDescriptor getLifeMonitor() throws RemoteException;

    int getLockTaskModeState() throws RemoteException;

    void getMemoryInfo(ActivityManager.MemoryInfo memoryInfo) throws RemoteException;

    int getMemoryTrimLevel() throws RemoteException;

    void getMyMemoryState(ActivityManager.RunningAppProcessInfo runningAppProcessInfo) throws RemoteException;

    int getPackageProcessState(String str, String str2) throws RemoteException;

    int getProcessLimit() throws RemoteException;

    Debug.MemoryInfo[] getProcessMemoryInfo(int[] iArr) throws RemoteException;

    long[] getProcessPss(int[] iArr) throws RemoteException;

    List<ActivityManager.ProcessErrorStateInfo> getProcessesInErrorState() throws RemoteException;

    @Deprecated
    String getProviderMimeType(Uri uri, int i) throws RemoteException;

    void getProviderMimeTypeAsync(Uri uri, int i, RemoteCallback remoteCallback) throws RemoteException;

    ParceledListSlice getRecentTasks(int i, int i2, int i3) throws RemoteException;

    List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses() throws RemoteException;

    List<ApplicationInfo> getRunningExternalApplications() throws RemoteException;

    PendingIntent getRunningServiceControlPanel(ComponentName componentName) throws RemoteException;

    int[] getRunningUserIds() throws RemoteException;

    List<ActivityManager.RunningServiceInfo> getServices(int i, int i2) throws RemoteException;

    String getTagForIntentSender(IIntentSender iIntentSender, String str) throws RemoteException;

    Rect getTaskBounds(int i) throws RemoteException;

    int getTaskForActivity(IBinder iBinder, boolean z) throws RemoteException;

    List<ActivityManager.RunningTaskInfo> getTasks(int i) throws RemoteException;

    int getUidProcessCapabilities(int i, String str) throws RemoteException;

    int getUidProcessState(int i, String str) throws RemoteException;

    void grantUriPermission(IApplicationThread iApplicationThread, String str, Uri uri, int i, int i2) throws RemoteException;

    void handleApplicationCrash(IBinder iBinder, ApplicationErrorReport.ParcelableCrashInfo parcelableCrashInfo) throws RemoteException;

    void handleApplicationStrictModeViolation(IBinder iBinder, int i, StrictMode.ViolationInfo violationInfo) throws RemoteException;

    boolean handleApplicationWtf(IBinder iBinder, String str, boolean z, ApplicationErrorReport.ParcelableCrashInfo parcelableCrashInfo, int i) throws RemoteException;

    int handleIncomingUser(int i, int i2, int i3, boolean z, boolean z2, String str, String str2) throws RemoteException;

    void hang(IBinder iBinder, boolean z) throws RemoteException;

    void holdLock(IBinder iBinder, int i) throws RemoteException;

    boolean isAppFreezerSupported() throws RemoteException;

    boolean isBackgroundRestricted(String str) throws RemoteException;

    boolean isInLockTaskMode() throws RemoteException;

    boolean isIntentSenderAnActivity(IIntentSender iIntentSender) throws RemoteException;

    boolean isIntentSenderTargetedToPackage(IIntentSender iIntentSender) throws RemoteException;

    boolean isTopActivityImmersive() throws RemoteException;

    boolean isTopOfTask(IBinder iBinder) throws RemoteException;

    boolean isUidActive(int i, String str) throws RemoteException;

    boolean isUserAMonkey() throws RemoteException;

    boolean isUserRunning(int i, int i2) throws RemoteException;

    boolean isVrModePackageEnabled(ComponentName componentName) throws RemoteException;

    boolean isWindowMode(IBinder iBinder) throws RemoteException;

    void killAllBackgroundProcesses() throws RemoteException;

    void killApplication(String str, int i, int i2, String str2) throws RemoteException;

    void killApplicationProcess(String str, int i) throws RemoteException;

    void killBackgroundProcesses(String str, int i) throws RemoteException;

    void killPackageDependents(String str, int i) throws RemoteException;

    boolean killPids(int[] iArr, String str, boolean z) throws RemoteException;

    boolean killProcessesBelowForeground(String str) throws RemoteException;

    void killProcessesWhenImperceptible(int[] iArr, String str) throws RemoteException;

    void killUid(int i, int i2, String str) throws RemoteException;

    void killUidForPermissionChange(int i, int i2, String str) throws RemoteException;

    boolean launchBugReportHandlerApp() throws RemoteException;

    void makePackageIdle(String str, int i) throws RemoteException;

    boolean moveActivityTaskToBack(IBinder iBinder, boolean z) throws RemoteException;

    void moveTaskToFront(IApplicationThread iApplicationThread, String str, int i, int i2, Bundle bundle) throws RemoteException;

    void moveTaskToRootTask(int i, int i2, boolean z) throws RemoteException;

    void noteAlarmFinish(IIntentSender iIntentSender, WorkSource workSource, int i, String str) throws RemoteException;

    void noteAlarmStart(IIntentSender iIntentSender, WorkSource workSource, int i, String str) throws RemoteException;

    void noteWakeupAlarm(IIntentSender iIntentSender, WorkSource workSource, int i, String str, String str2) throws RemoteException;

    void notifyCleartextNetwork(int i, byte[] bArr) throws RemoteException;

    void notifyLockedProfile(int i) throws RemoteException;

    ParcelFileDescriptor openContentUri(String str) throws RemoteException;

    IBinder peekService(Intent intent, String str, String str2) throws RemoteException;

    void performIdleMaintenance() throws RemoteException;

    boolean profileControl(String str, int i, boolean z, ProfilerInfo profilerInfo, int i2) throws RemoteException;

    void publishContentProviders(IApplicationThread iApplicationThread, List<ContentProviderHolder> list) throws RemoteException;

    void publishService(IBinder iBinder, Intent intent, IBinder iBinder2) throws RemoteException;

    ParceledListSlice queryIntentComponentsForIntentSender(IIntentSender iIntentSender, int i) throws RemoteException;

    boolean refContentProvider(IBinder iBinder, int i, int i2) throws RemoteException;

    void registerIntentSenderCancelListener(IIntentSender iIntentSender, IResultReceiver iResultReceiver) throws RemoteException;

    void registerProcessObserver(IProcessObserver iProcessObserver) throws RemoteException;

    Intent registerReceiver(IApplicationThread iApplicationThread, String str, IIntentReceiver iIntentReceiver, IntentFilter intentFilter, String str2, int i, int i2) throws RemoteException;

    Intent registerReceiverWithFeature(IApplicationThread iApplicationThread, String str, String str2, String str3, IIntentReceiver iIntentReceiver, IntentFilter intentFilter, String str4, int i, int i2) throws RemoteException;

    void registerTaskStackListener(ITaskStackListener iTaskStackListener) throws RemoteException;

    void registerUidObserver(IUidObserver iUidObserver, int i, int i2, String str) throws RemoteException;

    void registerUserSwitchObserver(IUserSwitchObserver iUserSwitchObserver, String str) throws RemoteException;

    void removeContentProvider(IBinder iBinder, boolean z) throws RemoteException;

    @Deprecated
    void removeContentProviderExternal(String str, IBinder iBinder) throws RemoteException;

    void removeContentProviderExternalAsUser(String str, IBinder iBinder, int i) throws RemoteException;

    boolean removeTask(int i) throws RemoteException;

    void requestBugReport(int i) throws RemoteException;

    void requestBugReportWithDescription(String str, String str2, int i) throws RemoteException;

    void requestFullBugReport() throws RemoteException;

    void requestInteractiveBugReport() throws RemoteException;

    void requestInteractiveBugReportWithDescription(String str, String str2) throws RemoteException;

    void requestRemoteBugReport() throws RemoteException;

    void requestSystemServerHeapDump() throws RemoteException;

    void requestTelephonyBugReport(String str, String str2) throws RemoteException;

    void requestWifiBugReport(String str, String str2) throws RemoteException;

    void resetAppErrors() throws RemoteException;

    void resizeTask(int i, Rect rect, int i2) throws RemoteException;

    void restart() throws RemoteException;

    int restartUserInBackground(int i) throws RemoteException;

    void resumeAppSwitches() throws RemoteException;

    void revokeUriPermission(IApplicationThread iApplicationThread, String str, Uri uri, int i, int i2) throws RemoteException;

    void scheduleApplicationInfoChanged(List<String> list, int i) throws RemoteException;

    void sendIdleJobTrigger() throws RemoteException;

    int sendIntentSender(IIntentSender iIntentSender, IBinder iBinder, int i, Intent intent, String str, IIntentReceiver iIntentReceiver, String str2, Bundle bundle) throws RemoteException;

    void serviceDoneExecuting(IBinder iBinder, int i, int i2, int i3) throws RemoteException;

    void setActivityController(IActivityController iActivityController, boolean z) throws RemoteException;

    void setActivityLocusContext(ComponentName componentName, LocusId locusId, IBinder iBinder) throws RemoteException;

    void setAgentApp(String str, String str2) throws RemoteException;

    void setAlwaysFinish(boolean z) throws RemoteException;

    void setDebugApp(String str, boolean z, boolean z2) throws RemoteException;

    void setDumpHeapDebugLimit(String str, int i, long j, String str2) throws RemoteException;

    void setFocusedRootTask(int i) throws RemoteException;

    void setHasTopUi(boolean z) throws RemoteException;

    void setPackageScreenCompatMode(String str, int i) throws RemoteException;

    void setPersistentVrThread(int i) throws RemoteException;

    void setProcessImportant(IBinder iBinder, int i, boolean z, String str) throws RemoteException;

    void setProcessLimit(int i) throws RemoteException;

    boolean setProcessMemoryTrimLevel(String str, int i, int i2) throws RemoteException;

    void setProcessStateSummary(byte[] bArr) throws RemoteException;

    void setRenderThread(int i) throws RemoteException;

    void setRequestedOrientation(IBinder iBinder, int i) throws RemoteException;

    void setServiceForeground(ComponentName componentName, IBinder iBinder, int i, Notification notification, int i2, int i3) throws RemoteException;

    void setTaskResizeable(int i, int i2) throws RemoteException;

    void setUserIsMonkey(boolean z) throws RemoteException;

    void showBootMessage(CharSequence charSequence, boolean z) throws RemoteException;

    void showWaitingForDebugger(IApplicationThread iApplicationThread, boolean z) throws RemoteException;

    boolean shutdown(int i) throws RemoteException;

    void signalPersistentProcesses(int i) throws RemoteException;

    @Deprecated
    int startActivity(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IBinder iBinder, String str3, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle) throws RemoteException;

    @Deprecated
    int startActivityAsUser(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IBinder iBinder, String str3, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle, int i3) throws RemoteException;

    int startActivityAsUserWithFeature(IApplicationThread iApplicationThread, String str, String str2, Intent intent, String str3, IBinder iBinder, String str4, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle, int i3) throws RemoteException;

    int startActivityFromRecents(int i, Bundle bundle) throws RemoteException;

    int startActivityWithFeature(IApplicationThread iApplicationThread, String str, String str2, Intent intent, String str3, IBinder iBinder, String str4, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle) throws RemoteException;

    boolean startBinderTracking() throws RemoteException;

    void startConfirmDeviceCredentialIntent(Intent intent, Bundle bundle) throws RemoteException;

    void startDelegateShellPermissionIdentity(int i, String[] strArr) throws RemoteException;

    boolean startInstrumentation(ComponentName componentName, String str, int i, Bundle bundle, IInstrumentationWatcher iInstrumentationWatcher, IUiAutomationConnection iUiAutomationConnection, int i2, String str2) throws RemoteException;

    boolean startProfile(int i) throws RemoteException;

    ComponentName startService(IApplicationThread iApplicationThread, Intent intent, String str, boolean z, String str2, String str3, int i) throws RemoteException;

    void startSystemLockTaskMode(int i) throws RemoteException;

    boolean startUserInBackground(int i) throws RemoteException;

    boolean startUserInBackgroundWithListener(int i, IProgressListener iProgressListener) throws RemoteException;

    boolean startUserInForegroundWithListener(int i, IProgressListener iProgressListener) throws RemoteException;

    void stopAppSwitches() throws RemoteException;

    boolean stopBinderTrackingAndDump(ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;

    void stopDelegateShellPermissionIdentity() throws RemoteException;

    boolean stopProfile(int i) throws RemoteException;

    int stopService(IApplicationThread iApplicationThread, Intent intent, String str, int i) throws RemoteException;

    boolean stopServiceToken(ComponentName componentName, IBinder iBinder, int i) throws RemoteException;

    int stopUser(int i, boolean z, IStopUserCallback iStopUserCallback) throws RemoteException;

    int stopUserWithDelayedLocking(int i, boolean z, IStopUserCallback iStopUserCallback) throws RemoteException;

    void suppressResizeConfigChanges(boolean z) throws RemoteException;

    boolean switchUser(int i) throws RemoteException;

    void unbindBackupAgent(ApplicationInfo applicationInfo) throws RemoteException;

    void unbindFinished(IBinder iBinder, Intent intent, boolean z) throws RemoteException;

    boolean unbindService(IServiceConnection iServiceConnection) throws RemoteException;

    void unbroadcastIntent(IApplicationThread iApplicationThread, Intent intent, int i) throws RemoteException;

    void unhandledBack() throws RemoteException;

    boolean unlockUser(int i, byte[] bArr, byte[] bArr2, IProgressListener iProgressListener) throws RemoteException;

    void unregisterIntentSenderCancelListener(IIntentSender iIntentSender, IResultReceiver iResultReceiver) throws RemoteException;

    void unregisterProcessObserver(IProcessObserver iProcessObserver) throws RemoteException;

    void unregisterReceiver(IIntentReceiver iIntentReceiver) throws RemoteException;

    void unregisterTaskStackListener(ITaskStackListener iTaskStackListener) throws RemoteException;

    void unregisterUidObserver(IUidObserver iUidObserver) throws RemoteException;

    void unregisterUserSwitchObserver(IUserSwitchObserver iUserSwitchObserver) throws RemoteException;

    void unstableProviderDied(IBinder iBinder) throws RemoteException;

    boolean updateConfiguration(Configuration configuration) throws RemoteException;

    void updateDeviceOwner(String str) throws RemoteException;

    void updateLockTaskPackages(int i, String[] strArr) throws RemoteException;

    boolean updateMccMncConfiguration(String str, String str2) throws RemoteException;

    void updatePersistentConfiguration(Configuration configuration) throws RemoteException;

    void updatePersistentConfigurationWithAttribution(Configuration configuration, String str, String str2) throws RemoteException;

    void updateServiceGroup(IServiceConnection iServiceConnection, int i, int i2) throws RemoteException;

    void waitForBroadcastIdle() throws RemoteException;

    void waitForNetworkStateUpdate(long j) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IActivityManager {
        @Override // android.app.IActivityManager
        public ParcelFileDescriptor openContentUri(String uriString) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void registerUidObserver(IUidObserver observer, int which, int cutpoint, String callingPackage) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void unregisterUidObserver(IUidObserver observer) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean isUidActive(int uid, String callingPackage) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public int getUidProcessState(int uid, String callingPackage) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public int checkPermission(String permission, int pid, int uid) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public void handleApplicationCrash(IBinder app, ApplicationErrorReport.ParcelableCrashInfo crashInfo) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public int startActivity(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public int startActivityWithFeature(IApplicationThread caller, String callingPackage, String callingFeatureId, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public void unhandledBack() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean finishActivity(IBinder token, int code, Intent data, int finishTask) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public Intent registerReceiver(IApplicationThread caller, String callerPackage, IIntentReceiver receiver, IntentFilter filter, String requiredPermission, int userId, int flags) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public Intent registerReceiverWithFeature(IApplicationThread caller, String callerPackage, String callingFeatureId, String receiverId, IIntentReceiver receiver, IntentFilter filter, String requiredPermission, int userId, int flags) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void unregisterReceiver(IIntentReceiver receiver) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public int broadcastIntent(IApplicationThread caller, Intent intent, String resolvedType, IIntentReceiver resultTo, int resultCode, String resultData, Bundle map, String[] requiredPermissions, int appOp, Bundle options, boolean serialized, boolean sticky, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public int broadcastIntentWithFeature(IApplicationThread caller, String callingFeatureId, Intent intent, String resolvedType, IIntentReceiver resultTo, int resultCode, String resultData, Bundle map, String[] requiredPermissions, String[] excludePermissions, String[] excludePackages, int appOp, Bundle options, boolean serialized, boolean sticky, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public void unbroadcastIntent(IApplicationThread caller, Intent intent, int userId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void finishReceiver(IBinder who, int resultCode, String resultData, Bundle map, boolean abortBroadcast, int flags) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void attachApplication(IApplicationThread app, long startSeq) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public List<ActivityManager.RunningTaskInfo> getTasks(int maxNum) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void moveTaskToFront(IApplicationThread caller, String callingPackage, int task, int flags, Bundle options) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public int getTaskForActivity(IBinder token, boolean onlyRoot) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public ContentProviderHolder getContentProvider(IApplicationThread caller, String callingPackage, String name, int userId, boolean stable) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void publishContentProviders(IApplicationThread caller, List<ContentProviderHolder> providers) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean refContentProvider(IBinder connection, int stableDelta, int unstableDelta) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public PendingIntent getRunningServiceControlPanel(ComponentName service) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public ComponentName startService(IApplicationThread caller, Intent service, String resolvedType, boolean requireForeground, String callingPackage, String callingFeatureId, int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public int stopService(IApplicationThread caller, Intent service, String resolvedType, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public int bindService(IApplicationThread caller, IBinder token, Intent service, String resolvedType, IServiceConnection connection, int flags, String callingPackage, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public int bindIsolatedService(IApplicationThread caller, IBinder token, Intent service, String resolvedType, IServiceConnection connection, int flags, String instanceName, String callingPackage, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public void updateServiceGroup(IServiceConnection connection, int group, int importance) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean unbindService(IServiceConnection connection) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void publishService(IBinder token, Intent intent, IBinder service) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setDebugApp(String packageName, boolean waitForDebugger, boolean persistent) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setAgentApp(String packageName, String agent) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setAlwaysFinish(boolean enabled) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean startInstrumentation(ComponentName className, String profileFile, int flags, Bundle arguments, IInstrumentationWatcher watcher, IUiAutomationConnection connection, int userId, String abiOverride) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void addInstrumentationResults(IApplicationThread target, Bundle results) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void finishInstrumentation(IApplicationThread target, int resultCode, Bundle results) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public Configuration getConfiguration() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public boolean updateConfiguration(Configuration values) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public boolean updateMccMncConfiguration(String mcc, String mnc) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public boolean stopServiceToken(ComponentName className, IBinder token, int startId) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void setProcessLimit(int max) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public int getProcessLimit() throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public int checkUriPermission(Uri uri, int pid, int uid, int mode, int userId, IBinder callerToken) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public int[] checkUriPermissions(List<Uri> uris, int pid, int uid, int mode, IBinder callerToken) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void grantUriPermission(IApplicationThread caller, String targetPkg, Uri uri, int mode, int userId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void revokeUriPermission(IApplicationThread caller, String targetPkg, Uri uri, int mode, int userId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setActivityController(IActivityController watcher, boolean imAMonkey) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void showWaitingForDebugger(IApplicationThread who, boolean waiting) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void signalPersistentProcesses(int signal) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public ParceledListSlice getRecentTasks(int maxNum, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void serviceDoneExecuting(IBinder token, int type, int startId, int res) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public IIntentSender getIntentSender(int type, String packageName, IBinder token, String resultWho, int requestCode, Intent[] intents, String[] resolvedTypes, int flags, Bundle options, int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public IIntentSender getIntentSenderWithFeature(int type, String packageName, String featureId, IBinder token, String resultWho, int requestCode, Intent[] intents, String[] resolvedTypes, int flags, Bundle options, int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void cancelIntentSender(IIntentSender sender) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public ActivityManager.PendingIntentInfo getInfoForIntentSender(IIntentSender sender) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void registerIntentSenderCancelListener(IIntentSender sender, IResultReceiver receiver) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void unregisterIntentSenderCancelListener(IIntentSender sender, IResultReceiver receiver) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void enterSafeMode() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void noteWakeupAlarm(IIntentSender sender, WorkSource workSource, int sourceUid, String sourcePkg, String tag) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void removeContentProvider(IBinder connection, boolean stable) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setRequestedOrientation(IBinder token, int requestedOrientation) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void unbindFinished(IBinder token, Intent service, boolean doRebind) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setProcessImportant(IBinder token, int pid, boolean isForeground, String reason) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setServiceForeground(ComponentName className, IBinder token, int id, Notification notification, int flags, int foregroundServiceType) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public int getForegroundServiceType(ComponentName className, IBinder token) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public boolean moveActivityTaskToBack(IBinder token, boolean nonRoot) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void getMemoryInfo(ActivityManager.MemoryInfo outInfo) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public List<ActivityManager.ProcessErrorStateInfo> getProcessesInErrorState() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public boolean clearApplicationUserData(String packageName, boolean keepState, IPackageDataObserver observer, int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void forceStopPackage(String packageName, int userId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean killPids(int[] pids, String reason, boolean secure) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public List<ActivityManager.RunningServiceInfo> getServices(int maxNum, int flags) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public IBinder peekService(Intent service, String resolvedType, String callingPackage) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public boolean profileControl(String process, int userId, boolean start, ProfilerInfo profilerInfo, int profileType) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public boolean shutdown(int timeout) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void stopAppSwitches() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void resumeAppSwitches() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean bindBackupAgent(String packageName, int backupRestoreMode, int targetUserId, int operationType) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void backupAgentCreated(String packageName, IBinder agent, int userId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void unbindBackupAgent(ApplicationInfo appInfo) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public int handleIncomingUser(int callingPid, int callingUid, int userId, boolean allowAll, boolean requireFull, String name, String callerPackage) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public void addPackageDependency(String packageName) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void killApplication(String pkg, int appId, int userId, String reason) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void closeSystemDialogs(String reason) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public Debug.MemoryInfo[] getProcessMemoryInfo(int[] pids) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void killApplicationProcess(String processName, int uid) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean handleApplicationWtf(IBinder app, String tag, boolean system, ApplicationErrorReport.ParcelableCrashInfo crashInfo, int immediateCallerPid) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void killBackgroundProcesses(String packageName, int userId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean isUserAMonkey() throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public List<ApplicationInfo> getRunningExternalApplications() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void finishHeavyWeightApp() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void handleApplicationStrictModeViolation(IBinder app, int penaltyMask, StrictMode.ViolationInfo crashInfo) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean isTopActivityImmersive() throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void crashApplicationWithType(int uid, int initialPid, String packageName, int userId, String message, boolean force, int exceptionTypeId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void crashApplicationWithTypeWithExtras(int uid, int initialPid, String packageName, int userId, String message, boolean force, int exceptionTypeId, Bundle extras) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public String getProviderMimeType(Uri uri, int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void getProviderMimeTypeAsync(Uri uri, int userId, RemoteCallback resultCallback) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean dumpHeap(String process, int userId, boolean managed, boolean mallocInfo, boolean runGc, String path, ParcelFileDescriptor fd, RemoteCallback finishCallback) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public boolean isUserRunning(int userid, int flags) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void setPackageScreenCompatMode(String packageName, int mode) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean switchUser(int userid) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public boolean removeTask(int taskId) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void registerProcessObserver(IProcessObserver observer) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void unregisterProcessObserver(IProcessObserver observer) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean isIntentSenderTargetedToPackage(IIntentSender sender) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void updatePersistentConfiguration(Configuration values) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void updatePersistentConfigurationWithAttribution(Configuration values, String callingPackageName, String callingAttributionTag) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public long[] getProcessPss(int[] pids) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void showBootMessage(CharSequence msg, boolean always) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void killAllBackgroundProcesses() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public ContentProviderHolder getContentProviderExternal(String name, int userId, IBinder token, String tag) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void removeContentProviderExternal(String name, IBinder token) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void removeContentProviderExternalAsUser(String name, IBinder token, int userId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void getMyMemoryState(ActivityManager.RunningAppProcessInfo outInfo) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean killProcessesBelowForeground(String reason) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public UserInfo getCurrentUser() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public int getCurrentUserId() throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public int getLaunchedFromUid(IBinder activityToken) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public void unstableProviderDied(IBinder connection) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean isIntentSenderAnActivity(IIntentSender sender) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public int startActivityAsUser(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public int startActivityAsUserWithFeature(IApplicationThread caller, String callingPackage, String callingFeatureId, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public int stopUser(int userid, boolean force, IStopUserCallback callback) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public int stopUserWithDelayedLocking(int userid, boolean force, IStopUserCallback callback) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public void registerUserSwitchObserver(IUserSwitchObserver observer, String name) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void unregisterUserSwitchObserver(IUserSwitchObserver observer) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public int[] getRunningUserIds() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void requestSystemServerHeapDump() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void requestBugReport(int bugreportType) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void requestBugReportWithDescription(String shareTitle, String shareDescription, int bugreportType) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void requestTelephonyBugReport(String shareTitle, String shareDescription) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void requestWifiBugReport(String shareTitle, String shareDescription) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void requestInteractiveBugReportWithDescription(String shareTitle, String shareDescription) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void requestInteractiveBugReport() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void requestFullBugReport() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void requestRemoteBugReport() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean launchBugReportHandlerApp() throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public List<String> getBugreportWhitelistedPackages() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public Intent getIntentForIntentSender(IIntentSender sender) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public String getLaunchedFromPackage(IBinder activityToken) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void killUid(int appId, int userId, String reason) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setUserIsMonkey(boolean monkey) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void hang(IBinder who, boolean allowRestart) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public List<ActivityTaskManager.RootTaskInfo> getAllRootTaskInfos() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void moveTaskToRootTask(int taskId, int rootTaskId, boolean toTop) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setFocusedRootTask(int taskId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public ActivityTaskManager.RootTaskInfo getFocusedRootTaskInfo() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void restart() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void performIdleMaintenance() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void appNotRespondingViaProvider(IBinder connection) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public Rect getTaskBounds(int taskId) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public boolean setProcessMemoryTrimLevel(String process, int userId, int level) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public String getTagForIntentSender(IIntentSender sender, String prefix) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public boolean startUserInBackground(int userid) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public boolean isInLockTaskMode() throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public int startActivityFromRecents(int taskId, Bundle options) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public void startSystemLockTaskMode(int taskId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean isTopOfTask(IBinder token) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void bootAnimationComplete() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void registerTaskStackListener(ITaskStackListener listener) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void unregisterTaskStackListener(ITaskStackListener listener) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void notifyCleartextNetwork(int uid, byte[] firstPacket) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setTaskResizeable(int taskId, int resizeableMode) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void resizeTask(int taskId, Rect bounds, int resizeMode) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public int getLockTaskModeState() throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public void setDumpHeapDebugLimit(String processName, int uid, long maxMemSize, String reportPackage) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void dumpHeapFinished(String path) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void updateLockTaskPackages(int userId, String[] packages) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void noteAlarmStart(IIntentSender sender, WorkSource workSource, int sourceUid, String tag) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void noteAlarmFinish(IIntentSender sender, WorkSource workSource, int sourceUid, String tag) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public int getPackageProcessState(String packageName, String callingPackage) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public void updateDeviceOwner(String packageName) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean startBinderTracking() throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public boolean stopBinderTrackingAndDump(ParcelFileDescriptor fd) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void suppressResizeConfigChanges(boolean suppress) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean unlockUser(int userid, byte[] token, byte[] secret, IProgressListener listener) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void killPackageDependents(String packageName, int userId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void makePackageIdle(String packageName, int userId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public int getMemoryTrimLevel() throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public boolean isVrModePackageEnabled(ComponentName packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void notifyLockedProfile(int userId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void startConfirmDeviceCredentialIntent(Intent intent, Bundle options) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void sendIdleJobTrigger() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public int sendIntentSender(IIntentSender target, IBinder whitelistToken, int code, Intent intent, String resolvedType, IIntentReceiver finishedReceiver, String requiredPermission, Bundle options) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public boolean isBackgroundRestricted(String packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void setRenderThread(int tid) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setHasTopUi(boolean hasTopUi) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public int restartUserInBackground(int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public void cancelTaskWindowTransition(int taskId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void scheduleApplicationInfoChanged(List<String> packageNames, int userId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setPersistentVrThread(int tid) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void waitForNetworkStateUpdate(long procStateSeq) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void backgroundAllowlistUid(int uid) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean startUserInBackgroundWithListener(int userid, IProgressListener unlockProgressListener) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void startDelegateShellPermissionIdentity(int uid, String[] permissions) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void stopDelegateShellPermissionIdentity() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public List<String> getDelegatedShellPermissions() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public ParcelFileDescriptor getLifeMonitor() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public boolean startUserInForegroundWithListener(int userid, IProgressListener unlockProgressListener) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void appNotResponding(String reason) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public ParceledListSlice<ApplicationExitInfo> getHistoricalProcessExitReasons(String packageName, int pid, int maxNum, int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void killProcessesWhenImperceptible(int[] pids, String reason) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setActivityLocusContext(ComponentName activity, LocusId locusId, IBinder appToken) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setProcessStateSummary(byte[] state) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean isAppFreezerSupported() throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void killUidForPermissionChange(int appId, int userId, String reason) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void resetAppErrors() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean enableAppFreezer(boolean enable) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public boolean enableFgsNotificationRateLimit(boolean enable) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void holdLock(IBinder token, int durationMs) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean startProfile(int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public boolean stopProfile(int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public ParceledListSlice queryIntentComponentsForIntentSender(IIntentSender sender, int matchFlags) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public int getUidProcessCapabilities(int uid, String callingPackage) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public void waitForBroadcastIdle() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean isWindowMode(IBinder token) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IActivityManager {
        public static final String DESCRIPTOR = "android.app.IActivityManager";
        static final int TRANSACTION_addInstrumentationResults = 38;
        static final int TRANSACTION_addPackageDependency = 86;
        static final int TRANSACTION_appNotResponding = 204;
        static final int TRANSACTION_appNotRespondingViaProvider = 154;
        static final int TRANSACTION_attachApplication = 19;
        static final int TRANSACTION_backgroundAllowlistUid = 197;
        static final int TRANSACTION_backupAgentCreated = 83;
        static final int TRANSACTION_bindBackupAgent = 82;
        static final int TRANSACTION_bindIsolatedService = 30;
        static final int TRANSACTION_bindService = 29;
        static final int TRANSACTION_bootAnimationComplete = 163;
        static final int TRANSACTION_broadcastIntent = 15;
        static final int TRANSACTION_broadcastIntentWithFeature = 16;
        static final int TRANSACTION_cancelIntentSender = 57;
        static final int TRANSACTION_cancelTaskWindowTransition = 193;
        static final int TRANSACTION_checkPermission = 6;
        static final int TRANSACTION_checkUriPermission = 46;
        static final int TRANSACTION_checkUriPermissions = 47;
        static final int TRANSACTION_clearApplicationUserData = 72;
        static final int TRANSACTION_closeSystemDialogs = 88;
        static final int TRANSACTION_crashApplicationWithType = 98;
        static final int TRANSACTION_crashApplicationWithTypeWithExtras = 99;
        static final int TRANSACTION_dumpHeap = 102;
        static final int TRANSACTION_dumpHeapFinished = 171;
        static final int TRANSACTION_enableAppFreezer = 212;
        static final int TRANSACTION_enableFgsNotificationRateLimit = 213;
        static final int TRANSACTION_enterSafeMode = 61;
        static final int TRANSACTION_finishActivity = 11;
        static final int TRANSACTION_finishHeavyWeightApp = 95;
        static final int TRANSACTION_finishInstrumentation = 39;
        static final int TRANSACTION_finishReceiver = 18;
        static final int TRANSACTION_forceStopPackage = 73;
        static final int TRANSACTION_getAllRootTaskInfos = 148;
        static final int TRANSACTION_getBugreportWhitelistedPackages = 142;
        static final int TRANSACTION_getConfiguration = 40;
        static final int TRANSACTION_getContentProvider = 23;
        static final int TRANSACTION_getContentProviderExternal = 115;
        static final int TRANSACTION_getCurrentUser = 120;
        static final int TRANSACTION_getCurrentUserId = 121;
        static final int TRANSACTION_getDelegatedShellPermissions = 201;
        static final int TRANSACTION_getFocusedRootTaskInfo = 151;
        static final int TRANSACTION_getForegroundServiceType = 68;
        static final int TRANSACTION_getHistoricalProcessExitReasons = 205;
        static final int TRANSACTION_getInfoForIntentSender = 58;
        static final int TRANSACTION_getIntentForIntentSender = 143;
        static final int TRANSACTION_getIntentSender = 55;
        static final int TRANSACTION_getIntentSenderWithFeature = 56;
        static final int TRANSACTION_getLaunchedFromPackage = 144;
        static final int TRANSACTION_getLaunchedFromUid = 122;
        static final int TRANSACTION_getLifeMonitor = 202;
        static final int TRANSACTION_getLockTaskModeState = 169;
        static final int TRANSACTION_getMemoryInfo = 70;
        static final int TRANSACTION_getMemoryTrimLevel = 183;
        static final int TRANSACTION_getMyMemoryState = 118;
        static final int TRANSACTION_getPackageProcessState = 175;
        static final int TRANSACTION_getProcessLimit = 45;
        static final int TRANSACTION_getProcessMemoryInfo = 89;
        static final int TRANSACTION_getProcessPss = 112;
        static final int TRANSACTION_getProcessesInErrorState = 71;
        static final int TRANSACTION_getProviderMimeType = 100;
        static final int TRANSACTION_getProviderMimeTypeAsync = 101;
        static final int TRANSACTION_getRecentTasks = 53;
        static final int TRANSACTION_getRunningAppProcesses = 76;
        static final int TRANSACTION_getRunningExternalApplications = 94;
        static final int TRANSACTION_getRunningServiceControlPanel = 26;
        static final int TRANSACTION_getRunningUserIds = 131;
        static final int TRANSACTION_getServices = 75;
        static final int TRANSACTION_getTagForIntentSender = 157;
        static final int TRANSACTION_getTaskBounds = 155;
        static final int TRANSACTION_getTaskForActivity = 22;
        static final int TRANSACTION_getTasks = 20;
        static final int TRANSACTION_getUidProcessCapabilities = 218;
        static final int TRANSACTION_getUidProcessState = 5;
        static final int TRANSACTION_grantUriPermission = 48;
        static final int TRANSACTION_handleApplicationCrash = 7;
        static final int TRANSACTION_handleApplicationStrictModeViolation = 96;
        static final int TRANSACTION_handleApplicationWtf = 91;
        static final int TRANSACTION_handleIncomingUser = 85;
        static final int TRANSACTION_hang = 147;
        static final int TRANSACTION_holdLock = 214;
        static final int TRANSACTION_isAppFreezerSupported = 209;
        static final int TRANSACTION_isBackgroundRestricted = 189;
        static final int TRANSACTION_isInLockTaskMode = 159;
        static final int TRANSACTION_isIntentSenderAnActivity = 124;
        static final int TRANSACTION_isIntentSenderTargetedToPackage = 109;
        static final int TRANSACTION_isTopActivityImmersive = 97;
        static final int TRANSACTION_isTopOfTask = 162;
        static final int TRANSACTION_isUidActive = 4;
        static final int TRANSACTION_isUserAMonkey = 93;
        static final int TRANSACTION_isUserRunning = 103;
        static final int TRANSACTION_isVrModePackageEnabled = 184;
        static final int TRANSACTION_isWindowMode = 220;
        static final int TRANSACTION_killAllBackgroundProcesses = 114;
        static final int TRANSACTION_killApplication = 87;
        static final int TRANSACTION_killApplicationProcess = 90;
        static final int TRANSACTION_killBackgroundProcesses = 92;
        static final int TRANSACTION_killPackageDependents = 181;
        static final int TRANSACTION_killPids = 74;
        static final int TRANSACTION_killProcessesBelowForeground = 119;
        static final int TRANSACTION_killProcessesWhenImperceptible = 206;
        static final int TRANSACTION_killUid = 145;
        static final int TRANSACTION_killUidForPermissionChange = 210;
        static final int TRANSACTION_launchBugReportHandlerApp = 141;
        static final int TRANSACTION_makePackageIdle = 182;
        static final int TRANSACTION_moveActivityTaskToBack = 69;
        static final int TRANSACTION_moveTaskToFront = 21;
        static final int TRANSACTION_moveTaskToRootTask = 149;
        static final int TRANSACTION_noteAlarmFinish = 174;
        static final int TRANSACTION_noteAlarmStart = 173;
        static final int TRANSACTION_noteWakeupAlarm = 62;
        static final int TRANSACTION_notifyCleartextNetwork = 166;
        static final int TRANSACTION_notifyLockedProfile = 185;
        static final int TRANSACTION_openContentUri = 1;
        static final int TRANSACTION_peekService = 77;
        static final int TRANSACTION_performIdleMaintenance = 153;
        static final int TRANSACTION_profileControl = 78;
        static final int TRANSACTION_publishContentProviders = 24;
        static final int TRANSACTION_publishService = 33;
        static final int TRANSACTION_queryIntentComponentsForIntentSender = 217;
        static final int TRANSACTION_refContentProvider = 25;
        static final int TRANSACTION_registerIntentSenderCancelListener = 59;
        static final int TRANSACTION_registerProcessObserver = 107;
        static final int TRANSACTION_registerReceiver = 12;
        static final int TRANSACTION_registerReceiverWithFeature = 13;
        static final int TRANSACTION_registerTaskStackListener = 164;
        static final int TRANSACTION_registerUidObserver = 2;
        static final int TRANSACTION_registerUserSwitchObserver = 129;
        static final int TRANSACTION_removeContentProvider = 63;
        static final int TRANSACTION_removeContentProviderExternal = 116;
        static final int TRANSACTION_removeContentProviderExternalAsUser = 117;
        static final int TRANSACTION_removeTask = 106;
        static final int TRANSACTION_requestBugReport = 133;
        static final int TRANSACTION_requestBugReportWithDescription = 134;
        static final int TRANSACTION_requestFullBugReport = 139;
        static final int TRANSACTION_requestInteractiveBugReport = 138;
        static final int TRANSACTION_requestInteractiveBugReportWithDescription = 137;
        static final int TRANSACTION_requestRemoteBugReport = 140;
        static final int TRANSACTION_requestSystemServerHeapDump = 132;
        static final int TRANSACTION_requestTelephonyBugReport = 135;
        static final int TRANSACTION_requestWifiBugReport = 136;
        static final int TRANSACTION_resetAppErrors = 211;
        static final int TRANSACTION_resizeTask = 168;
        static final int TRANSACTION_restart = 152;
        static final int TRANSACTION_restartUserInBackground = 192;
        static final int TRANSACTION_resumeAppSwitches = 81;
        static final int TRANSACTION_revokeUriPermission = 49;
        static final int TRANSACTION_scheduleApplicationInfoChanged = 194;
        static final int TRANSACTION_sendIdleJobTrigger = 187;
        static final int TRANSACTION_sendIntentSender = 188;
        static final int TRANSACTION_serviceDoneExecuting = 54;
        static final int TRANSACTION_setActivityController = 50;
        static final int TRANSACTION_setActivityLocusContext = 207;
        static final int TRANSACTION_setAgentApp = 35;
        static final int TRANSACTION_setAlwaysFinish = 36;
        static final int TRANSACTION_setDebugApp = 34;
        static final int TRANSACTION_setDumpHeapDebugLimit = 170;
        static final int TRANSACTION_setFocusedRootTask = 150;
        static final int TRANSACTION_setHasTopUi = 191;
        static final int TRANSACTION_setPackageScreenCompatMode = 104;
        static final int TRANSACTION_setPersistentVrThread = 195;
        static final int TRANSACTION_setProcessImportant = 66;
        static final int TRANSACTION_setProcessLimit = 44;
        static final int TRANSACTION_setProcessMemoryTrimLevel = 156;
        static final int TRANSACTION_setProcessStateSummary = 208;
        static final int TRANSACTION_setRenderThread = 190;
        static final int TRANSACTION_setRequestedOrientation = 64;
        static final int TRANSACTION_setServiceForeground = 67;
        static final int TRANSACTION_setTaskResizeable = 167;
        static final int TRANSACTION_setUserIsMonkey = 146;
        static final int TRANSACTION_showBootMessage = 113;
        static final int TRANSACTION_showWaitingForDebugger = 51;
        static final int TRANSACTION_shutdown = 79;
        static final int TRANSACTION_signalPersistentProcesses = 52;
        static final int TRANSACTION_startActivity = 8;
        static final int TRANSACTION_startActivityAsUser = 125;
        static final int TRANSACTION_startActivityAsUserWithFeature = 126;
        static final int TRANSACTION_startActivityFromRecents = 160;
        static final int TRANSACTION_startActivityWithFeature = 9;
        static final int TRANSACTION_startBinderTracking = 177;
        static final int TRANSACTION_startConfirmDeviceCredentialIntent = 186;
        static final int TRANSACTION_startDelegateShellPermissionIdentity = 199;
        static final int TRANSACTION_startInstrumentation = 37;
        static final int TRANSACTION_startProfile = 215;
        static final int TRANSACTION_startService = 27;
        static final int TRANSACTION_startSystemLockTaskMode = 161;
        static final int TRANSACTION_startUserInBackground = 158;
        static final int TRANSACTION_startUserInBackgroundWithListener = 198;
        static final int TRANSACTION_startUserInForegroundWithListener = 203;
        static final int TRANSACTION_stopAppSwitches = 80;
        static final int TRANSACTION_stopBinderTrackingAndDump = 178;
        static final int TRANSACTION_stopDelegateShellPermissionIdentity = 200;
        static final int TRANSACTION_stopProfile = 216;
        static final int TRANSACTION_stopService = 28;
        static final int TRANSACTION_stopServiceToken = 43;
        static final int TRANSACTION_stopUser = 127;
        static final int TRANSACTION_stopUserWithDelayedLocking = 128;
        static final int TRANSACTION_suppressResizeConfigChanges = 179;
        static final int TRANSACTION_switchUser = 105;
        static final int TRANSACTION_unbindBackupAgent = 84;
        static final int TRANSACTION_unbindFinished = 65;
        static final int TRANSACTION_unbindService = 32;
        static final int TRANSACTION_unbroadcastIntent = 17;
        static final int TRANSACTION_unhandledBack = 10;
        static final int TRANSACTION_unlockUser = 180;
        static final int TRANSACTION_unregisterIntentSenderCancelListener = 60;
        static final int TRANSACTION_unregisterProcessObserver = 108;
        static final int TRANSACTION_unregisterReceiver = 14;
        static final int TRANSACTION_unregisterTaskStackListener = 165;
        static final int TRANSACTION_unregisterUidObserver = 3;
        static final int TRANSACTION_unregisterUserSwitchObserver = 130;
        static final int TRANSACTION_unstableProviderDied = 123;
        static final int TRANSACTION_updateConfiguration = 41;
        static final int TRANSACTION_updateDeviceOwner = 176;
        static final int TRANSACTION_updateLockTaskPackages = 172;
        static final int TRANSACTION_updateMccMncConfiguration = 42;
        static final int TRANSACTION_updatePersistentConfiguration = 110;
        static final int TRANSACTION_updatePersistentConfigurationWithAttribution = 111;
        static final int TRANSACTION_updateServiceGroup = 31;
        static final int TRANSACTION_waitForBroadcastIdle = 219;
        static final int TRANSACTION_waitForNetworkStateUpdate = 196;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IActivityManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IActivityManager)) {
                return (IActivityManager) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "openContentUri";
                case 2:
                    return "registerUidObserver";
                case 3:
                    return "unregisterUidObserver";
                case 4:
                    return "isUidActive";
                case 5:
                    return "getUidProcessState";
                case 6:
                    return "checkPermission";
                case 7:
                    return "handleApplicationCrash";
                case 8:
                    return "startActivity";
                case 9:
                    return "startActivityWithFeature";
                case 10:
                    return "unhandledBack";
                case 11:
                    return "finishActivity";
                case 12:
                    return "registerReceiver";
                case 13:
                    return "registerReceiverWithFeature";
                case 14:
                    return "unregisterReceiver";
                case 15:
                    return "broadcastIntent";
                case 16:
                    return "broadcastIntentWithFeature";
                case 17:
                    return "unbroadcastIntent";
                case 18:
                    return "finishReceiver";
                case 19:
                    return "attachApplication";
                case 20:
                    return "getTasks";
                case 21:
                    return "moveTaskToFront";
                case 22:
                    return "getTaskForActivity";
                case 23:
                    return "getContentProvider";
                case 24:
                    return "publishContentProviders";
                case 25:
                    return "refContentProvider";
                case 26:
                    return "getRunningServiceControlPanel";
                case 27:
                    return "startService";
                case 28:
                    return "stopService";
                case 29:
                    return "bindService";
                case 30:
                    return "bindIsolatedService";
                case 31:
                    return "updateServiceGroup";
                case 32:
                    return "unbindService";
                case 33:
                    return "publishService";
                case 34:
                    return "setDebugApp";
                case 35:
                    return "setAgentApp";
                case 36:
                    return "setAlwaysFinish";
                case 37:
                    return "startInstrumentation";
                case 38:
                    return "addInstrumentationResults";
                case 39:
                    return "finishInstrumentation";
                case 40:
                    return "getConfiguration";
                case 41:
                    return "updateConfiguration";
                case 42:
                    return "updateMccMncConfiguration";
                case 43:
                    return "stopServiceToken";
                case 44:
                    return "setProcessLimit";
                case 45:
                    return "getProcessLimit";
                case 46:
                    return "checkUriPermission";
                case 47:
                    return "checkUriPermissions";
                case 48:
                    return "grantUriPermission";
                case 49:
                    return "revokeUriPermission";
                case 50:
                    return "setActivityController";
                case 51:
                    return "showWaitingForDebugger";
                case 52:
                    return "signalPersistentProcesses";
                case 53:
                    return "getRecentTasks";
                case 54:
                    return "serviceDoneExecuting";
                case 55:
                    return "getIntentSender";
                case 56:
                    return "getIntentSenderWithFeature";
                case 57:
                    return "cancelIntentSender";
                case 58:
                    return "getInfoForIntentSender";
                case 59:
                    return "registerIntentSenderCancelListener";
                case 60:
                    return "unregisterIntentSenderCancelListener";
                case 61:
                    return "enterSafeMode";
                case 62:
                    return "noteWakeupAlarm";
                case 63:
                    return "removeContentProvider";
                case 64:
                    return "setRequestedOrientation";
                case 65:
                    return "unbindFinished";
                case 66:
                    return "setProcessImportant";
                case 67:
                    return "setServiceForeground";
                case 68:
                    return "getForegroundServiceType";
                case 69:
                    return "moveActivityTaskToBack";
                case 70:
                    return "getMemoryInfo";
                case 71:
                    return "getProcessesInErrorState";
                case 72:
                    return "clearApplicationUserData";
                case 73:
                    return "forceStopPackage";
                case 74:
                    return "killPids";
                case 75:
                    return "getServices";
                case 76:
                    return "getRunningAppProcesses";
                case 77:
                    return "peekService";
                case 78:
                    return "profileControl";
                case 79:
                    return "shutdown";
                case 80:
                    return "stopAppSwitches";
                case 81:
                    return "resumeAppSwitches";
                case 82:
                    return "bindBackupAgent";
                case 83:
                    return "backupAgentCreated";
                case 84:
                    return "unbindBackupAgent";
                case 85:
                    return "handleIncomingUser";
                case 86:
                    return "addPackageDependency";
                case 87:
                    return "killApplication";
                case 88:
                    return "closeSystemDialogs";
                case 89:
                    return "getProcessMemoryInfo";
                case 90:
                    return "killApplicationProcess";
                case 91:
                    return "handleApplicationWtf";
                case 92:
                    return "killBackgroundProcesses";
                case 93:
                    return "isUserAMonkey";
                case 94:
                    return "getRunningExternalApplications";
                case 95:
                    return "finishHeavyWeightApp";
                case 96:
                    return "handleApplicationStrictModeViolation";
                case 97:
                    return "isTopActivityImmersive";
                case 98:
                    return "crashApplicationWithType";
                case 99:
                    return "crashApplicationWithTypeWithExtras";
                case 100:
                    return "getProviderMimeType";
                case 101:
                    return "getProviderMimeTypeAsync";
                case 102:
                    return "dumpHeap";
                case 103:
                    return "isUserRunning";
                case 104:
                    return "setPackageScreenCompatMode";
                case 105:
                    return "switchUser";
                case 106:
                    return "removeTask";
                case 107:
                    return "registerProcessObserver";
                case 108:
                    return "unregisterProcessObserver";
                case 109:
                    return "isIntentSenderTargetedToPackage";
                case 110:
                    return "updatePersistentConfiguration";
                case 111:
                    return "updatePersistentConfigurationWithAttribution";
                case 112:
                    return "getProcessPss";
                case 113:
                    return "showBootMessage";
                case 114:
                    return "killAllBackgroundProcesses";
                case 115:
                    return "getContentProviderExternal";
                case 116:
                    return "removeContentProviderExternal";
                case 117:
                    return "removeContentProviderExternalAsUser";
                case 118:
                    return "getMyMemoryState";
                case 119:
                    return "killProcessesBelowForeground";
                case 120:
                    return "getCurrentUser";
                case 121:
                    return "getCurrentUserId";
                case 122:
                    return "getLaunchedFromUid";
                case 123:
                    return "unstableProviderDied";
                case 124:
                    return "isIntentSenderAnActivity";
                case 125:
                    return "startActivityAsUser";
                case 126:
                    return "startActivityAsUserWithFeature";
                case 127:
                    return "stopUser";
                case 128:
                    return "stopUserWithDelayedLocking";
                case 129:
                    return "registerUserSwitchObserver";
                case 130:
                    return "unregisterUserSwitchObserver";
                case 131:
                    return "getRunningUserIds";
                case 132:
                    return "requestSystemServerHeapDump";
                case 133:
                    return "requestBugReport";
                case 134:
                    return "requestBugReportWithDescription";
                case 135:
                    return "requestTelephonyBugReport";
                case 136:
                    return "requestWifiBugReport";
                case 137:
                    return "requestInteractiveBugReportWithDescription";
                case 138:
                    return "requestInteractiveBugReport";
                case 139:
                    return "requestFullBugReport";
                case 140:
                    return "requestRemoteBugReport";
                case 141:
                    return "launchBugReportHandlerApp";
                case 142:
                    return "getBugreportWhitelistedPackages";
                case 143:
                    return "getIntentForIntentSender";
                case 144:
                    return "getLaunchedFromPackage";
                case 145:
                    return "killUid";
                case 146:
                    return "setUserIsMonkey";
                case 147:
                    return "hang";
                case 148:
                    return "getAllRootTaskInfos";
                case 149:
                    return "moveTaskToRootTask";
                case 150:
                    return "setFocusedRootTask";
                case 151:
                    return "getFocusedRootTaskInfo";
                case 152:
                    return "restart";
                case 153:
                    return "performIdleMaintenance";
                case 154:
                    return "appNotRespondingViaProvider";
                case 155:
                    return "getTaskBounds";
                case 156:
                    return "setProcessMemoryTrimLevel";
                case 157:
                    return "getTagForIntentSender";
                case 158:
                    return "startUserInBackground";
                case 159:
                    return "isInLockTaskMode";
                case 160:
                    return "startActivityFromRecents";
                case 161:
                    return "startSystemLockTaskMode";
                case 162:
                    return "isTopOfTask";
                case 163:
                    return "bootAnimationComplete";
                case 164:
                    return "registerTaskStackListener";
                case 165:
                    return "unregisterTaskStackListener";
                case 166:
                    return "notifyCleartextNetwork";
                case 167:
                    return "setTaskResizeable";
                case 168:
                    return "resizeTask";
                case 169:
                    return "getLockTaskModeState";
                case 170:
                    return "setDumpHeapDebugLimit";
                case 171:
                    return "dumpHeapFinished";
                case 172:
                    return "updateLockTaskPackages";
                case 173:
                    return "noteAlarmStart";
                case 174:
                    return "noteAlarmFinish";
                case 175:
                    return "getPackageProcessState";
                case 176:
                    return "updateDeviceOwner";
                case 177:
                    return "startBinderTracking";
                case 178:
                    return "stopBinderTrackingAndDump";
                case 179:
                    return "suppressResizeConfigChanges";
                case 180:
                    return "unlockUser";
                case 181:
                    return "killPackageDependents";
                case 182:
                    return "makePackageIdle";
                case 183:
                    return "getMemoryTrimLevel";
                case 184:
                    return "isVrModePackageEnabled";
                case 185:
                    return "notifyLockedProfile";
                case 186:
                    return "startConfirmDeviceCredentialIntent";
                case 187:
                    return "sendIdleJobTrigger";
                case 188:
                    return "sendIntentSender";
                case 189:
                    return "isBackgroundRestricted";
                case 190:
                    return "setRenderThread";
                case 191:
                    return "setHasTopUi";
                case 192:
                    return "restartUserInBackground";
                case 193:
                    return "cancelTaskWindowTransition";
                case 194:
                    return "scheduleApplicationInfoChanged";
                case 195:
                    return "setPersistentVrThread";
                case 196:
                    return "waitForNetworkStateUpdate";
                case 197:
                    return "backgroundAllowlistUid";
                case 198:
                    return "startUserInBackgroundWithListener";
                case 199:
                    return "startDelegateShellPermissionIdentity";
                case 200:
                    return "stopDelegateShellPermissionIdentity";
                case 201:
                    return "getDelegatedShellPermissions";
                case 202:
                    return "getLifeMonitor";
                case 203:
                    return "startUserInForegroundWithListener";
                case 204:
                    return "appNotResponding";
                case 205:
                    return "getHistoricalProcessExitReasons";
                case 206:
                    return "killProcessesWhenImperceptible";
                case 207:
                    return "setActivityLocusContext";
                case 208:
                    return "setProcessStateSummary";
                case 209:
                    return "isAppFreezerSupported";
                case 210:
                    return "killUidForPermissionChange";
                case 211:
                    return "resetAppErrors";
                case 212:
                    return "enableAppFreezer";
                case 213:
                    return "enableFgsNotificationRateLimit";
                case 214:
                    return "holdLock";
                case 215:
                    return "startProfile";
                case 216:
                    return "stopProfile";
                case 217:
                    return "queryIntentComponentsForIntentSender";
                case 218:
                    return "getUidProcessCapabilities";
                case 219:
                    return "waitForBroadcastIdle";
                case 220:
                    return "isWindowMode";
                default:
                    return null;
            }
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            ApplicationErrorReport.ParcelableCrashInfo _arg1;
            Intent _arg2;
            ProfilerInfo _arg8;
            Bundle _arg9;
            Intent _arg3;
            ProfilerInfo _arg92;
            Bundle _arg10;
            Intent _arg22;
            IntentFilter _arg32;
            IntentFilter _arg5;
            Intent _arg12;
            Bundle _arg6;
            Bundle _arg93;
            Intent _arg23;
            Bundle _arg7;
            Bundle _arg122;
            Intent _arg13;
            Bundle _arg33;
            Bundle _arg4;
            ComponentName _arg0;
            Intent _arg14;
            Intent _arg15;
            Intent _arg24;
            Intent _arg25;
            Intent _arg16;
            ComponentName _arg02;
            Bundle _arg34;
            Bundle _arg17;
            Bundle _arg26;
            Configuration _arg03;
            ComponentName _arg04;
            Uri _arg05;
            Uri _arg27;
            Uri _arg28;
            Bundle _arg82;
            Bundle _arg94;
            WorkSource _arg18;
            Intent _arg19;
            ComponentName _arg06;
            Notification _arg35;
            ComponentName _arg07;
            Intent _arg08;
            ProfilerInfo _arg36;
            ApplicationInfo _arg09;
            ApplicationErrorReport.ParcelableCrashInfo _arg37;
            StrictMode.ViolationInfo _arg29;
            Bundle _arg72;
            Uri _arg010;
            Uri _arg011;
            RemoteCallback _arg210;
            ParcelFileDescriptor _arg62;
            RemoteCallback _arg73;
            Configuration _arg012;
            Configuration _arg013;
            CharSequence _arg014;
            Intent _arg211;
            ProfilerInfo _arg83;
            Bundle _arg95;
            Intent _arg38;
            ProfilerInfo _arg96;
            Bundle _arg102;
            Bundle _arg110;
            Rect _arg111;
            WorkSource _arg112;
            WorkSource _arg113;
            ParcelFileDescriptor _arg015;
            ComponentName _arg016;
            Intent _arg017;
            Bundle _arg114;
            Intent _arg39;
            Bundle _arg74;
            ComponentName _arg018;
            LocusId _arg115;
            switch (code) {
                case IBinder.INTERFACE_TRANSACTION /* 1598968902 */:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    IBinder iBinder = null;
                    boolean z = false;
                    switch (code) {
                        case 1:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg019 = data.readString();
                            ParcelFileDescriptor _result = openContentUri(_arg019);
                            reply.writeNoException();
                            if (_result != null) {
                                reply.writeInt(1);
                                _result.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 2:
                            data.enforceInterface(DESCRIPTOR);
                            IUidObserver _arg020 = IUidObserver.Stub.asInterface(data.readStrongBinder());
                            int _arg116 = data.readInt();
                            int _arg212 = data.readInt();
                            String _arg310 = data.readString();
                            registerUidObserver(_arg020, _arg116, _arg212, _arg310);
                            reply.writeNoException();
                            return true;
                        case 3:
                            data.enforceInterface(DESCRIPTOR);
                            IUidObserver _arg021 = IUidObserver.Stub.asInterface(data.readStrongBinder());
                            unregisterUidObserver(_arg021);
                            reply.writeNoException();
                            return true;
                        case 4:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg022 = data.readInt();
                            String _arg117 = data.readString();
                            boolean isUidActive = isUidActive(_arg022, _arg117);
                            reply.writeNoException();
                            reply.writeInt(isUidActive ? 1 : 0);
                            return true;
                        case 5:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg023 = data.readInt();
                            String _arg118 = data.readString();
                            int _result2 = getUidProcessState(_arg023, _arg118);
                            reply.writeNoException();
                            reply.writeInt(_result2);
                            return true;
                        case 6:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg024 = data.readString();
                            int _arg119 = data.readInt();
                            int _arg213 = data.readInt();
                            int _result3 = checkPermission(_arg024, _arg119, _arg213);
                            reply.writeNoException();
                            reply.writeInt(_result3);
                            return true;
                        case 7:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg025 = data.readStrongBinder();
                            if (data.readInt() != 0) {
                                _arg1 = ApplicationErrorReport.ParcelableCrashInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg1 = null;
                            }
                            handleApplicationCrash(_arg025, _arg1);
                            reply.writeNoException();
                            return true;
                        case 8:
                            data.enforceInterface(DESCRIPTOR);
                            IApplicationThread _arg026 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            String _arg120 = data.readString();
                            if (data.readInt() != 0) {
                                _arg2 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg2 = null;
                            }
                            String _arg311 = data.readString();
                            IBinder _arg42 = data.readStrongBinder();
                            String _arg52 = data.readString();
                            int _arg63 = data.readInt();
                            int _arg75 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg8 = ProfilerInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg8 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg9 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg9 = null;
                            }
                            int _result4 = startActivity(_arg026, _arg120, _arg2, _arg311, _arg42, _arg52, _arg63, _arg75, _arg8, _arg9);
                            reply.writeNoException();
                            reply.writeInt(_result4);
                            return true;
                        case 9:
                            data.enforceInterface(DESCRIPTOR);
                            IApplicationThread _arg027 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            String _arg121 = data.readString();
                            String _arg214 = data.readString();
                            if (data.readInt() != 0) {
                                _arg3 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg3 = null;
                            }
                            String _arg43 = data.readString();
                            IBinder _arg53 = data.readStrongBinder();
                            String _arg64 = data.readString();
                            int _arg76 = data.readInt();
                            int _arg84 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg92 = ProfilerInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg92 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg10 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg10 = null;
                            }
                            int _result5 = startActivityWithFeature(_arg027, _arg121, _arg214, _arg3, _arg43, _arg53, _arg64, _arg76, _arg84, _arg92, _arg10);
                            reply.writeNoException();
                            reply.writeInt(_result5);
                            return true;
                        case 10:
                            data.enforceInterface(DESCRIPTOR);
                            unhandledBack();
                            reply.writeNoException();
                            return true;
                        case 11:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg028 = data.readStrongBinder();
                            int _arg123 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg22 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg22 = null;
                            }
                            int _arg312 = data.readInt();
                            boolean finishActivity = finishActivity(_arg028, _arg123, _arg22, _arg312);
                            reply.writeNoException();
                            reply.writeInt(finishActivity ? 1 : 0);
                            return true;
                        case 12:
                            data.enforceInterface(DESCRIPTOR);
                            IApplicationThread _arg029 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            String _arg124 = data.readString();
                            IIntentReceiver _arg215 = IIntentReceiver.Stub.asInterface(data.readStrongBinder());
                            if (data.readInt() != 0) {
                                _arg32 = IntentFilter.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg32 = null;
                            }
                            String _arg44 = data.readString();
                            int _arg54 = data.readInt();
                            int _arg65 = data.readInt();
                            Intent _result6 = registerReceiver(_arg029, _arg124, _arg215, _arg32, _arg44, _arg54, _arg65);
                            reply.writeNoException();
                            if (_result6 != null) {
                                reply.writeInt(1);
                                _result6.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 13:
                            data.enforceInterface(DESCRIPTOR);
                            IApplicationThread _arg030 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            String _arg125 = data.readString();
                            String _arg216 = data.readString();
                            String _arg313 = data.readString();
                            IIntentReceiver _arg45 = IIntentReceiver.Stub.asInterface(data.readStrongBinder());
                            if (data.readInt() != 0) {
                                _arg5 = IntentFilter.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg5 = null;
                            }
                            String _arg66 = data.readString();
                            int _arg77 = data.readInt();
                            int _arg85 = data.readInt();
                            Intent _result7 = registerReceiverWithFeature(_arg030, _arg125, _arg216, _arg313, _arg45, _arg5, _arg66, _arg77, _arg85);
                            reply.writeNoException();
                            if (_result7 != null) {
                                reply.writeInt(1);
                                _result7.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 14:
                            data.enforceInterface(DESCRIPTOR);
                            IIntentReceiver _arg031 = IIntentReceiver.Stub.asInterface(data.readStrongBinder());
                            unregisterReceiver(_arg031);
                            reply.writeNoException();
                            return true;
                        case 15:
                            data.enforceInterface(DESCRIPTOR);
                            IApplicationThread _arg032 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            if (data.readInt() != 0) {
                                _arg12 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg12 = null;
                            }
                            String _arg217 = data.readString();
                            IIntentReceiver _arg314 = IIntentReceiver.Stub.asInterface(data.readStrongBinder());
                            int _arg46 = data.readInt();
                            String _arg55 = data.readString();
                            if (data.readInt() != 0) {
                                _arg6 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg6 = null;
                            }
                            String[] _arg78 = data.createStringArray();
                            int _arg86 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg93 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg93 = null;
                            }
                            boolean _arg103 = data.readInt() != 0;
                            boolean _arg11 = data.readInt() != 0;
                            int _arg126 = data.readInt();
                            int _result8 = broadcastIntent(_arg032, _arg12, _arg217, _arg314, _arg46, _arg55, _arg6, _arg78, _arg86, _arg93, _arg103, _arg11, _arg126);
                            reply.writeNoException();
                            reply.writeInt(_result8);
                            return true;
                        case 16:
                            data.enforceInterface(DESCRIPTOR);
                            IApplicationThread _arg033 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            String _arg127 = data.readString();
                            if (data.readInt() != 0) {
                                _arg23 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg23 = null;
                            }
                            String _arg315 = data.readString();
                            IIntentReceiver _arg47 = IIntentReceiver.Stub.asInterface(data.readStrongBinder());
                            int _arg56 = data.readInt();
                            String _arg67 = data.readString();
                            if (data.readInt() != 0) {
                                _arg7 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg7 = null;
                            }
                            String[] _arg87 = data.createStringArray();
                            String[] _arg97 = data.createStringArray();
                            String[] _arg104 = data.createStringArray();
                            int _arg1110 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg122 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg122 = null;
                            }
                            boolean _arg132 = data.readInt() != 0;
                            if (data.readInt() != 0) {
                                z = true;
                            }
                            boolean _arg142 = z;
                            int _arg152 = data.readInt();
                            int _result9 = broadcastIntentWithFeature(_arg033, _arg127, _arg23, _arg315, _arg47, _arg56, _arg67, _arg7, _arg87, _arg97, _arg104, _arg1110, _arg122, _arg132, _arg142, _arg152);
                            reply.writeNoException();
                            reply.writeInt(_result9);
                            return true;
                        case 17:
                            data.enforceInterface(DESCRIPTOR);
                            IApplicationThread _arg034 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            if (data.readInt() != 0) {
                                _arg13 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg13 = null;
                            }
                            int _arg218 = data.readInt();
                            unbroadcastIntent(_arg034, _arg13, _arg218);
                            reply.writeNoException();
                            return true;
                        case 18:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg035 = data.readStrongBinder();
                            int _arg128 = data.readInt();
                            String _arg219 = data.readString();
                            if (data.readInt() != 0) {
                                _arg33 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg33 = null;
                            }
                            boolean _arg48 = data.readInt() != 0;
                            int _arg57 = data.readInt();
                            finishReceiver(_arg035, _arg128, _arg219, _arg33, _arg48, _arg57);
                            return true;
                        case 19:
                            data.enforceInterface(DESCRIPTOR);
                            IApplicationThread _arg036 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            long _arg129 = data.readLong();
                            attachApplication(_arg036, _arg129);
                            reply.writeNoException();
                            return true;
                        case 20:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg037 = data.readInt();
                            List<ActivityManager.RunningTaskInfo> _result10 = getTasks(_arg037);
                            reply.writeNoException();
                            reply.writeTypedList(_result10);
                            return true;
                        case 21:
                            data.enforceInterface(DESCRIPTOR);
                            IApplicationThread _arg038 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            String _arg130 = data.readString();
                            int _arg220 = data.readInt();
                            int _arg316 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg4 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg4 = null;
                            }
                            moveTaskToFront(_arg038, _arg130, _arg220, _arg316, _arg4);
                            reply.writeNoException();
                            return true;
                        case 22:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg039 = data.readStrongBinder();
                            if (data.readInt() != 0) {
                                z = true;
                            }
                            boolean _arg131 = z;
                            int _result11 = getTaskForActivity(_arg039, _arg131);
                            reply.writeNoException();
                            reply.writeInt(_result11);
                            return true;
                        case 23:
                            data.enforceInterface(DESCRIPTOR);
                            IApplicationThread _arg040 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            String _arg133 = data.readString();
                            String _arg221 = data.readString();
                            int _arg317 = data.readInt();
                            boolean _arg49 = data.readInt() != 0;
                            ContentProviderHolder _result12 = getContentProvider(_arg040, _arg133, _arg221, _arg317, _arg49);
                            reply.writeNoException();
                            if (_result12 != null) {
                                reply.writeInt(1);
                                _result12.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 24:
                            data.enforceInterface(DESCRIPTOR);
                            IApplicationThread _arg041 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            List<ContentProviderHolder> _arg134 = data.createTypedArrayList(ContentProviderHolder.CREATOR);
                            publishContentProviders(_arg041, _arg134);
                            reply.writeNoException();
                            return true;
                        case 25:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg042 = data.readStrongBinder();
                            int _arg135 = data.readInt();
                            int _arg222 = data.readInt();
                            boolean refContentProvider = refContentProvider(_arg042, _arg135, _arg222);
                            reply.writeNoException();
                            reply.writeInt(refContentProvider ? 1 : 0);
                            return true;
                        case 26:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0 = null;
                            }
                            PendingIntent _result13 = getRunningServiceControlPanel(_arg0);
                            reply.writeNoException();
                            if (_result13 != null) {
                                reply.writeInt(1);
                                _result13.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 27:
                            data.enforceInterface(DESCRIPTOR);
                            IApplicationThread _arg043 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            if (data.readInt() != 0) {
                                _arg14 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg14 = null;
                            }
                            String _arg223 = data.readString();
                            boolean _arg318 = data.readInt() != 0;
                            String _arg410 = data.readString();
                            String _arg58 = data.readString();
                            int _arg68 = data.readInt();
                            ComponentName _result14 = startService(_arg043, _arg14, _arg223, _arg318, _arg410, _arg58, _arg68);
                            reply.writeNoException();
                            if (_result14 != null) {
                                reply.writeInt(1);
                                _result14.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 28:
                            data.enforceInterface(DESCRIPTOR);
                            IApplicationThread _arg044 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            if (data.readInt() != 0) {
                                _arg15 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg15 = null;
                            }
                            String _arg224 = data.readString();
                            int _arg319 = data.readInt();
                            int _result15 = stopService(_arg044, _arg15, _arg224, _arg319);
                            reply.writeNoException();
                            reply.writeInt(_result15);
                            return true;
                        case 29:
                            data.enforceInterface(DESCRIPTOR);
                            IApplicationThread _arg045 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            IBinder _arg136 = data.readStrongBinder();
                            if (data.readInt() != 0) {
                                _arg24 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg24 = null;
                            }
                            String _arg320 = data.readString();
                            IServiceConnection _arg411 = IServiceConnection.Stub.asInterface(data.readStrongBinder());
                            int _arg59 = data.readInt();
                            String _arg69 = data.readString();
                            int _arg79 = data.readInt();
                            int _result16 = bindService(_arg045, _arg136, _arg24, _arg320, _arg411, _arg59, _arg69, _arg79);
                            reply.writeNoException();
                            reply.writeInt(_result16);
                            return true;
                        case 30:
                            data.enforceInterface(DESCRIPTOR);
                            IApplicationThread _arg046 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            IBinder _arg137 = data.readStrongBinder();
                            if (data.readInt() != 0) {
                                _arg25 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg25 = null;
                            }
                            String _arg321 = data.readString();
                            IServiceConnection _arg412 = IServiceConnection.Stub.asInterface(data.readStrongBinder());
                            int _arg510 = data.readInt();
                            String _arg610 = data.readString();
                            String _arg710 = data.readString();
                            int _arg88 = data.readInt();
                            int _result17 = bindIsolatedService(_arg046, _arg137, _arg25, _arg321, _arg412, _arg510, _arg610, _arg710, _arg88);
                            reply.writeNoException();
                            reply.writeInt(_result17);
                            return true;
                        case 31:
                            data.enforceInterface(DESCRIPTOR);
                            IServiceConnection _arg047 = IServiceConnection.Stub.asInterface(data.readStrongBinder());
                            int _arg138 = data.readInt();
                            int _arg225 = data.readInt();
                            updateServiceGroup(_arg047, _arg138, _arg225);
                            reply.writeNoException();
                            return true;
                        case 32:
                            data.enforceInterface(DESCRIPTOR);
                            IServiceConnection _arg048 = IServiceConnection.Stub.asInterface(data.readStrongBinder());
                            boolean unbindService = unbindService(_arg048);
                            reply.writeNoException();
                            reply.writeInt(unbindService ? 1 : 0);
                            return true;
                        case 33:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg049 = data.readStrongBinder();
                            if (data.readInt() != 0) {
                                _arg16 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg16 = null;
                            }
                            IBinder _arg226 = data.readStrongBinder();
                            publishService(_arg049, _arg16, _arg226);
                            reply.writeNoException();
                            return true;
                        case 34:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg050 = data.readString();
                            boolean _arg139 = data.readInt() != 0;
                            if (data.readInt() != 0) {
                                z = true;
                            }
                            boolean _arg227 = z;
                            setDebugApp(_arg050, _arg139, _arg227);
                            reply.writeNoException();
                            return true;
                        case 35:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg051 = data.readString();
                            String _arg140 = data.readString();
                            setAgentApp(_arg051, _arg140);
                            reply.writeNoException();
                            return true;
                        case 36:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                z = true;
                            }
                            boolean _arg052 = z;
                            setAlwaysFinish(_arg052);
                            reply.writeNoException();
                            return true;
                        case 37:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg02 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg02 = null;
                            }
                            String _arg141 = data.readString();
                            int _arg228 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg34 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg34 = null;
                            }
                            IInstrumentationWatcher _arg413 = IInstrumentationWatcher.Stub.asInterface(data.readStrongBinder());
                            IUiAutomationConnection _arg511 = IUiAutomationConnection.Stub.asInterface(data.readStrongBinder());
                            int _arg611 = data.readInt();
                            String _arg711 = data.readString();
                            boolean startInstrumentation = startInstrumentation(_arg02, _arg141, _arg228, _arg34, _arg413, _arg511, _arg611, _arg711);
                            reply.writeNoException();
                            reply.writeInt(startInstrumentation ? 1 : 0);
                            return true;
                        case 38:
                            data.enforceInterface(DESCRIPTOR);
                            IApplicationThread _arg053 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            if (data.readInt() != 0) {
                                _arg17 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg17 = null;
                            }
                            addInstrumentationResults(_arg053, _arg17);
                            reply.writeNoException();
                            return true;
                        case 39:
                            data.enforceInterface(DESCRIPTOR);
                            IApplicationThread _arg054 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            int _arg143 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg26 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg26 = null;
                            }
                            finishInstrumentation(_arg054, _arg143, _arg26);
                            reply.writeNoException();
                            return true;
                        case 40:
                            data.enforceInterface(DESCRIPTOR);
                            Configuration _result18 = getConfiguration();
                            reply.writeNoException();
                            if (_result18 != null) {
                                reply.writeInt(1);
                                _result18.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 41:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg03 = Configuration.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg03 = null;
                            }
                            boolean updateConfiguration = updateConfiguration(_arg03);
                            reply.writeNoException();
                            reply.writeInt(updateConfiguration ? 1 : 0);
                            return true;
                        case 42:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg055 = data.readString();
                            String _arg144 = data.readString();
                            boolean updateMccMncConfiguration = updateMccMncConfiguration(_arg055, _arg144);
                            reply.writeNoException();
                            reply.writeInt(updateMccMncConfiguration ? 1 : 0);
                            return true;
                        case 43:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg04 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg04 = null;
                            }
                            IBinder _arg145 = data.readStrongBinder();
                            int _arg229 = data.readInt();
                            boolean stopServiceToken = stopServiceToken(_arg04, _arg145, _arg229);
                            reply.writeNoException();
                            reply.writeInt(stopServiceToken ? 1 : 0);
                            return true;
                        case 44:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg056 = data.readInt();
                            setProcessLimit(_arg056);
                            reply.writeNoException();
                            return true;
                        case 45:
                            data.enforceInterface(DESCRIPTOR);
                            int _result19 = getProcessLimit();
                            reply.writeNoException();
                            reply.writeInt(_result19);
                            return true;
                        case 46:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg05 = Uri.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg05 = null;
                            }
                            int _arg146 = data.readInt();
                            int _arg230 = data.readInt();
                            int _arg322 = data.readInt();
                            int _arg414 = data.readInt();
                            IBinder _arg512 = data.readStrongBinder();
                            int _result20 = checkUriPermission(_arg05, _arg146, _arg230, _arg322, _arg414, _arg512);
                            reply.writeNoException();
                            reply.writeInt(_result20);
                            return true;
                        case 47:
                            data.enforceInterface(DESCRIPTOR);
                            List<Uri> _arg057 = data.createTypedArrayList(Uri.CREATOR);
                            int _arg147 = data.readInt();
                            int _arg231 = data.readInt();
                            int _arg323 = data.readInt();
                            IBinder _arg415 = data.readStrongBinder();
                            int[] _result21 = checkUriPermissions(_arg057, _arg147, _arg231, _arg323, _arg415);
                            reply.writeNoException();
                            reply.writeIntArray(_result21);
                            return true;
                        case 48:
                            data.enforceInterface(DESCRIPTOR);
                            IApplicationThread _arg058 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            String _arg148 = data.readString();
                            if (data.readInt() != 0) {
                                _arg27 = Uri.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg27 = null;
                            }
                            int _arg324 = data.readInt();
                            int _arg416 = data.readInt();
                            grantUriPermission(_arg058, _arg148, _arg27, _arg324, _arg416);
                            reply.writeNoException();
                            return true;
                        case 49:
                            data.enforceInterface(DESCRIPTOR);
                            IApplicationThread _arg059 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            String _arg149 = data.readString();
                            if (data.readInt() != 0) {
                                _arg28 = Uri.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg28 = null;
                            }
                            int _arg325 = data.readInt();
                            int _arg417 = data.readInt();
                            revokeUriPermission(_arg059, _arg149, _arg28, _arg325, _arg417);
                            reply.writeNoException();
                            return true;
                        case 50:
                            data.enforceInterface(DESCRIPTOR);
                            IActivityController _arg060 = IActivityController.Stub.asInterface(data.readStrongBinder());
                            if (data.readInt() != 0) {
                                z = true;
                            }
                            boolean _arg150 = z;
                            setActivityController(_arg060, _arg150);
                            reply.writeNoException();
                            return true;
                        case 51:
                            data.enforceInterface(DESCRIPTOR);
                            IApplicationThread _arg061 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            if (data.readInt() != 0) {
                                z = true;
                            }
                            boolean _arg151 = z;
                            showWaitingForDebugger(_arg061, _arg151);
                            reply.writeNoException();
                            return true;
                        case 52:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg062 = data.readInt();
                            signalPersistentProcesses(_arg062);
                            reply.writeNoException();
                            return true;
                        case 53:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg063 = data.readInt();
                            int _arg153 = data.readInt();
                            int _arg232 = data.readInt();
                            ParceledListSlice _result22 = getRecentTasks(_arg063, _arg153, _arg232);
                            reply.writeNoException();
                            if (_result22 != null) {
                                reply.writeInt(1);
                                _result22.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 54:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg064 = data.readStrongBinder();
                            int _arg154 = data.readInt();
                            int _arg233 = data.readInt();
                            int _arg326 = data.readInt();
                            serviceDoneExecuting(_arg064, _arg154, _arg233, _arg326);
                            return true;
                        case 55:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg065 = data.readInt();
                            String _arg155 = data.readString();
                            IBinder _arg234 = data.readStrongBinder();
                            String _arg327 = data.readString();
                            int _arg418 = data.readInt();
                            Intent[] _arg513 = (Intent[]) data.createTypedArray(Intent.CREATOR);
                            String[] _arg612 = data.createStringArray();
                            int _arg712 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg82 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg82 = null;
                            }
                            int _arg98 = data.readInt();
                            IIntentSender _result23 = getIntentSender(_arg065, _arg155, _arg234, _arg327, _arg418, _arg513, _arg612, _arg712, _arg82, _arg98);
                            reply.writeNoException();
                            if (_result23 != null) {
                                iBinder = _result23.asBinder();
                            }
                            reply.writeStrongBinder(iBinder);
                            return true;
                        case 56:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg066 = data.readInt();
                            String _arg156 = data.readString();
                            String _arg235 = data.readString();
                            IBinder _arg328 = data.readStrongBinder();
                            String _arg419 = data.readString();
                            int _arg514 = data.readInt();
                            Intent[] _arg613 = (Intent[]) data.createTypedArray(Intent.CREATOR);
                            String[] _arg713 = data.createStringArray();
                            int _arg89 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg94 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg94 = null;
                            }
                            int _arg105 = data.readInt();
                            IIntentSender _result24 = getIntentSenderWithFeature(_arg066, _arg156, _arg235, _arg328, _arg419, _arg514, _arg613, _arg713, _arg89, _arg94, _arg105);
                            reply.writeNoException();
                            if (_result24 != null) {
                                iBinder = _result24.asBinder();
                            }
                            reply.writeStrongBinder(iBinder);
                            return true;
                        case 57:
                            data.enforceInterface(DESCRIPTOR);
                            IIntentSender _arg067 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                            cancelIntentSender(_arg067);
                            reply.writeNoException();
                            return true;
                        case 58:
                            data.enforceInterface(DESCRIPTOR);
                            IIntentSender _arg068 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                            ActivityManager.PendingIntentInfo _result25 = getInfoForIntentSender(_arg068);
                            reply.writeNoException();
                            if (_result25 != null) {
                                reply.writeInt(1);
                                _result25.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 59:
                            data.enforceInterface(DESCRIPTOR);
                            IIntentSender _arg069 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                            IResultReceiver _arg157 = IResultReceiver.Stub.asInterface(data.readStrongBinder());
                            registerIntentSenderCancelListener(_arg069, _arg157);
                            reply.writeNoException();
                            return true;
                        case 60:
                            data.enforceInterface(DESCRIPTOR);
                            IIntentSender _arg070 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                            IResultReceiver _arg158 = IResultReceiver.Stub.asInterface(data.readStrongBinder());
                            unregisterIntentSenderCancelListener(_arg070, _arg158);
                            reply.writeNoException();
                            return true;
                        case 61:
                            data.enforceInterface(DESCRIPTOR);
                            enterSafeMode();
                            reply.writeNoException();
                            return true;
                        case 62:
                            data.enforceInterface(DESCRIPTOR);
                            IIntentSender _arg071 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                            if (data.readInt() != 0) {
                                _arg18 = WorkSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg18 = null;
                            }
                            int _arg236 = data.readInt();
                            String _arg329 = data.readString();
                            String _arg420 = data.readString();
                            noteWakeupAlarm(_arg071, _arg18, _arg236, _arg329, _arg420);
                            reply.writeNoException();
                            return true;
                        case 63:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg072 = data.readStrongBinder();
                            if (data.readInt() != 0) {
                                z = true;
                            }
                            boolean _arg159 = z;
                            removeContentProvider(_arg072, _arg159);
                            return true;
                        case 64:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg073 = data.readStrongBinder();
                            int _arg160 = data.readInt();
                            setRequestedOrientation(_arg073, _arg160);
                            reply.writeNoException();
                            return true;
                        case 65:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg074 = data.readStrongBinder();
                            if (data.readInt() != 0) {
                                _arg19 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg19 = null;
                            }
                            if (data.readInt() != 0) {
                                z = true;
                            }
                            boolean _arg237 = z;
                            unbindFinished(_arg074, _arg19, _arg237);
                            reply.writeNoException();
                            return true;
                        case 66:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg075 = data.readStrongBinder();
                            int _arg161 = data.readInt();
                            if (data.readInt() != 0) {
                                z = true;
                            }
                            boolean _arg238 = z;
                            String _arg330 = data.readString();
                            setProcessImportant(_arg075, _arg161, _arg238, _arg330);
                            reply.writeNoException();
                            return true;
                        case 67:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg06 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg06 = null;
                            }
                            IBinder _arg162 = data.readStrongBinder();
                            int _arg239 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg35 = Notification.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg35 = null;
                            }
                            int _arg421 = data.readInt();
                            int _arg515 = data.readInt();
                            setServiceForeground(_arg06, _arg162, _arg239, _arg35, _arg421, _arg515);
                            reply.writeNoException();
                            return true;
                        case 68:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg07 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg07 = null;
                            }
                            IBinder _arg163 = data.readStrongBinder();
                            int _result26 = getForegroundServiceType(_arg07, _arg163);
                            reply.writeNoException();
                            reply.writeInt(_result26);
                            return true;
                        case 69:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg076 = data.readStrongBinder();
                            if (data.readInt() != 0) {
                                z = true;
                            }
                            boolean _arg164 = z;
                            boolean moveActivityTaskToBack = moveActivityTaskToBack(_arg076, _arg164);
                            reply.writeNoException();
                            reply.writeInt(moveActivityTaskToBack ? 1 : 0);
                            return true;
                        case 70:
                            data.enforceInterface(DESCRIPTOR);
                            ActivityManager.MemoryInfo _arg077 = new ActivityManager.MemoryInfo();
                            getMemoryInfo(_arg077);
                            reply.writeNoException();
                            reply.writeInt(1);
                            _arg077.writeToParcel(reply, 1);
                            return true;
                        case 71:
                            data.enforceInterface(DESCRIPTOR);
                            List<ActivityManager.ProcessErrorStateInfo> _result27 = getProcessesInErrorState();
                            reply.writeNoException();
                            reply.writeTypedList(_result27);
                            return true;
                        case 72:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg078 = data.readString();
                            if (data.readInt() != 0) {
                                z = true;
                            }
                            boolean _arg165 = z;
                            IPackageDataObserver _arg240 = IPackageDataObserver.Stub.asInterface(data.readStrongBinder());
                            int _arg331 = data.readInt();
                            boolean clearApplicationUserData = clearApplicationUserData(_arg078, _arg165, _arg240, _arg331);
                            reply.writeNoException();
                            reply.writeInt(clearApplicationUserData ? 1 : 0);
                            return true;
                        case 73:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg079 = data.readString();
                            int _arg166 = data.readInt();
                            forceStopPackage(_arg079, _arg166);
                            reply.writeNoException();
                            return true;
                        case 74:
                            data.enforceInterface(DESCRIPTOR);
                            int[] _arg080 = data.createIntArray();
                            String _arg167 = data.readString();
                            if (data.readInt() != 0) {
                                z = true;
                            }
                            boolean _arg241 = z;
                            boolean killPids = killPids(_arg080, _arg167, _arg241);
                            reply.writeNoException();
                            reply.writeInt(killPids ? 1 : 0);
                            return true;
                        case 75:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg081 = data.readInt();
                            int _arg168 = data.readInt();
                            List<ActivityManager.RunningServiceInfo> _result28 = getServices(_arg081, _arg168);
                            reply.writeNoException();
                            reply.writeTypedList(_result28);
                            return true;
                        case 76:
                            data.enforceInterface(DESCRIPTOR);
                            List<ActivityManager.RunningAppProcessInfo> _result29 = getRunningAppProcesses();
                            reply.writeNoException();
                            reply.writeTypedList(_result29);
                            return true;
                        case 77:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg08 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg08 = null;
                            }
                            String _arg169 = data.readString();
                            String _arg242 = data.readString();
                            IBinder _result30 = peekService(_arg08, _arg169, _arg242);
                            reply.writeNoException();
                            reply.writeStrongBinder(_result30);
                            return true;
                        case 78:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg082 = data.readString();
                            int _arg170 = data.readInt();
                            boolean _arg243 = data.readInt() != 0;
                            if (data.readInt() != 0) {
                                _arg36 = ProfilerInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg36 = null;
                            }
                            int _arg422 = data.readInt();
                            boolean profileControl = profileControl(_arg082, _arg170, _arg243, _arg36, _arg422);
                            reply.writeNoException();
                            reply.writeInt(profileControl ? 1 : 0);
                            return true;
                        case 79:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg083 = data.readInt();
                            boolean shutdown = shutdown(_arg083);
                            reply.writeNoException();
                            reply.writeInt(shutdown ? 1 : 0);
                            return true;
                        case 80:
                            data.enforceInterface(DESCRIPTOR);
                            stopAppSwitches();
                            reply.writeNoException();
                            return true;
                        case 81:
                            data.enforceInterface(DESCRIPTOR);
                            resumeAppSwitches();
                            reply.writeNoException();
                            return true;
                        case 82:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg084 = data.readString();
                            int _arg171 = data.readInt();
                            int _arg244 = data.readInt();
                            int _arg332 = data.readInt();
                            boolean bindBackupAgent = bindBackupAgent(_arg084, _arg171, _arg244, _arg332);
                            reply.writeNoException();
                            reply.writeInt(bindBackupAgent ? 1 : 0);
                            return true;
                        case 83:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg085 = data.readString();
                            IBinder _arg172 = data.readStrongBinder();
                            int _arg245 = data.readInt();
                            backupAgentCreated(_arg085, _arg172, _arg245);
                            reply.writeNoException();
                            return true;
                        case 84:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg09 = ApplicationInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg09 = null;
                            }
                            unbindBackupAgent(_arg09);
                            reply.writeNoException();
                            return true;
                        case 85:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg086 = data.readInt();
                            int _arg173 = data.readInt();
                            int _arg246 = data.readInt();
                            boolean _arg333 = data.readInt() != 0;
                            boolean _arg423 = data.readInt() != 0;
                            String _arg516 = data.readString();
                            String _arg614 = data.readString();
                            int _result31 = handleIncomingUser(_arg086, _arg173, _arg246, _arg333, _arg423, _arg516, _arg614);
                            reply.writeNoException();
                            reply.writeInt(_result31);
                            return true;
                        case 86:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg087 = data.readString();
                            addPackageDependency(_arg087);
                            reply.writeNoException();
                            return true;
                        case 87:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg088 = data.readString();
                            int _arg174 = data.readInt();
                            int _arg247 = data.readInt();
                            String _arg334 = data.readString();
                            killApplication(_arg088, _arg174, _arg247, _arg334);
                            reply.writeNoException();
                            return true;
                        case 88:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg089 = data.readString();
                            closeSystemDialogs(_arg089);
                            reply.writeNoException();
                            return true;
                        case 89:
                            data.enforceInterface(DESCRIPTOR);
                            int[] _arg090 = data.createIntArray();
                            Debug.MemoryInfo[] _result32 = getProcessMemoryInfo(_arg090);
                            reply.writeNoException();
                            reply.writeTypedArray(_result32, 1);
                            return true;
                        case 90:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg091 = data.readString();
                            int _arg175 = data.readInt();
                            killApplicationProcess(_arg091, _arg175);
                            reply.writeNoException();
                            return true;
                        case 91:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg092 = data.readStrongBinder();
                            String _arg176 = data.readString();
                            boolean _arg248 = data.readInt() != 0;
                            if (data.readInt() != 0) {
                                _arg37 = ApplicationErrorReport.ParcelableCrashInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg37 = null;
                            }
                            int _arg424 = data.readInt();
                            boolean handleApplicationWtf = handleApplicationWtf(_arg092, _arg176, _arg248, _arg37, _arg424);
                            reply.writeNoException();
                            reply.writeInt(handleApplicationWtf ? 1 : 0);
                            return true;
                        case 92:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg093 = data.readString();
                            int _arg177 = data.readInt();
                            killBackgroundProcesses(_arg093, _arg177);
                            reply.writeNoException();
                            return true;
                        case 93:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isUserAMonkey = isUserAMonkey();
                            reply.writeNoException();
                            reply.writeInt(isUserAMonkey ? 1 : 0);
                            return true;
                        case 94:
                            data.enforceInterface(DESCRIPTOR);
                            List<ApplicationInfo> _result33 = getRunningExternalApplications();
                            reply.writeNoException();
                            reply.writeTypedList(_result33);
                            return true;
                        case 95:
                            data.enforceInterface(DESCRIPTOR);
                            finishHeavyWeightApp();
                            reply.writeNoException();
                            return true;
                        case 96:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg094 = data.readStrongBinder();
                            int _arg178 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg29 = StrictMode.ViolationInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg29 = null;
                            }
                            handleApplicationStrictModeViolation(_arg094, _arg178, _arg29);
                            reply.writeNoException();
                            return true;
                        case 97:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isTopActivityImmersive = isTopActivityImmersive();
                            reply.writeNoException();
                            reply.writeInt(isTopActivityImmersive ? 1 : 0);
                            return true;
                        case 98:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg095 = data.readInt();
                            int _arg179 = data.readInt();
                            String _arg249 = data.readString();
                            int _arg335 = data.readInt();
                            String _arg425 = data.readString();
                            boolean _arg517 = data.readInt() != 0;
                            int _arg615 = data.readInt();
                            crashApplicationWithType(_arg095, _arg179, _arg249, _arg335, _arg425, _arg517, _arg615);
                            reply.writeNoException();
                            return true;
                        case 99:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg096 = data.readInt();
                            int _arg180 = data.readInt();
                            String _arg250 = data.readString();
                            int _arg336 = data.readInt();
                            String _arg426 = data.readString();
                            boolean _arg518 = data.readInt() != 0;
                            int _arg616 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg72 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg72 = null;
                            }
                            crashApplicationWithTypeWithExtras(_arg096, _arg180, _arg250, _arg336, _arg426, _arg518, _arg616, _arg72);
                            reply.writeNoException();
                            return true;
                        case 100:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg010 = Uri.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg010 = null;
                            }
                            int _arg181 = data.readInt();
                            String _result34 = getProviderMimeType(_arg010, _arg181);
                            reply.writeNoException();
                            reply.writeString(_result34);
                            return true;
                        case 101:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg011 = Uri.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg011 = null;
                            }
                            int _arg182 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg210 = RemoteCallback.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg210 = null;
                            }
                            getProviderMimeTypeAsync(_arg011, _arg182, _arg210);
                            return true;
                        case 102:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg097 = data.readString();
                            int _arg183 = data.readInt();
                            boolean _arg251 = data.readInt() != 0;
                            boolean _arg337 = data.readInt() != 0;
                            boolean _arg427 = data.readInt() != 0;
                            String _arg519 = data.readString();
                            if (data.readInt() != 0) {
                                _arg62 = ParcelFileDescriptor.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg62 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg73 = RemoteCallback.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg73 = null;
                            }
                            boolean dumpHeap = dumpHeap(_arg097, _arg183, _arg251, _arg337, _arg427, _arg519, _arg62, _arg73);
                            reply.writeNoException();
                            reply.writeInt(dumpHeap ? 1 : 0);
                            return true;
                        case 103:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg098 = data.readInt();
                            int _arg184 = data.readInt();
                            boolean isUserRunning = isUserRunning(_arg098, _arg184);
                            reply.writeNoException();
                            reply.writeInt(isUserRunning ? 1 : 0);
                            return true;
                        case 104:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg099 = data.readString();
                            int _arg185 = data.readInt();
                            setPackageScreenCompatMode(_arg099, _arg185);
                            reply.writeNoException();
                            return true;
                        case 105:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0100 = data.readInt();
                            boolean switchUser = switchUser(_arg0100);
                            reply.writeNoException();
                            reply.writeInt(switchUser ? 1 : 0);
                            return true;
                        case 106:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0101 = data.readInt();
                            boolean removeTask = removeTask(_arg0101);
                            reply.writeNoException();
                            reply.writeInt(removeTask ? 1 : 0);
                            return true;
                        case 107:
                            data.enforceInterface(DESCRIPTOR);
                            IProcessObserver _arg0102 = IProcessObserver.Stub.asInterface(data.readStrongBinder());
                            registerProcessObserver(_arg0102);
                            reply.writeNoException();
                            return true;
                        case 108:
                            data.enforceInterface(DESCRIPTOR);
                            IProcessObserver _arg0103 = IProcessObserver.Stub.asInterface(data.readStrongBinder());
                            unregisterProcessObserver(_arg0103);
                            reply.writeNoException();
                            return true;
                        case 109:
                            data.enforceInterface(DESCRIPTOR);
                            IIntentSender _arg0104 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                            boolean isIntentSenderTargetedToPackage = isIntentSenderTargetedToPackage(_arg0104);
                            reply.writeNoException();
                            reply.writeInt(isIntentSenderTargetedToPackage ? 1 : 0);
                            return true;
                        case 110:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg012 = Configuration.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg012 = null;
                            }
                            updatePersistentConfiguration(_arg012);
                            reply.writeNoException();
                            return true;
                        case 111:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg013 = Configuration.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg013 = null;
                            }
                            String _arg186 = data.readString();
                            String _arg252 = data.readString();
                            updatePersistentConfigurationWithAttribution(_arg013, _arg186, _arg252);
                            reply.writeNoException();
                            return true;
                        case 112:
                            data.enforceInterface(DESCRIPTOR);
                            int[] _arg0105 = data.createIntArray();
                            long[] _result35 = getProcessPss(_arg0105);
                            reply.writeNoException();
                            reply.writeLongArray(_result35);
                            return true;
                        case 113:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg014 = TextUtils.CHAR_SEQUENCE_CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg014 = null;
                            }
                            if (data.readInt() != 0) {
                                z = true;
                            }
                            boolean _arg187 = z;
                            showBootMessage(_arg014, _arg187);
                            reply.writeNoException();
                            return true;
                        case 114:
                            data.enforceInterface(DESCRIPTOR);
                            killAllBackgroundProcesses();
                            reply.writeNoException();
                            return true;
                        case 115:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0106 = data.readString();
                            int _arg188 = data.readInt();
                            IBinder _arg253 = data.readStrongBinder();
                            String _arg338 = data.readString();
                            ContentProviderHolder _result36 = getContentProviderExternal(_arg0106, _arg188, _arg253, _arg338);
                            reply.writeNoException();
                            if (_result36 != null) {
                                reply.writeInt(1);
                                _result36.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 116:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0107 = data.readString();
                            IBinder _arg189 = data.readStrongBinder();
                            removeContentProviderExternal(_arg0107, _arg189);
                            reply.writeNoException();
                            return true;
                        case 117:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0108 = data.readString();
                            IBinder _arg190 = data.readStrongBinder();
                            int _arg254 = data.readInt();
                            removeContentProviderExternalAsUser(_arg0108, _arg190, _arg254);
                            reply.writeNoException();
                            return true;
                        case 118:
                            data.enforceInterface(DESCRIPTOR);
                            ActivityManager.RunningAppProcessInfo _arg0109 = new ActivityManager.RunningAppProcessInfo();
                            getMyMemoryState(_arg0109);
                            reply.writeNoException();
                            reply.writeInt(1);
                            _arg0109.writeToParcel(reply, 1);
                            return true;
                        case 119:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0110 = data.readString();
                            boolean killProcessesBelowForeground = killProcessesBelowForeground(_arg0110);
                            reply.writeNoException();
                            reply.writeInt(killProcessesBelowForeground ? 1 : 0);
                            return true;
                        case 120:
                            data.enforceInterface(DESCRIPTOR);
                            UserInfo _result37 = getCurrentUser();
                            reply.writeNoException();
                            if (_result37 != null) {
                                reply.writeInt(1);
                                _result37.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 121:
                            data.enforceInterface(DESCRIPTOR);
                            int _result38 = getCurrentUserId();
                            reply.writeNoException();
                            reply.writeInt(_result38);
                            return true;
                        case 122:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg0111 = data.readStrongBinder();
                            int _result39 = getLaunchedFromUid(_arg0111);
                            reply.writeNoException();
                            reply.writeInt(_result39);
                            return true;
                        case 123:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg0112 = data.readStrongBinder();
                            unstableProviderDied(_arg0112);
                            reply.writeNoException();
                            return true;
                        case 124:
                            data.enforceInterface(DESCRIPTOR);
                            IIntentSender _arg0113 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                            boolean isIntentSenderAnActivity = isIntentSenderAnActivity(_arg0113);
                            reply.writeNoException();
                            reply.writeInt(isIntentSenderAnActivity ? 1 : 0);
                            return true;
                        case 125:
                            data.enforceInterface(DESCRIPTOR);
                            IApplicationThread _arg0114 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            String _arg191 = data.readString();
                            if (data.readInt() != 0) {
                                _arg211 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg211 = null;
                            }
                            String _arg339 = data.readString();
                            IBinder _arg428 = data.readStrongBinder();
                            String _arg520 = data.readString();
                            int _arg617 = data.readInt();
                            int _arg714 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg83 = ProfilerInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg83 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg95 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg95 = null;
                            }
                            int _arg106 = data.readInt();
                            int _result40 = startActivityAsUser(_arg0114, _arg191, _arg211, _arg339, _arg428, _arg520, _arg617, _arg714, _arg83, _arg95, _arg106);
                            reply.writeNoException();
                            reply.writeInt(_result40);
                            return true;
                        case 126:
                            data.enforceInterface(DESCRIPTOR);
                            IApplicationThread _arg0115 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            String _arg192 = data.readString();
                            String _arg255 = data.readString();
                            if (data.readInt() != 0) {
                                _arg38 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg38 = null;
                            }
                            String _arg429 = data.readString();
                            IBinder _arg521 = data.readStrongBinder();
                            String _arg618 = data.readString();
                            int _arg715 = data.readInt();
                            int _arg810 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg96 = ProfilerInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg96 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg102 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg102 = null;
                            }
                            int _arg1111 = data.readInt();
                            int _result41 = startActivityAsUserWithFeature(_arg0115, _arg192, _arg255, _arg38, _arg429, _arg521, _arg618, _arg715, _arg810, _arg96, _arg102, _arg1111);
                            reply.writeNoException();
                            reply.writeInt(_result41);
                            return true;
                        case 127:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0116 = data.readInt();
                            if (data.readInt() != 0) {
                                z = true;
                            }
                            boolean _arg193 = z;
                            IStopUserCallback _arg256 = IStopUserCallback.Stub.asInterface(data.readStrongBinder());
                            int _result42 = stopUser(_arg0116, _arg193, _arg256);
                            reply.writeNoException();
                            reply.writeInt(_result42);
                            return true;
                        case 128:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0117 = data.readInt();
                            if (data.readInt() != 0) {
                                z = true;
                            }
                            boolean _arg194 = z;
                            IStopUserCallback _arg257 = IStopUserCallback.Stub.asInterface(data.readStrongBinder());
                            int _result43 = stopUserWithDelayedLocking(_arg0117, _arg194, _arg257);
                            reply.writeNoException();
                            reply.writeInt(_result43);
                            return true;
                        case 129:
                            data.enforceInterface(DESCRIPTOR);
                            IUserSwitchObserver _arg0118 = IUserSwitchObserver.Stub.asInterface(data.readStrongBinder());
                            String _arg195 = data.readString();
                            registerUserSwitchObserver(_arg0118, _arg195);
                            reply.writeNoException();
                            return true;
                        case 130:
                            data.enforceInterface(DESCRIPTOR);
                            IUserSwitchObserver _arg0119 = IUserSwitchObserver.Stub.asInterface(data.readStrongBinder());
                            unregisterUserSwitchObserver(_arg0119);
                            reply.writeNoException();
                            return true;
                        case 131:
                            data.enforceInterface(DESCRIPTOR);
                            int[] _result44 = getRunningUserIds();
                            reply.writeNoException();
                            reply.writeIntArray(_result44);
                            return true;
                        case 132:
                            data.enforceInterface(DESCRIPTOR);
                            requestSystemServerHeapDump();
                            reply.writeNoException();
                            return true;
                        case 133:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0120 = data.readInt();
                            requestBugReport(_arg0120);
                            reply.writeNoException();
                            return true;
                        case 134:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0121 = data.readString();
                            String _arg196 = data.readString();
                            int _arg258 = data.readInt();
                            requestBugReportWithDescription(_arg0121, _arg196, _arg258);
                            reply.writeNoException();
                            return true;
                        case 135:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0122 = data.readString();
                            String _arg197 = data.readString();
                            requestTelephonyBugReport(_arg0122, _arg197);
                            reply.writeNoException();
                            return true;
                        case 136:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0123 = data.readString();
                            String _arg198 = data.readString();
                            requestWifiBugReport(_arg0123, _arg198);
                            reply.writeNoException();
                            return true;
                        case 137:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0124 = data.readString();
                            String _arg199 = data.readString();
                            requestInteractiveBugReportWithDescription(_arg0124, _arg199);
                            reply.writeNoException();
                            return true;
                        case 138:
                            data.enforceInterface(DESCRIPTOR);
                            requestInteractiveBugReport();
                            reply.writeNoException();
                            return true;
                        case 139:
                            data.enforceInterface(DESCRIPTOR);
                            requestFullBugReport();
                            reply.writeNoException();
                            return true;
                        case 140:
                            data.enforceInterface(DESCRIPTOR);
                            requestRemoteBugReport();
                            reply.writeNoException();
                            return true;
                        case 141:
                            data.enforceInterface(DESCRIPTOR);
                            boolean launchBugReportHandlerApp = launchBugReportHandlerApp();
                            reply.writeNoException();
                            reply.writeInt(launchBugReportHandlerApp ? 1 : 0);
                            return true;
                        case 142:
                            data.enforceInterface(DESCRIPTOR);
                            List<String> _result45 = getBugreportWhitelistedPackages();
                            reply.writeNoException();
                            reply.writeStringList(_result45);
                            return true;
                        case 143:
                            data.enforceInterface(DESCRIPTOR);
                            IIntentSender _arg0125 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                            Intent _result46 = getIntentForIntentSender(_arg0125);
                            reply.writeNoException();
                            if (_result46 != null) {
                                reply.writeInt(1);
                                _result46.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 144:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg0126 = data.readStrongBinder();
                            String _result47 = getLaunchedFromPackage(_arg0126);
                            reply.writeNoException();
                            reply.writeString(_result47);
                            return true;
                        case 145:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0127 = data.readInt();
                            int _arg1100 = data.readInt();
                            String _arg259 = data.readString();
                            killUid(_arg0127, _arg1100, _arg259);
                            reply.writeNoException();
                            return true;
                        case 146:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                z = true;
                            }
                            boolean _arg0128 = z;
                            setUserIsMonkey(_arg0128);
                            reply.writeNoException();
                            return true;
                        case 147:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg0129 = data.readStrongBinder();
                            if (data.readInt() != 0) {
                                z = true;
                            }
                            boolean _arg1101 = z;
                            hang(_arg0129, _arg1101);
                            reply.writeNoException();
                            return true;
                        case 148:
                            data.enforceInterface(DESCRIPTOR);
                            List<ActivityTaskManager.RootTaskInfo> _result48 = getAllRootTaskInfos();
                            reply.writeNoException();
                            reply.writeTypedList(_result48);
                            return true;
                        case 149:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0130 = data.readInt();
                            int _arg1102 = data.readInt();
                            if (data.readInt() != 0) {
                                z = true;
                            }
                            boolean _arg260 = z;
                            moveTaskToRootTask(_arg0130, _arg1102, _arg260);
                            reply.writeNoException();
                            return true;
                        case 150:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0131 = data.readInt();
                            setFocusedRootTask(_arg0131);
                            reply.writeNoException();
                            return true;
                        case 151:
                            data.enforceInterface(DESCRIPTOR);
                            ActivityTaskManager.RootTaskInfo _result49 = getFocusedRootTaskInfo();
                            reply.writeNoException();
                            if (_result49 != null) {
                                reply.writeInt(1);
                                _result49.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 152:
                            data.enforceInterface(DESCRIPTOR);
                            restart();
                            reply.writeNoException();
                            return true;
                        case 153:
                            data.enforceInterface(DESCRIPTOR);
                            performIdleMaintenance();
                            reply.writeNoException();
                            return true;
                        case 154:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg0132 = data.readStrongBinder();
                            appNotRespondingViaProvider(_arg0132);
                            reply.writeNoException();
                            return true;
                        case 155:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0133 = data.readInt();
                            Rect _result50 = getTaskBounds(_arg0133);
                            reply.writeNoException();
                            if (_result50 != null) {
                                reply.writeInt(1);
                                _result50.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 156:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0134 = data.readString();
                            int _arg1103 = data.readInt();
                            int _arg261 = data.readInt();
                            boolean processMemoryTrimLevel = setProcessMemoryTrimLevel(_arg0134, _arg1103, _arg261);
                            reply.writeNoException();
                            reply.writeInt(processMemoryTrimLevel ? 1 : 0);
                            return true;
                        case 157:
                            data.enforceInterface(DESCRIPTOR);
                            IIntentSender _arg0135 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                            String _arg1104 = data.readString();
                            String _result51 = getTagForIntentSender(_arg0135, _arg1104);
                            reply.writeNoException();
                            reply.writeString(_result51);
                            return true;
                        case 158:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0136 = data.readInt();
                            boolean startUserInBackground = startUserInBackground(_arg0136);
                            reply.writeNoException();
                            reply.writeInt(startUserInBackground ? 1 : 0);
                            return true;
                        case 159:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isInLockTaskMode = isInLockTaskMode();
                            reply.writeNoException();
                            reply.writeInt(isInLockTaskMode ? 1 : 0);
                            return true;
                        case 160:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0137 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg110 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg110 = null;
                            }
                            int _result52 = startActivityFromRecents(_arg0137, _arg110);
                            reply.writeNoException();
                            reply.writeInt(_result52);
                            return true;
                        case 161:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0138 = data.readInt();
                            startSystemLockTaskMode(_arg0138);
                            reply.writeNoException();
                            return true;
                        case 162:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg0139 = data.readStrongBinder();
                            boolean isTopOfTask = isTopOfTask(_arg0139);
                            reply.writeNoException();
                            reply.writeInt(isTopOfTask ? 1 : 0);
                            return true;
                        case 163:
                            data.enforceInterface(DESCRIPTOR);
                            bootAnimationComplete();
                            reply.writeNoException();
                            return true;
                        case 164:
                            data.enforceInterface(DESCRIPTOR);
                            ITaskStackListener _arg0140 = ITaskStackListener.Stub.asInterface(data.readStrongBinder());
                            registerTaskStackListener(_arg0140);
                            reply.writeNoException();
                            return true;
                        case 165:
                            data.enforceInterface(DESCRIPTOR);
                            ITaskStackListener _arg0141 = ITaskStackListener.Stub.asInterface(data.readStrongBinder());
                            unregisterTaskStackListener(_arg0141);
                            reply.writeNoException();
                            return true;
                        case 166:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0142 = data.readInt();
                            byte[] _arg1105 = data.createByteArray();
                            notifyCleartextNetwork(_arg0142, _arg1105);
                            reply.writeNoException();
                            return true;
                        case 167:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0143 = data.readInt();
                            int _arg1106 = data.readInt();
                            setTaskResizeable(_arg0143, _arg1106);
                            reply.writeNoException();
                            return true;
                        case 168:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0144 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg111 = Rect.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg111 = null;
                            }
                            int _arg262 = data.readInt();
                            resizeTask(_arg0144, _arg111, _arg262);
                            reply.writeNoException();
                            return true;
                        case 169:
                            data.enforceInterface(DESCRIPTOR);
                            int _result53 = getLockTaskModeState();
                            reply.writeNoException();
                            reply.writeInt(_result53);
                            return true;
                        case 170:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0145 = data.readString();
                            int _arg1107 = data.readInt();
                            long _arg263 = data.readLong();
                            String _arg340 = data.readString();
                            setDumpHeapDebugLimit(_arg0145, _arg1107, _arg263, _arg340);
                            reply.writeNoException();
                            return true;
                        case 171:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0146 = data.readString();
                            dumpHeapFinished(_arg0146);
                            reply.writeNoException();
                            return true;
                        case 172:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0147 = data.readInt();
                            String[] _arg1108 = data.createStringArray();
                            updateLockTaskPackages(_arg0147, _arg1108);
                            reply.writeNoException();
                            return true;
                        case 173:
                            data.enforceInterface(DESCRIPTOR);
                            IIntentSender _arg0148 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                            if (data.readInt() != 0) {
                                _arg112 = WorkSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg112 = null;
                            }
                            int _arg264 = data.readInt();
                            String _arg341 = data.readString();
                            noteAlarmStart(_arg0148, _arg112, _arg264, _arg341);
                            reply.writeNoException();
                            return true;
                        case 174:
                            data.enforceInterface(DESCRIPTOR);
                            IIntentSender _arg0149 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                            if (data.readInt() != 0) {
                                _arg113 = WorkSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg113 = null;
                            }
                            int _arg265 = data.readInt();
                            String _arg342 = data.readString();
                            noteAlarmFinish(_arg0149, _arg113, _arg265, _arg342);
                            reply.writeNoException();
                            return true;
                        case 175:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0150 = data.readString();
                            String _arg1109 = data.readString();
                            int _result54 = getPackageProcessState(_arg0150, _arg1109);
                            reply.writeNoException();
                            reply.writeInt(_result54);
                            return true;
                        case 176:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0151 = data.readString();
                            updateDeviceOwner(_arg0151);
                            reply.writeNoException();
                            return true;
                        case 177:
                            data.enforceInterface(DESCRIPTOR);
                            boolean startBinderTracking = startBinderTracking();
                            reply.writeNoException();
                            reply.writeInt(startBinderTracking ? 1 : 0);
                            return true;
                        case 178:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg015 = ParcelFileDescriptor.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg015 = null;
                            }
                            boolean stopBinderTrackingAndDump = stopBinderTrackingAndDump(_arg015);
                            reply.writeNoException();
                            reply.writeInt(stopBinderTrackingAndDump ? 1 : 0);
                            return true;
                        case 179:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                z = true;
                            }
                            boolean _arg0152 = z;
                            suppressResizeConfigChanges(_arg0152);
                            reply.writeNoException();
                            return true;
                        case 180:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0153 = data.readInt();
                            byte[] _arg1112 = data.createByteArray();
                            byte[] _arg266 = data.createByteArray();
                            IProgressListener _arg343 = IProgressListener.Stub.asInterface(data.readStrongBinder());
                            boolean unlockUser = unlockUser(_arg0153, _arg1112, _arg266, _arg343);
                            reply.writeNoException();
                            reply.writeInt(unlockUser ? 1 : 0);
                            return true;
                        case 181:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0154 = data.readString();
                            int _arg1113 = data.readInt();
                            killPackageDependents(_arg0154, _arg1113);
                            reply.writeNoException();
                            return true;
                        case 182:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0155 = data.readString();
                            int _arg1114 = data.readInt();
                            makePackageIdle(_arg0155, _arg1114);
                            reply.writeNoException();
                            return true;
                        case 183:
                            data.enforceInterface(DESCRIPTOR);
                            int _result55 = getMemoryTrimLevel();
                            reply.writeNoException();
                            reply.writeInt(_result55);
                            return true;
                        case 184:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg016 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg016 = null;
                            }
                            boolean isVrModePackageEnabled = isVrModePackageEnabled(_arg016);
                            reply.writeNoException();
                            reply.writeInt(isVrModePackageEnabled ? 1 : 0);
                            return true;
                        case 185:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0156 = data.readInt();
                            notifyLockedProfile(_arg0156);
                            reply.writeNoException();
                            return true;
                        case 186:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg017 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg017 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg114 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg114 = null;
                            }
                            startConfirmDeviceCredentialIntent(_arg017, _arg114);
                            reply.writeNoException();
                            return true;
                        case 187:
                            data.enforceInterface(DESCRIPTOR);
                            sendIdleJobTrigger();
                            reply.writeNoException();
                            return true;
                        case 188:
                            data.enforceInterface(DESCRIPTOR);
                            IIntentSender _arg0157 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                            IBinder _arg1115 = data.readStrongBinder();
                            int _arg267 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg39 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg39 = null;
                            }
                            String _arg430 = data.readString();
                            IIntentReceiver _arg522 = IIntentReceiver.Stub.asInterface(data.readStrongBinder());
                            String _arg619 = data.readString();
                            if (data.readInt() != 0) {
                                _arg74 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg74 = null;
                            }
                            int _result56 = sendIntentSender(_arg0157, _arg1115, _arg267, _arg39, _arg430, _arg522, _arg619, _arg74);
                            reply.writeNoException();
                            reply.writeInt(_result56);
                            return true;
                        case 189:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0158 = data.readString();
                            boolean isBackgroundRestricted = isBackgroundRestricted(_arg0158);
                            reply.writeNoException();
                            reply.writeInt(isBackgroundRestricted ? 1 : 0);
                            return true;
                        case 190:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0159 = data.readInt();
                            setRenderThread(_arg0159);
                            reply.writeNoException();
                            return true;
                        case 191:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                z = true;
                            }
                            boolean _arg0160 = z;
                            setHasTopUi(_arg0160);
                            reply.writeNoException();
                            return true;
                        case 192:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0161 = data.readInt();
                            int _result57 = restartUserInBackground(_arg0161);
                            reply.writeNoException();
                            reply.writeInt(_result57);
                            return true;
                        case 193:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0162 = data.readInt();
                            cancelTaskWindowTransition(_arg0162);
                            reply.writeNoException();
                            return true;
                        case 194:
                            data.enforceInterface(DESCRIPTOR);
                            List<String> _arg0163 = data.createStringArrayList();
                            int _arg1116 = data.readInt();
                            scheduleApplicationInfoChanged(_arg0163, _arg1116);
                            reply.writeNoException();
                            return true;
                        case 195:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0164 = data.readInt();
                            setPersistentVrThread(_arg0164);
                            reply.writeNoException();
                            return true;
                        case 196:
                            data.enforceInterface(DESCRIPTOR);
                            long _arg0165 = data.readLong();
                            waitForNetworkStateUpdate(_arg0165);
                            reply.writeNoException();
                            return true;
                        case 197:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0166 = data.readInt();
                            backgroundAllowlistUid(_arg0166);
                            reply.writeNoException();
                            return true;
                        case 198:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0167 = data.readInt();
                            IProgressListener _arg1117 = IProgressListener.Stub.asInterface(data.readStrongBinder());
                            boolean startUserInBackgroundWithListener = startUserInBackgroundWithListener(_arg0167, _arg1117);
                            reply.writeNoException();
                            reply.writeInt(startUserInBackgroundWithListener ? 1 : 0);
                            return true;
                        case 199:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0168 = data.readInt();
                            String[] _arg1118 = data.createStringArray();
                            startDelegateShellPermissionIdentity(_arg0168, _arg1118);
                            reply.writeNoException();
                            return true;
                        case 200:
                            data.enforceInterface(DESCRIPTOR);
                            stopDelegateShellPermissionIdentity();
                            reply.writeNoException();
                            return true;
                        case 201:
                            data.enforceInterface(DESCRIPTOR);
                            List<String> _result58 = getDelegatedShellPermissions();
                            reply.writeNoException();
                            reply.writeStringList(_result58);
                            return true;
                        case 202:
                            data.enforceInterface(DESCRIPTOR);
                            ParcelFileDescriptor _result59 = getLifeMonitor();
                            reply.writeNoException();
                            if (_result59 != null) {
                                reply.writeInt(1);
                                _result59.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 203:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0169 = data.readInt();
                            IProgressListener _arg1119 = IProgressListener.Stub.asInterface(data.readStrongBinder());
                            boolean startUserInForegroundWithListener = startUserInForegroundWithListener(_arg0169, _arg1119);
                            reply.writeNoException();
                            reply.writeInt(startUserInForegroundWithListener ? 1 : 0);
                            return true;
                        case 204:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0170 = data.readString();
                            appNotResponding(_arg0170);
                            reply.writeNoException();
                            return true;
                        case 205:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0171 = data.readString();
                            int _arg1120 = data.readInt();
                            int _arg268 = data.readInt();
                            int _arg344 = data.readInt();
                            ParceledListSlice<ApplicationExitInfo> _result60 = getHistoricalProcessExitReasons(_arg0171, _arg1120, _arg268, _arg344);
                            reply.writeNoException();
                            if (_result60 != null) {
                                reply.writeInt(1);
                                _result60.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 206:
                            data.enforceInterface(DESCRIPTOR);
                            int[] _arg0172 = data.createIntArray();
                            String _arg1121 = data.readString();
                            killProcessesWhenImperceptible(_arg0172, _arg1121);
                            reply.writeNoException();
                            return true;
                        case 207:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg018 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg018 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg115 = LocusId.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg115 = null;
                            }
                            IBinder _arg269 = data.readStrongBinder();
                            setActivityLocusContext(_arg018, _arg115, _arg269);
                            reply.writeNoException();
                            return true;
                        case 208:
                            data.enforceInterface(DESCRIPTOR);
                            byte[] _arg0173 = data.createByteArray();
                            setProcessStateSummary(_arg0173);
                            reply.writeNoException();
                            return true;
                        case 209:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isAppFreezerSupported = isAppFreezerSupported();
                            reply.writeNoException();
                            reply.writeInt(isAppFreezerSupported ? 1 : 0);
                            return true;
                        case 210:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0174 = data.readInt();
                            int _arg1122 = data.readInt();
                            String _arg270 = data.readString();
                            killUidForPermissionChange(_arg0174, _arg1122, _arg270);
                            reply.writeNoException();
                            return true;
                        case 211:
                            data.enforceInterface(DESCRIPTOR);
                            resetAppErrors();
                            reply.writeNoException();
                            return true;
                        case 212:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                z = true;
                            }
                            boolean _arg0175 = z;
                            boolean enableAppFreezer = enableAppFreezer(_arg0175);
                            reply.writeNoException();
                            reply.writeInt(enableAppFreezer ? 1 : 0);
                            return true;
                        case 213:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                z = true;
                            }
                            boolean _arg0176 = z;
                            boolean enableFgsNotificationRateLimit = enableFgsNotificationRateLimit(_arg0176);
                            reply.writeNoException();
                            reply.writeInt(enableFgsNotificationRateLimit ? 1 : 0);
                            return true;
                        case 214:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg0177 = data.readStrongBinder();
                            int _arg1123 = data.readInt();
                            holdLock(_arg0177, _arg1123);
                            reply.writeNoException();
                            return true;
                        case 215:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0178 = data.readInt();
                            boolean startProfile = startProfile(_arg0178);
                            reply.writeNoException();
                            reply.writeInt(startProfile ? 1 : 0);
                            return true;
                        case 216:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0179 = data.readInt();
                            boolean stopProfile = stopProfile(_arg0179);
                            reply.writeNoException();
                            reply.writeInt(stopProfile ? 1 : 0);
                            return true;
                        case 217:
                            data.enforceInterface(DESCRIPTOR);
                            IIntentSender _arg0180 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                            int _arg1124 = data.readInt();
                            ParceledListSlice _result61 = queryIntentComponentsForIntentSender(_arg0180, _arg1124);
                            reply.writeNoException();
                            if (_result61 != null) {
                                reply.writeInt(1);
                                _result61.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 218:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0181 = data.readInt();
                            String _arg1125 = data.readString();
                            int _result62 = getUidProcessCapabilities(_arg0181, _arg1125);
                            reply.writeNoException();
                            reply.writeInt(_result62);
                            return true;
                        case 219:
                            data.enforceInterface(DESCRIPTOR);
                            waitForBroadcastIdle();
                            reply.writeNoException();
                            return true;
                        case 220:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg0182 = data.readStrongBinder();
                            boolean isWindowMode = isWindowMode(_arg0182);
                            reply.writeNoException();
                            reply.writeInt(isWindowMode ? 1 : 0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IActivityManager {
            public static IActivityManager sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // android.app.IActivityManager
            public ParcelFileDescriptor openContentUri(String uriString) throws RemoteException {
                ParcelFileDescriptor _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uriString);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().openContentUri(uriString);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParcelFileDescriptor.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void registerUidObserver(IUidObserver observer, int which, int cutpoint, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(which);
                    _data.writeInt(cutpoint);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerUidObserver(observer, which, cutpoint, callingPackage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void unregisterUidObserver(IUidObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterUidObserver(observer);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean isUidActive(int uid, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(callingPackage);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isUidActive(uid, callingPackage);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int getUidProcessState(int uid, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getUidProcessState(uid, callingPackage);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int checkPermission(String permission, int pid, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permission);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().checkPermission(permission, pid, uid);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void handleApplicationCrash(IBinder app, ApplicationErrorReport.ParcelableCrashInfo crashInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(app);
                    if (crashInfo != null) {
                        _data.writeInt(1);
                        crashInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().handleApplicationCrash(app, crashInfo);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int startActivity(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options) throws RemoteException {
                Parcel _reply;
                IBinder asBinder;
                Parcel _reply2;
                Parcel _data = Parcel.obtain();
                Parcel _reply3 = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (caller != null) {
                        try {
                            asBinder = caller.asBinder();
                        } catch (Throwable th) {
                            th = th;
                            _reply = _reply3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } else {
                        asBinder = null;
                    }
                    _data.writeStrongBinder(asBinder);
                    _data.writeString(callingPackage);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeStrongBinder(resultTo);
                    _data.writeString(resultWho);
                    _data.writeInt(requestCode);
                    _data.writeInt(flags);
                    if (profilerInfo != null) {
                        _data.writeInt(1);
                        profilerInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(8, _data, _reply3, 0);
                    try {
                        if (_status) {
                            _reply2 = _reply3;
                        } else if (Stub.getDefaultImpl() != null) {
                            int startActivity = Stub.getDefaultImpl().startActivity(caller, callingPackage, intent, resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options);
                            _reply3.recycle();
                            _data.recycle();
                            return startActivity;
                        } else {
                            _reply2 = _reply3;
                        }
                        _reply2.readException();
                        int _result = _reply2.readInt();
                        _reply2.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    _reply = _reply3;
                }
            }

            @Override // android.app.IActivityManager
            public int startActivityWithFeature(IApplicationThread caller, String callingPackage, String callingFeatureId, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options) throws RemoteException {
                Parcel _reply;
                Parcel _data;
                IBinder asBinder;
                Parcel _reply2;
                Parcel _data2;
                Parcel _data3 = Parcel.obtain();
                Parcel _reply3 = Parcel.obtain();
                try {
                    _data3.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (caller != null) {
                        try {
                            asBinder = caller.asBinder();
                        } catch (Throwable th) {
                            th = th;
                            _reply = _reply3;
                            _data = _data3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } else {
                        asBinder = null;
                    }
                    _data3.writeStrongBinder(asBinder);
                    _data3.writeString(callingPackage);
                    _data3.writeString(callingFeatureId);
                    if (intent != null) {
                        _data3.writeInt(1);
                        intent.writeToParcel(_data3, 0);
                    } else {
                        _data3.writeInt(0);
                    }
                    _data3.writeString(resolvedType);
                    _data3.writeStrongBinder(resultTo);
                    _data3.writeString(resultWho);
                    _data3.writeInt(requestCode);
                    _data3.writeInt(flags);
                    if (profilerInfo != null) {
                        _data3.writeInt(1);
                        profilerInfo.writeToParcel(_data3, 0);
                    } else {
                        _data3.writeInt(0);
                    }
                    if (options != null) {
                        _data3.writeInt(1);
                        options.writeToParcel(_data3, 0);
                    } else {
                        _data3.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(9, _data3, _reply3, 0);
                    try {
                        if (_status) {
                            _reply2 = _reply3;
                            _data2 = _data3;
                        } else if (Stub.getDefaultImpl() != null) {
                            int startActivityWithFeature = Stub.getDefaultImpl().startActivityWithFeature(caller, callingPackage, callingFeatureId, intent, resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options);
                            _reply3.recycle();
                            _data3.recycle();
                            return startActivityWithFeature;
                        } else {
                            _reply2 = _reply3;
                            _data2 = _data3;
                        }
                        _reply2.readException();
                        int _result = _reply2.readInt();
                        _reply2.recycle();
                        _data2.recycle();
                        return _result;
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    _reply = _reply3;
                    _data = _data3;
                }
            }

            @Override // android.app.IActivityManager
            public void unhandledBack() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unhandledBack();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean finishActivity(IBinder token, int code, Intent data, int finishTask) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(code);
                    boolean _result = true;
                    if (data != null) {
                        _data.writeInt(1);
                        data.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(finishTask);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().finishActivity(token, code, data, finishTask);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public Intent registerReceiver(IApplicationThread caller, String callerPackage, IIntentReceiver receiver, IntentFilter filter, String requiredPermission, int userId, int flags) throws RemoteException {
                Intent _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBinder iBinder = null;
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    try {
                        _data.writeString(callerPackage);
                        if (receiver != null) {
                            iBinder = receiver.asBinder();
                        }
                        _data.writeStrongBinder(iBinder);
                        if (filter != null) {
                            _data.writeInt(1);
                            filter.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeString(requiredPermission);
                            try {
                                _data.writeInt(userId);
                                try {
                                    _data.writeInt(flags);
                                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                                    if (!_status && Stub.getDefaultImpl() != null) {
                                        Intent registerReceiver = Stub.getDefaultImpl().registerReceiver(caller, callerPackage, receiver, filter, requiredPermission, userId, flags);
                                        _reply.recycle();
                                        _data.recycle();
                                        return registerReceiver;
                                    }
                                    _reply.readException();
                                    if (_reply.readInt() != 0) {
                                        _result = Intent.CREATOR.mo3559createFromParcel(_reply);
                                    } else {
                                        _result = null;
                                    }
                                    _reply.recycle();
                                    _data.recycle();
                                    return _result;
                                } catch (Throwable th) {
                                    th = th;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                }
            }

            @Override // android.app.IActivityManager
            public Intent registerReceiverWithFeature(IApplicationThread caller, String callerPackage, String callingFeatureId, String receiverId, IIntentReceiver receiver, IntentFilter filter, String requiredPermission, int userId, int flags) throws RemoteException {
                IBinder iBinder;
                Intent _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    iBinder = null;
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    try {
                        _data.writeString(callerPackage);
                    } catch (Throwable th) {
                        th = th;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
                try {
                    _data.writeString(callingFeatureId);
                    _data.writeString(receiverId);
                    if (receiver != null) {
                        iBinder = receiver.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    if (filter != null) {
                        _data.writeInt(1);
                        filter.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(requiredPermission);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Intent registerReceiverWithFeature = Stub.getDefaultImpl().registerReceiverWithFeature(caller, callerPackage, callingFeatureId, receiverId, receiver, filter, requiredPermission, userId, flags);
                        _reply.recycle();
                        _data.recycle();
                        return registerReceiverWithFeature;
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Intent.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityManager
            public void unregisterReceiver(IIntentReceiver receiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterReceiver(receiver);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int broadcastIntent(IApplicationThread caller, Intent intent, String resolvedType, IIntentReceiver resultTo, int resultCode, String resultData, Bundle map, String[] requiredPermissions, int appOp, Bundle options, boolean serialized, boolean sticky, int userId) throws RemoteException {
                Parcel _reply;
                Parcel _data;
                IBinder asBinder;
                Parcel _reply2;
                Parcel _data2;
                Parcel _data3 = Parcel.obtain();
                Parcel _reply3 = Parcel.obtain();
                try {
                    _data3.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBinder iBinder = null;
                    if (caller != null) {
                        try {
                            asBinder = caller.asBinder();
                        } catch (Throwable th) {
                            th = th;
                            _reply = _reply3;
                            _data = _data3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } else {
                        asBinder = null;
                    }
                    _data3.writeStrongBinder(asBinder);
                    int i = 1;
                    if (intent != null) {
                        _data3.writeInt(1);
                        intent.writeToParcel(_data3, 0);
                    } else {
                        _data3.writeInt(0);
                    }
                    _data3.writeString(resolvedType);
                    if (resultTo != null) {
                        iBinder = resultTo.asBinder();
                    }
                    _data3.writeStrongBinder(iBinder);
                    _data3.writeInt(resultCode);
                    _data3.writeString(resultData);
                    if (map != null) {
                        _data3.writeInt(1);
                        map.writeToParcel(_data3, 0);
                    } else {
                        _data3.writeInt(0);
                    }
                    _data3.writeStringArray(requiredPermissions);
                    _data3.writeInt(appOp);
                    if (options != null) {
                        _data3.writeInt(1);
                        options.writeToParcel(_data3, 0);
                    } else {
                        _data3.writeInt(0);
                    }
                    _data3.writeInt(serialized ? 1 : 0);
                    if (!sticky) {
                        i = 0;
                    }
                    _data3.writeInt(i);
                    _data3.writeInt(userId);
                    boolean _status = this.mRemote.transact(15, _data3, _reply3, 0);
                    try {
                        if (_status) {
                            _reply2 = _reply3;
                            _data2 = _data3;
                        } else if (Stub.getDefaultImpl() != null) {
                            int broadcastIntent = Stub.getDefaultImpl().broadcastIntent(caller, intent, resolvedType, resultTo, resultCode, resultData, map, requiredPermissions, appOp, options, serialized, sticky, userId);
                            _reply3.recycle();
                            _data3.recycle();
                            return broadcastIntent;
                        } else {
                            _reply2 = _reply3;
                            _data2 = _data3;
                        }
                        _reply2.readException();
                        int _result = _reply2.readInt();
                        _reply2.recycle();
                        _data2.recycle();
                        return _result;
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    _reply = _reply3;
                    _data = _data3;
                }
            }

            @Override // android.app.IActivityManager
            public int broadcastIntentWithFeature(IApplicationThread caller, String callingFeatureId, Intent intent, String resolvedType, IIntentReceiver resultTo, int resultCode, String resultData, Bundle map, String[] requiredPermissions, String[] excludePermissions, String[] excludePackages, int appOp, Bundle options, boolean serialized, boolean sticky, int userId) throws RemoteException {
                Parcel _reply;
                Parcel _data;
                IBinder asBinder;
                Parcel _reply2;
                Parcel _data2;
                Parcel _data3 = Parcel.obtain();
                Parcel _reply3 = Parcel.obtain();
                try {
                    _data3.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBinder iBinder = null;
                    if (caller != null) {
                        try {
                            asBinder = caller.asBinder();
                        } catch (Throwable th) {
                            th = th;
                            _reply = _reply3;
                            _data = _data3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } else {
                        asBinder = null;
                    }
                    _data3.writeStrongBinder(asBinder);
                    _data3.writeString(callingFeatureId);
                    int i = 1;
                    if (intent != null) {
                        _data3.writeInt(1);
                        intent.writeToParcel(_data3, 0);
                    } else {
                        _data3.writeInt(0);
                    }
                    _data3.writeString(resolvedType);
                    if (resultTo != null) {
                        iBinder = resultTo.asBinder();
                    }
                    _data3.writeStrongBinder(iBinder);
                    _data3.writeInt(resultCode);
                    _data3.writeString(resultData);
                    if (map != null) {
                        _data3.writeInt(1);
                        map.writeToParcel(_data3, 0);
                    } else {
                        _data3.writeInt(0);
                    }
                    _data3.writeStringArray(requiredPermissions);
                    _data3.writeStringArray(excludePermissions);
                    _data3.writeStringArray(excludePackages);
                    _data3.writeInt(appOp);
                    if (options != null) {
                        _data3.writeInt(1);
                        options.writeToParcel(_data3, 0);
                    } else {
                        _data3.writeInt(0);
                    }
                    _data3.writeInt(serialized ? 1 : 0);
                    if (!sticky) {
                        i = 0;
                    }
                    _data3.writeInt(i);
                    _data3.writeInt(userId);
                    boolean _status = this.mRemote.transact(16, _data3, _reply3, 0);
                    try {
                        if (_status) {
                            _reply2 = _reply3;
                            _data2 = _data3;
                        } else if (Stub.getDefaultImpl() != null) {
                            int broadcastIntentWithFeature = Stub.getDefaultImpl().broadcastIntentWithFeature(caller, callingFeatureId, intent, resolvedType, resultTo, resultCode, resultData, map, requiredPermissions, excludePermissions, excludePackages, appOp, options, serialized, sticky, userId);
                            _reply3.recycle();
                            _data3.recycle();
                            return broadcastIntentWithFeature;
                        } else {
                            _reply2 = _reply3;
                            _data2 = _data3;
                        }
                        _reply2.readException();
                        int _result = _reply2.readInt();
                        _reply2.recycle();
                        _data2.recycle();
                        return _result;
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    _reply = _reply3;
                    _data = _data3;
                }
            }

            @Override // android.app.IActivityManager
            public void unbroadcastIntent(IApplicationThread caller, Intent intent, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unbroadcastIntent(caller, intent, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void finishReceiver(IBinder who, int resultCode, String resultData, Bundle map, boolean abortBroadcast, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeStrongBinder(who);
                        try {
                            _data.writeInt(resultCode);
                            try {
                                _data.writeString(resultData);
                                int i = 0;
                                if (map != null) {
                                    _data.writeInt(1);
                                    map.writeToParcel(_data, 0);
                                } else {
                                    _data.writeInt(0);
                                }
                                if (abortBroadcast) {
                                    i = 1;
                                }
                                _data.writeInt(i);
                                try {
                                    _data.writeInt(flags);
                                    try {
                                        boolean _status = this.mRemote.transact(18, _data, null, 1);
                                        if (!_status && Stub.getDefaultImpl() != null) {
                                            Stub.getDefaultImpl().finishReceiver(who, resultCode, resultData, map, abortBroadcast, flags);
                                            _data.recycle();
                                            return;
                                        }
                                        _data.recycle();
                                    } catch (Throwable th) {
                                        th = th;
                                        _data.recycle();
                                        throw th;
                                    }
                                } catch (Throwable th2) {
                                    th = th2;
                                }
                            } catch (Throwable th3) {
                                th = th3;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                }
            }

            @Override // android.app.IActivityManager
            public void attachApplication(IApplicationThread app, long startSeq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(app != null ? app.asBinder() : null);
                    _data.writeLong(startSeq);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().attachApplication(app, startSeq);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public List<ActivityManager.RunningTaskInfo> getTasks(int maxNum) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(maxNum);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getTasks(maxNum);
                    }
                    _reply.readException();
                    List<ActivityManager.RunningTaskInfo> _result = _reply.createTypedArrayList(ActivityManager.RunningTaskInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void moveTaskToFront(IApplicationThread caller, String callingPackage, int task, int flags, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(callingPackage);
                    _data.writeInt(task);
                    _data.writeInt(flags);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().moveTaskToFront(caller, callingPackage, task, flags, options);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int getTaskForActivity(IBinder token, boolean onlyRoot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(onlyRoot ? 1 : 0);
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getTaskForActivity(token, onlyRoot);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public ContentProviderHolder getContentProvider(IApplicationThread caller, String callingPackage, String name, int userId, boolean stable) throws RemoteException {
                ContentProviderHolder _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(callingPackage);
                    _data.writeString(name);
                    _data.writeInt(userId);
                    _data.writeInt(stable ? 1 : 0);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getContentProvider(caller, callingPackage, name, userId, stable);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ContentProviderHolder.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void publishContentProviders(IApplicationThread caller, List<ContentProviderHolder> providers) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeTypedList(providers);
                    boolean _status = this.mRemote.transact(24, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().publishContentProviders(caller, providers);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean refContentProvider(IBinder connection, int stableDelta, int unstableDelta) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(connection);
                    _data.writeInt(stableDelta);
                    _data.writeInt(unstableDelta);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(25, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().refContentProvider(connection, stableDelta, unstableDelta);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public PendingIntent getRunningServiceControlPanel(ComponentName service) throws RemoteException {
                PendingIntent _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(26, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRunningServiceControlPanel(service);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = PendingIntent.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public ComponentName startService(IApplicationThread caller, Intent service, String resolvedType, boolean requireForeground, String callingPackage, String callingFeatureId, int userId) throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    int i = 1;
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeString(resolvedType);
                        if (!requireForeground) {
                            i = 0;
                        }
                        _data.writeInt(i);
                        try {
                            _data.writeString(callingPackage);
                        } catch (Throwable th) {
                            th = th;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
                try {
                    _data.writeString(callingFeatureId);
                    try {
                        _data.writeInt(userId);
                        boolean _status = this.mRemote.transact(27, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            ComponentName startService = Stub.getDefaultImpl().startService(caller, service, resolvedType, requireForeground, callingPackage, callingFeatureId, userId);
                            _reply.recycle();
                            _data.recycle();
                            return startService;
                        }
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            _result = ComponentName.CREATOR.mo3559createFromParcel(_reply);
                        } else {
                            _result = null;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityManager
            public int stopService(IApplicationThread caller, Intent service, String resolvedType, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(28, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().stopService(caller, service, resolvedType, userId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int bindService(IApplicationThread caller, IBinder token, Intent service, String resolvedType, IServiceConnection connection, int flags, String callingPackage, int userId) throws RemoteException {
                IBinder iBinder;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    iBinder = null;
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    try {
                        _data.writeStrongBinder(token);
                        if (service != null) {
                            _data.writeInt(1);
                            service.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                    } catch (Throwable th) {
                        th = th;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
                try {
                    _data.writeString(resolvedType);
                    if (connection != null) {
                        iBinder = connection.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    try {
                        _data.writeInt(flags);
                        _data.writeString(callingPackage);
                        _data.writeInt(userId);
                        boolean _status = this.mRemote.transact(29, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            int bindService = Stub.getDefaultImpl().bindService(caller, token, service, resolvedType, connection, flags, callingPackage, userId);
                            _reply.recycle();
                            _data.recycle();
                            return bindService;
                        }
                        _reply.readException();
                        int _result = _reply.readInt();
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityManager
            public int bindIsolatedService(IApplicationThread caller, IBinder token, Intent service, String resolvedType, IServiceConnection connection, int flags, String instanceName, String callingPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBinder iBinder = null;
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    try {
                        _data.writeStrongBinder(token);
                        if (service != null) {
                            _data.writeInt(1);
                            service.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeString(resolvedType);
                            if (connection != null) {
                                iBinder = connection.asBinder();
                            }
                            _data.writeStrongBinder(iBinder);
                            _data.writeInt(flags);
                            _data.writeString(instanceName);
                            _data.writeString(callingPackage);
                            _data.writeInt(userId);
                            boolean _status = this.mRemote.transact(30, _data, _reply, 0);
                            if (!_status && Stub.getDefaultImpl() != null) {
                                int bindIsolatedService = Stub.getDefaultImpl().bindIsolatedService(caller, token, service, resolvedType, connection, flags, instanceName, callingPackage, userId);
                                _reply.recycle();
                                _data.recycle();
                                return bindIsolatedService;
                            }
                            _reply.readException();
                            int _result = _reply.readInt();
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        } catch (Throwable th) {
                            th = th;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
            }

            @Override // android.app.IActivityManager
            public void updateServiceGroup(IServiceConnection connection, int group, int importance) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(connection != null ? connection.asBinder() : null);
                    _data.writeInt(group);
                    _data.writeInt(importance);
                    boolean _status = this.mRemote.transact(31, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateServiceGroup(connection, group, importance);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean unbindService(IServiceConnection connection) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(connection != null ? connection.asBinder() : null);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(32, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().unbindService(connection);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void publishService(IBinder token, Intent intent, IBinder service) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(service);
                    boolean _status = this.mRemote.transact(33, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().publishService(token, intent, service);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setDebugApp(String packageName, boolean waitForDebugger, boolean persistent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    int i = 1;
                    _data.writeInt(waitForDebugger ? 1 : 0);
                    if (!persistent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(34, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDebugApp(packageName, waitForDebugger, persistent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setAgentApp(String packageName, String agent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(agent);
                    boolean _status = this.mRemote.transact(35, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setAgentApp(packageName, agent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setAlwaysFinish(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    boolean _status = this.mRemote.transact(36, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setAlwaysFinish(enabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean startInstrumentation(ComponentName className, String profileFile, int flags, Bundle arguments, IInstrumentationWatcher watcher, IUiAutomationConnection connection, int userId, String abiOverride) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (className != null) {
                        _data.writeInt(1);
                        className.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeString(profileFile);
                        try {
                            _data.writeInt(flags);
                            if (arguments != null) {
                                _data.writeInt(1);
                                arguments.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            IBinder iBinder = null;
                            _data.writeStrongBinder(watcher != null ? watcher.asBinder() : null);
                            if (connection != null) {
                                iBinder = connection.asBinder();
                            }
                            _data.writeStrongBinder(iBinder);
                            _data.writeInt(userId);
                            _data.writeString(abiOverride);
                            boolean _status = this.mRemote.transact(37, _data, _reply, 0);
                            if (!_status && Stub.getDefaultImpl() != null) {
                                boolean startInstrumentation = Stub.getDefaultImpl().startInstrumentation(className, profileFile, flags, arguments, watcher, connection, userId, abiOverride);
                                _reply.recycle();
                                _data.recycle();
                                return startInstrumentation;
                            }
                            _reply.readException();
                            if (_reply.readInt() == 0) {
                                _result = false;
                            }
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        } catch (Throwable th) {
                            th = th;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
            }

            @Override // android.app.IActivityManager
            public void addInstrumentationResults(IApplicationThread target, Bundle results) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(target != null ? target.asBinder() : null);
                    if (results != null) {
                        _data.writeInt(1);
                        results.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(38, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addInstrumentationResults(target, results);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void finishInstrumentation(IApplicationThread target, int resultCode, Bundle results) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(target != null ? target.asBinder() : null);
                    _data.writeInt(resultCode);
                    if (results != null) {
                        _data.writeInt(1);
                        results.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(39, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().finishInstrumentation(target, resultCode, results);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public Configuration getConfiguration() throws RemoteException {
                Configuration _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(40, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getConfiguration();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Configuration.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean updateConfiguration(Configuration values) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (values != null) {
                        _data.writeInt(1);
                        values.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(41, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().updateConfiguration(values);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean updateMccMncConfiguration(String mcc, String mnc) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(mcc);
                    _data.writeString(mnc);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(42, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().updateMccMncConfiguration(mcc, mnc);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean stopServiceToken(ComponentName className, IBinder token, int startId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (className != null) {
                        _data.writeInt(1);
                        className.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(token);
                    _data.writeInt(startId);
                    boolean _status = this.mRemote.transact(43, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().stopServiceToken(className, token, startId);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setProcessLimit(int max) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(max);
                    boolean _status = this.mRemote.transact(44, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setProcessLimit(max);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int getProcessLimit() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(45, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getProcessLimit();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int checkUriPermission(Uri uri, int pid, int uid, int mode, int userId, IBinder callerToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeInt(pid);
                        try {
                            _data.writeInt(uid);
                            try {
                                _data.writeInt(mode);
                                try {
                                    _data.writeInt(userId);
                                    try {
                                        _data.writeStrongBinder(callerToken);
                                        boolean _status = this.mRemote.transact(46, _data, _reply, 0);
                                        if (!_status && Stub.getDefaultImpl() != null) {
                                            int checkUriPermission = Stub.getDefaultImpl().checkUriPermission(uri, pid, uid, mode, userId, callerToken);
                                            _reply.recycle();
                                            _data.recycle();
                                            return checkUriPermission;
                                        }
                                        _reply.readException();
                                        int _result = _reply.readInt();
                                        _reply.recycle();
                                        _data.recycle();
                                        return _result;
                                    } catch (Throwable th) {
                                        th = th;
                                        _reply.recycle();
                                        _data.recycle();
                                        throw th;
                                    }
                                } catch (Throwable th2) {
                                    th = th2;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th3) {
                                th = th3;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                }
            }

            @Override // android.app.IActivityManager
            public int[] checkUriPermissions(List<Uri> uris, int pid, int uid, int mode, IBinder callerToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(uris);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeInt(mode);
                    _data.writeStrongBinder(callerToken);
                    boolean _status = this.mRemote.transact(47, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().checkUriPermissions(uris, pid, uid, mode, callerToken);
                    }
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void grantUriPermission(IApplicationThread caller, String targetPkg, Uri uri, int mode, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(targetPkg);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(mode);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(48, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().grantUriPermission(caller, targetPkg, uri, mode, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void revokeUriPermission(IApplicationThread caller, String targetPkg, Uri uri, int mode, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(targetPkg);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(mode);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(49, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().revokeUriPermission(caller, targetPkg, uri, mode, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setActivityController(IActivityController watcher, boolean imAMonkey) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(watcher != null ? watcher.asBinder() : null);
                    _data.writeInt(imAMonkey ? 1 : 0);
                    boolean _status = this.mRemote.transact(50, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setActivityController(watcher, imAMonkey);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void showWaitingForDebugger(IApplicationThread who, boolean waiting) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(who != null ? who.asBinder() : null);
                    _data.writeInt(waiting ? 1 : 0);
                    boolean _status = this.mRemote.transact(51, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().showWaitingForDebugger(who, waiting);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void signalPersistentProcesses(int signal) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(signal);
                    boolean _status = this.mRemote.transact(52, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().signalPersistentProcesses(signal);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public ParceledListSlice getRecentTasks(int maxNum, int flags, int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(maxNum);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(53, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRecentTasks(maxNum, flags, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void serviceDoneExecuting(IBinder token, int type, int startId, int res) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(type);
                    _data.writeInt(startId);
                    _data.writeInt(res);
                    boolean _status = this.mRemote.transact(54, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().serviceDoneExecuting(token, type, startId, res);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public IIntentSender getIntentSender(int type, String packageName, IBinder token, String resultWho, int requestCode, Intent[] intents, String[] resolvedTypes, int flags, Bundle options, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(type);
                        _data.writeString(packageName);
                        _data.writeStrongBinder(token);
                        _data.writeString(resultWho);
                        _data.writeInt(requestCode);
                        _data.writeTypedArray(intents, 0);
                        _data.writeStringArray(resolvedTypes);
                        _data.writeInt(flags);
                        if (options != null) {
                            _data.writeInt(1);
                            options.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        _data.writeInt(userId);
                        boolean _status = this.mRemote.transact(55, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            IIntentSender intentSender = Stub.getDefaultImpl().getIntentSender(type, packageName, token, resultWho, requestCode, intents, resolvedTypes, flags, options, userId);
                            _reply.recycle();
                            _data.recycle();
                            return intentSender;
                        }
                        _reply.readException();
                        IIntentSender _result = IIntentSender.Stub.asInterface(_reply.readStrongBinder());
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th) {
                        th = th;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            }

            @Override // android.app.IActivityManager
            public IIntentSender getIntentSenderWithFeature(int type, String packageName, String featureId, IBinder token, String resultWho, int requestCode, Intent[] intents, String[] resolvedTypes, int flags, Bundle options, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(packageName);
                    _data.writeString(featureId);
                    _data.writeStrongBinder(token);
                    _data.writeString(resultWho);
                    _data.writeInt(requestCode);
                    _data.writeTypedArray(intents, 0);
                    _data.writeStringArray(resolvedTypes);
                    _data.writeInt(flags);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(56, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getIntentSenderWithFeature(type, packageName, featureId, token, resultWho, requestCode, intents, resolvedTypes, flags, options, userId);
                    }
                    _reply.readException();
                    IIntentSender _result = IIntentSender.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void cancelIntentSender(IIntentSender sender) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    boolean _status = this.mRemote.transact(57, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cancelIntentSender(sender);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public ActivityManager.PendingIntentInfo getInfoForIntentSender(IIntentSender sender) throws RemoteException {
                ActivityManager.PendingIntentInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    boolean _status = this.mRemote.transact(58, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getInfoForIntentSender(sender);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ActivityManager.PendingIntentInfo.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void registerIntentSenderCancelListener(IIntentSender sender, IResultReceiver receiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBinder iBinder = null;
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    if (receiver != null) {
                        iBinder = receiver.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    boolean _status = this.mRemote.transact(59, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerIntentSenderCancelListener(sender, receiver);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void unregisterIntentSenderCancelListener(IIntentSender sender, IResultReceiver receiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBinder iBinder = null;
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    if (receiver != null) {
                        iBinder = receiver.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    boolean _status = this.mRemote.transact(60, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterIntentSenderCancelListener(sender, receiver);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void enterSafeMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(61, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().enterSafeMode();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void noteWakeupAlarm(IIntentSender sender, WorkSource workSource, int sourceUid, String sourcePkg, String tag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    if (workSource != null) {
                        _data.writeInt(1);
                        workSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(sourceUid);
                    _data.writeString(sourcePkg);
                    _data.writeString(tag);
                    boolean _status = this.mRemote.transact(62, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteWakeupAlarm(sender, workSource, sourceUid, sourcePkg, tag);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void removeContentProvider(IBinder connection, boolean stable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(connection);
                    _data.writeInt(stable ? 1 : 0);
                    boolean _status = this.mRemote.transact(63, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeContentProvider(connection, stable);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setRequestedOrientation(IBinder token, int requestedOrientation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(requestedOrientation);
                    boolean _status = this.mRemote.transact(64, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setRequestedOrientation(token, requestedOrientation);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void unbindFinished(IBinder token, Intent service, boolean doRebind) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    int i = 1;
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!doRebind) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(65, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unbindFinished(token, service, doRebind);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setProcessImportant(IBinder token, int pid, boolean isForeground, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(pid);
                    _data.writeInt(isForeground ? 1 : 0);
                    _data.writeString(reason);
                    boolean _status = this.mRemote.transact(66, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setProcessImportant(token, pid, isForeground, reason);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setServiceForeground(ComponentName className, IBinder token, int id, Notification notification, int flags, int foregroundServiceType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (className != null) {
                        _data.writeInt(1);
                        className.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeStrongBinder(token);
                        try {
                            _data.writeInt(id);
                            if (notification != null) {
                                _data.writeInt(1);
                                notification.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            try {
                                _data.writeInt(flags);
                                try {
                                    _data.writeInt(foregroundServiceType);
                                    boolean _status = this.mRemote.transact(67, _data, _reply, 0);
                                    if (!_status && Stub.getDefaultImpl() != null) {
                                        Stub.getDefaultImpl().setServiceForeground(className, token, id, notification, flags, foregroundServiceType);
                                        _reply.recycle();
                                        _data.recycle();
                                        return;
                                    }
                                    _reply.readException();
                                    _reply.recycle();
                                    _data.recycle();
                                } catch (Throwable th) {
                                    th = th;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                }
            }

            @Override // android.app.IActivityManager
            public int getForegroundServiceType(ComponentName className, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (className != null) {
                        _data.writeInt(1);
                        className.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(68, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getForegroundServiceType(className, token);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean moveActivityTaskToBack(IBinder token, boolean nonRoot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _result = true;
                    _data.writeInt(nonRoot ? 1 : 0);
                    boolean _status = this.mRemote.transact(69, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().moveActivityTaskToBack(token, nonRoot);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void getMemoryInfo(ActivityManager.MemoryInfo outInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(70, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().getMemoryInfo(outInfo);
                        return;
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        outInfo.readFromParcel(_reply);
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public List<ActivityManager.ProcessErrorStateInfo> getProcessesInErrorState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(71, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getProcessesInErrorState();
                    }
                    _reply.readException();
                    List<ActivityManager.ProcessErrorStateInfo> _result = _reply.createTypedArrayList(ActivityManager.ProcessErrorStateInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean clearApplicationUserData(String packageName, boolean keepState, IPackageDataObserver observer, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _result = true;
                    _data.writeInt(keepState ? 1 : 0);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(72, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().clearApplicationUserData(packageName, keepState, observer, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void forceStopPackage(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(73, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().forceStopPackage(packageName, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean killPids(int[] pids, String reason, boolean secure) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(pids);
                    _data.writeString(reason);
                    boolean _result = true;
                    _data.writeInt(secure ? 1 : 0);
                    boolean _status = this.mRemote.transact(74, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().killPids(pids, reason, secure);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public List<ActivityManager.RunningServiceInfo> getServices(int maxNum, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(maxNum);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(75, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getServices(maxNum, flags);
                    }
                    _reply.readException();
                    List<ActivityManager.RunningServiceInfo> _result = _reply.createTypedArrayList(ActivityManager.RunningServiceInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(76, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRunningAppProcesses();
                    }
                    _reply.readException();
                    List<ActivityManager.RunningAppProcessInfo> _result = _reply.createTypedArrayList(ActivityManager.RunningAppProcessInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public IBinder peekService(Intent service, String resolvedType, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(77, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().peekService(service, resolvedType, callingPackage);
                    }
                    _reply.readException();
                    IBinder _result = _reply.readStrongBinder();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean profileControl(String process, int userId, boolean start, ProfilerInfo profilerInfo, int profileType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(process);
                        try {
                            _data.writeInt(userId);
                            boolean _result = true;
                            _data.writeInt(start ? 1 : 0);
                            if (profilerInfo != null) {
                                _data.writeInt(1);
                                profilerInfo.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            try {
                                _data.writeInt(profileType);
                                try {
                                    boolean _status = this.mRemote.transact(78, _data, _reply, 0);
                                    if (!_status && Stub.getDefaultImpl() != null) {
                                        boolean profileControl = Stub.getDefaultImpl().profileControl(process, userId, start, profilerInfo, profileType);
                                        _reply.recycle();
                                        _data.recycle();
                                        return profileControl;
                                    }
                                    _reply.readException();
                                    if (_reply.readInt() == 0) {
                                        _result = false;
                                    }
                                    _reply.recycle();
                                    _data.recycle();
                                    return _result;
                                } catch (Throwable th) {
                                    th = th;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                }
            }

            @Override // android.app.IActivityManager
            public boolean shutdown(int timeout) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(timeout);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(79, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().shutdown(timeout);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void stopAppSwitches() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(80, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().stopAppSwitches();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void resumeAppSwitches() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(81, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().resumeAppSwitches();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean bindBackupAgent(String packageName, int backupRestoreMode, int targetUserId, int operationType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(backupRestoreMode);
                    _data.writeInt(targetUserId);
                    _data.writeInt(operationType);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(82, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().bindBackupAgent(packageName, backupRestoreMode, targetUserId, operationType);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void backupAgentCreated(String packageName, IBinder agent, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(agent);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(83, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().backupAgentCreated(packageName, agent, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void unbindBackupAgent(ApplicationInfo appInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (appInfo != null) {
                        _data.writeInt(1);
                        appInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(84, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unbindBackupAgent(appInfo);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int handleIncomingUser(int callingPid, int callingUid, int userId, boolean allowAll, boolean requireFull, String name, String callerPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(callingPid);
                    try {
                        _data.writeInt(callingUid);
                        try {
                            _data.writeInt(userId);
                            int i = 1;
                            _data.writeInt(allowAll ? 1 : 0);
                            if (!requireFull) {
                                i = 0;
                            }
                            _data.writeInt(i);
                            try {
                                _data.writeString(name);
                                try {
                                    _data.writeString(callerPackage);
                                    boolean _status = this.mRemote.transact(85, _data, _reply, 0);
                                    if (!_status && Stub.getDefaultImpl() != null) {
                                        int handleIncomingUser = Stub.getDefaultImpl().handleIncomingUser(callingPid, callingUid, userId, allowAll, requireFull, name, callerPackage);
                                        _reply.recycle();
                                        _data.recycle();
                                        return handleIncomingUser;
                                    }
                                    _reply.readException();
                                    int _result = _reply.readInt();
                                    _reply.recycle();
                                    _data.recycle();
                                    return _result;
                                } catch (Throwable th2) {
                                    th = th2;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th3) {
                                th = th3;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityManager
            public void addPackageDependency(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(86, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addPackageDependency(packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void killApplication(String pkg, int appId, int userId, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(appId);
                    _data.writeInt(userId);
                    _data.writeString(reason);
                    boolean _status = this.mRemote.transact(87, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().killApplication(pkg, appId, userId, reason);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void closeSystemDialogs(String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(reason);
                    boolean _status = this.mRemote.transact(88, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().closeSystemDialogs(reason);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public Debug.MemoryInfo[] getProcessMemoryInfo(int[] pids) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(pids);
                    boolean _status = this.mRemote.transact(89, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getProcessMemoryInfo(pids);
                    }
                    _reply.readException();
                    Debug.MemoryInfo[] _result = (Debug.MemoryInfo[]) _reply.createTypedArray(Debug.MemoryInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void killApplicationProcess(String processName, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(processName);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(90, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().killApplicationProcess(processName, uid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean handleApplicationWtf(IBinder app, String tag, boolean system, ApplicationErrorReport.ParcelableCrashInfo crashInfo, int immediateCallerPid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeStrongBinder(app);
                        try {
                            _data.writeString(tag);
                            boolean _result = true;
                            _data.writeInt(system ? 1 : 0);
                            if (crashInfo != null) {
                                _data.writeInt(1);
                                crashInfo.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            try {
                                _data.writeInt(immediateCallerPid);
                                try {
                                    boolean _status = this.mRemote.transact(91, _data, _reply, 0);
                                    if (!_status && Stub.getDefaultImpl() != null) {
                                        boolean handleApplicationWtf = Stub.getDefaultImpl().handleApplicationWtf(app, tag, system, crashInfo, immediateCallerPid);
                                        _reply.recycle();
                                        _data.recycle();
                                        return handleApplicationWtf;
                                    }
                                    _reply.readException();
                                    if (_reply.readInt() == 0) {
                                        _result = false;
                                    }
                                    _reply.recycle();
                                    _data.recycle();
                                    return _result;
                                } catch (Throwable th) {
                                    th = th;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                }
            }

            @Override // android.app.IActivityManager
            public void killBackgroundProcesses(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(92, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().killBackgroundProcesses(packageName, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean isUserAMonkey() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(93, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isUserAMonkey();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public List<ApplicationInfo> getRunningExternalApplications() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(94, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRunningExternalApplications();
                    }
                    _reply.readException();
                    List<ApplicationInfo> _result = _reply.createTypedArrayList(ApplicationInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void finishHeavyWeightApp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(95, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().finishHeavyWeightApp();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void handleApplicationStrictModeViolation(IBinder app, int penaltyMask, StrictMode.ViolationInfo crashInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(app);
                    _data.writeInt(penaltyMask);
                    if (crashInfo != null) {
                        _data.writeInt(1);
                        crashInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(96, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().handleApplicationStrictModeViolation(app, penaltyMask, crashInfo);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean isTopActivityImmersive() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(97, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isTopActivityImmersive();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void crashApplicationWithType(int uid, int initialPid, String packageName, int userId, String message, boolean force, int exceptionTypeId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(uid);
                        try {
                            _data.writeInt(initialPid);
                            try {
                                _data.writeString(packageName);
                            } catch (Throwable th) {
                                th = th;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                }
                try {
                    _data.writeInt(userId);
                    try {
                        _data.writeString(message);
                        _data.writeInt(force ? 1 : 0);
                        _data.writeInt(exceptionTypeId);
                        boolean _status = this.mRemote.transact(98, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            Stub.getDefaultImpl().crashApplicationWithType(uid, initialPid, packageName, userId, message, force, exceptionTypeId);
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityManager
            public void crashApplicationWithTypeWithExtras(int uid, int initialPid, String packageName, int userId, String message, boolean force, int exceptionTypeId, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(uid);
                        try {
                            _data.writeInt(initialPid);
                        } catch (Throwable th) {
                            th = th;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
                try {
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeString(message);
                    _data.writeInt(force ? 1 : 0);
                    _data.writeInt(exceptionTypeId);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(99, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().crashApplicationWithTypeWithExtras(uid, initialPid, packageName, userId, message, force, exceptionTypeId, extras);
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    _reply.readException();
                    _reply.recycle();
                    _data.recycle();
                } catch (Throwable th4) {
                    th = th4;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityManager
            public String getProviderMimeType(Uri uri, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(100, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getProviderMimeType(uri, userId);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void getProviderMimeTypeAsync(Uri uri, int userId, RemoteCallback resultCallback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (resultCallback != null) {
                        _data.writeInt(1);
                        resultCallback.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(101, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().getProviderMimeTypeAsync(uri, userId, resultCallback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean dumpHeap(String process, int userId, boolean managed, boolean mallocInfo, boolean runGc, String path, ParcelFileDescriptor fd, RemoteCallback finishCallback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(process);
                        try {
                            _data.writeInt(userId);
                            boolean _result = true;
                            _data.writeInt(managed ? 1 : 0);
                            _data.writeInt(mallocInfo ? 1 : 0);
                            _data.writeInt(runGc ? 1 : 0);
                            _data.writeString(path);
                            if (fd != null) {
                                _data.writeInt(1);
                                fd.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (finishCallback != null) {
                                _data.writeInt(1);
                                finishCallback.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            boolean _status = this.mRemote.transact(102, _data, _reply, 0);
                            if (!_status && Stub.getDefaultImpl() != null) {
                                boolean dumpHeap = Stub.getDefaultImpl().dumpHeap(process, userId, managed, mallocInfo, runGc, path, fd, finishCallback);
                                _reply.recycle();
                                _data.recycle();
                                return dumpHeap;
                            }
                            _reply.readException();
                            if (_reply.readInt() == 0) {
                                _result = false;
                            }
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        } catch (Throwable th) {
                            th = th;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
            }

            @Override // android.app.IActivityManager
            public boolean isUserRunning(int userid, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    _data.writeInt(flags);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(103, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isUserRunning(userid, flags);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setPackageScreenCompatMode(String packageName, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(104, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPackageScreenCompatMode(packageName, mode);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean switchUser(int userid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(105, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().switchUser(userid);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean removeTask(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(106, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().removeTask(taskId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void registerProcessObserver(IProcessObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    boolean _status = this.mRemote.transact(107, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerProcessObserver(observer);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void unregisterProcessObserver(IProcessObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    boolean _status = this.mRemote.transact(108, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterProcessObserver(observer);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean isIntentSenderTargetedToPackage(IIntentSender sender) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(109, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isIntentSenderTargetedToPackage(sender);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void updatePersistentConfiguration(Configuration values) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (values != null) {
                        _data.writeInt(1);
                        values.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(110, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updatePersistentConfiguration(values);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void updatePersistentConfigurationWithAttribution(Configuration values, String callingPackageName, String callingAttributionTag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (values != null) {
                        _data.writeInt(1);
                        values.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackageName);
                    _data.writeString(callingAttributionTag);
                    boolean _status = this.mRemote.transact(111, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updatePersistentConfigurationWithAttribution(values, callingPackageName, callingAttributionTag);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public long[] getProcessPss(int[] pids) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(pids);
                    boolean _status = this.mRemote.transact(112, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getProcessPss(pids);
                    }
                    _reply.readException();
                    long[] _result = _reply.createLongArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void showBootMessage(CharSequence msg, boolean always) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (msg != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(msg, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!always) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(113, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().showBootMessage(msg, always);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void killAllBackgroundProcesses() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(114, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().killAllBackgroundProcesses();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public ContentProviderHolder getContentProviderExternal(String name, int userId, IBinder token, String tag) throws RemoteException {
                ContentProviderHolder _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(userId);
                    _data.writeStrongBinder(token);
                    _data.writeString(tag);
                    boolean _status = this.mRemote.transact(115, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getContentProviderExternal(name, userId, token, tag);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ContentProviderHolder.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void removeContentProviderExternal(String name, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(116, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeContentProviderExternal(name, token);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void removeContentProviderExternalAsUser(String name, IBinder token, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeStrongBinder(token);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(117, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeContentProviderExternalAsUser(name, token, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void getMyMemoryState(ActivityManager.RunningAppProcessInfo outInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(118, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().getMyMemoryState(outInfo);
                        return;
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        outInfo.readFromParcel(_reply);
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean killProcessesBelowForeground(String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(reason);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(119, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().killProcessesBelowForeground(reason);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public UserInfo getCurrentUser() throws RemoteException {
                UserInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(120, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCurrentUser();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = UserInfo.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int getCurrentUserId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(121, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCurrentUserId();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int getLaunchedFromUid(IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    boolean _status = this.mRemote.transact(122, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLaunchedFromUid(activityToken);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void unstableProviderDied(IBinder connection) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(connection);
                    boolean _status = this.mRemote.transact(123, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unstableProviderDied(connection);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean isIntentSenderAnActivity(IIntentSender sender) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(124, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isIntentSenderAnActivity(sender);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int startActivityAsUser(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
                Parcel _reply;
                Parcel _data;
                IBinder asBinder;
                Parcel _reply2;
                Parcel _data2;
                Parcel _data3 = Parcel.obtain();
                Parcel _reply3 = Parcel.obtain();
                try {
                    _data3.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (caller != null) {
                        try {
                            asBinder = caller.asBinder();
                        } catch (Throwable th) {
                            th = th;
                            _reply = _reply3;
                            _data = _data3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } else {
                        asBinder = null;
                    }
                    _data3.writeStrongBinder(asBinder);
                    _data3.writeString(callingPackage);
                    if (intent != null) {
                        _data3.writeInt(1);
                        intent.writeToParcel(_data3, 0);
                    } else {
                        _data3.writeInt(0);
                    }
                    _data3.writeString(resolvedType);
                    _data3.writeStrongBinder(resultTo);
                    _data3.writeString(resultWho);
                    _data3.writeInt(requestCode);
                    _data3.writeInt(flags);
                    if (profilerInfo != null) {
                        _data3.writeInt(1);
                        profilerInfo.writeToParcel(_data3, 0);
                    } else {
                        _data3.writeInt(0);
                    }
                    if (options != null) {
                        _data3.writeInt(1);
                        options.writeToParcel(_data3, 0);
                    } else {
                        _data3.writeInt(0);
                    }
                    _data3.writeInt(userId);
                    boolean _status = this.mRemote.transact(125, _data3, _reply3, 0);
                    try {
                        if (_status) {
                            _reply2 = _reply3;
                            _data2 = _data3;
                        } else if (Stub.getDefaultImpl() != null) {
                            int startActivityAsUser = Stub.getDefaultImpl().startActivityAsUser(caller, callingPackage, intent, resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options, userId);
                            _reply3.recycle();
                            _data3.recycle();
                            return startActivityAsUser;
                        } else {
                            _reply2 = _reply3;
                            _data2 = _data3;
                        }
                        _reply2.readException();
                        int _result = _reply2.readInt();
                        _reply2.recycle();
                        _data2.recycle();
                        return _result;
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    _reply = _reply3;
                    _data = _data3;
                }
            }

            @Override // android.app.IActivityManager
            public int startActivityAsUserWithFeature(IApplicationThread caller, String callingPackage, String callingFeatureId, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
                Parcel _reply;
                Parcel _data;
                IBinder asBinder;
                Parcel _reply2;
                Parcel _data2;
                Parcel _data3 = Parcel.obtain();
                Parcel _reply3 = Parcel.obtain();
                try {
                    _data3.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (caller != null) {
                        try {
                            asBinder = caller.asBinder();
                        } catch (Throwable th) {
                            th = th;
                            _reply = _reply3;
                            _data = _data3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } else {
                        asBinder = null;
                    }
                    _data3.writeStrongBinder(asBinder);
                    _data3.writeString(callingPackage);
                    _data3.writeString(callingFeatureId);
                    if (intent != null) {
                        _data3.writeInt(1);
                        intent.writeToParcel(_data3, 0);
                    } else {
                        _data3.writeInt(0);
                    }
                    _data3.writeString(resolvedType);
                    _data3.writeStrongBinder(resultTo);
                    _data3.writeString(resultWho);
                    _data3.writeInt(requestCode);
                    _data3.writeInt(flags);
                    if (profilerInfo != null) {
                        _data3.writeInt(1);
                        profilerInfo.writeToParcel(_data3, 0);
                    } else {
                        _data3.writeInt(0);
                    }
                    if (options != null) {
                        _data3.writeInt(1);
                        options.writeToParcel(_data3, 0);
                    } else {
                        _data3.writeInt(0);
                    }
                    _data3.writeInt(userId);
                    boolean _status = this.mRemote.transact(126, _data3, _reply3, 0);
                    try {
                        if (_status) {
                            _reply2 = _reply3;
                            _data2 = _data3;
                        } else if (Stub.getDefaultImpl() != null) {
                            int startActivityAsUserWithFeature = Stub.getDefaultImpl().startActivityAsUserWithFeature(caller, callingPackage, callingFeatureId, intent, resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options, userId);
                            _reply3.recycle();
                            _data3.recycle();
                            return startActivityAsUserWithFeature;
                        } else {
                            _reply2 = _reply3;
                            _data2 = _data3;
                        }
                        _reply2.readException();
                        int _result = _reply2.readInt();
                        _reply2.recycle();
                        _data2.recycle();
                        return _result;
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    _reply = _reply3;
                    _data = _data3;
                }
            }

            @Override // android.app.IActivityManager
            public int stopUser(int userid, boolean force, IStopUserCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    _data.writeInt(force ? 1 : 0);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(127, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().stopUser(userid, force, callback);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int stopUserWithDelayedLocking(int userid, boolean force, IStopUserCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    _data.writeInt(force ? 1 : 0);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(128, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().stopUserWithDelayedLocking(userid, force, callback);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void registerUserSwitchObserver(IUserSwitchObserver observer, String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeString(name);
                    boolean _status = this.mRemote.transact(129, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerUserSwitchObserver(observer, name);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void unregisterUserSwitchObserver(IUserSwitchObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    boolean _status = this.mRemote.transact(130, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterUserSwitchObserver(observer);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int[] getRunningUserIds() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(131, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRunningUserIds();
                    }
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void requestSystemServerHeapDump() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(132, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().requestSystemServerHeapDump();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void requestBugReport(int bugreportType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(bugreportType);
                    boolean _status = this.mRemote.transact(133, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().requestBugReport(bugreportType);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void requestBugReportWithDescription(String shareTitle, String shareDescription, int bugreportType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(shareTitle);
                    _data.writeString(shareDescription);
                    _data.writeInt(bugreportType);
                    boolean _status = this.mRemote.transact(134, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().requestBugReportWithDescription(shareTitle, shareDescription, bugreportType);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void requestTelephonyBugReport(String shareTitle, String shareDescription) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(shareTitle);
                    _data.writeString(shareDescription);
                    boolean _status = this.mRemote.transact(135, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().requestTelephonyBugReport(shareTitle, shareDescription);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void requestWifiBugReport(String shareTitle, String shareDescription) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(shareTitle);
                    _data.writeString(shareDescription);
                    boolean _status = this.mRemote.transact(136, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().requestWifiBugReport(shareTitle, shareDescription);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void requestInteractiveBugReportWithDescription(String shareTitle, String shareDescription) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(shareTitle);
                    _data.writeString(shareDescription);
                    boolean _status = this.mRemote.transact(137, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().requestInteractiveBugReportWithDescription(shareTitle, shareDescription);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void requestInteractiveBugReport() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(138, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().requestInteractiveBugReport();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void requestFullBugReport() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(139, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().requestFullBugReport();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void requestRemoteBugReport() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(140, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().requestRemoteBugReport();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean launchBugReportHandlerApp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(141, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().launchBugReportHandlerApp();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public List<String> getBugreportWhitelistedPackages() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(142, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getBugreportWhitelistedPackages();
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public Intent getIntentForIntentSender(IIntentSender sender) throws RemoteException {
                Intent _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    boolean _status = this.mRemote.transact(143, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getIntentForIntentSender(sender);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Intent.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public String getLaunchedFromPackage(IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    boolean _status = this.mRemote.transact(144, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLaunchedFromPackage(activityToken);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void killUid(int appId, int userId, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(appId);
                    _data.writeInt(userId);
                    _data.writeString(reason);
                    boolean _status = this.mRemote.transact(145, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().killUid(appId, userId, reason);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setUserIsMonkey(boolean monkey) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(monkey ? 1 : 0);
                    boolean _status = this.mRemote.transact(146, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setUserIsMonkey(monkey);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void hang(IBinder who, boolean allowRestart) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(who);
                    _data.writeInt(allowRestart ? 1 : 0);
                    boolean _status = this.mRemote.transact(147, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().hang(who, allowRestart);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public List<ActivityTaskManager.RootTaskInfo> getAllRootTaskInfos() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(148, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAllRootTaskInfos();
                    }
                    _reply.readException();
                    List<ActivityTaskManager.RootTaskInfo> _result = _reply.createTypedArrayList(ActivityTaskManager.RootTaskInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void moveTaskToRootTask(int taskId, int rootTaskId, boolean toTop) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(rootTaskId);
                    _data.writeInt(toTop ? 1 : 0);
                    boolean _status = this.mRemote.transact(149, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().moveTaskToRootTask(taskId, rootTaskId, toTop);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setFocusedRootTask(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean _status = this.mRemote.transact(150, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setFocusedRootTask(taskId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public ActivityTaskManager.RootTaskInfo getFocusedRootTaskInfo() throws RemoteException {
                ActivityTaskManager.RootTaskInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(151, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getFocusedRootTaskInfo();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ActivityTaskManager.RootTaskInfo.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void restart() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(152, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().restart();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void performIdleMaintenance() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(153, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().performIdleMaintenance();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void appNotRespondingViaProvider(IBinder connection) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(connection);
                    boolean _status = this.mRemote.transact(154, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().appNotRespondingViaProvider(connection);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public Rect getTaskBounds(int taskId) throws RemoteException {
                Rect _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean _status = this.mRemote.transact(155, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getTaskBounds(taskId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Rect.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean setProcessMemoryTrimLevel(String process, int userId, int level) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(process);
                    _data.writeInt(userId);
                    _data.writeInt(level);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(156, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setProcessMemoryTrimLevel(process, userId, level);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public String getTagForIntentSender(IIntentSender sender, String prefix) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    _data.writeString(prefix);
                    boolean _status = this.mRemote.transact(157, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getTagForIntentSender(sender, prefix);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean startUserInBackground(int userid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(158, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startUserInBackground(userid);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean isInLockTaskMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(159, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isInLockTaskMode();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int startActivityFromRecents(int taskId, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(160, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startActivityFromRecents(taskId, options);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void startSystemLockTaskMode(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean _status = this.mRemote.transact(161, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startSystemLockTaskMode(taskId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean isTopOfTask(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(162, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isTopOfTask(token);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void bootAnimationComplete() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(163, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().bootAnimationComplete();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void registerTaskStackListener(ITaskStackListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(164, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerTaskStackListener(listener);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void unregisterTaskStackListener(ITaskStackListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(165, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterTaskStackListener(listener);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void notifyCleartextNetwork(int uid, byte[] firstPacket) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeByteArray(firstPacket);
                    boolean _status = this.mRemote.transact(166, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyCleartextNetwork(uid, firstPacket);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setTaskResizeable(int taskId, int resizeableMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(resizeableMode);
                    boolean _status = this.mRemote.transact(167, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setTaskResizeable(taskId, resizeableMode);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void resizeTask(int taskId, Rect bounds, int resizeMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    if (bounds != null) {
                        _data.writeInt(1);
                        bounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(resizeMode);
                    boolean _status = this.mRemote.transact(168, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().resizeTask(taskId, bounds, resizeMode);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int getLockTaskModeState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(169, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLockTaskModeState();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setDumpHeapDebugLimit(String processName, int uid, long maxMemSize, String reportPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(processName);
                    _data.writeInt(uid);
                    _data.writeLong(maxMemSize);
                    _data.writeString(reportPackage);
                    boolean _status = this.mRemote.transact(170, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDumpHeapDebugLimit(processName, uid, maxMemSize, reportPackage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void dumpHeapFinished(String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    boolean _status = this.mRemote.transact(171, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dumpHeapFinished(path);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void updateLockTaskPackages(int userId, String[] packages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeStringArray(packages);
                    boolean _status = this.mRemote.transact(172, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateLockTaskPackages(userId, packages);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void noteAlarmStart(IIntentSender sender, WorkSource workSource, int sourceUid, String tag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    if (workSource != null) {
                        _data.writeInt(1);
                        workSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(sourceUid);
                    _data.writeString(tag);
                    boolean _status = this.mRemote.transact(173, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteAlarmStart(sender, workSource, sourceUid, tag);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void noteAlarmFinish(IIntentSender sender, WorkSource workSource, int sourceUid, String tag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    if (workSource != null) {
                        _data.writeInt(1);
                        workSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(sourceUid);
                    _data.writeString(tag);
                    boolean _status = this.mRemote.transact(174, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteAlarmFinish(sender, workSource, sourceUid, tag);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int getPackageProcessState(String packageName, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(175, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPackageProcessState(packageName, callingPackage);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void updateDeviceOwner(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(176, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateDeviceOwner(packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean startBinderTracking() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(177, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startBinderTracking();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean stopBinderTrackingAndDump(ParcelFileDescriptor fd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(178, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().stopBinderTrackingAndDump(fd);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void suppressResizeConfigChanges(boolean suppress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(suppress ? 1 : 0);
                    boolean _status = this.mRemote.transact(179, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().suppressResizeConfigChanges(suppress);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean unlockUser(int userid, byte[] token, byte[] secret, IProgressListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    _data.writeByteArray(token);
                    _data.writeByteArray(secret);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(180, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().unlockUser(userid, token, secret, listener);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void killPackageDependents(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(181, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().killPackageDependents(packageName, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void makePackageIdle(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(182, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().makePackageIdle(packageName, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int getMemoryTrimLevel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(183, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMemoryTrimLevel();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean isVrModePackageEnabled(ComponentName packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (packageName != null) {
                        _data.writeInt(1);
                        packageName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(184, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isVrModePackageEnabled(packageName);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void notifyLockedProfile(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(185, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyLockedProfile(userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void startConfirmDeviceCredentialIntent(Intent intent, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(186, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startConfirmDeviceCredentialIntent(intent, options);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void sendIdleJobTrigger() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(187, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().sendIdleJobTrigger();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int sendIntentSender(IIntentSender target, IBinder whitelistToken, int code, Intent intent, String resolvedType, IIntentReceiver finishedReceiver, String requiredPermission, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBinder iBinder = null;
                    _data.writeStrongBinder(target != null ? target.asBinder() : null);
                    try {
                        _data.writeStrongBinder(whitelistToken);
                        try {
                            _data.writeInt(code);
                            if (intent != null) {
                                _data.writeInt(1);
                                intent.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            _data.writeString(resolvedType);
                            if (finishedReceiver != null) {
                                iBinder = finishedReceiver.asBinder();
                            }
                            _data.writeStrongBinder(iBinder);
                            _data.writeString(requiredPermission);
                            if (options != null) {
                                _data.writeInt(1);
                                options.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            boolean _status = this.mRemote.transact(188, _data, _reply, 0);
                            if (!_status && Stub.getDefaultImpl() != null) {
                                int sendIntentSender = Stub.getDefaultImpl().sendIntentSender(target, whitelistToken, code, intent, resolvedType, finishedReceiver, requiredPermission, options);
                                _reply.recycle();
                                _data.recycle();
                                return sendIntentSender;
                            }
                            _reply.readException();
                            int _result = _reply.readInt();
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        } catch (Throwable th) {
                            th = th;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
            }

            @Override // android.app.IActivityManager
            public boolean isBackgroundRestricted(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(189, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isBackgroundRestricted(packageName);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setRenderThread(int tid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(tid);
                    boolean _status = this.mRemote.transact(190, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setRenderThread(tid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setHasTopUi(boolean hasTopUi) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(hasTopUi ? 1 : 0);
                    boolean _status = this.mRemote.transact(191, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setHasTopUi(hasTopUi);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int restartUserInBackground(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(192, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().restartUserInBackground(userId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void cancelTaskWindowTransition(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean _status = this.mRemote.transact(193, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cancelTaskWindowTransition(taskId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void scheduleApplicationInfoChanged(List<String> packageNames, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(packageNames);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(194, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scheduleApplicationInfoChanged(packageNames, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setPersistentVrThread(int tid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(tid);
                    boolean _status = this.mRemote.transact(195, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPersistentVrThread(tid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void waitForNetworkStateUpdate(long procStateSeq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(procStateSeq);
                    boolean _status = this.mRemote.transact(196, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().waitForNetworkStateUpdate(procStateSeq);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void backgroundAllowlistUid(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(197, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().backgroundAllowlistUid(uid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean startUserInBackgroundWithListener(int userid, IProgressListener unlockProgressListener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    _data.writeStrongBinder(unlockProgressListener != null ? unlockProgressListener.asBinder() : null);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(198, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startUserInBackgroundWithListener(userid, unlockProgressListener);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void startDelegateShellPermissionIdentity(int uid, String[] permissions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeStringArray(permissions);
                    boolean _status = this.mRemote.transact(199, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startDelegateShellPermissionIdentity(uid, permissions);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void stopDelegateShellPermissionIdentity() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(200, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().stopDelegateShellPermissionIdentity();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public List<String> getDelegatedShellPermissions() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(201, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDelegatedShellPermissions();
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public ParcelFileDescriptor getLifeMonitor() throws RemoteException {
                ParcelFileDescriptor _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(202, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLifeMonitor();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParcelFileDescriptor.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean startUserInForegroundWithListener(int userid, IProgressListener unlockProgressListener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    _data.writeStrongBinder(unlockProgressListener != null ? unlockProgressListener.asBinder() : null);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(203, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startUserInForegroundWithListener(userid, unlockProgressListener);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void appNotResponding(String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(reason);
                    boolean _status = this.mRemote.transact(204, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().appNotResponding(reason);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public ParceledListSlice<ApplicationExitInfo> getHistoricalProcessExitReasons(String packageName, int pid, int maxNum, int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(maxNum);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(205, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getHistoricalProcessExitReasons(packageName, pid, maxNum, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void killProcessesWhenImperceptible(int[] pids, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(pids);
                    _data.writeString(reason);
                    boolean _status = this.mRemote.transact(206, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().killProcessesWhenImperceptible(pids, reason);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setActivityLocusContext(ComponentName activity, LocusId locusId, IBinder appToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (activity != null) {
                        _data.writeInt(1);
                        activity.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (locusId != null) {
                        _data.writeInt(1);
                        locusId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(appToken);
                    boolean _status = this.mRemote.transact(207, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setActivityLocusContext(activity, locusId, appToken);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setProcessStateSummary(byte[] state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(state);
                    boolean _status = this.mRemote.transact(208, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setProcessStateSummary(state);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean isAppFreezerSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(209, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isAppFreezerSupported();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void killUidForPermissionChange(int appId, int userId, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(appId);
                    _data.writeInt(userId);
                    _data.writeString(reason);
                    boolean _status = this.mRemote.transact(210, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().killUidForPermissionChange(appId, userId, reason);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void resetAppErrors() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(211, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().resetAppErrors();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean enableAppFreezer(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    _data.writeInt(enable ? 1 : 0);
                    boolean _status = this.mRemote.transact(212, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().enableAppFreezer(enable);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean enableFgsNotificationRateLimit(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    _data.writeInt(enable ? 1 : 0);
                    boolean _status = this.mRemote.transact(213, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().enableFgsNotificationRateLimit(enable);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void holdLock(IBinder token, int durationMs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(durationMs);
                    boolean _status = this.mRemote.transact(214, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().holdLock(token, durationMs);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean startProfile(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(215, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startProfile(userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean stopProfile(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(216, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().stopProfile(userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public ParceledListSlice queryIntentComponentsForIntentSender(IIntentSender sender, int matchFlags) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    _data.writeInt(matchFlags);
                    boolean _status = this.mRemote.transact(217, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().queryIntentComponentsForIntentSender(sender, matchFlags);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int getUidProcessCapabilities(int uid, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(218, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getUidProcessCapabilities(uid, callingPackage);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void waitForBroadcastIdle() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(219, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().waitForBroadcastIdle();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean isWindowMode(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(220, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isWindowMode(token);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IActivityManager impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IActivityManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
