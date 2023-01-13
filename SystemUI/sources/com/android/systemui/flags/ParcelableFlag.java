package com.android.systemui.flags;

import android.os.Parcelable;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\bf\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00020\u0003J\b\u0010\u0007\u001a\u00020\bH\u0016R\u0012\u0010\u0004\u001a\u00028\u0000X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\tÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/flags/ParcelableFlag;", "T", "Lcom/android/systemui/flags/Flag;", "Landroid/os/Parcelable;", "default", "getDefault", "()Ljava/lang/Object;", "describeContents", "", "shared_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: Flag.kt */
public interface ParcelableFlag<T> extends Flag<T>, Parcelable {
    int describeContents() {
        return 0;
    }

    T getDefault();
}
