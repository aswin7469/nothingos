package com.android.systemui.statusbar.notification.stack;

import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.android.systemui.C1893R;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\u0012\u0010\u001f\u001a\u00020 2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0014J\u0010\u0010#\u001a\u00020 2\u0006\u0010$\u001a\u00020%H\u0014J*\u0010&\u001a\u00020 2\u0006\u0010'\u001a\u00020(2\u0006\u0010)\u001a\u00020(2\u0006\u0010*\u001a\u00020+2\b\u0010,\u001a\u0004\u0018\u00010-H\u0016JD\u0010.\u001a\u00020(2\u0006\u0010)\u001a\u00020(2\u0006\u0010'\u001a\u00020(2\u0006\u0010/\u001a\u00020\u001a2\u0006\u00100\u001a\u00020+2\u0006\u00101\u001a\u00020\u001a2\b\u00102\u001a\u0004\u0018\u00010-2\b\u00103\u001a\u0004\u0018\u000104H\u0016J\b\u00105\u001a\u00020 H\u0016J\b\u00106\u001a\u00020 H\u0002R\u001a\u0010\u0007\u001a\u00020\bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u00020\u000eX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001a\u0010\u0013\u001a\u00020\u0014X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u001a\u0010\u0019\u001a\u00020\u001aX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001e¨\u00067"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/stack/MediaContainerView;", "Lcom/android/systemui/statusbar/notification/row/ExpandableView;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "clipHeight", "", "getClipHeight", "()I", "setClipHeight", "(I)V", "clipPath", "Landroid/graphics/Path;", "getClipPath", "()Landroid/graphics/Path;", "setClipPath", "(Landroid/graphics/Path;)V", "clipRect", "Landroid/graphics/RectF;", "getClipRect", "()Landroid/graphics/RectF;", "setClipRect", "(Landroid/graphics/RectF;)V", "cornerRadius", "", "getCornerRadius", "()F", "setCornerRadius", "(F)V", "onConfigurationChanged", "", "newConfig", "Landroid/content/res/Configuration;", "onDraw", "canvas", "Landroid/graphics/Canvas;", "performAddAnimation", "delay", "", "duration", "isHeadsUpAppear", "", "onEnd", "Ljava/lang/Runnable;", "performRemoveAnimation", "translationDirection", "isHeadsUpAnimation", "endLocation", "onFinishedRunnable", "animationListener", "Landroid/animation/AnimatorListenerAdapter;", "updateClipping", "updateResources", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaContainerView.kt */
public final class MediaContainerView extends ExpandableView {
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private int clipHeight;
    private Path clipPath;
    private RectF clipRect;
    private float cornerRadius;

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

    public void performAddAnimation(long j, long j2, boolean z, Runnable runnable) {
    }

    public long performRemoveAnimation(long j, long j2, float f, boolean z, float f2, Runnable runnable, AnimatorListenerAdapter animatorListenerAdapter) {
        return 0;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public MediaContainerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Intrinsics.checkNotNullParameter(context, "context");
        this.clipRect = new RectF();
        this.clipPath = new Path();
        setWillNotDraw(false);
        updateResources();
    }

    public final float getCornerRadius() {
        return this.cornerRadius;
    }

    public final void setCornerRadius(float f) {
        this.cornerRadius = f;
    }

    public final int getClipHeight() {
        return this.clipHeight;
    }

    public final void setClipHeight(int i) {
        this.clipHeight = i;
    }

    public final RectF getClipRect() {
        return this.clipRect;
    }

    public final void setClipRect(RectF rectF) {
        Intrinsics.checkNotNullParameter(rectF, "<set-?>");
        this.clipRect = rectF;
    }

    public final Path getClipPath() {
        return this.clipPath;
    }

    public final void setClipPath(Path path) {
        Intrinsics.checkNotNullParameter(path, "<set-?>");
        this.clipPath = path;
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateResources();
    }

    private final void updateResources() {
        this.cornerRadius = (float) getContext().getResources().getDimensionPixelSize(C1893R.dimen.notification_corner_radius);
    }

    public void updateClipping() {
        if (this.clipHeight != getActualHeight()) {
            this.clipHeight = getActualHeight();
        }
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        super.onDraw(canvas);
        Rect clipBounds = canvas.getClipBounds();
        clipBounds.bottom = this.clipHeight;
        this.clipRect.set(clipBounds);
        this.clipPath.reset();
        Path path = this.clipPath;
        RectF rectF = this.clipRect;
        float f = this.cornerRadius;
        path.addRoundRect(rectF, f, f, Path.Direction.CW);
        canvas.clipPath(this.clipPath);
    }
}
