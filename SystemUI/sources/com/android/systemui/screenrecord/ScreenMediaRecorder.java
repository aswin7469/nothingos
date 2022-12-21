package com.android.systemui.screenrecord;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.display.VirtualDisplay;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.media.projection.IMediaProjection;
import android.media.projection.IMediaProjectionManager;
import android.media.projection.MediaProjection;
import android.net.Uri;
import android.net.connectivity.com.android.net.module.util.NetworkStackConstants;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.WindowManager;
import java.nio.file.Files;
import java.p026io.File;
import java.p026io.IOException;
import java.p026io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenMediaRecorder {
    private static final int AUDIO_BIT_RATE = 196000;
    private static final int AUDIO_SAMPLE_RATE = 44100;
    private static final int MAX_DURATION_MS = 3600000;
    private static final long MAX_FILESIZE_BYTES = 5000000000L;
    private static final String TAG = "ScreenMediaRecorder";
    private static final int TOTAL_NUM_TRACKS = 1;
    private static final int VIDEO_FRAME_RATE = 30;
    private static final int VIDEO_FRAME_RATE_TO_RESOLUTION_RATIO = 6;
    private ScreenInternalAudioRecorder mAudio;
    private ScreenRecordingAudioSource mAudioSource;
    private Context mContext;
    private Surface mInputSurface;
    MediaRecorder.OnInfoListener mListener;
    private MediaProjection mMediaProjection;
    private MediaRecorder mMediaRecorder;
    private ScreenRecordingMuxer mMuxer;
    private File mTempAudioFile;
    private File mTempVideoFile;
    private int mUser;
    private VirtualDisplay mVirtualDisplay;

    public ScreenMediaRecorder(Context context, int i, ScreenRecordingAudioSource screenRecordingAudioSource, MediaRecorder.OnInfoListener onInfoListener) {
        this.mContext = context;
        this.mUser = i;
        this.mListener = onInfoListener;
        this.mAudioSource = screenRecordingAudioSource;
    }

    private void prepare() throws IOException, RemoteException, RuntimeException {
        int i;
        int i2;
        boolean z = false;
        this.mMediaProjection = new MediaProjection(this.mContext, IMediaProjection.Stub.asInterface(IMediaProjectionManager.Stub.asInterface(ServiceManager.getService("media_projection")).createProjection(this.mUser, this.mContext.getPackageName(), 0, false).asBinder()));
        File cacheDir = this.mContext.getCacheDir();
        cacheDir.mkdirs();
        this.mTempVideoFile = File.createTempFile("temp", ".mp4", cacheDir);
        this.mMediaRecorder = new MediaRecorder();
        if (this.mAudioSource == ScreenRecordingAudioSource.MIC) {
            this.mMediaRecorder.setAudioSource(0);
        }
        this.mMediaRecorder.setVideoSource(2);
        this.mMediaRecorder.setOutputFormat(2);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) this.mContext.getSystemService("window");
        windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
        int[] supportedSize = getSupportedSize(displayMetrics.widthPixels, displayMetrics.heightPixels, (int) windowManager.getDefaultDisplay().getRefreshRate());
        int i3 = supportedSize[0];
        int i4 = supportedSize[1];
        int i5 = supportedSize[2];
        if (i5 > 90) {
            Log.d(TAG, "FrameRate change from " + i5 + " to 30fps");
            i5 = 30;
        }
        float f = ((float) i4) / ((float) i3);
        if (i3 > 1280) {
            i = (int) (((float) NetworkStackConstants.IPV6_MIN_MTU) * f);
            i2 = 1280;
        } else if (i4 > 1280) {
            i2 = (int) (((float) NetworkStackConstants.IPV6_MIN_MTU) / f);
            i = 1280;
        } else {
            i2 = i3;
            i = i4;
        }
        this.mMediaRecorder.setVideoEncoder(2);
        this.mMediaRecorder.setVideoEncodingProfileLevel(8, 256);
        this.mMediaRecorder.setVideoSize(i2, i);
        this.mMediaRecorder.setVideoFrameRate(i5);
        this.mMediaRecorder.setVideoEncodingBitRate((((i2 * i) * i5) / 30) * 6);
        this.mMediaRecorder.setMaxDuration(MAX_DURATION_MS);
        this.mMediaRecorder.setMaxFileSize(MAX_FILESIZE_BYTES);
        if (this.mAudioSource == ScreenRecordingAudioSource.MIC) {
            this.mMediaRecorder.setAudioEncoder(4);
            this.mMediaRecorder.setAudioChannels(1);
            this.mMediaRecorder.setAudioEncodingBitRate(AUDIO_BIT_RATE);
            this.mMediaRecorder.setAudioSamplingRate(AUDIO_SAMPLE_RATE);
        }
        this.mMediaRecorder.setOutputFile(this.mTempVideoFile);
        this.mMediaRecorder.prepare();
        this.mInputSurface = this.mMediaRecorder.getSurface();
        this.mVirtualDisplay = this.mMediaProjection.createVirtualDisplay("Recording Display", i2, i, displayMetrics.densityDpi, 16, this.mInputSurface, (VirtualDisplay.Callback) null, (Handler) null);
        this.mMediaRecorder.setOnInfoListener(this.mListener);
        if (this.mAudioSource == ScreenRecordingAudioSource.INTERNAL || this.mAudioSource == ScreenRecordingAudioSource.MIC_AND_INTERNAL) {
            this.mTempAudioFile = File.createTempFile("temp", ".aac", this.mContext.getCacheDir());
            String absolutePath = this.mTempAudioFile.getAbsolutePath();
            MediaProjection mediaProjection = this.mMediaProjection;
            if (this.mAudioSource == ScreenRecordingAudioSource.MIC_AND_INTERNAL) {
                z = true;
            }
            this.mAudio = new ScreenInternalAudioRecorder(absolutePath, mediaProjection, z);
        }
    }

    private int[] getSupportedSize(int i, int i2, int i3) throws IOException {
        MediaCodec createDecoderByType = MediaCodec.createDecoderByType("video/avc");
        MediaCodecInfo.VideoCapabilities videoCapabilities = createDecoderByType.getCodecInfo().getCapabilitiesForType("video/avc").getVideoCapabilities();
        createDecoderByType.release();
        int intValue = videoCapabilities.getSupportedWidths().getUpper().intValue();
        int intValue2 = videoCapabilities.getSupportedHeights().getUpper().intValue();
        int widthAlignment = i % videoCapabilities.getWidthAlignment() != 0 ? i - (i % videoCapabilities.getWidthAlignment()) : i;
        int heightAlignment = i2 % videoCapabilities.getHeightAlignment() != 0 ? i2 - (i2 % videoCapabilities.getHeightAlignment()) : i2;
        if (intValue < widthAlignment || intValue2 < heightAlignment || !videoCapabilities.isSizeSupported(widthAlignment, heightAlignment)) {
            double d = (double) i;
            double d2 = (double) i2;
            double min = Math.min(((double) intValue) / d, ((double) intValue2) / d2);
            int i4 = (int) (d * min);
            int i5 = (int) (d2 * min);
            if (i4 % videoCapabilities.getWidthAlignment() != 0) {
                i4 -= i4 % videoCapabilities.getWidthAlignment();
            }
            if (i5 % videoCapabilities.getHeightAlignment() != 0) {
                i5 -= i5 % videoCapabilities.getHeightAlignment();
            }
            int intValue3 = videoCapabilities.getSupportedFrameRatesFor(i4, i5).getUpper().intValue();
            if (intValue3 < i3) {
                i3 = intValue3;
            }
            Log.d(TAG, "Resized by " + min + ": " + i4 + ", " + i5 + ", " + i3);
            return new int[]{i4, i5, i3};
        }
        int intValue4 = videoCapabilities.getSupportedFrameRatesFor(widthAlignment, heightAlignment).getUpper().intValue();
        if (intValue4 < i3) {
            i3 = intValue4;
        }
        Log.d(TAG, "Screen size supported at rate " + i3);
        return new int[]{widthAlignment, heightAlignment, i3};
    }

    /* access modifiers changed from: package-private */
    public void start() throws IOException, RemoteException, RuntimeException {
        Log.d(TAG, "start recording");
        prepare();
        this.mMediaRecorder.start();
        recordInternalAudio();
    }

    /* access modifiers changed from: package-private */
    public void end() {
        this.mMediaRecorder.stop();
        this.mMediaRecorder.release();
        this.mInputSurface.release();
        this.mVirtualDisplay.release();
        this.mMediaProjection.stop();
        this.mMediaRecorder = null;
        this.mMediaProjection = null;
        stopInternalAudioRecording();
        Log.d(TAG, "end recording");
    }

    private void stopInternalAudioRecording() {
        if (this.mAudioSource == ScreenRecordingAudioSource.INTERNAL || this.mAudioSource == ScreenRecordingAudioSource.MIC_AND_INTERNAL) {
            this.mAudio.end();
            this.mAudio = null;
        }
    }

    private void recordInternalAudio() throws IllegalStateException {
        if (this.mAudioSource == ScreenRecordingAudioSource.INTERNAL || this.mAudioSource == ScreenRecordingAudioSource.MIC_AND_INTERNAL) {
            this.mAudio.start();
        }
    }

    /* access modifiers changed from: protected */
    public SavedRecording save() throws IOException {
        String format = new SimpleDateFormat("'screen-'yyyyMMdd-HHmmss'.mp4'").format(new Date());
        ContentValues contentValues = new ContentValues();
        contentValues.put("_display_name", format);
        contentValues.put("mime_type", "video/mp4");
        contentValues.put("date_added", Long.valueOf(System.currentTimeMillis()));
        contentValues.put("datetaken", Long.valueOf(System.currentTimeMillis()));
        ContentResolver contentResolver = this.mContext.getContentResolver();
        Uri insert = contentResolver.insert(MediaStore.Video.Media.getContentUri("external_primary"), contentValues);
        Log.d(TAG, insert.toString());
        if (this.mAudioSource == ScreenRecordingAudioSource.MIC_AND_INTERNAL || this.mAudioSource == ScreenRecordingAudioSource.INTERNAL) {
            try {
                Log.d(TAG, "muxing recording");
                File createTempFile = File.createTempFile("temp", ".mp4", this.mContext.getCacheDir());
                ScreenRecordingMuxer screenRecordingMuxer = new ScreenRecordingMuxer(0, createTempFile.getAbsolutePath(), this.mTempVideoFile.getAbsolutePath(), this.mTempAudioFile.getAbsolutePath());
                this.mMuxer = screenRecordingMuxer;
                screenRecordingMuxer.mux();
                this.mTempVideoFile.delete();
                this.mTempVideoFile = createTempFile;
            } catch (IOException e) {
                Log.e(TAG, "muxing recording " + e.getMessage());
                e.printStackTrace();
            }
        }
        OutputStream openOutputStream = contentResolver.openOutputStream(insert, "w");
        Files.copy(this.mTempVideoFile.toPath(), openOutputStream);
        openOutputStream.close();
        File file = this.mTempAudioFile;
        if (file != null) {
            file.delete();
        }
        DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
        SavedRecording savedRecording = new SavedRecording(insert, this.mTempVideoFile, new Size(displayMetrics.widthPixels, displayMetrics.heightPixels));
        this.mTempVideoFile.delete();
        return savedRecording;
    }

    public class SavedRecording {
        private Bitmap mThumbnailBitmap;
        private Uri mUri;

        protected SavedRecording(Uri uri, File file, Size size) {
            this.mUri = uri;
            try {
                this.mThumbnailBitmap = ThumbnailUtils.createVideoThumbnail(file, size, (CancellationSignal) null);
            } catch (IOException e) {
                Log.e(ScreenMediaRecorder.TAG, "Error creating thumbnail", e);
            }
        }

        public Uri getUri() {
            return this.mUri;
        }

        public Bitmap getThumbnail() {
            return this.mThumbnailBitmap;
        }
    }
}
