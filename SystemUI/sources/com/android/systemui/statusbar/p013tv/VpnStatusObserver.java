package com.android.systemui.statusbar.p013tv;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import androidx.core.app.NotificationCompat;
import com.android.internal.net.VpnConfig;
import com.android.systemui.C1893R;
import com.android.systemui.CoreStartable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.policy.SecurityController;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u0007\u0018\u0000 $2\u00020\u00012\u00020\u0002:\u0001$B\u0017\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\b\u0010\u001b\u001a\u00020\tH\u0002J\u0010\u0010\u001c\u001a\n \f*\u0004\u0018\u00010\u00120\u0012H\u0002J\u0010\u0010\u001d\u001a\n \f*\u0004\u0018\u00010\u00100\u0010H\u0002J\u0010\u0010\u001e\u001a\n \f*\u0004\u0018\u00010\u00120\u0012H\u0002J\b\u0010\u001f\u001a\u00020 H\u0002J\b\u0010!\u001a\u00020 H\u0002J\b\u0010\"\u001a\u00020 H\u0016J\b\u0010#\u001a\u00020 H\u0016R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\n\u001a\n \f*\u0004\u0018\u00010\u000b0\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\u000f\u001a\n \f*\u0004\u0018\u00010\u00100\u0010X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0011\u001a\n \f*\u0004\u0018\u00010\u00120\u0012X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0013\u001a\u00020\u00148BX\u0004¢\u0006\u0006\u001a\u0004\b\u0015\u0010\u0016R\u0016\u0010\u0017\u001a\u0004\u0018\u00010\u00188BX\u0004¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u001a¨\u0006%"}, mo64987d2 = {"Lcom/android/systemui/statusbar/tv/VpnStatusObserver;", "Lcom/android/systemui/CoreStartable;", "Lcom/android/systemui/statusbar/policy/SecurityController$SecurityControllerCallback;", "context", "Landroid/content/Context;", "securityController", "Lcom/android/systemui/statusbar/policy/SecurityController;", "(Landroid/content/Context;Lcom/android/systemui/statusbar/policy/SecurityController;)V", "notificationChannel", "Landroid/app/NotificationChannel;", "notificationManager", "Landroid/app/NotificationManager;", "kotlin.jvm.PlatformType", "vpnConnected", "", "vpnConnectedNotificationBuilder", "Landroid/app/Notification$Builder;", "vpnDisconnectedNotification", "Landroid/app/Notification;", "vpnIconId", "", "getVpnIconId", "()I", "vpnName", "", "getVpnName", "()Ljava/lang/String;", "createNotificationChannel", "createVpnConnectedNotification", "createVpnConnectedNotificationBuilder", "createVpnDisconnectedNotification", "notifyVpnConnected", "", "notifyVpnDisconnected", "onStateChanged", "start", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.statusbar.tv.VpnStatusObserver */
/* compiled from: VpnStatusObserver.kt */
public final class VpnStatusObserver extends CoreStartable implements SecurityController.SecurityControllerCallback {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final String NOTIFICATION_CHANNEL_TV_VPN = "VPN Status";
    /* access modifiers changed from: private */
    public static final String NOTIFICATION_TAG = "VpnStatusObserver";
    private static final String TAG = "TvVpnNotification";
    private static final long VPN_DISCONNECTED_NOTIFICATION_TIMEOUT_MS = 5000;
    private final NotificationChannel notificationChannel = createNotificationChannel();
    private final NotificationManager notificationManager;
    private final SecurityController securityController;
    private boolean vpnConnected;
    private final Notification.Builder vpnConnectedNotificationBuilder = createVpnConnectedNotificationBuilder();
    private final Notification vpnDisconnectedNotification = createVpnDisconnectedNotification();

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @Inject
    public VpnStatusObserver(Context context, SecurityController securityController2) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(securityController2, "securityController");
        this.securityController = securityController2;
        this.notificationManager = NotificationManager.from(context);
    }

    private final int getVpnIconId() {
        return this.securityController.isVpnBranded() ? C1893R.C1895drawable.stat_sys_branded_vpn : C1893R.C1895drawable.stat_sys_vpn_ic;
    }

    private final String getVpnName() {
        String primaryVpnName = this.securityController.getPrimaryVpnName();
        return primaryVpnName == null ? this.securityController.getWorkProfileVpnName() : primaryVpnName;
    }

    public void start() {
        this.securityController.addCallback(this);
    }

    public void onStateChanged() {
        boolean isVpnEnabled = this.securityController.isVpnEnabled();
        if (this.vpnConnected != isVpnEnabled) {
            if (isVpnEnabled) {
                notifyVpnConnected();
            } else {
                notifyVpnDisconnected();
            }
            this.vpnConnected = isVpnEnabled;
        }
    }

    private final void notifyVpnConnected() {
        this.notificationManager.notify(NOTIFICATION_TAG, 20, createVpnConnectedNotification());
    }

    private final void notifyVpnDisconnected() {
        NotificationManager notificationManager2 = this.notificationManager;
        String str = NOTIFICATION_TAG;
        notificationManager2.cancel(str, 20);
        notificationManager2.notify(str, 17, this.vpnDisconnectedNotification);
    }

    private final NotificationChannel createNotificationChannel() {
        NotificationChannel notificationChannel2 = new NotificationChannel(NOTIFICATION_CHANNEL_TV_VPN, NOTIFICATION_CHANNEL_TV_VPN, 4);
        this.notificationManager.createNotificationChannel(notificationChannel2);
        return notificationChannel2;
    }

    private final Notification createVpnConnectedNotification() {
        Notification.Builder builder = this.vpnConnectedNotificationBuilder;
        String vpnName = getVpnName();
        if (vpnName != null) {
            builder.setContentText(this.mContext.getString(C1893R.string.notification_disclosure_vpn_text, new Object[]{vpnName}));
        }
        return builder.build();
    }

    private final Notification.Builder createVpnConnectedNotificationBuilder() {
        return new Notification.Builder(this.mContext, NOTIFICATION_CHANNEL_TV_VPN).setSmallIcon(getVpnIconId()).setVisibility(1).setCategory(NotificationCompat.CATEGORY_SYSTEM).extend(new Notification.TvExtender()).setOngoing(true).setContentTitle(this.mContext.getString(C1893R.string.notification_vpn_connected)).setContentIntent(VpnConfig.getIntentForStatusPanel(this.mContext));
    }

    private final Notification createVpnDisconnectedNotification() {
        return new Notification.Builder(this.mContext, NOTIFICATION_CHANNEL_TV_VPN).setSmallIcon(getVpnIconId()).setVisibility(1).setCategory(NotificationCompat.CATEGORY_SYSTEM).extend(new Notification.TvExtender()).setTimeoutAfter(5000).setContentTitle(this.mContext.getString(C1893R.string.notification_vpn_disconnected)).build();
    }

    @Metadata(mo64986d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\t\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u000e\u0010\b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nXT¢\u0006\u0002\n\u0000¨\u0006\u000b"}, mo64987d2 = {"Lcom/android/systemui/statusbar/tv/VpnStatusObserver$Companion;", "", "()V", "NOTIFICATION_CHANNEL_TV_VPN", "", "NOTIFICATION_TAG", "getNOTIFICATION_TAG", "()Ljava/lang/String;", "TAG", "VPN_DISCONNECTED_NOTIFICATION_TIMEOUT_MS", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.systemui.statusbar.tv.VpnStatusObserver$Companion */
    /* compiled from: VpnStatusObserver.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final String getNOTIFICATION_TAG() {
            return VpnStatusObserver.NOTIFICATION_TAG;
        }
    }

    static {
        Intrinsics.checkNotNullExpressionValue("VpnStatusObserver", "VpnStatusObserver::class.java.simpleName");
    }
}
