package com.android.systemui.statusbar.notification.collection.render;

import android.view.LayoutInflater;
import android.view.View;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.notification.stack.MediaContainerView;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\"\u0010\u0007\u001a\u0004\u0018\u00010\u00062\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\u00020\u000bXD¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0014\u0010\u000e\u001a\u00020\u000f8VX\u0004¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011¨\u0006\u0016"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/render/MediaContainerController;", "Lcom/android/systemui/statusbar/notification/collection/render/NodeController;", "layoutInflater", "Landroid/view/LayoutInflater;", "(Landroid/view/LayoutInflater;)V", "<set-?>", "Lcom/android/systemui/statusbar/notification/stack/MediaContainerView;", "mediaContainerView", "getMediaContainerView", "()Lcom/android/systemui/statusbar/notification/stack/MediaContainerView;", "nodeLabel", "", "getNodeLabel", "()Ljava/lang/String;", "view", "Landroid/view/View;", "getView", "()Landroid/view/View;", "reinflateView", "", "parent", "Landroid/view/ViewGroup;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaContainerController.kt */
public final class MediaContainerController implements NodeController {
    private final LayoutInflater layoutInflater;
    private MediaContainerView mediaContainerView;
    private final String nodeLabel = "MediaContainer";

    @Inject
    public MediaContainerController(LayoutInflater layoutInflater2) {
        Intrinsics.checkNotNullParameter(layoutInflater2, "layoutInflater");
        this.layoutInflater = layoutInflater2;
    }

    public String getNodeLabel() {
        return this.nodeLabel;
    }

    public final MediaContainerView getMediaContainerView() {
        return this.mediaContainerView;
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0038  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x002b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void reinflateView(android.view.ViewGroup r6) {
        /*
            r5 = this;
            java.lang.String r0 = "parent"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r6, r0)
            com.android.systemui.statusbar.notification.stack.MediaContainerView r0 = r5.mediaContainerView
            r1 = -1
            if (r0 == 0) goto L_0x001e
            r0.removeFromTransientContainer()
            android.view.ViewParent r2 = r0.getParent()
            if (r2 != r6) goto L_0x001e
            android.view.View r0 = (android.view.View) r0
            int r2 = r6.indexOfChild(r0)
            r6.removeView(r0)
            goto L_0x001f
        L_0x001e:
            r2 = r1
        L_0x001f:
            android.view.LayoutInflater r0 = r5.layoutInflater
            r3 = 2131624132(0x7f0e00c4, float:1.8875435E38)
            r4 = 0
            android.view.View r0 = r0.inflate(r3, r6, r4)
            if (r0 == 0) goto L_0x0038
            com.android.systemui.statusbar.notification.stack.MediaContainerView r0 = (com.android.systemui.statusbar.notification.stack.MediaContainerView) r0
            if (r2 == r1) goto L_0x0035
            r1 = r0
            android.view.View r1 = (android.view.View) r1
            r6.addView(r1, r2)
        L_0x0035:
            r5.mediaContainerView = r0
            return
        L_0x0038:
            java.lang.NullPointerException r5 = new java.lang.NullPointerException
            java.lang.String r6 = "null cannot be cast to non-null type com.android.systemui.statusbar.notification.stack.MediaContainerView"
            r5.<init>(r6)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.render.MediaContainerController.reinflateView(android.view.ViewGroup):void");
    }

    public View getView() {
        MediaContainerView mediaContainerView2 = this.mediaContainerView;
        Intrinsics.checkNotNull(mediaContainerView2);
        return mediaContainerView2;
    }
}
