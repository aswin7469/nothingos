package com.android.keyguard;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.wifi.WifiManager;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.keyguard.CarrierTextManager;
import com.android.settingslib.WirelessUtils;
import com.android.systemui.R$array;
import com.android.systemui.R$bool;
import com.android.systemui.R$string;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.policy.FiveGServiceClient;
import com.android.systemui.telephony.TelephonyListenerManager;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes.dex */
public class CarrierTextManager {
    private final Executor mBgExecutor;
    protected final KeyguardUpdateMonitorCallback mCallback;
    private CarrierTextCallback mCarrierTextCallback;
    private final Context mContext;
    private FiveGServiceClient mFiveGServiceClient;
    private final boolean mIsEmergencyCallCapable;
    protected KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final Executor mMainExecutor;
    private final AtomicBoolean mNetworkSupported;
    private final TelephonyCallback.ActiveDataSubscriptionIdListener mPhoneStateListener;
    private final CharSequence mSeparator;
    private final boolean mShowAirplaneMode;
    private final boolean mShowMissingSim;
    private final boolean[] mSimErrorState;
    private final int mSimSlotsNumber;
    private boolean mTelephonyCapable;
    private final TelephonyListenerManager mTelephonyListenerManager;
    private final TelephonyManager mTelephonyManager;
    private final WakefulnessLifecycle mWakefulnessLifecycle;
    private final WakefulnessLifecycle.Observer mWakefulnessObserver;
    private final WifiManager mWifiManager;

    /* loaded from: classes.dex */
    public interface CarrierTextCallback {
        default void finishedWakingUp() {
        }

        default void startedGoingToSleep() {
        }

