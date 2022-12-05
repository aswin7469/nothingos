package com.sysaac.haptic.sync;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes2.dex */
final class c implements Parcelable.Creator<b> {
    @Override // android.os.Parcelable.Creator
    /* renamed from: a */
    public b createFromParcel(Parcel parcel) {
        return new b(parcel);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: a */
    public b[] newArray(int i) {
        return new b[i];
    }
}
