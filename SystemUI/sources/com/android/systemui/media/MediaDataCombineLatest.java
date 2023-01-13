package com.android.systemui.media;

import android.app.PendingIntent;
import android.graphics.drawable.Icon;
import android.media.session.MediaSession;
import com.android.internal.logging.InstanceId;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.MediaDeviceManager;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010#\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u00012\u00020\u0002B\u0007\b\u0007¢\u0006\u0002\u0010\u0003J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0001J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0006H\u0016J:\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00062\b\u0010\u0013\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\r2\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\rH\u0016J\u0010\u0010\u0019\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0006H\u0016J$\u0010\u001a\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00062\b\u0010\u0013\u001a\u0004\u0018\u00010\u00062\b\u0010\u0014\u001a\u0004\u0018\u00010\tH\u0016J \u0010\u001b\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0014\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\rH\u0016J\u0018\u0010\u001e\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0015\u001a\u00020\rH\u0016J\u0010\u0010\u001f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0006H\u0002J\u000e\u0010 \u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0001J\u001a\u0010!\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00062\b\u0010\u0013\u001a\u0004\u0018\u00010\u0006H\u0002R*\u0010\u0004\u001a\u001e\u0012\u0004\u0012\u00020\u0006\u0012\u0014\u0012\u0012\u0012\u0006\u0012\u0004\u0018\u00010\b\u0012\u0006\u0012\u0004\u0018\u00010\t0\u00070\u0005X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u000bX\u0004¢\u0006\u0002\n\u0000¨\u0006\""}, mo65043d2 = {"Lcom/android/systemui/media/MediaDataCombineLatest;", "Lcom/android/systemui/media/MediaDataManager$Listener;", "Lcom/android/systemui/media/MediaDeviceManager$Listener;", "()V", "entries", "", "", "Lkotlin/Pair;", "Lcom/android/systemui/media/MediaData;", "Lcom/android/systemui/media/MediaDeviceData;", "listeners", "", "addListener", "", "listener", "onKeyRemoved", "", "key", "onMediaDataLoaded", "oldKey", "data", "immediately", "receivedSmartspaceCardLatency", "", "isSsReactivated", "onMediaDataRemoved", "onMediaDeviceChanged", "onSmartspaceMediaDataLoaded", "Lcom/android/systemui/media/SmartspaceMediaData;", "shouldPrioritize", "onSmartspaceMediaDataRemoved", "remove", "removeListener", "update", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaDataCombineLatest.kt */
public final class MediaDataCombineLatest implements MediaDataManager.Listener, MediaDeviceManager.Listener {
    private final Map<String, Pair<MediaData, MediaDeviceData>> entries = new LinkedHashMap();
    private final Set<MediaDataManager.Listener> listeners = new LinkedHashSet();

    public void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, int i, boolean z2) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(mediaData, "data");
        MediaDeviceData mediaDeviceData = null;
        if (str2 == null || Intrinsics.areEqual((Object) str2, (Object) str) || !this.entries.containsKey(str2)) {
            Map<String, Pair<MediaData, MediaDeviceData>> map = this.entries;
            Pair pair = map.get(str);
            if (pair != null) {
                mediaDeviceData = (MediaDeviceData) pair.getSecond();
            }
            map.put(str, TuplesKt.m1802to(mediaData, mediaDeviceData));
            update(str, str);
            return;
        }
        Map<String, Pair<MediaData, MediaDeviceData>> map2 = this.entries;
        Pair remove = map2.remove(str2);
        if (remove != null) {
            mediaDeviceData = (MediaDeviceData) remove.getSecond();
        }
        map2.put(str, TuplesKt.m1802to(mediaData, mediaDeviceData));
        update(str, str2);
    }

    public void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(smartspaceMediaData, "data");
        for (MediaDataManager.Listener onSmartspaceMediaDataLoaded$default : CollectionsKt.toSet(this.listeners)) {
            MediaDataManager.Listener.onSmartspaceMediaDataLoaded$default(onSmartspaceMediaDataLoaded$default, str, smartspaceMediaData, false, 4, (Object) null);
        }
    }

    public void onMediaDataRemoved(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        remove(str);
    }

    public void onSmartspaceMediaDataRemoved(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "key");
        for (MediaDataManager.Listener onSmartspaceMediaDataRemoved : CollectionsKt.toSet(this.listeners)) {
            onSmartspaceMediaDataRemoved.onSmartspaceMediaDataRemoved(str, z);
        }
    }

    public void onMediaDeviceChanged(String str, String str2, MediaDeviceData mediaDeviceData) {
        Intrinsics.checkNotNullParameter(str, "key");
        MediaData mediaData = null;
        if (str2 == null || Intrinsics.areEqual((Object) str2, (Object) str) || !this.entries.containsKey(str2)) {
            Map<String, Pair<MediaData, MediaDeviceData>> map = this.entries;
            Pair pair = map.get(str);
            if (pair != null) {
                mediaData = (MediaData) pair.getFirst();
            }
            map.put(str, TuplesKt.m1802to(mediaData, mediaDeviceData));
            update(str, str);
            return;
        }
        Map<String, Pair<MediaData, MediaDeviceData>> map2 = this.entries;
        Pair remove = map2.remove(str2);
        if (remove != null) {
            mediaData = (MediaData) remove.getFirst();
        }
        map2.put(str, TuplesKt.m1802to(mediaData, mediaDeviceData));
        update(str, str2);
    }

    public void onKeyRemoved(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        remove(str);
    }

    public final boolean addListener(MediaDataManager.Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        return this.listeners.add(listener);
    }

    public final boolean removeListener(MediaDataManager.Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        return this.listeners.remove(listener);
    }

    private final void update(String str, String str2) {
        Pair pair = this.entries.get(str);
        if (pair == null) {
            pair = TuplesKt.m1802to(null, null);
        }
        MediaData mediaData = (MediaData) pair.component1();
        MediaDeviceData mediaDeviceData = (MediaDeviceData) pair.component2();
        if (mediaData != null && mediaDeviceData != null) {
            MediaData copy$default = MediaData.copy$default(mediaData, 0, false, (String) null, (Icon) null, (CharSequence) null, (CharSequence) null, (Icon) null, (List) null, (List) null, (MediaButton) null, (String) null, (MediaSession.Token) null, (PendingIntent) null, mediaDeviceData, false, (Runnable) null, 0, false, (String) null, false, (Boolean) null, false, 0, (InstanceId) null, 0, 33546239, (Object) null);
            for (MediaDataManager.Listener onMediaDataLoaded$default : CollectionsKt.toSet(this.listeners)) {
                MediaDataManager.Listener.onMediaDataLoaded$default(onMediaDataLoaded$default, str, str2, copy$default, false, 0, false, 56, (Object) null);
            }
        }
    }

    private final void remove(String str) {
        if (this.entries.remove(str) != null) {
            for (MediaDataManager.Listener onMediaDataRemoved : CollectionsKt.toSet(this.listeners)) {
                onMediaDataRemoved.onMediaDataRemoved(str);
            }
        }
    }
}
