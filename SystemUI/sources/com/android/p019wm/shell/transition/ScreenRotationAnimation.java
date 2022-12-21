package com.android.p019wm.shell.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.GraphicBuffer;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.HardwareBuffer;
import android.media.Image;
import android.media.ImageReader;
import android.util.RotationUtils;
import android.util.Slog;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.window.TransitionInfo;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.TransactionPool;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/* renamed from: com.android.wm.shell.transition.ScreenRotationAnimation */
class ScreenRotationAnimation {
    static final int MAX_ANIMATION_DURATION = 10000;
    private final int mAnimHint;
    private SurfaceControl mAnimLeash;
    /* access modifiers changed from: private */
    public SurfaceControl mBackColorSurface;
    private final Context mContext;
    private final Rect mEndBounds;
    private final int mEndHeight;
    private float mEndLuma;
    private final int mEndRotation;
    private final int mEndWidth;
    private Animation mRotateAlphaAnimation;
    private Animation mRotateEnterAnimation;
    private Animation mRotateExitAnimation;
    private SurfaceControl mScreenshotLayer;
    private final Matrix mSnapshotInitialMatrix = new Matrix();
    private final Rect mStartBounds;
    private final int mStartHeight;
    private float mStartLuma;
    private final int mStartRotation;
    private final int mStartWidth;
    private final SurfaceControl mSurfaceControl;
    private final float[] mTmpFloats = new float[9];
    private SurfaceControl.Transaction mTransaction;
    /* access modifiers changed from: private */
    public final TransactionPool mTransactionPool;

    ScreenRotationAnimation(Context context, SurfaceSession surfaceSession, TransactionPool transactionPool, SurfaceControl.Transaction transaction, TransitionInfo.Change change, SurfaceControl surfaceControl, int i) {
        Rect rect = new Rect();
        this.mStartBounds = rect;
        Rect rect2 = new Rect();
        this.mEndBounds = rect2;
        this.mContext = context;
        this.mTransactionPool = transactionPool;
        this.mAnimHint = i;
        SurfaceControl leash = change.getLeash();
        this.mSurfaceControl = leash;
        int width = change.getStartAbsBounds().width();
        this.mStartWidth = width;
        int height = change.getStartAbsBounds().height();
        this.mStartHeight = height;
        this.mEndWidth = change.getEndAbsBounds().width();
        this.mEndHeight = change.getEndAbsBounds().height();
        this.mStartRotation = change.getStartRotation();
        this.mEndRotation = change.getEndRotation();
        rect.set(change.getStartAbsBounds());
        rect2.set(change.getEndAbsBounds());
        this.mAnimLeash = new SurfaceControl.Builder(surfaceSession).setParent(surfaceControl).setEffectLayer().setCallsite("ShellRotationAnimation").setName("Animation leash of screenshot rotation").build();
        try {
            SurfaceControl.ScreenshotHardwareBuffer captureLayers = SurfaceControl.captureLayers(new SurfaceControl.LayerCaptureArgs.Builder(leash).setCaptureSecureLayers(true).setAllowProtected(true).setSourceCrop(new Rect(0, 0, width, height)).build());
            if (captureLayers == null) {
                Slog.w("ShellTransitions", "Unable to take screenshot of display");
                return;
            }
            this.mScreenshotLayer = new SurfaceControl.Builder(surfaceSession).setParent(this.mAnimLeash).setBLASTLayer().setSecure(captureLayers.containsSecureLayers()).setCallsite("ShellRotationAnimation").setName("RotationLayer").build();
            GraphicBuffer createFromHardwareBuffer = GraphicBuffer.createFromHardwareBuffer(captureLayers.getHardwareBuffer());
            transaction.setLayer(this.mAnimLeash, 2010000);
            transaction.setPosition(this.mAnimLeash, 0.0f, 0.0f);
            transaction.setAlpha(this.mAnimLeash, 1.0f);
            transaction.show(this.mAnimLeash);
            transaction.setBuffer(this.mScreenshotLayer, createFromHardwareBuffer);
            transaction.setColorSpace(this.mScreenshotLayer, captureLayers.getColorSpace());
            transaction.show(this.mScreenshotLayer);
            if (!isCustomRotate()) {
                this.mBackColorSurface = new SurfaceControl.Builder(surfaceSession).setParent(surfaceControl).setColorLayer().setCallsite("ShellRotationAnimation").setName("BackColorSurface").build();
                this.mStartLuma = getMedianBorderLuma(captureLayers.getHardwareBuffer(), captureLayers.getColorSpace());
                transaction.setLayer(this.mBackColorSurface, -1);
                SurfaceControl surfaceControl2 = this.mBackColorSurface;
                float f = this.mStartLuma;
                transaction.setColor(surfaceControl2, new float[]{f, f, f});
                transaction.setAlpha(this.mBackColorSurface, 1.0f);
                transaction.show(this.mBackColorSurface);
            }
            setRotation(transaction);
            transaction.apply();
        } catch (Surface.OutOfResourcesException e) {
            Slog.w("ShellTransitions", "Unable to allocate freeze surface", e);
        }
    }

