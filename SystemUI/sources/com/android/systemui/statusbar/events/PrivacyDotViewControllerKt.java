package com.android.systemui.statusbar.events;

import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000(\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\u001a\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\tH\u0002\u001a\u0010\u0010\u000f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\tH\u0002\u001a\f\u0010\u0010\u001a\u00020\u0001*\u00020\u0001H\u0002\u001a\f\u0010\u0011\u001a\u00020\u0001*\u00020\u0001H\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0007XT¢\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\tXT¢\u0006\u0002\n\u0000\"\u000e\u0010\n\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u000b\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000¨\u0006\u0012"}, mo65043d2 = {"BOTTOM_LEFT", "", "BOTTOM_RIGHT", "DEBUG", "", "DEBUG_VERBOSE", "DURATION", "", "TAG", "", "TOP_LEFT", "TOP_RIGHT", "dlog", "", "s", "vlog", "innerGravity", "toGravity", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PrivacyDotViewController.kt */
public final class PrivacyDotViewControllerKt {
    public static final int BOTTOM_LEFT = 3;
    public static final int BOTTOM_RIGHT = 2;
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_VERBOSE = false;
    private static final long DURATION = 160;
    private static final String TAG = "PrivacyDotViewController";
    public static final int TOP_LEFT = 0;
    public static final int TOP_RIGHT = 1;

    /* access modifiers changed from: private */
    public static final void dlog(String str) {
    }

    private static final void vlog(String str) {
    }

    /* access modifiers changed from: private */
    public static final int toGravity(int i) {
        if (i == 0) {
            return 51;
        }
        if (i == 1) {
            return 53;
        }
        if (i == 2) {
            return 85;
        }
        if (i == 3) {
            return 83;
        }
        throw new IllegalArgumentException("Not a corner");
    }

    /* access modifiers changed from: private */
    public static final int innerGravity(int i) {
        if (i == 0) {
            return 21;
        }
        if (i == 1 || i == 2) {
            return 19;
        }
        if (i == 3) {
            return 21;
        }
        throw new IllegalArgumentException("Not a corner");
    }
}
