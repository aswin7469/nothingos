package com.android.keyguard;

import android.app.ActivityOptions;
import android.app.ActivityTaskManager;
import android.content.res.Configuration;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.telecom.TelecomManager;
import android.telephony.CellInfo;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import com.android.internal.logging.MetricsLogger;
import com.android.keyguard.dagger.KeyguardBouncerScope;
import com.android.systemui.C1894R;
import com.android.systemui.DejankUtils;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.EmergencyDialerConstants;
import com.android.systemui.util.ViewController;
import java.util.List;
import javax.inject.Inject;

@KeyguardBouncerScope
public class EmergencyButtonController extends ViewController<EmergencyButton> {
    static final String LOG_TAG = "EmergencyButton";
    private final ActivityTaskManager mActivityTaskManager;
    private final ConfigurationController mConfigurationController;
    private final ConfigurationController.ConfigurationListener mConfigurationListener;
    private EmergencyButtonCallback mEmergencyButtonCallback;
    private final KeyguardUpdateMonitorCallback mInfoCallback;
    /* access modifiers changed from: private */
    public boolean mIsCellAvailable;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final MetricsLogger mMetricsLogger;
    private final PowerManager mPowerManager;
    /* access modifiers changed from: private */
    public ServiceState mServiceState;
    private ShadeController mShadeController;
    private final TelecomManager mTelecomManager;
    private final TelephonyManager mTelephonyManager;

    public interface EmergencyButtonCallback {
        void onEmergencyButtonClickedWhenInCall();
    }

    private EmergencyButtonController(EmergencyButton emergencyButton, ConfigurationController configurationController, KeyguardUpdateMonitor keyguardUpdateMonitor, TelephonyManager telephonyManager, PowerManager powerManager, ActivityTaskManager activityTaskManager, ShadeController shadeController, TelecomManager telecomManager, MetricsLogger metricsLogger) {
        super(emergencyButton);
        this.mInfoCallback = new KeyguardUpdateMonitorCallback() {
            public void onSimStateChanged(int i, int i2, int i3) {
                EmergencyButtonController.this.updateEmergencyCallButton();
                EmergencyButtonController.this.requestCellInfoUpdate();
            }

            public void onPhoneStateChanged(int i) {
                EmergencyButtonController.this.updateEmergencyCallButton();
                EmergencyButtonController.this.requestCellInfoUpdate();
            }

            public void onServiceStateChanged(int i, ServiceState serviceState) {
                ServiceState unused = EmergencyButtonController.this.mServiceState = serviceState;
                EmergencyButtonController.this.updateEmergencyCallButton();
                EmergencyButtonController.this.requestCellInfoUpdate();
            }
        };
        this.mConfigurationListener = new ConfigurationController.ConfigurationListener() {
            public void onConfigChanged(Configuration configuration) {
                EmergencyButtonController.this.updateEmergencyCallButton();
            }
        };
        this.mConfigurationController = configurationController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mTelephonyManager = telephonyManager;
        this.mPowerManager = powerManager;
        this.mActivityTaskManager = activityTaskManager;
        this.mShadeController = shadeController;
        this.mTelecomManager = telecomManager;
        this.mMetricsLogger = metricsLogger;
    }

