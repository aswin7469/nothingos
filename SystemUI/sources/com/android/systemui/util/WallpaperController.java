package com.android.systemui.util;

import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.util.Log;
import android.view.View;
import com.android.systemui.dagger.SysUISingleton;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0014\u001a\u00020\u00152\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013J\u000e\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u0006J\u000e\u0010\u0018\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u0006J\u0010\u0010\u0019\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u0006H\u0002J\b\u0010\u001a\u001a\u00020\u0015H\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u001c\u0010\u0007\u001a\u0004\u0018\u00010\bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u0014\u0010\r\u001a\u00020\u000e8BX\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0011\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u001b"}, mo65043d2 = {"Lcom/android/systemui/util/WallpaperController;", "", "wallpaperManager", "Landroid/app/WallpaperManager;", "(Landroid/app/WallpaperManager;)V", "notificationShadeZoomOut", "", "rootView", "Landroid/view/View;", "getRootView", "()Landroid/view/View;", "setRootView", "(Landroid/view/View;)V", "shouldUseDefaultUnfoldTransition", "", "getShouldUseDefaultUnfoldTransition", "()Z", "unfoldTransitionZoomOut", "wallpaperInfo", "Landroid/app/WallpaperInfo;", "onWallpaperInfoUpdated", "", "setNotificationShadeZoom", "zoomOut", "setUnfoldTransitionZoom", "setWallpaperZoom", "updateZoom", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: WallpaperController.kt */
public final class WallpaperController {
    private float notificationShadeZoomOut;
    private View rootView;
    private float unfoldTransitionZoomOut;
    private WallpaperInfo wallpaperInfo;
    private final WallpaperManager wallpaperManager;

    @Inject
    public WallpaperController(WallpaperManager wallpaperManager2) {
        Intrinsics.checkNotNullParameter(wallpaperManager2, "wallpaperManager");
        this.wallpaperManager = wallpaperManager2;
    }

    public final View getRootView() {
        return this.rootView;
    }

    public final void setRootView(View view) {
        this.rootView = view;
    }

    public final void onWallpaperInfoUpdated(WallpaperInfo wallpaperInfo2) {
        this.wallpaperInfo = wallpaperInfo2;
    }

    private final boolean getShouldUseDefaultUnfoldTransition() {
        WallpaperInfo wallpaperInfo2 = this.wallpaperInfo;
        if (wallpaperInfo2 != null) {
            return wallpaperInfo2.shouldUseDefaultUnfoldTransition();
        }
        return true;
    }

    public final void setNotificationShadeZoom(float f) {
        this.notificationShadeZoomOut = f;
        updateZoom();
    }

    public final void setUnfoldTransitionZoom(float f) {
        if (getShouldUseDefaultUnfoldTransition()) {
            this.unfoldTransitionZoomOut = f;
            updateZoom();
        }
    }

    private final void updateZoom() {
        setWallpaperZoom(Math.max(this.notificationShadeZoomOut, this.unfoldTransitionZoomOut));
    }

    private final void setWallpaperZoom(float f) {
        try {
            View view = this.rootView;
            if (view == null) {
                return;
            }
            if (!view.isAttachedToWindow() || view.getWindowToken() == null) {
                Log.i("WallpaperController", "Won't set zoom. Window not attached " + view);
            } else {
                this.wallpaperManager.setWallpaperZoomOut(view.getWindowToken(), f);
            }
        } catch (IllegalArgumentException e) {
            StringBuilder sb = new StringBuilder("Can't set zoom. Window is gone: ");
            View view2 = this.rootView;
            Log.w("WallpaperController", sb.append((Object) view2 != null ? view2.getWindowToken() : null).toString(), e);
        }
    }
}
