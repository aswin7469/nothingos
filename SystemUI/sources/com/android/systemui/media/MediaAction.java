package com.android.systemui.media;

import android.graphics.drawable.Drawable;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0013\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B9\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\b\u0010\b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n¢\u0006\u0002\u0010\u000bJ\u000b\u0010\u0016\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u0017\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u000b\u0010\u0018\u001a\u0004\u0018\u00010\u0007HÆ\u0003J\u000b\u0010\u0019\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u0010\u0010\u001a\u001a\u0004\u0018\u00010\nHÆ\u0003¢\u0006\u0002\u0010\u0014JJ\u0010\u001b\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\nHÆ\u0001¢\u0006\u0002\u0010\u001cJ\u0013\u0010\u001d\u001a\u00020\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010 \u001a\u00020\nHÖ\u0001J\t\u0010!\u001a\u00020\"HÖ\u0001R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0013\u0010\b\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000fR\u0015\u0010\t\u001a\u0004\u0018\u00010\n¢\u0006\n\n\u0002\u0010\u0015\u001a\u0004\b\u0013\u0010\u0014¨\u0006#"}, mo64987d2 = {"Lcom/android/systemui/media/MediaAction;", "", "icon", "Landroid/graphics/drawable/Drawable;", "action", "Ljava/lang/Runnable;", "contentDescription", "", "background", "rebindId", "", "(Landroid/graphics/drawable/Drawable;Ljava/lang/Runnable;Ljava/lang/CharSequence;Landroid/graphics/drawable/Drawable;Ljava/lang/Integer;)V", "getAction", "()Ljava/lang/Runnable;", "getBackground", "()Landroid/graphics/drawable/Drawable;", "getContentDescription", "()Ljava/lang/CharSequence;", "getIcon", "getRebindId", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "component1", "component2", "component3", "component4", "component5", "copy", "(Landroid/graphics/drawable/Drawable;Ljava/lang/Runnable;Ljava/lang/CharSequence;Landroid/graphics/drawable/Drawable;Ljava/lang/Integer;)Lcom/android/systemui/media/MediaAction;", "equals", "", "other", "hashCode", "toString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaData.kt */
public final class MediaAction {
    private final Runnable action;
    private final Drawable background;
    private final CharSequence contentDescription;
    private final Drawable icon;
    private final Integer rebindId;

    public static /* synthetic */ MediaAction copy$default(MediaAction mediaAction, Drawable drawable, Runnable runnable, CharSequence charSequence, Drawable drawable2, Integer num, int i, Object obj) {
        if ((i & 1) != 0) {
            drawable = mediaAction.icon;
        }
        if ((i & 2) != 0) {
            runnable = mediaAction.action;
        }
        Runnable runnable2 = runnable;
        if ((i & 4) != 0) {
            charSequence = mediaAction.contentDescription;
        }
        CharSequence charSequence2 = charSequence;
        if ((i & 8) != 0) {
            drawable2 = mediaAction.background;
        }
        Drawable drawable3 = drawable2;
        if ((i & 16) != 0) {
            num = mediaAction.rebindId;
        }
        return mediaAction.copy(drawable, runnable2, charSequence2, drawable3, num);
    }

    public final Drawable component1() {
        return this.icon;
    }

    public final Runnable component2() {
        return this.action;
    }

    public final CharSequence component3() {
        return this.contentDescription;
    }

    public final Drawable component4() {
        return this.background;
    }

    public final Integer component5() {
        return this.rebindId;
    }

    public final MediaAction copy(Drawable drawable, Runnable runnable, CharSequence charSequence, Drawable drawable2, Integer num) {
        return new MediaAction(drawable, runnable, charSequence, drawable2, num);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MediaAction)) {
            return false;
        }
        MediaAction mediaAction = (MediaAction) obj;
        return Intrinsics.areEqual((Object) this.icon, (Object) mediaAction.icon) && Intrinsics.areEqual((Object) this.action, (Object) mediaAction.action) && Intrinsics.areEqual((Object) this.contentDescription, (Object) mediaAction.contentDescription) && Intrinsics.areEqual((Object) this.background, (Object) mediaAction.background) && Intrinsics.areEqual((Object) this.rebindId, (Object) mediaAction.rebindId);
    }

    public int hashCode() {
        Drawable drawable = this.icon;
        int i = 0;
        int hashCode = (drawable == null ? 0 : drawable.hashCode()) * 31;
        Runnable runnable = this.action;
        int hashCode2 = (hashCode + (runnable == null ? 0 : runnable.hashCode())) * 31;
        CharSequence charSequence = this.contentDescription;
        int hashCode3 = (hashCode2 + (charSequence == null ? 0 : charSequence.hashCode())) * 31;
        Drawable drawable2 = this.background;
        int hashCode4 = (hashCode3 + (drawable2 == null ? 0 : drawable2.hashCode())) * 31;
        Integer num = this.rebindId;
        if (num != null) {
            i = num.hashCode();
        }
        return hashCode4 + i;
    }

    public String toString() {
        return "MediaAction(icon=" + this.icon + ", action=" + this.action + ", contentDescription=" + this.contentDescription + ", background=" + this.background + ", rebindId=" + this.rebindId + ')';
    }

    public MediaAction(Drawable drawable, Runnable runnable, CharSequence charSequence, Drawable drawable2, Integer num) {
        this.icon = drawable;
        this.action = runnable;
        this.contentDescription = charSequence;
        this.background = drawable2;
        this.rebindId = num;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ MediaAction(Drawable drawable, Runnable runnable, CharSequence charSequence, Drawable drawable2, Integer num, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(drawable, runnable, charSequence, drawable2, (i & 16) != 0 ? null : num);
    }

    public final Drawable getIcon() {
        return this.icon;
    }

    public final Runnable getAction() {
        return this.action;
    }

    public final CharSequence getContentDescription() {
        return this.contentDescription;
    }

    public final Drawable getBackground() {
        return this.background;
    }

    public final Integer getRebindId() {
        return this.rebindId;
    }
}
