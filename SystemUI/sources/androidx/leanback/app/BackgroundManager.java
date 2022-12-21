package androidx.leanback.app;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.leanback.C0742R;
import androidx.leanback.widget.BackgroundHelper;
import java.lang.ref.WeakReference;

public final class BackgroundManager {
    private static final int CHANGE_BG_DELAY_MS = 500;
    static final boolean DEBUG = false;
    private static final int FADE_DURATION = 500;
    private static final String FRAGMENT_TAG = "androidx.leanback.app.BackgroundManager";
    static final int FULL_ALPHA = 255;
    static final String TAG = "BackgroundManager";
    private final Animator.AnimatorListener mAnimationListener;
    private final ValueAnimator.AnimatorUpdateListener mAnimationUpdateListener;
    final ValueAnimator mAnimator;
    boolean mAttached;
    private boolean mAutoReleaseOnStop = true;
    int mBackgroundColor;
    Drawable mBackgroundDrawable;
    private View mBgView;
    ChangeBackgroundRunnable mChangeRunnable;
    private boolean mChangeRunnablePending;
    Activity mContext;
    private BackgroundFragment mFragmentState;
    Handler mHandler;
    private int mHeightPx;
    int mImageInWrapperIndex;
    int mImageOutWrapperIndex;
    private long mLastSetTime;
    TranslucentLayerDrawable mLayerDrawable;
    private BackgroundContinuityService mService;
    private int mThemeDrawableResourceId;
    private int mWidthPx;

    @Deprecated
    public Drawable getDimLayer() {
        return null;
    }

    @Deprecated
    public void setDimLayer(Drawable drawable) {
    }

    static class BitmapDrawable extends Drawable {
        boolean mMutated;
        ConstantState mState;

        public int getOpacity() {
            return -3;
        }

        static final class ConstantState extends Drawable.ConstantState {
            final Bitmap mBitmap;
            final Matrix mMatrix;
            final Paint mPaint;

            public int getChangingConfigurations() {
                return 0;
            }

            ConstantState(Bitmap bitmap, Matrix matrix) {
                Paint paint = new Paint();
                this.mPaint = paint;
                this.mBitmap = bitmap;
                this.mMatrix = matrix == null ? new Matrix() : matrix;
                paint.setFilterBitmap(true);
            }

            ConstantState(ConstantState constantState) {
                Paint paint = new Paint();
                this.mPaint = paint;
                this.mBitmap = constantState.mBitmap;
                this.mMatrix = constantState.mMatrix != null ? new Matrix(constantState.mMatrix) : new Matrix();
                if (constantState.mPaint.getAlpha() != 255) {
                    paint.setAlpha(constantState.mPaint.getAlpha());
                }
                if (constantState.mPaint.getColorFilter() != null) {
                    paint.setColorFilter(constantState.mPaint.getColorFilter());
                }
                paint.setFilterBitmap(true);
            }

            public Drawable newDrawable() {
                return new BitmapDrawable(this);
            }
        }

        BitmapDrawable(Resources resources, Bitmap bitmap) {
            this(resources, bitmap, (Matrix) null);
        }

        BitmapDrawable(Resources resources, Bitmap bitmap, Matrix matrix) {
            this.mState = new ConstantState(bitmap, matrix);
        }

        BitmapDrawable(ConstantState constantState) {
            this.mState = constantState;
        }

        /* access modifiers changed from: package-private */
        public Bitmap getBitmap() {
            return this.mState.mBitmap;
        }

        public void draw(Canvas canvas) {
            if (this.mState.mBitmap != null) {
                if (this.mState.mPaint.getAlpha() >= 255 || this.mState.mPaint.getColorFilter() == null) {
                    canvas.drawBitmap(this.mState.mBitmap, this.mState.mMatrix, this.mState.mPaint);
                    return;
                }
                throw new IllegalStateException("Can't draw with translucent alpha and color filter");
            }
        }

