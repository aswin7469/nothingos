package com.android.systemui.media;

import android.app.smartspace.SmartspaceAction;
import android.content.Intent;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: SmartspaceMediaData.kt */
/* loaded from: classes.dex */
public final class SmartspaceMediaData {
    private final int backgroundColor;
    @Nullable
    private final SmartspaceAction cardAction;
    @Nullable
    private final Intent dismissIntent;
    private final boolean isActive;
    private final boolean isValid;
    @NotNull
    private final String packageName;
    @NotNull
    private final List<SmartspaceAction> recommendations;
    @NotNull
    private final String targetId;

    @NotNull
    public final SmartspaceMediaData copy(@NotNull String targetId, boolean z, boolean z2, @NotNull String packageName, @Nullable SmartspaceAction smartspaceAction, @NotNull List<SmartspaceAction> recommendations, @Nullable Intent intent, int i) {
        Intrinsics.checkNotNullParameter(targetId, "targetId");
        Intrinsics.checkNotNullParameter(packageName, "packageName");
        Intrinsics.checkNotNullParameter(recommendations, "recommendations");
        return new SmartspaceMediaData(targetId, z, z2, packageName, smartspaceAction, recommendations, intent, i);
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SmartspaceMediaData)) {
            return false;
        }
        SmartspaceMediaData smartspaceMediaData = (SmartspaceMediaData) obj;
        return Intrinsics.areEqual(this.targetId, smartspaceMediaData.targetId) && this.isActive == smartspaceMediaData.isActive && this.isValid == smartspaceMediaData.isValid && Intrinsics.areEqual(this.packageName, smartspaceMediaData.packageName) && Intrinsics.areEqual(this.cardAction, smartspaceMediaData.cardAction) && Intrinsics.areEqual(this.recommendations, smartspaceMediaData.recommendations) && Intrinsics.areEqual(this.dismissIntent, smartspaceMediaData.dismissIntent) && this.backgroundColor == smartspaceMediaData.backgroundColor;
    }

    public int hashCode() {
        int hashCode = this.targetId.hashCode() * 31;
        boolean z = this.isActive;
        int i = 1;
        if (z) {
            z = true;
        }
        int i2 = z ? 1 : 0;
        int i3 = z ? 1 : 0;
        int i4 = (hashCode + i2) * 31;
        boolean z2 = this.isValid;
        if (!z2) {
            i = z2 ? 1 : 0;
        }
        int hashCode2 = (((i4 + i) * 31) + this.packageName.hashCode()) * 31;
        SmartspaceAction smartspaceAction = this.cardAction;
        int i5 = 0;
        int hashCode3 = (((hashCode2 + (smartspaceAction == null ? 0 : smartspaceAction.hashCode())) * 31) + this.recommendations.hashCode()) * 31;
        Intent intent = this.dismissIntent;
        if (intent != null) {
            i5 = intent.hashCode();
        }
        return ((hashCode3 + i5) * 31) + Integer.hashCode(this.backgroundColor);
    }

    @NotNull
    public String toString() {
        return "SmartspaceMediaData(targetId=" + this.targetId + ", isActive=" + this.isActive + ", isValid=" + this.isValid + ", packageName=" + this.packageName + ", cardAction=" + this.cardAction + ", recommendations=" + this.recommendations + ", dismissIntent=" + this.dismissIntent + ", backgroundColor=" + this.backgroundColor + ')';
    }

    public SmartspaceMediaData(@NotNull String targetId, boolean z, boolean z2, @NotNull String packageName, @Nullable SmartspaceAction smartspaceAction, @NotNull List<SmartspaceAction> recommendations, @Nullable Intent intent, int i) {
        Intrinsics.checkNotNullParameter(targetId, "targetId");
        Intrinsics.checkNotNullParameter(packageName, "packageName");
        Intrinsics.checkNotNullParameter(recommendations, "recommendations");
        this.targetId = targetId;
        this.isActive = z;
        this.isValid = z2;
        this.packageName = packageName;
        this.cardAction = smartspaceAction;
        this.recommendations = recommendations;
        this.dismissIntent = intent;
        this.backgroundColor = i;
    }

    @NotNull
    public final String getTargetId() {
        return this.targetId;
    }

    public final boolean isActive() {
        return this.isActive;
    }

    public final boolean isValid() {
        return this.isValid;
    }

    @NotNull
    public final String getPackageName() {
        return this.packageName;
    }

    @Nullable
    public final SmartspaceAction getCardAction() {
        return this.cardAction;
    }

    @NotNull
    public final List<SmartspaceAction> getRecommendations() {
        return this.recommendations;
    }

    @Nullable
    public final Intent getDismissIntent() {
        return this.dismissIntent;
    }

    public final int getBackgroundColor() {
        return this.backgroundColor;
    }
}
