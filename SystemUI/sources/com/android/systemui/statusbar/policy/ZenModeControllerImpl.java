package com.android.systemui.statusbar.policy;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.service.notification.ZenModeConfig;
import android.text.format.DateFormat;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.p012qs.SettingObserver;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.util.Utils;
import com.android.systemui.util.settings.GlobalSettings;
import com.google.android.setupcompat.util.WizardManagerHelper;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import javax.inject.Inject;

@SysUISingleton
public class ZenModeControllerImpl extends CurrentUserTracker implements ZenModeController, Dumpable {
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String TAG = "ZenModeController";
    private final AlarmManager mAlarmManager;
    private final ArrayList<ZenModeController.Callback> mCallbacks = new ArrayList<>();
    private final Object mCallbacksLock = new Object();
    private ZenModeConfig mConfig;
    private final SettingObserver mConfigSetting;
    private NotificationManager.Policy mConsolidatedNotificationPolicy;
    /* access modifiers changed from: private */
    public final Context mContext;
    private final SettingObserver mModeSetting;
    private final NotificationManager mNoMan;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.app.action.NEXT_ALARM_CLOCK_CHANGED".equals(intent.getAction())) {
                ZenModeControllerImpl.this.fireNextAlarmChanged();
            }
            if ("android.os.action.ACTION_EFFECTS_SUPPRESSOR_CHANGED".equals(intent.getAction())) {
                ZenModeControllerImpl.this.fireEffectsSuppressorChanged();
            }
        }
    };
    private boolean mRegistered;
    private final SetupObserver mSetupObserver;
    /* access modifiers changed from: private */
    public int mUserId;
    private final UserManager mUserManager;
    private int mZenMode;
    private long mZenUpdateTime;

    @Inject
    public ZenModeControllerImpl(Context context, @Main Handler handler, BroadcastDispatcher broadcastDispatcher, DumpManager dumpManager, GlobalSettings globalSettings) {
        super(broadcastDispatcher);
        this.mContext = context;
        C32021 r5 = new SettingObserver(globalSettings, handler, "zen_mode") {
            /* access modifiers changed from: protected */
            public void handleValueChanged(int i, boolean z) {
                ZenModeControllerImpl.this.updateZenMode(i);
                ZenModeControllerImpl.this.fireZenChanged(i);
            }
        };
        this.mModeSetting = r5;
        C32032 r0 = new SettingObserver(globalSettings, handler, "zen_mode_config_etag") {
            /* access modifiers changed from: protected */
            public void handleValueChanged(int i, boolean z) {
                ZenModeControllerImpl.this.updateZenModeConfig();
            }
        };
        this.mConfigSetting = r0;
        this.mNoMan = (NotificationManager) context.getSystemService("notification");
        r5.setListening(true);
        updateZenMode(r5.getValue());
        r0.setListening(true);
        updateZenModeConfig();
        updateConsolidatedNotificationPolicy();
        this.mAlarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        SetupObserver setupObserver = new SetupObserver(handler);
        this.mSetupObserver = setupObserver;
        setupObserver.register();
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
        startTracking();
        dumpManager.registerDumpable(getClass().getSimpleName(), this);
    }

    public boolean isVolumeRestricted() {
        return this.mUserManager.hasUserRestriction("no_adjust_volume", new UserHandle(this.mUserId));
    }

    public boolean areNotificationsHiddenInShade() {
        if (this.mZenMode == 0 || (this.mConsolidatedNotificationPolicy.suppressedVisualEffects & 256) == 0) {
            return false;
        }
        return true;
    }

    public void addCallback(ZenModeController.Callback callback) {
        synchronized (this.mCallbacksLock) {
            this.mCallbacks.add(callback);
        }
    }

    public void removeCallback(ZenModeController.Callback callback) {
        synchronized (this.mCallbacksLock) {
            this.mCallbacks.remove((Object) callback);
        }
    }

    public int getZen() {
        return this.mZenMode;
    }

    public void setZen(int i, Uri uri, String str) {
        this.mNoMan.setZenMode(i, uri, str);
    }

    public boolean isZenAvailable() {
        return this.mSetupObserver.isDeviceProvisioned() && this.mSetupObserver.isUserSetup();
    }

    public ZenModeConfig.ZenRule getManualRule() {
        ZenModeConfig zenModeConfig = this.mConfig;
        if (zenModeConfig == null) {
            return null;
        }
        return zenModeConfig.manualRule;
    }

    public ZenModeConfig getConfig() {
        return this.mConfig;
    }

    public NotificationManager.Policy getConsolidatedPolicy() {
        return this.mConsolidatedNotificationPolicy;
    }

    public long getNextAlarm() {
        AlarmManager.AlarmClockInfo nextAlarmClock = this.mAlarmManager.getNextAlarmClock(this.mUserId);
        if (nextAlarmClock != null) {
            return nextAlarmClock.getTriggerTime();
        }
        return 0;
    }

    public void onUserSwitched(int i) {
        this.mUserId = i;
        if (this.mRegistered) {
            this.mContext.unregisterReceiver(this.mReceiver);
        }
        IntentFilter intentFilter = new IntentFilter("android.app.action.NEXT_ALARM_CLOCK_CHANGED");
        intentFilter.addAction("android.os.action.ACTION_EFFECTS_SUPPRESSOR_CHANGED");
        this.mContext.registerReceiverAsUser(this.mReceiver, new UserHandle(this.mUserId), intentFilter, (String) null, (Handler) null);
        this.mRegistered = true;
        this.mSetupObserver.register();
    }

    public ComponentName getEffectsSuppressor() {
        return NotificationManager.from(this.mContext).getEffectsSuppressor();
    }

    public boolean isCountdownConditionSupported() {
        return NotificationManager.from(this.mContext).isSystemConditionProviderEnabled("countdown");
    }

    public int getCurrentUser() {
        return ActivityManager.getCurrentUser();
    }

    /* access modifiers changed from: private */
    public void fireNextAlarmChanged() {
        synchronized (this.mCallbacksLock) {
            Utils.safeForeach(this.mCallbacks, new ZenModeControllerImpl$$ExternalSyntheticLambda3());
        }
    }

    /* access modifiers changed from: private */
    public void fireEffectsSuppressorChanged() {
        synchronized (this.mCallbacksLock) {
            Utils.safeForeach(this.mCallbacks, new ZenModeControllerImpl$$ExternalSyntheticLambda4());
        }
    }

    /* access modifiers changed from: private */
    public void fireZenChanged(int i) {
        synchronized (this.mCallbacksLock) {
            Utils.safeForeach(this.mCallbacks, new ZenModeControllerImpl$$ExternalSyntheticLambda1(i));
        }
    }

    /* access modifiers changed from: private */
    public void fireZenAvailableChanged(boolean z) {
        synchronized (this.mCallbacksLock) {
            Utils.safeForeach(this.mCallbacks, new ZenModeControllerImpl$$ExternalSyntheticLambda6(z));
        }
    }

    private void fireManualRuleChanged(ZenModeConfig.ZenRule zenRule) {
        synchronized (this.mCallbacksLock) {
            Utils.safeForeach(this.mCallbacks, new ZenModeControllerImpl$$ExternalSyntheticLambda2(zenRule));
        }
    }

    private void fireConsolidatedPolicyChanged(NotificationManager.Policy policy) {
        synchronized (this.mCallbacksLock) {
            Utils.safeForeach(this.mCallbacks, new ZenModeControllerImpl$$ExternalSyntheticLambda5(policy));
        }
    }

    /* access modifiers changed from: protected */
    public void fireConfigChanged(ZenModeConfig zenModeConfig) {
        synchronized (this.mCallbacksLock) {
            Utils.safeForeach(this.mCallbacks, new ZenModeControllerImpl$$ExternalSyntheticLambda0(zenModeConfig));
        }
    }

    /* access modifiers changed from: protected */
    public void updateZenMode(int i) {
        this.mZenMode = i;
        this.mZenUpdateTime = System.currentTimeMillis();
    }

    /* access modifiers changed from: protected */
    public void updateConsolidatedNotificationPolicy() {
        NotificationManager.Policy consolidatedNotificationPolicy = this.mNoMan.getConsolidatedNotificationPolicy();
        if (!Objects.equals(consolidatedNotificationPolicy, this.mConsolidatedNotificationPolicy)) {
            this.mConsolidatedNotificationPolicy = consolidatedNotificationPolicy;
            fireConsolidatedPolicyChanged(consolidatedNotificationPolicy);
        }
    }

    /* access modifiers changed from: protected */
    public void updateZenModeConfig() {
        ZenModeConfig zenModeConfig = this.mNoMan.getZenModeConfig();
        if (!Objects.equals(zenModeConfig, this.mConfig)) {
            ZenModeConfig zenModeConfig2 = this.mConfig;
            ZenModeConfig.ZenRule zenRule = null;
            ZenModeConfig.ZenRule zenRule2 = zenModeConfig2 != null ? zenModeConfig2.manualRule : null;
            this.mConfig = zenModeConfig;
            this.mZenUpdateTime = System.currentTimeMillis();
            fireConfigChanged(zenModeConfig);
            if (zenModeConfig != null) {
                zenRule = zenModeConfig.manualRule;
            }
            if (!Objects.equals(zenRule2, zenRule)) {
                fireManualRuleChanged(zenRule);
            }
            NotificationManager.Policy consolidatedNotificationPolicy = this.mNoMan.getConsolidatedNotificationPolicy();
            if (!Objects.equals(consolidatedNotificationPolicy, this.mConsolidatedNotificationPolicy)) {
                this.mConsolidatedNotificationPolicy = consolidatedNotificationPolicy;
                fireConsolidatedPolicyChanged(consolidatedNotificationPolicy);
            }
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("ZenModeControllerImpl:");
        printWriter.println("  mZenMode=" + this.mZenMode);
        printWriter.println("  mConfig=" + this.mConfig);
        printWriter.println("  mConsolidatedNotificationPolicy=" + this.mConsolidatedNotificationPolicy);
        printWriter.println("  mZenUpdateTime=" + DateFormat.format("MM-dd HH:mm:ss", this.mZenUpdateTime));
    }

    private final class SetupObserver extends ContentObserver {
        private boolean mRegistered;
        private final ContentResolver mResolver;

        public SetupObserver(Handler handler) {
            super(handler);
            this.mResolver = ZenModeControllerImpl.this.mContext.getContentResolver();
        }

        public boolean isUserSetup() {
            return Settings.Secure.getIntForUser(this.mResolver, WizardManagerHelper.SETTINGS_SECURE_USER_SETUP_COMPLETE, 0, ZenModeControllerImpl.this.mUserId) != 0;
        }

        public boolean isDeviceProvisioned() {
            return Settings.Global.getInt(this.mResolver, WizardManagerHelper.SETTINGS_GLOBAL_DEVICE_PROVISIONED, 0) != 0;
        }

        public void register() {
            if (this.mRegistered) {
                this.mResolver.unregisterContentObserver(this);
            }
            this.mResolver.registerContentObserver(Settings.Global.getUriFor(WizardManagerHelper.SETTINGS_GLOBAL_DEVICE_PROVISIONED), false, this);
            this.mResolver.registerContentObserver(Settings.Secure.getUriFor(WizardManagerHelper.SETTINGS_SECURE_USER_SETUP_COMPLETE), false, this, ZenModeControllerImpl.this.mUserId);
            this.mRegistered = true;
            ZenModeControllerImpl zenModeControllerImpl = ZenModeControllerImpl.this;
            zenModeControllerImpl.fireZenAvailableChanged(zenModeControllerImpl.isZenAvailable());
        }

        public void onChange(boolean z, Uri uri) {
            if (Settings.Global.getUriFor(WizardManagerHelper.SETTINGS_GLOBAL_DEVICE_PROVISIONED).equals(uri) || Settings.Secure.getUriFor(WizardManagerHelper.SETTINGS_SECURE_USER_SETUP_COMPLETE).equals(uri)) {
                ZenModeControllerImpl zenModeControllerImpl = ZenModeControllerImpl.this;
                zenModeControllerImpl.fireZenAvailableChanged(zenModeControllerImpl.isZenAvailable());
            }
        }
    }
}
