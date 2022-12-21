package com.android.systemui.media;

import com.android.systemui.media.MediaPlayerData;
import java.util.Comparator;
import kotlin.Metadata;
import kotlin.comparisons.ComparisonsKt;

@Metadata(mo64986d1 = {"\u0000\n\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u000e\u0010\u0003\u001a\n \u0004*\u0004\u0018\u0001H\u0002H\u00022\u000e\u0010\u0005\u001a\n \u0004*\u0004\u0018\u0001H\u0002H\u0002H\n¢\u0006\u0004\b\u0006\u0010\u0007¨\u0006\b"}, mo64987d2 = {"<anonymous>", "", "T", "a", "kotlin.jvm.PlatformType", "b", "compare", "(Ljava/lang/Object;Ljava/lang/Object;)I", "kotlin/comparisons/ComparisonsKt__ComparisonsKt$thenByDescending$1"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: Comparisons.kt */
public final class MediaPlayerData$special$$inlined$thenByDescending$5<T> implements Comparator {
    final /* synthetic */ Comparator $this_thenByDescending;

    public MediaPlayerData$special$$inlined$thenByDescending$5(Comparator comparator) {
        this.$this_thenByDescending = comparator;
    }

    public final int compare(T t, T t2) {
        int compare = this.$this_thenByDescending.compare(t, t2);
        if (compare != 0) {
            return compare;
        }
        int playbackLocation = ((MediaPlayerData.MediaSortKey) t2).getData().getPlaybackLocation();
        boolean z = true;
        Comparable valueOf = Boolean.valueOf(playbackLocation != 2);
        if (((MediaPlayerData.MediaSortKey) t).getData().getPlaybackLocation() == 2) {
            z = false;
        }
        return ComparisonsKt.compareValues(valueOf, Boolean.valueOf(z));
    }
}
