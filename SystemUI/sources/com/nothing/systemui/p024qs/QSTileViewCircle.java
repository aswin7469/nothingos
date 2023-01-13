package com.nothing.systemui.p024qs;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import com.android.systemui.C1894R;
import com.android.systemui.p012qs.tileimpl.QSTileViewImpl;
import com.android.systemui.plugins.p011qs.QSIconView;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0010\u0007\n\u0002\b\u000e\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\b\u0010#\u001a\u00020$H\u0016J\b\u0010%\u001a\u00020&H\u0014J\u0010\u0010'\u001a\u00020\u001e2\u0006\u0010(\u001a\u00020\u001eH\u0016J\u0010\u0010)\u001a\u00020\u001e2\u0006\u0010(\u001a\u00020\u001eH\u0016J\b\u0010*\u001a\u00020\u001eH\u0016J\b\u0010+\u001a\u00020\u001eH\u0016J\u0018\u0010,\u001a\u00020&2\u0006\u0010-\u001a\u00020\u001e2\u0006\u0010.\u001a\u00020\u001eH\u0014J\u0010\u0010/\u001a\u00020\u00072\u0006\u00100\u001a\u000201H\u0016J\u0006\u00102\u001a\u00020&J\u000e\u00103\u001a\u00020&2\u0006\u00104\u001a\u00020\u000fJ\b\u00105\u001a\u00020\u0007H\u0016J\b\u00106\u001a\u00020&H\u0016R\u001a\u0010\t\u001a\u00020\u0007X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001a\u0010\u000e\u001a\u00020\u000fX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u001a\u0010\u0014\u001a\u00020\u000fX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0011\"\u0004\b\u0016\u0010\u0013R\u001a\u0010\u0017\u001a\u00020\u0007X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u000b\"\u0004\b\u0019\u0010\rR\u001a\u0010\u001a\u001a\u00020\u000fX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u0011\"\u0004\b\u001c\u0010\u0013R\u001a\u0010\u001d\u001a\u00020\u001eX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"¨\u00067"}, mo65043d2 = {"Lcom/nothing/systemui/qs/QSTileViewCircle;", "Lcom/android/systemui/qs/tileimpl/QSTileViewImpl;", "context", "Landroid/content/Context;", "_icon", "Lcom/android/systemui/plugins/qs/QSIconView;", "collapsed", "", "(Landroid/content/Context;Lcom/android/systemui/plugins/qs/QSIconView;Z)V", "animating", "getAnimating", "()Z", "setAnimating", "(Z)V", "initialX", "", "getInitialX", "()F", "setInitialX", "(F)V", "initialY", "getInitialY", "setInitialY", "interceptClick", "getInterceptClick", "setInterceptClick", "position", "getPosition", "setPosition", "touchSlop", "", "getTouchSlop", "()I", "setTouchSlop", "(I)V", "createTileBackground", "Landroid/graphics/drawable/Drawable;", "dealAndAddIcon", "", "getLabelColorForState", "state", "getSecondaryLabelColorForState", "getTileGravity", "getTileOrientation", "onMeasure", "widthMeasureSpec", "heightMeasureSpec", "onTouchEvent", "ev", "Landroid/view/MotionEvent;", "resetEvent", "setExpansion", "expansion", "shouldInterceptClick", "updateResources", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.nothing.systemui.qs.QSTileViewCircle */
/* compiled from: QSTileViewCircle.kt */
public final class QSTileViewCircle extends QSTileViewImpl {
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private boolean animating;
    private float initialX;
    private float initialY;
    private boolean interceptClick;
    private float position;
    private int touchSlop;

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

    public int getTileGravity() {
        return 1;
    }

    public int getTileOrientation() {
        return 1;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public QSTileViewCircle(Context context, QSIconView qSIconView, boolean z) {
        super(context, qSIconView, z);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(qSIconView, "_icon");
        int dimensionPixelSize = getResources().getDimensionPixelSize(C1894R.dimen.circle_qs_tile_padding);
        setPaddingRelative(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
        this.touchSlop = ViewConfiguration.get(context).getScaledPagingTouchSlop();
    }

    public final float getInitialX() {
        return this.initialX;
    }

    public final void setInitialX(float f) {
        this.initialX = f;
    }

    public final float getInitialY() {
        return this.initialY;
    }

    public final void setInitialY(float f) {
        this.initialY = f;
    }

    public final int getTouchSlop() {
        return this.touchSlop;
    }

    public final void setTouchSlop(int i) {
        this.touchSlop = i;
    }

    public final boolean getInterceptClick() {
        return this.interceptClick;
    }

    public final void setInterceptClick(boolean z) {
        this.interceptClick = z;
    }

    public final float getPosition() {
        return this.position;
    }

    public final void setPosition(float f) {
        this.position = f;
    }

    public final boolean getAnimating() {
        return this.animating;
    }

    public final void setAnimating(boolean z) {
        this.animating = z;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824));
    }

