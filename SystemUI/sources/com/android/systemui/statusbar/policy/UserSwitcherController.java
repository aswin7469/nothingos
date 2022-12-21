package com.android.systemui.statusbar.policy;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.IActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.UserInfo;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.telephony.TelephonyCallback;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.WindowManagerGlobal;
import android.widget.BaseAdapter;
import android.widget.Toast;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.util.LatencyTracker;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.users.UserCreatingDialog;
import com.android.settingslib.utils.ThreadUtils;
import com.android.systemui.C1893R;
import com.android.systemui.Dumpable;
import com.android.systemui.GuestResumeSessionReceiver;
import com.android.systemui.SystemUISecondaryUserService;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.LongRunning;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.p012qs.QSUserSwitcherEvent;
import com.android.systemui.p012qs.user.UserSwitchDialogController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.user.CreateUserActivity;
import com.android.systemui.util.settings.GlobalSettings;
import com.android.systemui.util.settings.SecureSettings;
import java.lang.ref.WeakReference;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import javax.inject.Inject;

@SysUISingleton
public class UserSwitcherController implements Dumpable {
    private static final boolean DEBUG = false;
    private static final long MULTI_USER_JOURNEY_TIMEOUT = 20000;
    private static final int PAUSE_REFRESH_USERS_TIMEOUT_MS = 3000;
    private static final String PERMISSION_SELF = "com.android.systemui.permission.SELF";
    private static final String SIMPLE_USER_SWITCHER_GLOBAL_SETTING = "lockscreenSimpleUserSwitcher";
    private static final String TAG = "UserSwitcherController";
    public static final float USER_SWITCH_DISABLED_ALPHA = 0.38f;
    public static final float USER_SWITCH_ENABLED_ALPHA = 1.0f;
    private final IActivityManager mActivityManager;
    private final ActivityStarter mActivityStarter;
    private final ArrayList<WeakReference<BaseUserAdapter>> mAdapters = new ArrayList<>();
    Dialog mAddUserDialog;
    /* access modifiers changed from: private */
    public boolean mAddUsersFromLockScreen;
    /* access modifiers changed from: private */
    public final Executor mBgExecutor;
    private final BroadcastDispatcher mBroadcastDispatcher;
    /* access modifiers changed from: private */
    public final BroadcastSender mBroadcastSender;
    private final KeyguardStateController.Callback mCallback;
    protected final Context mContext;
    private String mCreateSupervisedUserPackage;
    private final DevicePolicyManager mDevicePolicyManager;
    /* access modifiers changed from: private */
    public final DeviceProvisionedController mDeviceProvisionedController;
    /* access modifiers changed from: private */
    public final DialogLaunchAnimator mDialogLaunchAnimator;
    AlertDialog mExitGuestDialog;
    /* access modifiers changed from: private */
    public FalsingManager mFalsingManager;
    private SparseBooleanArray mForcePictureLoadForUserId = new SparseBooleanArray(2);
    /* access modifiers changed from: private */
    public GlobalSettings mGlobalSettings;
    /* access modifiers changed from: private */
    public final DeviceProvisionedController.DeviceProvisionedListener mGuaranteeGuestPresentAfterProvisioned;
    private final AtomicBoolean mGuestCreationScheduled;
    /* access modifiers changed from: private */
    public final AtomicBoolean mGuestIsResetting;
    final GuestResumeSessionReceiver mGuestResumeSessionReceiver;
    /* access modifiers changed from: private */
    public final boolean mGuestUserAutoCreated;
    protected final Handler mHandler;
    private final InteractionJankMonitor mInteractionJankMonitor;
    /* access modifiers changed from: private */
    public final KeyguardStateController mKeyguardStateController;
    /* access modifiers changed from: private */
    public int mLastNonGuestUser = 0;
    private final LatencyTracker mLatencyTracker;
    private final Executor mLongRunningExecutor;
    boolean mPauseRefreshUsers;
    private final TelephonyCallback.CallStateListener mPhoneStateListener = new TelephonyCallback.CallStateListener() {
        private int mCallState;

        public void onCallStateChanged(int i) {
            if (this.mCallState != i) {
                this.mCallState = i;
                UserSwitcherController.this.refreshUsers(-10000);
            }
        }
    };
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean z = false;
            int i = -10000;
            if ("android.intent.action.USER_SWITCHED".equals(intent.getAction())) {
                if (UserSwitcherController.this.mExitGuestDialog != null && UserSwitcherController.this.mExitGuestDialog.isShowing()) {
                    UserSwitcherController.this.mExitGuestDialog.cancel();
                    UserSwitcherController.this.mExitGuestDialog = null;
                }
                int intExtra = intent.getIntExtra("android.intent.extra.user_handle", -1);
                UserInfo userInfo = UserSwitcherController.this.mUserManager.getUserInfo(intExtra);
                int size = UserSwitcherController.this.mUsers.size();
                int i2 = 0;
                while (i2 < size) {
                    UserRecord userRecord = (UserRecord) UserSwitcherController.this.mUsers.get(i2);
                    if (userRecord.info != null) {
                        boolean z2 = userRecord.info.id == intExtra;
                        if (userRecord.isCurrent != z2) {
                            UserSwitcherController.this.mUsers.set(i2, userRecord.copyWithIsCurrent(z2));
                        }
                        if (z2 && !userRecord.isGuest) {
                            int unused = UserSwitcherController.this.mLastNonGuestUser = userRecord.info.id;
                        }
                        if ((userInfo == null || !userInfo.isAdmin()) && userRecord.isRestricted) {
                            UserSwitcherController.this.mUsers.remove(i2);
                            i2--;
                        }
                    }
                    i2++;
                }
                UserSwitcherController.this.notifyUserSwitchCallbacks();
                UserSwitcherController.this.notifyAdapters();
                if (UserSwitcherController.this.mSecondaryUser != -10000) {
                    context.stopServiceAsUser(UserSwitcherController.this.mSecondaryUserServiceIntent, UserHandle.of(UserSwitcherController.this.mSecondaryUser));
                    int unused2 = UserSwitcherController.this.mSecondaryUser = -10000;
                }
                if (!(userInfo == null || userInfo.id == 0)) {
                    context.startServiceAsUser(UserSwitcherController.this.mSecondaryUserServiceIntent, UserHandle.of(userInfo.id));
                    int unused3 = UserSwitcherController.this.mSecondaryUser = userInfo.id;
                }
                if (UserSwitcherController.this.mGuestUserAutoCreated) {
                    UserSwitcherController.this.guaranteeGuestPresent();
                }
                z = true;
            } else if ("android.intent.action.USER_INFO_CHANGED".equals(intent.getAction())) {
                i = intent.getIntExtra("android.intent.extra.user_handle", -10000);
            } else if ("android.intent.action.USER_UNLOCKED".equals(intent.getAction()) && intent.getIntExtra("android.intent.extra.user_handle", -10000) != 0) {
                return;
            }
            UserSwitcherController.this.refreshUsers(i);
            if (z) {
                UserSwitcherController.this.mUnpauseRefreshUsers.run();
            }
        }
    };
    private boolean mResumeUserOnGuestLogout = true;
    /* access modifiers changed from: private */
    public int mSecondaryUser = -10000;
    /* access modifiers changed from: private */
    public Intent mSecondaryUserServiceIntent;
    private final ContentObserver mSettingsObserver;
    /* access modifiers changed from: private */
    public boolean mSimpleUserSwitcher;
    private final TelephonyListenerManager mTelephonyListenerManager;
    /* access modifiers changed from: private */
    public final UiEventLogger mUiEventLogger;
    private final Executor mUiExecutor;
    /* access modifiers changed from: private */
    public final Runnable mUnpauseRefreshUsers = new Runnable() {
        public void run() {
            UserSwitcherController.this.mHandler.removeCallbacks(this);
            UserSwitcherController.this.mPauseRefreshUsers = false;
            UserSwitcherController.this.refreshUsers(-10000);
        }
    };
    protected final UserManager mUserManager;
    private List<UserSwitchCallback> mUserSwitchCallbacks = Collections.synchronizedList(new ArrayList());
    /* access modifiers changed from: private */
    public boolean mUserSwitcherEnabled;
    protected final UserTracker mUserTracker;
    /* access modifiers changed from: private */
    public ArrayList<UserRecord> mUsers = new ArrayList<>();
    private View mView;

    public interface UserSwitchCallback {
        void onUserSwitched();
    }

    @Inject
    public UserSwitcherController(Context context, IActivityManager iActivityManager, UserManager userManager, UserTracker userTracker, KeyguardStateController keyguardStateController, DeviceProvisionedController deviceProvisionedController, DevicePolicyManager devicePolicyManager, @Main Handler handler, ActivityStarter activityStarter, BroadcastDispatcher broadcastDispatcher, BroadcastSender broadcastSender, UiEventLogger uiEventLogger, FalsingManager falsingManager, TelephonyListenerManager telephonyListenerManager, SecureSettings secureSettings, GlobalSettings globalSettings, @Background Executor executor, @LongRunning Executor executor2, @Main Executor executor3, InteractionJankMonitor interactionJankMonitor, LatencyTracker latencyTracker, DumpManager dumpManager, DialogLaunchAnimator dialogLaunchAnimator) {
        Context context2 = context;
        UserTracker userTracker2 = userTracker;
        KeyguardStateController keyguardStateController2 = keyguardStateController;
        Handler handler2 = handler;
        BroadcastDispatcher broadcastDispatcher2 = broadcastDispatcher;
        UiEventLogger uiEventLogger2 = uiEventLogger;
        C31985 r10 = new KeyguardStateController.Callback() {
            public void onKeyguardShowingChanged() {
                if (!UserSwitcherController.this.mKeyguardStateController.isShowing()) {
                    UserSwitcherController.this.mHandler.post(new UserSwitcherController$5$$ExternalSyntheticLambda0(UserSwitcherController.this));
                } else {
                    UserSwitcherController.this.notifyAdapters();
                }
            }
        };
        this.mCallback = r10;
        this.mGuaranteeGuestPresentAfterProvisioned = new DeviceProvisionedController.DeviceProvisionedListener() {
            public void onDeviceProvisionedChanged() {
                if (UserSwitcherController.this.isDeviceAllowedToAddGuest()) {
                    UserSwitcherController.this.mBgExecutor.execute(new UserSwitcherController$6$$ExternalSyntheticLambda0(this));
                    UserSwitcherController.this.guaranteeGuestPresent();
                }
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onDeviceProvisionedChanged$0$com-android-systemui-statusbar-policy-UserSwitcherController$6 */
            public /* synthetic */ void mo46173xbadc88af() {
                UserSwitcherController.this.mDeviceProvisionedController.removeCallback(UserSwitcherController.this.mGuaranteeGuestPresentAfterProvisioned);
            }
        };
        this.mContext = context2;
        this.mActivityManager = iActivityManager;
        this.mUserTracker = userTracker2;
        this.mBroadcastDispatcher = broadcastDispatcher2;
        this.mBroadcastSender = broadcastSender;
        this.mTelephonyListenerManager = telephonyListenerManager;
        this.mUiEventLogger = uiEventLogger2;
        this.mFalsingManager = falsingManager;
        this.mInteractionJankMonitor = interactionJankMonitor;
        this.mLatencyTracker = latencyTracker;
        this.mGlobalSettings = globalSettings;
        GuestResumeSessionReceiver guestResumeSessionReceiver = new GuestResumeSessionReceiver(this, userTracker2, uiEventLogger2, secureSettings);
        this.mGuestResumeSessionReceiver = guestResumeSessionReceiver;
        this.mBgExecutor = executor;
        this.mLongRunningExecutor = executor2;
        this.mUiExecutor = executor3;
        if (!UserManager.isGuestUserEphemeral()) {
            guestResumeSessionReceiver.register(broadcastDispatcher2);
        }
        this.mGuestUserAutoCreated = context.getResources().getBoolean(17891673);
        this.mGuestIsResetting = new AtomicBoolean();
        this.mGuestCreationScheduled = new AtomicBoolean();
        this.mKeyguardStateController = keyguardStateController2;
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mDevicePolicyManager = devicePolicyManager;
        this.mHandler = handler2;
        this.mActivityStarter = activityStarter;
        this.mUserManager = userManager;
        this.mDialogLaunchAnimator = dialogLaunchAnimator;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_ADDED");
        intentFilter.addAction("android.intent.action.USER_REMOVED");
        intentFilter.addAction("android.intent.action.USER_INFO_CHANGED");
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        intentFilter.addAction("android.intent.action.USER_STOPPED");
        intentFilter.addAction("android.intent.action.USER_UNLOCKED");
        broadcastDispatcher.registerReceiver(this.mReceiver, intentFilter, (Executor) null, UserHandle.SYSTEM, 2, (String) null);
        this.mSimpleUserSwitcher = shouldUseSimpleUserSwitcher();
        this.mSecondaryUserServiceIntent = new Intent(context2, SystemUISecondaryUserService.class);
        IntentFilter intentFilter2 = new IntentFilter();
        context.registerReceiverAsUser(this.mReceiver, UserHandle.SYSTEM, intentFilter2, "com.android.systemui.permission.SELF", (Handler) null, 2);
        C31941 r2 = new ContentObserver(handler2) {
            public void onChange(boolean z) {
                UserSwitcherController userSwitcherController = UserSwitcherController.this;
                boolean unused = userSwitcherController.mSimpleUserSwitcher = userSwitcherController.shouldUseSimpleUserSwitcher();
                UserSwitcherController userSwitcherController2 = UserSwitcherController.this;
                boolean z2 = false;
                boolean unused2 = userSwitcherController2.mAddUsersFromLockScreen = userSwitcherController2.mGlobalSettings.getIntForUser("add_users_when_locked", 0, 0) != 0;
                UserSwitcherController userSwitcherController3 = UserSwitcherController.this;
                if (userSwitcherController3.mGlobalSettings.getIntForUser("user_switcher_enabled", 0, 0) != 0) {
                    z2 = true;
                }
                boolean unused3 = userSwitcherController3.mUserSwitcherEnabled = z2;
                UserSwitcherController.this.refreshUsers(-10000);
            }
        };
        this.mSettingsObserver = r2;
        context.getContentResolver().registerContentObserver(Settings.Global.getUriFor(SIMPLE_USER_SWITCHER_GLOBAL_SETTING), true, r2);
        context.getContentResolver().registerContentObserver(Settings.Global.getUriFor("user_switcher_enabled"), true, r2);
        context.getContentResolver().registerContentObserver(Settings.Global.getUriFor("add_users_when_locked"), true, r2);
        context.getContentResolver().registerContentObserver(Settings.Global.getUriFor("allow_user_switching_when_system_user_locked"), true, r2);
        r2.onChange(false);
        keyguardStateController2.addCallback(r10);
        listenForCallState();
        this.mCreateSupervisedUserPackage = context2.getString(17040043);
        dumpManager.registerDumpable(getClass().getSimpleName(), this);
        refreshUsers(-10000);
    }

    /* access modifiers changed from: private */
    public void refreshUsers(int i) {
        if (i != -10000) {
            this.mForcePictureLoadForUserId.put(i, true);
        }
        if (!this.mPauseRefreshUsers) {
            boolean z = this.mForcePictureLoadForUserId.get(-1);
            SparseArray sparseArray = new SparseArray(this.mUsers.size());
            int size = this.mUsers.size();
            for (int i2 = 0; i2 < size; i2++) {
                UserRecord userRecord = this.mUsers.get(i2);
                if (!(userRecord == null || userRecord.picture == null || userRecord.info == null || z || this.mForcePictureLoadForUserId.get(userRecord.info.id))) {
                    sparseArray.put(userRecord.info.id, userRecord.picture);
                }
            }
            this.mForcePictureLoadForUserId.clear();
            this.mBgExecutor.execute(new UserSwitcherController$$ExternalSyntheticLambda6(this, sparseArray));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$refreshUsers$1$com-android-systemui-statusbar-policy-UserSwitcherController */
    public /* synthetic */ void mo46157x20a6fb24(SparseArray sparseArray) {
        List<UserInfo> aliveUsers = this.mUserManager.getAliveUsers();
        if (aliveUsers != null) {
            ArrayList arrayList = new ArrayList(aliveUsers.size());
            int userId = this.mUserTracker.getUserId();
            boolean z = false;
            boolean z2 = this.mUserManager.getUserSwitchability(UserHandle.of(this.mUserTracker.getUserId())) == 0;
            UserRecord userRecord = null;
            for (UserInfo userInfo : aliveUsers) {
                boolean z3 = userId == userInfo.id;
                boolean z4 = z2 || z3;
                if ((this.mUserSwitcherEnabled || userInfo.isPrimary()) && userInfo.isEnabled()) {
                    if (userInfo.isGuest()) {
                        userRecord = new UserRecord(userInfo, (Bitmap) null, true, z3, false, false, z2, false);
                    } else if (userInfo.supportsSwitchToByUser()) {
                        Bitmap bitmap = (Bitmap) sparseArray.get(userInfo.id);
                        if (bitmap == null && (bitmap = this.mUserManager.getUserIcon(userInfo.id)) != null) {
                            int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(C1893R.dimen.max_avatar_size);
                            bitmap = Bitmap.createScaledBitmap(bitmap, dimensionPixelSize, dimensionPixelSize, true);
                        }
                        arrayList.add(new UserRecord(userInfo, bitmap, false, z3, false, false, z4, false));
                    }
                }
            }
            if (userRecord != null) {
                arrayList.add(userRecord);
            } else if (this.mGuestUserAutoCreated) {
                UserRecord userRecord2 = new UserRecord((UserInfo) null, (Bitmap) null, true, false, false, false, !this.mGuestIsResetting.get() && z2, false);
                checkIfAddUserDisallowedByAdminOnly(userRecord2);
                arrayList.add(userRecord2);
            } else {
                if (userRecord != null) {
                    z = true;
                }
                if (canCreateGuest(z)) {
                    UserRecord userRecord3 = new UserRecord((UserInfo) null, (Bitmap) null, true, false, false, createIsRestricted(), z2, false);
                    checkIfAddUserDisallowedByAdminOnly(userRecord3);
                    arrayList.add(userRecord3);
                }
            }
            if (canCreateUser()) {
                UserRecord userRecord4 = new UserRecord((UserInfo) null, (Bitmap) null, false, false, true, createIsRestricted(), z2, false);
                checkIfAddUserDisallowedByAdminOnly(userRecord4);
                arrayList.add(userRecord4);
            }
            if (canCreateSupervisedUser()) {
                UserRecord userRecord5 = new UserRecord((UserInfo) null, (Bitmap) null, false, false, false, createIsRestricted(), z2, true);
                checkIfAddUserDisallowedByAdminOnly(userRecord5);
                arrayList.add(userRecord5);
            }
            this.mUiExecutor.execute(new UserSwitcherController$$ExternalSyntheticLambda0(this, arrayList));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$refreshUsers$0$com-android-systemui-statusbar-policy-UserSwitcherController */
    public /* synthetic */ void mo46156x8c688b85(ArrayList arrayList) {
        if (arrayList != null) {
            this.mUsers = arrayList;
            notifyAdapters();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean systemCanCreateUsers() {
        return !this.mUserManager.hasBaseUserRestriction("no_add_user", UserHandle.SYSTEM);
    }

    /* access modifiers changed from: package-private */
    public boolean currentUserCanCreateUsers() {
        UserInfo userInfo = this.mUserTracker.getUserInfo();
        return userInfo != null && (userInfo.isAdmin() || this.mUserTracker.getUserId() == 0) && systemCanCreateUsers();
    }

    /* access modifiers changed from: package-private */
    public boolean anyoneCanCreateUsers() {
        return systemCanCreateUsers() && this.mAddUsersFromLockScreen;
    }

    /* access modifiers changed from: package-private */
    public boolean canCreateGuest(boolean z) {
        return this.mUserSwitcherEnabled && (currentUserCanCreateUsers() || anyoneCanCreateUsers()) && !z;
    }

    /* access modifiers changed from: package-private */
    public boolean canCreateUser() {
        return this.mUserSwitcherEnabled && (currentUserCanCreateUsers() || anyoneCanCreateUsers()) && this.mUserManager.canAddMoreUsers("android.os.usertype.full.SECONDARY");
    }

    /* access modifiers changed from: package-private */
    public boolean createIsRestricted() {
        return !this.mAddUsersFromLockScreen;
    }

    /* access modifiers changed from: package-private */
    public boolean canCreateSupervisedUser() {
        return !TextUtils.isEmpty(this.mCreateSupervisedUserPackage) && canCreateUser();
    }

    private void pauseRefreshUsers() {
        if (!this.mPauseRefreshUsers) {
            this.mHandler.postDelayed(this.mUnpauseRefreshUsers, 3000);
            this.mPauseRefreshUsers = true;
        }
    }

    /* access modifiers changed from: private */
    public void notifyAdapters() {
        for (int size = this.mAdapters.size() - 1; size >= 0; size--) {
            BaseUserAdapter baseUserAdapter = (BaseUserAdapter) this.mAdapters.get(size).get();
            if (baseUserAdapter != null) {
                baseUserAdapter.notifyDataSetChanged();
            } else {
                this.mAdapters.remove(size);
            }
        }
    }

    public boolean isSimpleUserSwitcher() {
        return this.mSimpleUserSwitcher;
    }

    public void setResumeUserOnGuestLogout(boolean z) {
        this.mResumeUserOnGuestLogout = z;
    }

    public boolean isSystemUser() {
        return this.mUserTracker.getUserId() == 0;
    }

    public void removeUserId(int i) {
        if (i == 0) {
            Log.w(TAG, "User " + i + " could not removed.");
            return;
        }
        if (this.mUserTracker.getUserId() == i) {
            switchToUserId(0);
        }
        if (this.mUserManager.removeUser(i)) {
            refreshUsers(-10000);
        }
    }

    public UserRecord getCurrentUserRecord() {
        for (int i = 0; i < this.mUsers.size(); i++) {
            UserRecord userRecord = this.mUsers.get(i);
            if (userRecord.isCurrent) {
                return userRecord;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void onUserListItemClicked(UserRecord userRecord, UserSwitchDialogController.DialogShower dialogShower) {
        if (userRecord.isGuest && userRecord.info == null) {
            createGuestAsync(new UserSwitcherController$$ExternalSyntheticLambda4(this, userRecord, dialogShower));
        } else if (userRecord.isAddUser) {
            showAddUserDialog(dialogShower);
        } else if (userRecord.isAddSupervisedUser) {
            startSupervisedUserActivity();
        } else {
            onUserListItemClicked(userRecord.info.id, userRecord, dialogShower);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onUserListItemClicked$2$com-android-systemui-statusbar-policy-UserSwitcherController */
    public /* synthetic */ void mo46155xc3fe867e(UserRecord userRecord, UserSwitchDialogController.DialogShower dialogShower, Integer num) {
        if (num.intValue() != -10000) {
            this.mUiEventLogger.log(QSUserSwitcherEvent.QS_USER_GUEST_ADD);
            onUserListItemClicked(num.intValue(), userRecord, dialogShower);
        }
    }

    private void onUserListItemClicked(int i, UserRecord userRecord, UserSwitchDialogController.DialogShower dialogShower) {
        UserInfo userInfo;
        int userId = this.mUserTracker.getUserId();
        if (userId == i) {
            if (userRecord.isGuest) {
                showExitGuestDialog(i, dialogShower);
            }
        } else if (!UserManager.isGuestUserEphemeral() || (userInfo = this.mUserManager.getUserInfo(userId)) == null || !userInfo.isGuest()) {
            if (dialogShower != null) {
                dialogShower.dismiss();
            }
            switchToUserId(i);
        } else {
            showExitGuestDialog(userId, userRecord.resolveId(), dialogShower);
        }
    }

    /* access modifiers changed from: protected */
    public void switchToUserId(int i) {
        try {
            this.mInteractionJankMonitor.begin(InteractionJankMonitor.Configuration.Builder.withView(37, this.mView).setTimeout(20000));
            this.mLatencyTracker.onActionStart(12);
            pauseRefreshUsers();
            this.mActivityManager.switchUser(i);
        } catch (RemoteException e) {
            Log.e(TAG, "Couldn't switch user.", e);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0008, code lost:
        r0 = r2.mUserManager.getUserInfo((r0 = r2.mLastNonGuestUser));
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void showExitGuestDialog(int r3, com.android.systemui.p012qs.user.UserSwitchDialogController.DialogShower r4) {
        /*
            r2 = this;
            boolean r0 = r2.mResumeUserOnGuestLogout
            if (r0 == 0) goto L_0x001f
            int r0 = r2.mLastNonGuestUser
            if (r0 == 0) goto L_0x001f
            android.os.UserManager r1 = r2.mUserManager
            android.content.pm.UserInfo r0 = r1.getUserInfo(r0)
            if (r0 == 0) goto L_0x001f
            boolean r1 = r0.isEnabled()
            if (r1 == 0) goto L_0x001f
            boolean r1 = r0.supportsSwitchToByUser()
            if (r1 == 0) goto L_0x001f
            int r0 = r0.id
            goto L_0x0020
        L_0x001f:
            r0 = 0
        L_0x0020:
            r2.showExitGuestDialog(r3, r0, r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.UserSwitcherController.showExitGuestDialog(int, com.android.systemui.qs.user.UserSwitchDialogController$DialogShower):void");
    }

    private void showExitGuestDialog(int i, int i2, UserSwitchDialogController.DialogShower dialogShower) {
        AlertDialog alertDialog = this.mExitGuestDialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            this.mExitGuestDialog.cancel();
        }
        ExitGuestDialog exitGuestDialog = new ExitGuestDialog(this.mContext, i, i2);
        this.mExitGuestDialog = exitGuestDialog;
        if (dialogShower != null) {
            dialogShower.showDialog(exitGuestDialog);
        } else {
            exitGuestDialog.show();
        }
    }

    private void showAddUserDialog(UserSwitchDialogController.DialogShower dialogShower) {
        Dialog dialog = this.mAddUserDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mAddUserDialog.cancel();
        }
        AddUserDialog addUserDialog = new AddUserDialog(this.mContext);
        this.mAddUserDialog = addUserDialog;
        if (dialogShower != null) {
            dialogShower.showDialog(addUserDialog);
        } else {
            addUserDialog.show();
        }
    }

    private void startSupervisedUserActivity() {
        this.mContext.startActivity(new Intent().setAction("android.os.action.CREATE_SUPERVISED_USER").setPackage(this.mCreateSupervisedUserPackage).addFlags(268435456));
    }

    private void listenForCallState() {
        this.mTelephonyListenerManager.addCallStateListener(this.mPhoneStateListener);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("UserSwitcherController state:");
        printWriter.println("  mLastNonGuestUser=" + this.mLastNonGuestUser);
        printWriter.print("  mUsers.size=");
        printWriter.println(this.mUsers.size());
        for (int i = 0; i < this.mUsers.size(); i++) {
            printWriter.print("    ");
            printWriter.println(this.mUsers.get(i).toString());
        }
        printWriter.println("mSimpleUserSwitcher=" + this.mSimpleUserSwitcher);
        printWriter.println("mGuestUserAutoCreated=" + this.mGuestUserAutoCreated);
    }

    public String getCurrentUserName() {
        UserRecord orElse;
        if (this.mUsers.isEmpty() || (orElse = this.mUsers.stream().filter(new UserSwitcherController$$ExternalSyntheticLambda1()).findFirst().orElse(null)) == null || orElse.info == null) {
            return null;
        }
        if (orElse.isGuest) {
            return this.mContext.getString(17040431);
        }
        return orElse.info.name;
    }

    public void onDensityOrFontScaleChanged() {
        refreshUsers(-1);
    }

    public void addAdapter(WeakReference<BaseUserAdapter> weakReference) {
        this.mAdapters.add(weakReference);
    }

    public ArrayList<UserRecord> getUsers() {
        return this.mUsers;
    }

    public void removeGuestUser(int i, int i2) {
        UserInfo userInfo = this.mUserTracker.getUserInfo();
        if (userInfo.id != i) {
            Log.w(TAG, "User requesting to start a new session (" + i + ") is not current user (" + userInfo.id + NavigationBarInflaterView.KEY_CODE_END);
        } else if (!userInfo.isGuest()) {
            Log.w(TAG, "User requesting to start a new session (" + i + ") is not a guest");
        } else if (!this.mUserManager.markGuestForDeletion(userInfo.id)) {
            Log.w(TAG, "Couldn't mark the guest for deletion for user " + i);
        } else if (i2 == -10000) {
            createGuestAsync(new UserSwitcherController$$ExternalSyntheticLambda3(this, userInfo));
        } else {
            if (this.mGuestUserAutoCreated) {
                this.mGuestIsResetting.set(true);
            }
            switchToUserId(i2);
            this.mUserManager.removeUser(userInfo.id);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$removeGuestUser$4$com-android-systemui-statusbar-policy-UserSwitcherController */
    public /* synthetic */ void mo46158x71aedde9(UserInfo userInfo, Integer num) {
        if (num.intValue() == -10000) {
            Log.e(TAG, "Could not create new guest, switching back to system user");
            switchToUserId(0);
            this.mUserManager.removeUser(userInfo.id);
            try {
                WindowManagerGlobal.getWindowManagerService().lockNow((Bundle) null);
            } catch (RemoteException unused) {
                Log.e(TAG, "Couldn't remove guest because ActivityManager or WindowManager is dead");
            }
        } else {
            switchToUserId(num.intValue());
            this.mUserManager.removeUser(userInfo.id);
        }
    }

    private void scheduleGuestCreation() {
        if (this.mGuestCreationScheduled.compareAndSet(false, true)) {
            this.mLongRunningExecutor.execute(new UserSwitcherController$$ExternalSyntheticLambda5(this));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$scheduleGuestCreation$5$com-android-systemui-statusbar-policy-UserSwitcherController */
    public /* synthetic */ void mo46159xf6e16dc7() {
        int createGuest = createGuest();
        this.mGuestCreationScheduled.set(false);
        this.mGuestIsResetting.set(false);
        if (createGuest == -10000) {
            Log.w(TAG, "Could not create new guest while exiting existing guest");
            refreshUsers(-10000);
        }
    }

    public void schedulePostBootGuestCreation() {
        if (isDeviceAllowedToAddGuest()) {
            guaranteeGuestPresent();
        } else {
            this.mDeviceProvisionedController.addCallback(this.mGuaranteeGuestPresentAfterProvisioned);
        }
    }

    /* access modifiers changed from: private */
    public boolean isDeviceAllowedToAddGuest() {
        return this.mDeviceProvisionedController.isDeviceProvisioned() && !this.mDevicePolicyManager.isDeviceManaged();
    }

    /* access modifiers changed from: private */
    public void guaranteeGuestPresent() {
        if (isDeviceAllowedToAddGuest() && this.mUserManager.findCurrentGuestUser() == null) {
            scheduleGuestCreation();
        }
    }

    private void createGuestAsync(Consumer<Integer> consumer) {
        UserCreatingDialog userCreatingDialog = new UserCreatingDialog(this.mContext, true);
        userCreatingDialog.show();
        ThreadUtils.postOnMainThread(new UserSwitcherController$$ExternalSyntheticLambda2(this, userCreatingDialog, consumer));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$createGuestAsync$6$com-android-systemui-statusbar-policy-UserSwitcherController */
    public /* synthetic */ void mo46154x5d5813ac(Dialog dialog, Consumer consumer) {
        int createGuest = createGuest();
        dialog.dismiss();
        if (createGuest == -10000) {
            Toast.makeText(this.mContext, C1893R.string.add_guest_failed, 0).show();
        }
        consumer.accept(Integer.valueOf(createGuest));
    }

    public int createGuest() {
        try {
            UserInfo createGuest = this.mUserManager.createGuest(this.mContext);
            if (createGuest != null) {
                return createGuest.id;
            }
            Log.e(TAG, "Couldn't create guest, most likely because there already exists one");
            return -10000;
        } catch (UserManager.UserOperationException e) {
            Log.e(TAG, "Couldn't create guest user", e);
            return -10000;
        }
    }

    public void init(View view) {
        this.mView = view;
    }

    public KeyguardStateController getKeyguardStateController() {
        return this.mKeyguardStateController;
    }

    public static abstract class BaseUserAdapter extends BaseAdapter {
        final UserSwitcherController mController;
        private final KeyguardStateController mKeyguardStateController;

        public long getItemId(int i) {
            return (long) i;
        }

        protected BaseUserAdapter(UserSwitcherController userSwitcherController) {
            this.mController = userSwitcherController;
            this.mKeyguardStateController = userSwitcherController.getKeyguardStateController();
            userSwitcherController.addAdapter(new WeakReference(this));
        }

        /* access modifiers changed from: protected */
        public ArrayList<UserRecord> getUsers() {
            return this.mController.getUsers();
        }

        public int getUserCount() {
            return countUsers(false);
        }

        public int getCount() {
            return countUsers(true);
        }

        private int countUsers(boolean z) {
            boolean isShowing = this.mKeyguardStateController.isShowing();
            int size = getUsers().size();
            int i = 0;
            for (int i2 = 0; i2 < size; i2++) {
                if (!getUsers().get(i2).isGuest || z) {
                    if (getUsers().get(i2).isRestricted && isShowing) {
                        break;
                    }
                    i++;
                }
            }
            return i;
        }

        public UserRecord getItem(int i) {
            return getUsers().get(i);
        }

        public void onUserListItemClicked(UserRecord userRecord, UserSwitchDialogController.DialogShower dialogShower) {
            this.mController.onUserListItemClicked(userRecord, dialogShower);
        }

        public void onUserListItemClicked(UserRecord userRecord) {
            onUserListItemClicked(userRecord, (UserSwitchDialogController.DialogShower) null);
        }

        public String getName(Context context, UserRecord userRecord) {
            if (userRecord.isGuest) {
                if (userRecord.isCurrent) {
                    return context.getString(this.mController.mGuestUserAutoCreated ? C1893R.string.guest_reset_guest : C1893R.string.guest_exit_guest);
                }
                int i = 17040431;
                if (userRecord.info != null) {
                    return context.getString(17040431);
                }
                if (!this.mController.mGuestUserAutoCreated) {
                    return context.getString(C1893R.string.guest_new_guest);
                }
                if (this.mController.mGuestIsResetting.get()) {
                    i = C1893R.string.guest_resetting;
                }
                return context.getString(i);
            } else if (userRecord.isAddUser) {
                return context.getString(C1893R.string.user_add_user);
            } else {
                if (userRecord.isAddSupervisedUser) {
                    return context.getString(C1893R.string.add_user_supervised);
                }
                return userRecord.info.name;
            }
        }

        protected static ColorFilter getDisabledUserAvatarColorFilter() {
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0.0f);
            return new ColorMatrixColorFilter(colorMatrix);
        }

        protected static Drawable getIconDrawable(Context context, UserRecord userRecord) {
            int i;
            if (userRecord.isAddUser) {
                i = C1893R.C1895drawable.ic_account_circle_filled;
            } else if (userRecord.isGuest) {
                i = C1893R.C1895drawable.ic_account_circle;
            } else {
                i = userRecord.isAddSupervisedUser ? C1893R.C1895drawable.ic_add_supervised_user : C1893R.C1895drawable.ic_avatar_user;
            }
            return context.getDrawable(i);
        }

        public void refresh() {
            this.mController.refreshUsers(-10000);
        }
    }

    private void checkIfAddUserDisallowedByAdminOnly(UserRecord userRecord) {
        RestrictedLockUtils.EnforcedAdmin checkIfRestrictionEnforced = RestrictedLockUtilsInternal.checkIfRestrictionEnforced(this.mContext, "no_add_user", this.mUserTracker.getUserId());
        if (checkIfRestrictionEnforced == null || RestrictedLockUtilsInternal.hasBaseUserRestriction(this.mContext, "no_add_user", this.mUserTracker.getUserId())) {
            userRecord.isDisabledByAdmin = false;
            userRecord.enforcedAdmin = null;
            return;
        }
        userRecord.isDisabledByAdmin = true;
        userRecord.enforcedAdmin = checkIfRestrictionEnforced;
    }

    /* access modifiers changed from: private */
    public boolean shouldUseSimpleUserSwitcher() {
        return this.mGlobalSettings.getIntForUser(SIMPLE_USER_SWITCHER_GLOBAL_SETTING, this.mContext.getResources().getBoolean(17891659) ? 1 : 0, 0) != 0;
    }

    public void startActivity(Intent intent) {
        this.mActivityStarter.startActivity(intent, true);
    }

    public void addUserSwitchCallback(UserSwitchCallback userSwitchCallback) {
        this.mUserSwitchCallbacks.add(userSwitchCallback);
    }

    public void removeUserSwitchCallback(UserSwitchCallback userSwitchCallback) {
        this.mUserSwitchCallbacks.remove((Object) userSwitchCallback);
    }

    /* access modifiers changed from: private */
    public void notifyUserSwitchCallbacks() {
        ArrayList<UserSwitchCallback> arrayList;
        synchronized (this.mUserSwitchCallbacks) {
            arrayList = new ArrayList<>(this.mUserSwitchCallbacks);
        }
        for (UserSwitchCallback onUserSwitched : arrayList) {
            onUserSwitched.onUserSwitched();
        }
    }

    public static final class UserRecord {
        public RestrictedLockUtils.EnforcedAdmin enforcedAdmin;
        public final UserInfo info;
        public final boolean isAddSupervisedUser;
        public final boolean isAddUser;
        public final boolean isCurrent;
        public boolean isDisabledByAdmin;
        public final boolean isGuest;
        public final boolean isRestricted;
        public boolean isSwitchToEnabled;
        public final Bitmap picture;

        public UserRecord(UserInfo userInfo, Bitmap bitmap, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6) {
            this.info = userInfo;
            this.picture = bitmap;
            this.isGuest = z;
            this.isCurrent = z2;
            this.isAddUser = z3;
            this.isRestricted = z4;
            this.isSwitchToEnabled = z5;
            this.isAddSupervisedUser = z6;
        }

        public UserRecord copyWithIsCurrent(boolean z) {
            return new UserRecord(this.info, this.picture, this.isGuest, z, this.isAddUser, this.isRestricted, this.isSwitchToEnabled, this.isAddSupervisedUser);
        }

        public int resolveId() {
            UserInfo userInfo;
            if (this.isGuest || (userInfo = this.info) == null) {
                return -10000;
            }
            return userInfo.id;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("UserRecord(");
            if (this.info != null) {
                sb.append("name=\"").append(this.info.name).append("\" id=").append(this.info.id);
            } else if (this.isGuest) {
                sb.append("<add guest placeholder>");
            } else if (this.isAddUser) {
                sb.append("<add user placeholder>");
            }
            if (this.isGuest) {
                sb.append(" <isGuest>");
            }
            if (this.isAddUser) {
                sb.append(" <isAddUser>");
            }
            if (this.isAddSupervisedUser) {
                sb.append(" <isAddSupervisedUser>");
            }
            if (this.isCurrent) {
                sb.append(" <isCurrent>");
            }
            if (this.picture != null) {
                sb.append(" <hasPicture>");
            }
            if (this.isRestricted) {
                sb.append(" <isRestricted>");
            }
            if (this.isDisabledByAdmin) {
                sb.append(" <isDisabledByAdmin> enforcedAdmin=");
                sb.append((Object) this.enforcedAdmin);
            }
            if (this.isSwitchToEnabled) {
                sb.append(" <isSwitchToEnabled>");
            }
            sb.append(')');
            return sb.toString();
        }
    }

    private final class ExitGuestDialog extends SystemUIDialog implements DialogInterface.OnClickListener {
        private final int mGuestId;
        private final int mTargetId;

        public ExitGuestDialog(Context context, int i, int i2) {
            super(context);
            setTitle(UserSwitcherController.this.mGuestUserAutoCreated ? C1893R.string.guest_reset_guest_dialog_title : C1893R.string.guest_remove_guest_dialog_title);
            setMessage(context.getString(C1893R.string.guest_exit_guest_dialog_message));
            setButton(-3, context.getString(17039360), this);
            setButton(-1, context.getString(UserSwitcherController.this.mGuestUserAutoCreated ? C1893R.string.guest_reset_guest_confirm_button : C1893R.string.guest_remove_guest_confirm_button), this);
            SystemUIDialog.setWindowOnTop(this, UserSwitcherController.this.mKeyguardStateController.isShowing());
            setCanceledOnTouchOutside(false);
            this.mGuestId = i;
            this.mTargetId = i2;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            if (!UserSwitcherController.this.mFalsingManager.isFalseTap(i == -2 ? 0 : 3)) {
                if (i == -3) {
                    cancel();
                    return;
                }
                UserSwitcherController.this.mUiEventLogger.log(QSUserSwitcherEvent.QS_USER_GUEST_REMOVE);
                UserSwitcherController.this.mDialogLaunchAnimator.dismissStack(this);
                UserSwitcherController.this.removeGuestUser(this.mGuestId, this.mTargetId);
            }
        }
    }

    final class AddUserDialog extends SystemUIDialog implements DialogInterface.OnClickListener {
        public AddUserDialog(Context context) {
            super(context);
            setTitle(C1893R.string.user_add_user_title);
            setMessage(C1893R.string.user_add_user_message_short);
            setButton(-3, context.getString(17039360), this);
            setButton(-1, context.getString(17039370), this);
            SystemUIDialog.setWindowOnTop(this, UserSwitcherController.this.mKeyguardStateController.isShowing());
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            if (!UserSwitcherController.this.mFalsingManager.isFalseTap(i == -2 ? 0 : 2)) {
                if (i == -3) {
                    cancel();
                    return;
                }
                UserSwitcherController.this.mDialogLaunchAnimator.dismissStack(this);
                if (!ActivityManager.isUserAMonkey()) {
                    UserSwitcherController.this.mBroadcastSender.sendBroadcastAsUser(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"), UserHandle.CURRENT);
                    getContext().startActivityAsUser(CreateUserActivity.createIntentForStart(getContext()), UserSwitcherController.this.mUserTracker.getUserHandle());
                }
            }
        }
    }
}
