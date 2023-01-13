package com.android.systemui.accessibility.floatingmenu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.recyclerview.widget.RecyclerView;
import com.android.internal.accessibility.dialog.AccessibilityTarget;
import com.android.systemui.C1894R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public class AccessibilityTargetAdapter extends RecyclerView.Adapter<ViewHolder> {
    private int mIconWidthHeight;
    private int mItemPadding;
    private final List<AccessibilityTarget> mTargets;

    @Retention(RetentionPolicy.SOURCE)
    @interface ItemType {
        public static final int FIRST_ITEM = 0;
        public static final int LAST_ITEM = 2;
        public static final int REGULAR_ITEM = 1;
    }

    public AccessibilityTargetAdapter(List<AccessibilityTarget> list) {
        this.mTargets = list;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(C1894R.layout.accessibility_floating_menu_item, viewGroup, false);
        if (i == 0) {
            return new TopViewHolder(inflate);
        }
        if (i == 2) {
            return new BottomViewHolder(inflate);
        }
        return new ViewHolder(inflate);
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        AccessibilityTarget accessibilityTarget = this.mTargets.get(i);
        viewHolder.mIconView.setBackground(accessibilityTarget.getIcon());
        viewHolder.updateIconWidthHeight(this.mIconWidthHeight);
        viewHolder.updateItemPadding(this.mItemPadding, getItemCount());
        viewHolder.itemView.setOnClickListener(new AccessibilityTargetAdapter$$ExternalSyntheticLambda0(accessibilityTarget));
        viewHolder.itemView.setStateDescription(accessibilityTarget.getStateDescription());
        viewHolder.itemView.setContentDescription(accessibilityTarget.getLabel());
        ViewCompat.replaceAccessibilityAction(viewHolder.itemView, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK, accessibilityTarget.getFragmentType() == 2 ? viewHolder.itemView.getResources().getString(C1894R.string.accessibility_floating_button_action_double_tap_to_toggle) : null, (AccessibilityViewCommand) null);
    }

    public int getItemViewType(int i) {
        if (i == 0) {
            return 0;
        }
        return i == getItemCount() - 1 ? 2 : 1;
    }

    public int getItemCount() {
        return this.mTargets.size();
    }

    public void setIconWidthHeight(int i) {
        this.mIconWidthHeight = i;
    }

    public void setItemPadding(int i) {
        this.mItemPadding = i;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final View mIconView;

        ViewHolder(View view) {
            super(view);
            this.mIconView = view.findViewById(C1894R.C1898id.icon_view);
        }

        /* access modifiers changed from: package-private */
        public void updateIconWidthHeight(int i) {
            ViewGroup.LayoutParams layoutParams = this.mIconView.getLayoutParams();
            if (layoutParams.width != i) {
                layoutParams.width = i;
                layoutParams.height = i;
                this.mIconView.setLayoutParams(layoutParams);
            }
        }

        /* access modifiers changed from: package-private */
        public void updateItemPadding(int i, int i2) {
            this.itemView.setPaddingRelative(i, i, i, 0);
        }
    }

    static class TopViewHolder extends ViewHolder {
        TopViewHolder(View view) {
            super(view);
        }

        /* access modifiers changed from: package-private */
        public void updateItemPadding(int i, int i2) {
            this.itemView.setPaddingRelative(i, i, i, i2 <= 1 ? i : 0);
        }
    }

    static class BottomViewHolder extends ViewHolder {
        BottomViewHolder(View view) {
            super(view);
        }

        /* access modifiers changed from: package-private */
        public void updateItemPadding(int i, int i2) {
            this.itemView.setPaddingRelative(i, i, i, i);
        }
    }
}
