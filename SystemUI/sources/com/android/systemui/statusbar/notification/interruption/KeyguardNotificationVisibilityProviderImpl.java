package com.android.systemui.statusbar.notification.interruption;

import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.UserHandle;
import android.util.IndentingPrintWriter;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.CoreStartable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.DumpUtilsKt;
import com.android.systemui.util.ListenerSet;
import com.android.systemui.util.settings.GlobalSettings;
import com.android.systemui.util.settings.SecureSettings;
import java.p026io.PrintWriter;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0003\u0018\u00002\u00020\u00012\u00020\u0002BY\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\b\b\u0001\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u0010\u0012\u0006\u0010\u0011\u001a\u00020\u0012\u0012\u0006\u0010\u0013\u001a\u00020\u0014\u0012\u0006\u0010\u0015\u001a\u00020\u0016¢\u0006\u0002\u0010\u0017J\u0016\u0010#\u001a\u00020$2\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u001f0\u001eH\u0016J%\u0010&\u001a\u00020$2\u0006\u0010'\u001a\u00020(2\u000e\u0010)\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u001f0*H\u0016¢\u0006\u0002\u0010+J\u0010\u0010,\u001a\u00020$2\u0006\u0010-\u001a\u00020\u001fH\u0002J\b\u0010.\u001a\u00020$H\u0002J\u0016\u0010/\u001a\u00020$2\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u001f0\u001eH\u0016J\u0010\u00100\u001a\u00020\u00192\u0006\u00101\u001a\u000202H\u0002J\u0010\u00103\u001a\u00020\u00192\u0006\u00101\u001a\u000204H\u0016J\b\u00105\u001a\u00020$H\u0016J\u0010\u00106\u001a\u00020\u00192\u0006\u00101\u001a\u000204H\u0002R\u000e\u0010\u0011\u001a\u00020\u0012X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u001a\u001a\u00020\u00198BX\u0004¢\u0006\u0006\u001a\u0004\b\u001a\u0010\u001bR\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001f0\u001e0\u001dX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010 \u001a\n \"*\u0004\u0018\u00010!0!X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000¨\u00067"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/interruption/KeyguardNotificationVisibilityProviderImpl;", "Lcom/android/systemui/CoreStartable;", "Lcom/android/systemui/statusbar/notification/interruption/KeyguardNotificationVisibilityProvider;", "context", "Landroid/content/Context;", "handler", "Landroid/os/Handler;", "keyguardStateController", "Lcom/android/systemui/statusbar/policy/KeyguardStateController;", "lockscreenUserManager", "Lcom/android/systemui/statusbar/NotificationLockscreenUserManager;", "keyguardUpdateMonitor", "Lcom/android/keyguard/KeyguardUpdateMonitor;", "highPriorityProvider", "Lcom/android/systemui/statusbar/notification/collection/provider/HighPriorityProvider;", "statusBarStateController", "Lcom/android/systemui/statusbar/SysuiStatusBarStateController;", "broadcastDispatcher", "Lcom/android/systemui/broadcast/BroadcastDispatcher;", "secureSettings", "Lcom/android/systemui/util/settings/SecureSettings;", "globalSettings", "Lcom/android/systemui/util/settings/GlobalSettings;", "(Landroid/content/Context;Landroid/os/Handler;Lcom/android/systemui/statusbar/policy/KeyguardStateController;Lcom/android/systemui/statusbar/NotificationLockscreenUserManager;Lcom/android/keyguard/KeyguardUpdateMonitor;Lcom/android/systemui/statusbar/notification/collection/provider/HighPriorityProvider;Lcom/android/systemui/statusbar/SysuiStatusBarStateController;Lcom/android/systemui/broadcast/BroadcastDispatcher;Lcom/android/systemui/util/settings/SecureSettings;Lcom/android/systemui/util/settings/GlobalSettings;)V", "hideSilentNotificationsOnLockscreen", "", "isLockedOrLocking", "()Z", "onStateChangedListeners", "Lcom/android/systemui/util/ListenerSet;", "Ljava/util/function/Consumer;", "", "showSilentNotifsUri", "Landroid/net/Uri;", "kotlin.jvm.PlatformType", "addOnStateChangedListener", "", "listener", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "notifyStateChanged", "reason", "readShowSilentNotificationSetting", "removeOnStateChangedListener", "shouldHideIfEntrySilent", "entry", "Lcom/android/systemui/statusbar/notification/collection/ListEntry;", "shouldHideNotification", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "start", "userSettingsDisallowNotification", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: KeyguardNotificationVisibilityProvider.kt */
final class KeyguardNotificationVisibilityProviderImpl extends CoreStartable implements KeyguardNotificationVisibilityProvider {
    private final BroadcastDispatcher broadcastDispatcher;
    private final GlobalSettings globalSettings;
    private final Handler handler;
    private boolean hideSilentNotificationsOnLockscreen;
    private final HighPriorityProvider highPriorityProvider;
    private final KeyguardStateController keyguardStateController;
    private final KeyguardUpdateMonitor keyguardUpdateMonitor;
    private final NotificationLockscreenUserManager lockscreenUserManager;
    private final ListenerSet<Consumer<String>> onStateChangedListeners = new ListenerSet<>();
    private final SecureSettings secureSettings;
    /* access modifiers changed from: private */
    public final Uri showSilentNotifsUri;
    private final SysuiStatusBarStateController statusBarStateController;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @Inject
    public KeyguardNotificationVisibilityProviderImpl(Context context, @Main Handler handler2, KeyguardStateController keyguardStateController2, NotificationLockscreenUserManager notificationLockscreenUserManager, KeyguardUpdateMonitor keyguardUpdateMonitor2, HighPriorityProvider highPriorityProvider2, SysuiStatusBarStateController sysuiStatusBarStateController, BroadcastDispatcher broadcastDispatcher2, SecureSettings secureSettings2, GlobalSettings globalSettings2) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(handler2, "handler");
        Intrinsics.checkNotNullParameter(keyguardStateController2, "keyguardStateController");
        Intrinsics.checkNotNullParameter(notificationLockscreenUserManager, "lockscreenUserManager");
        Intrinsics.checkNotNullParameter(keyguardUpdateMonitor2, "keyguardUpdateMonitor");
        Intrinsics.checkNotNullParameter(highPriorityProvider2, "highPriorityProvider");
        Intrinsics.checkNotNullParameter(sysuiStatusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(broadcastDispatcher2, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(secureSettings2, "secureSettings");
        Intrinsics.checkNotNullParameter(globalSettings2, "globalSettings");
        this.handler = handler2;
        this.keyguardStateController = keyguardStateController2;
        this.lockscreenUserManager = notificationLockscreenUserManager;
        this.keyguardUpdateMonitor = keyguardUpdateMonitor2;
        this.highPriorityProvider = highPriorityProvider2;
        this.statusBarStateController = sysuiStatusBarStateController;
        this.broadcastDispatcher = broadcastDispatcher2;
        this.secureSettings = secureSettings2;
        this.globalSettings = globalSettings2;
        this.showSilentNotifsUri = secureSettings2.getUriFor("lock_screen_show_silent_notifications");
    }

    public void start() {
        readShowSilentNotificationSetting();
        this.keyguardStateController.addCallback(new KeyguardNotificationVisibilityProviderImpl$start$1(this));
        this.keyguardUpdateMonitor.registerCallback(new KeyguardNotificationVisibilityProviderImpl$start$2(this));
        ContentObserver keyguardNotificationVisibilityProviderImpl$start$settingsObserver$1 = new C2729x92a76378(this, this.handler);
        this.secureSettings.registerContentObserverForUser("lock_screen_show_notifications", keyguardNotificationVisibilityProviderImpl$start$settingsObserver$1, -1);
        this.secureSettings.registerContentObserverForUser("lock_screen_allow_private_notifications", true, keyguardNotificationVisibilityProviderImpl$start$settingsObserver$1, -1);
        this.globalSettings.registerContentObserver("zen_mode", keyguardNotificationVisibilityProviderImpl$start$settingsObserver$1);
        this.secureSettings.registerContentObserverForUser("lock_screen_show_silent_notifications", keyguardNotificationVisibilityProviderImpl$start$settingsObserver$1, -1);
        this.statusBarStateController.addCallback(new KeyguardNotificationVisibilityProviderImpl$start$3(this));
        BroadcastDispatcher.registerReceiver$default(this.broadcastDispatcher, new KeyguardNotificationVisibilityProviderImpl$start$4(this), new IntentFilter("android.intent.action.USER_SWITCHED"), (Executor) null, (UserHandle) null, 0, (String) null, 60, (Object) null);
    }

    public void addOnStateChangedListener(Consumer<String> consumer) {
        Intrinsics.checkNotNullParameter(consumer, "listener");
        this.onStateChangedListeners.addIfAbsent(consumer);
    }

    public void removeOnStateChangedListener(Consumer<String> consumer) {
        Intrinsics.checkNotNullParameter(consumer, "listener");
        this.onStateChangedListeners.remove(consumer);
    }

    /* access modifiers changed from: private */
    public final void notifyStateChanged(String str) {
        for (Consumer accept : this.onStateChangedListeners) {
            accept.accept(str);
        }
    }

    public boolean shouldHideNotification(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        if (!isLockedOrLocking()) {
            return false;
        }
        if (this.lockscreenUserManager.shouldShowLockscreenNotifications() && !userSettingsDisallowNotification(notificationEntry) && notificationEntry.getSbn().getNotification().visibility != -1 && !shouldHideIfEntrySilent(notificationEntry)) {
            return false;
        }
        return true;
    }

    private final boolean shouldHideIfEntrySilent(ListEntry listEntry) {
        if (this.highPriorityProvider.isHighPriority(listEntry)) {
            return false;
        }
        NotificationEntry representativeEntry = listEntry.getRepresentativeEntry();
        if ((representativeEntry != null && representativeEntry.isAmbient()) || this.hideSilentNotificationsOnLockscreen) {
            return true;
        }
        GroupEntry parent = listEntry.getParent();
        if (parent == null) {
            return false;
        }
        shouldHideIfEntrySilent(parent);
        return false;
    }

    private static final boolean userSettingsDisallowNotification$disallowForUser(KeyguardNotificationVisibilityProviderImpl keyguardNotificationVisibilityProviderImpl, NotificationEntry notificationEntry, int i) {
        if (keyguardNotificationVisibilityProviderImpl.keyguardUpdateMonitor.isUserInLockdown(i)) {
            return true;
        }
        if (keyguardNotificationVisibilityProviderImpl.lockscreenUserManager.isLockscreenPublicMode(i) && (notificationEntry.getRanking().getLockscreenVisibilityOverride() == -1 || !keyguardNotificationVisibilityProviderImpl.lockscreenUserManager.userAllowsNotificationsInPublic(i))) {
            return true;
        }
        return false;
    }

    private final boolean userSettingsDisallowNotification(NotificationEntry notificationEntry) {
        int currentUserId = this.lockscreenUserManager.getCurrentUserId();
        int identifier = notificationEntry.getSbn().getUser().getIdentifier();
        if (userSettingsDisallowNotification$disallowForUser(this, notificationEntry, currentUserId)) {
            return true;
        }
        if (identifier == -1 || identifier == currentUserId) {
            return false;
        }
        return userSettingsDisallowNotification$disallowForUser(this, notificationEntry, identifier);
    }

    /* JADX INFO: finally extract failed */
    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        IndentingPrintWriter asIndenting = DumpUtilsKt.asIndenting(printWriter);
        asIndenting.println("isLockedOrLocking=" + isLockedOrLocking());
        asIndenting.increaseIndent();
        try {
            asIndenting.println("keyguardStateController.isShowing=" + this.keyguardStateController.isShowing());
            asIndenting.println("statusBarStateController.currentOrUpcomingState=" + this.statusBarStateController.getCurrentOrUpcomingState());
            asIndenting.decreaseIndent();
            asIndenting.println("hideSilentNotificationsOnLockscreen=" + this.hideSilentNotificationsOnLockscreen);
        } catch (Throwable th) {
            asIndenting.decreaseIndent();
            throw th;
        }
    }

    /* access modifiers changed from: private */
    public final boolean isLockedOrLocking() {
        if (this.keyguardStateController.isShowing() || this.statusBarStateController.getCurrentOrUpcomingState() == 1) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public final void readShowSilentNotificationSetting() {
        this.hideSilentNotificationsOnLockscreen = !this.secureSettings.getBoolForUser("lock_screen_show_silent_notifications", true, -2);
    }
}