    /* access modifiers changed from: protected */
    public void dealAndAddIcon() {
        int dimensionPixelSize = getResources().getDimensionPixelSize(C1894R.dimen.circle_qs_icon_size);
        addView(get_icon(), new LinearLayout.LayoutParams(dimensionPixelSize, dimensionPixelSize));
    }

    public Drawable createTileBackground() {
        RippleDrawable rippleDrawable;
        if (this.animating) {
            Drawable drawable = this.mContext.getDrawable(C1894R.C1896drawable.circle_qs_tile_background_for_animating);
            if (drawable != null) {
                rippleDrawable = (RippleDrawable) drawable;
            } else {
                throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.RippleDrawable");
            }
        } else {
            Drawable drawable2 = this.mContext.getDrawable(C1894R.C1896drawable.circle_qs_tile_background);
            if (drawable2 != null) {
                rippleDrawable = (RippleDrawable) drawable2;
            } else {
                throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.RippleDrawable");
            }
        }
        setRipple(rippleDrawable);
        Drawable findDrawableByLayerId = getRipple().findDrawableByLayerId(C1894R.C1898id.background);
        Intrinsics.checkNotNullExpressionValue(findDrawableByLayerId, "ripple.findDrawableByLayerId(R.id.background)");
        setColorBackgroundDrawable(findDrawableByLayerId);
        return getColorBackgroundDrawable();
    }

    public int getLabelColorForState(int i) {
        if (i == 0) {
            return getColorLabelUnavailable();
        }
        if (i == 1) {
            return getColorLabelInactive();
        }
        if (i != 2) {
            return 0;
        }
        return getColorLabelInactive();
    }

    public int getSecondaryLabelColorForState(int i) {
        if (i == 0) {
            return getColorSecondaryLabelUnavailable();
        }
        if (i == 1) {
            return getColorSecondaryLabelInactive();
        }
        if (i != 2) {
            return 0;
        }
        return getColorSecondaryLabelInactive();
    }

    public void updateResources() {
        super.updateResources();
        int dimensionPixelSize = getResources().getDimensionPixelSize(C1894R.dimen.circle_qs_tile_padding);
        setPaddingRelative(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0017, code lost:
        if ((r4 == 1.0f) == false) goto L_0x001b;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void setExpansion(float r4) {
        /*
            r3 = this;
            r3.position = r4
            r0 = 0
            int r0 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            r1 = 1
            r2 = 0
            if (r0 != 0) goto L_0x000b
            r0 = r1
            goto L_0x000c
        L_0x000b:
            r0 = r2
        L_0x000c:
            if (r0 != 0) goto L_0x001a
            r0 = 1065353216(0x3f800000, float:1.0)
            int r4 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r4 != 0) goto L_0x0016
            r4 = r1
            goto L_0x0017
        L_0x0016:
            r4 = r2
        L_0x0017:
            if (r4 != 0) goto L_0x001a
            goto L_0x001b
        L_0x001a:
            r1 = r2
        L_0x001b:
            boolean r4 = r3.animating
            if (r1 == r4) goto L_0x0033
            r3.animating = r1
            android.graphics.drawable.Drawable r4 = r3.createTileBackground()
            r3.setBackground(r4)
            int r4 = r3.getLastState()
            int r4 = r3.getBackgroundColorForState(r4)
            r3.setColor(r4)
        L_0x0033:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.systemui.p024qs.QSTileViewCircle.setExpansion(float):void");
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        Intrinsics.checkNotNullParameter(motionEvent, "ev");
        boolean onTouchEvent = super.onTouchEvent(motionEvent);
        int action = motionEvent.getAction() & 255;
        if (action == 0) {
            QSTileViewCircle qSTileViewCircle = this;
            resetEvent();
            this.initialX = motionEvent.getX();
            this.initialY = motionEvent.getY();
        } else if (action == 2) {
            float abs = Math.abs(motionEvent.getX() - this.initialX);
            this.interceptClick = abs > ((float) this.touchSlop) && abs > Math.abs(motionEvent.getY() - this.initialY);
        }
        return onTouchEvent;
    }

    public boolean shouldInterceptClick() {
        return this.interceptClick;
    }

    public final void resetEvent() {
        this.initialX = 0.0f;
        this.initialY = 0.0f;
        this.interceptClick = false;
    }
}
