package com.android.systemui.biometrics;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.widget.FrameLayout;
import com.airbnb.lottie.LottieAnimationView;
import com.android.systemui.R$id;
import com.android.systemui.R$styleable;
import com.android.systemui.biometrics.UdfpsSurfaceView;
import com.android.systemui.doze.DozeReceiver;
/* loaded from: classes.dex */
public class UdfpsView extends FrameLayout implements DozeReceiver {
    private UdfpsAnimationViewController mAnimationViewController;
    private String mDebugMessage;
    private final Paint mDebugTextPaint;
    private UdfpsSurfaceView mGhbmView;
    private UdfpsHbmProvider mHbmProvider;
    private final int mHbmType;
    private boolean mIlluminationRequested;
    private final int mOnIlluminatedDelayMs;
    private LottieAnimationView mScanningAnimView;
    private FingerprintSensorPropertiesInternal mSensorProps;
    private final RectF mSensorRect;
    private final float mSensorTouchAreaCoefficient;
    private UdfpsViewVisibilityListener mVisibilityListener;
    private boolean mEnableScanningAnim = false;
    private int mUdfpsScanningViewSize = 295;

    /* loaded from: classes.dex */
    public interface UdfpsViewVisibilityListener {
        void onVisibilityChanged(int i);
    }

