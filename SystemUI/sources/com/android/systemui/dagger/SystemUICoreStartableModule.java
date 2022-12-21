package com.android.systemui.dagger;

import com.android.keyguard.KeyguardBiometricLockoutLogger;
import com.android.systemui.CoreStartable;
import com.android.systemui.LatencyTester;
import com.android.systemui.ScreenDecorations;
import com.android.systemui.SliceBroadcastRelayHandler;
import com.android.systemui.accessibility.SystemActions;
import com.android.systemui.accessibility.WindowMagnification;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.clipboardoverlay.ClipboardListener;
import com.android.systemui.dagger.qualifiers.PerUser;
import com.android.systemui.globalactions.GlobalActionsComponent;
import com.android.systemui.keyboard.KeyboardUI;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.log.SessionTracker;
import com.android.systemui.media.RingtonePlayer;
import com.android.systemui.power.PowerUI;
import com.android.systemui.recents.Recents;
import com.android.systemui.shortcut.ShortcutKeyDispatcher;
import com.android.systemui.statusbar.notification.InstantAppNotifier;
import com.android.systemui.statusbar.phone.KeyguardLiftController;
import com.android.systemui.theme.ThemeOverlayController;
import com.android.systemui.toast.ToastUI;
import com.android.systemui.usb.StorageNotification;
import com.android.systemui.util.NotificationChannels;
import com.android.systemui.util.leak.GarbageMonitor;
import com.android.systemui.volume.VolumeUI;
import com.android.systemui.wmshell.WMShell;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000ª\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b'\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H'J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tH'J\u0010\u0010\n\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u000bH'J\u0010\u0010\f\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\rH'J\u0010\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u000fH'J\u0010\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0011H'J\u0010\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0013H'J\u0010\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0015H'J\u0010\u0010\u0016\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0017H'J\u0010\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0019H'J\u0010\u0010\u001a\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u001bH'J\u0010\u0010\u001c\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u001dH'J\u0010\u0010\u001e\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u001fH'J\u0010\u0010 \u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020!H'J\u0010\u0010\"\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020#H'J\u0010\u0010$\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020%H'J\u0010\u0010&\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020'H'J\u0010\u0010(\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020)H'J\u0010\u0010*\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020+H'J\u0010\u0010,\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020-H'J\u0010\u0010.\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020/H'J\u0010\u00100\u001a\u00020\u00042\u0006\u0010\b\u001a\u000201H'J\u0010\u00102\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u000203H'J\u0010\u00104\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u000205H'J\u0010\u00106\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u000207H'¨\u00068"}, mo64987d2 = {"Lcom/android/systemui/dagger/SystemUICoreStartableModule;", "", "()V", "bind", "Lcom/android/systemui/CoreStartable;", "sysui", "Lcom/android/systemui/media/RingtonePlayer;", "bindAuthController", "service", "Lcom/android/systemui/biometrics/AuthController;", "bindClipboardListener", "Lcom/android/systemui/clipboardoverlay/ClipboardListener;", "bindGarbageMonitorService", "Lcom/android/systemui/util/leak/GarbageMonitor$Service;", "bindGlobalActionsComponent", "Lcom/android/systemui/globalactions/GlobalActionsComponent;", "bindInstantAppNotifier", "Lcom/android/systemui/statusbar/notification/InstantAppNotifier;", "bindKeyboardUI", "Lcom/android/systemui/keyboard/KeyboardUI;", "bindKeyguardBiometricLockoutLogger", "Lcom/android/keyguard/KeyguardBiometricLockoutLogger;", "bindKeyguardLiftController", "Lcom/android/systemui/statusbar/phone/KeyguardLiftController;", "bindKeyguardViewMediator", "Lcom/android/systemui/keyguard/KeyguardViewMediator;", "bindLatencyTester", "Lcom/android/systemui/LatencyTester;", "bindNotificationChannels", "Lcom/android/systemui/util/NotificationChannels;", "bindPowerUI", "Lcom/android/systemui/power/PowerUI;", "bindRecents", "Lcom/android/systemui/recents/Recents;", "bindScreenDecorations", "Lcom/android/systemui/ScreenDecorations;", "bindSessionTracker", "Lcom/android/systemui/log/SessionTracker;", "bindShortcutKeyDispatcher", "Lcom/android/systemui/shortcut/ShortcutKeyDispatcher;", "bindSliceBroadcastRelayHandler", "Lcom/android/systemui/SliceBroadcastRelayHandler;", "bindStorageNotification", "Lcom/android/systemui/usb/StorageNotification;", "bindSystemActions", "Lcom/android/systemui/accessibility/SystemActions;", "bindThemeOverlayController", "Lcom/android/systemui/theme/ThemeOverlayController;", "bindToastUI", "Lcom/android/systemui/toast/ToastUI;", "bindVolumeUI", "Lcom/android/systemui/volume/VolumeUI;", "bindWMShell", "Lcom/android/systemui/wmshell/WMShell;", "bindWindowMagnification", "Lcom/android/systemui/accessibility/WindowMagnification;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
@Module
/* compiled from: SystemUICoreStartableModule.kt */
public abstract class SystemUICoreStartableModule {
    @IntoMap
    @ClassKey(RingtonePlayer.class)
    @Binds
    public abstract CoreStartable bind(RingtonePlayer ringtonePlayer);

