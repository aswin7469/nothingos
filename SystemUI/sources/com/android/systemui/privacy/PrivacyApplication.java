package com.android.systemui.privacy;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: PrivacyItem.kt */
/* loaded from: classes.dex */
public final class PrivacyApplication {
    @NotNull
    private final String packageName;
    private final int uid;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PrivacyApplication)) {
            return false;
        }
        PrivacyApplication privacyApplication = (PrivacyApplication) obj;
        return Intrinsics.areEqual(this.packageName, privacyApplication.packageName) && this.uid == privacyApplication.uid;
    }

    public int hashCode() {
        return (this.packageName.hashCode() * 31) + Integer.hashCode(this.uid);
    }

    @NotNull
    public String toString() {
        return "PrivacyApplication(packageName=" + this.packageName + ", uid=" + this.uid + ')';
    }

    public PrivacyApplication(@NotNull String packageName, int i) {
        Intrinsics.checkNotNullParameter(packageName, "packageName");
        this.packageName = packageName;
        this.uid = i;
    }

    @NotNull
    public final String getPackageName() {
        return this.packageName;
    }

    public final int getUid() {
        return this.uid;
    }
}
