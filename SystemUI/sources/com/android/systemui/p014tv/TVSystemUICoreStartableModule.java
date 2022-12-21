package com.android.systemui.p014tv;

import com.android.systemui.CoreStartable;
import com.android.systemui.SliceBroadcastRelayHandler;
import com.android.systemui.accessibility.WindowMagnification;
import com.android.systemui.dagger.qualifiers.PerUser;
import com.android.systemui.globalactions.GlobalActionsComponent;
import com.android.systemui.keyboard.KeyboardUI;
import com.android.systemui.media.RingtonePlayer;
import com.android.systemui.media.systemsounds.HomeSoundEffectController;
import com.android.systemui.power.PowerUI;
import com.android.systemui.privacy.television.TvOngoingPrivacyChip;
import com.android.systemui.shortcut.ShortcutKeyDispatcher;
import com.android.systemui.statusbar.notification.InstantAppNotifier;
import com.android.systemui.statusbar.p013tv.TvStatusBar;
import com.android.systemui.statusbar.p013tv.VpnStatusObserver;
import com.android.systemui.statusbar.p013tv.notifications.TvNotificationHandler;
import com.android.systemui.statusbar.p013tv.notifications.TvNotificationPanel;
import com.android.systemui.theme.ThemeOverlayController;
import com.android.systemui.toast.ToastUI;
import com.android.systemui.usb.StorageNotification;
import com.android.systemui.util.NotificationChannels;
import com.android.systemui.volume.VolumeUI;
import com.android.systemui.wmshell.WMShell;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b'\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H'J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\bH'J\u0010\u0010\t\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\nH'J\u0010\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\fH'J\u0010\u0010\r\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u000eH'J\u0010\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0010H'J\u0010\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0012H'J\u0010\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0014H'J\u0010\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0016H'J\u0010\u0010\u0017\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0018H'J\u0010\u0010\u0019\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u001aH'J\u0010\u0010\u001b\u001a\u00020\u00042\u0006\u0010\u001c\u001a\u00020\u001dH'J\u0010\u0010\u001e\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u001fH'J\u0010\u0010 \u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020!H'J\u0010\u0010\"\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020#H'J\u0010\u0010$\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020%H'J\u0010\u0010&\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020'H'J\u0010\u0010(\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020)H'J\u0010\u0010*\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020+H'J\u0010\u0010,\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020-H'¨\u0006."}, mo64987d2 = {"Lcom/android/systemui/tv/TVSystemUICoreStartableModule;", "", "()V", "bind", "Lcom/android/systemui/CoreStartable;", "sysui", "Lcom/android/systemui/media/RingtonePlayer;", "bindGlobalActionsComponent", "Lcom/android/systemui/globalactions/GlobalActionsComponent;", "bindHomeSoundEffectController", "Lcom/android/systemui/media/systemsounds/HomeSoundEffectController;", "bindInstantAppNotifier", "Lcom/android/systemui/statusbar/notification/InstantAppNotifier;", "bindKeyboardUI", "Lcom/android/systemui/keyboard/KeyboardUI;", "bindNotificationChannels", "Lcom/android/systemui/util/NotificationChannels;", "bindPowerUI", "Lcom/android/systemui/power/PowerUI;", "bindShortcutKeyDispatcher", "Lcom/android/systemui/shortcut/ShortcutKeyDispatcher;", "bindSliceBroadcastRelayHandler", "Lcom/android/systemui/SliceBroadcastRelayHandler;", "bindStorageNotification", "Lcom/android/systemui/usb/StorageNotification;", "bindThemeOverlayController", "Lcom/android/systemui/theme/ThemeOverlayController;", "bindToastUI", "service", "Lcom/android/systemui/toast/ToastUI;", "bindTvNotificationHandler", "Lcom/android/systemui/statusbar/tv/notifications/TvNotificationHandler;", "bindTvNotificationPanel", "Lcom/android/systemui/statusbar/tv/notifications/TvNotificationPanel;", "bindTvOngoingPrivacyChip", "Lcom/android/systemui/privacy/television/TvOngoingPrivacyChip;", "bindTvStatusBar", "Lcom/android/systemui/statusbar/tv/TvStatusBar;", "bindVolumeUI", "Lcom/android/systemui/volume/VolumeUI;", "bindVpnStatusObserver", "Lcom/android/systemui/statusbar/tv/VpnStatusObserver;", "bindWMShell", "Lcom/android/systemui/wmshell/WMShell;", "bindWindowMagnification", "Lcom/android/systemui/accessibility/WindowMagnification;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
@Module
/* renamed from: com.android.systemui.tv.TVSystemUICoreStartableModule */
/* compiled from: TVSystemUICoreStartableModule.kt */
public abstract class TVSystemUICoreStartableModule {
    @IntoMap
    @ClassKey(RingtonePlayer.class)
    @Binds
    public abstract CoreStartable bind(RingtonePlayer ringtonePlayer);

    @IntoMap
    @ClassKey(GlobalActionsComponent.class)
    @Binds
    public abstract CoreStartable bindGlobalActionsComponent(GlobalActionsComponent globalActionsComponent);

    @IntoMap
    @ClassKey(HomeSoundEffectController.class)
    @Binds
    public abstract CoreStartable bindHomeSoundEffectController(HomeSoundEffectController homeSoundEffectController);

    @IntoMap
    @ClassKey(InstantAppNotifier.class)
    @Binds
    public abstract CoreStartable bindInstantAppNotifier(InstantAppNotifier instantAppNotifier);

    @IntoMap
    @ClassKey(KeyboardUI.class)
    @Binds
    public abstract CoreStartable bindKeyboardUI(KeyboardUI keyboardUI);

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
    @ClassKey(ThemeOverlayController.class)
    @Binds
    public abstract CoreStartable bindThemeOverlayController(ThemeOverlayController themeOverlayController);

    @IntoMap
    @ClassKey(ToastUI.class)
    @Binds
    public abstract CoreStartable bindToastUI(ToastUI toastUI);

    @IntoMap
    @ClassKey(TvNotificationHandler.class)
    @Binds
    public abstract CoreStartable bindTvNotificationHandler(TvNotificationHandler tvNotificationHandler);

    @IntoMap
    @ClassKey(TvNotificationPanel.class)
    @Binds
    public abstract CoreStartable bindTvNotificationPanel(TvNotificationPanel tvNotificationPanel);

    @IntoMap
    @ClassKey(TvOngoingPrivacyChip.class)
    @Binds
    public abstract CoreStartable bindTvOngoingPrivacyChip(TvOngoingPrivacyChip tvOngoingPrivacyChip);

    @IntoMap
    @ClassKey(TvStatusBar.class)
    @Binds
    public abstract CoreStartable bindTvStatusBar(TvStatusBar tvStatusBar);

    @IntoMap
    @ClassKey(VolumeUI.class)
    @Binds
    public abstract CoreStartable bindVolumeUI(VolumeUI volumeUI);

    @IntoMap
    @ClassKey(VpnStatusObserver.class)
    @Binds
    public abstract CoreStartable bindVpnStatusObserver(VpnStatusObserver vpnStatusObserver);

    @IntoMap
    @ClassKey(WMShell.class)
    @Binds
    public abstract CoreStartable bindWMShell(WMShell wMShell);

    @IntoMap
    @ClassKey(WindowMagnification.class)
    @Binds
    public abstract CoreStartable bindWindowMagnification(WindowMagnification windowMagnification);
}
