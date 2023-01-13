package com.android.p019wm.shell.startingsurface;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Trace;
import android.util.Log;
import android.util.PathParser;
import android.window.SplashScreenView;
import java.util.function.LongConsumer;

/* renamed from: com.android.wm.shell.startingsurface.SplashscreenIconDrawableFactory */
public class SplashscreenIconDrawableFactory {
    private static final String TAG = "ShellStartingWindow";

    static Drawable[] makeIconDrawable(int i, int i2, Drawable drawable, int i3, int i4, boolean z, Handler handler) {
        Drawable drawable2;
        boolean z2 = (i == 0 || i == i2) ? false : true;
        if (drawable instanceof Animatable) {
            drawable2 = new AnimatableIconAnimateListener(drawable);
        } else if (drawable instanceof AdaptiveIconDrawable) {
            drawable2 = new ImmobileIconDrawable(drawable, i3, i4, z, handler);
            z2 = false;
        } else {
            drawable2 = new ImmobileIconDrawable(new AdaptiveForegroundDrawable(drawable), i3, i4, z, handler);
        }
        return new Drawable[]{drawable2, z2 ? new MaskBackgroundDrawable(i) : null};
    }

    static Drawable[] makeLegacyIconDrawable(Drawable drawable, int i, int i2, boolean z, Handler handler) {
        return new Drawable[]{new ImmobileIconDrawable(drawable, i, i2, z, handler)};
    }

    /* renamed from: com.android.wm.shell.startingsurface.SplashscreenIconDrawableFactory$ImmobileIconDrawable */
    private static class ImmobileIconDrawable extends Drawable {
        private Bitmap mIconBitmap;
        private final Matrix mMatrix;
        private final Paint mPaint = new Paint(7);

        public int getOpacity() {
            return 1;
        }

        public void setAlpha(int i) {
        }

        public void setColorFilter(ColorFilter colorFilter) {
        }

        ImmobileIconDrawable(Drawable drawable, int i, int i2, boolean z, Handler handler) {
            Matrix matrix = new Matrix();
            this.mMatrix = matrix;
            if (z) {
                handler.post(new C3603xa76c91d0(this, drawable, i2));
                return;
            }
            float f = ((float) i2) / ((float) i);
            matrix.setScale(f, f);
            handler.post(new C3604xa76c91d1(this, drawable, i));
        }

        /* access modifiers changed from: private */
        /* renamed from: preDrawIcon */
        public void mo51157xbc58ebb7(Drawable drawable, int i) {
            synchronized (this.mPaint) {
                Trace.traceBegin(32, "preDrawIcon");
                this.mIconBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(this.mIconBitmap);
                drawable.setBounds(0, 0, i, i);
                drawable.draw(canvas);
                Trace.traceEnd(32);
            }
        }

        public void draw(Canvas canvas) {
            synchronized (this.mPaint) {
                Bitmap bitmap = this.mIconBitmap;
                if (bitmap != null) {
                    canvas.drawBitmap(bitmap, this.mMatrix, this.mPaint);
                } else {
                    invalidateSelf();
                }
            }
        }
    }

    /* renamed from: com.android.wm.shell.startingsurface.SplashscreenIconDrawableFactory$MaskBackgroundDrawable */
    public static class MaskBackgroundDrawable extends Drawable {
        static final float DEFAULT_VIEW_PORT_SCALE = 0.6666667f;
        private static final float EXTRA_INSET_PERCENTAGE = 0.25f;
        private static final float MASK_SIZE = 100.0f;
        private static Path sMask;
        private final Paint mBackgroundPaint;
        private final Matrix mMaskMatrix = new Matrix();
        private final Path mMaskScaleOnly = new Path(new Path(sMask));

        public int getOpacity() {
            return 1;
        }

        public void setColorFilter(ColorFilter colorFilter) {
        }

        public MaskBackgroundDrawable(int i) {
            sMask = PathParser.createPathFromPathData(Resources.getSystem().getString(17039987));
            if (i != 0) {
                Paint paint = new Paint(7);
                this.mBackgroundPaint = paint;
                paint.setColor(i);
                paint.setStyle(Paint.Style.FILL);
                return;
            }
            this.mBackgroundPaint = null;
        }

        /* access modifiers changed from: protected */
        public void onBoundsChange(Rect rect) {
            if (!rect.isEmpty()) {
                updateLayerBounds(rect);
            }
        }

        /* access modifiers changed from: protected */
        public void updateLayerBounds(Rect rect) {
            this.mMaskMatrix.setScale(((float) rect.width()) / MASK_SIZE, ((float) rect.height()) / MASK_SIZE);
            sMask.transform(this.mMaskMatrix, this.mMaskScaleOnly);
        }

        public void draw(Canvas canvas) {
            canvas.clipPath(this.mMaskScaleOnly);
            Paint paint = this.mBackgroundPaint;
            if (paint != null) {
                canvas.drawPath(this.mMaskScaleOnly, paint);
            }
        }

        public void setAlpha(int i) {
            Paint paint = this.mBackgroundPaint;
            if (paint != null) {
                paint.setAlpha(i);
            }
        }
    }

