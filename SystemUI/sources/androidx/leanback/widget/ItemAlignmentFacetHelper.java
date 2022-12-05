package androidx.leanback.widget;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import androidx.leanback.widget.GridLayoutManager;
import androidx.leanback.widget.ItemAlignmentFacet;
/* loaded from: classes.dex */
class ItemAlignmentFacetHelper {
    private static Rect sRect = new Rect();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getAlignmentPosition(View itemView, ItemAlignmentFacet.ItemAlignmentDef facet, int orientation) {
        View view;
        int i;
        int width;
        int width2;
        int width3;
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) itemView.getLayoutParams();
        int i2 = facet.mViewId;
        if (i2 == 0 || (view = itemView.findViewById(i2)) == null) {
            view = itemView;
        }
        int i3 = facet.mOffset;
        if (orientation == 0) {
            if (itemView.getLayoutDirection() == 1) {
                if (view == itemView) {
                    width2 = layoutParams.getOpticalWidth(view);
                } else {
                    width2 = view.getWidth();
                }
                int i4 = width2 - i3;
                if (facet.mOffsetWithPadding) {
                    float f = facet.mOffsetPercent;
                    if (f == 0.0f) {
                        i4 -= view.getPaddingRight();
                    } else if (f == 100.0f) {
                        i4 += view.getPaddingLeft();
                    }
                }
                if (facet.mOffsetPercent != -1.0f) {
                    if (view == itemView) {
                        width3 = layoutParams.getOpticalWidth(view);
                    } else {
                        width3 = view.getWidth();
                    }
                    i4 -= (int) ((width3 * facet.mOffsetPercent) / 100.0f);
                }
                if (itemView == view) {
                    return i4;
                }
                Rect rect = sRect;
                rect.right = i4;
                ((ViewGroup) itemView).offsetDescendantRectToMyCoords(view, rect);
                return sRect.right + layoutParams.getOpticalRightInset();
            }
            if (facet.mOffsetWithPadding) {
                float f2 = facet.mOffsetPercent;
                if (f2 == 0.0f) {
                    i3 += view.getPaddingLeft();
                } else if (f2 == 100.0f) {
                    i3 -= view.getPaddingRight();
                }
            }
            if (facet.mOffsetPercent != -1.0f) {
                if (view == itemView) {
                    width = layoutParams.getOpticalWidth(view);
                } else {
                    width = view.getWidth();
                }
                i3 += (int) ((width * facet.mOffsetPercent) / 100.0f);
            }
            int i5 = i3;
            if (itemView == view) {
                return i5;
            }
            Rect rect2 = sRect;
            rect2.left = i5;
            ((ViewGroup) itemView).offsetDescendantRectToMyCoords(view, rect2);
            return sRect.left - layoutParams.getOpticalLeftInset();
        }
        if (facet.mOffsetWithPadding) {
            float f3 = facet.mOffsetPercent;
            if (f3 == 0.0f) {
                i3 += view.getPaddingTop();
            } else if (f3 == 100.0f) {
                i3 -= view.getPaddingBottom();
            }
        }
        if (facet.mOffsetPercent != -1.0f) {
            i3 += (int) (((view == itemView ? layoutParams.getOpticalHeight(view) : view.getHeight()) * facet.mOffsetPercent) / 100.0f);
        }
        if (itemView != view) {
            Rect rect3 = sRect;
            rect3.top = i3;
            ((ViewGroup) itemView).offsetDescendantRectToMyCoords(view, rect3);
            i = sRect.top - layoutParams.getOpticalTopInset();
        } else {
            i = i3;
        }
        return facet.isAlignedToTextViewBaseLine() ? i + view.getBaseline() : i;
    }
}
