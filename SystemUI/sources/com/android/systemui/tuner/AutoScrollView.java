package com.android.systemui.tuner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.ScrollView;

public class AutoScrollView extends ScrollView {
    private static final float SCROLL_PERCENT = 0.1f;

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public AutoScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public boolean onDragEvent(DragEvent dragEvent) {
        if (dragEvent.getAction() == 2) {
            int y = (int) dragEvent.getY();
            int height = getHeight();
            int i = (int) (((float) height) * 0.1f);
            if (y < i) {
                scrollBy(0, y - i);
            } else if (y > height - i) {
                scrollBy(0, (y - height) + i);
            }
        }
        return false;
    }
}
