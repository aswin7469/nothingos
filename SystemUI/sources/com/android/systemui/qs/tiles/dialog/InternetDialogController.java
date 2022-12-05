package com.android.systemui.qs.tiles.dialog;

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
import androidx.mediarouter.media.MediaRoute2Provider$$ExternalSyntheticLambda0;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.settingslib.DeviceInfoUtils;
import com.android.settingslib.Utils;
import com.android.settingslib.graph.SignalDrawable;
import com.android.settingslib.mobile.MobileMappings;
import com.android.settingslib.mobile.TelephonyIcons;
import com.android.settingslib.net.SignalStrengthUtil;
import com.android.settingslib.wifi.WifiUtils;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.qs.tiles.dialog.InternetDialogController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.LocationController;
import com.android.systemui.statusbar.policy.NetworkController;
import com.android.systemui.toast.SystemUIToast;
import com.android.systemui.toast.ToastFactory;
import com.android.systemui.util.CarrierConfigTracker;
import com.android.systemui.util.settings.GlobalSettings;
import com.android.wifitrackerlib.MergedCarrierEntry;
import com.android.wifitrackerlib.WifiEntry;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/* loaded from: classes.dex */
public class InternetDialogController implements WifiEntry.DisconnectCallback, NetworkController.AccessPointController.AccessPointCallback {
    static final long SHORT_DURATION_TIMEOUT = 4000;
    static final float TOAST_PARAMS_HORIZONTAL_WEIGHT = 1.0f;
    static final float TOAST_PARAMS_VERTICAL_WEIGHT = 1.0f;
    private NetworkController.AccessPointController mAccessPointController;
    protected ActivityStarter mActivityStarter;
    private BroadcastDispatcher mBroadcastDispatcher;
    private InternetDialogCallback mCallback;
    protected boolean mCanConfigWifi;
    private CarrierConfigTracker mCarrierConfigTracker;
    private WifiEntry mConnectedEntry;
    private IntentFilter mConnectionStateFilter;
    private ConnectivityManager mConnectivityManager;
    private ConnectivityManager.NetworkCallback mConnectivityManagerNetworkCallback;
    private Context mContext;
    private Executor mExecutor;
    private GlobalSettings mGlobalSettings;
    private Handler mHandler;
    protected InternetTelephonyCallback mInternetTelephonyCallback;
    protected KeyguardStateController mKeyguardStateController;
    private KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private LocationController mLocationController;
    protected SubscriptionManager.OnSubscriptionsChangedListener mOnSubscriptionsChangedListener;
    private SignalDrawable mSignalDrawable;
    private SubscriptionManager mSubscriptionManager;
    private TelephonyManager mTelephonyManager;
    private ToastFactory mToastFactory;
    private UiEventLogger mUiEventLogger;
    private int mWifiEntriesCount;
    protected WifiUtils.InternetIconInjector mWifiIconInjector;
    private WifiManager mWifiManager;
    private WindowManager mWindowManager;
    private Handler mWorkerHandler;
    public static final Drawable EMPTY_DRAWABLE = new ColorDrawable(0);
    private static final int SUBTITLE_TEXT_WIFI_IS_OFF = R$string.wifi_is_off;
    private static final int SUBTITLE_TEXT_TAP_A_NETWORK_TO_CONNECT = R$string.tap_a_network_to_connect;
    private static final int SUBTITLE_TEXT_UNLOCK_TO_VIEW_NETWORKS = R$string.unlock_to_view_networks;
    private static final int SUBTITLE_TEXT_SEARCHING_FOR_NETWORKS = R$string.wifi_empty_list_wifi_on;
    private static final int SUBTITLE_TEXT_NON_CARRIER_NETWORK_UNAVAILABLE = R$string.non_carrier_network_unavailable;
    private static final int SUBTITLE_TEXT_ALL_CARRIER_NETWORK_UNAVAILABLE = R$string.all_network_unavailable;
    private static final boolean DEBUG = Log.isLoggable("InternetDialogController", 3);
    private TelephonyDisplayInfo mTelephonyDisplayInfo = new TelephonyDisplayInfo(0, 0);
    private MobileMappings.Config mConfig = null;
    private int mDefaultDataSubId = -1;
    protected boolean mHasEthernet = false;
    private final KeyguardUpdateMonitorCallback mKeyguardUpdateCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialogController.1
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onRefreshCarrierInfo() {
            InternetDialogController.this.mCallback.onRefreshCarrierInfo();
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onSimStateChanged(int i, int i2, int i3) {
            InternetDialogController.this.mCallback.onSimStateChanged();
        }
    };
    private final BroadcastReceiver mConnectionStateReceiver = new BroadcastReceiver() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialogController.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED".equals(action)) {
                if (InternetDialogController.DEBUG) {
                    Log.d("InternetDialogController", "ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
                }
                InternetDialogController.this.mConfig = MobileMappings.Config.readConfig(context);
                InternetDialogController.this.updateListener();
            } else if (!"android.net.wifi.supplicant.CONNECTION_CHANGE".equals(action)) {
            } else {
                InternetDialogController.this.updateListener();
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface InternetDialogCallback {
        void dismissDialog();

        void onAccessPointsChanged(List<WifiEntry> list, WifiEntry wifiEntry);

        void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities);

        void onDataConnectionStateChanged(int i, int i2);

        void onDisplayInfoChanged(TelephonyDisplayInfo telephonyDisplayInfo);

        void onLost(Network network);

        void onRefreshCarrierInfo();

        void onServiceStateChanged(ServiceState serviceState);

        void onSignalStrengthsChanged(SignalStrength signalStrength);

        void onSimStateChanged();

        void onSubscriptionsChanged(int i);

        void onUserMobileDataStateChanged(boolean z);
    }

