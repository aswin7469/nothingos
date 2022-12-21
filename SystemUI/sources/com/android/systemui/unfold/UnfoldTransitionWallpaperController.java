package com.android.systemui.unfold;

import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import com.android.systemui.util.WallpaperController;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001:\u0001\tB\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000¨\u0006\n"}, mo64987d2 = {"Lcom/android/systemui/unfold/UnfoldTransitionWallpaperController;", "", "unfoldTransitionProgressProvider", "Lcom/android/systemui/unfold/UnfoldTransitionProgressProvider;", "wallpaperController", "Lcom/android/systemui/util/WallpaperController;", "(Lcom/android/systemui/unfold/UnfoldTransitionProgressProvider;Lcom/android/systemui/util/WallpaperController;)V", "init", "", "TransitionListener", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
@SysUIUnfoldScope
/* compiled from: UnfoldTransitionWallpaperController.kt */
public final class UnfoldTransitionWallpaperController {
    private final UnfoldTransitionProgressProvider unfoldTransitionProgressProvider;
    /* access modifiers changed from: private */
    public final WallpaperController wallpaperController;

    @Inject
    public UnfoldTransitionWallpaperController(UnfoldTransitionProgressProvider unfoldTransitionProgressProvider2, WallpaperController wallpaperController2) {
        Intrinsics.checkNotNullParameter(unfoldTransitionProgressProvider2, "unfoldTransitionProgressProvider");
        Intrinsics.checkNotNullParameter(wallpaperController2, "wallpaperController");
        this.unfoldTransitionProgressProvider = unfoldTransitionProgressProvider2;
        this.wallpaperController = wallpaperController2;
    }

    public final void init() {
        this.unfoldTransitionProgressProvider.addCallback(new TransitionListener());
    }

    @Metadata(mo64986d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, mo64987d2 = {"Lcom/android/systemui/unfold/UnfoldTransitionWallpaperController$TransitionListener;", "Lcom/android/systemui/unfold/UnfoldTransitionProgressProvider$TransitionProgressListener;", "(Lcom/android/systemui/unfold/UnfoldTransitionWallpaperController;)V", "onTransitionFinished", "", "onTransitionProgress", "progress", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: UnfoldTransitionWallpaperController.kt */
    private final class TransitionListener implements UnfoldTransitionProgressProvider.TransitionProgressListener {
        public TransitionListener() {
        }

        public void onTransitionProgress(float f) {
            UnfoldTransitionWallpaperController.this.wallpaperController.setUnfoldTransitionZoom(((float) 1) - f);
        }

        public void onTransitionFinished() {
            UnfoldTransitionWallpaperController.this.wallpaperController.setUnfoldTransitionZoom(0.0f);
        }
    }
}
