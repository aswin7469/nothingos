package com.android.systemui.shared.system.smartspace;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.PropertyReference1Impl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: SmartspaceState.kt */
/* loaded from: classes.dex */
public final class SmartspaceState implements Parcelable {
    @NotNull
    public static final CREATOR CREATOR = new CREATOR(null);
    @NotNull
    private Rect boundsOnScreen;
    private int selectedPage;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public SmartspaceState() {
        this.boundsOnScreen = new Rect();
    }

    @NotNull
    public final Rect getBoundsOnScreen() {
        return this.boundsOnScreen;
    }

    /* compiled from: SmartspaceState.kt */
    /* renamed from: com.android.systemui.shared.system.smartspace.SmartspaceState$1  reason: invalid class name */
    /* loaded from: classes.dex */
    /* synthetic */ class AnonymousClass1 extends PropertyReference1Impl {
        public static final AnonymousClass1 INSTANCE = new AnonymousClass1();

        AnonymousClass1() {
            super(JvmClassMappingKt.class, "javaClass", "getJavaClass(Ljava/lang/Object;)Ljava/lang/Class;", 1);
        }

        @Override // kotlin.jvm.internal.PropertyReference1Impl, kotlin.reflect.KProperty1
        @Nullable
        public Object get(@Nullable Object obj) {
            return obj.getClass();
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public SmartspaceState(@NotNull Parcel parcel) {
        this();
        Intrinsics.checkNotNullParameter(parcel, "parcel");
        Parcelable readParcelable = parcel.readParcelable(AnonymousClass1.INSTANCE.getClass().getClassLoader());
        Intrinsics.checkNotNullExpressionValue(readParcelable, "parcel.readParcelable(Rect::javaClass.javaClass.classLoader)");
        this.boundsOnScreen = (Rect) readParcelable;
        this.selectedPage = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(@Nullable Parcel parcel, int i) {
        if (parcel != null) {
            parcel.writeParcelable(this.boundsOnScreen, 0);
        }
        if (parcel == null) {
            return;
        }
        parcel.writeInt(this.selectedPage);
    }

    @NotNull
    public String toString() {
        return "boundsOnScreen: " + this.boundsOnScreen + ", selectedPage: " + this.selectedPage;
    }

    /* compiled from: SmartspaceState.kt */
    /* loaded from: classes.dex */
    public static final class CREATOR implements Parcelable.Creator<SmartspaceState> {
        public /* synthetic */ CREATOR(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private CREATOR() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        @NotNull
        /* renamed from: createFromParcel */
        public SmartspaceState mo979createFromParcel(@NotNull Parcel parcel) {
            Intrinsics.checkNotNullParameter(parcel, "parcel");
            return new SmartspaceState(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        @NotNull
        /* renamed from: newArray */
        public SmartspaceState[] mo980newArray(int i) {
            return new SmartspaceState[i];
        }
    }
}
