package com.android.systemui.statusbar.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.ims.ImsException;
import android.telephony.ims.ImsMmTelManager;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsRegistrationAttributes;
import android.telephony.ims.ImsStateCallback;
import android.telephony.ims.RegistrationManager;
import android.telephony.ims.feature.MmTelFeature;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import com.android.settingslib.AccessibilityContentDescriptions;
import com.android.settingslib.SignalIcon;
import com.android.settingslib.graph.SignalDrawable;
import com.android.settingslib.mobile.MobileMappings;
import com.android.settingslib.mobile.MobileStatusTracker;
import com.android.settingslib.mobile.TelephonyIcons;
import com.android.settingslib.net.SignalStrengthUtil;
import com.android.systemui.C1893R;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.statusbar.policy.FiveGServiceClient;
import com.android.systemui.util.CarrierConfigTracker;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.p024qs.QSTileHostEx;
import com.nothing.systemui.statusbar.connectivity.MobileSignalControllerEx;
import com.nothing.systemui.statusbar.connectivity.NTTelephonyIcons;
import com.nothing.systemui.util.NTLogUtil;
import java.p026io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MobileSignalController extends SignalController<MobileState, SignalIcon.MobileIconGroup> {
    private static final int IMS_TYPE_WLAN = 2;
    private static final int IMS_TYPE_WLAN_CROSS_SIM = 3;
    private static final int IMS_TYPE_WWAN = 1;
    /* access modifiers changed from: private */
    public static final SimpleDateFormat SSDF = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
    private static final int STATUS_HISTORY_SIZE = 64;
    private int mCallState = 0;
    private ImsMmTelManager.CapabilityCallback mCapabilityCallback;
    private final CarrierConfigTracker mCarrierConfigTracker;
    private FiveGServiceClient mClient;
    /* access modifiers changed from: private */
    public MobileMappings.Config mConfig;
    private SignalIcon.MobileIconGroup mDefaultIcons;
    private final MobileStatusTracker.SubscriptionDefaults mDefaults;
    FiveGServiceClient.FiveGServiceState mFiveGState;
    FiveGStateListener mFiveGStateListener;
    /* access modifiers changed from: private */
    public final ImsMmTelManager mImsMmTelManager;
    private final ImsStateCallback mImsStateCallback;
    /* access modifiers changed from: private */
    public int mImsType = 1;
    boolean mInflateSignalStrengths = false;
    private int mLastLevel;
    /* access modifiers changed from: private */
    public int mLastWlanCrossSimLevel;
    /* access modifiers changed from: private */
    public int mLastWlanLevel;
    /* access modifiers changed from: private */
    public int mLastWwanLevel;
    private final MobileStatusTracker.Callback mMobileCallback;
    private final String[] mMobileStatusHistory = new String[64];
    private int mMobileStatusHistoryIndex;
    MobileStatusTracker mMobileStatusTracker;
    MobileSignalControllerEx.MySubscriptionsChangedListener mMySubscriptionsChangedListener;
    private final String mNetworkNameDefault;
    private final String mNetworkNameSeparator;
    private Map<String, SignalIcon.MobileIconGroup> mNetworkToIconLookup;
    private final ContentObserver mObserver;
    private final TelephonyManager mPhone;
    /* access modifiers changed from: private */
    public final boolean mProviderModelBehavior;
    /* access modifiers changed from: private */
    public final Handler mReceiverHandler;
    /* access modifiers changed from: private */
    public final RegistrationManager.RegistrationCallback mRegistrationCallback;
    final SubscriptionInfo mSubscriptionInfo;
    /* access modifiers changed from: private */
    public final Runnable mTryRegisterIms;
    private final BroadcastReceiver mVolteSwitchObserver;

    private int getDataNetworkType() {
        return 0;
    }

    private int getVoiceNetworkType() {
        return 0;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public MobileSignalController(Context context, MobileMappings.Config config, boolean z, TelephonyManager telephonyManager, CallbackHandler callbackHandler, NetworkControllerImpl networkControllerImpl, SubscriptionInfo subscriptionInfo, MobileStatusTracker.SubscriptionDefaults subscriptionDefaults, Looper looper, CarrierConfigTracker carrierConfigTracker, FeatureFlags featureFlags) {
        super("MobileSignalController(" + subscriptionInfo.getSubscriptionId() + NavigationBarInflaterView.KEY_CODE_END, context, 0, callbackHandler, networkControllerImpl);
        boolean z2 = z;
        Looper looper2 = looper;
        C26081 r5 = new MobileStatusTracker.Callback() {
            private String mLastStatus;

            public void onMobileStatusChanged(boolean z, MobileStatusTracker.MobileStatus mobileStatus) {
                NTLogUtil.m1682i(MobileSignalController.this.mTag, "onMobileStatusChanged= updateTelephony=" + z + " mobileStatus=" + mobileStatus.toString());
                String mobileStatus2 = mobileStatus.toString();
                if (!mobileStatus2.equals(this.mLastStatus)) {
                    this.mLastStatus = mobileStatus2;
                    MobileSignalController.this.recordLastMobileStatus(MobileSignalController.SSDF.format(Long.valueOf(System.currentTimeMillis())) + NavigationBarInflaterView.BUTTON_SEPARATOR + mobileStatus2);
                }
                MobileSignalController.this.updateMobileStatus(mobileStatus);
                if (z) {
                    MobileSignalController.this.updateTelephony();
                } else {
                    MobileSignalController.this.notifyListenersIfNecessary();
                }
            }
        };
        this.mMobileCallback = r5;
        this.mRegistrationCallback = new RegistrationManager.RegistrationCallback() {
            public void onRegistered(ImsRegistrationAttributes imsRegistrationAttributes) {
                Log.d(MobileSignalController.this.mTag, "onRegistered: attributes=" + imsRegistrationAttributes);
                ((MobileState) MobileSignalController.this.mCurrentState).imsRegistered = true;
                ((MobileState) MobileSignalController.this.mCurrentState).imsRegistrationTech = imsRegistrationAttributes.getRegistrationTechnology();
                ((MobileSignalControllerEx) NTDependencyEx.get(MobileSignalControllerEx.class)).updateImsRegistrationTech(((MobileState) MobileSignalController.this.mCurrentState).imsRegistrationTech, MobileSignalController.this.mSubscriptionInfo.getSimSlotIndex());
                MobileSignalController.this.notifyListenersIfNecessary();
                if (MobileSignalController.this.mProviderModelBehavior) {
                    int transportType = imsRegistrationAttributes.getTransportType();
                    int attributeFlags = imsRegistrationAttributes.getAttributeFlags();
                    if (transportType == 1) {
                        int unused = MobileSignalController.this.mImsType = 1;
                        MobileSignalController mobileSignalController = MobileSignalController.this;
                        int access$700 = mobileSignalController.getCallStrengthIcon(mobileSignalController.mLastWwanLevel, false);
                        MobileSignalController mobileSignalController2 = MobileSignalController.this;
                        IconState iconState = new IconState(true, access$700, mobileSignalController2.getCallStrengthDescription(mobileSignalController2.mLastWwanLevel, false));
                        MobileSignalController mobileSignalController3 = MobileSignalController.this;
                        mobileSignalController3.notifyCallStateChange(iconState, mobileSignalController3.mSubscriptionInfo.getSubscriptionId());
                    } else if (transportType != 2) {
                    } else {
                        if (attributeFlags == 0) {
                            int unused2 = MobileSignalController.this.mImsType = 2;
                            MobileSignalController mobileSignalController4 = MobileSignalController.this;
                            int access$7002 = mobileSignalController4.getCallStrengthIcon(mobileSignalController4.mLastWlanLevel, true);
                            MobileSignalController mobileSignalController5 = MobileSignalController.this;
                            IconState iconState2 = new IconState(true, access$7002, mobileSignalController5.getCallStrengthDescription(mobileSignalController5.mLastWlanLevel, true));
                            MobileSignalController mobileSignalController6 = MobileSignalController.this;
                            mobileSignalController6.notifyCallStateChange(iconState2, mobileSignalController6.mSubscriptionInfo.getSubscriptionId());
                        } else if (attributeFlags == 1) {
                            int unused3 = MobileSignalController.this.mImsType = 3;
                            MobileSignalController mobileSignalController7 = MobileSignalController.this;
                            int access$7003 = mobileSignalController7.getCallStrengthIcon(mobileSignalController7.mLastWlanCrossSimLevel, false);
                            MobileSignalController mobileSignalController8 = MobileSignalController.this;
                            IconState iconState3 = new IconState(true, access$7003, mobileSignalController8.getCallStrengthDescription(mobileSignalController8.mLastWlanCrossSimLevel, false));
                            MobileSignalController mobileSignalController9 = MobileSignalController.this;
                            mobileSignalController9.notifyCallStateChange(iconState3, mobileSignalController9.mSubscriptionInfo.getSubscriptionId());
                        }
                    }
                }
            }

            public void onUnregistered(ImsReasonInfo imsReasonInfo) {
                Log.d(MobileSignalController.this.mTag, "onDeregistered: info=" + imsReasonInfo);
                ((MobileState) MobileSignalController.this.mCurrentState).imsRegistered = false;
                ((MobileState) MobileSignalController.this.mCurrentState).imsRegistrationTech = -1;
                MobileSignalController.this.notifyListenersIfNecessary();
                if (MobileSignalController.this.mProviderModelBehavior) {
                    int unused = MobileSignalController.this.mImsType = 1;
                    MobileSignalController mobileSignalController = MobileSignalController.this;
                    int access$700 = mobileSignalController.getCallStrengthIcon(mobileSignalController.mLastWwanLevel, false);
                    MobileSignalController mobileSignalController2 = MobileSignalController.this;
                    IconState iconState = new IconState(true, access$700, mobileSignalController2.getCallStrengthDescription(mobileSignalController2.mLastWwanLevel, false));
                    MobileSignalController mobileSignalController3 = MobileSignalController.this;
                    mobileSignalController3.notifyCallStateChange(iconState, mobileSignalController3.mSubscriptionInfo.getSubscriptionId());
                }
            }
        };
        this.mTryRegisterIms = new Runnable() {
            private static final int MAX_RETRY = 12;
            private int mRetryCount;

            public void run() {
                try {
                    this.mRetryCount++;
                    ImsMmTelManager access$1300 = MobileSignalController.this.mImsMmTelManager;
                    Handler access$1100 = MobileSignalController.this.mReceiverHandler;
                    Objects.requireNonNull(access$1100);
                    access$1300.registerImsRegistrationCallback(new MobileSignalController$4$$ExternalSyntheticLambda0(access$1100), MobileSignalController.this.mRegistrationCallback);
                    Log.d(MobileSignalController.this.mTag, "registerImsRegistrationCallback succeeded");
                } catch (ImsException | RuntimeException e) {
                    if (this.mRetryCount < 12) {
                        Log.e(MobileSignalController.this.mTag, this.mRetryCount + " registerImsRegistrationCallback failed", e);
                        MobileSignalController.this.mReceiverHandler.postDelayed(MobileSignalController.this.mTryRegisterIms, 5000);
                    }
                }
            }
        };
        this.mCapabilityCallback = new ImsMmTelManager.CapabilityCallback() {
            public void onCapabilitiesStatusChanged(MmTelFeature.MmTelCapabilities mmTelCapabilities) {
                ((MobileState) MobileSignalController.this.mCurrentState).voiceCapable = mmTelCapabilities.isCapable(1);
                ((MobileState) MobileSignalController.this.mCurrentState).videoCapable = mmTelCapabilities.isCapable(2);
                Log.d(MobileSignalController.this.mTag, "onCapabilitiesStatusChanged isVoiceCapable=" + ((MobileState) MobileSignalController.this.mCurrentState).voiceCapable + " isVideoCapable=" + ((MobileState) MobileSignalController.this.mCurrentState).videoCapable);
                ((MobileSignalControllerEx) NTDependencyEx.get(MobileSignalControllerEx.class)).updateMmTelCapabilities(mmTelCapabilities, MobileSignalController.this.mSubscriptionInfo.getSimSlotIndex());
                MobileSignalController.this.notifyListenersIfNecessary();
            }
        };
        this.mVolteSwitchObserver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Log.d(MobileSignalController.this.mTag, "action=" + intent.getAction());
                if (MobileSignalController.this.mConfig.showVolteIcon) {
                    MobileSignalController.this.notifyListeners();
                }
            }
        };
        this.mImsStateCallback = new ImsStateCallback() {
            public void onUnavailable(int i) {
                Log.d(MobileSignalController.this.mTag, "ImsStateCallback.onUnavailable: reason=" + i);
                MobileSignalController.this.removeListeners();
            }

            public void onAvailable() {
                Log.d(MobileSignalController.this.mTag, "ImsStateCallback.onAvailable");
                if (!MobileSignalControllerEx.isSubIdChange(MobileSignalController.this.mSubscriptionInfo.getSimSlotIndex(), MobileSignalController.this.mSubscriptionInfo, MobileSignalController.this.mContext)) {
                    MobileSignalController.this.setListeners();
                    return;
                }
                MobileSignalController.this.mMySubscriptionsChangedListener = new MobileSignalControllerEx.MySubscriptionsChangedListener(MobileSignalController.this.mSubscriptionInfo.getSimSlotIndex(), MobileSignalController.this.mSubscriptionInfo, new Runnable() {
                    public void run() {
                        MobileSignalController.this.setListeners();
                        ((MobileSignalControllerEx) NTDependencyEx.get(MobileSignalControllerEx.class)).removeOnSubscriptionsChangedListener(MobileSignalController.this.mMySubscriptionsChangedListener);
                        MobileSignalController.this.mMySubscriptionsChangedListener = null;
                    }
                }, MobileSignalController.this.mContext);
                ((MobileSignalControllerEx) NTDependencyEx.get(MobileSignalControllerEx.class)).addOnSubscriptionsChangedListener(MobileSignalController.this.mMySubscriptionsChangedListener);
            }

            public void onError() {
                Log.e(MobileSignalController.this.mTag, "ImsStateCallback.onError");
                MobileSignalController.this.removeListeners();
            }
        };
        this.mCarrierConfigTracker = carrierConfigTracker;
        Context context2 = context;
        ((MobileSignalControllerEx) NTDependencyEx.get(MobileSignalControllerEx.class)).init(NavigationBarInflaterView.KEY_CODE_START + subscriptionInfo.getSubscriptionId() + NavigationBarInflaterView.KEY_CODE_END, context);
        MobileMappings.Config config2 = config;
        this.mConfig = ((MobileSignalControllerEx) NTDependencyEx.get(MobileSignalControllerEx.class)).customConfig(config);
        this.mPhone = telephonyManager;
        this.mDefaults = subscriptionDefaults;
        this.mSubscriptionInfo = subscriptionInfo;
        this.mFiveGStateListener = new FiveGStateListener();
        this.mFiveGState = new FiveGServiceClient.FiveGServiceState();
        this.mNetworkNameSeparator = getTextIfExists(C1893R.string.status_bar_network_name_separator).toString();
        String charSequence = getTextIfExists(17040605).toString();
        this.mNetworkNameDefault = charSequence;
        this.mReceiverHandler = new Handler(looper2);
        this.mNetworkToIconLookup = MobileMappings.mapIconSets(this.mConfig);
        this.mDefaultIcons = MobileMappings.getDefaultIcons(this.mConfig);
        charSequence = subscriptionInfo.getCarrierName() != null ? subscriptionInfo.getCarrierName().toString() : charSequence;
        ((MobileState) this.mCurrentState).networkName = charSequence;
        ((MobileState) this.mLastState).networkName = charSequence;
        ((MobileState) this.mCurrentState).networkNameData = charSequence;
        ((MobileState) this.mLastState).networkNameData = charSequence;
        ((MobileState) this.mCurrentState).enabled = z2;
        ((MobileState) this.mLastState).enabled = z2;
        SignalIcon.MobileIconGroup mobileIconGroup = this.mDefaultIcons;
        ((MobileState) this.mCurrentState).iconGroup = mobileIconGroup;
        ((MobileState) this.mLastState).iconGroup = mobileIconGroup;
        this.mObserver = new ContentObserver(new Handler(looper2)) {
            public void onChange(boolean z) {
                MobileSignalController.this.updateTelephony();
            }
        };
        this.mImsMmTelManager = ImsMmTelManager.createForSubscriptionId(subscriptionInfo.getSubscriptionId());
        this.mMobileStatusTracker = new MobileStatusTracker(telephonyManager, looper, subscriptionInfo, subscriptionDefaults, r5);
        this.mProviderModelBehavior = featureFlags.isEnabled(Flags.COMBINED_STATUS_BAR_SIGNAL_ICONS);
    }

    /* access modifiers changed from: package-private */
    public void setConfiguration(MobileMappings.Config config) {
        this.mConfig = ((MobileSignalControllerEx) NTDependencyEx.get(MobileSignalControllerEx.class)).customConfig(config);
        updateInflateSignalStrength();
        this.mNetworkToIconLookup = MobileMappings.mapIconSets(this.mConfig);
        this.mDefaultIcons = MobileMappings.getDefaultIcons(this.mConfig);
        updateTelephony();
    }

    /* access modifiers changed from: package-private */
    public void setAirplaneMode(boolean z) {
        ((MobileState) this.mCurrentState).airplaneMode = z;
        notifyListenersIfNecessary();
    }

    /* access modifiers changed from: package-private */
    public void setUserSetupComplete(boolean z) {
        ((MobileState) this.mCurrentState).userSetup = z;
        notifyListenersIfNecessary();
    }

    public void updateConnectivity(BitSet bitSet, BitSet bitSet2) {
        boolean z = bitSet2.get(this.mTransportType);
        ((MobileState) this.mCurrentState).isDefault = bitSet.get(this.mTransportType);
        ((MobileState) this.mCurrentState).inetCondition = (z || !((MobileState) this.mCurrentState).isDefault) ? 1 : 0;
        notifyListenersIfNecessary();
    }

    /* access modifiers changed from: package-private */
    public void setCarrierNetworkChangeMode(boolean z) {
        ((MobileState) this.mCurrentState).carrierNetworkChangeMode = z;
        updateTelephony();
    }

    public void registerListener() {
        if (this.mConfig.signalSmooth) {
            this.mMobileStatusTracker.setSignalSmooth(true);
        }
        this.mMobileStatusTracker.setListening(true);
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("mobile_data"), true, this.mObserver);
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("mobile_data" + this.mSubscriptionInfo.getSubscriptionId()), true, this.mObserver);
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("data_roaming"), true, this.mObserver);
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("data_roaming" + this.mSubscriptionInfo.getSubscriptionId()), true, this.mObserver);
        this.mContext.registerReceiver(this.mVolteSwitchObserver, new IntentFilter("org.codeaurora.intent.action.ACTION_ENHANCE_4G_SWITCH"));
        if (this.mConfig.showVolteIcon || this.mConfig.showVowifiIcon) {
            try {
                this.mImsMmTelManager.registerImsStateCallback(this.mContext.getMainExecutor(), this.mImsStateCallback);
            } catch (ImsException e) {
                Log.e(this.mTag, "failed to call registerImsStateCallback ", e);
            }
        }
        if (this.mProviderModelBehavior) {
            this.mReceiverHandler.post(this.mTryRegisterIms);
        }
    }

    public void unregisterListener() {
        if (this.mConfig.signalSmooth) {
            this.mMobileStatusTracker.setSignalSmooth(false);
        }
        this.mMobileStatusTracker.setListening(false);
        this.mContext.getContentResolver().unregisterContentObserver(this.mObserver);
        try {
            this.mImsMmTelManager.unregisterImsRegistrationCallback(this.mRegistrationCallback);
        } catch (Exception e) {
            Log.e(this.mTag, "unregisterListener: fail to call unregisterImsRegistrationCallback", e);
        }
        this.mContext.unregisterReceiver(this.mVolteSwitchObserver);
        if (this.mConfig.showVolteIcon || this.mConfig.showVowifiIcon) {
            this.mImsMmTelManager.unregisterImsStateCallback(this.mImsStateCallback);
        }
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

    public int getCurrentIconId() {
        if (((MobileState) this.mCurrentState).iconGroup == TelephonyIcons.CARRIER_NETWORK_CHANGE) {
            return SignalDrawable.getCarrierChangeState(getNumLevels());
        }
        boolean z = false;
        if (((MobileState) this.mCurrentState).connected) {
            int i = ((MobileState) this.mCurrentState).level;
            if (this.mInflateSignalStrengths) {
                i++;
            }
            boolean z2 = true;
            boolean z3 = ((MobileState) this.mCurrentState).userSetup && (((MobileState) this.mCurrentState).iconGroup == TelephonyIcons.DATA_DISABLED || (((MobileState) this.mCurrentState).iconGroup == TelephonyIcons.NOT_DEFAULT_DATA && ((MobileState) this.mCurrentState).defaultDataOff));
            boolean z4 = ((MobileState) this.mCurrentState).inetCondition == 0;
            if (z3 || !z4) {
                z2 = false;
            }
            if (!this.mConfig.hideNoInternetState) {
                z = z2;
            }
            return SignalDrawable.getState(i, getNumLevels(), z);
        } else if (((MobileState) this.mCurrentState).enabled) {
            return SignalDrawable.getEmptyState(getNumLevels());
        } else {
            return 0;
        }
    }

    public int getQsCurrentIconId() {
        return getCurrentIconId();
    }

    private int getVolteResId() {
        int voiceNetworkType = ((MobileState) this.mCurrentState).getVoiceNetworkType();
        if (((MobileState) this.mCurrentState).imsRegistered && ((MobileSignalControllerEx) NTDependencyEx.get(MobileSignalControllerEx.class)).isVoiceOverCellularImsEnabled(this.mSubscriptionInfo.getSimSlotIndex())) {
            return C1893R.C1895drawable.ic_volte;
        }
        if (((MobileState) this.mCurrentState).telephonyDisplayInfo.getNetworkType() != 13) {
            int networkType = ((MobileState) this.mCurrentState).telephonyDisplayInfo.getNetworkType();
        }
        return 0;
    }

    /* access modifiers changed from: private */
    public void setListeners() {
        try {
            Log.d(this.mTag, "setListeners: register CapabilitiesCallback and RegistrationCallback");
            this.mImsMmTelManager.registerMmTelCapabilityCallback(this.mContext.getMainExecutor(), this.mCapabilityCallback);
            this.mImsMmTelManager.registerImsRegistrationCallback(this.mContext.getMainExecutor(), this.mRegistrationCallback);
        } catch (ImsException e) {
            Log.e(this.mTag, "unable to register listeners.", e);
        }
        queryImsState();
    }

    private void queryImsState() {
        TelephonyManager createForSubscriptionId = this.mPhone.createForSubscriptionId(this.mSubscriptionInfo.getSubscriptionId());
        ((MobileState) this.mCurrentState).voiceCapable = createForSubscriptionId.isVolteAvailable();
        ((MobileState) this.mCurrentState).videoCapable = createForSubscriptionId.isVideoTelephonyAvailable();
        ((MobileState) this.mCurrentState).imsRegistered = this.mPhone.isImsRegistered(this.mSubscriptionInfo.getSubscriptionId());
        if (DEBUG) {
            Log.d(this.mTag, "queryImsState tm=" + createForSubscriptionId + " phone=" + this.mPhone + " voiceCapable=" + ((MobileState) this.mCurrentState).voiceCapable + " videoCapable=" + ((MobileState) this.mCurrentState).videoCapable + " imsResitered=" + ((MobileState) this.mCurrentState).imsRegistered);
        }
        notifyListenersIfNecessary();
    }

    /* access modifiers changed from: private */
    public void removeListeners() {
        try {
            Log.d(this.mTag, "removeListeners: unregister CapabilitiesCallback and RegistrationCallback");
            this.mImsMmTelManager.unregisterMmTelCapabilityCallback(this.mCapabilityCallback);
            this.mImsMmTelManager.unregisterImsRegistrationCallback(this.mRegistrationCallback);
        } catch (Exception e) {
            Log.e(this.mTag, "removeListeners", e);
        }
        if (this.mMySubscriptionsChangedListener != null) {
            ((MobileSignalControllerEx) NTDependencyEx.get(MobileSignalControllerEx.class)).removeOnSubscriptionsChangedListener(this.mMySubscriptionsChangedListener);
            this.mMySubscriptionsChangedListener = null;
        }
    }

    public void notifyListeners(SignalCallback signalCallback) {
        if (!this.mNetworkController.isCarrierMergedWifi(this.mSubscriptionInfo.getSubscriptionId())) {
            SignalIcon.MobileIconGroup mobileIconGroup = (SignalIcon.MobileIconGroup) getIcons();
            String charSequence = getTextIfExists(getContentDescription()).toString();
            CharSequence textIfExists = getTextIfExists(mobileIconGroup.dataContentDescription);
            String obj = Html.fromHtml(textIfExists.toString(), 0).toString();
            if (((MobileState) this.mCurrentState).inetCondition == 0) {
                obj = this.mContext.getString(C1893R.string.data_connection_no_internet);
            }
            String str = obj;
            QsInfo qsInfo = getQsInfo(charSequence, mobileIconGroup.dataType);
            SbInfo sbInfo = getSbInfo(charSequence, mobileIconGroup.dataType);
            int volteResId = this.mConfig.showVolteIcon ? getVolteResId() : 0;
            IconState iconState = sbInfo.icon;
            IconState iconState2 = qsInfo.icon;
            int i = sbInfo.ratTypeIcon;
            int i2 = qsInfo.ratTypeIcon;
            boolean hasActivityIn = ((MobileState) this.mCurrentState).hasActivityIn();
            boolean hasActivityOut = ((MobileState) this.mCurrentState).hasActivityOut();
            CharSequence charSequence2 = qsInfo.description;
            int subscriptionId = this.mSubscriptionInfo.getSubscriptionId();
            boolean z = ((MobileState) this.mCurrentState).roaming;
            boolean z2 = sbInfo.showTriangle;
            MobileSignalControllerEx mobileSignalControllerEx = (MobileSignalControllerEx) NTDependencyEx.get(MobileSignalControllerEx.class);
            CharSequence charSequence3 = charSequence2;
            MobileDataIndicators mobileDataIndicators = r4;
            MobileDataIndicators mobileDataIndicators2 = new MobileDataIndicators(iconState, iconState2, i, i2, hasActivityIn, hasActivityOut, volteResId, str, textIfExists, charSequence3, subscriptionId, z, z2, MobileSignalControllerEx.shouldDisplayDataType(this.mSubscriptionInfo));
            ((MobileSignalControllerEx) NTDependencyEx.get(MobileSignalControllerEx.class)).logCurrentState((MobileState) this.mCurrentState, mobileIconGroup, this.mConfig, mobileDataIndicators);
            signalCallback.setMobileDataIndicators(mobileDataIndicators);
            this.mNetworkController.notifyInternetTuningChanged(QSTileHostEx.getCellularSpec(), ((MobileState) this.mCurrentState).dataConnected && ((MobileState) this.mCurrentState).isDefault);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v0, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v1, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v2, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v4, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v2, resolved type: int} */
    /* JADX WARNING: type inference failed for: r4v1, types: [java.lang.CharSequence] */
    /* JADX WARNING: type inference failed for: r4v2 */
    /* JADX WARNING: type inference failed for: r4v3 */
    /* JADX WARNING: type inference failed for: r2v4, types: [java.lang.String] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.android.systemui.statusbar.connectivity.MobileSignalController.QsInfo getQsInfo(java.lang.String r5, int r6) {
        /*
            r4 = this;
            com.android.systemui.statusbar.connectivity.ConnectivityState r0 = r4.mCurrentState
            com.android.systemui.statusbar.connectivity.MobileState r0 = (com.android.systemui.statusbar.connectivity.MobileState) r0
            boolean r0 = r0.dataSim
            r1 = 0
            r2 = 0
            if (r0 == 0) goto L_0x0048
            com.android.systemui.statusbar.connectivity.ConnectivityState r0 = r4.mCurrentState
            com.android.systemui.statusbar.connectivity.MobileState r0 = (com.android.systemui.statusbar.connectivity.MobileState) r0
            boolean r0 = r0.showQuickSettingsRatIcon()
            if (r0 != 0) goto L_0x001c
            com.android.settingslib.mobile.MobileMappings$Config r0 = r4.mConfig
            boolean r0 = r0.alwaysShowDataRatIcon
            if (r0 == 0) goto L_0x001b
            goto L_0x001c
        L_0x001b:
            r6 = r1
        L_0x001c:
            com.android.systemui.statusbar.connectivity.ConnectivityState r0 = r4.mCurrentState
            com.android.systemui.statusbar.connectivity.MobileState r0 = (com.android.systemui.statusbar.connectivity.MobileState) r0
            boolean r0 = r0.enabled
            if (r0 == 0) goto L_0x002d
            com.android.systemui.statusbar.connectivity.ConnectivityState r0 = r4.mCurrentState
            com.android.systemui.statusbar.connectivity.MobileState r0 = (com.android.systemui.statusbar.connectivity.MobileState) r0
            boolean r0 = r0.isEmergency
            if (r0 != 0) goto L_0x002d
            r1 = 1
        L_0x002d:
            com.android.systemui.statusbar.connectivity.IconState r0 = new com.android.systemui.statusbar.connectivity.IconState
            int r3 = r4.getQsCurrentIconId()
            r0.<init>(r1, r3, r5)
            com.android.systemui.statusbar.connectivity.ConnectivityState r5 = r4.mCurrentState
            com.android.systemui.statusbar.connectivity.MobileState r5 = (com.android.systemui.statusbar.connectivity.MobileState) r5
            boolean r5 = r5.isEmergency
            if (r5 != 0) goto L_0x0044
            com.android.systemui.statusbar.connectivity.ConnectivityState r4 = r4.mCurrentState
            com.android.systemui.statusbar.connectivity.MobileState r4 = (com.android.systemui.statusbar.connectivity.MobileState) r4
            java.lang.String r2 = r4.networkName
        L_0x0044:
            r1 = r6
            r4 = r2
            r2 = r0
            goto L_0x0049
        L_0x0048:
            r4 = r2
        L_0x0049:
            com.android.systemui.statusbar.connectivity.MobileSignalController$QsInfo r5 = new com.android.systemui.statusbar.connectivity.MobileSignalController$QsInfo
            r5.<init>(r1, r2, r4)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.connectivity.MobileSignalController.getQsInfo(java.lang.String, int):com.android.systemui.statusbar.connectivity.MobileSignalController$QsInfo");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0060, code lost:
        if (((com.android.systemui.statusbar.connectivity.MobileState) r6.mCurrentState).airplaneMode == false) goto L_0x0062;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00b4, code lost:
        if (((com.android.systemui.statusbar.connectivity.MobileState) r6.mCurrentState).airplaneMode == false) goto L_0x0062;
     */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00bd  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00c2  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00ec  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00ef  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.android.systemui.statusbar.connectivity.MobileSignalController.SbInfo getSbInfo(java.lang.String r7, int r8) {
        /*
            r6 = this;
            com.android.systemui.statusbar.connectivity.ConnectivityState r0 = r6.mCurrentState
            com.android.systemui.statusbar.connectivity.MobileState r0 = (com.android.systemui.statusbar.connectivity.MobileState) r0
            boolean r0 = r0.isDataDisabledOrNotDefault()
            boolean r1 = r6.mProviderModelBehavior
            r2 = 1
            r3 = 0
            if (r1 == 0) goto L_0x0064
            com.android.systemui.statusbar.connectivity.ConnectivityState r1 = r6.mCurrentState
            com.android.systemui.statusbar.connectivity.MobileState r1 = (com.android.systemui.statusbar.connectivity.MobileState) r1
            boolean r1 = r1.dataConnected
            if (r1 != 0) goto L_0x0018
            if (r0 == 0) goto L_0x002a
        L_0x0018:
            com.android.systemui.statusbar.connectivity.ConnectivityState r0 = r6.mCurrentState
            com.android.systemui.statusbar.connectivity.MobileState r0 = (com.android.systemui.statusbar.connectivity.MobileState) r0
            boolean r0 = r0.dataSim
            if (r0 == 0) goto L_0x002a
            com.android.systemui.statusbar.connectivity.ConnectivityState r0 = r6.mCurrentState
            com.android.systemui.statusbar.connectivity.MobileState r0 = (com.android.systemui.statusbar.connectivity.MobileState) r0
            boolean r0 = r0.isDefault
            if (r0 == 0) goto L_0x002a
            r0 = r2
            goto L_0x002b
        L_0x002a:
            r0 = r3
        L_0x002b:
            if (r0 != 0) goto L_0x003b
            com.android.settingslib.mobile.MobileMappings$Config r1 = r6.mConfig
            boolean r1 = r1.alwaysShowDataRatIcon
            if (r1 != 0) goto L_0x003b
            com.android.settingslib.mobile.MobileMappings$Config r1 = r6.mConfig
            boolean r1 = r1.alwaysShowNetworkTypeIcon
            if (r1 == 0) goto L_0x003a
            goto L_0x003b
        L_0x003a:
            r8 = r3
        L_0x003b:
            com.android.systemui.statusbar.connectivity.ConnectivityState r1 = r6.mCurrentState
            com.android.systemui.statusbar.connectivity.MobileState r1 = (com.android.systemui.statusbar.connectivity.MobileState) r1
            boolean r1 = r1.roaming
            r0 = r0 | r1
            com.android.systemui.statusbar.connectivity.IconState r1 = new com.android.systemui.statusbar.connectivity.IconState
            if (r0 == 0) goto L_0x0050
            com.android.systemui.statusbar.connectivity.ConnectivityState r4 = r6.mCurrentState
            com.android.systemui.statusbar.connectivity.MobileState r4 = (com.android.systemui.statusbar.connectivity.MobileState) r4
            boolean r4 = r4.airplaneMode
            if (r4 != 0) goto L_0x0050
            r4 = r2
            goto L_0x0051
        L_0x0050:
            r4 = r3
        L_0x0051:
            int r5 = r6.getCurrentIconId()
            r1.<init>(r4, r5, r7)
            if (r0 == 0) goto L_0x00b7
            com.android.systemui.statusbar.connectivity.ConnectivityState r7 = r6.mCurrentState
            com.android.systemui.statusbar.connectivity.MobileState r7 = (com.android.systemui.statusbar.connectivity.MobileState) r7
            boolean r7 = r7.airplaneMode
            if (r7 != 0) goto L_0x00b7
        L_0x0062:
            r3 = r2
            goto L_0x00b7
        L_0x0064:
            com.android.systemui.statusbar.connectivity.IconState r1 = new com.android.systemui.statusbar.connectivity.IconState
            com.android.systemui.statusbar.connectivity.ConnectivityState r4 = r6.mCurrentState
            com.android.systemui.statusbar.connectivity.MobileState r4 = (com.android.systemui.statusbar.connectivity.MobileState) r4
            boolean r4 = r4.enabled
            if (r4 == 0) goto L_0x0078
            com.android.systemui.statusbar.connectivity.ConnectivityState r4 = r6.mCurrentState
            com.android.systemui.statusbar.connectivity.MobileState r4 = (com.android.systemui.statusbar.connectivity.MobileState) r4
            boolean r4 = r4.airplaneMode
            if (r4 != 0) goto L_0x0078
            r4 = r2
            goto L_0x0079
        L_0x0078:
            r4 = r3
        L_0x0079:
            int r5 = r6.getCurrentIconId()
            r1.<init>(r4, r5, r7)
            com.android.systemui.statusbar.connectivity.ConnectivityState r7 = r6.mCurrentState
            com.android.systemui.statusbar.connectivity.MobileState r7 = (com.android.systemui.statusbar.connectivity.MobileState) r7
            boolean r7 = r7.dataConnected
            if (r7 == 0) goto L_0x0090
            com.android.systemui.statusbar.connectivity.ConnectivityState r7 = r6.mCurrentState
            com.android.systemui.statusbar.connectivity.MobileState r7 = (com.android.systemui.statusbar.connectivity.MobileState) r7
            boolean r7 = r7.isDefault
            if (r7 != 0) goto L_0x009b
        L_0x0090:
            com.android.settingslib.mobile.MobileMappings$Config r7 = r6.mConfig
            boolean r7 = r7.alwaysShowNetworkTypeIcon
            if (r7 != 0) goto L_0x009b
            if (r0 == 0) goto L_0x0099
            goto L_0x009b
        L_0x0099:
            r7 = r3
            goto L_0x009c
        L_0x009b:
            r7 = r2
        L_0x009c:
            if (r7 != 0) goto L_0x00a6
            com.android.settingslib.mobile.MobileMappings$Config r7 = r6.mConfig
            boolean r7 = r7.alwaysShowDataRatIcon
            if (r7 == 0) goto L_0x00a5
            goto L_0x00a6
        L_0x00a5:
            r8 = r3
        L_0x00a6:
            com.android.systemui.statusbar.connectivity.ConnectivityState r7 = r6.mCurrentState
            com.android.systemui.statusbar.connectivity.MobileState r7 = (com.android.systemui.statusbar.connectivity.MobileState) r7
            boolean r7 = r7.enabled
            if (r7 == 0) goto L_0x00b7
            com.android.systemui.statusbar.connectivity.ConnectivityState r7 = r6.mCurrentState
            com.android.systemui.statusbar.connectivity.MobileState r7 = (com.android.systemui.statusbar.connectivity.MobileState) r7
            boolean r7 = r7.airplaneMode
            if (r7 != 0) goto L_0x00b7
            goto L_0x0062
        L_0x00b7:
            com.android.settingslib.mobile.MobileMappings$Config r7 = r6.mConfig
            boolean r7 = r7.enableRatIconEnhancement
            if (r7 == 0) goto L_0x00c2
            int r8 = r6.getEnhancementDataRatIcon()
            goto L_0x00cc
        L_0x00c2:
            com.android.settingslib.mobile.MobileMappings$Config r7 = r6.mConfig
            boolean r7 = r7.enableDdsRatIconEnhancement
            if (r7 == 0) goto L_0x00cc
            int r8 = r6.getEnhancementDdsRatIcon()
        L_0x00cc:
            com.android.settingslib.SignalIcon$MobileIconGroup r7 = r6.getVowifiIconGroup()
            com.android.settingslib.mobile.MobileMappings$Config r0 = r6.mConfig
            boolean r0 = r0.showVowifiIcon
            if (r0 == 0) goto L_0x00f6
            if (r7 == 0) goto L_0x00f6
            int r8 = r7.dataType
            com.android.systemui.statusbar.connectivity.IconState r7 = new com.android.systemui.statusbar.connectivity.IconState
            com.android.systemui.statusbar.connectivity.ConnectivityState r0 = r6.mCurrentState
            com.android.systemui.statusbar.connectivity.MobileState r0 = (com.android.systemui.statusbar.connectivity.MobileState) r0
            boolean r0 = r0.enabled
            if (r0 == 0) goto L_0x00ef
            com.android.systemui.statusbar.connectivity.ConnectivityState r6 = r6.mCurrentState
            com.android.systemui.statusbar.connectivity.MobileState r6 = (com.android.systemui.statusbar.connectivity.MobileState) r6
            boolean r6 = r6.airplaneMode
            if (r6 != 0) goto L_0x00ef
            int r6 = r1.icon
            goto L_0x00f0
        L_0x00ef:
            r6 = -1
        L_0x00f0:
            java.lang.String r0 = r1.contentDescription
            r7.<init>(r2, r6, r0)
            r1 = r7
        L_0x00f6:
            com.android.systemui.statusbar.connectivity.MobileSignalController$SbInfo r6 = new com.android.systemui.statusbar.connectivity.MobileSignalController$SbInfo
            r6.<init>(r3, r8, r1)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.connectivity.MobileSignalController.getSbInfo(java.lang.String, int):com.android.systemui.statusbar.connectivity.MobileSignalController$SbInfo");
    }

    /* access modifiers changed from: protected */
    public MobileState cleanState() {
        return new MobileState();
    }

    public boolean isInService() {
        return ((MobileState) this.mCurrentState).isInService();
    }

    /* access modifiers changed from: package-private */
    public String getNetworkNameForCarrierWiFi() {
        return this.mPhone.getSimOperatorName();
    }

    private boolean isRoaming() {
        if (isCarrierNetworkChangeActive()) {
            return false;
        }
        if (!((MobileState) this.mCurrentState).isCdma()) {
            return ((MobileState) this.mCurrentState).isRoaming();
        }
        if (this.mPhone.getCdmaEnhancedRoamingIndicatorDisplayNumber() != 1) {
            return true;
        }
        return false;
    }

    private boolean isCarrierNetworkChangeActive() {
        return ((MobileState) this.mCurrentState).carrierNetworkChangeMode;
    }

    /* access modifiers changed from: package-private */
    public void handleBroadcast(Intent intent) {
        String action = intent.getAction();
        if (action.equals("android.telephony.action.SERVICE_PROVIDERS_UPDATED")) {
            updateNetworkName(intent.getBooleanExtra("android.telephony.extra.SHOW_SPN", false), intent.getStringExtra("android.telephony.extra.SPN"), intent.getStringExtra("android.telephony.extra.DATA_SPN"), intent.getBooleanExtra("android.telephony.extra.SHOW_PLMN", false), intent.getStringExtra("android.telephony.extra.PLMN"));
            notifyListenersIfNecessary();
        } else if (action.equals("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED")) {
            updateDataSim();
            notifyListenersIfNecessary();
        }
    }

    private void updateDataSim() {
        int activeDataSubId = this.mDefaults.getActiveDataSubId();
        boolean z = true;
        if (SubscriptionManager.isValidSubscriptionId(activeDataSubId)) {
            MobileState mobileState = (MobileState) this.mCurrentState;
            if (activeDataSubId != this.mSubscriptionInfo.getSubscriptionId()) {
                z = false;
            }
            mobileState.dataSim = z;
            return;
        }
        ((MobileState) this.mCurrentState).dataSim = true;
    }

    /* access modifiers changed from: package-private */
    public void updateNetworkName(boolean z, String str, String str2, boolean z2, String str3) {
        if (CHATTY) {
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
            ((MobileState) this.mCurrentState).networkName = sb.toString();
        } else {
            ((MobileState) this.mCurrentState).networkName = this.mNetworkNameDefault;
        }
        if (z && str2 != null) {
            if (sb2.length() != 0) {
                sb2.append(this.mNetworkNameSeparator);
            }
            sb2.append(str2);
        }
        if (sb2.length() != 0) {
            ((MobileState) this.mCurrentState).networkNameData = sb2.toString();
            return;
        }
        ((MobileState) this.mCurrentState).networkNameData = this.mNetworkNameDefault;
    }

    private int getCdmaLevel(SignalStrength signalStrength) {
        List<CellSignalStrengthCdma> cellSignalStrengths = signalStrength.getCellSignalStrengths(CellSignalStrengthCdma.class);
        if (!cellSignalStrengths.isEmpty()) {
            return cellSignalStrengths.get(0).getLevel();
        }
        return 0;
    }

    /* access modifiers changed from: private */
    public void updateMobileStatus(MobileStatusTracker.MobileStatus mobileStatus) {
        int voiceServiceState = ((MobileState) this.mCurrentState).getVoiceServiceState();
        ((MobileState) this.mCurrentState).setFromMobileStatus(mobileStatus);
        notifyMobileLevelChangeIfNecessary(mobileStatus.signalStrength);
        if (this.mProviderModelBehavior) {
            maybeNotifyCallStateChanged(voiceServiceState);
        }
    }

    private void maybeNotifyCallStateChanged(int i) {
        int voiceServiceState = ((MobileState) this.mCurrentState).getVoiceServiceState();
        if (i != voiceServiceState) {
            if (i == -1 || i == 0 || voiceServiceState == 0) {
                notifyCallStateChange(new IconState(((MobileState) this.mCurrentState).isNoCalling() & (!hideNoCalling()), C1893R.C1895drawable.ic_qs_no_calling_sms, getTextIfExists(AccessibilityContentDescriptions.NO_CALLING).toString()), this.mSubscriptionInfo.getSubscriptionId());
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void updateNoCallingState() {
        notifyCallStateChange(new IconState((((MobileState) this.mCurrentState).getVoiceServiceState() != 0) & (true ^ hideNoCalling()), C1893R.C1895drawable.ic_qs_no_calling_sms, getTextIfExists(AccessibilityContentDescriptions.NO_CALLING).toString()), this.mSubscriptionInfo.getSubscriptionId());
    }

    private boolean hideNoCalling() {
        return this.mNetworkController.hasDefaultNetwork() && this.mCarrierConfigTracker.getNoCallingConfig(this.mSubscriptionInfo.getSubscriptionId());
    }

    /* access modifiers changed from: private */
    public int getCallStrengthIcon(int i, boolean z) {
        if (z) {
            return TelephonyIcons.WIFI_CALL_STRENGTH_ICONS[i];
        }
        return TelephonyIcons.MOBILE_CALL_STRENGTH_ICONS[i];
    }

    /* access modifiers changed from: private */
    public String getCallStrengthDescription(int i, boolean z) {
        if (z) {
            return getTextIfExists(AccessibilityContentDescriptions.WIFI_CONNECTION_STRENGTH[i]).toString();
        }
        return getTextIfExists(AccessibilityContentDescriptions.PHONE_SIGNAL_STRENGTH[i]).toString();
    }

    /* access modifiers changed from: package-private */
    public void refreshCallIndicator(SignalCallback signalCallback) {
        IconState iconState = new IconState(((MobileState) this.mCurrentState).isNoCalling() & (!hideNoCalling()), C1893R.C1895drawable.ic_qs_no_calling_sms, getTextIfExists(AccessibilityContentDescriptions.NO_CALLING).toString());
        signalCallback.setCallIndicator(iconState, this.mSubscriptionInfo.getSubscriptionId());
        int i = this.mImsType;
        if (i == 1) {
            iconState = new IconState(true, getCallStrengthIcon(this.mLastWwanLevel, false), getCallStrengthDescription(this.mLastWwanLevel, false));
        } else if (i == 2) {
            iconState = new IconState(true, getCallStrengthIcon(this.mLastWlanLevel, true), getCallStrengthDescription(this.mLastWlanLevel, true));
        } else if (i == 3) {
            iconState = new IconState(true, getCallStrengthIcon(this.mLastWlanCrossSimLevel, false), getCallStrengthDescription(this.mLastWlanCrossSimLevel, false));
        }
        signalCallback.setCallIndicator(iconState, this.mSubscriptionInfo.getSubscriptionId());
    }

    /* access modifiers changed from: package-private */
    public void notifyWifiLevelChange(int i) {
        if (this.mProviderModelBehavior) {
            this.mLastWlanLevel = i;
            if (this.mImsType == 2) {
                notifyCallStateChange(new IconState(true, getCallStrengthIcon(i, true), getCallStrengthDescription(i, true)), this.mSubscriptionInfo.getSubscriptionId());
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyDefaultMobileLevelChange(int i) {
        if (this.mProviderModelBehavior) {
            this.mLastWlanCrossSimLevel = i;
            if (this.mImsType == 3) {
                notifyCallStateChange(new IconState(true, getCallStrengthIcon(i, false), getCallStrengthDescription(i, false)), this.mSubscriptionInfo.getSubscriptionId());
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyMobileLevelChangeIfNecessary(SignalStrength signalStrength) {
        int signalLevel;
        if (this.mProviderModelBehavior && (signalLevel = getSignalLevel(signalStrength)) != this.mLastLevel) {
            this.mLastLevel = signalLevel;
            this.mLastWwanLevel = signalLevel;
            if (this.mImsType == 1) {
                notifyCallStateChange(new IconState(true, getCallStrengthIcon(signalLevel, false), getCallStrengthDescription(signalLevel, false)), this.mSubscriptionInfo.getSubscriptionId());
            }
            if (((MobileState) this.mCurrentState).dataSim) {
                this.mNetworkController.notifyDefaultMobileLevelChange(signalLevel);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public int getSignalLevel(SignalStrength signalStrength) {
        if (signalStrength == null) {
            return 0;
        }
        if (signalStrength.isGsm() || !this.mConfig.alwaysShowCdmaRssi) {
            return this.mMobileStatusTracker.getSmoothLevel(signalStrength);
        }
        return getCdmaLevel(signalStrength);
    }

    /* access modifiers changed from: private */
    public void updateTelephony() {
        int voiceNetworkType;
        NTLogUtil.m1680d(this.mTag, "updateTelephonySignalStrength: hasService=" + ((MobileState) this.mCurrentState).isInService() + " ss=" + ((MobileState) this.mCurrentState).signalStrength + " displayInfo=" + ((MobileState) this.mCurrentState).telephonyDisplayInfo);
        checkDefaultData();
        ((MobileState) this.mCurrentState).connected = ((MobileState) this.mCurrentState).isInService();
        if (((MobileState) this.mCurrentState).connected) {
            ((MobileState) this.mCurrentState).level = getSignalLevel(((MobileState) this.mCurrentState).signalStrength);
            if (this.mConfig.showRsrpSignalLevelforLTE) {
                if (DEBUG) {
                    Log.d(this.mTag, "updateTelephony CS:" + ((MobileState) this.mCurrentState).getVoiceNetworkType() + "/" + TelephonyManager.getNetworkTypeName(((MobileState) this.mCurrentState).getVoiceNetworkType()) + ", PS:" + ((MobileState) this.mCurrentState).getDataNetworkType() + "/" + TelephonyManager.getNetworkTypeName(((MobileState) this.mCurrentState).getDataNetworkType()));
                }
                int dataNetworkType = ((MobileState) this.mCurrentState).getDataNetworkType();
                if (dataNetworkType == 13 || dataNetworkType == 19) {
                    ((MobileState) this.mCurrentState).level = getAlternateLteLevel(((MobileState) this.mCurrentState).signalStrength);
                } else if (dataNetworkType == 0 && ((voiceNetworkType = ((MobileState) this.mCurrentState).getVoiceNetworkType()) == 13 || voiceNetworkType == 19)) {
                    ((MobileState) this.mCurrentState).level = getAlternateLteLevel(((MobileState) this.mCurrentState).signalStrength);
                }
            }
        }
        String iconKey = MobileMappings.getIconKey(((MobileState) this.mCurrentState).telephonyDisplayInfo);
        if (this.mNetworkToIconLookup.get(iconKey) != null) {
            ((MobileState) this.mCurrentState).iconGroup = this.mNetworkToIconLookup.get(iconKey);
        } else {
            ((MobileState) this.mCurrentState).iconGroup = this.mDefaultIcons;
        }
        if (this.mFiveGState.isNrIconTypeValid()) {
            ((MobileState) this.mCurrentState).iconGroup = this.mFiveGState.getIconGroup();
        } else {
            ((MobileState) this.mCurrentState).iconGroup = getNetworkTypeIconGroup();
        }
        SubscriptionInfo subscriptionInfo = this.mSubscriptionInfo;
        ((MobileState) this.mCurrentState).iconGroup = ((MobileSignalControllerEx) NTDependencyEx.get(MobileSignalControllerEx.class)).getMobileIconGroup((SignalIcon.MobileIconGroup) ((MobileState) this.mCurrentState).iconGroup, subscriptionInfo, this.mPhone, subscriptionInfo.getSubscriptionId(), ((MobileState) this.mCurrentState).serviceState, this.mFiveGState);
        ((MobileState) this.mCurrentState).dataConnected = ((MobileState) this.mCurrentState).isDataConnected();
        ((MobileState) this.mCurrentState).roaming = isRoaming();
        if (isCarrierNetworkChangeActive()) {
            ((MobileState) this.mCurrentState).iconGroup = TelephonyIcons.CARRIER_NETWORK_CHANGE;
        } else if (isDataDisabled() && !this.mConfig.alwaysShowDataRatIcon) {
            if (this.mSubscriptionInfo.getSubscriptionId() != this.mDefaults.getDefaultDataSubId()) {
                ((MobileState) this.mCurrentState).iconGroup = TelephonyIcons.NOT_DEFAULT_DATA;
            } else {
                ((MobileState) this.mCurrentState).iconGroup = TelephonyIcons.DATA_DISABLED;
            }
        }
        if (((MobileState) this.mCurrentState).isEmergencyOnly() != ((MobileState) this.mCurrentState).isEmergency) {
            ((MobileState) this.mCurrentState).isEmergency = ((MobileState) this.mCurrentState).isEmergencyOnly();
            this.mNetworkController.recalculateEmergency();
        }
        if (((MobileState) this.mCurrentState).networkName.equals(this.mNetworkNameDefault) && !TextUtils.isEmpty(((MobileState) this.mCurrentState).getOperatorAlphaShort())) {
            ((MobileState) this.mCurrentState).networkName = ((MobileState) this.mCurrentState).getOperatorAlphaShort();
        }
        if (((MobileState) this.mCurrentState).networkNameData.equals(this.mNetworkNameDefault) && ((MobileState) this.mCurrentState).dataSim && !TextUtils.isEmpty(((MobileState) this.mCurrentState).getOperatorAlphaShort())) {
            ((MobileState) this.mCurrentState).networkNameData = ((MobileState) this.mCurrentState).getOperatorAlphaShort();
        }
        if (this.mConfig.alwaysShowNetworkTypeIcon) {
            if (!((MobileState) this.mCurrentState).connected) {
                ((MobileState) this.mCurrentState).iconGroup = TelephonyIcons.UNKNOWN;
            } else if (this.mFiveGState.isNrIconTypeValid()) {
                ((MobileState) this.mCurrentState).iconGroup = this.mFiveGState.getIconGroup();
            } else {
                ((MobileState) this.mCurrentState).iconGroup = getNetworkTypeIconGroup();
            }
        }
        ((MobileState) this.mCurrentState).mobileDataEnabled = this.mPhone.isDataEnabled();
        ((MobileState) this.mCurrentState).roamingDataEnabled = this.mPhone.isDataRoamingEnabled();
        notifyListenersIfNecessary();
    }

    private void checkDefaultData() {
        if (((MobileState) this.mCurrentState).iconGroup != TelephonyIcons.NOT_DEFAULT_DATA) {
            ((MobileState) this.mCurrentState).defaultDataOff = false;
            return;
        }
        ((MobileState) this.mCurrentState).defaultDataOff = this.mNetworkController.isDataControllerDisabled();
    }

    /* access modifiers changed from: package-private */
    public void onMobileDataChanged() {
        checkDefaultData();
        notifyListenersIfNecessary();
    }

    /* access modifiers changed from: package-private */
    public boolean isDataDisabled() {
        return !this.mPhone.isDataConnectionAllowed();
    }

    private boolean isDataNetworkTypeAvailable() {
        if (((MobileState) this.mCurrentState).telephonyDisplayInfo.getNetworkType() == 0) {
            return false;
        }
        int dataNetworkType = getDataNetworkType();
        int voiceNetworkType = getVoiceNetworkType();
        if ((dataNetworkType == 6 || dataNetworkType == 12 || dataNetworkType == 14 || dataNetworkType == 13 || dataNetworkType == 19) && ((voiceNetworkType == 16 || voiceNetworkType == 7 || voiceNetworkType == 4) && !isCallIdle())) {
            return false;
        }
        return true;
    }

    private boolean isCallIdle() {
        return this.mCallState == 0;
    }

    private int getAlternateLteLevel(SignalStrength signalStrength) {
        int i = 0;
        if (signalStrength == null) {
            Log.e(this.mTag, "getAlternateLteLevel signalStrength is null");
            return 0;
        }
        int lteDbm = signalStrength.getLteDbm();
        if (lteDbm == Integer.MAX_VALUE) {
            int level = signalStrength.getLevel();
            if (DEBUG) {
                Log.d(this.mTag, "getAlternateLteLevel lteRsrp:INVALID  signalStrengthLevel = " + level);
            }
            return level;
        }
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
        if (DEBUG) {
            Log.d(this.mTag, "getAlternateLteLevel lteRsrp:" + lteDbm + " rsrpLevel = " + i);
        }
        return i;
    }

    /* access modifiers changed from: package-private */
    public void setActivity(int i) {
        boolean z = false;
        ((MobileState) this.mCurrentState).activityIn = i == 3 || i == 1;
        MobileState mobileState = (MobileState) this.mCurrentState;
        if (i == 3 || i == 2) {
            z = true;
        }
        mobileState.activityOut = z;
        notifyListenersIfNecessary();
    }

    /* access modifiers changed from: private */
    public void recordLastMobileStatus(String str) {
        String[] strArr = this.mMobileStatusHistory;
        int i = this.mMobileStatusHistoryIndex;
        strArr[i] = str;
        this.mMobileStatusHistoryIndex = (i + 1) % 64;
    }

    /* access modifiers changed from: package-private */
    public void setImsType(int i) {
        this.mImsType = i;
    }

    public void registerFiveGStateListener(FiveGServiceClient fiveGServiceClient) {
        fiveGServiceClient.registerListener(this.mSubscriptionInfo.getSimSlotIndex(), this.mFiveGStateListener);
        this.mClient = fiveGServiceClient;
    }

    public void unregisterFiveGStateListener(FiveGServiceClient fiveGServiceClient) {
        fiveGServiceClient.unregisterListener(this.mSubscriptionInfo.getSimSlotIndex());
    }

    private SignalIcon.MobileIconGroup getNetworkTypeIconGroup() {
        String str;
        int overrideNetworkType = ((MobileState) this.mCurrentState).telephonyDisplayInfo.getOverrideNetworkType();
        if (overrideNetworkType == 0 || overrideNetworkType == 4 || overrideNetworkType == 3) {
            int networkType = ((MobileState) this.mCurrentState).telephonyDisplayInfo.getNetworkType();
            if (networkType == 0) {
                networkType = ((MobileState) this.mCurrentState).getVoiceNetworkType();
            }
            str = MobileMappings.toIconKey(networkType);
        } else {
            str = MobileMappings.toDisplayIconKey(overrideNetworkType);
        }
        return this.mNetworkToIconLookup.getOrDefault(str, this.mDefaultIcons);
    }

    private boolean showDataRatIcon() {
        return ((MobileState) this.mCurrentState).mobileDataEnabled && (((MobileState) this.mCurrentState).roamingDataEnabled || !((MobileState) this.mCurrentState).roaming);
    }

    private int getEnhancementDataRatIcon() {
        if (!showDataRatIcon() || !((MobileState) this.mCurrentState).connected) {
            return 0;
        }
        return getRatIconGroup().dataType;
    }

    private int getEnhancementDdsRatIcon() {
        if (!((MobileState) this.mCurrentState).dataSim || !((MobileState) this.mCurrentState).connected) {
            return 0;
        }
        return getRatIconGroup().dataType;
    }

    private SignalIcon.MobileIconGroup getRatIconGroup() {
        if (this.mFiveGState.isNrIconTypeValid()) {
            return this.mFiveGState.getIconGroup();
        }
        return getNetworkTypeIconGroup();
    }

    private boolean isVowifiAvailable() {
        return ((MobileSignalControllerEx) NTDependencyEx.get(MobileSignalControllerEx.class)).isVowifiEnabled(this.mSubscriptionInfo.getSimSlotIndex());
    }

    private SignalIcon.MobileIconGroup getVowifiIconGroup() {
        if (isVowifiAvailable() && !isCallIdle()) {
            return NTTelephonyIcons.VOWIFI_CALLING;
        }
        if (isVowifiAvailable()) {
            return NTTelephonyIcons.VOWIFI;
        }
        return null;
    }

    public void dump(PrintWriter printWriter) {
        super.dump(printWriter);
        printWriter.println("  mSubscription=" + this.mSubscriptionInfo + NavigationBarInflaterView.BUTTON_SEPARATOR);
        printWriter.println("  mProviderModelBehavior=" + this.mProviderModelBehavior + NavigationBarInflaterView.BUTTON_SEPARATOR);
        printWriter.println("  mInflateSignalStrengths=" + this.mInflateSignalStrengths + NavigationBarInflaterView.BUTTON_SEPARATOR);
        printWriter.println("  isDataDisabled=" + isDataDisabled() + NavigationBarInflaterView.BUTTON_SEPARATOR);
        printWriter.println("  mConfig.enableRatIconEnhancement=" + this.mConfig.enableRatIconEnhancement + NavigationBarInflaterView.BUTTON_SEPARATOR);
        printWriter.println("  mConfig.enableDdsRatIconEnhancement=" + this.mConfig.enableDdsRatIconEnhancement + NavigationBarInflaterView.BUTTON_SEPARATOR);
        printWriter.println("  mConfig.alwaysShowNetworkTypeIcon=" + this.mConfig.alwaysShowNetworkTypeIcon + NavigationBarInflaterView.BUTTON_SEPARATOR);
        printWriter.println("  mConfig.showVowifiIcon=" + this.mConfig.showVowifiIcon + NavigationBarInflaterView.BUTTON_SEPARATOR);
        printWriter.println("  mConfig.showVolteIcon=" + this.mConfig.showVolteIcon + NavigationBarInflaterView.BUTTON_SEPARATOR);
        printWriter.println("  mNetworkToIconLookup=" + this.mNetworkToIconLookup + NavigationBarInflaterView.BUTTON_SEPARATOR);
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
            if (i3 >= (this.mMobileStatusHistoryIndex + 64) - i) {
                printWriter.println("  Previous MobileStatus(" + ((this.mMobileStatusHistoryIndex + 64) - i3) + "): " + this.mMobileStatusHistory[i3 & 63]);
            } else {
                printWriter.println("  mFiveGState=" + this.mFiveGState + NavigationBarInflaterView.BUTTON_SEPARATOR);
                dumpTableData(printWriter);
                return;
            }
        }
    }

    class FiveGStateListener implements FiveGServiceClient.IFiveGStateListener {
        FiveGStateListener() {
        }

        public void onStateChanged(FiveGServiceClient.FiveGServiceState fiveGServiceState) {
            if (SignalController.DEBUG) {
                Log.d(MobileSignalController.this.mTag, "onStateChanged: state=" + fiveGServiceState);
            }
            MobileSignalController.this.mFiveGState = fiveGServiceState;
            MobileSignalController.this.updateTelephony();
            MobileSignalController.this.notifyListeners();
        }
    }

    private static final class QsInfo {
        final CharSequence description;
        final IconState icon;
        final int ratTypeIcon;

        QsInfo(int i, IconState iconState, CharSequence charSequence) {
            this.ratTypeIcon = i;
            this.icon = iconState;
            this.description = charSequence;
        }
    }

    private static final class SbInfo {
        final IconState icon;
        final int ratTypeIcon;
        final boolean showTriangle;

        SbInfo(boolean z, int i, IconState iconState) {
            this.showTriangle = z;
            this.ratTypeIcon = i;
            this.icon = iconState;
        }
    }

    /* access modifiers changed from: package-private */
    public void saveLastState() {
        super.saveLastState();
        ((MobileSignalControllerEx) NTDependencyEx.get(MobileSignalControllerEx.class)).saveExtraLastState();
    }

    /* access modifiers changed from: package-private */
    public boolean isDirty() {
        return super.isDirty() || ((MobileSignalControllerEx) NTDependencyEx.get(MobileSignalControllerEx.class)).checkExtraDirty();
    }
}
