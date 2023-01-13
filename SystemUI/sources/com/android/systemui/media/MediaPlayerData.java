package com.android.systemui.media;

import android.app.PendingIntent;
import android.graphics.drawable.Icon;
import android.media.session.MediaSession;
import com.android.internal.logging.InstanceId;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.util.time.SystemClock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import kotlin.Metadata;
import kotlin.Triple;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000~\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010#\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u001f\n\u0002\b\u0004\bÁ\u0002\u0018\u00002\u00020\u0001:\u00016B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J:\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u000b2\u0006\u0010\u001b\u001a\u00020\u00042\u0006\u0010\u001c\u001a\u00020\u000e2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u00102\n\b\u0002\u0010 \u001a\u0004\u0018\u00010!J:\u0010\"\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u000b2\u0006\u0010\u001b\u001a\u00020\u00142\u0006\u0010\u001c\u001a\u00020\u000e2\u0006\u0010#\u001a\u00020\u00102\u0006\u0010\u001d\u001a\u00020\u001e2\n\b\u0002\u0010 \u001a\u0004\u0018\u00010!J\b\u0010$\u001a\u00020\u0019H\u0007J\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u000b0&J\u0006\u0010'\u001a\u00020(J\u0010\u0010)\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u001a\u001a\u00020\u000bJ\u000e\u0010*\u001a\u00020(2\u0006\u0010\u001a\u001a\u00020\u000bJ\u0006\u0010+\u001a\u00020\u0010J\u000e\u0010\u001f\u001a\u00020\u00102\u0006\u0010\u001a\u001a\u00020\u000bJ\u001e\u0010\t\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00100-0,J$\u0010.\u001a\u00020\u00192\b\u0010/\u001a\u0004\u0018\u00010\u000b2\u0006\u00100\u001a\u00020\u000b2\n\b\u0002\u0010 \u001a\u0004\u0018\u00010!J\f\u00101\u001a\b\u0012\u0004\u0012\u00020\u00070&J\f\u00102\u001a\b\u0012\u0004\u0012\u00020\u000e03J\u0010\u00104\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u001a\u001a\u00020\u000bJ\b\u00105\u001a\u0004\u0018\u00010\u000bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u0005\u001a\u0012\u0012\u0004\u0012\u00020\u00070\u0006j\b\u0012\u0004\u0012\u00020\u0007`\bX\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00070\nX\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u000e0\rX\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u0010@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\"\u0010\u0015\u001a\u0004\u0018\u00010\u00142\b\u0010\u000f\u001a\u0004\u0018\u00010\u0014@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017¨\u00067"}, mo65043d2 = {"Lcom/android/systemui/media/MediaPlayerData;", "", "()V", "EMPTY", "Lcom/android/systemui/media/MediaData;", "comparator", "Ljava/util/Comparator;", "Lcom/android/systemui/media/MediaPlayerData$MediaSortKey;", "Lkotlin/Comparator;", "mediaData", "", "", "mediaPlayers", "Ljava/util/TreeMap;", "Lcom/android/systemui/media/MediaControlPanel;", "<set-?>", "", "shouldPrioritizeSs", "getShouldPrioritizeSs$SystemUI_nothingRelease", "()Z", "Lcom/android/systemui/media/SmartspaceMediaData;", "smartspaceMediaData", "getSmartspaceMediaData$SystemUI_nothingRelease", "()Lcom/android/systemui/media/SmartspaceMediaData;", "addMediaPlayer", "", "key", "data", "player", "clock", "Lcom/android/systemui/util/time/SystemClock;", "isSsReactivated", "debugLogger", "Lcom/android/systemui/media/MediaCarouselControllerLogger;", "addMediaRecommendation", "shouldPrioritize", "clear", "dataKeys", "", "firstActiveMediaIndex", "", "getMediaPlayer", "getMediaPlayerIndex", "hasActiveMediaOrRecommendationCard", "", "Lkotlin/Triple;", "moveIfExists", "oldKey", "newKey", "playerKeys", "players", "", "removeMediaPlayer", "smartspaceMediaKey", "MediaSortKey", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaCarouselController.kt */
public final class MediaPlayerData {
    private static final MediaData EMPTY;
    public static final MediaPlayerData INSTANCE = new MediaPlayerData();
    private static final Comparator<MediaSortKey> comparator;
    private static final Map<String, MediaSortKey> mediaData = new LinkedHashMap();
    private static final TreeMap<MediaSortKey, MediaControlPanel> mediaPlayers;
    private static boolean shouldPrioritizeSs;
    private static SmartspaceMediaData smartspaceMediaData;

