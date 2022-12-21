package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.statusbar.policy.FlashlightController;
import java.lang.ref.WeakReference;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import javax.inject.Inject;

@SysUISingleton
public class FlashlightControllerImpl implements FlashlightController {
    private static final String ACTION_FLASHLIGHT_CHANGED = "com.android.settings.flashlight.action.FLASHLIGHT_CHANGED";
    /* access modifiers changed from: private */
    public static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final int DISPATCH_AVAILABILITY_CHANGED = 2;
    private static final int DISPATCH_CHANGED = 1;
    private static final int DISPATCH_ERROR = 0;
    private static final String TAG = "FlashlightController";
    /* access modifiers changed from: private */
    public String mCameraId;
    private final CameraManager mCameraManager;
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public boolean mFlashlightEnabled;
    private Handler mHandler;
    private final ArrayList<WeakReference<FlashlightController.FlashlightListener>> mListeners = new ArrayList<>(1);
    /* access modifiers changed from: private */
    public boolean mTorchAvailable;
    private final CameraManager.TorchCallback mTorchCallback = new CameraManager.TorchCallback() {
        public void onTorchModeUnavailable(String str) {
            if (TextUtils.equals(str, FlashlightControllerImpl.this.mCameraId)) {
                setCameraAvailable(false);
                Settings.Secure.putInt(FlashlightControllerImpl.this.mContext.getContentResolver(), "flashlight_available", 0);
            }
        }

        public void onTorchModeChanged(String str, boolean z) {
            if (TextUtils.equals(str, FlashlightControllerImpl.this.mCameraId)) {
                setCameraAvailable(true);
                setTorchMode(z);
                Settings.Secure.putInt(FlashlightControllerImpl.this.mContext.getContentResolver(), "flashlight_available", 1);
                Settings.Secure.putInt(FlashlightControllerImpl.this.mContext.getContentResolver(), "flashlight_enabled", z ? 1 : 0);
                FlashlightControllerImpl.this.mContext.sendBroadcast(new Intent(FlashlightControllerImpl.ACTION_FLASHLIGHT_CHANGED));
            }
        }

        private void setCameraAvailable(boolean z) {
            boolean z2;
            synchronized (FlashlightControllerImpl.this) {
                z2 = FlashlightControllerImpl.this.mTorchAvailable != z;
                boolean unused = FlashlightControllerImpl.this.mTorchAvailable = z;
            }
            if (z2) {
                if (FlashlightControllerImpl.DEBUG) {
                    Log.d(FlashlightControllerImpl.TAG, "dispatchAvailabilityChanged(" + z + NavigationBarInflaterView.KEY_CODE_END);
                }
                FlashlightControllerImpl.this.dispatchAvailabilityChanged(z);
            }
        }

        private void setTorchMode(boolean z) {
            boolean z2;
            synchronized (FlashlightControllerImpl.this) {
                z2 = FlashlightControllerImpl.this.mFlashlightEnabled != z;
                boolean unused = FlashlightControllerImpl.this.mFlashlightEnabled = z;
            }
            if (z2) {
                if (FlashlightControllerImpl.DEBUG) {
                    Log.d(FlashlightControllerImpl.TAG, "dispatchModeChanged(" + z + NavigationBarInflaterView.KEY_CODE_END);
                }
                FlashlightControllerImpl.this.dispatchModeChanged(z);
            }
        }
    };

    @Inject
    public FlashlightControllerImpl(Context context, DumpManager dumpManager) {
        this.mContext = context;
        this.mCameraManager = (CameraManager) context.getSystemService("camera");
        dumpManager.registerDumpable(getClass().getSimpleName(), this);
        tryInitCamera();
    }

    private void tryInitCamera() {
        try {
            String cameraId = getCameraId();
            this.mCameraId = cameraId;
            if (cameraId != null) {
                ensureHandler();
                this.mCameraManager.registerTorchCallback(this.mTorchCallback, this.mHandler);
            }
        } catch (Throwable th) {
            Log.e(TAG, "Couldn't initialize.", th);
        }
    }

