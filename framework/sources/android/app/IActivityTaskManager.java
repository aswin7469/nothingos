package android.app;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.IActivityClientController;
import android.app.IActivityController;
import android.app.IApplicationThread;
import android.app.IAssistDataReceiver;
import android.app.ITaskStackListener;
import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.ComponentName;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.content.pm.ParceledListSlice;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.service.voice.IVoiceInteractionSession;
import android.view.IRecentsAnimationRunner;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationDefinition;
import android.window.IWindowOrganizerController;
import android.window.SplashScreenView;
import android.window.TaskSnapshot;
import com.android.internal.app.IVoiceInteractor;
import java.util.List;
/* loaded from: classes.dex */
public interface IActivityTaskManager extends IInterface {
    public static final String DESCRIPTOR = "android.app.IActivityTaskManager";

    int addAppTask(IBinder iBinder, Intent intent, ActivityManager.TaskDescription taskDescription, Bitmap bitmap) throws RemoteException;

    void alwaysShowUnsupportedCompileSdkWarning(ComponentName componentName) throws RemoteException;

    void cancelRecentsAnimation(boolean z) throws RemoteException;

    void cancelTaskWindowTransition(int i) throws RemoteException;

    void clearLaunchParamsForPackages(List<String> list) throws RemoteException;

    void finishVoiceTask(IVoiceInteractionSession iVoiceInteractionSession) throws RemoteException;

    IActivityClientController getActivityClientController() throws RemoteException;

    List<ActivityTaskManager.RootTaskInfo> getAllRootTaskInfos() throws RemoteException;

    List<ActivityTaskManager.RootTaskInfo> getAllRootTaskInfosOnDisplay(int i) throws RemoteException;

    Point getAppTaskThumbnailSize() throws RemoteException;

    List<IBinder> getAppTasks(String str) throws RemoteException;

    Bundle getAssistContextExtras(int i) throws RemoteException;

    ConfigurationInfo getDeviceConfigurationInfo() throws RemoteException;

    ActivityTaskManager.RootTaskInfo getFocusedRootTaskInfo() throws RemoteException;

    int getFrontActivityScreenCompatMode() throws RemoteException;

    int getLastResumedActivityUserId() throws RemoteException;

    int getLockTaskModeState() throws RemoteException;

    boolean getPackageAskScreenCompat(String str) throws RemoteException;

    int getPackageScreenCompatMode(String str) throws RemoteException;

    ParceledListSlice<ActivityManager.RecentTaskInfo> getRecentTasks(int i, int i2, int i3) throws RemoteException;

    ActivityTaskManager.RootTaskInfo getRootTaskInfo(int i, int i2) throws RemoteException;

    ActivityTaskManager.RootTaskInfo getRootTaskInfoOnDisplay(int i, int i2, int i3) throws RemoteException;

    Rect getTaskBounds(int i) throws RemoteException;

    ActivityManager.TaskDescription getTaskDescription(int i) throws RemoteException;

    Bitmap getTaskDescriptionIcon(String str, int i) throws RemoteException;

    TaskSnapshot getTaskSnapshot(int i, boolean z) throws RemoteException;

    List<ActivityManager.RunningTaskInfo> getTasks(int i, boolean z, boolean z2) throws RemoteException;

    List<ActivityManager.RunningTaskInfo> getTasksWm(int i, boolean z, boolean z2, int i2) throws RemoteException;

    IWindowOrganizerController getWindowOrganizerController() throws RemoteException;

    boolean isActivityStartAllowedOnDisplay(int i, Intent intent, String str, int i2) throws RemoteException;

    boolean isAssistDataAllowedOnCurrentActivity() throws RemoteException;

    boolean isInLockTaskMode() throws RemoteException;

    boolean isTopActivityImmersive() throws RemoteException;

    void keyguardGoingAway(int i) throws RemoteException;

    void moveRootTaskToDisplay(int i, int i2) throws RemoteException;

    void moveTaskToFront(IApplicationThread iApplicationThread, String str, int i, int i2, Bundle bundle) throws RemoteException;

    void moveTaskToRootTask(int i, int i2, boolean z) throws RemoteException;

    void onPictureInPictureStateChanged(PictureInPictureUiState pictureInPictureUiState) throws RemoteException;

    void onSplashScreenViewCopyFinished(int i, SplashScreenView.SplashScreenViewParcelable splashScreenViewParcelable) throws RemoteException;

    void registerRemoteAnimationForNextActivityStart(String str, RemoteAnimationAdapter remoteAnimationAdapter) throws RemoteException;

    void registerRemoteAnimationsForDisplay(int i, RemoteAnimationDefinition remoteAnimationDefinition) throws RemoteException;

    void registerTaskStackListener(ITaskStackListener iTaskStackListener) throws RemoteException;

    void releaseSomeActivities(IApplicationThread iApplicationThread) throws RemoteException;

    void removeAllVisibleRecentTasks() throws RemoteException;

    void removeRootTasksInWindowingModes(int[] iArr) throws RemoteException;

    void removeRootTasksWithActivityTypes(int[] iArr) throws RemoteException;

    boolean removeTask(int i) throws RemoteException;

    void reportAssistContextExtras(IBinder iBinder, Bundle bundle, AssistStructure assistStructure, AssistContent assistContent, Uri uri) throws RemoteException;

    boolean requestAssistContextExtras(int i, IAssistDataReceiver iAssistDataReceiver, Bundle bundle, IBinder iBinder, boolean z, boolean z2) throws RemoteException;

    boolean requestAssistDataForTask(IAssistDataReceiver iAssistDataReceiver, int i, String str) throws RemoteException;

    boolean requestAutofillData(IAssistDataReceiver iAssistDataReceiver, Bundle bundle, IBinder iBinder, int i) throws RemoteException;

    IBinder requestStartActivityPermissionToken(IBinder iBinder) throws RemoteException;

    boolean resizeTask(int i, Rect rect, int i2) throws RemoteException;

    void resumeAppSwitches() throws RemoteException;

    void setActivityController(IActivityController iActivityController, boolean z) throws RemoteException;

    void setFocusedRootTask(int i) throws RemoteException;

    void setFocusedTask(int i) throws RemoteException;

    void setFrontActivityScreenCompatMode(int i) throws RemoteException;

    void setLockScreenShown(boolean z, boolean z2) throws RemoteException;

    void setPackageAskScreenCompat(String str, boolean z) throws RemoteException;

    void setPackageScreenCompatMode(String str, int i) throws RemoteException;

    void setPersistentVrThread(int i) throws RemoteException;

    void setSplitScreenResizing(boolean z) throws RemoteException;

    void setTaskResizeable(int i, int i2) throws RemoteException;

    void setVoiceKeepAwake(IVoiceInteractionSession iVoiceInteractionSession, boolean z) throws RemoteException;

    void setVrThread(int i) throws RemoteException;

    int startActivities(IApplicationThread iApplicationThread, String str, String str2, Intent[] intentArr, String[] strArr, IBinder iBinder, Bundle bundle, int i) throws RemoteException;

    int startActivity(IApplicationThread iApplicationThread, String str, String str2, Intent intent, String str3, IBinder iBinder, String str4, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle) throws RemoteException;

    WaitResult startActivityAndWait(IApplicationThread iApplicationThread, String str, String str2, Intent intent, String str3, IBinder iBinder, String str4, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle, int i3) throws RemoteException;

    int startActivityAsCaller(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IBinder iBinder, String str3, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle, IBinder iBinder2, boolean z, int i3) throws RemoteException;

    int startActivityAsUser(IApplicationThread iApplicationThread, String str, String str2, Intent intent, String str3, IBinder iBinder, String str4, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle, int i3) throws RemoteException;

    int startActivityFromRecents(int i, Bundle bundle) throws RemoteException;

    int startActivityIntentSender(IApplicationThread iApplicationThread, IIntentSender iIntentSender, IBinder iBinder, Intent intent, String str, IBinder iBinder2, String str2, int i, int i2, int i3, Bundle bundle) throws RemoteException;

    int startActivityWithConfig(IApplicationThread iApplicationThread, String str, String str2, Intent intent, String str3, IBinder iBinder, String str4, int i, int i2, Configuration configuration, Bundle bundle, int i3) throws RemoteException;

    int startAssistantActivity(String str, String str2, int i, int i2, Intent intent, String str3, Bundle bundle, int i3) throws RemoteException;

    boolean startDreamActivity(Intent intent) throws RemoteException;

    boolean startNextMatchingActivity(IBinder iBinder, Intent intent, Bundle bundle) throws RemoteException;

    void startRecentsActivity(Intent intent, long j, IRecentsAnimationRunner iRecentsAnimationRunner) throws RemoteException;

    void startSystemLockTaskMode(int i) throws RemoteException;

    int startVoiceActivity(String str, String str2, int i, int i2, Intent intent, String str3, IVoiceInteractionSession iVoiceInteractionSession, IVoiceInteractor iVoiceInteractor, int i3, ProfilerInfo profilerInfo, Bundle bundle, int i4) throws RemoteException;

    void stopAppSwitches() throws RemoteException;

    void stopSystemLockTaskMode() throws RemoteException;

    boolean supportsLocalVoiceInteraction() throws RemoteException;

    void suppressResizeConfigChanges(boolean z) throws RemoteException;

    void unhandledBack() throws RemoteException;

    void unregisterTaskStackListener(ITaskStackListener iTaskStackListener) throws RemoteException;

    boolean updateConfiguration(Configuration configuration) throws RemoteException;

    void updateLockTaskFeatures(int i, int i2) throws RemoteException;

