package com.nothing.systemui.biometrics;

import android.content.Context;
import android.graphics.Point;
import android.hardware.display.DisplayManagerInternal;
import android.os.Trace;
import android.text.TextUtils;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.view.WindowManager;
import com.android.server.LocalServices;
import com.android.systemui.biometrics.UdfpsController;
import com.nothing.systemui.util.NTLogUtil;

public class NTFingerprintDimLayer {
    private static final boolean DEBUG = true;
    private static final String TAG = "FpDimLayer";
    private static FingerprintDimLayerController mFDC;
    private Context mContext;
    private final DisplayManagerInternal mDisplayManagerInternal = ((DisplayManagerInternal) LocalServices.getService(DisplayManagerInternal.class));
    /* access modifiers changed from: private */
    public UdfpsController mUdfpsController;

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

    public SurfaceControl getSurfaceControl() {
        return mFDC.getSurfaceControl();
    }

    public void setScreenState(boolean z) {
        mFDC.setScreenOn(z);
    }

    public void setDimlayerBlowAodContent(boolean z) {
        mFDC.setDimlayerBlowAodContent(z);
    }

    private class FingerprintDimLayerController {
        private boolean mBelowAodContent;
        private Display mDisplay;
        private int mDisplayHeight;
        private int mDisplayWidth;
        private boolean mIsScreenOn;
        private final Object mLock = new Object();
        private Surface mSurface;
        private float mSurfaceAlpha;
        private SurfaceControl mSurfaceControl;
        private SurfaceSession mSurfaceSession;

        public FingerprintDimLayerController(Context context) {
            this.mDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
            Point point = new Point();
            this.mDisplay.getRealSize(point);
            int i = point.x;
            int i2 = point.y;
            this.mDisplayHeight = Math.max(i, i2);
            this.mDisplayWidth = Math.min(i, i2);
            NTLogUtil.m1682i(NTFingerprintDimLayer.TAG, "FingerprintDimLayerController mDisplayWidth=" + this.mDisplayWidth + ", mDisplayHeight=" + this.mDisplayHeight);
        }

        public void dismiss() {
            if (this.mSurfaceControl != null) {
                destroySurface();
            }
        }

        public SurfaceControl getSurfaceControl() {
            return this.mSurfaceControl;
        }

        public void setScreenOn(boolean z) {
            this.mIsScreenOn = z;
        }

        public void setDimlayerBlowAodContent(boolean z) {
            this.mBelowAodContent = z;
        }

        public void draw(float f) {
            NTLogUtil.m1680d(NTFingerprintDimLayer.TAG, "draw alpha=" + f);
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
                try {
                    if (this.mSurfaceControl == null) {
                        NTLogUtil.m1680d(NTFingerprintDimLayer.TAG, "===createSurface===");
                        SurfaceControl.Builder builder = new SurfaceControl.Builder(this.mSurfaceSession);
                        builder.setName("NTFingerprintDimLayer");
                        builder.setFormat(-1);
                        builder.setFlags(131076);
                        builder.setColorLayer();
                        this.mSurfaceControl = builder.build();
                        new SurfaceControl.Transaction().setBufferSize(this.mSurfaceControl, this.mDisplayWidth, this.mDisplayHeight).apply();
                        Surface surface = new Surface();
                        this.mSurface = surface;
                        surface.copyFrom(this.mSurfaceControl);
                        showSurface(f, false);
                    }
                } catch (Surface.OutOfResourcesException e) {
                    NTLogUtil.m1681e(NTFingerprintDimLayer.TAG, "Unable to create surface." + e);
                } catch (Throwable th) {
                    SurfaceControl.closeTransaction();
                    throw th;
                }
                SurfaceControl.closeTransaction();
            }
        }

