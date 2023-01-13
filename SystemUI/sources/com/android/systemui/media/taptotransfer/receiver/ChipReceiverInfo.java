package com.android.systemui.media.taptotransfer.receiver;

import android.graphics.drawable.Drawable;
import android.media.MediaRoute2Info;
import com.android.systemui.media.taptotransfer.common.ChipInfoCommon;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0002\b\f\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\bJ\t\u0010\u000f\u001a\u00020\u0003HÆ\u0003J\u000b\u0010\u0010\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u000b\u0010\u0011\u001a\u0004\u0018\u00010\u0007HÆ\u0003J+\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007HÆ\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016HÖ\u0003J\b\u0010\u0017\u001a\u00020\u0018H\u0016J\t\u0010\u0019\u001a\u00020\u001aHÖ\u0001J\t\u0010\u001b\u001a\u00020\u001cHÖ\u0001R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e¨\u0006\u001d"}, mo65043d2 = {"Lcom/android/systemui/media/taptotransfer/receiver/ChipReceiverInfo;", "Lcom/android/systemui/media/taptotransfer/common/ChipInfoCommon;", "routeInfo", "Landroid/media/MediaRoute2Info;", "appIconDrawableOverride", "Landroid/graphics/drawable/Drawable;", "appNameOverride", "", "(Landroid/media/MediaRoute2Info;Landroid/graphics/drawable/Drawable;Ljava/lang/CharSequence;)V", "getAppIconDrawableOverride", "()Landroid/graphics/drawable/Drawable;", "getAppNameOverride", "()Ljava/lang/CharSequence;", "getRouteInfo", "()Landroid/media/MediaRoute2Info;", "component1", "component2", "component3", "copy", "equals", "", "other", "", "getTimeoutMs", "", "hashCode", "", "toString", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaTttChipControllerReceiver.kt */
public final class ChipReceiverInfo implements ChipInfoCommon {
    private final Drawable appIconDrawableOverride;
    private final CharSequence appNameOverride;
    private final MediaRoute2Info routeInfo;

    public static /* synthetic */ ChipReceiverInfo copy$default(ChipReceiverInfo chipReceiverInfo, MediaRoute2Info mediaRoute2Info, Drawable drawable, CharSequence charSequence, int i, Object obj) {
        if ((i & 1) != 0) {
            mediaRoute2Info = chipReceiverInfo.routeInfo;
        }
        if ((i & 2) != 0) {
            drawable = chipReceiverInfo.appIconDrawableOverride;
        }
        if ((i & 4) != 0) {
            charSequence = chipReceiverInfo.appNameOverride;
        }
        return chipReceiverInfo.copy(mediaRoute2Info, drawable, charSequence);
    }

    public final MediaRoute2Info component1() {
        return this.routeInfo;
    }

    public final Drawable component2() {
        return this.appIconDrawableOverride;
    }

    public final CharSequence component3() {
        return this.appNameOverride;
    }

    public final ChipReceiverInfo copy(MediaRoute2Info mediaRoute2Info, Drawable drawable, CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(mediaRoute2Info, "routeInfo");
        return new ChipReceiverInfo(mediaRoute2Info, drawable, charSequence);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ChipReceiverInfo)) {
            return false;
        }
        ChipReceiverInfo chipReceiverInfo = (ChipReceiverInfo) obj;
        return Intrinsics.areEqual((Object) this.routeInfo, (Object) chipReceiverInfo.routeInfo) && Intrinsics.areEqual((Object) this.appIconDrawableOverride, (Object) chipReceiverInfo.appIconDrawableOverride) && Intrinsics.areEqual((Object) this.appNameOverride, (Object) chipReceiverInfo.appNameOverride);
    }

    public long getTimeoutMs() {
        return 3000;
    }

    public int hashCode() {
        int hashCode = this.routeInfo.hashCode() * 31;
        Drawable drawable = this.appIconDrawableOverride;
        int i = 0;
        int hashCode2 = (hashCode + (drawable == null ? 0 : drawable.hashCode())) * 31;
        CharSequence charSequence = this.appNameOverride;
        if (charSequence != null) {
            i = charSequence.hashCode();
        }
        return hashCode2 + i;
    }

    public String toString() {
        return "ChipReceiverInfo(routeInfo=" + this.routeInfo + ", appIconDrawableOverride=" + this.appIconDrawableOverride + ", appNameOverride=" + this.appNameOverride + ')';
    }

    public ChipReceiverInfo(MediaRoute2Info mediaRoute2Info, Drawable drawable, CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(mediaRoute2Info, "routeInfo");
        this.routeInfo = mediaRoute2Info;
        this.appIconDrawableOverride = drawable;
        this.appNameOverride = charSequence;
    }

    public final MediaRoute2Info getRouteInfo() {
        return this.routeInfo;
    }

    public final Drawable getAppIconDrawableOverride() {
        return this.appIconDrawableOverride;
    }

    public final CharSequence getAppNameOverride() {
        return this.appNameOverride;
    }
}