    public UdfpsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R$styleable.UdfpsView, 0, 0);
        try {
            int i = R$styleable.UdfpsView_sensorTouchAreaCoefficient;
            if (!obtainStyledAttributes.hasValue(i)) {
                throw new IllegalArgumentException("UdfpsView must contain sensorTouchAreaCoefficient");
            }
            this.mSensorTouchAreaCoefficient = obtainStyledAttributes.getFloat(i, 0.0f);
            obtainStyledAttributes.recycle();
            this.mSensorRect = new RectF();
            Paint paint = new Paint();
            this.mDebugTextPaint = paint;
            paint.setAntiAlias(true);
            paint.setColor(-16776961);
            paint.setTextSize(32.0f);
            this.mOnIlluminatedDelayMs = ((FrameLayout) this).mContext.getResources().getInteger(17694939);
            if (Build.IS_ENG || Build.IS_USERDEBUG) {
                this.mHbmType = Settings.Secure.getIntForUser(((FrameLayout) this).mContext.getContentResolver(), "com.android.systemui.biometrics.UdfpsSurfaceView.hbmType", 0, -2);
            } else {
                this.mHbmType = 0;
            }
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        UdfpsAnimationViewController udfpsAnimationViewController = this.mAnimationViewController;
        return udfpsAnimationViewController == null || !udfpsAnimationViewController.shouldPauseAuth();
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        if (this.mHbmType == 0) {
            this.mGhbmView = (UdfpsSurfaceView) findViewById(R$id.hbm_view);
            LottieAnimationView lottieAnimationView = (LottieAnimationView) findViewById(R$id.nt_fp_scanning);
            this.mScanningAnimView = lottieAnimationView;
            lottieAnimationView.setAlpha(0.0f);
            this.mScanningAnimView.pauseAnimation();
            this.mScanningAnimView.setProgress(0.0f);
            updateViewLayoutParams();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSensorProperties(FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal) {
        this.mSensorProps = fingerprintSensorPropertiesInternal;
    }

    public void setHbmProvider(UdfpsHbmProvider udfpsHbmProvider) {
        this.mHbmProvider = udfpsHbmProvider;
    }

    @Override // com.android.systemui.doze.DozeReceiver
    public void dozeTimeTick() {
        UdfpsAnimationViewController udfpsAnimationViewController = this.mAnimationViewController;
        if (udfpsAnimationViewController != null) {
            udfpsAnimationViewController.dozeTimeTick();
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        UdfpsAnimationViewController udfpsAnimationViewController = this.mAnimationViewController;
        int i5 = 0;
        int paddingX = udfpsAnimationViewController == null ? 0 : udfpsAnimationViewController.getPaddingX();
        UdfpsAnimationViewController udfpsAnimationViewController2 = this.mAnimationViewController;
        if (udfpsAnimationViewController2 != null) {
            i5 = udfpsAnimationViewController2.getPaddingY();
        }
        int i6 = this.mSensorProps.sensorRadius;
        this.mSensorRect.set(paddingX, i5, (i6 * 2) + paddingX, (i6 * 2) + i5);
        UdfpsAnimationViewController udfpsAnimationViewController3 = this.mAnimationViewController;
        if (udfpsAnimationViewController3 != null) {
            udfpsAnimationViewController3.onSensorRectUpdated(new RectF(this.mSensorRect));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTouchOutsideView() {
        UdfpsAnimationViewController udfpsAnimationViewController = this.mAnimationViewController;
        if (udfpsAnimationViewController != null) {
            udfpsAnimationViewController.onTouchOutsideView();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAnimationViewController(UdfpsAnimationViewController udfpsAnimationViewController) {
        this.mAnimationViewController = udfpsAnimationViewController;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UdfpsAnimationViewController getAnimationViewController() {
        return this.mAnimationViewController;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.v("UdfpsView", "onAttachedToWindow");
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.v("UdfpsView", "onDetachedFromWindow");
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mIlluminationRequested || TextUtils.isEmpty(this.mDebugMessage)) {
            return;
        }
        canvas.drawText(this.mDebugMessage, 0.0f, 160.0f, this.mDebugTextPaint);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDebugMessage(String str) {
        this.mDebugMessage = str;
        postInvalidate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isWithinSensorArea(float f, float f2) {
        PointF touchTranslation;
        UdfpsAnimationViewController udfpsAnimationViewController = this.mAnimationViewController;
        if (udfpsAnimationViewController == null) {
            touchTranslation = new PointF(0.0f, 0.0f);
        } else {
            touchTranslation = udfpsAnimationViewController.getTouchTranslation();
        }
        float centerX = this.mSensorRect.centerX() + touchTranslation.x + ((this.mUdfpsScanningViewSize / 2) - this.mSensorProps.sensorRadius);
        float centerY = this.mSensorRect.centerY() + touchTranslation.y + ((this.mUdfpsScanningViewSize / 2) - this.mSensorProps.sensorRadius);
        RectF rectF = this.mSensorRect;
        float f3 = (rectF.right - rectF.left) / 2.0f;
        float f4 = (rectF.bottom - rectF.top) / 2.0f;
        Log.v("UdfpsView", "isWithinSensorArea | centerX()=" + this.mSensorRect.centerX() + ", centerY=" + this.mSensorRect.centerY() + "，translation.x=" + touchTranslation.x + "，translation.y=" + touchTranslation.y + "，mSensorRect=" + this.mSensorRect + ",x=" + f + ", y=" + f2 + ", mSensorTouchAreaCoefficient=" + this.mSensorTouchAreaCoefficient + ", cx=" + centerX + ", rx=" + f3 + ", cy=" + centerY + ", ry=" + f4 + ", " + ((this.mUdfpsScanningViewSize / 2) - this.mSensorProps.sensorRadius));
        float f5 = this.mSensorTouchAreaCoefficient;
        return f > centerX - (f3 * f5) && f < centerX + (f3 * f5) && f2 > centerY - (f4 * f5) && f2 < centerY + (f4 * f5) && !this.mAnimationViewController.shouldPauseAuth();
    }

    public void startIllumination(Runnable runnable) {
        this.mIlluminationRequested = true;
        UdfpsAnimationViewController udfpsAnimationViewController = this.mAnimationViewController;
        if (udfpsAnimationViewController != null) {
            udfpsAnimationViewController.onIlluminationStarting();
        }
        UdfpsSurfaceView udfpsSurfaceView = this.mGhbmView;
        if (udfpsSurfaceView != null) {
            udfpsSurfaceView.setGhbmIlluminationListener(new UdfpsSurfaceView.GhbmIlluminationListener() { // from class: com.android.systemui.biometrics.UdfpsView$$ExternalSyntheticLambda0
                @Override // com.android.systemui.biometrics.UdfpsSurfaceView.GhbmIlluminationListener
                public final void enableGhbm(Surface surface, Runnable runnable2) {
                    UdfpsView.this.doIlluminate(surface, runnable2);
                }
            });
            this.mGhbmView.setVisibility(0);
            startScanningAnimation();
            this.mGhbmView.startGhbmIllumination(runnable);
            return;
        }
        doIlluminate(null, runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doIlluminate(Surface surface, final Runnable runnable) {
        if (this.mGhbmView != null && surface == null) {
            Log.e("UdfpsView", "doIlluminate | surface must be non-null for GHBM");
        }
        UdfpsHbmProvider udfpsHbmProvider = this.mHbmProvider;
        if (udfpsHbmProvider != null) {
            udfpsHbmProvider.enableHbm(this.mHbmType, surface, new Runnable() { // from class: com.android.systemui.biometrics.UdfpsView$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    UdfpsView.this.lambda$doIlluminate$0(runnable);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doIlluminate$0(Runnable runnable) {
        UdfpsSurfaceView udfpsSurfaceView = this.mGhbmView;
        if (udfpsSurfaceView != null) {
            udfpsSurfaceView.drawIlluminationDot(this.mSensorRect);
        }
        if (runnable != null) {
            postDelayed(runnable, this.mOnIlluminatedDelayMs);
        } else {
            Log.w("UdfpsView", "doIlluminate | onIlluminatedRunnable is null");
        }
    }

    public void stopIllumination() {
        this.mIlluminationRequested = false;
        UdfpsAnimationViewController udfpsAnimationViewController = this.mAnimationViewController;
        if (udfpsAnimationViewController != null) {
            udfpsAnimationViewController.onIlluminationStopped();
        }
        UdfpsSurfaceView udfpsSurfaceView = this.mGhbmView;
        if (udfpsSurfaceView != null) {
            udfpsSurfaceView.setGhbmIlluminationListener(null);
            this.mGhbmView.setVisibility(4);
            stopScanningAnimation();
        }
        UdfpsHbmProvider udfpsHbmProvider = this.mHbmProvider;
        if (udfpsHbmProvider != null) {
            udfpsHbmProvider.disableHbm(null);
        }
    }

    public void startIlluminationNT(Runnable runnable) {
        Log.i("UdfpsView", "HBM startIlluminationNT");
        this.mIlluminationRequested = true;
        UdfpsAnimationViewController udfpsAnimationViewController = this.mAnimationViewController;
        if (udfpsAnimationViewController != null) {
            udfpsAnimationViewController.onIlluminationStarting();
        }
        UdfpsSurfaceView udfpsSurfaceView = this.mGhbmView;
        if (udfpsSurfaceView != null) {
            udfpsSurfaceView.setGhbmIlluminationListener(new UdfpsSurfaceView.GhbmIlluminationListener() { // from class: com.android.systemui.biometrics.UdfpsView$$ExternalSyntheticLambda1
                @Override // com.android.systemui.biometrics.UdfpsSurfaceView.GhbmIlluminationListener
                public final void enableGhbm(Surface surface, Runnable runnable2) {
                    UdfpsView.this.doIlluminateNT(surface, runnable2);
                }
            });
            this.mGhbmView.setVisibility(0);
            startScanningAnimation();
            this.mGhbmView.startGhbmIllumination(runnable);
            return;
        }
        doIlluminateNT(null, runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doIlluminateNT(Surface surface, Runnable runnable) {
        if (this.mGhbmView != null && surface == null) {
            Log.e("UdfpsView", "doIlluminate | surface must be non-null for GHBM");
        }
        if (this.mHbmProvider == null) {
            Log.i("UdfpsView", "doIlluminate | mHbmProvider is null");
        }
        UdfpsSurfaceView udfpsSurfaceView = this.mGhbmView;
        if (udfpsSurfaceView != null) {
            udfpsSurfaceView.drawIlluminationDot(this.mSensorRect);
            if (runnable != null) {
                Log.i("UdfpsView", "doIlluminate | onIlluminatedRunnable.run====");
                runnable.run();
                return;
            }
            Log.w("UdfpsView", "doIlluminate | onIlluminatedRunnable is null");
            return;
        }
        Log.i("UdfpsView", "doIlluminate | mGhbmView is null");
    }

    public void stopIlluminationNT() {
        Log.i("UdfpsView", "HBM stopIlluminationNT");
        this.mIlluminationRequested = false;
        UdfpsAnimationViewController udfpsAnimationViewController = this.mAnimationViewController;
        if (udfpsAnimationViewController != null) {
            udfpsAnimationViewController.onIlluminationStopped();
        }
        UdfpsSurfaceView udfpsSurfaceView = this.mGhbmView;
        if (udfpsSurfaceView != null) {
            udfpsSurfaceView.setGhbmIlluminationListener(null);
            this.mGhbmView.setVisibility(4);
            stopScanningAnimation();
        }
    }

    private void startScanningAnimation() {
        LottieAnimationView lottieAnimationView = this.mScanningAnimView;
        if (lottieAnimationView == null || !this.mEnableScanningAnim) {
            return;
        }
        lottieAnimationView.setAlpha(1.0f);
        this.mScanningAnimView.playAnimation();
    }

    private void stopScanningAnimation() {
        LottieAnimationView lottieAnimationView = this.mScanningAnimView;
        if (lottieAnimationView == null || !this.mEnableScanningAnim) {
            return;
        }
        lottieAnimationView.setAlpha(0.0f);
        this.mScanningAnimView.pauseAnimation();
        this.mScanningAnimView.setProgress(0.0f);
    }

    private void updateViewLayoutParams() {
        Log.i("UdfpsView", "updateViewLayoutParams mSensorProps= " + this.mSensorProps + ", mUdfpsScanningViewRadius=" + this.mUdfpsScanningViewSize);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mScanningAnimView.getLayoutParams();
        int i = this.mUdfpsScanningViewSize;
        layoutParams.width = i;
        layoutParams.height = i;
        this.mScanningAnimView.setLayoutParams(layoutParams);
    }

    public View getGhbmView() {
        return this.mGhbmView;
    }

    public void enableScanningAnim(boolean z) {
        this.mEnableScanningAnim = z;
    }

    @Override // android.view.View
    protected void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        Log.i("UdfpsView", "onVisibilityChanged: " + i);
        UdfpsViewVisibilityListener udfpsViewVisibilityListener = this.mVisibilityListener;
        if (udfpsViewVisibilityListener != null) {
            udfpsViewVisibilityListener.onVisibilityChanged(i);
        }
    }

    public void setVisibilityListener(UdfpsViewVisibilityListener udfpsViewVisibilityListener) {
        this.mVisibilityListener = udfpsViewVisibilityListener;
    }
}
