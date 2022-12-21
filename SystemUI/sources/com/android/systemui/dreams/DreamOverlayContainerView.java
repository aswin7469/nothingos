package com.android.systemui.dreams;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewOverlay;
import androidx.constraintlayout.widget.ConstraintLayout;

public class DreamOverlayContainerView extends ConstraintLayout {
    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public DreamOverlayContainerView(Context context) {
        this(context, (AttributeSet) null);
    }

    public DreamOverlayContainerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DreamOverlayContainerView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public DreamOverlayContainerView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }
}
