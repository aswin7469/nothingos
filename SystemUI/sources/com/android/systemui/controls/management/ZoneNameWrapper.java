package com.android.systemui.controls.management;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ControlsModel.kt */
/* loaded from: classes.dex */
public final class ZoneNameWrapper extends ElementWrapper {
    @NotNull
    private final CharSequence zoneName;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof ZoneNameWrapper) && Intrinsics.areEqual(this.zoneName, ((ZoneNameWrapper) obj).zoneName);
    }

    public int hashCode() {
        return this.zoneName.hashCode();
    }

    @NotNull
    public String toString() {
        return "ZoneNameWrapper(zoneName=" + ((Object) this.zoneName) + ')';
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ZoneNameWrapper(@NotNull CharSequence zoneName) {
        super(null);
        Intrinsics.checkNotNullParameter(zoneName, "zoneName");
        this.zoneName = zoneName;
    }

    @NotNull
    public final CharSequence getZoneName() {
        return this.zoneName;
    }
}
