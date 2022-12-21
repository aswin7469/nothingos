package com.android.systemui.privacy;

import com.google.android.setupcompat.internal.FocusChangedMetricHelper;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0004\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\t\u0010\f\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0005HÖ\u0001J\t\u0010\u0012\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0013"}, mo64987d2 = {"Lcom/android/systemui/privacy/PrivacyApplication;", "", "packageName", "", "uid", "", "(Ljava/lang/String;I)V", "getPackageName", "()Ljava/lang/String;", "getUid", "()I", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PrivacyItem.kt */
public final class PrivacyApplication {
    private final String packageName;
    private final int uid;

    public static /* synthetic */ PrivacyApplication copy$default(PrivacyApplication privacyApplication, String str, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            str = privacyApplication.packageName;
        }
        if ((i2 & 2) != 0) {
            i = privacyApplication.uid;
        }
        return privacyApplication.copy(str, i);
    }

    public final String component1() {
        return this.packageName;
    }

    public final int component2() {
        return this.uid;
    }

    public final PrivacyApplication copy(String str, int i) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        return new PrivacyApplication(str, i);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PrivacyApplication)) {
            return false;
        }
        PrivacyApplication privacyApplication = (PrivacyApplication) obj;
        return Intrinsics.areEqual((Object) this.packageName, (Object) privacyApplication.packageName) && this.uid == privacyApplication.uid;
    }

    public int hashCode() {
        return (this.packageName.hashCode() * 31) + Integer.hashCode(this.uid);
    }

    public String toString() {
        return "PrivacyApplication(packageName=" + this.packageName + ", uid=" + this.uid + ')';
    }

    public PrivacyApplication(String str, int i) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        this.packageName = str;
        this.uid = i;
    }

    public final String getPackageName() {
        return this.packageName;
    }

    public final int getUid() {
        return this.uid;
    }
}
