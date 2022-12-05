package com.android.systemui.media.dialog;

import android.content.Context;
import android.media.session.MediaSessionManager;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.phone.ShadeController;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: MediaOutputDialogFactory.kt */
/* loaded from: classes.dex */
public final class MediaOutputDialogFactory {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @Nullable
    private static MediaOutputDialog mediaOutputDialog;
    @NotNull
    private final Context context;
    @Nullable
    private final LocalBluetoothManager lbm;
    @NotNull
    private final MediaSessionManager mediaSessionManager;
    @NotNull
    private final NotificationEntryManager notificationEntryManager;
    @NotNull
    private final ShadeController shadeController;
    @NotNull
    private final ActivityStarter starter;
    @NotNull
    private final UiEventLogger uiEventLogger;

    public MediaOutputDialogFactory(@NotNull Context context, @NotNull MediaSessionManager mediaSessionManager, @Nullable LocalBluetoothManager localBluetoothManager, @NotNull ShadeController shadeController, @NotNull ActivityStarter starter, @NotNull NotificationEntryManager notificationEntryManager, @NotNull UiEventLogger uiEventLogger) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(mediaSessionManager, "mediaSessionManager");
        Intrinsics.checkNotNullParameter(shadeController, "shadeController");
        Intrinsics.checkNotNullParameter(starter, "starter");
        Intrinsics.checkNotNullParameter(notificationEntryManager, "notificationEntryManager");
        Intrinsics.checkNotNullParameter(uiEventLogger, "uiEventLogger");
        this.context = context;
        this.mediaSessionManager = mediaSessionManager;
        this.lbm = localBluetoothManager;
        this.shadeController = shadeController;
        this.starter = starter;
        this.notificationEntryManager = notificationEntryManager;
        this.uiEventLogger = uiEventLogger;
    }

    /* compiled from: MediaOutputDialogFactory.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @Nullable
        public final MediaOutputDialog getMediaOutputDialog() {
            return MediaOutputDialogFactory.mediaOutputDialog;
        }

        public final void setMediaOutputDialog(@Nullable MediaOutputDialog mediaOutputDialog) {
            MediaOutputDialogFactory.mediaOutputDialog = mediaOutputDialog;
        }
    }

    public final void create(@NotNull String packageName, boolean z) {
        Intrinsics.checkNotNullParameter(packageName, "packageName");
        Companion companion = Companion;
        MediaOutputDialog mediaOutputDialog2 = companion.getMediaOutputDialog();
        if (mediaOutputDialog2 != null) {
            mediaOutputDialog2.dismiss();
        }
        companion.setMediaOutputDialog(new MediaOutputDialog(this.context, z, new MediaOutputController(this.context, packageName, z, this.mediaSessionManager, this.lbm, this.shadeController, this.starter, this.notificationEntryManager, this.uiEventLogger), this.uiEventLogger));
    }

    public final void dismiss() {
        Companion companion = Companion;
        MediaOutputDialog mediaOutputDialog2 = companion.getMediaOutputDialog();
        if (mediaOutputDialog2 != null) {
            mediaOutputDialog2.dismiss();
        }
        companion.setMediaOutputDialog(null);
    }
}