    public void setFlashlight(boolean z) {
        boolean z2;
        synchronized (this) {
            String str = this.mCameraId;
            if (str != null) {
                z2 = false;
                if (this.mFlashlightEnabled != z) {
                    this.mFlashlightEnabled = z;
                    try {
                        this.mCameraManager.setTorchMode(str, z);
                    } catch (CameraAccessException e) {
                        Log.e(TAG, "Couldn't set torch mode", e);
                        this.mFlashlightEnabled = false;
                        z2 = true;
                    }
                }
            } else {
                return;
            }
        }
        dispatchModeChanged(this.mFlashlightEnabled);
        if (z2) {
            dispatchError();
        }
    }

    public boolean hasFlashlight() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.camera.flash");
    }

    public synchronized boolean isEnabled() {
        return this.mFlashlightEnabled;
    }

    public synchronized boolean isAvailable() {
        return this.mTorchAvailable;
    }

    public void addCallback(FlashlightController.FlashlightListener flashlightListener) {
        synchronized (this.mListeners) {
            if (this.mCameraId == null) {
                tryInitCamera();
            }
            cleanUpListenersLocked(flashlightListener);
            this.mListeners.add(new WeakReference(flashlightListener));
            flashlightListener.onFlashlightAvailabilityChanged(this.mTorchAvailable);
            flashlightListener.onFlashlightChanged(this.mFlashlightEnabled);
        }
    }

    public void removeCallback(FlashlightController.FlashlightListener flashlightListener) {
        synchronized (this.mListeners) {
            cleanUpListenersLocked(flashlightListener);
        }
    }

    private synchronized void ensureHandler() {
        if (this.mHandler == null) {
            HandlerThread handlerThread = new HandlerThread(TAG, 10);
            handlerThread.start();
            this.mHandler = new Handler(handlerThread.getLooper());
        }
    }

    private String getCameraId() throws CameraAccessException {
        for (String str : this.mCameraManager.getCameraIdList()) {
            CameraCharacteristics cameraCharacteristics = this.mCameraManager.getCameraCharacteristics(str);
            Boolean bool = (Boolean) cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
            Integer num = (Integer) cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
            if (bool != null && bool.booleanValue() && num != null && num.intValue() == 1) {
                return str;
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void dispatchModeChanged(boolean z) {
        dispatchListeners(1, z);
    }

    private void dispatchError() {
        dispatchListeners(1, false);
    }

    /* access modifiers changed from: private */
    public void dispatchAvailabilityChanged(boolean z) {
        dispatchListeners(2, z);
    }

    private void dispatchListeners(int i, boolean z) {
        synchronized (this.mListeners) {
            int size = this.mListeners.size();
            boolean z2 = false;
            for (int i2 = 0; i2 < size; i2++) {
                FlashlightController.FlashlightListener flashlightListener = (FlashlightController.FlashlightListener) this.mListeners.get(i2).get();
                if (flashlightListener == null) {
                    z2 = true;
                } else if (i == 0) {
                    flashlightListener.onFlashlightError();
                } else if (i == 1) {
                    flashlightListener.onFlashlightChanged(z);
                } else if (i == 2) {
                    flashlightListener.onFlashlightAvailabilityChanged(z);
                }
            }
            if (z2) {
                cleanUpListenersLocked((FlashlightController.FlashlightListener) null);
            }
        }
    }

    private void cleanUpListenersLocked(FlashlightController.FlashlightListener flashlightListener) {
        for (int size = this.mListeners.size() - 1; size >= 0; size--) {
            FlashlightController.FlashlightListener flashlightListener2 = (FlashlightController.FlashlightListener) this.mListeners.get(size).get();
            if (flashlightListener2 == null || flashlightListener2 == flashlightListener) {
                this.mListeners.remove(size);
            }
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("FlashlightController state:");
        printWriter.print("  mCameraId=");
        printWriter.println(this.mCameraId);
        printWriter.print("  mFlashlightEnabled=");
        printWriter.println(this.mFlashlightEnabled);
        printWriter.print("  mTorchAvailable=");
        printWriter.println(this.mTorchAvailable);
    }
}
