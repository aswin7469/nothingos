package com.android.systemui.statusbar.notification;

import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\t\u0010\t\u001a\u00020\u0003HÆ\u0003J\t\u0010\n\u001a\u00020\u0003HÆ\u0003J\u001d\u0010\u000b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u000f\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007¨\u0006\u0012"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/FeedbackIcon;", "", "iconRes", "", "contentDescRes", "(II)V", "getContentDescRes", "()I", "getIconRes", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: FeedbackIcon.kt */
public final class FeedbackIcon {
    private final int contentDescRes;
    private final int iconRes;

    public static /* synthetic */ FeedbackIcon copy$default(FeedbackIcon feedbackIcon, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = feedbackIcon.iconRes;
        }
        if ((i3 & 2) != 0) {
            i2 = feedbackIcon.contentDescRes;
        }
        return feedbackIcon.copy(i, i2);
    }

    public final int component1() {
        return this.iconRes;
    }

    public final int component2() {
        return this.contentDescRes;
    }

    public final FeedbackIcon copy(int i, int i2) {
        return new FeedbackIcon(i, i2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FeedbackIcon)) {
            return false;
        }
        FeedbackIcon feedbackIcon = (FeedbackIcon) obj;
        return this.iconRes == feedbackIcon.iconRes && this.contentDescRes == feedbackIcon.contentDescRes;
    }

    public int hashCode() {
        return (Integer.hashCode(this.iconRes) * 31) + Integer.hashCode(this.contentDescRes);
    }

    public String toString() {
        return "FeedbackIcon(iconRes=" + this.iconRes + ", contentDescRes=" + this.contentDescRes + ')';
    }

    public FeedbackIcon(int i, int i2) {
        this.iconRes = i;
        this.contentDescRes = i2;
    }

    public final int getIconRes() {
        return this.iconRes;
    }

    public final int getContentDescRes() {
        return this.contentDescRes;
    }
}
