package com.android.systemui.statusbar;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.UserInfo;
import android.database.ContentObserver;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Handler;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.DejankUtils;
import com.android.systemui.Dependency;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.ListenerSet;
import com.android.systemui.util.settings.SecureSettings;
import dagger.Lazy;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import javax.inject.Inject;

@SysUISingleton
public class NotificationLockscreenUserManagerImpl implements Dumpable, NotificationLockscreenUserManager, StatusBarStateController.StateListener {
    private static final boolean ENABLE_LOCK_SCREEN_ALLOW_REMOTE_INPUT = false;
    private static final String TAG = "LockscreenUserManager";
    protected final BroadcastReceiver mAllUsersReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED".equals(intent.getAction()) && NotificationLockscreenUserManagerImpl.this.isCurrentProfile(getSendingUserId())) {
                NotificationLockscreenUserManagerImpl.this.mUsersAllowingPrivateNotifications.clear();
                NotificationLockscreenUserManagerImpl.this.updateLockscreenNotificationSetting();
                NotificationLockscreenUserManagerImpl.this.getEntryManager().updateNotifications("ACTION_DEVICE_POLICY_MANAGER_STATE_CHANGED");
            }
        }
    };
    private boolean mAllowLockscreenRemoteInput;
    protected final BroadcastReceiver mBaseBroadcastReceiver = new BroadcastReceiver() {
        /* JADX WARNING: Can't fix incorrect switch cases order */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onReceive(android.content.Context r9, android.content.Intent r10) {
            /*
                r8 = this;
                java.lang.String r9 = r10.getAction()
                r9.hashCode()
                int r0 = r9.hashCode()
                r1 = 1
                r2 = -1
                switch(r0) {
                    case -2061058799: goto L_0x0054;
                    case -1238404651: goto L_0x0049;
                    case -864107122: goto L_0x003e;
                    case -598152660: goto L_0x0033;
                    case 833559602: goto L_0x0028;
                    case 959232034: goto L_0x001d;
                    case 1121780209: goto L_0x0012;
                    default: goto L_0x0010;
                }
            L_0x0010:
                r9 = r2
                goto L_0x005e
            L_0x0012:
                java.lang.String r0 = "android.intent.action.USER_ADDED"
                boolean r9 = r9.equals(r0)
                if (r9 != 0) goto L_0x001b
                goto L_0x0010
            L_0x001b:
                r9 = 6
                goto L_0x005e
            L_0x001d:
                java.lang.String r0 = "android.intent.action.USER_SWITCHED"
                boolean r9 = r9.equals(r0)
                if (r9 != 0) goto L_0x0026
                goto L_0x0010
            L_0x0026:
                r9 = 5
                goto L_0x005e
            L_0x0028:
                java.lang.String r0 = "android.intent.action.USER_UNLOCKED"
                boolean r9 = r9.equals(r0)
                if (r9 != 0) goto L_0x0031
                goto L_0x0010
            L_0x0031:
                r9 = 4
                goto L_0x005e
            L_0x0033:
                java.lang.String r0 = "com.android.systemui.statusbar.work_challenge_unlocked_notification_action"
                boolean r9 = r9.equals(r0)
                if (r9 != 0) goto L_0x003c
                goto L_0x0010
            L_0x003c:
                r9 = 3
                goto L_0x005e
            L_0x003e:
                java.lang.String r0 = "android.intent.action.MANAGED_PROFILE_AVAILABLE"
                boolean r9 = r9.equals(r0)
                if (r9 != 0) goto L_0x0047
                goto L_0x0010
            L_0x0047:
                r9 = 2
                goto L_0x005e
            L_0x0049:
                java.lang.String r0 = "android.intent.action.MANAGED_PROFILE_UNAVAILABLE"
                boolean r9 = r9.equals(r0)
                if (r9 != 0) goto L_0x0052
                goto L_0x0010
            L_0x0052:
                r9 = r1
                goto L_0x005e
            L_0x0054:
                java.lang.String r0 = "android.intent.action.USER_REMOVED"
                boolean r9 = r9.equals(r0)
                if (r9 != 0) goto L_0x005d
                goto L_0x0010
            L_0x005d:
                r9 = 0
            L_0x005e:
                java.lang.String r0 = "android.intent.extra.user_handle"
                switch(r9) {
                    case 0: goto L_0x011a;
                    case 1: goto L_0x0114;
                    case 2: goto L_0x0114;
                    case 3: goto L_0x00dc;
                    case 4: goto L_0x00d0;
                    case 5: goto L_0x0065;
                    case 6: goto L_0x0114;
                    default: goto L_0x0063;
                }
            L_0x0063:
                goto L_0x013f
            L_0x0065:
                com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl r9 = com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl.this
                int r10 = r10.getIntExtra(r0, r2)
                r9.mCurrentUserId = r10
                com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl r9 = com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl.this
                r9.updateCurrentProfilesCache()
                java.lang.StringBuilder r9 = new java.lang.StringBuilder
                java.lang.String r10 = "userId "
                r9.<init>((java.lang.String) r10)
                com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl r10 = com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl.this
                int r10 = r10.mCurrentUserId
                java.lang.StringBuilder r9 = r9.append((int) r10)
                java.lang.String r10 = " is in the house"
                java.lang.StringBuilder r9 = r9.append((java.lang.String) r10)
                java.lang.String r9 = r9.toString()
                java.lang.String r10 = "LockscreenUserManager"
                android.util.Log.v(r10, r9)
                com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl r9 = com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl.this
                r9.updateLockscreenNotificationSetting()
                com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl r9 = com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl.this
                r9.updatePublicMode()
                com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl r9 = com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl.this
                com.android.systemui.statusbar.notification.NotificationEntryManager r9 = r9.getEntryManager()
                java.lang.String r10 = "user switched"
                r9.reapplyFilterAndSort(r10)
                com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl r9 = com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl.this
                com.android.systemui.statusbar.NotificationPresenter r9 = r9.mPresenter
                com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl r10 = com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl.this
                int r10 = r10.mCurrentUserId
                r9.onUserSwitched(r10)
                com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl r9 = com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl.this
                java.util.List r9 = r9.mListeners
                java.util.Iterator r9 = r9.iterator()
            L_0x00bc:
                boolean r10 = r9.hasNext()
                if (r10 == 0) goto L_0x013f
                java.lang.Object r10 = r9.next()
                com.android.systemui.statusbar.NotificationLockscreenUserManager$UserChangedListener r10 = (com.android.systemui.statusbar.NotificationLockscreenUserManager.UserChangedListener) r10
                com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl r0 = com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl.this
                int r0 = r0.mCurrentUserId
                r10.onUserChanged(r0)
                goto L_0x00bc
            L_0x00d0:
                java.lang.Class<com.android.systemui.recents.OverviewProxyService> r8 = com.android.systemui.recents.OverviewProxyService.class
                java.lang.Object r8 = com.android.systemui.Dependency.get(r8)
                com.android.systemui.recents.OverviewProxyService r8 = (com.android.systemui.recents.OverviewProxyService) r8
                r8.startConnectionToCurrentUser()
                goto L_0x013f
            L_0x00dc:
                java.lang.String r9 = "android.intent.extra.INTENT"
                android.os.Parcelable r9 = r10.getParcelableExtra(r9)
                r3 = r9
                android.content.IntentSender r3 = (android.content.IntentSender) r3
                java.lang.String r9 = "android.intent.extra.INDEX"
                java.lang.String r9 = r10.getStringExtra(r9)
                if (r3 == 0) goto L_0x00f8
                com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl r10 = com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl.this     // Catch:{ SendIntentException -> 0x00f8 }
                android.content.Context r2 = r10.mContext     // Catch:{ SendIntentException -> 0x00f8 }
                r4 = 0
                r5 = 0
                r6 = 0
                r7 = 0
                r2.startIntentSender(r3, r4, r5, r6, r7)     // Catch:{ SendIntentException -> 0x00f8 }
            L_0x00f8:
                if (r9 == 0) goto L_0x013f
                com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl r10 = com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl.this
                dagger.Lazy r10 = r10.mVisibilityProviderLazy
                java.lang.Object r10 = r10.get()
                com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider r10 = (com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider) r10
                com.android.internal.statusbar.NotificationVisibility r10 = r10.obtain((java.lang.String) r9, (boolean) r1)
                com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl r8 = com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl.this
                com.android.systemui.statusbar.NotificationClickNotifier r8 = r8.mClickNotifier
                r8.onNotificationClick(r9, r10)
                goto L_0x013f
            L_0x0114:
                com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl r8 = com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl.this
                r8.updateCurrentProfilesCache()
                goto L_0x013f
            L_0x011a:
                int r9 = r10.getIntExtra(r0, r2)
                if (r9 == r2) goto L_0x013a
                com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl r10 = com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl.this
                java.util.List r10 = r10.mListeners
                java.util.Iterator r10 = r10.iterator()
            L_0x012a:
                boolean r0 = r10.hasNext()
                if (r0 == 0) goto L_0x013a
                java.lang.Object r0 = r10.next()
                com.android.systemui.statusbar.NotificationLockscreenUserManager$UserChangedListener r0 = (com.android.systemui.statusbar.NotificationLockscreenUserManager.UserChangedListener) r0
                r0.onUserRemoved(r9)
                goto L_0x012a
            L_0x013a:
                com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl r8 = com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl.this
                r8.updateCurrentProfilesCache()
            L_0x013f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl.C25702.onReceive(android.content.Context, android.content.Intent):void");
        }
    };
    private final BroadcastDispatcher mBroadcastDispatcher;
    /* access modifiers changed from: private */
    public final NotificationClickNotifier mClickNotifier;
    private final Lazy<CommonNotifCollection> mCommonNotifCollectionLazy;
    protected final Context mContext;
    protected final SparseArray<UserInfo> mCurrentManagedProfiles = new SparseArray<>();
    protected final SparseArray<UserInfo> mCurrentProfiles = new SparseArray<>();
    protected int mCurrentUserId = 0;
    private final DevicePolicyManager mDevicePolicyManager;
    /* access modifiers changed from: private */
    public final DeviceProvisionedController mDeviceProvisionedController;
    private NotificationEntryManager mEntryManager;
    private boolean mHideSilentNotificationsOnLockscreen;
    protected KeyguardManager mKeyguardManager;
    private final KeyguardStateController mKeyguardStateController;
    private List<NotificationLockscreenUserManager.KeyguardNotificationSuppressor> mKeyguardSuppressors = new ArrayList();
    /* access modifiers changed from: private */
    public final List<NotificationLockscreenUserManager.UserChangedListener> mListeners = new ArrayList();
    private final Object mLock = new Object();
    private LockPatternUtils mLockPatternUtils;
    private final SparseBooleanArray mLockscreenPublicMode = new SparseBooleanArray();
    protected ContentObserver mLockscreenSettingsObserver;
    private final Handler mMainHandler;
    private final ListenerSet<NotificationLockscreenUserManager.NotificationStateChangedListener> mNotifStateChangedListeners = new ListenerSet<>();
    protected NotificationPresenter mPresenter;
    private final SecureSettings mSecureSettings;
    protected ContentObserver mSettingsObserver;
    private final SparseBooleanArray mShouldHideNotifsLatestResult = new SparseBooleanArray();
    private boolean mShowLockscreenNotifications;
    private int mState = 0;
    private final UserManager mUserManager;
    /* access modifiers changed from: private */
    public final SparseBooleanArray mUsersAllowingNotifications = new SparseBooleanArray();
    /* access modifiers changed from: private */
    public final SparseBooleanArray mUsersAllowingPrivateNotifications = new SparseBooleanArray();
    private final SparseBooleanArray mUsersInLockdownLatestResult = new SparseBooleanArray();
    private final SparseBooleanArray mUsersWithSeparateWorkChallenge = new SparseBooleanArray();
    /* access modifiers changed from: private */
    public final Lazy<NotificationVisibilityProvider> mVisibilityProviderLazy;

    /* access modifiers changed from: private */
    public NotificationEntryManager getEntryManager() {
        if (this.mEntryManager == null) {
            this.mEntryManager = (NotificationEntryManager) Dependency.get(NotificationEntryManager.class);
        }
        return this.mEntryManager;
    }

    @Inject
    public NotificationLockscreenUserManagerImpl(Context context, BroadcastDispatcher broadcastDispatcher, DevicePolicyManager devicePolicyManager, UserManager userManager, Lazy<NotificationVisibilityProvider> lazy, Lazy<CommonNotifCollection> lazy2, NotificationClickNotifier notificationClickNotifier, KeyguardManager keyguardManager, StatusBarStateController statusBarStateController, @Main Handler handler, DeviceProvisionedController deviceProvisionedController, KeyguardStateController keyguardStateController, SecureSettings secureSettings, DumpManager dumpManager) {
        this.mContext = context;
        this.mMainHandler = handler;
        this.mDevicePolicyManager = devicePolicyManager;
        this.mUserManager = userManager;
        this.mCurrentUserId = ActivityManager.getCurrentUser();
        this.mVisibilityProviderLazy = lazy;
        this.mCommonNotifCollectionLazy = lazy2;
        this.mClickNotifier = notificationClickNotifier;
        StatusBarStateController statusBarStateController2 = statusBarStateController;
        statusBarStateController.addCallback(this);
        this.mLockPatternUtils = new LockPatternUtils(context);
        this.mKeyguardManager = keyguardManager;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mSecureSettings = secureSettings;
        this.mKeyguardStateController = keyguardStateController;
        dumpManager.registerDumpable(this);
    }

    public void setUpWithPresenter(NotificationPresenter notificationPresenter) {
        this.mPresenter = notificationPresenter;
        this.mLockscreenSettingsObserver = new ContentObserver(this.mMainHandler) {
            public void onChange(boolean z) {
                NotificationLockscreenUserManagerImpl.this.mUsersAllowingPrivateNotifications.clear();
                NotificationLockscreenUserManagerImpl.this.mUsersAllowingNotifications.clear();
                NotificationLockscreenUserManagerImpl.this.updateLockscreenNotificationSetting();
                NotificationLockscreenUserManagerImpl.this.getEntryManager().updateNotifications("LOCK_SCREEN_SHOW_NOTIFICATIONS, or LOCK_SCREEN_ALLOW_PRIVATE_NOTIFICATIONS change");
                NotificationLockscreenUserManagerImpl.this.notifyNotificationStateChanged();
            }
        };
        this.mSettingsObserver = new ContentObserver(this.mMainHandler) {
            public void onChange(boolean z) {
                NotificationLockscreenUserManagerImpl.this.updateLockscreenNotificationSetting();
                if (NotificationLockscreenUserManagerImpl.this.mDeviceProvisionedController.isDeviceProvisioned()) {
                    NotificationLockscreenUserManagerImpl.this.getEntryManager().updateNotifications("LOCK_SCREEN_ALLOW_REMOTE_INPUT or ZEN_MODE change");
                }
            }
        };
        this.mContext.getContentResolver().registerContentObserver(this.mSecureSettings.getUriFor("lock_screen_show_notifications"), false, this.mLockscreenSettingsObserver, -1);
        this.mContext.getContentResolver().registerContentObserver(this.mSecureSettings.getUriFor("lock_screen_allow_private_notifications"), true, this.mLockscreenSettingsObserver, -1);
        this.mContext.getContentResolver().registerContentObserver(this.mSecureSettings.getUriFor("lock_screen_show_silent_notifications"), true, this.mLockscreenSettingsObserver, -1);
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("zen_mode"), false, this.mSettingsObserver);
        this.mBroadcastDispatcher.registerReceiver(this.mAllUsersReceiver, new IntentFilter("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED"), (Executor) null, UserHandle.ALL);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        intentFilter.addAction("android.intent.action.USER_ADDED");
        intentFilter.addAction("android.intent.action.USER_REMOVED");
        intentFilter.addAction("android.intent.action.USER_UNLOCKED");
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_AVAILABLE");
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_UNAVAILABLE");
        this.mBroadcastDispatcher.registerReceiver(this.mBaseBroadcastReceiver, intentFilter, (Executor) null, UserHandle.ALL);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(NotificationLockscreenUserManager.NOTIFICATION_UNLOCKED_BY_WORK_CHALLENGE_ACTION);
        this.mContext.registerReceiver(this.mBaseBroadcastReceiver, intentFilter2, "com.android.systemui.permission.SELF", (Handler) null, 2);
        this.mCurrentUserId = ActivityManager.getCurrentUser();
        updateCurrentProfilesCache();
        this.mSettingsObserver.onChange(false);
    }

    public boolean shouldShowLockscreenNotifications() {
        return this.mShowLockscreenNotifications;
    }

    public boolean shouldAllowLockscreenRemoteInput() {
        return this.mAllowLockscreenRemoteInput;
    }

    public boolean isCurrentProfile(int i) {
        boolean z;
        synchronized (this.mLock) {
            if (i != -1) {
                try {
                    if (this.mCurrentProfiles.get(i) == null) {
                        z = false;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            z = true;
        }
        return z;
    }

    private boolean shouldTemporarilyHideNotifications(int i) {
        if (i == -1) {
            i = this.mCurrentUserId;
        }
        boolean isUserInLockdown = ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isUserInLockdown(i);
        this.mUsersInLockdownLatestResult.put(i, isUserInLockdown);
        return isUserInLockdown;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000c, code lost:
        r0 = r1.mCurrentUserId;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean shouldHideNotifications(int r2) {
        /*
            r1 = this;
            boolean r0 = r1.isLockscreenPublicMode(r2)
            if (r0 == 0) goto L_0x000c
            boolean r0 = r1.userAllowsNotificationsInPublic(r2)
            if (r0 == 0) goto L_0x001c
        L_0x000c:
            int r0 = r1.mCurrentUserId
            if (r2 == r0) goto L_0x0016
            boolean r0 = r1.shouldHideNotifications((int) r0)
            if (r0 != 0) goto L_0x001c
        L_0x0016:
            boolean r0 = r1.shouldTemporarilyHideNotifications(r2)
            if (r0 == 0) goto L_0x001e
        L_0x001c:
            r0 = 1
            goto L_0x001f
        L_0x001e:
            r0 = 0
        L_0x001f:
            android.util.SparseBooleanArray r1 = r1.mShouldHideNotifsLatestResult
            r1.put(r2, r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl.shouldHideNotifications(int):boolean");
    }

    public boolean shouldHideNotifications(String str) {
        if (this.mCommonNotifCollectionLazy.get() == null) {
            Log.wtf(TAG, "mCommonNotifCollectionLazy was null!", new Throwable());
            return true;
        }
        NotificationEntry entry = this.mCommonNotifCollectionLazy.get().getEntry(str);
        if (!isLockscreenPublicMode(this.mCurrentUserId) || entry == null || entry.getRanking().getLockscreenVisibilityOverride() != -1) {
            return false;
        }
        return true;
    }

    public boolean shouldShowOnKeyguard(NotificationEntry notificationEntry) {
        boolean z;
        if (this.mCommonNotifCollectionLazy.get() == null) {
            Log.wtf(TAG, "mCommonNotifCollectionLazy was null!", new Throwable());
            return false;
        }
        for (int i = 0; i < this.mKeyguardSuppressors.size(); i++) {
            if (this.mKeyguardSuppressors.get(i).shouldSuppressOnKeyguard(notificationEntry)) {
                return false;
            }
        }
        if (this.mHideSilentNotificationsOnLockscreen) {
            z = notificationEntry.getBucket() == 1 || (notificationEntry.getBucket() != 6 && notificationEntry.getImportance() >= 3);
        } else {
            z = !notificationEntry.getRanking().isAmbient();
        }
        if (!this.mShowLockscreenNotifications || !z) {
            return false;
        }
        return true;
    }

    private void setShowLockscreenNotifications(boolean z) {
        this.mShowLockscreenNotifications = z;
    }

    private void setLockscreenAllowRemoteInput(boolean z) {
        this.mAllowLockscreenRemoteInput = z;
    }

    /* access modifiers changed from: protected */
    public void updateLockscreenNotificationSetting() {
        boolean z = true;
        boolean z2 = this.mSecureSettings.getIntForUser("lock_screen_show_notifications", 1, this.mCurrentUserId) != 0;
        boolean z3 = (this.mDevicePolicyManager.getKeyguardDisabledFeatures((ComponentName) null, this.mCurrentUserId) & 4) == 0;
        this.mHideSilentNotificationsOnLockscreen = this.mSecureSettings.getIntForUser("lock_screen_show_silent_notifications", 1, this.mCurrentUserId) == 0;
        if (!z2 || !z3) {
            z = false;
        }
        setShowLockscreenNotifications(z);
        setLockscreenAllowRemoteInput(false);
    }

    public boolean userAllowsPrivateNotificationsInPublic(int i) {
        boolean z = true;
        if (i == -1) {
            return true;
        }
        if (this.mUsersAllowingPrivateNotifications.indexOfKey(i) >= 0) {
            return this.mUsersAllowingPrivateNotifications.get(i);
        }
        boolean z2 = this.mSecureSettings.getIntForUser("lock_screen_allow_private_notifications", 0, i) != 0;
        boolean adminAllowsKeyguardFeature = adminAllowsKeyguardFeature(i, 8);
        if (!z2 || !adminAllowsKeyguardFeature) {
            z = false;
        }
        this.mUsersAllowingPrivateNotifications.append(i, z);
        return z;
    }

    public boolean allowsManagedPrivateNotificationsInPublic() {
        synchronized (this.mLock) {
            for (int size = this.mCurrentManagedProfiles.size() - 1; size >= 0; size--) {
                if (!userAllowsPrivateNotificationsInPublic(this.mCurrentManagedProfiles.valueAt(size).id)) {
                    return false;
                }
            }
            return true;
        }
    }

    private boolean adminAllowsKeyguardFeature(int i, int i2) {
        if (i == -1 || (this.mDevicePolicyManager.getKeyguardDisabledFeatures((ComponentName) null, i) & i2) == 0) {
            return true;
        }
        return false;
    }

    public void setLockscreenPublicMode(boolean z, int i) {
        this.mLockscreenPublicMode.put(i, z);
    }

    public boolean isLockscreenPublicMode(int i) {
        if (i == -1) {
            return this.mLockscreenPublicMode.get(this.mCurrentUserId, false);
        }
        return this.mLockscreenPublicMode.get(i, false);
    }

    public boolean needsSeparateWorkChallenge(int i) {
        return this.mUsersWithSeparateWorkChallenge.get(i, false);
    }

    public boolean userAllowsNotificationsInPublic(int i) {
        boolean z = true;
        if (isCurrentProfile(i) && i != this.mCurrentUserId) {
            return true;
        }
        if (this.mUsersAllowingNotifications.indexOfKey(i) >= 0) {
            return this.mUsersAllowingNotifications.get(i);
        }
        boolean z2 = this.mSecureSettings.getIntForUser("lock_screen_show_notifications", 0, i) != 0;
        boolean adminAllowsKeyguardFeature = adminAllowsKeyguardFeature(i, 4);
        boolean privateNotificationsAllowed = this.mKeyguardManager.getPrivateNotificationsAllowed();
        if (!z2 || !adminAllowsKeyguardFeature || !privateNotificationsAllowed) {
            z = false;
        }
        this.mUsersAllowingNotifications.append(i, z);
        return z;
    }

    public boolean needsRedaction(NotificationEntry notificationEntry) {
        int userId = notificationEntry.getSbn().getUserId();
        boolean z = (!this.mCurrentManagedProfiles.contains(userId) && (userAllowsPrivateNotificationsInPublic(this.mCurrentUserId) ^ true)) || (userAllowsPrivateNotificationsInPublic(userId) ^ true);
        boolean z2 = notificationEntry.getSbn().getNotification().visibility == 0;
        if (packageHasVisibilityOverride(notificationEntry.getSbn().getKey())) {
            return true;
        }
        if (!z2 || !z) {
            return false;
        }
        return true;
    }

    private boolean packageHasVisibilityOverride(String str) {
        if (this.mCommonNotifCollectionLazy.get() == null) {
            Log.wtf(TAG, "mEntryManager was null!", new Throwable());
            return true;
        }
        NotificationEntry entry = this.mCommonNotifCollectionLazy.get().getEntry(str);
        if (entry == null || entry.getRanking().getLockscreenVisibilityOverride() != 0) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void updateCurrentProfilesCache() {
        synchronized (this.mLock) {
            this.mCurrentProfiles.clear();
            this.mCurrentManagedProfiles.clear();
            UserManager userManager = this.mUserManager;
            if (userManager != null) {
                for (UserInfo userInfo : userManager.getProfiles(this.mCurrentUserId)) {
                    this.mCurrentProfiles.put(userInfo.id, userInfo);
                    if ("android.os.usertype.profile.MANAGED".equals(userInfo.userType)) {
                        this.mCurrentManagedProfiles.put(userInfo.id, userInfo);
                    }
                }
            }
        }
        this.mMainHandler.post(new NotificationLockscreenUserManagerImpl$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateCurrentProfilesCache$0$com-android-systemui-statusbar-NotificationLockscreenUserManagerImpl */
    public /* synthetic */ void mo38762x1ee04e87() {
        for (NotificationLockscreenUserManager.UserChangedListener onCurrentProfilesChanged : this.mListeners) {
            onCurrentProfilesChanged.onCurrentProfilesChanged(this.mCurrentProfiles);
        }
    }

    public boolean isAnyProfilePublicMode() {
        synchronized (this.mLock) {
            for (int size = this.mCurrentProfiles.size() - 1; size >= 0; size--) {
                if (isLockscreenPublicMode(this.mCurrentProfiles.valueAt(size).id)) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean isAnyManagedProfilePublicMode() {
        synchronized (this.mLock) {
            for (int size = this.mCurrentManagedProfiles.size() - 1; size >= 0; size--) {
                if (isLockscreenPublicMode(this.mCurrentManagedProfiles.valueAt(size).id)) {
                    return true;
                }
            }
            return false;
        }
    }

    public int getCurrentUserId() {
        return this.mCurrentUserId;
    }

    public SparseArray<UserInfo> getCurrentProfiles() {
        return this.mCurrentProfiles;
    }

    public void onStateChanged(int i) {
        this.mState = i;
        updatePublicMode();
    }

    public void updatePublicMode() {
        boolean z = this.mState != 0 || this.mKeyguardStateController.isShowing();
        boolean z2 = z && this.mKeyguardStateController.isMethodSecure();
        SparseArray<UserInfo> currentProfiles = getCurrentProfiles();
        SparseBooleanArray clone = this.mLockscreenPublicMode.clone();
        SparseBooleanArray clone2 = this.mUsersWithSeparateWorkChallenge.clone();
        this.mUsersWithSeparateWorkChallenge.clear();
        for (int size = currentProfiles.size() - 1; size >= 0; size--) {
            int i = currentProfiles.valueAt(size).id;
            boolean booleanValue = ((Boolean) DejankUtils.whitelistIpcs(new NotificationLockscreenUserManagerImpl$$ExternalSyntheticLambda1(this, i))).booleanValue();
            setLockscreenPublicMode((z2 || i == getCurrentUserId() || !booleanValue || !this.mLockPatternUtils.isSecure(i)) ? z2 : z || this.mKeyguardManager.isDeviceLocked(i), i);
            this.mUsersWithSeparateWorkChallenge.put(i, booleanValue);
        }
        getEntryManager().updateNotifications("NotificationLockscreenUserManager.updatePublicMode");
        if (!this.mLockscreenPublicMode.equals(clone) || !this.mUsersWithSeparateWorkChallenge.equals(clone2)) {
            notifyNotificationStateChanged();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updatePublicMode$1$com-android-systemui-statusbar-NotificationLockscreenUserManagerImpl */
    public /* synthetic */ Boolean mo38763x300b5c19(int i) {
        return Boolean.valueOf(this.mLockPatternUtils.isSeparateProfileChallengeEnabled(i));
    }

    public void addUserChangedListener(NotificationLockscreenUserManager.UserChangedListener userChangedListener) {
        this.mListeners.add(userChangedListener);
    }

    public void addKeyguardNotificationSuppressor(NotificationLockscreenUserManager.KeyguardNotificationSuppressor keyguardNotificationSuppressor) {
        this.mKeyguardSuppressors.add(keyguardNotificationSuppressor);
    }

    public void removeUserChangedListener(NotificationLockscreenUserManager.UserChangedListener userChangedListener) {
        this.mListeners.remove((Object) userChangedListener);
    }

    public void addNotificationStateChangedListener(NotificationLockscreenUserManager.NotificationStateChangedListener notificationStateChangedListener) {
        this.mNotifStateChangedListeners.addIfAbsent(notificationStateChangedListener);
    }

    public void removeNotificationStateChangedListener(NotificationLockscreenUserManager.NotificationStateChangedListener notificationStateChangedListener) {
        this.mNotifStateChangedListeners.remove(notificationStateChangedListener);
    }

    /* access modifiers changed from: private */
    public void notifyNotificationStateChanged() {
        Iterator<NotificationLockscreenUserManager.NotificationStateChangedListener> it = this.mNotifStateChangedListeners.iterator();
        while (it.hasNext()) {
            it.next().onNotificationStateChanged();
        }
    }

    public boolean userAllowsNotificationsAndContents(int i) {
        return userAllowsNotificationsInPublic(i) && userAllowsPrivateNotificationsInPublic(i);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("NotificationLockscreenUserManager state:");
        printWriter.print("  mCurrentUserId=");
        printWriter.println(this.mCurrentUserId);
        printWriter.print("  mShowLockscreenNotifications=");
        printWriter.println(this.mShowLockscreenNotifications);
        printWriter.print("  mAllowLockscreenRemoteInput=");
        printWriter.println(this.mAllowLockscreenRemoteInput);
        printWriter.print("  mCurrentProfiles=");
        synchronized (this.mLock) {
            for (int size = this.mCurrentProfiles.size() - 1; size >= 0; size--) {
                printWriter.print("" + this.mCurrentProfiles.valueAt(size).id + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            }
        }
        printWriter.println();
        printWriter.print("  mCurrentManagedProfiles=");
        synchronized (this.mLock) {
            for (int size2 = this.mCurrentManagedProfiles.size() - 1; size2 >= 0; size2--) {
                printWriter.print("" + this.mCurrentManagedProfiles.valueAt(size2).id + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            }
        }
        printWriter.println();
        printWriter.print("  mLockscreenPublicMode=");
        printWriter.println((Object) this.mLockscreenPublicMode);
        printWriter.print("  mUsersWithSeparateWorkChallenge=");
        printWriter.println((Object) this.mUsersWithSeparateWorkChallenge);
        printWriter.print("  mUsersAllowingPrivateNotifications=");
        printWriter.println((Object) this.mUsersAllowingPrivateNotifications);
        printWriter.print("  mUsersAllowingNotifications=");
        printWriter.println((Object) this.mUsersAllowingNotifications);
        printWriter.print("  mUsersInLockdownLatestResult=");
        printWriter.println((Object) this.mUsersInLockdownLatestResult);
        printWriter.print("  mShouldHideNotifsLatestResult=");
        printWriter.println((Object) this.mShouldHideNotifsLatestResult);
    }
}