    void updateLockTaskPackages(int i, String[] strArr) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IActivityTaskManager {
        @Override // android.app.IActivityTaskManager
        public int startActivity(IApplicationThread caller, String callingPackage, String callingFeatureId, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public int startActivities(IApplicationThread caller, String callingPackage, String callingFeatureId, Intent[] intents, String[] resolvedTypes, IBinder resultTo, Bundle options, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public int startActivityAsUser(IApplicationThread caller, String callingPackage, String callingFeatureId, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public boolean startNextMatchingActivity(IBinder callingActivity, Intent intent, Bundle options) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public boolean startDreamActivity(Intent intent) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public int startActivityIntentSender(IApplicationThread caller, IIntentSender target, IBinder whitelistToken, Intent fillInIntent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flagsMask, int flagsValues, Bundle options) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public WaitResult startActivityAndWait(IApplicationThread caller, String callingPackage, String callingFeatureId, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public int startActivityWithConfig(IApplicationThread caller, String callingPackage, String callingFeatureId, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int startFlags, Configuration newConfig, Bundle options, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public int startVoiceActivity(String callingPackage, String callingFeatureId, int callingPid, int callingUid, Intent intent, String resolvedType, IVoiceInteractionSession session, IVoiceInteractor interactor, int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public int startAssistantActivity(String callingPackage, String callingFeatureId, int callingPid, int callingUid, Intent intent, String resolvedType, Bundle options, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public void startRecentsActivity(Intent intent, long eventTime, IRecentsAnimationRunner recentsAnimationRunner) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public int startActivityFromRecents(int taskId, Bundle options) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public int startActivityAsCaller(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options, IBinder permissionToken, boolean ignoreTargetSecurity, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public boolean isActivityStartAllowedOnDisplay(int displayId, Intent intent, String resolvedType, int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public void unhandledBack() throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public IActivityClientController getActivityClientController() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public int getFrontActivityScreenCompatMode() throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public void setFrontActivityScreenCompatMode(int mode) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void setFocusedTask(int taskId) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public boolean removeTask(int taskId) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public void removeAllVisibleRecentTasks() throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public List<ActivityManager.RunningTaskInfo> getTasks(int maxNum, boolean filterOnlyVisibleRecents, boolean keepIntentExtra) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public List<ActivityManager.RunningTaskInfo> getTasksWm(int maxNum, boolean filterOnlyVisibleRecents, boolean keepIntentExtra, int ignoreWindowingMode) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public void moveTaskToFront(IApplicationThread app, String callingPackage, int task, int flags, Bundle options) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public ParceledListSlice<ActivityManager.RecentTaskInfo> getRecentTasks(int maxNum, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public boolean isTopActivityImmersive() throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public ActivityManager.TaskDescription getTaskDescription(int taskId) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public void reportAssistContextExtras(IBinder assistToken, Bundle extras, AssistStructure structure, AssistContent content, Uri referrer) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void setFocusedRootTask(int taskId) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public ActivityTaskManager.RootTaskInfo getFocusedRootTaskInfo() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public Rect getTaskBounds(int taskId) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public void cancelRecentsAnimation(boolean restoreHomeRootTaskPosition) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void updateLockTaskPackages(int userId, String[] packages) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public boolean isInLockTaskMode() throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public int getLockTaskModeState() throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public List<IBinder> getAppTasks(String callingPackage) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public void startSystemLockTaskMode(int taskId) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void stopSystemLockTaskMode() throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void finishVoiceTask(IVoiceInteractionSession session) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public int addAppTask(IBinder activityToken, Intent intent, ActivityManager.TaskDescription description, Bitmap thumbnail) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public Point getAppTaskThumbnailSize() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public IBinder requestStartActivityPermissionToken(IBinder delegatorToken) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public void releaseSomeActivities(IApplicationThread app) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public Bitmap getTaskDescriptionIcon(String filename, int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public void registerTaskStackListener(ITaskStackListener listener) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void unregisterTaskStackListener(ITaskStackListener listener) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void setTaskResizeable(int taskId, int resizeableMode) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public boolean resizeTask(int taskId, Rect bounds, int resizeMode) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public void moveRootTaskToDisplay(int taskId, int displayId) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void moveTaskToRootTask(int taskId, int rootTaskId, boolean toTop) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void removeRootTasksInWindowingModes(int[] windowingModes) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void removeRootTasksWithActivityTypes(int[] activityTypes) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public List<ActivityTaskManager.RootTaskInfo> getAllRootTaskInfos() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public ActivityTaskManager.RootTaskInfo getRootTaskInfo(int windowingMode, int activityType) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public List<ActivityTaskManager.RootTaskInfo> getAllRootTaskInfosOnDisplay(int displayId) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public ActivityTaskManager.RootTaskInfo getRootTaskInfoOnDisplay(int windowingMode, int activityType, int displayId) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public void setLockScreenShown(boolean showingKeyguard, boolean showingAod) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public Bundle getAssistContextExtras(int requestType) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public boolean requestAssistContextExtras(int requestType, IAssistDataReceiver receiver, Bundle receiverExtras, IBinder activityToken, boolean focused, boolean newSessionId) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public boolean requestAutofillData(IAssistDataReceiver receiver, Bundle receiverExtras, IBinder activityToken, int flags) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public boolean isAssistDataAllowedOnCurrentActivity() throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public boolean requestAssistDataForTask(IAssistDataReceiver receiver, int taskId, String callingPackageName) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public void keyguardGoingAway(int flags) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void suppressResizeConfigChanges(boolean suppress) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public IWindowOrganizerController getWindowOrganizerController() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public void setSplitScreenResizing(boolean resizing) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public boolean supportsLocalVoiceInteraction() throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public ConfigurationInfo getDeviceConfigurationInfo() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public void cancelTaskWindowTransition(int taskId) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public TaskSnapshot getTaskSnapshot(int taskId, boolean isLowResolution) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public int getLastResumedActivityUserId() throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public boolean updateConfiguration(Configuration values) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public void updateLockTaskFeatures(int userId, int flags) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void registerRemoteAnimationForNextActivityStart(String packageName, RemoteAnimationAdapter adapter) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void registerRemoteAnimationsForDisplay(int displayId, RemoteAnimationDefinition definition) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void alwaysShowUnsupportedCompileSdkWarning(ComponentName activity) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void setVrThread(int tid) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void setPersistentVrThread(int tid) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void stopAppSwitches() throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void resumeAppSwitches() throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void setActivityController(IActivityController watcher, boolean imAMonkey) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void setVoiceKeepAwake(IVoiceInteractionSession session, boolean keepAwake) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public int getPackageScreenCompatMode(String packageName) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public void setPackageScreenCompatMode(String packageName, int mode) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public boolean getPackageAskScreenCompat(String packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public void setPackageAskScreenCompat(String packageName, boolean ask) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void clearLaunchParamsForPackages(List<String> packageNames) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void onSplashScreenViewCopyFinished(int taskId, SplashScreenView.SplashScreenViewParcelable material) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void onPictureInPictureStateChanged(PictureInPictureUiState pipState) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IActivityTaskManager {
        static final int TRANSACTION_addAppTask = 40;
        static final int TRANSACTION_alwaysShowUnsupportedCompileSdkWarning = 76;
        static final int TRANSACTION_cancelRecentsAnimation = 32;
        static final int TRANSACTION_cancelTaskWindowTransition = 69;
        static final int TRANSACTION_clearLaunchParamsForPackages = 87;
        static final int TRANSACTION_finishVoiceTask = 39;
        static final int TRANSACTION_getActivityClientController = 16;
        static final int TRANSACTION_getAllRootTaskInfos = 53;
        static final int TRANSACTION_getAllRootTaskInfosOnDisplay = 55;
        static final int TRANSACTION_getAppTaskThumbnailSize = 41;
        static final int TRANSACTION_getAppTasks = 36;
        static final int TRANSACTION_getAssistContextExtras = 58;
        static final int TRANSACTION_getDeviceConfigurationInfo = 68;
        static final int TRANSACTION_getFocusedRootTaskInfo = 30;
        static final int TRANSACTION_getFrontActivityScreenCompatMode = 17;
        static final int TRANSACTION_getLastResumedActivityUserId = 71;
        static final int TRANSACTION_getLockTaskModeState = 35;
        static final int TRANSACTION_getPackageAskScreenCompat = 85;
        static final int TRANSACTION_getPackageScreenCompatMode = 83;
        static final int TRANSACTION_getRecentTasks = 25;
        static final int TRANSACTION_getRootTaskInfo = 54;
        static final int TRANSACTION_getRootTaskInfoOnDisplay = 56;
        static final int TRANSACTION_getTaskBounds = 31;
        static final int TRANSACTION_getTaskDescription = 27;
        static final int TRANSACTION_getTaskDescriptionIcon = 44;
        static final int TRANSACTION_getTaskSnapshot = 70;
        static final int TRANSACTION_getTasks = 22;
        static final int TRANSACTION_getTasksWm = 23;
        static final int TRANSACTION_getWindowOrganizerController = 65;
        static final int TRANSACTION_isActivityStartAllowedOnDisplay = 14;
        static final int TRANSACTION_isAssistDataAllowedOnCurrentActivity = 61;
        static final int TRANSACTION_isInLockTaskMode = 34;
        static final int TRANSACTION_isTopActivityImmersive = 26;
        static final int TRANSACTION_keyguardGoingAway = 63;
        static final int TRANSACTION_moveRootTaskToDisplay = 49;
        static final int TRANSACTION_moveTaskToFront = 24;
        static final int TRANSACTION_moveTaskToRootTask = 50;
        static final int TRANSACTION_onPictureInPictureStateChanged = 89;
        static final int TRANSACTION_onSplashScreenViewCopyFinished = 88;
        static final int TRANSACTION_registerRemoteAnimationForNextActivityStart = 74;
        static final int TRANSACTION_registerRemoteAnimationsForDisplay = 75;
        static final int TRANSACTION_registerTaskStackListener = 45;
        static final int TRANSACTION_releaseSomeActivities = 43;
        static final int TRANSACTION_removeAllVisibleRecentTasks = 21;
        static final int TRANSACTION_removeRootTasksInWindowingModes = 51;
        static final int TRANSACTION_removeRootTasksWithActivityTypes = 52;
        static final int TRANSACTION_removeTask = 20;
        static final int TRANSACTION_reportAssistContextExtras = 28;
        static final int TRANSACTION_requestAssistContextExtras = 59;
        static final int TRANSACTION_requestAssistDataForTask = 62;
        static final int TRANSACTION_requestAutofillData = 60;
        static final int TRANSACTION_requestStartActivityPermissionToken = 42;
        static final int TRANSACTION_resizeTask = 48;
        static final int TRANSACTION_resumeAppSwitches = 80;
        static final int TRANSACTION_setActivityController = 81;
        static final int TRANSACTION_setFocusedRootTask = 29;
        static final int TRANSACTION_setFocusedTask = 19;
        static final int TRANSACTION_setFrontActivityScreenCompatMode = 18;
        static final int TRANSACTION_setLockScreenShown = 57;
        static final int TRANSACTION_setPackageAskScreenCompat = 86;
        static final int TRANSACTION_setPackageScreenCompatMode = 84;
        static final int TRANSACTION_setPersistentVrThread = 78;
        static final int TRANSACTION_setSplitScreenResizing = 66;
        static final int TRANSACTION_setTaskResizeable = 47;
        static final int TRANSACTION_setVoiceKeepAwake = 82;
        static final int TRANSACTION_setVrThread = 77;
        static final int TRANSACTION_startActivities = 2;
        static final int TRANSACTION_startActivity = 1;
        static final int TRANSACTION_startActivityAndWait = 7;
        static final int TRANSACTION_startActivityAsCaller = 13;
        static final int TRANSACTION_startActivityAsUser = 3;
        static final int TRANSACTION_startActivityFromRecents = 12;
        static final int TRANSACTION_startActivityIntentSender = 6;
        static final int TRANSACTION_startActivityWithConfig = 8;
        static final int TRANSACTION_startAssistantActivity = 10;
        static final int TRANSACTION_startDreamActivity = 5;
        static final int TRANSACTION_startNextMatchingActivity = 4;
        static final int TRANSACTION_startRecentsActivity = 11;
        static final int TRANSACTION_startSystemLockTaskMode = 37;
        static final int TRANSACTION_startVoiceActivity = 9;
        static final int TRANSACTION_stopAppSwitches = 79;
        static final int TRANSACTION_stopSystemLockTaskMode = 38;
        static final int TRANSACTION_supportsLocalVoiceInteraction = 67;
        static final int TRANSACTION_suppressResizeConfigChanges = 64;
        static final int TRANSACTION_unhandledBack = 15;
        static final int TRANSACTION_unregisterTaskStackListener = 46;
        static final int TRANSACTION_updateConfiguration = 72;
        static final int TRANSACTION_updateLockTaskFeatures = 73;
        static final int TRANSACTION_updateLockTaskPackages = 33;

        public Stub() {
            attachInterface(this, IActivityTaskManager.DESCRIPTOR);
        }

        public static IActivityTaskManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IActivityTaskManager.DESCRIPTOR);
            if (iin != null && (iin instanceof IActivityTaskManager)) {
                return (IActivityTaskManager) iin;
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
                    return "startActivity";
                case 2:
                    return "startActivities";
                case 3:
                    return "startActivityAsUser";
                case 4:
                    return "startNextMatchingActivity";
                case 5:
                    return "startDreamActivity";
                case 6:
                    return "startActivityIntentSender";
                case 7:
                    return "startActivityAndWait";
                case 8:
                    return "startActivityWithConfig";
                case 9:
                    return "startVoiceActivity";
                case 10:
                    return "startAssistantActivity";
                case 11:
                    return "startRecentsActivity";
                case 12:
                    return "startActivityFromRecents";
                case 13:
                    return "startActivityAsCaller";
                case 14:
                    return "isActivityStartAllowedOnDisplay";
                case 15:
                    return "unhandledBack";
                case 16:
                    return "getActivityClientController";
                case 17:
                    return "getFrontActivityScreenCompatMode";
                case 18:
                    return "setFrontActivityScreenCompatMode";
                case 19:
                    return "setFocusedTask";
                case 20:
                    return "removeTask";
                case 21:
                    return "removeAllVisibleRecentTasks";
                case 22:
                    return "getTasks";
                case 23:
                    return "getTasksWm";
                case 24:
                    return "moveTaskToFront";
                case 25:
                    return "getRecentTasks";
                case 26:
                    return "isTopActivityImmersive";
                case 27:
                    return "getTaskDescription";
                case 28:
                    return "reportAssistContextExtras";
                case 29:
                    return "setFocusedRootTask";
                case 30:
                    return "getFocusedRootTaskInfo";
                case 31:
                    return "getTaskBounds";
                case 32:
                    return "cancelRecentsAnimation";
                case 33:
                    return "updateLockTaskPackages";
                case 34:
                    return "isInLockTaskMode";
                case 35:
                    return "getLockTaskModeState";
                case 36:
                    return "getAppTasks";
                case 37:
                    return "startSystemLockTaskMode";
                case 38:
                    return "stopSystemLockTaskMode";
                case 39:
                    return "finishVoiceTask";
                case 40:
                    return "addAppTask";
                case 41:
                    return "getAppTaskThumbnailSize";
                case 42:
                    return "requestStartActivityPermissionToken";
                case 43:
                    return "releaseSomeActivities";
                case 44:
                    return "getTaskDescriptionIcon";
                case 45:
                    return "registerTaskStackListener";
                case 46:
                    return "unregisterTaskStackListener";
                case 47:
                    return "setTaskResizeable";
                case 48:
                    return "resizeTask";
                case 49:
                    return "moveRootTaskToDisplay";
                case 50:
                    return "moveTaskToRootTask";
                case 51:
                    return "removeRootTasksInWindowingModes";
                case 52:
                    return "removeRootTasksWithActivityTypes";
                case 53:
                    return "getAllRootTaskInfos";
                case 54:
                    return "getRootTaskInfo";
                case 55:
                    return "getAllRootTaskInfosOnDisplay";
                case 56:
                    return "getRootTaskInfoOnDisplay";
                case 57:
                    return "setLockScreenShown";
                case 58:
                    return "getAssistContextExtras";
                case 59:
                    return "requestAssistContextExtras";
                case 60:
                    return "requestAutofillData";
                case 61:
                    return "isAssistDataAllowedOnCurrentActivity";
                case 62:
                    return "requestAssistDataForTask";
                case 63:
                    return "keyguardGoingAway";
                case 64:
                    return "suppressResizeConfigChanges";
                case 65:
                    return "getWindowOrganizerController";
                case 66:
                    return "setSplitScreenResizing";
                case 67:
                    return "supportsLocalVoiceInteraction";
                case 68:
                    return "getDeviceConfigurationInfo";
                case 69:
                    return "cancelTaskWindowTransition";
                case 70:
                    return "getTaskSnapshot";
                case 71:
                    return "getLastResumedActivityUserId";
                case 72:
                    return "updateConfiguration";
                case 73:
                    return "updateLockTaskFeatures";
                case 74:
                    return "registerRemoteAnimationForNextActivityStart";
                case 75:
                    return "registerRemoteAnimationsForDisplay";
                case 76:
                    return "alwaysShowUnsupportedCompileSdkWarning";
                case 77:
                    return "setVrThread";
                case 78:
                    return "setPersistentVrThread";
                case 79:
                    return "stopAppSwitches";
                case 80:
                    return "resumeAppSwitches";
                case 81:
                    return "setActivityController";
                case 82:
                    return "setVoiceKeepAwake";
                case 83:
                    return "getPackageScreenCompatMode";
                case 84:
                    return "setPackageScreenCompatMode";
                case 85:
                    return "getPackageAskScreenCompat";
                case 86:
                    return "setPackageAskScreenCompat";
                case 87:
                    return "clearLaunchParamsForPackages";
                case 88:
                    return "onSplashScreenViewCopyFinished";
                case 89:
                    return "onPictureInPictureStateChanged";
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
            Intent _arg3;
            ProfilerInfo _arg9;
            Bundle _arg10;
            Bundle _arg6;
            Intent _arg32;
            ProfilerInfo _arg92;
            Bundle _arg102;
            Intent _arg1;
            Bundle _arg2;
            Intent _arg0;
            Intent _arg33;
            Bundle _arg103;
            Intent _arg34;
            ProfilerInfo _arg93;
            Bundle _arg104;
            Intent _arg35;
            Configuration _arg94;
            Bundle _arg105;
            Intent _arg4;
            ProfilerInfo _arg95;
            Bundle _arg106;
            Intent _arg42;
            Bundle _arg62;
            Intent _arg02;
            Bundle _arg12;
            Intent _arg22;
            ProfilerInfo _arg8;
            Bundle _arg96;
            Intent _arg13;
            Bundle _arg43;
            Bundle _arg14;
            AssistStructure _arg23;
            AssistContent _arg36;
            Uri _arg44;
            Intent _arg15;
            ActivityManager.TaskDescription _arg24;
            Bitmap _arg37;
            Rect _arg16;
            Bundle _arg25;
            Bundle _arg17;
            Configuration _arg03;
            RemoteAnimationAdapter _arg18;
            RemoteAnimationDefinition _arg19;
            ComponentName _arg04;
            SplashScreenView.SplashScreenViewParcelable _arg110;
            PictureInPictureUiState _arg05;
            switch (code) {
                case IBinder.INTERFACE_TRANSACTION /* 1598968902 */:
                    reply.writeString(IActivityTaskManager.DESCRIPTOR);
                    return true;
                default:
                    IBinder iBinder = null;
                    boolean _arg11 = false;
                    switch (code) {
                        case 1:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            IApplicationThread _arg06 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            String _arg111 = data.readString();
                            String _arg26 = data.readString();
                            if (data.readInt() != 0) {
                                _arg3 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg3 = null;
                            }
                            String _arg45 = data.readString();
                            IBinder _arg5 = data.readStrongBinder();
                            String _arg63 = data.readString();
                            int _arg7 = data.readInt();
                            int _arg82 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg9 = ProfilerInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg9 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg10 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg10 = null;
                            }
                            int _result = startActivity(_arg06, _arg111, _arg26, _arg3, _arg45, _arg5, _arg63, _arg7, _arg82, _arg9, _arg10);
                            reply.writeNoException();
                            reply.writeInt(_result);
                            return true;
                        case 2:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            IApplicationThread _arg07 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            String _arg112 = data.readString();
                            String _arg27 = data.readString();
                            Intent[] _arg38 = (Intent[]) data.createTypedArray(Intent.CREATOR);
                            String[] _arg46 = data.createStringArray();
                            IBinder _arg52 = data.readStrongBinder();
                            if (data.readInt() != 0) {
                                _arg6 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg6 = null;
                            }
                            int _arg72 = data.readInt();
                            int _result2 = startActivities(_arg07, _arg112, _arg27, _arg38, _arg46, _arg52, _arg6, _arg72);
                            reply.writeNoException();
                            reply.writeInt(_result2);
                            return true;
                        case 3:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            IApplicationThread _arg08 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            String _arg113 = data.readString();
                            String _arg28 = data.readString();
                            if (data.readInt() != 0) {
                                _arg32 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg32 = null;
                            }
                            String _arg47 = data.readString();
                            IBinder _arg53 = data.readStrongBinder();
                            String _arg64 = data.readString();
                            int _arg73 = data.readInt();
                            int _arg83 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg92 = ProfilerInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg92 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg102 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg102 = null;
                            }
                            int _result3 = startActivityAsUser(_arg08, _arg113, _arg28, _arg32, _arg47, _arg53, _arg64, _arg73, _arg83, _arg92, _arg102, data.readInt());
                            reply.writeNoException();
                            reply.writeInt(_result3);
                            return true;
                        case 4:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            IBinder _arg09 = data.readStrongBinder();
                            if (data.readInt() != 0) {
                                _arg1 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg1 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg2 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg2 = null;
                            }
                            boolean startNextMatchingActivity = startNextMatchingActivity(_arg09, _arg1, _arg2);
                            reply.writeNoException();
                            reply.writeInt(startNextMatchingActivity ? 1 : 0);
                            return true;
                        case 5:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0 = null;
                            }
                            boolean startDreamActivity = startDreamActivity(_arg0);
                            reply.writeNoException();
                            reply.writeInt(startDreamActivity ? 1 : 0);
                            return true;
                        case 6:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            IApplicationThread _arg010 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            IIntentSender _arg114 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                            IBinder _arg29 = data.readStrongBinder();
                            if (data.readInt() != 0) {
                                _arg33 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg33 = null;
                            }
                            String _arg48 = data.readString();
                            IBinder _arg54 = data.readStrongBinder();
                            String _arg65 = data.readString();
                            int _arg74 = data.readInt();
                            int _arg84 = data.readInt();
                            int _arg97 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg103 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg103 = null;
                            }
                            int _result4 = startActivityIntentSender(_arg010, _arg114, _arg29, _arg33, _arg48, _arg54, _arg65, _arg74, _arg84, _arg97, _arg103);
                            reply.writeNoException();
                            reply.writeInt(_result4);
                            return true;
                        case 7:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            IApplicationThread _arg011 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            String _arg115 = data.readString();
                            String _arg210 = data.readString();
                            if (data.readInt() != 0) {
                                _arg34 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg34 = null;
                            }
                            String _arg49 = data.readString();
                            IBinder _arg55 = data.readStrongBinder();
                            String _arg66 = data.readString();
                            int _arg75 = data.readInt();
                            int _arg85 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg93 = ProfilerInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg93 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg104 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg104 = null;
                            }
                            WaitResult _result5 = startActivityAndWait(_arg011, _arg115, _arg210, _arg34, _arg49, _arg55, _arg66, _arg75, _arg85, _arg93, _arg104, data.readInt());
                            reply.writeNoException();
                            if (_result5 != null) {
                                reply.writeInt(1);
                                _result5.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 8:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            IApplicationThread _arg012 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            String _arg116 = data.readString();
                            String _arg211 = data.readString();
                            if (data.readInt() != 0) {
                                _arg35 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg35 = null;
                            }
                            String _arg410 = data.readString();
                            IBinder _arg56 = data.readStrongBinder();
                            String _arg67 = data.readString();
                            int _arg76 = data.readInt();
                            int _arg86 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg94 = Configuration.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg94 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg105 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg105 = null;
                            }
                            int _result6 = startActivityWithConfig(_arg012, _arg116, _arg211, _arg35, _arg410, _arg56, _arg67, _arg76, _arg86, _arg94, _arg105, data.readInt());
                            reply.writeNoException();
                            reply.writeInt(_result6);
                            return true;
                        case 9:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            String _arg013 = data.readString();
                            String _arg117 = data.readString();
                            int _arg212 = data.readInt();
                            int _arg39 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg4 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg4 = null;
                            }
                            String _arg57 = data.readString();
                            IVoiceInteractionSession _arg68 = IVoiceInteractionSession.Stub.asInterface(data.readStrongBinder());
                            IVoiceInteractor _arg77 = IVoiceInteractor.Stub.asInterface(data.readStrongBinder());
                            int _arg87 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg95 = ProfilerInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg95 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg106 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg106 = null;
                            }
                            int _result7 = startVoiceActivity(_arg013, _arg117, _arg212, _arg39, _arg4, _arg57, _arg68, _arg77, _arg87, _arg95, _arg106, data.readInt());
                            reply.writeNoException();
                            reply.writeInt(_result7);
                            return true;
                        case 10:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            String _arg014 = data.readString();
                            String _arg118 = data.readString();
                            int _arg213 = data.readInt();
                            int _arg310 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg42 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg42 = null;
                            }
                            String _arg58 = data.readString();
                            if (data.readInt() != 0) {
                                _arg62 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg62 = null;
                            }
                            int _arg78 = data.readInt();
                            int _result8 = startAssistantActivity(_arg014, _arg118, _arg213, _arg310, _arg42, _arg58, _arg62, _arg78);
                            reply.writeNoException();
                            reply.writeInt(_result8);
                            return true;
                        case 11:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg02 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg02 = null;
                            }
                            long _arg119 = data.readLong();
                            IRecentsAnimationRunner _arg214 = IRecentsAnimationRunner.Stub.asInterface(data.readStrongBinder());
                            startRecentsActivity(_arg02, _arg119, _arg214);
                            reply.writeNoException();
                            return true;
                        case 12:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg015 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg12 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg12 = null;
                            }
                            int _result9 = startActivityFromRecents(_arg015, _arg12);
                            reply.writeNoException();
                            reply.writeInt(_result9);
                            return true;
                        case 13:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            IApplicationThread _arg016 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            String _arg120 = data.readString();
                            if (data.readInt() != 0) {
                                _arg22 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg22 = null;
                            }
                            String _arg311 = data.readString();
                            IBinder _arg411 = data.readStrongBinder();
                            String _arg59 = data.readString();
                            int _arg69 = data.readInt();
                            int _arg79 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg8 = ProfilerInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg8 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg96 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg96 = null;
                            }
                            IBinder _arg107 = data.readStrongBinder();
                            if (data.readInt() != 0) {
                                _arg11 = true;
                            }
                            int _arg122 = data.readInt();
                            int _result10 = startActivityAsCaller(_arg016, _arg120, _arg22, _arg311, _arg411, _arg59, _arg69, _arg79, _arg8, _arg96, _arg107, _arg11, _arg122);
                            reply.writeNoException();
                            reply.writeInt(_result10);
                            return true;
                        case 14:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg017 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg13 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg13 = null;
                            }
                            String _arg215 = data.readString();
                            int _arg312 = data.readInt();
                            boolean isActivityStartAllowedOnDisplay = isActivityStartAllowedOnDisplay(_arg017, _arg13, _arg215, _arg312);
                            reply.writeNoException();
                            reply.writeInt(isActivityStartAllowedOnDisplay ? 1 : 0);
                            return true;
                        case 15:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            unhandledBack();
                            reply.writeNoException();
                            return true;
                        case 16:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            IActivityClientController _result11 = getActivityClientController();
                            reply.writeNoException();
                            if (_result11 != null) {
                                iBinder = _result11.asBinder();
                            }
                            reply.writeStrongBinder(iBinder);
                            return true;
                        case 17:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _result12 = getFrontActivityScreenCompatMode();
                            reply.writeNoException();
                            reply.writeInt(_result12);
                            return true;
                        case 18:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg018 = data.readInt();
                            setFrontActivityScreenCompatMode(_arg018);
                            reply.writeNoException();
                            return true;
                        case 19:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg019 = data.readInt();
                            setFocusedTask(_arg019);
                            reply.writeNoException();
                            return true;
                        case 20:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg020 = data.readInt();
                            boolean removeTask = removeTask(_arg020);
                            reply.writeNoException();
                            reply.writeInt(removeTask ? 1 : 0);
                            return true;
                        case 21:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            removeAllVisibleRecentTasks();
                            reply.writeNoException();
                            return true;
                        case 22:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg021 = data.readInt();
                            boolean _arg121 = data.readInt() != 0;
                            if (data.readInt() != 0) {
                                _arg11 = true;
                            }
                            boolean _arg216 = _arg11;
                            List<ActivityManager.RunningTaskInfo> _result13 = getTasks(_arg021, _arg121, _arg216);
                            reply.writeNoException();
                            reply.writeTypedList(_result13);
                            return true;
                        case 23:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg022 = data.readInt();
                            boolean _arg123 = data.readInt() != 0;
                            if (data.readInt() != 0) {
                                _arg11 = true;
                            }
                            boolean _arg217 = _arg11;
                            int _arg313 = data.readInt();
                            List<ActivityManager.RunningTaskInfo> _result14 = getTasksWm(_arg022, _arg123, _arg217, _arg313);
                            reply.writeNoException();
                            reply.writeTypedList(_result14);
                            return true;
                        case 24:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            IApplicationThread _arg023 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            String _arg124 = data.readString();
                            int _arg218 = data.readInt();
                            int _arg314 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg43 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg43 = null;
                            }
                            moveTaskToFront(_arg023, _arg124, _arg218, _arg314, _arg43);
                            reply.writeNoException();
                            return true;
                        case 25:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg024 = data.readInt();
                            int _arg125 = data.readInt();
                            int _arg219 = data.readInt();
                            ParceledListSlice<ActivityManager.RecentTaskInfo> _result15 = getRecentTasks(_arg024, _arg125, _arg219);
                            reply.writeNoException();
                            if (_result15 != null) {
                                reply.writeInt(1);
                                _result15.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 26:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            boolean isTopActivityImmersive = isTopActivityImmersive();
                            reply.writeNoException();
                            reply.writeInt(isTopActivityImmersive ? 1 : 0);
                            return true;
                        case 27:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg025 = data.readInt();
                            ActivityManager.TaskDescription _result16 = getTaskDescription(_arg025);
                            reply.writeNoException();
                            if (_result16 != null) {
                                reply.writeInt(1);
                                _result16.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 28:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            IBinder _arg026 = data.readStrongBinder();
                            if (data.readInt() != 0) {
                                _arg14 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg14 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg23 = AssistStructure.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg23 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg36 = AssistContent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg36 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg44 = Uri.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg44 = null;
                            }
                            reportAssistContextExtras(_arg026, _arg14, _arg23, _arg36, _arg44);
                            reply.writeNoException();
                            return true;
                        case 29:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg027 = data.readInt();
                            setFocusedRootTask(_arg027);
                            reply.writeNoException();
                            return true;
                        case 30:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            ActivityTaskManager.RootTaskInfo _result17 = getFocusedRootTaskInfo();
                            reply.writeNoException();
                            if (_result17 != null) {
                                reply.writeInt(1);
                                _result17.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 31:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg028 = data.readInt();
                            Rect _result18 = getTaskBounds(_arg028);
                            reply.writeNoException();
                            if (_result18 != null) {
                                reply.writeInt(1);
                                _result18.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 32:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg11 = true;
                            }
                            boolean _arg029 = _arg11;
                            cancelRecentsAnimation(_arg029);
                            reply.writeNoException();
                            return true;
                        case 33:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg030 = data.readInt();
                            String[] _arg126 = data.createStringArray();
                            updateLockTaskPackages(_arg030, _arg126);
                            reply.writeNoException();
                            return true;
                        case 34:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            boolean isInLockTaskMode = isInLockTaskMode();
                            reply.writeNoException();
                            reply.writeInt(isInLockTaskMode ? 1 : 0);
                            return true;
                        case 35:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _result19 = getLockTaskModeState();
                            reply.writeNoException();
                            reply.writeInt(_result19);
                            return true;
                        case 36:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            String _arg031 = data.readString();
                            List<IBinder> _result20 = getAppTasks(_arg031);
                            reply.writeNoException();
                            reply.writeBinderList(_result20);
                            return true;
                        case 37:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg032 = data.readInt();
                            startSystemLockTaskMode(_arg032);
                            reply.writeNoException();
                            return true;
                        case 38:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            stopSystemLockTaskMode();
                            reply.writeNoException();
                            return true;
                        case 39:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            IVoiceInteractionSession _arg033 = IVoiceInteractionSession.Stub.asInterface(data.readStrongBinder());
                            finishVoiceTask(_arg033);
                            reply.writeNoException();
                            return true;
                        case 40:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            IBinder _arg034 = data.readStrongBinder();
                            if (data.readInt() != 0) {
                                _arg15 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg15 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg24 = ActivityManager.TaskDescription.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg24 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg37 = Bitmap.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg37 = null;
                            }
                            int _result21 = addAppTask(_arg034, _arg15, _arg24, _arg37);
                            reply.writeNoException();
                            reply.writeInt(_result21);
                            return true;
                        case 41:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            Point _result22 = getAppTaskThumbnailSize();
                            reply.writeNoException();
                            if (_result22 != null) {
                                reply.writeInt(1);
                                _result22.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 42:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            IBinder _arg035 = data.readStrongBinder();
                            IBinder _result23 = requestStartActivityPermissionToken(_arg035);
                            reply.writeNoException();
                            reply.writeStrongBinder(_result23);
                            return true;
                        case 43:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            IApplicationThread _arg036 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                            releaseSomeActivities(_arg036);
                            return true;
                        case 44:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            String _arg037 = data.readString();
                            int _arg127 = data.readInt();
                            Bitmap _result24 = getTaskDescriptionIcon(_arg037, _arg127);
                            reply.writeNoException();
                            if (_result24 != null) {
                                reply.writeInt(1);
                                _result24.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 45:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            ITaskStackListener _arg038 = ITaskStackListener.Stub.asInterface(data.readStrongBinder());
                            registerTaskStackListener(_arg038);
                            reply.writeNoException();
                            return true;
                        case 46:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            ITaskStackListener _arg039 = ITaskStackListener.Stub.asInterface(data.readStrongBinder());
                            unregisterTaskStackListener(_arg039);
                            reply.writeNoException();
                            return true;
                        case 47:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg040 = data.readInt();
                            int _arg128 = data.readInt();
                            setTaskResizeable(_arg040, _arg128);
                            reply.writeNoException();
                            return true;
                        case 48:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg041 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg16 = Rect.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg16 = null;
                            }
                            int _arg220 = data.readInt();
                            boolean resizeTask = resizeTask(_arg041, _arg16, _arg220);
                            reply.writeNoException();
                            reply.writeInt(resizeTask ? 1 : 0);
                            return true;
                        case 49:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg042 = data.readInt();
                            int _arg129 = data.readInt();
                            moveRootTaskToDisplay(_arg042, _arg129);
                            reply.writeNoException();
                            return true;
                        case 50:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg043 = data.readInt();
                            int _arg130 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg11 = true;
                            }
                            boolean _arg221 = _arg11;
                            moveTaskToRootTask(_arg043, _arg130, _arg221);
                            reply.writeNoException();
                            return true;
                        case 51:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int[] _arg044 = data.createIntArray();
                            removeRootTasksInWindowingModes(_arg044);
                            reply.writeNoException();
                            return true;
                        case 52:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int[] _arg045 = data.createIntArray();
                            removeRootTasksWithActivityTypes(_arg045);
                            reply.writeNoException();
                            return true;
                        case 53:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            List<ActivityTaskManager.RootTaskInfo> _result25 = getAllRootTaskInfos();
                            reply.writeNoException();
                            reply.writeTypedList(_result25);
                            return true;
                        case 54:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg046 = data.readInt();
                            int _arg131 = data.readInt();
                            ActivityTaskManager.RootTaskInfo _result26 = getRootTaskInfo(_arg046, _arg131);
                            reply.writeNoException();
                            if (_result26 != null) {
                                reply.writeInt(1);
                                _result26.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 55:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg047 = data.readInt();
                            List<ActivityTaskManager.RootTaskInfo> _result27 = getAllRootTaskInfosOnDisplay(_arg047);
                            reply.writeNoException();
                            reply.writeTypedList(_result27);
                            return true;
                        case 56:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg048 = data.readInt();
                            int _arg132 = data.readInt();
                            int _arg222 = data.readInt();
                            ActivityTaskManager.RootTaskInfo _result28 = getRootTaskInfoOnDisplay(_arg048, _arg132, _arg222);
                            reply.writeNoException();
                            if (_result28 != null) {
                                reply.writeInt(1);
                                _result28.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 57:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            boolean _arg049 = data.readInt() != 0;
                            if (data.readInt() != 0) {
                                _arg11 = true;
                            }
                            boolean _arg133 = _arg11;
                            setLockScreenShown(_arg049, _arg133);
                            reply.writeNoException();
                            return true;
                        case 58:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg050 = data.readInt();
                            Bundle _result29 = getAssistContextExtras(_arg050);
                            reply.writeNoException();
                            if (_result29 != null) {
                                reply.writeInt(1);
                                _result29.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 59:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg051 = data.readInt();
                            IAssistDataReceiver _arg134 = IAssistDataReceiver.Stub.asInterface(data.readStrongBinder());
                            if (data.readInt() != 0) {
                                _arg25 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg25 = null;
                            }
                            IBinder _arg315 = data.readStrongBinder();
                            boolean _arg412 = data.readInt() != 0;
                            boolean _arg510 = data.readInt() != 0;
                            boolean requestAssistContextExtras = requestAssistContextExtras(_arg051, _arg134, _arg25, _arg315, _arg412, _arg510);
                            reply.writeNoException();
                            reply.writeInt(requestAssistContextExtras ? 1 : 0);
                            return true;
                        case 60:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            IAssistDataReceiver _arg052 = IAssistDataReceiver.Stub.asInterface(data.readStrongBinder());
                            if (data.readInt() != 0) {
                                _arg17 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg17 = null;
                            }
                            IBinder _arg223 = data.readStrongBinder();
                            int _arg316 = data.readInt();
                            boolean requestAutofillData = requestAutofillData(_arg052, _arg17, _arg223, _arg316);
                            reply.writeNoException();
                            reply.writeInt(requestAutofillData ? 1 : 0);
                            return true;
                        case 61:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            boolean isAssistDataAllowedOnCurrentActivity = isAssistDataAllowedOnCurrentActivity();
                            reply.writeNoException();
                            reply.writeInt(isAssistDataAllowedOnCurrentActivity ? 1 : 0);
                            return true;
                        case 62:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            IAssistDataReceiver _arg053 = IAssistDataReceiver.Stub.asInterface(data.readStrongBinder());
                            int _arg135 = data.readInt();
                            String _arg224 = data.readString();
                            boolean requestAssistDataForTask = requestAssistDataForTask(_arg053, _arg135, _arg224);
                            reply.writeNoException();
                            reply.writeInt(requestAssistDataForTask ? 1 : 0);
                            return true;
                        case 63:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg054 = data.readInt();
                            keyguardGoingAway(_arg054);
                            reply.writeNoException();
                            return true;
                        case 64:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg11 = true;
                            }
                            boolean _arg055 = _arg11;
                            suppressResizeConfigChanges(_arg055);
                            reply.writeNoException();
                            return true;
                        case 65:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            IWindowOrganizerController _result30 = getWindowOrganizerController();
                            reply.writeNoException();
                            if (_result30 != null) {
                                iBinder = _result30.asBinder();
                            }
                            reply.writeStrongBinder(iBinder);
                            return true;
                        case 66:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg11 = true;
                            }
                            boolean _arg056 = _arg11;
                            setSplitScreenResizing(_arg056);
                            reply.writeNoException();
                            return true;
                        case 67:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            boolean supportsLocalVoiceInteraction = supportsLocalVoiceInteraction();
                            reply.writeNoException();
                            reply.writeInt(supportsLocalVoiceInteraction ? 1 : 0);
                            return true;
                        case 68:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            ConfigurationInfo _result31 = getDeviceConfigurationInfo();
                            reply.writeNoException();
                            if (_result31 != null) {
                                reply.writeInt(1);
                                _result31.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 69:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg057 = data.readInt();
                            cancelTaskWindowTransition(_arg057);
                            reply.writeNoException();
                            return true;
                        case 70:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg058 = data.readInt();
                            boolean _arg136 = data.readInt() != 0;
                            TaskSnapshot _result32 = getTaskSnapshot(_arg058, _arg136);
                            reply.writeNoException();
                            if (_result32 != null) {
                                reply.writeInt(1);
                                _result32.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 71:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _result33 = getLastResumedActivityUserId();
                            reply.writeNoException();
                            reply.writeInt(_result33);
                            return true;
                        case 72:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg03 = Configuration.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg03 = null;
                            }
                            boolean updateConfiguration = updateConfiguration(_arg03);
                            reply.writeNoException();
                            reply.writeInt(updateConfiguration ? 1 : 0);
                            return true;
                        case 73:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg059 = data.readInt();
                            int _arg137 = data.readInt();
                            updateLockTaskFeatures(_arg059, _arg137);
                            reply.writeNoException();
                            return true;
                        case 74:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            String _arg060 = data.readString();
                            if (data.readInt() != 0) {
                                _arg18 = RemoteAnimationAdapter.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg18 = null;
                            }
                            registerRemoteAnimationForNextActivityStart(_arg060, _arg18);
                            reply.writeNoException();
                            return true;
                        case 75:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg061 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg19 = RemoteAnimationDefinition.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg19 = null;
                            }
                            registerRemoteAnimationsForDisplay(_arg061, _arg19);
                            reply.writeNoException();
                            return true;
                        case 76:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg04 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg04 = null;
                            }
                            alwaysShowUnsupportedCompileSdkWarning(_arg04);
                            reply.writeNoException();
                            return true;
                        case 77:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg062 = data.readInt();
                            setVrThread(_arg062);
                            reply.writeNoException();
                            return true;
                        case 78:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg063 = data.readInt();
                            setPersistentVrThread(_arg063);
                            reply.writeNoException();
                            return true;
                        case 79:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            stopAppSwitches();
                            reply.writeNoException();
                            return true;
                        case 80:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            resumeAppSwitches();
                            reply.writeNoException();
                            return true;
                        case 81:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            IActivityController _arg064 = IActivityController.Stub.asInterface(data.readStrongBinder());
                            if (data.readInt() != 0) {
                                _arg11 = true;
                            }
                            boolean _arg138 = _arg11;
                            setActivityController(_arg064, _arg138);
                            reply.writeNoException();
                            return true;
                        case 82:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            IVoiceInteractionSession _arg065 = IVoiceInteractionSession.Stub.asInterface(data.readStrongBinder());
                            if (data.readInt() != 0) {
                                _arg11 = true;
                            }
                            boolean _arg139 = _arg11;
                            setVoiceKeepAwake(_arg065, _arg139);
                            reply.writeNoException();
                            return true;
                        case 83:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            String _arg066 = data.readString();
                            int _result34 = getPackageScreenCompatMode(_arg066);
                            reply.writeNoException();
                            reply.writeInt(_result34);
                            return true;
                        case 84:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            String _arg067 = data.readString();
                            int _arg140 = data.readInt();
                            setPackageScreenCompatMode(_arg067, _arg140);
                            reply.writeNoException();
                            return true;
                        case 85:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            String _arg068 = data.readString();
                            boolean packageAskScreenCompat = getPackageAskScreenCompat(_arg068);
                            reply.writeNoException();
                            reply.writeInt(packageAskScreenCompat ? 1 : 0);
                            return true;
                        case 86:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            String _arg069 = data.readString();
                            if (data.readInt() != 0) {
                                _arg11 = true;
                            }
                            boolean _arg141 = _arg11;
                            setPackageAskScreenCompat(_arg069, _arg141);
                            reply.writeNoException();
                            return true;
                        case 87:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            List<String> _arg070 = data.createStringArrayList();
                            clearLaunchParamsForPackages(_arg070);
                            reply.writeNoException();
                            return true;
                        case 88:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            int _arg071 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg110 = SplashScreenView.SplashScreenViewParcelable.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg110 = null;
                            }
                            onSplashScreenViewCopyFinished(_arg071, _arg110);
                            reply.writeNoException();
                            return true;
                        case 89:
                            data.enforceInterface(IActivityTaskManager.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg05 = PictureInPictureUiState.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg05 = null;
                            }
                            onPictureInPictureStateChanged(_arg05);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IActivityTaskManager {
            public static IActivityTaskManager sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IActivityTaskManager.DESCRIPTOR;
            }

            @Override // android.app.IActivityTaskManager
            public int startActivity(IApplicationThread caller, String callingPackage, String callingFeatureId, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options) throws RemoteException {
                Parcel _reply;
                Parcel _data;
                IBinder asBinder;
                Parcel _reply2;
                Parcel _data2;
                Parcel _data3 = Parcel.obtain();
                Parcel _reply3 = Parcel.obtain();
                try {
                    _data3.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
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
                    boolean _status = this.mRemote.transact(1, _data3, _reply3, 0);
                    try {
                        if (_status) {
                            _reply2 = _reply3;
                            _data2 = _data3;
                        } else if (Stub.getDefaultImpl() != null) {
                            int startActivity = Stub.getDefaultImpl().startActivity(caller, callingPackage, callingFeatureId, intent, resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options);
                            _reply3.recycle();
                            _data3.recycle();
                            return startActivity;
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

            @Override // android.app.IActivityTaskManager
            public int startActivities(IApplicationThread caller, String callingPackage, String callingFeatureId, Intent[] intents, String[] resolvedTypes, IBinder resultTo, Bundle options, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    try {
                        _data.writeString(callingPackage);
                        try {
                            _data.writeString(callingFeatureId);
                            try {
                                _data.writeTypedArray(intents, 0);
                                _data.writeStringArray(resolvedTypes);
                                _data.writeStrongBinder(resultTo);
                                if (options != null) {
                                    _data.writeInt(1);
                                    options.writeToParcel(_data, 0);
                                } else {
                                    _data.writeInt(0);
                                }
                                _data.writeInt(userId);
                                boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                                if (!_status && Stub.getDefaultImpl() != null) {
                                    int startActivities = Stub.getDefaultImpl().startActivities(caller, callingPackage, callingFeatureId, intents, resolvedTypes, resultTo, options, userId);
                                    _reply.recycle();
                                    _data.recycle();
                                    return startActivities;
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
                }
            }

            @Override // android.app.IActivityTaskManager
            public int startActivityAsUser(IApplicationThread caller, String callingPackage, String callingFeatureId, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
                Parcel _reply;
                Parcel _data;
                IBinder asBinder;
                Parcel _reply2;
                Parcel _data2;
                Parcel _data3 = Parcel.obtain();
                Parcel _reply3 = Parcel.obtain();
                try {
                    _data3.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
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
                    boolean _status = this.mRemote.transact(3, _data3, _reply3, 0);
                    try {
                        if (_status) {
                            _reply2 = _reply3;
                            _data2 = _data3;
                        } else if (Stub.getDefaultImpl() != null) {
                            int startActivityAsUser = Stub.getDefaultImpl().startActivityAsUser(caller, callingPackage, callingFeatureId, intent, resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options, userId);
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

            @Override // android.app.IActivityTaskManager
            public boolean startNextMatchingActivity(IBinder callingActivity, Intent intent, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongBinder(callingActivity);
                    boolean _result = true;
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
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startNextMatchingActivity(callingActivity, intent, options);
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

            @Override // android.app.IActivityTaskManager
            public boolean startDreamActivity(Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    boolean _result = true;
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startDreamActivity(intent);
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

            @Override // android.app.IActivityTaskManager
            public int startActivityIntentSender(IApplicationThread caller, IIntentSender target, IBinder whitelistToken, Intent fillInIntent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flagsMask, int flagsValues, Bundle options) throws RemoteException {
                Parcel _reply;
                IBinder asBinder;
                Parcel _reply2;
                Parcel _data = Parcel.obtain();
                Parcel _reply3 = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    IBinder iBinder = null;
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
                    if (target != null) {
                        iBinder = target.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    _data.writeStrongBinder(whitelistToken);
                    if (fillInIntent != null) {
                        _data.writeInt(1);
                        fillInIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeStrongBinder(resultTo);
                    _data.writeString(resultWho);
                    _data.writeInt(requestCode);
                    _data.writeInt(flagsMask);
                    _data.writeInt(flagsValues);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(6, _data, _reply3, 0);
                    try {
                        if (_status) {
                            _reply2 = _reply3;
                        } else if (Stub.getDefaultImpl() != null) {
                            int startActivityIntentSender = Stub.getDefaultImpl().startActivityIntentSender(caller, target, whitelistToken, fillInIntent, resolvedType, resultTo, resultWho, requestCode, flagsMask, flagsValues, options);
                            _reply3.recycle();
                            _data.recycle();
                            return startActivityIntentSender;
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

            @Override // android.app.IActivityTaskManager
            public WaitResult startActivityAndWait(IApplicationThread caller, String callingPackage, String callingFeatureId, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
                Parcel _reply;
                Parcel _data;
                IBinder asBinder;
                Parcel _reply2;
                WaitResult _result;
                Parcel _data2 = Parcel.obtain();
                Parcel _reply3 = Parcel.obtain();
                try {
                    _data2.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    if (caller != null) {
                        try {
                            asBinder = caller.asBinder();
                        } catch (Throwable th) {
                            th = th;
                            _reply = _reply3;
                            _data = _data2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } else {
                        asBinder = null;
                    }
                    _data2.writeStrongBinder(asBinder);
                    _data2.writeString(callingPackage);
                    _data2.writeString(callingFeatureId);
                    if (intent != null) {
                        _data2.writeInt(1);
                        intent.writeToParcel(_data2, 0);
                    } else {
                        _data2.writeInt(0);
                    }
                    _data2.writeString(resolvedType);
                    _data2.writeStrongBinder(resultTo);
                    _data2.writeString(resultWho);
                    _data2.writeInt(requestCode);
                    _data2.writeInt(flags);
                    if (profilerInfo != null) {
                        _data2.writeInt(1);
                        profilerInfo.writeToParcel(_data2, 0);
                    } else {
                        _data2.writeInt(0);
                    }
                    if (options != null) {
                        _data2.writeInt(1);
                        options.writeToParcel(_data2, 0);
                    } else {
                        _data2.writeInt(0);
                    }
                    _data2.writeInt(userId);
                    boolean _status = this.mRemote.transact(7, _data2, _reply3, 0);
                    if (!_status) {
                        try {
                            if (Stub.getDefaultImpl() != null) {
                                _data = _data2;
                                try {
                                    WaitResult startActivityAndWait = Stub.getDefaultImpl().startActivityAndWait(caller, callingPackage, callingFeatureId, intent, resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options, userId);
                                    _reply3.recycle();
                                    _data.recycle();
                                    return startActivityAndWait;
                                } catch (Throwable th2) {
                                    th = th2;
                                    _reply = _reply3;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            }
                            _reply2 = _reply3;
                            _data = _data2;
                        } catch (Throwable th3) {
                            th = th3;
                            _data = _data2;
                            _reply = _reply3;
                        }
                    } else {
                        _reply2 = _reply3;
                        _data = _data2;
                    }
                    try {
                        _reply2.readException();
                        if (_reply2.readInt() != 0) {
                            _reply = _reply2;
                            try {
                                _result = WaitResult.CREATOR.mo3559createFromParcel(_reply);
                            } catch (Throwable th4) {
                                th = th4;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } else {
                            _reply = _reply2;
                            _result = null;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th5) {
                        th = th5;
                        _reply = _reply2;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    _reply = _reply3;
                    _data = _data2;
                }
            }

            @Override // android.app.IActivityTaskManager
            public int startActivityWithConfig(IApplicationThread caller, String callingPackage, String callingFeatureId, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int startFlags, Configuration newConfig, Bundle options, int userId) throws RemoteException {
                Parcel _reply;
                Parcel _data;
                IBinder asBinder;
                Parcel _reply2;
                Parcel _data2;
                Parcel _data3 = Parcel.obtain();
                Parcel _reply3 = Parcel.obtain();
                try {
                    _data3.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
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
                    _data3.writeInt(startFlags);
                    if (newConfig != null) {
                        _data3.writeInt(1);
                        newConfig.writeToParcel(_data3, 0);
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
                    boolean _status = this.mRemote.transact(8, _data3, _reply3, 0);
                    try {
                        if (_status) {
                            _reply2 = _reply3;
                            _data2 = _data3;
                        } else if (Stub.getDefaultImpl() != null) {
                            int startActivityWithConfig = Stub.getDefaultImpl().startActivityWithConfig(caller, callingPackage, callingFeatureId, intent, resolvedType, resultTo, resultWho, requestCode, startFlags, newConfig, options, userId);
                            _reply3.recycle();
                            _data3.recycle();
                            return startActivityWithConfig;
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

            @Override // android.app.IActivityTaskManager
            public int startVoiceActivity(String callingPackage, String callingFeatureId, int callingPid, int callingUid, Intent intent, String resolvedType, IVoiceInteractionSession session, IVoiceInteractor interactor, int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
                Parcel _reply;
                Parcel _data;
                boolean _status;
                Parcel _reply2;
                Parcel _data2;
                Parcel _data3 = Parcel.obtain();
                Parcel _reply3 = Parcel.obtain();
                try {
                    _data3.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data3.writeString(callingPackage);
                    _data3.writeString(callingFeatureId);
                    _data3.writeInt(callingPid);
                    _data3.writeInt(callingUid);
                    if (intent != null) {
                        try {
                            _data3.writeInt(1);
                            intent.writeToParcel(_data3, 0);
                        } catch (Throwable th) {
                            th = th;
                            _reply = _reply3;
                            _data = _data3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } else {
                        _data3.writeInt(0);
                    }
                    _data3.writeString(resolvedType);
                    IBinder iBinder = null;
                    _data3.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (interactor != null) {
                        iBinder = interactor.asBinder();
                    }
                    _data3.writeStrongBinder(iBinder);
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
                    _status = this.mRemote.transact(9, _data3, _reply3, 0);
                } catch (Throwable th2) {
                    th = th2;
                    _reply = _reply3;
                    _data = _data3;
                }
                try {
                    if (_status) {
                        _reply2 = _reply3;
                        _data2 = _data3;
                    } else if (Stub.getDefaultImpl() != null) {
                        int startVoiceActivity = Stub.getDefaultImpl().startVoiceActivity(callingPackage, callingFeatureId, callingPid, callingUid, intent, resolvedType, session, interactor, flags, profilerInfo, options, userId);
                        _reply3.recycle();
                        _data3.recycle();
                        return startVoiceActivity;
                    } else {
                        _reply2 = _reply3;
                        _data2 = _data3;
                    }
                    _reply2.readException();
                    int _result = _reply2.readInt();
                    _reply2.recycle();
                    _data2.recycle();
                    return _result;
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityTaskManager
            public int startAssistantActivity(String callingPackage, String callingFeatureId, int callingPid, int callingUid, Intent intent, String resolvedType, Bundle options, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    try {
                        _data.writeString(callingPackage);
                        try {
                            _data.writeString(callingFeatureId);
                            _data.writeInt(callingPid);
                            _data.writeInt(callingUid);
                            if (intent != null) {
                                _data.writeInt(1);
                                intent.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            _data.writeString(resolvedType);
                            if (options != null) {
                                _data.writeInt(1);
                                options.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            _data.writeInt(userId);
                            boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                            if (!_status && Stub.getDefaultImpl() != null) {
                                int startAssistantActivity = Stub.getDefaultImpl().startAssistantActivity(callingPackage, callingFeatureId, callingPid, callingUid, intent, resolvedType, options, userId);
                                _reply.recycle();
                                _data.recycle();
                                return startAssistantActivity;
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

            @Override // android.app.IActivityTaskManager
            public void startRecentsActivity(Intent intent, long eventTime, IRecentsAnimationRunner recentsAnimationRunner) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(eventTime);
                    _data.writeStrongBinder(recentsAnimationRunner != null ? recentsAnimationRunner.asBinder() : null);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startRecentsActivity(intent, eventTime, recentsAnimationRunner);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public int startActivityFromRecents(int taskId, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(taskId);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public int startActivityAsCaller(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options, IBinder permissionToken, boolean ignoreTargetSecurity, int userId) throws RemoteException {
                Parcel _reply;
                Parcel _data;
                IBinder asBinder;
                Parcel _reply2;
                Parcel _data2;
                Parcel _data3 = Parcel.obtain();
                Parcel _reply3 = Parcel.obtain();
                try {
                    _data3.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
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
                    int i = 1;
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
                    _data3.writeStrongBinder(permissionToken);
                    if (!ignoreTargetSecurity) {
                        i = 0;
                    }
                    _data3.writeInt(i);
                    _data3.writeInt(userId);
                    boolean _status = this.mRemote.transact(13, _data3, _reply3, 0);
                    try {
                        if (_status) {
                            _reply2 = _reply3;
                            _data2 = _data3;
                        } else if (Stub.getDefaultImpl() != null) {
                            int startActivityAsCaller = Stub.getDefaultImpl().startActivityAsCaller(caller, callingPackage, intent, resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options, permissionToken, ignoreTargetSecurity, userId);
                            _reply3.recycle();
                            _data3.recycle();
                            return startActivityAsCaller;
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

            @Override // android.app.IActivityTaskManager
            public boolean isActivityStartAllowedOnDisplay(int displayId, Intent intent, String resolvedType, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(displayId);
                    boolean _result = true;
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isActivityStartAllowedOnDisplay(displayId, intent, resolvedType, userId);
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

            @Override // android.app.IActivityTaskManager
            public void unhandledBack() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public IActivityClientController getActivityClientController() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getActivityClientController();
                    }
                    _reply.readException();
                    IActivityClientController _result = IActivityClientController.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public int getFrontActivityScreenCompatMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getFrontActivityScreenCompatMode();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void setFrontActivityScreenCompatMode(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setFrontActivityScreenCompatMode(mode);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void setFocusedTask(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setFocusedTask(taskId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public boolean removeTask(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void removeAllVisibleRecentTasks() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeAllVisibleRecentTasks();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public List<ActivityManager.RunningTaskInfo> getTasks(int maxNum, boolean filterOnlyVisibleRecents, boolean keepIntentExtra) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(maxNum);
                    int i = 1;
                    _data.writeInt(filterOnlyVisibleRecents ? 1 : 0);
                    if (!keepIntentExtra) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getTasks(maxNum, filterOnlyVisibleRecents, keepIntentExtra);
                    }
                    _reply.readException();
                    List<ActivityManager.RunningTaskInfo> _result = _reply.createTypedArrayList(ActivityManager.RunningTaskInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public List<ActivityManager.RunningTaskInfo> getTasksWm(int maxNum, boolean filterOnlyVisibleRecents, boolean keepIntentExtra, int ignoreWindowingMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(maxNum);
                    int i = 1;
                    _data.writeInt(filterOnlyVisibleRecents ? 1 : 0);
                    if (!keepIntentExtra) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(ignoreWindowingMode);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getTasksWm(maxNum, filterOnlyVisibleRecents, keepIntentExtra, ignoreWindowingMode);
                    }
                    _reply.readException();
                    List<ActivityManager.RunningTaskInfo> _result = _reply.createTypedArrayList(ActivityManager.RunningTaskInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void moveTaskToFront(IApplicationThread app, String callingPackage, int task, int flags, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongBinder(app != null ? app.asBinder() : null);
                    _data.writeString(callingPackage);
                    _data.writeInt(task);
                    _data.writeInt(flags);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(24, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().moveTaskToFront(app, callingPackage, task, flags, options);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public ParceledListSlice<ActivityManager.RecentTaskInfo> getRecentTasks(int maxNum, int flags, int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(maxNum);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(25, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public boolean isTopActivityImmersive() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(26, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public ActivityManager.TaskDescription getTaskDescription(int taskId) throws RemoteException {
                ActivityManager.TaskDescription _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean _status = this.mRemote.transact(27, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getTaskDescription(taskId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ActivityManager.TaskDescription.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void reportAssistContextExtras(IBinder assistToken, Bundle extras, AssistStructure structure, AssistContent content, Uri referrer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongBinder(assistToken);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (structure != null) {
                        _data.writeInt(1);
                        structure.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (content != null) {
                        _data.writeInt(1);
                        content.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (referrer != null) {
                        _data.writeInt(1);
                        referrer.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(28, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reportAssistContextExtras(assistToken, extras, structure, content, referrer);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void setFocusedRootTask(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean _status = this.mRemote.transact(29, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public ActivityTaskManager.RootTaskInfo getFocusedRootTaskInfo() throws RemoteException {
                ActivityTaskManager.RootTaskInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(30, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public Rect getTaskBounds(int taskId) throws RemoteException {
                Rect _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean _status = this.mRemote.transact(31, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void cancelRecentsAnimation(boolean restoreHomeRootTaskPosition) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(restoreHomeRootTaskPosition ? 1 : 0);
                    boolean _status = this.mRemote.transact(32, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cancelRecentsAnimation(restoreHomeRootTaskPosition);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void updateLockTaskPackages(int userId, String[] packages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeStringArray(packages);
                    boolean _status = this.mRemote.transact(33, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public boolean isInLockTaskMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(34, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public int getLockTaskModeState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(35, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public List<IBinder> getAppTasks(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(36, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAppTasks(callingPackage);
                    }
                    _reply.readException();
                    List<IBinder> _result = _reply.createBinderArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void startSystemLockTaskMode(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean _status = this.mRemote.transact(37, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void stopSystemLockTaskMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(38, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().stopSystemLockTaskMode();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void finishVoiceTask(IVoiceInteractionSession session) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    boolean _status = this.mRemote.transact(39, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().finishVoiceTask(session);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public int addAppTask(IBinder activityToken, Intent intent, ActivityManager.TaskDescription description, Bitmap thumbnail) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (description != null) {
                        _data.writeInt(1);
                        description.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (thumbnail != null) {
                        _data.writeInt(1);
                        thumbnail.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(40, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().addAppTask(activityToken, intent, description, thumbnail);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public Point getAppTaskThumbnailSize() throws RemoteException {
                Point _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(41, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAppTaskThumbnailSize();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Point.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public IBinder requestStartActivityPermissionToken(IBinder delegatorToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongBinder(delegatorToken);
                    boolean _status = this.mRemote.transact(42, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().requestStartActivityPermissionToken(delegatorToken);
                    }
                    _reply.readException();
                    IBinder _result = _reply.readStrongBinder();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void releaseSomeActivities(IApplicationThread app) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongBinder(app != null ? app.asBinder() : null);
                    boolean _status = this.mRemote.transact(43, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().releaseSomeActivities(app);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public Bitmap getTaskDescriptionIcon(String filename, int userId) throws RemoteException {
                Bitmap _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeString(filename);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(44, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getTaskDescriptionIcon(filename, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Bitmap.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void registerTaskStackListener(ITaskStackListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(45, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void unregisterTaskStackListener(ITaskStackListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(46, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void setTaskResizeable(int taskId, int resizeableMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(resizeableMode);
                    boolean _status = this.mRemote.transact(47, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public boolean resizeTask(int taskId, Rect bounds, int resizeMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean _result = true;
                    if (bounds != null) {
                        _data.writeInt(1);
                        bounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(resizeMode);
                    boolean _status = this.mRemote.transact(48, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().resizeTask(taskId, bounds, resizeMode);
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

            @Override // android.app.IActivityTaskManager
            public void moveRootTaskToDisplay(int taskId, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(displayId);
                    boolean _status = this.mRemote.transact(49, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().moveRootTaskToDisplay(taskId, displayId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void moveTaskToRootTask(int taskId, int rootTaskId, boolean toTop) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(rootTaskId);
                    _data.writeInt(toTop ? 1 : 0);
                    boolean _status = this.mRemote.transact(50, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void removeRootTasksInWindowingModes(int[] windowingModes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeIntArray(windowingModes);
                    boolean _status = this.mRemote.transact(51, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeRootTasksInWindowingModes(windowingModes);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void removeRootTasksWithActivityTypes(int[] activityTypes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeIntArray(activityTypes);
                    boolean _status = this.mRemote.transact(52, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeRootTasksWithActivityTypes(activityTypes);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public List<ActivityTaskManager.RootTaskInfo> getAllRootTaskInfos() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(53, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public ActivityTaskManager.RootTaskInfo getRootTaskInfo(int windowingMode, int activityType) throws RemoteException {
                ActivityTaskManager.RootTaskInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(windowingMode);
                    _data.writeInt(activityType);
                    boolean _status = this.mRemote.transact(54, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRootTaskInfo(windowingMode, activityType);
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

            @Override // android.app.IActivityTaskManager
            public List<ActivityTaskManager.RootTaskInfo> getAllRootTaskInfosOnDisplay(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(displayId);
                    boolean _status = this.mRemote.transact(55, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAllRootTaskInfosOnDisplay(displayId);
                    }
                    _reply.readException();
                    List<ActivityTaskManager.RootTaskInfo> _result = _reply.createTypedArrayList(ActivityTaskManager.RootTaskInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public ActivityTaskManager.RootTaskInfo getRootTaskInfoOnDisplay(int windowingMode, int activityType, int displayId) throws RemoteException {
                ActivityTaskManager.RootTaskInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(windowingMode);
                    _data.writeInt(activityType);
                    _data.writeInt(displayId);
                    boolean _status = this.mRemote.transact(56, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRootTaskInfoOnDisplay(windowingMode, activityType, displayId);
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

            @Override // android.app.IActivityTaskManager
            public void setLockScreenShown(boolean showingKeyguard, boolean showingAod) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    int i = 1;
                    _data.writeInt(showingKeyguard ? 1 : 0);
                    if (!showingAod) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(57, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setLockScreenShown(showingKeyguard, showingAod);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public Bundle getAssistContextExtras(int requestType) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(requestType);
                    boolean _status = this.mRemote.transact(58, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAssistContextExtras(requestType);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Bundle.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public boolean requestAssistContextExtras(int requestType, IAssistDataReceiver receiver, Bundle receiverExtras, IBinder activityToken, boolean focused, boolean newSessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    try {
                        _data.writeInt(requestType);
                        _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                        boolean _result = true;
                        if (receiverExtras != null) {
                            _data.writeInt(1);
                            receiverExtras.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeStrongBinder(activityToken);
                            _data.writeInt(focused ? 1 : 0);
                            _data.writeInt(newSessionId ? 1 : 0);
                            try {
                                boolean _status = this.mRemote.transact(59, _data, _reply, 0);
                                if (!_status && Stub.getDefaultImpl() != null) {
                                    boolean requestAssistContextExtras = Stub.getDefaultImpl().requestAssistContextExtras(requestType, receiver, receiverExtras, activityToken, focused, newSessionId);
                                    _reply.recycle();
                                    _data.recycle();
                                    return requestAssistContextExtras;
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
                }
            }

            @Override // android.app.IActivityTaskManager
            public boolean requestAutofillData(IAssistDataReceiver receiver, Bundle receiverExtras, IBinder activityToken, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    boolean _result = true;
                    if (receiverExtras != null) {
                        _data.writeInt(1);
                        receiverExtras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(activityToken);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(60, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().requestAutofillData(receiver, receiverExtras, activityToken, flags);
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

            @Override // android.app.IActivityTaskManager
            public boolean isAssistDataAllowedOnCurrentActivity() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(61, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isAssistDataAllowedOnCurrentActivity();
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

            @Override // android.app.IActivityTaskManager
            public boolean requestAssistDataForTask(IAssistDataReceiver receiver, int taskId, String callingPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    _data.writeInt(taskId);
                    _data.writeString(callingPackageName);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(62, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().requestAssistDataForTask(receiver, taskId, callingPackageName);
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

            @Override // android.app.IActivityTaskManager
            public void keyguardGoingAway(int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(63, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().keyguardGoingAway(flags);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void suppressResizeConfigChanges(boolean suppress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(suppress ? 1 : 0);
                    boolean _status = this.mRemote.transact(64, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public IWindowOrganizerController getWindowOrganizerController() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(65, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getWindowOrganizerController();
                    }
                    _reply.readException();
                    IWindowOrganizerController _result = IWindowOrganizerController.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void setSplitScreenResizing(boolean resizing) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(resizing ? 1 : 0);
                    boolean _status = this.mRemote.transact(66, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSplitScreenResizing(resizing);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public boolean supportsLocalVoiceInteraction() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(67, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().supportsLocalVoiceInteraction();
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

            @Override // android.app.IActivityTaskManager
            public ConfigurationInfo getDeviceConfigurationInfo() throws RemoteException {
                ConfigurationInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(68, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDeviceConfigurationInfo();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ConfigurationInfo.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void cancelTaskWindowTransition(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean _status = this.mRemote.transact(69, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public TaskSnapshot getTaskSnapshot(int taskId, boolean isLowResolution) throws RemoteException {
                TaskSnapshot _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(isLowResolution ? 1 : 0);
                    boolean _status = this.mRemote.transact(70, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getTaskSnapshot(taskId, isLowResolution);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = TaskSnapshot.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public int getLastResumedActivityUserId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(71, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLastResumedActivityUserId();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public boolean updateConfiguration(Configuration values) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    boolean _result = true;
                    if (values != null) {
                        _data.writeInt(1);
                        values.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(72, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void updateLockTaskFeatures(int userId, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(73, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateLockTaskFeatures(userId, flags);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void registerRemoteAnimationForNextActivityStart(String packageName, RemoteAnimationAdapter adapter) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (adapter != null) {
                        _data.writeInt(1);
                        adapter.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(74, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerRemoteAnimationForNextActivityStart(packageName, adapter);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void registerRemoteAnimationsForDisplay(int displayId, RemoteAnimationDefinition definition) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(displayId);
                    if (definition != null) {
                        _data.writeInt(1);
                        definition.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(75, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerRemoteAnimationsForDisplay(displayId, definition);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void alwaysShowUnsupportedCompileSdkWarning(ComponentName activity) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    if (activity != null) {
                        _data.writeInt(1);
                        activity.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(76, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().alwaysShowUnsupportedCompileSdkWarning(activity);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void setVrThread(int tid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(tid);
                    boolean _status = this.mRemote.transact(77, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVrThread(tid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void setPersistentVrThread(int tid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(tid);
                    boolean _status = this.mRemote.transact(78, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void stopAppSwitches() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(79, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void resumeAppSwitches() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(80, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void setActivityController(IActivityController watcher, boolean imAMonkey) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongBinder(watcher != null ? watcher.asBinder() : null);
                    _data.writeInt(imAMonkey ? 1 : 0);
                    boolean _status = this.mRemote.transact(81, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void setVoiceKeepAwake(IVoiceInteractionSession session, boolean keepAwake) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    _data.writeInt(keepAwake ? 1 : 0);
                    boolean _status = this.mRemote.transact(82, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVoiceKeepAwake(session, keepAwake);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public int getPackageScreenCompatMode(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(83, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPackageScreenCompatMode(packageName);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void setPackageScreenCompatMode(String packageName, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(84, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public boolean getPackageAskScreenCompat(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(85, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPackageAskScreenCompat(packageName);
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

            @Override // android.app.IActivityTaskManager
            public void setPackageAskScreenCompat(String packageName, boolean ask) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(ask ? 1 : 0);
                    boolean _status = this.mRemote.transact(86, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPackageAskScreenCompat(packageName, ask);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void clearLaunchParamsForPackages(List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeStringList(packageNames);
                    boolean _status = this.mRemote.transact(87, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().clearLaunchParamsForPackages(packageNames);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void onSplashScreenViewCopyFinished(int taskId, SplashScreenView.SplashScreenViewParcelable material) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    _data.writeInt(taskId);
                    if (material != null) {
                        _data.writeInt(1);
                        material.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(88, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onSplashScreenViewCopyFinished(taskId, material);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void onPictureInPictureStateChanged(PictureInPictureUiState pipState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IActivityTaskManager.DESCRIPTOR);
                    if (pipState != null) {
                        _data.writeInt(1);
                        pipState.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(89, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onPictureInPictureStateChanged(pipState);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IActivityTaskManager impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IActivityTaskManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
