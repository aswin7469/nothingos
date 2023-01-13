package com.android.systemui.media;

import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0016¨\u0006\u0007"}, mo65043d2 = {"com/android/systemui/media/MediaSessionBasedFilter$sessionListener$1", "Landroid/media/session/MediaSessionManager$OnActiveSessionsChangedListener;", "onActiveSessionsChanged", "", "controllers", "", "Landroid/media/session/MediaController;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaSessionBasedFilter.kt */
public final class MediaSessionBasedFilter$sessionListener$1 implements MediaSessionManager.OnActiveSessionsChangedListener {
    final /* synthetic */ MediaSessionBasedFilter this$0;

    MediaSessionBasedFilter$sessionListener$1(MediaSessionBasedFilter mediaSessionBasedFilter) {
        this.this$0 = mediaSessionBasedFilter;
    }

    public void onActiveSessionsChanged(List<MediaController> list) {
        Intrinsics.checkNotNullParameter(list, "controllers");
        this.this$0.handleControllersChanged(list);
    }
}