    private boolean isCustomRotate() {
        int i = this.mAnimHint;
        return i == 1 || i == 2;
    }

    private void setRotation(SurfaceControl.Transaction transaction) {
        createRotationMatrix(RotationUtils.deltaRotation(this.mEndRotation, this.mStartRotation), this.mStartWidth, this.mStartHeight, this.mSnapshotInitialMatrix);
        setRotationTransform(transaction, this.mSnapshotInitialMatrix);
    }

    private void setRotationTransform(SurfaceControl.Transaction transaction, Matrix matrix) {
        if (this.mScreenshotLayer != null) {
            matrix.getValues(this.mTmpFloats);
            float[] fArr = this.mTmpFloats;
            transaction.setPosition(this.mScreenshotLayer, fArr[2], fArr[5]);
            SurfaceControl surfaceControl = this.mScreenshotLayer;
            float[] fArr2 = this.mTmpFloats;
            transaction.setMatrix(surfaceControl, fArr2[0], fArr2[3], fArr2[1], fArr2[4]);
            transaction.setAlpha(this.mScreenshotLayer, 1.0f);
            transaction.show(this.mScreenshotLayer);
        }
    }

    public boolean startAnimation(ArrayList<Animator> arrayList, Runnable runnable, float f, ShellExecutor shellExecutor, ShellExecutor shellExecutor2) {
        if (this.mScreenshotLayer == null) {
            return false;
        }
        boolean isCustomRotate = isCustomRotate();
        if (isCustomRotate) {
            this.mRotateExitAnimation = AnimationUtils.loadAnimation(this.mContext, this.mAnimHint == 2 ? 17432711 : 17432712);
            this.mRotateEnterAnimation = AnimationUtils.loadAnimation(this.mContext, 17432710);
            this.mRotateAlphaAnimation = AnimationUtils.loadAnimation(this.mContext, 17432718);
        } else {
            int deltaRotation = RotationUtils.deltaRotation(this.mEndRotation, this.mStartRotation);
            if (deltaRotation == 0) {
                this.mRotateExitAnimation = AnimationUtils.loadAnimation(this.mContext, 17432714);
                this.mRotateEnterAnimation = AnimationUtils.loadAnimation(this.mContext, 17432710);
            } else if (deltaRotation == 1) {
                this.mRotateExitAnimation = AnimationUtils.loadAnimation(this.mContext, 17432725);
                this.mRotateEnterAnimation = AnimationUtils.loadAnimation(this.mContext, 17432724);
            } else if (deltaRotation == 2) {
                this.mRotateExitAnimation = AnimationUtils.loadAnimation(this.mContext, 17432716);
                this.mRotateEnterAnimation = AnimationUtils.loadAnimation(this.mContext, 17432715);
            } else if (deltaRotation == 3) {
                this.mRotateExitAnimation = AnimationUtils.loadAnimation(this.mContext, 17432723);
                this.mRotateEnterAnimation = AnimationUtils.loadAnimation(this.mContext, 17432722);
            }
        }
        this.mRotateExitAnimation.initialize(this.mEndWidth, this.mEndHeight, this.mStartWidth, this.mStartHeight);
        this.mRotateExitAnimation.restrictDuration(10000);
        this.mRotateExitAnimation.scaleCurrentDuration(f);
        this.mRotateEnterAnimation.initialize(this.mEndWidth, this.mEndHeight, this.mStartWidth, this.mStartHeight);
        this.mRotateEnterAnimation.restrictDuration(10000);
        this.mRotateEnterAnimation.scaleCurrentDuration(f);
        this.mTransaction = this.mTransactionPool.acquire();
        if (isCustomRotate) {
            this.mRotateAlphaAnimation.initialize(this.mEndWidth, this.mEndHeight, this.mStartWidth, this.mStartHeight);
            this.mRotateAlphaAnimation.restrictDuration(10000);
            this.mRotateAlphaAnimation.scaleCurrentDuration(f);
            startScreenshotAlphaAnimation(arrayList, runnable, shellExecutor, shellExecutor2);
            startDisplayRotation(arrayList, runnable, shellExecutor, shellExecutor2);
        } else {
            startDisplayRotation(arrayList, runnable, shellExecutor, shellExecutor2);
            startScreenshotRotationAnimation(arrayList, runnable, shellExecutor, shellExecutor2);
        }
        return true;
    }

