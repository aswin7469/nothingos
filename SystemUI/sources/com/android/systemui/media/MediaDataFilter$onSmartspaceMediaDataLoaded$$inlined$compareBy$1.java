package com.android.systemui.media;

import java.util.Comparator;
import kotlin.Metadata;
import kotlin.comparisons.ComparisonsKt;

@Metadata(mo65042d1 = {"\u0000\n\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u000e\u0010\u0003\u001a\n \u0004*\u0004\u0018\u0001H\u0002H\u00022\u000e\u0010\u0005\u001a\n \u0004*\u0004\u0018\u0001H\u0002H\u0002H\n¢\u0006\u0004\b\u0006\u0010\u0007¨\u0006\b"}, mo65043d2 = {"<anonymous>", "", "T", "a", "kotlin.jvm.PlatformType", "b", "compare", "(Ljava/lang/Object;Ljava/lang/Object;)I", "kotlin/comparisons/ComparisonsKt__ComparisonsKt$compareBy$2"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: Comparisons.kt */
public final class MediaDataFilter$onSmartspaceMediaDataLoaded$$inlined$compareBy$1<T> implements Comparator {
    final /* synthetic */ MediaDataFilter this$0;

    public MediaDataFilter$onSmartspaceMediaDataLoaded$$inlined$compareBy$1(MediaDataFilter mediaDataFilter) {
        this.this$0 = mediaDataFilter;
    }

    public final int compare(T t, T t2) {
        MediaData mediaData = (MediaData) this.this$0.userEntries.get((String) t);
        Comparable valueOf = mediaData != null ? Long.valueOf(mediaData.getLastActive()) : (Comparable) -1;
        MediaData mediaData2 = (MediaData) this.this$0.userEntries.get((String) t2);
        return ComparisonsKt.compareValues(valueOf, mediaData2 != null ? Long.valueOf(mediaData2.getLastActive()) : (Comparable) -1);
    }
}
