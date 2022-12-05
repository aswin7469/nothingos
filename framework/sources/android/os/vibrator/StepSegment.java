package android.os.vibrator;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.VibrationEffect;
import com.android.internal.util.Preconditions;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class StepSegment extends VibrationEffectSegment {
    public static final Parcelable.Creator<StepSegment> CREATOR = new Parcelable.Creator<StepSegment>() { // from class: android.os.vibrator.StepSegment.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public StepSegment mo3559createFromParcel(Parcel in) {
            in.readInt();
            return new StepSegment(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public StepSegment[] mo3560newArray(int size) {
            return new StepSegment[size];
        }
    };
    private final float mAmplitude;
    private final int mDuration;
    private final float mFrequency;

    /* JADX INFO: Access modifiers changed from: package-private */
    public StepSegment(Parcel in) {
        this(in.readFloat(), in.readFloat(), in.readInt());
    }

    public StepSegment(float amplitude, float frequency, int duration) {
        this.mAmplitude = amplitude;
        this.mFrequency = frequency;
        this.mDuration = duration;
    }

    public boolean equals(Object o) {
        if (!(o instanceof StepSegment)) {
            return false;
        }
        StepSegment other = (StepSegment) o;
        return Float.compare(this.mAmplitude, other.mAmplitude) == 0 && Float.compare(this.mFrequency, other.mFrequency) == 0 && this.mDuration == other.mDuration;
    }

    public float getAmplitude() {
        return this.mAmplitude;
    }

    public float getFrequency() {
        return this.mFrequency;
    }

    @Override // android.os.vibrator.VibrationEffectSegment
    public long getDuration() {
        return this.mDuration;
    }

    @Override // android.os.vibrator.VibrationEffectSegment
    public boolean hasNonZeroAmplitude() {
        return Float.compare(this.mAmplitude, 0.0f) != 0;
    }

    @Override // android.os.vibrator.VibrationEffectSegment
    public void validate() {
        int i = this.mDuration;
        Preconditions.checkArgumentNonnegative(i, "Durations must all be >= 0, got " + this.mDuration);
        if (Float.compare(this.mAmplitude, -1.0f) != 0) {
            Preconditions.checkArgumentInRange(this.mAmplitude, 0.0f, 1.0f, "amplitude");
        }
    }

    @Override // android.os.vibrator.VibrationEffectSegment
    /* renamed from: resolve */
    public StepSegment mo2044resolve(int defaultAmplitude) {
        if (defaultAmplitude > 255 || defaultAmplitude <= 0) {
            throw new IllegalArgumentException("amplitude must be between 1 and 255 inclusive (amplitude=" + defaultAmplitude + ")");
        } else if (Float.compare(this.mAmplitude, -1.0f) != 0) {
            return this;
        } else {
            return new StepSegment(defaultAmplitude / 255.0f, this.mFrequency, this.mDuration);
        }
    }

    @Override // android.os.vibrator.VibrationEffectSegment
    /* renamed from: scale */
    public StepSegment mo2045scale(float scaleFactor) {
        if (Float.compare(this.mAmplitude, -1.0f) == 0) {
            return this;
        }
        return new StepSegment(VibrationEffect.scale(this.mAmplitude, scaleFactor), this.mFrequency, this.mDuration);
    }

    @Override // android.os.vibrator.VibrationEffectSegment
    /* renamed from: applyEffectStrength */
    public StepSegment mo2043applyEffectStrength(int effectStrength) {
        return this;
    }

    public int hashCode() {
        return Objects.hash(Float.valueOf(this.mAmplitude), Float.valueOf(this.mFrequency), Integer.valueOf(this.mDuration));
    }

    public String toString() {
        return "Step{amplitude=" + this.mAmplitude + ", frequency=" + this.mFrequency + ", duration=" + this.mDuration + "}";
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(3);
        out.writeFloat(this.mAmplitude);
        out.writeFloat(this.mFrequency);
        out.writeInt(this.mDuration);
    }
}
