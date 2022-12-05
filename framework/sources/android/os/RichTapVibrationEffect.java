package android.os;

import android.os.Parcelable;
import android.util.Log;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes2.dex */
public class RichTapVibrationEffect {
    private static final int AAC_CLIENT = 16711680;
    private static final int EFFECT_ID_START = 4096;
    private static final int HONOR_CLIENT = 327680;
    private static final int LENOVO_CLIENT = 393216;
    private static final int MAJOR_RICHTAP_VERSION = 6144;
    private static final int MINOR_RICHTAP_VERSION = 16;
    private static final int MI_CLIENT = 196608;
    private static final int NT_CLIENT = 524288;
    private static final int ONEPLUS_CLIENT = 131072;
    private static final int OPPO_CLIENT = 65536;
    private static final int PARCEL_TOKEN_ENVELOPE = 502;
    private static final int PARCEL_TOKEN_EXT_PREBAKED = 501;
    private static final int PARCEL_TOKEN_PATTERN_HE = 503;
    private static final int PARCEL_TOKEN_PATTERN_HE_LOOP_PARAMETER = 504;
    private static final String TAG = "RichTapVibrationEffect";
    private static final int VIBRATION_EFFECT_SUPPORT_NO = 2;
    private static final int VIBRATION_EFFECT_SUPPORT_UNKNOWN = 0;
    private static final int VIBRATION_EFFECT_SUPPORT_YES = 1;
    private static final int VIVO_CLIENT = 262144;
    private static final int ZTE_CLIENT = 458752;
    private static Map<String, Integer> effectStrength;
    private static Map<String, Integer> commonEffects = new HashMap();
    private static String DEFAULT_EXT_PREBAKED_STRENGTH = "STRONG";
    public static final Parcelable.Creator<VibrationEffect> CREATOR = new Parcelable.Creator<VibrationEffect>() { // from class: android.os.RichTapVibrationEffect.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public VibrationEffect mo3559createFromParcel(Parcel in) {
            int token = in.readInt();
            Log.d(RichTapVibrationEffect.TAG, "read token: " + token + "!");
            switch (token) {
                case 501:
                    return new ExtPrebaked(in);
                case 502:
                    return new Envelope(in);
                case 503:
                    return new PatternHe(in);
                case 504:
                    return new PatternHeParameter(in);
                default:
                    throw new IllegalStateException("Unexpected vibration event type token in parcel.");
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public VibrationEffect[] mo3560newArray(int size) {
            return new VibrationEffect[size];
        }
    };

    static {
        HashMap hashMap = new HashMap();
        effectStrength = hashMap;
        hashMap.put("LIGHT", 0);
        effectStrength.put("MEDIUM", 1);
        effectStrength.put("STRONG", 2);
    }

    private RichTapVibrationEffect() {
    }

    public static VibrationEffect createExtPreBaked(int effectId, int strength) {
        effectStrength.get(DEFAULT_EXT_PREBAKED_STRENGTH).intValue();
        VibrationEffect effect = new ExtPrebaked(effectId + 4096, strength);
        effect.validate();
        return effect;
    }

    public static VibrationEffect createEnvelope(int[] relativeTimeArr, int[] scaleArr, int[] freqArr, boolean steepMode, int amplitude) {
        VibrationEffect effect = new Envelope(relativeTimeArr, scaleArr, freqArr, steepMode, amplitude);
        effect.validate();
        return effect;
    }

    public static VibrationEffect createPatternHeParameter(int interval, int amplitude, int freq) {
        VibrationEffect effect = new PatternHeParameter(interval, amplitude, freq);
        effect.validate();
        return effect;
    }

    public static VibrationEffect createPatternHeWithParam(int[] patternInfo, int looper, int interval, int amplitude, int freq) {
        VibrationEffect effect = new PatternHe(patternInfo, looper, interval, amplitude, freq);
        effect.validate();
        return effect;
    }

    public static int checkIfRichTapSupport() {
        return 16717840;
    }

    /* loaded from: classes2.dex */
    public static final class ExtPrebaked extends VibrationEffect implements Parcelable {
        public static final Parcelable.Creator<ExtPrebaked> CREATOR = new Parcelable.Creator<ExtPrebaked>() { // from class: android.os.RichTapVibrationEffect.ExtPrebaked.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public ExtPrebaked mo3559createFromParcel(Parcel in) {
                in.readInt();
                return new ExtPrebaked(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public ExtPrebaked[] mo3560newArray(int size) {
                return new ExtPrebaked[size];
            }
        };
        private int mEffectId;
        private int mStrength;

        public ExtPrebaked(Parcel in) {
            this(in.readInt(), in.readInt());
        }

        public ExtPrebaked(int effectId, int strength) {
            this.mEffectId = effectId;
            this.mStrength = strength;
        }

        public int getId() {
            return this.mEffectId;
        }

        public int getScale() {
            return this.mStrength;
        }

        @Override // android.os.VibrationEffect, android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.VibrationEffect
        /* renamed from: resolve */
        public ExtPrebaked mo1981resolve(int defaultAmplitude) {
            return this;
        }

        @Override // android.os.VibrationEffect
        /* renamed from: scale */
        public ExtPrebaked mo1982scale(float scaleFactor) {
            return this;
        }

        @Override // android.os.VibrationEffect
        public long getDuration() {
            return -1L;
        }

        @Override // android.os.VibrationEffect
        public void validate() {
            if (this.mEffectId < 0) {
                throw new IllegalArgumentException("Unknown ExtPrebaked effect type (value=" + this.mEffectId + ")");
            }
            int i = this.mStrength;
            if (i < 1 || i > 100) {
                throw new IllegalArgumentException("mStrength must be between 1 and 100 inclusive (mStrength=" + this.mStrength + ")");
            }
        }

        public boolean equals(Object o) {
            if (!(o instanceof ExtPrebaked)) {
                return false;
            }
            ExtPrebaked other = (ExtPrebaked) o;
            return this.mEffectId == other.mEffectId;
        }

        public int hashCode() {
            return this.mEffectId;
        }

        public String toString() {
            return "ExtPrebaked{mEffectId=" + this.mEffectId + "mStrength = " + this.mStrength + "}";
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(501);
            out.writeInt(this.mEffectId);
            out.writeInt(this.mStrength);
        }
    }

    /* loaded from: classes2.dex */
    public static final class Envelope extends VibrationEffect implements Parcelable {
        public static final Parcelable.Creator<Envelope> CREATOR = new Parcelable.Creator<Envelope>() { // from class: android.os.RichTapVibrationEffect.Envelope.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public Envelope mo3559createFromParcel(Parcel in) {
                in.readInt();
                return new Envelope(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public Envelope[] mo3560newArray(int size) {
                return new Envelope[size];
            }
        };
        private int amplitude;
        private int[] freqArr;
        private int[] relativeTimeArr;
        private int[] scaleArr;
        private boolean steepMode;

        public Envelope(Parcel in) {
            this(in.createIntArray(), in.createIntArray(), in.createIntArray(), in.readInt() != 1 ? false : true, in.readInt());
        }

        public Envelope(int[] relativeTimeArr, int[] scaleArr, int[] freqArr, boolean steepMode, int amplitude) {
            this.relativeTimeArr = Arrays.copyOf(relativeTimeArr, 4);
            this.scaleArr = Arrays.copyOf(scaleArr, 4);
            this.freqArr = Arrays.copyOf(freqArr, 4);
            this.steepMode = steepMode;
            this.amplitude = amplitude;
        }

        public int[] getRelativeTimeArr() {
            return this.relativeTimeArr;
        }

        public int[] getScaleArr() {
            return this.scaleArr;
        }

        public int[] getFreqArr() {
            return this.freqArr;
        }

        public boolean isSteepMode() {
            return this.steepMode;
        }

        public int getAmplitude() {
            return this.amplitude;
        }

        @Override // android.os.VibrationEffect, android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.VibrationEffect
        /* renamed from: resolve */
        public Envelope mo1981resolve(int defaultAmplitude) {
            return this;
        }

        @Override // android.os.VibrationEffect
        /* renamed from: scale */
        public Envelope mo1982scale(float scaleFactor) {
            return this;
        }

        @Override // android.os.VibrationEffect
        public long getDuration() {
            return -1L;
        }

        @Override // android.os.VibrationEffect
        public void validate() {
            for (int i = 0; i < 4; i++) {
                if (this.relativeTimeArr[i] < 0) {
                    throw new IllegalArgumentException("relative time can not be negative");
                }
                if (this.scaleArr[i] < 0) {
                    throw new IllegalArgumentException("scale can not be negative");
                }
                if (this.freqArr[i] < 0) {
                    throw new IllegalArgumentException("freq must be positive");
                }
            }
            int i2 = this.amplitude;
            if (i2 < -1 || i2 == 0 || i2 > 255) {
                throw new IllegalArgumentException("amplitude must either be DEFAULT_AMPLITUDE, or between 1 and 255 inclusive (amplitude=" + this.amplitude + ")");
            }
        }

        public boolean equals(Object o) {
            if (!(o instanceof Envelope)) {
                return false;
            }
            Envelope other = (Envelope) o;
            int[] timeArr = other.getRelativeTimeArr();
            int[] scArr = other.getScaleArr();
            int[] frArr = other.getFreqArr();
            return this.amplitude == other.getAmplitude() && Arrays.equals(timeArr, this.relativeTimeArr) && Arrays.equals(scArr, this.scaleArr) && Arrays.equals(frArr, this.freqArr) && other.isSteepMode() == this.steepMode;
        }

        public int hashCode() {
            return this.relativeTimeArr[2] + this.scaleArr[2] + this.freqArr[2];
        }

        public String toString() {
            return "Envelope: {relativeTimeArr=" + this.relativeTimeArr + ", scaleArr = " + this.scaleArr + ", freqArr = " + this.freqArr + ", SteepMode = " + this.steepMode + ", Amplitude = " + this.amplitude + "}";
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(502);
            out.writeIntArray(this.relativeTimeArr);
            out.writeIntArray(this.scaleArr);
            out.writeIntArray(this.freqArr);
            out.writeInt(this.steepMode ? 1 : 0);
            out.writeInt(this.amplitude);
        }
    }

    /* loaded from: classes2.dex */
    public static final class PatternHeParameter extends VibrationEffect implements Parcelable {
        public static final Parcelable.Creator<PatternHeParameter> CREATOR = new Parcelable.Creator<PatternHeParameter>() { // from class: android.os.RichTapVibrationEffect.PatternHeParameter.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public PatternHeParameter mo3559createFromParcel(Parcel in) {
                in.readInt();
                return new PatternHeParameter(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public PatternHeParameter[] mo3560newArray(int size) {
                return new PatternHeParameter[size];
            }
        };
        private final String TAG = "PatternHeParameter";
        private int mAmplitude;
        private int mFreq;
        private int mInterval;

        public PatternHeParameter(Parcel in) {
            this.mInterval = in.readInt();
            this.mAmplitude = in.readInt();
            this.mFreq = in.readInt();
            Log.d("PatternHeParameter", "parcel mInterval:" + this.mInterval + " mAmplitude:" + this.mAmplitude + " mFreq:" + this.mFreq);
        }

        public PatternHeParameter(int interval, int amplitude, int freq) {
            this.mInterval = interval;
            this.mAmplitude = amplitude;
            this.mFreq = freq;
            Log.d("PatternHeParameter", "mInterval:" + this.mInterval + " mAmplitude:" + this.mAmplitude + " mFreq:" + this.mFreq);
        }

        public int getInterval() {
            return this.mInterval;
        }

        public int getAmplitude() {
            return this.mAmplitude;
        }

        public int getFreq() {
            return this.mFreq;
        }

        @Override // android.os.VibrationEffect, android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.VibrationEffect
        /* renamed from: resolve */
        public PatternHeParameter mo1981resolve(int defaultAmplitude) {
            return this;
        }

        @Override // android.os.VibrationEffect
        /* renamed from: scale */
        public PatternHeParameter mo1982scale(float scaleFactor) {
            return this;
        }

        @Override // android.os.VibrationEffect
        public long getDuration() {
            return -1L;
        }

        @Override // android.os.VibrationEffect
        public void validate() {
            int i = this.mAmplitude;
            if (i < -1 || i > 255 || this.mInterval < -1 || this.mFreq < -1) {
                throw new IllegalArgumentException("mAmplitude=" + this.mAmplitude + " mInterval=" + this.mInterval + " mFreq=" + this.mFreq);
            }
        }

        public boolean equals(Object o) {
            if (!(o instanceof PatternHeParameter)) {
                return false;
            }
            PatternHeParameter other = (PatternHeParameter) o;
            int interval = other.getInterval();
            int amplitude = other.getAmplitude();
            int freq = other.getFreq();
            return interval == this.mInterval && amplitude == this.mAmplitude && freq == this.mFreq;
        }

        public int hashCode() {
            int result = 14 + (this.mInterval * 37);
            return result + (this.mAmplitude * 37);
        }

        public String toString() {
            return "PatternHeParameter: {mAmplitude=" + this.mAmplitude + "}{mInterval=" + this.mInterval + "}";
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(504);
            out.writeInt(this.mInterval);
            out.writeInt(this.mAmplitude);
            out.writeInt(this.mFreq);
            Log.d("PatternHeParameter", "writeToParcel mInterval:" + this.mInterval + " mAmplitude:" + this.mAmplitude + " mFreq:" + this.mFreq);
        }
    }

    /* loaded from: classes2.dex */
    public static final class PatternHe extends VibrationEffect implements Parcelable {
        public static final Parcelable.Creator<PatternHe> CREATOR = new Parcelable.Creator<PatternHe>() { // from class: android.os.RichTapVibrationEffect.PatternHe.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public PatternHe mo3559createFromParcel(Parcel in) {
                in.readInt();
                return new PatternHe(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public PatternHe[] mo3560newArray(int size) {
                return new PatternHe[size];
            }
        };
        private int mAmplitude;
        private long mDuration;
        private int mEventCount;
        private int mFreq;
        private int mInterval;
        private int mLooper;
        private int[] mPatternInfo;

        public PatternHe(Parcel in) {
            this.mDuration = 100L;
            this.mPatternInfo = in.createIntArray();
            this.mLooper = in.readInt();
            this.mInterval = in.readInt();
            this.mAmplitude = in.readInt();
            this.mFreq = in.readInt();
        }

        public PatternHe(int[] patternInfo, long duration, int eventCount) {
            this.mDuration = 100L;
            this.mPatternInfo = patternInfo;
            this.mDuration = duration;
            this.mEventCount = eventCount;
        }

        public PatternHe(int[] patternInfo, int looper, int interval, int amplitude, int freq) {
            this.mDuration = 100L;
            this.mPatternInfo = patternInfo;
            this.mLooper = looper;
            this.mInterval = interval;
            this.mFreq = freq;
            this.mAmplitude = amplitude;
            this.mDuration = 100L;
            this.mEventCount = 0;
        }

        @Override // android.os.VibrationEffect
        public long getDuration() {
            return this.mDuration;
        }

        public int getEventCount() {
            return this.mEventCount;
        }

        @Override // android.os.VibrationEffect, android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.VibrationEffect
        /* renamed from: resolve */
        public PatternHe mo1981resolve(int defaultAmplitude) {
            return this;
        }

        @Override // android.os.VibrationEffect
        /* renamed from: scale */
        public PatternHe mo1982scale(float scaleFactor) {
            return this;
        }

        public int[] getPatternInfo() {
            return this.mPatternInfo;
        }

        public int getLooper() {
            return this.mLooper;
        }

        public int getInterval() {
            return this.mInterval;
        }

        public int getAmplitude() {
            return this.mAmplitude;
        }

        public int getFreq() {
            return this.mFreq;
        }

        @Override // android.os.VibrationEffect
        public void validate() {
            if (this.mDuration <= 0) {
                throw new IllegalArgumentException("duration must be positive (duration=" + this.mDuration + ")");
            }
        }

        public boolean equals(Object o) {
            if (!(o instanceof PatternHe)) {
                return false;
            }
            PatternHe other = (PatternHe) o;
            return other.mDuration == this.mDuration && other.mPatternInfo == this.mPatternInfo;
        }

        public int hashCode() {
            int result = 17 + (((int) this.mDuration) * 37);
            return result + (this.mEventCount * 37);
        }

        public String toString() {
            return "PatternHe{mLooper=" + this.mLooper + ", mInterval=" + this.mInterval + "}";
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(503);
            out.writeIntArray(this.mPatternInfo);
            out.writeInt(this.mLooper);
            out.writeInt(this.mInterval);
            out.writeInt(this.mAmplitude);
            out.writeInt(this.mFreq);
        }
    }

    public static final boolean isExtendedEffect(int token) {
        switch (token) {
            case 501:
            case 502:
            case 503:
            case 504:
                return true;
            default:
                return false;
        }
    }

    public static final VibrationEffect createExtendedEffect(Parcel in) {
        int offset = in.dataPosition() - 4;
        in.setDataPosition(offset);
        return CREATOR.mo3559createFromParcel(in);
    }
}
