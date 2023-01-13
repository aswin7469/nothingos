package com.android.systemui.media;

import android.app.PendingIntent;
import android.graphics.drawable.Drawable;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0018\n\u0002\u0010\b\n\u0002\b\u0002\b\b\u0018\u00002\u00020\u0001BC\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b\u0012\u0006\u0010\f\u001a\u00020\u0003¢\u0006\u0002\u0010\rJ\t\u0010\u0019\u001a\u00020\u0003HÆ\u0003J\u000b\u0010\u001a\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u000b\u0010\u001b\u001a\u0004\u0018\u00010\u0007HÆ\u0003J\u000b\u0010\u001c\u001a\u0004\u0018\u00010\tHÆ\u0003J\u000b\u0010\u001d\u001a\u0004\u0018\u00010\u000bHÆ\u0003J\t\u0010\u001e\u001a\u00020\u0003HÆ\u0003JM\u0010\u001f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b2\b\b\u0002\u0010\f\u001a\u00020\u0003HÆ\u0001J\u0013\u0010 \u001a\u00020\u00032\b\u0010!\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\u0010\u0010\"\u001a\u00020\u00032\b\u0010!\u001a\u0004\u0018\u00010\u0000J\t\u0010#\u001a\u00020$HÖ\u0001J\t\u0010%\u001a\u00020\u000bHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0013\u0010\n\u001a\u0004\u0018\u00010\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0013\u0010\b\u001a\u0004\u0018\u00010\t¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\f\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u000f¨\u0006&"}, mo65043d2 = {"Lcom/android/systemui/media/MediaDeviceData;", "", "enabled", "", "icon", "Landroid/graphics/drawable/Drawable;", "name", "", "intent", "Landroid/app/PendingIntent;", "id", "", "showBroadcastButton", "(ZLandroid/graphics/drawable/Drawable;Ljava/lang/CharSequence;Landroid/app/PendingIntent;Ljava/lang/String;Z)V", "getEnabled", "()Z", "getIcon", "()Landroid/graphics/drawable/Drawable;", "getId", "()Ljava/lang/String;", "getIntent", "()Landroid/app/PendingIntent;", "getName", "()Ljava/lang/CharSequence;", "getShowBroadcastButton", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "equalsWithoutIcon", "hashCode", "", "toString", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaData.kt */
public final class MediaDeviceData {
    private final boolean enabled;
    private final Drawable icon;

    /* renamed from: id */
    private final String f315id;
    private final PendingIntent intent;
    private final CharSequence name;
    private final boolean showBroadcastButton;

    public MediaDeviceData(boolean z, Drawable drawable, CharSequence charSequence, PendingIntent pendingIntent, boolean z2) {
        this(z, drawable, charSequence, pendingIntent, (String) null, z2, 16, (DefaultConstructorMarker) null);
    }

    public MediaDeviceData(boolean z, Drawable drawable, CharSequence charSequence, boolean z2) {
        this(z, drawable, charSequence, (PendingIntent) null, (String) null, z2, 24, (DefaultConstructorMarker) null);
    }

    public static /* synthetic */ MediaDeviceData copy$default(MediaDeviceData mediaDeviceData, boolean z, Drawable drawable, CharSequence charSequence, PendingIntent pendingIntent, String str, boolean z2, int i, Object obj) {
        if ((i & 1) != 0) {
            z = mediaDeviceData.enabled;
        }
        if ((i & 2) != 0) {
            drawable = mediaDeviceData.icon;
        }
        Drawable drawable2 = drawable;
        if ((i & 4) != 0) {
            charSequence = mediaDeviceData.name;
        }
        CharSequence charSequence2 = charSequence;
        if ((i & 8) != 0) {
            pendingIntent = mediaDeviceData.intent;
        }
        PendingIntent pendingIntent2 = pendingIntent;
        if ((i & 16) != 0) {
            str = mediaDeviceData.f315id;
        }
        String str2 = str;
        if ((i & 32) != 0) {
            z2 = mediaDeviceData.showBroadcastButton;
        }
        return mediaDeviceData.copy(z, drawable2, charSequence2, pendingIntent2, str2, z2);
    }

    public final boolean component1() {
        return this.enabled;
    }

    public final Drawable component2() {
        return this.icon;
    }

    public final CharSequence component3() {
        return this.name;
    }

    public final PendingIntent component4() {
        return this.intent;
    }

    public final String component5() {
        return this.f315id;
    }

    public final boolean component6() {
        return this.showBroadcastButton;
    }

    public final MediaDeviceData copy(boolean z, Drawable drawable, CharSequence charSequence, PendingIntent pendingIntent, String str, boolean z2) {
        return new MediaDeviceData(z, drawable, charSequence, pendingIntent, str, z2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MediaDeviceData)) {
            return false;
        }
        MediaDeviceData mediaDeviceData = (MediaDeviceData) obj;
        return this.enabled == mediaDeviceData.enabled && Intrinsics.areEqual((Object) this.icon, (Object) mediaDeviceData.icon) && Intrinsics.areEqual((Object) this.name, (Object) mediaDeviceData.name) && Intrinsics.areEqual((Object) this.intent, (Object) mediaDeviceData.intent) && Intrinsics.areEqual((Object) this.f315id, (Object) mediaDeviceData.f315id) && this.showBroadcastButton == mediaDeviceData.showBroadcastButton;
    }

    public int hashCode() {
        boolean z = this.enabled;
        boolean z2 = true;
        if (z) {
            z = true;
        }
        int i = (z ? 1 : 0) * true;
        Drawable drawable = this.icon;
        int i2 = 0;
        int hashCode = (i + (drawable == null ? 0 : drawable.hashCode())) * 31;
        CharSequence charSequence = this.name;
        int hashCode2 = (hashCode + (charSequence == null ? 0 : charSequence.hashCode())) * 31;
        PendingIntent pendingIntent = this.intent;
        int hashCode3 = (hashCode2 + (pendingIntent == null ? 0 : pendingIntent.hashCode())) * 31;
        String str = this.f315id;
        if (str != null) {
            i2 = str.hashCode();
        }
        int i3 = (hashCode3 + i2) * 31;
        boolean z3 = this.showBroadcastButton;
        if (!z3) {
            z2 = z3;
        }
        return i3 + (z2 ? 1 : 0);
    }

    public String toString() {
        return "MediaDeviceData(enabled=" + this.enabled + ", icon=" + this.icon + ", name=" + this.name + ", intent=" + this.intent + ", id=" + this.f315id + ", showBroadcastButton=" + this.showBroadcastButton + ')';
    }

    public MediaDeviceData(boolean z, Drawable drawable, CharSequence charSequence, PendingIntent pendingIntent, String str, boolean z2) {
        this.enabled = z;
        this.icon = drawable;
        this.name = charSequence;
        this.intent = pendingIntent;
        this.f315id = str;
        this.showBroadcastButton = z2;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ MediaDeviceData(boolean z, Drawable drawable, CharSequence charSequence, PendingIntent pendingIntent, String str, boolean z2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(z, drawable, charSequence, (i & 8) != 0 ? null : pendingIntent, (i & 16) != 0 ? null : str, z2);
    }

    public final boolean getEnabled() {
        return this.enabled;
    }

    public final Drawable getIcon() {
        return this.icon;
    }

    public final CharSequence getName() {
        return this.name;
    }

    public final PendingIntent getIntent() {
        return this.intent;
    }

    public final String getId() {
        return this.f315id;
    }

    public final boolean getShowBroadcastButton() {
        return this.showBroadcastButton;
    }

    public final boolean equalsWithoutIcon(MediaDeviceData mediaDeviceData) {
        if (mediaDeviceData != null && this.enabled == mediaDeviceData.enabled && Intrinsics.areEqual((Object) this.name, (Object) mediaDeviceData.name) && Intrinsics.areEqual((Object) this.intent, (Object) mediaDeviceData.intent) && Intrinsics.areEqual((Object) this.f315id, (Object) mediaDeviceData.f315id)) {
            return true;
        }
        return false;
    }
}
