package com.android.settings.localepicker;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
/* loaded from: classes.dex */
public class LocaleLinearLayoutManager extends LinearLayoutManager {
    private final AccessibilityNodeInfoCompat.AccessibilityActionCompat mActionMoveBottom;
    private final AccessibilityNodeInfoCompat.AccessibilityActionCompat mActionMoveDown;
    private final AccessibilityNodeInfoCompat.AccessibilityActionCompat mActionMoveTop;
    private final AccessibilityNodeInfoCompat.AccessibilityActionCompat mActionMoveUp;
    private final AccessibilityNodeInfoCompat.AccessibilityActionCompat mActionRemove;
    private final LocaleDragAndDropAdapter mAdapter;
    private final Context mContext;

    public LocaleLinearLayoutManager(Context context, LocaleDragAndDropAdapter localeDragAndDropAdapter) {
        super(context);
        this.mContext = context;
        this.mAdapter = localeDragAndDropAdapter;
        this.mActionMoveUp = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(R.id.action_drag_move_up, context.getString(R.string.action_drag_label_move_up));
        this.mActionMoveDown = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(R.id.action_drag_move_down, context.getString(R.string.action_drag_label_move_down));
        this.mActionMoveTop = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(R.id.action_drag_move_top, context.getString(R.string.action_drag_label_move_top));
        this.mActionMoveBottom = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(R.id.action_drag_move_bottom, context.getString(R.string.action_drag_label_move_bottom));
        this.mActionRemove = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(R.id.action_drag_remove, context.getString(R.string.action_drag_label_remove));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler recycler, RecyclerView.State state, View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        super.onInitializeAccessibilityNodeInfoForItem(recycler, state, view, accessibilityNodeInfoCompat);
        int itemCount = getItemCount();
        int position = getPosition(view);
        StringBuilder sb = new StringBuilder();
        int i = position + 1;
        sb.append(i);
        sb.append(", ");
        sb.append((Object) ((LocaleDragCell) view).getCheckbox().getContentDescription());
        accessibilityNodeInfoCompat.setContentDescription(sb.toString());
        if (this.mAdapter.isRemoveMode()) {
            return;
        }
        if (position > 0) {
            accessibilityNodeInfoCompat.addAction(this.mActionMoveUp);
            accessibilityNodeInfoCompat.addAction(this.mActionMoveTop);
        }
        if (i < itemCount) {
            accessibilityNodeInfoCompat.addAction(this.mActionMoveDown);
            accessibilityNodeInfoCompat.addAction(this.mActionMoveBottom);
        }
        if (itemCount <= 1) {
            return;
        }
        accessibilityNodeInfoCompat.addAction(this.mActionRemove);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x004e  */
    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean performAccessibilityActionForItem(RecyclerView.Recycler recycler, RecyclerView.State state, View view, int i, Bundle bundle) {
        int itemCount = getItemCount();
        int position = getPosition(view);
        boolean z = false;
        if (i == R.id.action_drag_move_up) {
            if (position > 0) {
                this.mAdapter.onItemMove(position, position - 1);
                z = true;
            }
            if (z) {
                this.mAdapter.doTheUpdate();
            }
            return z;
        } else if (i == R.id.action_drag_move_down) {
            int i2 = position + 1;
            if (i2 < itemCount) {
                this.mAdapter.onItemMove(position, i2);
                z = true;
            }
            if (z) {
            }
            return z;
        } else if (i == R.id.action_drag_move_top) {
            if (position != 0) {
                this.mAdapter.onItemMove(position, 0);
                z = true;
            }
            if (z) {
            }
            return z;
        } else if (i == R.id.action_drag_move_bottom) {
            int i3 = itemCount - 1;
            if (position != i3) {
                this.mAdapter.onItemMove(position, i3);
                z = true;
            }
            if (z) {
            }
            return z;
        } else if (i != R.id.action_drag_remove) {
            return super.performAccessibilityActionForItem(recycler, state, view, i, bundle);
        } else {
            if (itemCount > 1) {
                this.mAdapter.removeItem(position);
                z = true;
            }
            if (z) {
            }
            return z;
        }
    }
}
