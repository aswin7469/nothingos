package com.android.systemui.unfold;

import com.android.systemui.statusbar.policy.CallbackController;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\bf\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0005J\b\u0010\u0003\u001a\u00020\u0004H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0006À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/unfold/UnfoldTransitionProgressProvider;", "Lcom/android/systemui/statusbar/policy/CallbackController;", "Lcom/android/systemui/unfold/UnfoldTransitionProgressProvider$TransitionProgressListener;", "destroy", "", "TransitionProgressListener", "shared_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: UnfoldTransitionProgressProvider.kt */
public interface UnfoldTransitionProgressProvider extends CallbackController<TransitionProgressListener> {

    @Metadata(mo64986d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\u0010\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\b\u0010\u0007\u001a\u00020\u0003H\u0016ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\bÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/unfold/UnfoldTransitionProgressProvider$TransitionProgressListener;", "", "onTransitionFinished", "", "onTransitionProgress", "progress", "", "onTransitionStarted", "shared_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: UnfoldTransitionProgressProvider.kt */
    public interface TransitionProgressListener {
        void onTransitionFinished() {
        }

        void onTransitionProgress(float f) {
        }

        void onTransitionStarted() {
        }
    }

    void destroy();
}
