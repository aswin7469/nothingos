package com.android.systemui.screenrecord;

import android.media.AudioFormat;
import android.media.AudioPlaybackCaptureConfiguration;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.projection.MediaProjection;
import android.util.Log;
import android.util.MathUtils;
import android.view.Surface;
import com.qti.extphone.SignalStrength;
import java.nio.ByteBuffer;
import java.p026io.IOException;
import java.util.Arrays;
import libcore.util.FP16;

public class ScreenInternalAudioRecorder {
    private static final float MIC_VOLUME_SCALE = 1.4f;
    private static String TAG = "ScreenAudioRecorder";
    private static final int TIMEOUT = 500;
    private AudioRecord mAudioRecord;
    private AudioRecord mAudioRecordMic;
    private MediaCodec mCodec;
    private Config mConfig = new Config();
    private MediaProjection mMediaProjection;
    private boolean mMic;
    private MediaMuxer mMuxer;
    private long mPresentationTime;
    private boolean mStarted;
    private Thread mThread;
    private long mTotalBytes;
    private int mTrackId = -1;

    public ScreenInternalAudioRecorder(String str, MediaProjection mediaProjection, boolean z) throws IOException {
        this.mMic = z;
        this.mMuxer = new MediaMuxer(str, 0);
        this.mMediaProjection = mediaProjection;
        Log.d(TAG, "creating audio file " + str);
        setupSimple();
    }

    public static class Config {
        public int bitRate = 196000;
        public int bufferSizeBytes = 131072;
        public int channelInMask = 16;
        public int channelOutMask = 4;
        public int encoding = 2;
        public boolean legacy_app_looback = false;
        public boolean privileged = true;
        public int sampleRate = 44100;

        public String toString() {
            return "channelMask=" + this.channelOutMask + "\n   encoding=" + this.encoding + "\n sampleRate=" + this.sampleRate + "\n bufferSize=" + this.bufferSizeBytes + "\n privileged=" + this.privileged + "\n legacy app looback=" + this.legacy_app_looback;
        }
    }

    private void setupSimple() throws IOException {
        int minBufferSize = AudioRecord.getMinBufferSize(this.mConfig.sampleRate, this.mConfig.channelInMask, this.mConfig.encoding) * 2;
        Log.d(TAG, "audio buffer size: " + minBufferSize);
        AudioFormat build = new AudioFormat.Builder().setEncoding(this.mConfig.encoding).setSampleRate(this.mConfig.sampleRate).setChannelMask(this.mConfig.channelOutMask).build();
        this.mAudioRecord = new AudioRecord.Builder().setAudioFormat(build).setAudioPlaybackCaptureConfig(new AudioPlaybackCaptureConfiguration.Builder(this.mMediaProjection).addMatchingUsage(1).addMatchingUsage(0).addMatchingUsage(14).build()).build();
        if (this.mMic) {
            this.mAudioRecordMic = new AudioRecord(7, this.mConfig.sampleRate, 16, this.mConfig.encoding, minBufferSize);
        }
        this.mCodec = MediaCodec.createEncoderByType("audio/mp4a-latm");
        MediaFormat createAudioFormat = MediaFormat.createAudioFormat("audio/mp4a-latm", this.mConfig.sampleRate, 1);
        createAudioFormat.setInteger("aac-profile", 2);
        createAudioFormat.setInteger("bitrate", this.mConfig.bitRate);
        createAudioFormat.setInteger("pcm-encoding", this.mConfig.encoding);
        this.mCodec.configure(createAudioFormat, (Surface) null, (MediaCrypto) null, 1);
        this.mThread = new Thread((Runnable) new ScreenInternalAudioRecorder$$ExternalSyntheticLambda0(this, minBufferSize));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setupSimple$0$com-android-systemui-screenrecord-ScreenInternalAudioRecorder */
    public /* synthetic */ void mo37265x3197a335(int i) {
        short[] sArr;
        short[] sArr2;
        int i2;
        byte[] bArr = new byte[i];
        if (this.mMic) {
            int i3 = i / 2;
            sArr = new short[i3];
            sArr2 = new short[i3];
        } else {
            sArr = null;
            sArr2 = null;
        }
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        while (true) {
            if (this.mMic) {
                int read = this.mAudioRecord.read(sArr, i4, sArr.length - i4);
                int read2 = this.mAudioRecordMic.read(sArr2, i5, sArr2.length - i5);
                if (read < 0 && read2 < 0) {
                    break;
                }
                if (read < 0) {
                    Arrays.fill(sArr, 0);
                    i4 = i5;
                    read = read2;
                }
                if (read2 < 0) {
                    Arrays.fill(sArr2, 0);
                    i5 = i4;
                    read2 = read;
                }
                i6 = read + i4;
                i7 = read2 + i5;
                int min = Math.min(i6, i7);
                i2 = min * 2;
                scaleValues(sArr2, min, MIC_VOLUME_SCALE);
                addAndConvertBuffers(sArr, sArr2, bArr, min);
                shiftToStart(sArr, min, i4);
                shiftToStart(sArr2, min, i5);
                i4 = i6 - min;
                i5 = i7 - min;
            } else {
                i2 = this.mAudioRecord.read(bArr, 0, i);
            }
            if (i2 < 0) {
                Log.e(TAG, "read error " + i2 + ", shorts internal: " + i6 + ", shorts mic: " + i7);
                break;
            }
            encode(bArr, i2);
        }
        endStream();
    }

    private void shiftToStart(short[] sArr, int i, int i2) {
        for (int i3 = 0; i3 < i2 - i; i3++) {
            sArr[i3] = sArr[i + i3];
        }
    }

    private void scaleValues(short[] sArr, int i, float f) {
        for (int i2 = 0; i2 < i; i2++) {
            sArr[i2] = (short) MathUtils.constrain((int) (((float) sArr[i2]) * f), SignalStrength.INVALID, FP16.EXPONENT_SIGNIFICAND_MASK);
        }
    }

    private void addAndConvertBuffers(short[] sArr, short[] sArr2, byte[] bArr, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            short constrain = (short) MathUtils.constrain(sArr[i2] + sArr2[i2], SignalStrength.INVALID, FP16.EXPONENT_SIGNIFICAND_MASK);
            int i3 = i2 * 2;
            bArr[i3] = (byte) (constrain & 255);
            bArr[i3 + 1] = (byte) ((constrain >> 8) & 255);
        }
    }

