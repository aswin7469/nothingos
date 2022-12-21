package com.android.systemui.p012qs.tiles.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.UserHandle;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.settingslib.DeviceInfoUtils;
import com.android.settingslib.SignalIcon;
import com.android.settingslib.Utils;
import com.android.settingslib.graph.SignalDrawable;
import com.android.settingslib.mobile.MobileMappings;
import com.android.settingslib.mobile.TelephonyIcons;
import com.android.settingslib.net.SignalStrengthUtil;
import com.android.settingslib.wifi.WifiUtils;
import com.android.systemui.C1893R;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.connectivity.AccessPointController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.LocationController;
import com.android.systemui.toast.SystemUIToast;
import com.android.systemui.toast.ToastFactory;
import com.android.systemui.util.CarrierConfigTracker;
import com.android.systemui.util.CarrierNameCustomization;
import com.android.systemui.util.settings.GlobalSettings;
import com.android.wifitrackerlib.MergedCarrierEntry;
import com.android.wifitrackerlib.WifiEntry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;

/* renamed from: com.android.systemui.qs.tiles.dialog.InternetDialogController */
public class InternetDialogController implements AccessPointController.AccessPointCallback {
    private static final String ACTION_NETWORK_PROVIDER_SETTINGS = "android.settings.NETWORK_PROVIDER_SETTINGS";
    private static final String ACTION_WIFI_SCANNING_SETTINGS = "android.settings.WIFI_SCANNING_SETTINGS";
    /* access modifiers changed from: private */
    public static final boolean DEBUG = Log.isLoggable(TAG, 3);
    public static final Drawable EMPTY_DRAWABLE = new ColorDrawable(0);
    static final int MAX_WIFI_ENTRY_COUNT = 50;
    public static final int NO_CELL_DATA_TYPE_ICON = 0;
    static final long SHORT_DURATION_TIMEOUT = 4000;
    private static final int SUBTITLE_TEXT_ALL_CARRIER_NETWORK_UNAVAILABLE = 2131951861;
    private static final int SUBTITLE_TEXT_NON_CARRIER_NETWORK_UNAVAILABLE = 2131952879;
    private static final int SUBTITLE_TEXT_SEARCHING_FOR_NETWORKS = 2131953585;
    private static final int SUBTITLE_TEXT_TAP_A_NETWORK_TO_CONNECT = 2131953379;
    private static final int SUBTITLE_TEXT_UNLOCK_TO_VIEW_NETWORKS = 2131953466;
    private static final int SUBTITLE_TEXT_WIFI_IS_OFF = 2131953592;
    private static final String TAG = "InternetDialogController";
    static final float TOAST_PARAMS_HORIZONTAL_WEIGHT = 1.0f;
    static final float TOAST_PARAMS_VERTICAL_WEIGHT = 1.0f;
    private AccessPointController mAccessPointController;
    protected ActivityStarter mActivityStarter;
    private BroadcastDispatcher mBroadcastDispatcher;
    /* access modifiers changed from: private */
    public InternetDialogCallback mCallback;
    protected boolean mCanConfigWifi;
    private CarrierConfigTracker mCarrierConfigTracker;
    private CarrierNameCustomization mCarrierNameCustomization;
    /* access modifiers changed from: private */
    public MobileMappings.Config mConfig = null;
    protected ConnectedWifiInternetMonitor mConnectedWifiInternetMonitor;
    private IntentFilter mConnectionStateFilter;
    private final BroadcastReceiver mConnectionStateReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED".equals(action)) {
                if (InternetDialogController.DEBUG) {
                    Log.d(InternetDialogController.TAG, "ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
                }
                MobileMappings.Config unused = InternetDialogController.this.mConfig = MobileMappings.Config.readConfig(context);
                InternetDialogController.this.updateListener();
            } else if (WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION.equals(action)) {
                InternetDialogController.this.updateListener();
            }
        }
    };
    private ConnectivityManager mConnectivityManager;
    private ConnectivityManager.NetworkCallback mConnectivityManagerNetworkCallback;
    private Context mContext;
    private int mDefaultDataSubId = -1;
    private DialogLaunchAnimator mDialogLaunchAnimator;
    private Executor mExecutor;
    private GlobalSettings mGlobalSettings;
    private Handler mHandler;
    protected boolean mHasEthernet = false;
    private boolean mHasWifiEntries;
    protected InternetTelephonyCallback mInternetTelephonyCallback;
    protected KeyguardStateController mKeyguardStateController;
    private final KeyguardUpdateMonitorCallback mKeyguardUpdateCallback = new KeyguardUpdateMonitorCallback() {
        public void onRefreshCarrierInfo() {
            InternetDialogController.this.mCallback.onRefreshCarrierInfo();
        }

        public void onSimStateChanged(int i, int i2, int i3) {
            InternetDialogController.this.mCallback.onSimStateChanged();
        }
    };
    private KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private LocationController mLocationController;
    /* access modifiers changed from: private */
    public int mNonDdsCallState = 0;
    protected Map<Integer, NonDdsCallStateCallback> mNonDdsCallStateCallbacksMap;
    protected SubscriptionManager.OnSubscriptionsChangedListener mOnSubscriptionsChangedListener;
    private SignalDrawable mSignalDrawable;
    /* access modifiers changed from: private */
    public SubscriptionManager mSubscriptionManager;
    /* access modifiers changed from: private */
    public TelephonyDisplayInfo mTelephonyDisplayInfo = new TelephonyDisplayInfo(0, 0);
    private TelephonyManager mTelephonyManager;
    private ToastFactory mToastFactory;
    private UiEventLogger mUiEventLogger;
    protected WifiUtils.InternetIconInjector mWifiIconInjector;
    private WifiManager mWifiManager;
    private WifiStateWorker mWifiStateWorker;
    /* access modifiers changed from: private */
    public WindowManager mWindowManager;
    private Handler mWorkerHandler;

    /* renamed from: com.android.systemui.qs.tiles.dialog.InternetDialogController$InternetDialogCallback */
    interface InternetDialogCallback {
        void dismissDialog();

        void onAccessPointsChanged(List<WifiEntry> list, WifiEntry wifiEntry, boolean z);

        void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities);

        void onDataConnectionStateChanged(int i, int i2);

        void onDisplayInfoChanged(TelephonyDisplayInfo telephonyDisplayInfo);

        void onLost(Network network);

        void onNonDdsCallStateChanged(int i);

        void onRefreshCarrierInfo();

        void onServiceStateChanged(ServiceState serviceState);

        void onSignalStrengthsChanged(SignalStrength signalStrength);

        void onSimStateChanged();

        void onSubscriptionsChanged(int i);

        void onUserMobileDataStateChanged(boolean z);
    }

    public void onSettingsActivityTriggered(Intent intent) {
    }

    /* access modifiers changed from: protected */
    public List<SubscriptionInfo> getSubscriptionInfo() {
        return this.mKeyguardUpdateMonitor.getFilteredSubscriptionInfo(false);
    }

    @Inject
    public InternetDialogController(Context context, UiEventLogger uiEventLogger, ActivityStarter activityStarter, AccessPointController accessPointController, SubscriptionManager subscriptionManager, TelephonyManager telephonyManager, WifiManager wifiManager, ConnectivityManager connectivityManager, @Main Handler handler, @Main Executor executor, BroadcastDispatcher broadcastDispatcher, KeyguardUpdateMonitor keyguardUpdateMonitor, GlobalSettings globalSettings, KeyguardStateController keyguardStateController, WindowManager windowManager, ToastFactory toastFactory, @Background Handler handler2, CarrierConfigTracker carrierConfigTracker, LocationController locationController, DialogLaunchAnimator dialogLaunchAnimator, WifiStateWorker wifiStateWorker, CarrierNameCustomization carrierNameCustomization) {
        if (DEBUG) {
            Log.d(TAG, "Init InternetDialogController");
        }
        this.mHandler = handler;
        this.mWorkerHandler = handler2;
        this.mExecutor = executor;
        this.mContext = context;
        this.mGlobalSettings = globalSettings;
        this.mWifiManager = wifiManager;
        this.mTelephonyManager = telephonyManager;
        this.mConnectivityManager = connectivityManager;
        this.mSubscriptionManager = subscriptionManager;
        this.mCarrierConfigTracker = carrierConfigTracker;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mKeyguardStateController = keyguardStateController;
        IntentFilter intentFilter = new IntentFilter();
        this.mConnectionStateFilter = intentFilter;
        intentFilter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
        this.mConnectionStateFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        this.mUiEventLogger = uiEventLogger;
        this.mActivityStarter = activityStarter;
        this.mAccessPointController = accessPointController;
        this.mWifiIconInjector = new WifiUtils.InternetIconInjector(this.mContext);
        this.mConnectivityManagerNetworkCallback = new DataConnectivityListener();
        this.mWindowManager = windowManager;
        this.mToastFactory = toastFactory;
        this.mSignalDrawable = new SignalDrawable(this.mContext);
        this.mLocationController = locationController;
        this.mDialogLaunchAnimator = dialogLaunchAnimator;
        this.mConnectedWifiInternetMonitor = new ConnectedWifiInternetMonitor();
        this.mWifiStateWorker = wifiStateWorker;
        this.mNonDdsCallStateCallbacksMap = new HashMap();
        this.mCarrierNameCustomization = carrierNameCustomization;
    }

    /* access modifiers changed from: package-private */
    public void onStart(InternetDialogCallback internetDialogCallback, boolean z) {
        boolean z2 = DEBUG;
        if (z2) {
            Log.d(TAG, "onStart");
        }
        this.mCallback = internetDialogCallback;
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardUpdateCallback);
        this.mAccessPointController.addAccessPointCallback(this);
        this.mBroadcastDispatcher.registerReceiver(this.mConnectionStateReceiver, this.mConnectionStateFilter, this.mExecutor);
        InternetOnSubscriptionChangedListener internetOnSubscriptionChangedListener = new InternetOnSubscriptionChangedListener();
        this.mOnSubscriptionsChangedListener = internetOnSubscriptionChangedListener;
        this.mSubscriptionManager.addOnSubscriptionsChangedListener(this.mExecutor, internetOnSubscriptionChangedListener);
        this.mDefaultDataSubId = getDefaultDataSubscriptionId();
        if (z2) {
            Log.d(TAG, "Init, SubId: " + this.mDefaultDataSubId);
        }
        this.mConfig = MobileMappings.Config.readConfig(this.mContext);
        this.mTelephonyManager = this.mTelephonyManager.createForSubscriptionId(this.mDefaultDataSubId);
        InternetTelephonyCallback internetTelephonyCallback = new InternetTelephonyCallback();
        this.mInternetTelephonyCallback = internetTelephonyCallback;
        this.mTelephonyManager.registerTelephonyCallback(this.mExecutor, internetTelephonyCallback);
        for (SubscriptionInfo next : this.mSubscriptionManager.getActiveSubscriptionInfoList()) {
            if (next.getSubscriptionId() != this.mDefaultDataSubId) {
                NonDdsCallStateCallback nonDdsCallStateCallback = new NonDdsCallStateCallback();
                this.mTelephonyManager.createForSubscriptionId(next.getSubscriptionId()).registerTelephonyCallback(this.mExecutor, nonDdsCallStateCallback);
                this.mNonDdsCallStateCallbacksMap.put(Integer.valueOf(next.getSubscriptionId()), nonDdsCallStateCallback);
            }
        }
        this.mConnectivityManager.registerDefaultNetworkCallback(this.mConnectivityManagerNetworkCallback);
        this.mCanConfigWifi = z;
        scanWifiAccessPoints();
    }

    /* access modifiers changed from: package-private */
    public void onStop() {
        if (DEBUG) {
            Log.d(TAG, "onStop");
        }
        this.mBroadcastDispatcher.unregisterReceiver(this.mConnectionStateReceiver);
        this.mTelephonyManager.unregisterTelephonyCallback(this.mInternetTelephonyCallback);
        this.mSubscriptionManager.removeOnSubscriptionsChangedListener(this.mOnSubscriptionsChangedListener);
        this.mAccessPointController.removeAccessPointCallback(this);
        this.mKeyguardUpdateMonitor.removeCallback(this.mKeyguardUpdateCallback);
        this.mConnectivityManager.unregisterNetworkCallback(this.mConnectivityManagerNetworkCallback);
        this.mConnectedWifiInternetMonitor.unregisterCallback();
        for (Integer intValue : this.mNonDdsCallStateCallbacksMap.keySet()) {
            int intValue2 = intValue.intValue();
            this.mTelephonyManager.createForSubscriptionId(intValue2).unregisterTelephonyCallback(this.mNonDdsCallStateCallbacksMap.get(Integer.valueOf(intValue2)));
        }
        this.mNonDdsCallStateCallbacksMap.clear();
    }

    /* access modifiers changed from: package-private */
    public boolean isAirplaneModeEnabled() {
        return this.mGlobalSettings.getInt("airplane_mode_on", 0) != 0;
    }

    /* access modifiers changed from: package-private */
    public void setAirplaneModeDisabled() {
        this.mConnectivityManager.setAirplaneMode(false);
    }

    /* access modifiers changed from: protected */
    public int getDefaultDataSubscriptionId() {
        return SubscriptionManager.getDefaultDataSubscriptionId();
    }

    /* access modifiers changed from: protected */
    public Intent getSettingsIntent() {
        return new Intent(ACTION_NETWORK_PROVIDER_SETTINGS).addFlags(268435456);
    }

    /* access modifiers changed from: protected */
    public Intent getWifiDetailsSettingsIntent(String str) {
        if (!TextUtils.isEmpty(str)) {
            return WifiUtils.getWifiDetailsSettingsIntent(str);
        }
        if (!DEBUG) {
            return null;
        }
        Log.d(TAG, "connected entry's key is empty");
        return null;
    }

    /* access modifiers changed from: package-private */
    public CharSequence getDialogTitleText() {
        if (isAirplaneModeEnabled()) {
            return this.mContext.getText(C1893R.string.airplane_mode);
        }
        return this.mContext.getText(C1893R.string.quick_settings_internet_label);
    }

    /* access modifiers changed from: package-private */
    public CharSequence getSubtitleText(boolean z) {
        if (this.mCanConfigWifi && !isWifiEnabled()) {
            if (DEBUG) {
                Log.d(TAG, "Wi-Fi off.");
            }
            return this.mContext.getText(C1893R.string.wifi_is_off);
        } else if (isDeviceLocked()) {
            if (DEBUG) {
                Log.d(TAG, "The device is locked.");
            }
            return this.mContext.getText(C1893R.string.unlock_to_view_networks);
        } else if (this.mHasWifiEntries) {
            if (this.mCanConfigWifi) {
                return this.mContext.getText(C1893R.string.tap_a_network_to_connect);
            }
            return null;
        } else if (this.mCanConfigWifi && z) {
            return this.mContext.getText(C1893R.string.wifi_empty_list_wifi_on);
        } else {
            if (isCarrierNetworkActive()) {
                return this.mContext.getText(C1893R.string.non_carrier_network_unavailable);
            }
            boolean z2 = DEBUG;
            if (z2) {
                Log.d(TAG, "No Wi-Fi item.");
            }
            if (!hasActiveSubId() || (!isVoiceStateInService() && !isDataStateInService())) {
                if (z2) {
                    Log.d(TAG, "No carrier or service is out of service.");
                }
                return this.mContext.getText(C1893R.string.all_network_unavailable);
            } else if (this.mCanConfigWifi && !isMobileDataEnabled()) {
                if (z2) {
                    Log.d(TAG, "Mobile data off");
                }
                return this.mContext.getText(C1893R.string.non_carrier_network_unavailable);
            } else if (!activeNetworkIsCellular()) {
                if (z2) {
                    Log.d(TAG, "No carrier data.");
                }
                return this.mContext.getText(C1893R.string.all_network_unavailable);
            } else if (this.mCanConfigWifi) {
                return this.mContext.getText(C1893R.string.non_carrier_network_unavailable);
            } else {
                return null;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public Drawable getInternetWifiDrawable(WifiEntry wifiEntry) {
        Drawable icon;
        if (wifiEntry.getLevel() == -1 || (icon = this.mWifiIconInjector.getIcon(wifiEntry.shouldShowXLevelIcon(), wifiEntry.getLevel())) == null) {
            return null;
        }
        icon.setTint(this.mContext.getColor(C1893R.C1894color.connected_network_primary_color));
        return icon;
    }

    /* access modifiers changed from: package-private */
    public Drawable getSignalStrengthDrawable() {
        Drawable drawable = this.mContext.getDrawable(C1893R.C1895drawable.ic_signal_strength_zero_bar_no_internet);
        try {
            if (this.mTelephonyManager == null) {
                if (DEBUG) {
                    Log.d(TAG, "TelephonyManager is null");
                }
                return drawable;
            }
            boolean isCarrierNetworkActive = isCarrierNetworkActive();
            if (isDataStateInService() || isVoiceStateInService() || isCarrierNetworkActive) {
                AtomicReference atomicReference = new AtomicReference();
                atomicReference.set(getSignalStrengthDrawableWithLevel(isCarrierNetworkActive));
                drawable = (Drawable) atomicReference.get();
            }
            int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(this.mContext, 16843282);
            if (activeNetworkIsCellular() || isCarrierNetworkActive) {
                colorAttrDefaultColor = this.mContext.getColor(C1893R.C1894color.connected_network_primary_color);
            }
            drawable.setTint(colorAttrDefaultColor);
            return drawable;
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /* access modifiers changed from: package-private */
    public Drawable getSignalStrengthDrawableWithLevel(boolean z) {
        int i;
        SignalStrength signalStrength = this.mTelephonyManager.getSignalStrength();
        if (signalStrength == null) {
            i = 0;
        } else {
            i = signalStrength.getLevel();
        }
        int i2 = 5;
        if (z) {
            i = getCarrierNetworkLevel();
        } else if (this.mSubscriptionManager != null && shouldInflateSignalStrength(this.mDefaultDataSubId)) {
            i++;
            i2 = 6;
        }
        return getSignalStrengthIcon(this.mContext, i, i2, 0, !isMobileDataEnabled());
    }

    /* access modifiers changed from: package-private */
    public Drawable getSignalStrengthIcon(Context context, int i, int i2, int i3, boolean z) {
        Drawable drawable;
        this.mSignalDrawable.setLevel(SignalDrawable.getState(i, i2, z));
        if (i3 == 0) {
            drawable = EMPTY_DRAWABLE;
        } else {
            drawable = context.getResources().getDrawable(i3, context.getTheme());
        }
        Drawable[] drawableArr = {drawable, this.mSignalDrawable};
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(C1893R.dimen.signal_strength_icon_size);
        LayerDrawable layerDrawable = new LayerDrawable(drawableArr);
        layerDrawable.setLayerGravity(0, 51);
        layerDrawable.setLayerGravity(1, 85);
        layerDrawable.setLayerSize(1, dimensionPixelSize, dimensionPixelSize);
        layerDrawable.setTintList(Utils.getColorAttr(context, 16843282));
        return layerDrawable;
    }

    private boolean shouldInflateSignalStrength(int i) {
        return SignalStrengthUtil.shouldInflateSignalStrength(this.mContext, i);
    }

    private CharSequence getUniqueSubscriptionDisplayName(int i, Context context) {
        return getUniqueSubscriptionDisplayNames(context).getOrDefault(Integer.valueOf(i), "");
    }

    private Map<Integer, CharSequence> getUniqueSubscriptionDisplayNames(Context context) {
        InternetDialogController$$ExternalSyntheticLambda6 internetDialogController$$ExternalSyntheticLambda6 = new InternetDialogController$$ExternalSyntheticLambda6(this);
        HashSet hashSet = new HashSet();
        InternetDialogController$$ExternalSyntheticLambda9 internetDialogController$$ExternalSyntheticLambda9 = new InternetDialogController$$ExternalSyntheticLambda9(internetDialogController$$ExternalSyntheticLambda6, (Set) ((Stream) internetDialogController$$ExternalSyntheticLambda6.get()).filter(new InternetDialogController$$ExternalSyntheticLambda7(hashSet)).map(new InternetDialogController$$ExternalSyntheticLambda8()).collect(Collectors.toSet()), context);
        hashSet.clear();
        return (Map) ((Stream) internetDialogController$$ExternalSyntheticLambda9.get()).map(new InternetDialogController$$ExternalSyntheticLambda12((Set) ((Stream) internetDialogController$$ExternalSyntheticLambda9.get()).filter(new InternetDialogController$$ExternalSyntheticLambda10(hashSet)).map(new InternetDialogController$$ExternalSyntheticLambda11()).collect(Collectors.toSet()))).collect(Collectors.toMap(new InternetDialogController$$ExternalSyntheticLambda13(), new InternetDialogController$$ExternalSyntheticLambda1()));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getUniqueSubscriptionDisplayNames$2$com-android-systemui-qs-tiles-dialog-InternetDialogController */
    public /* synthetic */ Stream mo37060x30e9102e() {
        return getSubscriptionInfo().stream().filter(new InternetDialogController$$ExternalSyntheticLambda2()).map(new InternetDialogController$$ExternalSyntheticLambda3(this));
    }

    static /* synthetic */ boolean lambda$getUniqueSubscriptionDisplayNames$0(SubscriptionInfo subscriptionInfo) {
        return (subscriptionInfo == null || subscriptionInfo.getDisplayName() == null) ? false : true;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getUniqueSubscriptionDisplayNames$1$com-android-systemui-qs-tiles-dialog-InternetDialogController */
    public /* synthetic */ AnonymousClass1DisplayInfo mo37059x3d598bed(SubscriptionInfo subscriptionInfo) {
        return new Object(subscriptionInfo, subscriptionInfo.getDisplayName().toString().trim()) {
            public CharSequence originalName;
            public SubscriptionInfo subscriptionInfo;
            public CharSequence uniqueName;

            {
                this.subscriptionInfo = r2;
                this.originalName = r3;
            }
        };
    }

    static /* synthetic */ boolean lambda$getUniqueSubscriptionDisplayNames$3(Set set, AnonymousClass1DisplayInfo r1) {
        return !set.add(r1.originalName);
    }

    static /* synthetic */ AnonymousClass1DisplayInfo lambda$getUniqueSubscriptionDisplayNames$5(Set set, Context context, AnonymousClass1DisplayInfo r3) {
        if (set.contains(r3.originalName)) {
            String bidiFormattedPhoneNumber = DeviceInfoUtils.getBidiFormattedPhoneNumber(context, r3.subscriptionInfo);
            if (bidiFormattedPhoneNumber == null) {
                bidiFormattedPhoneNumber = "";
            } else if (bidiFormattedPhoneNumber.length() > 4) {
                bidiFormattedPhoneNumber = bidiFormattedPhoneNumber.substring(bidiFormattedPhoneNumber.length() - 4);
            }
            if (TextUtils.isEmpty(bidiFormattedPhoneNumber)) {
                r3.uniqueName = r3.originalName;
            } else {
                r3.uniqueName = r3.originalName + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + bidiFormattedPhoneNumber;
            }
        } else {
            r3.uniqueName = r3.originalName;
        }
        return r3;
    }

    static /* synthetic */ boolean lambda$getUniqueSubscriptionDisplayNames$7(Set set, AnonymousClass1DisplayInfo r1) {
        return !set.add(r1.uniqueName);
    }

    static /* synthetic */ AnonymousClass1DisplayInfo lambda$getUniqueSubscriptionDisplayNames$9(Set set, AnonymousClass1DisplayInfo r2) {
        if (set.contains(r2.uniqueName)) {
            r2.uniqueName = r2.originalName + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + r2.subscriptionInfo.getSubscriptionId();
        }
        return r2;
    }

    /* access modifiers changed from: package-private */
    public CharSequence getMobileNetworkTitle() {
        if (!this.mCarrierNameCustomization.isRoamingCustomizationEnabled() || !this.mCarrierNameCustomization.isRoaming(this.mDefaultDataSubId)) {
            return getUniqueSubscriptionDisplayName(this.mDefaultDataSubId, this.mContext);
        }
        return this.mCarrierNameCustomization.getRoamingCarrierName(this.mDefaultDataSubId);
    }

    /* access modifiers changed from: package-private */
    public String getMobileNetworkSummary() {
        return getMobileSummary(this.mContext, getNetworkTypeDescription(this.mContext, this.mConfig, this.mTelephonyDisplayInfo, this.mDefaultDataSubId));
    }

    private String getNetworkTypeDescription(Context context, MobileMappings.Config config, TelephonyDisplayInfo telephonyDisplayInfo, int i) {
        String iconKey = MobileMappings.getIconKey(telephonyDisplayInfo);
        if (MobileMappings.mapIconSets(config) == null || MobileMappings.mapIconSets(config).get(iconKey) == null) {
            if (DEBUG) {
                Log.d(TAG, "The description of network type is empty.");
            }
            return "";
        }
        int i2 = ((SignalIcon.MobileIconGroup) Objects.requireNonNull(MobileMappings.mapIconSets(config).get(iconKey))).dataContentDescription;
        if (isCarrierNetworkActive()) {
            i2 = TelephonyIcons.CARRIER_MERGED_WIFI.dataContentDescription;
        }
        if (i2 != 0) {
            return SubscriptionManager.getResourcesForSubId(context, i).getString(i2);
        }
        return "";
    }

    private String getMobileSummary(Context context, String str) {
        if (!isMobileDataEnabled()) {
            return context.getString(C1893R.string.mobile_data_off_summary);
        }
        if (activeNetworkIsCellular() || isCarrierNetworkActive()) {
            return context.getString(C1893R.string.preference_summary_default_combination, new Object[]{context.getString(C1893R.string.mobile_data_connection_active), str});
        } else if (!isDataStateInService()) {
            return context.getString(C1893R.string.mobile_data_no_connection);
        } else {
            return str;
        }
    }

    private void startActivity(Intent intent, View view) {
        ActivityLaunchAnimator.Controller createActivityLaunchController = this.mDialogLaunchAnimator.createActivityLaunchController(view);
        if (createActivityLaunchController == null) {
            this.mCallback.dismissDialog();
        }
        this.mActivityStarter.postStartActivityDismissingKeyguard(intent, 0, createActivityLaunchController);
    }

    /* access modifiers changed from: package-private */
    public void launchNetworkSetting(View view) {
        startActivity(getSettingsIntent(), view);
    }

    /* access modifiers changed from: package-private */
    public void launchWifiDetailsSetting(String str, View view) {
        Intent wifiDetailsSettingsIntent = getWifiDetailsSettingsIntent(str);
        if (wifiDetailsSettingsIntent != null) {
            startActivity(wifiDetailsSettingsIntent, view);
        }
    }

    /* access modifiers changed from: package-private */
    public void launchWifiScanningSetting(View view) {
        Intent intent = new Intent(ACTION_WIFI_SCANNING_SETTINGS);
        intent.addFlags(268435456);
        startActivity(intent, view);
    }

    public void setWifiEnabled(boolean z) {
        this.mWifiStateWorker.setWifiEnabled(z);
    }

    public boolean isWifiEnabled() {
        return this.mWifiStateWorker.isWifiEnabled();
    }

    /* access modifiers changed from: package-private */
    public void connectCarrierNetwork() {
        MergedCarrierEntry mergedCarrierEntry = this.mAccessPointController.getMergedCarrierEntry();
        if (mergedCarrierEntry != null && mergedCarrierEntry.canConnect()) {
            mergedCarrierEntry.connect((WifiEntry.ConnectCallback) null, false);
            makeOverlayToast(C1893R.string.wifi_wont_autoconnect_for_now);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isCarrierNetworkActive() {
        MergedCarrierEntry mergedCarrierEntry = this.mAccessPointController.getMergedCarrierEntry();
        return mergedCarrierEntry != null && mergedCarrierEntry.isDefaultNetwork();
    }

    /* access modifiers changed from: package-private */
    public int getCarrierNetworkLevel() {
        int level;
        MergedCarrierEntry mergedCarrierEntry = this.mAccessPointController.getMergedCarrierEntry();
        if (mergedCarrierEntry != null && (level = mergedCarrierEntry.getLevel()) >= 0) {
            return level;
        }
        return 0;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: setMergedCarrierWifiEnabledIfNeed */
    public void mo37061x5ec60448(int i, boolean z) {
        if (!this.mCarrierConfigTracker.getCarrierProvisionsWifiMergedNetworksBool(i)) {
            MergedCarrierEntry mergedCarrierEntry = this.mAccessPointController.getMergedCarrierEntry();
            if (mergedCarrierEntry != null) {
                mergedCarrierEntry.setEnabled(z);
            } else if (DEBUG) {
                Log.d(TAG, "MergedCarrierEntry is null, can not set the status.");
            }
        }
    }

    /* access modifiers changed from: package-private */
    public WifiManager getWifiManager() {
        return this.mWifiManager;
    }

    /* access modifiers changed from: package-private */
    public TelephonyManager getTelephonyManager() {
        return this.mTelephonyManager;
    }

    /* access modifiers changed from: package-private */
    public SubscriptionManager getSubscriptionManager() {
        return this.mSubscriptionManager;
    }

    /* access modifiers changed from: package-private */
    public boolean hasActiveSubId() {
        if (this.mSubscriptionManager == null) {
            if (DEBUG) {
                Log.d(TAG, "SubscriptionManager is null, can not check carrier.");
            }
            return false;
        } else if (isAirplaneModeEnabled() || this.mTelephonyManager == null || this.mSubscriptionManager.getActiveSubscriptionIdList().length <= 0) {
            return false;
        } else {
            return true;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isNonDdsCallStateIdle() {
        return this.mNonDdsCallState == 0;
    }

    /* access modifiers changed from: package-private */
    public boolean isMobileDataEnabled() {
        TelephonyManager telephonyManager = this.mTelephonyManager;
        return telephonyManager != null && telephonyManager.isDataEnabled();
    }

    /* access modifiers changed from: package-private */
    public void setMobileDataEnabled(Context context, int i, boolean z, boolean z2) {
        List<SubscriptionInfo> activeSubscriptionInfoList;
        TelephonyManager telephonyManager = this.mTelephonyManager;
        if (telephonyManager == null) {
            if (DEBUG) {
                Log.d(TAG, "TelephonyManager is null, can not set mobile data.");
            }
        } else if (this.mSubscriptionManager != null) {
            telephonyManager.setDataEnabled(z);
            if (z2 && (activeSubscriptionInfoList = this.mSubscriptionManager.getActiveSubscriptionInfoList()) != null) {
                for (SubscriptionInfo next : activeSubscriptionInfoList) {
                    if (next.getSubscriptionId() != i && !next.isOpportunistic()) {
                        ((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForSubscriptionId(next.getSubscriptionId()).setDataEnabled(false);
                    }
                }
            }
            this.mWorkerHandler.post(new InternetDialogController$$ExternalSyntheticLambda4(this, i, z));
        } else if (DEBUG) {
            Log.d(TAG, "SubscriptionManager is null, can not set mobile data.");
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isDataStateInService() {
        NetworkRegistrationInfo networkRegistrationInfo;
        ServiceState serviceState = this.mTelephonyManager.getServiceState();
        if (serviceState == null) {
            networkRegistrationInfo = null;
        } else {
            networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, 1);
        }
        if (networkRegistrationInfo == null) {
            return false;
        }
        return networkRegistrationInfo.isRegistered();
    }

    /* access modifiers changed from: package-private */
    public boolean isVoiceStateInService() {
        TelephonyManager telephonyManager = this.mTelephonyManager;
        if (telephonyManager == null) {
            if (DEBUG) {
                Log.d(TAG, "TelephonyManager is null, can not detect voice state.");
            }
            return false;
        }
        ServiceState serviceState = telephonyManager.getServiceState();
        if (serviceState == null || serviceState.getState() != 0) {
            return false;
        }
        return true;
    }

    public boolean isDeviceLocked() {
        return !this.mKeyguardStateController.isUnlocked();
    }

    /* access modifiers changed from: package-private */
    public boolean activeNetworkIsCellular() {
        NetworkCapabilities networkCapabilities;
        ConnectivityManager connectivityManager = this.mConnectivityManager;
        if (connectivityManager == null) {
            if (DEBUG) {
                Log.d(TAG, "ConnectivityManager is null, can not check active network.");
            }
            return false;
        }
        Network activeNetwork = connectivityManager.getActiveNetwork();
        if (activeNetwork == null || (networkCapabilities = this.mConnectivityManager.getNetworkCapabilities(activeNetwork)) == null) {
            return false;
        }
        return networkCapabilities.hasTransport(0);
    }

    /* access modifiers changed from: package-private */
    public boolean connect(WifiEntry wifiEntry) {
        if (wifiEntry == null) {
            if (DEBUG) {
                Log.d(TAG, "No Wi-Fi ap to connect.");
            }
            return false;
        }
        if (wifiEntry.getWifiConfiguration() != null) {
            if (DEBUG) {
                Log.d(TAG, "connect networkId=" + wifiEntry.getWifiConfiguration().networkId);
            }
        } else if (DEBUG) {
            Log.d(TAG, "connect to unsaved network " + wifiEntry.getTitle());
        }
        wifiEntry.connect(new WifiEntryConnectCallback(this.mActivityStarter, wifiEntry, this));
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean isWifiScanEnabled() {
        WifiManager wifiManager;
        if (this.mLocationController.isLocationEnabled() && (wifiManager = this.mWifiManager) != null && wifiManager.isScanAlwaysAvailable()) {
            return true;
        }
        return false;
    }

    /* renamed from: com.android.systemui.qs.tiles.dialog.InternetDialogController$WifiEntryConnectCallback */
    static class WifiEntryConnectCallback implements WifiEntry.ConnectCallback {
        final ActivityStarter mActivityStarter;
        final InternetDialogController mInternetDialogController;
        final WifiEntry mWifiEntry;

        WifiEntryConnectCallback(ActivityStarter activityStarter, WifiEntry wifiEntry, InternetDialogController internetDialogController) {
            this.mActivityStarter = activityStarter;
            this.mWifiEntry = wifiEntry;
            this.mInternetDialogController = internetDialogController;
        }

        public void onConnectResult(int i) {
            if (InternetDialogController.DEBUG) {
                Log.d(InternetDialogController.TAG, "onConnectResult " + i);
            }
            if (i == 1) {
                Intent wifiDialogIntent = WifiUtils.getWifiDialogIntent(this.mWifiEntry.getKey(), true);
                wifiDialogIntent.addFlags(268435456);
                this.mActivityStarter.startActivity(wifiDialogIntent, false);
            } else if (i == 2) {
                this.mInternetDialogController.makeOverlayToast(C1893R.string.wifi_failed_connect_message);
            } else if (InternetDialogController.DEBUG) {
                Log.d(InternetDialogController.TAG, "connect failure reason=" + i);
            }
        }
    }

    /* access modifiers changed from: private */
    public void scanWifiAccessPoints() {
        if (this.mCanConfigWifi) {
            this.mAccessPointController.scanForAccessPoints();
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v0, resolved type: java.util.ArrayList} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v1, resolved type: java.util.ArrayList} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v2, resolved type: java.util.ArrayList} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v3, resolved type: java.util.ArrayList} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v3, resolved type: com.android.wifitrackerlib.WifiEntry} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v4, resolved type: java.util.ArrayList} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v5, resolved type: java.util.ArrayList} */
    /* JADX WARNING: type inference failed for: r9v1, types: [com.android.wifitrackerlib.WifiEntry] */
    /* JADX WARNING: type inference failed for: r9v2 */
    /* JADX WARNING: type inference failed for: r9v3 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onAccessPointsChanged(java.util.List<com.android.wifitrackerlib.WifiEntry> r9) {
        /*
            r8 = this;
            boolean r0 = r8.mCanConfigWifi
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            r0 = 0
            if (r9 != 0) goto L_0x000a
            r1 = r0
            goto L_0x000e
        L_0x000a:
            int r1 = r9.size()
        L_0x000e:
            r2 = 50
            r3 = 1
            if (r1 <= r2) goto L_0x0015
            r4 = r3
            goto L_0x0016
        L_0x0015:
            r4 = r0
        L_0x0016:
            r5 = 0
            if (r1 <= 0) goto L_0x004e
            java.util.ArrayList r6 = new java.util.ArrayList
            r6.<init>()
            if (r4 == 0) goto L_0x0021
            r1 = r2
        L_0x0021:
            com.android.systemui.qs.tiles.dialog.InternetDialogController$ConnectedWifiInternetMonitor r2 = r8.mConnectedWifiInternetMonitor
            r2.unregisterCallback()
        L_0x0026:
            if (r0 >= r1) goto L_0x0049
            java.lang.Object r2 = r9.get(r0)
            com.android.wifitrackerlib.WifiEntry r2 = (com.android.wifitrackerlib.WifiEntry) r2
            com.android.systemui.qs.tiles.dialog.InternetDialogController$ConnectedWifiInternetMonitor r7 = r8.mConnectedWifiInternetMonitor
            r7.registerCallbackIfNeed(r2)
            if (r5 != 0) goto L_0x0043
            boolean r7 = r2.isDefaultNetwork()
            if (r7 == 0) goto L_0x0043
            boolean r7 = r2.hasInternetAccess()
            if (r7 == 0) goto L_0x0043
            r5 = r2
            goto L_0x0046
        L_0x0043:
            r6.add(r2)
        L_0x0046:
            int r0 = r0 + 1
            goto L_0x0026
        L_0x0049:
            r8.mHasWifiEntries = r3
            r9 = r5
            r5 = r6
            goto L_0x0051
        L_0x004e:
            r8.mHasWifiEntries = r0
            r9 = r5
        L_0x0051:
            com.android.systemui.qs.tiles.dialog.InternetDialogController$InternetDialogCallback r8 = r8.mCallback
            r8.onAccessPointsChanged(r5, r9, r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.p012qs.tiles.dialog.InternetDialogController.onAccessPointsChanged(java.util.List):void");
    }

    /* renamed from: com.android.systemui.qs.tiles.dialog.InternetDialogController$NonDdsCallStateCallback */
    private class NonDdsCallStateCallback extends TelephonyCallback implements TelephonyCallback.CallStateListener {
        private NonDdsCallStateCallback() {
        }

        public void onCallStateChanged(int i) {
            Log.d(InternetDialogController.TAG, "onCallStateChanged: " + i);
            int unused = InternetDialogController.this.mNonDdsCallState = i;
            InternetDialogController.this.mCallback.onNonDdsCallStateChanged(i);
        }
    }

    /* renamed from: com.android.systemui.qs.tiles.dialog.InternetDialogController$InternetTelephonyCallback */
    private class InternetTelephonyCallback extends TelephonyCallback implements TelephonyCallback.DataConnectionStateListener, TelephonyCallback.DisplayInfoListener, TelephonyCallback.ServiceStateListener, TelephonyCallback.SignalStrengthsListener, TelephonyCallback.UserMobileDataStateListener {
        private InternetTelephonyCallback() {
        }

        public void onServiceStateChanged(ServiceState serviceState) {
            InternetDialogController.this.mCallback.onServiceStateChanged(serviceState);
        }

        public void onDataConnectionStateChanged(int i, int i2) {
            InternetDialogController.this.mCallback.onDataConnectionStateChanged(i, i2);
        }

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            InternetDialogController.this.mCallback.onSignalStrengthsChanged(signalStrength);
        }

        public void onDisplayInfoChanged(TelephonyDisplayInfo telephonyDisplayInfo) {
            TelephonyDisplayInfo unused = InternetDialogController.this.mTelephonyDisplayInfo = telephonyDisplayInfo;
            InternetDialogController.this.mCallback.onDisplayInfoChanged(telephonyDisplayInfo);
        }

        public void onUserMobileDataStateChanged(boolean z) {
            InternetDialogController.this.mCallback.onUserMobileDataStateChanged(z);
        }
    }

    /* renamed from: com.android.systemui.qs.tiles.dialog.InternetDialogController$InternetOnSubscriptionChangedListener */
    private class InternetOnSubscriptionChangedListener extends SubscriptionManager.OnSubscriptionsChangedListener {
        InternetOnSubscriptionChangedListener() {
        }

        public void onSubscriptionsChanged() {
            if (InternetDialogController.this.mSubscriptionManager.getActiveSubscriptionInfoList().size() == 1) {
                Log.d(InternetDialogController.TAG, "Resetting call state of nDDS");
                int unused = InternetDialogController.this.mNonDdsCallState = 0;
            }
            InternetDialogController.this.updateListener();
        }
    }

    /* renamed from: com.android.systemui.qs.tiles.dialog.InternetDialogController$DataConnectivityListener */
    private class DataConnectivityListener extends ConnectivityManager.NetworkCallback {
        private DataConnectivityListener() {
        }

        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            InternetDialogController.this.mHasEthernet = networkCapabilities.hasTransport(3);
            if (InternetDialogController.this.mCanConfigWifi && (InternetDialogController.this.mHasEthernet || networkCapabilities.hasTransport(1))) {
                InternetDialogController.this.scanWifiAccessPoints();
            }
            InternetDialogController.this.mCallback.onCapabilitiesChanged(network, networkCapabilities);
        }

        public void onLost(Network network) {
            InternetDialogController.this.mHasEthernet = false;
            InternetDialogController.this.mCallback.onLost(network);
        }
    }

    /* renamed from: com.android.systemui.qs.tiles.dialog.InternetDialogController$ConnectedWifiInternetMonitor */
    protected class ConnectedWifiInternetMonitor implements WifiEntry.WifiEntryCallback {
        private WifiEntry mWifiEntry;

        protected ConnectedWifiInternetMonitor() {
        }

        public void registerCallbackIfNeed(WifiEntry wifiEntry) {
            if (wifiEntry == null || this.mWifiEntry != null || wifiEntry.getConnectedState() != 2) {
                return;
            }
            if (!wifiEntry.isDefaultNetwork() || !wifiEntry.hasInternetAccess()) {
                this.mWifiEntry = wifiEntry;
                wifiEntry.setListener(this);
            }
        }

        public void unregisterCallback() {
            WifiEntry wifiEntry = this.mWifiEntry;
            if (wifiEntry != null) {
                wifiEntry.setListener((WifiEntry.WifiEntryCallback) null);
                this.mWifiEntry = null;
            }
        }

        public void onUpdated() {
            WifiEntry wifiEntry = this.mWifiEntry;
            if (wifiEntry != null) {
                if (wifiEntry.getConnectedState() != 2) {
                    unregisterCallback();
                } else if (wifiEntry.isDefaultNetwork() && wifiEntry.hasInternetAccess()) {
                    unregisterCallback();
                    InternetDialogController.this.scanWifiAccessPoints();
                }
            }
        }
    }

    public boolean hasEthernet() {
        return this.mHasEthernet;
    }

    /* access modifiers changed from: private */
    public void updateListener() {
        int defaultDataSubscriptionId = getDefaultDataSubscriptionId();
        if (this.mDefaultDataSubId != getDefaultDataSubscriptionId()) {
            this.mDefaultDataSubId = defaultDataSubscriptionId;
            if (DEBUG) {
                Log.d(TAG, "DDS: defaultDataSubId:" + this.mDefaultDataSubId);
            }
            if (SubscriptionManager.isUsableSubscriptionId(this.mDefaultDataSubId)) {
                this.mTelephonyManager.unregisterTelephonyCallback(this.mInternetTelephonyCallback);
                TelephonyManager createForSubscriptionId = this.mTelephonyManager.createForSubscriptionId(this.mDefaultDataSubId);
                this.mTelephonyManager = createForSubscriptionId;
                Handler handler = this.mHandler;
                Objects.requireNonNull(handler);
                createForSubscriptionId.registerTelephonyCallback(new InternetDialogController$$ExternalSyntheticLambda0(handler), this.mInternetTelephonyCallback);
                this.mCallback.onSubscriptionsChanged(this.mDefaultDataSubId);
            }
        } else if (DEBUG) {
            Log.d(TAG, "DDS: no change");
        }
    }

    public WifiUtils.InternetIconInjector getWifiIconInjector() {
        return this.mWifiIconInjector;
    }

    /* access modifiers changed from: package-private */
    public void makeOverlayToast(int i) {
        Resources resources = this.mContext.getResources();
        final SystemUIToast createToast = this.mToastFactory.createToast(this.mContext, resources.getString(i), this.mContext.getPackageName(), UserHandle.myUserId(), resources.getConfiguration().orientation);
        if (createToast != null) {
            final View view = createToast.getView();
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.height = -2;
            layoutParams.width = -2;
            layoutParams.format = -3;
            layoutParams.type = 2017;
            layoutParams.flags = 152;
            layoutParams.y = createToast.getYOffset().intValue();
            int absoluteGravity = Gravity.getAbsoluteGravity(createToast.getGravity().intValue(), resources.getConfiguration().getLayoutDirection());
            layoutParams.gravity = absoluteGravity;
            if ((absoluteGravity & 7) == 7) {
                layoutParams.horizontalWeight = 1.0f;
            }
            if ((absoluteGravity & 112) == 112) {
                layoutParams.verticalWeight = 1.0f;
            }
            this.mWindowManager.addView(view, layoutParams);
            Animator inAnimation = createToast.getInAnimation();
            if (inAnimation != null) {
                inAnimation.start();
            }
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    Animator outAnimation = createToast.getOutAnimation();
                    if (outAnimation != null) {
                        outAnimation.start();
                        outAnimation.addListener(new AnimatorListenerAdapter() {
                            public void onAnimationEnd(Animator animator) {
                                InternetDialogController.this.mWindowManager.removeViewImmediate(view);
                            }
                        });
                    }
                }
            }, SHORT_DURATION_TIMEOUT);
        }
    }

    public void startActivityDismissingKeyguard(Intent intent, View view) {
        this.mActivityStarter.postStartActivityDismissingKeyguard(intent, 0, this.mDialogLaunchAnimator.createActivityLaunchController(view));
    }
}
