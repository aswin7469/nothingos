package com.android.systemui.privacy;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: PrivacyItem.kt */
/* loaded from: classes.dex */
public final class PrivacyItem {
    @NotNull
    private final PrivacyApplication application;
    @NotNull
    private final String log;
    private final boolean paused;
    @NotNull
    private final PrivacyType privacyType;
    private final long timeStampElapsed;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PrivacyItem)) {
            return false;
        }
        PrivacyItem privacyItem = (PrivacyItem) obj;
        return this.privacyType == privacyItem.privacyType && Intrinsics.areEqual(this.application, privacyItem.application) && this.timeStampElapsed == privacyItem.timeStampElapsed && this.paused == privacyItem.paused;
    }

    public int hashCode() {
        int hashCode = ((((this.privacyType.hashCode() * 31) + this.application.hashCode()) * 31) + Long.hashCode(this.timeStampElapsed)) * 31;
        boolean z = this.paused;
        if (z) {
            z = true;
        }
        int i = z ? 1 : 0;
        int i2 = z ? 1 : 0;
        return hashCode + i;
    }

    @NotNull
    public String toString() {
        return "PrivacyItem(privacyType=" + this.privacyType + ", application=" + this.application + ", timeStampElapsed=" + this.timeStampElapsed + ", paused=" + this.paused + ')';
    }

    public PrivacyItem(@NotNull PrivacyType privacyType, @NotNull PrivacyApplication application, long j, boolean z) {
        Intrinsics.checkNotNullParameter(privacyType, "privacyType");
        Intrinsics.checkNotNullParameter(application, "application");
        this.privacyType = privacyType;
        this.application = application;
        this.timeStampElapsed = j;
        this.paused = z;
        this.log = '(' + privacyType.getLogName() + ", " + application.getPackageName() + '(' + application.getUid() + "), " + j + ", paused=" + z + ')';
    }

    @NotNull
    public final PrivacyType getPrivacyType() {
        return this.privacyType;
    }

    @NotNull
    public final PrivacyApplication getApplication() {
        return this.application;
    }

    public final long getTimeStampElapsed() {
        return this.timeStampElapsed;
    }

    public final boolean getPaused() {
        return this.paused;
    }

    @NotNull
    public final String getLog() {
        return this.log;
    }
}
