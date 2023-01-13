package com.android.systemui.accessibility.floatingmenu;

import android.content.Context;
import com.android.systemui.C1894R;

class DockTooltipView extends BaseTooltipView {
    private final AccessibilityFloatingMenuView mAnchorView;

    DockTooltipView(Context context, AccessibilityFloatingMenuView accessibilityFloatingMenuView) {
        super(context, accessibilityFloatingMenuView);
        this.mAnchorView = accessibilityFloatingMenuView;
        setDescription(getContext().getText(C1894R.string.accessibility_floating_button_docking_tooltip));
    }

    /* access modifiers changed from: package-private */
    public void hide() {
        super.hide();
        this.mAnchorView.stopTranslateXAnimation();
    }

    /* access modifiers changed from: package-private */
    public void show() {
        super.show();
        this.mAnchorView.startTranslateXAnimation();
    }
}
