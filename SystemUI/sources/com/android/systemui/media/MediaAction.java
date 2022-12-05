package com.android.systemui.media;

import android.graphics.drawable.Icon;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: MediaData.kt */
/* loaded from: classes.dex */
public final class MediaAction {
    @Nullable
    private final Runnable action;
    @Nullable
    private final CharSequence contentDescription;
    @Nullable
    private final Icon icon;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MediaAction)) {
            return false;
        }
        MediaAction mediaAction = (MediaAction) obj;
        return Intrinsics.areEqual(this.icon, mediaAction.icon) && Intrinsics.areEqual(this.action, mediaAction.action) && Intrinsics.areEqual(this.contentDescription, mediaAction.contentDescription);
    }

    public int hashCode() {
        Icon icon = this.icon;
        int i = 0;
        int hashCode = (icon == null ? 0 : icon.hashCode()) * 31;
        Runnable runnable = this.action;
        int hashCode2 = (hashCode + (runnable == null ? 0 : runnable.hashCode())) * 31;
        CharSequence charSequence = this.contentDescription;
        if (charSequence != null) {
            i = charSequence.hashCode();
        }
        return hashCode2 + i;
    }

    @NotNull
    public String toString() {
        return "MediaAction(icon=" + this.icon + ", action=" + this.action + ", contentDescription=" + ((Object) this.contentDescription) + ')';
    }

    public MediaAction(@Nullable Icon icon, @Nullable Runnable runnable, @Nullable CharSequence charSequence) {
        this.icon = icon;
        this.action = runnable;
        this.contentDescription = charSequence;
    }

    @Nullable
    public final Icon getIcon() {
        return this.icon;
    }

    @Nullable
    public final Runnable getAction() {
        return this.action;
    }

    @Nullable
    public final CharSequence getContentDescription() {
        return this.contentDescription;
    }
}
