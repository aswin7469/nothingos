package com.android.systemui.accessibility.floatingmenu;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;
import com.android.systemui.C1893R;
import java.lang.ref.WeakReference;

final class ItemDelegateCompat extends RecyclerViewAccessibilityDelegate.ItemDelegate {
    private final WeakReference<AccessibilityFloatingMenuView> mMenuViewRef;

    ItemDelegateCompat(RecyclerViewAccessibilityDelegate recyclerViewAccessibilityDelegate, AccessibilityFloatingMenuView accessibilityFloatingMenuView) {
        super(recyclerViewAccessibilityDelegate);
        this.mMenuViewRef = new WeakReference<>(accessibilityFloatingMenuView);
    }

    public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
        if (this.mMenuViewRef.get() != null) {
            AccessibilityFloatingMenuView accessibilityFloatingMenuView = this.mMenuViewRef.get();
            Resources resources = accessibilityFloatingMenuView.getResources();
            accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(C1893R.C1897id.action_move_top_left, resources.getString(C1893R.string.accessibility_floating_button_action_move_top_left)));
            accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(C1893R.C1897id.action_move_top_right, resources.getString(C1893R.string.accessibility_floating_button_action_move_top_right)));
            accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(C1893R.C1897id.action_move_bottom_left, resources.getString(C1893R.string.accessibility_floating_button_action_move_bottom_left)));
            accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(C1893R.C1897id.action_move_bottom_right, resources.getString(C1893R.string.accessibility_floating_button_action_move_bottom_right)));
            accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(accessibilityFloatingMenuView.isOvalShape() ? C1893R.C1897id.action_move_to_edge_and_hide : C1893R.C1897id.action_move_out_edge_and_show, resources.getString(accessibilityFloatingMenuView.isOvalShape() ? C1893R.string.f261x82520f4a : C1893R.string.accessibility_floating_button_action_move_out_edge_and_show)));
        }
    }

    public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
        if (this.mMenuViewRef.get() == null) {
            return super.performAccessibilityAction(view, i, bundle);
        }
        AccessibilityFloatingMenuView accessibilityFloatingMenuView = this.mMenuViewRef.get();
        accessibilityFloatingMenuView.fadeIn();
        Rect availableBounds = accessibilityFloatingMenuView.getAvailableBounds();
        if (i == C1893R.C1897id.action_move_top_left) {
            accessibilityFloatingMenuView.setShapeType(0);
            accessibilityFloatingMenuView.snapToLocation(availableBounds.left, availableBounds.top);
            return true;
        } else if (i == C1893R.C1897id.action_move_top_right) {
            accessibilityFloatingMenuView.setShapeType(0);
            accessibilityFloatingMenuView.snapToLocation(availableBounds.right, availableBounds.top);
            return true;
        } else if (i == C1893R.C1897id.action_move_bottom_left) {
            accessibilityFloatingMenuView.setShapeType(0);
            accessibilityFloatingMenuView.snapToLocation(availableBounds.left, availableBounds.bottom);
            return true;
        } else if (i == C1893R.C1897id.action_move_bottom_right) {
            accessibilityFloatingMenuView.setShapeType(0);
            accessibilityFloatingMenuView.snapToLocation(availableBounds.right, availableBounds.bottom);
            return true;
        } else if (i == C1893R.C1897id.action_move_to_edge_and_hide) {
            accessibilityFloatingMenuView.setShapeType(1);
            return true;
        } else if (i != C1893R.C1897id.action_move_out_edge_and_show) {
            return super.performAccessibilityAction(view, i, bundle);
        } else {
            accessibilityFloatingMenuView.setShapeType(0);
            return true;
        }
    }
}
