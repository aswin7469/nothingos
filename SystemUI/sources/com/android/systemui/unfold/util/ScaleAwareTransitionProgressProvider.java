package com.android.systemui.unfold.util;

import android.animation.ValueAnimator;
import android.content.ContentResolver;
import android.provider.Settings;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000/\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005*\u0001\u0007\u0018\u00002\u00020\u0001:\u0001\u0012B\u0019\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0001\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0016J\b\u0010\u000f\u001a\u00020\fH\u0016J\b\u0010\u0010\u001a\u00020\fH\u0002J\u0010\u0010\u0011\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0016R\u0010\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0004\n\u0002\u0010\bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0013"}, mo64987d2 = {"Lcom/android/systemui/unfold/util/ScaleAwareTransitionProgressProvider;", "Lcom/android/systemui/unfold/UnfoldTransitionProgressProvider;", "progressProviderToWrap", "contentResolver", "Landroid/content/ContentResolver;", "(Lcom/android/systemui/unfold/UnfoldTransitionProgressProvider;Landroid/content/ContentResolver;)V", "animatorDurationScaleObserver", "com/android/systemui/unfold/util/ScaleAwareTransitionProgressProvider$animatorDurationScaleObserver$1", "Lcom/android/systemui/unfold/util/ScaleAwareTransitionProgressProvider$animatorDurationScaleObserver$1;", "scopedUnfoldTransitionProgressProvider", "Lcom/android/systemui/unfold/util/ScopedUnfoldTransitionProgressProvider;", "addCallback", "", "listener", "Lcom/android/systemui/unfold/UnfoldTransitionProgressProvider$TransitionProgressListener;", "destroy", "onAnimatorScaleChanged", "removeCallback", "Factory", "shared_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ScaleAwareTransitionProgressProvider.kt */
public final class ScaleAwareTransitionProgressProvider implements UnfoldTransitionProgressProvider {
    private final C3242x746b6035 animatorDurationScaleObserver;
    private final ContentResolver contentResolver;
    private final ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider;

    @AssistedFactory
    @Metadata(mo64986d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0006À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/unfold/util/ScaleAwareTransitionProgressProvider$Factory;", "", "wrap", "Lcom/android/systemui/unfold/util/ScaleAwareTransitionProgressProvider;", "progressProvider", "Lcom/android/systemui/unfold/UnfoldTransitionProgressProvider;", "shared_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ScaleAwareTransitionProgressProvider.kt */
    public interface Factory {
        ScaleAwareTransitionProgressProvider wrap(UnfoldTransitionProgressProvider unfoldTransitionProgressProvider);
    }

    @AssistedInject
    public ScaleAwareTransitionProgressProvider(@Assisted UnfoldTransitionProgressProvider unfoldTransitionProgressProvider, ContentResolver contentResolver2) {
        Intrinsics.checkNotNullParameter(unfoldTransitionProgressProvider, "progressProviderToWrap");
        Intrinsics.checkNotNullParameter(contentResolver2, "contentResolver");
        this.contentResolver = contentResolver2;
        this.scopedUnfoldTransitionProgressProvider = new ScopedUnfoldTransitionProgressProvider(unfoldTransitionProgressProvider);
        C3242x746b6035 scaleAwareTransitionProgressProvider$animatorDurationScaleObserver$1 = new C3242x746b6035(this);
        this.animatorDurationScaleObserver = scaleAwareTransitionProgressProvider$animatorDurationScaleObserver$1;
        contentResolver2.registerContentObserver(Settings.Global.getUriFor("animator_duration_scale"), false, scaleAwareTransitionProgressProvider$animatorDurationScaleObserver$1);
        onAnimatorScaleChanged();
    }

    /* access modifiers changed from: private */
    public final void onAnimatorScaleChanged() {
        this.scopedUnfoldTransitionProgressProvider.setReadyToHandleTransition(ValueAnimator.areAnimatorsEnabled());
    }

    public void addCallback(UnfoldTransitionProgressProvider.TransitionProgressListener transitionProgressListener) {
        Intrinsics.checkNotNullParameter(transitionProgressListener, "listener");
        this.scopedUnfoldTransitionProgressProvider.addCallback(transitionProgressListener);
    }

    public void removeCallback(UnfoldTransitionProgressProvider.TransitionProgressListener transitionProgressListener) {
        Intrinsics.checkNotNullParameter(transitionProgressListener, "listener");
        this.scopedUnfoldTransitionProgressProvider.removeCallback(transitionProgressListener);
    }

    public void destroy() {
        this.contentResolver.unregisterContentObserver(this.animatorDurationScaleObserver);
        this.scopedUnfoldTransitionProgressProvider.destroy();
    }
}
