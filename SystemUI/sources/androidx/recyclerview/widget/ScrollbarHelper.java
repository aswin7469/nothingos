package androidx.recyclerview.widget;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
/* loaded from: classes.dex */
class ScrollbarHelper {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static int computeScrollOffset(RecyclerView.State state, OrientationHelper orientation, View startChild, View endChild, RecyclerView.LayoutManager lm, boolean smoothScrollbarEnabled, boolean reverseLayout) {
        int max;
        if (lm.getChildCount() == 0 || state.getItemCount() == 0 || startChild == null || endChild == null) {
            return 0;
        }
        int min = Math.min(lm.getPosition(startChild), lm.getPosition(endChild));
        int max2 = Math.max(lm.getPosition(startChild), lm.getPosition(endChild));
        if (reverseLayout) {
            max = Math.max(0, (state.getItemCount() - max2) - 1);
        } else {
            max = Math.max(0, min);
        }
        if (!smoothScrollbarEnabled) {
            return max;
        }
        return Math.round((max * (Math.abs(orientation.getDecoratedEnd(endChild) - orientation.getDecoratedStart(startChild)) / (Math.abs(lm.getPosition(startChild) - lm.getPosition(endChild)) + 1))) + (orientation.getStartAfterPadding() - orientation.getDecoratedStart(startChild)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int computeScrollExtent(RecyclerView.State state, OrientationHelper orientation, View startChild, View endChild, RecyclerView.LayoutManager lm, boolean smoothScrollbarEnabled) {
        if (lm.getChildCount() == 0 || state.getItemCount() == 0 || startChild == null || endChild == null) {
            return 0;
        }
        if (!smoothScrollbarEnabled) {
            return Math.abs(lm.getPosition(startChild) - lm.getPosition(endChild)) + 1;
        }
        return Math.min(orientation.getTotalSpace(), orientation.getDecoratedEnd(endChild) - orientation.getDecoratedStart(startChild));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int computeScrollRange(RecyclerView.State state, OrientationHelper orientation, View startChild, View endChild, RecyclerView.LayoutManager lm, boolean smoothScrollbarEnabled) {
        if (lm.getChildCount() == 0 || state.getItemCount() == 0 || startChild == null || endChild == null) {
            return 0;
        }
        if (!smoothScrollbarEnabled) {
            return state.getItemCount();
        }
        return (int) (((orientation.getDecoratedEnd(endChild) - orientation.getDecoratedStart(startChild)) / (Math.abs(lm.getPosition(startChild) - lm.getPosition(endChild)) + 1)) * state.getItemCount());
    }
}
