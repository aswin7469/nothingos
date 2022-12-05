package com.android.systemui.statusbar.events;
/* compiled from: PrivacyDotViewController.kt */
/* loaded from: classes.dex */
public final class PrivacyDotViewControllerKt {
    /* JADX INFO: Access modifiers changed from: private */
    public static final void dlog(String str) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int toGravity(int i) {
        if (i != 0) {
            if (i == 1) {
                return 53;
            }
            if (i == 2) {
                return 85;
            }
            if (i != 3) {
                throw new IllegalArgumentException("Not a corner");
            }
            return 83;
        }
        return 51;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int innerGravity(int i) {
        if (i != 0) {
            if (i == 1 || i == 2) {
                return 19;
            }
            if (i != 3) {
                throw new IllegalArgumentException("Not a corner");
            }
        }
        return 21;
    }
}
