package com.android.systemui.flags;

import android.os.Parcel;
import android.os.Parcelable;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0010\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u001d\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0016¢\u0006\u0002\u0010\n¨\u0006\u000b"}, mo65043d2 = {"com/android/systemui/flags/FloatFlag$Companion$CREATOR$1", "Landroid/os/Parcelable$Creator;", "Lcom/android/systemui/flags/FloatFlag;", "createFromParcel", "parcel", "Landroid/os/Parcel;", "newArray", "", "size", "", "(I)[Lcom/android/systemui/flags/FloatFlag;", "shared_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: Flag.kt */
public final class FloatFlag$Companion$CREATOR$1 implements Parcelable.Creator<FloatFlag> {
    FloatFlag$Companion$CREATOR$1() {
    }

    public FloatFlag createFromParcel(Parcel parcel) {
        Intrinsics.checkNotNullParameter(parcel, "parcel");
        return new FloatFlag(parcel, (DefaultConstructorMarker) null);
    }

    public FloatFlag[] newArray(int i) {
        return new FloatFlag[i];
    }
}
