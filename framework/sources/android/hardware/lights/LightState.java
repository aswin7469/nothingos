package android.hardware.lights;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
/* loaded from: classes2.dex */
public final class LightState implements Parcelable {
    public static final Parcelable.Creator<LightState> CREATOR = new Parcelable.Creator<LightState>() { // from class: android.hardware.lights.LightState.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public LightState mo3559createFromParcel(Parcel in) {
            return new LightState(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public LightState[] mo3560newArray(int size) {
            return new LightState[size];
        }
    };
    private static final String TAG = "LightState";
    private int mAllWhiteBr;
    private int mBatteryPercent;
    private final int mColor;
    private int[] mExclamationColors;
    private int[] mLedColors;
    private int mMode;
    private int mOffDurationMilliseconds;
    private int mOnDurationMilliseconds;
    private final int mPlayerId;
    private int mShowBatteryPercent;

    @SystemApi
    @Deprecated
    public LightState(int color) {
        this(color, 0);
    }

    public LightState(int color, int playerId) {
        this.mLedColors = new int[5];
        this.mExclamationColors = new int[9];
        this.mColor = color;
        this.mPlayerId = playerId;
    }

    private LightState(int color, int playerId, int mode, int onMS, int offMS, int[] ledColors, int showBatteryPercent, int batteryPercent, int allWhiteBr, int[] exclamationColors) {
        this.mLedColors = new int[5];
        this.mExclamationColors = new int[9];
        this.mColor = color;
        this.mPlayerId = playerId;
        this.mMode = mode;
        this.mOnDurationMilliseconds = onMS;
        this.mOffDurationMilliseconds = offMS;
        this.mLedColors = ledColors;
        this.mShowBatteryPercent = showBatteryPercent;
        this.mBatteryPercent = batteryPercent;
        this.mAllWhiteBr = allWhiteBr;
        this.mExclamationColors = exclamationColors;
    }

    /* loaded from: classes2.dex */
    public static final class Builder {
        private int mAllWhiteBr;
        private int mBatteryPercent;
        private int mMode;
        private int mOffDurationMilliseconds;
        private int mOnDurationMilliseconds;
        private int mShowBatteryPercent;
        private int[] mLedColors = new int[5];
        private int[] mExclamationColors = new int[9];
        private int mValue = 0;
        private boolean mIsForPlayerId = false;

        public Builder setColor(int color) {
            this.mIsForPlayerId = false;
            this.mValue = color;
            return this;
        }

        public Builder setPlayerId(int playerId) {
            this.mIsForPlayerId = true;
            this.mValue = playerId;
            return this;
        }

        public Builder setMode(int mode) {
            this.mMode = mode;
            return this;
        }

        public Builder setOnOffMs(int onMS, int offMS) {
            this.mOnDurationMilliseconds = onMS;
            this.mOffDurationMilliseconds = offMS;
            return this;
        }

        public Builder setLEDColors(int[] colors) {
            if (colors == null || colors.length != this.mLedColors.length) {
                Log.w(LightState.TAG, "Illegal colors array");
                return this;
            }
            this.mLedColors = colors;
            return this;
        }

        public Builder setShowBatteryPercent(int showBatteryPercent) {
            this.mShowBatteryPercent = showBatteryPercent;
            return this;
        }

        public Builder setBatteryPercent(int batteryPercent) {
            this.mBatteryPercent = batteryPercent;
            return this;
        }

        public Builder setAllWhiteBr(int allWhiteBr) {
            this.mAllWhiteBr = allWhiteBr;
            return this;
        }

        public Builder setExclamationColors(int[] exclamationColors) {
            if (exclamationColors == null || exclamationColors.length != this.mExclamationColors.length) {
                Log.w(LightState.TAG, "Illegal exclamationColors colors array");
                return this;
            }
            this.mExclamationColors = exclamationColors;
            return this;
        }

        public LightState build() {
            if (!this.mIsForPlayerId) {
                return new LightState(this.mValue, 0, this.mMode, this.mOnDurationMilliseconds, this.mOffDurationMilliseconds, this.mLedColors, this.mShowBatteryPercent, this.mBatteryPercent, this.mAllWhiteBr, this.mExclamationColors);
            }
            return new LightState(0, this.mValue, this.mMode, this.mOnDurationMilliseconds, this.mOffDurationMilliseconds, this.mLedColors, this.mShowBatteryPercent, this.mBatteryPercent, this.mAllWhiteBr, this.mExclamationColors);
        }
    }

    private LightState(Parcel in) {
        this.mLedColors = new int[5];
        this.mExclamationColors = new int[9];
        this.mColor = in.readInt();
        this.mPlayerId = in.readInt();
        this.mMode = in.readInt();
        this.mOnDurationMilliseconds = in.readInt();
        this.mOffDurationMilliseconds = in.readInt();
        in.readIntArray(this.mLedColors);
        this.mShowBatteryPercent = in.readInt();
        this.mBatteryPercent = in.readInt();
        this.mAllWhiteBr = in.readInt();
        in.readIntArray(this.mExclamationColors);
    }

    public int getColor() {
        return this.mColor;
    }

    public int getPlayerId() {
        return this.mPlayerId;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mColor);
        dest.writeInt(this.mPlayerId);
        dest.writeInt(this.mMode);
        dest.writeInt(this.mOnDurationMilliseconds);
        dest.writeInt(this.mOffDurationMilliseconds);
        dest.writeIntArray(this.mLedColors);
        dest.writeInt(this.mShowBatteryPercent);
        dest.writeInt(this.mBatteryPercent);
        dest.writeInt(this.mAllWhiteBr);
        dest.writeIntArray(this.mExclamationColors);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "LightState{Color=0x" + Integer.toHexString(this.mColor) + ", PlayerId=" + this.mPlayerId + ", Mode=" + this.mMode + ", OnMS=" + this.mOnDurationMilliseconds + ", OffMS=" + this.mOffDurationMilliseconds + ", LedColors " + this.mLedColors + ", ShowBatteryPercent=" + this.mShowBatteryPercent + ", BatteryPercent= " + this.mBatteryPercent + ", allWhiteBr= " + this.mAllWhiteBr + ", ExclamationColors" + this.mExclamationColors + "}";
    }

    public int getMode() {
        return this.mMode;
    }

    public int getOnDurationMilliseconds() {
        return this.mOnDurationMilliseconds;
    }

    public int getOffDurationMilliseconds() {
        return this.mOffDurationMilliseconds;
    }

    public int[] getLedColors() {
        return this.mLedColors;
    }

    public int getShowBatteryPercent() {
        return this.mShowBatteryPercent;
    }

    public int getBatteryPercent() {
        return this.mBatteryPercent;
    }

    public int getAllWhiteBr() {
        return this.mAllWhiteBr;
    }

    public int[] getExclamationColors() {
        return this.mExclamationColors;
    }
}