        public void setAlpha(int i) {
            mutate();
            if (this.mState.mPaint.getAlpha() != i) {
                this.mState.mPaint.setAlpha(i);
                invalidateSelf();
            }
        }

        public void setColorFilter(ColorFilter colorFilter) {
            mutate();
            this.mState.mPaint.setColorFilter(colorFilter);
            invalidateSelf();
        }

        public ColorFilter getColorFilter() {
            return this.mState.mPaint.getColorFilter();
        }

        public ConstantState getConstantState() {
            return this.mState;
        }

        public Drawable mutate() {
            if (!this.mMutated) {
                this.mMutated = true;
                this.mState = new ConstantState(this.mState);
            }
            return this;
        }
    }

    static final class DrawableWrapper {
        int mAlpha = 255;
        final Drawable mDrawable;

        public DrawableWrapper(Drawable drawable) {
            this.mDrawable = drawable;
        }

        public DrawableWrapper(DrawableWrapper drawableWrapper, Drawable drawable) {
            this.mDrawable = drawable;
            this.mAlpha = drawableWrapper.mAlpha;
        }

        public Drawable getDrawable() {
            return this.mDrawable;
        }

        public void setColor(int i) {
            ((ColorDrawable) this.mDrawable).setColor(i);
        }
    }

    static final class TranslucentLayerDrawable extends LayerDrawable {
        int mAlpha = 255;
        WeakReference<BackgroundManager> mManagerWeakReference;
        boolean mSuspendInvalidation;
        DrawableWrapper[] mWrapper;

        public int getOpacity() {
            return -3;
        }

        TranslucentLayerDrawable(BackgroundManager backgroundManager, Drawable[] drawableArr) {
            super(drawableArr);
            this.mManagerWeakReference = new WeakReference<>(backgroundManager);
            int length = drawableArr.length;
            this.mWrapper = new DrawableWrapper[length];
            for (int i = 0; i < length; i++) {
                this.mWrapper[i] = new DrawableWrapper(drawableArr[i]);
            }
        }

