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
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001Bg\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013\u0012\u0006\u0010\u0015\u001a\u00020\u0016\u0012\u0006\u0010\u0017\u001a\u00020\u0018¢\u0006\u0002\u0010\u0019J\"\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020%2\n\b\u0002\u0010&\u001a\u0004\u0018\u00010'J\u0006\u0010(\u001a\u00020!R\u000e\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u001a\u001a\u0004\u0018\u00010\u001bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0002\n\u0000¨\u0006)"}, mo64987d2 = {"Lcom/android/systemui/media/dialog/MediaOutputBroadcastDialogFactory;", "", "context", "Landroid/content/Context;", "mediaSessionManager", "Landroid/media/session/MediaSessionManager;", "lbm", "Lcom/android/settingslib/bluetooth/LocalBluetoothManager;", "starter", "Lcom/android/systemui/plugins/ActivityStarter;", "broadcastSender", "Lcom/android/systemui/broadcast/BroadcastSender;", "notifCollection", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/CommonNotifCollection;", "uiEventLogger", "Lcom/android/internal/logging/UiEventLogger;", "dialogLaunchAnimator", "Lcom/android/systemui/animation/DialogLaunchAnimator;", "nearbyMediaDevicesManagerOptional", "Ljava/util/Optional;", "Lcom/android/systemui/media/nearby/NearbyMediaDevicesManager;", "audioManager", "Landroid/media/AudioManager;", "powerExemptionManager", "Landroid/os/PowerExemptionManager;", "(Landroid/content/Context;Landroid/media/session/MediaSessionManager;Lcom/android/settingslib/bluetooth/LocalBluetoothManager;Lcom/android/systemui/plugins/ActivityStarter;Lcom/android/systemui/broadcast/BroadcastSender;Lcom/android/systemui/statusbar/notification/collection/notifcollection/CommonNotifCollection;Lcom/android/internal/logging/UiEventLogger;Lcom/android/systemui/animation/DialogLaunchAnimator;Ljava/util/Optional;Landroid/media/AudioManager;Landroid/os/PowerExemptionManager;)V", "mediaOutputBroadcastDialog", "Lcom/android/systemui/media/dialog/MediaOutputBroadcastDialog;", "getMediaOutputBroadcastDialog", "()Lcom/android/systemui/media/dialog/MediaOutputBroadcastDialog;", "setMediaOutputBroadcastDialog", "(Lcom/android/systemui/media/dialog/MediaOutputBroadcastDialog;)V", "create", "", "packageName", "", "aboveStatusBar", "", "view", "Landroid/view/View;", "dismiss", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaOutputBroadcastDialogFactory.kt */
public final class MediaOutputBroadcastDialogFactory {
    private final AudioManager audioManager;
    private final BroadcastSender broadcastSender;
    private final Context context;
    private final DialogLaunchAnimator dialogLaunchAnimator;
    private final LocalBluetoothManager lbm;
    private MediaOutputBroadcastDialog mediaOutputBroadcastDialog;
    private final MediaSessionManager mediaSessionManager;
    private final Optional<NearbyMediaDevicesManager> nearbyMediaDevicesManagerOptional;
    private final CommonNotifCollection notifCollection;
    private final PowerExemptionManager powerExemptionManager;
    private final ActivityStarter starter;
    private final UiEventLogger uiEventLogger;

    @Inject
    public MediaOutputBroadcastDialogFactory(Context context2, MediaSessionManager mediaSessionManager2, LocalBluetoothManager localBluetoothManager, ActivityStarter activityStarter, BroadcastSender broadcastSender2, CommonNotifCollection commonNotifCollection, UiEventLogger uiEventLogger2, DialogLaunchAnimator dialogLaunchAnimator2, Optional<NearbyMediaDevicesManager> optional, AudioManager audioManager2, PowerExemptionManager powerExemptionManager2) {
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

    public final MediaOutputBroadcastDialog getMediaOutputBroadcastDialog() {
        return this.mediaOutputBroadcastDialog;
    }

    public final void setMediaOutputBroadcastDialog(MediaOutputBroadcastDialog mediaOutputBroadcastDialog2) {
        this.mediaOutputBroadcastDialog = mediaOutputBroadcastDialog2;
    }

    public static /* synthetic */ void create$default(MediaOutputBroadcastDialogFactory mediaOutputBroadcastDialogFactory, String str, boolean z, View view, int i, Object obj) {
        if ((i & 4) != 0) {
            view = null;
        }
        mediaOutputBroadcastDialogFactory.create(str, z, view);
    }

    public final void create(String str, boolean z, View view) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        MediaOutputBroadcastDialog mediaOutputBroadcastDialog2 = this.mediaOutputBroadcastDialog;
        if (mediaOutputBroadcastDialog2 != null) {
            mediaOutputBroadcastDialog2.dismiss();
        }
        MediaOutputBroadcastDialog mediaOutputBroadcastDialog3 = new MediaOutputBroadcastDialog(this.context, z, this.broadcastSender, new MediaOutputController(this.context, str, this.mediaSessionManager, this.lbm, this.starter, this.notifCollection, this.dialogLaunchAnimator, this.nearbyMediaDevicesManagerOptional, this.audioManager, this.powerExemptionManager));
        this.mediaOutputBroadcastDialog = mediaOutputBroadcastDialog3;
        if (view != null) {
            DialogLaunchAnimator.showFromView$default(this.dialogLaunchAnimator, mediaOutputBroadcastDialog3, view, false, 4, (Object) null);
        } else {
            mediaOutputBroadcastDialog3.show();
        }
    }

    public final void dismiss() {
        MediaOutputBroadcastDialog mediaOutputBroadcastDialog2 = this.mediaOutputBroadcastDialog;
        if (mediaOutputBroadcastDialog2 != null) {
            mediaOutputBroadcastDialog2.dismiss();
        }
        this.mediaOutputBroadcastDialog = null;
    }
}
