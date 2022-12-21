package com.android.systemui.plugins.p011qs;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import com.android.systemui.plugins.annotations.ProvidesInterface;
import com.android.systemui.plugins.p011qs.QSTile;

@ProvidesInterface(version = 1)
/* renamed from: com.android.systemui.plugins.qs.QSIconView */
public abstract class QSIconView extends ViewGroup {
    public static final int VERSION = 1;

    public abstract void disableAnimation();

    public abstract View getIconView();

    public abstract void setCircleIconBgColor(int i);

    public abstract void setIcon(QSTile.State state, boolean z);

    public abstract void setShouldAddCircleIconBg(boolean z);

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public QSIconView(Context context) {
        super(context);
    }
}
