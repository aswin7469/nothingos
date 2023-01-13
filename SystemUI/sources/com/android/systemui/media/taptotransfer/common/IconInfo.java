package com.android.systemui.media.taptotransfer.common;

import android.graphics.drawable.Drawable;
import com.android.launcher3.icons.cache.BaseIconCache;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\r\n\u0002\u0010\b\n\u0002\b\u0002\b\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000f\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0007HÆ\u0003J'\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007HÆ\u0001J\u0013\u0010\u0012\u001a\u00020\u00072\b\u0010\u0013\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0014\u001a\u00020\u0015HÖ\u0001J\t\u0010\u0016\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\r¨\u0006\u0017"}, mo65043d2 = {"Lcom/android/systemui/media/taptotransfer/common/IconInfo;", "", "iconName", "", "icon", "Landroid/graphics/drawable/Drawable;", "isAppIcon", "", "(Ljava/lang/String;Landroid/graphics/drawable/Drawable;Z)V", "getIcon", "()Landroid/graphics/drawable/Drawable;", "getIconName", "()Ljava/lang/String;", "()Z", "component1", "component2", "component3", "copy", "equals", "other", "hashCode", "", "toString", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaTttChipControllerCommon.kt */
final class IconInfo {
    private final Drawable icon;
    private final String iconName;
    private final boolean isAppIcon;

    public static /* synthetic */ IconInfo copy$default(IconInfo iconInfo, String str, Drawable drawable, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            str = iconInfo.iconName;
        }
        if ((i & 2) != 0) {
            drawable = iconInfo.icon;
        }
        if ((i & 4) != 0) {
            z = iconInfo.isAppIcon;
        }
        return iconInfo.copy(str, drawable, z);
    }

    public final String component1() {
        return this.iconName;
    }

    public final Drawable component2() {
        return this.icon;
    }

    public final boolean component3() {
        return this.isAppIcon;
    }

    public final IconInfo copy(String str, Drawable drawable, boolean z) {
        Intrinsics.checkNotNullParameter(str, "iconName");
        Intrinsics.checkNotNullParameter(drawable, BaseIconCache.IconDB.COLUMN_ICON);
        return new IconInfo(str, drawable, z);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof IconInfo)) {
            return false;
        }
        IconInfo iconInfo = (IconInfo) obj;
        return Intrinsics.areEqual((Object) this.iconName, (Object) iconInfo.iconName) && Intrinsics.areEqual((Object) this.icon, (Object) iconInfo.icon) && this.isAppIcon == iconInfo.isAppIcon;
    }

    public int hashCode() {
        int hashCode = ((this.iconName.hashCode() * 31) + this.icon.hashCode()) * 31;
        boolean z = this.isAppIcon;
        if (z) {
            z = true;
        }
        return hashCode + (z ? 1 : 0);
    }

    public String toString() {
        return "IconInfo(iconName=" + this.iconName + ", icon=" + this.icon + ", isAppIcon=" + this.isAppIcon + ')';
    }

    public IconInfo(String str, Drawable drawable, boolean z) {
        Intrinsics.checkNotNullParameter(str, "iconName");
        Intrinsics.checkNotNullParameter(drawable, BaseIconCache.IconDB.COLUMN_ICON);
        this.iconName = str;
        this.icon = drawable;
        this.isAppIcon = z;
    }

    public final String getIconName() {
        return this.iconName;
    }

    public final Drawable getIcon() {
        return this.icon;
    }

    public final boolean isAppIcon() {
        return this.isAppIcon;
    }
}
