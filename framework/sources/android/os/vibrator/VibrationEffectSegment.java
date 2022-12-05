package android.os.vibrator;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes2.dex */
public abstract class VibrationEffectSegment implements Parcelable {
    public static final Parcelable.Creator<VibrationEffectSegment> CREATOR = new Parcelable.Creator<VibrationEffectSegment>() { // from class: android.os.vibrator.VibrationEffectSegment.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public VibrationEffectSegment mo3559createFromParcel(Parcel in) {
            switch (in.readInt()) {
                case 1:
                    return new PrebakedSegment(in);
                case 2:
                    return new PrimitiveSegment(in);
                case 3:
                    return new StepSegment(in);
                case 4:
                    return new RampSegment(in);
                default:
                    throw new IllegalStateException("Unexpected vibration event type token in parcel.");
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public VibrationEffectSegment[] mo3560newArray(int size) {
            return new VibrationEffectSegment[size];
        }
    };
    static final int PARCEL_TOKEN_PREBAKED = 1;
    static final int PARCEL_TOKEN_PRIMITIVE = 2;
    static final int PARCEL_TOKEN_RAMP = 4;
    static final int PARCEL_TOKEN_STEP = 3;

    /* renamed from: applyEffectStrength */
    public abstract <T extends VibrationEffectSegment> T mo2043applyEffectStrength(int i);

    public abstract long getDuration();

    public abstract boolean hasNonZeroAmplitude();

    /* renamed from: resolve */
    public abstract <T extends VibrationEffectSegment> T mo2044resolve(int i);

    /* renamed from: scale */
    public abstract <T extends VibrationEffectSegment> T mo2045scale(float f);

    public abstract void validate();
}
