package com.android.systemui.media;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import com.android.p019wm.shell.animation.PhysicsAnimator;
import com.android.systemui.Gefingerpoken;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0010\u0018\u00002\u00020\u0001B%\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0006\u0010 \u001a\u00020!J\u0006\u0010\"\u001a\u00020\nJ\b\u0010#\u001a\u00020!H\u0014J\u0012\u0010$\u001a\u00020%2\b\u0010&\u001a\u0004\u0018\u00010'H\u0016J\u0012\u0010(\u001a\u00020%2\b\u0010&\u001a\u0004\u0018\u00010'H\u0016JP\u0010)\u001a\u00020%2\u0006\u0010*\u001a\u00020\u00072\u0006\u0010+\u001a\u00020\u00072\u0006\u0010,\u001a\u00020\u00072\u0006\u0010-\u001a\u00020\u00072\u0006\u0010.\u001a\u00020\u00072\u0006\u0010/\u001a\u00020\u00072\u0006\u00100\u001a\u00020\u00072\u0006\u00101\u001a\u00020\u00072\u0006\u00102\u001a\u00020%H\u0014J\u0018\u00103\u001a\u00020!2\u0006\u00104\u001a\u00020\u00072\u0006\u00105\u001a\u00020\u0007H\u0016J\u0010\u00106\u001a\u00020\u00072\u0006\u0010,\u001a\u00020\u0007H\u0002R\u001a\u0010\t\u001a\u00020\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001e\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u0010@BX.¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R$\u0010\u0015\u001a\u00020\u00072\u0006\u0010\u0014\u001a\u00020\u00078F@FX\u000e¢\u0006\f\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u001c\u0010\u001a\u001a\u0004\u0018\u00010\u001bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001f¨\u00067"}, mo65043d2 = {"Lcom/android/systemui/media/MediaScrollView;", "Landroid/widget/HorizontalScrollView;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "animationTargetX", "", "getAnimationTargetX", "()F", "setAnimationTargetX", "(F)V", "<set-?>", "Landroid/view/ViewGroup;", "contentContainer", "getContentContainer", "()Landroid/view/ViewGroup;", "value", "relativeScrollX", "getRelativeScrollX", "()I", "setRelativeScrollX", "(I)V", "touchListener", "Lcom/android/systemui/Gefingerpoken;", "getTouchListener", "()Lcom/android/systemui/Gefingerpoken;", "setTouchListener", "(Lcom/android/systemui/Gefingerpoken;)V", "cancelCurrentScroll", "", "getContentTranslation", "onFinishInflate", "onInterceptTouchEvent", "", "ev", "Landroid/view/MotionEvent;", "onTouchEvent", "overScrollBy", "deltaX", "deltaY", "scrollX", "scrollY", "scrollRangeX", "scrollRangeY", "maxOverScrollX", "maxOverScrollY", "isTouchEvent", "scrollTo", "x", "y", "transformScrollX", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaScrollView.kt */
public final class MediaScrollView extends HorizontalScrollView {
    public Map<Integer, View> _$_findViewCache;
    private float animationTargetX;
    private ViewGroup contentContainer;
    private Gefingerpoken touchListener;

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public MediaScrollView(Context context) {
        this(context, (AttributeSet) null, 0, 6, (DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public MediaScrollView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, (DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public void _$_clearFindViewByIdCache() {
        this._$_findViewCache.clear();
    }

    public View _$_findCachedViewById(int i) {
        Map<Integer, View> map = this._$_findViewCache;
        View view = map.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        if (findViewById == null) {
            return null;
        }
        map.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public MediaScrollView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        Intrinsics.checkNotNullParameter(context, "context");
        this._$_findViewCache = new LinkedHashMap();
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ MediaScrollView(Context context, AttributeSet attributeSet, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i2 & 2) != 0 ? null : attributeSet, (i2 & 4) != 0 ? 0 : i);
    }

    public final ViewGroup getContentContainer() {
        ViewGroup viewGroup = this.contentContainer;
        if (viewGroup != null) {
            return viewGroup;
        }
        Intrinsics.throwUninitializedPropertyAccessException("contentContainer");
        return null;
    }

    public final Gefingerpoken getTouchListener() {
        return this.touchListener;
    }

    public final void setTouchListener(Gefingerpoken gefingerpoken) {
        this.touchListener = gefingerpoken;
    }

    public final float getAnimationTargetX() {
        return this.animationTargetX;
    }

    public final void setAnimationTargetX(float f) {
        this.animationTargetX = f;
    }

    public final float getContentTranslation() {
        if (PhysicsAnimator.Companion.getInstance(getContentContainer()).isRunning()) {
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

    public void scrollTo(int i, int i2) {
        if (this.mScrollX != i || this.mScrollY != i2) {
            int i3 = this.mScrollX;
            int i4 = this.mScrollY;
            this.mScrollX = i;
            this.mScrollY = i2;
            invalidateParentCaches();
            onScrollChanged(this.mScrollX, this.mScrollY, i3, i4);
            if (!awakenScrollBars()) {
                postInvalidateOnAnimation();
            }
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        Gefingerpoken gefingerpoken = this.touchListener;
        boolean onInterceptTouchEvent = gefingerpoken != null ? gefingerpoken.onInterceptTouchEvent(motionEvent) : false;
        if (super.onInterceptTouchEvent(motionEvent) || onInterceptTouchEvent) {
            return true;
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        Gefingerpoken gefingerpoken = this.touchListener;
        boolean onTouchEvent = gefingerpoken != null ? gefingerpoken.onTouchEvent(motionEvent) : false;
        if (super.onTouchEvent(motionEvent) || onTouchEvent) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        View childAt = getChildAt(0);
        if (childAt != null) {
            this.contentContainer = (ViewGroup) childAt;
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
    }

    /* access modifiers changed from: protected */
    public boolean overScrollBy(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, boolean z) {
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
