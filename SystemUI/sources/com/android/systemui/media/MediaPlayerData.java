package com.android.systemui.media;

import com.android.systemui.media.MediaPlayerData;
import com.android.systemui.util.time.SystemClock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import kotlin.Triple;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: MediaCarouselController.kt */
/* loaded from: classes.dex */
public final class MediaPlayerData {
    @NotNull
    private static final MediaData EMPTY;
    @NotNull
    private static final Comparator<MediaSortKey> comparator;
    @NotNull
    private static final TreeMap<MediaSortKey, MediaControlPanel> mediaPlayers;
    private static boolean shouldPrioritizeSs;
    @Nullable
    private static SmartspaceMediaData smartspaceMediaData;
    @NotNull
    public static final MediaPlayerData INSTANCE = new MediaPlayerData();
    @NotNull
    private static final Map<String, MediaSortKey> mediaData = new LinkedHashMap();

    private MediaPlayerData() {
    }

    static {
        List emptyList;
        List emptyList2;
        emptyList = CollectionsKt__CollectionsKt.emptyList();
        emptyList2 = CollectionsKt__CollectionsKt.emptyList();
        EMPTY = new MediaData(-1, false, 0, null, null, null, null, null, emptyList, emptyList2, "INVALID", null, null, null, true, null, false, false, null, false, null, false, 0L, 8323072, null);
        final Comparator<T> comparator2 = new Comparator<T>() { // from class: com.android.systemui.media.MediaPlayerData$special$$inlined$compareByDescending$1
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int compareValues;
                compareValues = ComparisonsKt__ComparisonsKt.compareValues(((MediaPlayerData.MediaSortKey) t2).getData().isPlaying(), ((MediaPlayerData.MediaSortKey) t).getData().isPlaying());
                return compareValues;
            }
        };
        final Comparator<T> comparator3 = new Comparator<T>() { // from class: com.android.systemui.media.MediaPlayerData$special$$inlined$thenByDescending$1
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                MediaPlayerData.MediaSortKey mediaSortKey;
                int compareValues;
                int compare = comparator2.compare(t, t2);
                if (compare != 0) {
                    return compare;
                }
                MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
                boolean shouldPrioritizeSs$frameworks__base__packages__SystemUI__android_common__SystemUI_core = mediaPlayerData.getShouldPrioritizeSs$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
                boolean isSsMediaRec = ((MediaPlayerData.MediaSortKey) t2).isSsMediaRec();
                if (!shouldPrioritizeSs$frameworks__base__packages__SystemUI__android_common__SystemUI_core) {
                    isSsMediaRec = !isSsMediaRec;
                }
                compareValues = ComparisonsKt__ComparisonsKt.compareValues(Boolean.valueOf(isSsMediaRec), Boolean.valueOf(mediaPlayerData.getShouldPrioritizeSs$frameworks__base__packages__SystemUI__android_common__SystemUI_core() ? ((MediaPlayerData.MediaSortKey) t).isSsMediaRec() : !mediaSortKey.isSsMediaRec()));
                return compareValues;
            }
        };
        final Comparator<T> comparator4 = new Comparator<T>() { // from class: com.android.systemui.media.MediaPlayerData$special$$inlined$thenByDescending$2
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int compareValues;
                int compare = comparator3.compare(t, t2);
                if (compare != 0) {
                    return compare;
                }
                compareValues = ComparisonsKt__ComparisonsKt.compareValues(Boolean.valueOf(((MediaPlayerData.MediaSortKey) t2).getData().isLocalSession()), Boolean.valueOf(((MediaPlayerData.MediaSortKey) t).getData().isLocalSession()));
                return compareValues;
            }
        };
        final Comparator<T> comparator5 = new Comparator<T>() { // from class: com.android.systemui.media.MediaPlayerData$special$$inlined$thenByDescending$3
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int compareValues;
                int compare = comparator4.compare(t, t2);
                if (compare != 0) {
                    return compare;
                }
                compareValues = ComparisonsKt__ComparisonsKt.compareValues(Boolean.valueOf(!((MediaPlayerData.MediaSortKey) t2).getData().getResumption()), Boolean.valueOf(!((MediaPlayerData.MediaSortKey) t).getData().getResumption()));
                return compareValues;
            }
        };
        Comparator comparator6 = new Comparator<T>() { // from class: com.android.systemui.media.MediaPlayerData$special$$inlined$thenByDescending$4
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int compareValues;
                int compare = comparator5.compare(t, t2);
                if (compare != 0) {
                    return compare;
                }
                compareValues = ComparisonsKt__ComparisonsKt.compareValues(Long.valueOf(((MediaPlayerData.MediaSortKey) t2).getUpdateTime()), Long.valueOf(((MediaPlayerData.MediaSortKey) t).getUpdateTime()));
                return compareValues;
            }
        };
        comparator = comparator6;
        mediaPlayers = new TreeMap<>(comparator6);
    }

    public final boolean getShouldPrioritizeSs$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return shouldPrioritizeSs;
    }

    @Nullable
    public final SmartspaceMediaData getSmartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return smartspaceMediaData;
    }

    /* compiled from: MediaCarouselController.kt */
    /* loaded from: classes.dex */
    public static final class MediaSortKey {
        @NotNull
        private final MediaData data;
        private final boolean isSsMediaRec;
        private final long updateTime;

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof MediaSortKey)) {
                return false;
            }
            MediaSortKey mediaSortKey = (MediaSortKey) obj;
            return this.isSsMediaRec == mediaSortKey.isSsMediaRec && Intrinsics.areEqual(this.data, mediaSortKey.data) && this.updateTime == mediaSortKey.updateTime;
        }

        public int hashCode() {
            boolean z = this.isSsMediaRec;
            if (z) {
                z = true;
            }
            int i = z ? 1 : 0;
            int i2 = z ? 1 : 0;
            return (((i * 31) + this.data.hashCode()) * 31) + Long.hashCode(this.updateTime);
        }

        @NotNull
        public String toString() {
            return "MediaSortKey(isSsMediaRec=" + this.isSsMediaRec + ", data=" + this.data + ", updateTime=" + this.updateTime + ')';
        }

        public MediaSortKey(boolean z, @NotNull MediaData data, long j) {
            Intrinsics.checkNotNullParameter(data, "data");
            this.isSsMediaRec = z;
            this.data = data;
            this.updateTime = j;
        }

        public final boolean isSsMediaRec() {
            return this.isSsMediaRec;
        }

        @NotNull
        public final MediaData getData() {
            return this.data;
        }

        public final long getUpdateTime() {
            return this.updateTime;
        }
    }

    public final void addMediaPlayer(@NotNull String key, @NotNull MediaData data, @NotNull MediaControlPanel player, @NotNull SystemClock clock) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(data, "data");
        Intrinsics.checkNotNullParameter(player, "player");
        Intrinsics.checkNotNullParameter(clock, "clock");
        removeMediaPlayer(key);
        MediaSortKey mediaSortKey = new MediaSortKey(false, data, clock.currentTimeMillis());
        mediaData.put(key, mediaSortKey);
        mediaPlayers.put(mediaSortKey, player);
    }

    public final void addMediaRecommendation(@NotNull String key, @NotNull SmartspaceMediaData data, @NotNull MediaControlPanel player, boolean z, @NotNull SystemClock clock) {
        MediaData copy;
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(data, "data");
        Intrinsics.checkNotNullParameter(player, "player");
        Intrinsics.checkNotNullParameter(clock, "clock");
        shouldPrioritizeSs = z;
        removeMediaPlayer(key);
        copy = r5.copy((i3 & 1) != 0 ? r5.userId : 0, (i3 & 2) != 0 ? r5.initialized : false, (i3 & 4) != 0 ? r5.backgroundColor : 0, (i3 & 8) != 0 ? r5.app : null, (i3 & 16) != 0 ? r5.appIcon : null, (i3 & 32) != 0 ? r5.artist : null, (i3 & 64) != 0 ? r5.song : null, (i3 & 128) != 0 ? r5.artwork : null, (i3 & 256) != 0 ? r5.actions : null, (i3 & 512) != 0 ? r5.actionsToShowInCompact : null, (i3 & 1024) != 0 ? r5.packageName : null, (i3 & 2048) != 0 ? r5.token : null, (i3 & 4096) != 0 ? r5.clickIntent : null, (i3 & 8192) != 0 ? r5.device : null, (i3 & 16384) != 0 ? r5.active : false, (i3 & 32768) != 0 ? r5.resumeAction : null, (i3 & 65536) != 0 ? r5.isLocalSession : false, (i3 & 131072) != 0 ? r5.resumption : false, (i3 & 262144) != 0 ? r5.notificationKey : null, (i3 & 524288) != 0 ? r5.hasCheckedForResume : false, (i3 & 1048576) != 0 ? r5.isPlaying : Boolean.FALSE, (i3 & 2097152) != 0 ? r5.isClearable : false, (i3 & 4194304) != 0 ? EMPTY.lastActive : 0L);
        MediaSortKey mediaSortKey = new MediaSortKey(true, copy, clock.currentTimeMillis());
        mediaData.put(key, mediaSortKey);
        mediaPlayers.put(mediaSortKey, player);
        smartspaceMediaData = data;
    }

    public final void moveIfExists(@Nullable String str, @NotNull String newKey) {
        Map<String, MediaSortKey> map;
        MediaSortKey remove;
        Intrinsics.checkNotNullParameter(newKey, "newKey");
        if (str == null || Intrinsics.areEqual(str, newKey) || (remove = (map = mediaData).remove(str)) == null) {
            return;
        }
        INSTANCE.removeMediaPlayer(newKey);
        map.put(newKey, remove);
    }

    @Nullable
    public final MediaControlPanel getMediaPlayer(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        MediaSortKey mediaSortKey = mediaData.get(key);
        if (mediaSortKey == null) {
            return null;
        }
        return mediaPlayers.get(mediaSortKey);
    }

    public final int getMediaPlayerIndex(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        MediaSortKey mediaSortKey = mediaData.get(key);
        Set<Map.Entry<MediaSortKey, MediaControlPanel>> entrySet = mediaPlayers.entrySet();
        Intrinsics.checkNotNullExpressionValue(entrySet, "mediaPlayers.entries");
        int i = 0;
        for (Object obj : entrySet) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt__CollectionsKt.throwIndexOverflow();
            }
            if (Intrinsics.areEqual(((Map.Entry) obj).getKey(), mediaSortKey)) {
                return i;
            }
            i = i2;
        }
        return -1;
    }

    @Nullable
    public final MediaControlPanel removeMediaPlayer(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        MediaSortKey remove = mediaData.remove(key);
        if (remove == null) {
            return null;
        }
        if (remove.isSsMediaRec()) {
            smartspaceMediaData = null;
        }
        return mediaPlayers.remove(remove);
    }

    @NotNull
    public final List<Triple<String, MediaData, Boolean>> mediaData() {
        int collectionSizeOrDefault;
        Set<Map.Entry<String, MediaSortKey>> entrySet = mediaData.entrySet();
        collectionSizeOrDefault = CollectionsKt__IterablesKt.collectionSizeOrDefault(entrySet, 10);
        ArrayList arrayList = new ArrayList(collectionSizeOrDefault);
        Iterator<T> it = entrySet.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            arrayList.add(new Triple(entry.getKey(), ((MediaSortKey) entry.getValue()).getData(), Boolean.valueOf(((MediaSortKey) entry.getValue()).isSsMediaRec())));
        }
        return arrayList;
    }

    @NotNull
    public final Collection<MediaControlPanel> players() {
        Collection<MediaControlPanel> values = mediaPlayers.values();
        Intrinsics.checkNotNullExpressionValue(values, "mediaPlayers.values");
        return values;
    }

    @NotNull
    public final Set<MediaSortKey> playerKeys() {
        Set<MediaSortKey> keySet = mediaPlayers.keySet();
        Intrinsics.checkNotNullExpressionValue(keySet, "mediaPlayers.keys");
        return keySet;
    }

    public final int firstActiveMediaIndex() {
        Set<Map.Entry<MediaSortKey, MediaControlPanel>> entrySet = mediaPlayers.entrySet();
        Intrinsics.checkNotNullExpressionValue(entrySet, "mediaPlayers.entries");
        int i = 0;
        for (Object obj : entrySet) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt__CollectionsKt.throwIndexOverflow();
            }
            Map.Entry entry = (Map.Entry) obj;
            if (!((MediaSortKey) entry.getKey()).isSsMediaRec() && ((MediaSortKey) entry.getKey()).getData().getActive()) {
                return i;
            }
            i = i2;
        }
        return -1;
    }

    @Nullable
    public final String smartspaceMediaKey() {
        Iterator<T> it = mediaData.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
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
            Boolean valueOf = smartspaceMediaData2 == null ? null : Boolean.valueOf(smartspaceMediaData2.isActive());
            Intrinsics.checkNotNull(valueOf);
            if (valueOf.booleanValue()) {
                return true;
            }
        }
        return firstActiveMediaIndex() != -1;
    }
}