    private void startDisplayRotation(ArrayList<Animator> arrayList, Runnable runnable, ShellExecutor shellExecutor, ShellExecutor shellExecutor2) {
        DefaultTransitionHandler.startSurfaceAnimation(arrayList, this.mRotateEnterAnimation, this.mSurfaceControl, runnable, this.mTransactionPool, shellExecutor, shellExecutor2, (Point) null, 0.0f, (Rect) null);
    }

    private void startScreenshotRotationAnimation(ArrayList<Animator> arrayList, Runnable runnable, ShellExecutor shellExecutor, ShellExecutor shellExecutor2) {
        DefaultTransitionHandler.startSurfaceAnimation(arrayList, this.mRotateExitAnimation, this.mAnimLeash, runnable, this.mTransactionPool, shellExecutor, shellExecutor2, (Point) null, 0.0f, (Rect) null);
    }

    private void startScreenshotAlphaAnimation(ArrayList<Animator> arrayList, Runnable runnable, ShellExecutor shellExecutor, ShellExecutor shellExecutor2) {
        DefaultTransitionHandler.startSurfaceAnimation(arrayList, this.mRotateAlphaAnimation, this.mAnimLeash, runnable, this.mTransactionPool, shellExecutor, shellExecutor2, (Point) null, 0.0f, (Rect) null);
    }

    private void startColorAnimation(float f, ShellExecutor shellExecutor) {
        int integer = this.mContext.getResources().getInteger(17694931);
        float[] fArr = new float[3];
        float f2 = this.mStartLuma;
        int rgb = Color.rgb(f2, f2, f2);
        float f3 = this.mEndLuma;
        int rgb2 = Color.rgb(f3, f3, f3);
        SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat.overrideDurationScale(1.0f);
        ofFloat.setDuration(((long) integer) * ((long) f));
        ofFloat.addUpdateListener(new ScreenRotationAnimation$$ExternalSyntheticLambda0(this, ofFloat, rgb, rgb2, fArr, acquire));
        final int i = rgb;
        final int i2 = rgb2;
        final float[] fArr2 = fArr;
        final SurfaceControl.Transaction transaction = acquire;
        ofFloat.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator animator) {
                ScreenRotationAnimation.applyColor(i, i2, fArr2, 1.0f, ScreenRotationAnimation.this.mBackColorSurface, transaction);
                ScreenRotationAnimation.this.mTransactionPool.release(transaction);
            }

