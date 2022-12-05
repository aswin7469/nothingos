package android.app;

import android.accounts.AccountManager;
import android.accounts.IAccountManager;
import android.annotation.SystemApi;
import android.app.IAlarmManager;
import android.app.IWallpaperManager;
import android.app.admin.DevicePolicyManager;
import android.app.admin.IDevicePolicyManager;
import android.app.appsearch.AppSearchManagerFrameworkInitializer;
import android.app.blob.BlobStoreManagerFrameworkInitializer;
import android.app.contentsuggestions.ContentSuggestionsManager;
import android.app.contentsuggestions.IContentSuggestionsManager;
import android.app.job.JobSchedulerFrameworkInitializer;
import android.app.people.PeopleManager;
import android.app.prediction.AppPredictionManager;
import android.app.role.RoleFrameworkInitializer;
import android.app.search.SearchUiManager;
import android.app.slice.SliceManager;
import android.app.smartspace.SmartspaceManager;
import android.app.time.TimeManager;
import android.app.timedetector.TimeDetector;
import android.app.timedetector.TimeDetectorImpl;
import android.app.timezone.RulesManager;
import android.app.timezonedetector.TimeZoneDetector;
import android.app.timezonedetector.TimeZoneDetectorImpl;
import android.app.trust.TrustManager;
import android.app.usage.IStorageStatsManager;
import android.app.usage.IUsageStatsManager;
import android.app.usage.NetworkStatsManager;
import android.app.usage.StorageStatsManager;
import android.app.usage.UsageStatsManager;
import android.apphibernation.AppHibernationManager;
import android.appwidget.AppWidgetManager;
import android.bluetooth.BluetoothManager;
import android.companion.CompanionDeviceManager;
import android.companion.ICompanionDeviceManager;
import android.content.ClipboardManager;
import android.content.ContentCaptureOptions;
import android.content.Context;
import android.content.IRestrictionsManager;
import android.content.RestrictionsManager;
import android.content.integrity.AppIntegrityManager;
import android.content.integrity.IAppIntegrityManager;
import android.content.om.IOverlayManager;
import android.content.om.OverlayManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.CrossProfileApps;
import android.content.pm.DataLoaderManager;
import android.content.pm.ICrossProfileApps;
import android.content.pm.IDataLoaderManager;
import android.content.pm.IShortcutService;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutManager;
import android.content.pm.verify.domain.DomainVerificationManager;
import android.content.pm.verify.domain.IDomainVerificationManager;
import android.content.res.Resources;
import android.content.rollback.RollbackManagerFrameworkInitializer;
import android.debug.AdbManager;
import android.debug.IAdbManager;
import android.graphics.fonts.FontManager;
import android.hardware.ConsumerIrManager;
import android.hardware.ISerialManager;
import android.hardware.SensorManager;
import android.hardware.SensorPrivacyManager;
import android.hardware.SerialManager;
import android.hardware.SystemSensorManager;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.IAuthService;
import android.hardware.camera2.CameraManager;
import android.hardware.devicestate.DeviceStateManager;
import android.hardware.display.ColorDisplayManager;
import android.hardware.display.DisplayManager;
import android.hardware.face.FaceManager;
import android.hardware.face.IFaceService;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.IFingerprintService;
import android.hardware.hdmi.HdmiControlManager;
import android.hardware.hdmi.IHdmiControlService;
import android.hardware.input.InputManager;
import android.hardware.iris.IIrisService;
import android.hardware.iris.IrisManager;
import android.hardware.lights.LightsManager;
import android.hardware.lights.SystemLightsManager;
import android.hardware.location.ContextHubManager;
import android.hardware.radio.RadioManager;
import android.hardware.usb.IUsbManager;
import android.hardware.usb.UsbManager;
import android.location.CountryDetector;
import android.location.ICountryDetector;
import android.location.ILocationManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaFrameworkInitializer;
import android.media.MediaFrameworkPlatformInitializer;
import android.media.MediaRouter;
import android.media.metrics.IMediaMetricsManager;
import android.media.metrics.MediaMetricsManager;
import android.media.midi.IMidiManager;
import android.media.midi.MidiManager;
import android.media.musicrecognition.IMusicRecognitionManager;
import android.media.musicrecognition.MusicRecognitionManager;
import android.media.projection.MediaProjectionManager;
import android.media.soundtrigger.SoundTriggerManager;
import android.media.tv.ITvInputManager;
import android.media.tv.TvInputManager;
import android.media.tv.tunerresourcemanager.ITunerResourceManager;
import android.media.tv.tunerresourcemanager.TunerResourceManager;
import android.net.ConnectivityFrameworkInitializer;
import android.net.EthernetManager;
import android.net.IEthernetManager;
import android.net.IIpSecService;
import android.net.INetworkPolicyManager;
import android.net.IPacProxyManager;
import android.net.IVpnManager;
import android.net.IpSecManager;
import android.net.NetworkPolicyManager;
import android.net.NetworkScoreManager;
import android.net.NetworkWatchlistManager;
import android.net.PacProxyManager;
import android.net.TetheringManager;
import android.net.VpnManager;
import android.net.lowpan.ILowpanManager;
import android.net.lowpan.LowpanManager;
import android.net.nsd.INsdManager;
import android.net.nsd.NsdManager;
import android.net.vcn.IVcnManagementService;
import android.net.vcn.VcnManager;
import android.net.wifi.WifiFrameworkInitializer;
import android.net.wifi.nl80211.WifiNl80211Manager;
import android.nfc.NfcManager;
import android.os.BatteryManager;
import android.os.BatteryStatsManager;
import android.os.BugreportManager;
import android.os.DropBoxManager;
import android.os.HardwarePropertiesManager;
import android.os.IBatteryPropertiesRegistrar;
import android.os.IBinder;
import android.os.IDumpstate;
import android.os.IHardwarePropertiesManager;
import android.os.IPowerManager;
import android.os.IRecoverySystem;
import android.os.ISystemUpdateManager;
import android.os.IThermalService;
import android.os.IUserManager;
import android.os.IncidentManager;
import android.os.PerformanceHintManager;
import android.os.PowerManager;
import android.os.Process;
import android.os.RecoverySystem;
import android.os.ServiceManager;
import android.os.StatsFrameworkInitializer;
import android.os.SystemConfigManager;
import android.os.SystemUpdateManager;
import android.os.SystemVibrator;
import android.os.SystemVibratorManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.os.health.SystemHealthManager;
import android.os.image.DynamicSystemManager;
import android.os.image.IDynamicSystemService;
import android.os.incremental.IIncrementalService;
import android.os.incremental.IncrementalManager;
import android.os.storage.StorageManager;
import android.permission.LegacyPermissionManager;
import android.permission.PermissionCheckerManager;
import android.permission.PermissionControllerManager;
import android.permission.PermissionManager;
import android.print.IPrintManager;
import android.print.PrintManager;
import android.scheduling.SchedulingFrameworkInitializer;
import android.security.FileIntegrityManager;
import android.security.IFileIntegrityService;
import android.service.oemlock.IOemLockService;
import android.service.oemlock.OemLockManager;
import android.service.persistentdata.IPersistentDataBlockService;
import android.service.persistentdata.PersistentDataBlockManager;
import android.service.vr.IVrManager;
import android.telecom.TelecomManager;
import android.telephony.MmsManager;
import android.telephony.TelephonyFrameworkInitializer;
import android.telephony.TelephonyRegistryManager;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Slog;
import android.uwb.UwbManager;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.WindowManagerImpl;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.CaptioningManager;
import android.view.autofill.AutofillManager;
import android.view.autofill.IAutoFillManager;
import android.view.contentcapture.ContentCaptureManager;
import android.view.contentcapture.IContentCaptureManager;
import android.view.displayhash.DisplayHashManager;
import android.view.inputmethod.InputMethodManager;
import android.view.textclassifier.TextClassificationManager;
import android.view.textservice.TextServicesManager;
import android.view.translation.ITranslationManager;
import android.view.translation.TranslationManager;
import android.view.translation.UiTranslationManager;
import com.android.internal.R;
import com.android.internal.app.IAppOpsService;
import com.android.internal.app.IBatteryStats;
import com.android.internal.app.ISoundTriggerService;
import com.android.internal.appwidget.IAppWidgetService;
import com.android.internal.graphics.fonts.IFontManager;
import com.android.internal.net.INetworkWatchlistManager;
import com.android.internal.os.IDropBoxManagerService;
import com.android.internal.policy.PhoneLayoutInflater;
import com.android.internal.util.Preconditions;
import java.util.Map;
import java.util.Objects;
@SystemApi
/* loaded from: classes.dex */
public final class SystemServiceRegistry {
    private static final Map<Class<?>, String> SYSTEM_SERVICE_NAMES;
    private static final String TAG = "SystemServiceRegistry";
    private static volatile boolean sInitializing;
    private static int sServiceCacheSize;
    public static boolean sEnableServiceNotFoundWtf = false;
    private static final Map<String, ServiceFetcher<?>> SYSTEM_SERVICE_FETCHERS = new ArrayMap();
    private static final Map<String, String> SYSTEM_SERVICE_CLASS_NAMES = new ArrayMap();

