package android.os;

import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
/* loaded from: classes2.dex */
public final class DynamicEffect extends VibrationEffect implements Parcelable {
    public static final Parcelable.Creator<DynamicEffect> CREATOR = new Parcelable.Creator<DynamicEffect>() { // from class: android.os.DynamicEffect.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public DynamicEffect mo3559createFromParcel(Parcel in) {
            in.readInt();
            return new DynamicEffect(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public DynamicEffect[] mo3560newArray(int size) {
            return new DynamicEffect[size];
        }
    };
    public static final boolean DEBUG = true;
    private static final int PARCEL_TOKEN_DYNAMIC_EFFECT = 100;
    private static final String TAG = "DynamicEffect";
    private long mDuration = 0;
    private int mLooper;
    private int[] mPatternData;
    String mPatternJson;

    public DynamicEffect(Parcel in) {
    }

    public DynamicEffect(String patternJson) {
        this.mPatternJson = new String(patternJson);
    }

    public static DynamicEffect create(String json) {
        if (TextUtils.isEmpty(json)) {
            Log.e(TAG, "empty pattern,do nothing");
            return null;
        }
        DynamicEffect ret = new DynamicEffect(json);
        return ret;
    }

    @Override // android.os.VibrationEffect, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.VibrationEffect
    /* renamed from: resolve */
    public DynamicEffect mo1981resolve(int defaultAmplitude) {
        return this;
    }

    @Override // android.os.VibrationEffect
    /* renamed from: scale */
    public DynamicEffect mo1982scale(float scaleFactor) {
        return this;
    }

    public String getPatternInfo() {
        return this.mPatternJson;
    }

    @Override // android.os.VibrationEffect
    public void validate() {
    }

    public boolean equals(Object o) {
        if (!(o instanceof String)) {
            return false;
        }
        String other = (String) o;
        return this.mPatternJson == other;
    }

    public int hashCode() {
        String str = this.mPatternJson;
        if (str != null) {
            int result = 17 + (str.hashCode() * 37);
            return result;
        }
        return 17;
    }

    @Override // android.os.VibrationEffect
    public long getDuration() {
        return 0L;
    }

    public String toString() {
        return "DynamicEffect{mPatternJson=" + this.mPatternJson + "}";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
    }
}
