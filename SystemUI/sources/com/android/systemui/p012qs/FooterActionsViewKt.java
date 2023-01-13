package com.android.systemui.p012qs;

import android.util.Log;
import android.view.MotionEvent;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000\"\u0018\u0010\u0004\u001a\u00020\u0001*\u00020\u00058BX\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007¨\u0006\b"}, mo65043d2 = {"TAG", "", "VERBOSE", "", "string", "Landroid/view/MotionEvent;", "getString", "(Landroid/view/MotionEvent;)Ljava/lang/String;", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.qs.FooterActionsViewKt */
/* compiled from: FooterActionsView.kt */
public final class FooterActionsViewKt {
    private static final String TAG = "FooterActionsView";
    /* access modifiers changed from: private */
    public static final boolean VERBOSE = Log.isLoggable(TAG, 2);

    /* access modifiers changed from: private */
    public static final String getString(MotionEvent motionEvent) {
        return NavigationBarInflaterView.KEY_CODE_START + motionEvent.getId() + "): (" + motionEvent.getX() + ',' + motionEvent.getY() + ')';
    }
}
