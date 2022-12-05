package com.nothingos.keyguard.fingerprint;

import android.content.Context;
import android.graphics.Point;
import android.hardware.display.DisplayManagerInternal;
import android.os.Trace;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Choreographer;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import com.android.server.LocalServices;
import com.android.systemui.biometrics.UdfpsController;
/* loaded from: classes2.dex */
public class NTFingerprintDimLayer {
    private static FingerprintDimLayerController mFDC;
    private Context mContext;
    private final DisplayManagerInternal mDisplayManagerInternal = (DisplayManagerInternal) LocalServices.getService(DisplayManagerInternal.class);
    private NaturalSurfaceLayout mSurfaceLayout;
    private UdfpsController mUdfpsController;

    public NTFingerprintDimLayer(Context context, UdfpsController udfpsController) {
        this.mContext = context;
        mFDC = new FingerprintDimLayerController(context);
        this.mUdfpsController = udfpsController;
    }

    public void draw(float f) {
        mFDC.draw(f);
    }

    public void dismiss() {
        mFDC.dismiss();
    }

    public void updateAlpha(float f) {
        mFDC.updateAlpha(f);
    }

    /* loaded from: classes2.dex */
    private class FingerprintDimLayerController {
        private Display mDisplay;
        private int mDisplayHeight;
        private int mDisplayWidth;
        private boolean mIsScreenOn;
        private Surface mSurface;
        private float mSurfaceAlpha;
        private SurfaceControl mSurfaceControl;
        private SurfaceSession mSurfaceSession;
        private final Object mLock = new Object();
        private Runnable mSetLayerRunnable = new Runnable() { // from class: com.nothingos.keyguard.fingerprint.NTFingerprintDimLayer.FingerprintDimLayerController.1
            @Override // java.lang.Runnable
            public void run() {
                SurfaceControl.openTransaction();
                try {
                    SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
                    if (FingerprintDimLayerController.this.mSurfaceControl != null && NTFingerprintDimLayer.this.mUdfpsController.getView() != null && NTFingerprintDimLayer.this.mUdfpsController.getView().getViewRootImpl() != null && NTFingerprintDimLayer.this.mUdfpsController.getView().getViewRootImpl().getSurfaceControl() != null && NTFingerprintDimLayer.this.mUdfpsController.getView().getViewRootImpl().getSurfaceControl().isValid()) {
                        if (NTFingerprintDimLayer.this.mUdfpsController.getView().getViewTreeObserver() != null) {
                            NTFingerprintDimLayer.this.mUdfpsController.getView().getViewTreeObserver().removeOnPreDrawListener(FingerprintDimLayerController.this.mOnPreDrawListener);
                        }
                        transaction.setLayer(FingerprintDimLayerController.this.mSurfaceControl, 1073741827);
                        transaction.setRelativeLayer(NTFingerprintDimLayer.this.mUdfpsController.getView().getViewRootImpl().getSurfaceControl(), FingerprintDimLayerController.this.mSurfaceControl, 1);
                        Log.d("FpDimLayer", "setRelativeLayer");
                        transaction.apply();
                    } else {
                        Log.d("FpDimLayer", "==set relative layer failed in SetLayerRunnable==");
                    }
                } finally {
                    SurfaceControl.closeTransaction();
                }
            }
        };
        private ViewTreeObserver.OnPreDrawListener mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener() { // from class: com.nothingos.keyguard.fingerprint.NTFingerprintDimLayer.FingerprintDimLayerController.2
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                Log.d("FpDimLayer", "onPreDraw");
                if (NTFingerprintDimLayer.this.mUdfpsController.getView() != null && NTFingerprintDimLayer.this.mUdfpsController.getView().getViewRootImpl() != null && NTFingerprintDimLayer.this.mUdfpsController.getView().getViewRootImpl().getSurfaceControl() != null && NTFingerprintDimLayer.this.mUdfpsController.getView().getViewRootImpl().getSurfaceControl().isValid()) {
                    Log.d("FpDimLayer", "onPreDraw: set layer");
                    FingerprintDimLayerController.this.mSetLayerRunnable.run();
                    return true;
                }
                Log.d("FpDimLayer", "onPreDraw: don't set layer.");
                return true;
            }
        };
        Runnable sAnimationCallbackRunnable = new Runnable() { // from class: com.nothingos.keyguard.fingerprint.NTFingerprintDimLayer.FingerprintDimLayerController.3
            @Override // java.lang.Runnable
            public void run() {
                Log.e("FpDimLayer", "Choreographer sAnimationCallbackRunnable run====");
                if (NTFingerprintDimLayer.this.mUdfpsController != null) {
                    Log.e("FpDimLayer", "===doHBM===close");
                    NTFingerprintDimLayer.this.mUdfpsController.closeHBM();
                }
            }
        };
        Runnable sCallbackRunnable = new Runnable() { // from class: com.nothingos.keyguard.fingerprint.NTFingerprintDimLayer.FingerprintDimLayerController.4
            @Override // java.lang.Runnable
            public void run() {
                Log.e("FpDimLayer", "Choreographer sCallbackRunnable run====");
                Choreographer.getInstance().postCallback(0, FingerprintDimLayerController.this.sAnimationCallbackRunnable, null);
            }
        };
        Runnable sRemoveRunnable = new Runnable() { // from class: com.nothingos.keyguard.fingerprint.NTFingerprintDimLayer.FingerprintDimLayerController.5
            @Override // java.lang.Runnable
            public void run() {
                Log.e("FpDimLayer", "===destroySurface=== 120HZ");
                NTFingerprintDimLayer.this.mSurfaceLayout.dispose();
                NTFingerprintDimLayer.this.mSurfaceLayout = null;
                Log.d("FpDimLayer", "remove: mSurfaceControl start= ");
                new SurfaceControl.Transaction().remove(FingerprintDimLayerController.this.mSurfaceControl).apply();
                Log.d("FpDimLayer", "remove: mSurfaceControl end= ");
                FingerprintDimLayerController.this.mSurface.release();
                FingerprintDimLayerController.this.mSurfaceControl = null;
                FingerprintDimLayerController.this.mSurfaceAlpha = 0.0f;
            }
        };

