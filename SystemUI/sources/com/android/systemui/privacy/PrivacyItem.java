package com.android.systemui.privacy;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0010\n\u0002\u0010\b\n\u0002\b\u0002\b\b\u0018\u00002\u00020\u0001B)\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\t\u0010\u0017\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0018\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0019\u001a\u00020\u0007HÆ\u0003J\t\u0010\u001a\u001a\u00020\tHÆ\u0003J1\u0010\u001b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\tHÆ\u0001J\u0013\u0010\u001c\u001a\u00020\t2\b\u0010\u001d\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001e\u001a\u00020\u001fHÖ\u0001J\t\u0010 \u001a\u00020\u000eHÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016¨\u0006!"}, mo64987d2 = {"Lcom/android/systemui/privacy/PrivacyItem;", "", "privacyType", "Lcom/android/systemui/privacy/PrivacyType;", "application", "Lcom/android/systemui/privacy/PrivacyApplication;", "timeStampElapsed", "", "paused", "", "(Lcom/android/systemui/privacy/PrivacyType;Lcom/android/systemui/privacy/PrivacyApplication;JZ)V", "getApplication", "()Lcom/android/systemui/privacy/PrivacyApplication;", "log", "", "getLog", "()Ljava/lang/String;", "getPaused", "()Z", "getPrivacyType", "()Lcom/android/systemui/privacy/PrivacyType;", "getTimeStampElapsed", "()J", "component1", "component2", "component3", "component4", "copy", "equals", "other", "hashCode", "", "toString", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PrivacyItem.kt */
public final class PrivacyItem {
    private final PrivacyApplication application;
    private final String log;
    private final boolean paused;
    private final PrivacyType privacyType;
    private final long timeStampElapsed;

    public static /* synthetic */ PrivacyItem copy$default(PrivacyItem privacyItem, PrivacyType privacyType2, PrivacyApplication privacyApplication, long j, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            privacyType2 = privacyItem.privacyType;
        }
        if ((i & 2) != 0) {
            privacyApplication = privacyItem.application;
        }
        PrivacyApplication privacyApplication2 = privacyApplication;
        if ((i & 4) != 0) {
            j = privacyItem.timeStampElapsed;
        }
        long j2 = j;
        if ((i & 8) != 0) {
            z = privacyItem.paused;
        }
        return privacyItem.copy(privacyType2, privacyApplication2, j2, z);
    }

    public final PrivacyType component1() {
        return this.privacyType;
    }

    public final PrivacyApplication component2() {
        return this.application;
    }

    public final long component3() {
        return this.timeStampElapsed;
    }

    public final boolean component4() {
        return this.paused;
    }

    public final PrivacyItem copy(PrivacyType privacyType2, PrivacyApplication privacyApplication, long j, boolean z) {
        Intrinsics.checkNotNullParameter(privacyType2, "privacyType");
        Intrinsics.checkNotNullParameter(privacyApplication, "application");
        return new PrivacyItem(privacyType2, privacyApplication, j, z);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PrivacyItem)) {
            return false;
        }
        PrivacyItem privacyItem = (PrivacyItem) obj;
        return this.privacyType == privacyItem.privacyType && Intrinsics.areEqual((Object) this.application, (Object) privacyItem.application) && this.timeStampElapsed == privacyItem.timeStampElapsed && this.paused == privacyItem.paused;
    }

    public int hashCode() {
        int hashCode = ((((this.privacyType.hashCode() * 31) + this.application.hashCode()) * 31) + Long.hashCode(this.timeStampElapsed)) * 31;
        boolean z = this.paused;
        if (z) {
            z = true;
        }
        return hashCode + (z ? 1 : 0);
    }

    public String toString() {
        return "PrivacyItem(privacyType=" + this.privacyType + ", application=" + this.application + ", timeStampElapsed=" + this.timeStampElapsed + ", paused=" + this.paused + ')';
    }

    public PrivacyItem(PrivacyType privacyType2, PrivacyApplication privacyApplication, long j, boolean z) {
        Intrinsics.checkNotNullParameter(privacyType2, "privacyType");
        Intrinsics.checkNotNullParameter(privacyApplication, "application");
        this.privacyType = privacyType2;
        this.application = privacyApplication;
        this.timeStampElapsed = j;
        this.paused = z;
        this.log = NavigationBarInflaterView.KEY_CODE_START + privacyType2.getLogName() + ", " + privacyApplication.getPackageName() + '(' + privacyApplication.getUid() + "), " + j + ", paused=" + z + ')';
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ PrivacyItem(PrivacyType privacyType2, PrivacyApplication privacyApplication, long j, boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(privacyType2, privacyApplication, (i & 4) != 0 ? -1 : j, (i & 8) != 0 ? false : z);
    }

    public final PrivacyType getPrivacyType() {
        return this.privacyType;
    }

    public final PrivacyApplication getApplication() {
        return this.application;
    }

    public final long getTimeStampElapsed() {
        return this.timeStampElapsed;
    }

    public final boolean getPaused() {
        return this.paused;
    }

    public final String getLog() {
        return this.log;
    }
}
