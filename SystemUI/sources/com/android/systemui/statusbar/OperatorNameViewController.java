package com.android.systemui.statusbar;

import android.os.Bundle;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.view.View;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.settingslib.datetime.ZoneGetter;
import com.android.systemui.demomode.DemoModeCommandReceiver;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.connectivity.IconState;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.connectivity.SignalCallback;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.CarrierConfigTracker;
import com.android.systemui.util.ViewController;
import java.util.ArrayList;
import javax.inject.Inject;

public class OperatorNameViewController extends ViewController<OperatorNameView> {
    private static final String KEY_SHOW_OPERATOR_NAME = "show_operator_name";
    private final CarrierConfigTracker mCarrierConfigTracker;
    private final DarkIconDispatcher mDarkIconDispatcher;
    private final DarkIconDispatcher.DarkReceiver mDarkReceiver;
    private final DemoModeCommandReceiver mDemoModeCommandReceiver;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback;
    private final NetworkController mNetworkController;
    private final SignalCallback mSignalCallback;
    private final TelephonyManager mTelephonyManager;
    private final TunerService.Tunable mTunable;
    private final TunerService mTunerService;

    private OperatorNameViewController(OperatorNameView operatorNameView, DarkIconDispatcher darkIconDispatcher, NetworkController networkController, TunerService tunerService, TelephonyManager telephonyManager, KeyguardUpdateMonitor keyguardUpdateMonitor, CarrierConfigTracker carrierConfigTracker) {
        super(operatorNameView);
        this.mDarkReceiver = new OperatorNameViewController$$ExternalSyntheticLambda0(this);
        this.mSignalCallback = new SignalCallback() {
            public void setIsAirplaneMode(IconState iconState) {
                OperatorNameViewController.this.update();
            }
        };
        this.mTunable = new OperatorNameViewController$$ExternalSyntheticLambda1(this);
        this.mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() {
            public void onRefreshCarrierInfo() {
                ((OperatorNameView) OperatorNameViewController.this.mView).updateText(OperatorNameViewController.this.getDefaultSubInfo());
            }
        };
        this.mDemoModeCommandReceiver = new DemoModeCommandReceiver() {
            public void onDemoModeStarted() {
                ((OperatorNameView) OperatorNameViewController.this.mView).setDemoMode(true);
            }

            public void onDemoModeFinished() {
                ((OperatorNameView) OperatorNameViewController.this.mView).setDemoMode(false);
                OperatorNameViewController.this.update();
            }

            public void dispatchDemoCommand(String str, Bundle bundle) {
                ((OperatorNameView) OperatorNameViewController.this.mView).setText(bundle.getString(ZoneGetter.KEY_DISPLAYNAME));
            }
        };
        this.mDarkIconDispatcher = darkIconDispatcher;
        this.mNetworkController = networkController;
        this.mTunerService = tunerService;
        this.mTelephonyManager = telephonyManager;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mCarrierConfigTracker = carrierConfigTracker;
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        this.mDarkIconDispatcher.addDarkReceiver(this.mDarkReceiver);
        this.mNetworkController.addCallback(this.mSignalCallback);
        this.mTunerService.addTunable(this.mTunable, KEY_SHOW_OPERATOR_NAME);
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardUpdateMonitorCallback);
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        this.mDarkIconDispatcher.removeDarkReceiver(this.mDarkReceiver);
        this.mNetworkController.removeCallback(this.mSignalCallback);
        this.mTunerService.removeTunable(this.mTunable);
        this.mKeyguardUpdateMonitor.removeCallback(this.mKeyguardUpdateMonitorCallback);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:3:0x001a, code lost:
        if (r3.mTunerService.getValue(KEY_SHOW_OPERATOR_NAME, 1) != 0) goto L_0x001e;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void update() {
        /*
            r3 = this;
            com.android.systemui.statusbar.OperatorNameViewController$SubInfo r0 = r3.getDefaultSubInfo()
            com.android.systemui.util.CarrierConfigTracker r1 = r3.mCarrierConfigTracker
            int r0 = r0.getSubId()
            boolean r0 = r1.getShowOperatorNameInStatusBarConfig(r0)
            if (r0 == 0) goto L_0x001d
            com.android.systemui.tuner.TunerService r0 = r3.mTunerService
            java.lang.String r1 = "show_operator_name"
            r2 = 1
            int r0 = r0.getValue((java.lang.String) r1, (int) r2)
            if (r0 == 0) goto L_0x001d
            goto L_0x001e
        L_0x001d:
            r2 = 0
        L_0x001e:
            android.view.View r0 = r3.mView
            com.android.systemui.statusbar.OperatorNameView r0 = (com.android.systemui.statusbar.OperatorNameView) r0
            android.telephony.TelephonyManager r1 = r3.mTelephonyManager
            boolean r1 = r1.isDataCapable()
            com.android.systemui.statusbar.OperatorNameViewController$SubInfo r3 = r3.getDefaultSubInfo()
            r0.update(r2, r1, r3)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.OperatorNameViewController.update():void");
    }

    /* access modifiers changed from: private */
    public SubInfo getDefaultSubInfo() {
        int defaultDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();
        SubscriptionInfo subscriptionInfoForSubId = this.mKeyguardUpdateMonitor.getSubscriptionInfoForSubId(defaultDataSubscriptionId);
        return new SubInfo(subscriptionInfoForSubId.getSubscriptionId(), subscriptionInfoForSubId.getCarrierName(), this.mKeyguardUpdateMonitor.getSimState(defaultDataSubscriptionId), this.mKeyguardUpdateMonitor.getServiceState(defaultDataSubscriptionId));
    }

    public static class Factory {
        private final CarrierConfigTracker mCarrierConfigTracker;
        private final DarkIconDispatcher mDarkIconDispatcher;
        private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
        private final NetworkController mNetworkController;
        private final TelephonyManager mTelephonyManager;
        private final TunerService mTunerService;

        @Inject
        public Factory(DarkIconDispatcher darkIconDispatcher, NetworkController networkController, TunerService tunerService, TelephonyManager telephonyManager, KeyguardUpdateMonitor keyguardUpdateMonitor, CarrierConfigTracker carrierConfigTracker) {
            this.mDarkIconDispatcher = darkIconDispatcher;
            this.mNetworkController = networkController;
            this.mTunerService = tunerService;
            this.mTelephonyManager = telephonyManager;
            this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
            this.mCarrierConfigTracker = carrierConfigTracker;
        }

        public OperatorNameViewController create(OperatorNameView operatorNameView) {
            return new OperatorNameViewController(operatorNameView, this.mDarkIconDispatcher, this.mNetworkController, this.mTunerService, this.mTelephonyManager, this.mKeyguardUpdateMonitor, this.mCarrierConfigTracker);
        }
    }

    public View getView() {
        return this.mView;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-statusbar-OperatorNameViewController */
    public /* synthetic */ void mo38981x9ffb20e7(ArrayList arrayList, float f, int i) {
        ((OperatorNameView) this.mView).setTextColor(DarkIconDispatcher.getTint(arrayList, this.mView, i));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-systemui-statusbar-OperatorNameViewController */
    public /* synthetic */ void mo38982xb0b0eda8(String str, String str2) {
        update();
    }

    static class SubInfo {
        private final CharSequence mCarrierName;
        private final ServiceState mServiceState;
        private final int mSimState;
        private final int mSubId;

        private SubInfo(int i, CharSequence charSequence, int i2, ServiceState serviceState) {
            this.mSubId = i;
            this.mCarrierName = charSequence;
            this.mSimState = i2;
            this.mServiceState = serviceState;
        }

        /* access modifiers changed from: package-private */
        public int getSubId() {
            return this.mSubId;
        }

        /* access modifiers changed from: package-private */
        public boolean simReady() {
            return this.mSimState == 5;
        }

        /* access modifiers changed from: package-private */
        public CharSequence getCarrierName() {
            return this.mCarrierName;
        }

        /* access modifiers changed from: package-private */
        public boolean stateInService() {
            ServiceState serviceState = this.mServiceState;
            return serviceState != null && serviceState.getState() == 0;
        }
    }
}