        public FingerprintDimLayerController(Context context) {
            this.mDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
            Point point = new Point();
            this.mDisplay.getRealSize(point);
            int i = point.x;
            int i2 = point.y;
            this.mDisplayHeight = Math.max(i, i2);
            this.mDisplayWidth = Math.min(i, i2);
            Log.i("FpDimLayer", "FingerprintDimLayerController mDisplayWidth=" + this.mDisplayWidth + ", mDisplayHeight=" + this.mDisplayHeight);
        }

        public void dismiss() {
            if (this.mSurfaceControl != null) {
                destroySurface();
            }
        }

        public void draw(float f) {
            Log.i("FpDimLayer", "FingerprintDimLayerController draw alpha=" + f);
            if (f == 0.0f && this.mIsScreenOn) {
                dismiss();
            } else if (this.mSurfaceControl == null) {
                createSurface(f);
            } else {
                showSurface(f, true);
            }
        }

        private void createSurface(float f) {
            synchronized (this.mLock) {
                if (this.mSurfaceSession == null) {
                    this.mSurfaceSession = new SurfaceSession();
                }
                SurfaceControl.openTransaction();
                if (this.mSurfaceControl == null) {
                    try {
                        SurfaceControl.Builder builder = new SurfaceControl.Builder(this.mSurfaceSession);
                        builder.setName("NTFingerprintDimLayer");
                        builder.setFormat(-1);
                        builder.setFlags(1073872900);
                        builder.setColorLayer();
                        this.mSurfaceControl = builder.build();
                        setLayer(false);
                    } catch (Surface.OutOfResourcesException e) {
                        Log.e("FpDimLayer", "Unable to create surface.", e);
                    }
                    new SurfaceControl.Transaction().setBufferSize(this.mSurfaceControl, this.mDisplayWidth, this.mDisplayHeight).apply();
                    Surface surface = new Surface();
                    this.mSurface = surface;
                    surface.copyFrom(this.mSurfaceControl);
                    NTFingerprintDimLayer nTFingerprintDimLayer = NTFingerprintDimLayer.this;
                    nTFingerprintDimLayer.mSurfaceLayout = new NaturalSurfaceLayout(nTFingerprintDimLayer.mDisplayManagerInternal, 0, this.mSurfaceControl);
                    showSurface(f, false);
                }
                SurfaceControl.closeTransaction();
            }
        }

