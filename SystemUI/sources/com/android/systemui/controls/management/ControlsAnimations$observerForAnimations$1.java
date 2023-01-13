package com.android.systemui.controls.management;

import android.content.Intent;
import android.view.ViewGroup;
import android.view.Window;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import com.android.systemui.C1894R;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u001b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\b\u001a\u00020\tH\u0007J\b\u0010\n\u001a\u00020\tH\u0007J\b\u0010\u000b\u001a\u00020\tH\u0007R\u001a\u0010\u0002\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007¨\u0006\f"}, mo65043d2 = {"com/android/systemui/controls/management/ControlsAnimations$observerForAnimations$1", "Landroidx/lifecycle/LifecycleObserver;", "showAnimation", "", "getShowAnimation", "()Z", "setShowAnimation", "(Z)V", "enterAnimation", "", "resetAnimation", "setup", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsAnimations.kt */
public final class ControlsAnimations$observerForAnimations$1 implements LifecycleObserver {
    final /* synthetic */ ViewGroup $view;
    final /* synthetic */ Window $window;
    private boolean showAnimation;

    ControlsAnimations$observerForAnimations$1(Intent intent, ViewGroup viewGroup, Window window) {
        this.$view = viewGroup;
        this.$window = window;
        boolean z = false;
        this.showAnimation = intent.getBooleanExtra("extra_animate", false);
        viewGroup.setTransitionGroup(true);
        viewGroup.setTransitionAlpha(0.0f);
        if (ControlsAnimations.translationY == -1.0f ? true : z) {
            ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
            ControlsAnimations.translationY = (float) viewGroup.getContext().getResources().getDimensionPixelSize(C1894R.dimen.global_actions_controls_y_translation);
        }
    }

    public final boolean getShowAnimation() {
        return this.showAnimation;
    }

    public final void setShowAnimation(boolean z) {
        this.showAnimation = z;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public final void setup() {
        Window window = this.$window;
        ViewGroup viewGroup = this.$view;
        window.setAllowEnterTransitionOverlap(true);
        window.setEnterTransition(ControlsAnimations.INSTANCE.enterWindowTransition(viewGroup.getId()));
        window.setExitTransition(ControlsAnimations.INSTANCE.exitWindowTransition(viewGroup.getId()));
        window.setReenterTransition(ControlsAnimations.INSTANCE.enterWindowTransition(viewGroup.getId()));
        window.setReturnTransition(ControlsAnimations.INSTANCE.exitWindowTransition(viewGroup.getId()));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public final void enterAnimation() {
        if (this.showAnimation) {
            ControlsAnimations.INSTANCE.enterAnimation(this.$view).start();
            this.showAnimation = false;
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public final void resetAnimation() {
        this.$view.setTranslationY(0.0f);
    }
}
