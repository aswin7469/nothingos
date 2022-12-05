package com.android.systemui.globalactions;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.IActivityManager;
import android.app.IStopUserCallback;
import android.app.admin.DevicePolicyManager;
import android.app.trust.TrustManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.service.dreams.IDreamManager;
import android.sysprop.TelephonyProperties;
import android.telecom.TelecomManager;
import android.telephony.ServiceState;
import android.telephony.TelephonyCallback;
import android.util.ArraySet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.GestureDetector;
import android.view.IWindowManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.util.EmergencyAffordanceManager;
import com.android.internal.util.ScreenshotHelper;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.MultiListLayout;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$integer;
import com.android.systemui.R$layout;
import com.android.systemui.R$style;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import com.android.systemui.model.SysUiState;
import com.android.systemui.plugins.GlobalActions;
import com.android.systemui.plugins.GlobalActionsPanelPlugin;
import com.android.systemui.scrim.ScrimDrawable;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.util.RingerModeTracker;
import com.android.systemui.util.settings.GlobalSettings;
import com.android.systemui.util.settings.SecureSettings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class GlobalActionsDialogLite implements DialogInterface.OnDismissListener, DialogInterface.OnShowListener, ConfigurationController.ConfigurationListener, GlobalActionsPanelPlugin.Callbacks, LifecycleOwner {
    @VisibleForTesting
    static final String GLOBAL_ACTION_KEY_POWER = "power";
    protected MyAdapter mAdapter;
    private final ContentObserver mAirplaneModeObserver;
    private ToggleAction mAirplaneModeOn;
    private final AudioManager mAudioManager;
    private final Executor mBackgroundExecutor;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final ConfigurationController mConfigurationController;
    private final Context mContext;
    private final DevicePolicyManager mDevicePolicyManager;
    @VisibleForTesting
    protected ActionsDialogLite mDialog;
    private final IDreamManager mDreamManager;
    private final EmergencyAffordanceManager mEmergencyAffordanceManager;
    protected final GlobalSettings mGlobalSettings;
    private boolean mHasTelephony;
    private boolean mHasVibrator;
    private final IActivityManager mIActivityManager;
    private final IWindowManager mIWindowManager;
    private final GlobalActionsInfoProvider mInfoProvider;
    private final KeyguardStateController mKeyguardStateController;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final LockPatternUtils mLockPatternUtils;
    protected Handler mMainHandler;
    private final MetricsLogger mMetricsLogger;
    protected final NotificationShadeWindowController mNotificationShadeWindowController;
    protected MyOverflowAdapter mOverflowAdapter;
    private final TelephonyCallback.ServiceStateListener mPhoneStateListener;
    protected MyPowerOptionsAdapter mPowerAdapter;
    protected final Resources mResources;
    private final RingerModeTracker mRingerModeTracker;
    private final ScreenshotHelper mScreenshotHelper;
    protected final SecureSettings mSecureSettings;
    private final boolean mShowSilentToggle;
    private Action mSilentModeAction;
    private int mSmallestScreenWidthDp;
    private final StatusBar mStatusBar;
    private final IStatusBarService mStatusBarService;
    private final SysUiState mSysUiState;
    private final SysuiColorExtractor mSysuiColorExtractor;
    private final TelecomManager mTelecomManager;
    private final TelephonyListenerManager mTelephonyListenerManager;
    private final TrustManager mTrustManager;
    private final UiEventLogger mUiEventLogger;
    private final UserManager mUserManager;
    private final GlobalActions.GlobalActionsManager mWindowManagerFuncs;
    private final LifecycleRegistry mLifecycle = new LifecycleRegistry(this);
    @VisibleForTesting
    protected final ArrayList<Action> mItems = new ArrayList<>();
    @VisibleForTesting
    protected final ArrayList<Action> mOverflowItems = new ArrayList<>();
    @VisibleForTesting
    protected final ArrayList<Action> mPowerItems = new ArrayList<>();
    private boolean mKeyguardShowing = false;
    private boolean mDeviceProvisioned = false;
    private ToggleState mAirplaneState = ToggleState.Off;
    private boolean mIsWaitingForEcmExit = false;
    private int mDialogPressDelay = 850;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite.5
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(action) || "android.intent.action.SCREEN_OFF".equals(action)) {
                String stringExtra = intent.getStringExtra("reason");
                if ("globalactions".equals(stringExtra)) {
                    return;
                }
                GlobalActionsDialogLite.this.mHandler.sendMessage(GlobalActionsDialogLite.this.mHandler.obtainMessage(0, stringExtra));
            } else if (!"android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED".equals(action) || intent.getBooleanExtra("android.telephony.extra.PHONE_IN_ECM_STATE", false) || !GlobalActionsDialogLite.this.mIsWaitingForEcmExit) {
            } else {
                GlobalActionsDialogLite.this.mIsWaitingForEcmExit = false;
                GlobalActionsDialogLite.this.changeAirplaneModeSystemSetting(true);
            }
        }
    };
    private Handler mHandler = new Handler() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite.8
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i != 0) {
                if (i != 1) {
                    return;
                }
                GlobalActionsDialogLite.this.refreshSilentMode();
                GlobalActionsDialogLite.this.mAdapter.notifyDataSetChanged();
            } else if (GlobalActionsDialogLite.this.mDialog == null) {
            } else {
                if ("dream".equals(message.obj)) {
                    GlobalActionsDialogLite.this.mDialog.completeDismiss();
                } else {
                    GlobalActionsDialogLite.this.mDialog.lambda$initializeLayout$4();
                }
                GlobalActionsDialogLite.this.mDialog = null;
            }
        }
    };

    /* loaded from: classes.dex */
    public interface Action {
        View create(Context context, View view, ViewGroup viewGroup, LayoutInflater layoutInflater);

        Drawable getIcon(Context context);

        CharSequence getMessage();

        int getMessageResId();

        boolean isEnabled();

        void onPress();

        default boolean shouldBeSeparated() {
            return false;
        }

        default boolean shouldShow() {
            return true;
        }

        boolean showBeforeProvisioning();

        boolean showDuringKeyguard();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public interface LongPressAction extends Action {
        boolean onLongPress();
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public enum GlobalActionsEvent implements UiEventLogger.UiEventEnum {
        GA_POWER_MENU_OPEN(337),
        GA_POWER_MENU_CLOSE(471),
        GA_BUGREPORT_PRESS(344),
        GA_BUGREPORT_LONG_PRESS(345),
        GA_EMERGENCY_DIALER_PRESS(346),
        GA_SCREENSHOT_PRESS(347),
        GA_SCREENSHOT_LONG_PRESS(348),
        GA_SHUTDOWN_PRESS(802),
        GA_SHUTDOWN_LONG_PRESS(803),
        GA_REBOOT_PRESS(349),
        GA_REBOOT_LONG_PRESS(804),
        GA_LOCKDOWN_PRESS(354),
        GA_OPEN_QS(805),
        GA_OPEN_POWER_VOLUP(806),
        GA_OPEN_LONG_PRESS_POWER(807),
        GA_CLOSE_LONG_PRESS_POWER(808),
        GA_CLOSE_BACK(809),
        GA_CLOSE_TAP_OUTSIDE(810),
        GA_CLOSE_POWER_VOLUP(811);
        
        private final int mId;

        GlobalActionsEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    public GlobalActionsDialogLite(Context context, GlobalActions.GlobalActionsManager globalActionsManager, AudioManager audioManager, IDreamManager iDreamManager, DevicePolicyManager devicePolicyManager, LockPatternUtils lockPatternUtils, BroadcastDispatcher broadcastDispatcher, TelephonyListenerManager telephonyListenerManager, GlobalSettings globalSettings, SecureSettings secureSettings, Vibrator vibrator, Resources resources, ConfigurationController configurationController, KeyguardStateController keyguardStateController, UserManager userManager, TrustManager trustManager, IActivityManager iActivityManager, TelecomManager telecomManager, MetricsLogger metricsLogger, SysuiColorExtractor sysuiColorExtractor, IStatusBarService iStatusBarService, NotificationShadeWindowController notificationShadeWindowController, IWindowManager iWindowManager, Executor executor, UiEventLogger uiEventLogger, GlobalActionsInfoProvider globalActionsInfoProvider, RingerModeTracker ringerModeTracker, SysUiState sysUiState, Handler handler, PackageManager packageManager, StatusBar statusBar, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        boolean z = false;
        TelephonyCallback.ServiceStateListener serviceStateListener = new TelephonyCallback.ServiceStateListener() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite.6
            @Override // android.telephony.TelephonyCallback.ServiceStateListener
            public void onServiceStateChanged(ServiceState serviceState) {
                if (!GlobalActionsDialogLite.this.mHasTelephony) {
                    return;
                }
                if (GlobalActionsDialogLite.this.mAirplaneModeOn == null) {
                    Log.d("GlobalActionsDialogLite", "Service changed before actions created");
                    return;
                }
                boolean z2 = serviceState.getState() == 3;
                GlobalActionsDialogLite.this.mAirplaneState = z2 ? ToggleState.On : ToggleState.Off;
                GlobalActionsDialogLite.this.mAirplaneModeOn.updateState(GlobalActionsDialogLite.this.mAirplaneState);
                GlobalActionsDialogLite.this.mAdapter.notifyDataSetChanged();
                GlobalActionsDialogLite.this.mOverflowAdapter.notifyDataSetChanged();
                GlobalActionsDialogLite.this.mPowerAdapter.notifyDataSetChanged();
            }
        };
        this.mPhoneStateListener = serviceStateListener;
        ContentObserver contentObserver = new ContentObserver(this.mMainHandler) { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite.7
            @Override // android.database.ContentObserver
            public void onChange(boolean z2) {
                GlobalActionsDialogLite.this.onAirplaneModeChanged();
            }
        };
        this.mAirplaneModeObserver = contentObserver;
        this.mContext = context;
        this.mWindowManagerFuncs = globalActionsManager;
        this.mAudioManager = audioManager;
        this.mDreamManager = iDreamManager;
        this.mDevicePolicyManager = devicePolicyManager;
        this.mLockPatternUtils = lockPatternUtils;
        this.mTelephonyListenerManager = telephonyListenerManager;
        this.mKeyguardStateController = keyguardStateController;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mGlobalSettings = globalSettings;
        this.mSecureSettings = secureSettings;
        this.mResources = resources;
        this.mConfigurationController = configurationController;
        this.mUserManager = userManager;
        this.mTrustManager = trustManager;
        this.mIActivityManager = iActivityManager;
        this.mTelecomManager = telecomManager;
        this.mMetricsLogger = metricsLogger;
        this.mUiEventLogger = uiEventLogger;
        this.mInfoProvider = globalActionsInfoProvider;
        this.mSysuiColorExtractor = sysuiColorExtractor;
        this.mStatusBarService = iStatusBarService;
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        this.mIWindowManager = iWindowManager;
        this.mBackgroundExecutor = executor;
        this.mRingerModeTracker = ringerModeTracker;
        this.mSysUiState = sysUiState;
        this.mMainHandler = handler;
        this.mSmallestScreenWidthDp = resources.getConfiguration().smallestScreenWidthDp;
        this.mStatusBar = statusBar;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED");
        broadcastDispatcher.registerReceiver(this.mBroadcastReceiver, intentFilter);
        this.mHasTelephony = packageManager.hasSystemFeature("android.hardware.telephony");
        telephonyListenerManager.addServiceStateListener(serviceStateListener);
        globalSettings.registerContentObserver(Settings.Global.getUriFor("airplane_mode_on"), true, contentObserver);
        if (vibrator != null && vibrator.hasVibrator()) {
            z = true;
        }
        this.mHasVibrator = z;
        boolean z2 = !resources.getBoolean(17891686);
        this.mShowSilentToggle = z2;
        if (z2) {
            ringerModeTracker.getRingerMode().observe(this, new Observer() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$$ExternalSyntheticLambda0
                @Override // androidx.lifecycle.Observer
                public final void onChanged(Object obj) {
                    GlobalActionsDialogLite.this.lambda$new$0((Integer) obj);
                }
            });
        }
        this.mEmergencyAffordanceManager = new EmergencyAffordanceManager(context);
        this.mScreenshotHelper = new ScreenshotHelper(context);
        configurationController.addCallback(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Integer num) {
        this.mHandler.sendEmptyMessage(1);
    }

    public void destroy() {
        this.mBroadcastDispatcher.unregisterReceiver(this.mBroadcastReceiver);
        this.mTelephonyListenerManager.removeServiceStateListener(this.mPhoneStateListener);
        this.mGlobalSettings.unregisterContentObserver(this.mAirplaneModeObserver);
        this.mConfigurationController.removeCallback(this);
    }

    public void showOrHideDialog(boolean z, boolean z2) {
        this.mKeyguardShowing = z;
        this.mDeviceProvisioned = z2;
        ActionsDialogLite actionsDialogLite = this.mDialog;
        if (actionsDialogLite != null && actionsDialogLite.isShowing()) {
            this.mWindowManagerFuncs.onGlobalActionsShown();
            this.mDialog.lambda$initializeLayout$4();
            this.mDialog = null;
            return;
        }
        handleShow();
    }

    public void dismissDialog() {
        this.mHandler.removeMessages(0);
        this.mHandler.sendEmptyMessage(0);
    }

    protected void awakenIfNecessary() {
        IDreamManager iDreamManager = this.mDreamManager;
        if (iDreamManager != null) {
            try {
                if (!iDreamManager.isDreaming()) {
                    return;
                }
                this.mDreamManager.awaken();
            } catch (RemoteException unused) {
            }
        }
    }

    protected void handleShow() {
        awakenIfNecessary();
        this.mDialog = createDialog();
        prepareDialog();
        WindowManager.LayoutParams attributes = this.mDialog.getWindow().getAttributes();
        attributes.setTitle("ActionsDialog");
        attributes.layoutInDisplayCutoutMode = 3;
        this.mDialog.getWindow().setAttributes(attributes);
        this.mDialog.getWindow().setFlags(131072, 131072);
        this.mDialog.show();
        this.mWindowManagerFuncs.onGlobalActionsShown();
    }

    @VisibleForTesting
    protected boolean shouldShowAction(Action action) {
        if (!this.mKeyguardShowing || action.showDuringKeyguard()) {
            if (!this.mDeviceProvisioned && !action.showBeforeProvisioning()) {
                return false;
            }
            return action.shouldShow();
        }
        return false;
    }

    @VisibleForTesting
    protected int getMaxShownPowerItems() {
        return this.mResources.getInteger(R$integer.power_menu_lite_max_columns) * this.mResources.getInteger(R$integer.power_menu_lite_max_rows);
    }

    private void addActionItem(Action action) {
        if (this.mItems.size() < getMaxShownPowerItems()) {
            this.mItems.add(action);
        } else {
            this.mOverflowItems.add(action);
        }
    }

    @VisibleForTesting
    protected String[] getDefaultActions() {
        return this.mResources.getStringArray(17236048);
    }

    private void addIfShouldShowAction(List<Action> list, Action action) {
        if (shouldShowAction(action)) {
            list.add(action);
        }
    }

    @VisibleForTesting
    protected void createActionItems() {
        if (!this.mHasVibrator) {
            this.mSilentModeAction = new SilentModeToggleAction();
        } else {
            this.mSilentModeAction = new SilentModeTriStateAction(this.mAudioManager, this.mHandler);
        }
        this.mAirplaneModeOn = new AirplaneModeAction();
        onAirplaneModeChanged();
        this.mItems.clear();
        this.mOverflowItems.clear();
        this.mPowerItems.clear();
        String[] defaultActions = getDefaultActions();
        ShutDownAction shutDownAction = new ShutDownAction();
        RestartAction restartAction = new RestartAction();
        ArraySet arraySet = new ArraySet();
        ArrayList<Action> arrayList = new ArrayList();
        CurrentUserProvider currentUserProvider = new CurrentUserProvider();
        if (this.mEmergencyAffordanceManager.needsEmergencyAffordance()) {
            addIfShouldShowAction(arrayList, new EmergencyAffordanceAction());
            arraySet.add("emergency");
        }
        for (String str : defaultActions) {
            if (!arraySet.contains(str)) {
                if (GLOBAL_ACTION_KEY_POWER.equals(str)) {
                    addIfShouldShowAction(arrayList, shutDownAction);
                } else if ("airplane".equals(str)) {
                    addIfShouldShowAction(arrayList, this.mAirplaneModeOn);
                } else if ("bugreport".equals(str)) {
                    if (shouldDisplayBugReport(currentUserProvider.get())) {
                        addIfShouldShowAction(arrayList, new BugReportAction());
                    }
                } else if ("silent".equals(str)) {
                    if (this.mShowSilentToggle) {
                        addIfShouldShowAction(arrayList, this.mSilentModeAction);
                    }
                } else if ("users".equals(str)) {
                    if (SystemProperties.getBoolean("fw.power_user_switcher", false)) {
                        addUserActions(arrayList, currentUserProvider.get());
                    }
                } else if ("settings".equals(str)) {
                    addIfShouldShowAction(arrayList, getSettingsAction());
                } else if ("lockdown".equals(str)) {
                    if (shouldDisplayLockdown(currentUserProvider.get())) {
                        addIfShouldShowAction(arrayList, new LockDownAction());
                    }
                } else if ("voiceassist".equals(str)) {
                    addIfShouldShowAction(arrayList, getVoiceAssistAction());
                } else if ("assist".equals(str)) {
                    addIfShouldShowAction(arrayList, getAssistAction());
                } else if ("restart".equals(str)) {
                    addIfShouldShowAction(arrayList, restartAction);
                } else if ("screenshot".equals(str)) {
                    addIfShouldShowAction(arrayList, new ScreenshotAction());
                } else if ("logout".equals(str)) {
                    if (this.mDevicePolicyManager.isLogoutEnabled() && currentUserProvider.get() != null && currentUserProvider.get().id != 0) {
                        addIfShouldShowAction(arrayList, new LogoutAction());
                    }
                } else if ("emergency".equals(str)) {
                    addIfShouldShowAction(arrayList, new EmergencyDialerAction());
                } else {
                    Log.e("GlobalActionsDialogLite", "Invalid global action key " + str);
                }
                arraySet.add(str);
            }
        }
        if (arrayList.contains(shutDownAction) && arrayList.contains(restartAction) && arrayList.size() > getMaxShownPowerItems()) {
            int min = Math.min(arrayList.indexOf(restartAction), arrayList.indexOf(shutDownAction));
            arrayList.remove(shutDownAction);
            arrayList.remove(restartAction);
            this.mPowerItems.add(shutDownAction);
            this.mPowerItems.add(restartAction);
            arrayList.add(min, new PowerOptionsAction());
        }
        for (Action action : arrayList) {
            addActionItem(action);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onRotate() {
        createActionItems();
    }

    protected void initDialogItems() {
        createActionItems();
        this.mAdapter = new MyAdapter();
        this.mOverflowAdapter = new MyOverflowAdapter();
        this.mPowerAdapter = new MyPowerOptionsAdapter();
    }

    protected ActionsDialogLite createDialog() {
        initDialogItems();
        ActionsDialogLite actionsDialogLite = new ActionsDialogLite(this.mContext, R$style.Theme_SystemUI_Dialog_GlobalActionsLite, this.mAdapter, this.mOverflowAdapter, this.mSysuiColorExtractor, this.mStatusBarService, this.mNotificationShadeWindowController, this.mSysUiState, new Runnable() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                GlobalActionsDialogLite.this.onRotate();
            }
        }, this.mKeyguardShowing, this.mPowerAdapter, this.mUiEventLogger, this.mInfoProvider, this.mStatusBar, this.mKeyguardUpdateMonitor, this.mLockPatternUtils);
        actionsDialogLite.setOnDismissListener(this);
        actionsDialogLite.setOnShowListener(this);
        return actionsDialogLite;
    }

    @VisibleForTesting
    boolean shouldDisplayLockdown(UserInfo userInfo) {
        if (userInfo == null) {
            return false;
        }
        int i = userInfo.id;
        if (!this.mKeyguardStateController.isMethodSecure()) {
            return false;
        }
        int strongAuthForUser = this.mLockPatternUtils.getStrongAuthForUser(i);
        return strongAuthForUser == 0 || strongAuthForUser == 4;
    }

    @VisibleForTesting
    boolean shouldDisplayBugReport(UserInfo userInfo) {
        if (this.mGlobalSettings.getInt("bugreport_in_power_menu", 0) != 0) {
            return userInfo == null || userInfo.isPrimary();
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onUiModeChanged() {
        this.mContext.getTheme().applyStyle(this.mContext.getThemeResId(), true);
        ActionsDialogLite actionsDialogLite = this.mDialog;
        if (actionsDialogLite == null || !actionsDialogLite.isShowing()) {
            return;
        }
        this.mDialog.refreshDialog();
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onConfigChanged(Configuration configuration) {
        int i;
        ActionsDialogLite actionsDialogLite = this.mDialog;
        if (actionsDialogLite == null || !actionsDialogLite.isShowing() || (i = configuration.smallestScreenWidthDp) == this.mSmallestScreenWidthDp) {
            return;
        }
        this.mSmallestScreenWidthDp = i;
        this.mDialog.refreshDialog();
    }

    @Override // com.android.systemui.plugins.GlobalActionsPanelPlugin.Callbacks
    public void dismissGlobalActionsMenu() {
        dismissDialog();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @VisibleForTesting
    /* loaded from: classes.dex */
    public final class PowerOptionsAction extends SinglePressAction {
        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public boolean showBeforeProvisioning() {
            return true;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public boolean showDuringKeyguard() {
            return true;
        }

        private PowerOptionsAction() {
            super(R$drawable.ic_settings_power, 17040332);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public void onPress() {
            ActionsDialogLite actionsDialogLite = GlobalActionsDialogLite.this.mDialog;
            if (actionsDialogLite != null) {
                actionsDialogLite.showPowerOptionsMenu();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: classes.dex */
    public final class ShutDownAction extends SinglePressAction implements LongPressAction {
        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public boolean showBeforeProvisioning() {
            return true;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public boolean showDuringKeyguard() {
            return true;
        }

        ShutDownAction() {
            super(17301552, 17040331);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.LongPressAction
        public boolean onLongPress() {
            GlobalActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_SHUTDOWN_LONG_PRESS);
            if (!GlobalActionsDialogLite.this.mUserManager.hasUserRestriction("no_safe_boot")) {
                GlobalActionsDialogLite.this.mWindowManagerFuncs.reboot(true);
                return true;
            }
            return false;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public void onPress() {
            if (ActivityManager.isUserAMonkey()) {
                return;
            }
            GlobalActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_SHUTDOWN_PRESS);
            GlobalActionsDialogLite.this.mWindowManagerFuncs.shutdown();
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    protected abstract class EmergencyAction extends SinglePressAction {
        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public boolean shouldBeSeparated() {
            return false;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public boolean showBeforeProvisioning() {
            return true;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public boolean showDuringKeyguard() {
            return true;
        }

        EmergencyAction(int i, int i2) {
            super(i, i2);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.SinglePressAction, com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public View create(Context context, View view, ViewGroup viewGroup, LayoutInflater layoutInflater) {
            View create = super.create(context, view, viewGroup, layoutInflater);
            int emergencyTextColor = GlobalActionsDialogLite.this.getEmergencyTextColor(context);
            int emergencyIconColor = GlobalActionsDialogLite.this.getEmergencyIconColor(context);
            int emergencyBackgroundColor = GlobalActionsDialogLite.this.getEmergencyBackgroundColor(context);
            TextView textView = (TextView) create.findViewById(16908299);
            textView.setTextColor(emergencyTextColor);
            textView.setSelected(true);
            ImageView imageView = (ImageView) create.findViewById(16908294);
            imageView.getDrawable().setTint(emergencyIconColor);
            imageView.setBackgroundTintList(ColorStateList.valueOf(emergencyBackgroundColor));
            create.setBackgroundTintList(ColorStateList.valueOf(emergencyBackgroundColor));
            return create;
        }
    }

    protected int getEmergencyTextColor(Context context) {
        return context.getResources().getColor(R$color.global_actions_lite_text);
    }

    protected int getEmergencyIconColor(Context context) {
        return context.getResources().getColor(R$color.global_actions_lite_emergency_icon);
    }

    protected int getEmergencyBackgroundColor(Context context) {
        return context.getResources().getColor(R$color.global_actions_lite_emergency_background);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class EmergencyAffordanceAction extends EmergencyAction {
        EmergencyAffordanceAction() {
            super(17302210, 17040327);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public void onPress() {
            GlobalActionsDialogLite.this.mEmergencyAffordanceManager.performEmergencyCall();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: classes.dex */
    public class EmergencyDialerAction extends EmergencyAction {
        private EmergencyDialerAction() {
            super(R$drawable.ic_emergency_star, 17040327);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public void onPress() {
            GlobalActionsDialogLite.this.mMetricsLogger.action(1569);
            GlobalActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_EMERGENCY_DIALER_PRESS);
            if (GlobalActionsDialogLite.this.mTelecomManager != null) {
                GlobalActionsDialogLite.this.mStatusBar.collapseShade();
                Intent createLaunchEmergencyDialerIntent = GlobalActionsDialogLite.this.mTelecomManager.createLaunchEmergencyDialerIntent(null);
                createLaunchEmergencyDialerIntent.addFlags(343932928);
                createLaunchEmergencyDialerIntent.putExtra("com.android.phone.EmergencyDialer.extra.ENTRY_TYPE", 2);
                GlobalActionsDialogLite.this.mContext.startActivityAsUser(createLaunchEmergencyDialerIntent, UserHandle.CURRENT);
            }
        }
    }

    @VisibleForTesting
    EmergencyDialerAction makeEmergencyDialerActionForTesting() {
        return new EmergencyDialerAction();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: classes.dex */
    public final class RestartAction extends SinglePressAction implements LongPressAction {
        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public boolean showBeforeProvisioning() {
            return true;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public boolean showDuringKeyguard() {
            return true;
        }

        RestartAction() {
            super(17302826, 17040333);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.LongPressAction
        public boolean onLongPress() {
            GlobalActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_REBOOT_LONG_PRESS);
            if (!GlobalActionsDialogLite.this.mUserManager.hasUserRestriction("no_safe_boot")) {
                GlobalActionsDialogLite.this.mWindowManagerFuncs.reboot(true);
                return true;
            }
            return false;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public void onPress() {
            if (ActivityManager.isUserAMonkey()) {
                return;
            }
            GlobalActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_REBOOT_PRESS);
            GlobalActionsDialogLite.this.mWindowManagerFuncs.reboot(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: classes.dex */
    public class ScreenshotAction extends SinglePressAction {
        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public boolean showBeforeProvisioning() {
            return false;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public boolean showDuringKeyguard() {
            return true;
        }

        ScreenshotAction() {
            super(17302828, 17040334);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public void onPress() {
            GlobalActionsDialogLite.this.mHandler.postDelayed(new Runnable() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite.ScreenshotAction.1
                @Override // java.lang.Runnable
                public void run() {
                    GlobalActionsDialogLite.this.mScreenshotHelper.takeScreenshot(1, true, true, 0, GlobalActionsDialogLite.this.mHandler, (Consumer) null);
                    GlobalActionsDialogLite.this.mMetricsLogger.action(1282);
                    GlobalActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_SCREENSHOT_PRESS);
                }
            }, GlobalActionsDialogLite.this.mDialogPressDelay);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public boolean shouldShow() {
            return is2ButtonNavigationEnabled();
        }

        boolean is2ButtonNavigationEnabled() {
            return 1 == GlobalActionsDialogLite.this.mContext.getResources().getInteger(17694868);
        }
    }

    @VisibleForTesting
    ScreenshotAction makeScreenshotActionForTesting() {
        return new ScreenshotAction();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: classes.dex */
    public class BugReportAction extends SinglePressAction implements LongPressAction {
        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public boolean showBeforeProvisioning() {
            return false;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public boolean showDuringKeyguard() {
            return true;
        }

        BugReportAction() {
            super(17302486, 17039803);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public void onPress() {
            if (ActivityManager.isUserAMonkey()) {
                return;
            }
            GlobalActionsDialogLite.this.mHandler.postDelayed(new Runnable() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite.BugReportAction.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        GlobalActionsDialogLite.this.mMetricsLogger.action(292);
                        GlobalActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_BUGREPORT_PRESS);
                        if (!GlobalActionsDialogLite.this.mIActivityManager.launchBugReportHandlerApp()) {
                            Log.w("GlobalActionsDialogLite", "Bugreport handler could not be launched");
                            GlobalActionsDialogLite.this.mIActivityManager.requestInteractiveBugReport();
                        }
                        GlobalActionsDialogLite.this.mStatusBar.collapseShade();
                    } catch (RemoteException unused) {
                    }
                }
            }, GlobalActionsDialogLite.this.mDialogPressDelay);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.LongPressAction
        public boolean onLongPress() {
            if (ActivityManager.isUserAMonkey()) {
                return false;
            }
            try {
                GlobalActionsDialogLite.this.mMetricsLogger.action(293);
                GlobalActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_BUGREPORT_LONG_PRESS);
                GlobalActionsDialogLite.this.mIActivityManager.requestFullBugReport();
                GlobalActionsDialogLite.this.mStatusBar.collapseShade();
            } catch (RemoteException unused) {
            }
            return false;
        }
    }

    @VisibleForTesting
    BugReportAction makeBugReportActionForTesting() {
        return new BugReportAction();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class LogoutAction extends SinglePressAction {
        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public boolean showBeforeProvisioning() {
            return false;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public boolean showDuringKeyguard() {
            return true;
        }

        private LogoutAction() {
            super(17302536, 17040330);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public void onPress() {
            GlobalActionsDialogLite.this.mHandler.postDelayed(new Runnable() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$LogoutAction$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    GlobalActionsDialogLite.LogoutAction.this.lambda$onPress$0();
                }
            }, GlobalActionsDialogLite.this.mDialogPressDelay);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPress$0() {
            try {
                int i = GlobalActionsDialogLite.this.getCurrentUser().id;
                GlobalActionsDialogLite.this.mIActivityManager.switchUser(0);
                GlobalActionsDialogLite.this.mIActivityManager.stopUser(i, true, (IStopUserCallback) null);
            } catch (RemoteException e) {
                Log.e("GlobalActionsDialogLite", "Couldn't logout user " + e);
            }
        }
    }

    private Action getSettingsAction() {
        return new SinglePressAction(17302835, 17040335) { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite.1
            @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
            public boolean showBeforeProvisioning() {
                return true;
            }

            @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
            public boolean showDuringKeyguard() {
                return true;
            }

            @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
            public void onPress() {
                Intent intent = new Intent("android.settings.SETTINGS");
                intent.addFlags(335544320);
                GlobalActionsDialogLite.this.mContext.startActivity(intent);
            }
        };
    }

    private Action getAssistAction() {
        return new SinglePressAction(17302304, 17040325) { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite.2
            @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
            public boolean showBeforeProvisioning() {
                return true;
            }

            @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
            public boolean showDuringKeyguard() {
                return true;
            }

            @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
            public void onPress() {
                Intent intent = new Intent("android.intent.action.ASSIST");
                intent.addFlags(335544320);
                GlobalActionsDialogLite.this.mContext.startActivity(intent);
            }
        };
    }

    private Action getVoiceAssistAction() {
        return new SinglePressAction(17302877, 17040339) { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite.3
            @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
            public boolean showBeforeProvisioning() {
                return true;
            }

            @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
            public boolean showDuringKeyguard() {
                return true;
            }

            @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
            public void onPress() {
                Intent intent = new Intent("android.intent.action.VOICE_ASSIST");
                intent.addFlags(335544320);
                GlobalActionsDialogLite.this.mContext.startActivity(intent);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: classes.dex */
    public class LockDownAction extends SinglePressAction {
        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public boolean showBeforeProvisioning() {
            return false;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public boolean showDuringKeyguard() {
            return true;
        }

        LockDownAction() {
            super(17302489, 17040329);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public void onPress() {
            GlobalActionsDialogLite.this.mLockPatternUtils.requireStrongAuth(32, -1);
            GlobalActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_LOCKDOWN_PRESS);
            try {
                GlobalActionsDialogLite.this.mIWindowManager.lockNow((Bundle) null);
                GlobalActionsDialogLite.this.mBackgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$LockDownAction$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        GlobalActionsDialogLite.LockDownAction.this.lambda$onPress$0();
                    }
                });
            } catch (RemoteException e) {
                Log.e("GlobalActionsDialogLite", "Error while trying to lock device.", e);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPress$0() {
            GlobalActionsDialogLite.this.lockProfiles();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void lockProfiles() {
        int[] enabledProfileIds;
        int i = getCurrentUser().id;
        for (int i2 : this.mUserManager.getEnabledProfileIds(i)) {
            if (i2 != i) {
                this.mTrustManager.setDeviceLockedForUser(i2, true);
            }
        }
    }

    protected UserInfo getCurrentUser() {
        try {
            return this.mIActivityManager.getCurrentUser();
        } catch (RemoteException unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class CurrentUserProvider {
        private boolean mFetched;
        private UserInfo mUserInfo;

        private CurrentUserProvider() {
            this.mUserInfo = null;
            this.mFetched = false;
        }

        UserInfo get() {
            if (!this.mFetched) {
                this.mFetched = true;
                this.mUserInfo = GlobalActionsDialogLite.this.getCurrentUser();
            }
            return this.mUserInfo;
        }
    }

    private void addUserActions(List<Action> list, UserInfo userInfo) {
        if (this.mUserManager.isUserSwitcherEnabled()) {
            for (final UserInfo userInfo2 : this.mUserManager.getUsers()) {
                if (userInfo2.supportsSwitchToByUser()) {
                    boolean z = true;
                    if (userInfo != null ? userInfo.id != userInfo2.id : userInfo2.id != 0) {
                        z = false;
                    }
                    String str = userInfo2.iconPath;
                    Drawable createFromPath = str != null ? Drawable.createFromPath(str) : null;
                    StringBuilder sb = new StringBuilder();
                    String str2 = userInfo2.name;
                    if (str2 == null) {
                        str2 = "Primary";
                    }
                    sb.append(str2);
                    sb.append(z ? " ✔" : "");
                    addIfShouldShowAction(list, new SinglePressAction(17302705, createFromPath, sb.toString()) { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite.4
                        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
                        public boolean showBeforeProvisioning() {
                            return false;
                        }

                        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
                        public boolean showDuringKeyguard() {
                            return true;
                        }

                        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
                        public void onPress() {
                            try {
                                GlobalActionsDialogLite.this.mIActivityManager.switchUser(userInfo2.id);
                            } catch (RemoteException e) {
                                Log.e("GlobalActionsDialogLite", "Couldn't switch user " + e);
                            }
                        }
                    });
                }
            }
        }
    }

    protected void prepareDialog() {
        refreshSilentMode();
        this.mAirplaneModeOn.updateState(this.mAirplaneState);
        this.mAdapter.notifyDataSetChanged();
        this.mLifecycle.setCurrentState(Lifecycle.State.RESUMED);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshSilentMode() {
        if (!this.mHasVibrator) {
            Integer mo1438getValue = this.mRingerModeTracker.getRingerMode().mo1438getValue();
            ((ToggleAction) this.mSilentModeAction).updateState(mo1438getValue != null && mo1438getValue.intValue() != 2 ? ToggleState.On : ToggleState.Off);
        }
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        if (this.mDialog == dialogInterface) {
            this.mDialog = null;
        }
        this.mUiEventLogger.log(GlobalActionsEvent.GA_POWER_MENU_CLOSE);
        this.mWindowManagerFuncs.onGlobalActionsHidden();
        this.mLifecycle.setCurrentState(Lifecycle.State.CREATED);
    }

    @Override // android.content.DialogInterface.OnShowListener
    public void onShow(DialogInterface dialogInterface) {
        this.mMetricsLogger.visible(1568);
        this.mUiEventLogger.log(GlobalActionsEvent.GA_POWER_MENU_OPEN);
    }

    /* loaded from: classes.dex */
    public class MyAdapter extends MultiListLayout.MultiListAdapter {
        @Override // android.widget.BaseAdapter, android.widget.ListAdapter
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        public MyAdapter() {
        }

        private int countItems(boolean z) {
            int i = 0;
            for (int i2 = 0; i2 < GlobalActionsDialogLite.this.mItems.size(); i2++) {
                if (GlobalActionsDialogLite.this.mItems.get(i2).shouldBeSeparated() == z) {
                    i++;
                }
            }
            return i;
        }

        @Override // com.android.systemui.MultiListLayout.MultiListAdapter
        public int countSeparatedItems() {
            return countItems(true);
        }

        @Override // com.android.systemui.MultiListLayout.MultiListAdapter
        public int countListItems() {
            return countItems(false);
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return countSeparatedItems() + countListItems();
        }

        @Override // android.widget.BaseAdapter, android.widget.ListAdapter
        public boolean isEnabled(int i) {
            return mo605getItem(i).isEnabled();
        }

        @Override // android.widget.Adapter
        /* renamed from: getItem */
        public Action mo605getItem(int i) {
            int i2 = 0;
            for (int i3 = 0; i3 < GlobalActionsDialogLite.this.mItems.size(); i3++) {
                Action action = GlobalActionsDialogLite.this.mItems.get(i3);
                if (GlobalActionsDialogLite.this.shouldShowAction(action)) {
                    if (i2 == i) {
                        return action;
                    }
                    i2++;
                }
            }
            throw new IllegalArgumentException("position " + i + " out of range of showable actions, filtered count=" + getCount() + ", keyguardshowing=" + GlobalActionsDialogLite.this.mKeyguardShowing + ", provisioned=" + GlobalActionsDialogLite.this.mDeviceProvisioned);
        }

        @Override // android.widget.Adapter
        public View getView(final int i, View view, ViewGroup viewGroup) {
            Action mo605getItem = mo605getItem(i);
            View create = mo605getItem.create(GlobalActionsDialogLite.this.mContext, view, viewGroup, LayoutInflater.from(GlobalActionsDialogLite.this.mContext));
            create.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$MyAdapter$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    GlobalActionsDialogLite.MyAdapter.this.lambda$getView$0(i, view2);
                }
            });
            if (mo605getItem instanceof LongPressAction) {
                create.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$MyAdapter$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnLongClickListener
                    public final boolean onLongClick(View view2) {
                        boolean lambda$getView$1;
                        lambda$getView$1 = GlobalActionsDialogLite.MyAdapter.this.lambda$getView$1(i, view2);
                        return lambda$getView$1;
                    }
                });
            }
            return create;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getView$0(int i, View view) {
            onClickItem(i);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$getView$1(int i, View view) {
            return onLongClickItem(i);
        }

        public boolean onLongClickItem(int i) {
            Action mo605getItem = GlobalActionsDialogLite.this.mAdapter.mo605getItem(i);
            if (mo605getItem instanceof LongPressAction) {
                ActionsDialogLite actionsDialogLite = GlobalActionsDialogLite.this.mDialog;
                if (actionsDialogLite != null) {
                    actionsDialogLite.lambda$initializeLayout$4();
                } else {
                    Log.w("GlobalActionsDialogLite", "Action long-clicked while mDialog is null.");
                }
                return ((LongPressAction) mo605getItem).onLongPress();
            }
            return false;
        }

        public void onClickItem(int i) {
            Action mo605getItem = GlobalActionsDialogLite.this.mAdapter.mo605getItem(i);
            if (!(mo605getItem instanceof SilentModeTriStateAction)) {
                ActionsDialogLite actionsDialogLite = GlobalActionsDialogLite.this.mDialog;
                if (actionsDialogLite != null) {
                    if (!(mo605getItem instanceof PowerOptionsAction)) {
                        actionsDialogLite.lambda$initializeLayout$4();
                    }
                } else {
                    Log.w("GlobalActionsDialogLite", "Action clicked while mDialog is null.");
                }
                mo605getItem.onPress();
            }
        }

        @Override // com.android.systemui.MultiListLayout.MultiListAdapter
        public boolean shouldBeSeparated(int i) {
            return mo605getItem(i).shouldBeSeparated();
        }
    }

    /* loaded from: classes.dex */
    public class MyPowerOptionsAdapter extends BaseAdapter {
        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        public MyPowerOptionsAdapter() {
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return GlobalActionsDialogLite.this.mPowerItems.size();
        }

        @Override // android.widget.Adapter
        /* renamed from: getItem */
        public Action mo608getItem(int i) {
            return GlobalActionsDialogLite.this.mPowerItems.get(i);
        }

        @Override // android.widget.Adapter
        public View getView(final int i, View view, ViewGroup viewGroup) {
            Action mo608getItem = mo608getItem(i);
            if (mo608getItem == null) {
                Log.w("GlobalActionsDialogLite", "No power options action found at position: " + i);
                return null;
            }
            int i2 = R$layout.global_actions_power_item;
            if (view == null) {
                view = LayoutInflater.from(GlobalActionsDialogLite.this.mContext).inflate(i2, viewGroup, false);
            }
            view.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$MyPowerOptionsAdapter$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    GlobalActionsDialogLite.MyPowerOptionsAdapter.this.lambda$getView$0(i, view2);
                }
            });
            if (mo608getItem instanceof LongPressAction) {
                view.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$MyPowerOptionsAdapter$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnLongClickListener
                    public final boolean onLongClick(View view2) {
                        boolean lambda$getView$1;
                        lambda$getView$1 = GlobalActionsDialogLite.MyPowerOptionsAdapter.this.lambda$getView$1(i, view2);
                        return lambda$getView$1;
                    }
                });
            }
            ImageView imageView = (ImageView) view.findViewById(16908294);
            TextView textView = (TextView) view.findViewById(16908299);
            textView.setSelected(true);
            imageView.setImageDrawable(mo608getItem.getIcon(GlobalActionsDialogLite.this.mContext));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (mo608getItem.getMessage() != null) {
                textView.setText(mo608getItem.getMessage());
            } else {
                textView.setText(mo608getItem.getMessageResId());
            }
            return view;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getView$0(int i, View view) {
            onClickItem(i);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$getView$1(int i, View view) {
            return onLongClickItem(i);
        }

        private boolean onLongClickItem(int i) {
            Action mo608getItem = mo608getItem(i);
            if (mo608getItem instanceof LongPressAction) {
                ActionsDialogLite actionsDialogLite = GlobalActionsDialogLite.this.mDialog;
                if (actionsDialogLite != null) {
                    actionsDialogLite.lambda$initializeLayout$4();
                } else {
                    Log.w("GlobalActionsDialogLite", "Action long-clicked while mDialog is null.");
                }
                return ((LongPressAction) mo608getItem).onLongPress();
            }
            return false;
        }

        private void onClickItem(int i) {
            Action mo608getItem = mo608getItem(i);
            if (!(mo608getItem instanceof SilentModeTriStateAction)) {
                ActionsDialogLite actionsDialogLite = GlobalActionsDialogLite.this.mDialog;
                if (actionsDialogLite != null) {
                    actionsDialogLite.lambda$initializeLayout$4();
                } else {
                    Log.w("GlobalActionsDialogLite", "Action clicked while mDialog is null.");
                }
                mo608getItem.onPress();
            }
        }
    }

    /* loaded from: classes.dex */
    public class MyOverflowAdapter extends BaseAdapter {
        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        public MyOverflowAdapter() {
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return GlobalActionsDialogLite.this.mOverflowItems.size();
        }

        @Override // android.widget.Adapter
        /* renamed from: getItem */
        public Action mo606getItem(int i) {
            return GlobalActionsDialogLite.this.mOverflowItems.get(i);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            Action mo606getItem = mo606getItem(i);
            if (mo606getItem == null) {
                Log.w("GlobalActionsDialogLite", "No overflow action found at position: " + i);
                return null;
            }
            int i2 = R$layout.controls_more_item;
            if (view == null) {
                view = LayoutInflater.from(GlobalActionsDialogLite.this.mContext).inflate(i2, viewGroup, false);
            }
            TextView textView = (TextView) view;
            if (mo606getItem.getMessageResId() != 0) {
                textView.setText(mo606getItem.getMessageResId());
            } else {
                textView.setText(mo606getItem.getMessage());
            }
            return textView;
        }

        protected boolean onLongClickItem(int i) {
            Action mo606getItem = mo606getItem(i);
            if (mo606getItem instanceof LongPressAction) {
                ActionsDialogLite actionsDialogLite = GlobalActionsDialogLite.this.mDialog;
                if (actionsDialogLite != null) {
                    actionsDialogLite.lambda$initializeLayout$4();
                } else {
                    Log.w("GlobalActionsDialogLite", "Action long-clicked while mDialog is null.");
                }
                return ((LongPressAction) mo606getItem).onLongPress();
            }
            return false;
        }

        protected void onClickItem(int i) {
            Action mo606getItem = mo606getItem(i);
            if (!(mo606getItem instanceof SilentModeTriStateAction)) {
                ActionsDialogLite actionsDialogLite = GlobalActionsDialogLite.this.mDialog;
                if (actionsDialogLite != null) {
                    actionsDialogLite.lambda$initializeLayout$4();
                } else {
                    Log.w("GlobalActionsDialogLite", "Action clicked while mDialog is null.");
                }
                mo606getItem.onPress();
            }
        }
    }

    /* loaded from: classes.dex */
    private abstract class SinglePressAction implements Action {
        private final Drawable mIcon;
        private final int mIconResId;
        private final CharSequence mMessage;
        private final int mMessageResId;

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public boolean isEnabled() {
            return true;
        }

        protected SinglePressAction(int i, int i2) {
            this.mIconResId = i;
            this.mMessageResId = i2;
            this.mMessage = null;
            this.mIcon = null;
        }

        protected SinglePressAction(int i, Drawable drawable, CharSequence charSequence) {
            this.mIconResId = i;
            this.mMessageResId = 0;
            this.mMessage = charSequence;
            this.mIcon = drawable;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public int getMessageResId() {
            return this.mMessageResId;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public CharSequence getMessage() {
            return this.mMessage;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public Drawable getIcon(Context context) {
            Drawable drawable = this.mIcon;
            return drawable != null ? drawable : context.getDrawable(this.mIconResId);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public View create(Context context, View view, ViewGroup viewGroup, LayoutInflater layoutInflater) {
            View inflate = layoutInflater.inflate(GlobalActionsDialogLite.this.getGridItemLayoutResource(), viewGroup, false);
            inflate.setId(View.generateViewId());
            ImageView imageView = (ImageView) inflate.findViewById(16908294);
            TextView textView = (TextView) inflate.findViewById(16908299);
            textView.setSelected(true);
            imageView.setImageDrawable(getIcon(context));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            CharSequence charSequence = this.mMessage;
            if (charSequence != null) {
                textView.setText(charSequence);
            } else {
                textView.setText(this.mMessageResId);
            }
            return inflate;
        }
    }

    protected int getGridItemLayoutResource() {
        return R$layout.global_actions_grid_item_lite;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum ToggleState {
        Off(false),
        TurningOn(true),
        TurningOff(true),
        On(false);
        
        private final boolean mInTransition;

        ToggleState(boolean z) {
            this.mInTransition = z;
        }

        public boolean inTransition() {
            return this.mInTransition;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public abstract class ToggleAction implements Action {
        protected int mDisabledIconResid;
        protected int mDisabledStatusMessageResId;
        protected int mEnabledIconResId;
        protected int mEnabledStatusMessageResId;
        protected int mMessageResId;
        protected ToggleState mState = ToggleState.Off;

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public CharSequence getMessage() {
            return null;
        }

        abstract void onToggle(boolean z);

        void willCreate() {
        }

        ToggleAction(int i, int i2, int i3, int i4, int i5) {
            this.mEnabledIconResId = i;
            this.mDisabledIconResid = i2;
            this.mMessageResId = i3;
            this.mEnabledStatusMessageResId = i4;
            this.mDisabledStatusMessageResId = i5;
        }

        private boolean isOn() {
            ToggleState toggleState = this.mState;
            return toggleState == ToggleState.On || toggleState == ToggleState.TurningOn;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public int getMessageResId() {
            return isOn() ? this.mEnabledStatusMessageResId : this.mDisabledStatusMessageResId;
        }

        private int getIconResId() {
            return isOn() ? this.mEnabledIconResId : this.mDisabledIconResid;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public Drawable getIcon(Context context) {
            return context.getDrawable(getIconResId());
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public View create(Context context, View view, ViewGroup viewGroup, LayoutInflater layoutInflater) {
            willCreate();
            View inflate = layoutInflater.inflate(R$layout.global_actions_grid_item_v2, viewGroup, false);
            ViewGroup.LayoutParams layoutParams = inflate.getLayoutParams();
            layoutParams.width = -2;
            inflate.setLayoutParams(layoutParams);
            ImageView imageView = (ImageView) inflate.findViewById(16908294);
            TextView textView = (TextView) inflate.findViewById(16908299);
            boolean isEnabled = isEnabled();
            if (textView != null) {
                textView.setText(getMessageResId());
                textView.setEnabled(isEnabled);
                textView.setSelected(true);
            }
            if (imageView != null) {
                imageView.setImageDrawable(context.getDrawable(getIconResId()));
                imageView.setEnabled(isEnabled);
            }
            inflate.setEnabled(isEnabled);
            return inflate;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final void onPress() {
            if (this.mState.inTransition()) {
                Log.w("GlobalActionsDialogLite", "shouldn't be able to toggle when in transition");
                return;
            }
            boolean z = this.mState != ToggleState.On;
            onToggle(z);
            changeStateFromPress(z);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public boolean isEnabled() {
            return !this.mState.inTransition();
        }

        protected void changeStateFromPress(boolean z) {
            this.mState = z ? ToggleState.On : ToggleState.Off;
        }

        public void updateState(ToggleState toggleState) {
            this.mState = toggleState;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class AirplaneModeAction extends ToggleAction {
        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public boolean showBeforeProvisioning() {
            return false;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public boolean showDuringKeyguard() {
            return true;
        }

        AirplaneModeAction() {
            super(17302482, 17302484, 17040343, 17040342, 17040341);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.ToggleAction
        void onToggle(boolean z) {
            if (!GlobalActionsDialogLite.this.mHasTelephony || !((Boolean) TelephonyProperties.in_ecm_mode().orElse(Boolean.FALSE)).booleanValue()) {
                GlobalActionsDialogLite.this.changeAirplaneModeSystemSetting(z);
                return;
            }
            GlobalActionsDialogLite.this.mIsWaitingForEcmExit = true;
            Intent intent = new Intent("android.telephony.action.SHOW_NOTICE_ECM_BLOCK_OTHERS", (Uri) null);
            intent.addFlags(268435456);
            GlobalActionsDialogLite.this.mContext.startActivity(intent);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.ToggleAction
        protected void changeStateFromPress(boolean z) {
            if (GlobalActionsDialogLite.this.mHasTelephony && !((Boolean) TelephonyProperties.in_ecm_mode().orElse(Boolean.FALSE)).booleanValue()) {
                ToggleState toggleState = z ? ToggleState.TurningOn : ToggleState.TurningOff;
                this.mState = toggleState;
                GlobalActionsDialogLite.this.mAirplaneState = toggleState;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class SilentModeToggleAction extends ToggleAction {
        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public boolean showBeforeProvisioning() {
            return false;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public boolean showDuringKeyguard() {
            return true;
        }

        SilentModeToggleAction() {
            super(17302322, 17302321, 17040338, 17040337, 17040336);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.ToggleAction
        void onToggle(boolean z) {
            if (z) {
                GlobalActionsDialogLite.this.mAudioManager.setRingerMode(0);
            } else {
                GlobalActionsDialogLite.this.mAudioManager.setRingerMode(2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SilentModeTriStateAction implements Action, View.OnClickListener {
        private static final int[] ITEM_IDS = {16909268, 16909269, 16909270};
        private final AudioManager mAudioManager;
        private final Handler mHandler;

        private int indexToRingerMode(int i) {
            return i;
        }

        private int ringerModeToIndex(int i) {
            return i;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public Drawable getIcon(Context context) {
            return null;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public CharSequence getMessage() {
            return null;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public int getMessageResId() {
            return 0;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public boolean isEnabled() {
            return true;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public void onPress() {
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public boolean showBeforeProvisioning() {
            return false;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public boolean showDuringKeyguard() {
            return true;
        }

        SilentModeTriStateAction(AudioManager audioManager, Handler handler) {
            this.mAudioManager = audioManager;
            this.mHandler = handler;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public View create(Context context, View view, ViewGroup viewGroup, LayoutInflater layoutInflater) {
            View inflate = layoutInflater.inflate(17367169, viewGroup, false);
            int ringerModeToIndex = ringerModeToIndex(this.mAudioManager.getRingerMode());
            int i = 0;
            while (i < 3) {
                View findViewById = inflate.findViewById(ITEM_IDS[i]);
                findViewById.setSelected(ringerModeToIndex == i);
                findViewById.setTag(Integer.valueOf(i));
                findViewById.setOnClickListener(this);
                i++;
            }
            return inflate;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (!(view.getTag() instanceof Integer)) {
                return;
            }
            this.mAudioManager.setRingerMode(indexToRingerMode(((Integer) view.getTag()).intValue()));
            this.mHandler.sendEmptyMessageDelayed(0, 300L);
        }
    }

    @VisibleForTesting
    void setZeroDialogPressDelayForTesting() {
        this.mDialogPressDelay = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAirplaneModeChanged() {
        if (this.mHasTelephony || this.mAirplaneModeOn == null) {
            return;
        }
        boolean z = false;
        if (this.mGlobalSettings.getInt("airplane_mode_on", 0) == 1) {
            z = true;
        }
        ToggleState toggleState = z ? ToggleState.On : ToggleState.Off;
        this.mAirplaneState = toggleState;
        this.mAirplaneModeOn.updateState(toggleState);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeAirplaneModeSystemSetting(boolean z) {
        this.mGlobalSettings.putInt("airplane_mode_on", z ? 1 : 0);
        Intent intent = new Intent("android.intent.action.AIRPLANE_MODE");
        intent.addFlags(536870912);
        intent.putExtra("state", z);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
        if (!this.mHasTelephony) {
            this.mAirplaneState = z ? ToggleState.On : ToggleState.Off;
        }
    }

    @Override // androidx.lifecycle.LifecycleOwner
    /* renamed from: getLifecycle */
    public Lifecycle mo1437getLifecycle() {
        return this.mLifecycle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: classes.dex */
    public static class ActionsDialogLite extends Dialog implements ColorExtractor.OnColorsChangedListener {
        protected final MyAdapter mAdapter;
        protected Drawable mBackgroundDrawable;
        protected final SysuiColorExtractor mColorExtractor;
        protected ViewGroup mContainer;
        protected final Context mContext;
        private GestureDetector mGestureDetector;
        protected MultiListLayout mGlobalActionsLayout;
        private GlobalActionsInfoProvider mInfoProvider;
        private boolean mKeyguardShowing;
        private KeyguardUpdateMonitor mKeyguardUpdateMonitor;
        private LockPatternUtils mLockPatternUtils;
        protected final NotificationShadeWindowController mNotificationShadeWindowController;
        protected final Runnable mOnRotateCallback;
        protected final MyOverflowAdapter mOverflowAdapter;
        private ListPopupWindow mOverflowPopup;
        protected final MyPowerOptionsAdapter mPowerOptionsAdapter;
        private Dialog mPowerOptionsDialog;
        protected float mScrimAlpha;
        protected boolean mShowing;
        private StatusBar mStatusBar;
        protected final IStatusBarService mStatusBarService;
        protected final SysUiState mSysUiState;
        private UiEventLogger mUiEventLogger;
        protected final IBinder mToken = new Binder();
        @VisibleForTesting
        protected GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite.ActionsDialogLite.1
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onDown(MotionEvent motionEvent) {
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                ActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_CLOSE_TAP_OUTSIDE);
                ActionsDialogLite.this.cancel();
                return false;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (f2 >= 0.0f || f2 <= f || motionEvent.getY() > ActionsDialogLite.this.mStatusBar.getStatusBarHeight()) {
                    return false;
                }
                ActionsDialogLite.this.openShadeAndDismiss();
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (f2 <= 0.0f || Math.abs(f2) <= Math.abs(f) || motionEvent.getY() > ActionsDialogLite.this.mStatusBar.getStatusBarHeight()) {
                    return false;
                }
                ActionsDialogLite.this.openShadeAndDismiss();
                return true;
            }
        };

        ActionsDialogLite(Context context, int i, MyAdapter myAdapter, MyOverflowAdapter myOverflowAdapter, SysuiColorExtractor sysuiColorExtractor, IStatusBarService iStatusBarService, NotificationShadeWindowController notificationShadeWindowController, SysUiState sysUiState, Runnable runnable, boolean z, MyPowerOptionsAdapter myPowerOptionsAdapter, UiEventLogger uiEventLogger, GlobalActionsInfoProvider globalActionsInfoProvider, StatusBar statusBar, KeyguardUpdateMonitor keyguardUpdateMonitor, LockPatternUtils lockPatternUtils) {
            super(context, i);
            this.mContext = context;
            this.mAdapter = myAdapter;
            this.mOverflowAdapter = myOverflowAdapter;
            this.mPowerOptionsAdapter = myPowerOptionsAdapter;
            this.mColorExtractor = sysuiColorExtractor;
            this.mStatusBarService = iStatusBarService;
            this.mNotificationShadeWindowController = notificationShadeWindowController;
            this.mSysUiState = sysUiState;
            this.mOnRotateCallback = runnable;
            this.mKeyguardShowing = z;
            this.mUiEventLogger = uiEventLogger;
            this.mInfoProvider = globalActionsInfoProvider;
            this.mStatusBar = statusBar;
            this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
            this.mLockPatternUtils = lockPatternUtils;
            this.mGestureDetector = new GestureDetector(context, this.mGestureListener);
            Window window = getWindow();
            window.requestFeature(1);
            window.getDecorView();
            window.getAttributes().systemUiVisibility |= 768;
            window.setLayout(-1, -1);
            window.addFlags(17367296);
            window.setType(2020);
            window.getAttributes().setFitInsetsTypes(0);
            setTitle(17040340);
            initializeLayout();
        }

        @Override // android.app.Dialog
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return this.mGestureDetector.onTouchEvent(motionEvent) || super.onTouchEvent(motionEvent);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void openShadeAndDismiss() {
            this.mUiEventLogger.log(GlobalActionsEvent.GA_CLOSE_TAP_OUTSIDE);
            if (this.mStatusBar.isKeyguardShowing()) {
                this.mStatusBar.animateExpandSettingsPanel(null);
            } else {
                this.mStatusBar.animateExpandNotificationsPanel();
            }
            lambda$initializeLayout$4();
        }

        private ListPopupWindow createPowerOverflowPopup() {
            GlobalActionsPopupMenu globalActionsPopupMenu = new GlobalActionsPopupMenu(new ContextThemeWrapper(this.mContext, R$style.Control_ListPopupWindow), false);
            globalActionsPopupMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda5
                @Override // android.widget.AdapterView.OnItemClickListener
                public final void onItemClick(AdapterView adapterView, View view, int i, long j) {
                    GlobalActionsDialogLite.ActionsDialogLite.this.lambda$createPowerOverflowPopup$0(adapterView, view, i, j);
                }
            });
            globalActionsPopupMenu.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda6
                @Override // android.widget.AdapterView.OnItemLongClickListener
                public final boolean onItemLongClick(AdapterView adapterView, View view, int i, long j) {
                    boolean lambda$createPowerOverflowPopup$1;
                    lambda$createPowerOverflowPopup$1 = GlobalActionsDialogLite.ActionsDialogLite.this.lambda$createPowerOverflowPopup$1(adapterView, view, i, j);
                    return lambda$createPowerOverflowPopup$1;
                }
            });
            globalActionsPopupMenu.setAnchorView(findViewById(R$id.global_actions_overflow_button));
            globalActionsPopupMenu.setAdapter(this.mOverflowAdapter);
            return globalActionsPopupMenu;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$createPowerOverflowPopup$0(AdapterView adapterView, View view, int i, long j) {
            this.mOverflowAdapter.onClickItem(i);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$createPowerOverflowPopup$1(AdapterView adapterView, View view, int i, long j) {
            return this.mOverflowAdapter.onLongClickItem(i);
        }

        public void showPowerOptionsMenu() {
            Dialog create = GlobalActionsPowerDialog.create(this.mContext, this.mPowerOptionsAdapter);
            this.mPowerOptionsDialog = create;
            create.show();
        }

        protected void showPowerOverflowMenu() {
            ListPopupWindow createPowerOverflowPopup = createPowerOverflowPopup();
            this.mOverflowPopup = createPowerOverflowPopup;
            createPowerOverflowPopup.show();
        }

        protected int getLayoutResource() {
            return R$layout.global_actions_grid_lite;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void initializeLayout() {
            setContentView(getLayoutResource());
            fixNavBarClipping();
            MultiListLayout multiListLayout = (MultiListLayout) findViewById(R$id.global_actions_view);
            this.mGlobalActionsLayout = multiListLayout;
            multiListLayout.setListViewAccessibilityDelegate(new View.AccessibilityDelegate() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite.ActionsDialogLite.2
                @Override // android.view.View.AccessibilityDelegate
                public boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
                    accessibilityEvent.getText().add(ActionsDialogLite.this.mContext.getString(17040340));
                    return true;
                }
            });
            this.mGlobalActionsLayout.setRotationListener(new MultiListLayout.RotationListener() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda7
                @Override // com.android.systemui.MultiListLayout.RotationListener
                public final void onRotate(int i, int i2) {
                    GlobalActionsDialogLite.ActionsDialogLite.this.onRotate(i, i2);
                }
            });
            this.mGlobalActionsLayout.setAdapter(this.mAdapter);
            ViewGroup viewGroup = (ViewGroup) findViewById(R$id.global_actions_container);
            this.mContainer = viewGroup;
            viewGroup.setOnTouchListener(new View.OnTouchListener() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda4
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view, MotionEvent motionEvent) {
                    boolean lambda$initializeLayout$2;
                    lambda$initializeLayout$2 = GlobalActionsDialogLite.ActionsDialogLite.this.lambda$initializeLayout$2(view, motionEvent);
                    return lambda$initializeLayout$2;
                }
            });
            View findViewById = findViewById(R$id.global_actions_overflow_button);
            if (findViewById != null) {
                if (this.mOverflowAdapter.getCount() > 0) {
                    findViewById.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda3
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            GlobalActionsDialogLite.ActionsDialogLite.this.lambda$initializeLayout$3(view);
                        }
                    });
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mGlobalActionsLayout.getLayoutParams();
                    layoutParams.setMarginEnd(0);
                    this.mGlobalActionsLayout.setLayoutParams(layoutParams);
                } else {
                    findViewById.setVisibility(8);
                    LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mGlobalActionsLayout.getLayoutParams();
                    layoutParams2.setMarginEnd(this.mContext.getResources().getDimensionPixelSize(R$dimen.global_actions_side_margin));
                    this.mGlobalActionsLayout.setLayoutParams(layoutParams2);
                }
            }
            if (this.mBackgroundDrawable == null) {
                this.mBackgroundDrawable = new ScrimDrawable();
                this.mScrimAlpha = 1.0f;
            }
            GlobalActionsInfoProvider globalActionsInfoProvider = this.mInfoProvider;
            if (globalActionsInfoProvider != null && globalActionsInfoProvider.shouldShowMessage()) {
                this.mInfoProvider.addPanel(this.mContext, this.mContainer, this.mAdapter.getCount(), new Runnable() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda8
                    @Override // java.lang.Runnable
                    public final void run() {
                        GlobalActionsDialogLite.ActionsDialogLite.this.lambda$initializeLayout$4();
                    }
                });
            }
            boolean userHasTrust = this.mKeyguardUpdateMonitor.getUserHasTrust(KeyguardUpdateMonitor.getCurrentUser());
            if (!this.mKeyguardShowing || !userHasTrust) {
                return;
            }
            this.mLockPatternUtils.requireCredentialEntry(KeyguardUpdateMonitor.getCurrentUser());
            showSmartLockDisabledMessage();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$initializeLayout$2(View view, MotionEvent motionEvent) {
            this.mGestureDetector.onTouchEvent(motionEvent);
            return view.onTouchEvent(motionEvent);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$initializeLayout$3(View view) {
            showPowerOverflowMenu();
        }

        protected void fixNavBarClipping() {
            ViewGroup viewGroup = (ViewGroup) findViewById(16908290);
            viewGroup.setClipChildren(false);
            viewGroup.setClipToPadding(false);
            ViewGroup viewGroup2 = (ViewGroup) viewGroup.getParent();
            viewGroup2.setClipChildren(false);
            viewGroup2.setClipToPadding(false);
        }

        private void showSmartLockDisabledMessage() {
            final View inflate = LayoutInflater.from(this.mContext).inflate(R$layout.global_actions_toast, this.mContainer, false);
            final int recommendedTimeoutMillis = ((AccessibilityManager) getContext().getSystemService("accessibility")).getRecommendedTimeoutMillis(3500, 2);
            inflate.setVisibility(0);
            inflate.setAlpha(0.0f);
            this.mContainer.addView(inflate);
            inflate.animate().alpha(1.0f).setDuration(333L).setListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite.ActionsDialogLite.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    inflate.animate().alpha(0.0f).setDuration(333L).setStartDelay(recommendedTimeoutMillis);
                }
            });
        }

        @Override // android.app.Dialog
        protected void onStart() {
            super.onStart();
            this.mGlobalActionsLayout.updateList();
            if (this.mBackgroundDrawable instanceof ScrimDrawable) {
                this.mColorExtractor.addOnColorsChangedListener(this);
                updateColors(this.mColorExtractor.getNeutralColors(), false);
            }
        }

        private void updateColors(ColorExtractor.GradientColors gradientColors, boolean z) {
            Drawable drawable = this.mBackgroundDrawable;
            if (!(drawable instanceof ScrimDrawable)) {
                return;
            }
            ((ScrimDrawable) drawable).setColor(-16777216, z);
            View decorView = getWindow().getDecorView();
            if (gradientColors.supportsDarkText()) {
                decorView.setSystemUiVisibility(8208);
            } else {
                decorView.setSystemUiVisibility(0);
            }
        }

        @Override // android.app.Dialog
        protected void onStop() {
            super.onStop();
            this.mColorExtractor.removeOnColorsChangedListener(this);
        }

        @Override // android.app.Dialog
        public void onBackPressed() {
            super.onBackPressed();
            this.mUiEventLogger.log(GlobalActionsEvent.GA_CLOSE_BACK);
        }

        @Override // android.app.Dialog
        public void show() {
            super.show();
            showDialog();
        }

        protected void showDialog() {
            this.mShowing = true;
            this.mNotificationShadeWindowController.setRequestTopUi(true, "GlobalActionsDialogLite");
            this.mSysUiState.setFlag(32768, true).commitUpdate(this.mContext.getDisplayId());
            final ViewGroup viewGroup = (ViewGroup) this.mGlobalActionsLayout.getRootView();
            viewGroup.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda2
                @Override // android.view.View.OnApplyWindowInsetsListener
                public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                    WindowInsets lambda$showDialog$5;
                    lambda$showDialog$5 = GlobalActionsDialogLite.ActionsDialogLite.lambda$showDialog$5(viewGroup, view, windowInsets);
                    return lambda$showDialog$5;
                }
            });
            this.mBackgroundDrawable.setAlpha(0);
            float animationOffsetX = this.mGlobalActionsLayout.getAnimationOffsetX();
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mContainer, "alpha", 0.0f, 1.0f);
            Interpolator interpolator = Interpolators.LINEAR_OUT_SLOW_IN;
            ofFloat.setInterpolator(interpolator);
            ofFloat.setDuration(183L);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    GlobalActionsDialogLite.ActionsDialogLite.this.lambda$showDialog$6(valueAnimator);
                }
            });
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.mContainer, "translationX", animationOffsetX, 0.0f);
            ofFloat2.setInterpolator(interpolator);
            ofFloat2.setDuration(350L);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ofFloat, ofFloat2);
            animatorSet.start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ WindowInsets lambda$showDialog$5(ViewGroup viewGroup, View view, WindowInsets windowInsets) {
            viewGroup.setPadding(windowInsets.getStableInsetLeft(), windowInsets.getStableInsetTop(), windowInsets.getStableInsetRight(), windowInsets.getStableInsetBottom());
            return WindowInsets.CONSUMED;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$showDialog$6(ValueAnimator valueAnimator) {
            this.mBackgroundDrawable.setAlpha((int) (valueAnimator.getAnimatedFraction() * this.mScrimAlpha * 255.0f));
        }

        @Override // android.app.Dialog, android.content.DialogInterface
        /* renamed from: dismiss */
        public void lambda$initializeLayout$4() {
            dismissWithAnimation(new Runnable() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    GlobalActionsDialogLite.ActionsDialogLite.this.lambda$dismiss$7();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: dismissInternal */
        public void lambda$dismiss$7() {
            this.mContainer.setTranslationX(0.0f);
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mContainer, "alpha", 1.0f, 0.0f);
            Interpolator interpolator = Interpolators.FAST_OUT_LINEAR_IN;
            ofFloat.setInterpolator(interpolator);
            ofFloat.setDuration(233L);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    GlobalActionsDialogLite.ActionsDialogLite.this.lambda$dismissInternal$8(valueAnimator);
                }
            });
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.mContainer, "translationX", 0.0f, this.mGlobalActionsLayout.getAnimationOffsetX());
            ofFloat2.setInterpolator(interpolator);
            ofFloat2.setDuration(350L);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ofFloat, ofFloat2);
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite.ActionsDialogLite.4
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ActionsDialogLite.this.completeDismiss();
                }
            });
            animatorSet.start();
            dismissOverflow(false);
            dismissPowerOptions(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$dismissInternal$8(ValueAnimator valueAnimator) {
            this.mBackgroundDrawable.setAlpha((int) ((1.0f - valueAnimator.getAnimatedFraction()) * this.mScrimAlpha * 255.0f));
        }

        void dismissWithAnimation(Runnable runnable) {
            if (!this.mShowing) {
                return;
            }
            this.mShowing = false;
            runnable.run();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void completeDismiss() {
            this.mShowing = false;
            dismissOverflow(true);
            dismissPowerOptions(true);
            this.mNotificationShadeWindowController.setRequestTopUi(false, "GlobalActionsDialogLite");
            this.mSysUiState.setFlag(32768, false).commitUpdate(this.mContext.getDisplayId());
            super.dismiss();
        }

        protected final void dismissOverflow(boolean z) {
            ListPopupWindow listPopupWindow = this.mOverflowPopup;
            if (listPopupWindow != null) {
                if (z) {
                    listPopupWindow.dismissImmediate();
                } else {
                    listPopupWindow.dismiss();
                }
            }
        }

        protected final void dismissPowerOptions(boolean z) {
            Dialog dialog = this.mPowerOptionsDialog;
            if (dialog != null) {
                if (z) {
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public final void setRotationSuggestionsEnabled(boolean z) {
            try {
                this.mStatusBarService.disable2ForUser(z ? 0 : 16, this.mToken, this.mContext.getPackageName(), Binder.getCallingUserHandle().getIdentifier());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        public void onColorsChanged(ColorExtractor colorExtractor, int i) {
            if (this.mKeyguardShowing) {
                if ((i & 2) == 0) {
                    return;
                }
                updateColors(colorExtractor.getColors(2), true);
            } else if ((i & 1) == 0) {
            } else {
                updateColors(colorExtractor.getColors(1), true);
            }
        }

        public void refreshDialog() {
            dismissOverflow(true);
            dismissPowerOptions(true);
            initializeLayout();
            this.mGlobalActionsLayout.updateList();
        }

        public void onRotate(int i, int i2) {
            if (this.mShowing) {
                this.mOnRotateCallback.run();
                refreshDialog();
            }
        }
    }
}
