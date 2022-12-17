package com.nothing.p005os.device;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* renamed from: com.nothing.os.device.DeviceFunctionItem */
/* compiled from: DeviceFunctionItem.kt */
public final class DeviceFunctionItem implements Parcelable {
    @NotNull
    public static final CREATOR CREATOR = new CREATOR((DefaultConstructorMarker) null);
    private int ancLevel;
    @Nullable
    private ArrayList<DeviceFunctionItem> items;
    private int order;
    private int style;
    @Nullable
    private String summary;
    @Nullable
    private String title;

    public int describeContents() {
        return 0;
    }

    public DeviceFunctionItem(int i, @Nullable String str, @Nullable String str2, int i2, int i3, @Nullable ArrayList<DeviceFunctionItem> arrayList) {
        this.style = i;
        this.title = str;
        this.summary = str2;
        this.order = i2;
        this.ancLevel = i3;
        this.items = arrayList;
    }

    public final int getStyle() {
        return this.style;
    }

    @Nullable
    public final String getTitle() {
        return this.title;
    }

    @Nullable
    public final String getSummary() {
        return this.summary;
    }

    public final int getOrder() {
        return this.order;
    }

    public final int getAncLevel() {
        return this.ancLevel;
    }

    @Nullable
    public final ArrayList<DeviceFunctionItem> getItems() {
        return this.items;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public DeviceFunctionItem(@NotNull Parcel parcel) {
        this(parcel.readInt(), parcel.readString(), parcel.readString(), parcel.readInt(), parcel.readInt(), parcel.createTypedArrayList(CREATOR));
        Intrinsics.checkNotNullParameter(parcel, "parcel");
    }

    public void writeToParcel(@NotNull Parcel parcel, int i) {
        Intrinsics.checkNotNullParameter(parcel, "parcel");
        parcel.writeInt(this.style);
        parcel.writeString(this.title);
        parcel.writeString(this.summary);
        parcel.writeInt(this.order);
        parcel.writeInt(this.ancLevel);
        parcel.writeTypedList(this.items);
    }

    /* renamed from: com.nothing.os.device.DeviceFunctionItem$CREATOR */
    /* compiled from: DeviceFunctionItem.kt */
    public static final class CREATOR implements Parcelable.Creator<DeviceFunctionItem> {
        public /* synthetic */ CREATOR(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private CREATOR() {
        }

        @NotNull
        public DeviceFunctionItem createFromParcel(@NotNull Parcel parcel) {
            Intrinsics.checkNotNullParameter(parcel, "parcel");
            return new DeviceFunctionItem(parcel);
        }

        @NotNull
        public DeviceFunctionItem[] newArray(int i) {
            return new DeviceFunctionItem[i];
        }
    }
}
