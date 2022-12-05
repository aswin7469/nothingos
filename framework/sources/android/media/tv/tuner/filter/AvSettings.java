package android.media.tv.tuner.filter;

import android.annotation.SystemApi;
import android.media.tv.tuner.TunerUtils;
import android.media.tv.tuner.TunerVersionChecker;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@SystemApi
/* loaded from: classes2.dex */
public class AvSettings extends Settings {
    public static final int AUDIO_STREAM_TYPE_AAC = 6;
    public static final int AUDIO_STREAM_TYPE_AC3 = 7;
    public static final int AUDIO_STREAM_TYPE_AC4 = 9;
    public static final int AUDIO_STREAM_TYPE_DRA = 15;
    public static final int AUDIO_STREAM_TYPE_DTS = 10;
    public static final int AUDIO_STREAM_TYPE_DTS_HD = 11;
    public static final int AUDIO_STREAM_TYPE_EAC3 = 8;
    public static final int AUDIO_STREAM_TYPE_MP3 = 2;
    public static final int AUDIO_STREAM_TYPE_MPEG1 = 3;
    public static final int AUDIO_STREAM_TYPE_MPEG2 = 4;
    public static final int AUDIO_STREAM_TYPE_MPEGH = 5;
    public static final int AUDIO_STREAM_TYPE_OPUS = 13;
    public static final int AUDIO_STREAM_TYPE_PCM = 1;
    public static final int AUDIO_STREAM_TYPE_UNDEFINED = 0;
    public static final int AUDIO_STREAM_TYPE_VORBIS = 14;
    public static final int AUDIO_STREAM_TYPE_WMA = 12;
    public static final int VIDEO_STREAM_TYPE_AV1 = 10;
    public static final int VIDEO_STREAM_TYPE_AVC = 5;
    public static final int VIDEO_STREAM_TYPE_AVS = 11;
    public static final int VIDEO_STREAM_TYPE_AVS2 = 12;
    public static final int VIDEO_STREAM_TYPE_HEVC = 6;
    public static final int VIDEO_STREAM_TYPE_MPEG1 = 2;
    public static final int VIDEO_STREAM_TYPE_MPEG2 = 3;
    public static final int VIDEO_STREAM_TYPE_MPEG4P2 = 4;
    public static final int VIDEO_STREAM_TYPE_RESERVED = 1;
    public static final int VIDEO_STREAM_TYPE_UNDEFINED = 0;
    public static final int VIDEO_STREAM_TYPE_VC1 = 7;
    public static final int VIDEO_STREAM_TYPE_VP8 = 8;
    public static final int VIDEO_STREAM_TYPE_VP9 = 9;
    private int mAudioStreamType;
    private final boolean mIsPassthrough;
    private int mVideoStreamType;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface AudioStreamType {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface VideoStreamType {
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private AvSettings(int mainType, boolean isAudio, boolean isPassthrough, int audioStreamType, int videoStreamType) {
        super(TunerUtils.getFilterSubtype(mainType, r0));
        int i;
        if (isAudio) {
            i = 3;
        } else {
            i = 4;
        }
        this.mAudioStreamType = 0;
        this.mVideoStreamType = 0;
        this.mIsPassthrough = isPassthrough;
        this.mAudioStreamType = audioStreamType;
        this.mVideoStreamType = videoStreamType;
    }

    public boolean isPassthrough() {
        return this.mIsPassthrough;
    }

    public int getAudioStreamType() {
        return this.mAudioStreamType;
    }

    public int getVideoStreamType() {
        return this.mVideoStreamType;
    }

    public static Builder builder(int mainType, boolean isAudio) {
        return new Builder(mainType, isAudio);
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        private int mAudioStreamType;
        private final boolean mIsAudio;
        private boolean mIsPassthrough;
        private final int mMainType;
        private int mVideoStreamType;

        private Builder(int mainType, boolean isAudio) {
            this.mAudioStreamType = 0;
            this.mVideoStreamType = 0;
            this.mMainType = mainType;
            this.mIsAudio = isAudio;
        }

        public Builder setPassthrough(boolean isPassthrough) {
            this.mIsPassthrough = isPassthrough;
            return this;
        }

        public Builder setAudioStreamType(int audioStreamType) {
            if (TunerVersionChecker.checkHigherOrEqualVersionTo(65537, "setAudioStreamType") && this.mIsAudio) {
                this.mAudioStreamType = audioStreamType;
                this.mVideoStreamType = 0;
            }
            return this;
        }

        public Builder setVideoStreamType(int videoStreamType) {
            if (TunerVersionChecker.checkHigherOrEqualVersionTo(65537, "setVideoStreamType") && !this.mIsAudio) {
                this.mVideoStreamType = videoStreamType;
                this.mAudioStreamType = 0;
            }
            return this;
        }

        public AvSettings build() {
            return new AvSettings(this.mMainType, this.mIsAudio, this.mIsPassthrough, this.mAudioStreamType, this.mVideoStreamType);
        }
    }
}