    private void encode(byte[] bArr, int i) {
        int i2 = 0;
        while (i > 0) {
            int dequeueInputBuffer = this.mCodec.dequeueInputBuffer(500);
            if (dequeueInputBuffer < 0) {
                writeOutput();
                return;
            }
            ByteBuffer inputBuffer = this.mCodec.getInputBuffer(dequeueInputBuffer);
            inputBuffer.clear();
            int capacity = inputBuffer.capacity();
            int i3 = i > capacity ? capacity : i;
            i -= i3;
            inputBuffer.put(bArr, i2, i3);
            i2 += i3;
            this.mCodec.queueInputBuffer(dequeueInputBuffer, 0, i3, this.mPresentationTime, 0);
            long j = this.mTotalBytes + ((long) (i3 + 0));
            this.mTotalBytes = j;
            this.mPresentationTime = ((j / 2) * 1000000) / ((long) this.mConfig.sampleRate);
            writeOutput();
        }
    }

    private void endStream() {
        this.mCodec.queueInputBuffer(this.mCodec.dequeueInputBuffer(500), 0, 0, this.mPresentationTime, 4);
        writeOutput();
    }

    private void writeOutput() {
        while (true) {
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int dequeueOutputBuffer = this.mCodec.dequeueOutputBuffer(bufferInfo, 500);
            if (dequeueOutputBuffer == -2) {
                this.mTrackId = this.mMuxer.addTrack(this.mCodec.getOutputFormat());
                this.mMuxer.start();
            } else if (dequeueOutputBuffer != -1 && this.mTrackId >= 0) {
                ByteBuffer outputBuffer = this.mCodec.getOutputBuffer(dequeueOutputBuffer);
                if ((bufferInfo.flags & 2) == 0 || bufferInfo.size == 0) {
                    this.mMuxer.writeSampleData(this.mTrackId, outputBuffer, bufferInfo);
                }
                this.mCodec.releaseOutputBuffer(dequeueOutputBuffer, false);
            } else {
                return;
            }
        }
    }

    public synchronized void start() throws IllegalStateException {
        if (!this.mStarted) {
            this.mStarted = true;
            this.mAudioRecord.startRecording();
            if (this.mMic) {
                this.mAudioRecordMic.startRecording();
            }
            Log.d(TAG, "channel count " + this.mAudioRecord.getChannelCount());
            this.mCodec.start();
            if (this.mAudioRecord.getRecordingState() == 3) {
                this.mThread.start();
            } else {
                throw new IllegalStateException("Audio recording failed to start");
            }
        } else if (this.mThread == null) {
            throw new IllegalStateException("Recording stopped and can't restart (single use)");
        } else {
            throw new IllegalStateException("Recording already started");
        }
    }

    public void end() {
        this.mAudioRecord.stop();
        if (this.mMic) {
            this.mAudioRecordMic.stop();
        }
        this.mAudioRecord.release();
        if (this.mMic) {
            this.mAudioRecordMic.release();
        }
        try {
            this.mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.mCodec.stop();
        this.mCodec.release();
        this.mMuxer.stop();
        this.mMuxer.release();
        this.mThread = null;
    }
}
