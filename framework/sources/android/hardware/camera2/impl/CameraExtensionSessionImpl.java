package android.hardware.camera2.impl;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraExtensionCharacteristics;
import android.hardware.camera2.CameraExtensionSession;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.extension.CaptureBundle;
import android.hardware.camera2.extension.CaptureStageImpl;
import android.hardware.camera2.extension.ICaptureProcessorImpl;
import android.hardware.camera2.extension.IImageCaptureExtenderImpl;
import android.hardware.camera2.extension.IInitializeSessionCallback;
import android.hardware.camera2.extension.IPreviewExtenderImpl;
import android.hardware.camera2.extension.IRequestUpdateProcessorImpl;
import android.hardware.camera2.extension.ParcelImage;
import android.hardware.camera2.impl.CameraExtensionSessionImpl;
import android.hardware.camera2.impl.CameraExtensionUtils;
import android.hardware.camera2.params.ExtensionSessionConfiguration;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.params.SessionConfiguration;
import android.hardware.camera2.utils.SurfaceUtils;
import android.media.Image;
import android.media.ImageReader;
import android.media.ImageWriter;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.Pair;
import android.util.Size;
import android.view.Surface;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class CameraExtensionSessionImpl extends CameraExtensionSession {
    private static final int PREVIEW_QUEUE_SIZE = 3;
    private static final String TAG = "CameraExtensionSessionImpl";
    private final CameraExtensionSession.StateCallback mCallbacks;
    private Surface mCameraBurstSurface;
    private final CameraDevice mCameraDevice;
    private Surface mCameraRepeatingSurface;
    private Surface mClientCaptureSurface;
    private Surface mClientRepeatingRequestSurface;
    private final Executor mExecutor;
    private final long mExtensionClientId;
    private final Handler mHandler;
    private final HandlerThread mHandlerThread;
    private final IImageCaptureExtenderImpl mImageExtender;
    private final IPreviewExtenderImpl mPreviewExtender;
    private final List<Size> mSupportedPreviewSizes;
    private CameraCaptureSession mCaptureSession = null;
    private ImageReader mRepeatingRequestImageReader = null;
    private ImageReader mBurstCaptureImageReader = null;
    private ImageReader mStubCaptureImageReader = null;
    private ImageWriter mRepeatingRequestImageWriter = null;
    private CameraOutputImageCallback mRepeatingRequestImageCallback = null;
    private CameraOutputImageCallback mBurstCaptureImageCallback = null;
    private CameraExtensionJpegProcessor mImageJpegProcessor = null;
    private ICaptureProcessorImpl mImageProcessor = null;
    private CameraExtensionForwardProcessor mPreviewImageProcessor = null;
    private IRequestUpdateProcessorImpl mPreviewRequestUpdateProcessor = null;
    private int mPreviewProcessorType = 2;
    private boolean mInternalRepeatingRequestEnabled = true;
    final Object mInterfaceLock = new Object();
    private boolean mInitialized = false;
    private final InitializeSessionHandler mInitializeHandler = new InitializeSessionHandler();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public interface OnImageAvailableListener {
        void onImageAvailable(ImageReader imageReader, Image image);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int nativeGetSurfaceFormat(Surface surface) {
        return SurfaceUtils.getSurfaceFormat(surface);
    }

    public static CameraExtensionSessionImpl createCameraExtensionSession(CameraDevice cameraDevice, Context ctx, ExtensionSessionConfiguration config) throws CameraAccessException, RemoteException {
        int[] iArr;
        int suitableSurfaceCount;
        long clientId = CameraExtensionCharacteristics.registerClient(ctx);
        if (clientId < 0) {
            throw new UnsupportedOperationException("Unsupported extension!");
        }
        String cameraId = cameraDevice.getId();
        CameraManager manager = (CameraManager) ctx.getSystemService(CameraManager.class);
        CameraCharacteristics chars = manager.getCameraCharacteristics(cameraId);
        CameraExtensionCharacteristics extensionChars = new CameraExtensionCharacteristics(ctx, cameraId, chars);
        if (!CameraExtensionCharacteristics.isExtensionSupported(cameraDevice.getId(), config.getExtension(), chars)) {
            throw new UnsupportedOperationException("Unsupported extension type: " + config.getExtension());
        }
        if (!config.getOutputConfigurations().isEmpty() && config.getOutputConfigurations().size() <= 2) {
            Pair<IPreviewExtenderImpl, IImageCaptureExtenderImpl> extenders = CameraExtensionCharacteristics.initializeExtension(config.getExtension());
            int suitableSurfaceCount2 = 0;
            List<Size> supportedPreviewSizes = extensionChars.getExtensionSupportedSizes(config.getExtension(), SurfaceTexture.class);
            Surface repeatingRequestSurface = CameraExtensionUtils.getRepeatingRequestSurface(config.getOutputConfigurations(), supportedPreviewSizes);
            if (repeatingRequestSurface != null) {
                suitableSurfaceCount2 = 0 + 1;
            }
            HashMap<Integer, List<Size>> supportedCaptureSizes = new HashMap<>();
            for (int format : CameraExtensionUtils.SUPPORTED_CAPTURE_OUTPUT_FORMATS) {
                List<Size> supportedSizes = extensionChars.getExtensionSupportedSizes(config.getExtension(), format);
                if (supportedSizes != null) {
                    supportedCaptureSizes.put(Integer.valueOf(format), supportedSizes);
                }
            }
            Surface burstCaptureSurface = CameraExtensionUtils.getBurstCaptureSurface(config.getOutputConfigurations(), supportedCaptureSizes);
            if (burstCaptureSurface == null) {
                suitableSurfaceCount = suitableSurfaceCount2;
            } else {
                suitableSurfaceCount = suitableSurfaceCount2 + 1;
            }
            if (suitableSurfaceCount != config.getOutputConfigurations().size()) {
                throw new IllegalArgumentException("One or more unsupported output surfaces found!");
            }
            extenders.first.init(cameraId, chars.getNativeMetadata());
            extenders.first.onInit(cameraId, chars.getNativeMetadata());
            extenders.second.init(cameraId, chars.getNativeMetadata());
            extenders.second.onInit(cameraId, chars.getNativeMetadata());
            CameraExtensionSessionImpl session = new CameraExtensionSessionImpl(extenders.second, extenders.first, supportedPreviewSizes, clientId, cameraDevice, repeatingRequestSurface, burstCaptureSurface, config.getStateCallback(), config.getExecutor());
            session.initialize();
            return session;
        }
        throw new IllegalArgumentException("Unexpected amount of output surfaces, received: " + config.getOutputConfigurations().size() + " expected <= 2");
    }

    public CameraExtensionSessionImpl(IImageCaptureExtenderImpl imageExtender, IPreviewExtenderImpl previewExtender, List<Size> previewSizes, long extensionClientId, CameraDevice cameraDevice, Surface repeatingRequestSurface, Surface burstCaptureSurface, CameraExtensionSession.StateCallback callback, Executor executor) {
        this.mExtensionClientId = extensionClientId;
        this.mImageExtender = imageExtender;
        this.mPreviewExtender = previewExtender;
        this.mCameraDevice = cameraDevice;
        this.mCallbacks = callback;
        this.mExecutor = executor;
        this.mClientRepeatingRequestSurface = repeatingRequestSurface;
        this.mClientCaptureSurface = burstCaptureSurface;
        this.mSupportedPreviewSizes = previewSizes;
        HandlerThread handlerThread = new HandlerThread(TAG);
        this.mHandlerThread = handlerThread;
        handlerThread.start();
        this.mHandler = new Handler(handlerThread.getLooper());
    }

    private void initializeRepeatingRequestPipeline() throws RemoteException {
        CameraExtensionUtils.SurfaceInfo repeatingSurfaceInfo = new CameraExtensionUtils.SurfaceInfo();
        this.mPreviewProcessorType = this.mPreviewExtender.getProcessorType();
        Surface surface = this.mClientRepeatingRequestSurface;
        if (surface != null) {
            repeatingSurfaceInfo = CameraExtensionUtils.querySurface(surface);
        } else {
            CameraExtensionUtils.SurfaceInfo captureSurfaceInfo = CameraExtensionUtils.querySurface(this.mClientCaptureSurface);
            Size captureSize = new Size(captureSurfaceInfo.mWidth, captureSurfaceInfo.mHeight);
            Size previewSize = findSmallestAspectMatchedSize(this.mSupportedPreviewSizes, captureSize);
            repeatingSurfaceInfo.mWidth = previewSize.getWidth();
            repeatingSurfaceInfo.mHeight = previewSize.getHeight();
            repeatingSurfaceInfo.mUsage = 256L;
        }
        int i = this.mPreviewProcessorType;
        if (i == 1) {
            try {
                CameraExtensionForwardProcessor cameraExtensionForwardProcessor = new CameraExtensionForwardProcessor(this.mPreviewExtender.getPreviewImageProcessor(), repeatingSurfaceInfo.mFormat, repeatingSurfaceInfo.mUsage, this.mHandler);
                this.mPreviewImageProcessor = cameraExtensionForwardProcessor;
                cameraExtensionForwardProcessor.onImageFormatUpdate(35);
                this.mPreviewImageProcessor.onResolutionUpdate(new Size(repeatingSurfaceInfo.mWidth, repeatingSurfaceInfo.mHeight));
                this.mPreviewImageProcessor.onOutputSurface(null, -1);
                ImageReader newInstance = ImageReader.newInstance(repeatingSurfaceInfo.mWidth, repeatingSurfaceInfo.mHeight, 35, 3, repeatingSurfaceInfo.mUsage);
                this.mRepeatingRequestImageReader = newInstance;
                this.mCameraRepeatingSurface = newInstance.getSurface();
            } catch (ClassCastException e) {
                throw new UnsupportedOperationException("Failed casting preview processor!");
            }
        } else if (i == 0) {
            try {
                this.mPreviewRequestUpdateProcessor = this.mPreviewExtender.getRequestUpdateProcessor();
                ImageReader newInstance2 = ImageReader.newInstance(repeatingSurfaceInfo.mWidth, repeatingSurfaceInfo.mHeight, 34, 3, repeatingSurfaceInfo.mUsage);
                this.mRepeatingRequestImageReader = newInstance2;
                this.mCameraRepeatingSurface = newInstance2.getSurface();
                android.hardware.camera2.extension.Size sz = new android.hardware.camera2.extension.Size();
                sz.width = repeatingSurfaceInfo.mWidth;
                sz.height = repeatingSurfaceInfo.mHeight;
                this.mPreviewRequestUpdateProcessor.onResolutionUpdate(sz);
                this.mPreviewRequestUpdateProcessor.onImageFormatUpdate(34);
            } catch (ClassCastException e2) {
                throw new UnsupportedOperationException("Failed casting preview processor!");
            }
        } else {
            ImageReader newInstance3 = ImageReader.newInstance(repeatingSurfaceInfo.mWidth, repeatingSurfaceInfo.mHeight, 34, 3, repeatingSurfaceInfo.mUsage);
            this.mRepeatingRequestImageReader = newInstance3;
            this.mCameraRepeatingSurface = newInstance3.getSurface();
        }
        CameraOutputImageCallback cameraOutputImageCallback = new CameraOutputImageCallback(this.mRepeatingRequestImageReader);
        this.mRepeatingRequestImageCallback = cameraOutputImageCallback;
        this.mRepeatingRequestImageReader.setOnImageAvailableListener(cameraOutputImageCallback, this.mHandler);
    }

    private void initializeBurstCapturePipeline() throws RemoteException {
        ICaptureProcessorImpl captureProcessor = this.mImageExtender.getCaptureProcessor();
        this.mImageProcessor = captureProcessor;
        if (captureProcessor == null && this.mImageExtender.getMaxCaptureStage() != 1) {
            throw new UnsupportedOperationException("Multiple stages expected without a valid capture processor!");
        }
        if (this.mImageProcessor != null) {
            Surface surface = this.mClientCaptureSurface;
            if (surface != null) {
                CameraExtensionUtils.SurfaceInfo surfaceInfo = CameraExtensionUtils.querySurface(surface);
                if (surfaceInfo.mFormat == 256) {
                    CameraExtensionJpegProcessor cameraExtensionJpegProcessor = new CameraExtensionJpegProcessor(this.mImageProcessor);
                    this.mImageJpegProcessor = cameraExtensionJpegProcessor;
                    this.mImageProcessor = cameraExtensionJpegProcessor;
                }
                this.mBurstCaptureImageReader = ImageReader.newInstance(surfaceInfo.mWidth, surfaceInfo.mHeight, 35, this.mImageExtender.getMaxCaptureStage());
            } else {
                this.mBurstCaptureImageReader = ImageReader.newInstance(this.mRepeatingRequestImageReader.getWidth(), this.mRepeatingRequestImageReader.getHeight(), 35, 1);
                ImageReader newInstance = ImageReader.newInstance(this.mRepeatingRequestImageReader.getWidth(), this.mRepeatingRequestImageReader.getHeight(), 35, 1);
                this.mStubCaptureImageReader = newInstance;
                this.mImageProcessor.onOutputSurface(newInstance.getSurface(), 35);
            }
            CameraOutputImageCallback cameraOutputImageCallback = new CameraOutputImageCallback(this.mBurstCaptureImageReader);
            this.mBurstCaptureImageCallback = cameraOutputImageCallback;
            this.mBurstCaptureImageReader.setOnImageAvailableListener(cameraOutputImageCallback, this.mHandler);
            this.mCameraBurstSurface = this.mBurstCaptureImageReader.getSurface();
            android.hardware.camera2.extension.Size sz = new android.hardware.camera2.extension.Size();
            sz.width = this.mBurstCaptureImageReader.getWidth();
            sz.height = this.mBurstCaptureImageReader.getHeight();
            this.mImageProcessor.onResolutionUpdate(sz);
            this.mImageProcessor.onImageFormatUpdate(this.mBurstCaptureImageReader.getImageFormat());
            return;
        }
        Surface surface2 = this.mClientCaptureSurface;
        if (surface2 != null) {
            this.mCameraBurstSurface = surface2;
            return;
        }
        ImageReader newInstance2 = ImageReader.newInstance(this.mRepeatingRequestImageReader.getWidth(), this.mRepeatingRequestImageReader.getHeight(), 256, 1);
        this.mBurstCaptureImageReader = newInstance2;
        this.mCameraBurstSurface = newInstance2.getSurface();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishPipelineInitialization() throws RemoteException {
        Surface surface;
        Surface surface2 = this.mClientRepeatingRequestSurface;
        if (surface2 != null) {
            int i = this.mPreviewProcessorType;
            if (i == 0) {
                this.mPreviewRequestUpdateProcessor.onOutputSurface(surface2, nativeGetSurfaceFormat(surface2));
                this.mRepeatingRequestImageWriter = ImageWriter.newInstance(this.mClientRepeatingRequestSurface, 3, 34);
            } else if (i == 2) {
                this.mRepeatingRequestImageWriter = ImageWriter.newInstance(surface2, 3, 34);
            }
        }
        if (this.mImageProcessor != null && (surface = this.mClientCaptureSurface) != null) {
            CameraExtensionUtils.SurfaceInfo surfaceInfo = CameraExtensionUtils.querySurface(surface);
            this.mImageProcessor.onOutputSurface(this.mClientCaptureSurface, surfaceInfo.mFormat);
        }
    }

    public synchronized void initialize() throws CameraAccessException, RemoteException {
        if (this.mInitialized) {
            Log.d(TAG, "Session already initialized");
            return;
        }
        ArrayList<CaptureStageImpl> sessionParamsList = new ArrayList<>();
        ArrayList<OutputConfiguration> outputList = new ArrayList<>();
        initializeRepeatingRequestPipeline();
        outputList.add(new OutputConfiguration(this.mCameraRepeatingSurface));
        CaptureStageImpl previewSessionParams = this.mPreviewExtender.onPresetSession();
        if (previewSessionParams != null) {
            sessionParamsList.add(previewSessionParams);
        }
        initializeBurstCapturePipeline();
        outputList.add(new OutputConfiguration(this.mCameraBurstSurface));
        CaptureStageImpl stillCaptureSessionParams = this.mImageExtender.onPresetSession();
        if (stillCaptureSessionParams != null) {
            sessionParamsList.add(stillCaptureSessionParams);
        }
        SessionConfiguration sessionConfig = new SessionConfiguration(0, outputList, new CameraExtensionUtils.HandlerExecutor(this.mHandler), new SessionStateHandler());
        if (!sessionParamsList.isEmpty()) {
            CaptureRequest sessionParamRequest = createRequest(this.mCameraDevice, sessionParamsList, null, 1);
            sessionConfig.setSessionParameters(sessionParamRequest);
        }
        this.mCameraDevice.createCaptureSession(sessionConfig);
    }

    @Override // android.hardware.camera2.CameraExtensionSession
    public CameraDevice getDevice() {
        CameraDevice cameraDevice;
        synchronized (this.mInterfaceLock) {
            cameraDevice = this.mCameraDevice;
        }
        return cameraDevice;
    }

    @Override // android.hardware.camera2.CameraExtensionSession
    public int setRepeatingRequest(CaptureRequest request, Executor executor, CameraExtensionSession.ExtensionCaptureCallback listener) throws CameraAccessException {
        int repeatingRequest;
        synchronized (this.mInterfaceLock) {
            if (!this.mInitialized) {
                throw new IllegalStateException("Uninitialized component");
            }
            Surface surface = this.mClientRepeatingRequestSurface;
            if (surface == null) {
                throw new IllegalArgumentException("No registered preview surface");
            }
            if (!request.containsTarget(surface) || request.getTargets().size() != 1) {
                throw new IllegalArgumentException("Invalid repeating request output target!");
            }
            this.mInternalRepeatingRequestEnabled = false;
            try {
                repeatingRequest = setRepeatingRequest(this.mPreviewExtender.getCaptureStage(), new RepeatingRequestHandler(request, executor, listener, this.mRepeatingRequestImageCallback));
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to set repeating request! Extension service does not respond");
                throw new CameraAccessException(3);
            }
        }
        return repeatingRequest;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayList<CaptureStageImpl> compileInitialRequestList() {
        ArrayList<CaptureStageImpl> captureStageList = new ArrayList<>();
        try {
            CaptureStageImpl initialPreviewParams = this.mPreviewExtender.onEnableSession();
            if (initialPreviewParams != null) {
                captureStageList.add(initialPreviewParams);
            }
            CaptureStageImpl initialStillCaptureParams = this.mImageExtender.onEnableSession();
            if (initialStillCaptureParams != null) {
                captureStageList.add(initialStillCaptureParams);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to initialize session parameters! Extension service does not respond!");
        }
        return captureStageList;
    }

    private static List<CaptureRequest> createBurstRequest(CameraDevice cameraDevice, List<CaptureStageImpl> captureStageList, CaptureRequest clientRequest, Surface target, int captureTemplate, Map<CaptureRequest, Integer> captureMap) {
        ArrayList<CaptureRequest> ret = new ArrayList<>();
        for (CaptureStageImpl captureStage : captureStageList) {
            try {
                CaptureRequest.Builder requestBuilder = cameraDevice.createCaptureRequest(captureTemplate);
                Integer jpegRotation = (Integer) clientRequest.get(CaptureRequest.JPEG_ORIENTATION);
                if (jpegRotation != null) {
                    captureStage.parameters.set((CaptureRequest.Key<CaptureRequest.Key<Integer>>) CaptureRequest.JPEG_ORIENTATION, (CaptureRequest.Key<Integer>) jpegRotation);
                }
                Byte jpegQuality = (Byte) clientRequest.get(CaptureRequest.JPEG_QUALITY);
                if (jpegQuality != null) {
                    captureStage.parameters.set((CaptureRequest.Key<CaptureRequest.Key<Byte>>) CaptureRequest.JPEG_QUALITY, (CaptureRequest.Key<Byte>) jpegQuality);
                }
                requestBuilder.addTarget(target);
                CaptureRequest request = requestBuilder.build();
                CameraMetadataNative.update(request.getNativeMetadata(), captureStage.parameters);
                ret.add(request);
                captureMap.put(request, Integer.valueOf(captureStage.id));
            } catch (CameraAccessException e) {
                return null;
            }
        }
        return ret;
    }

    private static CaptureRequest createRequest(CameraDevice cameraDevice, List<CaptureStageImpl> captureStageList, Surface target, int captureTemplate) throws CameraAccessException {
        CaptureRequest.Builder requestBuilder = cameraDevice.createCaptureRequest(captureTemplate);
        if (target != null) {
            requestBuilder.addTarget(target);
        }
        CaptureRequest ret = requestBuilder.build();
        for (CaptureStageImpl captureStage : captureStageList) {
            if (captureStage != null) {
                CameraMetadataNative.update(ret.getNativeMetadata(), captureStage.parameters);
            }
        }
        return ret;
    }

    @Override // android.hardware.camera2.CameraExtensionSession
    public int capture(CaptureRequest request, Executor executor, CameraExtensionSession.ExtensionCaptureCallback listener) throws CameraAccessException {
        if (this.mInitialized) {
            Surface surface = this.mClientCaptureSurface;
            if (surface == null) {
                throw new IllegalArgumentException("No output surface registered for single requests!");
            }
            if (!request.containsTarget(surface) || request.getTargets().size() != 1) {
                throw new IllegalArgumentException("Invalid single capture output target!");
            }
            HashMap<CaptureRequest, Integer> requestMap = new HashMap<>();
            try {
                List<CaptureRequest> burstRequest = createBurstRequest(this.mCameraDevice, this.mImageExtender.getCaptureStages(), request, this.mCameraBurstSurface, 2, requestMap);
                if (burstRequest != null) {
                    return this.mCaptureSession.captureBurstRequests(burstRequest, new CameraExtensionUtils.HandlerExecutor(this.mHandler), new BurstRequestHandler(request, executor, listener, requestMap, this.mBurstCaptureImageCallback));
                }
                throw new UnsupportedOperationException("Failed to create still capture burst request");
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to initialize internal burst request! Extension service does not respond!");
                throw new CameraAccessException(3);
            }
        }
        throw new IllegalStateException("Uninitialized component");
    }

    @Override // android.hardware.camera2.CameraExtensionSession
    public void stopRepeating() throws CameraAccessException {
        synchronized (this.mInterfaceLock) {
            if (!this.mInitialized) {
                throw new IllegalStateException("Uninitialized component");
            }
            this.mInternalRepeatingRequestEnabled = true;
            this.mCaptureSession.stopRepeating();
        }
    }

    @Override // android.hardware.camera2.CameraExtensionSession, java.lang.AutoCloseable
    public void close() throws CameraAccessException {
        synchronized (this.mInterfaceLock) {
            if (this.mInitialized) {
                this.mInternalRepeatingRequestEnabled = false;
                this.mCaptureSession.stopRepeating();
                ArrayList<CaptureStageImpl> captureStageList = new ArrayList<>();
                try {
                    CaptureStageImpl disableParams = this.mPreviewExtender.onDisableSession();
                    if (disableParams != null) {
                        captureStageList.add(disableParams);
                    }
                    CaptureStageImpl disableStillCaptureParams = this.mImageExtender.onDisableSession();
                    if (disableStillCaptureParams != null) {
                        captureStageList.add(disableStillCaptureParams);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "Failed to disable extension! Extension service does not respond!");
                }
                if (!captureStageList.isEmpty()) {
                    CaptureRequest disableRequest = createRequest(this.mCameraDevice, captureStageList, this.mCameraRepeatingSurface, 1);
                    this.mCaptureSession.capture(disableRequest, new CloseRequestHandler(this.mRepeatingRequestImageCallback), this.mHandler);
                }
                this.mCaptureSession.close();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setInitialCaptureRequest(List<CaptureStageImpl> captureStageList, InitialRequestHandler requestHandler) throws CameraAccessException {
        CaptureRequest initialRequest = createRequest(this.mCameraDevice, captureStageList, this.mCameraRepeatingSurface, 1);
        this.mCaptureSession.capture(initialRequest, requestHandler, this.mHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int setRepeatingRequest(CaptureStageImpl captureStage, CameraCaptureSession.CaptureCallback requestHandler) throws CameraAccessException {
        ArrayList<CaptureStageImpl> captureStageList = new ArrayList<>();
        captureStageList.add(captureStage);
        CaptureRequest repeatingRequest = createRequest(this.mCameraDevice, captureStageList, this.mCameraRepeatingSurface, 1);
        return this.mCaptureSession.setSingleRepeatingRequest(repeatingRequest, new CameraExtensionUtils.HandlerExecutor(this.mHandler), requestHandler);
    }

    public void release(boolean skipCloseNotification) {
        boolean notifyClose = false;
        synchronized (this.mInterfaceLock) {
            this.mInternalRepeatingRequestEnabled = false;
            this.mHandlerThread.quitSafely();
            try {
                this.mPreviewExtender.onDeInit();
                this.mImageExtender.onDeInit();
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to release extensions! Extension service does not respond!");
            }
            long j = this.mExtensionClientId;
            if (j >= 0) {
                CameraExtensionCharacteristics.unregisterClient(j);
                if (this.mInitialized) {
                    notifyClose = true;
                    CameraExtensionCharacteristics.releaseSession();
                }
            }
            this.mInitialized = false;
            CameraOutputImageCallback cameraOutputImageCallback = this.mRepeatingRequestImageCallback;
            if (cameraOutputImageCallback != null) {
                cameraOutputImageCallback.close();
                this.mRepeatingRequestImageCallback = null;
            }
            ImageReader imageReader = this.mRepeatingRequestImageReader;
            if (imageReader != null) {
                imageReader.close();
                this.mRepeatingRequestImageReader = null;
            }
            CameraOutputImageCallback cameraOutputImageCallback2 = this.mBurstCaptureImageCallback;
            if (cameraOutputImageCallback2 != null) {
                cameraOutputImageCallback2.close();
                this.mBurstCaptureImageCallback = null;
            }
            ImageReader imageReader2 = this.mBurstCaptureImageReader;
            if (imageReader2 != null) {
                imageReader2.close();
                this.mBurstCaptureImageReader = null;
            }
            ImageReader imageReader3 = this.mStubCaptureImageReader;
            if (imageReader3 != null) {
                imageReader3.close();
                this.mStubCaptureImageReader = null;
            }
            ImageWriter imageWriter = this.mRepeatingRequestImageWriter;
            if (imageWriter != null) {
                imageWriter.close();
                this.mRepeatingRequestImageWriter = null;
            }
            CameraExtensionForwardProcessor cameraExtensionForwardProcessor = this.mPreviewImageProcessor;
            if (cameraExtensionForwardProcessor != null) {
                cameraExtensionForwardProcessor.close();
                this.mPreviewImageProcessor = null;
            }
            CameraExtensionJpegProcessor cameraExtensionJpegProcessor = this.mImageJpegProcessor;
            if (cameraExtensionJpegProcessor != null) {
                cameraExtensionJpegProcessor.close();
                this.mImageJpegProcessor = null;
            }
            this.mCaptureSession = null;
            this.mImageProcessor = null;
            this.mClientRepeatingRequestSurface = null;
            this.mCameraRepeatingSurface = null;
            this.mClientCaptureSurface = null;
            this.mCameraBurstSurface = null;
        }
        if (notifyClose && !skipCloseNotification) {
            long ident = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new Runnable() { // from class: android.hardware.camera2.impl.CameraExtensionSessionImpl$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        CameraExtensionSessionImpl.this.lambda$release$0$CameraExtensionSessionImpl();
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(ident);
            }
        }
    }

    public /* synthetic */ void lambda$release$0$CameraExtensionSessionImpl() {
        this.mCallbacks.onClosed(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyConfigurationFailure() {
        synchronized (this.mInterfaceLock) {
            if (this.mInitialized) {
                return;
            }
            release(true);
            long ident = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new Runnable() { // from class: android.hardware.camera2.impl.CameraExtensionSessionImpl$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        CameraExtensionSessionImpl.this.lambda$notifyConfigurationFailure$1$CameraExtensionSessionImpl();
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(ident);
            }
        }
    }

    public /* synthetic */ void lambda$notifyConfigurationFailure$1$CameraExtensionSessionImpl() {
        this.mCallbacks.onConfigureFailed(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyConfigurationSuccess() {
        synchronized (this.mInterfaceLock) {
            if (this.mInitialized) {
                return;
            }
            this.mInitialized = true;
            long ident = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new Runnable() { // from class: android.hardware.camera2.impl.CameraExtensionSessionImpl$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        CameraExtensionSessionImpl.this.lambda$notifyConfigurationSuccess$2$CameraExtensionSessionImpl();
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(ident);
            }
        }
    }

    public /* synthetic */ void lambda$notifyConfigurationSuccess$2$CameraExtensionSessionImpl() {
        this.mCallbacks.onConfigured(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class SessionStateHandler extends CameraCaptureSession.StateCallback {
        private SessionStateHandler() {
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onClosed(CameraCaptureSession session) {
            CameraExtensionSessionImpl.this.release(false);
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigureFailed(CameraCaptureSession session) {
            CameraExtensionSessionImpl.this.notifyConfigurationFailure();
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigured(CameraCaptureSession session) {
            synchronized (CameraExtensionSessionImpl.this.mInterfaceLock) {
                CameraExtensionSessionImpl.this.mCaptureSession = session;
                try {
                    CameraExtensionSessionImpl.this.finishPipelineInitialization();
                    CameraExtensionCharacteristics.initializeSession(CameraExtensionSessionImpl.this.mInitializeHandler);
                } catch (RemoteException e) {
                    Log.e(CameraExtensionSessionImpl.TAG, "Failed to initialize session! Extension service does not respond!");
                    CameraExtensionSessionImpl.this.notifyConfigurationFailure();
                }
            }
        }
    }

    /* loaded from: classes.dex */
    private class InitializeSessionHandler extends IInitializeSessionCallback.Stub {
        private InitializeSessionHandler() {
        }

        @Override // android.hardware.camera2.extension.IInitializeSessionCallback
        public void onSuccess() {
            boolean status = true;
            ArrayList<CaptureStageImpl> initialRequestList = CameraExtensionSessionImpl.this.compileInitialRequestList();
            if (!initialRequestList.isEmpty()) {
                try {
                    CameraExtensionSessionImpl cameraExtensionSessionImpl = CameraExtensionSessionImpl.this;
                    cameraExtensionSessionImpl.setInitialCaptureRequest(initialRequestList, new InitialRequestHandler(cameraExtensionSessionImpl.mRepeatingRequestImageCallback));
                } catch (CameraAccessException e) {
                    Log.e(CameraExtensionSessionImpl.TAG, "Failed to initialize the initial capture request!");
                    status = false;
                }
            } else {
                try {
                    CameraExtensionSessionImpl cameraExtensionSessionImpl2 = CameraExtensionSessionImpl.this;
                    CaptureStageImpl captureStage = cameraExtensionSessionImpl2.mPreviewExtender.getCaptureStage();
                    CameraExtensionSessionImpl cameraExtensionSessionImpl3 = CameraExtensionSessionImpl.this;
                    cameraExtensionSessionImpl2.setRepeatingRequest(captureStage, new RepeatingRequestHandler(null, null, null, cameraExtensionSessionImpl3.mRepeatingRequestImageCallback));
                } catch (CameraAccessException | RemoteException e2) {
                    Log.e(CameraExtensionSessionImpl.TAG, "Failed to initialize internal repeating request!");
                    status = false;
                }
            }
            if (!status) {
                CameraExtensionSessionImpl.this.notifyConfigurationFailure();
            }
        }

        @Override // android.hardware.camera2.extension.IInitializeSessionCallback
        public void onFailure() {
            CameraExtensionSessionImpl.this.mCaptureSession.close();
            Log.e(CameraExtensionSessionImpl.TAG, "Failed to initialize proxy service session! This can happen when trying to configure multiple concurrent extension sessions!");
            CameraExtensionSessionImpl.this.notifyConfigurationFailure();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class BurstRequestHandler extends CameraCaptureSession.CaptureCallback {
        private final CameraOutputImageCallback mBurstImageCallback;
        private final CameraExtensionSession.ExtensionCaptureCallback mCallbacks;
        private final HashMap<CaptureRequest, Integer> mCaptureRequestMap;
        private final CaptureRequest mClientRequest;
        private final Executor mExecutor;
        private HashMap<Integer, Pair<Image, TotalCaptureResult>> mCaptureStageMap = new HashMap<>();
        private LongSparseArray<Pair<Image, Integer>> mCapturePendingMap = new LongSparseArray<>();
        private ImageCallback mImageCallback = null;
        private boolean mCaptureFailed = false;

        public BurstRequestHandler(CaptureRequest request, Executor executor, CameraExtensionSession.ExtensionCaptureCallback callbacks, HashMap<CaptureRequest, Integer> requestMap, CameraOutputImageCallback imageCallback) {
            this.mClientRequest = request;
            this.mExecutor = executor;
            this.mCallbacks = callbacks;
            this.mCaptureRequestMap = requestMap;
            this.mBurstImageCallback = imageCallback;
        }

        private void notifyCaptureFailed() {
            if (!this.mCaptureFailed) {
                this.mCaptureFailed = true;
                long ident = Binder.clearCallingIdentity();
                try {
                    this.mExecutor.execute(new Runnable() { // from class: android.hardware.camera2.impl.CameraExtensionSessionImpl$BurstRequestHandler$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            CameraExtensionSessionImpl.BurstRequestHandler.this.lambda$notifyCaptureFailed$0$CameraExtensionSessionImpl$BurstRequestHandler();
                        }
                    });
                    Binder.restoreCallingIdentity(ident);
                    for (Pair<Image, TotalCaptureResult> captureStage : this.mCaptureStageMap.values()) {
                        captureStage.first.close();
                    }
                    this.mCaptureStageMap.clear();
                } catch (Throwable th) {
                    Binder.restoreCallingIdentity(ident);
                    throw th;
                }
            }
        }

        public /* synthetic */ void lambda$notifyCaptureFailed$0$CameraExtensionSessionImpl$BurstRequestHandler() {
            this.mCallbacks.onCaptureFailed(CameraExtensionSessionImpl.this, this.mClientRequest);
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, final long timestamp, long frameNumber) {
            boolean initialCallback = false;
            synchronized (CameraExtensionSessionImpl.this.mInterfaceLock) {
                if (CameraExtensionSessionImpl.this.mImageProcessor == null || this.mImageCallback != null) {
                    if (CameraExtensionSessionImpl.this.mImageProcessor == null) {
                        initialCallback = true;
                    }
                } else {
                    this.mImageCallback = new ImageCallback();
                    initialCallback = true;
                }
            }
            if (initialCallback) {
                long ident = Binder.clearCallingIdentity();
                try {
                    this.mExecutor.execute(new Runnable() { // from class: android.hardware.camera2.impl.CameraExtensionSessionImpl$BurstRequestHandler$$ExternalSyntheticLambda6
                        @Override // java.lang.Runnable
                        public final void run() {
                            CameraExtensionSessionImpl.BurstRequestHandler.this.lambda$onCaptureStarted$1$CameraExtensionSessionImpl$BurstRequestHandler(timestamp);
                        }
                    });
                } finally {
                    Binder.restoreCallingIdentity(ident);
                }
            }
            CameraOutputImageCallback cameraOutputImageCallback = this.mBurstImageCallback;
            if (cameraOutputImageCallback != null && this.mImageCallback != null) {
                cameraOutputImageCallback.registerListener(Long.valueOf(timestamp), this.mImageCallback);
            }
        }

        public /* synthetic */ void lambda$onCaptureStarted$1$CameraExtensionSessionImpl$BurstRequestHandler(long timestamp) {
            this.mCallbacks.onCaptureStarted(CameraExtensionSessionImpl.this, this.mClientRequest, timestamp);
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureBufferLost(CameraCaptureSession session, CaptureRequest request, Surface target, long frameNumber) {
            notifyCaptureFailed();
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureFailed(CameraCaptureSession session, CaptureRequest request, CaptureFailure failure) {
            notifyCaptureFailed();
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureSequenceAborted(CameraCaptureSession session, final int sequenceId) {
            long ident = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new Runnable() { // from class: android.hardware.camera2.impl.CameraExtensionSessionImpl$BurstRequestHandler$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        CameraExtensionSessionImpl.BurstRequestHandler.this.lambda$onCaptureSequenceAborted$2$CameraExtensionSessionImpl$BurstRequestHandler(sequenceId);
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(ident);
            }
        }

        public /* synthetic */ void lambda$onCaptureSequenceAborted$2$CameraExtensionSessionImpl$BurstRequestHandler(int sequenceId) {
            this.mCallbacks.onCaptureSequenceAborted(CameraExtensionSessionImpl.this, sequenceId);
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureSequenceCompleted(CameraCaptureSession session, final int sequenceId, long frameNumber) {
            long ident = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new Runnable() { // from class: android.hardware.camera2.impl.CameraExtensionSessionImpl$BurstRequestHandler$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        CameraExtensionSessionImpl.BurstRequestHandler.this.lambda$onCaptureSequenceCompleted$3$CameraExtensionSessionImpl$BurstRequestHandler(sequenceId);
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(ident);
            }
        }

        public /* synthetic */ void lambda$onCaptureSequenceCompleted$3$CameraExtensionSessionImpl$BurstRequestHandler(int sequenceId) {
            this.mCallbacks.onCaptureSequenceCompleted(CameraExtensionSessionImpl.this, sequenceId);
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            if (!this.mCaptureRequestMap.containsKey(request)) {
                Log.e(CameraExtensionSessionImpl.TAG, "Unexpected still capture request received!");
                return;
            }
            Integer stageId = this.mCaptureRequestMap.get(request);
            Long timestamp = (Long) result.get(CaptureResult.SENSOR_TIMESTAMP);
            if (timestamp != null) {
                if (CameraExtensionSessionImpl.this.mImageProcessor != null) {
                    if (this.mCapturePendingMap.indexOfKey(timestamp.longValue()) >= 0) {
                        Image img = this.mCapturePendingMap.get(timestamp.longValue()).first;
                        this.mCaptureStageMap.put(stageId, new Pair<>(img, result));
                        checkAndFireBurstProcessing();
                        return;
                    }
                    this.mCapturePendingMap.put(timestamp.longValue(), new Pair<>(null, stageId));
                    this.mCaptureStageMap.put(stageId, new Pair<>(null, result));
                    return;
                }
                this.mCaptureRequestMap.clear();
                long ident = Binder.clearCallingIdentity();
                try {
                    this.mExecutor.execute(new Runnable() { // from class: android.hardware.camera2.impl.CameraExtensionSessionImpl$BurstRequestHandler$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            CameraExtensionSessionImpl.BurstRequestHandler.this.lambda$onCaptureCompleted$4$CameraExtensionSessionImpl$BurstRequestHandler();
                        }
                    });
                    return;
                } finally {
                    Binder.restoreCallingIdentity(ident);
                }
            }
            Log.e(CameraExtensionSessionImpl.TAG, "Capture result without valid sensor timestamp!");
        }

        public /* synthetic */ void lambda$onCaptureCompleted$4$CameraExtensionSessionImpl$BurstRequestHandler() {
            this.mCallbacks.onCaptureProcessStarted(CameraExtensionSessionImpl.this, this.mClientRequest);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:6:0x001e  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void checkAndFireBurstProcessing() {
            if (this.mCaptureRequestMap.size() == this.mCaptureStageMap.size()) {
                for (Pair<Image, TotalCaptureResult> captureStage : this.mCaptureStageMap.values()) {
                    if (captureStage.first == null || captureStage.second == null) {
                        return;
                    }
                    while (r0.hasNext()) {
                    }
                }
                this.mCaptureRequestMap.clear();
                this.mCapturePendingMap.clear();
                boolean processStatus = true;
                Byte jpegQuality = (Byte) this.mClientRequest.get(CaptureRequest.JPEG_QUALITY);
                Integer jpegOrientation = (Integer) this.mClientRequest.get(CaptureRequest.JPEG_ORIENTATION);
                List<CaptureBundle> captureList = CameraExtensionSessionImpl.initializeParcelable(this.mCaptureStageMap, jpegOrientation, jpegQuality);
                try {
                    CameraExtensionSessionImpl.this.mImageProcessor.process(captureList);
                } catch (RemoteException e) {
                    Log.e(CameraExtensionSessionImpl.TAG, "Failed to process multi-frame request! Extension service does not respond!");
                    processStatus = false;
                }
                for (CaptureBundle bundle : captureList) {
                    bundle.captureImage.buffer.close();
                }
                captureList.clear();
                for (Pair<Image, TotalCaptureResult> captureStage2 : this.mCaptureStageMap.values()) {
                    captureStage2.first.close();
                }
                this.mCaptureStageMap.clear();
                long ident = Binder.clearCallingIdentity();
                try {
                    if (processStatus) {
                        this.mExecutor.execute(new Runnable() { // from class: android.hardware.camera2.impl.CameraExtensionSessionImpl$BurstRequestHandler$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                CameraExtensionSessionImpl.BurstRequestHandler.this.lambda$checkAndFireBurstProcessing$5$CameraExtensionSessionImpl$BurstRequestHandler();
                            }
                        });
                    } else {
                        this.mExecutor.execute(new Runnable() { // from class: android.hardware.camera2.impl.CameraExtensionSessionImpl$BurstRequestHandler$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                CameraExtensionSessionImpl.BurstRequestHandler.this.lambda$checkAndFireBurstProcessing$6$CameraExtensionSessionImpl$BurstRequestHandler();
                            }
                        });
                    }
                } finally {
                    Binder.restoreCallingIdentity(ident);
                }
            }
        }

        public /* synthetic */ void lambda$checkAndFireBurstProcessing$5$CameraExtensionSessionImpl$BurstRequestHandler() {
            this.mCallbacks.onCaptureProcessStarted(CameraExtensionSessionImpl.this, this.mClientRequest);
        }

        public /* synthetic */ void lambda$checkAndFireBurstProcessing$6$CameraExtensionSessionImpl$BurstRequestHandler() {
            this.mCallbacks.onCaptureFailed(CameraExtensionSessionImpl.this, this.mClientRequest);
        }

        /* loaded from: classes.dex */
        private class ImageCallback implements OnImageAvailableListener {
            private ImageCallback() {
            }

            @Override // android.hardware.camera2.impl.CameraExtensionSessionImpl.OnImageAvailableListener
            public void onImageAvailable(ImageReader reader, Image img) {
                if (BurstRequestHandler.this.mCaptureFailed) {
                    img.close();
                }
                long timestamp = img.getTimestamp();
                reader.detachImage(img);
                if (BurstRequestHandler.this.mCapturePendingMap.indexOfKey(timestamp) >= 0) {
                    Integer stageId = (Integer) ((Pair) BurstRequestHandler.this.mCapturePendingMap.get(timestamp)).second;
                    Pair<Image, TotalCaptureResult> captureStage = (Pair) BurstRequestHandler.this.mCaptureStageMap.get(stageId);
                    if (captureStage != null) {
                        BurstRequestHandler.this.mCaptureStageMap.put(stageId, new Pair(img, (TotalCaptureResult) captureStage.second));
                        BurstRequestHandler.this.checkAndFireBurstProcessing();
                        return;
                    }
                    Log.e(CameraExtensionSessionImpl.TAG, "Capture stage: " + ((Pair) BurstRequestHandler.this.mCapturePendingMap.get(timestamp)).second + " is absent!");
                    return;
                }
                BurstRequestHandler.this.mCapturePendingMap.put(timestamp, new Pair(img, -1));
            }
        }
    }

    /* loaded from: classes.dex */
    private class ImageLoopbackCallback implements OnImageAvailableListener {
        private ImageLoopbackCallback() {
        }

        @Override // android.hardware.camera2.impl.CameraExtensionSessionImpl.OnImageAvailableListener
        public void onImageAvailable(ImageReader reader, Image img) {
            img.close();
        }
    }

    /* loaded from: classes.dex */
    private class InitialRequestHandler extends CameraCaptureSession.CaptureCallback {
        private final CameraOutputImageCallback mImageCallback;

        public InitialRequestHandler(CameraOutputImageCallback imageCallback) {
            this.mImageCallback = imageCallback;
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frameNumber) {
            this.mImageCallback.registerListener(Long.valueOf(timestamp), new ImageLoopbackCallback());
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureSequenceAborted(CameraCaptureSession session, int sequenceId) {
            Log.e(CameraExtensionSessionImpl.TAG, "Initial capture request aborted!");
            CameraExtensionSessionImpl.this.notifyConfigurationFailure();
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureFailed(CameraCaptureSession session, CaptureRequest request, CaptureFailure failure) {
            Log.e(CameraExtensionSessionImpl.TAG, "Initial capture request failed!");
            CameraExtensionSessionImpl.this.notifyConfigurationFailure();
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureSequenceCompleted(CameraCaptureSession session, int sequenceId, long frameNumber) {
            boolean status = true;
            synchronized (CameraExtensionSessionImpl.this.mInterfaceLock) {
                try {
                    CameraExtensionSessionImpl cameraExtensionSessionImpl = CameraExtensionSessionImpl.this;
                    cameraExtensionSessionImpl.setRepeatingRequest(cameraExtensionSessionImpl.mPreviewExtender.getCaptureStage(), new RepeatingRequestHandler(null, null, null, this.mImageCallback));
                } catch (CameraAccessException | RemoteException e) {
                    Log.e(CameraExtensionSessionImpl.TAG, "Failed to start the internal repeating request!");
                    status = false;
                }
            }
            if (!status) {
                CameraExtensionSessionImpl.this.notifyConfigurationFailure();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class CameraOutputImageCallback implements ImageReader.OnImageAvailableListener, Closeable {
        private final ImageReader mImageReader;
        private HashMap<Long, Pair<Image, OnImageAvailableListener>> mImageListenerMap = new HashMap<>();
        private boolean mOutOfBuffers = false;

        CameraOutputImageCallback(ImageReader imageReader) {
            this.mImageReader = imageReader;
        }

        @Override // android.media.ImageReader.OnImageAvailableListener
        public void onImageAvailable(ImageReader reader) {
            try {
                Image img = reader.acquireNextImage();
                if (img == null) {
                    Log.e(CameraExtensionSessionImpl.TAG, "Invalid image!");
                    return;
                }
                Long timestamp = Long.valueOf(img.getTimestamp());
                if (this.mImageListenerMap.containsKey(timestamp)) {
                    Pair<Image, OnImageAvailableListener> entry = this.mImageListenerMap.remove(timestamp);
                    if (entry.second == null) {
                        Log.w(CameraExtensionSessionImpl.TAG, "Invalid image listener, dropping frame!");
                        img.close();
                        return;
                    }
                    entry.second.onImageAvailable(reader, img);
                    return;
                }
                this.mImageListenerMap.put(Long.valueOf(img.getTimestamp()), new Pair<>(img, null));
            } catch (IllegalStateException e) {
                Log.e(CameraExtensionSessionImpl.TAG, "Failed to acquire image, too many images pending!");
                this.mOutOfBuffers = true;
            }
        }

        public void registerListener(Long timestamp, OnImageAvailableListener listener) {
            if (this.mImageListenerMap.containsKey(timestamp)) {
                Pair<Image, OnImageAvailableListener> entry = this.mImageListenerMap.remove(timestamp);
                if (entry.first != null) {
                    listener.onImageAvailable(this.mImageReader, entry.first);
                    if (this.mOutOfBuffers) {
                        this.mOutOfBuffers = false;
                        Log.w(CameraExtensionSessionImpl.TAG, "Out of buffers, retry!");
                        onImageAvailable(this.mImageReader);
                        return;
                    }
                    return;
                }
                Log.w(CameraExtensionSessionImpl.TAG, "No valid image for listener with ts: " + timestamp.longValue());
                return;
            }
            this.mImageListenerMap.put(timestamp, new Pair<>(null, listener));
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            for (Pair<Image, OnImageAvailableListener> entry : this.mImageListenerMap.values()) {
                if (entry.first != null) {
                    entry.first.close();
                }
            }
            this.mImageListenerMap.clear();
        }
    }

    /* loaded from: classes.dex */
    private class CloseRequestHandler extends CameraCaptureSession.CaptureCallback {
        private final CameraOutputImageCallback mImageCallback;

        public CloseRequestHandler(CameraOutputImageCallback imageCallback) {
            this.mImageCallback = imageCallback;
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frameNumber) {
            this.mImageCallback.registerListener(Long.valueOf(timestamp), new ImageLoopbackCallback());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class RepeatingRequestHandler extends CameraCaptureSession.CaptureCallback {
        private final CameraExtensionSession.ExtensionCaptureCallback mCallbacks;
        private final boolean mClientNotificationsEnabled;
        private final CaptureRequest mClientRequest;
        private final Executor mExecutor;
        private final CameraOutputImageCallback mRepeatingImageCallback;
        private OnImageAvailableListener mImageCallback = null;
        private LongSparseArray<Pair<Image, TotalCaptureResult>> mPendingResultMap = new LongSparseArray<>();
        private boolean mRequestUpdatedNeeded = false;

        public RepeatingRequestHandler(CaptureRequest clientRequest, Executor executor, CameraExtensionSession.ExtensionCaptureCallback listener, CameraOutputImageCallback imageCallback) {
            boolean z = false;
            this.mClientRequest = clientRequest;
            this.mExecutor = executor;
            this.mCallbacks = listener;
            if (clientRequest != null && executor != null && listener != null) {
                z = true;
            }
            this.mClientNotificationsEnabled = z;
            this.mRepeatingImageCallback = imageCallback;
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, final long timestamp, long frameNumber) {
            OnImageAvailableListener imageLoopbackCallback;
            synchronized (CameraExtensionSessionImpl.this.mInterfaceLock) {
                if (this.mImageCallback == null) {
                    if (CameraExtensionSessionImpl.this.mPreviewProcessorType == 1) {
                        if (!this.mClientNotificationsEnabled) {
                            CameraExtensionSessionImpl.this.mPreviewImageProcessor.onOutputSurface(null, -1);
                        } else {
                            CameraExtensionSessionImpl.this.mPreviewImageProcessor.onOutputSurface(CameraExtensionSessionImpl.this.mClientRepeatingRequestSurface, CameraExtensionSessionImpl.nativeGetSurfaceFormat(CameraExtensionSessionImpl.this.mClientRepeatingRequestSurface));
                        }
                        this.mImageCallback = new ImageProcessCallback();
                    } else {
                        if (this.mClientNotificationsEnabled) {
                            imageLoopbackCallback = new ImageForwardCallback(CameraExtensionSessionImpl.this.mRepeatingRequestImageWriter);
                        } else {
                            imageLoopbackCallback = new ImageLoopbackCallback();
                        }
                        this.mImageCallback = imageLoopbackCallback;
                    }
                }
            }
            if (this.mClientNotificationsEnabled) {
                long ident = Binder.clearCallingIdentity();
                try {
                    this.mExecutor.execute(new Runnable() { // from class: android.hardware.camera2.impl.CameraExtensionSessionImpl$RepeatingRequestHandler$$ExternalSyntheticLambda6
                        @Override // java.lang.Runnable
                        public final void run() {
                            CameraExtensionSessionImpl.RepeatingRequestHandler.this.lambda$onCaptureStarted$0$CameraExtensionSessionImpl$RepeatingRequestHandler(timestamp);
                        }
                    });
                } finally {
                    Binder.restoreCallingIdentity(ident);
                }
            }
            this.mRepeatingImageCallback.registerListener(Long.valueOf(timestamp), this.mImageCallback);
        }

        public /* synthetic */ void lambda$onCaptureStarted$0$CameraExtensionSessionImpl$RepeatingRequestHandler(long timestamp) {
            this.mCallbacks.onCaptureStarted(CameraExtensionSessionImpl.this, this.mClientRequest, timestamp);
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureSequenceAborted(CameraCaptureSession session, final int sequenceId) {
            synchronized (CameraExtensionSessionImpl.this.mInterfaceLock) {
                if (CameraExtensionSessionImpl.this.mInternalRepeatingRequestEnabled) {
                    resumeInternalRepeatingRequest(true);
                }
            }
            if (!this.mClientNotificationsEnabled) {
                CameraExtensionSessionImpl.this.notifyConfigurationFailure();
                return;
            }
            long ident = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new Runnable() { // from class: android.hardware.camera2.impl.CameraExtensionSessionImpl$RepeatingRequestHandler$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        CameraExtensionSessionImpl.RepeatingRequestHandler.this.lambda$onCaptureSequenceAborted$1$CameraExtensionSessionImpl$RepeatingRequestHandler(sequenceId);
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(ident);
            }
        }

        public /* synthetic */ void lambda$onCaptureSequenceAborted$1$CameraExtensionSessionImpl$RepeatingRequestHandler(int sequenceId) {
            this.mCallbacks.onCaptureSequenceAborted(CameraExtensionSessionImpl.this, sequenceId);
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureSequenceCompleted(CameraCaptureSession session, final int sequenceId, long frameNumber) {
            synchronized (CameraExtensionSessionImpl.this.mInterfaceLock) {
                if (!this.mRequestUpdatedNeeded) {
                    if (CameraExtensionSessionImpl.this.mInternalRepeatingRequestEnabled) {
                        resumeInternalRepeatingRequest(true);
                    }
                } else {
                    this.mRequestUpdatedNeeded = false;
                    resumeInternalRepeatingRequest(false);
                }
            }
            if (this.mClientNotificationsEnabled) {
                long ident = Binder.clearCallingIdentity();
                try {
                    this.mExecutor.execute(new Runnable() { // from class: android.hardware.camera2.impl.CameraExtensionSessionImpl$RepeatingRequestHandler$$ExternalSyntheticLambda5
                        @Override // java.lang.Runnable
                        public final void run() {
                            CameraExtensionSessionImpl.RepeatingRequestHandler.this.lambda$onCaptureSequenceCompleted$2$CameraExtensionSessionImpl$RepeatingRequestHandler(sequenceId);
                        }
                    });
                } finally {
                    Binder.restoreCallingIdentity(ident);
                }
            }
        }

        public /* synthetic */ void lambda$onCaptureSequenceCompleted$2$CameraExtensionSessionImpl$RepeatingRequestHandler(int sequenceId) {
            this.mCallbacks.onCaptureSequenceCompleted(CameraExtensionSessionImpl.this, sequenceId);
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureFailed(CameraCaptureSession session, CaptureRequest request, CaptureFailure failure) {
            if (this.mClientNotificationsEnabled) {
                long ident = Binder.clearCallingIdentity();
                try {
                    this.mExecutor.execute(new Runnable() { // from class: android.hardware.camera2.impl.CameraExtensionSessionImpl$RepeatingRequestHandler$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            CameraExtensionSessionImpl.RepeatingRequestHandler.this.lambda$onCaptureFailed$3$CameraExtensionSessionImpl$RepeatingRequestHandler();
                        }
                    });
                } finally {
                    Binder.restoreCallingIdentity(ident);
                }
            }
        }

        public /* synthetic */ void lambda$onCaptureFailed$3$CameraExtensionSessionImpl$RepeatingRequestHandler() {
            this.mCallbacks.onCaptureFailed(CameraExtensionSessionImpl.this, this.mClientRequest);
        }

        /* JADX WARN: Removed duplicated region for block: B:16:0x0113 A[Catch: all -> 0x0149, TRY_LEAVE, TryCatch #5 {, blocks: (B:4:0x0008, B:6:0x0012, B:11:0x001d, B:29:0x003b, B:16:0x0113, B:19:0x012e, B:21:0x0140, B:14:0x0051, B:33:0x0044, B:37:0x0032, B:38:0x0055, B:40:0x005d, B:42:0x0069, B:45:0x0086, B:46:0x009b, B:47:0x00df, B:56:0x00aa, B:52:0x00c9, B:57:0x00ff, B:58:0x0139, B:18:0x0119, B:20:0x0124, B:44:0x007d, B:55:0x00a3, B:51:0x00c2), top: B:3:0x0008 }] */
        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            Image image;
            boolean notifyClient = this.mClientNotificationsEnabled;
            boolean processStatus = true;
            synchronized (CameraExtensionSessionImpl.this.mInterfaceLock) {
                Long timestamp = (Long) result.get(CaptureResult.SENSOR_TIMESTAMP);
                if (timestamp != null) {
                    if (CameraExtensionSessionImpl.this.mPreviewProcessorType == 0) {
                        CaptureStageImpl captureStage = null;
                        try {
                            captureStage = CameraExtensionSessionImpl.this.mPreviewRequestUpdateProcessor.process(result.getNativeMetadata(), result.getSequenceId());
                        } catch (RemoteException e) {
                            Log.e(CameraExtensionSessionImpl.TAG, "Extension service does not respond during processing!");
                        }
                        if (captureStage != null) {
                            try {
                                CameraExtensionSessionImpl.this.setRepeatingRequest(captureStage, this);
                                this.mRequestUpdatedNeeded = true;
                            } catch (CameraAccessException e2) {
                                Log.e(CameraExtensionSessionImpl.TAG, "Failed to update repeating request settings!");
                            } catch (IllegalStateException e3) {
                            }
                        } else {
                            this.mRequestUpdatedNeeded = false;
                        }
                        if (notifyClient) {
                            long ident = Binder.clearCallingIdentity();
                            if (processStatus) {
                                this.mExecutor.execute(new Runnable() { // from class: android.hardware.camera2.impl.CameraExtensionSessionImpl$RepeatingRequestHandler$$ExternalSyntheticLambda1
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        CameraExtensionSessionImpl.RepeatingRequestHandler.this.lambda$onCaptureCompleted$4$CameraExtensionSessionImpl$RepeatingRequestHandler();
                                    }
                                });
                            } else {
                                this.mExecutor.execute(new Runnable() { // from class: android.hardware.camera2.impl.CameraExtensionSessionImpl$RepeatingRequestHandler$$ExternalSyntheticLambda2
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        CameraExtensionSessionImpl.RepeatingRequestHandler.this.lambda$onCaptureCompleted$5$CameraExtensionSessionImpl$RepeatingRequestHandler();
                                    }
                                });
                            }
                            Binder.restoreCallingIdentity(ident);
                        }
                    } else {
                        if (CameraExtensionSessionImpl.this.mPreviewProcessorType == 1) {
                            int idx = this.mPendingResultMap.indexOfKey(timestamp.longValue());
                            if (idx >= 0) {
                                ParcelImage parcelImage = CameraExtensionSessionImpl.initializeParcelImage(this.mPendingResultMap.get(timestamp.longValue()).first);
                                try {
                                    CameraExtensionSessionImpl.this.mPreviewImageProcessor.process(parcelImage, result);
                                    parcelImage.buffer.close();
                                    image = this.mPendingResultMap.get(timestamp.longValue()).first;
                                } catch (RemoteException e4) {
                                    processStatus = false;
                                    Log.e(CameraExtensionSessionImpl.TAG, "Extension service does not respond during processing, dropping frame!");
                                    parcelImage.buffer.close();
                                    image = this.mPendingResultMap.get(timestamp.longValue()).first;
                                } catch (RuntimeException e5) {
                                    processStatus = false;
                                    Log.e(CameraExtensionSessionImpl.TAG, "Runtime exception encountered during buffer processing, dropping frame!");
                                    parcelImage.buffer.close();
                                    image = this.mPendingResultMap.get(timestamp.longValue()).first;
                                }
                                image.close();
                                discardPendingRepeatingResults(idx, this.mPendingResultMap, false);
                            } else {
                                notifyClient = false;
                                this.mPendingResultMap.put(timestamp.longValue(), new Pair<>(null, result));
                            }
                        }
                        if (notifyClient) {
                        }
                    }
                }
                Log.e(CameraExtensionSessionImpl.TAG, "Result without valid sensor timestamp!");
            }
            if (!notifyClient) {
                CameraExtensionSessionImpl.this.notifyConfigurationSuccess();
            }
        }

        public /* synthetic */ void lambda$onCaptureCompleted$4$CameraExtensionSessionImpl$RepeatingRequestHandler() {
            this.mCallbacks.onCaptureProcessStarted(CameraExtensionSessionImpl.this, this.mClientRequest);
        }

        public /* synthetic */ void lambda$onCaptureCompleted$5$CameraExtensionSessionImpl$RepeatingRequestHandler() {
            this.mCallbacks.onCaptureFailed(CameraExtensionSessionImpl.this, this.mClientRequest);
        }

        private void resumeInternalRepeatingRequest(boolean internal) {
            try {
                if (internal) {
                    CameraExtensionSessionImpl cameraExtensionSessionImpl = CameraExtensionSessionImpl.this;
                    cameraExtensionSessionImpl.setRepeatingRequest(cameraExtensionSessionImpl.mPreviewExtender.getCaptureStage(), new RepeatingRequestHandler(null, null, null, this.mRepeatingImageCallback));
                } else {
                    CameraExtensionSessionImpl cameraExtensionSessionImpl2 = CameraExtensionSessionImpl.this;
                    cameraExtensionSessionImpl2.setRepeatingRequest(cameraExtensionSessionImpl2.mPreviewExtender.getCaptureStage(), this);
                }
            } catch (CameraAccessException e) {
                Log.e(CameraExtensionSessionImpl.TAG, "Failed to resume internal repeating request!");
            } catch (RemoteException e2) {
                Log.e(CameraExtensionSessionImpl.TAG, "Failed to resume internal repeating request, extension service fails to respond!");
            } catch (IllegalStateException e3) {
                Log.w(CameraExtensionSessionImpl.TAG, "Failed to resume internal repeating request!");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Long calculatePruneThreshold(LongSparseArray<Pair<Image, TotalCaptureResult>> previewMap) {
            long oldestTimestamp = Long.MAX_VALUE;
            for (int idx = 0; idx < previewMap.size(); idx++) {
                Pair<Image, TotalCaptureResult> entry = previewMap.valueAt(idx);
                long timestamp = previewMap.keyAt(idx);
                if (entry.first != null && timestamp < oldestTimestamp) {
                    oldestTimestamp = timestamp;
                }
            }
            return Long.valueOf(oldestTimestamp == Long.MAX_VALUE ? 0L : oldestTimestamp);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void discardPendingRepeatingResults(int idx, LongSparseArray<Pair<Image, TotalCaptureResult>> previewMap, boolean notifyCurrentIndex) {
            if (idx < 0) {
                return;
            }
            for (int i = idx; i >= 0; i--) {
                if (previewMap.valueAt(i).first != null) {
                    previewMap.valueAt(i).first.close();
                } else if (this.mClientNotificationsEnabled && (i != idx || notifyCurrentIndex)) {
                    Log.w(CameraExtensionSessionImpl.TAG, "Preview frame drop with timestamp: " + previewMap.keyAt(i));
                    long ident = Binder.clearCallingIdentity();
                    try {
                        this.mExecutor.execute(new Runnable() { // from class: android.hardware.camera2.impl.CameraExtensionSessionImpl$RepeatingRequestHandler$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                CameraExtensionSessionImpl.RepeatingRequestHandler.this.lambda$discardPendingRepeatingResults$6$CameraExtensionSessionImpl$RepeatingRequestHandler();
                            }
                        });
                    } finally {
                        Binder.restoreCallingIdentity(ident);
                    }
                }
                previewMap.removeAt(i);
            }
        }

        public /* synthetic */ void lambda$discardPendingRepeatingResults$6$CameraExtensionSessionImpl$RepeatingRequestHandler() {
            this.mCallbacks.onCaptureFailed(CameraExtensionSessionImpl.this, this.mClientRequest);
        }

        /* loaded from: classes.dex */
        private class ImageForwardCallback implements OnImageAvailableListener {
            private final ImageWriter mOutputWriter;

            public ImageForwardCallback(ImageWriter imageWriter) {
                this.mOutputWriter = imageWriter;
            }

            @Override // android.hardware.camera2.impl.CameraExtensionSessionImpl.OnImageAvailableListener
            public void onImageAvailable(ImageReader reader, Image img) {
                if (img == null) {
                    Log.e(CameraExtensionSessionImpl.TAG, "Invalid image!");
                    return;
                }
                try {
                    this.mOutputWriter.queueInputImage(img);
                } catch (IllegalStateException e) {
                    Log.w(CameraExtensionSessionImpl.TAG, "Output surface likely abandoned, dropping buffer!");
                    img.close();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public class ImageProcessCallback implements OnImageAvailableListener {
            private ImageProcessCallback() {
            }

            @Override // android.hardware.camera2.impl.CameraExtensionSessionImpl.OnImageAvailableListener
            public void onImageAvailable(ImageReader reader, Image img) {
                if (RepeatingRequestHandler.this.mPendingResultMap.size() + 1 >= 3) {
                    RepeatingRequestHandler repeatingRequestHandler = RepeatingRequestHandler.this;
                    LongSparseArray longSparseArray = repeatingRequestHandler.mPendingResultMap;
                    RepeatingRequestHandler repeatingRequestHandler2 = RepeatingRequestHandler.this;
                    repeatingRequestHandler.discardPendingRepeatingResults(longSparseArray.indexOfKey(repeatingRequestHandler2.calculatePruneThreshold(repeatingRequestHandler2.mPendingResultMap).longValue()), RepeatingRequestHandler.this.mPendingResultMap, true);
                }
                if (img == null) {
                    Log.e(CameraExtensionSessionImpl.TAG, "Invalid preview buffer!");
                    return;
                }
                try {
                    reader.detachImage(img);
                    long timestamp = img.getTimestamp();
                    int idx = RepeatingRequestHandler.this.mPendingResultMap.indexOfKey(timestamp);
                    if (idx < 0) {
                        RepeatingRequestHandler.this.mPendingResultMap.put(timestamp, new Pair(img, null));
                        return;
                    }
                    boolean processStatus = true;
                    ParcelImage parcelImage = CameraExtensionSessionImpl.initializeParcelImage(img);
                    try {
                        try {
                            CameraExtensionSessionImpl.this.mPreviewImageProcessor.process(parcelImage, (TotalCaptureResult) ((Pair) RepeatingRequestHandler.this.mPendingResultMap.get(timestamp)).second);
                        } catch (RemoteException e) {
                            processStatus = false;
                            Log.e(CameraExtensionSessionImpl.TAG, "Extension service does not respond during processing, dropping frame!");
                        }
                        parcelImage.buffer.close();
                        img.close();
                        RepeatingRequestHandler repeatingRequestHandler3 = RepeatingRequestHandler.this;
                        repeatingRequestHandler3.discardPendingRepeatingResults(idx, repeatingRequestHandler3.mPendingResultMap, false);
                        if (RepeatingRequestHandler.this.mClientNotificationsEnabled) {
                            long ident = Binder.clearCallingIdentity();
                            try {
                                if (processStatus) {
                                    RepeatingRequestHandler.this.mExecutor.execute(new Runnable() { // from class: android.hardware.camera2.impl.CameraExtensionSessionImpl$RepeatingRequestHandler$ImageProcessCallback$$ExternalSyntheticLambda0
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            CameraExtensionSessionImpl.RepeatingRequestHandler.ImageProcessCallback.this.lambda$onImageAvailable$0$CameraExtensionSessionImpl$RepeatingRequestHandler$ImageProcessCallback();
                                        }
                                    });
                                } else {
                                    RepeatingRequestHandler.this.mExecutor.execute(new Runnable() { // from class: android.hardware.camera2.impl.CameraExtensionSessionImpl$RepeatingRequestHandler$ImageProcessCallback$$ExternalSyntheticLambda1
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            CameraExtensionSessionImpl.RepeatingRequestHandler.ImageProcessCallback.this.lambda$onImageAvailable$1$CameraExtensionSessionImpl$RepeatingRequestHandler$ImageProcessCallback();
                                        }
                                    });
                                }
                            } finally {
                                Binder.restoreCallingIdentity(ident);
                            }
                        }
                    } catch (Throwable th) {
                        parcelImage.buffer.close();
                        img.close();
                        throw th;
                    }
                } catch (Exception e2) {
                    Log.e(CameraExtensionSessionImpl.TAG, "Failed to detach image!");
                    img.close();
                }
            }

            public /* synthetic */ void lambda$onImageAvailable$0$CameraExtensionSessionImpl$RepeatingRequestHandler$ImageProcessCallback() {
                RepeatingRequestHandler.this.mCallbacks.onCaptureProcessStarted(CameraExtensionSessionImpl.this, RepeatingRequestHandler.this.mClientRequest);
            }

            public /* synthetic */ void lambda$onImageAvailable$1$CameraExtensionSessionImpl$RepeatingRequestHandler$ImageProcessCallback() {
                RepeatingRequestHandler.this.mCallbacks.onCaptureFailed(CameraExtensionSessionImpl.this, RepeatingRequestHandler.this.mClientRequest);
            }
        }
    }

    private static Size findSmallestAspectMatchedSize(List<Size> sizes, Size arSize) {
        if (arSize.getHeight() == 0) {
            throw new IllegalArgumentException("Invalid input aspect ratio");
        }
        float targetAR = arSize.getWidth() / arSize.getHeight();
        Size ret = null;
        Size fallbackSize = null;
        for (Size sz : sizes) {
            if (fallbackSize == null) {
                fallbackSize = sz;
            }
            if (sz.getHeight() > 0 && (ret == null || ret.getWidth() * ret.getHeight() < sz.getWidth() * sz.getHeight())) {
                float currentAR = sz.getWidth() / sz.getHeight();
                if (Math.abs(currentAR - targetAR) <= 0.01f) {
                    ret = sz;
                }
            }
        }
        if (ret == null) {
            Log.e(TAG, "AR matched size not found returning first size in list");
            Size ret2 = fallbackSize;
            return ret2;
        }
        return ret;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ParcelImage initializeParcelImage(Image img) {
        ParcelImage parcelImage = new ParcelImage();
        parcelImage.buffer = img.getHardwareBuffer();
        if (img.getFenceFd() >= 0) {
            try {
                parcelImage.fence = ParcelFileDescriptor.fromFd(img.getFenceFd());
            } catch (IOException e) {
                Log.e(TAG, "Failed to parcel buffer fence!");
            }
        }
        parcelImage.width = img.getWidth();
        parcelImage.height = img.getHeight();
        parcelImage.format = img.getFormat();
        parcelImage.timestamp = img.getTimestamp();
        parcelImage.transform = img.getTransform();
        parcelImage.scalingMode = img.getScalingMode();
        parcelImage.planeCount = img.getPlaneCount();
        parcelImage.crop = img.getCropRect();
        return parcelImage;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static List<CaptureBundle> initializeParcelable(HashMap<Integer, Pair<Image, TotalCaptureResult>> captureMap, Integer jpegOrientation, Byte jpegQuality) {
        ArrayList<CaptureBundle> ret = new ArrayList<>();
        for (Integer stagetId : captureMap.keySet()) {
            Pair<Image, TotalCaptureResult> entry = captureMap.get(stagetId);
            CaptureBundle bundle = new CaptureBundle();
            bundle.stage = stagetId.intValue();
            bundle.captureImage = initializeParcelImage(entry.first);
            bundle.sequenceId = entry.second.getSequenceId();
            bundle.captureResult = entry.second.getNativeMetadata();
            if (jpegOrientation != null) {
                bundle.captureResult.set((CaptureResult.Key<CaptureResult.Key<Integer>>) CaptureResult.JPEG_ORIENTATION, (CaptureResult.Key<Integer>) jpegOrientation);
            }
            if (jpegQuality != null) {
                bundle.captureResult.set((CaptureResult.Key<CaptureResult.Key<Byte>>) CaptureResult.JPEG_QUALITY, (CaptureResult.Key<Byte>) jpegQuality);
            }
            ret.add(bundle);
        }
        return ret;
    }
}
