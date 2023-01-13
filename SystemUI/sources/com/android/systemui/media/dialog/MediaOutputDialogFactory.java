package com.android.systemui.media.dialog;

import android.content.Context;
import android.media.AudioManager;
import android.media.session.MediaSessionManager;
import android.os.PowerExemptionManager;
import android.view.View;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.media.nearby.NearbyMediaDevicesManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.google.android.setupcompat.internal.FocusChangedMetricHelper;
import java.util.Optional;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 #2\u00020\u0001:\u0001#Bg\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013\u0012\u0006\u0010\u0015\u001a\u00020\u0016\u0012\u0006\u0010\u0017\u001a\u00020\u0018¢\u0006\u0002\u0010\u0019J\"\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\n\b\u0002\u0010 \u001a\u0004\u0018\u00010!J\u0006\u0010\"\u001a\u00020\u001bR\u000e\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0002\n\u0000¨\u0006$"}, mo65043d2 = {"Lcom/android/systemui/media/dialog/MediaOutputDialogFactory;", "", "context", "Landroid/content/Context;", "mediaSessionManager", "Landroid/media/session/MediaSessionManager;", "lbm", "Lcom/android/settingslib/bluetooth/LocalBluetoothManager;", "starter", "Lcom/android/systemui/plugins/ActivityStarter;", "broadcastSender", "Lcom/android/systemui/broadcast/BroadcastSender;", "notifCollection", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/CommonNotifCollection;", "uiEventLogger", "Lcom/android/internal/logging/UiEventLogger;", "dialogLaunchAnimator", "Lcom/android/systemui/animation/DialogLaunchAnimator;", "nearbyMediaDevicesManagerOptional", "Ljava/util/Optional;", "Lcom/android/systemui/media/nearby/NearbyMediaDevicesManager;", "audioManager", "Landroid/media/AudioManager;", "powerExemptionManager", "Landroid/os/PowerExemptionManager;", "(Landroid/content/Context;Landroid/media/session/MediaSessionManager;Lcom/android/settingslib/bluetooth/LocalBluetoothManager;Lcom/android/systemui/plugins/ActivityStarter;Lcom/android/systemui/broadcast/BroadcastSender;Lcom/android/systemui/statusbar/notification/collection/notifcollection/CommonNotifCollection;Lcom/android/internal/logging/UiEventLogger;Lcom/android/systemui/animation/DialogLaunchAnimator;Ljava/util/Optional;Landroid/media/AudioManager;Landroid/os/PowerExemptionManager;)V", "create", "", "packageName", "", "aboveStatusBar", "", "view", "Landroid/view/View;", "dismiss", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaOutputDialogFactory.kt */
public final class MediaOutputDialogFactory {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public static MediaOutputDialog mediaOutputDialog;
    private final AudioManager audioManager;
    private final BroadcastSender broadcastSender;
    private final Context context;
    private final DialogLaunchAnimator dialogLaunchAnimator;
    private final LocalBluetoothManager lbm;
    private final MediaSessionManager mediaSessionManager;
    private final Optional<NearbyMediaDevicesManager> nearbyMediaDevicesManagerOptional;
    private final CommonNotifCollection notifCollection;
    private final PowerExemptionManager powerExemptionManager;
    private final ActivityStarter starter;
    private final UiEventLogger uiEventLogger;

    @Inject
    public MediaOutputDialogFactory(Context context2, MediaSessionManager mediaSessionManager2, LocalBluetoothManager localBluetoothManager, ActivityStarter activityStarter, BroadcastSender broadcastSender2, CommonNotifCollection commonNotifCollection, UiEventLogger uiEventLogger2, DialogLaunchAnimator dialogLaunchAnimator2, Optional<NearbyMediaDevicesManager> optional, AudioManager audioManager2, PowerExemptionManager powerExemptionManager2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(mediaSessionManager2, "mediaSessionManager");
        Intrinsics.checkNotNullParameter(activityStarter, "starter");
        Intrinsics.checkNotNullParameter(broadcastSender2, "broadcastSender");
        Intrinsics.checkNotNullParameter(commonNotifCollection, "notifCollection");
        Intrinsics.checkNotNullParameter(uiEventLogger2, "uiEventLogger");
        Intrinsics.checkNotNullParameter(dialogLaunchAnimator2, "dialogLaunchAnimator");
        Intrinsics.checkNotNullParameter(optional, "nearbyMediaDevicesManagerOptional");
        Intrinsics.checkNotNullParameter(audioManager2, "audioManager");
        Intrinsics.checkNotNullParameter(powerExemptionManager2, "powerExemptionManager");
        this.context = context2;
        this.mediaSessionManager = mediaSessionManager2;
        this.lbm = localBluetoothManager;
        this.starter = activityStarter;
        this.broadcastSender = broadcastSender2;
        this.notifCollection = commonNotifCollection;
        this.uiEventLogger = uiEventLogger2;
        this.dialogLaunchAnimator = dialogLaunchAnimator2;
        this.nearbyMediaDevicesManagerOptional = optional;
        this.audioManager = audioManager2;
        this.powerExemptionManager = powerExemptionManager2;
    }

    @Metadata(mo65042d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b¨\u0006\t"}, mo65043d2 = {"Lcom/android/systemui/media/dialog/MediaOutputDialogFactory$Companion;", "", "()V", "mediaOutputDialog", "Lcom/android/systemui/media/dialog/MediaOutputDialog;", "getMediaOutputDialog", "()Lcom/android/systemui/media/dialog/MediaOutputDialog;", "setMediaOutputDialog", "(Lcom/android/systemui/media/dialog/MediaOutputDialog;)V", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: MediaOutputDialogFactory.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final MediaOutputDialog getMediaOutputDialog() {
            return MediaOutputDialogFactory.mediaOutputDialog;
        }

        public final void setMediaOutputDialog(MediaOutputDialog mediaOutputDialog) {
            MediaOutputDialogFactory.mediaOutputDialog = mediaOutputDialog;
        }
    }

    public static /* synthetic */ void create$default(MediaOutputDialogFactory mediaOutputDialogFactory, String str, boolean z, View view, int i, Object obj) {
        if ((i & 4) != 0) {
            view = null;
        }
        mediaOutputDialogFactory.create(str, z, view);
    }

    public final void create(String str, boolean z, View view) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        MediaOutputDialog mediaOutputDialog2 = mediaOutputDialog;
        if (mediaOutputDialog2 != null) {
            mediaOutputDialog2.dismiss();
        }
        MediaOutputController mediaOutputController = new MediaOutputController(this.context, str, this.mediaSessionManager, this.lbm, this.starter, this.notifCollection, this.dialogLaunchAnimator, this.nearbyMediaDevicesManagerOptional, this.audioManager, this.powerExemptionManager);
        MediaOutputDialog mediaOutputDialog3 = new MediaOutputDialog(this.context, z, this.broadcastSender, mediaOutputController, this.uiEventLogger);
        mediaOutputDialog = mediaOutputDialog3;
        if (view != null) {
            DialogLaunchAnimator.showFromView$default(this.dialogLaunchAnimator, mediaOutputDialog3, view, false, 4, (Object) null);
        } else {
            mediaOutputDialog3.show();
        }
    }

    public final void dismiss() {
        MediaOutputDialog mediaOutputDialog2 = mediaOutputDialog;
        if (mediaOutputDialog2 != null) {
            mediaOutputDialog2.dismiss();
        }
        mediaOutputDialog = null;
    }
}