    private MediaPlayerData() {
    }

    static {
        List emptyList = CollectionsKt.emptyList();
        List emptyList2 = CollectionsKt.emptyList();
        InstanceId fakeInstanceId = InstanceId.fakeInstanceId(-1);
        Intrinsics.checkNotNullExpressionValue(fakeInstanceId, "fakeInstanceId(-1)");
        EMPTY = new MediaData(-1, false, (String) null, (Icon) null, (CharSequence) null, (CharSequence) null, (Icon) null, emptyList, emptyList2, (MediaButton) null, "INVALID", (MediaSession.Token) null, (PendingIntent) null, (MediaDeviceData) null, true, (Runnable) null, 0, false, (String) null, false, (Boolean) null, false, 0, fakeInstanceId, -1, 8323584, (DefaultConstructorMarker) null);
        Comparator<MediaSortKey> mediaPlayerData$special$$inlined$thenByDescending$8 = new MediaPlayerData$special$$inlined$thenByDescending$8<>(new MediaPlayerData$special$$inlined$thenByDescending$7(new MediaPlayerData$special$$inlined$thenByDescending$6(new MediaPlayerData$special$$inlined$thenByDescending$5(new MediaPlayerData$special$$inlined$thenByDescending$4(new MediaPlayerData$special$$inlined$thenByDescending$3(new MediaPlayerData$special$$inlined$thenByDescending$2(new MediaPlayerData$special$$inlined$thenByDescending$1(new MediaPlayerData$special$$inlined$compareByDescending$1()))))))));
        comparator = mediaPlayerData$special$$inlined$thenByDescending$8;
        mediaPlayers = new TreeMap<>(mediaPlayerData$special$$inlined$thenByDescending$8);
    }

    public final boolean getShouldPrioritizeSs$SystemUI_nothingRelease() {
        return shouldPrioritizeSs;
    }

    public final SmartspaceMediaData getSmartspaceMediaData$SystemUI_nothingRelease() {
        return smartspaceMediaData;
    }

    @Metadata(mo65042d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u000f\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B)\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003¢\u0006\u0002\u0010\tJ\t\u0010\u000f\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0007HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0003HÆ\u0003J1\u0010\u0013\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0014\u001a\u00020\u00032\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0016\u001a\u00020\u0017HÖ\u0001J\t\u0010\u0018\u001a\u00020\u0019HÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\fR\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\fR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e¨\u0006\u001a"}, mo65043d2 = {"Lcom/android/systemui/media/MediaPlayerData$MediaSortKey;", "", "isSsMediaRec", "", "data", "Lcom/android/systemui/media/MediaData;", "updateTime", "", "isSsReactivated", "(ZLcom/android/systemui/media/MediaData;JZ)V", "getData", "()Lcom/android/systemui/media/MediaData;", "()Z", "getUpdateTime", "()J", "component1", "component2", "component3", "component4", "copy", "equals", "other", "hashCode", "", "toString", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: MediaCarouselController.kt */
    public static final class MediaSortKey {
        private final MediaData data;
        private final boolean isSsMediaRec;
        private final boolean isSsReactivated;
        private final long updateTime;

        public static /* synthetic */ MediaSortKey copy$default(MediaSortKey mediaSortKey, boolean z, MediaData mediaData, long j, boolean z2, int i, Object obj) {
            if ((i & 1) != 0) {
                z = mediaSortKey.isSsMediaRec;
            }
            if ((i & 2) != 0) {
                mediaData = mediaSortKey.data;
            }
            MediaData mediaData2 = mediaData;
            if ((i & 4) != 0) {
                j = mediaSortKey.updateTime;
            }
            long j2 = j;
            if ((i & 8) != 0) {
                z2 = mediaSortKey.isSsReactivated;
            }
            return mediaSortKey.copy(z, mediaData2, j2, z2);
        }