        private void destroySurface() {
            synchronized (this.mLock) {
                if (this.mSurfaceControl != null) {
                    NTLogUtil.m1680d(NTFingerprintDimLayer.TAG, "===destroySurface===");
                    new SurfaceControl.Transaction().remove(this.mSurfaceControl).apply();
                    Surface surface = this.mSurface;
                    if (surface != null) {
                        surface.release();
                    }
                    this.mSurfaceControl = null;
                    this.mSurfaceAlpha = 0.0f;
                }
            }
            if (NTFingerprintDimLayer.this.mUdfpsController != null) {
                NTFingerprintDimLayer.this.mUdfpsController.checkToReattachView();
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:6:0x002a, code lost:
            if (android.text.TextUtils.equals(java.lang.String.valueOf(r6.mSurfaceAlpha), java.lang.String.valueOf(r7)) == false) goto L_0x002c;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private boolean showSurface(float r7, boolean r8) {
            /*
                r6 = this;
                java.lang.String r0 = "mSurfaceControl show dimlayer alpha="
                java.lang.String r1 = "FpDimLayer"
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                java.lang.String r3 = "showSurface alpha="
                r2.<init>((java.lang.String) r3)
                java.lang.StringBuilder r2 = r2.append((float) r7)
                java.lang.String r2 = r2.toString()
                com.nothing.systemui.util.NTLogUtil.m1680d(r1, r2)
                java.lang.Object r1 = r6.mLock
                monitor-enter(r1)
                r2 = 1
                if (r8 != 0) goto L_0x002c
                float r8 = r6.mSurfaceAlpha     // Catch:{ all -> 0x006e }
                java.lang.String r8 = java.lang.String.valueOf((float) r8)     // Catch:{ all -> 0x006e }
                java.lang.String r3 = java.lang.String.valueOf((float) r7)     // Catch:{ all -> 0x006e }
                boolean r8 = android.text.TextUtils.equals(r8, r3)     // Catch:{ all -> 0x006e }
                if (r8 != 0) goto L_0x0067
            L_0x002c:
                r6.mSurfaceAlpha = r7     // Catch:{ all -> 0x006e }
                android.view.SurfaceControl.openTransaction()     // Catch:{ all -> 0x006e }
                java.lang.String r8 = "set_alpha"
                r3 = 8
                android.os.Trace.traceBegin(r3, r8)     // Catch:{ all -> 0x0069 }
                java.lang.String r8 = "FpDimLayer"
                java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0069 }
                r5.<init>((java.lang.String) r0)     // Catch:{ all -> 0x0069 }
                java.lang.StringBuilder r0 = r5.append((float) r7)     // Catch:{ all -> 0x0069 }
                java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0069 }
                com.nothing.systemui.util.NTLogUtil.m1682i(r8, r0)     // Catch:{ all -> 0x0069 }
                android.view.SurfaceControl$Transaction r8 = new android.view.SurfaceControl$Transaction     // Catch:{ all -> 0x0069 }
                r8.<init>()     // Catch:{ all -> 0x0069 }
                android.view.SurfaceControl r0 = r6.mSurfaceControl     // Catch:{ all -> 0x0069 }
                r8.setAlpha(r0, r7)     // Catch:{ all -> 0x0069 }
                android.view.SurfaceControl r7 = r6.mSurfaceControl     // Catch:{ all -> 0x0069 }
                r8.show(r7)     // Catch:{ all -> 0x0069 }
                android.view.SurfaceControl r6 = r6.mSurfaceControl     // Catch:{ all -> 0x0069 }
                r8.setSkipScreenshot(r6, r2)     // Catch:{ all -> 0x0069 }
                r8.apply()     // Catch:{ all -> 0x0069 }
                android.os.Trace.traceEnd(r3)     // Catch:{ all -> 0x0069 }
                android.view.SurfaceControl.closeTransaction()     // Catch:{ all -> 0x006e }
            L_0x0067:
                monitor-exit(r1)     // Catch:{ all -> 0x006e }
                return r2
            L_0x0069:
                r6 = move-exception
                android.view.SurfaceControl.closeTransaction()     // Catch:{ all -> 0x006e }
                throw r6     // Catch:{ all -> 0x006e }
            L_0x006e:
                r6 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x006e }
                throw r6
            */
            throw new UnsupportedOperationException("Method not decompiled: com.nothing.systemui.biometrics.NTFingerprintDimLayer.FingerprintDimLayerController.showSurface(float, boolean):boolean");
        }

        /* JADX INFO: finally extract failed */
        public void updateAlpha(float f) {
            NTLogUtil.m1680d(NTFingerprintDimLayer.TAG, "updateAlpha alpha=" + f);
            if (this.mSurfaceControl != null) {
                synchronized (this.mLock) {
                    if (!TextUtils.equals(String.valueOf(this.mSurfaceAlpha), String.valueOf(f))) {
                        this.mSurfaceAlpha = f;
                        SurfaceControl.openTransaction();
                        try {
                            Trace.traceBegin(8, "set_alpha");
                            SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
                            transaction.setAlpha(this.mSurfaceControl, f);
                            transaction.setSkipScreenshot(this.mSurfaceControl, true);
                            transaction.show(this.mSurfaceControl);
                            transaction.apply();
                            Trace.traceEnd(8);
                            SurfaceControl.closeTransaction();
                        } catch (Throwable th) {
                            SurfaceControl.closeTransaction();
                            throw th;
                        }
                    }
                }
            }
        }
    }
}