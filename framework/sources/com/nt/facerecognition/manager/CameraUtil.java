package com.nt.facerecognition.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.SystemClock;
import android.provider.SettingsStringUtil;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
/* loaded from: classes4.dex */
public class CameraUtil {
    private static final String TAG = "CameraUtil";
    public int Angle;
    public int cameraHeight;
    public int cameraId = 1;
    public int cameraWidth;
    public Camera mCamera;

    public Camera openCamera(boolean isBackCamera, Context context, HashMap<String, Integer> resolutionMap) {
        try {
            if (isBackCamera) {
                this.cameraId = 0;
            } else {
                this.cameraId = 1;
            }
            int width = 480;
            int height = 320;
            if (resolutionMap != null) {
                width = resolutionMap.get("width").intValue();
                height = resolutionMap.get("height").intValue();
            }
            long time = SystemClock.uptimeMillis();
            this.mCamera = Camera.open(this.cameraId);
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(this.cameraId, cameraInfo);
            Camera.Parameters params = this.mCamera.getParameters();
            calBestPreviewSize(this.mCamera.getParameters(), width, height);
            this.cameraWidth = 640;
            this.cameraHeight = 480;
            params.setPreviewSize(640, 480);
            params.setPreviewFormat(17);
            params.setPictureFormat(256);
            params.setExposureCompensation(0);
            this.Angle = getCameraAngle(context);
            Log.w(TAG, "Angle==" + this.Angle + "|cameraWidth=" + this.cameraWidth + "|cameraHeight=" + this.cameraHeight + "|width=" + width + "|height=" + height);
            this.mCamera.setParameters(params);
            StringBuilder sb = new StringBuilder();
            sb.append("openCamera ,time:");
            sb.append(SystemClock.uptimeMillis() - time);
            Log.d(TAG, sb.toString());
            return this.mCamera;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void actionDetect(Camera.PreviewCallback mActivity) {
        Camera camera = this.mCamera;
        if (camera != null) {
            camera.setPreviewCallback(mActivity);
        }
    }

    public void startPreview(SurfaceTexture surfaceTexture) {
        long time = SystemClock.uptimeMillis();
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
        Log.d(TAG, "startPreview SurfaceTexture:" + this.mCamera + ",time:" + (SystemClock.uptimeMillis() - time));
    }

    public void startPreview(Surface surface) {
        long time = SystemClock.uptimeMillis();
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
        Log.d(TAG, "startPreview SurfaceHolder:" + this.mCamera + ",time:" + (SystemClock.uptimeMillis() - time));
    }

    public void startPreview(SurfaceHolder surfaceHolder) {
        long time = SystemClock.uptimeMillis();
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
        Log.d(TAG, "startPreview SurfaceHolder:" + this.mCamera + ",time:" + (SystemClock.uptimeMillis() - time));
    }

    public synchronized void closeCamera() {
        Log.d(TAG, "closeCamera mCamera:" + this.mCamera);
        Camera camera = this.mCamera;
        if (camera != null) {
            try {
                camera.stopPreview();
                this.mCamera.setPreviewCallback(null);
                this.mCamera.release();
                this.mCamera = null;
                this.mCamera = null;
            } catch (RuntimeException e) {
                try {
                    this.mCamera.release();
                } catch (Exception e2) {
                }
                this.mCamera = null;
            }
        }
    }

    public Bitmap getBitMap(byte[] data, Rect roi, int angle, boolean isBackCamera) {
        YuvImage yuvImage = new YuvImage(data, this.mCamera.getParameters().getPreviewFormat(), this.cameraWidth, this.cameraHeight, null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, this.cameraWidth, this.cameraHeight), 80, byteArrayOutputStream);
        byte[] jpegData = byteArrayOutputStream.toByteArray();
        Bitmap tmpBitmap = BitmapFactory.decodeByteArray(jpegData, 0, jpegData.length);
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.preRotate(angle);
        if (!isBackCamera) {
            matrix.preScale(-1.0f, 1.0f);
        }
        return Bitmap.createBitmap(tmpBitmap, roi.left, roi.top, roi.width(), roi.height(), matrix, true).copy(Bitmap.Config.ARGB_8888, true);
    }

    public int getCameraAngle(Context context) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(this.cameraId, info);
        int rotation = getWindowManager(context).getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case 0:
                degrees = 0;
                break;
            case 1:
                degrees = 90;
                break;
            case 2:
                degrees = 180;
                break;
            case 3:
                degrees = 270;
                break;
        }
        if (info.facing == 1) {
            int rotateAngle = (info.orientation + degrees) % 360;
            return (360 - rotateAngle) % 360;
        }
        int rotateAngle2 = ((info.orientation - degrees) + 360) % 360;
        return rotateAngle2;
    }

    private WindowManager getWindowManager(Context context) {
        WindowManager wm = null;
        if (context != null && (wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE)) == null) {
            Log.e(TAG, "Unable to find WindowManager interface.");
        }
        return wm;
    }

    private Camera.Size calBestPreviewSize(Camera.Parameters camPara, final int width, final int height) {
        List<Camera.Size> allSupportedSize = camPara.getSupportedPreviewSizes();
        ArrayList<Camera.Size> widthLargerSize = new ArrayList<>();
        for (Camera.Size tmpSize : allSupportedSize) {
            Log.w("jacoco/org.jacoco.examples.test/src/test", "tmpSize.width===" + tmpSize.width + ", tmpSize.height===" + tmpSize.height);
            if (tmpSize.width > tmpSize.height) {
                widthLargerSize.add(tmpSize);
            }
        }
        Collections.sort(widthLargerSize, new Comparator<Camera.Size>() { // from class: com.nt.facerecognition.manager.CameraUtil.1
            @Override // java.util.Comparator
            public int compare(Camera.Size lhs, Camera.Size rhs) {
                int off_one = Math.abs((lhs.width * lhs.height) - (width * height));
                int off_two = Math.abs((rhs.width * rhs.height) - (width * height));
                return off_one - off_two;
            }
        });
        widthLargerSize.get(0).width = 640;
        widthLargerSize.get(0).height = 480;
        Log.w("NearSize", widthLargerSize.get(0).width + SettingsStringUtil.DELIMITER + widthLargerSize.get(0).height);
        return widthLargerSize.get(0);
    }

    public Camera getCamera() {
        return this.mCamera;
    }
}
