package com.android.systemui.statusbar.phone.panelstate;

import kotlin.Metadata;
import kotlin.jvm.internal.Reflection;

@Metadata(mo65042d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\u001a\f\u0010\b\u001a\u00020\u0007*\u00020\u0003H\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000\"\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0004¢\u0006\u0002\n\u0000¨\u0006\t"}, mo65043d2 = {"DEBUG", "", "STATE_CLOSED", "", "STATE_OPEN", "STATE_OPENING", "TAG", "", "stateToString", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PanelExpansionStateManager.kt */
public final class PanelExpansionStateManagerKt {
    private static final boolean DEBUG = false;
    public static final int STATE_CLOSED = 0;
    public static final int STATE_OPEN = 2;
    public static final int STATE_OPENING = 1;
    /* access modifiers changed from: private */
    public static final String TAG = Reflection.getOrCreateKotlinClass(PanelExpansionStateManager.class).getSimpleName();

    /* access modifiers changed from: private */
    public static final String stateToString(int i) {
        if (i == 0) {
            return "CLOSED";
        }
        if (i != 1) {
            return i != 2 ? String.valueOf(i) : "OPEN";
        }
        return "OPENING";
    }
}