    @Override // com.android.wifitrackerlib.WifiEntry.DisconnectCallback
    public void onDisconnectResult(int i) {
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController.AccessPointController.AccessPointCallback
    public void onSettingsActivityTriggered(Intent intent) {
    }

    protected List<SubscriptionInfo> getSubscriptionInfo() {
        return this.mKeyguardUpdateMonitor.getFilteredSubscriptionInfo(false);
    }

    public InternetDialogController(Context context, UiEventLogger uiEventLogger, ActivityStarter activityStarter, NetworkController.AccessPointController accessPointController, SubscriptionManager subscriptionManager, TelephonyManager telephonyManager, WifiManager wifiManager, ConnectivityManager connectivityManager, Handler handler, Executor executor, BroadcastDispatcher broadcastDispatcher, KeyguardUpdateMonitor keyguardUpdateMonitor, GlobalSettings globalSettings, KeyguardStateController keyguardStateController, WindowManager windowManager, ToastFactory toastFactory, Handler handler2, CarrierConfigTracker carrierConfigTracker, LocationController locationController) {
        if (DEBUG) {
            Log.d("InternetDialogController", "Init InternetDialogController");
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
        this.mConnectionStateFilter.addAction("android.net.wifi.supplicant.CONNECTION_CHANGE");
        this.mUiEventLogger = uiEventLogger;
        this.mActivityStarter = activityStarter;
        this.mAccessPointController = accessPointController;
        this.mWifiIconInjector = new WifiUtils.InternetIconInjector(this.mContext);
        this.mConnectivityManagerNetworkCallback = new DataConnectivityListener();
        this.mWindowManager = windowManager;
        this.mToastFactory = toastFactory;
        this.mSignalDrawable = new SignalDrawable(this.mContext);
        this.mLocationController = locationController;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onStart(InternetDialogCallback internetDialogCallback, boolean z) {
        boolean z2 = DEBUG;
        if (z2) {
            Log.d("InternetDialogController", "onStart");
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
            Log.d("InternetDialogController", "Init, SubId: " + this.mDefaultDataSubId);
        }
        this.mConfig = MobileMappings.Config.readConfig(this.mContext);
        this.mTelephonyManager = this.mTelephonyManager.createForSubscriptionId(this.mDefaultDataSubId);
        InternetTelephonyCallback internetTelephonyCallback = new InternetTelephonyCallback();
        this.mInternetTelephonyCallback = internetTelephonyCallback;
        this.mTelephonyManager.registerTelephonyCallback(this.mExecutor, internetTelephonyCallback);
        this.mConnectivityManager.registerDefaultNetworkCallback(this.mConnectivityManagerNetworkCallback);
        this.mCanConfigWifi = z;
        scanWifiAccessPoints();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onStop() {
        if (DEBUG) {
            Log.d("InternetDialogController", "onStop");
        }
        this.mBroadcastDispatcher.unregisterReceiver(this.mConnectionStateReceiver);
        this.mTelephonyManager.unregisterTelephonyCallback(this.mInternetTelephonyCallback);
        this.mSubscriptionManager.removeOnSubscriptionsChangedListener(this.mOnSubscriptionsChangedListener);
        this.mAccessPointController.removeAccessPointCallback(this);
        this.mKeyguardUpdateMonitor.removeCallback(this.mKeyguardUpdateCallback);
        this.mConnectivityManager.unregisterNetworkCallback(this.mConnectivityManagerNetworkCallback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAirplaneModeEnabled() {
        return this.mGlobalSettings.getInt("airplane_mode_on", 0) != 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getDefaultDataSubscriptionId() {
        return SubscriptionManager.getDefaultDataSubscriptionId();
    }

    protected Intent getSettingsIntent() {
        return new Intent("android.settings.NETWORK_PROVIDER_SETTINGS").addFlags(268435456);
    }

    protected Intent getWifiDetailsSettingsIntent(String str) {
        if (TextUtils.isEmpty(str)) {
            if (!DEBUG) {
                return null;
            }
            Log.d("InternetDialogController", "connected entry's key is empty");
            return null;
        }
        return WifiUtils.getWifiDetailsSettingsIntent(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CharSequence getDialogTitleText() {
        if (isAirplaneModeEnabled()) {
            return this.mContext.getText(R$string.airplane_mode);
        }
        return this.mContext.getText(R$string.quick_settings_internet_label);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CharSequence getSubtitleText(boolean z) {
        if (isAirplaneModeEnabled()) {
            return null;
        }
        if (this.mCanConfigWifi && !this.mWifiManager.isWifiEnabled()) {
            if (DEBUG) {
                Log.d("InternetDialogController", "Airplane mode off + Wi-Fi off.");
            }
            return this.mContext.getText(SUBTITLE_TEXT_WIFI_IS_OFF);
        } else if (isDeviceLocked()) {
            if (DEBUG) {
                Log.d("InternetDialogController", "The device is locked.");
            }
            return this.mContext.getText(SUBTITLE_TEXT_UNLOCK_TO_VIEW_NETWORKS);
        } else if (this.mConnectedEntry != null || this.mWifiEntriesCount > 0) {
            if (!this.mCanConfigWifi) {
                return null;
            }
            return this.mContext.getText(SUBTITLE_TEXT_TAP_A_NETWORK_TO_CONNECT);
        } else if (this.mCanConfigWifi && z) {
            return this.mContext.getText(SUBTITLE_TEXT_SEARCHING_FOR_NETWORKS);
        } else {
            boolean z2 = DEBUG;
            if (z2) {
                Log.d("InternetDialogController", "No Wi-Fi item.");
            }
            if (!hasCarrier() || (!isVoiceStateInService() && !isDataStateInService())) {
                if (z2) {
                    Log.d("InternetDialogController", "No carrier or service is out of service.");
                }
                return this.mContext.getText(SUBTITLE_TEXT_ALL_CARRIER_NETWORK_UNAVAILABLE);
            } else if (this.mCanConfigWifi && !isMobileDataEnabled()) {
                if (z2) {
                    Log.d("InternetDialogController", "Mobile data off");
                }
                return this.mContext.getText(SUBTITLE_TEXT_NON_CARRIER_NETWORK_UNAVAILABLE);
            } else if (!activeNetworkIsCellular()) {
                if (z2) {
                    Log.d("InternetDialogController", "No carrier data.");
                }
                return this.mContext.getText(SUBTITLE_TEXT_ALL_CARRIER_NETWORK_UNAVAILABLE);
            } else if (!this.mCanConfigWifi) {
                return null;
            } else {
                return this.mContext.getText(SUBTITLE_TEXT_NON_CARRIER_NETWORK_UNAVAILABLE);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Drawable getInternetWifiDrawable(WifiEntry wifiEntry) {
        Drawable icon;
        if (wifiEntry.getLevel() == -1 || (icon = this.mWifiIconInjector.getIcon(wifiEntry.shouldShowXLevelIcon(), wifiEntry.getLevel())) == null) {
            return null;
        }
        icon.setTint(this.mContext.getColor(R$color.connected_network_primary_color));
        return icon;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Drawable getSignalStrengthDrawable() {
        Drawable drawable = this.mContext.getDrawable(R$drawable.ic_signal_strength_zero_bar_no_internet);
        try {
        } catch (Throwable th) {
            th.printStackTrace();
        }
        if (this.mTelephonyManager == null) {
            if (DEBUG) {
                Log.d("InternetDialogController", "TelephonyManager is null");
            }
            return drawable;
        }
        if (isDataStateInService() || isVoiceStateInService()) {
            AtomicReference atomicReference = new AtomicReference();
            atomicReference.set(getSignalStrengthDrawableWithLevel());
            drawable = (Drawable) atomicReference.get();
        }
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(this.mContext, 16843282);
        if (activeNetworkIsCellular() || isCarrierNetworkActive()) {
            colorAttrDefaultColor = this.mContext.getColor(R$color.connected_network_primary_color);
        }
        drawable.setTint(colorAttrDefaultColor);
        return drawable;
    }

    Drawable getSignalStrengthDrawableWithLevel() {
        SignalStrength signalStrength = this.mTelephonyManager.getSignalStrength();
        int level = signalStrength == null ? 0 : signalStrength.getLevel();
        int i = 5;
        if (this.mSubscriptionManager != null && shouldInflateSignalStrength(this.mDefaultDataSubId)) {
            level++;
            i = 6;
        }
        return getSignalStrengthIcon(this.mContext, level, i, 0, !isMobileDataEnabled());
    }

    Drawable getSignalStrengthIcon(Context context, int i, int i2, int i3, boolean z) {
        Drawable drawable;
        this.mSignalDrawable.setLevel(SignalDrawable.getState(i, i2, z));
        if (i3 == 0) {
            drawable = EMPTY_DRAWABLE;
        } else {
            drawable = context.getResources().getDrawable(i3, context.getTheme());
        }
        Drawable[] drawableArr = {drawable, this.mSignalDrawable};
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(R$dimen.signal_strength_icon_size);
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

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.qs.tiles.dialog.InternetDialogController$1DisplayInfo  reason: invalid class name */
    /* loaded from: classes.dex */
    public class C1DisplayInfo {
        public CharSequence originalName;
        public SubscriptionInfo subscriptionInfo;
        public CharSequence uniqueName;

        C1DisplayInfo() {
        }
    }

    private Map<Integer, CharSequence> getUniqueSubscriptionDisplayNames(final Context context) {
        final Supplier supplier = new Supplier() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialogController$$ExternalSyntheticLambda12
            @Override // java.util.function.Supplier
            public final Object get() {
                Stream lambda$getUniqueSubscriptionDisplayNames$2;
                lambda$getUniqueSubscriptionDisplayNames$2 = InternetDialogController.this.lambda$getUniqueSubscriptionDisplayNames$2();
                return lambda$getUniqueSubscriptionDisplayNames$2;
            }
        };
        final HashSet hashSet = new HashSet();
        final Set set = (Set) ((Stream) supplier.get()).filter(new Predicate() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialogController$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getUniqueSubscriptionDisplayNames$3;
                lambda$getUniqueSubscriptionDisplayNames$3 = InternetDialogController.lambda$getUniqueSubscriptionDisplayNames$3(hashSet, (InternetDialogController.C1DisplayInfo) obj);
                return lambda$getUniqueSubscriptionDisplayNames$3;
            }
        }).map(InternetDialogController$$ExternalSyntheticLambda7.INSTANCE).collect(Collectors.toSet());
        Supplier supplier2 = new Supplier() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialogController$$ExternalSyntheticLambda13
            @Override // java.util.function.Supplier
            public final Object get() {
                Stream lambda$getUniqueSubscriptionDisplayNames$6;
                lambda$getUniqueSubscriptionDisplayNames$6 = InternetDialogController.lambda$getUniqueSubscriptionDisplayNames$6(supplier, set, context);
                return lambda$getUniqueSubscriptionDisplayNames$6;
            }
        };
        hashSet.clear();
        final Set set2 = (Set) ((Stream) supplier2.get()).filter(new Predicate() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialogController$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getUniqueSubscriptionDisplayNames$7;
                lambda$getUniqueSubscriptionDisplayNames$7 = InternetDialogController.lambda$getUniqueSubscriptionDisplayNames$7(hashSet, (InternetDialogController.C1DisplayInfo) obj);
                return lambda$getUniqueSubscriptionDisplayNames$7;
            }
        }).map(InternetDialogController$$ExternalSyntheticLambda5.INSTANCE).collect(Collectors.toSet());
        return (Map) ((Stream) supplier2.get()).map(new Function() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialogController$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                InternetDialogController.C1DisplayInfo lambda$getUniqueSubscriptionDisplayNames$9;
                lambda$getUniqueSubscriptionDisplayNames$9 = InternetDialogController.lambda$getUniqueSubscriptionDisplayNames$9(set2, (InternetDialogController.C1DisplayInfo) obj);
                return lambda$getUniqueSubscriptionDisplayNames$9;
            }
        }).collect(Collectors.toMap(InternetDialogController$$ExternalSyntheticLambda6.INSTANCE, InternetDialogController$$ExternalSyntheticLambda4.INSTANCE));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Stream lambda$getUniqueSubscriptionDisplayNames$2() {
        return getSubscriptionInfo().stream().filter(InternetDialogController$$ExternalSyntheticLambda10.INSTANCE).map(new Function() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialogController$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                InternetDialogController.C1DisplayInfo lambda$getUniqueSubscriptionDisplayNames$1;
                lambda$getUniqueSubscriptionDisplayNames$1 = InternetDialogController.this.lambda$getUniqueSubscriptionDisplayNames$1((SubscriptionInfo) obj);
                return lambda$getUniqueSubscriptionDisplayNames$1;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getUniqueSubscriptionDisplayNames$0(SubscriptionInfo subscriptionInfo) {
        return (subscriptionInfo == null || subscriptionInfo.getDisplayName() == null) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ C1DisplayInfo lambda$getUniqueSubscriptionDisplayNames$1(SubscriptionInfo subscriptionInfo) {
        C1DisplayInfo c1DisplayInfo = new C1DisplayInfo();
        c1DisplayInfo.subscriptionInfo = subscriptionInfo;
        c1DisplayInfo.originalName = subscriptionInfo.getDisplayName().toString().trim();
        return c1DisplayInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getUniqueSubscriptionDisplayNames$3(Set set, C1DisplayInfo c1DisplayInfo) {
        return !set.add(c1DisplayInfo.originalName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Stream lambda$getUniqueSubscriptionDisplayNames$6(Supplier supplier, final Set set, final Context context) {
        return ((Stream) supplier.get()).map(new Function() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialogController$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                InternetDialogController.C1DisplayInfo lambda$getUniqueSubscriptionDisplayNames$5;
                lambda$getUniqueSubscriptionDisplayNames$5 = InternetDialogController.lambda$getUniqueSubscriptionDisplayNames$5(set, context, (InternetDialogController.C1DisplayInfo) obj);
                return lambda$getUniqueSubscriptionDisplayNames$5;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ C1DisplayInfo lambda$getUniqueSubscriptionDisplayNames$5(Set set, Context context, C1DisplayInfo c1DisplayInfo) {
        if (set.contains(c1DisplayInfo.originalName)) {
            String bidiFormattedPhoneNumber = DeviceInfoUtils.getBidiFormattedPhoneNumber(context, c1DisplayInfo.subscriptionInfo);
            if (bidiFormattedPhoneNumber == null) {
                bidiFormattedPhoneNumber = "";
            } else if (bidiFormattedPhoneNumber.length() > 4) {
                bidiFormattedPhoneNumber = bidiFormattedPhoneNumber.substring(bidiFormattedPhoneNumber.length() - 4);
            }
            if (TextUtils.isEmpty(bidiFormattedPhoneNumber)) {
                c1DisplayInfo.uniqueName = c1DisplayInfo.originalName;
            } else {
                c1DisplayInfo.uniqueName = ((Object) c1DisplayInfo.originalName) + " " + bidiFormattedPhoneNumber;
            }
        } else {
            c1DisplayInfo.uniqueName = c1DisplayInfo.originalName;
        }
        return c1DisplayInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getUniqueSubscriptionDisplayNames$7(Set set, C1DisplayInfo c1DisplayInfo) {
        return !set.add(c1DisplayInfo.uniqueName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ C1DisplayInfo lambda$getUniqueSubscriptionDisplayNames$9(Set set, C1DisplayInfo c1DisplayInfo) {
        if (set.contains(c1DisplayInfo.uniqueName)) {
            c1DisplayInfo.uniqueName = ((Object) c1DisplayInfo.originalName) + " " + c1DisplayInfo.subscriptionInfo.getSubscriptionId();
        }
        return c1DisplayInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$getUniqueSubscriptionDisplayNames$10(C1DisplayInfo c1DisplayInfo) {
        return Integer.valueOf(c1DisplayInfo.subscriptionInfo.getSubscriptionId());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CharSequence getMobileNetworkTitle() {
        return getUniqueSubscriptionDisplayName(this.mDefaultDataSubId, this.mContext);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getMobileNetworkSummary() {
        return getMobileSummary(this.mContext, getNetworkTypeDescription(this.mContext, this.mConfig, this.mTelephonyDisplayInfo, this.mDefaultDataSubId));
    }

    private String getNetworkTypeDescription(Context context, MobileMappings.Config config, TelephonyDisplayInfo telephonyDisplayInfo, int i) {
        String iconKey = MobileMappings.getIconKey(telephonyDisplayInfo);
        if (MobileMappings.mapIconSets(config) == null || MobileMappings.mapIconSets(config).get(iconKey) == null) {
            if (DEBUG) {
                Log.d("InternetDialogController", "The description of network type is empty.");
            }
            return "";
        }
        int i2 = MobileMappings.mapIconSets(config).get(iconKey).dataContentDescription;
        if (isCarrierNetworkActive()) {
            i2 = TelephonyIcons.CARRIER_MERGED_WIFI.dataContentDescription;
        }
        return i2 != 0 ? SubscriptionManager.getResourcesForSubId(context, i).getString(i2) : "";
    }

    private String getMobileSummary(Context context, String str) {
        if (!isMobileDataEnabled()) {
            return context.getString(R$string.mobile_data_off_summary);
        }
        if (!isDataStateInService()) {
            return context.getString(R$string.mobile_data_no_connection);
        }
        return (activeNetworkIsCellular() || isCarrierNetworkActive()) ? context.getString(R$string.preference_summary_default_combination, context.getString(R$string.mobile_data_connection_active), str) : str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void launchNetworkSetting() {
        this.mCallback.dismissDialog();
        this.mActivityStarter.postStartActivityDismissingKeyguard(getSettingsIntent(), 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void launchWifiNetworkDetailsSetting(String str) {
        Intent wifiDetailsSettingsIntent = getWifiDetailsSettingsIntent(str);
        if (wifiDetailsSettingsIntent != null) {
            this.mCallback.dismissDialog();
            this.mActivityStarter.postStartActivityDismissingKeyguard(wifiDetailsSettingsIntent, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void launchWifiScanningSetting() {
        this.mCallback.dismissDialog();
        Intent intent = new Intent("android.settings.WIFI_SCANNING_SETTINGS");
        intent.addFlags(268435456);
        this.mActivityStarter.postStartActivityDismissingKeyguard(intent, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void connectCarrierNetwork() {
        MergedCarrierEntry mergedCarrierEntry = this.mAccessPointController.getMergedCarrierEntry();
        if (mergedCarrierEntry == null || !mergedCarrierEntry.canConnect()) {
            return;
        }
        mergedCarrierEntry.connect(null, false);
        makeOverlayToast(R$string.wifi_wont_autoconnect_for_now);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCarrierNetworkActive() {
        MergedCarrierEntry mergedCarrierEntry = this.mAccessPointController.getMergedCarrierEntry();
        return mergedCarrierEntry != null && mergedCarrierEntry.isDefaultNetwork();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: setMergedCarrierWifiEnabledIfNeed */
    public void lambda$setMobileDataEnabled$12(int i, boolean z) {
        if (this.mCarrierConfigTracker.getCarrierProvisionsWifiMergedNetworksBool(i)) {
            return;
        }
        MergedCarrierEntry mergedCarrierEntry = this.mAccessPointController.getMergedCarrierEntry();
        if (mergedCarrierEntry == null) {
            if (!DEBUG) {
                return;
            }
            Log.d("InternetDialogController", "MergedCarrierEntry is null, can not set the status.");
            return;
        }
        mergedCarrierEntry.setEnabled(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WifiManager getWifiManager() {
        return this.mWifiManager;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TelephonyManager getTelephonyManager() {
        return this.mTelephonyManager;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SubscriptionManager getSubscriptionManager() {
        return this.mSubscriptionManager;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasCarrier() {
        if (this.mSubscriptionManager != null) {
            return !isAirplaneModeEnabled() && this.mTelephonyManager != null && this.mSubscriptionManager.getActiveSubscriptionIdList().length > 0;
        }
        if (DEBUG) {
            Log.d("InternetDialogController", "SubscriptionManager is null, can not check carrier.");
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isMobileDataEnabled() {
        TelephonyManager telephonyManager = this.mTelephonyManager;
        return telephonyManager != null && telephonyManager.isDataEnabled();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setMobileDataEnabled(Context context, final int i, final boolean z, boolean z2) {
        List<SubscriptionInfo> activeSubscriptionInfoList;
        TelephonyManager telephonyManager = this.mTelephonyManager;
        if (telephonyManager == null) {
            if (!DEBUG) {
                return;
            }
            Log.d("InternetDialogController", "TelephonyManager is null, can not set mobile data.");
        } else if (this.mSubscriptionManager == null) {
            if (!DEBUG) {
                return;
            }
            Log.d("InternetDialogController", "SubscriptionManager is null, can not set mobile data.");
        } else {
            telephonyManager.setDataEnabled(z);
            if (z2 && (activeSubscriptionInfoList = this.mSubscriptionManager.getActiveSubscriptionInfoList()) != null) {
                for (SubscriptionInfo subscriptionInfo : activeSubscriptionInfoList) {
                    if (subscriptionInfo.getSubscriptionId() != i && !subscriptionInfo.isOpportunistic()) {
                        ((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForSubscriptionId(subscriptionInfo.getSubscriptionId()).setDataEnabled(false);
                    }
                }
            }
            this.mWorkerHandler.post(new Runnable() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialogController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    InternetDialogController.this.lambda$setMobileDataEnabled$12(i, z);
                }
            });
        }
    }

    boolean isDataStateInService() {
        ServiceState serviceState = this.mTelephonyManager.getServiceState();
        NetworkRegistrationInfo networkRegistrationInfo = serviceState == null ? null : serviceState.getNetworkRegistrationInfo(2, 1);
        if (networkRegistrationInfo == null) {
            return false;
        }
        return networkRegistrationInfo.isRegistered();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isVoiceStateInService() {
        TelephonyManager telephonyManager = this.mTelephonyManager;
        if (telephonyManager == null) {
            if (DEBUG) {
                Log.d("InternetDialogController", "TelephonyManager is null, can not detect voice state.");
            }
            return false;
        }
        ServiceState serviceState = telephonyManager.getServiceState();
        return serviceState != null && serviceState.getState() == 0;
    }

    public boolean isDeviceLocked() {
        return !this.mKeyguardStateController.isUnlocked();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean activeNetworkIsCellular() {
        NetworkCapabilities networkCapabilities;
        ConnectivityManager connectivityManager = this.mConnectivityManager;
        if (connectivityManager == null) {
            if (DEBUG) {
                Log.d("InternetDialogController", "ConnectivityManager is null, can not check active network.");
            }
            return false;
        }
        Network activeNetwork = connectivityManager.getActiveNetwork();
        if (activeNetwork != null && (networkCapabilities = this.mConnectivityManager.getNetworkCapabilities(activeNetwork)) != null) {
            return networkCapabilities.hasTransport(0);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean connect(WifiEntry wifiEntry) {
        if (wifiEntry == null) {
            if (DEBUG) {
                Log.d("InternetDialogController", "No Wi-Fi ap to connect.");
            }
            return false;
        }
        if (wifiEntry.getWifiConfiguration() != null) {
            if (DEBUG) {
                Log.d("InternetDialogController", "connect networkId=" + wifiEntry.getWifiConfiguration().networkId);
            }
        } else if (DEBUG) {
            Log.d("InternetDialogController", "connect to unsaved network " + wifiEntry.getTitle());
        }
        wifiEntry.connect(new WifiEntryConnectCallback(this.mActivityStarter, wifiEntry, this));
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isWifiScanEnabled() {
        if (!this.mLocationController.isLocationEnabled()) {
            return false;
        }
        return this.mWifiManager.isScanAlwaysAvailable();
    }

    /* loaded from: classes.dex */
    static class WifiEntryConnectCallback implements WifiEntry.ConnectCallback {
        final ActivityStarter mActivityStarter;
        final InternetDialogController mInternetDialogController;
        final WifiEntry mWifiEntry;

        WifiEntryConnectCallback(ActivityStarter activityStarter, WifiEntry wifiEntry, InternetDialogController internetDialogController) {
            this.mActivityStarter = activityStarter;
            this.mWifiEntry = wifiEntry;
            this.mInternetDialogController = internetDialogController;
        }

        @Override // com.android.wifitrackerlib.WifiEntry.ConnectCallback
        public void onConnectResult(int i) {
            if (InternetDialogController.DEBUG) {
                Log.d("InternetDialogController", "onConnectResult " + i);
            }
            if (i == 1) {
                Intent putExtra = new Intent("com.android.settings.WIFI_DIALOG").putExtra("key_chosen_wifientry_key", this.mWifiEntry.getKey());
                putExtra.addFlags(268435456);
                this.mActivityStarter.startActivity(putExtra, false);
            } else if (i != 2) {
                if (!InternetDialogController.DEBUG) {
                    return;
                }
                Log.d("InternetDialogController", "connect failure reason=" + i);
            } else {
                this.mInternetDialogController.makeOverlayToast(R$string.wifi_failed_connect_message);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scanWifiAccessPoints() {
        if (this.mCanConfigWifi) {
            this.mAccessPointController.scanForAccessPoints();
        }
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController.AccessPointController.AccessPointCallback
    public void onAccessPointsChanged(List<WifiEntry> list) {
        boolean z;
        if (!this.mCanConfigWifi) {
            return;
        }
        int i = 0;
        if (list == null || list.size() == 0) {
            this.mConnectedEntry = null;
            this.mWifiEntriesCount = 0;
            InternetDialogCallback internetDialogCallback = this.mCallback;
            if (internetDialogCallback == null) {
                return;
            }
            internetDialogCallback.onAccessPointsChanged(null, null);
            return;
        }
        int size = list.size();
        int i2 = 0;
        while (true) {
            if (i2 >= size) {
                z = false;
                break;
            }
            WifiEntry wifiEntry = list.get(i2);
            if (wifiEntry.isDefaultNetwork() && wifiEntry.hasInternetAccess()) {
                this.mConnectedEntry = wifiEntry;
                z = true;
                break;
            }
            i2++;
        }
        if (!z) {
            this.mConnectedEntry = null;
        }
        int i3 = 4;
        if (this.mHasEthernet) {
            i3 = 3;
        }
        if (hasCarrier()) {
            i3--;
        }
        if (z) {
            i3--;
        }
        List<WifiEntry> list2 = (List) list.stream().filter(InternetDialogController$$ExternalSyntheticLambda11.INSTANCE).limit(i3).collect(Collectors.toList());
        if (list2 != null) {
            i = list2.size();
        }
        this.mWifiEntriesCount = i;
        InternetDialogCallback internetDialogCallback2 = this.mCallback;
        if (internetDialogCallback2 == null) {
            return;
        }
        internetDialogCallback2.onAccessPointsChanged(list2, this.mConnectedEntry);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onAccessPointsChanged$13(WifiEntry wifiEntry) {
        return !wifiEntry.isDefaultNetwork() || !wifiEntry.hasInternetAccess();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class InternetTelephonyCallback extends TelephonyCallback implements TelephonyCallback.DataConnectionStateListener, TelephonyCallback.DisplayInfoListener, TelephonyCallback.ServiceStateListener, TelephonyCallback.SignalStrengthsListener, TelephonyCallback.UserMobileDataStateListener {
        private InternetTelephonyCallback() {
        }

        @Override // android.telephony.TelephonyCallback.ServiceStateListener
        public void onServiceStateChanged(ServiceState serviceState) {
            InternetDialogController.this.mCallback.onServiceStateChanged(serviceState);
        }

        @Override // android.telephony.TelephonyCallback.DataConnectionStateListener
        public void onDataConnectionStateChanged(int i, int i2) {
            InternetDialogController.this.mCallback.onDataConnectionStateChanged(i, i2);
        }

        @Override // android.telephony.TelephonyCallback.SignalStrengthsListener
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            InternetDialogController.this.mCallback.onSignalStrengthsChanged(signalStrength);
        }

        @Override // android.telephony.TelephonyCallback.DisplayInfoListener
        public void onDisplayInfoChanged(TelephonyDisplayInfo telephonyDisplayInfo) {
            InternetDialogController.this.mTelephonyDisplayInfo = telephonyDisplayInfo;
            InternetDialogController.this.mCallback.onDisplayInfoChanged(telephonyDisplayInfo);
        }

        @Override // android.telephony.TelephonyCallback.UserMobileDataStateListener
        public void onUserMobileDataStateChanged(boolean z) {
            InternetDialogController.this.mCallback.onUserMobileDataStateChanged(z);
        }
    }

    /* loaded from: classes.dex */
    private class InternetOnSubscriptionChangedListener extends SubscriptionManager.OnSubscriptionsChangedListener {
        InternetOnSubscriptionChangedListener() {
        }

        @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
        public void onSubscriptionsChanged() {
            InternetDialogController.this.updateListener();
        }
    }

    /* loaded from: classes.dex */
    private class DataConnectivityListener extends ConnectivityManager.NetworkCallback {
        private DataConnectivityListener() {
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            InternetDialogController.this.mHasEthernet = networkCapabilities.hasTransport(3);
            InternetDialogController internetDialogController = InternetDialogController.this;
            if (internetDialogController.mCanConfigWifi && (internetDialogController.mHasEthernet || networkCapabilities.hasTransport(1))) {
                InternetDialogController.this.scanWifiAccessPoints();
            }
            InternetDialogController.this.mCallback.onCapabilitiesChanged(network, networkCapabilities);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            InternetDialogController internetDialogController = InternetDialogController.this;
            internetDialogController.mHasEthernet = false;
            internetDialogController.mCallback.onLost(network);
        }
    }

    public boolean hasEthernet() {
        return this.mHasEthernet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateListener() {
        int defaultDataSubscriptionId = getDefaultDataSubscriptionId();
        if (this.mDefaultDataSubId == getDefaultDataSubscriptionId()) {
            if (!DEBUG) {
                return;
            }
            Log.d("InternetDialogController", "DDS: no change");
            return;
        }
        this.mDefaultDataSubId = defaultDataSubscriptionId;
        if (DEBUG) {
            Log.d("InternetDialogController", "DDS: defaultDataSubId:" + this.mDefaultDataSubId);
        }
        if (!SubscriptionManager.isUsableSubscriptionId(this.mDefaultDataSubId)) {
            return;
        }
        this.mTelephonyManager.unregisterTelephonyCallback(this.mInternetTelephonyCallback);
        TelephonyManager createForSubscriptionId = this.mTelephonyManager.createForSubscriptionId(this.mDefaultDataSubId);
        this.mTelephonyManager = createForSubscriptionId;
        Handler handler = this.mHandler;
        Objects.requireNonNull(handler);
        createForSubscriptionId.registerTelephonyCallback(new MediaRoute2Provider$$ExternalSyntheticLambda0(handler), this.mInternetTelephonyCallback);
        this.mCallback.onSubscriptionsChanged(this.mDefaultDataSubId);
    }

    public WifiUtils.InternetIconInjector getWifiIconInjector() {
        return this.mWifiIconInjector;
    }

    void makeOverlayToast(int i) {
        Resources resources = this.mContext.getResources();
        final SystemUIToast createToast = this.mToastFactory.createToast(this.mContext, resources.getString(i), this.mContext.getPackageName(), UserHandle.myUserId(), resources.getConfiguration().orientation);
        if (createToast == null) {
            return;
        }
        final View view = createToast.getView();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.height = -2;
        layoutParams.width = -2;
        layoutParams.format = -3;
        layoutParams.type = 2017;
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
        this.mHandler.postDelayed(new Runnable() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialogController.3
            @Override // java.lang.Runnable
            public void run() {
                Animator outAnimation = createToast.getOutAnimation();
                if (outAnimation != null) {
                    outAnimation.start();
                    outAnimation.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.qs.tiles.dialog.InternetDialogController.3.1
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            InternetDialogController.this.mWindowManager.removeViewImmediate(view);
                        }
                    });
                }
            }
        }, SHORT_DURATION_TIMEOUT);
    }
}
