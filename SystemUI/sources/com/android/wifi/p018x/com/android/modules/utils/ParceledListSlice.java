package com.android.wifi.p018x.com.android.modules.utils;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Collections;
import java.util.List;

/* renamed from: com.android.wifi.x.com.android.modules.utils.ParceledListSlice */
public class ParceledListSlice<T extends Parcelable> extends BaseParceledListSlice<T> {
    public static final Parcelable.ClassLoaderCreator<ParceledListSlice> CREATOR = new Parcelable.ClassLoaderCreator<ParceledListSlice>() {
        public ParceledListSlice createFromParcel(Parcel parcel) {
            return new ParceledListSlice(parcel, (ClassLoader) null);
        }

        public ParceledListSlice createFromParcel(Parcel parcel, ClassLoader classLoader) {
            return new ParceledListSlice(parcel, classLoader);
        }

        public ParceledListSlice[] newArray(int i) {
            return new ParceledListSlice[i];
        }
    };

    public /* bridge */ /* synthetic */ List getList() {
        return super.getList();
    }

    public /* bridge */ /* synthetic */ void setInlineCountLimit(int i) {
        super.setInlineCountLimit(i);
    }

    public /* bridge */ /* synthetic */ void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
    }

    public ParceledListSlice(List<T> list) {
        super(list);
    }

    private ParceledListSlice(Parcel parcel, ClassLoader classLoader) {
        super(parcel, classLoader);
    }

    public static <T extends Parcelable> ParceledListSlice<T> emptyList() {
        return new ParceledListSlice<>(Collections.emptyList());
    }

    public int describeContents() {
        List list = getList();
        int i = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            i |= ((Parcelable) list.get(i2)).describeContents();
        }
        return i;
    }

    /* access modifiers changed from: protected */
    public void writeElement(T t, Parcel parcel, int i) {
        t.writeToParcel(parcel, i);
    }

    /* access modifiers changed from: protected */
    public void writeParcelableCreator(T t, Parcel parcel) {
        parcel.writeParcelableCreator(t);
    }

    /* access modifiers changed from: protected */
    public Parcelable.Creator<?> readParcelableCreator(Parcel parcel, ClassLoader classLoader) {
        return parcel.readParcelableCreator(classLoader);
    }
}