        public void setAlpha(int i) {
            if (this.mAlpha != i) {
                this.mAlpha = i;
                invalidateSelf();
                BackgroundManager backgroundManager = this.mManagerWeakReference.get();
                if (backgroundManager != null) {
                    backgroundManager.postChangeRunnable();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void setWrapperAlpha(int i, int i2) {
            DrawableWrapper drawableWrapper = this.mWrapper[i];
            if (drawableWrapper != null) {
                drawableWrapper.mAlpha = i2;
                invalidateSelf();
            }
        }

        public int getAlpha() {
            return this.mAlpha;
        }

        public Drawable mutate() {
            Drawable mutate = super.mutate();
            int numberOfLayers = getNumberOfLayers();
            for (int i = 0; i < numberOfLayers; i++) {
                DrawableWrapper[] drawableWrapperArr = this.mWrapper;
                if (drawableWrapperArr[i] != null) {
                    drawableWrapperArr[i] = new DrawableWrapper(this.mWrapper[i], getDrawable(i));
                }
            }
            return mutate;
        }

        public boolean setDrawableByLayerId(int i, Drawable drawable) {
            return updateDrawable(i, drawable) != null;
        }

        public DrawableWrapper updateDrawable(int i, Drawable drawable) {
            super.setDrawableByLayerId(i, drawable);
            for (int i2 = 0; i2 < getNumberOfLayers(); i2++) {
                if (getId(i2) == i) {
                    this.mWrapper[i2] = new DrawableWrapper(drawable);
                    invalidateSelf();
                    return this.mWrapper[i2];
                }
            }
            return null;
        }

        public void clearDrawable(int i, Context context) {
            for (int i2 = 0; i2 < getNumberOfLayers(); i2++) {
                if (getId(i2) == i) {
                    this.mWrapper[i2] = null;
                    if (!(getDrawable(i2) instanceof EmptyDrawable)) {
                        super.setDrawableByLayerId(i, BackgroundManager.createEmptyDrawable(context));
                        return;
                    }
                    return;
                }
            }
        }

        public int findWrapperIndexById(int i) {
            for (int i2 = 0; i2 < getNumberOfLayers(); i2++) {
                if (getId(i2) == i) {
                    return i2;
                }
            }
            return -1;
        }

        public void invalidateDrawable(Drawable drawable) {
            if (!this.mSuspendInvalidation) {
                super.invalidateDrawable(drawable);
            }
        }

        /* JADX INFO: finally extract failed */
        public void draw(Canvas canvas) {
            Drawable drawable;
            int i;
            int i2;
            int i3 = 0;
            while (true) {
                DrawableWrapper[] drawableWrapperArr = this.mWrapper;
                if (i3 < drawableWrapperArr.length) {
                    DrawableWrapper drawableWrapper = drawableWrapperArr[i3];
                    if (!(drawableWrapper == null || (drawable = drawableWrapper.getDrawable()) == null)) {
                        int alpha = DrawableCompat.getAlpha(drawable);
                        int i4 = this.mAlpha;
                        if (i4 < 255) {
                            i2 = i4 * alpha;
                            i = 1;
                        } else {
                            i = 0;
                            i2 = alpha;
                        }
                        if (this.mWrapper[i3].mAlpha < 255) {
                            i2 *= this.mWrapper[i3].mAlpha;
                            i++;
                        }
                        if (i == 0) {
                            drawable.draw(canvas);
                        } else {
                            if (i == 1) {
                                i2 /= 255;
                            } else if (i == 2) {
                                i2 /= 65025;
                            }
                            try {
                                this.mSuspendInvalidation = true;
                                drawable.setAlpha(i2);
                                drawable.draw(canvas);
                                drawable.setAlpha(alpha);
                                this.mSuspendInvalidation = false;
                            } catch (Throwable th) {
                                this.mSuspendInvalidation = false;
                                throw th;
                            }
                        }
                    }
                    i3++;
                } else {
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public TranslucentLayerDrawable createTranslucentLayerDrawable(LayerDrawable layerDrawable) {
        int numberOfLayers = layerDrawable.getNumberOfLayers();
        Drawable[] drawableArr = new Drawable[numberOfLayers];
        for (int i = 0; i < numberOfLayers; i++) {
            drawableArr[i] = layerDrawable.getDrawable(i);
        }
        TranslucentLayerDrawable translucentLayerDrawable = new TranslucentLayerDrawable(this, drawableArr);
        for (int i2 = 0; i2 < numberOfLayers; i2++) {
            translucentLayerDrawable.setId(i2, layerDrawable.getId(i2));
        }
        return translucentLayerDrawable;
    }

    private static class BackgroundContinuityService {
        private static final boolean DEBUG = false;
        private static final String TAG = "BackgroundContinuity";
        private static BackgroundContinuityService sService = new BackgroundContinuityService();
        private int mColor;
        private int mCount;
        private Drawable mDrawable;
        private int mLastThemeDrawableId;
        private WeakReference<Drawable.ConstantState> mLastThemeDrawableState;

        private BackgroundContinuityService() {
            reset();
        }

        private void reset() {
            this.mColor = 0;
            this.mDrawable = null;
        }

        public static BackgroundContinuityService getInstance() {
            BackgroundContinuityService backgroundContinuityService = sService;
            backgroundContinuityService.mCount++;
            return backgroundContinuityService;
        }

        public void unref() {
            int i = this.mCount;
            if (i > 0) {
                int i2 = i - 1;
                this.mCount = i2;
                if (i2 == 0) {
                    reset();
                    return;
                }
                return;
            }
            throw new IllegalStateException("Can't unref, count " + this.mCount);
        }

        public int getColor() {
            return this.mColor;
        }

        public Drawable getDrawable() {
            return this.mDrawable;
        }

        public void setColor(int i) {
            this.mColor = i;
            this.mDrawable = null;
        }

        public void setDrawable(Drawable drawable) {
            this.mDrawable = drawable;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:4:0x0008, code lost:
            r0 = r0.get();
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public android.graphics.drawable.Drawable getThemeDrawable(android.content.Context r3, int r4) {
            /*
                r2 = this;
                java.lang.ref.WeakReference<android.graphics.drawable.Drawable$ConstantState> r0 = r2.mLastThemeDrawableState
                if (r0 == 0) goto L_0x0015
                int r1 = r2.mLastThemeDrawableId
                if (r1 != r4) goto L_0x0015
                java.lang.Object r0 = r0.get()
                android.graphics.drawable.Drawable$ConstantState r0 = (android.graphics.drawable.Drawable.ConstantState) r0
                if (r0 == 0) goto L_0x0015
                android.graphics.drawable.Drawable r0 = r0.newDrawable()
                goto L_0x0016
            L_0x0015:
                r0 = 0
            L_0x0016:
                if (r0 != 0) goto L_0x0029
                android.graphics.drawable.Drawable r0 = androidx.core.content.ContextCompat.getDrawable(r3, r4)
                java.lang.ref.WeakReference r3 = new java.lang.ref.WeakReference
                android.graphics.drawable.Drawable$ConstantState r1 = r0.getConstantState()
                r3.<init>(r1)
                r2.mLastThemeDrawableState = r3
                r2.mLastThemeDrawableId = r4
            L_0x0029:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.app.BackgroundManager.BackgroundContinuityService.getThemeDrawable(android.content.Context, int):android.graphics.drawable.Drawable");
        }
    }

    /* access modifiers changed from: package-private */
    public Drawable getDefaultDrawable() {
        if (this.mBackgroundColor != 0) {
            return new ColorDrawable(this.mBackgroundColor);
        }
        return getThemeDrawable();
    }

    private Drawable getThemeDrawable() {
        int i = this.mThemeDrawableResourceId;
        Drawable themeDrawable = i != -1 ? this.mService.getThemeDrawable(this.mContext, i) : null;
        return themeDrawable == null ? createEmptyDrawable(this.mContext) : themeDrawable;
    }

    public static BackgroundManager getInstance(Activity activity) {
        BackgroundManager backgroundManager;
        BackgroundFragment backgroundFragment = (BackgroundFragment) activity.getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (backgroundFragment == null || (backgroundManager = backgroundFragment.getBackgroundManager()) == null) {
            return new BackgroundManager(activity);
        }
        return backgroundManager;
    }

    private BackgroundManager(Activity activity) {
        C07471 r1 = new Animator.AnimatorListener() {
            final Runnable mRunnable = new Runnable() {
                public void run() {
                    BackgroundManager.this.postChangeRunnable();
                }
            };

            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
                if (BackgroundManager.this.mLayerDrawable != null) {
                    BackgroundManager.this.mLayerDrawable.clearDrawable(C0742R.C0745id.background_imageout, BackgroundManager.this.mContext);
                }
                BackgroundManager.this.mHandler.post(this.mRunnable);
            }
        };
        this.mAnimationListener = r1;
        C07492 r2 = new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                if (BackgroundManager.this.mImageInWrapperIndex != -1) {
                    BackgroundManager.this.mLayerDrawable.setWrapperAlpha(BackgroundManager.this.mImageInWrapperIndex, intValue);
                }
            }
        };
        this.mAnimationUpdateListener = r2;
        this.mContext = activity;
        this.mService = BackgroundContinuityService.getInstance();
        this.mHeightPx = this.mContext.getResources().getDisplayMetrics().heightPixels;
        this.mWidthPx = this.mContext.getResources().getDisplayMetrics().widthPixels;
        this.mHandler = new Handler();
        FastOutLinearInInterpolator fastOutLinearInInterpolator = new FastOutLinearInInterpolator();
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{0, 255});
        this.mAnimator = ofInt;
        ofInt.addListener(r1);
        ofInt.addUpdateListener(r2);
        ofInt.setInterpolator(fastOutLinearInInterpolator);
        TypedArray obtainStyledAttributes = activity.getTheme().obtainStyledAttributes(new int[]{16842836});
        this.mThemeDrawableResourceId = obtainStyledAttributes.getResourceId(0, -1);
        obtainStyledAttributes.recycle();
        createFragment(activity);
    }

    private void createFragment(Activity activity) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        String str = FRAGMENT_TAG;
        BackgroundFragment backgroundFragment = (BackgroundFragment) fragmentManager.findFragmentByTag(str);
        if (backgroundFragment == null) {
            backgroundFragment = new BackgroundFragment();
            activity.getFragmentManager().beginTransaction().add(backgroundFragment, str).commit();
        } else if (backgroundFragment.getBackgroundManager() != null) {
            throw new IllegalStateException("Created duplicated BackgroundManager for same activity, please use getInstance() instead");
        }
        backgroundFragment.setBackgroundManager(this);
        this.mFragmentState = backgroundFragment;
    }

    /* access modifiers changed from: package-private */
    public DrawableWrapper getImageInWrapper() {
        TranslucentLayerDrawable translucentLayerDrawable = this.mLayerDrawable;
        if (translucentLayerDrawable == null) {
            return null;
        }
        return translucentLayerDrawable.mWrapper[this.mImageInWrapperIndex];
    }

    /* access modifiers changed from: package-private */
    public DrawableWrapper getImageOutWrapper() {
        TranslucentLayerDrawable translucentLayerDrawable = this.mLayerDrawable;
        if (translucentLayerDrawable == null) {
            return null;
        }
        return translucentLayerDrawable.mWrapper[this.mImageOutWrapperIndex];
    }

    /* access modifiers changed from: package-private */
    public void onActivityStart() {
        updateImmediate();
    }

    /* access modifiers changed from: package-private */
    public void onStop() {
        if (isAutoReleaseOnStop()) {
            release();
        }
    }

    /* access modifiers changed from: package-private */
    public void onResume() {
        postChangeRunnable();
    }

    private void syncWithService() {
        Drawable drawable;
        int color = this.mService.getColor();
        Drawable drawable2 = this.mService.getDrawable();
        this.mBackgroundColor = color;
        if (drawable2 == null) {
            drawable = null;
        } else {
            drawable = drawable2.getConstantState().newDrawable().mutate();
        }
        this.mBackgroundDrawable = drawable;
        updateImmediate();
    }

    public void attach(Window window) {
        attachToViewInternal(window.getDecorView());
    }

    public void setThemeDrawableResourceId(int i) {
        this.mThemeDrawableResourceId = i;
    }

    public void attachToView(View view) {
        attachToViewInternal(view);
        this.mContext.getWindow().getDecorView().setBackground((Drawable) null);
    }

    /* access modifiers changed from: package-private */
    public void attachToViewInternal(View view) {
        if (!this.mAttached) {
            this.mBgView = view;
            this.mAttached = true;
            syncWithService();
            return;
        }
        throw new IllegalStateException("Already attached to " + this.mBgView);
    }

    public boolean isAttached() {
        return this.mAttached;
    }

    /* access modifiers changed from: package-private */
    public void detach() {
        release();
        this.mBgView = null;
        this.mAttached = false;
        BackgroundContinuityService backgroundContinuityService = this.mService;
        if (backgroundContinuityService != null) {
            backgroundContinuityService.unref();
            this.mService = null;
        }
    }

    public void release() {
        ChangeBackgroundRunnable changeBackgroundRunnable = this.mChangeRunnable;
        if (changeBackgroundRunnable != null) {
            this.mHandler.removeCallbacks(changeBackgroundRunnable);
            this.mChangeRunnable = null;
        }
        if (this.mAnimator.isStarted()) {
            this.mAnimator.cancel();
        }
        TranslucentLayerDrawable translucentLayerDrawable = this.mLayerDrawable;
        if (translucentLayerDrawable != null) {
            translucentLayerDrawable.clearDrawable(C0742R.C0745id.background_imagein, this.mContext);
            this.mLayerDrawable.clearDrawable(C0742R.C0745id.background_imageout, this.mContext);
            this.mLayerDrawable = null;
        }
        this.mBackgroundDrawable = null;
    }

    @Deprecated
    public Drawable getDefaultDimLayer() {
        return ContextCompat.getDrawable(this.mContext, C0742R.C0743color.lb_background_protection);
    }

    /* access modifiers changed from: package-private */
    public void postChangeRunnable() {
        if (this.mChangeRunnable != null && this.mChangeRunnablePending && !this.mAnimator.isStarted() && this.mFragmentState.isResumed() && this.mLayerDrawable.getAlpha() >= 255) {
            long runnableDelay = getRunnableDelay();
            this.mLastSetTime = System.currentTimeMillis();
            this.mHandler.postDelayed(this.mChangeRunnable, runnableDelay);
            this.mChangeRunnablePending = false;
        }
    }

    private void lazyInit() {
        if (this.mLayerDrawable == null) {
            TranslucentLayerDrawable createTranslucentLayerDrawable = createTranslucentLayerDrawable((LayerDrawable) ContextCompat.getDrawable(this.mContext, C0742R.C0744drawable.lb_background).mutate());
            this.mLayerDrawable = createTranslucentLayerDrawable;
            this.mImageInWrapperIndex = createTranslucentLayerDrawable.findWrapperIndexById(C0742R.C0745id.background_imagein);
            this.mImageOutWrapperIndex = this.mLayerDrawable.findWrapperIndexById(C0742R.C0745id.background_imageout);
            BackgroundHelper.setBackgroundPreservingAlpha(this.mBgView, this.mLayerDrawable);
        }
    }

    private void updateImmediate() {
        if (this.mAttached) {
            lazyInit();
            if (this.mBackgroundDrawable == null) {
                this.mLayerDrawable.updateDrawable(C0742R.C0745id.background_imagein, getDefaultDrawable());
            } else {
                this.mLayerDrawable.updateDrawable(C0742R.C0745id.background_imagein, this.mBackgroundDrawable);
            }
            this.mLayerDrawable.clearDrawable(C0742R.C0745id.background_imageout, this.mContext);
        }
    }

    public void setColor(int i) {
        this.mService.setColor(i);
        this.mBackgroundColor = i;
        this.mBackgroundDrawable = null;
        if (this.mLayerDrawable != null) {
            setDrawableInternal(getDefaultDrawable());
        }
    }

    public void setDrawable(Drawable drawable) {
        this.mService.setDrawable(drawable);
        this.mBackgroundDrawable = drawable;
        if (this.mLayerDrawable != null) {
            if (drawable == null) {
                setDrawableInternal(getDefaultDrawable());
            } else {
                setDrawableInternal(drawable);
            }
        }
    }

    public void clearDrawable() {
        setDrawable((Drawable) null);
    }

    private void setDrawableInternal(Drawable drawable) {
        if (this.mAttached) {
            ChangeBackgroundRunnable changeBackgroundRunnable = this.mChangeRunnable;
            if (changeBackgroundRunnable != null) {
                if (!sameDrawable(drawable, changeBackgroundRunnable.mDrawable)) {
                    this.mHandler.removeCallbacks(this.mChangeRunnable);
                    this.mChangeRunnable = null;
                } else {
                    return;
                }
            }
            this.mChangeRunnable = new ChangeBackgroundRunnable(drawable);
            this.mChangeRunnablePending = true;
            postChangeRunnable();
            return;
        }
        throw new IllegalStateException("Must attach before setting background drawable");
    }

    private long getRunnableDelay() {
        return Math.max(0, (this.mLastSetTime + 500) - System.currentTimeMillis());
    }

    public void setBitmap(Bitmap bitmap) {
        Matrix matrix = null;
        if (bitmap == null) {
            setDrawable((Drawable) null);
        } else if (bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
            if (!(bitmap.getWidth() == this.mWidthPx && bitmap.getHeight() == this.mHeightPx)) {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int i = this.mHeightPx;
                int i2 = width * i;
                int i3 = this.mWidthPx;
                float f = i2 > i3 * height ? ((float) i) / ((float) height) : ((float) i3) / ((float) width);
                int max = Math.max(0, (width - Math.min((int) (((float) i3) / f), width)) / 2);
                Matrix matrix2 = new Matrix();
                matrix2.setScale(f, f);
                matrix2.preTranslate((float) (-max), 0.0f);
                matrix = matrix2;
            }
            setDrawable(new BitmapDrawable(this.mContext.getResources(), bitmap, matrix));
        }
    }

    public void setAutoReleaseOnStop(boolean z) {
        this.mAutoReleaseOnStop = z;
    }

    public boolean isAutoReleaseOnStop() {
        return this.mAutoReleaseOnStop;
    }

    public final int getColor() {
        return this.mBackgroundColor;
    }

    public Drawable getDrawable() {
        return this.mBackgroundDrawable;
    }

    /* access modifiers changed from: package-private */
    public boolean sameDrawable(Drawable drawable, Drawable drawable2) {
        if (!(drawable == null || drawable2 == null)) {
            if (drawable == drawable2) {
                return true;
            }
            if (!(drawable instanceof BitmapDrawable) || !(drawable2 instanceof BitmapDrawable) || !((BitmapDrawable) drawable).getBitmap().sameAs(((BitmapDrawable) drawable2).getBitmap())) {
                return (drawable instanceof ColorDrawable) && (drawable2 instanceof ColorDrawable) && ((ColorDrawable) drawable).getColor() == ((ColorDrawable) drawable2).getColor();
            }
            return true;
        }
    }

    final class ChangeBackgroundRunnable implements Runnable {
        final Drawable mDrawable;

        ChangeBackgroundRunnable(Drawable drawable) {
            this.mDrawable = drawable;
        }

        public void run() {
            runTask();
            BackgroundManager.this.mChangeRunnable = null;
        }

        private void runTask() {
            if (BackgroundManager.this.mLayerDrawable != null) {
                DrawableWrapper imageInWrapper = BackgroundManager.this.getImageInWrapper();
                if (imageInWrapper != null) {
                    if (!BackgroundManager.this.sameDrawable(this.mDrawable, imageInWrapper.getDrawable())) {
                        BackgroundManager.this.mLayerDrawable.clearDrawable(C0742R.C0745id.background_imagein, BackgroundManager.this.mContext);
                        BackgroundManager.this.mLayerDrawable.updateDrawable(C0742R.C0745id.background_imageout, imageInWrapper.getDrawable());
                    } else {
                        return;
                    }
                }
                applyBackgroundChanges();
            }
        }

        /* access modifiers changed from: package-private */
        public void applyBackgroundChanges() {
            if (BackgroundManager.this.mAttached) {
                if (BackgroundManager.this.getImageInWrapper() == null && this.mDrawable != null) {
                    BackgroundManager.this.mLayerDrawable.updateDrawable(C0742R.C0745id.background_imagein, this.mDrawable);
                    BackgroundManager.this.mLayerDrawable.setWrapperAlpha(BackgroundManager.this.mImageInWrapperIndex, 0);
                }
                BackgroundManager.this.mAnimator.setDuration(500);
                BackgroundManager.this.mAnimator.start();
            }
        }
    }

    static class EmptyDrawable extends BitmapDrawable {
        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        EmptyDrawable(Resources resources) {
            super(resources, (Bitmap) null);
            Bitmap bitmap = null;
        }
    }

    static Drawable createEmptyDrawable(Context context) {
        return new EmptyDrawable(context.getResources());
    }
}
