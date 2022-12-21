package com.nothing.p023os.device;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0015\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u0000 %2\u00020\u0001:\u0001%B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004BA\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\b\u0010\t\u001a\u0004\u0018\u00010\b\u0012\u0006\u0010\n\u001a\u00020\u0006\u0012\u0006\u0010\u000b\u001a\u00020\u0006\u0012\u000e\u0010\f\u001a\n\u0012\u0004\u0012\u00020\u0000\u0018\u00010\r¢\u0006\u0002\u0010\u000eJ\b\u0010!\u001a\u00020\u0006H\u0016J\u0018\u0010\"\u001a\u00020#2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010$\u001a\u00020\u0006H\u0016R\u001a\u0010\u000b\u001a\u00020\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\"\u0010\f\u001a\n\u0012\u0004\u0012\u00020\u0000\u0018\u00010\rX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001a\u0010\n\u001a\u00020\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0010\"\u0004\b\u0018\u0010\u0012R\u001a\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u0010\"\u0004\b\u001a\u0010\u0012R\u001c\u0010\t\u001a\u0004\u0018\u00010\bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u001c\u0010\u0007\u001a\u0004\u0018\u00010\bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u001c\"\u0004\b \u0010\u001e¨\u0006&"}, mo64987d2 = {"Lcom/nothing/os/device/DeviceFunctionItem;", "Landroid/os/Parcelable;", "parcel", "Landroid/os/Parcel;", "(Landroid/os/Parcel;)V", "style", "", "title", "", "summary", "order", "ancLevel", "items", "Ljava/util/ArrayList;", "(ILjava/lang/String;Ljava/lang/String;IILjava/util/ArrayList;)V", "getAncLevel", "()I", "setAncLevel", "(I)V", "getItems", "()Ljava/util/ArrayList;", "setItems", "(Ljava/util/ArrayList;)V", "getOrder", "setOrder", "getStyle", "setStyle", "getSummary", "()Ljava/lang/String;", "setSummary", "(Ljava/lang/String;)V", "getTitle", "setTitle", "describeContents", "writeToParcel", "", "flags", "CREATOR", "osConnect_SnapshotRelease"}, mo64988k = 1, mo64989mv = {1, 5, 1}, mo64991xi = 48)
/* renamed from: com.nothing.os.device.DeviceFunctionItem */
/* compiled from: DeviceFunctionItem.kt */
public final class DeviceFunctionItem implements Parcelable {
    public static final CREATOR CREATOR = new CREATOR((DefaultConstructorMarker) null);
    private int ancLevel;
    private ArrayList<DeviceFunctionItem> items;
    private int order;
    private int style;
    private String summary;
    private String title;

    public int describeContents() {
        return 0;
    }

    public DeviceFunctionItem(int i, String str, String str2, int i2, int i3, ArrayList<DeviceFunctionItem> arrayList) {
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

    public final void setStyle(int i) {
        this.style = i;
    }

    public final String getTitle() {
        return this.title;
    }

    public final void setTitle(String str) {
        this.title = str;
    }

    public final String getSummary() {
        return this.summary;
    }

    public final void setSummary(String str) {
        this.summary = str;
    }

    public final int getOrder() {
        return this.order;
    }

    public final void setOrder(int i) {
        this.order = i;
    }

    public final int getAncLevel() {
        return this.ancLevel;
    }

    public final void setAncLevel(int i) {
        this.ancLevel = i;
    }

    public final ArrayList<DeviceFunctionItem> getItems() {
        return this.items;
    }

    public final void setItems(ArrayList<DeviceFunctionItem> arrayList) {
        this.items = arrayList;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public DeviceFunctionItem(Parcel parcel) {
        this(parcel.readInt(), parcel.readString(), parcel.readString(), parcel.readInt(), parcel.readInt(), parcel.createTypedArrayList(CREATOR));
        Intrinsics.checkNotNullParameter(parcel, "parcel");
    }

    public void writeToParcel(Parcel parcel, int i) {
        Intrinsics.checkNotNullParameter(parcel, "parcel");
        parcel.writeInt(this.style);
        parcel.writeString(this.title);
        parcel.writeString(this.summary);
        parcel.writeInt(this.order);
        parcel.writeInt(this.ancLevel);
        parcel.writeTypedList(this.items);
    }

    @Metadata(mo64986d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u001d\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016¢\u0006\u0002\u0010\u000b¨\u0006\f"}, mo64987d2 = {"Lcom/nothing/os/device/DeviceFunctionItem$CREATOR;", "Landroid/os/Parcelable$Creator;", "Lcom/nothing/os/device/DeviceFunctionItem;", "()V", "createFromParcel", "parcel", "Landroid/os/Parcel;", "newArray", "", "size", "", "(I)[Lcom/nothing/os/device/DeviceFunctionItem;", "osConnect_SnapshotRelease"}, mo64988k = 1, mo64989mv = {1, 5, 1}, mo64991xi = 48)
    /* renamed from: com.nothing.os.device.DeviceFunctionItem$CREATOR */
    /* compiled from: DeviceFunctionItem.kt */
    public static final class CREATOR implements Parcelable.Creator<DeviceFunctionItem> {
        public /* synthetic */ CREATOR(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private CREATOR() {
        }

        public DeviceFunctionItem createFromParcel(Parcel parcel) {
            Intrinsics.checkNotNullParameter(parcel, "parcel");
            return new DeviceFunctionItem(parcel);
        }

        public DeviceFunctionItem[] newArray(int i) {
            return new DeviceFunctionItem[i];
        }
    }
}
