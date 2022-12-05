package com.android.systemui.dagger;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.AlarmManager;
import android.app.IActivityManager;
import android.app.IActivityTaskManager;
import android.app.IWallpaperManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.WallpaperManager;
import android.app.admin.DevicePolicyManager;
import android.app.smartspace.SmartspaceManager;
import android.app.trust.TrustManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.om.OverlayManager;
import android.content.pm.IPackageManager;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutManager;
import android.content.res.Resources;
import android.hardware.SensorManager;
import android.hardware.SensorPrivacyManager;
import android.hardware.display.ColorDisplayManager;
import android.hardware.display.DisplayManager;
import android.hardware.face.FaceManager;
import android.hardware.fingerprint.FingerprintManager;
import android.media.AudioManager;
import android.media.IAudioService;
import android.media.MediaRouter2Manager;
import android.media.session.MediaSessionManager;
import android.net.ConnectivityManager;
import android.net.NetworkScoreManager;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.os.ServiceManager;
import android.os.UserManager;
import android.os.Vibrator;
import android.permission.PermissionManager;
import android.service.dreams.IDreamManager;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.view.CrossWindowBlurListeners;
import android.view.IWindowManager;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.app.IBatteryStats;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.util.LatencyTracker;
import com.android.systemui.shared.system.PackageManagerWrapper;
import java.util.Optional;
/* loaded from: classes.dex */
public class FrameworkServicesModule {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static AccessibilityManager provideAccessibilityManager(Context context) {
        return (AccessibilityManager) context.getSystemService(AccessibilityManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ActivityManager provideActivityManager(Context context) {
        return (ActivityManager) context.getSystemService(ActivityManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AlarmManager provideAlarmManager(Context context) {
        return (AlarmManager) context.getSystemService(AlarmManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AudioManager provideAudioManager(Context context) {
        return (AudioManager) context.getSystemService(AudioManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ColorDisplayManager provideColorDisplayManager(Context context) {
        return (ColorDisplayManager) context.getSystemService(ColorDisplayManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ConnectivityManager provideConnectivityManagager(Context context) {
        return (ConnectivityManager) context.getSystemService(ConnectivityManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ContentResolver provideContentResolver(Context context) {
        return context.getContentResolver();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static DevicePolicyManager provideDevicePolicyManager(Context context) {
        return (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CrossWindowBlurListeners provideCrossWindowBlurListeners() {
        return CrossWindowBlurListeners.getInstance();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int provideDisplayId(Context context) {
        return context.getDisplayId();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static DisplayManager provideDisplayManager(Context context) {
        return (DisplayManager) context.getSystemService(DisplayManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static IActivityManager provideIActivityManager() {
        return ActivityManager.getService();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ActivityTaskManager provideActivityTaskManager() {
        return ActivityTaskManager.getInstance();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static IActivityTaskManager provideIActivityTaskManager() {
        return ActivityTaskManager.getService();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static IAudioService provideIAudioService() {
        return IAudioService.Stub.asInterface(ServiceManager.getService("audio"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static IBatteryStats provideIBatteryStats() {
        return IBatteryStats.Stub.asInterface(ServiceManager.getService("batterystats"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static IDreamManager provideIDreamManager() {
        return IDreamManager.Stub.asInterface(ServiceManager.checkService("dreams"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static FaceManager provideFaceManager(Context context) {
        return (FaceManager) context.getSystemService(FaceManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static FingerprintManager providesFingerprintManager(Context context) {
        return (FingerprintManager) context.getSystemService(FingerprintManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static InputMethodManager provideInputMethodManager(Context context) {
        return (InputMethodManager) context.getSystemService(InputMethodManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static IPackageManager provideIPackageManager() {
        return IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static IStatusBarService provideIStatusBarService() {
        return IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static IWallpaperManager provideIWallPaperManager() {
        return IWallpaperManager.Stub.asInterface(ServiceManager.getService("wallpaper"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static IWindowManager provideIWindowManager() {
        return WindowManagerGlobal.getWindowManagerService();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static KeyguardManager provideKeyguardManager(Context context) {
        return (KeyguardManager) context.getSystemService(KeyguardManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static LatencyTracker provideLatencyTracker(Context context) {
        return LatencyTracker.getInstance(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static LauncherApps provideLauncherApps(Context context) {
        return (LauncherApps) context.getSystemService(LauncherApps.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static MediaRouter2Manager provideMediaRouter2Manager(Context context) {
        return MediaRouter2Manager.getInstance(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static MediaSessionManager provideMediaSessionManager(Context context) {
        return (MediaSessionManager) context.getSystemService(MediaSessionManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static NetworkScoreManager provideNetworkScoreManager(Context context) {
        return (NetworkScoreManager) context.getSystemService(NetworkScoreManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static NotificationManager provideNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(NotificationManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static PackageManager providePackageManager(Context context) {
        return context.getPackageManager();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static PackageManagerWrapper providePackageManagerWrapper() {
        return PackageManagerWrapper.getInstance();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static PowerManager providePowerManager(Context context) {
        return (PowerManager) context.getSystemService(PowerManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Resources provideResources(Context context) {
        return context.getResources();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SensorManager providesSensorManager(Context context) {
        return (SensorManager) context.getSystemService(SensorManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SensorPrivacyManager provideSensorPrivacyManager(Context context) {
        return (SensorPrivacyManager) context.getSystemService(SensorPrivacyManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ShortcutManager provideShortcutManager(Context context) {
        return (ShortcutManager) context.getSystemService(ShortcutManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SubscriptionManager provideSubcriptionManager(Context context) {
        return (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static TelecomManager provideTelecomManager(Context context) {
        return (TelecomManager) context.getSystemService(TelecomManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static TelephonyManager provideTelephonyManager(Context context) {
        return (TelephonyManager) context.getSystemService(TelephonyManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static TrustManager provideTrustManager(Context context) {
        return (TrustManager) context.getSystemService(TrustManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Vibrator provideVibrator(Context context) {
        return (Vibrator) context.getSystemService(Vibrator.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Optional<Vibrator> provideOptionalVibrator(Context context) {
        return Optional.ofNullable((Vibrator) context.getSystemService(Vibrator.class));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ViewConfiguration provideViewConfiguration(Context context) {
        return ViewConfiguration.get(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static UserManager provideUserManager(Context context) {
        return (UserManager) context.getSystemService(UserManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static WallpaperManager provideWallpaperManager(Context context) {
        return (WallpaperManager) context.getSystemService(WallpaperManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static WifiManager provideWifiManager(Context context) {
        return (WifiManager) context.getSystemService(WifiManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static OverlayManager provideOverlayManager(Context context) {
        return (OverlayManager) context.getSystemService(OverlayManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static WindowManager provideWindowManager(Context context) {
        return (WindowManager) context.getSystemService(WindowManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static PermissionManager providePermissionManager(Context context) {
        return (PermissionManager) context.getSystemService(PermissionManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SmartspaceManager provideSmartspaceManager(Context context) {
        return (SmartspaceManager) context.getSystemService(SmartspaceManager.class);
    }
}
