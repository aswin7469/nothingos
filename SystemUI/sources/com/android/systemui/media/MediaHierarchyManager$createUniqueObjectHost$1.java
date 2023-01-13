package com.android.systemui.media;

import android.view.View;
import android.view.ViewGroupOverlay;
import android.view.ViewOverlay;
import com.android.systemui.util.animation.UniqueObjectHostView;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u0012\u0010\u0006\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006\u0007"}, mo65043d2 = {"com/android/systemui/media/MediaHierarchyManager$createUniqueObjectHost$1", "Landroid/view/View$OnAttachStateChangeListener;", "onViewAttachedToWindow", "", "p0", "Landroid/view/View;", "onViewDetachedFromWindow", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaHierarchyManager.kt */
public final class MediaHierarchyManager$createUniqueObjectHost$1 implements View.OnAttachStateChangeListener {
    final /* synthetic */ UniqueObjectHostView $viewHost;
    final /* synthetic */ MediaHierarchyManager this$0;

    public void onViewDetachedFromWindow(View view) {
    }

    MediaHierarchyManager$createUniqueObjectHost$1(MediaHierarchyManager mediaHierarchyManager, UniqueObjectHostView uniqueObjectHostView) {
        this.this$0 = mediaHierarchyManager;
        this.$viewHost = uniqueObjectHostView;
    }

    public void onViewAttachedToWindow(View view) {
        if (this.this$0.rootOverlay == null) {
            this.this$0.rootView = this.$viewHost.getViewRootImpl().getView();
            MediaHierarchyManager mediaHierarchyManager = this.this$0;
            View access$getRootView$p = mediaHierarchyManager.rootView;
            Intrinsics.checkNotNull(access$getRootView$p);
            ViewOverlay overlay = access$getRootView$p.getOverlay();
            if (overlay != null) {
                mediaHierarchyManager.rootOverlay = (ViewGroupOverlay) overlay;
            } else {
                throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroupOverlay");
            }
        }
        this.$viewHost.removeOnAttachStateChangeListener(this);
    }
}