    /* renamed from: com.android.wm.shell.startingsurface.SplashscreenIconDrawableFactory$AdaptiveForegroundDrawable */
    private static class AdaptiveForegroundDrawable extends MaskBackgroundDrawable {
        protected final Drawable mForegroundDrawable;
        private final Rect mTmpOutRect = new Rect();

        AdaptiveForegroundDrawable(Drawable drawable) {
            super(0);
            this.mForegroundDrawable = drawable;
        }

        /* access modifiers changed from: protected */
        public void updateLayerBounds(Rect rect) {
            super.updateLayerBounds(rect);
            int width = rect.width() / 2;
            int height = rect.height() / 2;
            int width2 = (int) (((float) rect.width()) / 1.3333334f);
            int height2 = (int) (((float) rect.height()) / 1.3333334f);
            Rect rect2 = this.mTmpOutRect;
            rect2.set(width - width2, height - height2, width + width2, height + height2);
            Drawable drawable = this.mForegroundDrawable;
            if (drawable != null) {
                drawable.setBounds(rect2);
            }
            invalidateSelf();
        }

        public void draw(Canvas canvas) {
            super.draw(canvas);
            this.mForegroundDrawable.draw(canvas);
        }

        public void setColorFilter(ColorFilter colorFilter) {
            this.mForegroundDrawable.setColorFilter(colorFilter);
        }
    }

    /* renamed from: com.android.wm.shell.startingsurface.SplashscreenIconDrawableFactory$AnimatableIconAnimateListener */
    public static class AnimatableIconAnimateListener extends AdaptiveForegroundDrawable implements SplashScreenView.IconAnimateListener {
        private final Animatable mAnimatableIcon = ((Animatable) this.mForegroundDrawable);
        private boolean mAnimationTriggered;
        private AnimatorListenerAdapter mJankMonitoringListener;
        private boolean mRunning;
        private LongConsumer mStartListener;

        public /* bridge */ /* synthetic */ void setColorFilter(ColorFilter colorFilter) {
            super.setColorFilter(colorFilter);
        }

        AnimatableIconAnimateListener(Drawable drawable) {
            super(drawable);
            this.mForegroundDrawable.setCallback(new Drawable.Callback() {
                public void invalidateDrawable(Drawable drawable) {
                    AnimatableIconAnimateListener.this.invalidateSelf();
                }

                public void scheduleDrawable(Drawable drawable, Runnable runnable, long j) {
                    AnimatableIconAnimateListener.this.scheduleSelf(runnable, j);
                }

                public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
                    AnimatableIconAnimateListener.this.unscheduleSelf(runnable);
                }
            });
        }

        public void setAnimationJankMonitoring(AnimatorListenerAdapter animatorListenerAdapter) {
            this.mJankMonitoringListener = animatorListenerAdapter;
        }

        public void prepareAnimate(LongConsumer longConsumer) {
            stopAnimation();
            this.mStartListener = longConsumer;
        }

        private void startAnimation() {
            AnimatorListenerAdapter animatorListenerAdapter = this.mJankMonitoringListener;
            if (animatorListenerAdapter != null) {
                animatorListenerAdapter.onAnimationStart((Animator) null);
            }
            long j = 0;
            try {
                this.mAnimatableIcon.start();
                Animatable animatable = this.mAnimatableIcon;
                if (!(animatable instanceof AnimatedVectorDrawable) || ((AnimatedVectorDrawable) animatable).getTotalDuration() <= 0) {
                    Animatable animatable2 = this.mAnimatableIcon;
                    if ((animatable2 instanceof AnimationDrawable) && ((AnimationDrawable) animatable2).getTotalDuration() > 0) {
                        j = ((AnimationDrawable) this.mAnimatableIcon).getTotalDuration();
                    }
                } else {
                    j = ((AnimatedVectorDrawable) this.mAnimatableIcon).getTotalDuration();
                }
                this.mRunning = true;
                LongConsumer longConsumer = this.mStartListener;
                if (longConsumer != null) {
                    longConsumer.accept(j);
                }
            } catch (Exception e) {
                Log.e("ShellStartingWindow", "Error while running the splash screen animated icon", e);
                this.mRunning = false;
                AnimatorListenerAdapter animatorListenerAdapter2 = this.mJankMonitoringListener;
                if (animatorListenerAdapter2 != null) {
                    animatorListenerAdapter2.onAnimationCancel((Animator) null);
                }
                LongConsumer longConsumer2 = this.mStartListener;
                if (longConsumer2 != null) {
                    longConsumer2.accept(0);
                }
            }
        }

        private void onAnimationEnd() {
            this.mAnimatableIcon.stop();
            AnimatorListenerAdapter animatorListenerAdapter = this.mJankMonitoringListener;
            if (animatorListenerAdapter != null) {
                animatorListenerAdapter.onAnimationEnd((Animator) null);
            }
            this.mStartListener = null;
            this.mRunning = false;
        }

        public void stopAnimation() {
            if (this.mRunning) {
                onAnimationEnd();
                this.mJankMonitoringListener = null;
            }
        }

        private void ensureAnimationStarted() {
            if (!this.mAnimationTriggered) {
                if (!this.mRunning) {
                    startAnimation();
                }
                this.mAnimationTriggered = true;
            }
        }

        public void draw(Canvas canvas) {
            ensureAnimationStarted();
            super.draw(canvas);
        }
    }
}
