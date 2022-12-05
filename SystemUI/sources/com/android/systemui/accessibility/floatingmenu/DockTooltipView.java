package com.android.systemui.accessibility.floatingmenu;

import android.content.Context;
import com.android.systemui.R$string;
/* loaded from: classes.dex */
class DockTooltipView extends BaseTooltipView {
    private final AccessibilityFloatingMenuView mAnchorView;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DockTooltipView(Context context, AccessibilityFloatingMenuView accessibilityFloatingMenuView) {
        super(context, accessibilityFloatingMenuView);
        this.mAnchorView = accessibilityFloatingMenuView;
        setDescription(getContext().getText(R$string.accessibility_floating_button_docking_tooltip));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.systemui.accessibility.floatingmenu.BaseTooltipView
    public void hide() {
        super.hide();
        this.mAnchorView.stopTranslateXAnimation();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.systemui.accessibility.floatingmenu.BaseTooltipView
    public void show() {
        super.show();
        this.mAnchorView.startTranslateXAnimation();
    }
}
