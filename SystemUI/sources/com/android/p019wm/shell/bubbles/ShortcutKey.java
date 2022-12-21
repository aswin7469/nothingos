package com.android.p019wm.shell.bubbles;

import androidx.slice.compat.SliceProviderCompat;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0004\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\t\u0010\f\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0012\u001a\u00020\u0005HÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0013"}, mo64987d2 = {"Lcom/android/wm/shell/bubbles/ShortcutKey;", "", "userId", "", "pkg", "", "(ILjava/lang/String;)V", "getPkg", "()Ljava/lang/String;", "getUserId", "()I", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "WMShell_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.wm.shell.bubbles.ShortcutKey */
/* compiled from: BubbleDataRepository.kt */
public final class ShortcutKey {
    private final String pkg;
    private final int userId;

    public static /* synthetic */ ShortcutKey copy$default(ShortcutKey shortcutKey, int i, String str, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = shortcutKey.userId;
        }
        if ((i2 & 2) != 0) {
            str = shortcutKey.pkg;
        }
        return shortcutKey.copy(i, str);
    }

    public final int component1() {
        return this.userId;
    }

    public final String component2() {
        return this.pkg;
    }

    public final ShortcutKey copy(int i, String str) {
        Intrinsics.checkNotNullParameter(str, SliceProviderCompat.EXTRA_PKG);
        return new ShortcutKey(i, str);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ShortcutKey)) {
            return false;
        }
        ShortcutKey shortcutKey = (ShortcutKey) obj;
        return this.userId == shortcutKey.userId && Intrinsics.areEqual((Object) this.pkg, (Object) shortcutKey.pkg);
    }

    public int hashCode() {
        return (Integer.hashCode(this.userId) * 31) + this.pkg.hashCode();
    }

    public String toString() {
        return "ShortcutKey(userId=" + this.userId + ", pkg=" + this.pkg + ')';
    }

    public ShortcutKey(int i, String str) {
        Intrinsics.checkNotNullParameter(str, SliceProviderCompat.EXTRA_PKG);
        this.userId = i;
        this.pkg = str;
    }

    public final String getPkg() {
        return this.pkg;
    }

    public final int getUserId() {
        return this.userId;
    }
}
