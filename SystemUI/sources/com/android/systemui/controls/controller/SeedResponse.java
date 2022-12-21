package com.android.systemui.controls.controller;

import com.google.android.setupcompat.internal.FocusChangedMetricHelper;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\b\u0002\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\t\u0010\f\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u00052\b\u0010\u000f\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001J\t\u0010\u0012\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0013"}, mo64987d2 = {"Lcom/android/systemui/controls/controller/SeedResponse;", "", "packageName", "", "accepted", "", "(Ljava/lang/String;Z)V", "getAccepted", "()Z", "getPackageName", "()Ljava/lang/String;", "component1", "component2", "copy", "equals", "other", "hashCode", "", "toString", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlsController.kt */
public final class SeedResponse {
    private final boolean accepted;
    private final String packageName;

    public static /* synthetic */ SeedResponse copy$default(SeedResponse seedResponse, String str, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            str = seedResponse.packageName;
        }
        if ((i & 2) != 0) {
            z = seedResponse.accepted;
        }
        return seedResponse.copy(str, z);
    }

    public final String component1() {
        return this.packageName;
    }

    public final boolean component2() {
        return this.accepted;
    }

    public final SeedResponse copy(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        return new SeedResponse(str, z);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SeedResponse)) {
            return false;
        }
        SeedResponse seedResponse = (SeedResponse) obj;
        return Intrinsics.areEqual((Object) this.packageName, (Object) seedResponse.packageName) && this.accepted == seedResponse.accepted;
    }

    public int hashCode() {
        int hashCode = this.packageName.hashCode() * 31;
        boolean z = this.accepted;
        if (z) {
            z = true;
        }
        return hashCode + (z ? 1 : 0);
    }

    public String toString() {
        return "SeedResponse(packageName=" + this.packageName + ", accepted=" + this.accepted + ')';
    }

    public SeedResponse(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        this.packageName = str;
        this.accepted = z;
    }

    public final boolean getAccepted() {
        return this.accepted;
    }

    public final String getPackageName() {
        return this.packageName;
    }
}
