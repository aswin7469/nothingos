package com.android.systemui.biometrics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Debug;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.airbnb.lottie.LottieAnimationView;
import com.android.systemui.C1894R;
import com.android.systemui.doze.DozeReceiver;
import com.nothing.systemui.biometrics.NTUdfpsSurfaceView;
import com.nothing.systemui.util.NTLogUtil;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0016\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u0003:\u0001`B\u0017\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\bJ\u0012\u00107\u001a\u0002082\b\u00109\u001a\u0004\u0018\u00010:H\u0002J\b\u0010;\u001a\u000208H\u0016J\u000e\u0010<\u001a\u0002082\u0006\u0010=\u001a\u00020\u001bJ\b\u0010>\u001a\u0004\u0018\u00010?J\u0016\u0010@\u001a\u00020\u001b2\u0006\u0010A\u001a\u0002062\u0006\u0010B\u001a\u000206J\b\u0010C\u001a\u000208H\u0014J\b\u0010D\u001a\u000208H\u0014J\u0010\u0010E\u001a\u0002082\u0006\u0010F\u001a\u00020GH\u0014J\b\u0010H\u001a\u000208H\u0014J\u0010\u0010I\u001a\u00020\u001b2\u0006\u0010J\u001a\u00020KH\u0016J0\u0010L\u001a\u0002082\u0006\u0010M\u001a\u00020\u001b2\u0006\u0010N\u001a\u00020(2\u0006\u0010O\u001a\u00020(2\u0006\u0010P\u001a\u00020(2\u0006\u0010Q\u001a\u00020(H\u0014J\u0006\u0010R\u001a\u000208J\u0018\u0010S\u001a\u0002082\u0006\u0010T\u001a\u00020?2\u0006\u0010U\u001a\u00020(H\u0014J\u0012\u0010V\u001a\u0002082\b\u0010W\u001a\u0004\u0018\u00010!H\u0016J\u0010\u0010X\u001a\u0002082\b\u0010Y\u001a\u0004\u0018\u00010*J\u0012\u0010Z\u001a\u0002082\b\u00109\u001a\u0004\u0018\u00010:H\u0016J\b\u0010[\u001a\u000208H\u0002J\b\u0010\\\u001a\u000208H\u0016J\b\u0010]\u001a\u000208H\u0002J\b\u0010^\u001a\u000208H\u0002J\b\u0010_\u001a\u000208H\u0002R \u0010\t\u001a\b\u0012\u0002\b\u0003\u0018\u00010\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR(\u0010\u0011\u001a\u0004\u0018\u00010\u00102\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0016\u001a\u00020\u0017X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u001a\u001a\u00020\u001bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\u0010\u0010 \u001a\u0004\u0018\u00010!X\u000e¢\u0006\u0002\n\u0000R\u001e\u0010#\u001a\u00020\u001b2\u0006\u0010\"\u001a\u00020\u001b@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b#\u0010\u001dR\u000e\u0010$\u001a\u00020\u001bX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010%\u001a\u0004\u0018\u00010&X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020(XD¢\u0006\u0002\n\u0000R\u0010\u0010)\u001a\u0004\u0018\u00010*X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020,X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010-\u001a\u00020.X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b/\u00100\"\u0004\b1\u00102R\u000e\u00103\u001a\u000204X\u0004¢\u0006\u0002\n\u0000R\u000e\u00105\u001a\u000206X\u0004¢\u0006\u0002\n\u0000¨\u0006a"}, mo65043d2 = {"Lcom/android/systemui/biometrics/UdfpsView;", "Landroid/widget/FrameLayout;", "Lcom/android/systemui/doze/DozeReceiver;", "Lcom/android/systemui/biometrics/UdfpsIlluminator;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "animationViewController", "Lcom/android/systemui/biometrics/UdfpsAnimationViewController;", "getAnimationViewController", "()Lcom/android/systemui/biometrics/UdfpsAnimationViewController;", "setAnimationViewController", "(Lcom/android/systemui/biometrics/UdfpsAnimationViewController;)V", "value", "", "debugMessage", "getDebugMessage", "()Ljava/lang/String;", "setDebugMessage", "(Ljava/lang/String;)V", "debugTextPaint", "Landroid/graphics/Paint;", "ghbmView", "Lcom/nothing/systemui/biometrics/NTUdfpsSurfaceView;", "halControlsIllumination", "", "getHalControlsIllumination", "()Z", "setHalControlsIllumination", "(Z)V", "hbmProvider", "Lcom/android/systemui/biometrics/UdfpsHbmProvider;", "<set-?>", "isIlluminationRequested", "mEnableScanningAnim", "mScanningAnimView", "Lcom/airbnb/lottie/LottieAnimationView;", "mUdfpsScanningViewSize", "", "mVisibilityListener", "Lcom/android/systemui/biometrics/UdfpsView$UdfpsViewVisibilityListener;", "onIlluminatedDelayMs", "", "overlayParams", "Lcom/android/systemui/biometrics/UdfpsOverlayParams;", "getOverlayParams", "()Lcom/android/systemui/biometrics/UdfpsOverlayParams;", "setOverlayParams", "(Lcom/android/systemui/biometrics/UdfpsOverlayParams;)V", "sensorRect", "Landroid/graphics/RectF;", "sensorTouchAreaCoefficient", "", "doIlluminate", "", "onIlluminatedRunnable", "Ljava/lang/Runnable;", "dozeTimeTick", "enableScanningAnim", "enable", "getGhbmView", "Landroid/view/View;", "isWithinSensorArea", "x", "y", "onAttachedToWindow", "onDetachedFromWindow", "onDraw", "canvas", "Landroid/graphics/Canvas;", "onFinishInflate", "onInterceptTouchEvent", "ev", "Landroid/view/MotionEvent;", "onLayout", "changed", "left", "top", "right", "bottom", "onTouchOutsideView", "onVisibilityChanged", "changedView", "visibility", "setHbmProvider", "provider", "setVisibilityListener", "listener", "startIllumination", "startScanningAnimation", "stopIllumination", "stopScanningAnimation", "updateViewLayoutParams", "updateghbmLayoutParams", "UdfpsViewVisibilityListener", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: UdfpsView.kt */
public final class UdfpsView extends FrameLayout implements DozeReceiver, UdfpsIlluminator {
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private UdfpsAnimationViewController<?> animationViewController;
    private String debugMessage;
    private final Paint debugTextPaint;
    private NTUdfpsSurfaceView ghbmView;
    private boolean halControlsIllumination;
    private UdfpsHbmProvider hbmProvider;
    private boolean isIlluminationRequested;
    private boolean mEnableScanningAnim;
    private LottieAnimationView mScanningAnimView;
    private final int mUdfpsScanningViewSize;
    private UdfpsViewVisibilityListener mVisibilityListener;
    private final long onIlluminatedDelayMs;
    private UdfpsOverlayParams overlayParams;
    private final RectF sensorRect;
    private final float sensorTouchAreaCoefficient;

    @Metadata(mo65042d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0006À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/biometrics/UdfpsView$UdfpsViewVisibilityListener;", "", "onVisibilityChanged", "", "visibility", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: UdfpsView.kt */
    public interface UdfpsViewVisibilityListener {
        void onVisibilityChanged(int i);
    }

    private final void doIlluminate(Runnable runnable) {
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
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0082, code lost:
        r11 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0083, code lost:
        kotlin.jdk7.AutoCloseableKt.closeFinally(r12, r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0086, code lost:
        throw r11;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public UdfpsView(android.content.Context r11, android.util.AttributeSet r12) {
        /*
            r10 = this;
            java.lang.String r0 = "context"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r11, r0)
            java.util.LinkedHashMap r0 = new java.util.LinkedHashMap
            r0.<init>()
            java.util.Map r0 = (java.util.Map) r0
            r10._$_findViewCache = r0
            r10.<init>(r11, r12)
            android.graphics.RectF r0 = new android.graphics.RectF
            r0.<init>()
            r10.sensorRect = r0
            android.graphics.Paint r0 = new android.graphics.Paint
            r0.<init>()
            r1 = 1
            r0.setAntiAlias(r1)
            r2 = -16776961(0xffffffffff0000ff, float:-1.7014636E38)
            r0.setColor(r2)
            r2 = 1107296256(0x42000000, float:32.0)
            r0.setTextSize(r2)
            r10.debugTextPaint = r0
            android.content.res.Resources$Theme r0 = r11.getTheme()
            int[] r2 = com.android.systemui.C1894R.styleable.UdfpsView
            r3 = 0
            android.content.res.TypedArray r12 = r0.obtainStyledAttributes(r12, r2, r3, r3)
            java.lang.AutoCloseable r12 = (java.lang.AutoCloseable) r12
            r0 = r12
            android.content.res.TypedArray r0 = (android.content.res.TypedArray) r0     // Catch:{ all -> 0x0080 }
            boolean r2 = r0.hasValue(r1)     // Catch:{ all -> 0x0080 }
            if (r2 == 0) goto L_0x0074
            r2 = 0
            float r0 = r0.getFloat(r1, r2)     // Catch:{ all -> 0x0080 }
            r2 = 0
            kotlin.jdk7.AutoCloseableKt.closeFinally(r12, r2)
            r10.sensorTouchAreaCoefficient = r0
            android.content.res.Resources r11 = r11.getResources()
            r12 = 17694956(0x10e00ec, float:2.6081942E-38)
            int r11 = r11.getInteger(r12)
            long r11 = (long) r11
            r10.onIlluminatedDelayMs = r11
            com.android.systemui.biometrics.UdfpsOverlayParams r11 = new com.android.systemui.biometrics.UdfpsOverlayParams
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            r8 = 31
            r9 = 0
            r2 = r11
            r2.<init>(r3, r4, r5, r6, r7, r8, r9)
            r10.overlayParams = r11
            r10.halControlsIllumination = r1
            r11 = 295(0x127, float:4.13E-43)
            r10.mUdfpsScanningViewSize = r11
            return
        L_0x0074:
            java.lang.String r10 = "UdfpsView must contain sensorTouchAreaCoefficient"
            java.lang.IllegalArgumentException r11 = new java.lang.IllegalArgumentException     // Catch:{ all -> 0x0080 }
            java.lang.String r10 = r10.toString()     // Catch:{ all -> 0x0080 }
            r11.<init>((java.lang.String) r10)     // Catch:{ all -> 0x0080 }
            throw r11     // Catch:{ all -> 0x0080 }
        L_0x0080:
            r10 = move-exception
            throw r10     // Catch:{ all -> 0x0082 }
        L_0x0082:
            r11 = move-exception
            kotlin.jdk7.AutoCloseableKt.closeFinally(r12, r10)
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.biometrics.UdfpsView.<init>(android.content.Context, android.util.AttributeSet):void");
    }

    public final UdfpsAnimationViewController<?> getAnimationViewController() {
        return this.animationViewController;
    }

    public final void setAnimationViewController(UdfpsAnimationViewController<?> udfpsAnimationViewController) {
        this.animationViewController = udfpsAnimationViewController;
    }

    public final UdfpsOverlayParams getOverlayParams() {
        return this.overlayParams;
    }

    public final void setOverlayParams(UdfpsOverlayParams udfpsOverlayParams) {
        Intrinsics.checkNotNullParameter(udfpsOverlayParams, "<set-?>");
        this.overlayParams = udfpsOverlayParams;
    }

    public final boolean getHalControlsIllumination() {
        return this.halControlsIllumination;
    }

    public final void setHalControlsIllumination(boolean z) {
        this.halControlsIllumination = z;
    }

    public final String getDebugMessage() {
        return this.debugMessage;
    }

    public final void setDebugMessage(String str) {
        this.debugMessage = str;
        postInvalidate();
    }

    public final boolean isIlluminationRequested() {
        return this.isIlluminationRequested;
    }

    public void setHbmProvider(UdfpsHbmProvider udfpsHbmProvider) {
        this.hbmProvider = udfpsHbmProvider;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        Intrinsics.checkNotNullParameter(motionEvent, "ev");
        UdfpsAnimationViewController<?> udfpsAnimationViewController = this.animationViewController;
        if (udfpsAnimationViewController != null) {
            Intrinsics.checkNotNull(udfpsAnimationViewController);
            return !udfpsAnimationViewController.shouldPauseAuth();
        }
    }

    public void dozeTimeTick() {
        UdfpsAnimationViewController<?> udfpsAnimationViewController = this.animationViewController;
        if (udfpsAnimationViewController != null) {
            udfpsAnimationViewController.dozeTimeTick();
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        UdfpsAnimationViewController<?> udfpsAnimationViewController = this.animationViewController;
        int i5 = 0;
        int paddingX = udfpsAnimationViewController != null ? udfpsAnimationViewController.getPaddingX() : 0;
        UdfpsAnimationViewController<?> udfpsAnimationViewController2 = this.animationViewController;
        if (udfpsAnimationViewController2 != null) {
            i5 = udfpsAnimationViewController2.getPaddingY();
        }
        this.sensorRect.set((float) paddingX, (float) i5, (float) (this.overlayParams.getSensorBounds().width() + paddingX), (float) (this.overlayParams.getSensorBounds().height() + i5));
        if (this.ghbmView == null) {
            this.ghbmView = (NTUdfpsSurfaceView) findViewById(C1894R.C1898id.hbm_view);
            updateghbmLayoutParams();
        }
        UdfpsAnimationViewController<?> udfpsAnimationViewController3 = this.animationViewController;
        if (udfpsAnimationViewController3 != null) {
            udfpsAnimationViewController3.onSensorRectUpdated(new RectF(this.sensorRect));
        }
    }

    public final void onTouchOutsideView() {
        UdfpsAnimationViewController<?> udfpsAnimationViewController = this.animationViewController;
        if (udfpsAnimationViewController != null) {
            udfpsAnimationViewController.onTouchOutsideView();
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.v("UdfpsView", "onAttachedToWindow");
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.v("UdfpsView", "onDetachedFromWindow");
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        super.onDraw(canvas);
        if (!this.isIlluminationRequested) {
            CharSequence charSequence = this.debugMessage;
            if (!(charSequence == null || charSequence.length() == 0)) {
                String str = this.debugMessage;
                Intrinsics.checkNotNull(str);
                canvas.drawText(str, 0.0f, 160.0f, this.debugTextPaint);
            }
        }
    }

    public final boolean isWithinSensorArea(float f, float f2) {
        PointF pointF;
        UdfpsAnimationViewController<?> udfpsAnimationViewController = this.animationViewController;
        if (udfpsAnimationViewController == null || (pointF = udfpsAnimationViewController.getTouchTranslation()) == null) {
            pointF = new PointF(0.0f, 0.0f);
        }
        int width = this.overlayParams.getSensorBounds().width() / 2;
        float centerX = this.sensorRect.centerX() + pointF.x + ((float) ((this.mUdfpsScanningViewSize / 2) - width));
        float centerY = this.sensorRect.centerY() + pointF.y + ((float) ((this.mUdfpsScanningViewSize / 2) - width));
        float f3 = (this.sensorRect.right - this.sensorRect.left) / 2.0f;
        float f4 = (this.sensorRect.bottom - this.sensorRect.top) / 2.0f;
        StringBuilder sb = new StringBuilder("isWithinSensorArea | centerX()=");
        sb.append(this.sensorRect.centerX()).append(", centerY=").append(this.sensorRect.centerY()).append("，translation.x=").append(pointF.x).append("，translation.y=").append(pointF.y).append("，mSensorRect=").append((Object) this.sensorRect).append(",x=").append(f).append(", y=").append(f2).append(", mSensorTouchAreaCoefficient=").append(this.sensorTouchAreaCoefficient).append(", cx=").append(centerX).append(", rx=").append(f3).append(", cy=").append(centerY).append(", ry=");
        StringBuilder append = sb.append(f4).append(", ").append((this.mUdfpsScanningViewSize / 2) - width).append(",shouldPauseAuth=");
        UdfpsAnimationViewController<?> udfpsAnimationViewController2 = this.animationViewController;
        append.append(udfpsAnimationViewController2 != null ? udfpsAnimationViewController2.shouldPauseAuth() : false);
        NTLogUtil.m1689v("UdfpsView", sb.toString());
        float f5 = this.sensorTouchAreaCoefficient;
        if (f <= centerX - (f3 * f5) || f >= centerX + (f3 * f5) || f2 <= centerY - (f4 * f5) || f2 >= centerY + (f4 * f5)) {
            return false;
        }
        UdfpsAnimationViewController<?> udfpsAnimationViewController3 = this.animationViewController;
        if (!(udfpsAnimationViewController3 != null ? udfpsAnimationViewController3.shouldPauseAuth() : false)) {
            return true;
        }
        return false;
    }

    public void startIllumination(Runnable runnable) {
        NTLogUtil.m1686d("UdfpsView", "startIllumination " + Debug.getCallers(5));
        boolean z = this.isIlluminationRequested;
        this.isIlluminationRequested = true;
        UdfpsAnimationViewController<?> udfpsAnimationViewController = this.animationViewController;
        if (udfpsAnimationViewController != null) {
            udfpsAnimationViewController.onIlluminationStarting();
        }
        if (!z) {
            NTUdfpsSurfaceView nTUdfpsSurfaceView = this.ghbmView;
            if (nTUdfpsSurfaceView != null) {
                nTUdfpsSurfaceView.setVisibility(0);
            }
            NTUdfpsSurfaceView nTUdfpsSurfaceView2 = this.ghbmView;
            if (nTUdfpsSurfaceView2 != null) {
                nTUdfpsSurfaceView2.drawIlluminationDot(this.sensorRect);
            }
            startScanningAnimation();
        }
    }

    public void stopIllumination() {
        NTLogUtil.m1686d("UdfpsView", "stopIllumination " + Debug.getCallers(5));
        this.isIlluminationRequested = false;
        UdfpsAnimationViewController<?> udfpsAnimationViewController = this.animationViewController;
        if (udfpsAnimationViewController != null) {
            udfpsAnimationViewController.onIlluminationStopped();
        }
        NTUdfpsSurfaceView nTUdfpsSurfaceView = this.ghbmView;
        if (nTUdfpsSurfaceView != null) {
            nTUdfpsSurfaceView.setVisibility(4);
        }
        stopScanningAnimation();
        UdfpsHbmProvider udfpsHbmProvider = this.hbmProvider;
        if (udfpsHbmProvider != null) {
            udfpsHbmProvider.disableHbm((Runnable) null);
        }
    }

    public final View getGhbmView() {
        return this.ghbmView;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        LottieAnimationView lottieAnimationView = (LottieAnimationView) findViewById(C1894R.C1898id.nt_fp_scanning);
        this.mScanningAnimView = lottieAnimationView;
        if (lottieAnimationView != null) {
            lottieAnimationView.setAlpha(0.0f);
        }
        LottieAnimationView lottieAnimationView2 = this.mScanningAnimView;
        if (lottieAnimationView2 != null) {
            lottieAnimationView2.pauseAnimation();
        }
        LottieAnimationView lottieAnimationView3 = this.mScanningAnimView;
        if (lottieAnimationView3 != null) {
            lottieAnimationView3.setProgress(0.0f);
        }
        updateViewLayoutParams();
    }

    private final void startScanningAnimation() {
        LottieAnimationView lottieAnimationView;
        if (this.mEnableScanningAnim && (lottieAnimationView = this.mScanningAnimView) != null) {
            lottieAnimationView.setAlpha(1.0f);
            lottieAnimationView.playAnimation();
        }
    }

    private final void stopScanningAnimation() {
        LottieAnimationView lottieAnimationView;
        if (this.mEnableScanningAnim && (lottieAnimationView = this.mScanningAnimView) != null) {
            lottieAnimationView.setAlpha(0.0f);
            lottieAnimationView.pauseAnimation();
            lottieAnimationView.setProgress(0.0f);
        }
    }

    private final void updateghbmLayoutParams() {
        NTUdfpsSurfaceView nTUdfpsSurfaceView = this.ghbmView;
        if (nTUdfpsSurfaceView != null) {
            ViewGroup.LayoutParams layoutParams = nTUdfpsSurfaceView.getLayoutParams();
            if (layoutParams != null) {
                FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) layoutParams;
                int width = this.overlayParams.getSensorBounds().width() / 2;
                layoutParams2.width = this.overlayParams.getSensorBounds().width();
                layoutParams2.height = this.overlayParams.getSensorBounds().height();
                layoutParams2.topMargin = (this.mUdfpsScanningViewSize / 2) - width;
                layoutParams2.setMarginStart((this.mUdfpsScanningViewSize / 2) - width);
                nTUdfpsSurfaceView.setLayoutParams(layoutParams2);
                return;
            }
            throw new NullPointerException("null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
        }
    }

    private final void updateViewLayoutParams() {
        NTLogUtil.m1688i("UdfpsView", "updateViewLayoutParams mUdfpsScanningViewRadius=" + this.mUdfpsScanningViewSize);
        LottieAnimationView lottieAnimationView = this.mScanningAnimView;
        if (lottieAnimationView != null) {
            ViewGroup.LayoutParams layoutParams = lottieAnimationView.getLayoutParams();
            if (layoutParams != null) {
                FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) layoutParams;
                layoutParams2.width = this.mUdfpsScanningViewSize;
                layoutParams2.height = this.mUdfpsScanningViewSize;
                lottieAnimationView.setLayoutParams(layoutParams2);
                return;
            }
            throw new NullPointerException("null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
        }
    }

    public final void enableScanningAnim(boolean z) {
        this.mEnableScanningAnim = z;
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(View view, int i) {
        Intrinsics.checkNotNullParameter(view, "changedView");
        super.onVisibilityChanged(view, i);
        NTLogUtil.m1688i("UdfpsView", "onVisibilityChanged: " + i);
        UdfpsViewVisibilityListener udfpsViewVisibilityListener = this.mVisibilityListener;
        if (udfpsViewVisibilityListener != null) {
            Intrinsics.checkNotNull(udfpsViewVisibilityListener);
            udfpsViewVisibilityListener.onVisibilityChanged(i);
        }
    }

    public final void setVisibilityListener(UdfpsViewVisibilityListener udfpsViewVisibilityListener) {
        this.mVisibilityListener = udfpsViewVisibilityListener;
    }
}
