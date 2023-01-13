package com.android.systemui.biometrics;

import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.app.NotificationCompat;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, mo65043d2 = {"com/android/systemui/biometrics/SidefpsController$createOverlayForDisplay$2", "Landroid/view/View$AccessibilityDelegate;", "dispatchPopulateAccessibilityEvent", "", "host", "Landroid/view/View;", "event", "Landroid/view/accessibility/AccessibilityEvent;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SidefpsController.kt */
public final class SidefpsController$createOverlayForDisplay$2 extends View.AccessibilityDelegate {
    SidefpsController$createOverlayForDisplay$2() {
    }

    public boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        Intrinsics.checkNotNullParameter(view, "host");
        Intrinsics.checkNotNullParameter(accessibilityEvent, NotificationCompat.CATEGORY_EVENT);
        if (accessibilityEvent.getEventType() == 32) {
            return true;
        }
        return super.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
    }
}