        default void updateCarrierInfo(CarrierTextCallbackInfo carrierTextCallbackInfo) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum StatusMode {
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

    private CarrierTextManager(Context context, CharSequence charSequence, boolean z, boolean z2, WifiManager wifiManager, TelephonyManager telephonyManager, TelephonyListenerManager telephonyListenerManager, WakefulnessLifecycle wakefulnessLifecycle, Executor executor, Executor executor2, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        this.mNetworkSupported = new AtomicBoolean();
        this.mWakefulnessObserver = new WakefulnessLifecycle.Observer() { // from class: com.android.keyguard.CarrierTextManager.1
            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public void onFinishedWakingUp() {
                CarrierTextCallback carrierTextCallback = CarrierTextManager.this.mCarrierTextCallback;
                if (carrierTextCallback != null) {
                    carrierTextCallback.finishedWakingUp();
                }
            }

            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public void onStartedGoingToSleep() {
                CarrierTextCallback carrierTextCallback = CarrierTextManager.this.mCarrierTextCallback;
                if (carrierTextCallback != null) {
                    carrierTextCallback.startedGoingToSleep();
                }
            }
        };
        this.mCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.keyguard.CarrierTextManager.2
            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onRefreshCarrierInfo() {
                Log.d("CarrierTextController", "onRefreshCarrierInfo(), mTelephonyCapable: " + Boolean.toString(CarrierTextManager.this.mTelephonyCapable));
                CarrierTextManager.this.updateCarrierText();
            }

            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onTelephonyCapable(boolean z3) {
                Log.d("CarrierTextController", "onTelephonyCapable() mTelephonyCapable: " + Boolean.toString(z3));
                CarrierTextManager.this.mTelephonyCapable = z3;
                CarrierTextManager.this.updateCarrierText();
            }

            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onSimStateChanged(int i, int i2, int i3) {
                if (i2 < 0 || i2 >= CarrierTextManager.this.mSimSlotsNumber) {
                    Log.d("CarrierTextController", "onSimStateChanged() - slotId invalid: " + i2 + " mTelephonyCapable: " + Boolean.toString(CarrierTextManager.this.mTelephonyCapable));
                    return;
                }
                Log.d("CarrierTextController", "onSimStateChanged: " + CarrierTextManager.this.getStatusForIccState(i3));
                if (CarrierTextManager.this.getStatusForIccState(i3) == StatusMode.SimIoError) {
                    CarrierTextManager.this.mSimErrorState[i2] = true;
                    CarrierTextManager.this.updateCarrierText();
                } else if (!CarrierTextManager.this.mSimErrorState[i2]) {
                } else {
                    CarrierTextManager.this.mSimErrorState[i2] = false;
                    CarrierTextManager.this.updateCarrierText();
                }
            }
        };
        this.mPhoneStateListener = new TelephonyCallback.ActiveDataSubscriptionIdListener() { // from class: com.android.keyguard.CarrierTextManager.3
            @Override // android.telephony.TelephonyCallback.ActiveDataSubscriptionIdListener
            public void onActiveDataSubscriptionIdChanged(int i) {
                if (!CarrierTextManager.this.mNetworkSupported.get() || CarrierTextManager.this.mCarrierTextCallback == null) {
                    return;
                }
                CarrierTextManager.this.updateCarrierText();
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
        executor2.execute(new Runnable() { // from class: com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                CarrierTextManager.this.lambda$new$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        boolean hasSystemFeature = this.mContext.getPackageManager().hasSystemFeature("android.hardware.telephony");
        if (!hasSystemFeature || !this.mNetworkSupported.compareAndSet(false, hasSystemFeature)) {
            return;
        }
        lambda$setListening$4(this.mCarrierTextCallback);
    }

    private TelephonyManager getTelephonyManager() {
        return this.mTelephonyManager;
    }

    private CharSequence updateCarrierTextWithSimIoError(CharSequence charSequence, CharSequence[] charSequenceArr, int[] iArr, boolean z) {
        CharSequence carrierTextForSimState = getCarrierTextForSimState(8, "");
        for (int i = 0; i < getTelephonyManager().getActiveModemCount(); i++) {
            if (this.mSimErrorState[i]) {
                if (z) {
                    return concatenate(carrierTextForSimState, getContext().getText(17040142), this.mSeparator);
                }
                if (iArr[i] != -1) {
                    int i2 = iArr[i];
                    charSequenceArr[i2] = concatenate(carrierTextForSimState, charSequenceArr[i2], this.mSeparator);
                } else {
                    charSequence = concatenate(charSequence, carrierTextForSimState, this.mSeparator);
                }
            }
        }
        return charSequence;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: handleSetListening */
    public void lambda$setListening$4(final CarrierTextCallback carrierTextCallback) {
        if (carrierTextCallback != null) {
            this.mCarrierTextCallback = carrierTextCallback;
            if (this.mNetworkSupported.get()) {
                this.mMainExecutor.execute(new Runnable() { // from class: com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        CarrierTextManager.this.lambda$handleSetListening$1();
                    }
                });
                this.mWakefulnessLifecycle.addObserver(this.mWakefulnessObserver);
                this.mTelephonyListenerManager.addActiveDataSubscriptionIdListener(this.mPhoneStateListener);
                return;
            }
            this.mMainExecutor.execute(new Runnable() { // from class: com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    CarrierTextManager.lambda$handleSetListening$2(CarrierTextManager.CarrierTextCallback.this);
                }
            });
            return;
        }
        this.mCarrierTextCallback = null;
        this.mMainExecutor.execute(new Runnable() { // from class: com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                CarrierTextManager.this.lambda$handleSetListening$3();
            }
        });
        this.mWakefulnessLifecycle.removeObserver(this.mWakefulnessObserver);
        this.mTelephonyListenerManager.removeActiveDataSubscriptionIdListener(this.mPhoneStateListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleSetListening$1() {
        this.mKeyguardUpdateMonitor.registerCallback(this.mCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$handleSetListening$2(CarrierTextCallback carrierTextCallback) {
        carrierTextCallback.updateCarrierInfo(new CarrierTextCallbackInfo("", null, false, null));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleSetListening$3() {
        this.mKeyguardUpdateMonitor.removeCallback(this.mCallback);
    }

    public void setListening(final CarrierTextCallback carrierTextCallback) {
        this.mBgExecutor.execute(new Runnable() { // from class: com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                CarrierTextManager.this.lambda$setListening$4(carrierTextCallback);
            }
        });
    }

    protected List<SubscriptionInfo> getSubscriptionInfo() {
        return this.mKeyguardUpdateMonitor.getFilteredSubscriptionInfo(false);
    }

    /* JADX WARN: Removed duplicated region for block: B:61:0x01b6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void updateCarrierText() {
        String str;
        boolean z;
        CharSequence charSequence;
        ServiceState serviceState;
        WifiManager wifiManager;
        boolean z2 = getContext().getResources().getBoolean(R$bool.config_show_customize_carrier_name);
        List<SubscriptionInfo> subscriptionInfo = getSubscriptionInfo();
        int size = subscriptionInfo.size();
        int[] iArr = new int[size];
        Log.d("CarrierTextController", "updateCarrierText(): " + size);
        int[] iArr2 = new int[this.mSimSlotsNumber];
        for (int i = 0; i < this.mSimSlotsNumber; i++) {
            iArr2[i] = -1;
        }
        CharSequence[] charSequenceArr = new CharSequence[size];
        Log.d("CarrierTextController", "updateCarrierText(): " + size);
        int i2 = 0;
        boolean z3 = true;
        boolean z4 = false;
        while (true) {
            str = "";
            if (i2 >= size) {
                break;
            }
            int subscriptionId = subscriptionInfo.get(i2).getSubscriptionId();
            charSequenceArr[i2] = str;
            iArr[i2] = subscriptionId;
            iArr2[subscriptionInfo.get(i2).getSimSlotIndex()] = i2;
            int simState = this.mKeyguardUpdateMonitor.getSimState(subscriptionId);
            CharSequence carrierName = subscriptionInfo.get(i2).getCarrierName();
            if (z2) {
                carrierName = getCustomizeCarrierName(carrierName, subscriptionInfo.get(i2));
            }
            CharSequence carrierTextForSimState = getCarrierTextForSimState(simState, carrierName);
            StringBuilder sb = new StringBuilder();
            boolean z5 = z2;
            sb.append("Handling (subId=");
            sb.append(subscriptionId);
            sb.append("): ");
            sb.append(simState);
            sb.append(" ");
            sb.append((Object) carrierName);
            Log.d("CarrierTextController", sb.toString());
            if (carrierTextForSimState != null) {
                charSequenceArr[i2] = carrierTextForSimState;
                z3 = false;
            }
            if (simState == 5 && (serviceState = this.mKeyguardUpdateMonitor.mServiceStates.get(Integer.valueOf(subscriptionId))) != null && serviceState.getDataRegistrationState() == 0 && (serviceState.getRilDataRadioTechnology() != 18 || ((wifiManager = this.mWifiManager) != null && wifiManager.isWifiEnabled() && this.mWifiManager.getConnectionInfo() != null && this.mWifiManager.getConnectionInfo().getBSSID() != null))) {
                Log.d("CarrierTextController", "SIM ready and in service: subId=" + subscriptionId + ", ss=" + serviceState);
                z4 = true;
            }
            i2++;
            z2 = z5;
        }
        CharSequence charSequence2 = null;
        if (z3 && !z4) {
            if (size != 0) {
                charSequence2 = makeCarrierStringOnEmergencyCapable(getMissingSimMessage(), subscriptionInfo.get(0).getCarrierName());
            } else {
                CharSequence text = getContext().getText(17040142);
                Intent registerReceiver = getContext().registerReceiver(null, new IntentFilter("android.telephony.action.SERVICE_PROVIDERS_UPDATED"));
                if (registerReceiver != null) {
                    z = false;
                    String stringExtra = registerReceiver.getBooleanExtra("android.telephony.extra.SHOW_SPN", false) ? registerReceiver.getStringExtra("android.telephony.extra.SPN") : str;
                    if (registerReceiver.getBooleanExtra("android.telephony.extra.SHOW_PLMN", false)) {
                        str = registerReceiver.getStringExtra("android.telephony.extra.PLMN");
                    }
                    Log.d("CarrierTextController", "Getting plmn/spn sticky brdcst " + str + "/" + stringExtra);
                    text = Objects.equals(str, stringExtra) ? str : concatenate(str, stringExtra, this.mSeparator);
                } else {
                    z = false;
                }
                charSequence2 = makeCarrierStringOnEmergencyCapable(getMissingSimMessage(), text);
                if (TextUtils.isEmpty(charSequence2)) {
                    charSequence2 = joinNotEmpty(this.mSeparator, charSequenceArr);
                }
                CharSequence updateCarrierTextWithSimIoError = updateCarrierTextWithSimIoError(charSequence2, charSequenceArr, iArr2, z3);
                if (!z4 || !WirelessUtils.isAirplaneModeOn(this.mContext)) {
                    charSequence = updateCarrierTextWithSimIoError;
                } else {
                    charSequence = getAirplaneModeMessage();
                    z = true;
                }
                postToCallback(new CarrierTextCallbackInfo(charSequence, charSequenceArr, !z3, iArr, z));
            }
        }
        z = false;
        if (TextUtils.isEmpty(charSequence2)) {
        }
        CharSequence updateCarrierTextWithSimIoError2 = updateCarrierTextWithSimIoError(charSequence2, charSequenceArr, iArr2, z3);
        if (!z4) {
        }
        charSequence = updateCarrierTextWithSimIoError2;
        postToCallback(new CarrierTextCallbackInfo(charSequence, charSequenceArr, !z3, iArr, z));
    }

    protected void postToCallback(final CarrierTextCallbackInfo carrierTextCallbackInfo) {
        final CarrierTextCallback carrierTextCallback = this.mCarrierTextCallback;
        if (carrierTextCallback != null) {
            this.mMainExecutor.execute(new Runnable() { // from class: com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    CarrierTextManager.CarrierTextCallback.this.updateCarrierInfo(carrierTextCallbackInfo);
                }
            });
        }
    }

    private Context getContext() {
        return this.mContext;
    }

    private String getMissingSimMessage() {
        return (!this.mShowMissingSim || !this.mTelephonyCapable) ? "" : getContext().getString(R$string.keyguard_missing_sim_message_short);
    }

    private String getAirplaneModeMessage() {
        return this.mShowAirplaneMode ? getContext().getString(R$string.airplane_mode) : "";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.keyguard.CarrierTextManager$4  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode;

        static {
            int[] iArr = new int[StatusMode.values().length];
            $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode = iArr;
            try {
                iArr[StatusMode.Normal.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode[StatusMode.SimNotReady.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode[StatusMode.NetworkLocked.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode[StatusMode.SimMissing.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode[StatusMode.SimPermDisabled.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode[StatusMode.SimMissingLocked.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode[StatusMode.SimLocked.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode[StatusMode.SimPukLocked.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode[StatusMode.SimIoError.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode[StatusMode.SimUnknown.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
        }
    }

    private CharSequence getCarrierTextForSimState(int i, CharSequence charSequence) {
        switch (AnonymousClass4.$SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode[getStatusForIccState(i).ordinal()]) {
            case 1:
                return charSequence;
            case 2:
                return "";
            case 3:
                return makeCarrierStringOnEmergencyCapable(this.mContext.getText(R$string.keyguard_network_locked_message), charSequence);
            case 4:
            case 6:
            case 10:
            default:
                return null;
            case 5:
                return makeCarrierStringOnEmergencyCapable(getContext().getText(R$string.keyguard_permanent_disabled_sim_message_short), charSequence);
            case 7:
                return makeCarrierStringOnLocked(getContext().getText(R$string.keyguard_sim_locked_message), charSequence);
            case 8:
                return makeCarrierStringOnLocked(getContext().getText(R$string.keyguard_sim_puk_locked_message), charSequence);
            case 9:
                return makeCarrierStringOnEmergencyCapable(getContext().getText(R$string.keyguard_sim_error_message_short), charSequence);
        }
    }

    private CharSequence makeCarrierStringOnEmergencyCapable(CharSequence charSequence, CharSequence charSequence2) {
        return this.mIsEmergencyCallCapable ? concatenate(charSequence, charSequence2, this.mSeparator) : charSequence;
    }

    private CharSequence makeCarrierStringOnLocked(CharSequence charSequence, CharSequence charSequence2) {
        boolean z = !TextUtils.isEmpty(charSequence);
        boolean z2 = !TextUtils.isEmpty(charSequence2);
        return (!z || !z2) ? z ? charSequence : z2 ? charSequence2 : "" : this.mContext.getString(R$string.keyguard_carrier_name_with_sim_locked_template, charSequence2, charSequence);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public StatusMode getStatusForIccState(int i) {
        boolean z = true;
        if (this.mKeyguardUpdateMonitor.isDeviceProvisioned() || (i != 1 && i != 7)) {
            z = false;
        }
        if (z) {
            i = 4;
        }
        switch (i) {
            case 0:
                return StatusMode.SimUnknown;
            case 1:
                return StatusMode.SimMissing;
            case 2:
                return StatusMode.SimLocked;
            case 3:
                return StatusMode.SimPukLocked;
            case 4:
                return StatusMode.SimMissingLocked;
            case 5:
                return StatusMode.Normal;
            case 6:
                return StatusMode.SimNotReady;
            case 7:
                return StatusMode.SimPermDisabled;
            case 8:
                return StatusMode.SimIoError;
            default:
                return StatusMode.SimUnknown;
        }
    }

    private static CharSequence concatenate(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3) {
        boolean z = !TextUtils.isEmpty(charSequence);
        boolean z2 = !TextUtils.isEmpty(charSequence2);
        if (!z || !z2) {
            return z ? charSequence : z2 ? charSequence2 : "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(charSequence);
        sb.append(charSequence3);
        sb.append(charSequence2);
        return sb.toString();
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

    /* loaded from: classes.dex */
    public static class Builder {
        private final Executor mBgExecutor;
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

        public Builder(Context context, Resources resources, WifiManager wifiManager, TelephonyManager telephonyManager, TelephonyListenerManager telephonyListenerManager, WakefulnessLifecycle wakefulnessLifecycle, Executor executor, Executor executor2, KeyguardUpdateMonitor keyguardUpdateMonitor) {
            this.mContext = context;
            this.mSeparator = resources.getString(17040495);
            this.mWifiManager = wifiManager;
            this.mTelephonyManager = telephonyManager;
            this.mTelephonyListenerManager = telephonyListenerManager;
            this.mWakefulnessLifecycle = wakefulnessLifecycle;
            this.mMainExecutor = executor;
            this.mBgExecutor = executor2;
            this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
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
            return new CarrierTextManager(this.mContext, this.mSeparator, this.mShowAirplaneMode, this.mShowMissingSim, this.mWifiManager, this.mTelephonyManager, this.mTelephonyListenerManager, this.mWakefulnessLifecycle, this.mMainExecutor, this.mBgExecutor, this.mKeyguardUpdateMonitor);
        }
    }

    /* loaded from: classes.dex */
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
                split[i] = getLocalString(split[i], R$array.origin_carrier_names, R$array.locale_carrier_names);
                if (!TextUtils.isEmpty(split[i])) {
                    if (!TextUtils.isEmpty(networkTypeToString)) {
                        split[i] = split[i] + " " + networkTypeToString;
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
        if (serviceState == null || !(serviceState.getDataRegState() == 0 || serviceState.getVoiceRegState() == 0)) {
            return 0;
        }
        int dataNetworkType = serviceState.getDataNetworkType();
        return dataNetworkType == 0 ? serviceState.getVoiceNetworkType() : dataNetworkType;
    }

    private String networkTypeToString(int i) {
        int i2 = R$string.config_rat_unknown;
        long bitMaskForNetworkType = TelephonyManager.getBitMaskForNetworkType(i);
        if ((32843 & bitMaskForNetworkType) != 0) {
            i2 = R$string.config_rat_2g;
        } else if ((93108 & bitMaskForNetworkType) != 0) {
            i2 = R$string.config_rat_3g;
        } else if ((bitMaskForNetworkType & 397312) != 0) {
            i2 = R$string.config_rat_4g;
        }
        return getContext().getResources().getString(i2);
    }

    private String get5GNetworkClass(SubscriptionInfo subscriptionInfo, int i) {
        if (i == 20) {
            return this.mContext.getResources().getString(R$string.data_connection_5g);
        }
        int simSlotIndex = subscriptionInfo.getSimSlotIndex();
        int subscriptionId = subscriptionInfo.getSubscriptionId();
        if (this.mFiveGServiceClient == null) {
            FiveGServiceClient fiveGServiceClient = FiveGServiceClient.getInstance(this.mContext);
            this.mFiveGServiceClient = fiveGServiceClient;
            fiveGServiceClient.registerCallback(this.mCallback);
        }
        if (this.mFiveGServiceClient.getCurrentServiceState(simSlotIndex).isNrIconTypeValid() && isDataRegisteredOnLte(subscriptionId)) {
            return this.mContext.getResources().getString(R$string.data_connection_5g);
        }
        return null;
    }

    private boolean isDataRegisteredOnLte(int i) {
        int dataNetworkType = ((TelephonyManager) this.mContext.getSystemService("phone")).getDataNetworkType(i);
        return dataNetworkType == 13 || dataNetworkType == 19;
    }
}
