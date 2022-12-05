package com.android.systemui.qs.external;

import android.content.ComponentName;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: CustomTileStatePersister.kt */
/* loaded from: classes.dex */
public final class TileServiceKey {
    @NotNull
    private final ComponentName componentName;
    @NotNull
    private final String string;
    private final int user;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TileServiceKey)) {
            return false;
        }
        TileServiceKey tileServiceKey = (TileServiceKey) obj;
        return Intrinsics.areEqual(this.componentName, tileServiceKey.componentName) && this.user == tileServiceKey.user;
    }

    public int hashCode() {
        return (this.componentName.hashCode() * 31) + Integer.hashCode(this.user);
    }

    public TileServiceKey(@NotNull ComponentName componentName, int i) {
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        this.componentName = componentName;
        this.user = i;
        StringBuilder sb = new StringBuilder();
        sb.append((Object) componentName.flattenToString());
        sb.append(':');
        sb.append(i);
        this.string = sb.toString();
    }

    @NotNull
    public String toString() {
        return this.string;
    }
}
