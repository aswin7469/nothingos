package com.android.p019wm.shell.draganddrop;

import android.animation.ObjectAnimator;
import android.animation.RectEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.IntProperty;
import android.util.Property;
import android.view.animation.Interpolator;
import androidx.constraintlayout.motion.widget.Key;
import com.android.internal.graphics.ColorUtils;
import com.android.internal.policy.ScreenDecorationsUtils;
import com.android.internal.protolog.common.ProtoLog;
import com.android.p019wm.shell.C3343R;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;

/* renamed from: com.android.wm.shell.draganddrop.DropOutlineDrawable */
public class DropOutlineDrawable extends Drawable {
    private static final int ALPHA_DURATION = 135;
    private static final int BOUNDS_DURATION = 200;
    private final IntProperty<DropOutlineDrawable> ALPHA;
    private final Property<DropOutlineDrawable, Rect> BOUNDS = new Property<DropOutlineDrawable, Rect>(Rect.class, "bounds") {
        public void set(DropOutlineDrawable dropOutlineDrawable, Rect rect) {
            dropOutlineDrawable.setRegionBounds(rect);
        }

        public Rect get(DropOutlineDrawable dropOutlineDrawable) {
            return dropOutlineDrawable.getRegionBounds();
        }
    };
    private ObjectAnimator mAlphaAnimator;
    private final Rect mBounds = new Rect();
    private ObjectAnimator mBoundsAnimator;
    private int mColor;
    private final float mCornerRadius;
    private final int mMaxAlpha;
    private final Paint mPaint = new Paint(1);
    private final RectEvaluator mRectEvaluator = new RectEvaluator(new Rect());

    public int getOpacity() {
        return -3;
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    public DropOutlineDrawable(Context context) {
        C34571 r0 = new IntProperty<DropOutlineDrawable>(Key.ALPHA) {
            public /* bridge */ /* synthetic */ void set(Object obj, Object obj2) {
                super.set(obj, (Integer) obj2);
            }

            public void setValue(DropOutlineDrawable dropOutlineDrawable, int i) {
                dropOutlineDrawable.setAlpha(i);
            }

            public Integer get(DropOutlineDrawable dropOutlineDrawable) {
                return Integer.valueOf(dropOutlineDrawable.getAlpha());
            }
        };
        this.ALPHA = r0;
        this.mCornerRadius = ScreenDecorationsUtils.getWindowCornerRadius(context);
        int color = context.getColor(C3343R.C3344color.drop_outline_background);
        this.mColor = color;
        this.mMaxAlpha = Color.alpha(color);
        r0.set(this, 0);
    }

    public void setAlpha(int i) {
        int alphaComponent = ColorUtils.setAlphaComponent(this.mColor, i);
        this.mColor = alphaComponent;
        this.mPaint.setColor(alphaComponent);
        invalidateSelf();
    }

    public int getAlpha() {
        return Color.alpha(this.mColor);
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect rect) {
        invalidateSelf();
    }

    public void draw(Canvas canvas) {
        float f = this.mCornerRadius;
        canvas.drawRoundRect((float) this.mBounds.left, (float) this.mBounds.top, (float) this.mBounds.right, (float) this.mBounds.bottom, f, f, this.mPaint);
    }

    public void setRegionBounds(Rect rect) {
        this.mBounds.set(rect);
        invalidateSelf();
    }

    public Rect getRegionBounds() {
        return this.mBounds;
    }

    /* access modifiers changed from: package-private */
    public ObjectAnimator startBoundsAnimation(Rect rect, Interpolator interpolator) {
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_DRAG_AND_DROP, "Animate bounds: from=%s to=%s", new Object[]{this.mBounds, rect});
        ObjectAnimator objectAnimator = this.mBoundsAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
        ObjectAnimator ofObject = ObjectAnimator.ofObject(this, this.BOUNDS, this.mRectEvaluator, new Rect[]{this.mBounds, rect});
        this.mBoundsAnimator = ofObject;
        ofObject.setDuration(200);
        this.mBoundsAnimator.setInterpolator(interpolator);
        this.mBoundsAnimator.start();
        return this.mBoundsAnimator;
    }

    /* access modifiers changed from: package-private */
    public ObjectAnimator startVisibilityAnimation(boolean z, Interpolator interpolator) {
        ShellProtoLogGroup shellProtoLogGroup = ShellProtoLogGroup.WM_SHELL_DRAG_AND_DROP;
        Object[] objArr = new Object[2];
        int i = 0;
        objArr[0] = Integer.valueOf(Color.alpha(this.mColor));
        objArr[1] = Integer.valueOf(z ? this.mMaxAlpha : 0);
        ProtoLog.v(shellProtoLogGroup, "Animate alpha: from=%d to=%d", objArr);
        ObjectAnimator objectAnimator = this.mAlphaAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
        IntProperty<DropOutlineDrawable> intProperty = this.ALPHA;
        int[] iArr = new int[2];
        iArr[0] = Color.alpha(this.mColor);
        if (z) {
            i = this.mMaxAlpha;
        }
        iArr[1] = i;
        ObjectAnimator ofInt = ObjectAnimator.ofInt(this, intProperty, iArr);
        this.mAlphaAnimator = ofInt;
        ofInt.setDuration(135);
        this.mAlphaAnimator.setInterpolator(interpolator);
        this.mAlphaAnimator.start();
        return this.mAlphaAnimator;
    }
}
