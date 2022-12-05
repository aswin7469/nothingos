package android.hardware.camera2.impl;

import android.app.ActivityThread;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraExtensionCharacteristics;
import android.hardware.camera2.CameraOfflineSession;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.ICameraDeviceCallbacks;
import android.hardware.camera2.ICameraDeviceUser;
import android.hardware.camera2.ICameraOfflineSession;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.impl.CameraOfflineSessionImpl;
import android.hardware.camera2.params.ExtensionSessionConfiguration;
import android.hardware.camera2.params.InputConfiguration;
import android.hardware.camera2.params.MultiResolutionStreamConfigurationMap;
import android.hardware.camera2.params.MultiResolutionStreamInfo;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.params.SessionConfiguration;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.hardware.camera2.utils.SubmitInfo;
import android.hardware.camera2.utils.SurfaceUtils;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.ServiceSpecificException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.util.SparseArray;
import android.view.Surface;
import com.android.internal.util.function.pooled.PooledLambda;
import com.nothing.experience.AppTracking;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes.dex */
public class CameraDeviceImpl extends CameraDevice implements IBinder.DeathRecipient {
    private static final long NANO_PER_SECOND = 1000000000;
    private static final int REQUEST_ID_NONE = -1;
    private static final String THERMAL_TEMPERATURE_TAG = "quiet-therm-usr";
    private final String TAG;
    private final int mAppTargetSdkVersion;
    private final String mCameraId;
    private final CameraCharacteristics mCharacteristics;
    private final Context mContext;
    private CameraAdvancedExtensionSessionImpl mCurrentAdvancedExtensionSession;
    private CameraExtensionSessionImpl mCurrentExtensionSession;
    private CameraCaptureSessionCore mCurrentSession;
    private final CameraDevice.StateCallback mDeviceCallback;
    private final Executor mDeviceExecutor;
    private boolean mIsPrivilegedApp;
    private CameraOfflineSessionImpl mOfflineSessionImpl;
    private ExecutorService mOfflineSwitchService;
    private final Map<String, CameraCharacteristics> mPhysicalIdsToChars;
    private ICameraDeviceUserWrapper mRemoteDevice;
    private int[] mRepeatingRequestTypes;
    private volatile StateCallbackKK mSessionStateCallback;
    private final int mTotalPartialCount;
    private final boolean DEBUG = false;
    private int customOpMode = 0;
    final Object mInterfaceLock = new Object();
    private final CameraDeviceCallbacks mCallbacks = new CameraDeviceCallbacks();
    private final AtomicBoolean mClosing = new AtomicBoolean();
    private boolean mInError = false;
    private boolean mIdle = true;
    private SparseArray<CaptureCallbackHolder> mCaptureCallbackMap = new SparseArray<>();
    private HashMap<Integer, Integer> mBatchOutputMap = new HashMap<>();
    private int mRepeatingRequestId = -1;
    private AbstractMap.SimpleEntry<Integer, InputConfiguration> mConfiguredInput = new AbstractMap.SimpleEntry<>(-1, null);
    private final SparseArray<OutputConfiguration> mConfiguredOutputs = new SparseArray<>();
    private final HashSet<Integer> mOfflineSupport = new HashSet<>();
    private long thirdPartyApplicationEnterTemperature = 0;
    private long thirdPartyApplicationExistTemperature = 0;
    private String thirdPartyApplicationEnterTime = "";
    private String thirdPartyApplicationExitTime = "";
    private String currentUserPackageName = "";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final List<RequestLastFrameNumbersHolder> mRequestLastFrameNumbersList = new ArrayList();
    private FrameNumberTracker mFrameNumberTracker = new FrameNumberTracker();
    private int mNextSessionId = 0;
    private final Runnable mCallOnOpened = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.1
        @Override // java.lang.Runnable
        public void run() {
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                if (CameraDeviceImpl.this.mRemoteDevice == null) {
                    return;
                }
                StateCallbackKK sessionCallback = CameraDeviceImpl.this.mSessionStateCallback;
                if (sessionCallback != null) {
                    sessionCallback.onOpened(CameraDeviceImpl.this);
                }
                CameraDeviceImpl.this.mDeviceCallback.onOpened(CameraDeviceImpl.this);
            }
        }
    };
    private final Runnable mCallOnUnconfigured = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.2
        @Override // java.lang.Runnable
        public void run() {
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                if (CameraDeviceImpl.this.mRemoteDevice == null) {
                    return;
                }
                StateCallbackKK sessionCallback = CameraDeviceImpl.this.mSessionStateCallback;
                if (sessionCallback != null) {
                    sessionCallback.onUnconfigured(CameraDeviceImpl.this);
                }
            }
        }
    };
    private final Runnable mCallOnActive = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.3
        @Override // java.lang.Runnable
        public void run() {
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                if (CameraDeviceImpl.this.mRemoteDevice == null) {
                    return;
                }
                StateCallbackKK sessionCallback = CameraDeviceImpl.this.mSessionStateCallback;
                if (sessionCallback != null) {
                    sessionCallback.onActive(CameraDeviceImpl.this);
                }
            }
        }
    };
    private final Runnable mCallOnBusy = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.4
        @Override // java.lang.Runnable
        public void run() {
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                if (CameraDeviceImpl.this.mRemoteDevice == null) {
                    return;
                }
                StateCallbackKK sessionCallback = CameraDeviceImpl.this.mSessionStateCallback;
                if (sessionCallback != null) {
                    sessionCallback.onBusy(CameraDeviceImpl.this);
                }
            }
        }
    };
    private final Runnable mCallOnClosed = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.5
        private boolean mClosedOnce = false;

        @Override // java.lang.Runnable
        public void run() {
            StateCallbackKK sessionCallback;
            if (this.mClosedOnce) {
                throw new AssertionError("Don't post #onClosed more than once");
            }
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                sessionCallback = CameraDeviceImpl.this.mSessionStateCallback;
            }
            if (sessionCallback != null) {
                sessionCallback.onClosed(CameraDeviceImpl.this);
            }
            CameraDeviceImpl.this.mDeviceCallback.onClosed(CameraDeviceImpl.this);
            this.mClosedOnce = true;
        }
    };
    private final Runnable mCallOnIdle = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.6
        @Override // java.lang.Runnable
        public void run() {
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                if (CameraDeviceImpl.this.mRemoteDevice == null) {
                    return;
                }
                StateCallbackKK sessionCallback = CameraDeviceImpl.this.mSessionStateCallback;
                if (sessionCallback != null) {
                    sessionCallback.onIdle(CameraDeviceImpl.this);
                }
            }
        }
    };
    private final Runnable mCallOnDisconnected = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.7
        @Override // java.lang.Runnable
        public void run() {
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                if (CameraDeviceImpl.this.mRemoteDevice == null) {
                    return;
                }
                StateCallbackKK sessionCallback = CameraDeviceImpl.this.mSessionStateCallback;
                if (sessionCallback != null) {
                    sessionCallback.onDisconnected(CameraDeviceImpl.this);
                }
                CameraDeviceImpl.this.mDeviceCallback.onDisconnected(CameraDeviceImpl.this);
            }
        }
    };

    public CameraDeviceImpl(String cameraId, CameraDevice.StateCallback callback, Executor executor, CameraCharacteristics characteristics, Map<String, CameraCharacteristics> physicalIdsToChars, int appTargetSdkVersion, Context ctx) {
        this.mIsPrivilegedApp = false;
        if (cameraId == null || callback == null || executor == null || characteristics == null) {
            throw new IllegalArgumentException("Null argument given");
        }
        this.mCameraId = cameraId;
        this.mDeviceCallback = callback;
        this.mDeviceExecutor = executor;
        this.mCharacteristics = characteristics;
        this.mPhysicalIdsToChars = physicalIdsToChars;
        this.mAppTargetSdkVersion = appTargetSdkVersion;
        this.mContext = ctx;
        String tag = String.format("CameraDevice-JV-%s", cameraId);
        this.TAG = tag.length() > 23 ? tag.substring(0, 23) : tag;
        Integer partialCount = (Integer) characteristics.get(CameraCharacteristics.REQUEST_PARTIAL_RESULT_COUNT);
        if (partialCount == null) {
            this.mTotalPartialCount = 1;
        } else {
            this.mTotalPartialCount = partialCount.intValue();
        }
        this.mIsPrivilegedApp = checkPrivilegedAppList();
    }

    public CameraDeviceCallbacks getCallbacks() {
        return this.mCallbacks;
    }

    public void setRemoteDevice(ICameraDeviceUser remoteDevice) throws CameraAccessException {
        synchronized (this.mInterfaceLock) {
            if (this.mInError) {
                return;
            }
            this.mRemoteDevice = new ICameraDeviceUserWrapper(remoteDevice);
            IBinder remoteDeviceBinder = remoteDevice.asBinder();
            if (remoteDeviceBinder != null) {
                try {
                    remoteDeviceBinder.linkToDeath(this, 0);
                } catch (RemoteException e) {
                    this.mDeviceExecutor.execute(this.mCallOnDisconnected);
                    throw new CameraAccessException(2, "The camera device has encountered a serious error");
                }
            }
            this.mDeviceExecutor.execute(this.mCallOnOpened);
            this.mDeviceExecutor.execute(this.mCallOnUnconfigured);
        }
    }

    public void setRemoteFailure(ServiceSpecificException failure) {
        int failureCode = 4;
        boolean failureIsError = true;
        switch (failure.errorCode) {
            case 4:
                failureIsError = false;
                break;
            case 5:
            case 9:
            default:
                String str = this.TAG;
                Log.e(str, "Unexpected failure in opening camera device: " + failure.errorCode + failure.getMessage());
                break;
            case 6:
                failureCode = 3;
                break;
            case 7:
                failureCode = 1;
                break;
            case 8:
                failureCode = 2;
                break;
            case 10:
                failureCode = 4;
                break;
        }
        final int code = failureCode;
        final boolean isError = failureIsError;
        synchronized (this.mInterfaceLock) {
            this.mInError = true;
            this.mDeviceExecutor.execute(new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.8
                @Override // java.lang.Runnable
                public void run() {
                    if (isError) {
                        CameraDeviceImpl.this.mDeviceCallback.onError(CameraDeviceImpl.this, code);
                    } else {
                        CameraDeviceImpl.this.mDeviceCallback.onDisconnected(CameraDeviceImpl.this);
                    }
                }
            });
        }
    }

    @Override // android.hardware.camera2.CameraDevice
    public void setVendorStreamConfigMode(int fpsrange) {
        this.customOpMode = fpsrange;
    }

    @Override // android.hardware.camera2.CameraDevice
    public String getId() {
        return this.mCameraId;
    }

    public void configureOutputs(List<Surface> outputs) throws CameraAccessException {
        ArrayList<OutputConfiguration> outputConfigs = new ArrayList<>(outputs.size());
        for (Surface s : outputs) {
            outputConfigs.add(new OutputConfiguration(s));
        }
        configureStreamsChecked(null, outputConfigs, 0, null, SystemClock.uptimeMillis());
    }

    public boolean configureStreamsChecked(InputConfiguration inputConfig, List<OutputConfiguration> outputs, int operatingMode, CaptureRequest sessionParams, long createSessionStartTime) throws CameraAccessException {
        List<OutputConfiguration> outputs2 = outputs == null ? new ArrayList<>() : outputs;
        if (outputs2.size() != 0 || inputConfig == null) {
            checkInputConfiguration(inputConfig);
            synchronized (this.mInterfaceLock) {
                try {
                    try {
                        checkIfCameraClosedOrInError();
                        HashSet<OutputConfiguration> addSet = new HashSet<>(outputs2);
                        List<Integer> deleteList = new ArrayList<>();
                        for (int i = 0; i < this.mConfiguredOutputs.size(); i++) {
                            int streamId = this.mConfiguredOutputs.keyAt(i);
                            OutputConfiguration outConfig = this.mConfiguredOutputs.valueAt(i);
                            if (outputs2.contains(outConfig) && !outConfig.isDeferredConfiguration()) {
                                addSet.remove(outConfig);
                            }
                            deleteList.add(Integer.valueOf(streamId));
                        }
                        try {
                            this.mDeviceExecutor.execute(this.mCallOnBusy);
                            stopRepeating();
                        } catch (Throwable th) {
                            e = th;
                        }
                        try {
                            waitUntilIdle();
                            this.mRemoteDevice.beginConfigure();
                            InputConfiguration currentInputConfig = this.mConfiguredInput.getValue();
                            if (inputConfig != currentInputConfig && (inputConfig == null || !inputConfig.equals(currentInputConfig))) {
                                if (currentInputConfig != null) {
                                    this.mRemoteDevice.deleteStream(this.mConfiguredInput.getKey().intValue());
                                    this.mConfiguredInput = new AbstractMap.SimpleEntry<>(-1, null);
                                }
                                if (inputConfig != null) {
                                    this.mConfiguredInput = new AbstractMap.SimpleEntry<>(Integer.valueOf(this.mRemoteDevice.createInputStream(inputConfig.getWidth(), inputConfig.getHeight(), inputConfig.getFormat(), inputConfig.isMultiResolution())), inputConfig);
                                }
                            }
                            for (Integer streamId2 : deleteList) {
                                this.mRemoteDevice.deleteStream(streamId2.intValue());
                                this.mConfiguredOutputs.delete(streamId2.intValue());
                            }
                            for (OutputConfiguration outConfig2 : outputs2) {
                                if (addSet.contains(outConfig2)) {
                                    this.mConfiguredOutputs.put(this.mRemoteDevice.createStream(outConfig2), outConfig2);
                                }
                            }
                            int operatingMode2 = operatingMode | (this.customOpMode << 16);
                            try {
                                int[] offlineStreamIds = sessionParams != null ? this.mRemoteDevice.endConfigure(operatingMode2, sessionParams.getNativeCopy(), createSessionStartTime) : this.mRemoteDevice.endConfigure(operatingMode2, null, createSessionStartTime);
                                this.mOfflineSupport.clear();
                                if (offlineStreamIds != null && offlineStreamIds.length > 0) {
                                    int length = offlineStreamIds.length;
                                    int i2 = 0;
                                    while (i2 < length) {
                                        int offlineStreamId = offlineStreamIds[i2];
                                        InputConfiguration currentInputConfig2 = currentInputConfig;
                                        this.mOfflineSupport.add(Integer.valueOf(offlineStreamId));
                                        i2++;
                                        currentInputConfig = currentInputConfig2;
                                    }
                                }
                                if (1 == 0 || outputs2.size() <= 0) {
                                    this.mDeviceExecutor.execute(this.mCallOnUnconfigured);
                                } else {
                                    this.mDeviceExecutor.execute(this.mCallOnIdle);
                                }
                                return true;
                            } catch (CameraAccessException e) {
                                e = e;
                                if (e.getReason() != 4) {
                                    throw e;
                                }
                                throw new IllegalStateException("The camera is currently busy. You must wait until the previous operation completes.", e);
                            } catch (IllegalArgumentException e2) {
                                e = e2;
                                String str = this.TAG;
                                Log.w(str, "Stream configuration failed due to: " + e.getMessage());
                                if (0 == 0 || outputs2.size() <= 0) {
                                    this.mDeviceExecutor.execute(this.mCallOnUnconfigured);
                                } else {
                                    this.mDeviceExecutor.execute(this.mCallOnIdle);
                                }
                                return false;
                            }
                        } catch (CameraAccessException e3) {
                            e = e3;
                        } catch (IllegalArgumentException e4) {
                            e = e4;
                        } catch (Throwable th2) {
                            e = th2;
                            if (0 == 0 || outputs2.size() <= 0) {
                                this.mDeviceExecutor.execute(this.mCallOnUnconfigured);
                            } else {
                                this.mDeviceExecutor.execute(this.mCallOnIdle);
                            }
                            throw e;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    throw th;
                }
            }
        } else {
            throw new IllegalArgumentException("cannot configure an input stream without any output streams");
        }
    }

    @Override // android.hardware.camera2.CameraDevice
    public void createCaptureSession(List<Surface> outputs, CameraCaptureSession.StateCallback callback, Handler handler) throws CameraAccessException {
        List<OutputConfiguration> outConfigurations = new ArrayList<>(outputs.size());
        for (Surface surface : outputs) {
            outConfigurations.add(new OutputConfiguration(surface));
        }
        createCaptureSessionInternal(null, outConfigurations, callback, checkAndWrapHandler(handler), 0, null);
    }

    @Override // android.hardware.camera2.CameraDevice
    public void createCaptureSessionByOutputConfigurations(List<OutputConfiguration> outputConfigurations, CameraCaptureSession.StateCallback callback, Handler handler) throws CameraAccessException {
        List<OutputConfiguration> currentOutputs = new ArrayList<>(outputConfigurations);
        createCaptureSessionInternal(null, currentOutputs, callback, checkAndWrapHandler(handler), 0, null);
    }

    @Override // android.hardware.camera2.CameraDevice
    public void createReprocessableCaptureSession(InputConfiguration inputConfig, List<Surface> outputs, CameraCaptureSession.StateCallback callback, Handler handler) throws CameraAccessException {
        if (inputConfig == null) {
            throw new IllegalArgumentException("inputConfig cannot be null when creating a reprocessable capture session");
        }
        List<OutputConfiguration> outConfigurations = new ArrayList<>(outputs.size());
        for (Surface surface : outputs) {
            outConfigurations.add(new OutputConfiguration(surface));
        }
        createCaptureSessionInternal(inputConfig, outConfigurations, callback, checkAndWrapHandler(handler), 0, null);
    }

    @Override // android.hardware.camera2.CameraDevice
    public void createReprocessableCaptureSessionByConfigurations(InputConfiguration inputConfig, List<OutputConfiguration> outputs, CameraCaptureSession.StateCallback callback, Handler handler) throws CameraAccessException {
        if (inputConfig == null) {
            throw new IllegalArgumentException("inputConfig cannot be null when creating a reprocessable capture session");
        }
        if (outputs == null) {
            throw new IllegalArgumentException("Output configurations cannot be null when creating a reprocessable capture session");
        }
        List<OutputConfiguration> currentOutputs = new ArrayList<>();
        for (OutputConfiguration output : outputs) {
            currentOutputs.add(new OutputConfiguration(output));
        }
        createCaptureSessionInternal(inputConfig, currentOutputs, callback, checkAndWrapHandler(handler), 0, null);
    }

    @Override // android.hardware.camera2.CameraDevice
    public void createConstrainedHighSpeedCaptureSession(List<Surface> outputs, CameraCaptureSession.StateCallback callback, Handler handler) throws CameraAccessException {
        if (outputs == null || outputs.size() == 0 || outputs.size() > 2) {
            throw new IllegalArgumentException("Output surface list must not be null and the size must be no more than 2");
        }
        List<OutputConfiguration> outConfigurations = new ArrayList<>(outputs.size());
        for (Surface surface : outputs) {
            outConfigurations.add(new OutputConfiguration(surface));
        }
        createCaptureSessionInternal(null, outConfigurations, callback, checkAndWrapHandler(handler), 1, null);
    }

    @Override // android.hardware.camera2.CameraDevice
    public void createCustomCaptureSession(InputConfiguration inputConfig, List<OutputConfiguration> outputs, int operatingMode, CameraCaptureSession.StateCallback callback, Handler handler) throws CameraAccessException {
        List<OutputConfiguration> currentOutputs = new ArrayList<>();
        for (OutputConfiguration output : outputs) {
            currentOutputs.add(new OutputConfiguration(output));
        }
        createCaptureSessionInternal(inputConfig, currentOutputs, callback, checkAndWrapHandler(handler), operatingMode, null);
    }

    @Override // android.hardware.camera2.CameraDevice
    public void createCaptureSession(SessionConfiguration config) throws CameraAccessException {
        if (config == null) {
            throw new IllegalArgumentException("Invalid session configuration");
        }
        List<OutputConfiguration> outputConfigs = config.getOutputConfigurations();
        if (outputConfigs == null) {
            throw new IllegalArgumentException("Invalid output configurations");
        }
        if (config.getExecutor() == null) {
            throw new IllegalArgumentException("Invalid executor");
        }
        createCaptureSessionInternal(config.getInputConfiguration(), outputConfigs, config.getStateCallback(), config.getExecutor(), config.getSessionType(), config.getSessionParameters());
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x0071 A[Catch: all -> 0x0110, TRY_ENTER, TryCatch #1 {all -> 0x0110, blocks: (B:11:0x001b, B:12:0x0022, B:57:0x010e, B:14:0x0023, B:16:0x0027, B:17:0x002a, B:19:0x002f, B:20:0x0034, B:22:0x0038, B:25:0x004e, B:30:0x0057, B:38:0x0071, B:39:0x007f, B:41:0x0085, B:43:0x0094, B:44:0x00de, B:46:0x00e2, B:47:0x0107, B:49:0x010a, B:50:0x00c6), top: B:3:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00e2 A[Catch: all -> 0x0110, TryCatch #1 {all -> 0x0110, blocks: (B:11:0x001b, B:12:0x0022, B:57:0x010e, B:14:0x0023, B:16:0x0027, B:17:0x002a, B:19:0x002f, B:20:0x0034, B:22:0x0038, B:25:0x004e, B:30:0x0057, B:38:0x0071, B:39:0x007f, B:41:0x0085, B:43:0x0094, B:44:0x00de, B:46:0x00e2, B:47:0x0107, B:49:0x010a, B:50:0x00c6), top: B:3:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x010a A[Catch: all -> 0x0110, TryCatch #1 {all -> 0x0110, blocks: (B:11:0x001b, B:12:0x0022, B:57:0x010e, B:14:0x0023, B:16:0x0027, B:17:0x002a, B:19:0x002f, B:20:0x0034, B:22:0x0038, B:25:0x004e, B:30:0x0057, B:38:0x0071, B:39:0x007f, B:41:0x0085, B:43:0x0094, B:44:0x00de, B:46:0x00e2, B:47:0x0107, B:49:0x010a, B:50:0x00c6), top: B:3:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00c6 A[Catch: all -> 0x0110, TryCatch #1 {all -> 0x0110, blocks: (B:11:0x001b, B:12:0x0022, B:57:0x010e, B:14:0x0023, B:16:0x0027, B:17:0x002a, B:19:0x002f, B:20:0x0034, B:22:0x0038, B:25:0x004e, B:30:0x0057, B:38:0x0071, B:39:0x007f, B:41:0x0085, B:43:0x0094, B:44:0x00de, B:46:0x00e2, B:47:0x0107, B:49:0x010a, B:50:0x00c6), top: B:3:0x0009 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void createCaptureSessionInternal(InputConfiguration inputConfig, List<OutputConfiguration> outputConfigurations, CameraCaptureSession.StateCallback callback, Executor executor, int operatingMode, CaptureRequest sessionParams) throws CameraAccessException {
        boolean configureSuccess;
        CameraCaptureSessionCore newSession;
        long createSessionStartTime = SystemClock.uptimeMillis();
        synchronized (this.mInterfaceLock) {
            try {
                try {
                    checkIfCameraClosedOrInError();
                    boolean isConstrainedHighSpeed = operatingMode == 1;
                    if (isConstrainedHighSpeed && inputConfig != null) {
                        throw new IllegalArgumentException("Constrained high speed session doesn't support input configuration yet.");
                    }
                    CameraCaptureSessionCore cameraCaptureSessionCore = this.mCurrentSession;
                    if (cameraCaptureSessionCore != null) {
                        cameraCaptureSessionCore.replaceSessionClose();
                    }
                    CameraExtensionSessionImpl cameraExtensionSessionImpl = this.mCurrentExtensionSession;
                    if (cameraExtensionSessionImpl != null) {
                        cameraExtensionSessionImpl.release(false);
                        this.mCurrentExtensionSession = null;
                    }
                    CameraAdvancedExtensionSessionImpl cameraAdvancedExtensionSessionImpl = this.mCurrentAdvancedExtensionSession;
                    if (cameraAdvancedExtensionSessionImpl != null) {
                        cameraAdvancedExtensionSessionImpl.release(false);
                        this.mCurrentAdvancedExtensionSession = null;
                    }
                    CameraAccessException pendingException = null;
                    Surface input = null;
                    try {
                        boolean configureSuccess2 = configureStreamsChecked(inputConfig, outputConfigurations, operatingMode, sessionParams, createSessionStartTime);
                        if (configureSuccess2 && inputConfig != null) {
                            try {
                                input = this.mRemoteDevice.getInputSurface();
                            } catch (CameraAccessException e) {
                                e = e;
                                pendingException = e;
                                input = null;
                                configureSuccess = false;
                                if (isConstrainedHighSpeed) {
                                }
                                this.mCurrentSession = newSession;
                                if (pendingException == null) {
                                }
                            }
                        }
                        configureSuccess = configureSuccess2;
                    } catch (CameraAccessException e2) {
                        e = e2;
                    }
                    if (isConstrainedHighSpeed) {
                        int i = this.mNextSessionId;
                        this.mNextSessionId = i + 1;
                        newSession = new CameraCaptureSessionImpl(i, input, callback, executor, this, this.mDeviceExecutor, configureSuccess);
                    } else {
                        ArrayList<Surface> surfaces = new ArrayList<>(outputConfigurations.size());
                        for (OutputConfiguration outConfig : outputConfigurations) {
                            surfaces.add(outConfig.getSurface());
                        }
                        StreamConfigurationMap config = (StreamConfigurationMap) getCharacteristics().get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                        SurfaceUtils.checkConstrainedHighSpeedSurfaces(surfaces, null, config);
                        int i2 = this.mNextSessionId;
                        this.mNextSessionId = i2 + 1;
                        newSession = new CameraConstrainedHighSpeedCaptureSessionImpl(i2, callback, executor, this, this.mDeviceExecutor, configureSuccess, this.mCharacteristics);
                    }
                    this.mCurrentSession = newSession;
                    if (pendingException == null) {
                        throw pendingException;
                    }
                    this.mSessionStateCallback = newSession.getDeviceStateCallback();
                    this.currentUserPackageName = getCallerProcessName();
                    this.thirdPartyApplicationEnterTemperature = getThermalInfo(THERMAL_TEMPERATURE_TAG);
                    long currentTime = System.currentTimeMillis();
                    this.thirdPartyApplicationEnterTime = this.dateFormatter.format(Long.valueOf(currentTime));
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    @Override // android.hardware.camera2.CameraDevice
    public boolean isSessionConfigurationSupported(SessionConfiguration sessionConfig) throws CameraAccessException, UnsupportedOperationException, IllegalArgumentException {
        boolean isSessionConfigurationSupported;
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            isSessionConfigurationSupported = this.mRemoteDevice.isSessionConfigurationSupported(sessionConfig);
        }
        return isSessionConfigurationSupported;
    }

    public void setSessionListener(StateCallbackKK sessionCallback) {
        synchronized (this.mInterfaceLock) {
            this.mSessionStateCallback = sessionCallback;
        }
    }

    private void overrideEnableZsl(CameraMetadataNative request, boolean newValue) {
        Boolean enableZsl = (Boolean) request.get(CaptureRequest.CONTROL_ENABLE_ZSL);
        if (enableZsl == null) {
            return;
        }
        request.set((CaptureRequest.Key<CaptureRequest.Key<Boolean>>) CaptureRequest.CONTROL_ENABLE_ZSL, (CaptureRequest.Key<Boolean>) Boolean.valueOf(newValue));
    }

    @Override // android.hardware.camera2.CameraDevice
    public CaptureRequest.Builder createCaptureRequest(int templateType, Set<String> physicalCameraIdSet) throws CameraAccessException {
        CaptureRequest.Builder builder;
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            for (String physicalId : physicalCameraIdSet) {
                if (physicalId == getId()) {
                    throw new IllegalStateException("Physical id matches the logical id!");
                }
            }
            CameraMetadataNative templatedRequest = this.mRemoteDevice.createDefaultRequest(templateType);
            if (this.mAppTargetSdkVersion < 26 || templateType != 2) {
                overrideEnableZsl(templatedRequest, false);
            }
            builder = new CaptureRequest.Builder(templatedRequest, false, -1, getId(), physicalCameraIdSet);
        }
        return builder;
    }

    @Override // android.hardware.camera2.CameraDevice
    public CaptureRequest.Builder createCaptureRequest(int templateType) throws CameraAccessException {
        CaptureRequest.Builder builder;
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            CameraMetadataNative templatedRequest = this.mRemoteDevice.createDefaultRequest(templateType);
            if (this.mAppTargetSdkVersion < 26 || templateType != 2) {
                overrideEnableZsl(templatedRequest, false);
            }
            builder = new CaptureRequest.Builder(templatedRequest, false, -1, getId(), null);
        }
        return builder;
    }

    @Override // android.hardware.camera2.CameraDevice
    public CaptureRequest.Builder createReprocessCaptureRequest(TotalCaptureResult inputResult) throws CameraAccessException {
        CaptureRequest.Builder builder;
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            CameraMetadataNative resultMetadata = new CameraMetadataNative(inputResult.getNativeCopy());
            builder = new CaptureRequest.Builder(resultMetadata, true, inputResult.getSessionId(), getId(), null);
        }
        return builder;
    }

    public void prepare(Surface surface) throws CameraAccessException {
        if (surface == null) {
            throw new IllegalArgumentException("Surface is null");
        }
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            int streamId = -1;
            int i = 0;
            while (true) {
                if (i >= this.mConfiguredOutputs.size()) {
                    break;
                }
                List<Surface> surfaces = this.mConfiguredOutputs.valueAt(i).getSurfaces();
                if (!surfaces.contains(surface)) {
                    i++;
                } else {
                    streamId = this.mConfiguredOutputs.keyAt(i);
                    break;
                }
            }
            if (streamId == -1) {
                throw new IllegalArgumentException("Surface is not part of this session");
            }
            this.mRemoteDevice.prepare(streamId);
        }
    }

    public void prepare(int maxCount, Surface surface) throws CameraAccessException {
        if (surface == null) {
            throw new IllegalArgumentException("Surface is null");
        }
        if (maxCount <= 0) {
            throw new IllegalArgumentException("Invalid maxCount given: " + maxCount);
        }
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            int streamId = -1;
            int i = 0;
            while (true) {
                if (i >= this.mConfiguredOutputs.size()) {
                    break;
                } else if (surface != this.mConfiguredOutputs.valueAt(i).getSurface()) {
                    i++;
                } else {
                    streamId = this.mConfiguredOutputs.keyAt(i);
                    break;
                }
            }
            if (streamId == -1) {
                throw new IllegalArgumentException("Surface is not part of this session");
            }
            this.mRemoteDevice.prepare2(maxCount, streamId);
        }
    }

    public void updateOutputConfiguration(OutputConfiguration config) throws CameraAccessException {
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            int streamId = -1;
            int i = 0;
            while (true) {
                if (i >= this.mConfiguredOutputs.size()) {
                    break;
                } else if (config.getSurface() != this.mConfiguredOutputs.valueAt(i).getSurface()) {
                    i++;
                } else {
                    streamId = this.mConfiguredOutputs.keyAt(i);
                    break;
                }
            }
            if (streamId == -1) {
                throw new IllegalArgumentException("Invalid output configuration");
            }
            this.mRemoteDevice.updateOutputConfiguration(streamId, config);
            this.mConfiguredOutputs.put(streamId, config);
        }
    }

    public CameraOfflineSession switchToOffline(Collection<Surface> offlineOutputs, Executor executor, CameraOfflineSession.CameraOfflineSessionCallback listener) throws CameraAccessException {
        CameraOfflineSessionImpl cameraOfflineSessionImpl;
        if (offlineOutputs.isEmpty()) {
            throw new IllegalArgumentException("Invalid offline surfaces!");
        }
        final HashSet<Integer> offlineStreamIds = new HashSet<>();
        SparseArray<OutputConfiguration> offlineConfiguredOutputs = new SparseArray<>();
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            if (this.mOfflineSessionImpl != null) {
                throw new IllegalStateException("Switch to offline mode already in progress");
            }
            for (Surface surface : offlineOutputs) {
                int streamId = -1;
                int i = 0;
                while (true) {
                    if (i < this.mConfiguredOutputs.size()) {
                        if (surface != this.mConfiguredOutputs.valueAt(i).getSurface()) {
                            i++;
                        } else {
                            streamId = this.mConfiguredOutputs.keyAt(i);
                            offlineConfiguredOutputs.append(streamId, this.mConfiguredOutputs.valueAt(i));
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (streamId == -1) {
                    throw new IllegalArgumentException("Offline surface is not part of this session");
                }
                if (!this.mOfflineSupport.contains(Integer.valueOf(streamId))) {
                    throw new IllegalArgumentException("Surface: " + surface + " does not  support offline mode");
                }
                offlineStreamIds.add(Integer.valueOf(streamId));
            }
            stopRepeating();
            cameraOfflineSessionImpl = new CameraOfflineSessionImpl(this.mCameraId, this.mCharacteristics, executor, listener, offlineConfiguredOutputs, this.mConfiguredInput, this.mConfiguredOutputs, this.mFrameNumberTracker, this.mCaptureCallbackMap, this.mRequestLastFrameNumbersList);
            this.mOfflineSessionImpl = cameraOfflineSessionImpl;
            this.mOfflineSwitchService = Executors.newSingleThreadExecutor();
            this.mConfiguredOutputs.clear();
            this.mConfiguredInput = new AbstractMap.SimpleEntry<>(-1, null);
            this.mIdle = true;
            this.mCaptureCallbackMap = new SparseArray<>();
            this.mBatchOutputMap = new HashMap<>();
            this.mFrameNumberTracker = new FrameNumberTracker();
            this.mCurrentSession.closeWithoutDraining();
            this.mCurrentSession = null;
        }
        this.mOfflineSwitchService.execute(new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.9
            @Override // java.lang.Runnable
            public void run() {
                try {
                    try {
                        ICameraDeviceUserWrapper iCameraDeviceUserWrapper = CameraDeviceImpl.this.mRemoteDevice;
                        CameraOfflineSessionImpl.CameraDeviceCallbacks callbacks = CameraDeviceImpl.this.mOfflineSessionImpl.getCallbacks();
                        HashSet hashSet = offlineStreamIds;
                        ICameraOfflineSession remoteOfflineSession = iCameraDeviceUserWrapper.switchToOffline(callbacks, Arrays.stream((Integer[]) hashSet.toArray(new Integer[hashSet.size()])).mapToInt(CameraDeviceImpl$9$$ExternalSyntheticLambda0.INSTANCE).toArray());
                        CameraDeviceImpl.this.mOfflineSessionImpl.setRemoteSession(remoteOfflineSession);
                    } catch (CameraAccessException e) {
                        CameraDeviceImpl.this.mOfflineSessionImpl.notifyFailedSwitch();
                    }
                } finally {
                    CameraDeviceImpl.this.mOfflineSessionImpl = null;
                }
            }
        });
        return cameraOfflineSessionImpl;
    }

    public boolean supportsOfflineProcessing(Surface surface) {
        boolean contains;
        if (surface == null) {
            throw new IllegalArgumentException("Surface is null");
        }
        synchronized (this.mInterfaceLock) {
            int streamId = -1;
            int i = 0;
            while (true) {
                if (i >= this.mConfiguredOutputs.size()) {
                    break;
                } else if (surface != this.mConfiguredOutputs.valueAt(i).getSurface()) {
                    i++;
                } else {
                    streamId = this.mConfiguredOutputs.keyAt(i);
                    break;
                }
            }
            if (streamId == -1) {
                throw new IllegalArgumentException("Surface is not part of this session");
            }
            contains = this.mOfflineSupport.contains(Integer.valueOf(streamId));
        }
        return contains;
    }

    public void tearDown(Surface surface) throws CameraAccessException {
        if (surface == null) {
            throw new IllegalArgumentException("Surface is null");
        }
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            int streamId = -1;
            int i = 0;
            while (true) {
                if (i >= this.mConfiguredOutputs.size()) {
                    break;
                } else if (surface != this.mConfiguredOutputs.valueAt(i).getSurface()) {
                    i++;
                } else {
                    streamId = this.mConfiguredOutputs.keyAt(i);
                    break;
                }
            }
            if (streamId == -1) {
                throw new IllegalArgumentException("Surface is not part of this session");
            }
            this.mRemoteDevice.tearDown(streamId);
        }
    }

    public void finalizeOutputConfigs(List<OutputConfiguration> outputConfigs) throws CameraAccessException {
        if (outputConfigs == null || outputConfigs.size() == 0) {
            throw new IllegalArgumentException("deferred config is null or empty");
        }
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            for (OutputConfiguration config : outputConfigs) {
                int streamId = -1;
                int i = 0;
                while (true) {
                    if (i < this.mConfiguredOutputs.size()) {
                        if (!config.equals(this.mConfiguredOutputs.valueAt(i))) {
                            i++;
                        } else {
                            streamId = this.mConfiguredOutputs.keyAt(i);
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (streamId == -1) {
                    throw new IllegalArgumentException("Deferred config is not part of this session");
                }
                if (config.getSurfaces().size() == 0) {
                    throw new IllegalArgumentException("The final config for stream " + streamId + " must have at least 1 surface");
                }
                this.mRemoteDevice.finalizeOutputConfigurations(streamId, config);
                this.mConfiguredOutputs.put(streamId, config);
            }
        }
    }

    public int capture(CaptureRequest request, CaptureCallback callback, Executor executor) throws CameraAccessException {
        List<CaptureRequest> requestList = new ArrayList<>();
        requestList.add(request);
        return submitCaptureRequest(requestList, callback, executor, false);
    }

    public int captureBurst(List<CaptureRequest> requests, CaptureCallback callback, Executor executor) throws CameraAccessException {
        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException("At least one request must be given");
        }
        return submitCaptureRequest(requests, callback, executor, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkEarlyTriggerSequenceCompleteLocked(final int requestId, long lastFrameNumber, int[] repeatingRequestTypes) {
        if (lastFrameNumber == -1) {
            int index = this.mCaptureCallbackMap.indexOfKey(requestId);
            final CaptureCallbackHolder holder = index >= 0 ? this.mCaptureCallbackMap.valueAt(index) : null;
            if (holder != null) {
                this.mCaptureCallbackMap.removeAt(index);
            }
            if (holder != null) {
                Runnable resultDispatch = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.10
                    @Override // java.lang.Runnable
                    public void run() {
                        if (!CameraDeviceImpl.this.isClosed()) {
                            holder.getCallback().onCaptureSequenceAborted(CameraDeviceImpl.this, requestId);
                        }
                    }
                };
                long ident = Binder.clearCallingIdentity();
                try {
                    holder.getExecutor().execute(resultDispatch);
                    return;
                } finally {
                    Binder.restoreCallingIdentity(ident);
                }
            }
            Log.w(this.TAG, String.format("did not register callback to request %d", Integer.valueOf(requestId)));
            return;
        }
        this.mRequestLastFrameNumbersList.add(new RequestLastFrameNumbersHolder(requestId, lastFrameNumber, repeatingRequestTypes));
        checkAndFireSequenceComplete();
    }

    private int[] getRequestTypes(CaptureRequest[] requestArray) {
        int[] requestTypes = new int[requestArray.length];
        for (int i = 0; i < requestArray.length; i++) {
            requestTypes[i] = requestArray[i].getRequestType();
        }
        return requestTypes;
    }

    private boolean hasBatchedOutputs(List<CaptureRequest> requestList) {
        for (int i = 0; i < requestList.size(); i++) {
            CaptureRequest request = requestList.get(i);
            if (!request.isPartOfCRequestList()) {
                return false;
            }
            if (i == 0) {
                Collection<Surface> targets = request.getTargets();
                if (targets.size() != 2) {
                    return false;
                }
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTracker(int requestId, long frameNumber, int requestType, CaptureResult result, boolean isPartialResult) {
        if (this.mBatchOutputMap.containsKey(Integer.valueOf(requestId))) {
            int requestCount = this.mBatchOutputMap.get(Integer.valueOf(requestId)).intValue();
            for (int i = 0; i < requestCount; i++) {
                this.mFrameNumberTracker.updateTracker(frameNumber - ((requestCount - 1) - i), result, isPartialResult, requestType);
            }
            return;
        }
        this.mFrameNumberTracker.updateTracker(frameNumber, result, isPartialResult, requestType);
    }

    private int submitCaptureRequest(List<CaptureRequest> requestList, CaptureCallback callback, Executor executor, boolean repeating) throws CameraAccessException {
        Executor executor2 = checkExecutor(executor, callback);
        synchronized (this.mInterfaceLock) {
            try {
                try {
                    checkIfCameraClosedOrInError();
                    for (CaptureRequest request : requestList) {
                        try {
                            if (request.getTargets().isEmpty()) {
                                throw new IllegalArgumentException("Each request must have at least one Surface target");
                            }
                            for (Surface surface : request.getTargets()) {
                                if (surface == null) {
                                    throw new IllegalArgumentException("Null Surface targets are not allowed");
                                }
                            }
                        } catch (Throwable th) {
                            th = th;
                            throw th;
                        }
                    }
                    if (repeating) {
                        stopRepeating();
                    }
                    CaptureRequest[] requestArray = (CaptureRequest[]) requestList.toArray(new CaptureRequest[requestList.size()]);
                    for (CaptureRequest request2 : requestArray) {
                        request2.convertSurfaceToStreamId(this.mConfiguredOutputs);
                    }
                    SubmitInfo requestInfo = this.mRemoteDevice.submitRequestList(requestArray, repeating);
                    for (CaptureRequest request3 : requestArray) {
                        request3.recoverStreamIdToSurface();
                    }
                    boolean hasBatchedOutputs = hasBatchedOutputs(requestList);
                    if (hasBatchedOutputs) {
                        int requestCount = requestList.size();
                        this.mBatchOutputMap.put(Integer.valueOf(requestInfo.getRequestId()), Integer.valueOf(requestCount));
                    }
                    if (callback != null) {
                        this.mCaptureCallbackMap.put(requestInfo.getRequestId(), new CaptureCallbackHolder(callback, requestList, executor2, repeating, this.mNextSessionId - 1));
                    }
                    if (repeating) {
                        int i = this.mRepeatingRequestId;
                        if (i != -1) {
                            checkEarlyTriggerSequenceCompleteLocked(i, requestInfo.getLastFrameNumber(), this.mRepeatingRequestTypes);
                        }
                        this.mRepeatingRequestId = requestInfo.getRequestId();
                        this.mRepeatingRequestTypes = getRequestTypes(requestArray);
                    } else {
                        this.mRequestLastFrameNumbersList.add(new RequestLastFrameNumbersHolder(requestList, requestInfo));
                    }
                    if (this.mIdle) {
                        this.mDeviceExecutor.execute(this.mCallOnActive);
                    }
                    this.mIdle = false;
                    return requestInfo.getRequestId();
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        }
    }

    public int setRepeatingRequest(CaptureRequest request, CaptureCallback callback, Executor executor) throws CameraAccessException {
        List<CaptureRequest> requestList = new ArrayList<>();
        requestList.add(request);
        return submitCaptureRequest(requestList, callback, executor, true);
    }

    public int setRepeatingBurst(List<CaptureRequest> requests, CaptureCallback callback, Executor executor) throws CameraAccessException {
        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException("At least one request must be given");
        }
        return submitCaptureRequest(requests, callback, executor, true);
    }

    public void stopRepeating() throws CameraAccessException {
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            int requestId = this.mRepeatingRequestId;
            if (requestId != -1) {
                this.mRepeatingRequestId = -1;
                int[] requestTypes = this.mRepeatingRequestTypes;
                this.mRepeatingRequestTypes = null;
                try {
                    long lastFrameNumber = this.mRemoteDevice.cancelRequest(requestId);
                    checkEarlyTriggerSequenceCompleteLocked(requestId, lastFrameNumber, requestTypes);
                } catch (IllegalArgumentException e) {
                }
            }
        }
    }

    private void waitUntilIdle() throws CameraAccessException {
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            if (this.mRepeatingRequestId != -1) {
                throw new IllegalStateException("Active repeating request ongoing");
            }
            this.mRemoteDevice.waitUntilIdle();
        }
    }

    public void flush() throws CameraAccessException {
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            this.mDeviceExecutor.execute(this.mCallOnBusy);
            if (this.mIdle) {
                this.mDeviceExecutor.execute(this.mCallOnIdle);
                return;
            }
            long lastFrameNumber = this.mRemoteDevice.flush();
            int i = this.mRepeatingRequestId;
            if (i != -1) {
                checkEarlyTriggerSequenceCompleteLocked(i, lastFrameNumber, this.mRepeatingRequestTypes);
                this.mRepeatingRequestId = -1;
                this.mRepeatingRequestTypes = null;
            }
        }
    }

    @Override // android.hardware.camera2.CameraDevice, java.lang.AutoCloseable
    public void close() {
        synchronized (this.mInterfaceLock) {
            if (this.mClosing.getAndSet(true)) {
                return;
            }
            ExecutorService executorService = this.mOfflineSwitchService;
            if (executorService != null) {
                executorService.shutdownNow();
                this.mOfflineSwitchService = null;
            }
            ICameraDeviceUserWrapper iCameraDeviceUserWrapper = this.mRemoteDevice;
            if (iCameraDeviceUserWrapper != null) {
                iCameraDeviceUserWrapper.disconnect();
                this.mRemoteDevice.unlinkToDeath(this, 0);
            }
            CameraExtensionSessionImpl cameraExtensionSessionImpl = this.mCurrentExtensionSession;
            if (cameraExtensionSessionImpl != null) {
                cameraExtensionSessionImpl.release(true);
                this.mCurrentExtensionSession = null;
            }
            CameraAdvancedExtensionSessionImpl cameraAdvancedExtensionSessionImpl = this.mCurrentAdvancedExtensionSession;
            if (cameraAdvancedExtensionSessionImpl != null) {
                cameraAdvancedExtensionSessionImpl.release(true);
                this.mCurrentAdvancedExtensionSession = null;
            }
            if (this.mRemoteDevice != null || this.mInError) {
                this.mDeviceExecutor.execute(this.mCallOnClosed);
            }
            this.mRemoteDevice = null;
        }
    }

    protected void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    private boolean checkInputConfigurationWithStreamConfigurationsAs(InputConfiguration inputConfig, StreamConfigurationMap configMap) {
        int[] inputFormats = configMap.getInputFormats();
        boolean validFormat = false;
        int inputFormat = inputConfig.getFormat();
        for (int format : inputFormats) {
            if (format == inputFormat) {
                validFormat = true;
            }
        }
        if (!validFormat) {
            return false;
        }
        boolean validSize = false;
        Size[] inputSizes = configMap.getInputSizes(inputFormat);
        for (Size s : inputSizes) {
            if (inputConfig.getWidth() == s.getWidth() && inputConfig.getHeight() == s.getHeight()) {
                validSize = true;
            }
        }
        return validSize;
    }

    private boolean checkInputConfigurationWithStreamConfigurations(InputConfiguration inputConfig, boolean maxResolution) {
        CameraCharacteristics.Key<StreamConfigurationMap> ck = CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP;
        if (maxResolution) {
            ck = CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP_MAXIMUM_RESOLUTION;
        }
        StreamConfigurationMap configMap = (StreamConfigurationMap) this.mCharacteristics.get(ck);
        if (configMap != null && checkInputConfigurationWithStreamConfigurationsAs(inputConfig, configMap)) {
            return true;
        }
        for (Map.Entry<String, CameraCharacteristics> entry : this.mPhysicalIdsToChars.entrySet()) {
            StreamConfigurationMap configMap2 = (StreamConfigurationMap) entry.getValue().get(ck);
            if (configMap2 != null && checkInputConfigurationWithStreamConfigurationsAs(inputConfig, configMap2)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkPrivilegedAppList() {
        String packageName = ActivityThread.currentOpPackageName();
        String packageList = SystemProperties.get("persist.vendor.camera.privapp.list");
        if (packageList.length() > 0) {
            TextUtils.StringSplitter splitter = new TextUtils.SimpleStringSplitter(',');
            splitter.setString(packageList);
            for (String str : splitter) {
                if (packageName.equals(str)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public boolean isPrivilegedApp() {
        return this.mIsPrivilegedApp;
    }

    private void checkInputConfiguration(InputConfiguration inputConfig) {
        if (inputConfig == null) {
            return;
        }
        int inputFormat = inputConfig.getFormat();
        if (inputConfig.isMultiResolution()) {
            MultiResolutionStreamConfigurationMap configMap = (MultiResolutionStreamConfigurationMap) this.mCharacteristics.get(CameraCharacteristics.SCALER_MULTI_RESOLUTION_STREAM_CONFIGURATION_MAP);
            int[] inputFormats = configMap.getInputFormats();
            boolean validFormat = false;
            for (int format : inputFormats) {
                if (format == inputFormat) {
                    validFormat = true;
                }
            }
            if (!validFormat) {
                throw new IllegalArgumentException("multi-resolution input format " + inputFormat + " is not valid");
            }
            boolean validSize = false;
            Collection<MultiResolutionStreamInfo> inputStreamInfo = configMap.getInputInfo(inputFormat);
            for (MultiResolutionStreamInfo info : inputStreamInfo) {
                if (inputConfig.getWidth() == info.getWidth() && inputConfig.getHeight() == info.getHeight()) {
                    validSize = true;
                }
            }
            if (!validSize) {
                throw new IllegalArgumentException("Multi-resolution input size " + inputConfig.getWidth() + "x" + inputConfig.getHeight() + " is not valid");
            }
        } else if (isPrivilegedApp()) {
            Log.w(this.TAG, "ignore input format/size check for white listed app");
        } else if (!checkInputConfigurationWithStreamConfigurations(inputConfig, false) && !checkInputConfigurationWithStreamConfigurations(inputConfig, true)) {
            throw new IllegalArgumentException("Input config with format " + inputFormat + " and size " + inputConfig.getWidth() + "x" + inputConfig.getHeight() + " not supported by camera id " + this.mCameraId);
        }
    }

    /* loaded from: classes.dex */
    public static abstract class StateCallbackKK extends CameraDevice.StateCallback {
        public void onUnconfigured(CameraDevice camera) {
        }

        public void onActive(CameraDevice camera) {
        }

        public void onBusy(CameraDevice camera) {
        }

        public void onIdle(CameraDevice camera) {
        }

        public void onRequestQueueEmpty() {
        }

        public void onSurfacePrepared(Surface surface) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkAndFireSequenceComplete() {
        long completedFrameNumber;
        long completedFrameNumber2 = this.mFrameNumberTracker.getCompletedFrameNumber();
        long completedReprocessFrameNumber = this.mFrameNumberTracker.getCompletedReprocessFrameNumber();
        long completedZslStillFrameNumber = this.mFrameNumberTracker.getCompletedZslStillFrameNumber();
        Iterator<RequestLastFrameNumbersHolder> iter = this.mRequestLastFrameNumbersList.iterator();
        while (iter.hasNext()) {
            final RequestLastFrameNumbersHolder requestLastFrameNumbers = iter.next();
            final int requestId = requestLastFrameNumbers.getRequestId();
            if (this.mRemoteDevice == null) {
                Log.w(this.TAG, "Camera closed while checking sequences");
                return;
            }
            if (requestLastFrameNumbers.isSequenceCompleted()) {
                completedFrameNumber = completedFrameNumber2;
            } else {
                long lastRegularFrameNumber = requestLastFrameNumbers.getLastRegularFrameNumber();
                long lastReprocessFrameNumber = requestLastFrameNumbers.getLastReprocessFrameNumber();
                long lastZslStillFrameNumber = requestLastFrameNumbers.getLastZslStillFrameNumber();
                if (lastRegularFrameNumber <= completedFrameNumber2 && lastReprocessFrameNumber <= completedReprocessFrameNumber && lastZslStillFrameNumber <= completedZslStillFrameNumber) {
                    requestLastFrameNumbers.markSequenceCompleted();
                }
                completedFrameNumber = completedFrameNumber2;
                int index = this.mCaptureCallbackMap.indexOfKey(requestId);
                final CaptureCallbackHolder holder = index >= 0 ? this.mCaptureCallbackMap.valueAt(index) : null;
                if (holder != null && requestLastFrameNumbers.isSequenceCompleted()) {
                    Runnable resultDispatch = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.11
                        @Override // java.lang.Runnable
                        public void run() {
                            if (!CameraDeviceImpl.this.isClosed()) {
                                holder.getCallback().onCaptureSequenceCompleted(CameraDeviceImpl.this, requestId, requestLastFrameNumbers.getLastFrameNumber());
                            }
                        }
                    };
                    long ident = Binder.clearCallingIdentity();
                    try {
                        try {
                            holder.getExecutor().execute(resultDispatch);
                            Binder.restoreCallingIdentity(ident);
                        } catch (Throwable th) {
                            th = th;
                            Binder.restoreCallingIdentity(ident);
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                    }
                }
            }
            if (requestLastFrameNumbers.isSequenceCompleted() && requestLastFrameNumbers.isInflightCompleted()) {
                int index2 = this.mCaptureCallbackMap.indexOfKey(requestId);
                if (index2 >= 0) {
                    this.mCaptureCallbackMap.removeAt(index2);
                }
                iter.remove();
            }
            completedFrameNumber2 = completedFrameNumber;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeCompletedCallbackHolderLocked(long lastCompletedRegularFrameNumber, long lastCompletedReprocessFrameNumber, long lastCompletedZslStillFrameNumber) {
        Iterator<RequestLastFrameNumbersHolder> iter = this.mRequestLastFrameNumbersList.iterator();
        while (iter.hasNext()) {
            RequestLastFrameNumbersHolder requestLastFrameNumbers = iter.next();
            int requestId = requestLastFrameNumbers.getRequestId();
            if (this.mRemoteDevice == null) {
                Log.w(this.TAG, "Camera closed while removing completed callback holders");
                return;
            }
            long lastRegularFrameNumber = requestLastFrameNumbers.getLastRegularFrameNumber();
            long lastReprocessFrameNumber = requestLastFrameNumbers.getLastReprocessFrameNumber();
            long lastZslStillFrameNumber = requestLastFrameNumbers.getLastZslStillFrameNumber();
            if (lastRegularFrameNumber <= lastCompletedRegularFrameNumber && lastReprocessFrameNumber <= lastCompletedReprocessFrameNumber && lastZslStillFrameNumber <= lastCompletedZslStillFrameNumber) {
                if (requestLastFrameNumbers.isSequenceCompleted()) {
                    int index = this.mCaptureCallbackMap.indexOfKey(requestId);
                    if (index >= 0) {
                        this.mCaptureCallbackMap.removeAt(index);
                    }
                    iter.remove();
                } else {
                    requestLastFrameNumbers.markInflightCompleted();
                }
            }
        }
    }

    public void onDeviceError(int errorCode, CaptureResultExtras resultExtras) {
        synchronized (this.mInterfaceLock) {
            if (this.mRemoteDevice == null) {
                return;
            }
            CameraOfflineSessionImpl cameraOfflineSessionImpl = this.mOfflineSessionImpl;
            if (cameraOfflineSessionImpl != null) {
                cameraOfflineSessionImpl.getCallbacks().onDeviceError(errorCode, resultExtras);
                return;
            }
            switch (errorCode) {
                case 0:
                    long ident = Binder.clearCallingIdentity();
                    this.mDeviceExecutor.execute(this.mCallOnDisconnected);
                    Binder.restoreCallingIdentity(ident);
                    break;
                case 1:
                    scheduleNotifyError(4);
                    break;
                case 2:
                default:
                    String str = this.TAG;
                    Log.e(str, "Unknown error from camera device: " + errorCode);
                    scheduleNotifyError(5);
                    break;
                case 3:
                case 4:
                case 5:
                    onCaptureErrorLocked(errorCode, resultExtras);
                    break;
                case 6:
                    scheduleNotifyError(3);
                    break;
            }
        }
    }

    private void scheduleNotifyError(int code) {
        this.mInError = true;
        long ident = Binder.clearCallingIdentity();
        try {
            this.mDeviceExecutor.execute(PooledLambda.obtainRunnable(CameraDeviceImpl$$ExternalSyntheticLambda1.INSTANCE, this, Integer.valueOf(code)).mo3494recycleOnUse());
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyError(int code) {
        if (!isClosed()) {
            this.mDeviceCallback.onError(this, code);
        }
    }

    private void onCaptureErrorLocked(int errorCode, CaptureResultExtras resultExtras) {
        long ident;
        int requestId = resultExtras.getRequestId();
        int subsequenceId = resultExtras.getSubsequenceId();
        final long frameNumber = resultExtras.getFrameNumber();
        String errorPhysicalCameraId = resultExtras.getErrorPhysicalCameraId();
        final CaptureCallbackHolder holder = this.mCaptureCallbackMap.get(requestId);
        int i = 0;
        if (holder == null) {
            Log.e(this.TAG, String.format("Receive capture error on unknown request ID %d", Integer.valueOf(requestId)));
            return;
        }
        final CaptureRequest request = holder.getRequest(subsequenceId);
        if (errorCode == 5) {
            OutputConfiguration config = this.mConfiguredOutputs.get(resultExtras.getErrorStreamId());
            if (config == null) {
                Log.v(this.TAG, String.format("Stream %d has been removed. Skipping buffer lost callback", Integer.valueOf(resultExtras.getErrorStreamId())));
                return;
            }
            for (final Surface surface : config.getSurfaces()) {
                if (request.containsTarget(surface)) {
                    Runnable failureDispatch = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.12
                        @Override // java.lang.Runnable
                        public void run() {
                            if (!CameraDeviceImpl.this.isClosed()) {
                                holder.getCallback().onCaptureBufferLost(CameraDeviceImpl.this, request, surface, frameNumber);
                            }
                        }
                    };
                    ident = Binder.clearCallingIdentity();
                    try {
                        holder.getExecutor().execute(failureDispatch);
                    } finally {
                    }
                }
            }
            return;
        }
        boolean mayHaveBuffers = errorCode == 4;
        CameraCaptureSessionCore cameraCaptureSessionCore = this.mCurrentSession;
        if (cameraCaptureSessionCore != null && cameraCaptureSessionCore.isAborting()) {
            i = 1;
        }
        int reason = i;
        int subsequenceId2 = subsequenceId;
        final CaptureFailure failure = new CaptureFailure(request, reason, mayHaveBuffers, requestId, frameNumber, errorPhysicalCameraId);
        Runnable failureDispatch2 = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.13
            @Override // java.lang.Runnable
            public void run() {
                if (!CameraDeviceImpl.this.isClosed()) {
                    holder.getCallback().onCaptureFailed(CameraDeviceImpl.this, request, failure);
                }
            }
        };
        if (this.mBatchOutputMap.containsKey(Integer.valueOf(requestId))) {
            int i2 = 0;
            while (i2 < this.mBatchOutputMap.get(Integer.valueOf(requestId)).intValue()) {
                this.mFrameNumberTracker.updateTracker(frameNumber - (subsequenceId2 - i2), true, request.getRequestType());
                i2++;
                subsequenceId2 = subsequenceId2;
            }
        } else {
            this.mFrameNumberTracker.updateTracker(frameNumber, true, request.getRequestType());
        }
        checkAndFireSequenceComplete();
        ident = Binder.clearCallingIdentity();
        try {
            holder.getExecutor().execute(failureDispatch2);
        } finally {
        }
    }

    public void onDeviceIdle() {
        synchronized (this.mInterfaceLock) {
            if (this.mRemoteDevice == null) {
                return;
            }
            CameraOfflineSessionImpl cameraOfflineSessionImpl = this.mOfflineSessionImpl;
            if (cameraOfflineSessionImpl != null) {
                cameraOfflineSessionImpl.getCallbacks().onDeviceIdle();
                return;
            }
            removeCompletedCallbackHolderLocked(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE);
            if (!this.mIdle) {
                long ident = Binder.clearCallingIdentity();
                this.mDeviceExecutor.execute(this.mCallOnIdle);
                Binder.restoreCallingIdentity(ident);
            }
            this.mIdle = true;
        }
    }

    /* loaded from: classes.dex */
    public class CameraDeviceCallbacks extends ICameraDeviceCallbacks.Stub {
        public CameraDeviceCallbacks() {
        }

        @Override // android.hardware.camera2.ICameraDeviceCallbacks.Stub, android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.hardware.camera2.ICameraDeviceCallbacks
        public void onDeviceError(int errorCode, CaptureResultExtras resultExtras) {
            CameraDeviceImpl.this.onDeviceError(errorCode, resultExtras);
        }

        @Override // android.hardware.camera2.ICameraDeviceCallbacks
        public void onRepeatingRequestError(long lastFrameNumber, int repeatingRequestId) {
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                if (CameraDeviceImpl.this.mRemoteDevice != null && CameraDeviceImpl.this.mRepeatingRequestId != -1) {
                    if (CameraDeviceImpl.this.mOfflineSessionImpl != null) {
                        CameraDeviceImpl.this.mOfflineSessionImpl.getCallbacks().onRepeatingRequestError(lastFrameNumber, repeatingRequestId);
                        return;
                    }
                    CameraDeviceImpl cameraDeviceImpl = CameraDeviceImpl.this;
                    cameraDeviceImpl.checkEarlyTriggerSequenceCompleteLocked(cameraDeviceImpl.mRepeatingRequestId, lastFrameNumber, CameraDeviceImpl.this.mRepeatingRequestTypes);
                    if (CameraDeviceImpl.this.mRepeatingRequestId == repeatingRequestId) {
                        CameraDeviceImpl.this.mRepeatingRequestId = -1;
                        CameraDeviceImpl.this.mRepeatingRequestTypes = null;
                    }
                }
            }
        }

        @Override // android.hardware.camera2.ICameraDeviceCallbacks
        public void onDeviceIdle() {
            CameraDeviceImpl.this.onDeviceIdle();
        }

        @Override // android.hardware.camera2.ICameraDeviceCallbacks
        public void onCaptureStarted(final CaptureResultExtras resultExtras, final long timestamp) {
            int requestId = resultExtras.getRequestId();
            final long frameNumber = resultExtras.getFrameNumber();
            long lastCompletedRegularFrameNumber = resultExtras.getLastCompletedRegularFrameNumber();
            long lastCompletedReprocessFrameNumber = resultExtras.getLastCompletedReprocessFrameNumber();
            long lastCompletedZslFrameNumber = resultExtras.getLastCompletedZslFrameNumber();
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                try {
                    try {
                        if (CameraDeviceImpl.this.mRemoteDevice == null) {
                            return;
                        }
                        if (CameraDeviceImpl.this.mOfflineSessionImpl != null) {
                            try {
                                CameraDeviceImpl.this.mOfflineSessionImpl.getCallbacks().onCaptureStarted(resultExtras, timestamp);
                                return;
                            } catch (Throwable th) {
                                th = th;
                                throw th;
                            }
                        }
                        CameraDeviceImpl.this.removeCompletedCallbackHolderLocked(lastCompletedRegularFrameNumber, lastCompletedReprocessFrameNumber, lastCompletedZslFrameNumber);
                        final CaptureCallbackHolder holder = (CaptureCallbackHolder) CameraDeviceImpl.this.mCaptureCallbackMap.get(requestId);
                        if (holder == null) {
                            return;
                        }
                        if (CameraDeviceImpl.this.isClosed()) {
                            return;
                        }
                        long ident = Binder.clearCallingIdentity();
                        try {
                            try {
                                holder.getExecutor().execute(new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.CameraDeviceCallbacks.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        if (!CameraDeviceImpl.this.isClosed()) {
                                            int subsequenceId = resultExtras.getSubsequenceId();
                                            CaptureRequest request = holder.getRequest(subsequenceId);
                                            if (holder.hasBatchedOutputs()) {
                                                Range<Integer> fpsRange = (Range) request.get(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE);
                                                for (int i = 0; i < holder.getRequestCount(); i++) {
                                                    holder.getCallback().onCaptureStarted(CameraDeviceImpl.this, holder.getRequest(i), timestamp - (((subsequenceId - i) * 1000000000) / fpsRange.getUpper().intValue()), frameNumber - (subsequenceId - i));
                                                }
                                                return;
                                            }
                                            holder.getCallback().onCaptureStarted(CameraDeviceImpl.this, holder.getRequest(resultExtras.getSubsequenceId()), timestamp, frameNumber);
                                        }
                                    }
                                });
                                Binder.restoreCallingIdentity(ident);
                            } catch (Throwable th2) {
                                th = th2;
                                Binder.restoreCallingIdentity(ident);
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                    }
                } catch (Throwable th5) {
                    th = th5;
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Not initialized variable reg: 22, insn: 0x00bb: MOVE  (r16 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r22 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('requestId' int)]), block:B:72:0x00b9 */
        /* JADX WARN: Type inference failed for: r14v0, types: [long] */
        /* JADX WARN: Type inference failed for: r14v1 */
        /* JADX WARN: Type inference failed for: r14v10 */
        /* JADX WARN: Type inference failed for: r14v15 */
        /* JADX WARN: Type inference failed for: r14v2 */
        /* JADX WARN: Type inference failed for: r14v3 */
        /* JADX WARN: Type inference failed for: r14v4 */
        /* JADX WARN: Type inference failed for: r14v5 */
        /* JADX WARN: Type inference failed for: r14v6 */
        /* JADX WARN: Type inference failed for: r14v7 */
        /* JADX WARN: Type inference failed for: r1v10, types: [android.hardware.camera2.impl.FrameNumberTracker] */
        @Override // android.hardware.camera2.ICameraDeviceCallbacks
        public void onResultReceived(CameraMetadataNative result, final CaptureResultExtras resultExtras, PhysicalCaptureResultInfo[] physicalResults) throws RemoteException {
            final TotalCaptureResult resultAsCapture;
            long frameNumber;
            Object obj;
            int requestId;
            Runnable resultDispatch;
            CaptureResult finalResult;
            Object obj2;
            int requestId2 = resultExtras.getRequestId();
            Object frameNumber2 = resultExtras.getFrameNumber();
            Object obj3 = CameraDeviceImpl.this.mInterfaceLock;
            synchronized (obj3) {
                try {
                    try {
                        try {
                        } catch (Throwable th) {
                            th = th;
                            frameNumber2 = obj3;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    frameNumber2 = obj3;
                }
                if (CameraDeviceImpl.this.mRemoteDevice == null) {
                    return;
                }
                if (CameraDeviceImpl.this.mOfflineSessionImpl != null) {
                    try {
                        CameraDeviceImpl.this.mOfflineSessionImpl.getCallbacks().onResultReceived(result, resultExtras, physicalResults);
                        return;
                    } catch (Throwable th4) {
                        th = th4;
                        frameNumber2 = obj3;
                    }
                } else {
                    result.set((CameraCharacteristics.Key<CameraCharacteristics.Key<Size>>) CameraCharacteristics.LENS_INFO_SHADING_MAP_SIZE, (CameraCharacteristics.Key<Size>) ((Size) CameraDeviceImpl.this.getCharacteristics().get(CameraCharacteristics.LENS_INFO_SHADING_MAP_SIZE)));
                    final CaptureCallbackHolder holder = (CaptureCallbackHolder) CameraDeviceImpl.this.mCaptureCallbackMap.get(requestId2);
                    final CaptureRequest request = holder.getRequest(resultExtras.getSubsequenceId());
                    boolean isPartialResult = resultExtras.getPartialResultCount() < CameraDeviceImpl.this.mTotalPartialCount;
                    int requestType = request.getRequestType();
                    if (holder == null) {
                        CameraDeviceImpl.this.updateTracker(requestId2, frameNumber2, requestType, null, isPartialResult);
                        return;
                    }
                    try {
                        if (CameraDeviceImpl.this.isClosed()) {
                            try {
                                CameraDeviceImpl.this.updateTracker(requestId2, frameNumber2, requestType, null, isPartialResult);
                                return;
                            } catch (Throwable th5) {
                                th = th5;
                                frameNumber2 = obj3;
                            }
                        } else {
                            try {
                                CameraMetadataNative resultCopy = holder.hasBatchedOutputs() ? new CameraMetadataNative(result) : null;
                                if (isPartialResult) {
                                    final CaptureResult resultAsCapture2 = new CaptureResult(CameraDeviceImpl.this.getId(), result, request, resultExtras);
                                    final CameraMetadataNative cameraMetadataNative = resultCopy;
                                    Runnable resultDispatch2 = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.CameraDeviceCallbacks.2
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            if (!CameraDeviceImpl.this.isClosed()) {
                                                if (holder.hasBatchedOutputs()) {
                                                    for (int i = 0; i < holder.getRequestCount(); i++) {
                                                        CameraMetadataNative resultLocal = new CameraMetadataNative(cameraMetadataNative);
                                                        CaptureResult resultInBatch = new CaptureResult(CameraDeviceImpl.this.getId(), resultLocal, holder.getRequest(i), resultExtras);
                                                        holder.getCallback().onCaptureProgressed(CameraDeviceImpl.this, holder.getRequest(i), resultInBatch);
                                                    }
                                                    return;
                                                }
                                                holder.getCallback().onCaptureProgressed(CameraDeviceImpl.this, request, resultAsCapture2);
                                            }
                                        }
                                    };
                                    resultDispatch = resultDispatch2;
                                    frameNumber = frameNumber2;
                                    requestId = requestId2;
                                    finalResult = resultAsCapture2;
                                    obj2 = obj3;
                                } else {
                                    final List<CaptureResult> partialResults = CameraDeviceImpl.this.mFrameNumberTracker.popPartialResults(frameNumber2);
                                    final long sensorTimestamp = ((Long) result.get(CaptureResult.SENSOR_TIMESTAMP)).longValue();
                                    final Range<Integer> fpsRange = (Range) request.get(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE);
                                    final int subsequenceId = resultExtras.getSubsequenceId();
                                    frameNumber = frameNumber2;
                                    try {
                                        resultAsCapture = new TotalCaptureResult(CameraDeviceImpl.this.getId(), result, request, resultExtras, partialResults, holder.getSessionId(), physicalResults);
                                        obj = obj3;
                                    } catch (Throwable th6) {
                                        th = th6;
                                        frameNumber2 = obj3;
                                    }
                                    try {
                                        final CameraMetadataNative cameraMetadataNative2 = resultCopy;
                                        requestId = requestId2;
                                        Runnable resultDispatch3 = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.CameraDeviceCallbacks.3
                                            @Override // java.lang.Runnable
                                            public void run() {
                                                if (!CameraDeviceImpl.this.isClosed()) {
                                                    if (holder.hasBatchedOutputs()) {
                                                        for (int i = 0; i < holder.getRequestCount(); i++) {
                                                            cameraMetadataNative2.set((CaptureResult.Key<CaptureResult.Key<Long>>) CaptureResult.SENSOR_TIMESTAMP, (CaptureResult.Key<Long>) Long.valueOf(sensorTimestamp - (((subsequenceId - i) * 1000000000) / ((Integer) fpsRange.getUpper()).intValue())));
                                                            CameraMetadataNative resultLocal = new CameraMetadataNative(cameraMetadataNative2);
                                                            TotalCaptureResult resultInBatch = new TotalCaptureResult(CameraDeviceImpl.this.getId(), resultLocal, holder.getRequest(i), resultExtras, partialResults, holder.getSessionId(), new PhysicalCaptureResultInfo[0]);
                                                            holder.getCallback().onCaptureCompleted(CameraDeviceImpl.this, holder.getRequest(i), resultInBatch);
                                                        }
                                                        return;
                                                    }
                                                    holder.getCallback().onCaptureCompleted(CameraDeviceImpl.this, request, resultAsCapture);
                                                }
                                            }
                                        };
                                        resultDispatch = resultDispatch3;
                                        finalResult = resultAsCapture;
                                        obj2 = obj;
                                    } catch (Throwable th7) {
                                        th = th7;
                                        frameNumber2 = obj;
                                        throw th;
                                    }
                                }
                                long ident = Binder.clearCallingIdentity();
                                holder.getExecutor().execute(resultDispatch);
                                Binder.restoreCallingIdentity(ident);
                                CameraDeviceImpl.this.updateTracker(requestId, frameNumber, requestType, finalResult, isPartialResult);
                                if (!isPartialResult) {
                                    CameraDeviceImpl.this.checkAndFireSequenceComplete();
                                }
                                return;
                            } catch (Throwable th8) {
                                th = th8;
                                frameNumber2 = obj3;
                            }
                        }
                    } catch (Throwable th9) {
                        th = th9;
                        frameNumber2 = obj3;
                    }
                }
                throw th;
            }
        }

        @Override // android.hardware.camera2.ICameraDeviceCallbacks
        public void onPrepared(int streamId) {
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                if (CameraDeviceImpl.this.mOfflineSessionImpl != null) {
                    CameraDeviceImpl.this.mOfflineSessionImpl.getCallbacks().onPrepared(streamId);
                    return;
                }
                OutputConfiguration output = (OutputConfiguration) CameraDeviceImpl.this.mConfiguredOutputs.get(streamId);
                StateCallbackKK sessionCallback = CameraDeviceImpl.this.mSessionStateCallback;
                if (sessionCallback == null) {
                    return;
                }
                if (output == null) {
                    Log.w(CameraDeviceImpl.this.TAG, "onPrepared invoked for unknown output Surface");
                    return;
                }
                List<Surface> surfaces = output.getSurfaces();
                for (Surface surface : surfaces) {
                    sessionCallback.onSurfacePrepared(surface);
                }
            }
        }

        @Override // android.hardware.camera2.ICameraDeviceCallbacks
        public void onRequestQueueEmpty() {
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                if (CameraDeviceImpl.this.mOfflineSessionImpl != null) {
                    CameraDeviceImpl.this.mOfflineSessionImpl.getCallbacks().onRequestQueueEmpty();
                    return;
                }
                StateCallbackKK sessionCallback = CameraDeviceImpl.this.mSessionStateCallback;
                if (sessionCallback == null) {
                    return;
                }
                sessionCallback.onRequestQueueEmpty();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class CameraHandlerExecutor implements Executor {
        private final Handler mHandler;

        public CameraHandlerExecutor(Handler handler) {
            Objects.requireNonNull(handler);
            this.mHandler = handler;
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable command) {
            this.mHandler.post(command);
        }
    }

    static Executor checkExecutor(Executor executor) {
        return executor == null ? checkAndWrapHandler(null) : executor;
    }

    public static <T> Executor checkExecutor(Executor executor, T callback) {
        return callback != null ? checkExecutor(executor) : executor;
    }

    public static Executor checkAndWrapHandler(Handler handler) {
        return new CameraHandlerExecutor(checkHandler(handler));
    }

    static Handler checkHandler(Handler handler) {
        if (handler == null) {
            Looper looper = Looper.myLooper();
            if (looper == null) {
                throw new IllegalArgumentException("No handler given, and current thread has no looper!");
            }
            return new Handler(looper);
        }
        return handler;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> Handler checkHandler(Handler handler, T callback) {
        if (callback != null) {
            return checkHandler(handler);
        }
        return handler;
    }

    private void checkIfCameraClosedOrInError() throws CameraAccessException {
        if (this.mRemoteDevice == null) {
            throw new IllegalStateException("CameraDevice was already closed");
        }
        if (this.mInError) {
            throw new CameraAccessException(3, "The camera device has encountered a serious error");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isClosed() {
        return this.mClosing.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public CameraCharacteristics getCharacteristics() {
        return this.mCharacteristics;
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        String str = this.TAG;
        Log.w(str, "CameraDevice " + this.mCameraId + " died unexpectedly");
        if (this.mRemoteDevice == null) {
            return;
        }
        this.mInError = true;
        Runnable r = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.14
            @Override // java.lang.Runnable
            public void run() {
                if (!CameraDeviceImpl.this.isClosed()) {
                    CameraDeviceImpl.this.mDeviceCallback.onError(CameraDeviceImpl.this, 5);
                }
            }
        };
        long ident = Binder.clearCallingIdentity();
        try {
            this.mDeviceExecutor.execute(r);
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }

    @Override // android.hardware.camera2.CameraDevice
    public void setCameraAudioRestriction(int mode) throws CameraAccessException {
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            this.mRemoteDevice.setCameraAudioRestriction(mode);
        }
    }

    @Override // android.hardware.camera2.CameraDevice
    public int getCameraAudioRestriction() throws CameraAccessException {
        int globalAudioRestriction;
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            globalAudioRestriction = this.mRemoteDevice.getGlobalAudioRestriction();
        }
        return globalAudioRestriction;
    }

    @Override // android.hardware.camera2.CameraDevice
    public void createExtensionSession(ExtensionSessionConfiguration extensionConfiguration) throws CameraAccessException {
        try {
            if (CameraExtensionCharacteristics.areAdvancedExtensionsSupported()) {
                this.mCurrentAdvancedExtensionSession = CameraAdvancedExtensionSessionImpl.createCameraAdvancedExtensionSession(this, this.mContext, extensionConfiguration);
            } else {
                this.mCurrentExtensionSession = CameraExtensionSessionImpl.createCameraExtensionSession(this, this.mContext, extensionConfiguration);
            }
        } catch (RemoteException e) {
            throw new CameraAccessException(3);
        }
    }

    private long getThermalInfo(String thermalZone) {
        BufferedReader brType = null;
        BufferedReader brTemp = null;
        try {
            try {
                try {
                    File dir = new File("/sys/class/thermal/");
                    File[] files = dir.listFiles(CameraDeviceImpl$$ExternalSyntheticLambda0.INSTANCE);
                    Objects.requireNonNull(files);
                    int SIZE = files.length;
                    for (int i = 0; i < SIZE; i++) {
                        brType = new BufferedReader(new FileReader("/sys/class/thermal/thermal_zone" + i + "/type"));
                        String line = brType.readLine();
                        if (line != null && line.contains(thermalZone)) {
                            brTemp = new BufferedReader(new FileReader("/sys/class/thermal/thermal_zone" + i + "/temp"));
                            String line2 = brTemp.readLine();
                            if (line2 != null) {
                                long parseLong = Long.parseLong(line2);
                                try {
                                    brTemp.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    brType.close();
                                } catch (IOException e2) {
                                    e2.printStackTrace();
                                }
                                return parseLong;
                            }
                        }
                        brType.close();
                    }
                    if (brTemp != null) {
                        try {
                            brTemp.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                    if (brType == null) {
                        return 0L;
                    }
                    brType.close();
                    return 0L;
                } catch (Exception e4) {
                    e4.printStackTrace();
                    if (brTemp != null) {
                        try {
                            brTemp.close();
                        } catch (IOException e5) {
                            e5.printStackTrace();
                        }
                    }
                    if (brType == null) {
                        return 0L;
                    }
                    brType.close();
                    return 0L;
                }
            } catch (IOException e6) {
                e6.printStackTrace();
                return 0L;
            }
        } catch (Throwable th) {
            if (brTemp != null) {
                try {
                    brTemp.close();
                } catch (IOException e7) {
                    e7.printStackTrace();
                }
            }
            if (brType != null) {
                try {
                    brType.close();
                } catch (IOException e8) {
                    e8.printStackTrace();
                }
            }
            throw th;
        }
    }

    private String getCallerProcessName() {
        int uid = Binder.getCallingUid();
        Context context = this.mContext;
        if (context == null) {
            return "";
        }
        String result = context.getPackageManager().getNameForUid(uid);
        return result;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void triggerDeviceClose() {
        String str;
        String userPackage = getCallerProcessName();
        if (userPackage != null && (str = this.currentUserPackageName) != null && userPackage.equals(str)) {
            long currentTime = System.currentTimeMillis();
            this.thirdPartyApplicationExitTime = this.dateFormatter.format(Long.valueOf(currentTime));
            this.thirdPartyApplicationExistTemperature = getThermalInfo(THERMAL_TEMPERATURE_TAG);
            Bundle params = new Bundle();
            params.putString("user_packagename", userPackage);
            params.putString("user_camera_id", this.mCameraId);
            params.putString("user_enter_temperature", String.valueOf(this.thirdPartyApplicationEnterTemperature));
            params.putString("user_exit_temperature", String.valueOf(this.thirdPartyApplicationExistTemperature));
            params.putString("user_enter_time", String.valueOf(this.thirdPartyApplicationEnterTime));
            params.putString("user_exit_time", String.valueOf(this.thirdPartyApplicationExitTime));
            AppTracking.getInstance(this.mContext).logQualityEvent("CameraPower", params);
        }
    }
}