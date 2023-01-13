package com.android.keyguard;

import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiManager;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.policy.FiveGServiceClient;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.util.CarrierNameCustomization;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.inject.Inject;

public class CarrierTextManager {
    /* access modifiers changed from: private */
    public static final boolean DEBUG = KeyguardConstants.DEBUG;
    private static final String TAG = "CarrierTextController";
    private final Executor mBgExecutor;
    protected final KeyguardUpdateMonitorCallback mCallback;
    private CarrierNameCustomization mCarrierNameCustomization;
    /* access modifiers changed from: private */
    public CarrierTextCallback mCarrierTextCallback;
    private final Context mContext;
    private FiveGServiceClient mFiveGServiceClient;
    private final boolean mIsEmergencyCallCapable;
    protected KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final Executor mMainExecutor;
    /* access modifiers changed from: private */
    public final AtomicBoolean mNetworkSupported;
    private final TelephonyCallback.ActiveDataSubscriptionIdListener mPhoneStateListener;
    private final CharSequence mSeparator;
    private final boolean mShowAirplaneMode;
    private final boolean mShowMissingSim;
    /* access modifiers changed from: private */
    public final boolean[] mSimErrorState;
    /* access modifiers changed from: private */
    public final int mSimSlotsNumber;
    /* access modifiers changed from: private */
    public boolean mTelephonyCapable;
    private final TelephonyListenerManager mTelephonyListenerManager;
    private final TelephonyManager mTelephonyManager;
    private final WakefulnessLifecycle mWakefulnessLifecycle;
    private final WakefulnessLifecycle.Observer mWakefulnessObserver;
    private final WifiManager mWifiManager;

    public interface CarrierTextCallback {
        void finishedWakingUp() {
        }

        void startedGoingToSleep() {
        }

        void updateCarrierInfo(CarrierTextCallbackInfo carrierTextCallbackInfo) {
        }
    }

    private enum StatusMode {
        Normal,
        NetworkLocked,
        SimMissing,
        SimMissingLocked,
        SimPukLocked,
        SimLocked,
        SimPermDisabled,
        SimNotReady,
        SimIoError,
        SimUnknown
    }

