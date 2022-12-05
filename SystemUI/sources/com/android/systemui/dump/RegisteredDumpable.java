package com.android.systemui.dump;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: DumpManager.kt */
/* loaded from: classes.dex */
public final class RegisteredDumpable<T> {
    private final T dumpable;
    @NotNull
    private final String name;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RegisteredDumpable)) {
            return false;
        }
        RegisteredDumpable registeredDumpable = (RegisteredDumpable) obj;
        return Intrinsics.areEqual(this.name, registeredDumpable.name) && Intrinsics.areEqual(this.dumpable, registeredDumpable.dumpable);
    }

    public int hashCode() {
        int hashCode = this.name.hashCode() * 31;
        T t = this.dumpable;
        return hashCode + (t == null ? 0 : t.hashCode());
    }

    @NotNull
    public String toString() {
        return "RegisteredDumpable(name=" + this.name + ", dumpable=" + this.dumpable + ')';
    }

    public RegisteredDumpable(@NotNull String name, T t) {
        Intrinsics.checkNotNullParameter(name, "name");
        this.name = name;
        this.dumpable = t;
    }

    @NotNull
    public final String getName() {
        return this.name;
    }

    public final T getDumpable() {
        return this.dumpable;
    }
}
