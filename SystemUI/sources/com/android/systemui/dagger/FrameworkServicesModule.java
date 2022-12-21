package com.android.systemui.dagger;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.AlarmManager;
import android.app.IActivityManager;
import android.app.IActivityTaskManager;
import android.app.INotificationManager;
import android.app.IWallpaperManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.StatsManager;
import android.app.UiModeManager;
import android.app.WallpaperManager;
import android.app.admin.DevicePolicyManager;
import android.app.role.RoleManager;
import android.app.smartspace.SmartspaceManager;
import android.app.trust.TrustManager;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.om.OverlayManager;
import android.content.pm.IPackageManager;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutManager;
import android.content.res.Resources;
import android.hardware.SensorManager;
import android.hardware.SensorPrivacyManager;
import android.hardware.devicestate.DeviceStateManager;
import android.hardware.display.AmbientDisplayConfiguration;
import android.hardware.display.ColorDisplayManager;
import android.hardware.display.DisplayManager;
import android.hardware.face.FaceManager;
import android.hardware.fingerprint.FingerprintManager;
import android.media.AudioManager;
import android.media.IAudioService;
import android.media.MediaRouter2Manager;
import android.media.projection.MediaProjectionManager;
import android.media.session.MediaSessionManager;
import android.net.ConnectivityManager;
import android.net.NetworkScoreManager;
import android.net.wifi.WifiManager;
import android.os.PowerExemptionManager;
import android.os.PowerManager;
import android.os.ServiceManager;
import android.os.UserManager;
import android.os.Vibrator;
import android.permission.PermissionManager;
import android.safetycenter.SafetyCenterManager;
import android.service.dreams.IDreamManager;
import android.telecom.TelecomManager;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.view.Choreographer;
import android.view.CrossWindowBlurListeners;
import android.view.IWindowManager;
import android.view.LayoutInflater;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.CaptioningManager;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.app.IBatteryStats;
import com.android.internal.appwidget.IAppWidgetService;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.util.LatencyTracker;
import com.android.systemui.Prefs;
import com.android.systemui.dagger.qualifiers.DisplayId;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dagger.qualifiers.TestHarness;
import com.android.systemui.shared.system.PackageManagerWrapper;
import dagger.Module;
import dagger.Provides;
import java.util.Optional;
import javax.inject.Singleton;

@Module
public class FrameworkServicesModule {
    @Singleton
    @Provides
    static AccessibilityManager provideAccessibilityManager(Context context) {
        return (AccessibilityManager) context.getSystemService(AccessibilityManager.class);
    }

    @Singleton
    @Provides
    static ActivityManager provideActivityManager(Context context) {
        return (ActivityManager) context.getSystemService(ActivityManager.class);
    }

    @Singleton
    @Provides
    static AlarmManager provideAlarmManager(Context context) {
        return (AlarmManager) context.getSystemService(AlarmManager.class);
    }

    @Provides
    public AmbientDisplayConfiguration provideAmbientDisplayConfiguration(Context context) {
        return new AmbientDisplayConfiguration(context);
    }

    @Singleton
    @Provides
    static AudioManager provideAudioManager(Context context) {
        return (AudioManager) context.getSystemService(AudioManager.class);
    }

    @Singleton
    @Provides
    static CaptioningManager provideCaptioningManager(Context context) {
        return (CaptioningManager) context.getSystemService(CaptioningManager.class);
    }

    @Singleton
    @Provides
    public Choreographer providesChoreographer() {
        return Choreographer.getInstance();
    }

    @Singleton
    @Provides
    static ColorDisplayManager provideColorDisplayManager(Context context) {
        return (ColorDisplayManager) context.getSystemService(ColorDisplayManager.class);
    }

    @Singleton
    @Provides
    static ConnectivityManager provideConnectivityManagager(Context context) {
        return (ConnectivityManager) context.getSystemService(ConnectivityManager.class);
    }

    @Singleton
    @Provides
    static ContentResolver provideContentResolver(Context context) {
        return context.getContentResolver();
    }