        private void destroySurface() {
            synchronized (this.mLock) {
                if (this.mSurfaceControl != null) {
                    boolean z = Settings.System.getFloat(NTFingerprintDimLayer.this.mContext.getContentResolver(), "peak_refresh_rate", 120.0f) > 65.0f;
                    Log.e("FpDimLayer", "===doHBM===refreshRate===" + Settings.System.getFloat(NTFingerprintDimLayer.this.mContext.getContentResolver(), "peak_refresh_rate", 120.0f));
                    Log.e("FpDimLayer", "===doHBM===is120Hz===" + z);
                    if (z) {
                        Choreographer choreographer = Choreographer.getInstance();
                        choreographer.postCallback(1, this.sRemoveRunnable, null);
                        choreographer.postCallback(1, this.sAnimationCallbackRunnable, null);
                    } else {
                        Log.e("FpDimLayer", "===destroySurface===");
                        NTFingerprintDimLayer.this.mSurfaceLayout.dispose();
                        NTFingerprintDimLayer.this.mSurfaceLayout = null;
                        new SurfaceControl.Transaction().remove(this.mSurfaceControl).apply();
                        this.mSurface.release();
                        this.mSurfaceControl = null;
                        this.mSurfaceAlpha = 0.0f;
                    }
                    Log.d("FpDimLayer", "dismiss");
                }
            }
        }

        private void setLayer(boolean z) {
            if (this.mSurfaceControl == null) {
                return;
            }
            Log.d("FpDimLayer", "setLayer reset=" + z);
            if (NTFingerprintDimLayer.this.mUdfpsController != null && NTFingerprintDimLayer.this.mUdfpsController.getView() != null && NTFingerprintDimLayer.this.mUdfpsController.getView().getViewTreeObserver() != null && NTFingerprintDimLayer.this.mUdfpsController.getView().getViewTreeObserver().isAlive()) {
                Log.d("FpDimLayer", "setLayer addOnPreDrawListener");
                NTFingerprintDimLayer.this.mUdfpsController.getView().getViewTreeObserver().removeOnPreDrawListener(this.mOnPreDrawListener);
                NTFingerprintDimLayer.this.mUdfpsController.getView().getViewTreeObserver().addOnPreDrawListener(this.mOnPreDrawListener);
                return;
            }
            Log.e("FpDimLayer", "setLayer Don't addOnPreDrawListener");
        }

        /* JADX WARN: Code restructure failed: missing block: B:18:0x002a, code lost:
            if (android.text.TextUtils.equals(java.lang.String.valueOf(r6.mSurfaceAlpha), java.lang.String.valueOf(r7)) == false) goto L5;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private boolean showSurface(float f, boolean z) {
            Log.i("FpDimLayer", "showSurface alpha=" + f);
            synchronized (this.mLock) {
                if (!z) {
                }
                SurfaceControl.openTransaction();
                setLayer(false);
                Trace.traceBegin(8L, "set_alpha");
                Log.i("FpDimLayer", "mSurfaceControl show dimlayer alpha=" + f);
                SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
                transaction.setAlpha(this.mSurfaceControl, f);
                transaction.show(this.mSurfaceControl);
                transaction.setSkipScreenshot(this.mSurfaceControl, true);
                transaction.apply();
                Trace.traceEnd(8L);
                SurfaceControl.closeTransaction();
                this.mSurfaceAlpha = f;
                Log.d("FpDimLayer", "drawFrame: alpha=" + f);
            }
            return true;
        }

        public void updateAlpha(float f) {
            Log.i("FpDimLayer", "updateAlpha alpha=" + f);
            if (this.mSurfaceControl == null) {
                return;
            }
            synchronized (this.mLock) {
                if (!TextUtils.equals(String.valueOf(this.mSurfaceAlpha), String.valueOf(f))) {
                    SurfaceControl.openTransaction();
                    setLayer(true);
                    Trace.traceBegin(8L, "set_alpha");
                    SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
                    transaction.setAlpha(this.mSurfaceControl, f);
                    transaction.setSkipScreenshot(this.mSurfaceControl, true);
                    transaction.show(this.mSurfaceControl);
                    transaction.apply();
                    Trace.traceEnd(8L);
                    SurfaceControl.closeTransaction();
                    this.mSurfaceAlpha = f;
                    Log.d("FpDimLayer", "drawFrame: alpha=" + f);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class NaturalSurfaceLayout implements DisplayManagerInternal.DisplayTransactionListener {
        private final int mDisplayId;
        private final DisplayManagerInternal mDisplayManagerInternal;
        private SurfaceControl mSurfaceControl;

        public NaturalSurfaceLayout(DisplayManagerInternal displayManagerInternal, int i, SurfaceControl surfaceControl) {
            this.mDisplayManagerInternal = displayManagerInternal;
            this.mDisplayId = i;
            this.mSurfaceControl = surfaceControl;
        }

        public void dispose() {
            synchronized (this) {
                this.mSurfaceControl = null;
            }
        }

        public void onDisplayTransaction(SurfaceControl.Transaction transaction) {
            synchronized (this) {
                if (this.mSurfaceControl == null) {
                }
            }
        }
    }
}
