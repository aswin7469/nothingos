package com.android.systemui.media;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import com.android.systemui.Gefingerpoken;
import com.android.wm.shell.animation.PhysicsAnimatorKt;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: MediaScrollView.kt */
/* loaded from: classes.dex */
public final class MediaScrollView extends HorizontalScrollView {
    private float animationTargetX;
    private ViewGroup contentContainer;
    @Nullable
    private Gefingerpoken touchListener;

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public MediaScrollView(@NotNull Context context) {
        this(context, null, 0, 6, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public MediaScrollView(@NotNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public /* synthetic */ MediaScrollView(Context context, AttributeSet attributeSet, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i2 & 2) != 0 ? null : attributeSet, (i2 & 4) != 0 ? 0 : i);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public MediaScrollView(@NotNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    @NotNull
    public final ViewGroup getContentContainer() {
        ViewGroup viewGroup = this.contentContainer;
        if (viewGroup != null) {
            return viewGroup;
        }
        Intrinsics.throwUninitializedPropertyAccessException("contentContainer");
        throw null;
    }

    public final void setTouchListener(@Nullable Gefingerpoken gefingerpoken) {
        this.touchListener = gefingerpoken;
    }

    public final void setAnimationTargetX(float f) {
        this.animationTargetX = f;
    }

    public final float getContentTranslation() {
        if (PhysicsAnimatorKt.getPhysicsAnimator(getContentContainer()).isRunning()) {
            return this.animationTargetX;
        }
        return getContentContainer().getTranslationX();
    }

    private final int transformScrollX(int i) {
        return isLayoutRtl() ? (getContentContainer().getWidth() - getWidth()) - i : i;
    }

    public final int getRelativeScrollX() {
        return transformScrollX(getScrollX());
    }

    public final void setRelativeScrollX(int i) {
        setScrollX(transformScrollX(i));
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    public void scrollTo(int i, int i2) {
        int i3 = ((HorizontalScrollView) this).mScrollX;
        if (i3 == i && ((HorizontalScrollView) this).mScrollY == i2) {
            return;
        }
        int i4 = ((HorizontalScrollView) this).mScrollY;
        ((HorizontalScrollView) this).mScrollX = i;
        ((HorizontalScrollView) this).mScrollY = i2;
        invalidateParentCaches();
        onScrollChanged(((HorizontalScrollView) this).mScrollX, ((HorizontalScrollView) this).mScrollY, i3, i4);
        if (awakenScrollBars()) {
            return;
        }
        postInvalidateOnAnimation();
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(@Nullable MotionEvent motionEvent) {
        Gefingerpoken gefingerpoken = this.touchListener;
        return super.onInterceptTouchEvent(motionEvent) || (gefingerpoken == null ? false : gefingerpoken.onInterceptTouchEvent(motionEvent));
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    public boolean onTouchEvent(@Nullable MotionEvent motionEvent) {
        Gefingerpoken gefingerpoken = this.touchListener;
        return super.onTouchEvent(motionEvent) || (gefingerpoken == null ? false : gefingerpoken.onTouchEvent(motionEvent));
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        View childAt = getChildAt(0);
        Objects.requireNonNull(childAt, "null cannot be cast to non-null type android.view.ViewGroup");
        this.contentContainer = (ViewGroup) childAt;
    }

    @Override // android.view.View
    protected boolean overScrollBy(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, boolean z) {
        if (!(getContentTranslation() == 0.0f)) {
            return false;
        }
        return super.overScrollBy(i, i2, i3, i4, i5, i6, i7, i8, z);
    }

    public final void cancelCurrentScroll() {
        long uptimeMillis = SystemClock.uptimeMillis();
        MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
        obtain.setSource(4098);
        super.onTouchEvent(obtain);
        obtain.recycle();
    }
}
