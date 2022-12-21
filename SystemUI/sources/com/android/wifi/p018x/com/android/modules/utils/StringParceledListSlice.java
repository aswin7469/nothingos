package com.android.wifi.p018x.com.android.modules.utils;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Collections;
import java.util.List;

/* renamed from: com.android.wifi.x.com.android.modules.utils.StringParceledListSlice */
public class StringParceledListSlice extends BaseParceledListSlice<String> {
    public static final Parcelable.ClassLoaderCreator<StringParceledListSlice> CREATOR = new Parcelable.ClassLoaderCreator<StringParceledListSlice>() {
        public StringParceledListSlice createFromParcel(Parcel parcel) {
            return new StringParceledListSlice(parcel, (ClassLoader) null);
        }

        public StringParceledListSlice createFromParcel(Parcel parcel, ClassLoader classLoader) {
            return new StringParceledListSlice(parcel, classLoader);
        }

        public StringParceledListSlice[] newArray(int i) {
            return new StringParceledListSlice[i];
        }
    };

    public int describeContents() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public void writeParcelableCreator(String str, Parcel parcel) {
    }

    public /* bridge */ /* synthetic */ List getList() {
        return super.getList();
    }

    public /* bridge */ /* synthetic */ void setInlineCountLimit(int i) {
        super.setInlineCountLimit(i);
    }

    public /* bridge */ /* synthetic */ void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
    }

    public StringParceledListSlice(List<String> list) {
        super(list);
    }

    private StringParceledListSlice(Parcel parcel, ClassLoader classLoader) {
        super(parcel, classLoader);
    }

    public static StringParceledListSlice emptyList() {
        return new StringParceledListSlice(Collections.emptyList());
    }

    /* access modifiers changed from: protected */
    public void writeElement(String str, Parcel parcel, int i) {
        parcel.writeString(str);
    }

    /* access modifiers changed from: protected */
    public Parcelable.Creator<?> readParcelableCreator(Parcel parcel, ClassLoader classLoader) {
        return Parcel.STRING_CREATOR;
    }
}