        public final boolean component1() {
            return this.isSsMediaRec;
        }

        public final MediaData component2() {
            return this.data;
        }

        public final long component3() {
            return this.updateTime;
        }

        public final boolean component4() {
            return this.isSsReactivated;
        }

        public final MediaSortKey copy(boolean z, MediaData mediaData, long j, boolean z2) {
            Intrinsics.checkNotNullParameter(mediaData, "data");
            return new MediaSortKey(z, mediaData, j, z2);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof MediaSortKey)) {
                return false;
            }
            MediaSortKey mediaSortKey = (MediaSortKey) obj;
            return this.isSsMediaRec == mediaSortKey.isSsMediaRec && Intrinsics.areEqual((Object) this.data, (Object) mediaSortKey.data) && this.updateTime == mediaSortKey.updateTime && this.isSsReactivated == mediaSortKey.isSsReactivated;
        }

        public int hashCode() {
            boolean z = this.isSsMediaRec;
            boolean z2 = true;
            if (z) {
                z = true;
            }
            int hashCode = (((((z ? 1 : 0) * true) + this.data.hashCode()) * 31) + Long.hashCode(this.updateTime)) * 31;
            boolean z3 = this.isSsReactivated;
            if (!z3) {
                z2 = z3;
            }
            return hashCode + (z2 ? 1 : 0);
        }

        public String toString() {
            return "MediaSortKey(isSsMediaRec=" + this.isSsMediaRec + ", data=" + this.data + ", updateTime=" + this.updateTime + ", isSsReactivated=" + this.isSsReactivated + ')';
        }

        public MediaSortKey(boolean z, MediaData mediaData, long j, boolean z2) {
            Intrinsics.checkNotNullParameter(mediaData, "data");
            this.isSsMediaRec = z;
            this.data = mediaData;
            this.updateTime = j;
            this.isSsReactivated = z2;
        }

        /* JADX INFO: this call moved to the top of the method (can break code semantics) */
        public /* synthetic */ MediaSortKey(boolean z, MediaData mediaData, long j, boolean z2, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this(z, mediaData, (i & 4) != 0 ? 0 : j, (i & 8) != 0 ? false : z2);
        }

        public final boolean isSsMediaRec() {
            return this.isSsMediaRec;
        }

        public final MediaData getData() {
            return this.data;
        }

        public final long getUpdateTime() {
            return this.updateTime;
        }

        public final boolean isSsReactivated() {
            return this.isSsReactivated;
        }
    }

    public static /* synthetic */ void addMediaPlayer$default(MediaPlayerData mediaPlayerData, String str, MediaData mediaData2, MediaControlPanel mediaControlPanel, SystemClock systemClock, boolean z, MediaCarouselControllerLogger mediaCarouselControllerLogger, int i, Object obj) {
        if ((i & 32) != 0) {
            mediaCarouselControllerLogger = null;
        }
        mediaPlayerData.addMediaPlayer(str, mediaData2, mediaControlPanel, systemClock, z, mediaCarouselControllerLogger);
    }

    public final void addMediaPlayer(String str, MediaData mediaData2, MediaControlPanel mediaControlPanel, SystemClock systemClock, boolean z, MediaCarouselControllerLogger mediaCarouselControllerLogger) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(mediaData2, "data");
        Intrinsics.checkNotNullParameter(mediaControlPanel, "player");
        Intrinsics.checkNotNullParameter(systemClock, DemoMode.COMMAND_CLOCK);
        MediaControlPanel removeMediaPlayer = removeMediaPlayer(str);
        if (!(removeMediaPlayer == null || Intrinsics.areEqual((Object) removeMediaPlayer, (Object) mediaControlPanel) || mediaCarouselControllerLogger == null)) {
            mediaCarouselControllerLogger.logPotentialMemoryLeak(str);
        }
        MediaSortKey mediaSortKey = new MediaSortKey(false, mediaData2, systemClock.currentTimeMillis(), z);
        mediaData.put(str, mediaSortKey);
        mediaPlayers.put(mediaSortKey, mediaControlPanel);
    }

    public static /* synthetic */ void addMediaRecommendation$default(MediaPlayerData mediaPlayerData, String str, SmartspaceMediaData smartspaceMediaData2, MediaControlPanel mediaControlPanel, boolean z, SystemClock systemClock, MediaCarouselControllerLogger mediaCarouselControllerLogger, int i, Object obj) {
        if ((i & 32) != 0) {
            mediaCarouselControllerLogger = null;
        }
        mediaPlayerData.addMediaRecommendation(str, smartspaceMediaData2, mediaControlPanel, z, systemClock, mediaCarouselControllerLogger);
    }

    public final void addMediaRecommendation(String str, SmartspaceMediaData smartspaceMediaData2, MediaControlPanel mediaControlPanel, boolean z, SystemClock systemClock, MediaCarouselControllerLogger mediaCarouselControllerLogger) {
        String str2 = str;
        SmartspaceMediaData smartspaceMediaData3 = smartspaceMediaData2;
        MediaControlPanel mediaControlPanel2 = mediaControlPanel;
        MediaCarouselControllerLogger mediaCarouselControllerLogger2 = mediaCarouselControllerLogger;
        Intrinsics.checkNotNullParameter(str2, "key");
        Intrinsics.checkNotNullParameter(smartspaceMediaData3, "data");
        Intrinsics.checkNotNullParameter(mediaControlPanel2, "player");
        Intrinsics.checkNotNullParameter(systemClock, DemoMode.COMMAND_CLOCK);
        shouldPrioritizeSs = z;
        MediaControlPanel removeMediaPlayer = removeMediaPlayer(str);
        if (!(removeMediaPlayer == null || Intrinsics.areEqual((Object) removeMediaPlayer, (Object) mediaControlPanel2) || mediaCarouselControllerLogger2 == null)) {
            mediaCarouselControllerLogger2.logPotentialMemoryLeak(str2);
        }
        MediaSortKey mediaSortKey = new MediaSortKey(true, MediaData.copy$default(EMPTY, 0, false, (String) null, (Icon) null, (CharSequence) null, (CharSequence) null, (Icon) null, (List) null, (List) null, (MediaButton) null, (String) null, (MediaSession.Token) null, (PendingIntent) null, (MediaDeviceData) null, false, (Runnable) null, 0, false, (String) null, false, false, false, 0, (InstanceId) null, 0, 32505855, (Object) null), systemClock.currentTimeMillis(), true);
        mediaData.put(str2, mediaSortKey);
        mediaPlayers.put(mediaSortKey, mediaControlPanel2);
        smartspaceMediaData = smartspaceMediaData3;
    }

    public static /* synthetic */ void moveIfExists$default(MediaPlayerData mediaPlayerData, String str, String str2, MediaCarouselControllerLogger mediaCarouselControllerLogger, int i, Object obj) {
        if ((i & 4) != 0) {
            mediaCarouselControllerLogger = null;
        }
        mediaPlayerData.moveIfExists(str, str2, mediaCarouselControllerLogger);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000f, code lost:
        r1 = mediaData;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void moveIfExists(java.lang.String r2, java.lang.String r3, com.android.systemui.media.MediaCarouselControllerLogger r4) {
        /*
            r1 = this;
            java.lang.String r1 = "newKey"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r3, r1)
            if (r2 == 0) goto L_0x002c
            boolean r1 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r2, (java.lang.Object) r3)
            if (r1 == 0) goto L_0x000f
            goto L_0x002c
        L_0x000f:
            java.util.Map<java.lang.String, com.android.systemui.media.MediaPlayerData$MediaSortKey> r1 = mediaData
            java.lang.Object r2 = r1.remove(r2)
            com.android.systemui.media.MediaPlayerData$MediaSortKey r2 = (com.android.systemui.media.MediaPlayerData.MediaSortKey) r2
            if (r2 == 0) goto L_0x002c
            com.android.systemui.media.MediaPlayerData r0 = INSTANCE
            com.android.systemui.media.MediaControlPanel r0 = r0.removeMediaPlayer(r3)
            if (r0 == 0) goto L_0x0026
            if (r4 == 0) goto L_0x0026
            r4.logPotentialMemoryLeak(r3)
        L_0x0026:
            java.lang.Object r1 = r1.put(r3, r2)
            com.android.systemui.media.MediaPlayerData$MediaSortKey r1 = (com.android.systemui.media.MediaPlayerData.MediaSortKey) r1
        L_0x002c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaPlayerData.moveIfExists(java.lang.String, java.lang.String, com.android.systemui.media.MediaCarouselControllerLogger):void");
    }

    public final MediaControlPanel getMediaPlayer(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        MediaSortKey mediaSortKey = mediaData.get(str);
        if (mediaSortKey != null) {
            return mediaPlayers.get(mediaSortKey);
        }
        return null;
    }

    public final int getMediaPlayerIndex(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        MediaSortKey mediaSortKey = mediaData.get(str);
        Set<Map.Entry<MediaSortKey, MediaControlPanel>> entrySet = mediaPlayers.entrySet();
        Intrinsics.checkNotNullExpressionValue(entrySet, "mediaPlayers.entries");
        int i = 0;
        for (Object next : entrySet) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            if (Intrinsics.areEqual(((Map.Entry) next).getKey(), (Object) mediaSortKey)) {
                return i;
            }
            i = i2;
        }
        return -1;
    }

    public final MediaControlPanel removeMediaPlayer(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        MediaSortKey remove = mediaData.remove(str);
        if (remove == null) {
            return null;
        }
        if (remove.isSsMediaRec()) {
            smartspaceMediaData = null;
        }
        return mediaPlayers.remove(remove);
    }

    public final List<Triple<String, MediaData, Boolean>> mediaData() {
        Iterable<Map.Entry> entrySet = mediaData.entrySet();
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(entrySet, 10));
        for (Map.Entry entry : entrySet) {
            arrayList.add(new Triple(entry.getKey(), ((MediaSortKey) entry.getValue()).getData(), Boolean.valueOf(((MediaSortKey) entry.getValue()).isSsMediaRec())));
        }
        return (List) arrayList;
    }

    public final Set<String> dataKeys() {
        return mediaData.keySet();
    }

    public final Collection<MediaControlPanel> players() {
        Collection<MediaControlPanel> values = mediaPlayers.values();
        Intrinsics.checkNotNullExpressionValue(values, "mediaPlayers.values");
        return values;
    }

    public final Set<MediaSortKey> playerKeys() {
        Set<MediaSortKey> keySet = mediaPlayers.keySet();
        Intrinsics.checkNotNullExpressionValue(keySet, "mediaPlayers.keys");
        return keySet;
    }

    public final int firstActiveMediaIndex() {
        Set<Map.Entry<MediaSortKey, MediaControlPanel>> entrySet = mediaPlayers.entrySet();
        Intrinsics.checkNotNullExpressionValue(entrySet, "mediaPlayers.entries");
        int i = 0;
        for (Object next : entrySet) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            Map.Entry entry = (Map.Entry) next;
            if (!((MediaSortKey) entry.getKey()).isSsMediaRec() && ((MediaSortKey) entry.getKey()).getData().getActive()) {
                return i;
            }
            i = i2;
        }
        return -1;
    }

    public final String smartspaceMediaKey() {
        for (Map.Entry entry : mediaData.entrySet()) {
            if (((MediaSortKey) entry.getValue()).isSsMediaRec()) {
                return (String) entry.getKey();
            }
        }
        return null;
    }

    public final void clear() {
        mediaData.clear();
        mediaPlayers.clear();
    }

    public final boolean hasActiveMediaOrRecommendationCard() {
        SmartspaceMediaData smartspaceMediaData2 = smartspaceMediaData;
        if (smartspaceMediaData2 != null) {
            Boolean valueOf = smartspaceMediaData2 != null ? Boolean.valueOf(smartspaceMediaData2.isActive()) : null;
            Intrinsics.checkNotNull(valueOf);
            if (valueOf.booleanValue()) {
                return true;
            }
        }
        if (firstActiveMediaIndex() != -1) {
            return true;
        }
        return false;
    }

    public final boolean isSsReactivated(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        MediaSortKey mediaSortKey = mediaData.get(str);
        if (mediaSortKey != null) {
            return mediaSortKey.isSsReactivated();
        }
        return false;
    }
}
