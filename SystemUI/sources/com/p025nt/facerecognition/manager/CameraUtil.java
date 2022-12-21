package com.p025nt.facerecognition.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.SystemClock;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import com.android.systemui.statusbar.notification.stack.StackStateAnimator;
import java.p026io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/* renamed from: com.nt.facerecognition.manager.CameraUtil */
public class CameraUtil {
    private static final String TAG = "CameraUtil";
    public int Angle;
    public int cameraHeight;
    public int cameraId = 1;
    public int cameraWidth;
    public Camera mCamera;

    public Camera openCamera(boolean z, Context context, HashMap<String, Integer> hashMap) {
        int i;
        int i2;
        if (z) {
            try {
                this.cameraId = 0;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            this.cameraId = 1;
        }
        if (hashMap != null) {
            i2 = hashMap.get("width").intValue();
            i = hashMap.get("height").intValue();
        } else {
            i = 320;
            i2 = 480;
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        this.mCamera = Camera.open(this.cameraId);
        Camera.getCameraInfo(this.cameraId, new Camera.CameraInfo());
        Camera.Parameters parameters = this.mCamera.getParameters();
        calBestPreviewSize(this.mCamera.getParameters(), i2, i);
        this.cameraWidth = 640;
        this.cameraHeight = 480;
        parameters.setPreviewSize(640, 480);
        parameters.setPreviewFormat(17);
        parameters.setPictureFormat(256);
        parameters.setExposureCompensation(0);
        this.Angle = getCameraAngle(context);
        Log.w(TAG, "Angle==" + this.Angle + "|cameraWidth=" + this.cameraWidth + "|cameraHeight=" + this.cameraHeight + "|width=" + i2 + "|height=" + i);
        this.mCamera.setParameters(parameters);
        StringBuilder sb = new StringBuilder("openCamera ,time:");
        sb.append(SystemClock.uptimeMillis() - uptimeMillis);
        Log.d(TAG, sb.toString());
        return this.mCamera;
    }

    public void actionDetect(Camera.PreviewCallback previewCallback) {
        Camera camera = this.mCamera;
        if (camera != null) {
            camera.setPreviewCallback(previewCallback);
        }
    }

    public void startPreview(SurfaceTexture surfaceTexture) {
        long uptimeMillis = SystemClock.uptimeMillis();
        Camera camera = this.mCamera;
        if (camera != null) {
            try {
                camera.setDisplayOrientation(90);
                this.mCamera.setPreviewTexture(surfaceTexture);
                this.mCamera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "startPreview SurfaceTexture:" + this.mCamera + ",time:" + (SystemClock.uptimeMillis() - uptimeMillis));
    }

    public void startPreview(Surface surface) {
        long uptimeMillis = SystemClock.uptimeMillis();
        Log.d(TAG, "startPreview mCamera:" + this.mCamera);
        Camera camera = this.mCamera;
        if (camera != null) {
            try {
                camera.setDisplayOrientation(90);
                this.mCamera.setPreviewSurface(surface);
                this.mCamera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "startPreview SurfaceHolder:" + this.mCamera + ",time:" + (SystemClock.uptimeMillis() - uptimeMillis));
    }

    public void startPreview(SurfaceHolder surfaceHolder) {
        long uptimeMillis = SystemClock.uptimeMillis();
        Log.d(TAG, "startPreview mCamera:" + this.mCamera);
        Camera camera = this.mCamera;
        if (camera != null) {
            try {
                camera.setDisplayOrientation(90);
                this.mCamera.setPreviewDisplay(surfaceHolder);
                this.mCamera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "startPreview SurfaceHolder:" + this.mCamera + ",time:" + (SystemClock.uptimeMillis() - uptimeMillis));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x002b, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        r3.mCamera.release();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0033, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r3.mCamera = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x003a, code lost:
        throw r0;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x002d */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void closeCamera() {
        /*
            r3 = this;
            java.lang.String r0 = "closeCamera mCamera:"
            monitor-enter(r3)
            java.lang.String r1 = "CameraUtil"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x003d }
            r2.<init>((java.lang.String) r0)     // Catch:{ all -> 0x003d }
            android.hardware.Camera r0 = r3.mCamera     // Catch:{ all -> 0x003d }
            r2.append((java.lang.Object) r0)     // Catch:{ all -> 0x003d }
            java.lang.String r0 = r2.toString()     // Catch:{ all -> 0x003d }
            android.util.Log.d(r1, r0)     // Catch:{ all -> 0x003d }
            android.hardware.Camera r0 = r3.mCamera     // Catch:{ all -> 0x003d }
            if (r0 == 0) goto L_0x003b
            r1 = 0
            r0.stopPreview()     // Catch:{ RuntimeException -> 0x002d }
            android.hardware.Camera r0 = r3.mCamera     // Catch:{ RuntimeException -> 0x002d }
            r0.setPreviewCallback(r1)     // Catch:{ RuntimeException -> 0x002d }
            android.hardware.Camera r0 = r3.mCamera     // Catch:{ RuntimeException -> 0x002d }
            r0.release()     // Catch:{ RuntimeException -> 0x002d }
        L_0x0028:
            r3.mCamera = r1     // Catch:{ all -> 0x003d }
            goto L_0x003b
        L_0x002b:
            r0 = move-exception
            goto L_0x0038
        L_0x002d:
            android.hardware.Camera r0 = r3.mCamera     // Catch:{ Exception -> 0x0033 }
            r0.release()     // Catch:{ Exception -> 0x0033 }
            goto L_0x0028
        L_0x0033:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x002b }
            goto L_0x0028
        L_0x0038:
            r3.mCamera = r1     // Catch:{ all -> 0x003d }
            throw r0     // Catch:{ all -> 0x003d }
        L_0x003b:
            monitor-exit(r3)
            return
        L_0x003d:
            r0 = move-exception
            monitor-exit(r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.p025nt.facerecognition.manager.CameraUtil.closeCamera():void");
    }

    public Bitmap getBitMap(byte[] bArr, Rect rect, int i, boolean z) {
        int[] iArr = null;
        YuvImage yuvImage = new YuvImage(bArr, this.mCamera.getParameters().getPreviewFormat(), this.cameraWidth, this.cameraHeight, (int[]) null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, this.cameraWidth, this.cameraHeight), 80, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        Bitmap decodeByteArray = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.preRotate((float) i);
        if (!z) {
            matrix.preScale(-1.0f, 1.0f);
        }
        return Bitmap.createBitmap(decodeByteArray, rect.left, rect.top, rect.width(), rect.height(), matrix, true).copy(Bitmap.Config.ARGB_8888, true);
    }

    public int getCameraAngle(Context context) {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(this.cameraId, cameraInfo);
        int rotation = getWindowManager(context).getDefaultDisplay().getRotation();
        int i = 0;
        if (rotation != 0) {
            if (rotation == 1) {
                i = 90;
            } else if (rotation == 2) {
                i = 180;
            } else if (rotation == 3) {
                i = 270;
            }
        }
        if (cameraInfo.facing == 1) {
            return (360 - ((cameraInfo.orientation + i) % StackStateAnimator.ANIMATION_DURATION_STANDARD)) % StackStateAnimator.ANIMATION_DURATION_STANDARD;
        }
        return ((cameraInfo.orientation - i) + StackStateAnimator.ANIMATION_DURATION_STANDARD) % StackStateAnimator.ANIMATION_DURATION_STANDARD;
    }

    private WindowManager getWindowManager(Context context) {
        return (WindowManager) context.getSystemService("window");
    }

    private Camera.Size calBestPreviewSize(Camera.Parameters parameters, final int i, final int i2) {
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        ArrayList arrayList = new ArrayList();
        for (Camera.Size next : supportedPreviewSizes) {
            Log.w("jacoco/org.jacoco.examples.test/src/test", "tmpSize.width===" + next.width + ", tmpSize.height===" + next.height);
            if (next.width > next.height) {
                arrayList.add(next);
            }
        }
        Collections.sort(arrayList, new Comparator<Camera.Size>() {
            public int compare(Camera.Size size, Camera.Size size2) {
                return Math.abs((size.width * size.height) - (i * i2)) - Math.abs((size2.width * size2.height) - (i * i2));
            }
        });
        ((Camera.Size) arrayList.get(0)).width = 640;
        ((Camera.Size) arrayList.get(0)).height = 480;
        Log.w("NearSize", ((Camera.Size) arrayList.get(0)).width + ":" + ((Camera.Size) arrayList.get(0)).height);
        return (Camera.Size) arrayList.get(0);
    }

    public Camera getCamera() {
        return this.mCamera;
    }
}
