package com.android.wm.shell.bubbles.storage;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: BubbleEntity.kt */
/* loaded from: classes2.dex */
public final class BubbleEntity {
    private final int desiredHeight;
    private final int desiredHeightResId;
    @NotNull
    private final String key;
    @Nullable
    private final String locus;
    @NotNull
    private final String packageName;
    @NotNull
    private final String shortcutId;
    private final int taskId;
    @Nullable
    private final String title;
    private final int userId;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BubbleEntity)) {
            return false;
        }
        BubbleEntity bubbleEntity = (BubbleEntity) obj;
        return this.userId == bubbleEntity.userId && Intrinsics.areEqual(this.packageName, bubbleEntity.packageName) && Intrinsics.areEqual(this.shortcutId, bubbleEntity.shortcutId) && Intrinsics.areEqual(this.key, bubbleEntity.key) && this.desiredHeight == bubbleEntity.desiredHeight && this.desiredHeightResId == bubbleEntity.desiredHeightResId && Intrinsics.areEqual(this.title, bubbleEntity.title) && this.taskId == bubbleEntity.taskId && Intrinsics.areEqual(this.locus, bubbleEntity.locus);
    }

    public int hashCode() {
        int hashCode = ((((((((((Integer.hashCode(this.userId) * 31) + this.packageName.hashCode()) * 31) + this.shortcutId.hashCode()) * 31) + this.key.hashCode()) * 31) + Integer.hashCode(this.desiredHeight)) * 31) + Integer.hashCode(this.desiredHeightResId)) * 31;
        String str = this.title;
        int i = 0;
        int hashCode2 = (((hashCode + (str == null ? 0 : str.hashCode())) * 31) + Integer.hashCode(this.taskId)) * 31;
        String str2 = this.locus;
        if (str2 != null) {
            i = str2.hashCode();
        }
        return hashCode2 + i;
    }

    @NotNull
    public String toString() {
        return "BubbleEntity(userId=" + this.userId + ", packageName=" + this.packageName + ", shortcutId=" + this.shortcutId + ", key=" + this.key + ", desiredHeight=" + this.desiredHeight + ", desiredHeightResId=" + this.desiredHeightResId + ", title=" + ((Object) this.title) + ", taskId=" + this.taskId + ", locus=" + ((Object) this.locus) + ')';
    }

    public BubbleEntity(int i, @NotNull String packageName, @NotNull String shortcutId, @NotNull String key, int i2, int i3, @Nullable String str, int i4, @Nullable String str2) {
        Intrinsics.checkNotNullParameter(packageName, "packageName");
        Intrinsics.checkNotNullParameter(shortcutId, "shortcutId");
        Intrinsics.checkNotNullParameter(key, "key");
        this.userId = i;
        this.packageName = packageName;
        this.shortcutId = shortcutId;
        this.key = key;
        this.desiredHeight = i2;
        this.desiredHeightResId = i3;
        this.title = str;
        this.taskId = i4;
        this.locus = str2;
    }

    public final int getUserId() {
        return this.userId;
    }

    @NotNull
    public final String getPackageName() {
        return this.packageName;
    }

    @NotNull
    public final String getShortcutId() {
        return this.shortcutId;
    }

    @NotNull
    public final String getKey() {
        return this.key;
    }

    public final int getDesiredHeight() {
        return this.desiredHeight;
    }

    public final int getDesiredHeightResId() {
        return this.desiredHeightResId;
    }

    @Nullable
    public final String getTitle() {
        return this.title;
    }

    public final int getTaskId() {
        return this.taskId;
    }

    @Nullable
    public final String getLocus() {
        return this.locus;
    }
}
