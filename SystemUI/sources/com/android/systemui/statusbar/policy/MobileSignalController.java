package com.android.systemui.statusbar.policy;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyManager;
import android.telephony.ims.ImsException;
import android.telephony.ims.ImsMmTelManager;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsRegistrationAttributes;
import android.telephony.ims.RegistrationManager;
import android.telephony.ims.feature.MmTelFeature;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import androidx.mediarouter.media.MediaRoute2Provider$$ExternalSyntheticLambda0;
import com.android.ims.FeatureConnector;
import com.android.ims.ImsManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.PhoneConstants;
import com.android.settingslib.AccessibilityContentDescriptions;
import com.android.settingslib.SignalIcon$MobileIconGroup;
import com.android.settingslib.SignalIcon$MobileState;
import com.android.settingslib.Utils;
import com.android.settingslib.graph.SignalDrawable;
import com.android.settingslib.mobile.MobileMappings;
import com.android.settingslib.mobile.MobileStatusTracker;
import com.android.settingslib.mobile.TelephonyIcons;
import com.android.settingslib.net.SignalStrengthUtil;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.policy.FiveGServiceClient;
import com.android.systemui.statusbar.policy.MobileSignalController$LogInfo$CurrentIconIdLog;
import com.android.systemui.statusbar.policy.NetworkController;
import com.android.systemui.util.CarrierConfigTracker;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes2.dex */
public class MobileSignalController extends SignalController<SignalIcon$MobileState, SignalIcon$MobileIconGroup> {
    private static final SimpleDateFormat SSDF = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
    private final MobileSignalController$LogInfo$CurrentIconIdLog _currentIconIdLog;
    private final Runnable _logRunnableForCurrentIconIdChanged;
    private MobileStatusTracker.Callback mCallback;
    private final CarrierConfigTracker mCarrierConfigTracker;
    private FiveGServiceClient mClient;
    private MobileMappings.Config mConfig;
    private SignalIcon$MobileIconGroup mDefaultIcons;
    private final MobileStatusTracker.SubscriptionDefaults mDefaults;
    private FeatureConnector<ImsManager> mFeatureConnector;
    private ImsManager mImsManager;
    private final ImsMmTelManager mImsMmTelManager;
    private boolean mIsVoiceOverCellularImsEnabled;
    private boolean mIsVowifiEnabled;
    private boolean mLastIsVoiceOverCellularImsEnabled;
    private boolean mLastIsVowifiEnabled;
    private int mLastLevel;
    private int mLastServiceStateDataNetworkType;
    private int mLastServiceStateVoiceNetworkType;
    private int mLastWlanCrossSimLevel;
    private int mLastWlanLevel;
    private int mLastWwanLevel;
    private int mMobileStatusHistoryIndex;
    @VisibleForTesting
    MobileStatusTracker mMobileStatusTracker;
    private final String mNetworkNameDefault;
    Map<String, SignalIcon$MobileIconGroup> mNetworkToIconLookup;
    private final ContentObserver mObserver;
    private final TelephonyManager mPhone;
    private final boolean mProviderModelBehavior;
    private final boolean mProviderModelSetting;
    private final Handler mReceiverHandler;
    private RegistrationManager.RegistrationCallback mRegistrationCallback;
    private ServiceState mServiceState;
    private SignalStrength mSignalStrength;
    final SubscriptionInfo mSubscriptionInfo;
    private int mImsType = 1;
    private int mDataState = 0;
    private PhoneConstants.DataState mMMSDataState = PhoneConstants.DataState.DISCONNECTED;
    private TelephonyDisplayInfo mTelephonyDisplayInfo = new TelephonyDisplayInfo(0, 0);
    @VisibleForTesting
    boolean mInflateSignalStrengths = false;
    private final String[] mMobileStatusHistory = new String[64];
    private int mCallState = 0;
    private final Runnable mTryRegisterIms = new Runnable() { // from class: com.android.systemui.statusbar.policy.MobileSignalController.5
        private int mRetryCount;

        @Override // java.lang.Runnable
        public void run() {
            try {
                this.mRetryCount++;
                ImsMmTelManager imsMmTelManager = MobileSignalController.this.mImsMmTelManager;
                Handler handler = MobileSignalController.this.mReceiverHandler;
                Objects.requireNonNull(handler);
                imsMmTelManager.registerImsRegistrationCallback(new MediaRoute2Provider$$ExternalSyntheticLambda0(handler), MobileSignalController.this.mRegistrationCallback);
                Log.d(MobileSignalController.this.mTag, "registerImsRegistrationCallback succeeded");
            } catch (ImsException | RuntimeException e) {
                if (this.mRetryCount >= 12) {
                    return;
                }
                Log.e(MobileSignalController.this.mTag, this.mRetryCount + " registerImsRegistrationCallback failed", e);
                MobileSignalController.this.mReceiverHandler.postDelayed(MobileSignalController.this.mTryRegisterIms, 5000L);
            }
        }
    };
    private ImsMmTelManager.CapabilityCallback mCapabilityCallback = new ImsMmTelManager.CapabilityCallback() { // from class: com.android.systemui.statusbar.policy.MobileSignalController.6
        @Override // android.telephony.ims.ImsMmTelManager.CapabilityCallback
        public void onCapabilitiesStatusChanged(MmTelFeature.MmTelCapabilities mmTelCapabilities) {
            ((SignalIcon$MobileState) MobileSignalController.this.mCurrentState).voiceCapable = mmTelCapabilities.isCapable(1);
            ((SignalIcon$MobileState) MobileSignalController.this.mCurrentState).videoCapable = mmTelCapabilities.isCapable(2);
            String str = MobileSignalController.this.mTag;
            Log.d(str, "onCapabilitiesStatusChanged isVoiceCapable=" + ((SignalIcon$MobileState) MobileSignalController.this.mCurrentState).voiceCapable + " isVideoCapable=" + ((SignalIcon$MobileState) MobileSignalController.this.mCurrentState).videoCapable);
            MobileSignalController.this.refreshVoiceOverCellularImsEnabled(mmTelCapabilities);
            MobileSignalController.this.refreshVowifiEnabled(mmTelCapabilities);
            MobileSignalController.this.notifyListenersIfNecessary();
        }
    };
    private final ImsMmTelManager.RegistrationCallback mImsRegistrationCallback = new ImsMmTelManager.RegistrationCallback() { // from class: com.android.systemui.statusbar.policy.MobileSignalController.7
        public void onRegistered(int i) {
            String str = MobileSignalController.this.mTag;
            Log.d(str, "onRegistered imsTransportType=" + i);
            MobileSignalController mobileSignalController = MobileSignalController.this;
            ((SignalIcon$MobileState) mobileSignalController.mCurrentState).imsRegistered = true;
            mobileSignalController.notifyListenersIfNecessary();
        }

        public void onRegistering(int i) {
            String str = MobileSignalController.this.mTag;
            Log.d(str, "onRegistering imsTransportType=" + i);
            MobileSignalController mobileSignalController = MobileSignalController.this;
            ((SignalIcon$MobileState) mobileSignalController.mCurrentState).imsRegistered = false;
            mobileSignalController.notifyListenersIfNecessary();
        }

        public void onUnregistered(ImsReasonInfo imsReasonInfo) {
            String str = MobileSignalController.this.mTag;
            Log.d(str, "onDeregistered imsReasonInfo=" + imsReasonInfo);
            MobileSignalController mobileSignalController = MobileSignalController.this;
            ((SignalIcon$MobileState) mobileSignalController.mCurrentState).imsRegistered = false;
            mobileSignalController.notifyListenersIfNecessary();
        }
    };
    private final BroadcastReceiver mVolteSwitchObserver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.policy.MobileSignalController.8
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String str = MobileSignalController.this.mTag;
            Log.d(str, "action=" + intent.getAction());
            if (MobileSignalController.this.mConfig.showVolteIcon) {
                MobileSignalController.this.notifyListeners();
            }
        }
    };
    @VisibleForTesting
    FiveGStateListener mFiveGStateListener = new FiveGStateListener();
    @VisibleForTesting
    FiveGServiceClient.FiveGServiceState mFiveGState = new FiveGServiceClient.FiveGServiceState();
    private final String mNetworkNameSeparator = getTextIfExists(R$string.status_bar_network_name_separator).toString();

    public MobileSignalController(Context context, MobileMappings.Config config, boolean z, TelephonyManager telephonyManager, CallbackHandler callbackHandler, NetworkControllerImpl networkControllerImpl, SubscriptionInfo subscriptionInfo, MobileStatusTracker.SubscriptionDefaults subscriptionDefaults, Looper looper, CarrierConfigTracker carrierConfigTracker, FeatureFlags featureFlags) {
        super("MobileSignalController(" + subscriptionInfo.getSubscriptionId() + ")", context, 0, callbackHandler, networkControllerImpl);
        Runnable runnable = new Runnable() { // from class: com.android.systemui.statusbar.policy.MobileSignalController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                MobileSignalController.this.lambda$new$0();
            }
        };
        this._logRunnableForCurrentIconIdChanged = runnable;
        this.mCarrierConfigTracker = carrierConfigTracker;
        this.mConfig = config;
        this.mPhone = telephonyManager;
        this.mDefaults = subscriptionDefaults;
        this.mSubscriptionInfo = subscriptionInfo;
        String charSequence = getTextIfExists(17040528).toString();
        this.mNetworkNameDefault = charSequence;
        this.mReceiverHandler = new Handler(looper);
        this.mNetworkToIconLookup = MobileMappings.mapIconSets(this.mConfig);
        this.mDefaultIcons = MobileMappings.getDefaultIcons(this.mConfig);
        charSequence = subscriptionInfo.getCarrierName() != null ? subscriptionInfo.getCarrierName().toString() : charSequence;
        T t = this.mLastState;
        T t2 = this.mCurrentState;
        ((SignalIcon$MobileState) t2).networkName = charSequence;
        ((SignalIcon$MobileState) t).networkName = charSequence;
        ((SignalIcon$MobileState) t2).networkNameData = charSequence;
        ((SignalIcon$MobileState) t).networkNameData = charSequence;
        ((SignalIcon$MobileState) t2).enabled = z;
        ((SignalIcon$MobileState) t).enabled = z;
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup = this.mDefaultIcons;
        ((SignalIcon$MobileState) t2).iconGroup = signalIcon$MobileIconGroup;
        ((SignalIcon$MobileState) t).iconGroup = signalIcon$MobileIconGroup;
        this.mFeatureConnector = ImsManager.getConnector(this.mContext, subscriptionInfo.getSimSlotIndex(), "?", new FeatureConnector.Listener<ImsManager>() { // from class: com.android.systemui.statusbar.policy.MobileSignalController.1
            public void connectionReady(ImsManager imsManager) throws com.android.ims.ImsException {
                Log.d(MobileSignalController.this.mTag, "ImsManager: connection ready.");
                MobileSignalController.this.mImsManager = imsManager;
                MobileSignalController.this.setListeners();
            }

            public void connectionUnavailable(int i) {
                Log.d(MobileSignalController.this.mTag, "ImsManager: connection unavailable.");
                MobileSignalController.this.removeListeners();
            }
        }, this.mContext.getMainExecutor());
        this.mObserver = new ContentObserver(new Handler(looper)) { // from class: com.android.systemui.statusbar.policy.MobileSignalController.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z2) {
                MobileSignalController.this.updateTelephony();
            }
        };
        this.mCallback = new MobileStatusTracker.Callback() { // from class: com.android.systemui.statusbar.policy.MobileSignalController.3
            private String mLastStatus;

            @Override // com.android.settingslib.mobile.MobileStatusTracker.Callback
            public void onMobileStatusChanged(boolean z2, MobileStatusTracker.MobileStatus mobileStatus) {
                if (Log.isLoggable(MobileSignalController.this.mTag, 3)) {
                    String str = MobileSignalController.this.mTag;
                    Log.d(str, "onMobileStatusChanged= updateTelephony=" + z2 + " mobileStatus=" + mobileStatus.toString());
                }
                String mobileStatus2 = mobileStatus.toString();
                if (!mobileStatus2.equals(this.mLastStatus)) {
                    this.mLastStatus = mobileStatus2;
                    MobileSignalController.this.recordLastMobileStatus(MobileSignalController.SSDF.format(Long.valueOf(System.currentTimeMillis())) + "," + mobileStatus2);
                }
                MobileSignalController.this.updateMobileStatus(mobileStatus);
                if (z2) {
                    MobileSignalController.this.updateTelephony();
                } else {
                    MobileSignalController.this.notifyListenersIfNecessary();
                }
            }
        };
        this.mRegistrationCallback = new RegistrationManager.RegistrationCallback() { // from class: com.android.systemui.statusbar.policy.MobileSignalController.4
            @Override // android.telephony.ims.RegistrationManager.RegistrationCallback
            public void onRegistered(ImsRegistrationAttributes imsRegistrationAttributes) {
                String str = MobileSignalController.this.mTag;
                Log.d(str, "onRegistered: attributes=" + imsRegistrationAttributes);
                int transportType = imsRegistrationAttributes.getTransportType();
                int attributeFlags = imsRegistrationAttributes.getAttributeFlags();
                if (transportType == 1) {
                    MobileSignalController.this.mImsType = 1;
                    MobileSignalController mobileSignalController = MobileSignalController.this;
                    int callStrengthIcon = mobileSignalController.getCallStrengthIcon(mobileSignalController.mLastWwanLevel, false);
                    MobileSignalController mobileSignalController2 = MobileSignalController.this;
                    NetworkController.IconState iconState = new NetworkController.IconState(true, callStrengthIcon, mobileSignalController2.getCallStrengthDescription(mobileSignalController2.mLastWwanLevel, false));
                    MobileSignalController mobileSignalController3 = MobileSignalController.this;
                    mobileSignalController3.notifyCallStateChange(iconState, mobileSignalController3.mSubscriptionInfo.getSubscriptionId());
                } else if (transportType != 2) {
                } else {
                    if (attributeFlags == 0) {
                        MobileSignalController.this.mImsType = 2;
                        MobileSignalController mobileSignalController4 = MobileSignalController.this;
                        int callStrengthIcon2 = mobileSignalController4.getCallStrengthIcon(mobileSignalController4.mLastWlanLevel, true);
                        MobileSignalController mobileSignalController5 = MobileSignalController.this;
                        NetworkController.IconState iconState2 = new NetworkController.IconState(true, callStrengthIcon2, mobileSignalController5.getCallStrengthDescription(mobileSignalController5.mLastWlanLevel, true));
                        MobileSignalController mobileSignalController6 = MobileSignalController.this;
                        mobileSignalController6.notifyCallStateChange(iconState2, mobileSignalController6.mSubscriptionInfo.getSubscriptionId());
                    } else if (attributeFlags != 1) {
                    } else {
                        MobileSignalController.this.mImsType = 3;
                        MobileSignalController mobileSignalController7 = MobileSignalController.this;
                        int callStrengthIcon3 = mobileSignalController7.getCallStrengthIcon(mobileSignalController7.mLastWlanCrossSimLevel, false);
                        MobileSignalController mobileSignalController8 = MobileSignalController.this;
                        NetworkController.IconState iconState3 = new NetworkController.IconState(true, callStrengthIcon3, mobileSignalController8.getCallStrengthDescription(mobileSignalController8.mLastWlanCrossSimLevel, false));
                        MobileSignalController mobileSignalController9 = MobileSignalController.this;
                        mobileSignalController9.notifyCallStateChange(iconState3, mobileSignalController9.mSubscriptionInfo.getSubscriptionId());
                    }
                }
            }

            @Override // android.telephony.ims.RegistrationManager.RegistrationCallback
            public void onUnregistered(ImsReasonInfo imsReasonInfo) {
                String str = MobileSignalController.this.mTag;
                Log.d(str, "onDeregistered: info=" + imsReasonInfo);
                MobileSignalController.this.mImsType = 1;
                MobileSignalController mobileSignalController = MobileSignalController.this;
                int callStrengthIcon = mobileSignalController.getCallStrengthIcon(mobileSignalController.mLastWwanLevel, false);
                MobileSignalController mobileSignalController2 = MobileSignalController.this;
                NetworkController.IconState iconState = new NetworkController.IconState(true, callStrengthIcon, mobileSignalController2.getCallStrengthDescription(mobileSignalController2.mLastWwanLevel, false));
                MobileSignalController mobileSignalController3 = MobileSignalController.this;
                mobileSignalController3.notifyCallStateChange(iconState, mobileSignalController3.mSubscriptionInfo.getSubscriptionId());
            }
        };
        this.mImsMmTelManager = ImsMmTelManager.createForSubscriptionId(subscriptionInfo.getSubscriptionId());
        this.mMobileStatusTracker = new MobileStatusTracker(telephonyManager, looper, subscriptionInfo, subscriptionDefaults, this.mCallback);
        this.mProviderModelBehavior = featureFlags.isCombinedStatusBarSignalIconsEnabled();
        this.mProviderModelSetting = featureFlags.isProviderModelSettingEnabled();
        this._currentIconIdLog = new MobileSignalController$LogInfo$CurrentIconIdLog(this.mTag, runnable);
    }

    public void setConfiguration(MobileMappings.Config config) {
        this.mConfig = config;
        updateInflateSignalStrength();
        this.mNetworkToIconLookup = MobileMappings.mapIconSets(this.mConfig);
        this.mDefaultIcons = MobileMappings.getDefaultIcons(this.mConfig);
        updateTelephony();
    }

    public void setAirplaneMode(boolean z) {
        ((SignalIcon$MobileState) this.mCurrentState).airplaneMode = z;
        notifyListenersIfNecessary();
    }

    public void setUserSetupComplete(boolean z) {
        ((SignalIcon$MobileState) this.mCurrentState).userSetup = z;
        notifyListenersIfNecessary();
    }

    @Override // com.android.systemui.statusbar.policy.SignalController
    public void updateConnectivity(BitSet bitSet, BitSet bitSet2) {
        boolean z = bitSet2.get(this.mTransportType);
        ((SignalIcon$MobileState) this.mCurrentState).isDefault = bitSet.get(this.mTransportType);
        T t = this.mCurrentState;
        ((SignalIcon$MobileState) t).inetCondition = (z || !((SignalIcon$MobileState) t).isDefault) ? 1 : 0;
        notifyListenersIfNecessary();
    }

    public void setCarrierNetworkChangeMode(boolean z) {
        ((SignalIcon$MobileState) this.mCurrentState).carrierNetworkChangeMode = z;
        updateTelephony();
    }

    public void registerListener() {
        this.mMobileStatusTracker.setListening(true);
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("mobile_data"), true, this.mObserver);
        ContentResolver contentResolver = this.mContext.getContentResolver();
        contentResolver.registerContentObserver(Settings.Global.getUriFor("mobile_data" + this.mSubscriptionInfo.getSubscriptionId()), true, this.mObserver);
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("data_roaming"), true, this.mObserver);
        ContentResolver contentResolver2 = this.mContext.getContentResolver();
        contentResolver2.registerContentObserver(Settings.Global.getUriFor("data_roaming" + this.mSubscriptionInfo.getSubscriptionId()), true, this.mObserver);
        this.mContext.registerReceiver(this.mVolteSwitchObserver, new IntentFilter("org.codeaurora.intent.action.ACTION_ENHANCE_4G_SWITCH"));
        this.mFeatureConnector.connect();
        if (this.mProviderModelBehavior) {
            this.mReceiverHandler.post(this.mTryRegisterIms);
        }
    }

    public void unregisterListener() {
        this.mMobileStatusTracker.setListening(false);
        this.mContext.getContentResolver().unregisterContentObserver(this.mObserver);
        this.mImsMmTelManager.unregisterImsRegistrationCallback(this.mRegistrationCallback);
        this.mContext.unregisterReceiver(this.mVolteSwitchObserver);
        this.mFeatureConnector.disconnect();
    }

    private void updateInflateSignalStrength() {
        this.mInflateSignalStrengths = SignalStrengthUtil.shouldInflateSignalStrength(this.mContext, this.mSubscriptionInfo.getSubscriptionId());
    }

    private int getNumLevels() {
        if (this.mInflateSignalStrengths) {
            return CellSignalStrength.getNumSignalStrengthLevels() + 1;
        }
        return CellSignalStrength.getNumSignalStrengthLevels();
    }

    @Override // com.android.systemui.statusbar.policy.SignalController
    public int getCurrentIconId() {
        T t = this.mCurrentState;
        if (((SignalIcon$MobileState) t).iconGroup == TelephonyIcons.CARRIER_NETWORK_CHANGE) {
            this._currentIconIdLog.logIfChanged(MobileSignalController$LogInfo$CurrentIconIdLog.Changed.CARRIER_NETWORK_CHANGE);
            return SignalDrawable.getCarrierChangeState(getNumLevels());
        }
        boolean z = false;
        if (((SignalIcon$MobileState) t).connected) {
            int i = ((SignalIcon$MobileState) t).level;
            if (this.mInflateSignalStrengths) {
                i++;
            }
            boolean z2 = true;
            boolean z3 = ((SignalIcon$MobileState) t).userSetup && (((SignalIcon$MobileState) t).iconGroup == TelephonyIcons.DATA_DISABLED || (((SignalIcon$MobileState) t).iconGroup == TelephonyIcons.NOT_DEFAULT_DATA && ((SignalIcon$MobileState) t).defaultDataOff));
            boolean z4 = ((SignalIcon$MobileState) t).inetCondition == 0;
            boolean z5 = z3 || z4;
            if (z3) {
                boolean z6 = ((SignalIcon$MobileState) t).iconGroup == TelephonyIcons.DATA_DISABLED;
                if (((SignalIcon$MobileState) t).iconGroup != TelephonyIcons.NOT_DEFAULT_DATA || !((SignalIcon$MobileState) t).defaultDataOff) {
                    z2 = false;
                }
                Log.i(this.mTag, "cancel cutout, isIconGroupDataDisabled: " + z6 + ", isIconGroupNoDefaultData: " + z2);
                z5 = false;
            }
            if (!this.mConfig.hideNoInternetState) {
                z = z5;
            }
            MobileSignalController$LogInfo$CurrentIconIdLog.Changed changed = MobileSignalController$LogInfo$CurrentIconIdLog.Changed.CONNECTED;
            if (z4) {
                changed = MobileSignalController$LogInfo$CurrentIconIdLog.Changed.CONNECTED_NOINTERNET;
            } else if (z3) {
                changed = MobileSignalController$LogInfo$CurrentIconIdLog.Changed.CONNECTED_DATADISABED;
            }
            this._currentIconIdLog.logIfChanged(changed);
            return SignalDrawable.getState(i, getNumLevels(), z);
        } else if (((SignalIcon$MobileState) t).enabled) {
            this._currentIconIdLog.logIfChanged(MobileSignalController$LogInfo$CurrentIconIdLog.Changed.ENABLED);
            return SignalDrawable.getEmptyState(getNumLevels());
        } else {
            this._currentIconIdLog.logIfChanged(MobileSignalController$LogInfo$CurrentIconIdLog.Changed.NONE);
            return 0;
        }
    }

    @Override // com.android.systemui.statusbar.policy.SignalController
    public int getQsCurrentIconId() {
        return getCurrentIconId();
    }

    private boolean isVolteSwitchOn() {
        ImsManager imsManager = this.mImsManager;
        return imsManager != null && imsManager.isEnhanced4gLteModeSettingEnabledByUser();
    }

    private int getVolteResId() {
        getVoiceNetworkType();
        if (((SignalIcon$MobileState) this.mCurrentState).imsRegistered && this.mIsVoiceOverCellularImsEnabled) {
            return R$drawable.ic_volte;
        }
        if (this.mTelephonyDisplayInfo.getNetworkType() == 13) {
            return 0;
        }
        this.mTelephonyDisplayInfo.getNetworkType();
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setListeners() {
        ImsManager imsManager = this.mImsManager;
        if (imsManager == null) {
            Log.e(this.mTag, "setListeners mImsManager is null");
            return;
        }
        try {
            imsManager.addCapabilitiesCallback(this.mCapabilityCallback, this.mContext.getMainExecutor());
            this.mImsManager.addRegistrationCallback(this.mImsRegistrationCallback, this.mContext.getMainExecutor());
            String str = this.mTag;
            Log.d(str, "addCapabilitiesCallback " + this.mCapabilityCallback + " into " + this.mImsManager);
            String str2 = this.mTag;
            Log.d(str2, "addRegistrationCallback " + this.mImsRegistrationCallback + " into " + this.mImsManager);
        } catch (com.android.ims.ImsException unused) {
            Log.d(this.mTag, "unable to addCapabilitiesCallback callback.");
        }
        queryImsState();
    }

    private void queryImsState() {
        TelephonyManager createForSubscriptionId = this.mPhone.createForSubscriptionId(this.mSubscriptionInfo.getSubscriptionId());
        ((SignalIcon$MobileState) this.mCurrentState).voiceCapable = createForSubscriptionId.isVolteAvailable();
        ((SignalIcon$MobileState) this.mCurrentState).videoCapable = createForSubscriptionId.isVideoTelephonyAvailable();
        ((SignalIcon$MobileState) this.mCurrentState).imsRegistered = this.mPhone.isImsRegistered(this.mSubscriptionInfo.getSubscriptionId());
        if (SignalController.DEBUG) {
            String str = this.mTag;
            Log.d(str, "queryImsState tm=" + createForSubscriptionId + " phone=" + this.mPhone + " voiceCapable=" + ((SignalIcon$MobileState) this.mCurrentState).voiceCapable + " videoCapable=" + ((SignalIcon$MobileState) this.mCurrentState).videoCapable + " imsResitered=" + ((SignalIcon$MobileState) this.mCurrentState).imsRegistered);
        }
        notifyListenersIfNecessary();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeListeners() {
        ImsManager imsManager = this.mImsManager;
        if (imsManager == null) {
            Log.e(this.mTag, "removeListeners mImsManager is null");
            return;
        }
        imsManager.removeCapabilitiesCallback(this.mCapabilityCallback);
        this.mImsManager.removeRegistrationListener(this.mImsRegistrationCallback);
        String str = this.mTag;
        Log.d(str, "removeCapabilitiesCallback " + this.mCapabilityCallback + " from " + this.mImsManager);
        String str2 = this.mTag;
        Log.d(str2, "removeRegistrationCallback " + this.mImsRegistrationCallback + " from " + this.mImsManager);
    }

    /* JADX WARN: Removed duplicated region for block: B:142:0x0278  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x02ca  */
    /* JADX WARN: Removed duplicated region for block: B:168:0x036b  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x027d  */
    @Override // com.android.systemui.statusbar.policy.SignalController
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void notifyListeners(NetworkController.SignalCallback signalCallback) {
        int i;
        NetworkController.IconState iconState;
        String str;
        int i2;
        int i3;
        MobileMappings.Config config;
        int i4;
        NetworkController.IconState iconState2;
        String str2;
        NetworkController.IconState iconState3;
        String str3;
        int i5;
        if (this.mNetworkController.isCarrierMergedWifi(this.mSubscriptionInfo.getSubscriptionId())) {
            return;
        }
        SignalIcon$MobileIconGroup icons = getIcons();
        String charSequence = getTextIfExists(getContentDescription()).toString();
        CharSequence textIfExists = getTextIfExists(icons.dataContentDescription);
        String obj = Html.fromHtml(textIfExists.toString(), 0).toString();
        if (((SignalIcon$MobileState) this.mCurrentState).inetCondition == 0) {
            obj = this.mContext.getString(R$string.data_connection_no_internet);
        }
        String str4 = obj;
        T t = this.mCurrentState;
        boolean z = (((SignalIcon$MobileState) t).iconGroup == TelephonyIcons.DATA_DISABLED || ((SignalIcon$MobileState) t).iconGroup == TelephonyIcons.NOT_DEFAULT_DATA) && ((SignalIcon$MobileState) t).userSetup;
        String str5 = null;
        if (this.mProviderModelBehavior) {
            boolean z2 = ((SignalIcon$MobileState) t).dataConnected || z;
            if (!((SignalIcon$MobileState) t).dataSim || !((SignalIcon$MobileState) t).isDefault) {
                iconState3 = null;
                str3 = null;
                i5 = 0;
            } else {
                int i6 = (z2 || this.mConfig.alwaysShowDataRatIcon) ? icons.qsDataType : 0;
                NetworkController.IconState iconState4 = new NetworkController.IconState(((SignalIcon$MobileState) t).enabled && !((SignalIcon$MobileState) t).isEmergency, getQsCurrentIconId(), charSequence);
                T t2 = this.mCurrentState;
                if (!((SignalIcon$MobileState) t2).isEmergency) {
                    str5 = ((SignalIcon$MobileState) t2).networkName;
                }
                str3 = str5;
                i5 = i6;
                iconState3 = iconState4;
            }
            T t3 = this.mCurrentState;
            boolean z3 = ((SignalIcon$MobileState) t3).dataConnected && !((SignalIcon$MobileState) t3).carrierNetworkChangeMode && ((SignalIcon$MobileState) t3).activityIn;
            boolean z4 = ((SignalIcon$MobileState) t3).dataConnected && !((SignalIcon$MobileState) t3).carrierNetworkChangeMode && ((SignalIcon$MobileState) t3).activityOut;
            boolean z5 = z2 & (((SignalIcon$MobileState) t3).dataSim && ((SignalIcon$MobileState) t3).isDefault);
            boolean z6 = z5 && !((SignalIcon$MobileState) t3).airplaneMode;
            signalCallback.setMobileDataIndicators(new NetworkController.MobileDataIndicators(new NetworkController.IconState((((SignalIcon$MobileState) t3).roaming || z5) && !((SignalIcon$MobileState) t3).airplaneMode, getCurrentIconId(), charSequence), iconState3, (z5 || this.mConfig.alwaysShowDataRatIcon) ? icons.dataType : 0, i5, z3, z4, (!this.mConfig.showVolteIcon || !isVolteSwitchOn()) ? 0 : getVolteResId(), str4, textIfExists, str3, icons.isWide, this.mSubscriptionInfo.getSubscriptionId(), ((SignalIcon$MobileState) this.mCurrentState).roaming, z6));
            return;
        }
        boolean z7 = ((SignalIcon$MobileState) t).dataConnected || z;
        NetworkController.IconState iconState5 = new NetworkController.IconState(((SignalIcon$MobileState) t).enabled && !((SignalIcon$MobileState) t).airplaneMode, getCurrentIconId(), charSequence);
        if (this.mProviderModelSetting) {
            T t4 = this.mCurrentState;
            if (((SignalIcon$MobileState) t4).dataSim && ((SignalIcon$MobileState) t4).isDefault) {
                i = (z7 || this.mConfig.alwaysShowDataRatIcon) ? icons.qsDataType : 0;
                iconState = new NetworkController.IconState(((SignalIcon$MobileState) t4).enabled && !((SignalIcon$MobileState) t4).isEmergency, getQsCurrentIconId(), charSequence);
                T t5 = this.mCurrentState;
                if (!((SignalIcon$MobileState) t5).isEmergency) {
                    str5 = ((SignalIcon$MobileState) t5).networkName;
                }
                str = str5;
                i2 = i;
            }
            iconState = null;
            str = null;
            i2 = 0;
        } else {
            T t6 = this.mCurrentState;
            if (((SignalIcon$MobileState) t6).dataSim) {
                i = (z7 || this.mConfig.alwaysShowDataRatIcon) ? icons.qsDataType : 0;
                iconState = new NetworkController.IconState(((SignalIcon$MobileState) t6).enabled && !((SignalIcon$MobileState) t6).isEmergency, getQsCurrentIconId(), charSequence);
                T t7 = this.mCurrentState;
                if (!((SignalIcon$MobileState) t7).isEmergency) {
                    str5 = ((SignalIcon$MobileState) t7).networkName;
                }
                str = str5;
                i2 = i;
            }
            iconState = null;
            str = null;
            i2 = 0;
        }
        T t8 = this.mCurrentState;
        boolean z8 = ((SignalIcon$MobileState) t8).dataConnected && !((SignalIcon$MobileState) t8).carrierNetworkChangeMode && ((SignalIcon$MobileState) t8).activityIn;
        boolean z9 = ((SignalIcon$MobileState) t8).dataConnected && !((SignalIcon$MobileState) t8).carrierNetworkChangeMode && ((SignalIcon$MobileState) t8).activityOut;
        boolean z10 = (((SignalIcon$MobileState) t8).isDefault || z) & z7;
        if (!z10) {
            MobileMappings.Config config2 = this.mConfig;
            if (!config2.alwaysShowDataRatIcon && !config2.alwaysShowNetworkTypeIcon) {
                i3 = 0;
                config = this.mConfig;
                if (!config.enableRatIconEnhancement) {
                    i3 = getEnhancementDataRatIcon();
                } else if (config.enableDdsRatIconEnhancement) {
                    i3 = getEnhancementDdsRatIcon();
                }
                SignalIcon$MobileIconGroup vowifiIconGroup = getVowifiIconGroup();
                if (this.mConfig.showVowifiIcon || vowifiIconGroup == null) {
                    i4 = i3;
                    iconState2 = iconState5;
                } else {
                    int i7 = vowifiIconGroup.dataType;
                    T t9 = this.mCurrentState;
                    iconState2 = new NetworkController.IconState(true, (!((SignalIcon$MobileState) t9).enabled || ((SignalIcon$MobileState) t9).airplaneMode) ? -1 : iconState5.icon, iconState5.contentDescription);
                    i4 = i7;
                }
                int volteResId = (this.mConfig.showVolteIcon || !isVolteSwitchOn()) ? 0 : getVolteResId();
                if (!SignalController.DEBUG) {
                    String str6 = this.mTag;
                    StringBuilder sb = new StringBuilder();
                    sb.append("notifyListeners mConfig.alwaysShowNetworkTypeIcon=");
                    sb.append(this.mConfig.alwaysShowNetworkTypeIcon);
                    sb.append("  getNetworkType:");
                    sb.append(this.mTelephonyDisplayInfo.getNetworkType());
                    sb.append("/");
                    str2 = str;
                    sb.append(TelephonyManager.getNetworkTypeName(this.mTelephonyDisplayInfo.getNetworkType()));
                    sb.append(" voiceNetType=");
                    sb.append(getVoiceNetworkType());
                    sb.append("/");
                    sb.append(TelephonyManager.getNetworkTypeName(getVoiceNetworkType()));
                    sb.append(" showDataIcon=");
                    sb.append(z10);
                    sb.append(" mConfig.alwaysShowDataRatIcon=");
                    sb.append(this.mConfig.alwaysShowDataRatIcon);
                    sb.append(" icons.dataType=");
                    sb.append(icons.dataType);
                    sb.append(" mConfig.showVolteIcon=");
                    sb.append(this.mConfig.showVolteIcon);
                    sb.append(" isVolteSwitchOn=");
                    sb.append(isVolteSwitchOn());
                    sb.append(" volteIcon=");
                    sb.append(volteResId);
                    sb.append(" mConfig.showVowifiIcon=");
                    sb.append(this.mConfig.showVowifiIcon);
                    Log.d(str6, sb.toString());
                } else {
                    str2 = str;
                }
                T t10 = this.mCurrentState;
                signalCallback.setMobileDataIndicators(new NetworkController.MobileDataIndicators(iconState2, iconState, i4, i2, z8, z9, volteResId, str4, textIfExists, str2, icons.isWide, this.mSubscriptionInfo.getSubscriptionId(), ((SignalIcon$MobileState) this.mCurrentState).roaming, !((SignalIcon$MobileState) t10).enabled && !((SignalIcon$MobileState) t10).airplaneMode));
            }
        }
        i3 = icons.dataType;
        config = this.mConfig;
        if (!config.enableRatIconEnhancement) {
        }
        SignalIcon$MobileIconGroup vowifiIconGroup2 = getVowifiIconGroup();
        if (this.mConfig.showVowifiIcon) {
        }
        i4 = i3;
        iconState2 = iconState5;
        if (this.mConfig.showVolteIcon) {
        }
        if (!SignalController.DEBUG) {
        }
        T t102 = this.mCurrentState;
        signalCallback.setMobileDataIndicators(new NetworkController.MobileDataIndicators(iconState2, iconState, i4, i2, z8, z9, volteResId, str4, textIfExists, str2, icons.isWide, this.mSubscriptionInfo.getSubscriptionId(), ((SignalIcon$MobileState) this.mCurrentState).roaming, !((SignalIcon$MobileState) t102).enabled && !((SignalIcon$MobileState) t102).airplaneMode));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.systemui.statusbar.policy.SignalController
    /* renamed from: cleanState */
    public SignalIcon$MobileState mo1340cleanState() {
        return new SignalIcon$MobileState();
    }

    private boolean isCdma() {
        SignalStrength signalStrength = this.mSignalStrength;
        return signalStrength != null && !signalStrength.isGsm();
    }

    public boolean isEmergencyOnly() {
        ServiceState serviceState = this.mServiceState;
        return serviceState != null && serviceState.isEmergencyOnly();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getNetworkNameForCarrierWiFi() {
        return this.mPhone.getSimOperatorName();
    }

    private boolean isRoaming() {
        if (isCarrierNetworkChangeActive()) {
            return false;
        }
        if (isCdma()) {
            return this.mPhone.getCdmaEnhancedRoamingIndicatorDisplayNumber() != 1;
        }
        ServiceState serviceState = this.mServiceState;
        return serviceState != null && serviceState.getRoaming();
    }

    private boolean isCarrierNetworkChangeActive() {
        return ((SignalIcon$MobileState) this.mCurrentState).carrierNetworkChangeMode;
    }

    public void handleBroadcast(Intent intent) {
        String action = intent.getAction();
        if (action.equals("android.telephony.action.SERVICE_PROVIDERS_UPDATED")) {
            updateNetworkName(intent.getBooleanExtra("android.telephony.extra.SHOW_SPN", false), intent.getStringExtra("android.telephony.extra.SPN"), intent.getStringExtra("android.telephony.extra.DATA_SPN"), intent.getBooleanExtra("android.telephony.extra.SHOW_PLMN", false), intent.getStringExtra("android.telephony.extra.PLMN"));
            notifyListenersIfNecessary();
        } else if (action.equals("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED")) {
            updateDataSim();
            notifyListenersIfNecessary();
        } else if (!action.equals("android.intent.action.ANY_DATA_STATE")) {
        } else {
            String stringExtra = intent.getStringExtra("apnType");
            String stringExtra2 = intent.getStringExtra("state");
            if (!"mms".equals(stringExtra)) {
                return;
            }
            if (SignalController.DEBUG) {
                String str = this.mTag;
                Log.d(str, "handleBroadcast MMS connection state=" + stringExtra2);
            }
            this.mMMSDataState = PhoneConstants.DataState.valueOf(stringExtra2);
            updateTelephony();
        }
    }

    private void updateDataSim() {
        int activeDataSubId = this.mDefaults.getActiveDataSubId();
        boolean z = true;
        if (SubscriptionManager.isValidSubscriptionId(activeDataSubId)) {
            SignalIcon$MobileState signalIcon$MobileState = (SignalIcon$MobileState) this.mCurrentState;
            if (activeDataSubId != this.mSubscriptionInfo.getSubscriptionId()) {
                z = false;
            }
            signalIcon$MobileState.dataSim = z;
            return;
        }
        ((SignalIcon$MobileState) this.mCurrentState).dataSim = true;
    }

    void updateNetworkName(boolean z, String str, String str2, boolean z2, String str3) {
        if (SignalController.CHATTY) {
            Log.d("CarrierLabel", "updateNetworkName showSpn=" + z + " spn=" + str + " dataSpn=" + str2 + " showPlmn=" + z2 + " plmn=" + str3);
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        if (z2 && str3 != null) {
            sb.append(str3);
            sb2.append(str3);
        }
        if (z && str != null) {
            if (sb.length() != 0) {
                sb.append(this.mNetworkNameSeparator);
            }
            sb.append(str);
        }
        if (sb.length() != 0) {
            ((SignalIcon$MobileState) this.mCurrentState).networkName = sb.toString();
        } else {
            ((SignalIcon$MobileState) this.mCurrentState).networkName = this.mNetworkNameDefault;
        }
        if (z && str2 != null) {
            if (sb2.length() != 0) {
                sb2.append(this.mNetworkNameSeparator);
            }
            sb2.append(str2);
        }
        if (sb2.length() != 0) {
            ((SignalIcon$MobileState) this.mCurrentState).networkNameData = sb2.toString();
            return;
        }
        ((SignalIcon$MobileState) this.mCurrentState).networkNameData = this.mNetworkNameDefault;
    }

    private int getCdmaLevel(SignalStrength signalStrength) {
        List cellSignalStrengths = signalStrength.getCellSignalStrengths(CellSignalStrengthCdma.class);
        if (!cellSignalStrengths.isEmpty()) {
            return ((CellSignalStrengthCdma) cellSignalStrengths.get(0)).getLevel();
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMobileStatus(MobileStatusTracker.MobileStatus mobileStatus) {
        T t = this.mCurrentState;
        ((SignalIcon$MobileState) t).activityIn = mobileStatus.activityIn;
        ((SignalIcon$MobileState) t).activityOut = mobileStatus.activityOut;
        ((SignalIcon$MobileState) t).dataSim = mobileStatus.dataSim;
        ((SignalIcon$MobileState) t).carrierNetworkChangeMode = mobileStatus.carrierNetworkChangeMode;
        this.mDataState = mobileStatus.dataState;
        notifyMobileLevelChangeIfNecessary(mobileStatus.signalStrength);
        this.mSignalStrength = mobileStatus.signalStrength;
        this.mTelephonyDisplayInfo = mobileStatus.telephonyDisplayInfo;
        ServiceState serviceState = this.mServiceState;
        int state = serviceState != null ? serviceState.getState() : -1;
        ServiceState serviceState2 = mobileStatus.serviceState;
        this.mServiceState = serviceState2;
        int state2 = serviceState2 != null ? serviceState2.getState() : -1;
        if (!this.mProviderModelBehavior || state == state2) {
            return;
        }
        if (state != -1 && state != 0 && state2 != 0) {
            return;
        }
        notifyCallStateChange(new NetworkController.IconState((state2 != 0) & (true ^ hideNoCalling()), R$drawable.ic_qs_no_calling_sms, getTextIfExists(AccessibilityContentDescriptions.NO_CALLING).toString()), this.mSubscriptionInfo.getSubscriptionId());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateNoCallingState() {
        ServiceState serviceState = this.mServiceState;
        notifyCallStateChange(new NetworkController.IconState(((serviceState != null ? serviceState.getState() : -1) != 0) & (true ^ hideNoCalling()), R$drawable.ic_qs_no_calling_sms, getTextIfExists(AccessibilityContentDescriptions.NO_CALLING).toString()), this.mSubscriptionInfo.getSubscriptionId());
    }

    private boolean hideNoCalling() {
        return this.mNetworkController.hasDefaultNetwork() && this.mCarrierConfigTracker.getNoCallingConfig(this.mSubscriptionInfo.getSubscriptionId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCallStrengthIcon(int i, boolean z) {
        if (z) {
            return TelephonyIcons.WIFI_CALL_STRENGTH_ICONS[i];
        }
        return TelephonyIcons.MOBILE_CALL_STRENGTH_ICONS[i];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getCallStrengthDescription(int i, boolean z) {
        if (z) {
            return getTextIfExists(AccessibilityContentDescriptions.WIFI_CONNECTION_STRENGTH[i]).toString();
        }
        return getTextIfExists(AccessibilityContentDescriptions.PHONE_SIGNAL_STRENGTH[i]).toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void refreshCallIndicator(NetworkController.SignalCallback signalCallback) {
        ServiceState serviceState = this.mServiceState;
        NetworkController.IconState iconState = new NetworkController.IconState(((serviceState == null || serviceState.getState() == 0) ? false : true) & (!hideNoCalling()), R$drawable.ic_qs_no_calling_sms, getTextIfExists(AccessibilityContentDescriptions.NO_CALLING).toString());
        signalCallback.setCallIndicator(iconState, this.mSubscriptionInfo.getSubscriptionId());
        int i = this.mImsType;
        if (i == 1) {
            iconState = new NetworkController.IconState(true, getCallStrengthIcon(this.mLastWwanLevel, false), getCallStrengthDescription(this.mLastWwanLevel, false));
        } else if (i == 2) {
            iconState = new NetworkController.IconState(true, getCallStrengthIcon(this.mLastWlanLevel, true), getCallStrengthDescription(this.mLastWlanLevel, true));
        } else if (i == 3) {
            iconState = new NetworkController.IconState(true, getCallStrengthIcon(this.mLastWlanCrossSimLevel, false), getCallStrengthDescription(this.mLastWlanCrossSimLevel, false));
        }
        signalCallback.setCallIndicator(iconState, this.mSubscriptionInfo.getSubscriptionId());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyWifiLevelChange(int i) {
        if (!this.mProviderModelBehavior) {
            return;
        }
        this.mLastWlanLevel = i;
        if (this.mImsType != 2) {
            return;
        }
        notifyCallStateChange(new NetworkController.IconState(true, getCallStrengthIcon(i, true), getCallStrengthDescription(i, true)), this.mSubscriptionInfo.getSubscriptionId());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyDefaultMobileLevelChange(int i) {
        if (!this.mProviderModelBehavior) {
            return;
        }
        this.mLastWlanCrossSimLevel = i;
        if (this.mImsType != 3) {
            return;
        }
        notifyCallStateChange(new NetworkController.IconState(true, getCallStrengthIcon(i, false), getCallStrengthDescription(i, false)), this.mSubscriptionInfo.getSubscriptionId());
    }

    void notifyMobileLevelChangeIfNecessary(SignalStrength signalStrength) {
        int signalLevel;
        if (this.mProviderModelBehavior && (signalLevel = getSignalLevel(signalStrength)) != this.mLastLevel) {
            this.mLastLevel = signalLevel;
            this.mLastWwanLevel = signalLevel;
            if (this.mImsType == 1) {
                notifyCallStateChange(new NetworkController.IconState(true, getCallStrengthIcon(signalLevel, false), getCallStrengthDescription(signalLevel, false)), this.mSubscriptionInfo.getSubscriptionId());
            }
            if (!((SignalIcon$MobileState) this.mCurrentState).dataSim) {
                return;
            }
            this.mNetworkController.notifyDefaultMobileLevelChange(signalLevel);
        }
    }

    int getSignalLevel(SignalStrength signalStrength) {
        if (signalStrength == null) {
            return 0;
        }
        if (!signalStrength.isGsm() && this.mConfig.alwaysShowCdmaRssi) {
            return getCdmaLevel(signalStrength);
        }
        return signalStrength.getSmoothSignalLevel();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateTelephony() {
        String iconKey;
        ServiceState serviceState;
        ServiceState serviceState2;
        int voiceNetworkType;
        if (Log.isLoggable(this.mTag, 3)) {
            Log.d(this.mTag, "updateTelephonySignalStrength: hasService=" + Utils.isInService(this.mServiceState) + " ss=" + this.mSignalStrength + " displayInfo=" + this.mTelephonyDisplayInfo);
        }
        checkDefaultData();
        boolean z = true;
        ((SignalIcon$MobileState) this.mCurrentState).connected = Utils.isInService(this.mServiceState) && this.mSignalStrength != null;
        T t = this.mCurrentState;
        if (((SignalIcon$MobileState) t).connected) {
            ((SignalIcon$MobileState) t).level = getSignalLevel(this.mSignalStrength);
            if (this.mConfig.showRsrpSignalLevelforLTE) {
                if (SignalController.DEBUG) {
                    Log.d(this.mTag, "updateTelephony CS:" + this.mServiceState.getVoiceNetworkType() + "/" + TelephonyManager.getNetworkTypeName(this.mServiceState.getVoiceNetworkType()) + ", PS:" + this.mServiceState.getDataNetworkType() + "/" + TelephonyManager.getNetworkTypeName(this.mServiceState.getDataNetworkType()));
                }
                int dataNetworkType = this.mServiceState.getDataNetworkType();
                if (dataNetworkType == 13 || dataNetworkType == 19) {
                    ((SignalIcon$MobileState) this.mCurrentState).level = getAlternateLteLevel(this.mSignalStrength);
                } else if (dataNetworkType == 0 && ((voiceNetworkType = this.mServiceState.getVoiceNetworkType()) == 13 || voiceNetworkType == 19)) {
                    ((SignalIcon$MobileState) this.mCurrentState).level = getAlternateLteLevel(this.mSignalStrength);
                }
            }
        }
        String iconKey2 = MobileMappings.getIconKey(this.mTelephonyDisplayInfo);
        if (this.mNetworkToIconLookup.get(iconKey2) != null) {
            ((SignalIcon$MobileState) this.mCurrentState).iconGroup = this.mNetworkToIconLookup.get(iconKey2);
        } else {
            ((SignalIcon$MobileState) this.mCurrentState).iconGroup = this.mDefaultIcons;
        }
        ServiceState serviceState3 = this.mServiceState;
        if (serviceState3 != null && (3 == serviceState3.getNrState() || 2 == this.mServiceState.getNrState())) {
            ((SignalIcon$MobileState) this.mCurrentState).iconGroup = TelephonyIcons.FIVE_G_BASIC;
            if (SignalController.DEBUG) {
                Log.d(this.mTag, "updateTelephony:  " + this.mServiceState.getNrState());
            }
        } else {
            ((SignalIcon$MobileState) this.mCurrentState).iconGroup = getNetworkTypeIconGroup();
        }
        T t2 = this.mCurrentState;
        SignalIcon$MobileState signalIcon$MobileState = (SignalIcon$MobileState) t2;
        if (!((SignalIcon$MobileState) t2).connected || (this.mDataState != 2 && this.mMMSDataState != PhoneConstants.DataState.CONNECTED)) {
            z = false;
        }
        signalIcon$MobileState.dataConnected = z;
        ((SignalIcon$MobileState) t2).roaming = isRoaming();
        if (isCarrierNetworkChangeActive()) {
            ((SignalIcon$MobileState) this.mCurrentState).iconGroup = TelephonyIcons.CARRIER_NETWORK_CHANGE;
        } else if (isDataDisabled() && !this.mConfig.alwaysShowDataRatIcon) {
            if (this.mSubscriptionInfo.getSubscriptionId() != this.mDefaults.getDefaultDataSubId()) {
                ((SignalIcon$MobileState) this.mCurrentState).iconGroup = TelephonyIcons.NOT_DEFAULT_DATA;
            } else {
                ((SignalIcon$MobileState) this.mCurrentState).iconGroup = TelephonyIcons.DATA_DISABLED;
            }
        }
        boolean isEmergencyOnly = isEmergencyOnly();
        T t3 = this.mCurrentState;
        if (isEmergencyOnly != ((SignalIcon$MobileState) t3).isEmergency) {
            ((SignalIcon$MobileState) t3).isEmergency = isEmergencyOnly();
            this.mNetworkController.recalculateEmergency();
        }
        if (((SignalIcon$MobileState) this.mCurrentState).networkName.equals(this.mNetworkNameDefault) && (serviceState2 = this.mServiceState) != null && !TextUtils.isEmpty(serviceState2.getOperatorAlphaShort())) {
            ((SignalIcon$MobileState) this.mCurrentState).networkName = this.mServiceState.getOperatorAlphaShort();
        }
        if (((SignalIcon$MobileState) this.mCurrentState).networkNameData.equals(this.mNetworkNameDefault) && (serviceState = this.mServiceState) != null && ((SignalIcon$MobileState) this.mCurrentState).dataSim && !TextUtils.isEmpty(serviceState.getOperatorAlphaShort())) {
            ((SignalIcon$MobileState) this.mCurrentState).networkNameData = this.mServiceState.getOperatorAlphaShort();
        }
        if (this.mConfig.alwaysShowNetworkTypeIcon) {
            if (this.mFiveGState.isNrIconTypeValid()) {
                ((SignalIcon$MobileState) this.mCurrentState).iconGroup = this.mFiveGState.getIconGroup();
            } else {
                if (((SignalIcon$MobileState) this.mCurrentState).connected) {
                    if (isDataNetworkTypeAvailable()) {
                        int overrideNetworkType = this.mTelephonyDisplayInfo.getOverrideNetworkType();
                        if (overrideNetworkType == 0 || overrideNetworkType == 4 || overrideNetworkType == 3) {
                            iconKey = MobileMappings.toIconKey(this.mTelephonyDisplayInfo.getNetworkType());
                        } else {
                            iconKey = MobileMappings.toDisplayIconKey(overrideNetworkType);
                        }
                    } else {
                        iconKey = MobileMappings.toIconKey(getVoiceNetworkType());
                    }
                } else {
                    iconKey = MobileMappings.toIconKey(0);
                }
                ((SignalIcon$MobileState) this.mCurrentState).iconGroup = this.mNetworkToIconLookup.getOrDefault(iconKey, this.mDefaultIcons);
            }
        }
        ((SignalIcon$MobileState) this.mCurrentState).mobileDataEnabled = this.mPhone.isDataEnabled();
        ((SignalIcon$MobileState) this.mCurrentState).roamingDataEnabled = this.mPhone.isDataRoamingEnabled();
        notifyListenersIfNecessary();
    }

    private void checkDefaultData() {
        T t = this.mCurrentState;
        if (((SignalIcon$MobileState) t).iconGroup != TelephonyIcons.NOT_DEFAULT_DATA) {
            ((SignalIcon$MobileState) t).defaultDataOff = false;
            return;
        }
        ((SignalIcon$MobileState) t).defaultDataOff = this.mNetworkController.isDataControllerDisabled();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onMobileDataChanged() {
        checkDefaultData();
        notifyListenersIfNecessary();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDataDisabled() {
        return !this.mPhone.isDataConnectionAllowed();
    }

    private boolean isDataNetworkTypeAvailable() {
        if (this.mTelephonyDisplayInfo.getNetworkType() == 0) {
            return false;
        }
        int dataNetworkType = getDataNetworkType();
        int voiceNetworkType = getVoiceNetworkType();
        return !(dataNetworkType == 6 || dataNetworkType == 12 || dataNetworkType == 14 || dataNetworkType == 13 || dataNetworkType == 19) || !(voiceNetworkType == 16 || voiceNetworkType == 7 || voiceNetworkType == 4) || isCallIdle();
    }

    private boolean isCallIdle() {
        return this.mCallState == 0;
    }

    private int getVoiceNetworkType() {
        ServiceState serviceState = this.mServiceState;
        if (serviceState != null) {
            return serviceState.getVoiceNetworkType();
        }
        return 0;
    }

    private int getDataNetworkType() {
        ServiceState serviceState = this.mServiceState;
        if (serviceState != null) {
            return serviceState.getDataNetworkType();
        }
        return 0;
    }

    private int getAlternateLteLevel(SignalStrength signalStrength) {
        int lteDbm = signalStrength.getLteDbm();
        if (lteDbm == Integer.MAX_VALUE) {
            int level = signalStrength.getLevel();
            if (SignalController.DEBUG) {
                String str = this.mTag;
                Log.d(str, "getAlternateLteLevel lteRsrp:INVALID  signalStrengthLevel = " + level);
            }
            return level;
        }
        int i = 0;
        if (lteDbm <= -44) {
            if (lteDbm >= -97) {
                i = 4;
            } else if (lteDbm >= -105) {
                i = 3;
            } else if (lteDbm >= -113) {
                i = 2;
            } else if (lteDbm >= -120) {
                i = 1;
            }
        }
        if (SignalController.DEBUG) {
            String str2 = this.mTag;
            Log.d(str2, "getAlternateLteLevel lteRsrp:" + lteDbm + " rsrpLevel = " + i);
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void setActivity(int i) {
        T t = this.mCurrentState;
        boolean z = false;
        ((SignalIcon$MobileState) t).activityIn = i == 3 || i == 1;
        SignalIcon$MobileState signalIcon$MobileState = (SignalIcon$MobileState) t;
        if (i == 3 || i == 2) {
            z = true;
        }
        signalIcon$MobileState.activityOut = z;
        notifyListenersIfNecessary();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordLastMobileStatus(String str) {
        String[] strArr = this.mMobileStatusHistory;
        int i = this.mMobileStatusHistoryIndex;
        strArr[i] = str;
        this.mMobileStatusHistoryIndex = (i + 1) % 64;
    }

    @VisibleForTesting
    void setImsType(int i) {
        this.mImsType = i;
    }

    public void registerFiveGStateListener(FiveGServiceClient fiveGServiceClient) {
        fiveGServiceClient.registerListener(this.mSubscriptionInfo.getSimSlotIndex(), this.mFiveGStateListener);
        this.mClient = fiveGServiceClient;
    }

    public void unregisterFiveGStateListener(FiveGServiceClient fiveGServiceClient) {
        fiveGServiceClient.unregisterListener(this.mSubscriptionInfo.getSimSlotIndex());
    }

    private SignalIcon$MobileIconGroup getNetworkTypeIconGroup() {
        String iconKey;
        int overrideNetworkType = this.mTelephonyDisplayInfo.getOverrideNetworkType();
        if (overrideNetworkType == 0 || overrideNetworkType == 4 || overrideNetworkType == 3) {
            int networkType = this.mTelephonyDisplayInfo.getNetworkType();
            if (networkType == 0) {
                networkType = getVoiceNetworkType();
            }
            iconKey = MobileMappings.toIconKey(networkType);
        } else {
            iconKey = MobileMappings.toDisplayIconKey(overrideNetworkType);
        }
        return this.mNetworkToIconLookup.getOrDefault(iconKey, this.mDefaultIcons);
    }

    private boolean showDataRatIcon() {
        T t = this.mCurrentState;
        return ((SignalIcon$MobileState) t).mobileDataEnabled && (((SignalIcon$MobileState) t).roamingDataEnabled || !((SignalIcon$MobileState) t).roaming);
    }

    private int getEnhancementDataRatIcon() {
        if (!showDataRatIcon() || !((SignalIcon$MobileState) this.mCurrentState).connected) {
            return 0;
        }
        return getRatIconGroup().dataType;
    }

    private int getEnhancementDdsRatIcon() {
        T t = this.mCurrentState;
        if (!((SignalIcon$MobileState) t).dataSim || !((SignalIcon$MobileState) t).connected) {
            return 0;
        }
        return getRatIconGroup().dataType;
    }

    private SignalIcon$MobileIconGroup getRatIconGroup() {
        if (this.mFiveGState.isNrIconTypeValid()) {
            return this.mFiveGState.getIconGroup();
        }
        return getNetworkTypeIconGroup();
    }

    private boolean isVowifiAvailable() {
        return ((SignalIcon$MobileState) this.mCurrentState).imsRegistered && this.mIsVowifiEnabled;
    }

    private SignalIcon$MobileIconGroup getVowifiIconGroup() {
        if (isVowifiAvailable() && !isCallIdle()) {
            return TelephonyIcons.VOWIFI_CALLING;
        }
        if (!isVowifiAvailable()) {
            return null;
        }
        return TelephonyIcons.VOWIFI;
    }

    @Override // com.android.systemui.statusbar.policy.SignalController
    public void dump(PrintWriter printWriter) {
        super.dump(printWriter);
        printWriter.println("  mSubscription=" + this.mSubscriptionInfo + ",");
        printWriter.println("  mServiceState=" + this.mServiceState + ",");
        printWriter.println("  mSignalStrength=" + this.mSignalStrength + ",");
        printWriter.println("  mTelephonyDisplayInfo=" + this.mTelephonyDisplayInfo + ",");
        printWriter.println("  mDataState=" + this.mDataState + ",");
        printWriter.println("  mInflateSignalStrengths=" + this.mInflateSignalStrengths + ",");
        printWriter.println("  isDataDisabled=" + isDataDisabled() + ",");
        printWriter.println("  MobileStatusHistory");
        int i = 0;
        for (int i2 = 0; i2 < 64; i2++) {
            if (this.mMobileStatusHistory[i2] != null) {
                i++;
            }
        }
        int i3 = this.mMobileStatusHistoryIndex + 64;
        while (true) {
            i3--;
            if (i3 < (this.mMobileStatusHistoryIndex + 64) - i) {
                printWriter.println("  mFiveGState=" + this.mFiveGState + ",");
                return;
            }
            printWriter.println("  Previous MobileStatus(" + ((this.mMobileStatusHistoryIndex + 64) - i3) + "): " + this.mMobileStatusHistory[i3 & 63]);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class FiveGStateListener implements FiveGServiceClient.IFiveGStateListener {
        FiveGStateListener() {
        }

        @Override // com.android.systemui.statusbar.policy.FiveGServiceClient.IFiveGStateListener
        public void onStateChanged(FiveGServiceClient.FiveGServiceState fiveGServiceState) {
            if (SignalController.DEBUG) {
                String str = MobileSignalController.this.mTag;
                Log.d(str, "onStateChanged: state=" + fiveGServiceState);
            }
            MobileSignalController mobileSignalController = MobileSignalController.this;
            mobileSignalController.mFiveGState = fiveGServiceState;
            mobileSignalController.updateTelephony();
            MobileSignalController.this.notifyListeners();
        }
    }

    @Override // com.android.systemui.statusbar.policy.SignalController
    public boolean isDirty() {
        return super.isDirty() || checkExtraDirty();
    }

    @Override // com.android.systemui.statusbar.policy.SignalController
    public void saveLastState() {
        super.saveLastState();
        saveExtraLastState();
    }

    private boolean checkExtraDirty() {
        ServiceState serviceState = this.mServiceState;
        boolean z = false;
        int voiceNetworkType = serviceState == null ? 0 : serviceState.getVoiceNetworkType();
        int dataNetworkType = serviceState == null ? 0 : serviceState.getDataNetworkType();
        if (((voiceNetworkType == this.mLastServiceStateVoiceNetworkType && dataNetworkType == this.mLastServiceStateDataNetworkType) ? false : true) || this.mIsVoiceOverCellularImsEnabled != this.mLastIsVoiceOverCellularImsEnabled || this.mIsVowifiEnabled != this.mLastIsVowifiEnabled) {
            z = true;
        }
        if (z) {
            Log.d(this.mTag, "extraDirty: voiceNetworkType:" + voiceNetworkType + ", last:" + this.mLastServiceStateVoiceNetworkType + "; dataNetworkType:" + dataNetworkType + ", last:" + this.mLastServiceStateDataNetworkType + "; isVoiceOverCellularImsEnabled:" + this.mIsVoiceOverCellularImsEnabled + ", last:" + this.mLastIsVoiceOverCellularImsEnabled + "; isVowifiEnabled:" + this.mIsVowifiEnabled + ", last:" + this.mLastIsVowifiEnabled);
        }
        return z;
    }

    private void saveExtraLastState() {
        ServiceState serviceState = this.mServiceState;
        int i = 0;
        int voiceNetworkType = serviceState == null ? 0 : serviceState.getVoiceNetworkType();
        if (serviceState != null) {
            i = serviceState.getDataNetworkType();
        }
        this.mLastServiceStateVoiceNetworkType = voiceNetworkType;
        this.mLastServiceStateDataNetworkType = i;
        this.mLastIsVoiceOverCellularImsEnabled = this.mIsVoiceOverCellularImsEnabled;
        this.mLastIsVowifiEnabled = this.mIsVowifiEnabled;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshVoiceOverCellularImsEnabled(MmTelFeature.MmTelCapabilities mmTelCapabilities) {
        boolean z = true;
        boolean isImsCapabilityInCacheAvailable = isImsCapabilityInCacheAvailable(mmTelCapabilities, 1, 0);
        boolean isImsCapabilityInCacheAvailable2 = isImsCapabilityInCacheAvailable(mmTelCapabilities, 1, 3);
        if (!isImsCapabilityInCacheAvailable && !isImsCapabilityInCacheAvailable2) {
            z = false;
        }
        this.mIsVoiceOverCellularImsEnabled = z;
        Log.d(this.mTag, "refreshVoiceOverCellularImsEnabled isVoiceAndLte=" + isImsCapabilityInCacheAvailable + " isVoiceAndNr=" + isImsCapabilityInCacheAvailable2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshVowifiEnabled(MmTelFeature.MmTelCapabilities mmTelCapabilities) {
        boolean z = true;
        boolean isImsCapabilityInCacheAvailable = isImsCapabilityInCacheAvailable(mmTelCapabilities, 1, 1);
        boolean isImsCapabilityInCacheAvailable2 = isImsCapabilityInCacheAvailable(mmTelCapabilities, 1, 2);
        if (!isImsCapabilityInCacheAvailable && !isImsCapabilityInCacheAvailable2) {
            z = false;
        }
        this.mIsVowifiEnabled = z;
        String str = this.mTag;
        Log.d(str, "refreshVowifiEnabled isVoiceAndIwlan=" + isImsCapabilityInCacheAvailable + " isVoiceAndCrossSim=" + isImsCapabilityInCacheAvailable2);
    }

    private boolean isImsCapabilityInCacheAvailable(MmTelFeature.MmTelCapabilities mmTelCapabilities, int i, int i2) {
        return getImsRegistrationTech() == i2 && mmTelCapabilities.isCapable(i);
    }

    private int getImsRegistrationTech() {
        ImsManager imsManager = this.mImsManager;
        if (imsManager != null) {
            return imsManager.getRegistrationTech();
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        StringBuilder sb = new StringBuilder("log for CurrentIconId changed\n");
        sb.append("mServiceState=" + this.mServiceState);
        sb.append('\n');
        sb.append("mSignalStrength=" + this.mSignalStrength);
        sb.append('\n');
        sb.append("mFiveGState=" + this.mFiveGState);
        sb.append('\n');
        Log.d(this.mTag, sb.toString());
    }
}
