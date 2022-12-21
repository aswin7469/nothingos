package com.android.systemui.controls.p010ui;

import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.app.NotificationCompat;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000=\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J \u0010\b\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\rH\u0016J\"\u0010\u000e\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0016¨\u0006\u0013"}, mo64987d2 = {"com/android/systemui/controls/ui/ToggleRangeBehavior$bind$1", "Landroid/view/View$AccessibilityDelegate;", "onInitializeAccessibilityNodeInfo", "", "host", "Landroid/view/View;", "info", "Landroid/view/accessibility/AccessibilityNodeInfo;", "onRequestSendAccessibilityEvent", "", "Landroid/view/ViewGroup;", "child", "event", "Landroid/view/accessibility/AccessibilityEvent;", "performAccessibilityAction", "action", "", "arguments", "Landroid/os/Bundle;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.controls.ui.ToggleRangeBehavior$bind$1 */
/* compiled from: ToggleRangeBehavior.kt */
public final class ToggleRangeBehavior$bind$1 extends View.AccessibilityDelegate {
    final /* synthetic */ ToggleRangeBehavior this$0;

    public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
        Intrinsics.checkNotNullParameter(viewGroup, "host");
        Intrinsics.checkNotNullParameter(view, "child");
        Intrinsics.checkNotNullParameter(accessibilityEvent, NotificationCompat.CATEGORY_EVENT);
        return true;
    }

    ToggleRangeBehavior$bind$1(ToggleRangeBehavior toggleRangeBehavior) {
        this.this$0 = toggleRangeBehavior;
    }

    public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
        Intrinsics.checkNotNullParameter(view, "host");
        Intrinsics.checkNotNullParameter(accessibilityNodeInfo, "info");
        super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
        int i = 0;
        float access$levelToRangeValue = this.this$0.levelToRangeValue(0);
        ToggleRangeBehavior toggleRangeBehavior = this.this$0;
        float access$levelToRangeValue2 = toggleRangeBehavior.levelToRangeValue(toggleRangeBehavior.getClipLayer().getLevel());
        float access$levelToRangeValue3 = this.this$0.levelToRangeValue(10000);
        double stepValue = (double) this.this$0.getRangeTemplate().getStepValue();
        if (stepValue == Math.floor(stepValue)) {
            i = 1;
        }
        int i2 = i ^ 1;
        if (this.this$0.isChecked()) {
            accessibilityNodeInfo.setRangeInfo(AccessibilityNodeInfo.RangeInfo.obtain(i2, access$levelToRangeValue, access$levelToRangeValue3, access$levelToRangeValue2));
        }
        accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS);
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0082  */
    /* JADX WARNING: Removed duplicated region for block: B:21:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean performAccessibilityAction(android.view.View r7, int r8, android.os.Bundle r9) {
        /*
            r6 = this;
            java.lang.String r0 = "host"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r7, r0)
            r0 = 16
            r1 = 0
            r2 = 1
            if (r8 != r0) goto L_0x0035
            com.android.systemui.controls.ui.ToggleRangeBehavior r0 = r6.this$0
            boolean r0 = r0.isToggleable()
            if (r0 != 0) goto L_0x0015
        L_0x0013:
            r0 = r1
            goto L_0x007a
        L_0x0015:
            com.android.systemui.controls.ui.ToggleRangeBehavior r0 = r6.this$0
            com.android.systemui.controls.ui.ControlViewHolder r0 = r0.getCvh()
            com.android.systemui.controls.ui.ControlActionCoordinator r0 = r0.getControlActionCoordinator()
            com.android.systemui.controls.ui.ToggleRangeBehavior r3 = r6.this$0
            com.android.systemui.controls.ui.ControlViewHolder r3 = r3.getCvh()
            com.android.systemui.controls.ui.ToggleRangeBehavior r4 = r6.this$0
            java.lang.String r4 = r4.getTemplateId()
            com.android.systemui.controls.ui.ToggleRangeBehavior r5 = r6.this$0
            boolean r5 = r5.isChecked()
            r0.toggle(r3, r4, r5)
            goto L_0x004c
        L_0x0035:
            r0 = 32
            if (r8 != r0) goto L_0x004e
            com.android.systemui.controls.ui.ToggleRangeBehavior r0 = r6.this$0
            com.android.systemui.controls.ui.ControlViewHolder r0 = r0.getCvh()
            com.android.systemui.controls.ui.ControlActionCoordinator r0 = r0.getControlActionCoordinator()
            com.android.systemui.controls.ui.ToggleRangeBehavior r3 = r6.this$0
            com.android.systemui.controls.ui.ControlViewHolder r3 = r3.getCvh()
            r0.longPress(r3)
        L_0x004c:
            r0 = r2
            goto L_0x007a
        L_0x004e:
            android.view.accessibility.AccessibilityNodeInfo$AccessibilityAction r0 = android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS
            int r0 = r0.getId()
            if (r8 != r0) goto L_0x0013
            if (r9 == 0) goto L_0x0013
            java.lang.String r0 = "android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE"
            boolean r3 = r9.containsKey(r0)
            if (r3 != 0) goto L_0x0061
            goto L_0x0013
        L_0x0061:
            float r0 = r9.getFloat(r0)
            com.android.systemui.controls.ui.ToggleRangeBehavior r3 = r6.this$0
            int r0 = r3.rangeToLevelValue(r0)
            com.android.systemui.controls.ui.ToggleRangeBehavior r3 = r6.this$0
            boolean r4 = r3.isChecked()
            r3.updateRange(r0, r4, r2)
            com.android.systemui.controls.ui.ToggleRangeBehavior r0 = r6.this$0
            r0.endUpdateRange()
            goto L_0x004c
        L_0x007a:
            if (r0 != 0) goto L_0x0082
            boolean r6 = super.performAccessibilityAction(r7, r8, r9)
            if (r6 == 0) goto L_0x0083
        L_0x0082:
            r1 = r2
        L_0x0083:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.p010ui.ToggleRangeBehavior$bind$1.performAccessibilityAction(android.view.View, int, android.os.Bundle):boolean");
    }
}