    @SystemApi
    /* loaded from: classes.dex */
    public interface ContextAwareServiceProducerWithBinder<TServiceClass> {
        TServiceClass createService(Context context, IBinder iBinder);
    }

    @SystemApi
    /* loaded from: classes.dex */
    public interface ContextAwareServiceProducerWithoutBinder<TServiceClass> {
        TServiceClass createService(Context context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface ServiceFetcher<T> {
        /* renamed from: getService */
        T mo257getService(ContextImpl contextImpl);
    }

    @SystemApi
    /* loaded from: classes.dex */
    public interface StaticServiceProducerWithBinder<TServiceClass> {
        TServiceClass createService(IBinder iBinder);
    }

    @SystemApi
    /* loaded from: classes.dex */
    public interface StaticServiceProducerWithoutBinder<TServiceClass> {
        TServiceClass createService();
    }

    static /* synthetic */ int access$008() {
        int i = sServiceCacheSize;
        sServiceCacheSize = i + 1;
        return i;
    }

    static {
        ArrayMap arrayMap = new ArrayMap();
        SYSTEM_SERVICE_NAMES = arrayMap;
        registerService(Context.ACCESSIBILITY_SERVICE, AccessibilityManager.class, new CachedServiceFetcher<AccessibilityManager>() { // from class: android.app.SystemServiceRegistry.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public AccessibilityManager mo332createService(ContextImpl ctx) {
                return AccessibilityManager.getInstance(ctx);
            }
        });
        registerService(Context.CAPTIONING_SERVICE, CaptioningManager.class, new CachedServiceFetcher<CaptioningManager>() { // from class: android.app.SystemServiceRegistry.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public CaptioningManager mo332createService(ContextImpl ctx) {
                return new CaptioningManager(ctx);
            }
        });
        registerService("account", AccountManager.class, new CachedServiceFetcher<AccountManager>() { // from class: android.app.SystemServiceRegistry.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public AccountManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow("account");
                IAccountManager service = IAccountManager.Stub.asInterface(b);
                return new AccountManager(ctx, service);
            }
        });
        registerService("activity", ActivityManager.class, new CachedServiceFetcher<ActivityManager>() { // from class: android.app.SystemServiceRegistry.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public ActivityManager mo332createService(ContextImpl ctx) {
                return new ActivityManager(ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        });
        registerService(Context.ACTIVITY_TASK_SERVICE, ActivityTaskManager.class, new CachedServiceFetcher<ActivityTaskManager>() { // from class: android.app.SystemServiceRegistry.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public ActivityTaskManager mo332createService(ContextImpl ctx) {
                return ActivityTaskManager.getInstance();
            }
        });
        registerService(Context.URI_GRANTS_SERVICE, UriGrantsManager.class, new CachedServiceFetcher<UriGrantsManager>() { // from class: android.app.SystemServiceRegistry.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public UriGrantsManager mo332createService(ContextImpl ctx) {
                return new UriGrantsManager(ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        });
        registerService("alarm", AlarmManager.class, new CachedServiceFetcher<AlarmManager>() { // from class: android.app.SystemServiceRegistry.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public AlarmManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow("alarm");
                IAlarmManager service = IAlarmManager.Stub.asInterface(b);
                return new AlarmManager(service, ctx);
            }
        });
        registerService("audio", AudioManager.class, new CachedServiceFetcher<AudioManager>() { // from class: android.app.SystemServiceRegistry.8
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public AudioManager mo332createService(ContextImpl ctx) {
                return new AudioManager(ctx);
            }
        });
        registerService(Context.MEDIA_ROUTER_SERVICE, MediaRouter.class, new CachedServiceFetcher<MediaRouter>() { // from class: android.app.SystemServiceRegistry.9
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public MediaRouter mo332createService(ContextImpl ctx) {
                return new MediaRouter(ctx);
            }
        });
        registerService("bluetooth", BluetoothManager.class, new CachedServiceFetcher<BluetoothManager>() { // from class: android.app.SystemServiceRegistry.10
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public BluetoothManager mo332createService(ContextImpl ctx) {
                return new BluetoothManager(ctx);
            }
        });
        registerService(Context.HDMI_CONTROL_SERVICE, HdmiControlManager.class, new StaticServiceFetcher<HdmiControlManager>() { // from class: android.app.SystemServiceRegistry.11
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.StaticServiceFetcher
            /* renamed from: createService */
            public HdmiControlManager mo316createService() throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.HDMI_CONTROL_SERVICE);
                return new HdmiControlManager(IHdmiControlService.Stub.asInterface(b));
            }
        });
        registerService(Context.TEXT_CLASSIFICATION_SERVICE, TextClassificationManager.class, new CachedServiceFetcher<TextClassificationManager>() { // from class: android.app.SystemServiceRegistry.12
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public TextClassificationManager mo332createService(ContextImpl ctx) {
                return new TextClassificationManager(ctx);
            }
        });
        registerService(Context.FONT_SERVICE, FontManager.class, new CachedServiceFetcher<FontManager>() { // from class: android.app.SystemServiceRegistry.13
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public FontManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.FONT_SERVICE);
                return FontManager.create(IFontManager.Stub.asInterface(b));
            }
        });
        registerService("clipboard", ClipboardManager.class, new CachedServiceFetcher<ClipboardManager>() { // from class: android.app.SystemServiceRegistry.14
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public ClipboardManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new ClipboardManager(ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        });
        arrayMap.put(android.text.ClipboardManager.class, "clipboard");
        registerService(Context.PAC_PROXY_SERVICE, PacProxyManager.class, new CachedServiceFetcher<PacProxyManager>() { // from class: android.app.SystemServiceRegistry.15
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public PacProxyManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.PAC_PROXY_SERVICE);
                IPacProxyManager service = IPacProxyManager.Stub.asInterface(b);
                return new PacProxyManager(ctx.getOuterContext(), service);
            }
        });
        registerService(Context.NETD_SERVICE, IBinder.class, new StaticServiceFetcher<IBinder>() { // from class: android.app.SystemServiceRegistry.16
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.StaticServiceFetcher
            /* renamed from: createService */
            public IBinder mo316createService() throws ServiceManager.ServiceNotFoundException {
                return ServiceManager.getServiceOrThrow(Context.NETD_SERVICE);
            }
        });
        registerService(Context.TETHERING_SERVICE, TetheringManager.class, new AnonymousClass17());
        registerService(Context.VPN_MANAGEMENT_SERVICE, VpnManager.class, new CachedServiceFetcher<VpnManager>() { // from class: android.app.SystemServiceRegistry.18
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public VpnManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getService(Context.VPN_MANAGEMENT_SERVICE);
                IVpnManager service = IVpnManager.Stub.asInterface(b);
                return new VpnManager(ctx, service);
            }
        });
        registerService(Context.VCN_MANAGEMENT_SERVICE, VcnManager.class, new CachedServiceFetcher<VcnManager>() { // from class: android.app.SystemServiceRegistry.19
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public VcnManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getService(Context.VCN_MANAGEMENT_SERVICE);
                IVcnManagementService service = IVcnManagementService.Stub.asInterface(b);
                return new VcnManager(ctx, service);
            }
        });
        registerService(Context.IPSEC_SERVICE, IpSecManager.class, new CachedServiceFetcher<IpSecManager>() { // from class: android.app.SystemServiceRegistry.20
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public IpSecManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getService(Context.IPSEC_SERVICE);
                IIpSecService service = IIpSecService.Stub.asInterface(b);
                return new IpSecManager(ctx, service);
            }
        });
        registerService(Context.COUNTRY_DETECTOR, CountryDetector.class, new StaticServiceFetcher<CountryDetector>() { // from class: android.app.SystemServiceRegistry.21
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.StaticServiceFetcher
            /* renamed from: createService */
            public CountryDetector mo316createService() throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.COUNTRY_DETECTOR);
                return new CountryDetector(ICountryDetector.Stub.asInterface(b));
            }
        });
        registerService(Context.DEVICE_POLICY_SERVICE, DevicePolicyManager.class, new CachedServiceFetcher<DevicePolicyManager>() { // from class: android.app.SystemServiceRegistry.22
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public DevicePolicyManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.DEVICE_POLICY_SERVICE);
                return new DevicePolicyManager(ctx, IDevicePolicyManager.Stub.asInterface(b));
            }
        });
        registerService(Context.DOWNLOAD_SERVICE, DownloadManager.class, new CachedServiceFetcher<DownloadManager>() { // from class: android.app.SystemServiceRegistry.23
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public DownloadManager mo332createService(ContextImpl ctx) {
                return new DownloadManager(ctx);
            }
        });
        registerService(Context.BATTERY_SERVICE, BatteryManager.class, new CachedServiceFetcher<BatteryManager>() { // from class: android.app.SystemServiceRegistry.24
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public BatteryManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBatteryStats stats = IBatteryStats.Stub.asInterface(ServiceManager.getServiceOrThrow("batterystats"));
                IBatteryPropertiesRegistrar registrar = IBatteryPropertiesRegistrar.Stub.asInterface(ServiceManager.getServiceOrThrow("batteryproperties"));
                return new BatteryManager(ctx, stats, registrar);
            }
        });
        registerService("nfc", NfcManager.class, new CachedServiceFetcher<NfcManager>() { // from class: android.app.SystemServiceRegistry.25
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public NfcManager mo332createService(ContextImpl ctx) {
                return new NfcManager(ctx);
            }
        });
        registerService(Context.DROPBOX_SERVICE, DropBoxManager.class, new CachedServiceFetcher<DropBoxManager>() { // from class: android.app.SystemServiceRegistry.26
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public DropBoxManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.DROPBOX_SERVICE);
                IDropBoxManagerService service = IDropBoxManagerService.Stub.asInterface(b);
                return new DropBoxManager(ctx, service);
            }
        });
        registerService("input", InputManager.class, new StaticServiceFetcher<InputManager>() { // from class: android.app.SystemServiceRegistry.27
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.StaticServiceFetcher
            /* renamed from: createService */
            public InputManager mo316createService() {
                return InputManager.getInstance();
            }
        });
        registerService(Context.DISPLAY_SERVICE, DisplayManager.class, new CachedServiceFetcher<DisplayManager>() { // from class: android.app.SystemServiceRegistry.28
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public DisplayManager mo332createService(ContextImpl ctx) {
                return new DisplayManager(ctx.getOuterContext());
            }
        });
        registerService(Context.COLOR_DISPLAY_SERVICE, ColorDisplayManager.class, new CachedServiceFetcher<ColorDisplayManager>() { // from class: android.app.SystemServiceRegistry.29
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public ColorDisplayManager mo332createService(ContextImpl ctx) {
                return new ColorDisplayManager();
            }
        });
        registerService(Context.INPUT_METHOD_SERVICE, InputMethodManager.class, new ServiceFetcher<InputMethodManager>() { // from class: android.app.SystemServiceRegistry.30
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.ServiceFetcher
            /* renamed from: getService */
            public InputMethodManager mo257getService(ContextImpl ctx) {
                return InputMethodManager.forContext(ctx.getOuterContext());
            }
        });
        registerService(Context.TEXT_SERVICES_MANAGER_SERVICE, TextServicesManager.class, new CachedServiceFetcher<TextServicesManager>() { // from class: android.app.SystemServiceRegistry.31
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public TextServicesManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return TextServicesManager.createInstance(ctx);
            }
        });
        registerService(Context.KEYGUARD_SERVICE, KeyguardManager.class, new CachedServiceFetcher<KeyguardManager>() { // from class: android.app.SystemServiceRegistry.32
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public KeyguardManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new KeyguardManager(ctx);
            }
        });
        registerService(Context.LAYOUT_INFLATER_SERVICE, LayoutInflater.class, new CachedServiceFetcher<LayoutInflater>() { // from class: android.app.SystemServiceRegistry.33
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public LayoutInflater mo332createService(ContextImpl ctx) {
                return new PhoneLayoutInflater(ctx.getOuterContext());
            }
        });
        registerService("location", LocationManager.class, new CachedServiceFetcher<LocationManager>() { // from class: android.app.SystemServiceRegistry.34
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public LocationManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow("location");
                return new LocationManager(ctx, ILocationManager.Stub.asInterface(b));
            }
        });
        registerService(Context.NETWORK_POLICY_SERVICE, NetworkPolicyManager.class, new CachedServiceFetcher<NetworkPolicyManager>() { // from class: android.app.SystemServiceRegistry.35
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public NetworkPolicyManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new NetworkPolicyManager(ctx, INetworkPolicyManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.NETWORK_POLICY_SERVICE)));
            }
        });
        registerService("notification", NotificationManager.class, new CachedServiceFetcher<NotificationManager>() { // from class: android.app.SystemServiceRegistry.36
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public NotificationManager mo332createService(ContextImpl ctx) {
                Context outerContext = ctx.getOuterContext();
                return new NotificationManager(new ContextThemeWrapper(outerContext, Resources.selectSystemTheme(0, outerContext.getApplicationInfo().targetSdkVersion, 16973835, 16973935, 16974126, 16974130)), ctx.mMainThread.getHandler());
            }
        });
        registerService(Context.NSD_SERVICE, NsdManager.class, new CachedServiceFetcher<NsdManager>() { // from class: android.app.SystemServiceRegistry.37
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public NsdManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.NSD_SERVICE);
                INsdManager service = INsdManager.Stub.asInterface(b);
                return new NsdManager(ctx.getOuterContext(), service);
            }
        });
        registerService(Context.PEOPLE_SERVICE, PeopleManager.class, new CachedServiceFetcher<PeopleManager>() { // from class: android.app.SystemServiceRegistry.38
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public PeopleManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new PeopleManager(ctx);
            }
        });
        registerService(Context.POWER_SERVICE, PowerManager.class, new CachedServiceFetcher<PowerManager>() { // from class: android.app.SystemServiceRegistry.39
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public PowerManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder powerBinder = ServiceManager.getServiceOrThrow(Context.POWER_SERVICE);
                IPowerManager powerService = IPowerManager.Stub.asInterface(powerBinder);
                IBinder thermalBinder = ServiceManager.getServiceOrThrow(Context.THERMAL_SERVICE);
                IThermalService thermalService = IThermalService.Stub.asInterface(thermalBinder);
                return new PowerManager(ctx.getOuterContext(), powerService, thermalService, ctx.mMainThread.getHandler());
            }
        });
        registerService(Context.PERFORMANCE_HINT_SERVICE, PerformanceHintManager.class, new CachedServiceFetcher<PerformanceHintManager>() { // from class: android.app.SystemServiceRegistry.40
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public PerformanceHintManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return PerformanceHintManager.create();
            }
        });
        registerService("recovery", RecoverySystem.class, new CachedServiceFetcher<RecoverySystem>() { // from class: android.app.SystemServiceRegistry.41
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public RecoverySystem mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow("recovery");
                IRecoverySystem service = IRecoverySystem.Stub.asInterface(b);
                return new RecoverySystem(service);
            }
        });
        registerService("search", SearchManager.class, new CachedServiceFetcher<SearchManager>() { // from class: android.app.SystemServiceRegistry.42
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public SearchManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new SearchManager(ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        });
        registerService(Context.SENSOR_SERVICE, SensorManager.class, new CachedServiceFetcher<SensorManager>() { // from class: android.app.SystemServiceRegistry.43
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public SensorManager mo332createService(ContextImpl ctx) {
                return new SystemSensorManager(ctx.getOuterContext(), ctx.mMainThread.getHandler().getLooper());
            }
        });
        registerService(Context.SENSOR_PRIVACY_SERVICE, SensorPrivacyManager.class, new CachedServiceFetcher<SensorPrivacyManager>() { // from class: android.app.SystemServiceRegistry.44
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public SensorPrivacyManager mo332createService(ContextImpl ctx) {
                return SensorPrivacyManager.getInstance(ctx);
            }
        });
        registerService(Context.STATUS_BAR_SERVICE, StatusBarManager.class, new CachedServiceFetcher<StatusBarManager>() { // from class: android.app.SystemServiceRegistry.45
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public StatusBarManager mo332createService(ContextImpl ctx) {
                return new StatusBarManager(ctx.getOuterContext());
            }
        });
        registerService("storage", StorageManager.class, new CachedServiceFetcher<StorageManager>() { // from class: android.app.SystemServiceRegistry.46
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public StorageManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new StorageManager(ctx, ctx.mMainThread.getHandler().getLooper());
            }
        });
        registerService(Context.STORAGE_STATS_SERVICE, StorageStatsManager.class, new CachedServiceFetcher<StorageStatsManager>() { // from class: android.app.SystemServiceRegistry.47
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public StorageStatsManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IStorageStatsManager service = IStorageStatsManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.STORAGE_STATS_SERVICE));
                return new StorageStatsManager(ctx, service);
            }
        });
        registerService(Context.SYSTEM_UPDATE_SERVICE, SystemUpdateManager.class, new CachedServiceFetcher<SystemUpdateManager>() { // from class: android.app.SystemServiceRegistry.48
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public SystemUpdateManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.SYSTEM_UPDATE_SERVICE);
                ISystemUpdateManager service = ISystemUpdateManager.Stub.asInterface(b);
                return new SystemUpdateManager(service);
            }
        });
        registerService(Context.SYSTEM_CONFIG_SERVICE, SystemConfigManager.class, new CachedServiceFetcher<SystemConfigManager>() { // from class: android.app.SystemServiceRegistry.49
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public SystemConfigManager mo332createService(ContextImpl ctx) {
                return new SystemConfigManager();
            }
        });
        registerService(Context.TELEPHONY_REGISTRY_SERVICE, TelephonyRegistryManager.class, new CachedServiceFetcher<TelephonyRegistryManager>() { // from class: android.app.SystemServiceRegistry.50
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public TelephonyRegistryManager mo332createService(ContextImpl ctx) {
                return new TelephonyRegistryManager(ctx);
            }
        });
        registerService(Context.TELECOM_SERVICE, TelecomManager.class, new CachedServiceFetcher<TelecomManager>() { // from class: android.app.SystemServiceRegistry.51
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public TelecomManager mo332createService(ContextImpl ctx) {
                return new TelecomManager(ctx.getOuterContext());
            }
        });
        registerService("mms", MmsManager.class, new CachedServiceFetcher<MmsManager>() { // from class: android.app.SystemServiceRegistry.52
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public MmsManager mo332createService(ContextImpl ctx) {
                return new MmsManager(ctx.getOuterContext());
            }
        });
        registerService(Context.UI_MODE_SERVICE, UiModeManager.class, new CachedServiceFetcher<UiModeManager>() { // from class: android.app.SystemServiceRegistry.53
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public UiModeManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new UiModeManager(ctx.getOuterContext());
            }
        });
        registerService(Context.USB_SERVICE, UsbManager.class, new CachedServiceFetcher<UsbManager>() { // from class: android.app.SystemServiceRegistry.54
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public UsbManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.USB_SERVICE);
                return new UsbManager(ctx, IUsbManager.Stub.asInterface(b));
            }
        });
        registerService("adb", AdbManager.class, new CachedServiceFetcher<AdbManager>() { // from class: android.app.SystemServiceRegistry.55
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public AdbManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow("adb");
                return new AdbManager(ctx, IAdbManager.Stub.asInterface(b));
            }
        });
        registerService(Context.SERIAL_SERVICE, SerialManager.class, new CachedServiceFetcher<SerialManager>() { // from class: android.app.SystemServiceRegistry.56
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public SerialManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.SERIAL_SERVICE);
                return new SerialManager(ctx, ISerialManager.Stub.asInterface(b));
            }
        });
        registerService(Context.UWB_SERVICE, UwbManager.class, new CachedServiceFetcher<UwbManager>() { // from class: android.app.SystemServiceRegistry.57
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public UwbManager mo332createService(ContextImpl ctx) {
                return UwbManager.getInstance(ctx);
            }
        });
        registerService(Context.VIBRATOR_MANAGER_SERVICE, VibratorManager.class, new CachedServiceFetcher<VibratorManager>() { // from class: android.app.SystemServiceRegistry.58
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public VibratorManager mo332createService(ContextImpl ctx) {
                return new SystemVibratorManager(ctx);
            }
        });
        registerService(Context.VIBRATOR_SERVICE, Vibrator.class, new CachedServiceFetcher<Vibrator>() { // from class: android.app.SystemServiceRegistry.59
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public Vibrator mo332createService(ContextImpl ctx) {
                return new SystemVibrator(ctx);
            }
        });
        registerService(Context.WALLPAPER_SERVICE, WallpaperManager.class, new CachedServiceFetcher<WallpaperManager>() { // from class: android.app.SystemServiceRegistry.60
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public WallpaperManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getService(Context.WALLPAPER_SERVICE);
                if (b == null) {
                    ApplicationInfo appInfo = ctx.getApplicationInfo();
                    if (appInfo.targetSdkVersion >= 28 && appInfo.isInstantApp()) {
                        throw new ServiceManager.ServiceNotFoundException(Context.WALLPAPER_SERVICE);
                    }
                    boolean enabled = Resources.getSystem().getBoolean(R.bool.config_enableWallpaperService);
                    if (!enabled) {
                        return DisabledWallpaperManager.getInstance();
                    }
                    Log.e(SystemServiceRegistry.TAG, "No wallpaper service");
                }
                IWallpaperManager service = IWallpaperManager.Stub.asInterface(b);
                return new WallpaperManager(service, ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        });
        registerService("lowpan", LowpanManager.class, new CachedServiceFetcher<LowpanManager>() { // from class: android.app.SystemServiceRegistry.61
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public LowpanManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow("lowpan");
                ILowpanManager service = ILowpanManager.Stub.asInterface(b);
                return new LowpanManager(ctx.getOuterContext(), service);
            }
        });
        registerService(Context.ETHERNET_SERVICE, EthernetManager.class, new CachedServiceFetcher<EthernetManager>() { // from class: android.app.SystemServiceRegistry.62
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public EthernetManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.ETHERNET_SERVICE);
                IEthernetManager service = IEthernetManager.Stub.asInterface(b);
                return new EthernetManager(ctx.getOuterContext(), service);
            }
        });
        registerService(Context.WIFI_NL80211_SERVICE, WifiNl80211Manager.class, new CachedServiceFetcher<WifiNl80211Manager>() { // from class: android.app.SystemServiceRegistry.63
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public WifiNl80211Manager mo332createService(ContextImpl ctx) {
                return new WifiNl80211Manager(ctx.getOuterContext());
            }
        });
        registerService(Context.WINDOW_SERVICE, WindowManager.class, new CachedServiceFetcher<WindowManager>() { // from class: android.app.SystemServiceRegistry.64
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public WindowManager mo332createService(ContextImpl ctx) {
                return new WindowManagerImpl(ctx);
            }
        });
        registerService("user", UserManager.class, new CachedServiceFetcher<UserManager>() { // from class: android.app.SystemServiceRegistry.65
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public UserManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow("user");
                IUserManager service = IUserManager.Stub.asInterface(b);
                return new UserManager(ctx, service);
            }
        });
        registerService(Context.APP_OPS_SERVICE, AppOpsManager.class, new CachedServiceFetcher<AppOpsManager>() { // from class: android.app.SystemServiceRegistry.66
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public AppOpsManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.APP_OPS_SERVICE);
                IAppOpsService service = IAppOpsService.Stub.asInterface(b);
                return new AppOpsManager(ctx, service);
            }
        });
        registerService(Context.CAMERA_SERVICE, CameraManager.class, new CachedServiceFetcher<CameraManager>() { // from class: android.app.SystemServiceRegistry.67
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public CameraManager mo332createService(ContextImpl ctx) {
                return new CameraManager(ctx);
            }
        });
        registerService(Context.LAUNCHER_APPS_SERVICE, LauncherApps.class, new CachedServiceFetcher<LauncherApps>() { // from class: android.app.SystemServiceRegistry.68
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public LauncherApps mo332createService(ContextImpl ctx) {
                return new LauncherApps(ctx);
            }
        });
        registerService(Context.RESTRICTIONS_SERVICE, RestrictionsManager.class, new CachedServiceFetcher<RestrictionsManager>() { // from class: android.app.SystemServiceRegistry.69
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public RestrictionsManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.RESTRICTIONS_SERVICE);
                IRestrictionsManager service = IRestrictionsManager.Stub.asInterface(b);
                return new RestrictionsManager(ctx, service);
            }
        });
        registerService(Context.PRINT_SERVICE, PrintManager.class, new CachedServiceFetcher<PrintManager>() { // from class: android.app.SystemServiceRegistry.70
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public PrintManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IPrintManager service = null;
                if (ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_PRINTING)) {
                    service = IPrintManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.PRINT_SERVICE));
                }
                int userId = ctx.getUserId();
                int appId = UserHandle.getAppId(ctx.getApplicationInfo().uid);
                return new PrintManager(ctx.getOuterContext(), service, userId, appId);
            }
        });
        registerService(Context.COMPANION_DEVICE_SERVICE, CompanionDeviceManager.class, new CachedServiceFetcher<CompanionDeviceManager>() { // from class: android.app.SystemServiceRegistry.71
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public CompanionDeviceManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                ICompanionDeviceManager service = null;
                if (ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_COMPANION_DEVICE_SETUP)) {
                    service = ICompanionDeviceManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.COMPANION_DEVICE_SERVICE));
                }
                return new CompanionDeviceManager(service, ctx.getOuterContext());
            }
        });
        registerService(Context.CONSUMER_IR_SERVICE, ConsumerIrManager.class, new CachedServiceFetcher<ConsumerIrManager>() { // from class: android.app.SystemServiceRegistry.72
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public ConsumerIrManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new ConsumerIrManager(ctx);
            }
        });
        registerService(Context.TRUST_SERVICE, TrustManager.class, new StaticServiceFetcher<TrustManager>() { // from class: android.app.SystemServiceRegistry.73
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.StaticServiceFetcher
            /* renamed from: createService */
            public TrustManager mo316createService() throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.TRUST_SERVICE);
                return new TrustManager(b);
            }
        });
        registerService(Context.FINGERPRINT_SERVICE, FingerprintManager.class, new CachedServiceFetcher<FingerprintManager>() { // from class: android.app.SystemServiceRegistry.74
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public FingerprintManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder binder;
                if (ctx.getApplicationInfo().targetSdkVersion >= 26) {
                    binder = ServiceManager.getServiceOrThrow(Context.FINGERPRINT_SERVICE);
                } else {
                    binder = ServiceManager.getService(Context.FINGERPRINT_SERVICE);
                }
                IFingerprintService service = IFingerprintService.Stub.asInterface(binder);
                return new FingerprintManager(ctx.getOuterContext(), service);
            }
        });
        registerService(Context.FACE_SERVICE, FaceManager.class, new CachedServiceFetcher<FaceManager>() { // from class: android.app.SystemServiceRegistry.75
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public FaceManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder binder;
                if (ctx.getApplicationInfo().targetSdkVersion >= 26) {
                    binder = ServiceManager.getServiceOrThrow(Context.FACE_SERVICE);
                } else {
                    binder = ServiceManager.getService(Context.FACE_SERVICE);
                }
                IFaceService service = IFaceService.Stub.asInterface(binder);
                return new FaceManager(ctx.getOuterContext(), service);
            }
        });
        registerService(Context.IRIS_SERVICE, IrisManager.class, new CachedServiceFetcher<IrisManager>() { // from class: android.app.SystemServiceRegistry.76
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public IrisManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder binder = ServiceManager.getServiceOrThrow(Context.IRIS_SERVICE);
                IIrisService service = IIrisService.Stub.asInterface(binder);
                return new IrisManager(ctx.getOuterContext(), service);
            }
        });
        registerService(Context.BIOMETRIC_SERVICE, BiometricManager.class, new CachedServiceFetcher<BiometricManager>() { // from class: android.app.SystemServiceRegistry.77
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public BiometricManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder binder = ServiceManager.getServiceOrThrow(Context.AUTH_SERVICE);
                IAuthService service = IAuthService.Stub.asInterface(binder);
                return new BiometricManager(ctx.getOuterContext(), service);
            }
        });
        registerService(Context.TV_INPUT_SERVICE, TvInputManager.class, new CachedServiceFetcher<TvInputManager>() { // from class: android.app.SystemServiceRegistry.78
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public TvInputManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder iBinder = ServiceManager.getServiceOrThrow(Context.TV_INPUT_SERVICE);
                ITvInputManager service = ITvInputManager.Stub.asInterface(iBinder);
                return new TvInputManager(service, ctx.getUserId());
            }
        });
        registerService(Context.TV_TUNER_RESOURCE_MGR_SERVICE, TunerResourceManager.class, new CachedServiceFetcher<TunerResourceManager>() { // from class: android.app.SystemServiceRegistry.79
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public TunerResourceManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder iBinder = ServiceManager.getServiceOrThrow(Context.TV_TUNER_RESOURCE_MGR_SERVICE);
                ITunerResourceManager service = ITunerResourceManager.Stub.asInterface(iBinder);
                return new TunerResourceManager(service, ctx.getUserId());
            }
        });
        registerService(Context.NETWORK_SCORE_SERVICE, NetworkScoreManager.class, new CachedServiceFetcher<NetworkScoreManager>() { // from class: android.app.SystemServiceRegistry.80
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public NetworkScoreManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new NetworkScoreManager(ctx);
            }
        });
        registerService(Context.USAGE_STATS_SERVICE, UsageStatsManager.class, new CachedServiceFetcher<UsageStatsManager>() { // from class: android.app.SystemServiceRegistry.81
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public UsageStatsManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder iBinder = ServiceManager.getServiceOrThrow(Context.USAGE_STATS_SERVICE);
                IUsageStatsManager service = IUsageStatsManager.Stub.asInterface(iBinder);
                return new UsageStatsManager(ctx.getOuterContext(), service);
            }
        });
        registerService(Context.NETWORK_STATS_SERVICE, NetworkStatsManager.class, new CachedServiceFetcher<NetworkStatsManager>() { // from class: android.app.SystemServiceRegistry.82
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public NetworkStatsManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new NetworkStatsManager(ctx.getOuterContext());
            }
        });
        registerService(Context.PERSISTENT_DATA_BLOCK_SERVICE, PersistentDataBlockManager.class, new StaticServiceFetcher<PersistentDataBlockManager>() { // from class: android.app.SystemServiceRegistry.83
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.StaticServiceFetcher
            /* renamed from: createService */
            public PersistentDataBlockManager mo316createService() throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.PERSISTENT_DATA_BLOCK_SERVICE);
                IPersistentDataBlockService persistentDataBlockService = IPersistentDataBlockService.Stub.asInterface(b);
                if (persistentDataBlockService != null) {
                    return new PersistentDataBlockManager(persistentDataBlockService);
                }
                return null;
            }
        });
        registerService(Context.OEM_LOCK_SERVICE, OemLockManager.class, new StaticServiceFetcher<OemLockManager>() { // from class: android.app.SystemServiceRegistry.84
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.StaticServiceFetcher
            /* renamed from: createService */
            public OemLockManager mo316createService() throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.OEM_LOCK_SERVICE);
                IOemLockService oemLockService = IOemLockService.Stub.asInterface(b);
                if (oemLockService != null) {
                    return new OemLockManager(oemLockService);
                }
                return null;
            }
        });
        registerService(Context.MEDIA_PROJECTION_SERVICE, MediaProjectionManager.class, new CachedServiceFetcher<MediaProjectionManager>() { // from class: android.app.SystemServiceRegistry.85
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public MediaProjectionManager mo332createService(ContextImpl ctx) {
                return new MediaProjectionManager(ctx);
            }
        });
        registerService(Context.APPWIDGET_SERVICE, AppWidgetManager.class, new CachedServiceFetcher<AppWidgetManager>() { // from class: android.app.SystemServiceRegistry.86
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public AppWidgetManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.APPWIDGET_SERVICE);
                return new AppWidgetManager(ctx, IAppWidgetService.Stub.asInterface(b));
            }
        });
        registerService("midi", MidiManager.class, new CachedServiceFetcher<MidiManager>() { // from class: android.app.SystemServiceRegistry.87
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public MidiManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow("midi");
                return new MidiManager(IMidiManager.Stub.asInterface(b));
            }
        });
        registerService(Context.RADIO_SERVICE, RadioManager.class, new CachedServiceFetcher<RadioManager>() { // from class: android.app.SystemServiceRegistry.88
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public RadioManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new RadioManager(ctx);
            }
        });
        registerService(Context.HARDWARE_PROPERTIES_SERVICE, HardwarePropertiesManager.class, new CachedServiceFetcher<HardwarePropertiesManager>() { // from class: android.app.SystemServiceRegistry.89
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public HardwarePropertiesManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.HARDWARE_PROPERTIES_SERVICE);
                IHardwarePropertiesManager service = IHardwarePropertiesManager.Stub.asInterface(b);
                return new HardwarePropertiesManager(ctx, service);
            }
        });
        registerService(Context.SOUND_TRIGGER_SERVICE, SoundTriggerManager.class, new CachedServiceFetcher<SoundTriggerManager>() { // from class: android.app.SystemServiceRegistry.90
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public SoundTriggerManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.SOUND_TRIGGER_SERVICE);
                return new SoundTriggerManager(ctx, ISoundTriggerService.Stub.asInterface(b));
            }
        });
        registerService("shortcut", ShortcutManager.class, new CachedServiceFetcher<ShortcutManager>() { // from class: android.app.SystemServiceRegistry.91
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public ShortcutManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow("shortcut");
                return new ShortcutManager(ctx, IShortcutService.Stub.asInterface(b));
            }
        });
        registerService("overlay", OverlayManager.class, new CachedServiceFetcher<OverlayManager>() { // from class: android.app.SystemServiceRegistry.92
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public OverlayManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow("overlay");
                return new OverlayManager(ctx, IOverlayManager.Stub.asInterface(b));
            }
        });
        registerService(Context.NETWORK_WATCHLIST_SERVICE, NetworkWatchlistManager.class, new CachedServiceFetcher<NetworkWatchlistManager>() { // from class: android.app.SystemServiceRegistry.93
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public NetworkWatchlistManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.NETWORK_WATCHLIST_SERVICE);
                return new NetworkWatchlistManager(ctx, INetworkWatchlistManager.Stub.asInterface(b));
            }
        });
        registerService(Context.SYSTEM_HEALTH_SERVICE, SystemHealthManager.class, new CachedServiceFetcher<SystemHealthManager>() { // from class: android.app.SystemServiceRegistry.94
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public SystemHealthManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow("batterystats");
                return new SystemHealthManager(IBatteryStats.Stub.asInterface(b));
            }
        });
        registerService(Context.CONTEXTHUB_SERVICE, ContextHubManager.class, new CachedServiceFetcher<ContextHubManager>() { // from class: android.app.SystemServiceRegistry.95
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public ContextHubManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new ContextHubManager(ctx.getOuterContext(), ctx.mMainThread.getHandler().getLooper());
            }
        });
        registerService(Context.INCIDENT_SERVICE, IncidentManager.class, new CachedServiceFetcher<IncidentManager>() { // from class: android.app.SystemServiceRegistry.96
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public IncidentManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new IncidentManager(ctx);
            }
        });
        registerService(Context.BUGREPORT_SERVICE, BugreportManager.class, new CachedServiceFetcher<BugreportManager>() { // from class: android.app.SystemServiceRegistry.97
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public BugreportManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.BUGREPORT_SERVICE);
                return new BugreportManager(ctx.getOuterContext(), IDumpstate.Stub.asInterface(b));
            }
        });
        registerService("autofill", AutofillManager.class, new CachedServiceFetcher<AutofillManager>() { // from class: android.app.SystemServiceRegistry.98
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public AutofillManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getService("autofill");
                IAutoFillManager service = IAutoFillManager.Stub.asInterface(b);
                return new AutofillManager(ctx.getOuterContext(), service);
            }
        });
        registerService(Context.MUSIC_RECOGNITION_SERVICE, MusicRecognitionManager.class, new CachedServiceFetcher<MusicRecognitionManager>() { // from class: android.app.SystemServiceRegistry.99
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public MusicRecognitionManager mo332createService(ContextImpl ctx) {
                IBinder b = ServiceManager.getService(Context.MUSIC_RECOGNITION_SERVICE);
                return new MusicRecognitionManager(IMusicRecognitionManager.Stub.asInterface(b));
            }
        });
        registerService("content_capture", ContentCaptureManager.class, new CachedServiceFetcher<ContentCaptureManager>() { // from class: android.app.SystemServiceRegistry.100
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public ContentCaptureManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                Context outerContext = ctx.getOuterContext();
                ContentCaptureOptions options = outerContext.getContentCaptureOptions();
                if (options != null) {
                    if (options.lite || options.isWhitelisted(outerContext)) {
                        IBinder b = ServiceManager.getService("content_capture");
                        IContentCaptureManager service = IContentCaptureManager.Stub.asInterface(b);
                        if (service != null) {
                            return new ContentCaptureManager(outerContext, service, options);
                        }
                        return null;
                    }
                    return null;
                }
                return null;
            }
        });
        registerService(Context.TRANSLATION_MANAGER_SERVICE, TranslationManager.class, new CachedServiceFetcher<TranslationManager>() { // from class: android.app.SystemServiceRegistry.101
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public TranslationManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getService(Context.TRANSLATION_MANAGER_SERVICE);
                ITranslationManager service = ITranslationManager.Stub.asInterface(b);
                if (service != null) {
                    return new TranslationManager(ctx.getOuterContext(), service);
                }
                return null;
            }
        });
        registerService(Context.UI_TRANSLATION_SERVICE, UiTranslationManager.class, new CachedServiceFetcher<UiTranslationManager>() { // from class: android.app.SystemServiceRegistry.102
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public UiTranslationManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getService(Context.TRANSLATION_MANAGER_SERVICE);
                ITranslationManager service = ITranslationManager.Stub.asInterface(b);
                if (service != null) {
                    return new UiTranslationManager(ctx.getOuterContext(), service);
                }
                return null;
            }
        });
        registerService(Context.SEARCH_UI_SERVICE, SearchUiManager.class, new CachedServiceFetcher<SearchUiManager>() { // from class: android.app.SystemServiceRegistry.103
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public SearchUiManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getService(Context.SEARCH_UI_SERVICE);
                if (b == null) {
                    return null;
                }
                return new SearchUiManager(ctx);
            }
        });
        registerService(Context.SMARTSPACE_SERVICE, SmartspaceManager.class, new CachedServiceFetcher<SmartspaceManager>() { // from class: android.app.SystemServiceRegistry.104
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public SmartspaceManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getService(Context.SMARTSPACE_SERVICE);
                if (b == null) {
                    return null;
                }
                return new SmartspaceManager(ctx);
            }
        });
        registerService(Context.APP_PREDICTION_SERVICE, AppPredictionManager.class, new CachedServiceFetcher<AppPredictionManager>() { // from class: android.app.SystemServiceRegistry.105
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public AppPredictionManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getService(Context.APP_PREDICTION_SERVICE);
                if (b == null) {
                    return null;
                }
                return new AppPredictionManager(ctx);
            }
        });
        registerService(Context.CONTENT_SUGGESTIONS_SERVICE, ContentSuggestionsManager.class, new CachedServiceFetcher<ContentSuggestionsManager>() { // from class: android.app.SystemServiceRegistry.106
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public ContentSuggestionsManager mo332createService(ContextImpl ctx) {
                IBinder b = ServiceManager.getService(Context.CONTENT_SUGGESTIONS_SERVICE);
                IContentSuggestionsManager service = IContentSuggestionsManager.Stub.asInterface(b);
                return new ContentSuggestionsManager(ctx.getUserId(), service);
            }
        });
        registerService(Context.VR_SERVICE, VrManager.class, new CachedServiceFetcher<VrManager>() { // from class: android.app.SystemServiceRegistry.107
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public VrManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.VR_SERVICE);
                return new VrManager(IVrManager.Stub.asInterface(b));
            }
        });
        registerService(Context.TIME_ZONE_RULES_MANAGER_SERVICE, RulesManager.class, new CachedServiceFetcher<RulesManager>() { // from class: android.app.SystemServiceRegistry.108
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public RulesManager mo332createService(ContextImpl ctx) {
                return new RulesManager(ctx.getOuterContext());
            }
        });
        registerService(Context.CROSS_PROFILE_APPS_SERVICE, CrossProfileApps.class, new CachedServiceFetcher<CrossProfileApps>() { // from class: android.app.SystemServiceRegistry.109
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public CrossProfileApps mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.CROSS_PROFILE_APPS_SERVICE);
                return new CrossProfileApps(ctx.getOuterContext(), ICrossProfileApps.Stub.asInterface(b));
            }
        });
        registerService("slice", SliceManager.class, new CachedServiceFetcher<SliceManager>() { // from class: android.app.SystemServiceRegistry.110
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public SliceManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new SliceManager(ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        });
        registerService("time_detector", TimeDetector.class, new CachedServiceFetcher<TimeDetector>() { // from class: android.app.SystemServiceRegistry.111
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public TimeDetector mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new TimeDetectorImpl();
            }
        });
        registerService("time_zone_detector", TimeZoneDetector.class, new CachedServiceFetcher<TimeZoneDetector>() { // from class: android.app.SystemServiceRegistry.112
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public TimeZoneDetector mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new TimeZoneDetectorImpl();
            }
        });
        registerService(Context.TIME_MANAGER, TimeManager.class, new CachedServiceFetcher<TimeManager>() { // from class: android.app.SystemServiceRegistry.113
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public TimeManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new TimeManager();
            }
        });
        registerService("permission", PermissionManager.class, new CachedServiceFetcher<PermissionManager>() { // from class: android.app.SystemServiceRegistry.114
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public PermissionManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new PermissionManager(ctx.getOuterContext());
            }
        });
        registerService(Context.LEGACY_PERMISSION_SERVICE, LegacyPermissionManager.class, new CachedServiceFetcher<LegacyPermissionManager>() { // from class: android.app.SystemServiceRegistry.115
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public LegacyPermissionManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new LegacyPermissionManager();
            }
        });
        registerService(Context.PERMISSION_CONTROLLER_SERVICE, PermissionControllerManager.class, new CachedServiceFetcher<PermissionControllerManager>() { // from class: android.app.SystemServiceRegistry.116
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public PermissionControllerManager mo332createService(ContextImpl ctx) {
                return new PermissionControllerManager(ctx.getOuterContext(), ctx.getMainThreadHandler());
            }
        });
        registerService(Context.PERMISSION_CHECKER_SERVICE, PermissionCheckerManager.class, new CachedServiceFetcher<PermissionCheckerManager>() { // from class: android.app.SystemServiceRegistry.117
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public PermissionCheckerManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new PermissionCheckerManager(ctx.getOuterContext());
            }
        });
        registerService(Context.DYNAMIC_SYSTEM_SERVICE, DynamicSystemManager.class, new CachedServiceFetcher<DynamicSystemManager>() { // from class: android.app.SystemServiceRegistry.118
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public DynamicSystemManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.DYNAMIC_SYSTEM_SERVICE);
                return new DynamicSystemManager(IDynamicSystemService.Stub.asInterface(b));
            }
        });
        registerService("batterystats", BatteryStatsManager.class, new CachedServiceFetcher<BatteryStatsManager>() { // from class: android.app.SystemServiceRegistry.119
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public BatteryStatsManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow("batterystats");
                return new BatteryStatsManager(IBatteryStats.Stub.asInterface(b));
            }
        });
        registerService(Context.DATA_LOADER_MANAGER_SERVICE, DataLoaderManager.class, new CachedServiceFetcher<DataLoaderManager>() { // from class: android.app.SystemServiceRegistry.120
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public DataLoaderManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.DATA_LOADER_MANAGER_SERVICE);
                return new DataLoaderManager(IDataLoaderManager.Stub.asInterface(b));
            }
        });
        registerService(Context.LIGHTS_SERVICE, LightsManager.class, new CachedServiceFetcher<LightsManager>() { // from class: android.app.SystemServiceRegistry.121
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public LightsManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new SystemLightsManager(ctx);
            }
        });
        registerService(Context.INCREMENTAL_SERVICE, IncrementalManager.class, new CachedServiceFetcher<IncrementalManager>() { // from class: android.app.SystemServiceRegistry.122
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public IncrementalManager mo332createService(ContextImpl ctx) {
                IBinder b = ServiceManager.getService(Context.INCREMENTAL_SERVICE);
                if (b == null) {
                    return null;
                }
                return new IncrementalManager(IIncrementalService.Stub.asInterface(b));
            }
        });
        registerService(Context.FILE_INTEGRITY_SERVICE, FileIntegrityManager.class, new CachedServiceFetcher<FileIntegrityManager>() { // from class: android.app.SystemServiceRegistry.123
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public FileIntegrityManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.FILE_INTEGRITY_SERVICE);
                return new FileIntegrityManager(ctx.getOuterContext(), IFileIntegrityService.Stub.asInterface(b));
            }
        });
        registerService(Context.APP_INTEGRITY_SERVICE, AppIntegrityManager.class, new CachedServiceFetcher<AppIntegrityManager>() { // from class: android.app.SystemServiceRegistry.124
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public AppIntegrityManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.APP_INTEGRITY_SERVICE);
                return new AppIntegrityManager(IAppIntegrityManager.Stub.asInterface(b));
            }
        });
        registerService("app_hibernation", AppHibernationManager.class, new CachedServiceFetcher<AppHibernationManager>() { // from class: android.app.SystemServiceRegistry.125
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public AppHibernationManager mo332createService(ContextImpl ctx) {
                IBinder b = ServiceManager.getService("app_hibernation");
                if (b == null) {
                    return null;
                }
                return new AppHibernationManager(ctx);
            }
        });
        registerService(Context.DREAM_SERVICE, DreamManager.class, new CachedServiceFetcher<DreamManager>() { // from class: android.app.SystemServiceRegistry.126
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public DreamManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new DreamManager(ctx);
            }
        });
        registerService(Context.DEVICE_STATE_SERVICE, DeviceStateManager.class, new CachedServiceFetcher<DeviceStateManager>() { // from class: android.app.SystemServiceRegistry.127
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public DeviceStateManager mo332createService(ContextImpl ctx) {
                return new DeviceStateManager();
            }
        });
        registerService(Context.MEDIA_METRICS_SERVICE, MediaMetricsManager.class, new CachedServiceFetcher<MediaMetricsManager>() { // from class: android.app.SystemServiceRegistry.128
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public MediaMetricsManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder iBinder = ServiceManager.getServiceOrThrow(Context.MEDIA_METRICS_SERVICE);
                IMediaMetricsManager service = IMediaMetricsManager.Stub.asInterface(iBinder);
                return new MediaMetricsManager(service, ctx.getUserId());
            }
        });
        registerService(Context.GAME_SERVICE, GameManager.class, new CachedServiceFetcher<GameManager>() { // from class: android.app.SystemServiceRegistry.129
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public GameManager mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new GameManager(ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        });
        registerService(Context.DOMAIN_VERIFICATION_SERVICE, DomainVerificationManager.class, new CachedServiceFetcher<DomainVerificationManager>() { // from class: android.app.SystemServiceRegistry.130
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public DomainVerificationManager mo332createService(ContextImpl context) throws ServiceManager.ServiceNotFoundException {
                IBinder binder = ServiceManager.getServiceOrThrow(Context.DOMAIN_VERIFICATION_SERVICE);
                IDomainVerificationManager service = IDomainVerificationManager.Stub.asInterface(binder);
                return new DomainVerificationManager(context, service);
            }
        });
        registerService(Context.DISPLAY_HASH_SERVICE, DisplayHashManager.class, new CachedServiceFetcher<DisplayHashManager>() { // from class: android.app.SystemServiceRegistry.131
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public DisplayHashManager mo332createService(ContextImpl ctx) {
                return new DisplayHashManager();
            }
        });
        sInitializing = true;
        try {
            ConnectivityFrameworkInitializer.registerServiceWrappers();
            JobSchedulerFrameworkInitializer.registerServiceWrappers();
            BlobStoreManagerFrameworkInitializer.initialize();
            TelephonyFrameworkInitializer.registerServiceWrappers();
            AppSearchManagerFrameworkInitializer.initialize();
            WifiFrameworkInitializer.registerServiceWrappers();
            StatsFrameworkInitializer.registerServiceWrappers();
            RollbackManagerFrameworkInitializer.initialize();
            MediaFrameworkPlatformInitializer.registerServiceWrappers();
            MediaFrameworkInitializer.registerServiceWrappers();
            RoleFrameworkInitializer.registerServiceWrappers();
            SchedulingFrameworkInitializer.registerServiceWrappers();
        } finally {
            sInitializing = false;
        }
    }

    private SystemServiceRegistry() {
    }

    /* renamed from: android.app.SystemServiceRegistry$17  reason: invalid class name */
    /* loaded from: classes.dex */
    class AnonymousClass17 extends CachedServiceFetcher<TetheringManager> {
        AnonymousClass17() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
        /* renamed from: createService */
        public TetheringManager mo332createService(ContextImpl ctx) {
            return new TetheringManager(ctx, SystemServiceRegistry$17$$ExternalSyntheticLambda0.INSTANCE);
        }
    }

    private static void ensureInitializing(String methodName) {
        Preconditions.checkState(sInitializing, "Internal error: %s can only be called during class initialization.", methodName);
    }

    public static Object[] createServiceCache() {
        return new Object[sServiceCacheSize];
    }

    public static Object getSystemService(ContextImpl ctx, String name) {
        if (name == null) {
            return null;
        }
        ServiceFetcher<?> fetcher = SYSTEM_SERVICE_FETCHERS.get(name);
        if (fetcher == null) {
            if (sEnableServiceNotFoundWtf) {
                Slog.wtf(TAG, "Unknown manager requested: " + name);
            }
            return null;
        }
        Object ret = fetcher.mo257getService(ctx);
        if (sEnableServiceNotFoundWtf && ret == null) {
            char c = 65535;
            switch (name.hashCode()) {
                case -1419358249:
                    if (name.equals(Context.ETHERNET_SERVICE)) {
                        c = 3;
                        break;
                    }
                    break;
                case -769002131:
                    if (name.equals(Context.APP_PREDICTION_SERVICE)) {
                        c = 1;
                        break;
                    }
                    break;
                case 974854528:
                    if (name.equals("content_capture")) {
                        c = 0;
                        break;
                    }
                    break;
                case 1085372378:
                    if (name.equals(Context.INCREMENTAL_SERVICE)) {
                        c = 2;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                case 1:
                case 2:
                case 3:
                    return null;
                default:
                    Slog.wtf(TAG, "Manager wrapper not available: " + name);
                    return null;
            }
        }
        return ret;
    }

    public static String getSystemServiceName(Class<?> serviceClass) {
        if (serviceClass == null) {
            return null;
        }
        String serviceName = SYSTEM_SERVICE_NAMES.get(serviceClass);
        if (sEnableServiceNotFoundWtf && serviceName == null) {
            Slog.wtf(TAG, "Unknown manager requested: " + serviceClass.getCanonicalName());
        }
        return serviceName;
    }

    private static <T> void registerService(String serviceName, Class<T> serviceClass, ServiceFetcher<T> serviceFetcher) {
        SYSTEM_SERVICE_NAMES.put(serviceClass, serviceName);
        SYSTEM_SERVICE_FETCHERS.put(serviceName, serviceFetcher);
        SYSTEM_SERVICE_CLASS_NAMES.put(serviceName, serviceClass.getSimpleName());
    }

    public static String getSystemServiceClassName(String name) {
        return SYSTEM_SERVICE_CLASS_NAMES.get(name);
    }

    @SystemApi
    public static <TServiceClass> void registerStaticService(final String serviceName, Class<TServiceClass> serviceWrapperClass, final StaticServiceProducerWithBinder<TServiceClass> serviceProducer) {
        ensureInitializing("registerStaticService");
        Preconditions.checkStringNotEmpty(serviceName);
        Objects.requireNonNull(serviceWrapperClass);
        Objects.requireNonNull(serviceProducer);
        registerService(serviceName, serviceWrapperClass, new StaticServiceFetcher<TServiceClass>() { // from class: android.app.SystemServiceRegistry.132
            /* JADX WARN: Type inference failed for: r0v1, types: [TServiceClass, java.lang.Object] */
            @Override // android.app.SystemServiceRegistry.StaticServiceFetcher
            /* renamed from: createService */
            public TServiceClass mo316createService() throws ServiceManager.ServiceNotFoundException {
                return StaticServiceProducerWithBinder.this.createService(ServiceManager.getServiceOrThrow(serviceName));
            }
        });
    }

    @SystemApi
    public static <TServiceClass> void registerStaticService(String serviceName, Class<TServiceClass> serviceWrapperClass, final StaticServiceProducerWithoutBinder<TServiceClass> serviceProducer) {
        ensureInitializing("registerStaticService");
        Preconditions.checkStringNotEmpty(serviceName);
        Objects.requireNonNull(serviceWrapperClass);
        Objects.requireNonNull(serviceProducer);
        registerService(serviceName, serviceWrapperClass, new StaticServiceFetcher<TServiceClass>() { // from class: android.app.SystemServiceRegistry.133
            /* JADX WARN: Type inference failed for: r0v1, types: [TServiceClass, java.lang.Object] */
            @Override // android.app.SystemServiceRegistry.StaticServiceFetcher
            /* renamed from: createService */
            public TServiceClass mo316createService() {
                return StaticServiceProducerWithoutBinder.this.createService();
            }
        });
    }

    @SystemApi
    public static <TServiceClass> void registerContextAwareService(final String serviceName, Class<TServiceClass> serviceWrapperClass, final ContextAwareServiceProducerWithBinder<TServiceClass> serviceProducer) {
        ensureInitializing("registerContextAwareService");
        Preconditions.checkStringNotEmpty(serviceName);
        Objects.requireNonNull(serviceWrapperClass);
        Objects.requireNonNull(serviceProducer);
        registerService(serviceName, serviceWrapperClass, new CachedServiceFetcher<TServiceClass>() { // from class: android.app.SystemServiceRegistry.134
            /* JADX WARN: Type inference failed for: r0v1, types: [TServiceClass, java.lang.Object] */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public TServiceClass mo332createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return ContextAwareServiceProducerWithBinder.this.createService(ctx.getOuterContext(), ServiceManager.getServiceOrThrow(serviceName));
            }
        });
    }

    @SystemApi
    public static <TServiceClass> void registerContextAwareService(String serviceName, Class<TServiceClass> serviceWrapperClass, final ContextAwareServiceProducerWithoutBinder<TServiceClass> serviceProducer) {
        ensureInitializing("registerContextAwareService");
        Preconditions.checkStringNotEmpty(serviceName);
        Objects.requireNonNull(serviceWrapperClass);
        Objects.requireNonNull(serviceProducer);
        registerService(serviceName, serviceWrapperClass, new CachedServiceFetcher<TServiceClass>() { // from class: android.app.SystemServiceRegistry.135
            /* JADX WARN: Type inference failed for: r0v1, types: [TServiceClass, java.lang.Object] */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            /* renamed from: createService */
            public TServiceClass mo332createService(ContextImpl ctx) {
                return ContextAwareServiceProducerWithoutBinder.this.createService(ctx.getOuterContext());
            }
        });
    }

    /* loaded from: classes.dex */
    static abstract class CachedServiceFetcher<T> implements ServiceFetcher<T> {
        private final int mCacheIndex = SystemServiceRegistry.access$008();

        /* renamed from: createService */
        public abstract T mo332createService(ContextImpl contextImpl) throws ServiceManager.ServiceNotFoundException;

        CachedServiceFetcher() {
        }

        /* JADX WARN: Code restructure failed: missing block: B:78:0x000e, code lost:
            r3 = r6;
         */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.Object[], java.lang.Object] */
        /* JADX WARN: Type inference failed for: r6v0 */
        @Override // android.app.SystemServiceRegistry.ServiceFetcher
        /* renamed from: getService */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final T mo257getService(ContextImpl ctx) {
            T ret;
            ?? r0 = ctx.mServiceCache;
            int[] gates = ctx.mServiceInitializationStateArray;
            boolean interrupted = false;
            while (true) {
                boolean doInitialize = false;
                synchronized (r0) {
                    int i = this.mCacheIndex;
                    ?? r6 = r0[i];
                    if (r6 != 0) {
                        break;
                    }
                    if (gates[i] == 2 || gates[i] == 3) {
                        gates[i] = 0;
                    }
                    if (gates[i] == 0) {
                        doInitialize = true;
                        gates[i] = 1;
                    }
                    if (doInitialize) {
                        T service = null;
                        int newState = 3;
                        try {
                            try {
                                T service2 = mo332createService(ctx);
                                synchronized (r0) {
                                    int i2 = this.mCacheIndex;
                                    r0[i2] = service2;
                                    gates[i2] = 2;
                                    r0.notifyAll();
                                }
                                service = service2;
                                newState = 2;
                            } catch (Throwable th) {
                                synchronized (r0) {
                                    int i3 = this.mCacheIndex;
                                    r0[i3] = service;
                                    gates[i3] = newState;
                                    r0.notifyAll();
                                    throw th;
                                }
                            }
                        } catch (ServiceManager.ServiceNotFoundException e) {
                            SystemServiceRegistry.onServiceNotFound(e);
                            synchronized (r0) {
                                r0[this.mCacheIndex] = 0;
                                gates[this.mCacheIndex] = 3;
                                r0.notifyAll();
                            }
                        }
                        ret = service;
                        break;
                    }
                    synchronized (r0) {
                        while (gates[this.mCacheIndex] < 2) {
                            try {
                                interrupted |= Thread.interrupted();
                                r0.wait();
                            } catch (InterruptedException e2) {
                                Slog.w(SystemServiceRegistry.TAG, "getService() interrupted");
                                interrupted = true;
                            }
                        }
                    }
                }
            }
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
            return ret;
        }
    }

    /* loaded from: classes.dex */
    static abstract class StaticServiceFetcher<T> implements ServiceFetcher<T> {
        private T mCachedInstance;

        /* renamed from: createService */
        public abstract T mo316createService() throws ServiceManager.ServiceNotFoundException;

        StaticServiceFetcher() {
        }

        @Override // android.app.SystemServiceRegistry.ServiceFetcher
        /* renamed from: getService */
        public final T mo257getService(ContextImpl ctx) {
            T t;
            synchronized (this) {
                if (this.mCachedInstance == null) {
                    try {
                        this.mCachedInstance = mo316createService();
                    } catch (ServiceManager.ServiceNotFoundException e) {
                        SystemServiceRegistry.onServiceNotFound(e);
                    }
                }
                t = this.mCachedInstance;
            }
            return t;
        }
    }

    /* loaded from: classes.dex */
    static abstract class StaticApplicationContextServiceFetcher<T> implements ServiceFetcher<T> {
        private T mCachedInstance;

        public abstract T createService(Context context) throws ServiceManager.ServiceNotFoundException;

        StaticApplicationContextServiceFetcher() {
        }

        @Override // android.app.SystemServiceRegistry.ServiceFetcher
        /* renamed from: getService */
        public final T mo257getService(ContextImpl ctx) {
            T t;
            synchronized (this) {
                if (this.mCachedInstance == null) {
                    Context appContext = ctx.getApplicationContext();
                    try {
                        this.mCachedInstance = createService(appContext != null ? appContext : ctx);
                    } catch (ServiceManager.ServiceNotFoundException e) {
                        SystemServiceRegistry.onServiceNotFound(e);
                    }
                }
                t = this.mCachedInstance;
            }
            return t;
        }
    }

    public static void onServiceNotFound(ServiceManager.ServiceNotFoundException e) {
        if (Process.myUid() < 10000) {
            Log.wtf(TAG, e.getMessage(), e);
        } else {
            Log.w(TAG, e.getMessage());
        }
    }
}