            public void onAnimationEnd(Animator animator) {
                ScreenRotationAnimation.applyColor(i, i2, fArr2, 1.0f, ScreenRotationAnimation.this.mBackColorSurface, transaction);
                ScreenRotationAnimation.this.mTransactionPool.release(transaction);
            }
        });
        Objects.requireNonNull(ofFloat);
        shellExecutor.execute(new ScreenRotationAnimation$$ExternalSyntheticLambda1(ofFloat));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startColorAnimation$0$com-android-wm-shell-transition-ScreenRotationAnimation */
    public /* synthetic */ void mo51270x9a10279a(ValueAnimator valueAnimator, int i, int i2, float[] fArr, SurfaceControl.Transaction transaction, ValueAnimator valueAnimator2) {
        applyColor(i, i2, fArr, (float) (Math.min(valueAnimator.getDuration(), valueAnimator.getCurrentPlayTime()) / valueAnimator.getDuration()), this.mBackColorSurface, transaction);
    }

    public void kill() {
        SurfaceControl.Transaction transaction = this.mTransaction;
        if (transaction == null) {
            transaction = this.mTransactionPool.acquire();
        }
        if (this.mAnimLeash.isValid()) {
            transaction.remove(this.mAnimLeash);
        }
        SurfaceControl surfaceControl = this.mScreenshotLayer;
        if (surfaceControl != null) {
            if (surfaceControl.isValid()) {
                transaction.remove(this.mScreenshotLayer);
            }
            this.mScreenshotLayer = null;
        }
        SurfaceControl surfaceControl2 = this.mBackColorSurface;
        if (surfaceControl2 != null) {
            if (surfaceControl2.isValid()) {
                transaction.remove(this.mBackColorSurface);
            }
            this.mBackColorSurface = null;
        }
        transaction.apply();
        this.mTransactionPool.release(transaction);
    }

    private static float getMedianBorderLuma(HardwareBuffer hardwareBuffer, ColorSpace colorSpace) {
        if (hardwareBuffer != null && hardwareBuffer.getFormat() == 1 && !hasProtectedContent(hardwareBuffer)) {
            ImageReader newInstance = ImageReader.newInstance(hardwareBuffer.getWidth(), hardwareBuffer.getHeight(), hardwareBuffer.getFormat(), 1);
            newInstance.getSurface().attachAndQueueBufferWithColorSpace(hardwareBuffer, colorSpace);
            Image acquireLatestImage = newInstance.acquireLatestImage();
            if (!(acquireLatestImage == null || acquireLatestImage.getPlanes().length == 0)) {
                Image.Plane plane = acquireLatestImage.getPlanes()[0];
                ByteBuffer buffer = plane.getBuffer();
                int width = acquireLatestImage.getWidth();
                int height = acquireLatestImage.getHeight();
                int pixelStride = plane.getPixelStride();
                int rowStride = plane.getRowStride();
                int i = (width * 2) + (height * 2);
                float[] fArr = new float[i];
                int i2 = 0;
                for (int i3 = 0; i3 < width; i3++) {
                    int i4 = i2 + 1;
                    fArr[i2] = getPixelLuminance(buffer, i3, 0, pixelStride, rowStride);
                    i2 = i4 + 1;
                    fArr[i4] = getPixelLuminance(buffer, i3, height - 1, pixelStride, rowStride);
                }
                for (int i5 = 0; i5 < height; i5++) {
                    int i6 = i2 + 1;
                    fArr[i2] = getPixelLuminance(buffer, 0, i5, pixelStride, rowStride);
                    i2 = i6 + 1;
                    fArr[i6] = getPixelLuminance(buffer, width - 1, i5, pixelStride, rowStride);
                }
                newInstance.close();
                Arrays.sort(fArr);
                return fArr[i / 2];
            }
        }
        return 0.0f;
    }

    private static boolean hasProtectedContent(HardwareBuffer hardwareBuffer) {
        return (hardwareBuffer.getUsage() & 16384) == 16384;
    }

    private static float getPixelLuminance(ByteBuffer byteBuffer, int i, int i2, int i3, int i4) {
        int i5 = (i2 * i4) + (i * i3);
        return Color.valueOf(((byteBuffer.get(i5 + 3) & 255) << 24) | ((byteBuffer.get(i5) & 255) << 16) | 0 | ((byteBuffer.get(i5 + 1) & 255) << 8) | (byteBuffer.get(i5 + 2) & 255)).luminance();
    }

    private static float getLumaOfSurfaceControl(Rect rect, SurfaceControl surfaceControl) {
        SurfaceControl.ScreenshotHardwareBuffer captureLayers;
        if (surfaceControl == null || (captureLayers = SurfaceControl.captureLayers(surfaceControl, new Rect(0, 0, rect.width(), rect.height()), 1.0f)) == null) {
            return 0.0f;
        }
        return getMedianBorderLuma(captureLayers.getHardwareBuffer(), captureLayers.getColorSpace());
    }

    private static void createRotationMatrix(int i, int i2, int i3, Matrix matrix) {
        if (i == 0) {
            matrix.reset();
        } else if (i == 1) {
            matrix.setRotate(90.0f, 0.0f, 0.0f);
            matrix.postTranslate((float) i3, 0.0f);
        } else if (i == 2) {
            matrix.setRotate(180.0f, 0.0f, 0.0f);
            matrix.postTranslate((float) i2, (float) i3);
        } else if (i == 3) {
            matrix.setRotate(270.0f, 0.0f, 0.0f);
            matrix.postTranslate(0.0f, (float) i2);
        }
    }

    /* access modifiers changed from: private */
    public static void applyColor(int i, int i2, float[] fArr, float f, SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
        Color valueOf = Color.valueOf(((Integer) ArgbEvaluator.getInstance().evaluate(f, Integer.valueOf(i), Integer.valueOf(i2))).intValue());
        fArr[0] = valueOf.red();
        fArr[1] = valueOf.green();
        fArr[2] = valueOf.blue();
        if (surfaceControl.isValid()) {
            transaction.setColor(surfaceControl, fArr);
        }
        transaction.apply();
    }
}
