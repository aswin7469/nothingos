package android.app;

import android.annotation.IntRange;
import android.annotation.NonNull;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.util.AnnotationValidations;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SyncNotedAppOp implements Parcelable {
    public static final Parcelable.Creator<SyncNotedAppOp> CREATOR = new Parcelable.Creator<SyncNotedAppOp>() { // from class: android.app.SyncNotedAppOp.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public SyncNotedAppOp[] mo3560newArray(int size) {
            return new SyncNotedAppOp[size];
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public SyncNotedAppOp mo3559createFromParcel(Parcel in) {
            return new SyncNotedAppOp(in);
        }
    };
    private final String mAttributionTag;
    private final int mOpCode;
    private final int mOpMode;
    private final String mPackageName;

    public SyncNotedAppOp(int opMode, int opCode, String attributionTag, String packageName) {
        this.mOpCode = opCode;
        AnnotationValidations.validate((Class<IntRange>) IntRange.class, (IntRange) null, opCode, "from", 0L, "to", 115L);
        this.mAttributionTag = attributionTag;
        this.mOpMode = opMode;
        this.mPackageName = packageName;
    }

    public SyncNotedAppOp(int opCode, String attributionTag) {
        this(1, opCode, attributionTag, ActivityThread.currentPackageName());
    }

    public SyncNotedAppOp(int opCode, String attributionTag, String packageName) {
        this(1, opCode, attributionTag, packageName);
    }

    public String getOp() {
        return AppOpsManager.opToPublicName(this.mOpCode);
    }

    public int getOpMode() {
        return this.mOpMode;
    }

    public String getAttributionTag() {
        return this.mAttributionTag;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SyncNotedAppOp that = (SyncNotedAppOp) o;
        if (this.mOpMode == that.mOpMode && this.mOpCode == that.mOpCode && Objects.equals(this.mAttributionTag, that.mAttributionTag) && Objects.equals(this.mPackageName, that.mPackageName)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int _hash = (1 * 31) + this.mOpMode;
        return (((((_hash * 31) + this.mOpCode) * 31) + Objects.hashCode(this.mAttributionTag)) * 31) + Objects.hashCode(this.mPackageName);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        byte flg = 0;
        if (this.mAttributionTag != null) {
            flg = (byte) (0 | 4);
        }
        dest.writeByte(flg);
        dest.writeInt(this.mOpMode);
        dest.writeInt(this.mOpCode);
        String str = this.mAttributionTag;
        if (str != null) {
            dest.writeString(str);
        }
        dest.writeString(this.mPackageName);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    SyncNotedAppOp(Parcel in) {
        byte flg = in.readByte();
        int opMode = in.readInt();
        int opCode = in.readInt();
        String attributionTag = (flg & 4) == 0 ? null : in.readString();
        String packageName = in.readString();
        this.mOpMode = opMode;
        this.mOpCode = opCode;
        AnnotationValidations.validate((Class<IntRange>) IntRange.class, (IntRange) null, opCode, "from", 0L, "to", 115L);
        this.mAttributionTag = attributionTag;
        this.mPackageName = packageName;
        AnnotationValidations.validate((Class<NonNull>) NonNull.class, (NonNull) null, (Object) packageName);
    }

    @Deprecated
    private void __metadata() {
    }
}