    private CarrierTextManager(Context context, CharSequence charSequence, boolean z, boolean z2, WifiManager wifiManager, TelephonyManager telephonyManager, TelephonyListenerManager telephonyListenerManager, WakefulnessLifecycle wakefulnessLifecycle, @Main Executor executor, @Background Executor executor2, KeyguardUpdateMonitor keyguardUpdateMonitor, CarrierNameCustomization carrierNameCustomization) {
        this.mNetworkSupported = new AtomicBoolean();
        this.mWakefulnessObserver = new WakefulnessLifecycle.Observer() {
            public void onFinishedWakingUp() {
                CarrierTextCallback access$000 = CarrierTextManager.this.mCarrierTextCallback;
                if (access$000 != null) {
                    access$000.finishedWakingUp();
                }
            }

            public void onStartedGoingToSleep() {
                CarrierTextCallback access$000 = CarrierTextManager.this.mCarrierTextCallback;
                if (access$000 != null) {
                    access$000.startedGoingToSleep();
                }
            }
        };
        this.mCallback = new KeyguardUpdateMonitorCallback() {
            public void onRefreshCarrierInfo() {
                if (CarrierTextManager.DEBUG) {
                    Log.d(CarrierTextManager.TAG, "onRefreshCarrierInfo(), mTelephonyCapable: " + Boolean.toString(CarrierTextManager.this.mTelephonyCapable));
                }
                CarrierTextManager.this.updateCarrierText();
            }

            public void onTelephonyCapable(boolean z) {
                if (CarrierTextManager.DEBUG) {
                    Log.d(CarrierTextManager.TAG, "onTelephonyCapable() mTelephonyCapable: " + Boolean.toString(z));
                }
                boolean unused = CarrierTextManager.this.mTelephonyCapable = z;
                CarrierTextManager.this.updateCarrierText();
            }

            public void onSimStateChanged(int i, int i2, int i3) {
                if (i2 < 0 || i2 >= CarrierTextManager.this.mSimSlotsNumber) {
                    Log.d(CarrierTextManager.TAG, "onSimStateChanged() - slotId invalid: " + i2 + " mTelephonyCapable: " + Boolean.toString(CarrierTextManager.this.mTelephonyCapable));
                    return;
                }
                if (CarrierTextManager.DEBUG) {
                    Log.d(CarrierTextManager.TAG, "onSimStateChanged: " + CarrierTextManager.this.getStatusForIccState(i3));
                }
                if (CarrierTextManager.this.getStatusForIccState(i3) == StatusMode.SimIoError) {
                    CarrierTextManager.this.mSimErrorState[i2] = true;
                    CarrierTextManager.this.updateCarrierText();
                } else if (CarrierTextManager.this.mSimErrorState[i2]) {
                    CarrierTextManager.this.mSimErrorState[i2] = false;
                    CarrierTextManager.this.updateCarrierText();
                }
            }
        };
        this.mPhoneStateListener = new TelephonyCallback.ActiveDataSubscriptionIdListener() {
            public void onActiveDataSubscriptionIdChanged(int i) {
                if (CarrierTextManager.this.mNetworkSupported.get() && CarrierTextManager.this.mCarrierTextCallback != null) {
                    CarrierTextManager.this.updateCarrierText();
                }
            }
        };
        this.mContext = context;
        this.mIsEmergencyCallCapable = telephonyManager.isVoiceCapable();
        this.mShowAirplaneMode = z;
        this.mShowMissingSim = z2;
        this.mWifiManager = wifiManager;
        this.mTelephonyManager = telephonyManager;
        this.mSeparator = charSequence;
        this.mTelephonyListenerManager = telephonyListenerManager;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        int supportedModemCount = getTelephonyManager().getSupportedModemCount();
        this.mSimSlotsNumber = supportedModemCount;
        this.mSimErrorState = new boolean[supportedModemCount];
        this.mMainExecutor = executor;
        this.mBgExecutor = executor2;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        executor2.execute(new CarrierTextManager$$ExternalSyntheticLambda4(this));
        this.mCarrierNameCustomization = carrierNameCustomization;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-keyguard-CarrierTextManager  reason: not valid java name */
    public /* synthetic */ void m2285lambda$new$0$comandroidkeyguardCarrierTextManager() {
        boolean hasSystemFeature = this.mContext.getPackageManager().hasSystemFeature("android.hardware.telephony");
        if (hasSystemFeature && this.mNetworkSupported.compareAndSet(false, hasSystemFeature)) {
            m2286lambda$setListening$4$comandroidkeyguardCarrierTextManager(this.mCarrierTextCallback);
        }
    }

    private TelephonyManager getTelephonyManager() {
        return this.mTelephonyManager;
    }

    private CharSequence updateCarrierTextWithSimIoError(CharSequence charSequence, CharSequence[] charSequenceArr, int[] iArr, boolean z) {
        CharSequence carrierTextForSimState = getCarrierTextForSimState(8, "");
        for (int i = 0; i < getTelephonyManager().getActiveModemCount(); i++) {
            if (this.mSimErrorState[i]) {
                if (z) {
                    return concatenate(carrierTextForSimState, getContext().getText(17040205), this.mSeparator);
                }
                int i2 = iArr[i];
                if (i2 != -1) {
                    charSequenceArr[i2] = concatenate(carrierTextForSimState, charSequenceArr[i2], this.mSeparator);
                } else {
                    charSequence = concatenate(charSequence, carrierTextForSimState, this.mSeparator);
                }
            }
        }
        return charSequence;
    }

    /* access modifiers changed from: private */
    /* renamed from: handleSetListening */
    public void m2286lambda$setListening$4$comandroidkeyguardCarrierTextManager(CarrierTextCallback carrierTextCallback) {
        if (carrierTextCallback != null) {
            this.mCarrierTextCallback = carrierTextCallback;
            if (this.mNetworkSupported.get()) {
                this.mMainExecutor.execute(new CarrierTextManager$$ExternalSyntheticLambda1(this));
                this.mTelephonyListenerManager.addActiveDataSubscriptionIdListener(this.mPhoneStateListener);
                return;
            }
            this.mMainExecutor.execute(new CarrierTextManager$$ExternalSyntheticLambda2(carrierTextCallback));
            return;
        }
        this.mCarrierTextCallback = null;
        this.mMainExecutor.execute(new CarrierTextManager$$ExternalSyntheticLambda3(this));
        this.mTelephonyListenerManager.removeActiveDataSubscriptionIdListener(this.mPhoneStateListener);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$handleSetListening$1$com-android-keyguard-CarrierTextManager */
    public /* synthetic */ void mo25558xd9ef2ecc() {
        this.mKeyguardUpdateMonitor.registerCallback(this.mCallback);
        this.mWakefulnessLifecycle.addObserver(this.mWakefulnessObserver);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$handleSetListening$3$com-android-keyguard-CarrierTextManager */
    public /* synthetic */ void mo25559xf32f84ce() {
        this.mKeyguardUpdateMonitor.removeCallback(this.mCallback);
        this.mWakefulnessLifecycle.removeObserver(this.mWakefulnessObserver);
    }

    public void setListening(CarrierTextCallback carrierTextCallback) {
        this.mBgExecutor.execute(new CarrierTextManager$$ExternalSyntheticLambda0(this, carrierTextCallback));
    }

    /* access modifiers changed from: protected */
    public List<SubscriptionInfo> getSubscriptionInfo() {
        return this.mKeyguardUpdateMonitor.getFilteredSubscriptionInfo(false);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x01df  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x01f3  */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x01fa  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateCarrierText() {
        /*
            r18 = this;
            r0 = r18
            android.content.Context r1 = r18.getContext()
            android.content.res.Resources r1 = r1.getResources()
            r2 = 2131034174(0x7f05003e, float:1.7678858E38)
            boolean r1 = r1.getBoolean(r2)
            java.util.List r2 = r18.getSubscriptionInfo()
            int r3 = r2.size()
            int[] r8 = new int[r3]
            boolean r4 = DEBUG
            java.lang.String r5 = "updateCarrierText(): "
            java.lang.String r6 = "CarrierTextController"
            if (r4 == 0) goto L_0x0034
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>((java.lang.String) r5)
            java.lang.StringBuilder r4 = r4.append((int) r3)
            java.lang.String r4 = r4.toString()
            android.util.Log.d(r6, r4)
        L_0x0034:
            int r4 = r0.mSimSlotsNumber
            int[] r4 = new int[r4]
            r9 = 0
        L_0x0039:
            int r10 = r0.mSimSlotsNumber
            if (r9 >= r10) goto L_0x0043
            r10 = -1
            r4[r9] = r10
            int r9 = r9 + 1
            goto L_0x0039
        L_0x0043:
            java.lang.CharSequence[] r9 = new java.lang.CharSequence[r3]
            boolean r10 = DEBUG
            if (r10 == 0) goto L_0x0059
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>((java.lang.String) r5)
            java.lang.StringBuilder r5 = r10.append((int) r3)
            java.lang.String r5 = r5.toString()
            android.util.Log.d(r6, r5)
        L_0x0059:
            r10 = 0
            r11 = 1
            r12 = 0
        L_0x005c:
            java.lang.String r13 = ""
            if (r10 >= r3) goto L_0x0149
            java.lang.Object r14 = r2.get(r10)
            android.telephony.SubscriptionInfo r14 = (android.telephony.SubscriptionInfo) r14
            int r14 = r14.getSubscriptionId()
            r9[r10] = r13
            r8[r10] = r14
            java.lang.Object r13 = r2.get(r10)
            android.telephony.SubscriptionInfo r13 = (android.telephony.SubscriptionInfo) r13
            int r13 = r13.getSimSlotIndex()
            r4[r13] = r10
            com.android.keyguard.KeyguardUpdateMonitor r13 = r0.mKeyguardUpdateMonitor
            int r13 = r13.getSimState(r14)
            java.lang.Object r15 = r2.get(r10)
            android.telephony.SubscriptionInfo r15 = (android.telephony.SubscriptionInfo) r15
            java.lang.CharSequence r15 = r15.getCarrierName()
            if (r1 == 0) goto L_0x00ad
            com.android.systemui.util.CarrierNameCustomization r5 = r0.mCarrierNameCustomization
            boolean r5 = r5.isRoamingCustomizationEnabled()
            if (r5 == 0) goto L_0x00a3
            com.android.systemui.util.CarrierNameCustomization r5 = r0.mCarrierNameCustomization
            boolean r5 = r5.isRoaming(r14)
            if (r5 == 0) goto L_0x00a3
            com.android.systemui.util.CarrierNameCustomization r5 = r0.mCarrierNameCustomization
            java.lang.String r15 = r5.getRoamingCarrierName(r14)
            goto L_0x00ad
        L_0x00a3:
            java.lang.Object r5 = r2.get(r10)
            android.telephony.SubscriptionInfo r5 = (android.telephony.SubscriptionInfo) r5
            java.lang.String r15 = r0.getCustomizeCarrierName(r15, r5)
        L_0x00ad:
            java.lang.CharSequence r5 = r0.getCarrierTextForSimState(r13, r15)
            boolean r16 = DEBUG
            if (r16 == 0) goto L_0x00de
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r17 = r1
            java.lang.String r1 = "Handling (subId="
            r7.<init>((java.lang.String) r1)
            java.lang.StringBuilder r1 = r7.append((int) r14)
            java.lang.String r7 = "): "
            java.lang.StringBuilder r1 = r1.append((java.lang.String) r7)
            java.lang.StringBuilder r1 = r1.append((int) r13)
            java.lang.String r7 = " "
            java.lang.StringBuilder r1 = r1.append((java.lang.String) r7)
            java.lang.StringBuilder r1 = r1.append((java.lang.Object) r15)
            java.lang.String r1 = r1.toString()
            android.util.Log.d(r6, r1)
            goto L_0x00e0
        L_0x00de:
            r17 = r1
        L_0x00e0:
            if (r5 == 0) goto L_0x00e5
            r9[r10] = r5
            r11 = 0
        L_0x00e5:
            r1 = 5
            if (r13 != r1) goto L_0x0143
            com.android.keyguard.KeyguardUpdateMonitor r1 = r0.mKeyguardUpdateMonitor
            java.util.HashMap<java.lang.Integer, android.telephony.ServiceState> r1 = r1.mServiceStates
            java.lang.Integer r5 = java.lang.Integer.valueOf((int) r14)
            java.lang.Object r1 = r1.get(r5)
            android.telephony.ServiceState r1 = (android.telephony.ServiceState) r1
            if (r1 == 0) goto L_0x0143
            int r5 = r1.getDataRegistrationState()
            if (r5 != 0) goto L_0x0143
            int r5 = r1.getRilDataRadioTechnology()
            r7 = 18
            if (r5 != r7) goto L_0x0124
            android.net.wifi.WifiManager r5 = r0.mWifiManager
            if (r5 == 0) goto L_0x0143
            boolean r5 = r5.isWifiEnabled()
            if (r5 == 0) goto L_0x0143
            android.net.wifi.WifiManager r5 = r0.mWifiManager
            android.net.wifi.WifiInfo r5 = r5.getConnectionInfo()
            if (r5 == 0) goto L_0x0143
            android.net.wifi.WifiManager r5 = r0.mWifiManager
            android.net.wifi.WifiInfo r5 = r5.getConnectionInfo()
            java.lang.String r5 = r5.getBSSID()
            if (r5 == 0) goto L_0x0143
        L_0x0124:
            if (r16 == 0) goto L_0x0142
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r7 = "SIM ready and in service: subId="
            r5.<init>((java.lang.String) r7)
            java.lang.StringBuilder r5 = r5.append((int) r14)
            java.lang.String r7 = ", ss="
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r7)
            java.lang.StringBuilder r1 = r5.append((java.lang.Object) r1)
            java.lang.String r1 = r1.toString()
            android.util.Log.d(r6, r1)
        L_0x0142:
            r12 = 1
        L_0x0143:
            int r10 = r10 + 1
            r1 = r17
            goto L_0x005c
        L_0x0149:
            r1 = 0
            if (r11 == 0) goto L_0x0163
            if (r12 != 0) goto L_0x0163
            if (r3 == 0) goto L_0x0166
            java.lang.String r1 = r18.getMissingSimMessage()
            r3 = 0
            java.lang.Object r2 = r2.get(r3)
            android.telephony.SubscriptionInfo r2 = (android.telephony.SubscriptionInfo) r2
            java.lang.CharSequence r2 = r2.getCarrierName()
            java.lang.CharSequence r1 = r0.makeCarrierStringOnEmergencyCapable(r1, r2)
        L_0x0163:
            r3 = 0
            goto L_0x01d9
        L_0x0166:
            android.content.Context r2 = r18.getContext()
            r3 = 17040205(0x104034d, float:2.424694E-38)
            java.lang.CharSequence r2 = r2.getText(r3)
            android.content.Context r3 = r18.getContext()
            android.content.IntentFilter r5 = new android.content.IntentFilter
            java.lang.String r7 = "android.telephony.action.SERVICE_PROVIDERS_UPDATED"
            r5.<init>(r7)
            android.content.Intent r1 = r3.registerReceiver(r1, r5)
            if (r1 == 0) goto L_0x01d0
            java.lang.String r2 = "android.telephony.extra.SHOW_SPN"
            r3 = 0
            boolean r2 = r1.getBooleanExtra(r2, r3)
            if (r2 == 0) goto L_0x0192
            java.lang.String r2 = "android.telephony.extra.SPN"
            java.lang.String r2 = r1.getStringExtra(r2)
            goto L_0x0193
        L_0x0192:
            r2 = r13
        L_0x0193:
            java.lang.String r5 = "android.telephony.extra.SHOW_PLMN"
            boolean r5 = r1.getBooleanExtra(r5, r3)
            if (r5 == 0) goto L_0x01a1
            java.lang.String r5 = "android.telephony.extra.PLMN"
            java.lang.String r13 = r1.getStringExtra(r5)
        L_0x01a1:
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x01c1
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r5 = "Getting plmn/spn sticky brdcst "
            r1.<init>((java.lang.String) r5)
            java.lang.StringBuilder r1 = r1.append((java.lang.String) r13)
            java.lang.String r5 = "/"
            java.lang.StringBuilder r1 = r1.append((java.lang.String) r5)
            java.lang.StringBuilder r1 = r1.append((java.lang.String) r2)
            java.lang.String r1 = r1.toString()
            android.util.Log.d(r6, r1)
        L_0x01c1:
            boolean r1 = java.util.Objects.equals(r13, r2)
            if (r1 == 0) goto L_0x01c9
            r2 = r13
            goto L_0x01d1
        L_0x01c9:
            java.lang.CharSequence r1 = r0.mSeparator
            java.lang.CharSequence r2 = concatenate(r13, r2, r1)
            goto L_0x01d1
        L_0x01d0:
            r3 = 0
        L_0x01d1:
            java.lang.String r1 = r18.getMissingSimMessage()
            java.lang.CharSequence r1 = r0.makeCarrierStringOnEmergencyCapable(r1, r2)
        L_0x01d9:
            boolean r2 = android.text.TextUtils.isEmpty(r1)
            if (r2 == 0) goto L_0x01e5
            java.lang.CharSequence r1 = r0.mSeparator
            java.lang.CharSequence r1 = joinNotEmpty(r1, r9)
        L_0x01e5:
            java.lang.CharSequence r1 = r0.updateCarrierTextWithSimIoError(r1, r9, r4, r11)
            if (r12 != 0) goto L_0x01fa
            android.content.Context r2 = r0.mContext
            boolean r2 = com.android.settingslib.WirelessUtils.isAirplaneModeOn(r2)
            if (r2 == 0) goto L_0x01fa
            java.lang.String r1 = r18.getAirplaneModeMessage()
            r5 = r1
            r3 = 1
            goto L_0x01fb
        L_0x01fa:
            r5 = r1
        L_0x01fb:
            com.android.keyguard.CarrierTextManager$CarrierTextCallbackInfo r1 = new com.android.keyguard.CarrierTextManager$CarrierTextCallbackInfo
            r2 = 1
            r7 = r11 ^ 1
            r4 = r1
            r6 = r9
            r9 = r3
            r4.<init>(r5, r6, r7, r8, r9)
            r0.postToCallback(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.CarrierTextManager.updateCarrierText():void");
    }

    /* access modifiers changed from: protected */
    public void postToCallback(CarrierTextCallbackInfo carrierTextCallbackInfo) {
        CarrierTextCallback carrierTextCallback = this.mCarrierTextCallback;
        if (carrierTextCallback != null) {
            this.mMainExecutor.execute(new CarrierTextManager$$ExternalSyntheticLambda5(carrierTextCallback, carrierTextCallbackInfo));
        }
    }

    private Context getContext() {
        return this.mContext;
    }

    private String getMissingSimMessage() {
        return (!this.mShowMissingSim || !this.mTelephonyCapable) ? "" : getContext().getString(C1894R.string.keyguard_missing_sim_message_short);
    }

    private String getAirplaneModeMessage() {
        return this.mShowAirplaneMode ? getContext().getString(C1894R.string.airplane_mode) : "";
    }

    /* renamed from: com.android.keyguard.CarrierTextManager$4 */
    static /* synthetic */ class C15844 {
        static final /* synthetic */ int[] $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode;

        /* JADX WARNING: Can't wrap try/catch for region: R(20:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|(3:19|20|22)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(22:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|22) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0054 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0060 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x006c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                com.android.keyguard.CarrierTextManager$StatusMode[] r0 = com.android.keyguard.CarrierTextManager.StatusMode.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode = r0
                com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.Normal     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.SimNotReady     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.NetworkLocked     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.SimMissing     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode     // Catch:{ NoSuchFieldError -> 0x003e }
                com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.SimPermDisabled     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode     // Catch:{ NoSuchFieldError -> 0x0049 }
                com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.SimMissingLocked     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode     // Catch:{ NoSuchFieldError -> 0x0054 }
                com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.SimLocked     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                int[] r0 = $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode     // Catch:{ NoSuchFieldError -> 0x0060 }
                com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.SimPukLocked     // Catch:{ NoSuchFieldError -> 0x0060 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0060 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0060 }
            L_0x0060:
                int[] r0 = $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode     // Catch:{ NoSuchFieldError -> 0x006c }
                com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.SimIoError     // Catch:{ NoSuchFieldError -> 0x006c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x006c }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x006c }
            L_0x006c:
                int[] r0 = $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode     // Catch:{ NoSuchFieldError -> 0x0078 }
                com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.SimUnknown     // Catch:{ NoSuchFieldError -> 0x0078 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0078 }
                r2 = 10
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0078 }
            L_0x0078:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.CarrierTextManager.C15844.<clinit>():void");
        }
    }

    private CharSequence getCarrierTextForSimState(int i, CharSequence charSequence) {
        switch (C15844.$SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode[getStatusForIccState(i).ordinal()]) {
            case 1:
                return charSequence;
            case 2:
                return "";
            case 3:
                return makeCarrierStringOnEmergencyCapable(this.mContext.getText(C1894R.string.keyguard_network_locked_message), charSequence);
            case 5:
                return makeCarrierStringOnEmergencyCapable(getContext().getText(C1894R.string.keyguard_permanent_disabled_sim_message_short), charSequence);
            case 7:
                return makeCarrierStringOnLocked(getContext().getText(C1894R.string.keyguard_sim_locked_message), charSequence);
            case 8:
                return makeCarrierStringOnLocked(getContext().getText(C1894R.string.keyguard_sim_puk_locked_message), charSequence);
            case 9:
                return makeCarrierStringOnEmergencyCapable(getContext().getText(C1894R.string.keyguard_sim_error_message_short), charSequence);
            default:
                return null;
        }
    }

    private CharSequence makeCarrierStringOnEmergencyCapable(CharSequence charSequence, CharSequence charSequence2) {
        return this.mIsEmergencyCallCapable ? concatenate(charSequence, charSequence2, this.mSeparator) : charSequence;
    }

    private CharSequence makeCarrierStringOnLocked(CharSequence charSequence, CharSequence charSequence2) {
        boolean z = !TextUtils.isEmpty(charSequence);
        boolean z2 = !TextUtils.isEmpty(charSequence2);
        if (z && z2) {
            return this.mContext.getString(C1894R.string.keyguard_carrier_name_with_sim_locked_template, new Object[]{charSequence2, charSequence});
        } else if (z) {
            return charSequence;
        } else {
            return z2 ? charSequence2 : "";
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x000c, code lost:
        if (r2 != 7) goto L_0x000f;
     */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x0016  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0019  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x001c  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x001f  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0022  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0025  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0028  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x002b  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x002e  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0031  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0012  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.android.keyguard.CarrierTextManager.StatusMode getStatusForIccState(int r2) {
        /*
            r1 = this;
            com.android.keyguard.KeyguardUpdateMonitor r1 = r1.mKeyguardUpdateMonitor
            boolean r1 = r1.isDeviceProvisioned()
            if (r1 != 0) goto L_0x000f
            r1 = 1
            if (r2 == r1) goto L_0x0010
            r0 = 7
            if (r2 != r0) goto L_0x000f
            goto L_0x0010
        L_0x000f:
            r1 = 0
        L_0x0010:
            if (r1 == 0) goto L_0x0013
            r2 = 4
        L_0x0013:
            switch(r2) {
                case 0: goto L_0x0031;
                case 1: goto L_0x002e;
                case 2: goto L_0x002b;
                case 3: goto L_0x0028;
                case 4: goto L_0x0025;
                case 5: goto L_0x0022;
                case 6: goto L_0x001f;
                case 7: goto L_0x001c;
                case 8: goto L_0x0019;
                default: goto L_0x0016;
            }
        L_0x0016:
            com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.SimUnknown
            return r1
        L_0x0019:
            com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.SimIoError
            return r1
        L_0x001c:
            com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.SimPermDisabled
            return r1
        L_0x001f:
            com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.SimNotReady
            return r1
        L_0x0022:
            com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.Normal
            return r1
        L_0x0025:
            com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.SimMissingLocked
            return r1
        L_0x0028:
            com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.SimPukLocked
            return r1
        L_0x002b:
            com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.SimLocked
            return r1
        L_0x002e:
            com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.SimMissing
            return r1
        L_0x0031:
            com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.SimUnknown
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.CarrierTextManager.getStatusForIccState(int):com.android.keyguard.CarrierTextManager$StatusMode");
    }

    private static CharSequence concatenate(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3) {
        boolean z = !TextUtils.isEmpty(charSequence);
        boolean z2 = !TextUtils.isEmpty(charSequence2);
        if (z && z2) {
            return new StringBuilder().append(charSequence).append(charSequence3).append(charSequence2).toString();
        }
        if (z) {
            return charSequence;
        }
        return z2 ? charSequence2 : "";
    }

    private static CharSequence joinNotEmpty(CharSequence charSequence, CharSequence[] charSequenceArr) {
        int length = charSequenceArr.length;
        if (length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (!TextUtils.isEmpty(charSequenceArr[i])) {
                if (!TextUtils.isEmpty(sb)) {
                    sb.append(charSequence);
                }
                sb.append(charSequenceArr[i]);
            }
        }
        return sb.toString();
    }

    private static List<CharSequence> append(List<CharSequence> list, CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            list.add(charSequence);
        }
        return list;
    }

    private CharSequence getCarrierHelpTextForSimState(int i, String str, String str2) {
        int i2 = C15844.$SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode[getStatusForIccState(i).ordinal()];
        return this.mContext.getText(i2 != 3 ? i2 != 4 ? i2 != 5 ? i2 != 6 ? 0 : C1894R.string.keyguard_missing_sim_instructions : C1894R.string.keyguard_permanent_disabled_sim_instructions : C1894R.string.keyguard_missing_sim_instructions_long : C1894R.string.keyguard_instructions_when_pattern_disabled);
    }

    public static class Builder {
        private final Executor mBgExecutor;
        private CarrierNameCustomization mCarrierNameCustomization;
        private final Context mContext;
        private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
        private final Executor mMainExecutor;
        private final String mSeparator;
        private boolean mShowAirplaneMode;
        private boolean mShowMissingSim;
        private final TelephonyListenerManager mTelephonyListenerManager;
        private final TelephonyManager mTelephonyManager;
        private final WakefulnessLifecycle mWakefulnessLifecycle;
        private final WifiManager mWifiManager;

        @Inject
        public Builder(Context context, @Main Resources resources, WifiManager wifiManager, TelephonyManager telephonyManager, TelephonyListenerManager telephonyListenerManager, WakefulnessLifecycle wakefulnessLifecycle, @Main Executor executor, @Background Executor executor2, KeyguardUpdateMonitor keyguardUpdateMonitor, CarrierNameCustomization carrierNameCustomization) {
            this.mContext = context;
            this.mSeparator = resources.getString(17040571);
            this.mWifiManager = wifiManager;
            this.mTelephonyManager = telephonyManager;
            this.mTelephonyListenerManager = telephonyListenerManager;
            this.mWakefulnessLifecycle = wakefulnessLifecycle;
            this.mMainExecutor = executor;
            this.mBgExecutor = executor2;
            this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
            this.mCarrierNameCustomization = carrierNameCustomization;
        }

        public Builder setShowAirplaneMode(boolean z) {
            this.mShowAirplaneMode = z;
            return this;
        }

        public Builder setShowMissingSim(boolean z) {
            this.mShowMissingSim = z;
            return this;
        }

        public CarrierTextManager build() {
            return new CarrierTextManager(this.mContext, this.mSeparator, this.mShowAirplaneMode, this.mShowMissingSim, this.mWifiManager, this.mTelephonyManager, this.mTelephonyListenerManager, this.mWakefulnessLifecycle, this.mMainExecutor, this.mBgExecutor, this.mKeyguardUpdateMonitor, this.mCarrierNameCustomization);
        }
    }

    public static final class CarrierTextCallbackInfo {
        public boolean airplaneMode;
        public final boolean anySimReady;
        public final CharSequence carrierText;
        public final CharSequence[] listOfCarriers;
        public final int[] subscriptionIds;

        public CarrierTextCallbackInfo(CharSequence charSequence, CharSequence[] charSequenceArr, boolean z, int[] iArr) {
            this(charSequence, charSequenceArr, z, iArr, false);
        }

        public CarrierTextCallbackInfo(CharSequence charSequence, CharSequence[] charSequenceArr, boolean z, int[] iArr, boolean z2) {
            this.carrierText = charSequence;
            this.listOfCarriers = charSequenceArr;
            this.anySimReady = z;
            this.subscriptionIds = iArr;
            this.airplaneMode = z2;
        }
    }

    private String getCustomizeCarrierName(CharSequence charSequence, SubscriptionInfo subscriptionInfo) {
        StringBuilder sb = new StringBuilder();
        int networkType = getNetworkType(subscriptionInfo.getSubscriptionId());
        String networkTypeToString = networkTypeToString(networkType);
        String str = get5GNetworkClass(subscriptionInfo, networkType);
        if (str != null) {
            networkTypeToString = str;
        }
        if (!TextUtils.isEmpty(charSequence)) {
            String[] split = charSequence.toString().split(this.mSeparator.toString(), 2);
            for (int i = 0; i < split.length; i++) {
                String localString = getLocalString(split[i], C1894R.array.origin_carrier_names, C1894R.array.locale_carrier_names);
                split[i] = localString;
                if (!TextUtils.isEmpty(localString)) {
                    if (!TextUtils.isEmpty(networkTypeToString)) {
                        split[i] = split[i] + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + networkTypeToString;
                    }
                    if (i <= 0 || !split[i].equals(split[i - 1])) {
                        if (i > 0) {
                            sb.append(this.mSeparator);
                        }
                        sb.append(split[i]);
                    }
                }
            }
        }
        return sb.toString();
    }

    private String getLocalString(String str, int i, int i2) {
        String[] stringArray = getContext().getResources().getStringArray(i);
        String[] stringArray2 = getContext().getResources().getStringArray(i2);
        for (int i3 = 0; i3 < stringArray.length; i3++) {
            if (stringArray[i3].equalsIgnoreCase(str)) {
                return stringArray2[i3];
            }
        }
        return str;
    }

    private int getNetworkType(int i) {
        ServiceState serviceState = this.mKeyguardUpdateMonitor.mServiceStates.get(Integer.valueOf(i));
        if (serviceState == null || (serviceState.getDataRegState() != 0 && serviceState.getVoiceRegState() != 0)) {
            return 0;
        }
        int dataNetworkType = serviceState.getDataNetworkType();
        return dataNetworkType == 0 ? serviceState.getVoiceNetworkType() : dataNetworkType;
    }

    private String networkTypeToString(int i) {
        long bitMaskForNetworkType = TelephonyManager.getBitMaskForNetworkType(i);
        return getContext().getResources().getString((32843 & bitMaskForNetworkType) != 0 ? C1894R.string.config_rat_2g : (93108 & bitMaskForNetworkType) != 0 ? C1894R.string.config_rat_3g : (bitMaskForNetworkType & 397312) != 0 ? C1894R.string.config_rat_4g : C1894R.string.config_rat_unknown);
    }

    private String get5GNetworkClass(SubscriptionInfo subscriptionInfo, int i) {
        if (i == 20) {
            return this.mContext.getResources().getString(C1894R.string.data_connection_5g);
        }
        int simSlotIndex = subscriptionInfo.getSimSlotIndex();
        int subscriptionId = subscriptionInfo.getSubscriptionId();
        if (this.mFiveGServiceClient == null) {
            FiveGServiceClient instance = FiveGServiceClient.getInstance(this.mContext);
            this.mFiveGServiceClient = instance;
            instance.registerCallback(this.mCallback);
        }
        if (!this.mFiveGServiceClient.getCurrentServiceState(simSlotIndex).isNrIconTypeValid() || !isDataRegisteredOnLte(subscriptionId)) {
            return null;
        }
        return this.mContext.getResources().getString(C1894R.string.data_connection_5g);
    }

    private boolean isDataRegisteredOnLte(int i) {
        int dataNetworkType = ((TelephonyManager) this.mContext.getSystemService("phone")).getDataNetworkType(i);
        return dataNetworkType == 13 || dataNetworkType == 19;
    }
}
