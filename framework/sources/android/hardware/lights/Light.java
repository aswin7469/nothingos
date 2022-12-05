package android.hardware.lights;

import android.os.Parcel;
import android.os.Parcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes2.dex */
public final class Light implements Parcelable {
    public static final Parcelable.Creator<Light> CREATOR = new Parcelable.Creator<Light>() { // from class: android.hardware.lights.Light.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public Light mo3559createFromParcel(Parcel in) {
            return new Light(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public Light[] mo3560newArray(int size) {
            return new Light[size];
        }
    };
    public static final int LIGHT_CAPABILITY_BRIGHTNESS = 1;
    public static final int LIGHT_CAPABILITY_RGB = 0;
    public static final int LIGHT_ID_BATTERY = 100;
    public static final int LIGHT_ID_CALL = 103;
    public static final int LIGHT_ID_CAMERA = 106;
    public static final int LIGHT_ID_FLIP = 108;
    public static final int LIGHT_ID_MUSIC = 107;
    public static final int LIGHT_ID_NOTIFICATIONS = 102;
    public static final int LIGHT_ID_SETTING = 109;
    public static final int LIGHT_ID_VIDEO = 105;
    public static final int LIGHT_ID_VOICE_ASSISTANT = 104;
    public static final int LIGHT_ID_WLR_BATTERY = 101;
    public static final int LIGHT_TYPE_INPUT = 10001;
    public static final int LIGHT_TYPE_MICROPHONE = 8;
    public static final int LIGHT_TYPE_PLAYER_ID = 10002;
    public static final int SESSION_PRIORITY_BASE = 10000;
    public static final int SESSION_PRIORITY_BATTERY = 10005;
    public static final int SESSION_PRIORITY_CALL = 10012;
    public static final int SESSION_PRIORITY_CALL_PREVIEW = 10011;
    public static final int SESSION_PRIORITY_CAMERA = 10013;
    public static final int SESSION_PRIORITY_FLIP = 10008;
    public static final int SESSION_PRIORITY_KEYBOARD = 10003;
    public static final int SESSION_PRIORITY_MUSIC = 10004;
    public static final int SESSION_PRIORITY_NOTIFICATIONS = 10010;
    public static final int SESSION_PRIORITY_NOTIFICATIONS_PREVIEW = 10009;
    public static final int SESSION_PRIORITY_RUN = 10001;
    public static final int SESSION_PRIORITY_VIDEO = 10002;
    public static final int SESSION_PRIORITY_VOICE_ASSISTANT = 10007;
    public static final int SESSION_PRIORITY_WLR_BATTERY = 10006;
    private final int mCapabilities;
    private final int mId;
    private final String mName;
    private final int mOrdinal;
    private final int mType;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface LightCapability {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface LightType {
    }

    public Light(int id, int ordinal, int type) {
        this(id, "Light", ordinal, type, 0);
    }

    public Light(int id, String name, int ordinal, int type, int capabilities) {
        this.mId = id;
        this.mName = name;
        this.mOrdinal = ordinal;
        this.mType = type;
        this.mCapabilities = capabilities;
    }

    private Light(Parcel in) {
        this.mId = in.readInt();
        this.mName = in.readString();
        this.mOrdinal = in.readInt();
        this.mType = in.readInt();
        this.mCapabilities = in.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mName);
        dest.writeInt(this.mOrdinal);
        dest.writeInt(this.mType);
        dest.writeInt(this.mCapabilities);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Light) {
            Light light = (Light) obj;
            return this.mId == light.mId && this.mOrdinal == light.mOrdinal && this.mType == light.mType && this.mCapabilities == light.mCapabilities;
        }
        return false;
    }

    public int hashCode() {
        return this.mId;
    }

    public String toString() {
        return "[Name=" + this.mName + " Id=" + this.mId + " Type=" + this.mType + " Capabilities=" + this.mCapabilities + " Ordinal=" + this.mOrdinal + "]";
    }

    public int getId() {
        return this.mId;
    }

    public String getName() {
        return this.mName;
    }

    public int getOrdinal() {
        return this.mOrdinal;
    }

    public int getType() {
        return this.mType;
    }

    public int getCapabilities() {
        return this.mCapabilities;
    }

    public boolean hasBrightnessControl() {
        return (this.mCapabilities & 1) == 1;
    }

    public boolean hasRgbControl() {
        return (this.mCapabilities & 0) == 0;
    }
}
