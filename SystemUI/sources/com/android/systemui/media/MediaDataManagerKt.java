package com.android.systemui.media;

import android.app.Notification;
import android.service.notification.StatusBarNotification;
import com.android.internal.annotations.VisibleForTesting;
import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: MediaDataManager.kt */
/* loaded from: classes.dex */
public final class MediaDataManagerKt {
    @NotNull
    private static final String[] ART_URIS = {"android.media.metadata.ALBUM_ART_URI", "android.media.metadata.ART_URI", "android.media.metadata.DISPLAY_ICON_URI"};
    @NotNull
    private static final SmartspaceMediaData EMPTY_SMARTSPACE_MEDIA_DATA;
    @NotNull
    private static final MediaData LOADING;

    public static final /* synthetic */ String[] access$getART_URIS$p() {
        return ART_URIS;
    }

    public static final /* synthetic */ MediaData access$getLOADING$p() {
        return LOADING;
    }

    @VisibleForTesting
    public static /* synthetic */ void getEMPTY_SMARTSPACE_MEDIA_DATA$annotations() {
    }

    static {
        List emptyList;
        List emptyList2;
        List emptyList3;
        emptyList = CollectionsKt__CollectionsKt.emptyList();
        emptyList2 = CollectionsKt__CollectionsKt.emptyList();
        LOADING = new MediaData(-1, false, 0, null, null, null, null, null, emptyList, emptyList2, "INVALID", null, null, null, true, null, false, false, null, false, null, false, 0L, 8323072, null);
        emptyList3 = CollectionsKt__CollectionsKt.emptyList();
        EMPTY_SMARTSPACE_MEDIA_DATA = new SmartspaceMediaData("INVALID", false, false, "INVALID", null, emptyList3, null, 0);
    }

    @NotNull
    public static final SmartspaceMediaData getEMPTY_SMARTSPACE_MEDIA_DATA() {
        return EMPTY_SMARTSPACE_MEDIA_DATA;
    }

    public static final boolean isMediaNotification(@NotNull StatusBarNotification sbn) {
        Intrinsics.checkNotNullParameter(sbn, "sbn");
        if (!sbn.getNotification().hasMediaSession()) {
            return false;
        }
        Class notificationStyle = sbn.getNotification().getNotificationStyle();
        return Notification.DecoratedMediaCustomViewStyle.class.equals(notificationStyle) || Notification.MediaStyle.class.equals(notificationStyle);
    }
}
