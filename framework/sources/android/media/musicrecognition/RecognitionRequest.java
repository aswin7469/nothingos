package android.media.musicrecognition;

import android.annotation.SystemApi;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;
@SystemApi
/* loaded from: classes2.dex */
public final class RecognitionRequest implements Parcelable {
    public static final Parcelable.Creator<RecognitionRequest> CREATOR = new Parcelable.Creator<RecognitionRequest>() { // from class: android.media.musicrecognition.RecognitionRequest.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public RecognitionRequest mo3559createFromParcel(Parcel p) {
            return new RecognitionRequest(p);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public RecognitionRequest[] mo3560newArray(int size) {
            return new RecognitionRequest[size];
        }
    };
    private final AudioAttributes mAudioAttributes;
    private final AudioFormat mAudioFormat;
    private final int mCaptureSession;
    private final int mIgnoreBeginningFrames;
    private final int mMaxAudioLengthSeconds;

    private RecognitionRequest(Builder b) {
        AudioAttributes audioAttributes = b.mAudioAttributes;
        Objects.requireNonNull(audioAttributes);
        this.mAudioAttributes = audioAttributes;
        AudioFormat audioFormat = b.mAudioFormat;
        Objects.requireNonNull(audioFormat);
        this.mAudioFormat = audioFormat;
        this.mCaptureSession = b.mCaptureSession;
        this.mMaxAudioLengthSeconds = b.mMaxAudioLengthSeconds;
        this.mIgnoreBeginningFrames = b.mIgnoreBeginningFrames;
    }

    public AudioAttributes getAudioAttributes() {
        return this.mAudioAttributes;
    }

    public AudioFormat getAudioFormat() {
        return this.mAudioFormat;
    }

    public int getCaptureSession() {
        return this.mCaptureSession;
    }

    public int getMaxAudioLengthSeconds() {
        return this.mMaxAudioLengthSeconds;
    }

    public int getIgnoreBeginningFrames() {
        return this.mIgnoreBeginningFrames;
    }

    @SystemApi
    /* loaded from: classes2.dex */
    public static final class Builder {
        private AudioFormat mAudioFormat = new AudioFormat.Builder().setSampleRate(16000).setEncoding(2).build();
        private AudioAttributes mAudioAttributes = new AudioAttributes.Builder().setContentType(2).build();
        private int mCaptureSession = 1;
        private int mMaxAudioLengthSeconds = 24;
        private int mIgnoreBeginningFrames = 0;

        public Builder setAudioAttributes(AudioAttributes audioAttributes) {
            this.mAudioAttributes = audioAttributes;
            return this;
        }

        public Builder setAudioFormat(AudioFormat audioFormat) {
            this.mAudioFormat = audioFormat;
            return this;
        }

        public Builder setCaptureSession(int captureSession) {
            this.mCaptureSession = captureSession;
            return this;
        }

        public Builder setMaxAudioLengthSeconds(int maxAudioLengthSeconds) {
            this.mMaxAudioLengthSeconds = maxAudioLengthSeconds;
            return this;
        }

        public Builder setIgnoreBeginningFrames(int ignoreBeginningFrames) {
            this.mIgnoreBeginningFrames = ignoreBeginningFrames;
            return this;
        }

        public RecognitionRequest build() {
            return new RecognitionRequest(this);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mAudioFormat, flags);
        dest.writeParcelable(this.mAudioAttributes, flags);
        dest.writeInt(this.mCaptureSession);
        dest.writeInt(this.mMaxAudioLengthSeconds);
        dest.writeInt(this.mIgnoreBeginningFrames);
    }

    private RecognitionRequest(Parcel in) {
        this.mAudioFormat = (AudioFormat) in.readParcelable(AudioFormat.class.getClassLoader());
        this.mAudioAttributes = (AudioAttributes) in.readParcelable(AudioAttributes.class.getClassLoader());
        this.mCaptureSession = in.readInt();
        this.mMaxAudioLengthSeconds = in.readInt();
        this.mIgnoreBeginningFrames = in.readInt();
    }
}