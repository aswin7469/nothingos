package androidx.leanback.widget;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import androidx.leanback.widget.GridLayoutManager;
import androidx.leanback.widget.ItemAlignmentFacet;

class ItemAlignmentFacetHelper {
    private static Rect sRect = new Rect();

    static int getAlignmentPosition(View view, ItemAlignmentFacet.ItemAlignmentDef itemAlignmentDef, int i) {
        View view2;
        int i2;
        int i3;
        int i4;
        int i5;
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        if (itemAlignmentDef.mViewId == 0 || (view2 = view.findViewById(itemAlignmentDef.mViewId)) == null) {
            view2 = view;
        }
        int i6 = itemAlignmentDef.mOffset;
        if (i != 0) {
            if (itemAlignmentDef.mOffsetWithPadding) {
                if (itemAlignmentDef.mOffsetPercent == 0.0f) {
                    i6 += view2.getPaddingTop();
                } else if (itemAlignmentDef.mOffsetPercent == 100.0f) {
                    i6 -= view2.getPaddingBottom();
                }
            }
            if (itemAlignmentDef.mOffsetPercent != -1.0f) {
                i6 += (int) ((((float) (view2 == view ? layoutParams.getOpticalHeight(view2) : view2.getHeight())) * itemAlignmentDef.mOffsetPercent) / 100.0f);
            }
            if (view != view2) {
                sRect.top = i6;
                ((ViewGroup) view).offsetDescendantRectToMyCoords(view2, sRect);
                i2 = sRect.top - layoutParams.getOpticalTopInset();
            } else {
                i2 = i6;
            }
            return itemAlignmentDef.isAlignedToTextViewBaseLine() ? i2 + view2.getBaseline() : i2;
        } else if (view.getLayoutDirection() == 1) {
            if (view2 == view) {
                i4 = layoutParams.getOpticalWidth(view2);
            } else {
                i4 = view2.getWidth();
            }
            int i7 = i4 - i6;
            if (itemAlignmentDef.mOffsetWithPadding) {
                if (itemAlignmentDef.mOffsetPercent == 0.0f) {
                    i7 -= view2.getPaddingRight();
                } else if (itemAlignmentDef.mOffsetPercent == 100.0f) {
                    i7 += view2.getPaddingLeft();
                }
            }
            if (itemAlignmentDef.mOffsetPercent != -1.0f) {
                if (view2 == view) {
                    i5 = layoutParams.getOpticalWidth(view2);
                } else {
                    i5 = view2.getWidth();
                }
                i7 -= (int) ((((float) i5) * itemAlignmentDef.mOffsetPercent) / 100.0f);
            }
            if (view == view2) {
                return i7;
            }
            sRect.right = i7;
            ((ViewGroup) view).offsetDescendantRectToMyCoords(view2, sRect);
            return sRect.right + layoutParams.getOpticalRightInset();
        } else {
            if (itemAlignmentDef.mOffsetWithPadding) {
                if (itemAlignmentDef.mOffsetPercent == 0.0f) {
                    i6 += view2.getPaddingLeft();
                } else if (itemAlignmentDef.mOffsetPercent == 100.0f) {
                    i6 -= view2.getPaddingRight();
                }
            }
            if (itemAlignmentDef.mOffsetPercent != -1.0f) {
                if (view2 == view) {
                    i3 = layoutParams.getOpticalWidth(view2);
                } else {
                    i3 = view2.getWidth();
                }
                i6 += (int) ((((float) i3) * itemAlignmentDef.mOffsetPercent) / 100.0f);
            }
            int i8 = i6;
            if (view == view2) {
                return i8;
            }
            sRect.left = i8;
            ((ViewGroup) view).offsetDescendantRectToMyCoords(view2, sRect);
            return sRect.left - layoutParams.getOpticalLeftInset();
        }
    }

    private ItemAlignmentFacetHelper() {
    }
}
