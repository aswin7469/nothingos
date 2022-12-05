package androidx.media;

import android.media.AudioAttributes;
import androidx.media.AudioAttributesImpl;
/* loaded from: classes.dex */
public class AudioAttributesImplApi21 implements AudioAttributesImpl {
    public AudioAttributes mAudioAttributes;
    public int mLegacyStreamType;

    public AudioAttributesImplApi21() {
        this.mLegacyStreamType = -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioAttributesImplApi21(AudioAttributes audioAttributes) {
        this(audioAttributes, -1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioAttributesImplApi21(AudioAttributes audioAttributes, int explicitLegacyStream) {
        this.mLegacyStreamType = -1;
        this.mAudioAttributes = audioAttributes;
        this.mLegacyStreamType = explicitLegacyStream;
    }

    public int hashCode() {
        return this.mAudioAttributes.hashCode();
    }

    public boolean equals(Object o) {
        if (!(o instanceof AudioAttributesImplApi21)) {
            return false;
        }
        return this.mAudioAttributes.equals(((AudioAttributesImplApi21) o).mAudioAttributes);
    }

    public String toString() {
        return "AudioAttributesCompat: audioattributes=" + this.mAudioAttributes;
    }

    /* loaded from: classes.dex */
    static class Builder implements AudioAttributesImpl.Builder {
        final AudioAttributes.Builder mFwkBuilder = new AudioAttributes.Builder();

        @Override // androidx.media.AudioAttributesImpl.Builder
        public AudioAttributesImpl build() {
            return new AudioAttributesImplApi21(this.mFwkBuilder.build());
        }

        @Override // androidx.media.AudioAttributesImpl.Builder
        /* renamed from: setLegacyStreamType  reason: collision with other method in class */
        public Builder mo111setLegacyStreamType(int streamType) {
            this.mFwkBuilder.setLegacyStreamType(streamType);
            return this;
        }
    }
}
