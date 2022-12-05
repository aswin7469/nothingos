package com.android.systemui.media;

import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.MediaDeviceManager;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: MediaDataCombineLatest.kt */
/* loaded from: classes.dex */
public final class MediaDataCombineLatest implements MediaDataManager.Listener, MediaDeviceManager.Listener {
    @NotNull
    private final Set<MediaDataManager.Listener> listeners = new LinkedHashSet();
    @NotNull
    private final Map<String, Pair<MediaData, MediaDeviceData>> entries = new LinkedHashMap();

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onMediaDataLoaded(@NotNull String key, @Nullable String str, @NotNull MediaData data, boolean z, boolean z2) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(data, "data");
        MediaDeviceData mediaDeviceData = null;
        if (str != null && !Intrinsics.areEqual(str, key) && this.entries.containsKey(str)) {
            Map<String, Pair<MediaData, MediaDeviceData>> map = this.entries;
            Pair<MediaData, MediaDeviceData> remove = map.remove(str);
            if (remove != null) {
                mediaDeviceData = remove.getSecond();
            }
            map.put(key, TuplesKt.to(data, mediaDeviceData));
            update(key, str);
            return;
        }
        Map<String, Pair<MediaData, MediaDeviceData>> map2 = this.entries;
        Pair<MediaData, MediaDeviceData> pair = map2.get(key);
        if (pair != null) {
            mediaDeviceData = pair.getSecond();
        }
        map2.put(key, TuplesKt.to(data, mediaDeviceData));
        update(key, key);
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onSmartspaceMediaDataLoaded(@NotNull String key, @NotNull SmartspaceMediaData data, boolean z) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(data, "data");
        for (MediaDataManager.Listener listener : CollectionsKt.toSet(this.listeners)) {
            MediaDataManager.Listener.DefaultImpls.onSmartspaceMediaDataLoaded$default(listener, key, data, false, 4, null);
        }
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onMediaDataRemoved(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        remove(key);
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onSmartspaceMediaDataRemoved(@NotNull String key, boolean z) {
        Intrinsics.checkNotNullParameter(key, "key");
        for (MediaDataManager.Listener listener : CollectionsKt.toSet(this.listeners)) {
            listener.onSmartspaceMediaDataRemoved(key, z);
        }
    }

    @Override // com.android.systemui.media.MediaDeviceManager.Listener
    public void onMediaDeviceChanged(@NotNull String key, @Nullable String str, @Nullable MediaDeviceData mediaDeviceData) {
        Intrinsics.checkNotNullParameter(key, "key");
        MediaData mediaData = null;
        if (str != null && !Intrinsics.areEqual(str, key) && this.entries.containsKey(str)) {
            Map<String, Pair<MediaData, MediaDeviceData>> map = this.entries;
            Pair<MediaData, MediaDeviceData> remove = map.remove(str);
            if (remove != null) {
                mediaData = remove.getFirst();
            }
            map.put(key, TuplesKt.to(mediaData, mediaDeviceData));
            update(key, str);
            return;
        }
        Map<String, Pair<MediaData, MediaDeviceData>> map2 = this.entries;
        Pair<MediaData, MediaDeviceData> pair = map2.get(key);
        if (pair != null) {
            mediaData = pair.getFirst();
        }
        map2.put(key, TuplesKt.to(mediaData, mediaDeviceData));
        update(key, key);
    }

    @Override // com.android.systemui.media.MediaDeviceManager.Listener
    public void onKeyRemoved(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        remove(key);
    }

    public final boolean addListener(@NotNull MediaDataManager.Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        return this.listeners.add(listener);
    }

    private final void update(String str, String str2) {
        Pair<MediaData, MediaDeviceData> pair = this.entries.get(str);
        if (pair == null) {
            pair = TuplesKt.to(null, null);
        }
        MediaData component1 = pair.component1();
        MediaDeviceData component2 = pair.component2();
        if (component1 == null || component2 == null) {
            return;
        }
        MediaData copy$default = MediaData.copy$default(component1, 0, false, 0, null, null, null, null, null, null, null, null, null, null, component2, false, null, false, false, null, false, null, false, 0L, 8380415, null);
        for (MediaDataManager.Listener listener : CollectionsKt.toSet(this.listeners)) {
            MediaDataManager.Listener.DefaultImpls.onMediaDataLoaded$default(listener, str, str2, copy$default, false, false, 24, null);
        }
    }

    private final void remove(String str) {
        if (this.entries.remove(str) == null) {
            return;
        }
        for (MediaDataManager.Listener listener : CollectionsKt.toSet(this.listeners)) {
            listener.onMediaDataRemoved(str);
        }
    }
}