    @Singleton
    @Provides
    static DevicePolicyManager provideDevicePolicyManager(Context context) {
        return (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
    }

    @Singleton
    @Provides
    static CrossWindowBlurListeners provideCrossWindowBlurListeners() {
        return CrossWindowBlurListeners.getInstance();
    }

    @DisplayId
    @Provides
    static int provideDisplayId(Context context) {
        return context.getDisplayId();
    }

    @Singleton
    @Provides
    static DisplayManager provideDisplayManager(Context context) {
        return (DisplayManager) context.getSystemService(DisplayManager.class);
    }

    @Singleton
    @Provides
    static DeviceStateManager provideDeviceStateManager(Context context) {
        return (DeviceStateManager) context.getSystemService(DeviceStateManager.class);
    }

    @Singleton
    @Provides
    static IActivityManager provideIActivityManager() {
        return ActivityManager.getService();
    }

    @Singleton
    @Provides
    static ActivityTaskManager provideActivityTaskManager() {
        return ActivityTaskManager.getInstance();
    }

    @Singleton
    @Provides
    static IActivityTaskManager provideIActivityTaskManager() {
        return ActivityTaskManager.getService();
    }

    @Singleton
    @Provides
    static IAudioService provideIAudioService() {
        return IAudioService.Stub.asInterface(ServiceManager.getService("audio"));
    }

    @Singleton
    @Provides
    static IBatteryStats provideIBatteryStats() {
        return IBatteryStats.Stub.asInterface(ServiceManager.getService("batterystats"));
    }

    @Singleton
    @Provides
    static IDreamManager provideIDreamManager() {
        return IDreamManager.Stub.asInterface(ServiceManager.checkService("dreams"));
    }

    @Singleton
    @Provides
    static FaceManager provideFaceManager(Context context) {
        return (FaceManager) context.getSystemService(FaceManager.class);
    }

    @Singleton
    @Provides
    static FingerprintManager providesFingerprintManager(Context context) {
        return (FingerprintManager) context.getSystemService(FingerprintManager.class);
    }

    @Singleton
    @Provides
    static InteractionJankMonitor provideInteractionJankMonitor() {
        return InteractionJankMonitor.getInstance();
    }

    @Singleton
    @Provides
    static InputMethodManager provideInputMethodManager(Context context) {
        return (InputMethodManager) context.getSystemService(InputMethodManager.class);
    }

    @Singleton
    @Provides
    static IAppWidgetService provideIAppWidgetService() {
        return IAppWidgetService.Stub.asInterface(ServiceManager.getService("appwidget"));
    }

    @Singleton
    @Provides
    static IPackageManager provideIPackageManager() {
        return IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
    }

    @Singleton
    @Provides
    static IStatusBarService provideIStatusBarService() {
        return IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));
    }

    @Provides
    static IWallpaperManager provideIWallPaperManager() {
        return IWallpaperManager.Stub.asInterface(ServiceManager.getService("wallpaper"));
    }

    @Singleton
    @Provides
    static IWindowManager provideIWindowManager() {
        return WindowManagerGlobal.getWindowManagerService();
    }

    @Singleton
    @Provides
    static KeyguardManager provideKeyguardManager(Context context) {
        return (KeyguardManager) context.getSystemService(KeyguardManager.class);
    }

    @Singleton
    @Provides
    static LatencyTracker provideLatencyTracker(Context context) {
        return LatencyTracker.getInstance(context);
    }

    @Singleton
    @Provides
    static LauncherApps provideLauncherApps(Context context) {
        return (LauncherApps) context.getSystemService(LauncherApps.class);
    }

    @Singleton
    @Provides
    public LayoutInflater providerLayoutInflater(Context context) {
        return LayoutInflater.from(context);
    }

    @Provides
    static MediaProjectionManager provideMediaProjectionManager(Context context) {
        return (MediaProjectionManager) context.getSystemService(MediaProjectionManager.class);
    }

    @Provides
    static MediaRouter2Manager provideMediaRouter2Manager(Context context) {
        return MediaRouter2Manager.getInstance(context);
    }

    @Provides
    static MediaSessionManager provideMediaSessionManager(Context context) {
        return (MediaSessionManager) context.getSystemService(MediaSessionManager.class);
    }

    @Singleton
    @Provides
    static NetworkScoreManager provideNetworkScoreManager(Context context) {
        return (NetworkScoreManager) context.getSystemService(NetworkScoreManager.class);
    }

    @Singleton
    @Provides
    static NotificationManager provideNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(NotificationManager.class);
    }

    @Singleton
    @Provides
    public INotificationManager provideINotificationManager() {
        return INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
    }

    @Singleton
    @Provides
    static PackageManager providePackageManager(Context context) {
        return context.getPackageManager();
    }

    @Singleton
    @Provides
    static PackageManagerWrapper providePackageManagerWrapper() {
        return PackageManagerWrapper.getInstance();
    }

    @Singleton
    @Provides
    static PowerManager providePowerManager(Context context) {
        return (PowerManager) context.getSystemService(PowerManager.class);
    }

    @Singleton
    @Provides
    static PowerExemptionManager providePowerExemptionManager(Context context) {
        return (PowerExemptionManager) context.getSystemService(PowerExemptionManager.class);
    }

    @Main
    @Provides
    public SharedPreferences provideSharePreferences(Context context) {
        return Prefs.get(context);
    }

    @Singleton
    @Provides
    static UiModeManager provideUiModeManager(Context context) {
        return (UiModeManager) context.getSystemService(UiModeManager.class);
    }

    @Main
    @Provides
    static Resources provideResources(Context context) {
        return context.getResources();
    }

    @Singleton
    @Provides
    static RoleManager provideRoleManager(Context context) {
        return (RoleManager) context.getSystemService(RoleManager.class);
    }

    @Singleton
    @Provides
    static SensorManager providesSensorManager(Context context) {
        return (SensorManager) context.getSystemService(SensorManager.class);
    }

    @Singleton
    @Provides
    static SensorPrivacyManager provideSensorPrivacyManager(Context context) {
        return (SensorPrivacyManager) context.getSystemService(SensorPrivacyManager.class);
    }

    @Singleton
    @Provides
    static ShortcutManager provideShortcutManager(Context context) {
        return (ShortcutManager) context.getSystemService(ShortcutManager.class);
    }

    @Singleton
    @Provides
    static StatsManager provideStatsManager(Context context) {
        return (StatsManager) context.getSystemService(StatsManager.class);
    }

    @Singleton
    @Provides
    static SubscriptionManager provideSubcriptionManager(Context context) {
        return (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
    }

    @Singleton
    @Provides
    static TelecomManager provideTelecomManager(Context context) {
        return (TelecomManager) context.getSystemService(TelecomManager.class);
    }

    @Singleton
    @Provides
    static Optional<TelecomManager> provideOptionalTelecomManager(Context context) {
        return Optional.ofNullable((TelecomManager) context.getSystemService(TelecomManager.class));
    }

    @Singleton
    @Provides
    static TelephonyManager provideTelephonyManager(Context context) {
        return (TelephonyManager) context.getSystemService(TelephonyManager.class);
    }

    @Singleton
    @TestHarness
    @Provides
    static boolean provideIsTestHarness() {
        return ActivityManager.isRunningInUserTestHarness();
    }

    @Singleton
    @Provides
    static TrustManager provideTrustManager(Context context) {
        return (TrustManager) context.getSystemService(TrustManager.class);
    }

    @Singleton
    @Provides
    static Vibrator provideVibrator(Context context) {
        return (Vibrator) context.getSystemService(Vibrator.class);
    }

    @Singleton
    @Provides
    static Optional<Vibrator> provideOptionalVibrator(Context context) {
        return Optional.ofNullable((Vibrator) context.getSystemService(Vibrator.class));
    }

    @Singleton
    @Provides
    static ViewConfiguration provideViewConfiguration(Context context) {
        return ViewConfiguration.get(context);
    }

    @Singleton
    @Provides
    static UserManager provideUserManager(Context context) {
        return (UserManager) context.getSystemService(UserManager.class);
    }

    @Provides
    static WallpaperManager provideWallpaperManager(Context context) {
        return (WallpaperManager) context.getSystemService(WallpaperManager.class);
    }

    @Singleton
    @Provides
    static WifiManager provideWifiManager(Context context) {
        return (WifiManager) context.getSystemService(WifiManager.class);
    }

    @Singleton
    @Provides
    static OverlayManager provideOverlayManager(Context context) {
        return (OverlayManager) context.getSystemService(OverlayManager.class);
    }

    @Singleton
    @Provides
    static CarrierConfigManager provideCarrierConfigManager(Context context) {
        return (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
    }

    @Singleton
    @Provides
    static WindowManager provideWindowManager(Context context) {
        return (WindowManager) context.getSystemService(WindowManager.class);
    }

    @Singleton
    @Provides
    static PermissionManager providePermissionManager(Context context) {
        PermissionManager permissionManager = (PermissionManager) context.getSystemService(PermissionManager.class);
        if (permissionManager != null) {
            permissionManager.initializeUsageHelper();
        }
        return permissionManager;
    }

    @Singleton
    @Provides
    static ClipboardManager provideClipboardManager(Context context) {
        return (ClipboardManager) context.getSystemService(ClipboardManager.class);
    }

    @Singleton
    @Provides
    static SmartspaceManager provideSmartspaceManager(Context context) {
        return (SmartspaceManager) context.getSystemService(SmartspaceManager.class);
    }

    @Singleton
    @Provides
    static SafetyCenterManager provideSafetyCenterManager(Context context) {
        return (SafetyCenterManager) context.getSystemService(SafetyCenterManager.class);
    }
}
