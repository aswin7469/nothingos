package com.android.systemui.media;

import android.view.View;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u0012\u0010\u0006\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006\u0007"}, mo65043d2 = {"com/android/systemui/media/MediaHost$init$1", "Landroid/view/View$OnAttachStateChangeListener;", "onViewAttachedToWindow", "", "v", "Landroid/view/View;", "onViewDetachedFromWindow", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaHost.kt */
public final class MediaHost$init$1 implements View.OnAttachStateChangeListener {
    final /* synthetic */ MediaHost this$0;

    MediaHost$init$1(MediaHost mediaHost) {
        this.this$0 = mediaHost;
    }

    public void onViewAttachedToWindow(View view) {
        this.this$0.setListeningToMediaData(true);
        this.this$0.updateViewVisibility();
    }

    public void onViewDetachedFromWindow(View view) {
        this.this$0.setListeningToMediaData(false);
    }
}
