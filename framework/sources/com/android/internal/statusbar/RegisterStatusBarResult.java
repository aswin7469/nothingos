package com.android.internal.statusbar;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArrayMap;
import com.android.internal.view.AppearanceRegion;
/* loaded from: classes4.dex */
public final class RegisterStatusBarResult implements Parcelable {
    public static final Parcelable.Creator<RegisterStatusBarResult> CREATOR = new Parcelable.Creator<RegisterStatusBarResult>() { // from class: com.android.internal.statusbar.RegisterStatusBarResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public RegisterStatusBarResult mo3559createFromParcel(Parcel source) {
            ArrayMap<String, StatusBarIcon> icons = source.createTypedArrayMap(StatusBarIcon.CREATOR);
            int disabledFlags1 = source.readInt();
            int appearance = source.readInt();
            AppearanceRegion[] appearanceRegions = (AppearanceRegion[]) source.readParcelableArray(null, AppearanceRegion.class);
            int imeWindowVis = source.readInt();
            int imeBackDisposition = source.readInt();
            boolean showImeSwitcher = source.readBoolean();
            int disabledFlags2 = source.readInt();
            IBinder imeToken = source.readStrongBinder();
            boolean navbarColorManagedByIme = source.readBoolean();
            int behavior = source.readInt();
            boolean appFullscreen = source.readBoolean();
            int[] transientBarTypes = source.createIntArray();
            return new RegisterStatusBarResult(icons, disabledFlags1, appearance, appearanceRegions, imeWindowVis, imeBackDisposition, showImeSwitcher, disabledFlags2, imeToken, navbarColorManagedByIme, behavior, appFullscreen, transientBarTypes);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public RegisterStatusBarResult[] mo3560newArray(int size) {
            return new RegisterStatusBarResult[size];
        }
    };
    public final boolean mAppFullscreen;
    public final int mAppearance;
    public final AppearanceRegion[] mAppearanceRegions;
    public final int mBehavior;
    public final int mDisabledFlags1;
    public final int mDisabledFlags2;
    public final ArrayMap<String, StatusBarIcon> mIcons;
    public final int mImeBackDisposition;
    public final IBinder mImeToken;
    public final int mImeWindowVis;
    public final boolean mNavbarColorManagedByIme;
    public final boolean mShowImeSwitcher;
    public final int[] mTransientBarTypes;

    public RegisterStatusBarResult(ArrayMap<String, StatusBarIcon> icons, int disabledFlags1, int appearance, AppearanceRegion[] appearanceRegions, int imeWindowVis, int imeBackDisposition, boolean showImeSwitcher, int disabledFlags2, IBinder imeToken, boolean navbarColorManagedByIme, int behavior, boolean appFullscreen, int[] transientBarTypes) {
        this.mIcons = new ArrayMap<>(icons);
        this.mDisabledFlags1 = disabledFlags1;
        this.mAppearance = appearance;
        this.mAppearanceRegions = appearanceRegions;
        this.mImeWindowVis = imeWindowVis;
        this.mImeBackDisposition = imeBackDisposition;
        this.mShowImeSwitcher = showImeSwitcher;
        this.mDisabledFlags2 = disabledFlags2;
        this.mImeToken = imeToken;
        this.mNavbarColorManagedByIme = navbarColorManagedByIme;
        this.mBehavior = behavior;
        this.mAppFullscreen = appFullscreen;
        this.mTransientBarTypes = transientBarTypes;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArrayMap(this.mIcons, flags);
        dest.writeInt(this.mDisabledFlags1);
        dest.writeInt(this.mAppearance);
        dest.writeParcelableArray(this.mAppearanceRegions, 0);
        dest.writeInt(this.mImeWindowVis);
        dest.writeInt(this.mImeBackDisposition);
        dest.writeBoolean(this.mShowImeSwitcher);
        dest.writeInt(this.mDisabledFlags2);
        dest.writeStrongBinder(this.mImeToken);
        dest.writeBoolean(this.mNavbarColorManagedByIme);
        dest.writeInt(this.mBehavior);
        dest.writeBoolean(this.mAppFullscreen);
        dest.writeIntArray(this.mTransientBarTypes);
    }
}