package com.android.systemui.media;

import android.app.PendingIntent;
import android.graphics.drawable.Icon;
import android.media.session.MediaSession;
import com.android.internal.logging.InstanceId;
import com.google.android.setupcompat.internal.FocusChangedMetricHelper;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\bT\b\b\u0018\u0000 z2\u00020\u0001:\u0001zB\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u000b\u0012\b\u0010\f\u001a\u0004\u0018\u00010\u000b\u0012\b\u0010\r\u001a\u0004\u0018\u00010\t\u0012\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f\u0012\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00030\u000f\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0013\u0012\u0006\u0010\u0014\u001a\u00020\u0007\u0012\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016\u0012\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018\u0012\b\u0010\u0019\u001a\u0004\u0018\u00010\u001a\u0012\u0006\u0010\u001b\u001a\u00020\u0005\u0012\b\u0010\u001c\u001a\u0004\u0018\u00010\u001d\u0012\b\b\u0002\u0010\u001e\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u001f\u001a\u00020\u0005\u0012\n\b\u0002\u0010 \u001a\u0004\u0018\u00010\u0007\u0012\b\b\u0002\u0010!\u001a\u00020\u0005\u0012\n\b\u0002\u0010\"\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010#\u001a\u00020\u0005\u0012\b\b\u0002\u0010$\u001a\u00020%\u0012\u0006\u0010&\u001a\u00020'\u0012\u0006\u0010(\u001a\u00020\u0003¢\u0006\u0002\u0010)J\t\u0010Z\u001a\u00020\u0003HÆ\u0003J\u000b\u0010[\u001a\u0004\u0018\u00010\u0013HÆ\u0003J\t\u0010\\\u001a\u00020\u0007HÆ\u0003J\u000b\u0010]\u001a\u0004\u0018\u00010\u0016HÆ\u0003J\u000b\u0010^\u001a\u0004\u0018\u00010\u0018HÆ\u0003J\u000b\u0010_\u001a\u0004\u0018\u00010\u001aHÆ\u0003J\t\u0010`\u001a\u00020\u0005HÆ\u0003J\u000b\u0010a\u001a\u0004\u0018\u00010\u001dHÆ\u0003J\t\u0010b\u001a\u00020\u0003HÆ\u0003J\t\u0010c\u001a\u00020\u0005HÆ\u0003J\u000b\u0010d\u001a\u0004\u0018\u00010\u0007HÆ\u0003J\t\u0010e\u001a\u00020\u0005HÆ\u0003J\t\u0010f\u001a\u00020\u0005HÆ\u0003J\u0010\u0010g\u001a\u0004\u0018\u00010\u0005HÆ\u0003¢\u0006\u0002\u0010CJ\t\u0010h\u001a\u00020\u0005HÆ\u0003J\t\u0010i\u001a\u00020%HÆ\u0003J\t\u0010j\u001a\u00020'HÆ\u0003J\t\u0010k\u001a\u00020\u0003HÆ\u0003J\u000b\u0010l\u001a\u0004\u0018\u00010\u0007HÆ\u0003J\u000b\u0010m\u001a\u0004\u0018\u00010\tHÆ\u0003J\u000b\u0010n\u001a\u0004\u0018\u00010\u000bHÆ\u0003J\u000b\u0010o\u001a\u0004\u0018\u00010\u000bHÆ\u0003J\u000b\u0010p\u001a\u0004\u0018\u00010\tHÆ\u0003J\u000f\u0010q\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fHÆ\u0003J\u000f\u0010r\u001a\b\u0012\u0004\u0012\u00020\u00030\u000fHÆ\u0003J¬\u0002\u0010s\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u000b2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\t2\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\u000e\b\u0002\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00030\u000f2\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u00132\b\b\u0002\u0010\u0014\u001a\u00020\u00072\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u00162\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u00182\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u001a2\b\b\u0002\u0010\u001b\u001a\u00020\u00052\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\b\b\u0002\u0010\u001e\u001a\u00020\u00032\b\b\u0002\u0010\u001f\u001a\u00020\u00052\n\b\u0002\u0010 \u001a\u0004\u0018\u00010\u00072\b\b\u0002\u0010!\u001a\u00020\u00052\n\b\u0002\u0010\"\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010#\u001a\u00020\u00052\b\b\u0002\u0010$\u001a\u00020%2\b\b\u0002\u0010&\u001a\u00020'2\b\b\u0002\u0010(\u001a\u00020\u0003HÆ\u0001¢\u0006\u0002\u0010tJ\u0013\u0010u\u001a\u00020\u00052\b\u0010v\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010w\u001a\u00020\u0003HÖ\u0001J\u0006\u0010x\u001a\u00020\u0005J\t\u0010y\u001a\u00020\u0007HÖ\u0001R\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f¢\u0006\b\n\u0000\u001a\u0004\b*\u0010+R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00030\u000f¢\u0006\b\n\u0000\u001a\u0004\b,\u0010+R\u001a\u0010\u001b\u001a\u00020\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b-\u0010.\"\u0004\b/\u00100R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\b\n\u0000\u001a\u0004\b1\u00102R\u0013\u0010\b\u001a\u0004\u0018\u00010\t¢\u0006\b\n\u0000\u001a\u0004\b3\u00104R\u0011\u0010(\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b5\u00106R\u0013\u0010\n\u001a\u0004\u0018\u00010\u000b¢\u0006\b\n\u0000\u001a\u0004\b7\u00108R\u0013\u0010\r\u001a\u0004\u0018\u00010\t¢\u0006\b\n\u0000\u001a\u0004\b9\u00104R\u0013\u0010\u0017\u001a\u0004\u0018\u00010\u0018¢\u0006\b\n\u0000\u001a\u0004\b:\u0010;R\u0013\u0010\u0019\u001a\u0004\u0018\u00010\u001a¢\u0006\b\n\u0000\u001a\u0004\b<\u0010=R\u001a\u0010!\u001a\u00020\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b>\u0010.\"\u0004\b?\u00100R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b@\u0010.R\u0011\u0010&\u001a\u00020'¢\u0006\b\n\u0000\u001a\u0004\bA\u0010BR\u0011\u0010#\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b#\u0010.R\u0015\u0010\"\u001a\u0004\u0018\u00010\u0005¢\u0006\n\n\u0002\u0010D\u001a\u0004\b\"\u0010CR\u001a\u0010$\u001a\u00020%X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bE\u0010F\"\u0004\bG\u0010HR\u0013\u0010 \u001a\u0004\u0018\u00010\u0007¢\u0006\b\n\u0000\u001a\u0004\bI\u00102R\u0011\u0010\u0014\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\bJ\u00102R\u001a\u0010\u001e\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bK\u00106\"\u0004\bL\u0010MR\u001c\u0010\u001c\u001a\u0004\u0018\u00010\u001dX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bN\u0010O\"\u0004\bP\u0010QR\u001a\u0010\u001f\u001a\u00020\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bR\u0010.\"\u0004\bS\u00100R\u0013\u0010\u0012\u001a\u0004\u0018\u00010\u0013¢\u0006\b\n\u0000\u001a\u0004\bT\u0010UR\u0013\u0010\f\u001a\u0004\u0018\u00010\u000b¢\u0006\b\n\u0000\u001a\u0004\bV\u00108R\u0013\u0010\u0015\u001a\u0004\u0018\u00010\u0016¢\u0006\b\n\u0000\u001a\u0004\bW\u0010XR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\bY\u00106¨\u0006{"}, mo64987d2 = {"Lcom/android/systemui/media/MediaData;", "", "userId", "", "initialized", "", "app", "", "appIcon", "Landroid/graphics/drawable/Icon;", "artist", "", "song", "artwork", "actions", "", "Lcom/android/systemui/media/MediaAction;", "actionsToShowInCompact", "semanticActions", "Lcom/android/systemui/media/MediaButton;", "packageName", "token", "Landroid/media/session/MediaSession$Token;", "clickIntent", "Landroid/app/PendingIntent;", "device", "Lcom/android/systemui/media/MediaDeviceData;", "active", "resumeAction", "Ljava/lang/Runnable;", "playbackLocation", "resumption", "notificationKey", "hasCheckedForResume", "isPlaying", "isClearable", "lastActive", "", "instanceId", "Lcom/android/internal/logging/InstanceId;", "appUid", "(IZLjava/lang/String;Landroid/graphics/drawable/Icon;Ljava/lang/CharSequence;Ljava/lang/CharSequence;Landroid/graphics/drawable/Icon;Ljava/util/List;Ljava/util/List;Lcom/android/systemui/media/MediaButton;Ljava/lang/String;Landroid/media/session/MediaSession$Token;Landroid/app/PendingIntent;Lcom/android/systemui/media/MediaDeviceData;ZLjava/lang/Runnable;IZLjava/lang/String;ZLjava/lang/Boolean;ZJLcom/android/internal/logging/InstanceId;I)V", "getActions", "()Ljava/util/List;", "getActionsToShowInCompact", "getActive", "()Z", "setActive", "(Z)V", "getApp", "()Ljava/lang/String;", "getAppIcon", "()Landroid/graphics/drawable/Icon;", "getAppUid", "()I", "getArtist", "()Ljava/lang/CharSequence;", "getArtwork", "getClickIntent", "()Landroid/app/PendingIntent;", "getDevice", "()Lcom/android/systemui/media/MediaDeviceData;", "getHasCheckedForResume", "setHasCheckedForResume", "getInitialized", "getInstanceId", "()Lcom/android/internal/logging/InstanceId;", "()Ljava/lang/Boolean;", "Ljava/lang/Boolean;", "getLastActive", "()J", "setLastActive", "(J)V", "getNotificationKey", "getPackageName", "getPlaybackLocation", "setPlaybackLocation", "(I)V", "getResumeAction", "()Ljava/lang/Runnable;", "setResumeAction", "(Ljava/lang/Runnable;)V", "getResumption", "setResumption", "getSemanticActions", "()Lcom/android/systemui/media/MediaButton;", "getSong", "getToken", "()Landroid/media/session/MediaSession$Token;", "getUserId", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component25", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(IZLjava/lang/String;Landroid/graphics/drawable/Icon;Ljava/lang/CharSequence;Ljava/lang/CharSequence;Landroid/graphics/drawable/Icon;Ljava/util/List;Ljava/util/List;Lcom/android/systemui/media/MediaButton;Ljava/lang/String;Landroid/media/session/MediaSession$Token;Landroid/app/PendingIntent;Lcom/android/systemui/media/MediaDeviceData;ZLjava/lang/Runnable;IZLjava/lang/String;ZLjava/lang/Boolean;ZJLcom/android/internal/logging/InstanceId;I)Lcom/android/systemui/media/MediaData;", "equals", "other", "hashCode", "isLocalSession", "toString", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaData.kt */
public final class MediaData {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final int PLAYBACK_CAST_LOCAL = 1;
    public static final int PLAYBACK_CAST_REMOTE = 2;
    public static final int PLAYBACK_LOCAL = 0;
    private final List<MediaAction> actions;
    private final List<Integer> actionsToShowInCompact;
    private boolean active;
    private final String app;
    private final Icon appIcon;
    private final int appUid;
    private final CharSequence artist;
    private final Icon artwork;
    private final PendingIntent clickIntent;
    private final MediaDeviceData device;
    private boolean hasCheckedForResume;
    private final boolean initialized;
    private final InstanceId instanceId;
    private final boolean isClearable;
    private final Boolean isPlaying;
    private long lastActive;
    private final String notificationKey;
    private final String packageName;
    private int playbackLocation;
    private Runnable resumeAction;
    private boolean resumption;
    private final MediaButton semanticActions;
    private final CharSequence song;
    private final MediaSession.Token token;
    private final int userId;

    public static /* synthetic */ MediaData copy$default(MediaData mediaData, int i, boolean z, String str, Icon icon, CharSequence charSequence, CharSequence charSequence2, Icon icon2, List list, List list2, MediaButton mediaButton, String str2, MediaSession.Token token2, PendingIntent pendingIntent, MediaDeviceData mediaDeviceData, boolean z2, Runnable runnable, int i2, boolean z3, String str3, boolean z4, Boolean bool, boolean z5, long j, InstanceId instanceId2, int i3, int i4, Object obj) {
        MediaData mediaData2 = mediaData;
        int i5 = i4;
        return mediaData.copy((i5 & 1) != 0 ? mediaData2.userId : i, (i5 & 2) != 0 ? mediaData2.initialized : z, (i5 & 4) != 0 ? mediaData2.app : str, (i5 & 8) != 0 ? mediaData2.appIcon : icon, (i5 & 16) != 0 ? mediaData2.artist : charSequence, (i5 & 32) != 0 ? mediaData2.song : charSequence2, (i5 & 64) != 0 ? mediaData2.artwork : icon2, (i5 & 128) != 0 ? mediaData2.actions : list, (i5 & 256) != 0 ? mediaData2.actionsToShowInCompact : list2, (i5 & 512) != 0 ? mediaData2.semanticActions : mediaButton, (i5 & 1024) != 0 ? mediaData2.packageName : str2, (i5 & 2048) != 0 ? mediaData2.token : token2, (i5 & 4096) != 0 ? mediaData2.clickIntent : pendingIntent, (i5 & 8192) != 0 ? mediaData2.device : mediaDeviceData, (i5 & 16384) != 0 ? mediaData2.active : z2, (i5 & 32768) != 0 ? mediaData2.resumeAction : runnable, (i5 & 65536) != 0 ? mediaData2.playbackLocation : i2, (i5 & 131072) != 0 ? mediaData2.resumption : z3, (i5 & 262144) != 0 ? mediaData2.notificationKey : str3, (i5 & 524288) != 0 ? mediaData2.hasCheckedForResume : z4, (i5 & 1048576) != 0 ? mediaData2.isPlaying : bool, (i5 & 2097152) != 0 ? mediaData2.isClearable : z5, (i5 & 4194304) != 0 ? mediaData2.lastActive : j, (i5 & 8388608) != 0 ? mediaData2.instanceId : instanceId2, (i5 & 16777216) != 0 ? mediaData2.appUid : i3);
    }

    public final int component1() {
        return this.userId;
    }

    public final MediaButton component10() {
        return this.semanticActions;
    }

    public final String component11() {
        return this.packageName;
    }

    public final MediaSession.Token component12() {
        return this.token;
    }

    public final PendingIntent component13() {
        return this.clickIntent;
    }

    public final MediaDeviceData component14() {
        return this.device;
    }

    public final boolean component15() {
        return this.active;
    }

    public final Runnable component16() {
        return this.resumeAction;
    }

    public final int component17() {
        return this.playbackLocation;
    }

    public final boolean component18() {
        return this.resumption;
    }

    public final String component19() {
        return this.notificationKey;
    }

    public final boolean component2() {
        return this.initialized;
    }

    public final boolean component20() {
        return this.hasCheckedForResume;
    }

    public final Boolean component21() {
        return this.isPlaying;
    }

    public final boolean component22() {
        return this.isClearable;
    }

    public final long component23() {
        return this.lastActive;
    }

    public final InstanceId component24() {
        return this.instanceId;
    }

    public final int component25() {
        return this.appUid;
    }

    public final String component3() {
        return this.app;
    }

    public final Icon component4() {
        return this.appIcon;
    }

    public final CharSequence component5() {
        return this.artist;
    }

    public final CharSequence component6() {
        return this.song;
    }

    public final Icon component7() {
        return this.artwork;
    }

    public final List<MediaAction> component8() {
        return this.actions;
    }

    public final List<Integer> component9() {
        return this.actionsToShowInCompact;
    }

    public final MediaData copy(int i, boolean z, String str, Icon icon, CharSequence charSequence, CharSequence charSequence2, Icon icon2, List<MediaAction> list, List<Integer> list2, MediaButton mediaButton, String str2, MediaSession.Token token2, PendingIntent pendingIntent, MediaDeviceData mediaDeviceData, boolean z2, Runnable runnable, int i2, boolean z3, String str3, boolean z4, Boolean bool, boolean z5, long j, InstanceId instanceId2, int i3) {
        Intrinsics.checkNotNullParameter(list, "actions");
        Intrinsics.checkNotNullParameter(list2, "actionsToShowInCompact");
        Intrinsics.checkNotNullParameter(str2, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(instanceId2, "instanceId");
        return new MediaData(i, z, str, icon, charSequence, charSequence2, icon2, list, list2, mediaButton, str2, token2, pendingIntent, mediaDeviceData, z2, runnable, i2, z3, str3, z4, bool, z5, j, instanceId2, i3);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MediaData)) {
            return false;
        }
        MediaData mediaData = (MediaData) obj;
        return this.userId == mediaData.userId && this.initialized == mediaData.initialized && Intrinsics.areEqual((Object) this.app, (Object) mediaData.app) && Intrinsics.areEqual((Object) this.appIcon, (Object) mediaData.appIcon) && Intrinsics.areEqual((Object) this.artist, (Object) mediaData.artist) && Intrinsics.areEqual((Object) this.song, (Object) mediaData.song) && Intrinsics.areEqual((Object) this.artwork, (Object) mediaData.artwork) && Intrinsics.areEqual((Object) this.actions, (Object) mediaData.actions) && Intrinsics.areEqual((Object) this.actionsToShowInCompact, (Object) mediaData.actionsToShowInCompact) && Intrinsics.areEqual((Object) this.semanticActions, (Object) mediaData.semanticActions) && Intrinsics.areEqual((Object) this.packageName, (Object) mediaData.packageName) && Intrinsics.areEqual((Object) this.token, (Object) mediaData.token) && Intrinsics.areEqual((Object) this.clickIntent, (Object) mediaData.clickIntent) && Intrinsics.areEqual((Object) this.device, (Object) mediaData.device) && this.active == mediaData.active && Intrinsics.areEqual((Object) this.resumeAction, (Object) mediaData.resumeAction) && this.playbackLocation == mediaData.playbackLocation && this.resumption == mediaData.resumption && Intrinsics.areEqual((Object) this.notificationKey, (Object) mediaData.notificationKey) && this.hasCheckedForResume == mediaData.hasCheckedForResume && Intrinsics.areEqual((Object) this.isPlaying, (Object) mediaData.isPlaying) && this.isClearable == mediaData.isClearable && this.lastActive == mediaData.lastActive && Intrinsics.areEqual((Object) this.instanceId, (Object) mediaData.instanceId) && this.appUid == mediaData.appUid;
    }

    public int hashCode() {
        int hashCode = Integer.hashCode(this.userId) * 31;
        boolean z = this.initialized;
        boolean z2 = true;
        if (z) {
            z = true;
        }
        int i = (hashCode + (z ? 1 : 0)) * 31;
        String str = this.app;
        int i2 = 0;
        int hashCode2 = (i + (str == null ? 0 : str.hashCode())) * 31;
        Icon icon = this.appIcon;
        int hashCode3 = (hashCode2 + (icon == null ? 0 : icon.hashCode())) * 31;
        CharSequence charSequence = this.artist;
        int hashCode4 = (hashCode3 + (charSequence == null ? 0 : charSequence.hashCode())) * 31;
        CharSequence charSequence2 = this.song;
        int hashCode5 = (hashCode4 + (charSequence2 == null ? 0 : charSequence2.hashCode())) * 31;
        Icon icon2 = this.artwork;
        int hashCode6 = (((((hashCode5 + (icon2 == null ? 0 : icon2.hashCode())) * 31) + this.actions.hashCode()) * 31) + this.actionsToShowInCompact.hashCode()) * 31;
        MediaButton mediaButton = this.semanticActions;
        int hashCode7 = (((hashCode6 + (mediaButton == null ? 0 : mediaButton.hashCode())) * 31) + this.packageName.hashCode()) * 31;
        MediaSession.Token token2 = this.token;
        int hashCode8 = (hashCode7 + (token2 == null ? 0 : token2.hashCode())) * 31;
        PendingIntent pendingIntent = this.clickIntent;
        int hashCode9 = (hashCode8 + (pendingIntent == null ? 0 : pendingIntent.hashCode())) * 31;
        MediaDeviceData mediaDeviceData = this.device;
        int hashCode10 = (hashCode9 + (mediaDeviceData == null ? 0 : mediaDeviceData.hashCode())) * 31;
        boolean z3 = this.active;
        if (z3) {
            z3 = true;
        }
        int i3 = (hashCode10 + (z3 ? 1 : 0)) * 31;
        Runnable runnable = this.resumeAction;
        int hashCode11 = (((i3 + (runnable == null ? 0 : runnable.hashCode())) * 31) + Integer.hashCode(this.playbackLocation)) * 31;
        boolean z4 = this.resumption;
        if (z4) {
            z4 = true;
        }
        int i4 = (hashCode11 + (z4 ? 1 : 0)) * 31;
        String str2 = this.notificationKey;
        int hashCode12 = (i4 + (str2 == null ? 0 : str2.hashCode())) * 31;
        boolean z5 = this.hasCheckedForResume;
        if (z5) {
            z5 = true;
        }
        int i5 = (hashCode12 + (z5 ? 1 : 0)) * 31;
        Boolean bool = this.isPlaying;
        if (bool != null) {
            i2 = bool.hashCode();
        }
        int i6 = (i5 + i2) * 31;
        boolean z6 = this.isClearable;
        if (!z6) {
            z2 = z6;
        }
        return ((((((i6 + (z2 ? 1 : 0)) * 31) + Long.hashCode(this.lastActive)) * 31) + this.instanceId.hashCode()) * 31) + Integer.hashCode(this.appUid);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("MediaData(userId=");
        sb.append(this.userId).append(", initialized=").append(this.initialized).append(", app=").append(this.app).append(", appIcon=").append((Object) this.appIcon).append(", artist=").append((Object) this.artist).append(", song=").append((Object) this.song).append(", artwork=").append((Object) this.artwork).append(", actions=").append((Object) this.actions).append(", actionsToShowInCompact=").append((Object) this.actionsToShowInCompact).append(", semanticActions=").append((Object) this.semanticActions).append(", packageName=").append(this.packageName).append(", token=");
        sb.append((Object) this.token).append(", clickIntent=").append((Object) this.clickIntent).append(", device=").append((Object) this.device).append(", active=").append(this.active).append(", resumeAction=").append((Object) this.resumeAction).append(", playbackLocation=").append(this.playbackLocation).append(", resumption=").append(this.resumption).append(", notificationKey=").append(this.notificationKey).append(", hasCheckedForResume=").append(this.hasCheckedForResume).append(", isPlaying=").append((Object) this.isPlaying).append(", isClearable=").append(this.isClearable).append(", lastActive=").append(this.lastActive);
        sb.append(", instanceId=").append((Object) this.instanceId).append(", appUid=").append(this.appUid).append(')');
        return sb.toString();
    }

    public MediaData(int i, boolean z, String str, Icon icon, CharSequence charSequence, CharSequence charSequence2, Icon icon2, List<MediaAction> list, List<Integer> list2, MediaButton mediaButton, String str2, MediaSession.Token token2, PendingIntent pendingIntent, MediaDeviceData mediaDeviceData, boolean z2, Runnable runnable, int i2, boolean z3, String str3, boolean z4, Boolean bool, boolean z5, long j, InstanceId instanceId2, int i3) {
        String str4 = str2;
        InstanceId instanceId3 = instanceId2;
        Intrinsics.checkNotNullParameter(list, "actions");
        Intrinsics.checkNotNullParameter(list2, "actionsToShowInCompact");
        Intrinsics.checkNotNullParameter(str4, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(instanceId3, "instanceId");
        this.userId = i;
        this.initialized = z;
        this.app = str;
        this.appIcon = icon;
        this.artist = charSequence;
        this.song = charSequence2;
        this.artwork = icon2;
        this.actions = list;
        this.actionsToShowInCompact = list2;
        this.semanticActions = mediaButton;
        this.packageName = str4;
        this.token = token2;
        this.clickIntent = pendingIntent;
        this.device = mediaDeviceData;
        this.active = z2;
        this.resumeAction = runnable;
        this.playbackLocation = i2;
        this.resumption = z3;
        this.notificationKey = str3;
        this.hasCheckedForResume = z4;
        this.isPlaying = bool;
        this.isClearable = z5;
        this.lastActive = j;
        this.instanceId = instanceId3;
        this.appUid = i3;
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ MediaData(int r31, boolean r32, java.lang.String r33, android.graphics.drawable.Icon r34, java.lang.CharSequence r35, java.lang.CharSequence r36, android.graphics.drawable.Icon r37, java.util.List r38, java.util.List r39, com.android.systemui.media.MediaButton r40, java.lang.String r41, android.media.session.MediaSession.Token r42, android.app.PendingIntent r43, com.android.systemui.media.MediaDeviceData r44, boolean r45, java.lang.Runnable r46, int r47, boolean r48, java.lang.String r49, boolean r50, java.lang.Boolean r51, boolean r52, long r53, com.android.internal.logging.InstanceId r55, int r56, int r57, kotlin.jvm.internal.DefaultConstructorMarker r58) {
        /*
            r30 = this;
            r0 = r57
            r1 = r0 & 2
            r2 = 0
            if (r1 == 0) goto L_0x0009
            r5 = r2
            goto L_0x000b
        L_0x0009:
            r5 = r32
        L_0x000b:
            r1 = r0 & 512(0x200, float:7.175E-43)
            r3 = 0
            if (r1 == 0) goto L_0x0012
            r13 = r3
            goto L_0x0014
        L_0x0012:
            r13 = r40
        L_0x0014:
            r1 = 65536(0x10000, float:9.18355E-41)
            r1 = r1 & r0
            if (r1 == 0) goto L_0x001c
            r20 = r2
            goto L_0x001e
        L_0x001c:
            r20 = r47
        L_0x001e:
            r1 = 131072(0x20000, float:1.83671E-40)
            r1 = r1 & r0
            if (r1 == 0) goto L_0x0026
            r21 = r2
            goto L_0x0028
        L_0x0026:
            r21 = r48
        L_0x0028:
            r1 = 262144(0x40000, float:3.67342E-40)
            r1 = r1 & r0
            if (r1 == 0) goto L_0x0030
            r22 = r3
            goto L_0x0032
        L_0x0030:
            r22 = r49
        L_0x0032:
            r1 = 524288(0x80000, float:7.34684E-40)
            r1 = r1 & r0
            if (r1 == 0) goto L_0x003a
            r23 = r2
            goto L_0x003c
        L_0x003a:
            r23 = r50
        L_0x003c:
            r1 = 1048576(0x100000, float:1.469368E-39)
            r1 = r1 & r0
            if (r1 == 0) goto L_0x0044
            r24 = r3
            goto L_0x0046
        L_0x0044:
            r24 = r51
        L_0x0046:
            r1 = 2097152(0x200000, float:2.938736E-39)
            r1 = r1 & r0
            if (r1 == 0) goto L_0x004f
            r1 = 1
            r25 = r1
            goto L_0x0051
        L_0x004f:
            r25 = r52
        L_0x0051:
            r1 = 4194304(0x400000, float:5.877472E-39)
            r0 = r0 & r1
            if (r0 == 0) goto L_0x005b
            r0 = 0
            r26 = r0
            goto L_0x005d
        L_0x005b:
            r26 = r53
        L_0x005d:
            r3 = r30
            r4 = r31
            r6 = r33
            r7 = r34
            r8 = r35
            r9 = r36
            r10 = r37
            r11 = r38
            r12 = r39
            r14 = r41
            r15 = r42
            r16 = r43
            r17 = r44
            r18 = r45
            r19 = r46
            r28 = r55
            r29 = r56
            r3.<init>(r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24, r25, r26, r28, r29)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaData.<init>(int, boolean, java.lang.String, android.graphics.drawable.Icon, java.lang.CharSequence, java.lang.CharSequence, android.graphics.drawable.Icon, java.util.List, java.util.List, com.android.systemui.media.MediaButton, java.lang.String, android.media.session.MediaSession$Token, android.app.PendingIntent, com.android.systemui.media.MediaDeviceData, boolean, java.lang.Runnable, int, boolean, java.lang.String, boolean, java.lang.Boolean, boolean, long, com.android.internal.logging.InstanceId, int, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    public final int getUserId() {
        return this.userId;
    }

    public final boolean getInitialized() {
        return this.initialized;
    }

    public final String getApp() {
        return this.app;
    }

    public final Icon getAppIcon() {
        return this.appIcon;
    }

    public final CharSequence getArtist() {
        return this.artist;
    }

    public final CharSequence getSong() {
        return this.song;
    }

    public final Icon getArtwork() {
        return this.artwork;
    }

    public final List<MediaAction> getActions() {
        return this.actions;
    }

    public final List<Integer> getActionsToShowInCompact() {
        return this.actionsToShowInCompact;
    }

    public final MediaButton getSemanticActions() {
        return this.semanticActions;
    }

    public final String getPackageName() {
        return this.packageName;
    }

    public final MediaSession.Token getToken() {
        return this.token;
    }

    public final PendingIntent getClickIntent() {
        return this.clickIntent;
    }

    public final MediaDeviceData getDevice() {
        return this.device;
    }

    public final boolean getActive() {
        return this.active;
    }

    public final void setActive(boolean z) {
        this.active = z;
    }

    public final Runnable getResumeAction() {
        return this.resumeAction;
    }

    public final void setResumeAction(Runnable runnable) {
        this.resumeAction = runnable;
    }

    public final int getPlaybackLocation() {
        return this.playbackLocation;
    }

    public final void setPlaybackLocation(int i) {
        this.playbackLocation = i;
    }

    public final boolean getResumption() {
        return this.resumption;
    }

    public final void setResumption(boolean z) {
        this.resumption = z;
    }

    public final String getNotificationKey() {
        return this.notificationKey;
    }

    public final boolean getHasCheckedForResume() {
        return this.hasCheckedForResume;
    }

    public final void setHasCheckedForResume(boolean z) {
        this.hasCheckedForResume = z;
    }

    public final Boolean isPlaying() {
        return this.isPlaying;
    }

    public final boolean isClearable() {
        return this.isClearable;
    }

    public final long getLastActive() {
        return this.lastActive;
    }

    public final void setLastActive(long j) {
        this.lastActive = j;
    }

    public final InstanceId getInstanceId() {
        return this.instanceId;
    }

    public final int getAppUid() {
        return this.appUid;
    }

    @Metadata(mo64986d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0007"}, mo64987d2 = {"Lcom/android/systemui/media/MediaData$Companion;", "", "()V", "PLAYBACK_CAST_LOCAL", "", "PLAYBACK_CAST_REMOTE", "PLAYBACK_LOCAL", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: MediaData.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final boolean isLocalSession() {
        return this.playbackLocation == 0;
    }
}
