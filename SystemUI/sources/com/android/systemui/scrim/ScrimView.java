package com.android.systemui.scrim;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.graphics.ColorUtils;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.systemui.screenshot.SaveImageInBackgroundTask$$ExternalSyntheticLambda0;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class ScrimView extends View {
    private Runnable mChangeRunnable;
    private Executor mChangeRunnableExecutor;
    private PorterDuffColorFilter mColorFilter;
    private final Object mColorLock;
    @GuardedBy({"mColorLock"})
    private final ColorExtractor.GradientColors mColors;
    private Drawable mDrawable;
    private Rect mDrawableBounds;
    private Executor mExecutor;
    private Looper mExecutorLooper;
    private int mTintColor;
    private final ColorExtractor.GradientColors mTmpColors;
    private float mViewAlpha;

    protected boolean canReceivePointerEvents() {
        return false;
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public ScrimView(Context context) {
        this(context, null);
    }

    public ScrimView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ScrimView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public ScrimView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mColorLock = new Object();
        this.mTmpColors = new ColorExtractor.GradientColors();
        this.mViewAlpha = 1.0f;
        ScrimDrawable scrimDrawable = new ScrimDrawable();
        this.mDrawable = scrimDrawable;
        scrimDrawable.setCallback(this);
        this.mColors = new ColorExtractor.GradientColors();
        this.mExecutorLooper = Looper.myLooper();
        this.mExecutor = SaveImageInBackgroundTask$$ExternalSyntheticLambda0.INSTANCE;
        executeOnExecutor(new Runnable() { // from class: com.android.systemui.scrim.ScrimView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ScrimView.this.lambda$new$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        updateColorWithTint(false);
    }

    public void setExecutor(Executor executor, Looper looper) {
        this.mExecutor = executor;
        this.mExecutorLooper = looper;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.mDrawable.getAlpha() > 0) {
            this.mDrawable.draw(canvas);
        }
    }

    @VisibleForTesting
    void setDrawable(final Drawable drawable) {
        executeOnExecutor(new Runnable() { // from class: com.android.systemui.scrim.ScrimView$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                ScrimView.this.lambda$setDrawable$1(drawable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setDrawable$1(Drawable drawable) {
        this.mDrawable = drawable;
        drawable.setCallback(this);
        this.mDrawable.setBounds(getLeft(), getTop(), getRight(), getBottom());
        this.mDrawable.setAlpha((int) (this.mViewAlpha * 255.0f));
        invalidate();
    }

    @Override // android.view.View, android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        super.invalidateDrawable(drawable);
        if (drawable == this.mDrawable) {
            invalidate();
        }
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        Rect rect = this.mDrawableBounds;
        if (rect != null) {
            this.mDrawable.setBounds(rect);
        } else if (!z) {
        } else {
            this.mDrawable.setBounds(i, i2, i3, i4);
            invalidate();
        }
    }

    @Override // android.view.View
    public void setClickable(final boolean z) {
        executeOnExecutor(new Runnable() { // from class: com.android.systemui.scrim.ScrimView$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                ScrimView.this.lambda$setClickable$2(z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setClickable$2(boolean z) {
        super.setClickable(z);
    }

    public void setColors(final ColorExtractor.GradientColors gradientColors, final boolean z) {
        if (gradientColors == null) {
            throw new IllegalArgumentException("Colors cannot be null");
        }
        executeOnExecutor(new Runnable() { // from class: com.android.systemui.scrim.ScrimView$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                ScrimView.this.lambda$setColors$3(gradientColors, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setColors$3(ColorExtractor.GradientColors gradientColors, boolean z) {
        synchronized (this.mColorLock) {
            if (this.mColors.equals(gradientColors)) {
                return;
            }
            this.mColors.set(gradientColors);
            updateColorWithTint(z);
        }
    }

    @VisibleForTesting
    Drawable getDrawable() {
        return this.mDrawable;
    }

    public ColorExtractor.GradientColors getColors() {
        synchronized (this.mColorLock) {
            this.mTmpColors.set(this.mColors);
        }
        return this.mTmpColors;
    }

    public void setTint(int i) {
        setTint(i, false);
    }

    public void setTint(final int i, final boolean z) {
        executeOnExecutor(new Runnable() { // from class: com.android.systemui.scrim.ScrimView$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                ScrimView.this.lambda$setTint$4(i, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setTint$4(int i, boolean z) {
        if (this.mTintColor == i) {
            return;
        }
        this.mTintColor = i;
        updateColorWithTint(z);
    }

    private void updateColorWithTint(boolean z) {
        Drawable drawable = this.mDrawable;
        if (drawable instanceof ScrimDrawable) {
            ((ScrimDrawable) drawable).setColor(ColorUtils.blendARGB(this.mColors.getMainColor(), this.mTintColor, Color.alpha(this.mTintColor) / 255.0f), z);
        } else {
            if (Color.alpha(this.mTintColor) != 0) {
                PorterDuffColorFilter porterDuffColorFilter = this.mColorFilter;
                PorterDuff.Mode mode = porterDuffColorFilter == null ? PorterDuff.Mode.SRC_OVER : porterDuffColorFilter.getMode();
                PorterDuffColorFilter porterDuffColorFilter2 = this.mColorFilter;
                if (porterDuffColorFilter2 == null || porterDuffColorFilter2.getColor() != this.mTintColor) {
                    this.mColorFilter = new PorterDuffColorFilter(this.mTintColor, mode);
                }
            } else {
                this.mColorFilter = null;
            }
            this.mDrawable.setColorFilter(this.mColorFilter);
            this.mDrawable.invalidateSelf();
        }
        Runnable runnable = this.mChangeRunnable;
        if (runnable != null) {
            this.mChangeRunnableExecutor.execute(runnable);
        }
    }

    public int getTint() {
        return this.mTintColor;
    }

    public void setViewAlpha(final float f) {
        if (Float.isNaN(f)) {
            throw new IllegalArgumentException("alpha cannot be NaN: " + f);
        }
        executeOnExecutor(new Runnable() { // from class: com.android.systemui.scrim.ScrimView$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                ScrimView.this.lambda$setViewAlpha$5(f);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setViewAlpha$5(float f) {
        if (f != this.mViewAlpha) {
            this.mViewAlpha = f;
            this.mDrawable.setAlpha((int) (f * 255.0f));
            Runnable runnable = this.mChangeRunnable;
            if (runnable == null) {
                return;
            }
            this.mChangeRunnableExecutor.execute(runnable);
        }
    }

    public float getViewAlpha() {
        return this.mViewAlpha;
    }

    public void setChangeRunnable(Runnable runnable, Executor executor) {
        this.mChangeRunnable = runnable;
        this.mChangeRunnableExecutor = executor;
    }

    private void executeOnExecutor(Runnable runnable) {
        if (this.mExecutor == null || Looper.myLooper() == this.mExecutorLooper) {
            runnable.run();
        } else {
            this.mExecutor.execute(runnable);
        }
    }

    public void enableBottomEdgeConcave(boolean z) {
        Drawable drawable = this.mDrawable;
        if (drawable instanceof ScrimDrawable) {
            ((ScrimDrawable) drawable).setBottomEdgeConcave(z);
        }
    }

    public void setBottomEdgePosition(int i) {
        Drawable drawable = this.mDrawable;
        if (drawable instanceof ScrimDrawable) {
            ((ScrimDrawable) drawable).setBottomEdgePosition(i);
        }
    }

    public void enableRoundedCorners(boolean z) {
        Drawable drawable = this.mDrawable;
        if (drawable instanceof ScrimDrawable) {
            ((ScrimDrawable) drawable).setRoundedCornersEnabled(z);
        }
    }

    public void setDrawableBounds(float f, float f2, float f3, float f4) {
        if (this.mDrawableBounds == null) {
            this.mDrawableBounds = new Rect();
        }
        this.mDrawableBounds.set((int) f, (int) f2, (int) f3, (int) f4);
        this.mDrawable.setBounds(this.mDrawableBounds);
    }

    public void setCornerRadius(int i) {
        Drawable drawable = this.mDrawable;
        if (drawable instanceof ScrimDrawable) {
            ((ScrimDrawable) drawable).setRoundedCorners(i);
        }
    }
}