    @IntoMap
    @ClassKey(AuthController.class)
    @Binds
    public abstract CoreStartable bindAuthController(AuthController authController);

    @IntoMap
    @ClassKey(ClipboardListener.class)
    @Binds
    public abstract CoreStartable bindClipboardListener(ClipboardListener clipboardListener);

    @IntoMap
    @ClassKey(GarbageMonitor.class)
    @Binds
    public abstract CoreStartable bindGarbageMonitorService(GarbageMonitor.Service service);

    @IntoMap
    @ClassKey(GlobalActionsComponent.class)
    @Binds
    public abstract CoreStartable bindGlobalActionsComponent(GlobalActionsComponent globalActionsComponent);

    @IntoMap
    @ClassKey(InstantAppNotifier.class)
    @Binds
    public abstract CoreStartable bindInstantAppNotifier(InstantAppNotifier instantAppNotifier);

    @IntoMap
    @ClassKey(KeyboardUI.class)
    @Binds
    public abstract CoreStartable bindKeyboardUI(KeyboardUI keyboardUI);

    @IntoMap
    @ClassKey(KeyguardBiometricLockoutLogger.class)
    @Binds
    public abstract CoreStartable bindKeyguardBiometricLockoutLogger(KeyguardBiometricLockoutLogger keyguardBiometricLockoutLogger);

    @IntoMap
    @ClassKey(KeyguardLiftController.class)
    @Binds
    public abstract CoreStartable bindKeyguardLiftController(KeyguardLiftController keyguardLiftController);

    @IntoMap
    @ClassKey(KeyguardViewMediator.class)
    @Binds
    public abstract CoreStartable bindKeyguardViewMediator(KeyguardViewMediator keyguardViewMediator);

    @IntoMap
    @ClassKey(LatencyTester.class)
    @Binds
    public abstract CoreStartable bindLatencyTester(LatencyTester latencyTester);

    @Binds
    @PerUser
    @IntoMap
    @ClassKey(NotificationChannels.class)
    public abstract CoreStartable bindNotificationChannels(NotificationChannels notificationChannels);

    @IntoMap
    @ClassKey(PowerUI.class)
    @Binds
    public abstract CoreStartable bindPowerUI(PowerUI powerUI);

    @IntoMap
    @ClassKey(Recents.class)
    @Binds
    public abstract CoreStartable bindRecents(Recents recents);

    @IntoMap
    @ClassKey(ScreenDecorations.class)
    @Binds
    public abstract CoreStartable bindScreenDecorations(ScreenDecorations screenDecorations);

    @IntoMap
    @ClassKey(SessionTracker.class)
    @Binds
    public abstract CoreStartable bindSessionTracker(SessionTracker sessionTracker);

    @IntoMap
    @ClassKey(ShortcutKeyDispatcher.class)
    @Binds
    public abstract CoreStartable bindShortcutKeyDispatcher(ShortcutKeyDispatcher shortcutKeyDispatcher);

    @IntoMap
    @ClassKey(SliceBroadcastRelayHandler.class)
    @Binds
    public abstract CoreStartable bindSliceBroadcastRelayHandler(SliceBroadcastRelayHandler sliceBroadcastRelayHandler);

    @IntoMap
    @ClassKey(StorageNotification.class)
    @Binds
    public abstract CoreStartable bindStorageNotification(StorageNotification storageNotification);

    @IntoMap
    @ClassKey(SystemActions.class)
    @Binds
    public abstract CoreStartable bindSystemActions(SystemActions systemActions);

    @IntoMap
    @ClassKey(ThemeOverlayController.class)
    @Binds
    public abstract CoreStartable bindThemeOverlayController(ThemeOverlayController themeOverlayController);

    @IntoMap
    @ClassKey(ToastUI.class)
    @Binds
    public abstract CoreStartable bindToastUI(ToastUI toastUI);

    @IntoMap
    @ClassKey(VolumeUI.class)
    @Binds
    public abstract CoreStartable bindVolumeUI(VolumeUI volumeUI);

    @IntoMap
    @ClassKey(WMShell.class)
    @Binds
    public abstract CoreStartable bindWMShell(WMShell wMShell);

    @IntoMap
    @ClassKey(WindowMagnification.class)
    @Binds
    public abstract CoreStartable bindWindowMagnification(WindowMagnification windowMagnification);
}
