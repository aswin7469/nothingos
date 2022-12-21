package com.android.systemui.media;

import com.android.systemui.media.MediaPlayerData;
import java.util.Comparator;
import kotlin.Metadata;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\n\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u000e\u0010\u0003\u001a\n \u0004*\u0004\u0018\u0001H\u0002H\u00022\u000e\u0010\u0005\u001a\n \u0004*\u0004\u0018\u0001H\u0002H\u0002H\n¢\u0006\u0004\b\u0006\u0010\u0007¨\u0006\b"}, mo64987d2 = {"<anonymous>", "", "T", "a", "kotlin.jvm.PlatformType", "b", "compare", "(Ljava/lang/Object;Ljava/lang/Object;)I", "kotlin/comparisons/ComparisonsKt__ComparisonsKt$compareByDescending$1"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: Comparisons.kt */
public final class MediaPlayerData$special$$inlined$compareByDescending$1<T> implements Comparator {
    public final int compare(T t, T t2) {
        MediaPlayerData.MediaSortKey mediaSortKey = (MediaPlayerData.MediaSortKey) t2;
        boolean z = true;
        Comparable valueOf = Boolean.valueOf(Intrinsics.areEqual((Object) mediaSortKey.getData().isPlaying(), (Object) true) && mediaSortKey.getData().getPlaybackLocation() == 0);
        MediaPlayerData.MediaSortKey mediaSortKey2 = (MediaPlayerData.MediaSortKey) t;
        if (!Intrinsics.areEqual((Object) mediaSortKey2.getData().isPlaying(), (Object) true) || mediaSortKey2.getData().getPlaybackLocation() != 0) {
            z = false;
        }
        return ComparisonsKt.compareValues(valueOf, Boolean.valueOf(z));
    }
}
