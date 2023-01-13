package com.android.systemui.dump;

import com.android.settingslib.datetime.ZoneGetter;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\b\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00028\u0000¢\u0006\u0002\u0010\u0006J\t\u0010\f\u001a\u00020\u0004HÆ\u0003J\u000e\u0010\r\u001a\u00028\u0000HÆ\u0003¢\u0006\u0002\u0010\bJ(\u0010\u000e\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00028\u0000HÆ\u0001¢\u0006\u0002\u0010\u000fJ\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0002HÖ\u0003J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001J\t\u0010\u0015\u001a\u00020\u0004HÖ\u0001R\u0013\u0010\u0005\u001a\u00028\u0000¢\u0006\n\n\u0002\u0010\t\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0016"}, mo65043d2 = {"Lcom/android/systemui/dump/RegisteredDumpable;", "T", "", "name", "", "dumpable", "(Ljava/lang/String;Ljava/lang/Object;)V", "getDumpable", "()Ljava/lang/Object;", "Ljava/lang/Object;", "getName", "()Ljava/lang/String;", "component1", "component2", "copy", "(Ljava/lang/String;Ljava/lang/Object;)Lcom/android/systemui/dump/RegisteredDumpable;", "equals", "", "other", "hashCode", "", "toString", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: DumpManager.kt */
final class RegisteredDumpable<T> {
    private final T dumpable;
    private final String name;

    public static /* synthetic */ RegisteredDumpable copy$default(RegisteredDumpable registeredDumpable, String str, T t, int i, Object obj) {
        if ((i & 1) != 0) {
            str = registeredDumpable.name;
        }
        if ((i & 2) != 0) {
            t = registeredDumpable.dumpable;
        }
        return registeredDumpable.copy(str, t);
    }

    public final String component1() {
        return this.name;
    }

    public final T component2() {
        return this.dumpable;
    }

    public final RegisteredDumpable<T> copy(String str, T t) {
        Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
        return new RegisteredDumpable<>(str, t);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RegisteredDumpable)) {
            return false;
        }
        RegisteredDumpable registeredDumpable = (RegisteredDumpable) obj;
        return Intrinsics.areEqual((Object) this.name, (Object) registeredDumpable.name) && Intrinsics.areEqual((Object) this.dumpable, (Object) registeredDumpable.dumpable);
    }

    public int hashCode() {
        int hashCode = this.name.hashCode() * 31;
        T t = this.dumpable;
        return hashCode + (t == null ? 0 : t.hashCode());
    }

    public String toString() {
        return "RegisteredDumpable(name=" + this.name + ", dumpable=" + this.dumpable + ')';
    }

    public RegisteredDumpable(String str, T t) {
        Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
        this.name = str;
        this.dumpable = t;
    }

    public final String getName() {
        return this.name;
    }

    public final T getDumpable() {
        return this.dumpable;
    }
}
