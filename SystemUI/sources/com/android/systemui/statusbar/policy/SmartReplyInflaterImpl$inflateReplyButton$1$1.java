package com.android.systemui.statusbar.policy;

import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import com.android.systemui.C1894R;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, mo65043d2 = {"com/android/systemui/statusbar/policy/SmartReplyInflaterImpl$inflateReplyButton$1$1", "Landroid/view/View$AccessibilityDelegate;", "onInitializeAccessibilityNodeInfo", "", "host", "Landroid/view/View;", "info", "Landroid/view/accessibility/AccessibilityNodeInfo;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SmartReplyStateInflater.kt */
public final class SmartReplyInflaterImpl$inflateReplyButton$1$1 extends View.AccessibilityDelegate {
    final /* synthetic */ SmartReplyView $parent;

    SmartReplyInflaterImpl$inflateReplyButton$1$1(SmartReplyView smartReplyView) {
        this.$parent = smartReplyView;
    }

    public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
        Intrinsics.checkNotNullParameter(view, "host");
        Intrinsics.checkNotNullParameter(accessibilityNodeInfo, "info");
        super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
        accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, this.$parent.getResources().getString(C1894R.string.accessibility_send_smart_reply)));
    }
}