    /* access modifiers changed from: protected */
    public void onInit() {
        DejankUtils.whitelistIpcs((Runnable) new EmergencyButtonController$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        this.mKeyguardUpdateMonitor.registerCallback(this.mInfoCallback);
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        ((EmergencyButton) this.mView).setOnClickListener(new EmergencyButtonController$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onViewAttached$0$com-android-keyguard-EmergencyButtonController */
    public /* synthetic */ void mo25581xca4bd8d5(View view) {
        takeEmergencyCallAction();
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        this.mKeyguardUpdateMonitor.removeCallback(this.mInfoCallback);
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
    }

    /* access modifiers changed from: private */
    public void updateEmergencyCallButton() {
        if (this.mView != null) {
            EmergencyButton emergencyButton = (EmergencyButton) this.mView;
            TelecomManager telecomManager = this.mTelecomManager;
            emergencyButton.updateEmergencyCallButton(telecomManager != null && telecomManager.isInCall(), getContext().getPackageManager().hasSystemFeature("android.hardware.telephony"), this.mKeyguardUpdateMonitor.isSimPinVoiceSecure(), isEmergencyCapable());
        }
    }

    public void setEmergencyButtonCallback(EmergencyButtonCallback emergencyButtonCallback) {
        this.mEmergencyButtonCallback = emergencyButtonCallback;
    }

    public void takeEmergencyCallAction() {
        this.mMetricsLogger.action(200);
        PowerManager powerManager = this.mPowerManager;
        if (powerManager != null) {
            powerManager.userActivity(SystemClock.uptimeMillis(), true);
        }
        this.mActivityTaskManager.stopSystemLockTaskMode();
        this.mShadeController.collapsePanel(false);
        TelecomManager telecomManager = this.mTelecomManager;
        if (telecomManager == null || !telecomManager.isInCall()) {
            this.mKeyguardUpdateMonitor.reportEmergencyCallAction(true);
            TelecomManager telecomManager2 = this.mTelecomManager;
            if (telecomManager2 == null) {
                Log.wtf(LOG_TAG, "TelecomManager was null, cannot launch emergency dialer");
                return;
            }
            getContext().startActivityAsUser(telecomManager2.createLaunchEmergencyDialerIntent((String) null).setFlags(343932928).putExtra(EmergencyDialerConstants.EXTRA_ENTRY_TYPE, 1), ActivityOptions.makeCustomAnimation(getContext(), 0, 0).toBundle(), new UserHandle(KeyguardUpdateMonitor.getCurrentUser()));
            return;
        }
        this.mTelecomManager.showInCallScreen(false);
        EmergencyButtonCallback emergencyButtonCallback = this.mEmergencyButtonCallback;
        if (emergencyButtonCallback != null) {
            emergencyButtonCallback.onEmergencyButtonClickedWhenInCall();
        }
    }

    /* access modifiers changed from: private */
    public void requestCellInfoUpdate() {
        if (getContext().getResources().getBoolean(C1894R.bool.kg_hide_emgcy_btn_when_oos)) {
            try {
                this.mTelephonyManager.createForSubscriptionId(-1).requestCellInfoUpdate(getContext().getMainExecutor(), new TelephonyManager.CellInfoCallback() {
                    public void onCellInfo(List<CellInfo> list) {
                        int i;
                        StringBuilder sb = new StringBuilder("requestCellInfoUpdate.onCellInfo cellInfoList.size=");
                        if (list == null) {
                            i = 0;
                        } else {
                            i = list.size();
                        }
                        Log.d(EmergencyButtonController.LOG_TAG, sb.append(i).toString());
                        if (list == null || list.isEmpty()) {
                            boolean unused = EmergencyButtonController.this.mIsCellAvailable = false;
                        } else {
                            boolean unused2 = EmergencyButtonController.this.mIsCellAvailable = true;
                        }
                        EmergencyButtonController.this.updateEmergencyCallButton();
                    }
                });
            } catch (Exception e) {
                Log.e(LOG_TAG, "Fail to call TelephonyManager.requestCellInfoUpdate ", e);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000c, code lost:
        r1 = r1.mServiceState;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isEmergencyCapable() {
        /*
            r1 = this;
            com.android.keyguard.KeyguardUpdateMonitor r0 = r1.mKeyguardUpdateMonitor
            boolean r0 = r0.isOOS()
            if (r0 == 0) goto L_0x0019
            boolean r0 = r1.mIsCellAvailable
            if (r0 != 0) goto L_0x0019
            android.telephony.ServiceState r1 = r1.mServiceState
            if (r1 == 0) goto L_0x0017
            boolean r1 = r1.isEmergencyOnly()
            if (r1 == 0) goto L_0x0017
            goto L_0x0019
        L_0x0017:
            r1 = 0
            goto L_0x001a
        L_0x0019:
            r1 = 1
        L_0x001a:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.EmergencyButtonController.isEmergencyCapable():boolean");
    }

    public static class Factory {
        private final ActivityTaskManager mActivityTaskManager;
        private final ConfigurationController mConfigurationController;
        private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
        private final MetricsLogger mMetricsLogger;
        private final PowerManager mPowerManager;
        private ShadeController mShadeController;
        private final TelecomManager mTelecomManager;
        private final TelephonyManager mTelephonyManager;

        @Inject
        public Factory(ConfigurationController configurationController, KeyguardUpdateMonitor keyguardUpdateMonitor, TelephonyManager telephonyManager, PowerManager powerManager, ActivityTaskManager activityTaskManager, ShadeController shadeController, TelecomManager telecomManager, MetricsLogger metricsLogger) {
            this.mConfigurationController = configurationController;
            this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
            this.mTelephonyManager = telephonyManager;
            this.mPowerManager = powerManager;
            this.mActivityTaskManager = activityTaskManager;
            this.mShadeController = shadeController;
            this.mTelecomManager = telecomManager;
            this.mMetricsLogger = metricsLogger;
        }

        public EmergencyButtonController create(EmergencyButton emergencyButton) {
            return new EmergencyButtonController(emergencyButton, this.mConfigurationController, this.mKeyguardUpdateMonitor, this.mTelephonyManager, this.mPowerManager, this.mActivityTaskManager, this.mShadeController, this.mTelecomManager, this.mMetricsLogger);
        }
    }
}
